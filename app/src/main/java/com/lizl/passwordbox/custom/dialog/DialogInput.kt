package com.lizl.passwordbox.custom.dialog

import android.content.Context
import androidx.core.widget.addTextChangedListener
import com.lizl.passwordbox.R
import kotlinx.android.synthetic.main.dialog_input.*

/**
 * 用于输入信息的Dialog
 */
class DialogInput(context: Context, private var title: String, private var editHint: String, private var inputCompletedCallback: (String) -> Unit) :
    BaseDialog(context, R.layout.dialog_input)
{
    override fun initView()
    {
        tv_title.text = title
        et_input.hint = editHint
        et_input.setText("")

        et_input.addTextChangedListener { tv_confirm.isEnabled = !it.isNullOrEmpty() }

        tv_cancel.setOnClickListener { dismiss() }
        tv_confirm.setOnClickListener {
            inputCompletedCallback.invoke(et_input.text.toString())
            dismiss()
        }
    }
}