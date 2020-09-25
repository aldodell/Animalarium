package org.philosophicas.animalarium

import android.graphics.BitmapFactory
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var image: ImageView = itemView.findViewById(R.id.product_image)
    var description: TextView = itemView.findViewById(R.id.product_description)
    var price: TextView = itemView.findViewById(R.id.product_price)
    var name: TextView = itemView.findViewById(R.id.product_name)
    var editButton: Button = itemView.findViewById(R.id.product_edit_button)
    var deleteButton: Button = itemView.findViewById(R.id.product_delete_button)

    fun bind(product: ProductModel) {
        name.setText(product.name)
        description.setText(product.description)
        price.setText(product.price)
        if (product.image != null) {
            val bitmap = BitmapFactory.decodeByteArray(product.image, 0, product.image!!.size)
            image.setImageBitmap(bitmap)
        }
    }

}