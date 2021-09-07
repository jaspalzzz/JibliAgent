package com.ssas.jibli.agent.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.ssas.jibli.agent.MApplication
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.prefs.PrefMain
import com.ssas.jibli.agent.dialogs.ProgressCircularDialog
import com.ssas.jibli.agent.dialogs.ProgressDialog
import com.ssas.jibli.agent.utils.Utils
import javax.inject.Inject


open class BaseFragmentSimple : Fragment() {

    @Inject
    lateinit var prefMain: PrefMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MApplication.appComponent.inject(this)
    }

    fun setFocusView(view: EditText){
        view.requestFocus()
    }

    fun setFocus(view: View) {
        Utils.hideKeyboardOnClick(requireContext(), view)
        view.requestFocus()
        //view.bringToFront()
    }

    fun showSnackbar(view: View, messsage: Int) {
        Snackbar.make(view, getString(messsage), Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbar(view: View, messsage: String) {
        Snackbar.make(view, messsage, Snackbar.LENGTH_SHORT).show()
    }

    fun snackBarAction(view: View, message: Int, clickListener: View.OnClickListener) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Retry", clickListener)
        snackbar.show()
    }


    fun alertDialogShow(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton("Ok") { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton("Ok") { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(
        context: Context,
        title: String,
        message: String,
        okLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Ok", okLister)
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
        builder.setPositiveButton("Ok", okLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun alertDialogShow(
        context: Context,
        title: String,
        message: String,
        okButtonTitle: String,
        okLister: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(okButtonTitle, okLister)
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
        builder.setNegativeButton(getString(R.string.no), canelLister)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean?) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item !== exception) {
                item.isVisible = visible!!
            }
        }
    }


    fun hideKeyboard(activity: Activity) {
        val inputManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    lateinit var progressCirluarDialog: ProgressCircularDialog
    private var progressDialog: ProgressDialog?=null


    fun showCircularProgress() {
        progressCirluarDialog = ProgressCircularDialog()
        progressCirluarDialog.show(parentFragmentManager, progressCirluarDialog.tag)

    }

    fun dismissCircularProgress() {
        if (progressCirluarDialog != null) {
            progressCirluarDialog.dismiss()
        }
    }

    fun showProgress() {
        progressDialog = ProgressDialog()
        progressDialog?.show(parentFragmentManager, progressDialog?.tag)
    }

    fun dismissProgress() {
        if (progressDialog != null) {
            progressDialog?.dismiss()
        }
    }


    fun isLifeCycleResumed() =
        viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
}