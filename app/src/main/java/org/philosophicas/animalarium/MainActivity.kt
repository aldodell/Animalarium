package org.philosophicas.animalarium

//import temporal.AppDatabase
//import temporal.Product
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.philosophicas.flumen.Engine
import org.philosophicas.flumen.Status

private var dataBaseFileName = "animalarium.db"
lateinit var eng: Engine

class MainActivity : AppCompatActivity() {

    // lateinit var db: AppDatabase
    /*
       private var urlRemoteDatabase =
           "https://raw.githubusercontent.com/aldodell/animalarium_data/master/animalarium.db"
       private var urlRemoteDatabaseVersion =
           "https://raw.githubusercontent.com/aldodell/animalarium_data/master/db_version"
   */


    private var urlRemoteDatabase =
        "http://psiqueylogos-ac.com/animalarium/animalarium.db"
    private var urlRemoteDatabaseVersion =
        "http://psiqueylogos-ac.com/animalarium/db_version"


    //http://psiqueylogos-ac.com/animalarium/

    private var dbLocalVersion = 0
    private var dbRemoteVersion = 0
    private val PREFERENCE_NAME = "preferences"
    private val PRIVATE_MODE = 0
    private val downloader = FileDownloader()
    private lateinit var recyclerView: RecyclerView
    private lateinit var products: List<Product>
    private lateinit var searcher: SearchView


/*
    private fun XdownloadData(context: Context) {
        // runBlocking(Dispatchers.IO) {
        val tmpDatabasePath =
            context.getExternalFilesDir(null)!!.absolutePath + "/${dataBaseFileName}"

        //Get local database version
        dbLocalVersion =
            context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE).getInt("db_version", 0)

        //Download database version
        dbRemoteVersion = urlRemoteDatabaseVersion.fromHttp()?.toIntOrNull() ?: 0

        //Get remote file
        downloader.execute(urlRemoteDatabase, tmpDatabasePath)

        //Create Database with this file
        val file = File(tmpDatabasePath)

        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "products"
        )
            .createFromFile(file)
            .build()

        //file.deleteOnExit()

    }
*/


    /*
    private fun downloadData(context: Context) {
        runBlocking(Dispatchers.IO) {
            val tmpDatabasePath =
                context.getExternalFilesDir(null)!!.absolutePath + "/${dataBaseFileName}"

            //Get local database version
            dbLocalVersion =
                context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE).getInt("db_version", 0)

            //Download database version
            dbRemoteVersion = urlRemoteDatabaseVersion.fromHttp()?.toIntOrNull() ?: 0

            //Get remote file
            downloader.execute(urlRemoteDatabase, tmpDatabasePath)

            //Create Database with this file
            val file = File(tmpDatabasePath)

            //file.deleteOnExit()

        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // downloadData(this.applicationContext)

        eng = Engine(this, dataBaseFileName)
        products = eng.query(null)


        val product = products.first()
        product.name = "Gatarina"
        product.update()
        eng.update(arrayOf(product).toList())


        /*
         products = eng.query(null)
         val product = products.first()
         product.remove()
         eng.update(arrayOf(product).toList())
 */

        searcher = findViewById(R.id.main_search_view)
        recyclerView = findViewById(R.id.main_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)



        runBlocking(Dispatchers.IO) {
            //products = db.productDao().getAll()
            recyclerView.adapter = MainRecyclerViewAdapter(products)
            recyclerView.adapter!!.notifyDataSetChanged()
        }



        searcher.setOnSearchClickListener {
            it as SearchView
//            recyclerView.adapter!!.notifyDataSetChanged()
        }

        searcher.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 == "cargar") {
                    var intent = Intent(this@MainActivity, LoadProductActivity::class.java)
                    this@MainActivity.startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        //Link whatsapp button
        ib_whatsapp.setOnClickListener {
            //val url = "https://api.whatsapp.com/send?phone=$number"
            val url = "https://wa.me/c/584129169520"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        //Link twitter button
        ib_twitter.setOnClickListener {

            val url = "https://twitter.com/Animalarium_VE?s=09"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

    }

}