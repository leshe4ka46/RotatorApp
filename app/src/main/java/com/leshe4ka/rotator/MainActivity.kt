package com.leshe4ka.rotator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.leshe4ka.rotator.databinding.ActivityMainBinding
import android.provider.Settings
fun valid(str: Editable): Boolean {
    var l_str =str.toString()
    if (l_str!="" && l_str!="." && l_str!="null" && l_str!=null){
        return true
    }
    return false
}

val request = LocationRequest()
private val toolbar: Toolbar? = null
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var fusedLocationProvider: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val view: View = findViewById(android.R.id.content)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(findViewById(R.id.toolbar))
        /*  top navigation text */
        /*val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        binding.editTextAzimuth.setText("0");
        binding.editTextElevation.setText("0");
        binding.sendAngles.setOnClickListener {
            if (valid(binding.editTextAzimuth.text) && valid(binding.editTextElevation.text)) {
                binding.textViewAzimuth.text = binding.editTextAzimuth.text.toString()
                binding.textViewElevation.text = binding.editTextElevation.text.toString()
                post_angles(binding.editTextAzimuth.text.toString(),binding.editTextElevation.text.toString(),view)
            }
            else{
                Snackbar.make(view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG).setAction("Закрыть", { }).show()
            }
        }
        binding.getGPS.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProvider.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        binding.editTextHomeLatitude.setText(location?.latitude.toString())
                        binding.editTextHomeLongitude.setText(location?.longitude.toString())
                        if(location?.hashCode()==null){
                            Snackbar.make(view,"GPS выключен, нет данных", Snackbar.LENGTH_LONG).setAction("Открыть настройки") {
                                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }.show()
                            fusedLocationProvider.requestLocationUpdates(request, object : LocationCallback() {
                                override fun onLocationResult(locationResult: LocationResult) {
                                    val location: Location? = locationResult.lastLocation
                                    binding.editTextHomeLatitude.setText(location?.latitude.toString())
                                    binding.editTextHomeLongitude.setText(location?.longitude.toString())
                                }
                            }, null)

                        }
                    }
            }
            else{
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),Priority.PRIORITY_HIGH_ACCURACY)
                Snackbar.make(view,"Необходимо разрешение", Snackbar.LENGTH_LONG).setAction("Открыть настройки") {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }.show()
            }
        }
        binding.sendSatPos.setOnClickListener {
            if(valid(binding.editTextSatLatitude.text) && valid(binding.editTextSatLongitude.text) && valid(binding.editTextSatHeight.text)){
                post_coords_sat(binding.editTextSatLatitude.text.toString(),binding.editTextSatLongitude.text.toString(),binding.editTextSatHeight.text.toString(),view)
            }
            else{
                Snackbar.make(view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG).setAction("Закрыть", { }).show()
            }
        }

        binding.sendHomePos.setOnClickListener {
            if(valid(binding.editTextHomeLatitude.text) && valid(binding.editTextHomeLongitude.text) && valid(binding.editTextHomeHeight.text)){
                post_coords_home(binding.editTextHomeLatitude.text.toString(),binding.editTextHomeLongitude.text.toString(),binding.editTextHomeHeight.text.toString(),view)
            }
            else{
                Snackbar.make(view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG).setAction("Закрыть", { }).show()
            }
        }

    }
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //thetext.text = ("" + location.longitude + ":" + location.latitude)
        }
    }
override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}