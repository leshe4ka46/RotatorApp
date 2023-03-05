package com.leshe4ka.rotator

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.leshe4ka.rotator.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


var host="http://192.168.1.128/"
object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
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
data class RequestModelCoords(
    val key: String,
    val lat: Float,
    val lon: Float,
    val height: Float
)
data class RequestModelDorotate(
    val key: String,
    val value: Int
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
interface ApiInterfaceDorotateSet {
    @POST("api/v1/data/set/dorotate")
    fun sendReq(@Body requestModel: RequestModelDorotate) : Call<ResponseModel>
}
interface ApiInterfaceCoordsGet {
    @GET("api/v1/data/get/angles")
    fun getData() : Call<ResponseAngles>
}
data class ResponseAngles(
    val azimut: Float,
    val elevation: Float,
    val voltage: Float,
    val setted_azimut: Float,
    val setted_elevation: Float,
    val is_ready: Int,
    val delta_enabled: Int,
    val dorotate_enabled: Int
)

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

fun get_data(binding: ActivityMainBinding) {
    val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(ApiInterfaceCoordsGet::class.java)
    val call = service.getData()
    call.enqueue(object : Callback<ResponseAngles> {
        override fun onResponse(call: Call<ResponseAngles>, response: Response<ResponseAngles>) {
            if (response.code() == 200) {
                val dataResponse = response.body()!!
                binding.textViewAzimuth.text=dataResponse.azimut.toString()
                binding.textViewElevation.text=dataResponse.elevation.toString()
                binding.dorotateSwitch.isChecked=(dataResponse.dorotate_enabled==1)
            }
        }
        override fun onFailure(call: Call<ResponseAngles>, t: Throwable) {  Log.e("ROATATOR_APP", t.toString())          }
    })


}



/*
//not async
fun get_data():Array<String> {
    try {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val connection = URL(host + "api/v1/data/get/angles").openConnection() as HttpURLConnection
        val data = connection.inputStream.bufferedReader().readText()
        var resp: ResponseAngles = Gson().fromJson(data, ResponseAngles::class.java)
        return arrayOf(resp.azimut.toString(), resp.elevation.toString(),resp.dorotate_enabled.toString())
    }
    catch (e:Exception) {
        Log.e("ROTATOR_APP",e.toString());
        return arrayOf("-1","-1","0")
    }

}

 */
fun Boolean.toInt() = if (this) 1 else 0
fun post_dorotate(dorotate:Boolean) {
    var response = ServiceBuilder.buildService(ApiInterfaceDorotateSet::class.java)
    val requestModel = RequestModelDorotate("SATAPPSP",dorotate.toInt())
    response.sendReq(requestModel).enqueue(
        object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                //Snackbar.make(view,response.message().toString(),Snackbar.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.e("ROTATOR_APP",t.toString());
            }

        }
    )
}