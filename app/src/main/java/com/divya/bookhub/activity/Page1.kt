package com.divya.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.divya.bookhub.*
import com.divya.bookhub.fragment.AboutApp
import com.divya.bookhub.fragment.dashboardfragment
import com.divya.bookhub.fragment.fav
import com.divya.bookhub.fragment.profile

class page1 : AppCompatActivity() {

    lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout
    lateinit var coordinator:androidx.coordinatorlayout.widget.CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigation: com.google.android.material.navigation.NavigationView

    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page1)

        drawerLayout=findViewById(R.id.drawerlayout)
        coordinator=findViewById(R.id.coordinator)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.framelayout)
        navigation=findViewById(R.id.navigation)
        setUpToolbar()

        opendashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@page1,drawerLayout, R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigation.setNavigationItemSelectedListener {

            if (previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId){
                R.id.dashboard ->{
                   opendashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.fav ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fav())
                        .commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.app ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, AboutApp())
                        .commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, profile())
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }


    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="BookHub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun opendashboard(){
        val fragment= dashboardfragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout,fragment)
        transaction.commit()
        supportActionBar?.title="Dashboard"
        navigation.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.framelayout)
        when(frag){
            !is dashboardfragment ->  opendashboard()
            else -> super.onBackPressed()
        }
    }


}

