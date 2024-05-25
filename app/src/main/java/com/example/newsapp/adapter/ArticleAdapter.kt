package com.example.newsapp.adapter

// Step 2: Create RecyclerView Adapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.dataclass.Article
import com.example.newsapp.utils.formatDate

class ArticleAdapter(private var articles: List<Article>, private val context: Context) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val imageArticle: ImageView = itemView.findViewById(R.id.imageArticle)
        val dateTextView:TextView=itemView.findViewById(R.id.dateTextView)
        val visitWebsiteButton:AppCompatButton =itemView.findViewById(R.id.visitWebsiteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.top_headline_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.textTitle.text = article.title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.dateTextView.text= formatDate(article.publishedAt)
        }
        println("Date ")

        // Load article image using Glide
        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .apply(RequestOptions.placeholderOf(R.drawable.placeholder_news)) // Placeholder image
            .into(holder.imageArticle)

        holder.visitWebsiteButton.setOnClickListener(){
            openLink(article.url)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }
    fun setData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged() // Notify RecyclerView that the data has changed
    }
    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

}
