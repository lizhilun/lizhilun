package com.lizl.demo.passwordbox.mvvm.fragment

import android.text.TextUtils
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.mvvm.model.AccountModel
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.db.AppDatabase
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.PinyinUtil
import com.lizl.demo.passwordbox.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_add_account.*

/**
 * 添加账号界面
 */
class AddAccountFragment : BaseFragment(R.layout.fragment_add_account)
{
    private var accountModel: AccountModel? = null

    override fun initData()
    {
        accountModel = arguments?.getSerializable(Constant.BUNDLE_DATA_SERIALIZABLE) as AccountModel?
        et_account_description.setText(accountModel?.description)
        et_account.setText(accountModel?.account)
        et_password.setText(accountModel?.password)
    }

    override fun initListener()
    {
        btn_confirm.setOnClickListener { onConfirmButtonClick() }
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
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
        accountModel?.let {
            it.description = description
            it.account = account
            it.password = password
            it.desPinyin = PinyinUtil.getPinyin(description)

            AppDatabase.instance.getAccountDao().insert(it)
        }

        backToPreFragment()
    }

    override fun onBackPressed() = false
}