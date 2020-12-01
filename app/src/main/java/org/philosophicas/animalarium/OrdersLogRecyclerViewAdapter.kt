package org.philosophicas.animalarium

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class OrdersLogRecyclerViewAdapter(var orders: ArrayList<Order>) :
    RecyclerView.Adapter<OrdersLogRecyclerViewAdapter.OrdersLogViewHolder>() {


    class OrdersLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.ol_date_tv)
        var products: TextView = itemView.findViewById(R.id.ol_products_tv)
        var price: TextView = itemView.findViewById(R.id.ol_price_tv)
        var status: TextView = itemView.findViewById(R.id.ol_status_tv)

        fun bind(order: Order) {
            date.setText(order.date)
            products.setText(order.products)
            price.setText(order.price.toString())
            status.setText(order.status.name)

        }
    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersLogViewHolder {

        var card =
            LayoutInflater.from(parent.context).inflate(R.layout.order_log_card, parent, false)
        var vh = OrdersLogViewHolder(card)
        return vh
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return orders.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: OrdersLogViewHolder, position: Int) {
        holder.bind(orders[position])
    }

}