package com.pesimatik.appbox.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pesimatik.appbox.model.ActivityEntry

class Config(
    val entries: MutableList<RenderableActivityEntry>,
    val clickListener: (RenderableActivityEntry) -> Unit,
    val longClickListener: (RenderableActivityEntry) -> Unit
)

class EntriesAdapter(
    val config: Config
) : RecyclerView.Adapter<EntriesAdapter.EntriesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntriesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EntriesHolder(
            layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false),
            config.clickListener,
            config.longClickListener
        )
    }

    override fun onBindViewHolder(holder: EntriesHolder, position: Int) {
        holder.bindActivityEntry(config.entries[position])
    }

    override fun getItemCount(): Int {
        return config.entries.size
    }

    fun updateEntries(newEntries: List<RenderableActivityEntry>) {
        config.entries.clear()
        config.entries.addAll(newEntries)
        notifyDataSetChanged()
    }

    class EntriesHolder(
        itemView: View,
        val clickListener: (RenderableActivityEntry) -> Unit,
        val longPressListener: (RenderableActivityEntry) -> Unit
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val nameTextView = itemView as TextView
        private lateinit var activityEntry: RenderableActivityEntry

        init {
            nameTextView.setOnClickListener(this)
            nameTextView.setOnLongClickListener {
                longPressListener(activityEntry)
                true
            }
        }

        fun bindActivityEntry(activityEntry: RenderableActivityEntry) {
            this.activityEntry = activityEntry
            nameTextView.text = activityEntry.activityEntry.name
            nameTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                activityEntry.textSize
            )
        }

        override fun onClick(p0: View?) {
            clickListener(activityEntry)
        }
    }
}