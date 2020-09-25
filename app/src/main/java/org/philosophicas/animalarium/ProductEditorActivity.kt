package org.philosophicas.animalarium

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_load_product.*
import java.io.ByteArrayOutputStream


class ProductEditorActivity : AppCompatActivity() {

    companion object {
        enum class Action(val ini: String) {
            Add("ADD"),
            Update("UPDATE"),
            Delete("DELETE")
        }

    }

    val PERMISSION_CODE = 1000
    val IMAGE_PICK_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_product)

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()


        val intent = getIntent()
        val ID = intent.getIntExtra("ID", -1)
        val action: Action = Action.valueOf(intent.getStringExtra("ACTION")!!)

        btnSave.setOnClickListener {
            val storageReference = FirebaseStorage.getInstance().reference
            val bmos = ByteArrayOutputStream()
            val bm = ivLoader.drawable as BitmapDrawable
            bm.bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bmos)

            storageReference.putBytes(bmos.toByteArray())
            val bucket = storageReference.bucket


            val product = hashMapOf(
                "name" to etName.text.toString(),
                "description" to etDescription.text.toString(),
                "price" to etPrice.text.toString(),
                "image" to bucket
            )

            if (action == Action.Add) {

                db.collection("products")
                    .add(product)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Error agregado producto:" + it.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
            }
        }

        btnPickImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            }
        }


    }


    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            ivLoader.setImageURI(data?.data)
        }
    }

}