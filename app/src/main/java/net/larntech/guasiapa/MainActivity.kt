package net.larntech.guasiapa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import net.larntech.guasiapa.allstories.StoriesActivity
import net.larntech.guasiapa.share_pref.SharePref

class MainActivity : AppCompatActivity() {
    private lateinit var tvAppName: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData();
    }

    private fun initData(){
        tvAppName = findViewById(R.id.tvAppName);
        checkIfUserLogin();
    }

    private fun checkIfUserLogin(){
       if(SharePref.getLoginDetails() == null){
           goToLogin()
       }else{
           goToStories()
       }
    }

    private fun goToLogin(){
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
    private fun goToStories(){
        startActivity(Intent(this, StoriesActivity::class.java))
        finish()
    }


}