package com.ssas.jibli.agent.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.utils.Utils

class PinView(context: Context?, attrs: AttributeSet?) : LinearLayout(context!!, attrs),
	View.OnKeyListener, View.OnFocusChangeListener {
	
	private var hasFocusEt = 0
	private var firstPinEt: EditText
	private var secondPinEt: EditText
	private var thirdPinEt: EditText
	private var fourthPinEt: EditText
	private var listener: PinViewListener? = null
	
	
	init {
		var view = inflate(context, R.layout.pin_view, this)
		firstPinEt = view.findViewById(R.id.first_pin_et)
		secondPinEt = view.findViewById(R.id.second_pin_et)
		thirdPinEt = view.findViewById(R.id.third_pin_et)
		fourthPinEt = view.findViewById(R.id.fourth_pin_et)
		
		firstPinEt.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				var input = s.toString()
				if (input.length == 1) {
					secondPinEt.requestFocus()
				} else if (input.length == 0) {
					firstPinEt.requestFocus()
				}
			}
			
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			
			}
			
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			
			}
		})
		
		secondPinEt.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				var input = s.toString()
				if (input.length == 1) {
					thirdPinEt.requestFocus()
				} else if (input.length == 0) {
					firstPinEt.requestFocus()
				}
			}
			
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			
			}
			
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			
			}
			
		})
		
		thirdPinEt.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				var input = s.toString()
				if (input.length == 1) {
					fourthPinEt.requestFocus()
				} else if (input.length == 0) {
					secondPinEt.requestFocus()
				}
			}
			
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			
			}
			
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			
			}
		})
		
		fourthPinEt.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				var input = s.toString()
				if (input.length == 1) {
					fourthPinEt.requestFocus()
					Utils.hideKeyboardOnClick(context!!, firstPinEt)
					var securityCode = getSecurityCode()
					listener?.onPinCodeRecieved(securityCode)
				} else if (input.length == 0) {
					thirdPinEt.requestFocus()
					listener?.onPinCodeDelete()
				}
			}
			
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			
			}
			
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			
			}
			
		})
		
		firstPinEt.setOnKeyListener(this)
		secondPinEt.setOnKeyListener(this)
		thirdPinEt.setOnKeyListener(this)
		fourthPinEt.setOnKeyListener(this)
		
		firstPinEt.onFocusChangeListener = this
		secondPinEt.onFocusChangeListener = this
		thirdPinEt.onFocusChangeListener = this
		fourthPinEt.onFocusChangeListener = this
		
	}
	
	override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
		if (event?.action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_DEL) {
				when (v?.id) {
					R.id.first_pin_et -> {
						firstPinEt.setText("")
						//binding.optFirstPinEt.requestFocus()
					}
					
					R.id.second_pin_et -> {
						secondPinEt.setText("")
						//binding.optFirstPinEt.requestFocus()
					}
					
					R.id.third_pin_et -> {
						thirdPinEt.setText("")
						//binding.optSecondPinEt.requestFocus()
					}
					
					R.id.fourth_pin_et -> {
						fourthPinEt.setText("")
						//binding.optThirdPinEt.requestFocus()
					}
				}
			} else {
				val pressedKey = event.unicodeChar.toChar()
				when (hasFocusEt) {
					1 -> {
						if (firstPinEt.text?.isNotEmpty()!!) {
							secondPinEt.setText(pressedKey.toString())
						}
					}
					
					2 -> {
						if (secondPinEt.text?.isNotEmpty()!!) {
							thirdPinEt.setText(pressedKey.toString())
						}
					}
					
					3 -> {
						if (thirdPinEt.text?.isNotEmpty()!!) {
							fourthPinEt.setText(pressedKey.toString())
						}
					}
					
					4 -> {
						if (fourthPinEt.text?.isNotEmpty()!!) {
							fourthPinEt.setText(pressedKey.toString())
						}
					}
				}
			}
		}
		return false
	}
	
	override fun onFocusChange(v: View?, hasFocus: Boolean) {
		when (v?.id) {
			R.id.first_pin_et -> {
				hasFocusEt = 1
			}
			
			R.id.second_pin_et -> {
				hasFocusEt = 2
			}
			
			R.id.third_pin_et -> {
				hasFocusEt = 3
			}
			
			R.id.fourth_pin_et -> {
				hasFocusEt = 4
			}
		}
	}
	
	private fun getSecurityCode(): String {
		return firstPinEt.text.toString().trim() + secondPinEt.text.toString().trim() +
				thirdPinEt.text.toString().trim() + fourthPinEt.text.toString().trim()
	}
	
	interface PinViewListener {
		fun onPinCodeRecieved(pinCode: String)
		fun onPinCodeDelete()
	}
	
	fun setPinViewListener(owner: PinViewListener?) {
		if (owner == null) {
			return
		}
		this.listener = owner
	}
	
	fun hideCodeText() {
		Utils.hidePassword(firstPinEt)
		Utils.hidePassword(secondPinEt)
		Utils.hidePassword(thirdPinEt)
		Utils.hidePassword(fourthPinEt)
	}
	
	fun showCodeText() {
		Utils.showPassword(firstPinEt)
		Utils.showPassword(secondPinEt)
		Utils.showPassword(thirdPinEt)
		Utils.showPassword(fourthPinEt)
	}
	
	fun requestFocusFirstEt() {
		firstPinEt.requestFocus()
	}

	fun clearText(){
		firstPinEt.setText("")
		secondPinEt.setText("")
		thirdPinEt.setText("")
		fourthPinEt.setText("")
		firstPinEt.requestFocus()
	}

}