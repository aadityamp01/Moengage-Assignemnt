package com.aaditya.moengageassignemnt.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aaditya.moengageassignemnt.R
import com.aaditya.moengageassignemnt.ui.WebViewActivity
import com.aaditya.moengageassignemnt.model.Article
import com.bumptech.glide.Glide

class NewsAdapter(private val context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var newsList: List<Article> = emptyList()

    fun setNewsList(articles: List<Article>?) {
        if (articles != null) {
            this.newsList = articles
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = newsList[position]
        holder.bind(article)

        // Handle item click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.NEWS_URL, article.url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = newsList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = article.title
            itemView.findViewById<TextView>(R.id.descriptionTextView).text = article.description

            Glide.with(itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder_image)
                .into(itemView.findViewById(R.id.imageView))
        }
    }
}
