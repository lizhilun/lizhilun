package com.lizl.demo.passwordbox.adapter

import android.view.View
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.settingmodel.SettingBaseItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingBooleanItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingDivideItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingNormalItem
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*

class SettingListAdapter(settingList: List<SettingBaseItem>) :
        BaseDelegateMultiAdapter<SettingBaseItem, SettingListAdapter.ViewHolder>(settingList.toMutableList())
{
    companion object
    {
        const val ITEM_TYPE_DIVIDE = 1
        const val ITEM_TYPE_BOOLEAN = 2
        const val ITEM_TYPE_NORMAL = 3
    }

    init
    {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<SettingBaseItem>()
        {
            override fun getItemType(data: List<SettingBaseItem>, position: Int): Int
            {
                return when (data[position])
                {
                    is SettingDivideItem  -> ITEM_TYPE_DIVIDE
                    is SettingBooleanItem -> ITEM_TYPE_BOOLEAN
                    is SettingNormalItem  -> ITEM_TYPE_NORMAL
                    else                  -> ITEM_TYPE_DIVIDE
                }
            }
        })

        getMultiTypeDelegate()?.let {
            it.addItemType(ITEM_TYPE_DIVIDE, R.layout.item_setting_divide)
            it.addItemType(ITEM_TYPE_BOOLEAN, R.layout.item_setting_boolean)
            it.addItemType(ITEM_TYPE_NORMAL, R.layout.item_setting_normal)
        }
    }

    override fun convert(helper: ViewHolder, item: SettingBaseItem)
    {
        when (item)
        {
            is SettingNormalItem  -> helper.bindNormalViewHolder(item)
            is SettingBooleanItem -> helper.bindBooleanViewHolder(item)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindBooleanViewHolder(settingItem: SettingBooleanItem)
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

        fun bindNormalViewHolder(settingItem: SettingNormalItem)
        {
            itemView.tv_normal_setting_name.text = settingItem.settingName

            itemView.setOnClickListener { settingItem.onItemClickListener.invoke() }
        }
    }
}