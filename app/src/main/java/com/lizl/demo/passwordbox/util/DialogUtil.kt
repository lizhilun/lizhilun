package com.lizl.demo.passwordbox.util

import android.content.Context
import com.lizl.demo.passwordbox.customview.dialog.*
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem

class DialogUtil
{
    companion object
    {
        private var dialog: BaseDialog? = null

        fun showAccountInfoDialog(context: Context, accountModel: AccountModel)
        {
            dialog?.dismiss()
            dialog = DialogAccountInfo(context, accountModel)
            dialog?.show()
        }

        fun showFingerprintDialog(context: Context, fingerprintUnlockCallBack: DialogFingerprint.FingerprintUnlockCallBack)
        {
            dialog?.dismiss()
            dialog = DialogFingerprint(context, fingerprintUnlockCallBack)
            dialog?.show()
        }

        fun showInputDialog(context: Context, title: String, editHint: String, inputCompletedCallback: DialogInput.InputCompletedCallback)
        {
            dialog?.dismiss()
            dialog = DialogInput(context, title, editHint, inputCompletedCallback)
            dialog?.show()
        }

        fun showOperationConfirmDialog(context: Context, title: String, notify: String, operationConfirmCallback: DialogOperationConfirm.OperationConfirmCallback)
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
}