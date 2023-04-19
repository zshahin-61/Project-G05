package com.example.project_g05

import android.content.Context
import android.content.pm.PackageManager
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class ParkFragment : Fragment(), OnMapReadyCallback {

    // TODO: Class property for location manager
    private lateinit var locationManager: LocationManager

    private var _binding: FragmentParkBinding? = null
    private lateinit var binding: FragmentParkBinding
    private lateinit var spinner: Spinner
    private lateinit var button: Button
    private lateinit var apiKey: String
    private lateinit var apiService: ApiService
    private lateinit var mMap: GoogleMap

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

        spinner = view.findViewById(R.id.spinner)
        val states = State.values()
        val stateNames = states.map { it.fullName }
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
                selectedState = states[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // setup the map
        val mapFragment = childFragmentManager.findFragmentById(binding.mapView.id) as? SupportMapFragment

        if (mapFragment == null) {
            Log.d(TAG, "++++ map fragment is null")
        }
        else {
            Log.d(TAG, "++++ map fragment is NOT null")
            mapFragment.getMapAsync(this)
        }


        // Find Parks button click listener
        binding.findParksButton.setOnClickListener {
            if (selectedState != null) {
                findParks(selectedState!!)
            } else {
                Toast.makeText(requireContext(), "Please choose a state", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun allPermissionsGranted():Boolean {
        if ((ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            && (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            return true
        }
        else {
            return false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "+++ Map callback is executing...")
        this.mMap = googleMap
        Toast.makeText(requireContext(), "googleMap loading", Toast.LENGTH_SHORT).show()

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
            val parkLatLng = LatLng(park.latitude, park.longitude)
            mMap.addMarker(
                MarkerOptions()
                    .position(parkLatLng)
                    .title(park.fullName)
            )
            boundsBuilder.include(parkLatLng) // Include park LatLng in bounds calculation
        }

        // Animate camera to fit all markers within bounds
        val bounds = boundsBuilder.build()
        val padding = 200
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


}

