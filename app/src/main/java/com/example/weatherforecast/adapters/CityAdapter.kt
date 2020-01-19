package com.example.weatherforecast.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityReporsitory

class CityAdapter(val activity: Activity, var favoriteCityList: ArrayList<City>) :
    RecyclerView.Adapter<CityAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_row_item, parent, false)
        return CityHolder(view)
    }

    fun updateList(cityList: ArrayList<City>) {
        this.favoriteCityList = cityList
    }

    override fun getItemCount(): Int = favoriteCityList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val field = favoriteCityList[position]
        holder.bind(field)
    }

    inner class CityHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var cityText = itemView.findViewById<TextView>(R.id.cityItemText)
        private var deleteBtn = itemView.findViewById<Button>(R.id.deleteBtn)

        fun bind(field: City) {
            cityText.text = field.toString()

            deleteBtn.setOnClickListener {
                val  cityRepository = CityReporsitory(activity)
                cityRepository.updateCity(isFavorite = false, cityId = field.id)

                favoriteCityList.remove(field)
                updateList(favoriteCityList)
                notifyDataSetChanged()
            }
        }
    }
}
