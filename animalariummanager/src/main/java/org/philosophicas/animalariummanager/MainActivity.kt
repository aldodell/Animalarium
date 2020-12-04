package org.philosophicas.animalariummanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var tabLayout: TabLayout
    lateinit var rv_products: RecyclerView
    lateinit var rv_orders: RecyclerView
    lateinit var cl_a: ConstraintLayout
    lateinit var cl_b: ConstraintLayout
    lateinit var btn_add_more: Button
    var callbackImagePickup: ((Uri?) -> Unit)? = null
    private var IMAGE_PICKUP = 100

    val productAdapter = ProductsAdapter(this)
    val storageReference = FirebaseStorage.getInstance().reference


    fun updateUI(): Activity {
        productAdapter.notifyDataSetChanged()
        return this
    }

    fun pickImage(callback: (Uri?) -> Unit) {
        callbackImagePickup = callback
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, IMAGE_PICKUP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKUP) {
            val uri = data?.data
            if (uri != null) {
                callbackImagePickup?.invoke(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Obtenemos los componentes
        tabLayout = findViewById(R.id.tabLayout)
        rv_products = findViewById(R.id.rv_products)
        rv_orders = findViewById(R.id.rv_orders)
        cl_a = findViewById(R.id.constraintsLayoutA)
        cl_b = findViewById(R.id.constraintsLayoutB)
        btn_add_more = findViewById(R.id.btn_add_more)


        //Conectamos los adaptadores
        rv_products.layoutManager = LinearLayoutManager(this.baseContext)
        rv_products.adapter = productAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            /**
             * Called when a tab enters the selected state.
             *
             * @param tab The tab that was selected
             */
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {

                    when (tab.position) {

                        0 -> {
                            cl_a.visibility = View.VISIBLE
                            cl_b.visibility = View.GONE
                        }

                        1 -> {
                            cl_a.visibility = View.GONE
                            cl_b.visibility = View.VISIBLE
                        }
                    }
                }
            }

            /**
             * Called when a tab exits the selected state.
             *
             * @param tab The tab that was unselected
             */
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            /**
             * Called when a tab that is already selected is chosen again by the user. Some applications may
             * use this action to return to the top level of a category.
             *
             * @param tab The tab that was reselected.
             */
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        //Cargamos los productos
        val db = FirebaseFirestore.getInstance()
        db.collection("products").get().addOnSuccessListener {
            it.documents.forEach { doc ->
                val p = ProductModel()
                p.id = doc.id
                p.name = doc["name"] as String
                p.description = doc["description"] as String
                p.price = doc["price"] as String
                p.image = doc["image"] as String

                products.add(p)

                //Download images:
                val img = File(this.filesDir, doc.id)

                if (!img.exists()) {

                    val imageReference =
                        storageReference.child("products/images/" + p.id)
                    val MB: Long = 1024 * 1024
                    imageReference.getBytes(MB).addOnSuccessListener {
                        img.writeBytes(it)

                    }
                }

            }
            productAdapter.notifyDataSetChanged()

        }


        //Manejador de crear un producto nuevo
        btn_add_more.setOnClickListener {
            val product = ProductModel()
            db.collection("products")
                .add(product.toHashMap)
                .addOnSuccessListener {
                    product.id = it.id
                    products.add(0, product)
                    updateUI()
                    Toast.makeText(this.baseContext, "¡Nuevo!", Toast.LENGTH_SHORT)
                        .show()
                }
        }


    }
}