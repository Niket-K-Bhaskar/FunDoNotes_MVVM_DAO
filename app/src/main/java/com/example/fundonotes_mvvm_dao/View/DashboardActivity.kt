package com.example.fundonotes_mvvm_dao.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fundonotes_mvvm_dao.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mDrawerLayout : DrawerLayout
    lateinit var mNavigationView :NavigationView
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(findViewById(R.id.appBar))
        mDrawerLayout = findViewById(R.id.mainDrawer)
        mNavigationView = findViewById<View>(R.id.nav_view) as NavigationView
        setupDrawerLayout()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.rightcorner_menu, menu)

        return true
    }

    fun setupDrawerLayout() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this, findViewById(R.id.mainDrawer),
             R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        mNavigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navLogout -> {
                    firebaseAuth.signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            return@OnNavigationItemSelectedListener true
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var menuItem = item.itemId
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        when(menuItem){
            R.id.dashboardProfileBtn -> ProfileFragment().show(supportFragmentManager,
                ProfileFragment.TAG
            )
        }
        return super.onOptionsItemSelected(item)
    }

}