package com.lizl.demo.passwordbox.mvp.fragment

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.SettingListAdapter
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.config.ConfigConstant
import com.lizl.demo.passwordbox.model.settingmodel.SettingBaseItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingBooleanItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingDivideItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingNormalItem
import com.lizl.demo.passwordbox.mvp.presenter.EmptyPresenter
import com.lizl.demo.passwordbox.util.*
import kotlinx.android.synthetic.main.fragment_setting.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

/**
 * 设置界面
 */
@RuntimePermissions
class SettingFragment : BaseFragment<EmptyPresenter>()
{

    override fun getLayoutResId() = R.layout.fragment_setting

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        val settingAdapter = SettingListAdapter(getSettingData())
        rv_setting_list.layoutManager = LinearLayoutManager(activity)
        rv_setting_list.addItemDecoration(DividerItemDecoration(activity as Context, DividerItemDecoration.VERTICAL))
        rv_setting_list.adapter = settingAdapter

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
    }

    private fun getSettingData(): List<SettingBaseItem>
    {
        val settingList = mutableListOf<SettingBaseItem>()

        settingList.add(SettingDivideItem())

        // 密码保护是否可用（密码保护为开且密码非空）
        val isAppLockPasswordOn = AppConfig.isAppLockPasswordOn() && !TextUtils.isEmpty(AppConfig.getAppLockPassword())

        // 支持指纹识别的情况显示指纹解锁设置
        if (AppConfig.isAppFingerprintSupport())
        {
            settingList.add(SettingBooleanItem(getString(R.string.setting_fingerprint), ConfigConstant.IS_FINGERPRINT_LOCK_ON,
                    ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON, isAppLockPasswordOn) {
                if (isAppLockPasswordOn || !it)
                {
                    return@SettingBooleanItem
                }

                val bundle = Bundle()
                bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD)
                turnToFragment(R.id.lockPasswordFragment, bundle)
            })
        }

        // 密码保护可用的情况显示修改密码界面
        if (isAppLockPasswordOn)
        {
            settingList.add(SettingNormalItem(getString(R.string.setting_modify_lock_password)) {
                val bundle = Bundle()
                bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD)
                turnToFragment(R.id.lockPasswordFragment, bundle)
            })
        }
        // 反之显示设置密码界面
        else
        {
            settingList.add(SettingNormalItem(getString(R.string.setting_set_lock_password)) {
                val bundle = Bundle()
                bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD)
                turnToFragment(R.id.lockPasswordFragment, bundle)
            })
        }

        settingList.add(SettingDivideItem())

        // 备份数据设置
        settingList.add(SettingNormalItem(getString(R.string.setting_backup_data)) {
            if (DataUtil.getInstance().queryAll().isEmpty())
            {
                ToastUtil.showToast(R.string.notify_no_data_to_backup)
                return@SettingNormalItem
            }

            // 保护密码不可用的情况下先设置密码
            if (!isAppLockPasswordOn)
            {
                val bundle = Bundle()
                bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD)
                turnToFragment(R.id.lockPasswordFragment, bundle)
            }
            else
            {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.setting_backup_data),
                        getString(R.string.notify_backup_data)) { backupDataWithPermissionCheck() }
            }
        })

        // 还原数据设置
        settingList.add(SettingNormalItem(getString(R.string.setting_restore_data)) { turnToBackupFileFragmentWithPermissionCheck() })

        return settingList
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun backupData()
    {
        BackupUtil.backupData(object : BackupUtil.DataBackupCallback
        {
            override fun onDataBackupSuccess()
            {
                ToastUtil.showToast(R.string.notify_success_to_backup)
            }

            override fun onDataBackupFailed(reason: String)
            {
                //do nothing
            }
        })
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun turnToBackupFileFragment()
    {
        turnToFragment(R.id.backupFileListFragment)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied()
    {
        ToastUtil.showToast(R.string.notify_failed_to_get_permission)
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionNeverAskAgain()
    {
        DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused)) { UiUtil.goToAppDetailPage() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onBackPressed() = false
}