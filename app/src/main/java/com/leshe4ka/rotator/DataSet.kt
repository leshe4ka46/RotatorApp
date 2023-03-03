package com.leshe4ka.rotator

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*

import com.google.android.material.snackbar.Snackbar
import com.leshe4ka.rotator.databinding.DataSetBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
fun valid(str:Editable): Boolean {
    var l_str =str.toString()
    if (l_str!="" && l_str!="." && l_str!="null" && l_str!=null){
        return true
    }
    return false
}

val request = LocationRequest()
class DataSet : Fragment() {
private var _binding: DataSetBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var fusedLocationProvider: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = DataSetBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context!!)

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_dataset_to_SecondFragment)
        }*/
        binding.editTextAzimuth.setText("0");
        binding.editTextElevation.setText("0");
        binding.sendAngles.setOnClickListener { view
            if (valid(binding.editTextAzimuth.text) && valid(binding.editTextElevation.text)) {
                binding.textViewAzimuth.setText(binding.editTextAzimuth.text.toString())
                binding.textViewElevation.setText(binding.editTextElevation.text.toString())
                post_angles(binding.editTextAzimuth.text.toString(),binding.editTextElevation.text.toString(),view)
            }
            else{
                Snackbar.make(view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG).setAction("Закрыть", { }).show()
            }
        }
        binding.getGPS.setOnClickListener {view
            if (ActivityCompat.checkSelfPermission(context!!,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context!!,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProvider.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        binding.editTextHomeLatitude.setText(location?.latitude.toString())
                        binding.editTextHomeLongitude.setText(location?.longitude.toString())
                        if(location?.hashCode()==null){
                            Snackbar.make(view,"GPS выключен или нет данных", Snackbar.LENGTH_LONG).setAction("Открыть настройки", {
                                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }).show()
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
            }
        }
        binding.sendSatPos.setOnClickListener {view
            if(valid(binding.editTextSatLatitude.text) && valid(binding.editTextSatLongitude.text) && valid(binding.editTextSatHeight.text)){
                post_coords_sat(binding.editTextSatLatitude.text.toString(),binding.editTextSatLongitude.text.toString(),binding.editTextSatHeight.text.toString(),view)
            }
            else{
                Snackbar.make(view,"Надо хоть что-то написать", Snackbar.LENGTH_LONG).setAction("Закрыть", { }).show()
            }
        }

        binding.sendHomePos.setOnClickListener {view
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
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

