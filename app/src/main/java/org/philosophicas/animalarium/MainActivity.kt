package org.philosophicas.animalarium

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


var editMode = false
var shoppingCarList = ArrayList<ProductModel>()

class MainActivity : AppCompatActivity() {

    var products = ArrayList<ProductModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: ProductRecyclerViewAdapter

    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    //private lateinit var products: List<Product>
    private lateinit var searcher: SearchView

    private var productSelectedId = ""

    override fun onResume() {
        super.onResume()
        recyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //get references
        searcher = findViewById(R.id.main_search_view)
        recyclerView = findViewById(R.id.main_recycler)

        //UI Configuration
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)


        searcher.setOnSearchClickListener {
            it as SearchView
            recyclerView.adapter!!.notifyDataSetChanged()
        }

        searcher.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                val intent = Intent(this@MainActivity, ProductEditorActivity::class.java)

                if (p0 == "cargar") {
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

        //Orders logs
        ib_envios.setOnClickListener {
            var intent = Intent(this.baseContext, OrdersLogActivity::class.java)
            startActivity(intent)
        }


        //Set data source
        var source = Source.SERVER

        if (!isNetworkAvailable()) {
            source = Source.CACHE
        }

        // Retrieve product list
        db.collection("products").get(source).addOnSuccessListener { it ->
            for (document in it.documents) {
                var product = ProductModel()
                product.description = document.getString("description")
                product.image = document.getString("image")
                product.id = document.id
                product.name = document.getString("name")
                product.price = document.getString("price")
                products.add(product)

                val img = File(this.filesDir, product.image!!)

                if (!img.exists()) {

                    val storageReference = FirebaseStorage.getInstance().reference
                    val imageReference =
                        storageReference.child("products/images/" + product.image)
                    val MB: Long = 1024 * 1024
                    imageReference.getBytes(MB).addOnSuccessListener {
                        img.writeBytes(it)
                    }
                }
            }

            recyclerView.adapter = ProductRecyclerViewAdapter(products)

            Timer("loading", false).schedule(2000) {
                runOnUiThread {
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }


        }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                    .show()
            }


    }


    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}

fun deleteProduct(id: String) {
    val db = FirebaseFirestore.getInstance()
    db.document("products/$id").delete()
    val storageReference = FirebaseStorage.getInstance().reference
    val imageReference =
        storageReference.child("products/images/" + id)
    imageReference.delete()
}
