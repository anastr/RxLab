package com.github.anastr.rxlab.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.anastr.rxlab.R

class CardAdapter(val activity: Activity, val cards: List<CardData>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return CardViewHolder(v)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.titleView.text = cards[position].title
    }

    override fun getItemCount(): Int = cards.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleView: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener {
                val cardItem = cards[bindingAdapterPosition]
                val intent = Intent(activity, cardItem.clazz)
                intent.putExtra("title", cardItem.title)
                activity.startActivity(intent)
            }
        }
    }
}