package com.lizl.demo.passwordbox.custom.view

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.util.UiUtil


class EditTextWithDecText(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ConstraintLayout(context, attrs, defStyleAttr)
{

    private lateinit var textView: AppCompatTextView
    private lateinit var editText: AppCompatEditText

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        textView = AppCompatTextView(context).apply {
            id = View.generateViewId()
            gravity = Gravity.CENTER
            this@EditTextWithDecText.addView(this)
        }

        editText = AppCompatEditText(context).apply {
            id = View.generateViewId()
            setLineSpacing(0F, 1.2F)
            this@EditTextWithDecText.addView(this)
        }

        val bottomLine = View(context).apply {
            id = View.generateViewId()
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivideLine))
            this@EditTextWithDecText.addView(this)
        }

        var editTextSize = context.resources.getDimensionPixelSize(R.dimen.global_text_size)
        var decTextSize = context.resources.getDimensionPixelSize(R.dimen.global_text_size)
        var decEmsSize = 4
        var marginDecText = context.resources.getDimensionPixelSize(R.dimen.global_content_padding_content)
        var editTextInputType = 0
        var editTextMaxEms = Int.MAX_VALUE

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithDecText)
        for (index in 0 until typeArray.indexCount)
        {
            when (val attr = typeArray.getIndex(index))
            {
                R.styleable.EditTextWithDecText_editTextHit       -> editText.hint = typeArray.getString(attr)
                R.styleable.EditTextWithDecText_editTextSize      -> editTextSize = typeArray.getLayoutDimension(attr, editTextSize)
                R.styleable.EditTextWithDecText_exitTextInputType -> editTextInputType = typeArray.getInt(attr, editTextInputType)
                R.styleable.EditTextWithDecText_marginDecText     -> marginDecText = typeArray.getLayoutDimension(attr, decTextSize)
                R.styleable.EditTextWithDecText_editTextMaxEms    -> editTextMaxEms = typeArray.getInteger(attr, editTextMaxEms)
                R.styleable.EditTextWithDecText_editTextColor     ->
                {
                    textView.setTextColor(typeArray.getColorStateList(attr))
                }
                R.styleable.EditTextWithDecText_decText           -> textView.text = typeArray.getString(attr)
                R.styleable.EditTextWithDecText_decTextSize       -> decTextSize = typeArray.getLayoutDimension(attr, decTextSize)
                R.styleable.EditTextWithDecText_decTextColor      ->
                {
                    textView.setTextColor(typeArray.getColorStateList(attr))
                }
                R.styleable.EditTextWithDecText_decTextEmsSize    -> decEmsSize = typeArray.getInteger(attr, 4)
            }
        }
        typeArray.recycle()

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, decTextSize.toFloat())
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize.toFloat())

        editText.background = null
        when (editTextInputType)
        {
            InputType.TYPE_CLASS_TEXT                ->
            {
                editText.inputType = InputType.TYPE_CLASS_TEXT
                editText.setLines(2)
            }
            InputType.TYPE_NUMBER_VARIATION_PASSWORD ->
            {
                editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                editText.setLines(2)
            }
            InputType.TYPE_TEXT_FLAG_MULTI_LINE      ->
            {
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                editText.minLines = 2
            }
            else                                     ->
            {
                editText.inputType = InputType.TYPE_CLASS_TEXT
                editText.setLines(2)
            }
        }
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(editTextMaxEms))

        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        constraintSet.constrainHeight(textView.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainWidth(textView.id, decEmsSize * editTextSize)
        constraintSet.connect(textView.id, ConstraintSet.TOP, editText.id, ConstraintSet.TOP)
        constraintSet.connect(textView.id, ConstraintSet.BOTTOM, editText.id, ConstraintSet.BOTTOM)

        constraintSet.constrainHeight(editText.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainWidth(editText.id, LayoutParams.MATCH_CONSTRAINT)
        constraintSet.connect(editText.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(editText.id, ConstraintSet.START, textView.id, ConstraintSet.END, marginDecText)
        constraintSet.connect(editText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        constraintSet.constrainHeight(bottomLine.id, UiUtil.dpToPx(1))
        constraintSet.constrainWidth(bottomLine.id, LayoutParams.MATCH_PARENT)
        constraintSet.connect(bottomLine.id, ConstraintSet.BOTTOM, editText.id, ConstraintSet.BOTTOM)

        constraintSet.applyTo(this)

        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (v.id == editText.id)
            {
                bottomLine.setBackgroundColor(ContextCompat.getColor(context, if (hasFocus) R.color.colorPrimary else R.color.colorDivideLine))
            }
        }
    }

    fun setText(text: String?)
    {
        editText.setText(text)
    }

    fun getText() = editText.text.toString()
}