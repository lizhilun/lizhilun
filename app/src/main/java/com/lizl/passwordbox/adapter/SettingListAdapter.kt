package com.lizl.passwordbox.adapter

import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.passwordbox.R
import com.lizl.passwordbox.mvvm.model.settingmodel.SettingBaseModel
import com.lizl.passwordbox.mvvm.model.settingmodel.SettingBooleanModel
import com.lizl.passwordbox.mvvm.model.settingmodel.SettingDivideModel
import com.lizl.passwordbox.mvvm.model.settingmodel.SettingNormalModel
import kotlinx.android.synthetic.main.item_setting_boolean.view.*
import kotlinx.android.synthetic.main.item_setting_normal.view.*

class SettingListAdapter(settingList: List<SettingBaseModel>) : BaseDelegateMultiAdapter<SettingBaseModel, BaseViewHolder>(settingList.toMutableList())
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

    override fun convert(helper: BaseViewHolder, item: SettingBaseModel)
    {
        when (item)
        {
            is SettingNormalModel  -> bindNormalViewHolder(helper, item)
            is SettingBooleanModel -> bindBooleanViewHolder(helper, item)
        }
    }

    private fun bindBooleanViewHolder(helper: BaseViewHolder, settingItem: SettingBooleanModel)
    {
        with(helper.itemView) {
            val isChecked = settingItem.getConfig()
            tv_boolean_setting_name.text = settingItem.settingName
            iv_boolean_setting_checked.isSelected = isChecked

            setOnClickListener {
                if (settingItem.needSave)
                {
                    settingItem.saveConfig(!isChecked)
                    notifyDataSetChanged()
                }
                settingItem.onItemClickListener.invoke(!isChecked)
            }
        }
    }

    private fun bindNormalViewHolder(helper: BaseViewHolder, settingItem: SettingNormalModel)
    {
        with(helper.itemView) {
            tv_normal_setting_name.text = settingItem.settingName
            setOnClickListener { settingItem.onItemClickListener.invoke() }
        }
    }
}