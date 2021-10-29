package com.ssas.jibli.agent.utils

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.BindingAdapter
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.constants.ValConstant
import java.text.SimpleDateFormat
import java.util.*

object BindingUtils {


    @JvmStatic
    @BindingAdapter(value = ["generateBarcodeImage"])
    fun generateBarcodeImage(imageView: ImageView, barcode: String?) {
        if (barcode != null) {
            var bitmap = Utils.generateBarCode(barcode)
            imageView.setImageBitmap(bitmap)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["formatDate"])
    fun formatDate(textView: TextView, date: String?) {
        if (date.isNullOrEmpty()) {
            textView.text = ""
        }
        val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
        try {
            val c = df.parse(date)
            val df1 = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
            textView.text = df1.format(c)
        } catch (e: Exception) {
            textView.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["paymentStatus"])
    fun orderPaymentStatus(textView: TextView, paymentStatus:String? ) {
        if(!paymentStatus.isNullOrEmpty()){
            if(paymentStatus == ValConstant.PAID){
                textView.text = textView.context.getString(R.string.paid)
                textView.setTextColor(ActivityCompat.getColor(textView.context,R.color.colorGreen))
            }else{
                textView.text = textView.context.getString(R.string.not_paid)
                textView.setTextColor(ActivityCompat.getColor(textView.context,R.color.colorRed))
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["orderCurrency", "orderPrice", "orderUnit"])
    fun orderPriceQuantityHandler(
        textView: TextView,
        orderCurrency: String?,
        orderPrice: String?,
        orderUnit: String?
    ) {
        if (orderUnit.isNullOrEmpty()) {
            textView.text = "$orderCurrency $orderPrice"
        } else {
            textView.text = "$orderCurrency $orderPrice/$orderUnit"
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["firstString", "secondString"])
    fun concatText(textView: TextView, firstString: String?, secondString: String?) {
        var firstStr = ""
        var secondStr = ""

        if (firstString != null) {
            firstStr = firstString.toString()
        }
        if (secondString != null) {
            secondStr = secondString
        }
        textView.text =
            String.format(textView.context.getString(R.string.concatText), firstStr, secondStr)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["colonFirstString", "colonSecondString", "colonSecondThird"],
        requireAll = false
    )
    fun concatColonText(
        textView: TextView,
        firstString: String?,
        secondString: String?,
        thirdString: String?
    ) {
        var firstStr = ""
        var secondStr = ""

        if (!firstString.isNullOrEmpty()) {
            firstStr = firstString.toString()
        }
        if (!secondString.isNullOrEmpty()) {
            secondStr = secondString
        }
        if (thirdString.isNullOrEmpty()) {
            textView.text = "$firstStr: $secondStr"
        }else{
            textView.text = "$firstStr: $secondStr $thirdString"
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["storeRating"])
    fun cardNumberMaskText(ratingBar: RatingBar, rating: String?) {
        if (rating.isNullOrEmpty()) {
            ratingBar.rating = 0f
        } else {
            ratingBar.rating = rating.toFloat()
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["cardExpireMonth", "cardExpireYear"])
    fun cardNumberMaskText(textView: TextView, expireMonth: String?, expireYear: String?) {
        textView.text = "$expireMonth.$expireYear"
    }

    @JvmStatic
    @BindingAdapter(value = ["maskMobileCode", "maskMobileNumber"])
    fun maskMobileDigits(textView: TextView, code: String, number: String) {
        val lastDigits: String = number.substring(number.length - 2)
        var convertStringLength = number.length - lastDigits.length
        val chars = CharArray(convertStringLength)
        Arrays.fill(chars, '*')
        val requiredMask = String(chars)
        var output = "$code $requiredMask$lastDigits"
        textView.text = output
    }

    @JvmStatic
    @BindingAdapter(value = ["cardNumberMaskText"])
    fun cardNumberMaskText(textView: TextView, cardNumber: String?) {
        if (!cardNumber.isNullOrEmpty()) {
            textView.text = maskCardNumber(addSpacesToCardNumber(cardNumber))
        } else {
            textView.text = ""
        }
    }

    fun addSpacesToCardNumber(cardNumber: String?): String {
        var builder = StringBuilder(cardNumber ?: "")
        if (builder.isNotEmpty()) {
            for (i in 4 until builder.length step 5) {
                builder.insert(i, " ")
            }
        }
        return builder.toString()
    }

    private fun maskCardNumber(cardNumber: String?): String {
        if (!cardNumber.isNullOrEmpty()) {
            var newCardNumber = lastFour(cardNumber)
            return newCardNumber ?: ""
        } else {
            return ""
        }
    }

    private fun lastFour(s: String): String? {
        val lastFour = StringBuilder()
        var check = 0
        for (i in s.length - 1 downTo 0) {
            if (Character.isDigit(s[i])) {
                check++
            }
            if (check <= 4) {
                lastFour.append(s[i])
            } else {
                lastFour.append(if (Character.isDigit(s[i])) "*" else s[i])
            }
        }
        return lastFour.reverse().toString()
    }

}