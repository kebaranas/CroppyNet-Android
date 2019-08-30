package com.kebaranas.croppynet.activities.logging

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.activities.home.HomeActivity
import com.kebaranas.croppynet.activities.registration.RegisterActivity
import durdinapps.rxfirebase2.RxFirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*


class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        logInButton.setOnClickListener {
            val userAccount: String = emailPhoneEditText.text.toString().trim()
            val userPassword: String = passwordEditText.text.toString().trim()

            when {
                userAccount.isEmpty() -> {
                    emailPhoneEditText.error = "Please provide your email or phone number."
                    emailPhoneEditText.requestFocus()
                }
                userPassword.isEmpty() -> {
                    passwordEditText.error = "Please provide the password of your account."
                    passwordEditText.requestFocus()
                }
                else -> {
                    loading()
                    logInWithEmail(userAccount, userPassword)
                }
            }
        }

        noAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_login_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.actionRegister -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("CheckResult")
    private fun logInWithEmail(userAccount: String, userPassword: String) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, userAccount, userPassword).subscribe({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            unloading()
        }, {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            unloading()
        })
    }

    @SuppressLint("CheckResult")
    private fun logInWithPhoneNumber(userAccount: String, userPassword: String) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val credential: AuthCredential = PhoneAuthProvider.getCredential(userAccount, userPassword)

        RxFirebaseAuth.signInWithCredential(firebaseAuth, credential).subscribe({
            d("Kyle", "Signed in using a phone number.")
            unloading()
        }, {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            unloading()
        })
    }

    private fun isValidMail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidMobile(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    private fun loading() {
        progressBar.visibility = View.VISIBLE
        logInButton.visibility = View.INVISIBLE
    }

    private fun unloading() {
        progressBar.visibility = View.GONE
        logInButton.visibility = View.VISIBLE
    }
}
