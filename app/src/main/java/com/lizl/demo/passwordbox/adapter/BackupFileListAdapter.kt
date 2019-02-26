package com.lizl.demo.passwordbox.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lizl.demo.passwordbox.R
import kotlinx.android.synthetic.main.item_backup_file.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BackupFileListAdapter(private val fileList: List<String>, private val onItemClickListener: OnBackFileItemClickListener?) : RecyclerView.Adapter<BackupFileListAdapter.ViewHolder>()
{
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun getItemCount(): Int = fileList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_backup_file, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(fileList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindViewHolder(filePath: String)
        {
            val file = File(filePath)

            itemView.tv_file_name.text = file.name

            itemView.tv_file_time.text = formatter.format(file.lastModified())
            itemView.setOnClickListener { onItemClickListener?.onBackupFileItemClick(filePath) }
        }
    }

    interface OnBackFileItemClickListener
    {
        fun onBackupFileItemClick(filePath: String)
    }
}