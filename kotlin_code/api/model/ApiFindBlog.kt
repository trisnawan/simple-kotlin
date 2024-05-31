package com.trisnasejati.testingapps.api.model

import android.os.AsyncTask
import android.util.Log
import com.trisnasejati.testingapps.api.data.BlogData
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ApiFindBlog(private val callback: (MutableList<BlogData>) -> Unit) : AsyncTask<String, Void, MutableList<BlogData>>() {

    override fun doInBackground(vararg params: String?): MutableList<BlogData> {
        val result: MutableList<BlogData> = mutableListOf()
        try {

            // persiapan konek ke API
            val url = URL(params[0]) // ambil URL
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                // result bawaan
                val inputStream = urlConnection.inputStream

                // dari result bawaan di jadiin ke format JSON
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                // di ambilah dalam format JSONArray
                val jsonArray = JSONArray(jsonString)

                // di looping
                for (i in 0 until jsonArray.length()){

                    // di masukan ke dalam bentuk Objek
                    result.add(BlogData(
                        jsonArray.getJSONObject(i).getString("id"),
                        jsonArray.getJSONObject(i).getString("title"),
                        jsonArray.getJSONObject(i).getString("content"),
                        jsonArray.getJSONObject(i).getString("image"),
                    ))
                }
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    override fun onPostExecute(result: MutableList<BlogData>) {
        super.onPostExecute(result)
        callback(result)
    }

}