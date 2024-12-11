package com.literify.ui.fragment.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.literify.R

class OnboardingAdapter(
    private val context: Context,
    private val lottieFiles: List<Int>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.lottieAnimationView.setAnimation(lottieFiles[position])
        holder.lottieAnimationView.playAnimation()    }

    override fun getItemCount(): Int = lottieFiles.size

    class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lottieAnimationView: LottieAnimationView = itemView.findViewById(R.id.lottieAnimationView)
    }
}