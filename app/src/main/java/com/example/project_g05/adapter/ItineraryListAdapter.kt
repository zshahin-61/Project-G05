package com.example.project_g05.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.project_g05.R
import com.example.project_g05.models.Itinerary

class ItineraryListAdapter (context: Context, list: List<Itinerary>) :
    ArrayAdapter<Itinerary>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_items, parent, false)

        val tvParkName = view.findViewById<TextView>(R.id.tvParkName)
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)

        val tvTripDate = view.findViewById<TextView>(R.id.tvTripDate)

        val c = getItem(position)
        c?.let {
            tvParkName.text = it.parkName
            tvAddress.text = it.address
            tvTripDate.text = it.tripDate.toString()
        }

        return view


//        // replace this view with a binding variable
//
//        var binding:ListItemPetBinding
//                = ListItemPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//
//        val flower = getItem(position)
//        flower?.let {
//            binding.tvName.text = it.name
//            binding.tvDetail.text = it.detail
//            binding.imgPet.setImageResource(it.imgPet)
//        }
//
//        return binding.root



    }
}