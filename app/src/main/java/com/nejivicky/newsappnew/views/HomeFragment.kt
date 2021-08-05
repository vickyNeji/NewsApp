package com.nejivicky.newsappnew.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nejivicky.newsappnew.R
import com.nejivicky.newsappnew.adapters.NewsAdapter
import com.nejivicky.newsappnew.models.Article
import com.nejivicky.newsappnew.utils.Constants.TOTAL_ITEMS
import com.nejivicky.newsappnew.utils.network_utils.Resource
import com.nejivicky.newsappnew.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),NewsAdapter.OnArticleClicked {

    private val homeViewModel by viewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycleView()
        homeViewModel.breakingNews.observe(viewLifecycleOwner, { resource ->

            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let { newsResponse ->
                        //Log.d("TAG", "onViewCreated:${it.status} ")
                        newsAdapter.differ.submitList(newsResponse.articles?.toList())
                         val totalPages= newsResponse.totalResults!! /TOTAL_ITEMS +2
                        isLastPage = homeViewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            rvHomeBreaking.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    resource.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

            }


        })

    }

    var isLoading=false
    var isLastPage=false
    var isScrolling=false


    private val scrollListenerRv= object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
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
                homeViewModel.getBreakingNews("in")
                isScrolling=false
            }



        }
    }





    private fun setupRecycleView(){
        newsAdapter= NewsAdapter(this)
        rvHomeBreaking.apply {
            adapter=newsAdapter
            addOnScrollListener(scrollListenerRv)
        }
    }

    private fun hideProgressBar(){
            paginationProgressBar.visibility= View.INVISIBLE
            isLoading=false
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility= View.VISIBLE
        isLoading=true
    }

    override fun onArticleCLicked(article: Article) {

        val action:HomeFragmentDirections.ActionHomeFragmentToDetailFragment =
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(article)
        findNavController().navigate(action)

    }




}