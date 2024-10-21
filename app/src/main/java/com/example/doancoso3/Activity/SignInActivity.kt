package com.example.doancoso3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.doancoso3.Activity.ForgotPasswordActivity
import com.example.doancoso3.Data.User
import com.example.doancoso3.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var userList: ArrayList<User>
    private lateinit var auth : FirebaseAuth
    private lateinit var client: GoogleSignInClient
    private lateinit var googleSignInClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      Khai báo phương thức kết nối google
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.imgBack.setOnClickListener {
            val i = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(i)
        }

        binding.tvSignUpAccount.setOnClickListener {
            val i = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(i)
        }

        binding.tvForgotPassword.setOnClickListener {
            val i = Intent(this@SignInActivity, ForgotPasswordActivity::class.java)
            startActivity(i)
        }

        binding.btnSignin.setOnClickListener {
            checkAccount()
        }

        binding.btnSignByGoogle.setOnClickListener {
            signInWithGoogle()
        }

    }

//  Đăng nhập với google
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful) {
            val account : GoogleSignInAccount? = task.result
            if(account != null) {
                updateUI(account)
            }
        }
        else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

//  Xử lý sự kiện khi đăng nhập google thành công
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("userName", account.displayName)
                Log.d("googleIntent", account.displayName.toString())
                startActivity(intent)
            }
            else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

//  Xử lý khi cảm ứng vuốt để trở về
    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(i)
    }

//  Kiểm tra đăng nhập
    private fun checkAccount() {
        dbRef = FirebaseDatabase.getInstance().getReference("User")
        val email = binding.tietEmail.text.toString()
        val password = binding.tietPassword.text.toString()

//      kiểm tra email và password lúc đăng nhập có đúng ở trong authentication
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
//              Duyệt dữ liệu trong firebase từ authentication
                val user = FirebaseAuth.getInstance().currentUser // lấy thông tin người dùng thông qua FirebaseAuth.getInstance().currentUser
                val userId = user?.uid.toString()
                val userName = user?.displayName.toString()
                val imgUser = user?.photoUrl.toString()
                val email = user?.email.toString()

//              Truyền dữ liệu vừa duyệt qua ActivityMain khi đăng nhập thành công
                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                intent.putExtra("userName", userName)
                intent.putExtra("imgUser", imgUser)
                intent.putExtra("userId", userId)
                startActivity(intent)
                Toast.makeText(this@SignInActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@SignInActivity, "Đăng nhập không thành công, Tài khoản hoặc mật khẩu sai", Toast.LENGTH_SHORT).show()
            }
        }
    }
}