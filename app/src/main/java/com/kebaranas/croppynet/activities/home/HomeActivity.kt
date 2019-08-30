package com.kebaranas.croppynet.activities.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.activities.MyProduceActivity
import com.kebaranas.croppynet.activities.logging.LogInActivity
import com.kebaranas.croppynet.activities.registration.RegisterActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(bottomToolbar)

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        val bottomNavHostFragment: NavController = Navigation.findNavController(
            this, R.id.bottomNavHostFragment
        )
        NavigationUI.setupWithNavController(bottomNavView, bottomNavHostFragment)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, bottomToolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        updateNavDrawer()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navProfile -> {
                val intent = Intent(applicationContext, MyProduceActivity::class.java)
                startActivity(intent)
            }
            R.id.navLogging -> {
                FirebaseAuth.getInstance().signOut()
                val intent: Intent = if (currentUser.isAnonymous) Intent(
                    applicationContext, RegisterActivity::class.java
                ) else Intent(
                    applicationContext, LogInActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun updateNavDrawer() {
        val headerView: View = navView.getHeaderView(0)

        headerView.navUserNameTextView.text = currentUser.displayName
        Glide.with(this).load(currentUser.photoUrl).into(headerView.navUserImageView)

        when {
            currentUser.isAnonymous -> headerView.navUserIdTextView.text = "Anonymous account"
            currentUser.email != null -> headerView.navUserIdTextView.text = currentUser.email
            currentUser.phoneNumber != null -> {
                headerView.navUserIdTextView.text = currentUser.phoneNumber
            }
        }

        when {
            currentUser.isAnonymous && currentUser.photoUrl == null -> {
                Glide.with(this).load(R.drawable.ic_anonymous_farmer)
                    .into(headerView.navUserImageView)
            }
            currentUser.photoUrl != null && !currentUser.isAnonymous -> {
                Glide.with(this).load(currentUser.photoUrl)
                    .into(headerView.navUserImageView)
            }
            currentUser.photoUrl == null && !currentUser.isAnonymous -> {
                Glide.with(this).load(R.drawable.ic_male_farmer)
                    .into(headerView.navUserImageView)
            }
        }
    }
}
