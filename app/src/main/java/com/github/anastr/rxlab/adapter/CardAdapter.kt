package com.github.anastr.rxlab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.anastr.rxlab.R
import com.github.anastr.rxlab.ui.ClickListener
import com.github.anastr.rxlab.ui.HomeFragment

class CardAdapter(
    val context: Context,
    val cards: List<CardData>,
    val clickListener: ClickListener
) :
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
        private val iconView: ImageView = itemView.findViewById(R.id.iconView)

        init {
            iconView.visibility = View.GONE
            itemView.setOnClickListener {
                val cardItem = cards[bindingAdapterPosition]

                print("CardViewHolder")

                clickListener.onCardClick(cardItem)
//                val intent = Intent(context, cardItem.clazz)
//                intent.putExtra("title", cardItem.title)
//                context.startActivity(intent)
            }
        }
    }
}