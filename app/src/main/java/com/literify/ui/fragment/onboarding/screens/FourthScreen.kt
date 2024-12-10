package com.literify.ui.fragment.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.literify.R
import com.literify.databinding.FragmentFourthScreenBinding

class FourthScreen : Fragment() {

    private var _binding: FragmentFourthScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFourthScreenBinding.inflate(inflater, container, false)

        binding.apply {
            buttonSignup.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingFragment_to_signupFragment)
            }
            buttonSignin.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            }
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Pastikan binding null ketika view dihancurkan untuk menghindari memory leak
    }
}
