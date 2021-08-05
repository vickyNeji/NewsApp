package com.nejivicky.newsappnew.repository

import com.nejivicky.newsappnew.models.Article
import com.nejivicky.newsappnew.room.ArticleDao
import com.nejivicky.newsappnew.utils.network_utils.Api
import javax.inject.Inject

class NewsRepository @Inject constructor(val api:Api, val articleDao:ArticleDao) {

    suspend fun getBreakingNews(countryCode:String,pageNo:Int)= api.getBreakingNews(countryCode, pageNo)

    suspend fun searchNews(searchQuery:String,pageNo: Int)= api.searchForNews(searchQuery, pageNo)

    suspend fun upsert(article: Article)= articleDao.upsertArticle(article)

    fun getSavedNews() = articleDao.getArticles()

    suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article)

}