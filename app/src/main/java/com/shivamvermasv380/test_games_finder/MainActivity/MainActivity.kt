package com.shivamvermasv380.test_games_finder.MainActivity

import Login_Register.LoginActivity
import View_Create_Posts.ViewPost
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shivamvermasv380.test_games_finder.R

class MainActivity : AppCompatActivity() {
    var token:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun view_posts(view: android.view.View) {
        val intent = Intent(applicationContext, ViewPost::class.java)
        intent.putExtra("token",token)
        startActivity(intent)
    }



    fun displayloginPage(view: android.view.View) {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
}