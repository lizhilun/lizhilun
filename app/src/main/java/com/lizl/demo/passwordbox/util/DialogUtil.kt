package com.lizl.demo.passwordbox.util

import android.content.Context
import com.lizl.demo.passwordbox.customview.dialog.*
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem

object DialogUtil
{
    private var dialog: BaseDialog? = null

    fun showAccountInfoDialog(context: Context, accountModel: AccountModel)
    {
        dialog?.dismiss()
        dialog = DialogAccountInfo(context, accountModel)
        dialog?.show()
    }

    fun showInputDialog(context: Context, title: String, editHint: String, inputCompletedCallback: (String) -> Unit)
    {
        dialog?.dismiss()
        dialog = DialogInput(context, title, editHint, inputCompletedCallback)
        dialog?.show()
    }

    fun showOperationConfirmDialog(context: Context, title: String, notify: String, operationConfirmCallback: () -> Unit)
    {
        dialog?.dismiss()
        dialog = DialogOperationConfirm(context, title, notify, operationConfirmCallback)
        dialog?.show()
    }

    fun showOperationListDialog(context: Context, operationList: List<OperationItem>)
    {
        dialog?.dismiss()
        dialog = DialogOperationList(context, operationList)
        dialog?.show()
    }

    fun dismissDialog()
    {
        dialog?.dismiss()
    }
}