package com.example.mobiory.data.article

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.navigation.NavHostController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mobiory.ui.screens.Routes
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

interface  ApiResponseListener{
    fun onResponseSuccess(navigator: NavHostController)
    fun onResponseError(errorMsg: String)
}


class Article() : ApiResponseListener{
    private var articleTitle: String = ""
    private var articleText: String = ""
    private var articleImageBitmap: Bitmap? = null
    private var textRequestDone: Boolean = false
    private var imageRequestDone: Boolean = false


    fun getArticle(textApiUrl: String, imageApiUrl: String, navigator: NavHostController, context: Context)
    {


        // Request for text content
        val textRequest = JsonObjectRequest(
            Request.Method.GET, textApiUrl, null,
            { response ->
                try {
                    val (title, text) = handleTextApiResponse(response)
                    this.articleTitle = title
                    this.articleText = text

                    // Log the text after updating the property
                    Log.e("Text Success", articleText)
                    textRequestDone = true
                        onResponseSuccess(navigator)

                } catch (e: JSONException) {
                    Log.e("Text Error", e.message ?: "Unknown error")
                }
            },
            { error ->
                Log.e("Text Request Error", error.message ?: "Unknown error")
            })

        // Request for image content
        val imageRequest = JsonObjectRequest(
            Request.Method.GET, imageApiUrl, null,
            { response ->
                try {
                    val bitmap = handleImageApiResponse(response)
                    this.articleImageBitmap = bitmap

                    // Log the success
                    Log.e("Image Success", "Image loaded successfully")
                    imageRequestDone = true
                    onResponseSuccess(navigator)

                    // Now, navigate or perform any other action requiring article data

                } catch (e: JSONException) {
                    Log.e("Image Error", e.message ?: "Unknown error")
                }
            },
            { error ->
                Log.e("Image Request Error", error.message ?: "Unknown error")
            })

        // Add both requests to the request queue
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(textRequest)
        requestQueue.add(imageRequest)
    }


    override fun onResponseSuccess(navigator: NavHostController) {
        /*if (textRequestDone && imageRequestDone) {
            // Update article properties
            //articleViewModel.setArticleData(articleTitle, articleText, articleImageBitmap)
        }*/
        navigator.navigate(Routes.Article.route)
    }

    override fun onResponseError(errorMsg: String) {
        Log.e("Article", errorMsg)
    }






    private fun handleTextApiResponse(response: JSONObject): Pair<String, String> {
        val query = response.getJSONObject("query")
        val pages = query.getJSONArray("pages")
        if (pages.length() > 0) {
            val pageObject = pages.getJSONObject(0)
            val title = pageObject.getString("title")
            var extract = pageObject.getString("extract")
            extract = extract.replace(Regex("<.*?>"), "") // Remove HTML tags
            return title to extract
        } else {
            throw JSONException("No page found in response")
        }
    }

    private fun handleImageApiResponse(response: JSONObject): Bitmap? {
        val query = response.getJSONObject("query")
        val pages = query.getJSONArray("pages")
        if (pages.length() > 0) {
            val pageObject = pages.getJSONObject(0)
            if (pageObject.has("thumbnail")) {
                val thumbnail = pageObject.getJSONObject("thumbnail")
                val imageUrl = thumbnail.getString("source")
                return loadImage(imageUrl)
            }
        }
        return null
    }

    private fun loadImage(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


