package com.literify.ui.fragment.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.literify.databinding.FragmentOnboardingBinding
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.literify.R

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private var isAutoScrolling = true
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var onboardingTexts: List<String>
    private val lottieAnimations = listOf(
        R.raw.onboarding1,
        R.raw.onboarding2,
        R.raw.onboarding3
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onboardingTexts = listOf(
            getString(R.string.body_onboarding_2),
            getString(R.string.body_onboarding_3),
            getString(R.string.body_onboarding_4)
        )


        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val dynamicText = binding.body
        val buttonLogin = binding.buttonLogin
        val buttonRegister = binding.buttonRegister

        viewPager.adapter = OnboardingAdapter(requireContext(), lottieAnimations)
        viewPager.offscreenPageLimit = 1

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        dynamicText.text = onboardingTexts[0]

        buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signinFragment)
        }

        buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signupFragment)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                dynamicText.text = onboardingTexts[position]
            }
        })

        startAutoScroll(viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        isAutoScrolling = false
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }

    private fun startAutoScroll(viewPager: ViewPager2) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isAutoScrolling) return

                val nextItem = (viewPager.currentItem + 1) % lottieAnimations.size
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, AUTO_SCROLL_INTERVAL)
            }
        }, AUTO_SCROLL_INTERVAL)
    }

    companion object {
        private const val AUTO_SCROLL_INTERVAL = 6000L
    }
}
