package com.example.doancoso3.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doancoso3.Activity.MovieDetailsActivity
import com.example.doancoso3.Data.Film
import com.example.doancoso3.R

class FilmListCategoryAdapter(val list: MutableList<Film>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class FilmViewHolder(item: View):RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film_catagory, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            val imgFilmImage = findViewById<ImageView>(R.id.imgFilmImage)
            val tvFilmName = findViewById<TextView>(R.id.tvFilmName)
            if (list[position] != null) {
                tvFilmName.text = list[position].movieName
                Glide.with(this).load(list[position].movieImage).into(imgFilmImage)
            }
            imgFilmImage.setOnClickListener {
                Toast.makeText(this.context, "Bạn click vào film ${list[position].movieName}", Toast.LENGTH_SHORT).show()
                val i = Intent(holder.itemView.context, MovieDetailsActivity::class.java)
                i.putExtra("categoryId", list[position].categoryId)
                i.putExtra("movieName", list[position].movieName)
                i.putExtra("movieEpisodes", list[position].movieEpisodes)
                i.putExtra("movieTime", list[position].movieTime)
                i.putExtra("movieDescription", list[position].movieDescription)
                holder.itemView.context.startActivity(i)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}