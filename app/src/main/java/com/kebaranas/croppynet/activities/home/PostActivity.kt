package com.kebaranas.croppynet.activities.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kebaranas.croppynet.R
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        postEditText.requestFocus()

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!

        when {
            currentUser.isAnonymous && currentUser.photoUrl == null -> {
                Glide.with(this).load(R.drawable.ic_anonymous_farmer)
                    .into(userImageView)
            }
            currentUser.photoUrl != null && !currentUser.isAnonymous -> {
                Glide.with(this).load(currentUser.photoUrl)
                    .into(userImageView)
            }
            currentUser.photoUrl == null && !currentUser.isAnonymous -> {
                Glide.with(this).load(R.drawable.ic_male_farmer)
                    .into(userImageView)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_post_action_bar, menu)
        bottomToolbar.inflateMenu(R.menu.activity_post_bottom_action_bar)
        return true
    }
}