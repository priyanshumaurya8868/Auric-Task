package com.priyanshumaurya8868.aurictask.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.priyanshumaurya8868.aurictask.R

fun ImageView.load(uri: String) {
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(this).load(uri).circleCrop().placeholder(R.drawable.man).apply(requestOptions)
        .into(this)
}

