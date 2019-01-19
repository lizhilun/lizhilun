package com.lizl.demo.passwordbox.customview.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.lizl.demo.passwordbox.R
import kotlinx.android.synthetic.main.dialog_input.*

/**
 * 用于输入信息的Dialog
 */
class DialogInput(context: Context, private var title: String, private var editHint: String, private var inputCompletedCallback: InputCompletedCallback) : BaseDialog(context)
{
    override fun getDialogLayoutResId(): Int
    {
        return R.layout.dialog_input
    }

    override fun initView()
    {
        tv_title.text = title
        et_input.hint = editHint
        et_input.setText("")

        et_input.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                //do nothing
            }

            override fun afterTextChanged(s: Editable?)
            {
                tv_confirm.isEnabled = !s.isNullOrEmpty()
            }
        })

        tv_cancel.setOnClickListener { dismiss() }
        tv_confirm.setOnClickListener {
            inputCompletedCallback.onInputCompleted(et_input.text.toString())
            dismiss()
        }
    }

    interface InputCompletedCallback
    {
        fun onInputCompleted(inputValue: String)
    }
}