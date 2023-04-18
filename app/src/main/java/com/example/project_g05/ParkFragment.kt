package com.example.project_g05

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project_g05.databinding.FragmentParkBinding
import com.example.project_g05.models.NationalPark
import com.example.project_g05.models.State
import com.example.project_g05.networking.ApiService
import com.example.project_g05.networking.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.example.project_g05.*
import kotlin.Function
import com.example.project_g05.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity

class ParkFragment : Fragment() {

     private var _binding: FragmentParkBinding? = null
    private lateinit var binding:  FragmentParkBinding
    private lateinit var spinner: Spinner
    private lateinit var button: Button
    private lateinit var apiKey: String
    private lateinit var apiService: ApiService
    private lateinit var mMap: GoogleMap
    private lateinit var parkList: List<NationalPark>
    private lateinit var stateList: List<State>
    private var selectedState: State? = null

    private val TAG = "Map_Park"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentParkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var  toast = Toast.makeText(requireActivity().applicationContext, "Screen Map", Toast.LENGTH_LONG)
        toast.show()
        Log.d(TAG, "We are in Map Screen")

        val spinner = view.findViewById<Spinner>(R.id.spinner)

        val states = State.values()
        val stateNames = states.map { it.fullName }
        // Initialize the Spinner adapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


// Initialize the Button
        button = view.findViewById(R.id.findParksButton)
        // Initialize the Button
        button = view.findViewById(R.id.findParksButton)
        button.setOnClickListener {
            // Get the selected state from the Spinner
            val selectedStateFullName = spinner.selectedItem as String
            val selectedState = states.firstOrNull { it.fullName == selectedStateFullName }

            // Show a Toast with the selected state
            if (selectedState != null) {
                Toast.makeText(
                    requireContext(),
                    "Selected state: ${selectedState.name} (${selectedState.abbreviation})",
                    Toast.LENGTH_SHORT
                ).show()

                // Get the park name for the selected state and show in Toast
                val selectedPark = parkList.firstOrNull { it.fullName == selectedState.name }
                if (selectedPark != null) {
                    Toast.makeText(
                        requireContext(),
                        "Park name: ${selectedPark.fullName}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No park found for the selected state",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }



    }

}