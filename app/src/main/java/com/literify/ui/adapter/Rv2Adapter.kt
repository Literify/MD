package com.literify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.literify.data.remote.model.BookGenreResponse
import com.literify.databinding.ItemRv2Binding
import com.literify.databinding.ItemRv2SkeletonBinding

class Rv2Adapter(
    private val isLoading: Boolean = false,
    private val onItemClick: (String) -> Unit
) : ListAdapter<BookGenreResponse, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SKELETON else VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SKELETON) {
            val binding =
                ItemRv2SkeletonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SkeletonViewHolder(binding)
        } else {
            val binding =
                ItemRv2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            EventListViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventListViewHolder && !isLoading) {
            val genre = getItem(position)
            holder.apply {
                bind(genre)
                itemView.setOnClickListener {
                    genre.genre?.let { onItemClick(it) }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class EventListViewHolder(private val binding: ItemRv2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: BookGenreResponse) {
            with(binding) {
                Glide.with(image.context)
                    .load(genre.imageUrl)
                    .into(image)

                label.text = genre.genre
            }
        }
    }

    inner class SkeletonViewHolder(binding: ItemRv2SkeletonBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_SKELETON = 2

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookGenreResponse>() {
            override fun areItemsTheSame(oldItem: BookGenreResponse, newItem: BookGenreResponse): Boolean {
                return oldItem.genre == newItem.genre
            }

            override fun areContentsTheSame(oldItem: BookGenreResponse, newItem: BookGenreResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}