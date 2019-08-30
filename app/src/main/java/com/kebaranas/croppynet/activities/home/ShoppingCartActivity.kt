package com.kebaranas.croppynet.activities.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kebaranas.croppynet.R

class ShoppingCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
