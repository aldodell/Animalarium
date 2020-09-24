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


        val intent = getIntent()
        val ID = intent.getIntExtra("ID", -1)
        val action: Action = Action.valueOf(intent.getStringExtra("ACTION")!!)


        eng.query<Product> { it.record == ID }.firstOrNull()?.let { p ->
            etName.text.append(p.name)
            etPrice.text.append(p.price)
            etDescription.text.append(p.description)
            val bm = BitmapFactory.decodeByteArray(p.image, 0, p.image!!.size)
            ivLoader.setImageBitmap(bm)
        }


        btnSave.setOnClickListener {

            val bmos = ByteArrayOutputStream()
            val bm = ivLoader.drawable as BitmapDrawable
            bm.bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bmos)

            val p = Product()
            p.name = etName.text.toString()
            p.price = etPrice.text.toString()
            p.description = etDescription.text.toString()
            p.image = bmos.toByteArray()


            when (action) {

                Action.Add -> {
                    eng.newRecord(p)
                    Toast.makeText(this, "Producto nuevo guardado", Toast.LENGTH_SHORT).show()
                    this.finish()
                }

                Action.Update -> {
                    AlertDialog.Builder(this, R.style.AppTheme)
                        .setMessage("¿Deseas modificar el producto?")
                        .setPositiveButton("Sí") { _, _ ->
                            p.update()
                            eng.update(arrayOf(p).toList())
                            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("No") { _, _ ->
                            this.finish()
                        }
                        .create().show()
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