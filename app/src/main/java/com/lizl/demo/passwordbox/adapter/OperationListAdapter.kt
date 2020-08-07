package com.lizl.demo.passwordbox.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.mvvm.model.OperationModel
import kotlinx.android.synthetic.main.item_operation.view.*

class OperationListAdapter(operationList: List<OperationModel>) :
    BaseQuickAdapter<OperationModel, BaseViewHolder>(R.layout.item_operation, operationList.toMutableList())
{
    private var onOperationItemClickListener: (() -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: OperationModel)
    {
        with(helper.itemView) {
            tv_operation_name.text = item.operationName

            setOnClickListener {
                onOperationItemClickListener?.invoke()
                item.operationItemCallBack.invoke()
            }
        }
    }

    fun setOnOperationItemClickListener(onOperationItemClickListener: () -> Unit)
    {
        this.onOperationItemClickListener = onOperationItemClickListener
    }
}