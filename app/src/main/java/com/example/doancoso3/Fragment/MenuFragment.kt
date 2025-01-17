package com.example.doancoso3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doancoso3.Activity.AccountInformationActivity
import com.example.doancoso3.Activity.FavoriteMoviesActivity
import com.example.doancoso3.Activity.ViewHistoryActivity
import com.example.doancoso3.Adapter.CategoryMenuAdapter
import com.example.doancoso3.Data.Category
import com.example.doancoso3.Data.User
import com.example.doancoso3.Interface.DatabaseCallback
import com.example.doancoso3.databinding.FragmentMenuBinding
import com.google.firebase.database.FirebaseDatabase

class MenuFragment() : Fragment(), DatabaseCallback  {
    lateinit var binding: FragmentMenuBinding
    private var categoryList: MutableList<Category> = mutableListOf()
    private var userList: MutableList<User> = mutableListOf()
    private var userId: String = ""
    private var userName: String = ""
    private var email: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(layoutInflater)

//      hiển thị ra danh mục phim
        getDataList(this)

        return binding.root
    }

    override fun onDatabaseLoaded() {
//      Hiển thị ra danh mục film
        binding.rvCategoryFilm.adapter = CategoryMenuAdapter(categoryList)
        binding.rvCategoryFilm.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

//      hiển thị tên đăng nhập (nhận dữ liệu từ MainActivity chuyển qua)
        if (arguments?.getString("userId").toString() != "") {
            userId = arguments?.getString("userId").toString()
            for (i in userList) {
                if (i.userId == userId) {
                    binding.tvUserName.text = i.userName.toString()
                    userName = i.userName.toString()
                    email = i.email.toString()
                }
            }
            binding.tvUserName.visibility = View.VISIBLE
            binding.tvLogout.visibility = View.VISIBLE
            binding.tvLogin.visibility = View.GONE
        }
//      xử lý các sự kiện khi click vào các item menu
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this.context, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.tvAccount.setOnClickListener {
            if (userId != "") {
                val intent = Intent(this.context, AccountInformationActivity::class.java)
                intent.putExtra("userId", userId)
                intent.putExtra("userName", userName)
                intent.putExtra("email", email)
                startActivity(intent)
            }else {
                val intent = Intent(this.context, SignInActivity::class.java)
                startActivity(intent)
            }

        }
        binding.tvFavorite.setOnClickListener {
            val intent = Intent(this.context, FavoriteMoviesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        binding.tvNotify.setOnClickListener {
            Toast.makeText(this.context, "Bạn click vào thông báo", Toast.LENGTH_SHORT).show()
        }
        binding.tvWatching.setOnClickListener {
            val intent = Intent(this.context, ViewHistoryActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        binding.tvLogout.setOnClickListener {
            val intent = Intent(this.context, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    fun getDataList(callback: DatabaseCallback) {
        val dbRef1 = FirebaseDatabase.getInstance().getReference("Category")
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
            // Gọi callback với danh sách đã được cập nhật hoàn chỉnh
            callback.onDatabaseLoaded()
        }

        val dbRef2 = FirebaseDatabase.getInstance().getReference("User")
        dbRef2.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot != null && snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val userData = userSnap.getValue(User::class.java)
                        userList.add(userData!!)
                    }
                }
            }
            // Gọi callback với danh sách đã được cập nhật hoàn chỉnh
            callback.onDatabaseLoaded()
        }
    }
}