package com.example.doancoso3.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doancoso3.Data.Film
import com.example.doancoso3.R
import kotlinx.android.synthetic.main.chapter_layout.view.*

class FilmChapterAdapter(val ds : List<Film>) : RecyclerView.Adapter<FilmChapterAdapter.MovieChapterViewHolder>() {
    inner class MovieChapterViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieChapterViewHolder {
        val context = LayoutInflater.from(parent.context).inflate(R.layout.chapter_layout, parent, false)
        return MovieChapterViewHolder(context)
    }

    override fun onBindViewHolder(holder: MovieChapterViewHolder, position: Int) {
        holder.itemView.apply {
            tv_chapterMovie.text = ds[position].movieEpisodes.toString()
        }
    }

    override fun getItemCount(): Int {
        return ds.size
    }
}