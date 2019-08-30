package com.kebaranas.croppynet.activities

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.adapters.CategorySpinnerAdapter
import com.kebaranas.croppynet.models.CategorySpinnerItem
import com.kebaranas.croppynet.models.Product
import durdinapps.rxfirebase2.RxFirebaseStorage
import durdinapps.rxfirebase2.RxFirestore
import kotlinx.android.synthetic.main.activity_my_produce.*

class MyProduceActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var selectedCategory: CategorySpinnerItem
    private var mImageUri: Uri? = null

    @SuppressLint("ShowToast", "CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_produce)

        val categories: ArrayList<CategorySpinnerItem> = arrayListOf()
        categories.add(CategorySpinnerItem("Fruits"))
        categories.add(CategorySpinnerItem("Vegetables"))
        categories.add(CategorySpinnerItem("Meat"))
        categories.add(CategorySpinnerItem("Fish"))
        val categoriesSpinnerAdapter = CategorySpinnerAdapter(this, categories)
        categorySpinner.adapter = categoriesSpinnerAdapter
        categorySpinner.onItemSelectedListener = this

        submitButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            if (mImageUri != null) {
                val title: String = titleEditText.text.toString().trim()
                val firestorage: FirebaseStorage = FirebaseStorage.getInstance()
                val storageReference = firestorage.getReference("product_images")
                val fileReference: StorageReference = storageReference
                    .child("${title}_${System.currentTimeMillis()}.${getFileExtension(mImageUri!!)}")

                RxFirebaseStorage.putFile(fileReference, mImageUri!!).subscribe({
                    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

                    RxFirebaseStorage.getDownloadUrl(fileReference).subscribe({
                        val price: Double = priceEditText.text.toString().toDouble()
                        val product = Product(title, price, it.toString())
                        val document: DocumentReference = firestore.collection("Products").document()

                        RxFirestore.setDocument(document, product).subscribe({
                            progressBar.visibility = View.GONE
                            Toast.makeText(applicationContext,
                                "The product was added successfully.",
                                Toast.LENGTH_LONG).show()
                        }, {
                            Toast.makeText(applicationContext,
                                "The product was not added.",
                                Toast.LENGTH_SHORT).show()
                        })
                    }, {
                        Toast.makeText(this, "Failed to post the product.", Toast.LENGTH_LONG).show()
                    })
                }, {
                    Toast.makeText(applicationContext,
                        "Failed to post the product",
                        Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(applicationContext,
                    "Please choose an image to be uploaded.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        addImageButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data
            Glide.with(this).load(mImageUri).into(selectedImageView)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedCategory = parent?.selectedItem as CategorySpinnerItem
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mimeType: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
