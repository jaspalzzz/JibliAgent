package com.ssas.jibli.agent.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.ssas.jibli.agent.MApplication
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.prefs.PrefMain
import com.ssas.jibli.agent.dialogs.ProgressCircularDialog
import com.ssas.jibli.agent.dialogs.ProgressDialog
import com.ssas.jibli.agent.utils.OnActivityResult
import com.ssas.jibli.agent.utils.PermissionResultListener
import java.util.*
import javax.inject.Inject

abstract class BaseActivitySimple : AppCompatActivity() {

    private lateinit var progressCirluarDialog: ProgressCircularDialog
    private lateinit var progressDialog: ProgressDialog
    var startSession = true
    private var timer: Timer? = null


    @Inject
    lateinit var prefMain: PrefMain

    private var mPermissionResultListener: PermissionResultListener? = null
    var mActivityResultListener: OnActivityResult? = null

    fun setPermissionResultListener(mPermissionResultListener: PermissionResultListener) {
        this.mPermissionResultListener = mPermissionResultListener
    }

    fun setOnActivityResultListener(mActivityResultListener: OnActivityResult) {
        this.mActivityResultListener = mActivityResultListener
    }


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        MApplication.appComponent.inject(this)
    }

    fun changeStatusBarColor(colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var window = window
            window.statusBarColor = ActivityCompat.getColor(this, colorId)
        }
    }


    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    fun hideSoftKeyboard(activity: Activity) {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        } catch (exp: Exception) {
        }

    }

    fun showSnackbar(view: View, messsage: Int) {
        Snackbar.make(view, getString(messsage), Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbar(view: View, messsage: String) {
        Snackbar.make(view, messsage, Snackbar.LENGTH_SHORT).show()
    }

    fun snackBarAction(view: View, message: Int, clickListener: View.OnClickListener) {
        val snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry, clickListener)
        snackbar.show()
    }


    fun alertDialogShow(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok) { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(R.string.ok) { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(context: Context, title: String, message: String,buttonTitle:String,okLister: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(buttonTitle, okLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(
        context: Context,
        message: String,
        okLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.ok, okLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(
        context: Context,
        title: String,
        message: String,
        okButtonTitle: String,
        okLister: DialogInterface.OnClickListener,
        canelLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(okButtonTitle, okLister)
        builder.setNegativeButton(R.string.cancel, canelLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(
        context: Context,
        message: String,
        okButtonTitle: String,
        okLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton(okButtonTitle, okLister)
        builder.setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showCircularProgress() {
        progressCirluarDialog = ProgressCircularDialog()
        progressCirluarDialog.show(supportFragmentManager, progressCirluarDialog.tag)
    }

    fun dismissCircularProgress() {
        if (progressCirluarDialog != null) {
            progressCirluarDialog.dismiss()
        }
    }

    fun showProgress() {
        progressDialog = ProgressDialog()
        progressDialog.show(supportFragmentManager, progressDialog.tag)

    }

    fun dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionResultListener?.onPermissionResult(requestCode, permissions, grantResults)
    }

    fun isLifeCycleResumed(): Boolean = lifecycle.currentState == Lifecycle.State.RESUMED

    fun setFocusView(view: EditText) {
        view.requestFocus()
    }


}