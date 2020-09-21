package org.philosophicas.animalarium

import android.os.AsyncTask
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


fun String.fromHttp(): String? {
    var result: String? = null
    val urlString = this
    runBlocking(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            val responseCode = con.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                var reader = BufferedReader(InputStreamReader(con.inputStream))
                var line: String?
                result = ""
                do {
                    line = reader.readLine()
                    if (line == null) break
                    result += line
                } while (true)
                con.inputStream.close()
                reader.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    return result
}

class FileDownloader : AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg arguments: String?): String {
        try {
            var url = URL(arguments[0])
            var con: HttpURLConnection = url.openConnection() as HttpURLConnection
            var responseCode = con.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                var file = FileOutputStream(arguments[1])
                con.inputStream.copyTo(file)
                con.inputStream.close()
                file.close()
                return "ok"
            }
        } catch (ex: Exception) {
            Log.e("FileDownloader", ex.message)
        }
        return ""
    }

}

