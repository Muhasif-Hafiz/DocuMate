package com.muhasib.documate.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.muhasib.documate.activity.HomeActivity
import com.muhasib.documate.R
import com.muhasib.documate.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment() {

    private var isActive = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkUserStatus()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun checkUserStatus() {
        lifecycleScope.launch {
            try {
                val user = withContext(Dispatchers.IO) {
                    AppDatabase.getDatabase(requireContext().applicationContext)
                        .userDao()
                        .getUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                }

                delay(2000)

                if (!isActive) return@launch

                if (user != null) {
                    Log.d("SplashFragment", "User found: ${user.email}")
                    launchHomeActivity()
                } else {
                    navigateToLogin()
                }
            } catch (e: Exception) {
                Log.e("SplashFragment", "Error checking user", e)
                if (isActive) navigateToLogin()
            }
        }
    }

    private fun launchHomeActivity() {
        startActivity(Intent(requireActivity(), HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        requireActivity().finish()
    }

    private fun navigateToLogin() {
        if (findNavController().currentDestination?.id == R.id.splashFragment) {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        isActive = false
        super.onDestroyView()
    }
}