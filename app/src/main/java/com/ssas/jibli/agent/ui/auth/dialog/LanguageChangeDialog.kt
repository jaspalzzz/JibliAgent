package com.ssas.jibli.agent.ui.auth.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ssas.jibli.agent.MApplication
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.data.prefs.PrefMain
import com.ssas.jibli.agent.dialogs.CommonDialog
import com.ssas.jibli.agent.ui.auth.LoginActivity
import com.ssas.jibli.agent.utils.LanguageUtils
import com.ssas.jibli.agent.utils.Utils


import kotlinx.android.synthetic.main.dialog_language_change.*
import javax.inject.Inject

class LanguageChangeDialog : BottomSheetDialogFragment() {

    private var selectedLanguage = LanguageUtils.ARABIC

    @Inject
    lateinit var prefMain: PrefMain

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_language_change, container, false)
    }
	
	override fun onViewStateRestored(savedInstanceState: Bundle?) {
		super.onViewStateRestored(savedInstanceState)
		MApplication.appComponent.inject(this)
		selectPreviousLanguage()
		setListeners()
	}

    private fun setListeners() {
        englishBt.setOnClickListener {
            selectEnglishLanguage()
        }
        arabicBt.setOnClickListener {
            selectArabicLanguage()
        }

        changeLangBt.setOnClickListener {
            implementSelectedLanguage(selectedLanguage)
        }
    }

    private fun selectPreviousLanguage() {
        var language = prefMain.get(PrefKeys.LANGUAGE, LanguageUtils.ARABIC)
        when (language) {
            LanguageUtils.ENGLISH -> {
                selectEnglishLanguage()
            }
            LanguageUtils.ARABIC -> {
                selectArabicLanguage()
            }
        }
    }

    private fun selectEnglishLanguage() {
        selectedLanguage = LanguageUtils.ENGLISH
        englishCheckbox.isChecked = true
        arabicCheckbox.isChecked = false
    }

    private fun selectArabicLanguage() {
        selectedLanguage = LanguageUtils.ARABIC
        englishCheckbox.isChecked = false
        arabicCheckbox.isChecked = true
    }

    private fun implementSelectedLanguage(language: String) {
        MApplication.language = language
        prefMain.put(PrefKeys.LANGUAGE, language)
        LanguageUtils.setLanguage(context, MApplication.language)
        showSuccessLanguage()
    }

    private fun showSuccessLanguage() {
        var successDialog = CommonDialog.newInstance(
            "", getString(R.string.language_changes_success_msg),
            R.drawable.ic_check, getString(R.string.ok)
        )
        successDialog.setListener(object : CommonDialog.SuccessDialogListener {
            override fun onPositiveButtonClick(dialog: CommonDialog) {
                Utils.jumpActivityClearTask(requireContext(), LoginActivity::class.java)

            }
	
	        override fun onCancelButtonClick(dialog: CommonDialog) {
		        dialog.dismiss()
	        }
        })
        successDialog.show(parentFragmentManager, "")
    }
}