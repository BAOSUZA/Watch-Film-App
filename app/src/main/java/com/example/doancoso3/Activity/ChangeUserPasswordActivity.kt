package com.example.doancoso3.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.doancoso3.MainActivity
import com.example.doancoso3.databinding.ActivityChangeUserPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ChangeUserPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangeUserPasswordBinding
    lateinit var userId: String
    lateinit var userName: String
    lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeUserPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      Nhận dữ liệu từ AccountInformationActivity gửi qua
        val i = intent.extras
        if (i != null) {
            userId = i.getString("userId").toString()
            email = i.getString("email").toString()
            userName = i.getString("userName").toString()
        }

//      xử lý sự kiển đổi mật khẩu
        binding.btnComplete.setOnClickListener {
            changePassword()
        }

//      xử lý sự kiện trở về AccountInformationActivity(quản lý tài khoản)
        binding.imgBack.setOnClickListener {
            val intent = Intent(this, AccountInformationActivity::class.java)
            intent.putExtra("userId",userId)
            intent.putExtra("userName", userName)
            intent.putExtra("email",email)
            startActivity(intent)
        }

//      xử lý sự kiện trở về trang chủ
        binding.imgHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }

    }

    private fun changePassword() {
        val password = binding.tietNewPassword.text.toString()
        val repassword = binding.tietNewRepassword.text.toString()
        val user =  FirebaseAuth.getInstance().currentUser
        if (password == repassword) {
            user?.updatePassword(password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                    binding.tietOldPassword.setText("")
                    binding.tietNewPassword.setText("")
                    binding.tietNewRepassword.setText("")
                }
            }
        }else {
            binding.tietNewRepassword.error = "Nhập lại mật khẩu chưa chính xác!"
        }
    }
}