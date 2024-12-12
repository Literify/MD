package com.literify.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.literify.R
import com.literify.data.repository.AuthRepository
import com.literify.databinding.FragmentProfileBinding
import com.literify.ui.activity.auth.AuthActivity
import com.literify.ui.activity.edit_profile.EditProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Implement better handler
        showUI()
    }

    private fun showUI() {
        binding.username.text = firebaseAuth.currentUser?.displayName
        Glide.with(this)
            .load(firebaseAuth.currentUser?.photoUrl)
            .into(binding.profileImage)

        binding.editButton.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSignOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Sign Out")
                .setMessage(getString(R.string.title_logout))
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        authRepository.signout()
                    }

                    if (firebaseAuth.currentUser == null) {
                        val intent = Intent(requireActivity(), AuthActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        requireActivity()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}