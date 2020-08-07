package com.lizl.demo.passwordbox.mvvm.model.settingmodel

import com.lizl.demo.passwordbox.config.util.ConfigUtil

class SettingBooleanModel(override val settingName: String, override val settingKey: String, val needSave: Boolean,
                          val onItemClickListener: (result: Boolean) -> Unit) : SettingBaseModel(settingName, settingKey)
{
    fun getConfig() = ConfigUtil.getBoolean(settingKey)

    fun saveConfig(config: Boolean) = ConfigUtil.set(settingKey, config)
}