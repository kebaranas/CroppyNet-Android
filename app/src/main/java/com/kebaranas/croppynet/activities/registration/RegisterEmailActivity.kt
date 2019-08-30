package com.kebaranas.croppynet.activities.registration

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.activities.home.HomeActivity
import com.kebaranas.croppynet.activities.logging.LogInActivity
import com.kebaranas.croppynet.adapters.SexSpinnerAdapter
import com.kebaranas.croppynet.models.AccountBasicInfo
import com.kebaranas.croppynet.models.SexSpinnerItem
import durdinapps.rxfirebase2.*
import kotlinx.android.synthetic.main.activity_register_email.*
import java.util.*

class RegisterEmailActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    DatePickerDialog.OnDateSetListener {

    private val pReqCode: Int = 1
    private val gReqCode: Int = 1
    private lateinit var sex: SexSpinnerItem
    private var image: Uri? = null
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email)

        val sexes: ArrayList<SexSpinnerItem> = arrayListOf(
            SexSpinnerItem("Male"), SexSpinnerItem("Female"),
            SexSpinnerItem("Intersex")
        )
        val sexSpinnerAdapter = SexSpinnerAdapter(this, sexes)

        sexSpinner.adapter = sexSpinnerAdapter
        sexSpinner.onItemSelectedListener = this

        val datePickerDialog = DatePickerDialog(
            this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        profileImageCircularImageView.setOnClickListener {
            checkAndRequestForPermission()
        }

        birthDateButton.setOnClickListener {
            datePickerDialog.updateDate(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        signUpButton.setOnClickListener {
            val userEmail: String = emailEditText.text.toString().trim()
            val userPassword: String = passwordEditText.text.toString().trim()
            val userConfirmPassword: String = confirmPasswordEditText.text.toString().trim()
            val userFirstName: String = firstNameEditText.text.toString().trim()
            val userLastName: String = lastNameEditText.text.toString().trim()
            val userBirthDate: String = birthDateButton.text.toString().trim()

            when {
                userEmail.isEmpty() -> {
                    emailEditText.error = "Please provide an email."
                    emailEditText.requestFocus()
                }
                userPassword.isEmpty() -> {
                    passwordEditText.error = "Please provide a password."
                    passwordEditText.requestFocus()
                }
                userConfirmPassword.isEmpty() -> {
                    confirmPasswordEditText.error = "Please provide a matching password."
                    confirmPasswordEditText.requestFocus()
                }
                userPassword != userConfirmPassword -> {
                    confirmPasswordEditText.error = "The password does not match."
                    confirmPasswordEditText.requestFocus()
                }
                userFirstName.isEmpty() -> {
                    firstNameEditText.error = "Please provide your first name."
                    firstNameEditText.requestFocus()
                }
                userLastName.isEmpty() -> {
                    lastNameEditText.error = "Please provide your last name."
                    lastNameEditText.requestFocus()
                }
                userBirthDate.isEmpty() -> {
                    birthDateButton.error = "Please provide your birth date."
                    birthDateButton.requestFocus()
                }
                else -> {
                    loading()
                    val year: Int = calendar.get(Calendar.YEAR)
                    val month: Int = calendar.get(Calendar.MONTH)
                    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
                    createUserAccount(
                        userEmail, userPassword, userFirstName, userLastName, year, month, day,
                        sex.text
                    )
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sex = parent?.selectedItem as SexSpinnerItem
        if (image == null) {
            when {
                sex.text == "Male" -> {
                    Glide.with(this).load(R.drawable.ic_male_farmer)
                        .into(profileImageCircularImageView)
                }
                sex.text == "Female" -> {
                    Glide.with(this).load(R.drawable.ic_female_farmer)
                        .into(profileImageCircularImageView)
                }
                else -> {
                    Glide.with(this).load(R.drawable.ic_anonymous_farmer)
                        .into(profileImageCircularImageView)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val monthName: String = calendar.getDisplayName(
            Calendar.MONTH, Calendar.LONG, Locale.getDefault()
        )
        birthDateButton.text = "${monthName.capitalize()} $dayOfMonth, $year"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == gReqCode && data != null) {
            image = data.data!!
            Glide.with(this).load(image).into(profileImageCircularImageView)
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

    @SuppressLint("CheckResult")
    private fun createUserAccount(
        email: String, password: String, firstName: String, lastName: String, birthYear: Int,
        birthMonth: Int, birthDay: Int, sex: String
    ) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        RxFirebaseAuth.createUserWithEmailAndPassword(firebaseAuth, email, password)
            .subscribe({
                if (image != null) {
                    updateUserInfoWithImage(
                        email, firstName, lastName, image, birthYear, birthMonth, birthDay, sex,
                        firebaseAuth.currentUser
                    )
                } else {
                    updateUserInfoWithoutImage(
                        email, firstName, lastName, birthYear, birthMonth, birthDay, sex,
                        firebaseAuth.currentUser
                    )
                }
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                unloading()
            })
    }

    @SuppressLint("CheckResult")
    private fun updateUserInfoWithoutImage(
        email: String, firstName: String, lastName: String, birthYear: Int, birthMonth: Int,
        birthDay: Int, sex: String, currentUser: FirebaseUser?
    ) {
        val profileUpdate: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName("$firstName $lastName")
            .build()

        RxFirebaseUser.updateProfile(currentUser!!, profileUpdate).subscribe({
            uploadUserInfo(
                email, firstName, lastName, birthYear, birthMonth, birthDay, sex, currentUser
            )
        }, {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            unloading()
        })
    }

    @SuppressLint("CheckResult")
    private fun updateUserInfoWithImage(
        email: String, firstName: String, lastName: String, image: Uri?, birthYear: Int,
        birthMonth: Int, birthDay: Int, sex: String, currentUser: FirebaseUser?
    ) {
        val userProfileImage: StorageReference = FirebaseStorage.getInstance().reference
            .child("users_files")
            .child(currentUser?.uid!!)
            .child("images")
            .child("profile_picture")

        RxFirebaseStorage.putFile(userProfileImage, image!!).subscribe({
            RxFirebaseStorage.getDownloadUrl(userProfileImage).subscribe({
                val profileUpdate: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName("$firstName $lastName")
                    .setPhotoUri(it)
                    .build()
                createUserProfile(
                    currentUser, profileUpdate, email, firstName, lastName, birthYear, birthMonth,
                    birthDay, sex
                )
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                unloading()
            })
        }, {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            unloading()
        })
    }

    @SuppressLint("CheckResult")
    private fun createUserProfile(
        currentUser: FirebaseUser, profileUpdate: UserProfileChangeRequest, email: String,
        firstName: String, lastName: String, birthYear: Int, birthMonth: Int, birthDay: Int,
        sex: String
    ) {
        RxFirebaseUser.updateProfile(currentUser, profileUpdate).subscribe({
            uploadUserInfo(
                email, firstName, lastName, birthYear, birthMonth, birthDay, sex, currentUser
            )
        }, {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            unloading()
        })
    }

    @SuppressLint("CheckResult")
    private fun uploadUserInfo(
        email: String, firstName: String, lastName: String, birthYear: Int, birthMonth: Int,
        birthDay: Int, sex: String, currentUser: FirebaseUser
    ) {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val document: DocumentReference = firestore.collection("Users")
            .document(currentUser.uid)
        val userInfo =  AccountBasicInfo(
            email, "Regular", firstName, lastName, birthYear, birthMonth, birthDay, sex
        )

        RxFirestore.setDocument(document, userInfo).subscribe({
            Toast.makeText(this, "Account registration complete.", Toast.LENGTH_LONG)
                .show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            unloading()
        }, {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            unloading()
        })
    }

    private fun loading() {
        signUpButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun unloading() {
        signUpButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, gReqCode)
    }

    private fun checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), pReqCode
                )
        } else {
            openGallery()
        }
    }
}
