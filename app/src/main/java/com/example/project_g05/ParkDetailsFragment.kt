package com.example.project_g05

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.project_g05.databinding.FragmentParkDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ParkDetailsFragment : Fragment(R.layout.fragment_park_details) {
    private val TAG:String = "Park Details"

    // binding variables
    private var _binding: FragmentParkDetailsBinding? = null
    private val binding get() = _binding!!

    // TODO: safe args class property
    private val args: ParkDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        _binding = FragmentParkDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = args.argsFromPark?.fullName
        binding.tvDescription.text = args.argsFromPark?.description

        var strAddress=""
        for(address in args.argsFromPark!!.addresses)
            strAddress+="$address \n"
        binding.tvAddress.text = strAddress

        binding.tvWebsite.loadUrl(args.argsFromPark!!.url)


        val imageView: ImageView = binding.ivImage

        Glide.with(requireContext())
            .load(args.argsFromPark?.images)
            .into(imageView)


        Log.d(TAG, "Selected park from screen #1: ${args.argsFromPark}")

        binding.btnAddItinerary.setOnClickListener(){

            val db = Firebase.firestore
            // Create a new document in Firestore
            val itinerary = hashMapOf(
                "parkName" to binding.tvTitle,
                "address" to binding.tvAddress,
                "tripDate" to "",
                "notes" to ""
            )
            // Add the document to the "users" collection
            db.collection("itinerary")
                .add(itinerary)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Document was successfully added")
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, exception.message.toString())
                }


            val action = ParkDetailsFragmentDirections.actionParkDetailsFragmentToItineraryFragment(binding.tvTitle.text.toString(),
               binding.tvAddress.text.toString())

            findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}