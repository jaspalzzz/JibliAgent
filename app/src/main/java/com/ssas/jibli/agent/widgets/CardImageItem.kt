package com.ssas.jibli.agent.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ssas.jibli.agent.R


class CardImageItem(context: Context?, attrs: AttributeSet?) : LinearLayout(context!!, attrs) {
    init {
        inflate(context, R.layout.card_image_item, this)
        val imageView: ImageView = findViewById(R.id.itemImage)
        val itemTitle:TextView = findViewById(R.id.itemTitle)

        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.cardImageItem)
        imageView.setImageDrawable(attributes?.getDrawable(R.styleable.cardImageItem_item_card_img))
        itemTitle.text = attributes?.getText(R.styleable.cardImageItem_item_card_title)

        attributes?.recycle()
    }
}
