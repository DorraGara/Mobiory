package com.example.mobiory.data.article

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.text.substring
import androidx.core.graphics.get
import androidx.navigation.NavHostController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mobiory.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface  ApiResponseListener{
    fun onResponseSuccess(navigator: NavHostController)
    fun onResponseError(navigator: NavHostController)
}


class Article() : ApiResponseListener{
    private var articleTitle: String = ""
    private var articleText: String = ""
    private var articleImageBitmap: ByteArray? = null
    private var textRequestDone: Boolean = false
    private var imageRequestDone: Boolean = false


    fun getArticle(textApiUrl: String, imageApiUrl: String, navigator: NavHostController, context: Context) {
        val outerThis = this
        GlobalScope.launch(Dispatchers.Main) { // Launch a coroutine in the main thread
            try {
                // Request for text content
                val textResponse = withContext(Dispatchers.IO) {
                    makeTextRequest(textApiUrl)
                }

                val (title, text) = handleTextApiResponse(textResponse)
                outerThis.articleTitle = title
                outerThis.articleText = text

                // Log the text after updating the property
                Log.e("Text Success", articleText)
                textRequestDone = true

                // Request for image content
                val imageResponse = withContext(Dispatchers.IO) {
                    makeImageRequest(imageApiUrl)
                }

                val bitmap = handleImageApiResponse(imageResponse)
                if (bitmap != null) {
                    outerThis.articleImageBitmap = bitmap

                    // Log the success
                    Log.e("Image Success", "Image loaded successfully")
                    //Log.e("test", (outerThis.articleImageBitmap != null).toString())
                    imageRequestDone = true
                    onResponseSuccess(navigator)
                }
            } catch (e: Exception) {
                Log.e("Error", e.message ?: "Unknown error")
                onResponseError(navigator)
            }
        }
    }


  data class Page(val title:String,val text:String,val image:ByteArray?)

    fun bitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun onResponseSuccess(navigator: NavHostController) {
        //val bitmap = this.articleImageBitmap?.let { bitmapToString(it) }
        if (textRequestDone && imageRequestDone) {
            val page =  Page(this.articleTitle, this.articleText,this.articleImageBitmap)
            navigator.currentBackStackEntry?.savedStateHandle?.apply {
                set("page_title", page.title)
                set("page_text", page.text)
               set("page_image", page.image)

            }
            navigator.navigate(Routes.Article.route)
        }

    }


    override fun onResponseError(navigator: NavHostController) {
        navigator.navigate(Routes.Home.route)
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

    private suspend fun handleImageApiResponse(response: JSONObject): ByteArray? {
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

    suspend fun loadImage(imageUrl: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as? HttpURLConnection
                connection?.doInput = true
                connection?.connect()
                val inputStream: InputStream? = connection?.inputStream
                inputStream?.readBytes()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    private suspend fun makeHttpRequest(urlString: String): String {
        return withContext(Dispatchers.IO) {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            try {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                response.toString()
            } finally {
                connection.disconnect()
            }
        }
    }

    suspend fun makeTextRequest(textApiUrl: String): JSONObject {
        val response = makeHttpRequest(textApiUrl)
        return JSONObject(response)
    }

    suspend fun makeImageRequest(imageApiUrl: String): JSONObject {
        val response = makeHttpRequest(imageApiUrl)
        return JSONObject(response)
    }
}


