package com.trisnasejati.testingapps.api.model

import android.os.AsyncTask
import com.trisnasejati.testingapps.api.data.BlogData
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ApiReadBlog(private val callback: (BlogData) -> Unit) : AsyncTask<String, Void, BlogData>() {

    override fun doInBackground(vararg params: String?): BlogData {
        var result = BlogData(null, null, null, null)
        try {
            val url = URL(params[0])
            val urlConnection = url.openConnection() as HttpURLConnection

            try {
                val inputStream = urlConnection.inputStream
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(jsonString)

                result = BlogData(
                    jsonObject.getString("id"),
                    jsonObject.getString("title"),
                    jsonObject.getString("content"),
                    jsonObject.getString("image"),
                )
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    override fun onPostExecute(result: BlogData) {
        super.onPostExecute(result)
        callback(result)
    }
}