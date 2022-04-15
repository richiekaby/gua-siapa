package net.larntech.guasiapa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import net.larntech.guasiapa.allstories.StoriesActivity
import net.larntech.guasiapa.model.login.LoginModelRequest
import net.larntech.guasiapa.model.login.LoginResponse
import net.larntech.guasiapa.network.ApiClient
import net.larntech.guasiapa.share_pref.SharePref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText;
    private lateinit var edPassword: EditText;
    private lateinit var btnLogin: Button;
    private lateinit var tvCreateAccount: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initData()
    }

    private fun initData(){
        etEmail = findViewById(R.id.etEmail)
        edPassword = findViewById(R.id.edPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvCreateAccount = findViewById(R.id.tvCreateAccount)
        clickListener();
    }

    private fun clickListener(){
        btnLogin.setOnClickListener{
            loginUser();
        }
        tvCreateAccount.setOnClickListener {
            registerUser();
        }
    }

    private fun loginUser(){
        var email = etEmail.text.toString();
        var password = edPassword.text.toString();

        if(email.isNotEmpty() && password.isNotEmpty()){
            authUser(email,password)
        }else{
            showMessage("All inputs required ...")
        }

    }


    private fun authUser(email: String, password: String){
        showMessage(" Authenticating user ...")
        val loginRequest = LoginModelRequest(email,password);
        val apiCall = ApiClient.getApiService().loginUser(loginRequest);
        apiCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    moveToLogin(response.body()!!);
                }else{
                    showMessage("Unable to login, Please check your credential and try again")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showMessage("An error occurred "+t.localizedMessage)
            }

        })
    }


    private fun moveToLogin(loginResponse: LoginResponse){
        SharePref.saveLoginResponse(loginResponse)
            startActivity(Intent(this, StoriesActivity::class.java))
            finish()
    }

    private fun showMessage(message: String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }



    private fun registerUser(){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

}