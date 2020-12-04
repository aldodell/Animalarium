package org.philosophicas.animalariummanager

class ProductModel {
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var price: String? = null
    var image: String? = null

    val toHashMap: HashMap<String, Any?>
        get() = hashMapOf(
            "name" to name,
            "description" to description,
            "price" to price,
            "image" to image
        )


}

var products = ArrayList<ProductModel>()
