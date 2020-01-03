package com.lizl.demo.passwordbox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.demo.passwordbox.R
import kotlinx.android.synthetic.main.item_backup_file.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter : RecyclerView.Adapter<BackupFileListAdapter.ViewHolder>()
{
    private val fileList: MutableList<File> = mutableListOf()

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    private var onItemClickListener: ((File) -> Unit)? = null

    fun setData(fileList: List<File>)
    {
        this.fileList.clear()
        this.fileList.addAll(fileList.sortedByDescending { it.lastModified() })
        notifyDataSetChanged()
    }

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
        fun bindViewHolder(file: File)
        {
            itemView.tv_file_name.text = file.name

            itemView.tv_file_time.text = formatter.format(file.lastModified())
            itemView.setOnClickListener { onItemClickListener?.invoke(file) }
        }
    }

    fun setOnItemClickListener(onItemClickListener: ((File) -> Unit))
    {
        this.onItemClickListener = onItemClickListener
    }
}