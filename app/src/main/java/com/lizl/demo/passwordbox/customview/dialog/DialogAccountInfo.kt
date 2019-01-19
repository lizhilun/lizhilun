package com.lizl.demo.passwordbox.customview.dialog

import android.content.Context
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.dialog_account_info.*

/**
 * 显示账号信息的Dialog
 */
class DialogAccountInfo(context: Context, accountModel: AccountModel) : BaseDialog(context)
{
    private var accountModel: AccountModel? = null

    override fun getDialogLayoutResId(): Int
    {
        return R.layout.dialog_account_info
    }

    init
    {
        this.accountModel = accountModel
    }

    override fun initView()
    {
        tv_description.text = accountModel?.description
        tv_account.text = context.getString(R.string.account_, accountModel?.account)
        tv_password.text = context.getString(R.string.password_, accountModel?.password)

        UiUtil.clearTextViewAutoWrap(tv_account)
        UiUtil.clearTextViewAutoWrap(tv_password)

        iv_copy_account.setOnClickListener { UiUtil.copyTextToClipboard(context, tv_account.text.toString()) }
        iv_copy_password.setOnClickListener { UiUtil.copyTextToClipboard(context, tv_password.text.toString()) }
    }
}