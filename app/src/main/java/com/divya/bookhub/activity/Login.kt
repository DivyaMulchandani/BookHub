package com.divya.bookhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.divya.bookhub.R

class login : AppCompatActivity() {

    lateinit var bt_login:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bt_login = findViewById(R.id.bt_login)

        bt_login.setOnClickListener{

            val intent=Intent(this@login, page1::class.java)
            startActivity(intent)

        }

    }



}