package com.lizl.demo.passwordbox.custom.dialog

import android.content.Context
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.mvvm.model.AccountModel
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.dialog_account_info.*

/**
 * 显示账号信息的Dialog
 */
class DialogAccountInfo(context: Context, private val accountModel: AccountModel) : BaseDialog(context, R.layout.dialog_account_info)
{
    override fun initView()
    {
        tv_description.text = accountModel.description
        tv_account.text = context.getString(R.string.account_, accountModel.account)
        tv_password.text = context.getString(R.string.password_, accountModel.password)

        UiUtil.clearTextViewAutoWrap(tv_account)
        UiUtil.clearTextViewAutoWrap(tv_password)

        iv_copy_account.setOnClickListener { UiUtil.copyTextToClipboard(context, accountModel.account) }
        iv_copy_password.setOnClickListener { UiUtil.copyTextToClipboard(context, accountModel.password) }
    }
}