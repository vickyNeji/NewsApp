package com.nejivicky.newsappnew.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nejivicky.newsappnew.models.Article

@Database(entities = [Article::class],version = 1)
@TypeConverters(Convertors::class)
abstract class ArticleDatabase:RoomDatabase() {

    abstract fun getArticleDao():ArticleDao

}