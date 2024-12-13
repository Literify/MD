package com.literify.ui.activity.edit_profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.literify.R
import com.literify.databinding.ActivityEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    // TODO: Implement update profile
    private fun setupUI() {
        Glide.with(this)
            .load(firebaseAuth.currentUser?.photoUrl)
            .placeholder(R.drawable.ic_profile)
            .into(binding.profileImage)

        binding.inputFirstName.editText?.setText(firebaseAuth.currentUser?.displayName)
        binding.inputEmail.editText?.setText(firebaseAuth.currentUser?.email)
        binding.inputPassword.editText?.setText("********")
    }
}
