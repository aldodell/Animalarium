package org.philosophicas.animalariummanager

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class ProductsAdapter(var mainActivity: MainActivity) :
    RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {

    class ProductHolder(itemView: View, var mainActivity: MainActivity) :
        RecyclerView.ViewHolder(itemView) {

        var name = itemView.findViewById<EditText>(R.id.et_name)
        var description = itemView.findViewById<EditText>(R.id.et_description)
        var price = itemView.findViewById<EditText>(R.id.et_price)
        var btn_save = itemView.findViewById<Button>(R.id.btn_save)
        var btn_delete = itemView.findViewById<Button>(R.id.btn_delete)
        var btnPickImage = itemView.findViewById<ImageButton>(R.id.imageButton)
        private val db = FirebaseFirestore.getInstance()
        val storageReference = FirebaseStorage.getInstance().reference


        fun bind(position: Int) {
            val product = products[position]

            name.setText(product.name)
            description.setText(product.description)
            price.setText(product.price)

            if (product.image != null) {
                val img = File(itemView.context.filesDir, product.image!!)
                if (img.exists()) {
                    btnPickImage.setImageURI(Uri.fromFile(img))
                }
            }


            btn_save.setOnClickListener {
                product.name = name.text.toString()
                product.description = description.text.toString()
                product.price = price.text.toString()

                db.collection("products")
                    .document(product.id!!)
                    .update(product.toHashMap)
                    .addOnSuccessListener {
                        products.remove(products.first { it.id == product.id })
                        products.add(0, product)
                        mainActivity.updateUI()
                        Toast.makeText(itemView.context, "¡Guardado!", Toast.LENGTH_SHORT)
                            .show()
                    }

            }

            btn_delete.setOnClickListener {
                db.collection("products")
                    .document(product.id!!)
                    .delete()
                    .addOnSuccessListener {
                        products.remove(products.first { it.id == product.id })
                        mainActivity.updateUI()
                        Toast.makeText(itemView.context, "¡Eliminado!", Toast.LENGTH_SHORT)
                            .show()


                    }
            }

            btnPickImage.setOnClickListener {
                mainActivity.pickImage { uri ->
                    if (uri != null) {
                        btnPickImage.setImageURI(uri)
                        saveImage(position)
                    }
                }
            }
        }

        private fun saveImage(position: Int) {
            val product = products[position]
            val imageID = UUID.randomUUID()!!.toString()
            product.image = imageID
            products[position] = product

            val imageReference =
                storageReference.child("products/images/$imageID")
            val bmos = ByteArrayOutputStream()
            val bm = btnPickImage.drawable as BitmapDrawable
            bm.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bmos)
            imageReference.putBytes(bmos.toByteArray()).addOnSuccessListener {
                db.collection("products")
                    .document(product.id!!)
                    .update(product.toHashMap)
                    .addOnSuccessListener {
                        Toast.makeText(itemView.context, "¡Imagen y producto guardado!", Toast.LENGTH_SHORT)
                            .show()
                    }

            }

            val img = File(itemView.context.filesDir, imageID)
            img.writeBytes(bmos.toByteArray())
            bmos.close()

            mainActivity.updateUI()
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder =
        ProductHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false),
            mainActivity
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
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(position)
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = products.size


}