package com.example.doancoso3.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import at.blogc.android.views.ExpandableTextView
import com.example.doancoso3.Adapter.ViewPagerAdapter
import com.example.doancoso3.Data.Film
import com.example.doancoso3.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_film_details.*
import kotlinx.android.synthetic.main.item_film_catagory.*

class FilmDetailsFragment(val nameCategory: String, val list: MutableList<Film>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_film_details, container, false)
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, nameCategory,list)
        val pageLayout = view.findViewById<ViewPager2>(R.id.pageDemo)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabDemo)
        // Inflate the layout for this fragment

        pageLayout.adapter = adapter
        TabLayoutMediator(tabLayout, pageLayout) { tab, pos ->
            Log.d("ukinown", pos.toString())
            when (pos) {
                0 -> {
                    tab.text = "Đề xuất cho bạn"
                }
                1 -> {
                    tab.text = "Đánh giá"
                }
            }
        }.attach()
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val expandableTextview = view.findViewById<ExpandableTextView>(R.id.expandableTextview)
        val tv_Title = view.findViewById<TextView>(R.id.tv_Title)
        val txt_toggle = view.findViewById<TextView>(R.id.txt_toggle)
        val pageLayout = view.findViewById<ViewPager2>(R.id.pageDemo)
        val tv_Desc = view.findViewById<ExpandableTextView>(R.id.expandableTextview)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabDemo)
        val movieTimeFilm = view.findViewById<TextView>(R.id.movieTime)
        val bundle = this.arguments
        val bundle_Sent = Bundle()

        val categoryId = bundle?.getString("categoryId")
        val movieName = bundle?.getString("movieName")
        val movieEpisodes = bundle?.getInt("movieEpisodes")
        val movieTime = bundle?.getInt("movieTime")
        val movieDescription = bundle?.getString("movieDescription")

        expandableTextview.setAnimationDuration(750L)
        expandableTextview.setInterpolator(OvershootInterpolator())

        tv_Title.setText(movieName)
        tv_Desc.setText(movieDescription)
        movieTimeFilm.text = movieTime.toString() + " Phút"

        txt_toggle.setOnClickListener {
            showHideDescription()
        }

    }

    private fun showHideDescription() {
        if (expandableTextview.isExpanded) {
            expandableTextview.collapse()
            txt_toggle.setText("Xem thêm")
        } else {
            expandableTextview.expand()
            txt_toggle.setText("Ẩn bớt")
        }
    }

}