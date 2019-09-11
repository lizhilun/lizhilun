package com.lizl.demo.passwordbox.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.SettingListAdapter
import com.lizl.demo.passwordbox.config.ConfigConstant
import com.lizl.demo.passwordbox.customview.dialog.DialogOperationConfirm
import com.lizl.demo.passwordbox.model.settingmodel.SettingBaseItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingBooleanItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingDivideItem
import com.lizl.demo.passwordbox.model.settingmodel.SettingNormalItem
import com.lizl.demo.passwordbox.util.*
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * 设置界面
 */
class SettingFragment : BaseFragment()
{
    private val REQUEST_CODE_READ_EX_PERMISSION_FOR_BACKUP = 1
    private val REQUEST_CODE_READ_EX_PERMISSION_FOR_RESTORE = 2

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_setting
    }

    override fun initView()
    {
        val settingAdapter = SettingListAdapter(getSettingData())
        rv_setting_list.layoutManager = LinearLayoutManager(activity)
        rv_setting_list.addItemDecoration(
                DividerItemDecoration(activity as Context, DividerItemDecoration.VERTICAL))
        rv_setting_list.adapter = settingAdapter

        iv_back.setOnClickListener { onBackButtonClick() }
    }

    private fun getSettingData(): List<SettingBaseItem>
    {
        val settingList = mutableListOf<SettingBaseItem>()

        settingList.add(SettingDivideItem())

        // 密码保护是否可用（密码保护为开且密码非空）
        val isAppLockPasswordOn = UiApplication.instance.getAppConfig().isAppLockPasswordOn() && !TextUtils.isEmpty(UiApplication.instance.getAppConfig().getAppLockPassword())

        // 支持指纹识别的情况显示指纹解锁设置
        if (UiApplication.instance.getAppConfig().isAppFingerprintSupport())
        {
            settingList.add(
                    SettingBooleanItem(getString(R.string.setting_fingerprint), ConfigConstant.IS_FINGERPRINT_LOCK_ON, ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON, isAppLockPasswordOn, object : SettingBaseItem.SettingItemCallBack
                    {
                        override fun onSettingItemCallBack(result: Boolean)
                        {
                            if (isAppLockPasswordOn || !result)
                            {
                                return
                            }

                            val bundle = Bundle()
                            bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD)
                            turnToFragment(R.id.lockPasswordFragment, bundle)
                        }
                    })
            )
        }

        // 密码保护可用的情况显示修改密码界面
        if (isAppLockPasswordOn)
        {
            settingList.add(SettingNormalItem(getString(R.string.setting_modify_lock_password), object : SettingBaseItem.SettingItemCallBack
            {
                override fun onSettingItemCallBack(result: Boolean)
                {
                    val bundle = Bundle()
                    bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD)
                    turnToFragment(R.id.lockPasswordFragment, bundle)
                }
            }))
        }
        // 反之显示设置密码界面
        else
        {
            settingList.add(SettingNormalItem(getString(R.string.setting_set_lock_password), object : SettingBaseItem.SettingItemCallBack
            {
                override fun onSettingItemCallBack(result: Boolean)
                {
                    val bundle = Bundle()
                    bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD)
                    turnToFragment(R.id.lockPasswordFragment, bundle)
                }
            }))
        }

        settingList.add(SettingDivideItem())

        // 备份数据设置
        settingList.add(SettingNormalItem(getString(R.string.setting_backup_data), object : SettingBaseItem.SettingItemCallBack
        {
            override fun onSettingItemCallBack(result: Boolean)
            {
                if (DataUtil.getInstance().queryAll(activity as Context)?.isEmpty()!!)
                {
                    ToastUtil.showToast(R.string.notify_no_data_to_backup)
                    return
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
                    DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.setting_backup_data), getString(R.string.notify_backup_data), object : DialogOperationConfirm.OperationConfirmCallback
                    {
                        override fun onOperationConfirmed()
                        {
                            if (checkWriteStoragePermission(REQUEST_CODE_READ_EX_PERMISSION_FOR_BACKUP))
                            {
                                backupData()
                            }
                        }
                    })
                }
            }
        }))

        // 还原数据设置
        settingList.add(SettingNormalItem(getString(R.string.setting_restore_data), object : SettingBaseItem.SettingItemCallBack
        {
            override fun onSettingItemCallBack(result: Boolean)
            {
                if (checkWriteStoragePermission(REQUEST_CODE_READ_EX_PERMISSION_FOR_RESTORE))
                {
                    turnToFragment(R.id.backupFileListFragment)
                }
            }
        }))

        return settingList
    }

    private fun backupData()
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

    /**
     * 检查内部存储读取权限
     */
    private fun checkWriteStoragePermission(requestCode: Int): Boolean
    {
        // 权限已获取直接返回
        if (ContextCompat.checkSelfPermission(UiApplication.instance, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        this@SettingFragment.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        if (grantResults.isEmpty())
        {
            return
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            when (requestCode)
            {
                REQUEST_CODE_READ_EX_PERMISSION_FOR_BACKUP -> backupData()
                REQUEST_CODE_READ_EX_PERMISSION_FOR_RESTORE -> turnToFragment(R.id.backupFileListFragment)
            }
        }
        else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                ToastUtil.showToast(R.string.notify_failed_to_get_permission)
            }
            else
            {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.notify_failed_to_get_permission), getString(R.string.notify_permission_be_refused), object : DialogOperationConfirm.OperationConfirmCallback
                {
                    override fun onOperationConfirmed()
                    {
                        UiUtil.goToAppDetailPage()
                    }
                })
            }
        }
    }

    private fun onBackButtonClick()
    {
        backToPreFragment()
    }

    override fun onBackPressed(): Boolean
    {
        onBackButtonClick()
        return true
    }
}