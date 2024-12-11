package com.literify.ui.activity.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.FirebaseAuth
import com.literify.R
import com.literify.data.repository.AuthRepository
import com.literify.databinding.ActivityAuthBinding
import com.literify.ui.activity.main.MainActivity
import com.literify.ui.activity.main.MainActivity.Companion.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = firebaseAuth.currentUser
        val deeplinkUri: Uri? = intent?.data

        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)

            finish()
            return
        }

        if (deeplinkUri != null) {
            handleImplicitDeeplink(deeplinkUri)
            return
        }
    }

    // TODO: Extract string resources & show state according to ui/ux plan
    private fun handleImplicitDeeplink(deeplinkUri: Uri) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_auth) as NavHostFragment
        val navController = navHostFragment.navController

        val mode = deeplinkUri.getQueryParameter("mode") ?: ""
        val oobCode = deeplinkUri.getQueryParameter("oobCode") ?: ""

        lifecycleScope.launch {
            try {
                val oobActionCodeResult = authRepository.verifyOobCode(oobCode)

                when {
                    mode == "resetPassword" && (oobActionCodeResult?.operation == ActionCodeResult.PASSWORD_RESET) -> {
                        val bundle = Bundle().apply {
                            putString("oobCode", oobCode)
                        }
                        navController.navigate(R.id.forgotPasswordFragment, bundle)
                    }
                    mode == "verifyEmail" && (oobActionCodeResult?.operation == ActionCodeResult.VERIFY_EMAIL) -> {
                        try {
                            authRepository.confirmEmailVerification(oobCode)
                            Snackbar.make(binding.root, "Email berhasil diverifikasi", Snackbar.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to confirm email verification: ${e.message}")
                            Snackbar.make(binding.root, "Gagal verifikasi email", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        Snackbar.make(binding.root, "Invalid or unsupported deeplink", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to process OOB code: ${e.message}")
                Snackbar.make(binding.root, "Gagal memproses tautan", Snackbar.LENGTH_SHORT).show()
            }
        }
        intent.data = null
    }
}