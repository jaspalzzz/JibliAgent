package com.ssas.jibli.agent.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.viewpager2.widget.ViewPager2
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.models.ErrorResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.abs


object Utils {

    @Synchronized
    fun <T> jumpActivityClearTask(context: Context, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    @Synchronized
    fun <T> jumpActivityClearTask(context: Context, clazz: Class<T>, bundle: Bundle) {
        val intent = Intent(context, clazz)
        intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    @Synchronized
    fun <T> jumpActivity(context: Context, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        context.startActivity(intent)
    }

    @Synchronized
    fun <T> jumpActivityForResult(context: Activity, clazz: Class<T>, resultCode: Int) {
        val intent = Intent(context, clazz)
        context.startActivityForResult(intent, resultCode)
    }

    @Synchronized
    fun <T> jumpActivityWithData(context: Context, clazz: Class<T>, bundle: Bundle) {
        val intent = Intent(context, clazz)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    @Synchronized
    fun <T> jumpActivityWithAction(context: Context, clazz: Class<T>, action: String) {
        val intent = Intent(context, clazz)
        intent.action = action
        context.startActivity(intent)
    }

    @Synchronized
    fun <T> jumpActivityForResult(
        context: Activity,
        resultCode: Int,
        clazz: Class<T>,
        bundle: Bundle
    ) {
        val intent = Intent(context, clazz)
        intent.putExtras(bundle)
        context.startActivityForResult(intent, resultCode)
    }


    fun hideSoftKeyBoard(context: Activity) {
        context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    fun hideKeyboardOnClick(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }


    fun isInternet(context: Context): Boolean {
        return ConnectivityReceiver.isNetworkAvailable(context)
    }

    fun pxToDp(px: Int, context: Context): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun hidePassword(et: EditText) {
        et.transformationMethod = HideReturnsTransformationMethod.getInstance()
        et.setSelection(et.text!!.length)
    }

    fun showPassword(et: EditText) {
        et.transformationMethod = PasswordTransformationMethod()
        et.setSelection(et.text!!.length)
    }



    fun generateBase64Image(path: String): String? {
        return if (path.isNotEmpty()) {
            var bitmap = BitmapFactory.decodeFile(path)
            var baos = ByteArrayOutputStream()
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                var byteArray = baos.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } else {
                ""
            }
        } else {
            ""
        }
    }

    /*
    * Rate Us Dialog
    * */
    fun openPlayStoreApp(context: Context,packageName:String) {
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    /*
    * Call to telephone number
    * */
    fun callNumber(context: Context, tellPhone: String) {
        var callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$tellPhone")
        context.startActivity(callIntent)
    }

    /*
     *Share intent method
     * */
    fun shareContent(context: Context, shareContent: String) {
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent)
        shareIntent.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.app_name)
            )
        )
    }

    /*
    * Open weblink with intent
    * */
    fun openWebLink(context: Context, link: String) {
        var webLink = link
        if (!webLink.startsWith("https://") && !webLink.startsWith("http://")) {
            webLink = "http://$webLink"
        }
        var intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(webLink)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.app_name)
            )
        )
    }


    fun getBitmapFromViewCanvas(context: Context, view: View): Bitmap? {
        try {
            /* view.measure(
                 View.MeasureSpec.makeMeasureSpec(
                     0,
                     View.MeasureSpec.UNSPECIFIED
                 ),
                 View.MeasureSpec.makeMeasureSpec(
                     0,
                     View.MeasureSpec.UNSPECIFIED
                 )
             )
             view.layout(0, 0, view.measuredWidth, view.measuredHeight)*/

            var bitmap = Bitmap.createBitmap(
                view.width,
                view.height,
                Bitmap.Config.ARGB_8888
            )
            var canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            view.draw(canvas)
            return bitmap
        } catch (e: Exception) {
            Log.e("jaspal", "Layout to bitmap conversion Error " + e)
        }
        return null
    }

    fun errorHandlingWithStatus(context: Context, e: Throwable?): ErrorResponse {
        var errorResponse = ErrorResponse(context.getString(R.string.exception_msg), "")
        if (e is HttpException) {
            val response = e.response()
            try {
                val jObjError = JSONObject(response?.errorBody()?.string())
                errorResponse.message = jObjError.optString("message")
                errorResponse.statusCode = jObjError.optString("status")
            } catch (e1: JSONException) {
                errorResponse.message = e1.localizedMessage
                e1.printStackTrace()
            } catch (e1: IOException) {
                errorResponse.message = e1.localizedMessage
                e1.printStackTrace()
            }
        }
        return errorResponse
    }


    fun generateBarCode(barcode: String): Bitmap? {
        return try {
            var barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(barcode, BarcodeFormat.CODE_128, 800, 300)
        } catch (e: Exception) {
            null
        }
    }


    fun productUnit(context: Context, unit: String): String {
        return when (unit) {
            "1" -> {
                context.getString(R.string.kg)
            }

            "2" -> {
                context.getString(R.string.bundle)
            }

            "3" -> {
                context.getString(R.string.box)
            }

            "4" -> {
                context.getString(R.string.piece)
            }

            "5" -> {
                context.getString(R.string.pound)
            }

            "6" -> {
                context.getString(R.string.other)
            }

            else -> {
                ""
            }
        }
    }
}
