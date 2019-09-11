package com.lizl.demo.passwordbox.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.settingmodel.SettingBaseItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingBooleanItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingDivideItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingNormalItem
import com.lizl.demo.passwordbox.util.UiApplication
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*

class SettingListAdapter(private val settingList: List<SettingBaseItem>) : RecyclerView.Adapter<SettingListAdapter.ViewHolder>()
{
    companion object
    {
        const val ITEM_TYPE_DIVIDE = 1
        const val ITEM_TYPE_BOOLEAN = 2
        const val ITEM_TYPE_NORMAL = 3
    }

    override fun getItemCount(): Int = settingList.size

    override fun getItemViewType(position: Int): Int
    {
        if (position >= itemCount)
        {
            return -1
        }
        return when
        {
            settingList[position] is SettingDivideItem  -> ITEM_TYPE_DIVIDE
            settingList[position] is SettingBooleanItem -> ITEM_TYPE_BOOLEAN
            settingList[position] is SettingNormalItem  -> ITEM_TYPE_NORMAL
            else                                        -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        when (viewType)
        {
            ITEM_TYPE_BOOLEAN -> return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_boolean, parent, false))
            ITEM_TYPE_DIVIDE -> return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_divide, parent, false))
            ITEM_TYPE_NORMAL -> return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_normal, parent, false))
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_divide, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val settingItem = settingList[position]
        if (settingItem is SettingBooleanItem)
        {
            holder.bindBooleanViewHolder(settingItem)
        }
        else if (settingItem is SettingNormalItem)
        {
            holder.bindNormalViewHolder(settingItem)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindBooleanViewHolder(settingItem: SettingBooleanItem)
        {
            val isChecked = UiApplication.instance.getConfigHelper().getBoolean(settingItem.settingKey!!, settingItem.checked)
            itemView.tv_boolean_setting_name.text = settingItem.settingName
            itemView.iv_boolean_setting_checked.isSelected = isChecked

            itemView.iv_boolean_setting_checked.setOnClickListener {
                if (settingItem.needSave)
                {
                    UiApplication.instance.getConfigHelper().putBoolean(settingItem.settingKey!!, !isChecked)
                    notifyDataSetChanged()
                }
                settingItem.settingItemCallBack?.onSettingItemCallBack(!isChecked)
            }
        }

        fun bindNormalViewHolder(settingItem: SettingNormalItem)
        {
            itemView.tv_normal_setting_name.text = settingItem.settingName

            itemView.setOnClickListener { settingItem.settingItemCallBack?.onSettingItemCallBack(true) }
        }
    }
}