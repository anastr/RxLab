package com.github.anastr.rxlab.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.anastr.rxlab.R

/**
 * Created by Anas Altair on 4/1/2020.
 */
class MyAdapter(val activity: Activity, val operations: List<OperationData>)
    : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount() = operations.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.titleView.text = operations[position].name
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleView: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener {
                val intent = Intent(activity, operations[adapterPosition].clazz)
                intent.putExtra("title", operations[adapterPosition].name)
                intent.putExtra("OperationController", operations[adapterPosition].operationController)
                activity.startActivity(intent)
            }
        }
    }
}