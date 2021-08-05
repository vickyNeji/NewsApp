package com.nejivicky.newsappnew.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nejivicky.newsappnew.models.Article
import com.nejivicky.newsappnew.models.NewsResponse
import com.nejivicky.newsappnew.repository.NewsRepository
import com.nejivicky.newsappnew.utils.network_utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel  @Inject constructor(private val repository: NewsRepository)  :ViewModel(){

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsResponse:NewsResponse?=null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse:NewsResponse?=null

    init {
        getBreakingNews("in")
    }

     fun getBreakingNews(countryCode:String){
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response= repository.getBreakingNews(countryCode,breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))
        }
    }


    fun searchNews(searchQuery:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response= repository.searchNews(searchQuery,searchNewsPage)
         searchNews.postValue(handleSearchNewsResponse(response))
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{

        if(response.isSuccessful){
            response.body()?.let {newsResponse ->
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=newsResponse
                }else{
                    val oldArticles= breakingNewsResponse?.articles
                    val newArticles= newsResponse.articles
                    newArticles?.let { oldArticles?.addAll(it) }
                }
                return Resource.Success(breakingNewsResponse?:newsResponse)
            }
        }

        return Resource.Error("Something went wrong")


    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{

        if(response.isSuccessful){
            response.body()?.let {newsResponse ->
                searchNewsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse=newsResponse
                }else{
                    val oldArticles= searchNewsResponse?.articles
                    val newArticles= newsResponse.articles
                    newArticles?.let { oldArticles?.addAll(it) }
                }
                return Resource.Success(searchNewsResponse?:newsResponse)
            }
        }

        return Resource.Error("Something went wrong")

    }


    fun saveArticle(article: Article) =viewModelScope.launch {
        repository.upsert(article)
    }


    fun getSavedNews() = repository.getSavedNews()

    fun deleteArticle(article: Article) =viewModelScope.launch {
        repository.deleteArticle(article)
    }


}