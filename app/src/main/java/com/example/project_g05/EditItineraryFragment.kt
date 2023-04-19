package com.example.project_g05

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_g05.databinding.FragmentEditItineraryBinding
import com.example.project_g05.models.Itinerary
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.internal.wait


class EditItineraryFragment : Fragment(R.layout.fragment_edit_itinerary) {

    private val TAG: String = "Edit Itinerary Fragmant"

    // binding variables
    private var _binding: FragmentEditItineraryBinding? = null
    private val binding get() = _binding!!

    // TODO: safe args class property
    private val args: EditItineraryFragmentArgs by navArgs()


    // Initialize Firestore
    private val db = Firebase.firestore

    //////
    private lateinit var database: DatabaseReference
    private val firestore = FirebaseFirestore.getInstance()
    private val itineraryCollection = firestore.collection("itinerary")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        _binding = FragmentEditItineraryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text=args.argsSelectedItinerary.parkName
        binding.etNOtes.setText(args.argsSelectedItinerary.notes)
        binding.etDate.setText(args.argsSelectedItinerary.tripDate)


        // Update button click listener
        binding.btnSave.setOnClickListener {
            updateItinerary(args.argsSelectedItinerary)
        }

        // Update button click listener
        binding.btnDelete.setOnClickListener {
            deleteItinerary(args.argsSelectedItinerary.parkId)
        }

    }

    private fun updateItinerary(iti:Itinerary) {
        val itineraryId = iti.parkId // Replace with the actual ID of the itinerary to update
        val parkName = iti.parkName // Replace with the updated park name
        val address = iti.address // Replace with the updated address
        val updatedTripDate = binding.etDate.text.toString() // Replace with the updated trip date
        val updatedNotes = binding.etNOtes.text.toString() // Replace with the updated notes


        Log.d(TAG, "itineraryId is $itineraryId")
        Log.d(TAG, "updatedParkName is $parkName")
        Log.d(TAG, "updatedAddress is $address")
        Log.d(TAG, "updatedTripDate is $updatedTripDate")
        Log.d(TAG, "updatedNotes is $updatedNotes")


        // Create a map to hold the updated data
        val updatedData = mapOf(
            "parkName" to parkName,
            "address" to address,
            "tripDate" to updatedTripDate,
            "notes" to updatedNotes
        )

        // Update the document in Firebase Firestore
        itineraryCollection.document(itineraryId).update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Successfully Updated", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Successfully Updated")
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Update failure, handle error")
            }
    }

    private fun deleteItinerary(documentId: String) {

        itineraryCollection.document(documentId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Successfully Deleted", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Successfully Deleted")

                val action = EditItineraryFragmentDirections.actionEditItineraryFragmentToItineraryFragment()
                findNavController().navigate(action)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Delete failure, handle error")
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}