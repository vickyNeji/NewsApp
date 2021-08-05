package com.nejivicky.newsappnew.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nejivicky.newsappnew.models.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles")
     fun getArticles():LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticle(article: Article)


    @Delete
    suspend fun deleteArticle(article: Article)




}