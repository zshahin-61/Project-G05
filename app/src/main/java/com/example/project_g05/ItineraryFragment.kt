package com.example.project_g05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_g05.adapter.ItineraryListAdapter
import com.example.project_g05.databinding.FragmentItineraryBinding
import com.example.project_g05.models.Itinerary
import com.example.project_g05.models.NationalPark
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.Date


class ItineraryFragment : Fragment(R.layout.fragment_itinerary) {

    private val TAG: String = "Itinerary Fragmant"
    lateinit var itineraryListAdapter: ItineraryListAdapter

    // binding variables
    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!

    // TODO: safe args class property
    //private val args: ItineraryFragmentArgs by navArgs()


    // Initialize Firestore
    private val db = Firebase.firestore

    ///////
    private lateinit var itineraryAdapter: ItineraryListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        _binding = FragmentItineraryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var itineraryList = mutableListOf<Itinerary>()

        //////////////retrieve from db
        db.collection("itinerary")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
//                    val firstName = document.getString("first_name") ?: "N/A"
//                    val lastName = document.getString("last_name") ?: "N/A"
//                    val age = document.getLong("age") ?: "N/A"
                    itineraryList.add(
                        Itinerary(
                            document.getString("parkName") ?: "N/A",
                            document.getString("address") ?: "N/A",
                            document.getString("tripDate") ?: "N/A",
                            document.getString("notes") ?: "N/A"
                        )
                    )
                }
                itineraryAdapter = ItineraryListAdapter(itineraryList)
                binding.rvItinerary.adapter = itineraryAdapter
                binding.rvItinerary.layoutManager = LinearLayoutManager(requireContext())

            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.d(TAG, exception.message.toString())
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}