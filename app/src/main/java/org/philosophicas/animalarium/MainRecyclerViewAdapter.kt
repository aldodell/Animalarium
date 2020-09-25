package org.philosophicas.animalarium

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class MainRecyclerViewAdapter(val products: List<ProductModel>) :
    RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    var viewHolder: ViewHolder? = null
    val db = FirebaseFirestore.getInstance()


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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // if (viewHolder == null) {
        var card =
            LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false)
        viewHolder = ViewHolder(card)
        //}
        return viewHolder!!
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
       return products.size

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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ba = products[position].image!!
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(ba, 0, ba.size))
        holder.description.text = products[position].description
        holder.price.text = products[position].price
        holder.name.text = products[position].name
        if (editMode) {
            holder.editButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE

            holder.editButton.setOnClickListener {
                val ctx = holder.editButton.context
                var intent = Intent(ctx, ProductEditorActivity::class.java)
                intent.putExtra("ID", position)
                intent.putExtra("ACTION", ProductEditorActivity.Companion.Action.Update.name)
                ctx.startActivity(intent)
            }



            holder.deleteButton.setOnClickListener {
                if (position > -1) {
                    AlertDialog.Builder(holder.deleteButton.context, R.style.AppTheme)
                        .setMessage("¿Deseas eliminar el producto?")
                        .setPositiveButton("Sí") { _, _ ->

                        }.show()
                }


            }
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.product_image)
        var description: TextView = itemView.findViewById(R.id.product_description)
        var price: TextView = itemView.findViewById(R.id.product_price)
        var name: TextView = itemView.findViewById(R.id.product_name)
        var editButton: Button = itemView.findViewById(R.id.product_edit_button)
        var deleteButton: Button = itemView.findViewById(R.id.product_delete_button)
    }


}

