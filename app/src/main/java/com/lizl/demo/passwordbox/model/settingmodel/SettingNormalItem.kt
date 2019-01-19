package com.lizl.demo.passwordbox.model.settingmodel

class SettingNormalItem(settingName: String, callBack: SettingItemCallBack) : SettingBaseItem()
{
    init
    {
        this.settingName = settingName
        this.settingItemCallBack = callBack
    }
}