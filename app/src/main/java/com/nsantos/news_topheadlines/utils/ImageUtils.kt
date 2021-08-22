package com.nsantos.news_topheadlines.utils

import android.widget.ImageView
import com.nsantos.news_topheadlines.R
import com.squareup.picasso.Picasso

fun ImageView.loadImageWithDimensions(url: String?, width: Int, height: Int){
    Picasso.get()
        .load(url)
        .resize(width, height)
        .centerCrop()
        .placeholder(R.drawable.loading_animation)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}

fun ImageView.loadImage(url: String?){
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.loading_animation)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}