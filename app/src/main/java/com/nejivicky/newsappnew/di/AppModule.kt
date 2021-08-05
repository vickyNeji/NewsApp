package com.nejivicky.newsappnew.di

import android.content.Context
import androidx.room.Room
import com.nejivicky.newsappnew.repository.NewsRepository
import com.nejivicky.newsappnew.room.ArticleDao
import com.nejivicky.newsappnew.room.ArticleDatabase
import com.nejivicky.newsappnew.utils.Constants.BASE_URL
import com.nejivicky.newsappnew.utils.Constants.DATABASE_NAME
import com.nejivicky.newsappnew.utils.network_utils.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{

        val client=OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()


    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit):Api{
        return retrofit.create(Api::class.java)
    }


    @Provides
    @Singleton
    fun providesArticleDatabase(
        @ApplicationContext app:Context
    ):ArticleDatabase{
        return Room.databaseBuilder(
            app,ArticleDatabase::class.java, DATABASE_NAME
        ).build()
    }


    @Provides
    @Singleton
    fun provideArticleDao(articleDatabase: ArticleDatabase):ArticleDao{
        return articleDatabase.getArticleDao()
    }


    @Provides
    @Singleton
    fun providesNewsRepository(api: Api,articleDao: ArticleDao):NewsRepository{
        return NewsRepository(api,articleDao)
    }

}