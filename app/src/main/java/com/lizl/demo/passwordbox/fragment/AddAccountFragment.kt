package com.lizl.demo.passwordbox.fragment

import android.text.TextUtils
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.customview.CustomTitleBar
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.TitleBarBtnItem
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.DataUtil
import com.lizl.demo.passwordbox.util.PinyinUtil
import com.lizl.demo.passwordbox.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_add_account.*

/**
 * 添加账号界面
 */
class AddAccountFragment : BaseFragment()
{
    private var accountModel: AccountModel? = null

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_add_account
    }

    override fun initView()
    {
        btn_confirm.setOnClickListener { onConfirmButtonClick() }

        accountModel = arguments?.getParcelable(Constant.BUNDLE_DATA)

        et_account_description.setText(accountModel?.description)
        et_account.setText(accountModel?.account)
        et_password.setText(accountModel?.password)

        ctb_title.setOnBackBtnClickListener(object : CustomTitleBar.OnBackBtnClickListener
        {
            override fun onBackBtnClick()
            {
                backToPreFragment()
            }
        })
    }

    private fun onConfirmButtonClick()
    {
        val description = et_account_description.getText()
        val account = et_account.getText()
        val password = et_password.getText()

        // 判断每个信息是否正确填入
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(account) || TextUtils.isEmpty(password))
        {
            ToastUtil.showToast(R.string.notify_account_info_not_complete)
            return
        }

        if (accountModel == null)
        {
            accountModel = AccountModel()
        }
        accountModel?.description = description
        accountModel?.account = account
        accountModel?.password = password
        accountModel?.desPinyin = PinyinUtil.getPinyin(description)

        DataUtil.getInstance().saveData(activity, accountModel!!)

        backToPreFragment()
    }

    override fun onBackPressed(): Boolean
    {
        return false
    }
}