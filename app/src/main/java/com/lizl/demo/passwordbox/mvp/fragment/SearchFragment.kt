package com.lizl.demo.passwordbox.mvp.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.mvp.presenter.EmptyPresenter
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.DataUtil
import com.lizl.demo.passwordbox.util.DialogUtil
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * 搜索界面
 */
class SearchFragment : BaseFragment<EmptyPresenter>(), AccountListAdapter.OnItemClickListener
{
    private var accountListAdapter: AccountListAdapter? = null
    private lateinit var allAccountList: MutableList<AccountModel>

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_search
    }

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        allAccountList = DataUtil.getInstance().queryAll()

        iv_back.setOnClickListener { onBackButtonClick() }
        iv_cancel.setOnClickListener { et_search.setText("") }

        iv_cancel.visibility = View.GONE

        UiUtil.showSoftKeyboard(et_search)
        et_search.filters = arrayOf(InputFilter.LengthFilter(20), UiUtil.getNoWrapOrSpaceFilter())

        et_search.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                getSearchResult(s.toString())
                iv_cancel.visibility = if (s.toString().isNotEmpty()) View.VISIBLE else View.GONE
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

        et_search.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER)
            {
                if (event.action == KeyEvent.ACTION_DOWN)
                {
                    UiUtil.hideSoftKeyboard(et_search)
                }
                true
            }
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
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
        accountListAdapter = AccountListAdapter(resultList, this)
        rv_result_list.layoutManager = LinearLayoutManager(activity)
        rv_result_list.adapter = accountListAdapter
    }

    override fun onAccountItemClick(accountModel: AccountModel)
    {
        DialogUtil.showAccountInfoDialog(activity as Context, accountModel)
    }

    override fun onAccountItemLongClick(accountModel: AccountModel): Boolean
    {
        val operationList = mutableListOf<OperationItem>()

        operationList.add(OperationItem(getString(R.string.modify_account_info)) {
            val bundle = Bundle()
            bundle.putSerializable(Constant.BUNDLE_DATA, accountModel)
            turnToFragment(R.id.addAccountFragment, bundle)
        })

        operationList.add(OperationItem(getString(R.string.delete_account_item)) {
            DataUtil.getInstance().deleteData(accountModel)

            allAccountList = DataUtil.getInstance().queryAll()
            getSearchResult(et_search.text.toString())
        })

        DialogUtil.showOperationListDialog(activity as Context, operationList)
        return true
    }

    override fun onStop()
    {
        super.onStop()

        UiUtil.hideInputKeyboard(et_search)
    }

    private fun onBackButtonClick()
    {
        backToPreFragment()
    }

    override fun onBackPressed(): Boolean
    {
        return false
    }
}