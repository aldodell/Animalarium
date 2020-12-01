package org.philosophicas.animalarium

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_load_product.*
import kotlinx.android.synthetic.main.product_card.*

class ProductCard : AppCompatActivity() {

    val ID = intent.getIntExtra("ID", -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_card)
    }
}