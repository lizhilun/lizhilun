package com.lizl.demo.passwordbox.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.customview.dialog.DialogAccountInfo
import com.lizl.demo.passwordbox.customview.dialog.DialogOperationList
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.DataUtil
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * 搜索界面
 */
class SearchFragment : BaseFragment(), AccountListAdapter.OnItemClickListener
{
    private var accountListAdapter: AccountListAdapter? = null
    private lateinit var allAccountList: MutableList<AccountModel>

    private var dialogAccountInfo: DialogAccountInfo? = null
    private var dialogOperationList: DialogOperationList? = null

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_search
    }

    override fun initView()
    {
        allAccountList = DataUtil.getInstance().queryAll(activity)!!

        iv_back.setOnClickListener { onBackButtonClick() }
        iv_cancel.setOnClickListener { et_search.setText("") }

        iv_cancel.visibility = View.GONE

        et_search.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                getSearchResult(s.toString())
                if (s.toString().isNotEmpty())
                {
                    iv_cancel.visibility = View.VISIBLE
                }
                else
                {
                    iv_cancel.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                //do nothing
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item?.itemId)
        {
            android.R.id.home -> onBackButtonClick()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getSearchResult(keyword: String?)
    {
        if (keyword == null)
        {
            return
        }
        val resultList = DataUtil.getInstance().searchByKeyword(keyword)
        accountListAdapter = AccountListAdapter(activity as Context, resultList, this)
        rv_result_list.layoutManager = LinearLayoutManager(activity)
        rv_result_list.adapter = accountListAdapter
    }

    override fun onAccountItemClick(accountModel: AccountModel)
    {
        dialogAccountInfo = DialogAccountInfo(activity as Context, accountModel)
        dialogAccountInfo?.show()
    }

    override fun onAccountItemLongClick(accountModel: AccountModel): Boolean
    {
        val operationList = mutableListOf<OperationItem>()

        operationList.add(OperationItem(getString(R.string.modify_account_info), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                dialogOperationList?.dismiss()

                val bundle = Bundle()
                bundle.putParcelable(Constant.BUNDLE_DATA, accountModel)
                turnToFragment(AddAccountFragment(), bundle)
            }
        }))

        operationList.add(OperationItem(getString(R.string.delete_account_item), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                dialogOperationList?.dismiss()

                DataUtil.getInstance().deleteData(activity, accountModel)

                allAccountList = DataUtil.getInstance().queryAll(activity)!!
                getSearchResult(et_search.text.toString())
            }
        }))

        dialogOperationList = DialogOperationList(activity as Context, operationList)
        dialogOperationList?.show()
        return true
    }

    override fun onPause()
    {
        super.onPause()

        dialogAccountInfo?.dismiss()
        dialogOperationList?.dismiss()
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