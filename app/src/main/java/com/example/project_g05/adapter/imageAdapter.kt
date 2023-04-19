package com.example.project_g05.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.project_g05.R
import com.example.project_g05.models.ImageModel
import com.example.project_g05.models.Itinerary


class ImageAdapter (
    private val context: Context,private val iamgeList: List<ImageModel>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // UI components to be set later
        val ivImage=itemView.findViewById<ImageView>(R.id.ivImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_images, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = iamgeList[position]

        Glide.with(context)
            .load(img?.url)
            .into(holder.ivImage)


    }

    override fun getItemCount(): Int {
        return iamgeList.size
    }
}
