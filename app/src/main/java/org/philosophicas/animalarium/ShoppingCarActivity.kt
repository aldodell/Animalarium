package org.philosophicas.animalarium

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_shopping_car.*

class ShoppingCarActivity : AppCompatActivity() {

    private val adapter = ShoppingCarRecyclerViewAdapter()
    lateinit var observer: RecyclerView.AdapterDataObserver

    class Observer(var sca: ShoppingCarActivity) : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            sca.updateUI()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_car)

        sc_recyclerview.layoutManager = LinearLayoutManager(this)
        sc_recyclerview.adapter = adapter
        sc_recyclerview.adapter?.notifyDataSetChanged()

        sc_add_more_button.setOnClickListener {
            this.finish()
        }

        sc_continue_button.setOnClickListener {
            val intent = Intent(it.context, OrderActivity::class.java)
            it.context.startActivity(intent)
        }

        observer = Observer(this)

        updateUI()
    }


    fun updateUI() {

        val quantity = shoppingCarList.size
        var total = 0.0
        shoppingCarList.map { total += it.price?.toDouble() ?: 0.0 }
        val s = "Productos: $quantity, total precio: $total"
        sc_resume_tv.text = s

    }


    override fun onResume() {
        super.onResume()

        sc_recyclerview.adapter!!.registerAdapterDataObserver(observer)
        sc_recyclerview.adapter?.notifyDataSetChanged()


        updateUI()
    }


    override fun onPause() {
        super.onPause()
        sc_recyclerview.adapter!!.unregisterAdapterDataObserver(observer)
    }
}