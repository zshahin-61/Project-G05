package com.example.project_g05

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.example.project_g05.adapter.ImageAdapter
import com.example.project_g05.databinding.FragmentParkDetailsBinding
import com.example.project_g05.models.AddressModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ParkDetailsFragment : Fragment(R.layout.fragment_park_details) {
    private val TAG:String = "Park Details"

    // binding variables
    private var _binding: FragmentParkDetailsBinding? = null
    private val binding get() = _binding!!

    // TODO: safe args class property
    private val args: ParkDetailsFragmentArgs by navArgs()
////////
var recyclerView: RecyclerView? = null
    var Manager: GridLayoutManager? = null
    var adapter: ImageAdapter? = null

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

        //Log.d(TAG,args.argsFromPark!!.fullName)
        binding.tvTitle.text = args.argsFromPark?.fullName?:""
        binding.tvDescription.text = args.argsFromPark?.description?:""

        var strAddress=""

        if(args.argsFromPark!= null) {
            var address: AddressModel = args.argsFromPark!!.addresses[0]
            if (address.street != null)
                strAddress += "street: ${address.street ?: ""},"
            if (address.city != null)
                strAddress += "city: ${address.city ?: ""},"
            if (address.state != null)
                strAddress += "state: ${address.state ?: ""},"
            if (address.postalCode != null)
                strAddress += "postalCode: ${address.postalCode ?: ""},"
            if (address.country != null)
                strAddress += "country: ${address.country ?: ""}"
        }
        binding.tvAddress.text = strAddress

        binding.tvWebsite.text=  args.argsFromPark!!.url
        //val imageView: ImageView = binding.ivImage

        if(args.argsFromPark!=null) {
            Glide.with(requireContext())
                .load(args.argsFromPark!!.images[0].url)
                .into(binding.ivImage)
        }

//        recyclerView = binding.rvDesign //.findViewById<View>(R.id.rv_design) as RecyclerView
//        Manager = GridLayoutManager(requireContext(),1)
//        recyclerView!!.layoutManager = Manager
//        adapter = ImageAdapter(requireContext(),args.argsFromPark!!.images)
//        recyclerView!!.adapter = adapter


        Log.d(TAG, "Selected park from screen #1: ${args.argsFromPark}")

        binding.btnAddItinerary.setOnClickListener(){

            val db = Firebase.firestore
            // Create a new document in Firestore
            val itinerary = hashMapOf(
                "parkId" to args.argsFromPark!!.id,
                "parkName" to binding.tvTitle.text,
                "address" to binding.tvAddress.text,
                "tripDate" to "",
                "notes" to ""
            )

            Log.d(TAG,itinerary.toString())


//            //val parkId = args.argsFromPark!!.id
//            // Add the document to the "itinerary" collection
            db.collection("itinerary")
                .add(itinerary)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Itinerary was successfully added")
                    Toast.makeText(
                        requireContext(),
                        "Itinerary was successfully added",
                        Toast.LENGTH_SHORT ).show()
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, exception.message.toString())
                    Toast.makeText(
                        requireContext(),
                        "Error: ${exception.message.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            val action = ParkDetailsFragmentDirections.actionParkDetailsFragmentToItineraryFragment()

            findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}