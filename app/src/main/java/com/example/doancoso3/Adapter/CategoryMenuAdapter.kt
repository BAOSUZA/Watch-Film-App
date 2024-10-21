package com.example.doancoso3.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.doancoso3.Data.Category
import com.example.doancoso3.R

class CategoryMenuAdapter(val list: MutableList<Category>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class FilmViewHolder(item: View):RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_menu, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            val tvItemCategory = findViewById<TextView>(R.id.tvItemCategory)
            tvItemCategory.text = list[position].categoryName.toString()
            tvItemCategory.setOnClickListener {
                Toast.makeText(this.context, "Bạn click vào danh mục ${list[position].categoryName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = list.size
}