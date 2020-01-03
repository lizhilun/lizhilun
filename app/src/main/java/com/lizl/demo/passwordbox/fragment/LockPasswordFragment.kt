package com.lizl.demo.passwordbox.fragment

import android.text.TextUtils
import android.view.View
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.customview.CustomTitleBar
import com.lizl.demo.passwordbox.model.TitleBarBtnItem
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.ToastUtil
import com.lizl.demo.passwordbox.UiApplication
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_lock_password.*

/**
 * 保护密码设置/修改界面
 */
class LockPasswordFragment : BaseFragment()
{
    private var fragmentType: Int? = Constant.LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_lock_password
    }

    override fun initView()
    {
        fragmentType = arguments?.getInt(Constant.BUNDLE_DATA)

        ctb_title.setOnBackBtnClickListener(object : CustomTitleBar.OnBackBtnClickListener
        {
            override fun onBackBtnClick()
            {
                backToPreFragment()
            }
        })

        when (fragmentType)
        {
            Constant.LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD       ->
            {
                et_current_password.visibility = View.GONE
                ctb_title.setTitleText(getString(R.string.set_lock_password))
                ctb_title.setBackBtnVisible(true)
            }
            Constant.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD ->
            {
                et_current_password.visibility = View.GONE
                ctb_title.setTitleText(getString(R.string.set_lock_password))
                ctb_title.setBackBtnVisible(false)
                val titleBtnList = mutableListOf<TitleBarBtnItem.BaseItem>()
                titleBtnList.add(TitleBarBtnItem.TextBtnItem(getString(R.string.skip), object : TitleBarBtnItem.OnBtnClickListener
                {
                    override fun onBtnClick()
                    {
                        UiApplication.instance.getAppConfig().setAppLockPasswordOn(false)
                        turnToFragment(R.id.accountListFragment)
                    }
                }))
                ctb_title.setBtnList(titleBtnList)
            }
            Constant.LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD    ->
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

        if (et_current_password.visibility == View.VISIBLE && curPassword != UiApplication.instance.getAppConfig().getAppLockPassword())
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

    override fun onBackPressed(): Boolean
    {
        if (fragmentType == Constant.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)
        {
            UiUtil.backToLauncher()
            return true
        }
        return false
    }
}