package com.lizl.demo.passwordbox.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.customview.ScrollTopLayoutManager
import com.lizl.demo.passwordbox.customview.quicksearchbar.OnQuickSideBarTouchListener
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.DataUtil
import com.lizl.demo.passwordbox.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_account_list.*

/**
 * 账号列表界面
 */
class AccountListFragment : BaseFragment(), AccountListAdapter.OnItemClickListener, OnQuickSideBarTouchListener
{
    private lateinit var accountListAdapter: AccountListAdapter

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_account_list
    }

    override fun initView()
    {
        fab_add.setOnClickListener { turnToFragment(R.id.addAccountFragment) }
        iv_search.setOnClickListener { turnToFragment(R.id.searchFragment) }
        iv_setting.setOnClickListener { turnToFragment(R.id.settingFragment) }
        qsb_slide.setOnQuickSideBarTouchListener(this)

        getData()
    }

    private fun getData()
    {
        val accountList: MutableList<AccountModel> = DataUtil.getInstance().queryAll(activity)!!

        accountListAdapter = AccountListAdapter(accountList, this)
        rv_password_list.layoutManager = ScrollTopLayoutManager(activity as Context)
        rv_password_list.adapter = accountListAdapter
    }

    override fun onAccountItemClick(accountModel: AccountModel)
    {
        DialogUtil.showAccountInfoDialog(activity as Context, accountModel)
    }

    override fun onAccountItemLongClick(accountModel: AccountModel): Boolean
    {
        val operationList = mutableListOf<OperationItem>()

        // 修改账号信息
        operationList.add(OperationItem(getString(R.string.modify_account_info), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                val bundle = Bundle()
                bundle.putParcelable(Constant.BUNDLE_DATA, accountModel)
                turnToFragment(R.id.lockPasswordFragment, bundle)
            }
        }))

        // 删除账号
        operationList.add(OperationItem(getString(R.string.delete_account_item), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                DataUtil.getInstance().deleteData(activity, accountModel)
                getData()
            }
        }))

        DialogUtil.showOperationListDialog(activity as Context, operationList)
        return true
    }

    override fun onBackPressed(): Boolean
    {
        return false
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
        if (touching) tv_select_letter.visibility = View.VISIBLE else tv_select_letter.visibility = View.GONE
    }
}