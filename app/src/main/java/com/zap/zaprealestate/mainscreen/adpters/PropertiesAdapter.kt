package com.zap.zaprealestate.mainscreen.adpters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zap.zaprealestate.R
import com.zap.zaprealestate.model.Property

class PropertiesAdapter(private val properties: List<Property>): RecyclerView.Adapter<PropertiesAdapter.PropertyViewHolder>() {

    class PropertyViewHolder(private val layout: ConstraintLayout) :
        RecyclerView.ViewHolder(layout) {

        fun setCharacter(propertie: Property) {
            layout.findViewById<TextView>(R.id.text_view_property_price).text = propertie.id
        }

        fun setProperty(property: Property) {
            layout.findViewById<TextView>(R.id.text_view_property_price).text = property.id

            layout.findViewById<ImageView>(R.id.image_view_property_image).run {
                Picasso.get()
                    .load(property.images.firstOrNull())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .resize(50, 50)
                    .centerCrop()
                    .into(this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.property_item_list, parent, false) as ConstraintLayout

        return PropertyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.setProperty(properties[position])
    }

    override fun getItemCount() = properties.size
}