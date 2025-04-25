package com.muhasib.documate.Fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.muhasib.documate.activity.MainActivity
import com.muhasib.documate.R
import com.muhasib.documate.data.AppDatabase
import com.muhasib.documate.data.ItemDao
import com.muhasib.documate.data.UserDao
import com.muhasib.documate.utils.MySharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PdfFragment : Fragment() {

    private lateinit var uploadedImage: ImageView
    private lateinit var uploadImageFab: ExtendedFloatingActionButton
    private lateinit var viewPdf: CardView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userDao: UserDao
    private lateinit var itemDao: ItemDao
    private var imageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uploadedImage.setImageURI(it)
            }
        }


    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri != null) {
                uploadedImage.setImageURI(imageUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pdf, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(view.rootView) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }
        initViews(view)
        googleSignInClient =
            GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)
        userDao = AppDatabase.getDatabase(requireContext()).userDao()
        itemDao = AppDatabase.getDatabase(requireContext()).itemDao()


        val logoutButton = view.findViewById<View>(R.id.logoutButton)
        logoutButton.setOnClickListener { logout() }
        uploadImageFab.setOnClickListener { showImagePickerDialog() }
        viewPdf.setOnClickListener { findNavController().navigate(R.id.action_pdfFragment_to_pdfViewerFragment) }



        return view
    }

    private fun initViews(view: View) {
        uploadedImage = view.findViewById(R.id.uploadedImage)
        uploadImageFab = view.findViewById(R.id.uploadImageFab)
        viewPdf = view.findViewById(R.id.pdfCard)
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Upload Image via Gallery", "Take Picture")
        AlertDialog.Builder(requireContext())
            .setTitle("Choose Option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkGalleryPermission()
                    1 -> checkCameraPermission()
                }
            }
            .show()
    }

    private fun checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1001)
        } else {
            pickImageFromGallery()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1002)
        } else {
            takePicture()
        }
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun takePicture() {
        val imageFile = File(requireContext().cacheDir, "temp_image.jpg")
        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
        takePictureLauncher.launch(imageUri)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                1001 -> pickImageFromGallery()
                1002 -> takePicture()
            }
        }
    }

    private fun logout() {

        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            clearUserData()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun clearUserData() {

        MySharedPreferences(requireContext()).clearAll()
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteAll()
            itemDao.deleteAll()
        }
    }
}
