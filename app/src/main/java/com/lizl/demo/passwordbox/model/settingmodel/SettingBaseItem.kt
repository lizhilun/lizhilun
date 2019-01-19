package com.lizl.demo.passwordbox.model.settingmodel

open class SettingBaseItem
{
    var settingName: String? = null
    var settingKey: String? = null
    var settingItemCallBack: SettingItemCallBack? = null

    public interface SettingItemCallBack
    {
        fun onSettingItemCallBack(result: Boolean)
    }
}