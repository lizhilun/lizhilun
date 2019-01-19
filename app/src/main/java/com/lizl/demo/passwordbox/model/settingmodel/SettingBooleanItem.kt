package com.lizl.demo.passwordbox.model.settingmodel

class SettingBooleanItem(settingName: String, settingKey: String, checked: Boolean, needSave: Boolean, callback: SettingItemCallBack?) : SettingBaseItem()
{
    var checked: Boolean = false
    var needSave: Boolean = true

    init
    {
        this.settingName = settingName
        this.settingKey = settingKey
        this.settingItemCallBack = callback
        this.checked = checked
        this.needSave = needSave
    }
}