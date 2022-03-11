package com.ssas.jibli.agent.utils

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.utils.countrycode.Country
import com.ssas.jibli.agent.utils.countrycode.CountryUtils


object BindingImageAdapter {

	
	@JvmStatic
	@BindingAdapter(value = ["base_image"])
	fun setImageBase64(imageView: ImageView, encodedImage: String?) {
		if (encodedImage != null) {
			val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
			val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
			if (decodedByte != null) {
				Glide.with(imageView.context).load(decodedByte).into(imageView)
			} else {
				imageView.setImageBitmap(decodedByte)
			}
		}
	}


	@JvmStatic
	@BindingAdapter(value = ["base_image_default"])
	fun setImageBase64WithDefault(imageView: ImageView, encodedImage: String?) {
		if (encodedImage != null) {
			val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
			val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
			if (decodedByte != null) {
				Glide.with(imageView.context).load(decodedByte).into(imageView)
			} else {
				imageView.setImageResource(R.drawable.ic_scoter)
			}
		}
	}

	
	@JvmStatic
	@BindingAdapter("imageRoundCorner")
	fun setRoundCornerImage(imageView: ImageView, imageURL: String?) {
		if(imageURL.isNullOrEmpty()){
			imageView.setImageResource(R.drawable.ic_scoter)
		}else{
			val circularProgressDrawable = CircularProgressDrawable(imageView.context)
			circularProgressDrawable.strokeWidth = 20f
			circularProgressDrawable.centerRadius = 5f
			circularProgressDrawable.start()

			val requestOptions = RequestOptions()
				.placeholder(circularProgressDrawable)
				.apply(RequestOptions.bitmapTransform(CircleCrop()))
			    .diskCacheStrategy(DiskCacheStrategy.ALL)

			Glide.with(imageView.context)
				.load(imageURL)
				.apply(requestOptions)
				.into(imageView)
		}
	}
	
	@JvmStatic
	@BindingAdapter("networkImage","networkErrorImage",requireAll = false)
	fun setNetworkImage(imageView: ImageView, imageURL: String?, errorImage:Int?) {
		if (!imageURL.isNullOrEmpty()) {
			val circularProgressDrawable = CircularProgressDrawable(imageView.context)
			circularProgressDrawable.strokeWidth = 5f
			circularProgressDrawable.centerRadius = 5f
			circularProgressDrawable.start()
			
			val requestOptions = RequestOptions()
				.placeholder(circularProgressDrawable)
				.apply(RequestOptions.bitmapTransform(CircleCrop()))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
			Glide.with(imageView.context)
				.load(imageURL)
				.apply(requestOptions)
				.error(errorImage?: R.drawable.ic_scoter)
				.into(imageView)
		}
	}

	@JvmStatic
	@BindingAdapter(value = ["src_image"], requireAll = false)
	fun setImageDrawable(imageView: ImageView, country: Country) {
		val image = CountryUtils.getFlagDrawableResId(country)
		imageView.setImageResource(image)
	}
}