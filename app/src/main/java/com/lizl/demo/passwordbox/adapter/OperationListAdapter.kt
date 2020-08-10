package com.lizl.demo.passwordbox.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.mvvm.model.OperationModel
import kotlinx.android.synthetic.main.item_operation.view.*

class OperationListAdapter(operationList: List<OperationModel>) :
    BaseQuickAdapter<OperationModel, BaseViewHolder>(R.layout.item_operation, operationList.toMutableList())
{
    override fun convert(helper: BaseViewHolder, item: OperationModel)
    {
        with(helper.itemView) {
            tv_operation_name.text = item.operationName
        }
    }

    fun setOnOperationItemClickListener(onOperationItemClickListener: () -> Unit)
    {
        setOnItemClickListener { _, _, position ->
            getItemOrNull(position)?.let {
                it.operationItemCallBack.invoke()
                onOperationItemClickListener.invoke()
            }
        }
    }
}