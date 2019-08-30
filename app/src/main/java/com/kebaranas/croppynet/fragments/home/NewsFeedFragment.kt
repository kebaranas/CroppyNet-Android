package com.kebaranas.croppynet.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.activities.ProductDetailsActivity
import com.kebaranas.croppynet.activities.home.PostActivity
import com.kebaranas.croppynet.adapters.ProductsAdapter
import com.kebaranas.croppynet.models.Product
import durdinapps.rxfirebase2.RxFirestore
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_store.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsFeedFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        activity?.fab?.setImageResource(R.drawable.ic_create)
        return inflater.inflate(R.layout.fragment_news_feed, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//        val collection: CollectionReference = firestore.collection("Products")

//        RxFirestore.getCollection(collection).map<List<Product>> {
//            it.toObjects(Product::class.java)
//        }.subscribe({
//            itemsRecycleView.apply {
//                layoutManager = GridLayoutManager(activity, 2)
//                adapter = ProductsAdapter(it) { title, photoUrl, photoView ->
//                    val intent = Intent(activity, ProductDetailsActivity::class.java)
//                    intent.putExtra("title", title)
//                    intent.putExtra("photoUrl", photoUrl)
//                    val options: ActivityOptionsCompat = ActivityOptionsCompat
//                        .makeSceneTransitionAnimation(
//                            activity as AppCompatActivity, photoView,
//                            "transitionTarget"
//                        )
//                    startActivity(intent, options.toBundle())
//                }
//            }
//            activity?.progressBar!!.visibility = View.GONE
//        }, {
//            Toast.makeText(this.activity, it.message, Toast.LENGTH_LONG).show()
//        })
//
        activity?.fab?.setOnClickListener {
            val intent = Intent(activity, PostActivity::class.java)
            startActivity(intent)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsFeedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
