package com.lizl.demo.passwordbox.customview.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.OperationListAdapter
import com.lizl.demo.passwordbox.customview.recylerviewitemdivider.ListDividerItemDecoration
import com.lizl.demo.passwordbox.model.OperationItem
import kotlinx.android.synthetic.main.dialog_operation_list.*

/**
 * 用于显示操作列表的Dialog
 */
class DialogOperationList(context: Context, private var operationList: List<OperationItem>) : BaseDialog(context)
{
    override fun getDialogLayoutResId(): Int
    {
        return R.layout.dialog_operation_list
    }

    override fun initView()
    {
        val operationListAdapter = OperationListAdapter(context, operationList)
        rv_operation_list.layoutManager = LinearLayoutManager(context)
        rv_operation_list.addItemDecoration(ListDividerItemDecoration(context))
        rv_operation_list.adapter = operationListAdapter

        operationListAdapter.setOnOperationItemClickListener(object : OperationListAdapter.OnOperationItemClickListener
        {
            override fun onOperationItemClick()
            {
                dismiss()
            }
        })
    }
}