package org.philosophicas.animalarium

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_orders_log.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class OrdersLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_log)

        val personID =
            getSharedPreferences("MySettings", Context.MODE_PRIVATE).getString("personID", "")

        if (personID.isNullOrEmpty()) finish()

        var orders = ArrayList<Order>()
        orders_log_recycler_view.layoutManager = LinearLayoutManager(this.baseContext)
        orders_log_recycler_view.adapter = OrdersLogRecyclerViewAdapter(orders)

        val db = FirebaseFirestore.getInstance()

        db.collection("orders").whereEqualTo("personID", personID).get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.forEach {
                    val order = Order()
                    val ts = it["dateTime"] as Timestamp
                    order.date = ts.toDate().toString()
                    order.status = Status.valueOf(it["status"] as String)
                    val productsID = it["products"] as String
                    orders.add(order)
                    val n = orders.size - 1
                    productsID.split(",").forEach { pID ->
                        db.collection("products").document(pID).get()
                            .addOnSuccessListener { doc ->
                                val name = doc["name"] as String
                                orders[n].products = "${order.products}$name, "
                                orders[n].price += (doc["price"] as String).toDouble()
                            }
                    }

                }

            }


        Timer("loader", false).schedule(2000) {
            orders.forEach { it.products = it.products.removeSuffix(", ") }
            runOnUiThread {
                (orders_log_recycler_view.adapter as OrdersLogRecyclerViewAdapter).notifyDataSetChanged()
            }
        }
    }


    /*
    EN aquel tiempo, dijo Jesús a sus discípulos:
«Cuando venga en su gloria el Hijo del hombre, y todos los ángeles con él, se sentará en el trono de su gloria y serán reunidas ante él todas las naciones.
Él separará a unos de otros, como un pastor separa las ovejas de las cabras.
Y pondrá las ovejas a su derecha y las cabras a su izquierda. Entonces dirá el rey a los de su derecha:
“Venid vosotros, benditos de mi Padre; heredad el reino preparado para vosotros desde la creación del mundo. Porque tuve hambre y me disteis de comer, tuve sed y me disteis de beber, fui forastero y me hospedasteis, estuve desnudo y me vestisteis, enfermo y me visitasteis, en la cárcel y vinisteis a verme”.
Entonces los justos le contestarán.

Allí empieza el canto y la representació de los muchachos.
Terminado este momento el Padre dirá: “Palabra del Señor”.

     */

}