package com.example.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class Email : AppCompatEditText {

    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = resources.getString(R.string.email)
        setHintTextColor(txtColor)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        setPadding(20, 20, 20, 20)
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isNotEmpty()) {
                        error = if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                            resources.getString(R.string.email_error)
                        } else {
                            null
                        }
                    }
                }
            }
        })
        txtColor = ContextCompat.getColor(context, R.color.dark_orange)
    }
}