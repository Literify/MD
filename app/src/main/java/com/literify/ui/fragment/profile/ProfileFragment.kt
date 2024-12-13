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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.literify.R
import com.literify.data.repository.AuthRepository
import com.literify.databinding.FragmentProfileBinding
import com.literify.ui.activity.auth.AuthActivity
import com.literify.ui.activity.edit_profile.EditProfileActivity
import com.literify.ui.activity.edit_profile.EditProfileActivity.Companion.EXTRA_USER_ID
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
        binding.apply {
            username.text = firebaseAuth.currentUser?.displayName
            Glide.with(this@ProfileFragment)
                .load(firebaseAuth.currentUser?.photoUrl)
                .into(profileImage)

            editButton.setOnClickListener {
                val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                intent.putExtra(EXTRA_USER_ID, firebaseAuth.currentUser?.uid)
                startActivity(intent)
            }

            buttonSignOut.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.sign_out))
                    .setMessage(getString(R.string.body_signout))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
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
                    .setNegativeButton(getString(R.string.no), null)
                    .show()
            }
        }
    }
}