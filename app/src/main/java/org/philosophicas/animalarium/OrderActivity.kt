package org.philosophicas.animalarium

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.type.Date
import kotlinx.android.synthetic.main.activity_load_product.*
import kotlinx.android.synthetic.main.activity_order.*
import java.io.ByteArrayOutputStream

class OrderActivity : AppCompatActivity() {


    var PERMISSION_CODE = 0
    var IMAGE_PICK_CODE = 0


    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        try {
            var pid = this.getSharedPreferences("MySettings", Context.MODE_PRIVATE)
                .getString("personID", null)
            if (pid != null) {
                order_id.setText(pid)
                order_name.setText(this.getPreferences(Context.MODE_PRIVATE).getString("name", ""))
                order_lastname.setText(
                    this.getPreferences(Context.MODE_PRIVATE).getString("lastname", "")
                )
                order_email.setText(
                    this.getPreferences(Context.MODE_PRIVATE).getString("email", "")
                )
                order_phone.setText(
                    this.getPreferences(Context.MODE_PRIVATE).getString("phone", "")
                )
                order_address.setText(
                    this.getPreferences(Context.MODE_PRIVATE).getString("address", "")
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


        order_get_capture_button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            }
        }


        order_send_order_button.setOnClickListener {
            sendOrder()
        }


    }


    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            order_capture_imageview.setImageURI(data?.data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(
                        this,
                        "Usted ha denegado el permiso para acceder a la galería de imágenes.",
                        Toast.LENGTH_LONG
                    )
                }
            }
        }
    }


    fun sendOrder() {

        var ids = ""
        shoppingCarList.forEach { ids = "$ids${it.id}," }
        ids = ids.removeSuffix(",")


        val order = hashMapOf(
            "name" to order_name.text.toString(),
            "lastname" to order_lastname.text.toString(),
            "personID" to order_id.text.toString(),
            "email" to order_email.text.toString(),
            "phone" to order_phone.text.toString(),
            "products" to ids,
            "dateTime" to Timestamp.now(),
            "status" to Status.delivered.name
        )

        //Save preferences
        this.getSharedPreferences("MySettings", Context.MODE_PRIVATE).edit()
            .putString("personID", order_id.text.toString())
            .putString("email", order_email.text.toString())
            .putString("address", order_address.text.toString())
            .putString("name", order_name.text.toString())
            .putString("lastname", order_lastname.text.toString())
            .putString("phone", order_phone.text.toString())
            .apply()


        db.collection("orders").add(order)
            .addOnFailureListener {
                Toast.makeText(this.baseContext, "Excepción: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnSuccessListener {

                val bmos = ByteArrayOutputStream()
                val bm = order_capture_imageview.drawable as BitmapDrawable
                bm.bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bmos)
                val storageReference = FirebaseStorage.getInstance().reference
                val imageReference =
                    storageReference.child("orders/captures/" + it.id)
                imageReference.putBytes(bmos.toByteArray())
                bmos.close()
                Toast.makeText(this.baseContext, "Orden enviada", Toast.LENGTH_LONG)
                    .show()
                this.finish()
            }

    }


}