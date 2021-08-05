package com.nejivicky.newsappnew.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nejivicky.newsappnew.R
import com.nejivicky.newsappnew.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {


    val args:DetailFragmentArgs by navArgs()
    val newsViewModel by viewModels<NewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article= args.article
        webView.apply {
            webViewClient= WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        fab.setOnClickListener {
            newsViewModel.saveArticle(args.article)
            Snackbar.make(view,"Article Saved Successfully",Snackbar.LENGTH_SHORT)
                .show()
        }

    }

}