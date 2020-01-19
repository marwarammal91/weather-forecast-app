package com.example.weatherforecast.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.models.City


class CityAdapter(var cityListItems: ArrayList<City>,
                  var selectedItems: ArrayList<City>) :
    RecyclerView.Adapter<CityAdapter.CityMultiSelectHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityMultiSelectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.multiselect_row_item, parent, false)
        return CityMultiSelectHolder(view)
    }

    fun updateList(cityList: ArrayList<City>) {
        this.cityListItems = cityList
    }

    fun getItem(position: Int): City = cityListItems[position]

    override fun getItemCount(): Int = cityListItems.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: CityMultiSelectHolder, position: Int) {
        val field = cityListItems[position]
        holder.bind(field)
    }

    inner class CityMultiSelectHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var checkBox = itemView.findViewById<CheckBox>(R.id.multiSelectItem)

        @SuppressLint("SetTextI18n")
        fun bind(field: City) {
            checkBox.text = "City: " + field.name + "\nCountry: " + field.country
            if (selectedItems.any { it.name == field.name }) {
                checkBox.isChecked = true
                field.isFavorite = true
            }
            checkBox!!.setOnClickListener {
                field.isFavorite = checkBox.isChecked
            }
        }
    }
}
