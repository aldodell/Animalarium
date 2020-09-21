package org.philosophicas.animalarium

import org.philosophicas.flumen.RecordBaseAnnotation
import org.philosophicas.flumen.DatumType
import org.philosophicas.flumen.RecordBase


class Product : RecordBase() {

    @property:RecordBaseAnnotation("/products/name", DatumType.String)
    var name: String? = null

    @property:RecordBaseAnnotation("/products/description", DatumType.String)
    var description: String? = null

    @property:RecordBaseAnnotation("/products/price", DatumType.String)
    var price: String? = null

    @property:RecordBaseAnnotation("/products/image", DatumType.Bytes)
    var image: ByteArray? = null
}
