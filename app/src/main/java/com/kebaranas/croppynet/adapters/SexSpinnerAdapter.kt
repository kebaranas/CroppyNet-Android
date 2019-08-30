package com.kebaranas.croppynet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kebaranas.croppynet.R
import com.kebaranas.croppynet.models.SexSpinnerItem

class SexSpinnerAdapter(context: Context, list: ArrayList<SexSpinnerItem>) :
    ArrayAdapter<SexSpinnerItem>(context, 0, list)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        return customView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        return customView(position, parent)
    }

    private fun customView(position: Int, parent: ViewGroup): View? {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.sex_spinner, parent, false)
        val items: SexSpinnerItem? = getItem(position)
        val spinnerText: TextView? = convertView.findViewById(R.id.sexTextView)
        spinnerText?.text = items?.text
        return convertView
    }
}