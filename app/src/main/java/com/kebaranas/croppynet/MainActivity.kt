package com.kebaranas.croppynet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kebaranas.croppynet.activities.registration.RegisterActivity
import com.kebaranas.croppynet.activities.home.HomeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
//    @SuppressLint("CheckResult")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
//
//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu)
//        }
//
//        supportFragmentManager.beginTransaction().replace(R.id.contentFrameLayout, MainFragment()).commit()
//
//        navView.setNavigationItemSelectedListener {
//            it.isChecked = true
//            drawerLayout.closeDrawers()
//            when (it.itemId) {
//                R.id.actionShop -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.contentFrameLayout, MainFragment()).commit()
//                }
//                R.id.actionShoppingCart -> {
//                    val intent = Intent(this, ShoppingCartActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.actionMyProduce -> {
//                    val intent = Intent(this, MyProduceActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.actionSettings -> {
////                    supportFragmentManager.beginTransaction().replace(R.id.contentFrameLayout,
////                        SettingsFragment()
////                    ).commit()
//                }
//                R.id.actionAccountRegistration -> {
//                    val intent = Intent(this, RegisterActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.actionAccountLogging-> {
//                    val intent = Intent(this, LogInActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//            true
//        }
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            android.R.id.home -> {
//                drawerLayout.openDrawer(GravityCompat.START)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        val manager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchItem: MenuItem? = menu?.findItem(R.id.actionSearch)
//        val searchView: SearchView = searchItem?.actionView as SearchView
//
//        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
//
//        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//        })
//        return super.onCreateOptionsMenu(menu)
//    }
}
