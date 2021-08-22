package com.nsantos.news_topheadlines.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nsantos.news_topheadlines.R
import com.nsantos.news_topheadlines.databinding.ListArticleItemBinding
import com.nsantos.news_topheadlines.databinding.ListFirstItemBinding
import com.nsantos.news_topheadlines.models.NewsArticleModel
import com.nsantos.news_topheadlines.utils.loadImage
import com.nsantos.news_topheadlines.utils.loadImageWithDimensions

class NewsArticlesListAdapter(private val clickListener: OnClickListener): ListAdapter<NewsArticleModel, RecyclerView.ViewHolder>(
    DiffCallback
) {

    val FIRST_ITEM = 1
    val ITEM = 2

    companion object DiffCallback : DiffUtil.ItemCallback<NewsArticleModel>() {

        override fun areItemsTheSame(oldItem: NewsArticleModel, newItem: NewsArticleModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: NewsArticleModel, newItem: NewsArticleModel): Boolean {
            return oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 1){
            FirstListItemViewHolder(ListFirstItemBinding.inflate(LayoutInflater.from(parent.context)))
        } else {
            ArticleItemViewHolder(ListArticleItemBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val headlineItem = getItem(position)
        if(holder is ArticleItemViewHolder) {
            holder.bind(headlineItem, position, currentList)
            holder.itemView.setOnClickListener {
                clickListener.onClick(headlineItem)
            }
        }
        if(holder is FirstListItemViewHolder){
            holder.bind(headlineItem, position)
            holder.itemView.setOnClickListener {
                clickListener.onClick(headlineItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0)
            FIRST_ITEM
        else
            ITEM
    }

    class ArticleItemViewHolder(private val headlineBinding: ListArticleItemBinding) : RecyclerView.ViewHolder(headlineBinding.root){
        fun bind(
            newsArticleModel: NewsArticleModel,
            position: Int,
            currentList: MutableList<NewsArticleModel>
        ){
            headlineBinding.newsArticleModel = newsArticleModel
            val resources = headlineBinding.tvItemPosition.context.resources
            headlineBinding.tvItemPosition.text = String.format(resources.getString(R.string.headline_item_position), position.plus(1))
            headlineBinding.ivArticle.loadImageWithDimensions(newsArticleModel.urlToImage, 225, 225)
            if(currentList.size - 1 == position && headlineBinding.separator != null)
                headlineBinding.separator.visibility = View.INVISIBLE

            headlineBinding.executePendingBindings()
        }
    }

    class FirstListItemViewHolder(private val firstItemBinding: ListFirstItemBinding) : RecyclerView.ViewHolder(firstItemBinding.root){
        fun bind(newsArticleModel: NewsArticleModel, position: Int){
            firstItemBinding.newsArticleModel = newsArticleModel
            val tvItemPosition = firstItemBinding.tvItemPosition
            tvItemPosition.text = String.format(tvItemPosition.context.resources.getString(R.string.headline_item_position), position.plus(1))
            firstItemBinding.ivArticle.loadImage(newsArticleModel.urlToImage)
            firstItemBinding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (newsArticleModel: NewsArticleModel) -> Unit) {
        fun onClick(newsArticleModel: NewsArticleModel) = clickListener(newsArticleModel)
    }
}

