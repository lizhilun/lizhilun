package com.lizl.demo.passwordbox.fragment

import android.text.TextUtils
import android.view.View
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.ToastUtil
import com.lizl.demo.passwordbox.util.UiApplication
import kotlinx.android.synthetic.main.fragment_lock_password.*

/**
 * 保护密码设置/修改界面
 */
class LockPasswordFragment : BaseFragment()
{
    private var isNeedCurPassword = false
    private var isNeedBackButton = false
    private var isNeedSkipButton = false

    private var fragmentType: Int? = Constant.LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_lock_password
    }

    override fun initView()
    {
        fragmentType = arguments?.getInt(Constant.BUNDLE_DATA)

        isNeedCurPassword = (fragmentType == Constant.LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD)
        isNeedBackButton = (fragmentType != Constant.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)
        isNeedSkipButton = (fragmentType == Constant.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)

        if (isNeedBackButton) iv_back.visibility = View.VISIBLE else iv_back.visibility = View.GONE

        if (isNeedSkipButton) tv_skip.visibility = View.VISIBLE else tv_skip.visibility = View.GONE

        if (isNeedCurPassword)
        {
            et_current_password.visibility = View.VISIBLE
            tv_toolbar_title.text = getString(R.string.modify_lock_password)
        }
        else
        {
            et_current_password.visibility = View.GONE
            tv_toolbar_title.text = getString(R.string.set_lock_password)
        }

        iv_back.setOnClickListener { onBackButtonClick() }
        tv_skip.setOnClickListener { onSkipButtonClick() }
        btn_confirm.setOnClickListener { onConfirmButtonClick() }
    }

    private fun onBackButtonClick()
    {
        backToPreFragment()
    }

    private fun onConfirmButtonClick()
    {
        val curPassword = et_current_password.getText()
        val newPassword = et_new_password.getText()
        val confirmPassword = et_confirm_password.getText()

        if (isNeedCurPassword && curPassword != UiApplication.instance.getAppConfig().getAppLockPassword())
        {
            ToastUtil.showToast(R.string.notify_wrong_cur_password)
            return
        }

        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword) || newPassword != confirmPassword)
        {
            ToastUtil.showToast(R.string.notify_password_not_same)
            return
        }

        UiApplication.instance.getAppConfig().setAppLockPasswordOn(true)
        UiApplication.instance.getAppConfig().setAppLockPassword(newPassword)

        backToPreFragment()
    }

    private fun onSkipButtonClick()
    {
        UiApplication.instance.getAppConfig().setAppLockPasswordOn(false)
        turnToFragment(AccountListFragment())
    }

    override fun onBackPressed(): Boolean
    {
        if (fragmentType == Constant.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)
        {
            return false
        }
        onBackButtonClick()
        return true
    }
}