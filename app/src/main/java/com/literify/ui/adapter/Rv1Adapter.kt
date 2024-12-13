package com.literify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.literify.data.remote.model.BookResponse
import com.literify.databinding.ItemRv1Binding
import com.literify.databinding.ItemRv1SkeletonBinding

class Rv1Adapter(
    private val isLoading: Boolean = false,
    private val onItemClick: (String) -> Unit
) : ListAdapter<BookResponse, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SKELETON else VIEW_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SKELETON) {
            val binding =
                ItemRv1SkeletonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SkeletonViewHolder(binding)
        } else {
            val binding =
                ItemRv1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            EventListViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventListViewHolder && !isLoading) {
            val book = getItem(position)
            holder.apply {
                bind(book)
                itemView.setOnClickListener {
                    onItemClick(book.id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class EventListViewHolder(private val binding: ItemRv1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookResponse) {
            with(binding) {
                bookTitle.text = book.title
                bookAuthor.text = book.authors
                Glide.with(bookCover.context)
                    .load(book.image)
                    .into(bookCover)
            }
        }
    }

    inner class SkeletonViewHolder(binding: ItemRv1SkeletonBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_SKELETON = 2

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookResponse>() {
            override fun areItemsTheSame(oldItem: BookResponse, newItem: BookResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BookResponse, newItem: BookResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}