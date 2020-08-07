package com.lizl.demo.passwordbox.mvvm.fragment

import android.text.TextUtils
import android.view.View
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.mvvm.model.TitleBarBtnModel
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.ToastUtil
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_lock_password.*

/**
 * 保护密码设置/修改界面
 */
class LockPasswordFragment : BaseFragment(R.layout.fragment_lock_password)
{
    private var fragmentType: Int? = LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD

    companion object
    {
        const val LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD = 1 //保护密码设置界面类型：设置密码
        const val LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD = 2  //保护密码设置界面类型：应用第一次启动时设置密码
        const val LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD = 3 //保护密码设置界面类型：修改密码
    }

    override fun initView()
    {
        fragmentType = arguments?.getInt(Constant.BUNDLE_DATA_INT)

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        when (fragmentType)
        {
            LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD       ->
            {
                et_current_password.visibility = View.GONE
                ctb_title.setTitleText(getString(R.string.set_lock_password))
                ctb_title.setBackBtnVisible(true)
            }
            LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD ->
            {
                et_current_password.visibility = View.GONE
                ctb_title.setTitleText(getString(R.string.set_lock_password))
                ctb_title.setBackBtnVisible(false)
                val titleBtnList = mutableListOf<TitleBarBtnModel.BaseModel>().apply {
                    add(TitleBarBtnModel.TextBtnModel(getString(R.string.skip)) {
                        AppConfig.setAppLockPasswordOn(false)
                        turnToFragment(R.id.accountListFragment)
                    })
                }
                ctb_title.setBtnList(titleBtnList)
            }
            LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD    ->
            {
                et_current_password.visibility = View.VISIBLE
                ctb_title.setTitleText(getString(R.string.modify_lock_password))
                ctb_title.setBackBtnVisible(true)
            }
        }

        btn_confirm.setOnClickListener { onConfirmButtonClick() }
    }

    private fun onConfirmButtonClick()
    {
        val curPassword = et_current_password.getText()
        val newPassword = et_new_password.getText()
        val confirmPassword = et_confirm_password.getText()

        if (et_current_password.visibility == View.VISIBLE && curPassword != AppConfig.getAppLockPassword())
        {
            ToastUtil.showToast(R.string.notify_wrong_cur_password)
            return
        }

        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword) || newPassword != confirmPassword)
        {
            ToastUtil.showToast(R.string.notify_password_not_same)
            return
        }

        AppConfig.setAppLockPasswordOn(true)
        AppConfig.setAppLockPassword(newPassword)

        backToPreFragment()
    }

    override fun onBackPressed(): Boolean
    {
        if (fragmentType == LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)
        {
            UiUtil.backToLauncher()
            return true
        }
        return false
    }
}