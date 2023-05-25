package com.leshe4ka.rotator
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.leshe4ka.rotator.databinding.ActivityMainBinding
import com.leshe4ka.rotator.databinding.JoyActivityBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


var host="http://192.168.4.1"
object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()
    var gsonBuilder: GsonBuilder = GsonBuilder().setLenient()
    var gson: Gson = gsonBuilder.create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}

object AltitudeBuilder {
    private val client = OkHttpClient.Builder().build()
    var gsonBuilder: GsonBuilder = GsonBuilder().setLenient()
    var gson: Gson = gsonBuilder.create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.opentopodata.org")
        .addConverterFactory(GsonConverterFactory.create(gson))
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
data class RequestModelJoystick(
    val key: String,
    val axis: Int,
    var angle: Float,
    var reset: Int
)
data class RequestModelAltitude(
    val locations: String
)
data class ResponseModel(
    val message: String
)
data class ResponseResult(
    val elevation: Float
)
data class ResponseAltitude(
    val results: Array<ResponseResult>
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

interface ApiInterfaceJoystickSet {
    @POST("/api/v1/data/set/delta")
    fun sendReq(@Body requestModel: RequestModelJoystick) : Call<ResponseModel>
}
interface ApiInterfaceResetSet{
    @POST("api/v1/reset/as5600/")
    fun sendReq(@Body requestModel: RequestModelDorotate) : Call<ResponseModel>
}
interface ApiInterfaceCoordsGet {
    @GET("api/v1/data/get/angles")
    fun getData() : Call<ResponseAngles>
}
interface ApiInterfaceJoyCoordsGet {
    @GET("/api/v1/data/get/joyangles")
    fun getData() : Call<ResponseAnglesJoy>
}
interface ApiInterfaceGetAltitude {
    @POST("v1/mapzen")
    fun sendReq(@Body requestModel: RequestModelAltitude) : Call<ResponseAltitude>
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
data class ResponseAnglesJoy(
    val azimut: Float,
    val elevation: Float,
    val deltajoyazimut: Float,
    val deltajoyelevation: Float
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
                Log.e("ROTATOR_APP",t.toString())
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
                Log.e("ROTATOR_APP",t.toString())
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
                binding.textViewAzimuth.text="%.4f".format(dataResponse.azimut)
                binding.textViewElevation.text="%.4f".format(dataResponse.elevation)
                binding.dorotateSwitch.isChecked=(dataResponse.dorotate_enabled==1)
            }
        }
        override fun onFailure(call: Call<ResponseAngles>, t: Throwable) {  Log.e("ROATATOR_APP", t.toString())          }
    })
}

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

fun post_reset() {

    var response = ServiceBuilder.buildService(ApiInterfaceResetSet::class.java)
    val requestModel = RequestModelDorotate("SATAPPSP",0)
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

fun post_joystick(axis:Int,angle:Float,reset:Int) {
    var response = ServiceBuilder.buildService(ApiInterfaceJoystickSet::class.java)
    val requestModel = RequestModelJoystick("SATAPPSP",axis,angle,reset)
    response.sendReq(requestModel).enqueue(
        object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
            }
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.e("ROTATOR_APP",t.toString())
            }

        }
    )
}

fun get_altitude(binding: ActivityMainBinding,view: View,latitude: String,longitude: String) {
    var response = AltitudeBuilder.buildService(ApiInterfaceGetAltitude::class.java)
    val requestModel = RequestModelAltitude(latitude+","+longitude)
    response.sendReq(requestModel).enqueue(
        object : Callback<ResponseAltitude> {
            override fun onResponse(
                call: Call<ResponseAltitude>,
                response: Response<ResponseAltitude>
            ) {
                if (response.code() == 200) {
                    val dataResponse = response.body()!!
                    binding.editTextHomeHeight.setText(dataResponse.results.first().elevation.toString())
                }
            }
            override fun onFailure(call: Call<ResponseAltitude>, t: Throwable) {
                Log.e("ROTATOR_APP",t.toString())
                if ("Unable to resolve" in t.message.toString()){
                    Snackbar.make(view,"Нет подключения к интернету для получения высоты над уровнем моря",Snackbar.LENGTH_LONG).show()
                }
            }

        }
    )
}

fun get_data_joystick(binding: JoyActivityBinding) {
    val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(ApiInterfaceJoyCoordsGet::class.java)
    val call = service.getData()
    call.enqueue(object : Callback<ResponseAnglesJoy> {
        @SuppressLint("SetTextI18n")
        override fun onResponse(call: Call<ResponseAnglesJoy>, response: Response<ResponseAnglesJoy>) {
            if (response.code() == 200) {
                val dataResponse = response.body()!!
                binding.textViewJoyAzimuth.text="%.4f".format(dataResponse.azimut)
                binding.textViewJoyElevation.text="%.4f".format(dataResponse.elevation)
                binding.textViewJoyAzimuthCorrect.text="%.4f".format(dataResponse.azimut+dataResponse.deltajoyazimut)
                binding.textViewJoyElevationCorrect.text="%.4f".format(dataResponse.elevation+dataResponse.deltajoyelevation)
            }
        }
        override fun onFailure(call: Call<ResponseAnglesJoy>, t: Throwable) {
            Log.e("ROATATOR_APP", t.toString())
        }
    })
}
