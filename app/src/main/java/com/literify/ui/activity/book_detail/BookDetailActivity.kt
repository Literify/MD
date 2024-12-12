package com.literify.ui.activity.book_detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.literify.R
import com.literify.databinding.ActivityBookDetailBinding
import com.literify.ui.adapter.Rv1Adapter
import com.literify.util.DummyData.bookResponseDummy
import com.literify.util.DummyData.rv1Dummy

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookId = intent.getStringExtra(EXTRA_BOOK_ID)
        if (bookId != null) {
            // TODO : Implement fetch book detail by id
        } else {
            // TODO : Show error message
        }

        showDummyData()
    }

    private fun showDummyData() {
        binding.apply {
            appBar.title = getString(R.string.book_detail)
            bookTitle.text = bookResponseDummy.title
            bookAuthor.text = bookResponseDummy.authors
            bookPublishedDate.text = bookResponseDummy.publisher
            ratingBar.rating = bookResponseDummy.averageRating?.toFloat() ?: 0f

            Glide.with(this@BookDetailActivity)
                .load(bookResponseDummy.image)
                .into(binding.bookCover)

            genre1.text = bookResponseDummy.genre
            genre2.visibility = View.GONE
            genre3.visibility = View.GONE
            genre4.visibility = View.GONE

            bookDescription.text = bookResponseDummy.description

            val adapter = Rv1Adapter(false) { id ->
                val intent = Intent(this@BookDetailActivity, BookDetailActivity::class.java)
                intent.putExtra(EXTRA_BOOK_ID, id)

                startActivity(intent)
            }
            adapter.submitList(rv1Dummy)

            rvPersonalized.layoutManager = LinearLayoutManager(this@BookDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            rvPersonalized.adapter = adapter
        }
    }

    companion object {
        const val TAG = "BookDetailActivity"
        const val EXTRA_BOOK_ID = "book_id"
    }
}
