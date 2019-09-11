package com.lizl.demo.passwordbox.customview

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.util.UiUtil


class EditTextWithDecText(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr)
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
        textView = AppCompatTextView(context)
        textView.id = View.generateViewId()

        editText = AppCompatEditText(context)
        editText.id = View.generateViewId()

        val bottomLine = View(context)
        bottomLine.id = View.generateViewId()

        var editTextSize = context.resources.getDimensionPixelSize(com.lizl.demo.passwordbox.R.dimen.global_text_size)
        var decTextSize = context.resources.getDimensionPixelSize(com.lizl.demo.passwordbox.R.dimen.global_text_size)
        var decEmsSize = 4
        var marginDecText = context.resources.getDimensionPixelSize(com.lizl.demo.passwordbox.R.dimen.global_content_padding_content)
        var editTextInputType = 0
        var editTextMaxEms = Int.MAX_VALUE

        val typeArray = context.obtainStyledAttributes(attrs, com.lizl.demo.passwordbox.R.styleable.EditTextWithDecText)
        for (index in 0 until typeArray.indexCount)
        {
            val attr = typeArray.getIndex(index)
            when (attr)
            {
                R.styleable.EditTextWithDecText_editTextHit -> editText.hint = typeArray.getString(attr)
                R.styleable.EditTextWithDecText_editTextSize -> editTextSize = typeArray.getLayoutDimension(attr, editTextSize)
                R.styleable.EditTextWithDecText_exitTextInputType -> editTextInputType = typeArray.getInt(attr, editTextInputType)
                R.styleable.EditTextWithDecText_marginDecText -> marginDecText = typeArray.getLayoutDimension(attr, decTextSize)
                R.styleable.EditTextWithDecText_editTextMaxEms -> editTextMaxEms = typeArray.getInteger(attr, editTextMaxEms)
                R.styleable.EditTextWithDecText_editTextColor ->
                {
                    textView.setTextColor(typeArray.getColorStateList(attr))
                }
                R.styleable.EditTextWithDecText_decText -> textView.text = typeArray.getString(attr)
                R.styleable.EditTextWithDecText_decTextSize -> decTextSize = typeArray.getLayoutDimension(attr, decTextSize)
                R.styleable.EditTextWithDecText_decTextColor ->
                {
                    textView.setTextColor(typeArray.getColorStateList(attr))
                }
                R.styleable.EditTextWithDecText_decTextEmsSize -> decEmsSize = typeArray.getInteger(attr, 4)
            }
        }
        typeArray.recycle()


        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, decTextSize.toFloat())
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize.toFloat())

        editText.background = null
        when (editTextInputType)
        {
            InputType.TYPE_CLASS_TEXT ->
            {
                editText.inputType = InputType.TYPE_CLASS_TEXT
                editText.setLines(2)
            }
            InputType.TYPE_NUMBER_VARIATION_PASSWORD ->
            {
                editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                editText.setLines(2)
            }
            InputType.TYPE_TEXT_FLAG_MULTI_LINE ->
            {
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                editText.minLines = 2
            }
            else ->
            {
                editText.inputType = InputType.TYPE_CLASS_TEXT
                editText.setLines(2)
            }
        }
        editText.setLineSpacing(0F, 1.2F)
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(editTextMaxEms))

        val decTextWidth = decEmsSize * editTextSize

        bottomLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivideLine))

        val relativeLayout = RelativeLayout(context)
        val decTextLp = RelativeLayout.LayoutParams(decTextWidth, 0)
        val editTextLp = RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        val bottomLineLp = RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, UiUtil.dpToPx(1))

        bottomLineLp.addRule(RelativeLayout.BELOW, editText.id)

        decTextLp.addRule(RelativeLayout.ALIGN_TOP, editText.id)
        decTextLp.addRule(RelativeLayout.ALIGN_BOTTOM, editText.id)
        decTextLp.addRule(RelativeLayout.ALIGN_PARENT_START)
        textView.gravity = Gravity.CENTER

        editTextLp.addRule(RelativeLayout.END_OF, textView.id)
        editText.setPadding(marginDecText, UiUtil.dpToPx(5), 0, UiUtil.dpToPx(5))
        editText.gravity = Gravity.CENTER_VERTICAL

        relativeLayout.addView(bottomLine, bottomLineLp)
        relativeLayout.addView(textView, decTextLp)
        relativeLayout.addView(editText, editTextLp)

        editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (v.id == editText.id)
            {
                bottomLine.setBackgroundColor(ContextCompat.getColor(context, if (hasFocus) R.color.colorPrimary else R.color.colorDivideLine))
            }
        }

        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        addView(relativeLayout, layoutParams)
    }

    fun setText(text: String?)
    {
        editText.setText(text)
    }

    fun getText() = editText.text.toString()
}