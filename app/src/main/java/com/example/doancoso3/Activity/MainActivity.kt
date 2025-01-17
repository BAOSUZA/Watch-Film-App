package com.example.doancoso3

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.doancoso3.Activity.SearchFilmActivity
import com.example.doancoso3.Adapter.SilderAdapter
import com.example.doancoso3.Data.Category
import com.example.doancoso3.Data.FavoriteMovies
import com.example.doancoso3.Data.Film
import com.example.doancoso3.Interface.DatabaseCallback
import com.example.doancoso3.Interface.SliderInterface
import com.example.doancoso3.databinding.ActivityMainBinding
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity(), DatabaseCallback {
    lateinit var binding: ActivityMainBinding
    lateinit var sliderAdapter: SilderAdapter
    private lateinit var userId: String
    private lateinit var filmList: MutableList<Film>
    private lateinit var categoryList: MutableList<Category>
    private lateinit var favoriteMoviesList: MutableList<FavoriteMovies>
    private lateinit var idFragmentList: MutableList<Int>
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      Nhận thông tin của người dùng khi đăng nhập
        val i = intent.extras
        userId = ""
        if (i != null) {
            userId = i.getString("userId").toString()
        }

//      Xử lý sự kiện khi nhấn vào nút Menu -> MenuFragment hiển thi ra
        binding.taskMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userId", userId)
            val menuFragment = MenuFragment()
            menuFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_to_right_menu, R.anim.right_to_left_menu)
                .replace(R.id.framelayoutMenu, menuFragment)
                .addToBackStack(null)
                .commit()

            binding.framelayoutMenu.visibility = View.VISIBLE
            binding.CoatingScreen.animate().setDuration(200).alpha(1f).withEndAction { binding.CoatingScreen.visibility = View.VISIBLE }

//          Xử lý sự kiện khi click ngoài vùng fragment -> MenuFragment tắt đi
            binding.CoatingScreen.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.left_to_right_menu, R.anim.right_to_left_menu)
                        .remove(menuFragment)
                        .commit()
                    binding.CoatingScreen.visibility = View.GONE
                    true
                } else {
                    false
                }
            }
        }
//        Xử lý sự kiện khi click vào nút tìm kiếm
        binding.taskSearch.setOnClickListener {
            val intent = Intent(this, SearchFilmActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        binding.container.setOnClickListener {}

//      Kiểm tra thiết bị đã kết nối với internet chưa
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            // Thiết bị đã kết nối với internet -> duyệt dữ liệu từ firebase
            getDatabase(this)
        } else {
            // Thiết bị chưa kết nối với internet
            binding.scrollView2.visibility = View.GONE
            binding.linearLayoutNoConnect.visibility = View.VISIBLE
        }
        binding.btnRetry.setOnClickListener {
            binding.scrollView2.visibility = View.VISIBLE
            binding.linearLayoutNoConnect.visibility = View.GONE
            getDatabase(this)
        }
    }


    override fun onDatabaseLoaded() {
//      Thiết lập danh sách các bộ phim nổi bật ra silde
        sliderAdapter = SilderAdapter(this, filmList, favoriteMoviesList, object : SliderInterface {
//          Xử lý sự kiện xem phim bằng cách click nút xem phim tại slider
            override fun watchMovies(position: Int) {
                // xử lý tại đây
            }

//          Xử lý sự kiện thêm bộ phim yêu thích bằng cách click vào nút yêu thích trên slider
            override fun addFavoriteMovies(position: Int) {
                val dbRef = FirebaseDatabase.getInstance().getReference("FavoriteMovies")
                val favoriteMoviesId = dbRef.push().key!!
                val movieId = filmList[position].movieId.toString()
                val favorite = FavoriteMovies(favoriteMoviesId, movieId, userId)
                dbRef.child(favoriteMoviesId).setValue(favorite)
                getDatabase(this@MainActivity)
            }

//          Xử lý sự kiện hủy bộ phim yêu thích bằng cách click vào nút xóa yêu thích trên slider
            override fun removeFavoriteMovies(id: String) {
                val dbRef12 = FirebaseDatabase.getInstance().getReference("FavoriteMovies").child(id)
                dbRef12.removeValue()
                getDatabase(this@MainActivity)
            }

//          Hiện thị thông báo không được thêm và xóa yêu thích bộ phim khi chưa đăng nhập
            override fun notifyAddOrRemove() {
                val build = AlertDialog.Builder(this@MainActivity)
                val view = layoutInflater.inflate(R.layout.dialog_search_history_remove, null)
                build.setView(view)

                // Ánh xạ đến các element trong dialog_search_history_remove
                val btnDialogCancel = view.findViewById<Button>(R.id.btnDialogCancel)
                val btnDialogRemove = view.findViewById<Button>(R.id.btnDialogRemove)
                val tvDialogRemoveTitle = view.findViewById<TextView>(R.id.tvDialogRemoveTitle)
                val tvDialogQues = view.findViewById<TextView>(R.id.tvDialogQues)
                tvDialogRemoveTitle.setText("Chưa đăng nhập!")
                tvDialogQues.setText("Vui lòng đăng nhập tài khoản.")
                btnDialogRemove.setText("Đồng ý")

                //Xử lý các sự kiện của các button
                btnDialogCancel.setOnClickListener {
                    dialog.dismiss()
                }
                btnDialogRemove.setOnClickListener {
                    val intent = Intent(this@MainActivity, SignInActivity::class.java)
                    startActivity(intent)
                }

                dialog = build.create()
                dialog.show()
            }
        }, userId)
        binding.vpSlider.adapter = sliderAdapter
        binding.circleIndicator.setViewPager(binding.vpSlider)
        sliderAdapter.registerDataSetObserver(binding.circleIndicator.dataSetObserver)
        binding.vpSlider.setPageTransformer(true, sliderAdapter)

        // Thiết lập tự động trượt vòng
        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            var currentItem = binding.vpSlider.currentItem
            currentItem++
            if (currentItem >= sliderAdapter.count) {
                currentItem = 0
            }
            binding.vpSlider.setCurrentItem(currentItem, true)
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 6000, 6000) // Thiết lập thời gian trượt vòng tại đây


