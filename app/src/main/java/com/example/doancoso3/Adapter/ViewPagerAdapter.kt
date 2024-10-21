package com.example.doancoso3.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.doancoso3.Data.Film
import com.example.doancoso3.FilmListCategoryFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val nameCategory: String, val list: MutableList<Film>) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FilmListCategoryFragment(nameCategory, list)
            else -> FilmListCategoryFragment(nameCategory, list)
        }
    }
}