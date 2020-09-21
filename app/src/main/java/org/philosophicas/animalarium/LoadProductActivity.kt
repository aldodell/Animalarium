package org.philosophicas.animalarium

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_load_product.*
import org.philosophicas.flumen.Datum
import java.io.ByteArrayOutputStream


class LoadProductActivity : AppCompatActivity() {

    val PERMISSION_CODE = 1000
    val IMAGE_PICK_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_product)

        btnSave.setOnClickListener {

            var bmos = ByteArrayOutputStream()
            var bm = ivLoader.drawable as BitmapDrawable
            bm.bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bmos)

            var p = Product()
            p.name = etName.text.toString()
            p.price = etPrice.text.toString()
            p.description = etDescription.text.toString()
            p.image = bmos.toByteArray()


            eng.newRecord(p)

            Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show()
            this.finish()

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