package com.example.project_g05

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project_g05.databinding.FragmentParkBinding
import com.example.project_g05.models.NationalPark
import com.example.project_g05.models.State
import com.example.project_g05.networking.ApiService
import com.example.project_g05.networking.RetrofitInstance
import kotlinx.coroutines.launch
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.util.*
import android.location.*
import android.os.Build
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.maps.model.Marker


class ParkFragment : Fragment(), OnMapReadyCallback , LocationListener{

    // TODO: Class property for location manager
    private lateinit var locationManager: LocationManager

    private var _binding: FragmentParkBinding? = null
    private lateinit var binding: FragmentParkBinding
    private lateinit var spinner: Spinner
    private lateinit var button: Button
    private lateinit var apiKey: String
    private lateinit var apiService: ApiService
    private lateinit var mMap: GoogleMap
    // TODO: Class properties for permissions
    private val REQUEST_PERMISSION_CODE = 1234
    private val REQUIRED_PERMISSIONS_LIST
            = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)

    private lateinit var parkList: List<NationalPark>
    private lateinit var stateList: List<State>
    private var selectedState: State? = null

    private val TAG = "Map_Park"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentParkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toast =
            Toast.makeText(requireActivity().applicationContext, "Screen Map", Toast.LENGTH_LONG)
        toast.show()
        Log.d(TAG, "We are in Map Screen")
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager


        // Initialize the Spinner adapter

        spinner = view.findViewById(R.id.spinner)
        val states = State.values()
        val stateNames = mutableListOf("Select State")
        stateNames.addAll(states.map { it.fullName })
        // Initialize the Spinner adapter
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set item selection listener for spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Update the selected state based on the position
                selectedState = if (position > 0) states[position - 1] else null
               // selectedState = states[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
      //  Toast.makeText(requireContext(), "googleMap.${selectedState}. loading", Toast.LENGTH_SHORT).show()

        // setup the map
        val mapFragment = childFragmentManager.findFragmentById(binding.fragmentMap.id) as? SupportMapFragment

        if (mapFragment == null) {
            Log.d(TAG, "++++ map fragment is null")
        }
        else {
            Log.d(TAG, "++++ map fragment is NOT null")
            mapFragment?.getMapAsync(this)
            Toast.makeText(requireContext(), "googleMap.${this}. loading", Toast.LENGTH_SHORT).show()

        }


        // Find Parks button click listener
        binding.findParksButton.setOnClickListener {
            if (allPermissionsGranted() == true) {
                if (selectedState != null) {
                    Toast.makeText(requireContext(), "googleMap.${selectedState}. loading", Toast.LENGTH_SHORT).show()

                    findParks(selectedState!!)
                } else {
                    Toast.makeText(requireContext(), "Please choose a state", Toast.LENGTH_SHORT).show()
                }
            getDeviceCurrentLocation()
        } else {
            // The else executes if:
            // - this is the first time the user has installed the application
            // - this is the first time they're clicking on that GET LOCATION BUTTOn
            // - in the past, the user selected "only this time" in the permission popup box
            requestPermissions(REQUIRED_PERMISSIONS_LIST, REQUEST_PERMISSION_CODE)
        }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "+++ Map callback is executing...")
        this.mMap = googleMap
        Toast.makeText(requireContext(), "googleMap.${ this.mMap}. loading", Toast.LENGTH_SHORT).show()

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.isTrafficEnabled = true
        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true
       val intialLocation = LatLng(43.6426, -79.3871)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(intialLocation, 2.0f))
        // Set up marker click listener
        mMap.setOnMarkerClickListener { marker ->
            val park = parkList.find { it.fullName == marker.title }
            if (park != null) {
                val action = ParkFragmentDirections.actionParkFragmentToParkDetailsFragment(park)
                findNavController().navigate(action)
            }
            true
        }
        // Set up map click listener
    }

    private fun findParks(state: State) {
        // Hide soft keyboard
        hideSoftKeyboard()

        apiService = RetrofitInstance.retrofitService
        // Make API call to get parks by state
        lifecycleScope.launch {
            try {
                val response = apiService.getUsaNationalParksbyState(state.abbreviation)

                if (response.isSuccessful) {
                    val usaNationalParks = response.body()
                    parkList = usaNationalParks?.data ?: emptyList()
                    showParksOnMap(parkList)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch parks",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching parks: ${e.localizedMessage}")
                Toast.makeText(
                    requireContext(),
                    "Failed to fetch parks",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                // Hide progress bar

            }
        }
    }

    private fun showParksOnMap(parks: List<NationalPark>) {
        mMap.clear() // Clear existing markers on map
        val boundsBuilder = LatLngBounds.builder() // Builder to calculate bounds for camera update

        // Loop through each park and add marker to map
        for (park in parks) {
            val parkLatLng = LatLng(park.latitude.toDouble(), park.longitude.toDouble())
            mMap.addMarker(
                MarkerOptions()
                    .position(parkLatLng)
                    .title(park.fullName)
            )
            boundsBuilder.include(parkLatLng) // Include park LatLng in bounds calculation
        }

        // Animate camera to fit all markers within bounds
        val bounds = boundsBuilder.build()
        val padding = 10
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cameraUpdate)
    }

    private fun hideSoftKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // --------------------------------------
    // TODO: permissions helper functions
    // --------------------------------------

    fun allPermissionsGranted():Boolean {
        if ((ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            && (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            return true
        }
        else {
            return false
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            Log.d(TAG, "+++++++TODO Do something here.....")
            if (allPermissionsGranted() === true) {
                Log.d(TAG, "+++++++ User selected ALLOW (or Allow THis time")
                this.getDeviceCurrentLocation()
            }
            else {
                Log.d(TAG, "+++++++ User selected DENY")
                val snackbar = Snackbar.make(binding.fragmentMap, "FAILURE: No permissions granted", Snackbar.LENGTH_SHORT)
                snackbar.show()

            }

        }

    }

    // --------------------------------------
    // TODO: location helper functions
    // --------------------------------------
    @SuppressLint("MissingPermission")
    private fun getDeviceCurrentLocation() {
        Log.d(TAG, "+++++ Attemping to get location")
        // initialize the location manager class property
        this.locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // subscribe to location updates
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }
    // Function to request location permissions
    private fun requestLocationUpdates() {
        requestPermissions(
            REQUIRED_PERMISSIONS_LIST,
            REQUEST_PERMISSION_CODE
        )
    }
    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "+++ Location update detected")
        val lat = location.latitude
        val lng = location.longitude
      //binding.tvCoordinates.text = "Latitude: ${lat}, Longitude: ${lng}"
        Toast.makeText(requireContext(), "googleMap.${ this.mMap}. loading", Toast.LENGTH_SHORT).show()
        Log.d(TAG,  "+++ Latitude: ${lat}, Longitude: ${lng}")

        // get the human readable addres (geocoding)
        val mGeocoder:Geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addressResultsList = mGeocoder.getFromLocation(lat, lng, 1)
        if (addressResultsList == null || addressResultsList.size == 0) {
            Log.d(TAG, "No matching addresses found")
          //  binding.tvCoordinates.text = "No matching address found."

        }
        else {
            val currAddress:Address = addressResultsList.get(0)
            Log.d(TAG, "Address: ${currAddress.getAddressLine(0)}")
            Log.d(TAG, "Address: ${currAddress.locality}")
            Log.d(TAG, "Address: ${currAddress.countryName}")

            // output to the ui
           // binding.tvCoordinates.text = "Address: ${currAddress.getAddressLine(0)}"
        }


        // move the map to the person's current location
        // add a marker
        val userLocationAsCoordinate = LatLng(lat, lng)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocationAsCoordinate, 2.0f))
        // remove any pre-existing markers
        mMap.clear()
        // add a new marker on the user's position
        mMap.addMarker(MarkerOptions().position(userLocationAsCoordinate).title("You are here"))
    }


}

