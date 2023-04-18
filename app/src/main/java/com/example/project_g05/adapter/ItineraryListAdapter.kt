package com.example.project_g05.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_g05.R
import com.example.project_g05.models.Itinerary
import com.example.project_g05.models.NationalPark

class ItineraryListAdapter (private val parkList: MutableList<Itinerary>) : RecyclerView.Adapter<ItineraryListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // UI components to be set later
        val tvParkName=itemView.findViewById<TextView>(R.id.tvParkName)
        val tvAddress= itemView.findViewById<TextView>(R.id.tvAddress)
        val tvTripDate = itemView.findViewById<TextView>(R.id.tvTripDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val park = parkList[position]


        holder.tvParkName.text=park.parkName
        holder.tvAddress.text = park.address
        holder.tvTripDate.text = park.tripDate.toString()
    }

    override fun getItemCount(): Int {
        return parkList.size
    }
}
