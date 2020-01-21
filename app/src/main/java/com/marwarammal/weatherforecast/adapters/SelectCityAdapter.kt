package com.marwarammal.weatherforecast.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.marwarammal.weatherforecast.R
import com.marwarammal.weatherforecast.models.City

class SelectCityAdapter(
    internal val activity: Activity,
    var cityListItems: ArrayList<City>,
    var selectedItems: ArrayList<City>
) : RecyclerView.Adapter<SelectCityAdapter.CityMultiSelectHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityMultiSelectHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multiselect_row_item, parent, false)
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

    inner class CityMultiSelectHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var checkBox = itemView.findViewById<CheckBox>(R.id.multiSelectItem)

        fun bind(field: City) {
            checkBox.text = field.toString()
            checkBox.isChecked = false
            if (selectedItems.any { it.name == field.name }) {
                checkBox.isChecked = true
                field.isFavorite = true
            }
            checkBox!!.setOnClickListener {
                if (selectedItems.size >= 7 && checkBox.isChecked) {
                    checkBox.isChecked = false
                    Toast.makeText(
                        activity,
                        "You've reached the maximum allowed cities",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    field.isFavorite = checkBox.isChecked

                    if (selectedItems.any { it.name == field.name }) {
                        if (!checkBox.isChecked) {
                            selectedItems.remove(field)
                        }
                    } else {
                        if (!checkBox.isChecked) {
                            selectedItems.remove(field)
                        } else {
                            selectedItems.add(field)
                        }
                    }
                }
            }
        }
    }
}
