package com.literify.ui.activity.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.FirebaseAuth
import com.literify.R
import com.literify.data.repository.AuthPreferences
import com.literify.data.repository.AuthRepository
import com.literify.databinding.ActivityMainBinding
import com.literify.ui.activity.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var logoutDialog: AlertDialog? = null

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var credentialManager: CredentialManager

    @Inject
    lateinit var authPreferences: AuthPreferences

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        val currentUser = firebaseAuth.currentUser
        val deeplinkUri: Uri? = intent?.data

        if (currentUser == null) {
            val intent = Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                data = deeplinkUri
            }
            startActivity(intent)

            finish()
            return
        }

        if (deeplinkUri != null) {
            handleImplicitDeeplink(deeplinkUri)
            return
        }

        handleCredentialPrompt()
    }

    override fun onDestroy() {
        super.onDestroy()

        logoutDialog?.dismiss()
    }

    // TODO: Implement the AlertDialog for logout according to ui/ux plan
    private fun handleImplicitDeeplink(deeplinkUri: Uri) {
        val mode = deeplinkUri.getQueryParameter("mode") ?: ""
        val oobCode = deeplinkUri.getQueryParameter("oobCode") ?: ""

        when (mode) {
            "resetPassword" -> {
                logoutDialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.title_logout))
                    .setMessage(getString(R.string.body_logout_msg))
                    .setPositiveButton(getString(R.string.sign_out)) { _, _ ->
                        val intent = Intent(this, AuthActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            data = deeplinkUri
                        }

                        firebaseAuth.signOut()
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .create()
                logoutDialog?.show()
            }
            "verifyEmail" -> {
                lifecycleScope.launch {
                    try {
                        val oobAction = authRepository.verifyOobCode(oobCode)
                        if (oobAction?.operation == ActionCodeResult.VERIFY_EMAIL) {
                            authRepository.confirmEmailVerification(oobCode)
                            Snackbar.make(binding.root, "Email berhasil diverifikasi", Snackbar.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error: ${e.message}")
                        Snackbar.make(binding.root, "Gagal verifikasi email", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            else -> {
                Snackbar.make(binding.root, "Invalid deeplink", Snackbar.LENGTH_SHORT).show()
            }
        }
        intent.data = null
    }

    private fun handleCredentialPrompt() {
        val saveCredentialRequest =
            intent.getBooleanExtra(EXTRA_SAVE_CREDENTIAL, false)

        lifecycleScope.launch {
            val credential = authPreferences.getCredential().first()
            val isCredentialSaved = credential?.isCredentialSavePrompted

            if (saveCredentialRequest && credential != null && !isCredentialSaved!!) {
                saveCredentialToCredentialManager(credential.id!!, credential.password!!)
                authPreferences.saveCredential(credential.id, credential.password, true)
            }
        }
    }

    private suspend fun saveCredentialToCredentialManager(identifier: String, password: String) {
        val createPasswordRequest = CreatePasswordRequest(identifier, password)
        try {
            credentialManager.createCredential(
                context = this@MainActivity,
                request = createPasswordRequest
            )

        } catch (e: Exception) {
            Log.e(TAG, "Failed to save credential", e)
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val EXTRA_SAVE_CREDENTIAL = "extra_save_credential"
    }
}