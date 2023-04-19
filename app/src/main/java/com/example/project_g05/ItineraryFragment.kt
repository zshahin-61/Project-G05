package com.example.project_g05

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.project_g05.adapter.ItineraryListAdapter
import com.example.project_g05.databinding.FragmentItineraryBinding
import com.example.project_g05.models.Itinerary
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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
                            document.id,
                            document.getString("parkName") ?: "",
                            document.getString("address") ?: "",
                            document.getString("tripDate") ?: "",
                            document.getString("notes") ?: ""
                        )
                    )
                }
                itineraryAdapter = ItineraryListAdapter(requireContext(),itineraryList)
                binding.lvItinerary.adapter = itineraryAdapter
                //binding.lvItinerary.layoutManager = LinearLayoutManager(requireContext())

            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.d(TAG, exception.message.toString())
            }


            binding.lvItinerary.setOnItemClickListener { adapterView, view, i, l ->
            //Log.d(TAG, "Row clicked is: ${i}")
            // create an association between the row that was clicked & the user id of the person you want to retrieve
            // userid = row# + 1
            //val idToRetrieve = i+1
            val action = ItineraryFragmentDirections.actionItineraryFragmentToEditItineraryFragment(itineraryList[i])
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}