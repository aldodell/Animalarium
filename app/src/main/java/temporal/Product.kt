/*
package temporal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(

    @PrimaryKey
    @ColumnInfo(name = "uid")
    val uid: Int,

    @ColumnInfo(name = "image")
    val image: ByteArray?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "onPromotion")
    val onPromotion: Int,

    @ColumnInfo(name = "price")
    val price: String?


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (uid != other.uid) return false
        if (description != other.description) return false
        if (price != other.price) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (onPromotion != other.onPromotion) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + onPromotion
        return result
    }
}
*/