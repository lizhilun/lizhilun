package com.lizl.passwordbox.custom.dialog

import android.content.Context
import com.lizl.passwordbox.R
import com.lizl.passwordbox.adapter.OperationListAdapter
import com.lizl.passwordbox.mvvm.model.OperationModel
import kotlinx.android.synthetic.main.dialog_operation_list.*

/**
 * 用于显示操作列表的Dialog
 */
class DialogOperationList(context: Context, private var operationList: List<OperationModel>) : BaseDialog(context, R.layout.dialog_operation_list)
{
    override fun initView()
    {
        rv_operation_list.adapter = OperationListAdapter(operationList).apply {
            setOnOperationItemClickListener { dismiss() }
        }
    }
}