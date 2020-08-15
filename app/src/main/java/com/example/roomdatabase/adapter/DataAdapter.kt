package com.example.roomdatabase.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.R
import com.example.roomdatabase.model.Data
import java.util.*


class DataAdapter(
    private var items: List<Data>,
    private val callback: AdapterCallback
) :
    RecyclerView.Adapter<DataAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.data_item,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var status = false

        val data = items[position]

        holder.title.text = data.title
        holder.detail.text = data.details
        holder.time.text = data.time

        holder.arrowBtn.setOnClickListener {

            if (status) {
                holder.editLayout.visibility = View.GONE
                holder.view.visibility = View.GONE
                holder.arrowBtn.setImageResource(R.drawable.ic_baseline_arrow_drop_down)
                status = false
            } else {
                holder.editLayout.visibility = View.VISIBLE
                holder.view.visibility = View.VISIBLE
                holder.arrowBtn.setImageResource(R.drawable.ic_baseline_arrow_drop_up)
                status = true
            }
        }

//        val r = Random()
//        val red: Int = r.nextInt(255 - 0 + 1) + 0
//        val green: Int = r.nextInt(255 - 0 + 1) + 0
//        val blue: Int = r.nextInt(255 - 0 + 1) + 0
//
//        val draw = GradientDrawable()
//        draw.shape = GradientDrawable.RECTANGLE
//        draw.setColor(Color.rgb(red, green, blue))
//        holder.cardView.background = draw


        holder.updateButton.setOnClickListener {
            callback.update(data)
        }

        holder.deleteButton.setOnClickListener {
            callback.delete(data)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


    fun addItem(dataList: List<Data>) {

        this.items = dataList
        notifyDataSetChanged()
    }

    interface AdapterCallback {
        fun update(data: Data)
        fun delete(data: Data)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_tv)
        val detail: TextView = itemView.findViewById(R.id.detail_tv)
        val time: TextView = itemView.findViewById(R.id.time_tv)
        val updateButton: ImageView = itemView.findViewById(R.id.button_update)
        val deleteButton: ImageView = itemView.findViewById(R.id.button_delete)
        val editLayout: LinearLayout = itemView.findViewById(R.id.edit_layout)
        val arrowBtn: ImageView = itemView.findViewById(R.id.arrow_btn)
        val view: View = itemView.findViewById(R.id.view)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
    }
}