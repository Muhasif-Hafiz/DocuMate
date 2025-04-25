package com.muhasib.documate.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.github.barteksc.pdfviewer.PDFView
import com.muhasib.documate.R
import com.muhasib.documate.utils.CommonUrls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream


class PDFViewerFragment : Fragment() {

    private lateinit var pdfView: PDFView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=  inflater.inflate(R.layout.fragment_p_d_f_viewer, container, false)
        val backButton = view.findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener { parentFragmentManager.popBackStack()}
        pdfView = view.findViewById(R.id.pdfView)
        downloadAndDisplayPdf(CommonUrls.PDF_URL)

        return view
    }
    private fun downloadAndDisplayPdf(Url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(Url).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val file = File(requireContext().cacheDir, "temp.pdf")
                    val inputStream = response.body?.byteStream()
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    outputStream.close()

                    withContext(Dispatchers.Main) {
                        pdfView.fromFile(file)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .defaultPage(0)
                            .spacing(10)
                            .load()
                    }
                } else {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}