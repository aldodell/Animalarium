package org.philosophicas.animalarium

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ProductRecyclerViewAdapter(private val products: List<ProductModel>) :
    RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder>() {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false)
        )


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
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(products[position])


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return products.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.product_image)
        var description: TextView = itemView.findViewById(R.id.product_description)
        var price: TextView = itemView.findViewById(R.id.product_price)
        var name: TextView = itemView.findViewById(R.id.product_name)
        var editButton: Button = itemView.findViewById(R.id.product_edit_button)
        var deleteButton: Button = itemView.findViewById(R.id.product_delete_button)
        var buyButton: Button = itemView.findViewById(R.id.product_buy_button)


        fun bind(product: ProductModel) {
            name.text = product.name
            description.text = product.description
            price.text = product.price

            if (product.image != null) {
                val file = File(image.context.filesDir, product.image!!)
                val bitmap = BitmapFactory.decodeFile(file.path)
                image.setImageBitmap(bitmap)
            }

            editButton.setOnClickListener {
                val intent = Intent(itemView.context, ProductEditorActivity::class.java)
                intent.putExtra("ACTION", ProductEditorActivity.Companion.Action.Update.name)
                intent.putExtra("ID", product.id!!)
                itemView.context.startActivity(intent)
            }

            deleteButton.setOnClickListener {
                deleteProduct(product.id!!)
                Toast.makeText(
                    deleteButton.context,
                    "Producto eliminado. Para visualizar los cambios reinicia el programa",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            buyButton.setOnClickListener {
                shoppingCarList.add(product)
                val intent = Intent(it.context, ShoppingCarActivity::class.java)
                it.context.startActivity(intent)
            }


            if (editMode) {
                editButton.visibility = View.VISIBLE
                deleteButton.visibility = View.VISIBLE
            } else {
                editButton.visibility = View.INVISIBLE
                deleteButton.visibility = View.INVISIBLE
            }

        }
    }
}
/*
04142675407,04263178274,04242518845,04242341373,04126159110,04143772880,04241289215,04242148429,04143019021,04145877994,
        04242310596,04268214180,04166081383,04168030925,04267104744,04143882803,04241274035,04140231840,04141778260,04261112085
04264268150,04149162956,04141586204,04129800851,04144710471,04129509190,04262057286,04241606022,04242094205,04242127616,04241473712,04144902148,04129375791,04242116171,04127331601,04244594980,04125077163,04167222890,04169377420,04123643781

 */