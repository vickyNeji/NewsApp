package com.nejivicky.newsappnew.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nejivicky.newsappnew.R
import com.nejivicky.newsappnew.adapters.NewsAdapter
import com.nejivicky.newsappnew.models.Article
import com.nejivicky.newsappnew.utils.Constants
import com.nejivicky.newsappnew.utils.network_utils.Resource
import com.nejivicky.newsappnew.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),NewsAdapter.OnArticleClicked {


    private val searchViewModel by viewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecycleView()

        var job:Job?=null

        etSearch.addTextChangedListener {editable->

            job?.cancel()

            job= CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        searchViewModel.searchNewsPage=1
                        searchViewModel.searchNewsResponse=null
                        searchViewModel.searchNews(editable.toString())

                    }
                }
            }

        }


        searchViewModel.searchNews.observe(viewLifecycleOwner, Observer { resource->
            when(resource){

                is Resource.Loading->{
                    showProgressBar()
                }

                is Resource.Success->{
                    hideProgressBar()
                    resource.data?.let {searchResponse->
                        newsAdapter.differ.submitList(searchResponse.articles?.toList())
                        val totalPages= searchResponse.totalResults!! / Constants.TOTAL_ITEMS +2
                        isLastPage = searchViewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error->{
                    hideProgressBar()
                }


            }
        })


    }


    var isLoading=false
    var isLastPage=false
    var isScrolling=false


    private val scrollListenerRv= object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager= recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition= layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount= layoutManager.childCount
            val totalItemCount= layoutManager.itemCount


            val isNotLoadingAndNotLastPage= !isLoading && !isLastPage
            val isAtLastItem= firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning= firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible= totalItemCount >= 20

            val shouldPaginate= isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                searchViewModel.searchNews(etSearch.text.toString())
                isScrolling=false
            }



        }
    }







    private fun setupRecycleView(){
        newsAdapter= NewsAdapter(this)
        rvSearchNews.apply {
            adapter=newsAdapter
            addOnScrollListener(scrollListenerRv)

        }
    }

    private fun hideProgressBar(){
        paginationProgressBarSearch.visibility= View.INVISIBLE
        isLoading=false
    }

    private fun showProgressBar(){
        paginationProgressBarSearch.visibility= View.VISIBLE
        isLoading=true
    }

    override fun onArticleCLicked(article: Article) {

        val action: SearchFragmentDirections.ActionSearchFragmentToDetailFragment =
             SearchFragmentDirections.actionSearchFragmentToDetailFragment(article)
        findNavController().navigate(action)
    }


}