//      Thiết lập phim của mỗi danh mục ra RecyclerView
        var dem: Int = 0
        for (i in categoryList) {
            idFragmentList = mutableListOf()
            idFragmentList.addAll(listOf(R.id.frameLayoutCategoryFilm1, R.id.frameLayoutCategoryFilm2, R.id.frameLayoutCategoryFilm3, R.id.frameLayoutCategoryFilm4, R.id.frameLayoutCategoryFilm5))
            var moviesByCatagory: MutableList<Film> = mutableListOf()

            //  Kiểm tra danh dách film có thuộc category qua từng lần duyệt hay không
            //  Nếu có thì add dữ liệu danh sách film đó vào moviesByCatagory
            for (j in filmList) {
                if (i.categoryId.toString() == j.categoryId.toString()) {
                    moviesByCatagory.add(j)
                }
            }
            // Kiểm tra danh sách đó nếu không có bộ film nào thì sẽ không hiển thị ra
            // Còn nếu có thì hiển thị ra.....
            if (moviesByCatagory.size > 0) {
                supportFragmentManager.beginTransaction()
                    .add(idFragmentList[dem] , FilmListCategoryFragment(categoryList[dem].categoryName.toString(), moviesByCatagory))
                    .commit()
                dem++
            }
        }
    }

    fun getDatabase(callback: DatabaseCallback) {
        val dbRef1 = FirebaseDatabase.getInstance().getReference("Category")
        categoryList = mutableListOf()
        dbRef1.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    for (categorySnap in snapshot.children) {
                        val categoryData = categorySnap.getValue(Category::class.java)
                        categoryList.add(categoryData!!)
                    }
                }
            }
        }

        val dbRef2 = FirebaseDatabase.getInstance().getReference("Film")
        filmList = mutableListOf()
        dbRef2.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    for (filmSnap in snapshot.children) {
                        val filmData = filmSnap.getValue(Film::class.java)
                        if (filmData != null) {
                            filmList.add(filmData)
                        }
                    }
                }
            }
        }

        val dbRef3 = FirebaseDatabase.getInstance().getReference("FavoriteMovies")
        favoriteMoviesList = mutableListOf()
        dbRef3.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    for (favoriteMoviesSnap in snapshot.children) {
                        val favoriteMoviesData = favoriteMoviesSnap.getValue(FavoriteMovies::class.java)
                        if (favoriteMoviesData != null) {
                            favoriteMoviesList.add(favoriteMoviesData)
                        }
                    }
                }
            }
            callback.onDatabaseLoaded()
        }
    }
}

