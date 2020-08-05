package com.lizl.demo.passwordbox.mvvm.fragment

import android.content.Context
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.custom.view.recyclerview.ScrollTopLayoutManager
import com.lizl.demo.passwordbox.custom.view.quicksearchbar.OnQuickSideBarTouchListener
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationModel
import com.lizl.demo.passwordbox.model.TitleBarBtnModel
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.db.AppDatabase
import com.lizl.demo.passwordbox.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_account_list.*

/**
 * 账号列表界面
 */
class AccountListFragment : BaseFragment(R.layout.fragment_account_list), OnQuickSideBarTouchListener
{
    private val accountListAdapter = AccountListAdapter()

    override fun initView()
    {
        fab_add.setOnClickListener { turnToFragment(R.id.addAccountFragment) }

        val titleBtnList = mutableListOf<TitleBarBtnModel.BaseModel>().apply {
            add(TitleBarBtnModel.ImageBtnModel(R.mipmap.ic_setting) { turnToFragment(R.id.settingFragment) })
            add(TitleBarBtnModel.ImageBtnModel(R.mipmap.ic_search) { turnToFragment(R.id.searchFragment) })
        }
        ctb_title.setBtnList(titleBtnList)

        rv_password_list.layoutManager = ScrollTopLayoutManager(activity as Context)
        rv_password_list.adapter = accountListAdapter
    }

    override fun initData()
    {
        AppDatabase.instance.getAccountDao().getAllDiaryLiveData().observe(this, Observer {
            accountListAdapter.setData(it)
        })
    }

    override fun initListener()
    {
        accountListAdapter.setOnAccountItemClickListener { DialogUtil.showAccountInfoDialog(activity as Context, it) }

        accountListAdapter.setOnAccountItemLongClickListener { onAccountItemLongClick(it) }

        qsb_slide.setOnQuickSideBarTouchListener(this)
    }

    private fun onAccountItemLongClick(accountModel: AccountModel): Boolean
    {
        val operationList = mutableListOf<OperationModel>().apply {

            // 修改账号信息
            add(OperationModel(getString(R.string.modify_account_info)) { turnToFragment(R.id.addAccountFragment, accountModel) })

            // 删除账号
            add(OperationModel(getString(R.string.delete_account_item)) {
                AppDatabase.instance.getAccountDao().delete(accountModel)
            })
        }

        DialogUtil.showOperationListDialog(activity as Context, operationList)
        return true
    }

    override fun onLetterChanged(letter: String, position: Int, y: Float)
    {
        tv_select_letter.text = letter

        val firstLetterPosition = accountListAdapter.getFirstLetterPosition(letter[0])
        if (firstLetterPosition >= 0)
        {
            rv_password_list.smoothScrollToPosition(firstLetterPosition)
        }
    }

    override fun onLetterTouching(touching: Boolean)
    {
        tv_select_letter.isVisible = touching
    }
}