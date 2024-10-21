package com.example.doancoso3.Activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.example.doancoso3.Data.Film
import com.example.doancoso3.Fragment.FilmChapterFragment
import com.example.doancoso3.Fragment.FilmDetailsFragment
import com.example.doancoso3.R
import com.example.doancoso3.databinding.ActivityMovieDetailsBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class MovieDetailsActivity(val nameCategory: String, val list: MutableList<Film>)  : AppCompatActivity() {

    companion object {
        var isFullScreen = false
        var isLock = false
    }

    lateinit var handler: Handler
    lateinit var simpleExoPlayer: SimpleExoPlayer
    lateinit var bt_fullscreen: ImageView
    lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bt_fullscreen = findViewById<ImageView>(R.id.bt_fullscreen)
        val bt_lockscreen = findViewById<ImageView>(R.id.exo_lock)
        val list = mutableListOf<Film>()
        handler = Handler(Looper.getMainLooper())
        val filmDetailsFragment = FilmDetailsFragment("", list)
        val filmChapterFragment = FilmChapterFragment()
        val i = intent.extras
        val bundle_Sent = Bundle()
        val playerView = binding.player
        val categoryId = intent.getStringExtra("categoryId")
        val movieName = intent.getStringExtra("movieName")
        val movieEpisodes = intent.getIntExtra("movieEpisodes",0)
        val movieTime = intent.getIntExtra("movieTime",0)
        val movieDescription = intent.getStringExtra("movieDescription")



        bundle_Sent.putString("categoryId", categoryId)
        bundle_Sent.putString("movieName", movieName)
        bundle_Sent.putString("movieEpisodes", movieEpisodes.toString())
        bundle_Sent.putInt("movieTime", movieTime)
        bundle_Sent.putString("movieDescription", movieDescription)

        supportFragmentManager.beginTransaction().apply {
            filmDetailsFragment.arguments = bundle_Sent
            replace(R.id.ll_descMovie, filmDetailsFragment).commit()
        }

        supportFragmentManager.beginTransaction().apply {
            filmChapterFragment.arguments = bundle_Sent
            replace(R.id.movie_chapter, filmChapterFragment).commit()
        }


//        pageLayout.adapter = adapter
//        TabLayoutMediator(tabLayout, pageLayout) { tab, pos ->
//            when (pos) {
//                0 -> {
//                    tab.text = "Đề xuất cho bạn"
//                }
//                1 -> {
//                    tab.text = "Đánh giá"
//                }
//            }
//        }.attach()
        bt_fullscreen.setOnClickListener {
            if (!isFullScreen) {
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                bt_fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen_exit_vp
                    )
                )
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
                playerView.layoutParams = params
            } else {
                val dpValue = 200
                val density = resources.displayMetrics.density
                val pixelValue = (dpValue * density).toInt()
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    pixelValue
                )
                bt_fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen_vp
                    )
                )
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                playerView.layoutParams = params
            }
            isFullScreen = !isFullScreen
        }

        bt_lockscreen.setOnClickListener {
            if (!isLock) {
                bt_lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock_vp
                    )
                )
            } else {
                bt_lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock_open_vp
                    )
                )
            }
            isLock = !isLock
            lockScreen(isLock)
        }

        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        playerView.player = simpleExoPlayer
        playerView.keepScreenOn = true
        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.progressBar.visibility = View.VISIBLE
//                    startBufferingTimer()

                } else if (playbackState == Player.STATE_READY) {
                    binding.progressBar.visibility = View.GONE
                }

                if (!simpleExoPlayer.playWhenReady) {
                    handler.removeCallbacks(updateProgressAction)
                } else {
                    onProgress()
                }
            }
        })
        val videoSource = Uri.parse("https://scontent.fdad1-4.fna.fbcdn.net/v/t42.1790-2/331178948_186906700701361_3027448970841977436_n.mp4?_nc_cat=100&ccb=1-7&_nc_sid=985c63&efg=eyJ2ZW5jb2RlX3RhZyI6InN2ZV9zZCJ9&_nc_ohc=luu1fiZ_6x0AX8SQBvO&tn=cxOykJTd-5ok-gwA&_nc_rml=0&_nc_ht=scontent.fdad1-4.fna&oh=00_AfD6SY8WUtEN_gJ6jUPtwzU4V8AFMXpdRdhQiZ2_to06aA&oe=643CE91B")
        val mediaItem = MediaItem.fromUri(videoSource)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    var check = false
    fun onProgress() {
        val player = simpleExoPlayer
        val position: Long = if (player == null) 0 else player.currentPosition
        handler.removeCallbacks(updateProgressAction)
        val playbackState = if (player == null) Player.STATE_IDLE else player.playbackState
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            var delayMs: Long
            if (player.playWhenReady && playbackState == Player.STATE_READY) {
                delayMs = 1000 - position % 1000
                if (delayMs < 200) {
                    delayMs += 1000
                }
            } else {
                delayMs = 1000
            }

            handler.postDelayed(updateProgressAction, delayMs)
        }

    }

    private val updateProgressAction = Runnable { onProgress() }
    private fun lockScreen(lock: Boolean) {
        val sec_mid = findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom = findViewById<LinearLayout>(R.id.sec_controlvid2)
        if (lock) {
            sec_mid.visibility = View.INVISIBLE
            sec_bottom.visibility = View.INVISIBLE
        } else {
            sec_mid.visibility = View.VISIBLE
            sec_bottom.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (isLock) return
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bt_fullscreen.performClick()
        } else super.onBackPressed()

    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }

}