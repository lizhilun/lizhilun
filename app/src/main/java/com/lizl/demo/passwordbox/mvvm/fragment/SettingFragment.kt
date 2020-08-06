package com.lizl.demo.passwordbox.mvvm.fragment

import android.content.Context
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.SettingListAdapter
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.config.ConfigConstant
import com.lizl.demo.passwordbox.db.AppDatabase
import com.lizl.demo.passwordbox.model.settingmodel.SettingBaseModel
import com.lizl.demo.passwordbox.model.settingmodel.SettingBooleanModel
import com.lizl.demo.passwordbox.model.settingmodel.SettingDivideModel
import com.lizl.demo.passwordbox.model.settingmodel.SettingNormalModel
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.util.*
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * 设置界面
 */
class SettingFragment : BaseFragment(R.layout.fragment_setting)
{
    override fun initView()
    {
        val settingAdapter = SettingListAdapter(getSettingData())
        rv_setting_list.layoutManager = LinearLayoutManager(activity)
        rv_setting_list.adapter = settingAdapter
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
    }

    private fun getSettingData(): List<SettingBaseModel>
    {
        val settingList = mutableListOf<SettingBaseModel>()

        settingList.add(SettingDivideModel())

        // 密码保护是否可用（密码保护为开且密码非空）
        val isAppLockPasswordOn = AppConfig.isAppLockPasswordOn() && !TextUtils.isEmpty(AppConfig.getAppLockPassword())

        // 支持指纹识别的情况显示指纹解锁设置
        if (BiometricAuthenticationUtil.isFingerprintSupport())
        {
            settingList.add(SettingBooleanModel(getString(R.string.setting_fingerprint), ConfigConstant.IS_FINGERPRINT_LOCK_ON,
                    ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON, isAppLockPasswordOn) {
                if (isAppLockPasswordOn || !it)
                {
                    return@SettingBooleanModel
                }

                turnToFragment(R.id.lockPasswordFragment, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD)
            })
        }

        // 密码保护可用的情况显示修改密码界面
        if (isAppLockPasswordOn)
        {
            settingList.add(SettingNormalModel(getString(R.string.setting_modify_lock_password)) {
                turnToFragment(R.id.lockPasswordFragment, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD)
            })
        }
        // 反之显示设置密码界面
        else
        {
            settingList.add(SettingNormalModel(getString(R.string.setting_set_lock_password)) {
                turnToFragment(R.id.lockPasswordFragment, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD)
            })
        }

        settingList.add(SettingDivideModel())

        // 自动备份
        settingList.add(SettingBooleanModel(getString(R.string.setting_auto_backup), ConfigConstant.IS_AUTO_BACKUP,
                ConfigConstant.DEFAULT_IS_AUTO_BACKUP, true) {})

        // 备份数据设置
        settingList.add(SettingNormalModel(getString(R.string.setting_backup_data)) {
            if (AppDatabase.instance.getAccountDao().getDiariesCount() == 0)
            {
                ToastUtil.showToast(R.string.notify_no_data_to_backup)
                return@SettingNormalModel
            }

            // 保护密码不可用的情况下先设置密码
            if (!isAppLockPasswordOn)
            {
                turnToFragment(R.id.lockPasswordFragment, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD)
            }
            else
            {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.setting_backup_data),
                        getString(R.string.notify_backup_data)) { backupData() }
            }
        })

        // 还原数据设置
        settingList.add(SettingNormalModel(getString(R.string.setting_restore_data)) { turnToBackupFileFragment() })

        return settingList
    }

    fun backupData()
    {
        BackupUtil.backupData { ToastUtil.showToast(R.string.notify_success_to_backup) }
    }

    fun turnToBackupFileFragment()
    {
        turnToFragment(R.id.backupFileListFragment)
    }

    override fun onBackPressed() = false
}