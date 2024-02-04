package com.example.articles.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.articles.R
import com.example.articles.data.ListItem


class ListOfArticleAdapter(val article: List<ListItem>) :
    RecyclerView.Adapter<ListOfArticleAdapter.ViewHolder>() {

    private val expandedItems = HashSet<Int>()
    private var liked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_list_articles, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return article.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = article[position]
        holder.Author.text = item.author
        holder.Title.text = item.title
        holder.url.text = item.url
        holder.url.setOnClickListener {
            openWebPage(holder.itemView.context, item.url)
        }
        holder.source.text = item.source.name
        holder.hidden.visibility = if (expandedItems.contains(position)) View.VISIBLE else View.GONE
        holder.arrow.setOnClickListener {
            if (expandedItems.contains(position)) {
                expandedItems.remove(position)
                holder.content.visibility = View.VISIBLE
                holder.arrow.setImageResource(R.drawable.upwards)
                holder.content.text = item.content
            } else {
                expandedItems.add(position)
                holder.content.visibility = View.GONE
                holder.content.text = item.content
            }
            notifyItemChanged(position)
        }

        holder.important.setOnClickListener {
            liked = !liked
            toggleLikeAnimation(holder.important)

        }
    }

    private fun toggleLikeAnimation(important: ImageButton): Boolean {
        if (liked) {
            important.setImageResource(R.drawable.ic_star_liked);
            // Change to unliked icon
        } else {
            important.setImageResource(R.drawable.ic_star); // Change to liked icon
        }

        return liked;
    }


    fun updateList(newList: List<ListItem>) {
        var article = newList
        notifyDataSetChanged()
    }

    private fun openWebPage(context: Context?, url: String?) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (context != null) {
            if (intent.resolveActivity(context.packageManager) != null) {
                if (context != null) {
                    context.startActivity(intent)
                }
            } else {
                // Handle case where a web browser app is not installed
                Toast.makeText(context, "No web browser found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Author: TextView = itemView.findViewById(R.id.author)
        val Title: TextView = itemView.findViewById(R.id.title)
        val url: TextView = itemView.findViewById(R.id.webView)
        val source: TextView = itemView.findViewById(R.id.source)
        val hidden: LinearLayout = itemView.findViewById(R.id.hidden_view)
        val arrow: ImageButton = itemView.findViewById(R.id.arrow_button)
        val content: TextView = itemView.findViewById(R.id.details)
        val important: ImageButton = itemView.findViewById(R.id.important)


    }
}