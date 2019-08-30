package com.kebaranas.croppynet.fragments.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.activities.ProductDetailsActivity
import com.kebaranas.croppynet.activities.home.ShoppingCartActivity
import com.kebaranas.croppynet.adapters.ProductsAdapter
import com.kebaranas.croppynet.models.Product
import durdinapps.rxfirebase2.RxFirestore
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.android.synthetic.main.fragment_store.view.*

class StoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        activity?.fab?.setImageResource(R.drawable.ic_shopping_cart)
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.fab?.setOnClickListener {
            val intent = Intent(activity, ShoppingCartActivity::class.java)
            startActivity(intent)
        }

        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collection: CollectionReference = firestore.collection("Products")

        RxFirestore.getCollection(collection).map<List<Product>> {
            it.toObjects(Product::class.java)
        }.subscribe({
            view.itemsRecycleView.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = ProductsAdapter(it) { title, photoUrl, photoView ->
                    val intent = Intent(activity, ProductDetailsActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("photoUrl", photoUrl)
                    val options: ActivityOptionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(
                            activity as AppCompatActivity, photoView,
                            "transitionTarget"
                        )
                    startActivity(intent, options.toBundle())
                }
            }
            if (isAdded) {
                activity?.progressBar!!.visibility = View.GONE
            }
        }, {
            Toast.makeText(this.activity, it.message, Toast.LENGTH_LONG).show()
        })
        super.onViewCreated(view, savedInstanceState)
    }
}
