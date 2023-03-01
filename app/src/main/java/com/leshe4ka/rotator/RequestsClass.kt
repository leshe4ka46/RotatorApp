package com.leshe4ka.rotator

import android.util.Log
import org.json.JSONObject

/*
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient

import java.io.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.nio.charset.StandardCharsets

fun POST2(url: String?,data:String): String? {
    //data.toByteArray()
    Log.i("ROTATORLOG","0");
    val url = URL(url)
    Log.i("ROTATORLOG","1");
    val connection = url.openConnection() as HttpURLConnection
    Log.i("ROTATORLOG","2");
    connection.setRequestMethod("POST")
    connection.setRequestProperty("Content-Type", "application/json")
    connection.setRequestProperty("Accept", "application/json")
    connection.setRequestProperty("charset", "utf-8")
    connection.setDoOutput(true)
    connection.setInstanceFollowRedirects(false)
    Log.i("ROTATORLOG","3");
    DataOutputStream(connection.getOutputStream()).use { wr -> wr.write(data.toByteArray()) }
    Log.i("ROTATORLOG","4");
    return "0"
}*/

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.128/") //
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}
data class RequestModel(
    val key: String,
    val azimut: String,
    val elevation: String,
)
data class ResponseModel(
    val message: String
)
interface ApiInterface {
    @POST("api/v1/data/set/angles")
    fun sendReq(@Body requestModel: RequestModel) : Call<ResponseModel>
}

fun POST2(url: String?,azimut:String,elevation:String): String? {
    val response = ServiceBuilder.buildService(ApiInterface::class.java)
    val requestModel = RequestModel("SATAPPSP",azimut.toFloat().toString(),elevation.toFloat().toString())
    response.sendReq(requestModel).enqueue(
        object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                Log.i("ROTATORLOG",response.message().toString());
                //Toast.makeText(this@FirstFr,response.message().toString(),Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.e("ROTATORLOG",t.toString());
                //Toast.makeText(this@MainActivity,t.toString(),Toast.LENGTH_LONG).show()
            }

        }
    )

    return "0"
}




/*fun POST(url: String?,data:String?): String? {
    var inputStream: InputStream? = null
    var result: String? = ""
    try {
        Log.i("ROTATORLOG","0");
        val httpclient: HttpClient = DefaultHttpClient()
        Log.i("ROTATORLOG","1");
        val httpPost = HttpPost(url)
        val se = StringEntity(data)
        httpPost.setEntity(se)
        Log.i("ROTATORLOG","2");
        httpPost.setHeader("Accept", "application/json")
        httpPost.setHeader("Content-type", "application/json")
        Log.i("ROTATORLOG","3");
        val httpResponse: HttpResponse = httpclient.execute(httpPost)
        Log.i("ROTATORLOG","4");
        inputStream = httpResponse.getEntity().getContent()
        var result=""
        if (inputStream != null) result = convertInputStreamToString(inputStream) else result = "Did not work!"
    } catch (e: Exception) {
        Log.d("InputStream", e.localizedMessage)
    }

    // 11. return result
    return result
}*/
fun sendangles(azimut: String?,elevation: String?): String? {
    /*val jsonObject = JSONObject()
    jsonObject.accumulate("key", "SATAPPSP")
    jsonObject.accumulate("azimut", azimut)
    jsonObject.accumulate("elevation", elevation)
    val json = jsonObject.toString()*/
    //Log.i("ROTATORLOG1",json);
    if (azimut != null && elevation != null) {
        POST2("http://192.168.1.128/api/v1/data/set/angles",azimut,elevation)
    }
    return "0"
}