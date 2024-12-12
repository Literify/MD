package com.literify.ui.fragment.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.literify.databinding.FragmentExploreBinding
import com.literify.ui.activity.book_detail.BookDetailActivity
import com.literify.ui.activity.book_detail.BookDetailActivity.Companion.EXTRA_BOOK_ID
import com.literify.ui.adapter.Rv1Adapter
import com.literify.ui.adapter.Rv2Adapter
import com.literify.util.DummyData
import com.literify.util.DummyData.genreResponseDummy

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExploreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Implement fetch genre data

        showDummyData()
    }

    private fun showDummyData() {
        val genreAdapter = Rv2Adapter(false) {
            // TODO: Implement genre click
        }
        genreAdapter.submitList(genreResponseDummy)


        val booksAdapter = Rv1Adapter(false) { id ->
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra(EXTRA_BOOK_ID, id)

            startActivity(intent)
        }
        booksAdapter.submitList(DummyData.rv1Dummy)

        binding.rvPersonalized.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPersonalized.adapter = genreAdapter

        binding.rvMostReading.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMostReading.adapter = booksAdapter

        binding.rvMostAuthors.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMostAuthors.adapter = booksAdapter
    }
}