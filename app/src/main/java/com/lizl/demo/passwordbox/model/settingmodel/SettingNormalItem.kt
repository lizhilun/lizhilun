package com.lizl.demo.passwordbox.model.settingmodel

class SettingNormalItem(override val settingName: String, val onItemClickListener: () -> Unit) : SettingBaseItem(settingName)