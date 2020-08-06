package com.lizl.demo.passwordbox.adapter

import android.view.View
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.settingmodel.SettingBaseModel
import com.lizl.demo.passwordbox.model.settingmodel.SettingBooleanModel
import com.lizl.demo.passwordbox.model.settingmodel.SettingDivideModel
import com.lizl.demo.passwordbox.model.settingmodel.SettingNormalModel
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*

class SettingListAdapter(settingList: List<SettingBaseModel>) :
        BaseDelegateMultiAdapter<SettingBaseModel, SettingListAdapter.ViewHolder>(settingList.toMutableList())
{
    companion object
    {
        const val ITEM_TYPE_DIVIDE = 1
        const val ITEM_TYPE_BOOLEAN = 2
        const val ITEM_TYPE_NORMAL = 3
    }

    init
    {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<SettingBaseModel>()
        {
            override fun getItemType(data: List<SettingBaseModel>, position: Int): Int
            {
                return when (data[position])
                {
                    is SettingDivideModel  -> ITEM_TYPE_DIVIDE
                    is SettingBooleanModel -> ITEM_TYPE_BOOLEAN
                    is SettingNormalModel  -> ITEM_TYPE_NORMAL
                    else                   -> ITEM_TYPE_DIVIDE
                }
            }
        })

        getMultiTypeDelegate()?.let {
            it.addItemType(ITEM_TYPE_DIVIDE, R.layout.item_setting_divide)
            it.addItemType(ITEM_TYPE_BOOLEAN, R.layout.item_setting_boolean)
            it.addItemType(ITEM_TYPE_NORMAL, R.layout.item_setting_normal)
        }
    }

    override fun convert(helper: ViewHolder, item: SettingBaseModel)
    {
        when (item)
        {
            is SettingNormalModel  -> helper.bindNormalViewHolder(item)
            is SettingBooleanModel -> helper.bindBooleanViewHolder(item)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindBooleanViewHolder(settingItem: SettingBooleanModel)
        {
            val isChecked = settingItem.getConfig()
            itemView.tv_boolean_setting_name.text = settingItem.settingName
            itemView.iv_boolean_setting_checked.isSelected = isChecked

            itemView.iv_boolean_setting_checked.setOnClickListener {
                if (settingItem.needSave)
                {
                    settingItem.saveConfig(!isChecked)
                    notifyDataSetChanged()
                }
                settingItem.onItemClickListener.invoke(!isChecked)
            }
        }

        fun bindNormalViewHolder(settingItem: SettingNormalModel)
        {
            itemView.tv_normal_setting_name.text = settingItem.settingName

            itemView.setOnClickListener { settingItem.onItemClickListener.invoke() }
        }
    }
}