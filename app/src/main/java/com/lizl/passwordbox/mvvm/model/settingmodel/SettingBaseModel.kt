package com.lizl.passwordbox.mvvm.model.settingmodel

open class SettingBaseModel(open val settingName: String?, open val settingKey: String? = null)
{
    constructor() : this(null, null)

    constructor(settingName: String?) : this(settingName, null)
}