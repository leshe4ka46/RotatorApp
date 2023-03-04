package com.leshe4ka.rotator

import android.util.Log
import android.view.View
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.material.snackbar.Snackbar

var host="http://192.168.1.128/"
object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(host) //
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}
data class RequestModelAngles(
    val key: String,
    val azimut: Float,
    val elevation: Float,
)

//if (!cJSON_HasObjectItem(root,"key") || !cJSON_HasObjectItem(root,"lat") || !cJSON_HasObjectItem(root,"lon") || !cJSON_HasObjectItem(root,"height")){
//    /api/v1/data/set/homegps
//    /api/v1/data/set/satgps
data class RequestModelCoords(
    val key: String,
    val lat: Float,
    val lon: Float,
    val height: Float
)
data class ResponseModel(
    val message: String
)
interface ApiInterfaceAngles {
    @POST("api/v1/data/set/angles")
    fun sendReq(@Body requestModel: RequestModelAngles) : Call<ResponseModel>
}
interface ApiInterfaceCoordsSat {
    @POST("api/v1/data/set/satgps")
    fun sendReq(@Body requestModel: RequestModelCoords) : Call<ResponseModel>
}
interface ApiInterfaceCoordsHome {
    @POST("api/v1/data/set/homegps")
    fun sendReq(@Body requestModel: RequestModelCoords) : Call<ResponseModel>
}

fun post_angles(azimut:String,elevation:String,view: View) {
    val response = ServiceBuilder.buildService(ApiInterfaceAngles::class.java)
    val requestModel = RequestModelAngles("SATAPPSP",azimut.toFloat(),elevation.toFloat())
    response.sendReq(requestModel).enqueue(
        object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                Snackbar.make(view,response.message().toString(),Snackbar.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.e("ROTATOR_APP",t.toString())
                Snackbar.make(view,t.toString(),Snackbar.LENGTH_LONG).show()
            }

        }
    )
}

fun post_coords_sat(latitude:String,langitude:String,height: String,view: View) {
    var response = ServiceBuilder.buildService(ApiInterfaceCoordsSat::class.java)
    val requestModel = RequestModelCoords("SATAPPSP",latitude.toFloat(),langitude.toFloat(),height.toFloat())
    response.sendReq(requestModel).enqueue(
        object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                Snackbar.make(view,response.message().toString(),Snackbar.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Snackbar.make(view,t.toString(),Snackbar.LENGTH_LONG).show()
            }

        }
    )
}

fun post_coords_home(latitude:String,langitude:String,height: String,view: View) {
    var response = ServiceBuilder.buildService(ApiInterfaceCoordsHome::class.java)
    val requestModel = RequestModelCoords("SATAPPSP",latitude.toFloat(),langitude.toFloat(),height.toFloat())
    response.sendReq(requestModel).enqueue(
        object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                Snackbar.make(view,response.message().toString(),Snackbar.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Snackbar.make(view,t.toString(),Snackbar.LENGTH_LONG).show()
            }

        }
    )
}