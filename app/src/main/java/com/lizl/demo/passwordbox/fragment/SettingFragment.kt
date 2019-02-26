package com.lizl.demo.passwordbox.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
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
    private val REQUEST_CODE_READ_EX_PERMISSION = 1

    private var dialogOperationConfirm: DialogOperationConfirm? = null

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_setting
    }

    override fun initView()
    {
        val settingAdapter = SettingListAdapter(getSettingData())
        rv_setting_list.layoutManager = LinearLayoutManager(activity)
        rv_setting_list.addItemDecoration(DividerItemDecoration(activity as Context, DividerItemDecoration.VERTICAL))
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
                            turnToFragment(LockPasswordFragment(), bundle)
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
                    turnToFragment(LockPasswordFragment(), bundle)
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
                    turnToFragment(LockPasswordFragment(), bundle)
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
                    turnToFragment(LockPasswordFragment(), bundle)
                }
                else
                {
                    dialogOperationConfirm = DialogOperationConfirm(activity as Context, getString(R.string.setting_backup_data), getString(R.string.notify_backup_data), object : DialogOperationConfirm.OperationConfirmCallback
                    {
                        override fun onOperationConfirmed()
                        {
                            if (checkReadStoragePermission())
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
                        }
                    })
                    dialogOperationConfirm?.show()
                }
            }
        }))

        // 还原数据设置
        settingList.add(SettingNormalItem(getString(R.string.setting_restore_data), object : SettingBaseItem.SettingItemCallBack
        {
            override fun onSettingItemCallBack(result: Boolean)
            {
                if (checkReadStoragePermission())
                {
                    turnToFragment(BackupFileListFragment())
                }
            }
        }))

        return settingList
    }

    /**
     * 检查内部存储读取权限
     */
    private fun checkReadStoragePermission(): Boolean
    {
        // 权限以获取直接返回
        if (ContextCompat.checkSelfPermission(UiApplication.instance, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        // 权限被拒绝过但是用户没有设置权限弹窗不再弹出的情况下继续申请权限
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity as Activity, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EX_PERMISSION)
        }
        // 权限被拒绝且不再允许弹出申请权限弹窗的情况下弹出跳转到APP详情确认弹窗（用于重新设置权限）
        else
        {
            dialogOperationConfirm = DialogOperationConfirm(activity as Context, getString(R.string.notify_failed_to_get_permission), getString(R.string.notify_permission_be_refused), object : DialogOperationConfirm.OperationConfirmCallback
            {
                override fun onOperationConfirmed()
                {
                    UiUtil.goToAppDetailPage()
                }
            })
            dialogOperationConfirm?.show()
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED)
        {
            ToastUtil.showToast(R.string.notify_failed_to_get_permission)
        }
    }

    override fun onPause()
    {
        super.onPause()

        dialogOperationConfirm?.dismiss()
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