package com.lizl.demo.passwordbox.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.OperationModel
import kotlinx.android.synthetic.main.item_operation.view.*

class OperationListAdapter(operationList: List<OperationModel>) :
        BaseQuickAdapter<OperationModel, OperationListAdapter.ViewHolder>(R.layout.item_operation, operationList.toMutableList())
{
    private var onOperationItemClickListener: (() -> Unit)? = null

    override fun convert(helper: ViewHolder, model: OperationModel)
    {
        helper.bindViewHolder(model)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(operationModel: OperationModel)
        {
            itemView.tv_operation_name.text = operationModel.operationName

            itemView.setOnClickListener {
                onOperationItemClickListener?.invoke()
                operationModel.operationItemCallBack.invoke()
            }
        }
    }

    fun setOnOperationItemClickListener(onOperationItemClickListener: () -> Unit)
    {
        this.onOperationItemClickListener = onOperationItemClickListener
    }
}