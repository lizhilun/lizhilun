package com.lizl.demo.passwordbox.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.util.PinyinUtil
import kotlinx.android.synthetic.main.item_account.view.*

class AccountListAdapter(accountList: List<AccountModel>, private val onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>()
{
    private var accountList: List<AccountModel> = accountList.sorted()

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

            itemView.tv_first_letter.visibility = if(showFirstLetter) View.VISIBLE else View.INVISIBLE

            itemView.setOnClickListener { onItemClickListener?.onAccountItemClick(accountModel) }
            itemView.setOnLongClickListener { onItemClickListener?.onAccountItemLongClick(accountModel)!! }
        }
    }

    /**
     * 获取第一个首字母为选定字母的item的位置
     */
    fun getFirstLetterPosition(letter: Char): Int
    {
        for (i in 0 until itemCount)
        {
            if (PinyinUtil.getSortFirstLetter(accountList[i].desPinyin) == letter.toLowerCase())
            {
                return i
            }
        }
        return -1
    }

    interface OnItemClickListener
    {
        fun onAccountItemClick(accountModel: AccountModel)

        fun onAccountItemLongClick(accountModel: AccountModel): Boolean
    }
}