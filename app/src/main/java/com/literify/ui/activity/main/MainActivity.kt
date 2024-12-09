package com.literify.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.literify.R
import com.literify.data.repository.AuthPreferences
import com.literify.databinding.ActivityMainBinding
import com.literify.ui.activity.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var credentialManager: CredentialManager

    @Inject
    lateinit var authPreferences: AuthPreferences

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
        if (currentUser == null) {
            val intent = Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        saveCredentialPrompt()
    }

    private fun saveCredentialPrompt() {
        lifecycleScope.launch {
            val saveCredentialRequest =
                intent.getBooleanExtra(EXTRA_SAVE_CREDENTIAL, false)

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