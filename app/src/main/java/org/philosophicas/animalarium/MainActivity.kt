package org.philosophicas.animalarium

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

var editMode = false

class MainActivity : AppCompatActivity() {


    //http://psiqueylogos-ac.com/animalarium/

    private var dbLocalVersion = 0
    private var dbRemoteVersion = 0
    private val PREFERENCE_NAME = "preferences"
    private val PRIVATE_MODE = 0
    private val downloader = FileDownloader()
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: FirestorePagingAdapter<ProductModel, ProductViewHolder>

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()


    //private lateinit var products: List<Product>
    private lateinit var searcher: SearchView


    override fun onResume() {
        super.onResume()
        //recyclerView.adapter = MainRecyclerViewAdapter(products)
        //recyclerView.adapter!!.notifyDataSetChanged()

    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searcher = findViewById(R.id.main_search_view)
        recyclerView = findViewById(R.id.main_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Init Paging Configuration
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        //Set query
        val mQuery = db.collection("products")
            .orderBy("name")


        // Init Adapter Configuration
        val options = FirestorePagingOptions.Builder<ProductModel>()
            .setLifecycleOwner(this)
            .setQuery(mQuery, config, ProductModel::class.java)
            .build()


        // Instantiate Paging Adapter
        mAdapter = object : FirestorePagingAdapter<ProductModel, ProductViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = layoutInflater.inflate(R.layout.product_card, parent, false)
                return ProductViewHolder(view)
            }

            override fun onBindViewHolder(
                viewHolder: ProductViewHolder,
                position: Int,
                post: ProductModel
            ) {
                // Bind to ViewHolder
                viewHolder.bind(post)
            }

            override fun onError(e: Exception) {
                super.onError(e)
                Log.e("MainActivity", e.message!!)
            }

            /*
            override fun onLoadingStateChanged(state: LoadingState) {
                when (state) {
                    LoadingState.LOADING_INITIAL -> {
                        swipeRefreshLayout.isRefreshing = true
                    }

                    LoadingState.LOADING_MORE -> {
                        swipeRefreshLayout.isRefreshing = true
                    }

                    LoadingState.LOADED -> {
                        swipeRefreshLayout.isRefreshing = false
                    }

                    LoadingState.ERROR -> {
                        Toast.makeText(
                            applicationContext,
                            "Error Occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                        swipeRefreshLayout.isRefreshing = false
                    }

                    LoadingState.FINISHED -> {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        */
        }

        recyclerView.adapter = mAdapter



        searcher.setOnSearchClickListener {
            it as SearchView
            //   recyclerView.adapter!!.notifyDataSetChanged()
        }

        searcher.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 == "cargar") {
                    var intent = Intent(this@MainActivity, ProductEditorActivity::class.java)
                    intent.putExtra("ACTION", ProductEditorActivity.Companion.Action.Add.name)
                    this@MainActivity.startActivity(intent)
                } else if (p0 == "editar") {
                    editMode = true
                    recyclerView.invalidate()
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        //Link whatsapp button
        ib_whatsapp.setOnClickListener {
            //val url = "https://api.whatsapp.com/send?phone=$number"
            val url = "https://wa.me/c/584129169520"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        //Link twitter button
        ib_twitter.setOnClickListener {

            val url = "https://twitter.com/Animalarium_VE?s=09"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

    }

}