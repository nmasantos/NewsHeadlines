package com.nsantos.news_topheadlines.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nsantos.news_topheadlines.BuildConfig
import com.nsantos.news_topheadlines.R
import com.nsantos.news_topheadlines.database.asModel
import com.nsantos.news_topheadlines.database.getDatabase
import com.nsantos.news_topheadlines.databinding.FragmentHomeScreenBinding
import com.nsantos.news_topheadlines.network.NewsApi
import com.nsantos.news_topheadlines.repository.NewsRepository
import com.nsantos.news_topheadlines.ui.fragments.adapters.NewsArticlesListAdapter
import com.nsantos.news_topheadlines.ui.fragments.viewmodel.NewsArticlesViewModel
import com.nsantos.news_topheadlines.ui.fragments.viewmodel.NewsArticlesViewModelFactory
import com.nsantos.news_topheadlines.utils.biometricutils.BiometricListener
import com.nsantos.news_topheadlines.utils.biometricutils.canAuthenticateWithBiometrics
import com.nsantos.news_topheadlines.utils.repositoryutils.Resource
import com.nsantos.news_topheadlines.utils.biometricutils.promptBiometricAuthentication
import com.nsantos.news_topheadlines.utils.biometricutils.setBiometricPromptInfo


class NewsArticlesFragment : Fragment(), BiometricListener {

    private lateinit var fragmentHomeScreenBinding: FragmentHomeScreenBinding

    private val viewModel: NewsArticlesViewModel by viewModels {
        NewsArticlesViewModelFactory(this,
            NewsRepository(getDatabase(requireActivity().application), NewsApi.retrofitService))
    }

    private var authenticationResult: Boolean = false

    private val listAdapter by lazy {
            NewsArticlesListAdapter(NewsArticlesListAdapter.OnClickListener { headlineItem ->
                viewModel.navigateToArticleDetail(headlineItem)
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeScreenBinding = FragmentHomeScreenBinding.inflate(inflater,container,false)
        authenticationResult = viewModel.getBiometricAuthenticationResult()

        (requireActivity() as AppCompatActivity).supportActionBar?.title = BuildConfig.sourceName

        return fragmentHomeScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!authenticationResult && canAuthenticateWithBiometrics(requireContext()) == BiometricManager.BIOMETRIC_SUCCESS){
            startBiometricAuthenticationPrompt()
        } else {
            setupRecyclerView()
            setupObservables()
        }
    }

    private fun startBiometricAuthenticationPrompt() {
        val promptInfo = setBiometricPromptInfo(
            resources.getString(R.string.biometric_authentication),
            resources.getString(R.string.biometric_authentication_subtitle)
        )
        val biometricPrompt = promptBiometricAuthentication(this, this)
        biometricPrompt.authenticate(promptInfo)
    }

    private fun setupObservables() {
        viewModel.newsArticlesList.observe(viewLifecycleOwner, { result ->
            listAdapter.submitList(result.data?.asModel())

            fragmentHomeScreenBinding.progressBar.isVisible =
                result is Resource.Loading && result.data.isNullOrEmpty()
            if(result is Resource.Error){
                //Improve error handling
                Toast.makeText(
                    requireActivity().applicationContext, result.throwable?.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        viewModel.newsArticleModel.observe(viewLifecycleOwner, { headlineItem ->
            if (headlineItem != null) {
                findNavController().navigate(
                    NewsArticlesFragmentDirections.actionHomeScreenToHeadlineDetail(
                        headlineItem
                    )
                )
                viewModel.onNavigateToArticleDetailFinished()
            }
        })
    }

    private fun setupRecyclerView() {
        val recyclerView = fragmentHomeScreenBinding.rvTopHeadlines
        recyclerView.apply {
            if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val gridLayoutManager = GridLayoutManager(context, 2)
                gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (listAdapter.getItemViewType(position)) {
                            listAdapter.FIRST_ITEM -> 2
                            listAdapter.ITEM -> 1
                            else -> -1
                        }
                    }
                }
                layoutManager = gridLayoutManager
            } else
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = listAdapter
            (adapter as NewsArticlesListAdapter).stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    override fun onBiometricAuthenticationSuccess(authenticationResult: BiometricPrompt.AuthenticationResult) {
        this.authenticationResult = true
        viewModel.saveBiometricAuthenticationResult(this.authenticationResult)
        setupRecyclerView()
        setupObservables()
    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
       requireActivity().finish()
    }
}