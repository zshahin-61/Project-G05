package com.example.project_g05.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.project_g05.R


class ImageAdapter (
    private val context: Context
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_images, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(FitCenter(), RoundedCorners(16))
        Glide.with(context)
            .load("https://openai.com/content/images/2021/01/2x-no-mark-1.jpg")
            .apply(requestOptions)
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(viewHolder.img_android)

    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var img_android: ImageView

        init {
            img_android =
                view.findViewById<View>(R.id.iv_glide) as ImageView
        }
    }

}