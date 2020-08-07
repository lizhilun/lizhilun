package com.lizl.demo.passwordbox.mvvm.model.settingmodel

class SettingNormalModel(override val settingName: String, val onItemClickListener: () -> Unit) : SettingBaseModel(settingName)