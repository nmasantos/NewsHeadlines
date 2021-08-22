package com.nsantos.news_topheadlines.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.nsantos.news_topheadlines.BuildConfig
import com.nsantos.news_topheadlines.databinding.FragmentNewsArticleDetailBinding
import com.nsantos.news_topheadlines.models.NewsArticleModel
import com.nsantos.news_topheadlines.utils.loadImage
import com.squareup.picasso.Picasso

class NewsArticleDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsArticleDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsArticleDetailBinding.inflate(inflater)

        val headlineItem = NewsArticleDetailFragmentArgs.fromBundle(
            requireArguments()
        ).newsArticleModel
        binding.newsArticleModel = headlineItem
        binding.ivArticle.loadImage(headlineItem.urlToImage)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = BuildConfig.sourceName
        return binding.root
    }
}