package com.example.project_g05

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.project_g05.databinding.FragmentItineraryBinding
import com.example.project_g05.models.Itinerary
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EditItineraryFragment : Fragment(R.layout.fragment_edit_itinerary) {

    private val TAG: String = "Edit Itinerary Fragmant"

    // binding variables
    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!

    // TODO: safe args class property
    private val args: EditItineraryFragmentArgs by navArgs()


    // Initialize Firestore
    private val db = Firebase.firestore


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
                    itineraryList.add(
                        Itinerary(
                            document.getString("parkId") ?: "N/A",
                            document.getString("parkName") ?: "N/A",
                            document.getString("Address") ?: "N/A",
                            document.getString("tripDate") ?: "N/A",
                            document.getString("notes") ?: "N/A"
                        )
                    )
                }


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