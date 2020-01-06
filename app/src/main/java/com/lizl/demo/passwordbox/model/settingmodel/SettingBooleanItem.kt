package com.lizl.demo.passwordbox.model.settingmodel

import com.blankj.utilcode.util.SPUtils

class SettingBooleanItem(override val settingName: String, override val settingKey: String, val defaultValue: Boolean, val needSave: Boolean,
                         val onItemClickListener: (result: Boolean) -> Unit) : SettingBaseItem(settingName, settingKey)
{
    fun getConfig() = SPUtils.getInstance().getBoolean(settingKey, defaultValue)

    fun saveConfig(config: Boolean) = SPUtils.getInstance().put(settingKey, config)
}