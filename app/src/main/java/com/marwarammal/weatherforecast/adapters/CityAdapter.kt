package com.marwarammal.weatherforecast.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.marwarammal.weatherforecast.R
import com.marwarammal.weatherforecast.activities.CitiesWeatherActivity
import com.marwarammal.weatherforecast.models.City
import com.marwarammal.weatherforecast.utils.Utils
import kotlinx.android.synthetic.main.city_row_item.view.*

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
        private var cityText = itemView.cityItemText
        private var countryText = itemView.counttryItemText
        private var holderLayout: LinearLayout = itemView.holderLayout

        fun bind(field: City) {
            cityText.text = field.name
            countryText.text = field.country

            holderLayout.setOnClickListener {

                if (favoriteCityList.size >= 3) {
                    val intent = Intent(activity, CitiesWeatherActivity::class.java)
                    intent.putExtra("selectedCity", field.id)
                    intent.putExtra("selectedName", field.name)
                    intent.putExtra("isCurrent", false)
                    intent.putExtra("latitude", field.coord?.lat)
                    intent.putExtra("longitude", field.coord?.lon)
                    activity.startActivityForResult(intent, 5555)
                } else {
                    Utils.showAlertDialog(activity, activity.getString(R.string.cities_count_alert), "Ok", false)
                }
            }
        }
    }
}
