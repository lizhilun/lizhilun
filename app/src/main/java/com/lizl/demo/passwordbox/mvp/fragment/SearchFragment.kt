package com.lizl.demo.passwordbox.mvp.fragment

import android.content.Context
import android.text.InputFilter
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.mvp.contract.SearchContract
import com.lizl.demo.passwordbox.mvp.presenter.SearchPresenter
import com.lizl.demo.passwordbox.util.*
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * 搜索界面
 */
class SearchFragment : BaseFragment<SearchPresenter>(), SearchContract.View
{
    private lateinit var accountListAdapter: AccountListAdapter

    override fun getLayoutResId() = R.layout.fragment_search

    override fun initPresenter() = SearchPresenter(this)

    override fun initView()
    {
        iv_back.setOnClickListener { onBackButtonClick() }
        iv_cancel.setOnClickListener { et_search.setText("") }

        iv_cancel.visibility = View.GONE

        et_search.filters = arrayOf(InputFilter.LengthFilter(20), UiUtil.getNoWrapOrSpaceFilter())

        accountListAdapter = AccountListAdapter()
        rv_result_list.layoutManager = LinearLayoutManager(activity)
        rv_result_list.adapter = accountListAdapter

        et_search.addTextChangedListener {
            presenter.search(it.toString())
            iv_cancel.isVisible = it.toString().isNotEmpty()
        }

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

        accountListAdapter.setOnAccountItemClickListener { DialogUtil.showAccountInfoDialog(activity as Context, it) }

        accountListAdapter.setOnAccountItemLongClickListener { onAccountItemLongClick(it) }

        UiUtil.showSoftKeyboard(et_search)
    }

    override fun showSearchResult(accountList: List<AccountModel>)
    {
        accountListAdapter.setData(accountList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            android.R.id.home -> onBackButtonClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onAccountItemLongClick(accountModel: AccountModel)
    {
        val operationList = mutableListOf<OperationItem>()

        operationList.add(OperationItem(getString(R.string.modify_account_info)) {
            turnToFragment(R.id.addAccountFragment, accountModel)
        })

        operationList.add(OperationItem(getString(R.string.delete_account_item)) {
            AppDatabase.instance.getAccountDao().delete(accountModel)

            presenter.search(et_search.text.toString())
        })

        DialogUtil.showOperationListDialog(activity as Context, operationList)
    }

    override fun onStop()
    {
        super.onStop()

        UiUtil.hideSoftKeyboard(et_search)
    }

    private fun onBackButtonClick()
    {
        backToPreFragment()
    }

    override fun onBackPressed() = false
}