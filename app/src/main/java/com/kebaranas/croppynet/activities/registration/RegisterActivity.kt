package com.kebaranas.croppynet.activities.registration

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.activities.home.HomeActivity
import com.kebaranas.croppynet.activities.logging.LogInActivity
import durdinapps.rxfirebase2.RxFirebaseAuth
import durdinapps.rxfirebase2.RxFirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        accountTextView.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        anonymousRegisterButton.setOnClickListener {
            loading()
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
            RxFirebaseAuth.signInAnonymously(firebaseAuth).subscribe({ authResult ->
                val profileUpdate: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName("Anonymous User")
                .build()

                RxFirebaseUser.updateProfile(authResult.user!!, profileUpdate).subscribe({
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    unloading()
                }, {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    unloading()
                })
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                unloading()
            })
        }

        phoneRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterPhoneActivity::class.java)
            startActivity(intent)
        }

        emailRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterEmailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_register_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.actionLogIn -> {
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loading() {
        anonymousRegisterButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun unloading() {
        anonymousRegisterButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }
}
