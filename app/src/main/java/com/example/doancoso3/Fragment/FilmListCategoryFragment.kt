package com.example.doancoso3

import android.graphics.LinearGradient
import android.graphics.Outline
import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doancoso3.Adapter.FilmListCategoryAdapter
import com.example.doancoso3.Data.Film
import com.example.doancoso3.databinding.FragmentCategoryFilmBinding

class FilmListCategoryFragment(val nameCategory: String, val list: MutableList<Film>) : Fragment() {
    lateinit var binding: FragmentCategoryFilmBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryFilmBinding.inflate(layoutInflater)
        binding.rvFilm.adapter = FilmListCategoryAdapter(list)
        binding.rvFilm.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.tvViewALlFilm.setOnClickListener {
            Toast.makeText(this.context, "Bạn click vào xem tất cả phim trong danh mục ${nameCategory}", Toast.LENGTH_SHORT).show()
        }
        binding.tvCategoryName.text = nameCategory
        binding.tvCategoryName.setGradientColor()
        return binding.root
    }
}

private fun TextView.setGradientColor() {
    paint.shader = LinearGradient(
        0f,
        0f,
        0f,
        textSize,
        ContextCompat.getColor(this.context, R.color.textview_gradient_start),
        ContextCompat.getColor(this.context, R.color.textview_gradient_end),
        Shader.TileMode.CLAMP
    )
    invalidate()
}
