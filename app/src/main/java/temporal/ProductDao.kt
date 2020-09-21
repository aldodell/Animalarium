/*
package temporal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import temporal.Product

@Dao
interface ProductDao {
    @Query("select * from products")
    fun getAll(): List<Product>

    @Insert
    fun insertAll(vararg products: Product)

    @Delete
    fun delete(product: Product)

}
*/