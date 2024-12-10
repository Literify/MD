package com.literify.ui.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.literify.databinding.FragmentOnboardingBinding
import com.literify.ui.fragment.onboarding.screens.FirstScreen
import com.literify.ui.fragment.onboarding.screens.FourthScreen
import com.literify.ui.fragment.onboarding.screens.SecondScreen
import com.literify.ui.fragment.onboarding.screens.ThirdScreen
import androidx.viewpager2.widget.ViewPager2

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menggunakan View Binding
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen(),
            FourthScreen()
        )

        // Set adapter ke ViewPager
        val adapter = OnboardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = adapter

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
