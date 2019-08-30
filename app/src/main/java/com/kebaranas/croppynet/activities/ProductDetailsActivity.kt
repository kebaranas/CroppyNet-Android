package com.kebaranas.croppynet.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kebaranas.croppynet.R
import kotlinx.android.synthetic.main.product_details.*

class ProductDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)

        val title: String = intent.getStringExtra("title")
        val photoUrl: String = intent.getStringExtra("photoUrl")

        titleProduct.text = title
        Glide.with(this).load(photoUrl).into(imageProduct)

        availableButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("$title is in stock.")
                .create()
                .show()
        }
    }
}