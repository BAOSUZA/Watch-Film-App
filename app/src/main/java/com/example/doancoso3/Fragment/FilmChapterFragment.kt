package com.example.doancoso3.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doancoso3.Adapter.FilmChapterAdapter
import com.example.doancoso3.Data.Film
import com.example.doancoso3.R


class FilmChapterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_film_chapter, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = this.arguments
        super.onViewCreated(view, savedInstanceState)
        val chapter_Movie = bundle?.getString("movieEpisodes")?.toInt() ?:0
        val list_dataActor = mutableListOf<Film>()
        val ryc_chapter = view.findViewById<RecyclerView>(R.id.ryc_Chapter)
        for(i in 1..chapter_Movie) {
            list_dataActor.add(Film("", "", "", "", i, 0, ""))
        }

        val adapters = FilmChapterAdapter(list_dataActor)
        ryc_chapter.adapter = adapters
        ryc_chapter.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
    }
}