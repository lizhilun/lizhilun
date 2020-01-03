package com.lizl.demo.passwordbox.model.settingmodel

class SettingBooleanItem(override val settingName: String, override val settingKey: String, val checked: Boolean, val needSave: Boolean,
        val onItemClickListener: (result: Boolean) -> Unit) : SettingBaseItem(settingName, settingKey)