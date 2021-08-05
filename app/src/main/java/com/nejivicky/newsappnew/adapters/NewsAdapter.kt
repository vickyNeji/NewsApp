package com.nejivicky.newsappnew.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nejivicky.newsappnew.R
import com.nejivicky.newsappnew.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter(private val onArticleClicked: OnArticleClicked) :RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    private val differCallback= object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false))

    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article= differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(article.urlToImage)
                .into(ivArticleImage)

            tvSource.text=article.source?.name
            tvTitle.text=article.title
            tvPublishedAt.text=article.publishedAt

            setOnClickListener {
                onArticleClicked.onArticleCLicked(article)
            }

        }
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }


    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface OnArticleClicked{
        fun onArticleCLicked(article: Article)
    }

}