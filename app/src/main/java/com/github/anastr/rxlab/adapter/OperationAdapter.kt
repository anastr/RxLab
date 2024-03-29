package com.github.anastr.rxlab.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.objects.Operation
import com.github.anastr.rxlab.objects.OperationType
import com.github.anastr.rxlab.preview.OperationActivity

/**
 * Created by Anas Altair on 4/1/2020.
 */
class OperationAdapter(val activity: Activity, val operations: List<Operation>)
    : RecyclerView.Adapter<OperationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount() = operations.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.titleView.text = operations[position].operationName.text
        holder.iconView.setImageResource(
            when (operations[position].type) {
                OperationType.Coroutine -> R.drawable.icon_kotlin
                OperationType.RxJava -> R.drawable.icon_rxjava
            }
        )
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleView: TextView = itemView.findViewById(R.id.textView)
        val iconView: ImageView = itemView.findViewById(R.id.iconView)

        init {
            itemView.setOnClickListener {
                val intent = Intent(activity, OperationActivity::class.java)
                intent.putExtra("title", operations[bindingAdapterPosition].operationName.text)
                intent.putExtra("OperationController", operations[bindingAdapterPosition].controller)
                activity.startActivity(intent)
            }
        }
    }
}