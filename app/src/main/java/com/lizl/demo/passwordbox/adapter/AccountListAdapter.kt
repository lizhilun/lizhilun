package com.lizl.demo.passwordbox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.util.PinyinUtil
import kotlinx.android.synthetic.main.item_account.view.*

class AccountListAdapter : RecyclerView.Adapter<AccountListAdapter.ViewHolder>()
{

    private var accountList: MutableList<AccountModel> = mutableListOf()

    private var onAccountItemClickListener: ((AccountModel) -> Unit)? = null
    private var onAccountItemLongClickListener: ((AccountModel) -> Unit)? = null

    fun setData(accountList: List<AccountModel>)
    {
        accountList.sorted()
        this.accountList.clear()
        this.accountList.addAll(accountList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = accountList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindViewHolder(position: Int)
        {
            val accountModel = accountList[position]
            val firstLetter = PinyinUtil.getSortFirstLetter(accountModel.desPinyin)
            var showFirstLetter = true // 是否显示首字母标识
            if (position > 0)
            {
                val lastAccountModel = accountList[position - 1]
                val lastFirstLetter = PinyinUtil.getSortFirstLetter(lastAccountModel.desPinyin)
                // 与上一个item首字母相同的情况下不显示首字母
                if (firstLetter == lastFirstLetter)
                {
                    showFirstLetter = false
                }
            }
            itemView.tv_description.text = accountModel.description
            itemView.tv_account.text = accountModel.account
            itemView.tv_first_letter.text = firstLetter.toString().toUpperCase()

            itemView.tv_first_letter.visibility = if (showFirstLetter) View.VISIBLE else View.INVISIBLE

            itemView.setOnClickListener { onAccountItemClickListener?.invoke(accountModel) }
            itemView.setOnLongClickListener {
                onAccountItemLongClickListener?.invoke(accountModel)
                true
            }
        }
    }

    /**
     * 获取第一个首字母为选定字母的item的位置
     */
    fun getFirstLetterPosition(letter: Char): Int
    {
        return accountList.indexOfFirst { PinyinUtil.getSortFirstLetter(it.desPinyin) == letter.toLowerCase() }
    }

    fun setOnAccountItemClickListener(onAccountItemClickListener: (AccountModel) -> Unit)
    {
        this.onAccountItemClickListener = onAccountItemClickListener
    }

    fun setOnAccountItemLongClickListener(onAccountItemLongClickListener: (AccountModel) -> Unit)
    {
        this.onAccountItemLongClickListener = onAccountItemLongClickListener
    }
}