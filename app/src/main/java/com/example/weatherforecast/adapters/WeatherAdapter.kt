package com.example.weatherforecast.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.beans.WeatherList
import com.example.weatherforecast.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_row_item.view.*

class WeatherAdapter(internal var activity: Activity, internal var list: List<WeatherList>, private var cityName: String) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_row_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherItem = list[position]

        holder.timeTxtView.text = Utils.formatDateString(weatherItem.dtTxt!!, "yyyy-MM-dd HH:mm:ss", "EEE dd, MMM 'at' hh:mm a")
        holder.cityTxtView.text = cityName
        holder.tempTxtView.text = weatherItem.main?.temp.toString() + "°"
        holder.dexrTxtView.text = weatherItem.weather?.get(0)?.description
        holder.minTempTxtView.text = weatherItem.main?.tempMin.toString() + "° /  " + weatherItem.main?.tempMax.toString() + "°"
        holder.windTxtView.text = weatherItem.wind?.speed.toString() + "km/h"

        holder.imageView.visibility = VISIBLE
        Picasso.get()
            .load("https://api.openweathermap.org/img/w/" + weatherItem.weather?.get(0)?.icon + ".png")
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeTxtView: TextView = itemView.timeTxtView
        var imageView: ImageView = itemView.weatherImageView
        var windTxtView: TextView = itemView.windTxtView
        var cityTxtView: TextView = itemView.cityTxtView
        var tempTxtView: TextView = itemView.tempTxtView
        var dexrTxtView: TextView = itemView.dexrTxtView
        var minTempTxtView: TextView = itemView.minTempTxtView
    }
}
