package net.larntech.guasiapa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import net.larntech.guasiapa.model.login.LoginModelRequest
import net.larntech.guasiapa.model.login.LoginResponse
import net.larntech.guasiapa.model.register.RegisterModelRequest
import net.larntech.guasiapa.model.register.RegisterModelResponse
import net.larntech.guasiapa.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUserName: EditText;
    private lateinit var etUserEmail: EditText;
    private lateinit var edPassword: EditText;
    private lateinit var edcPassword: EditText;
    private lateinit var btnRegister: Button;
    private lateinit var tvLogin: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initData()
    }


    private fun initData(){
        etUserEmail = findViewById(R.id.etUserEmail)
        etUserName = findViewById(R.id.etUserName)
        edPassword = findViewById(R.id.edPassword)
        edcPassword = findViewById(R.id.edcPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)
        clickListener();
    }

    private fun clickListener(){
        btnRegister.setOnClickListener{
            createUser()
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    private fun createUser(){

        var name = etUserName.text.toString();
        var email = etUserEmail.text.toString();
        var password = edPassword.text.toString();
        var cPassword = edcPassword.text.toString();
        if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && cPassword.isNotEmpty()){
            if(password == cPassword){
                saveUser(name, email,password)
            }else{
                showMessage("Password mismatch")
            }
        }else{
            showMessage("All inputs required")
        }
    }

    private fun saveUser(name: String, email: String, password: String){
        showMessage("Registering user ...")
        val registerRequest = RegisterModelRequest(name, email,password);

        val apiCall = ApiClient.getApiService().registerUser(registerRequest);
        apiCall.enqueue(object : Callback<RegisterModelResponse> {
            override fun onResponse(call: Call<RegisterModelResponse>, response: Response<RegisterModelResponse>) {
                if(response.isSuccessful){
                    if(!response.body()!!.error)
                     moveToLogin()
                    else
                        showMessage(response.body()!!.message)
                }else{
                    showMessage("Unable to register, Please check your credential and try again")
                }
            }

            override fun onFailure(call: Call<RegisterModelResponse>, t: Throwable) {
                showMessage("An error occurred "+t.localizedMessage)
            }

        })
    }

    private fun moveToLogin(){
        Handler().postDelayed(Runnable {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },1500)

    }

    private fun showMessage(message: String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

}