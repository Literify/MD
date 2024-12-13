package com.literify.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.literify.databinding.FragmentHomeBinding
import com.literify.ui.activity.book_detail.BookDetailActivity
import com.literify.ui.adapter.Rv1Adapter
import com.literify.ui.adapter.Rv2Adapter
import com.literify.util.DummyData.genreResponseDummy
import com.literify.util.DummyData.rv1Dummy

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Implement real data

        showDummyData()
    }

    private fun showDummyData() {
        binding.tvWelcome.text = "Selamat datang kembali!"
        binding.tvGenre1.text = genreResponseDummy[2].genre
        binding.tvGenre2.text = genreResponseDummy[3].genre
        binding.tvGenre3.text = genreResponseDummy[4].genre
        binding.tvGenre4.text = genreResponseDummy[5].genre

        Glide.with(this)
            .load("https://media.discordapp.net/attachments/1293113678329282572/1316782029102120974/1.png?ex=675c4c54&is=675afad4&hm=20507dbfe05517b663200743bc3ea446a8d0595b8dd8797a31d7d1d9476f7863&=&format=webp&quality=lossless&width=437&height=437")
            .into(binding.imgBadge)

        val genreAdapter = Rv2Adapter(false) {
            // TODO: Implement genre click
        }
        genreAdapter.submitList(genreResponseDummy)

        binding.rvPersonalized.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPersonalized.adapter = genreAdapter

        val booksAdapter = Rv1Adapter(false) { id ->
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.EXTRA_BOOK_ID, id)

            startActivity(intent)
        }
        booksAdapter.submitList(rv1Dummy)

        binding.rvMostReading.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMostReading.adapter = booksAdapter
    }
}