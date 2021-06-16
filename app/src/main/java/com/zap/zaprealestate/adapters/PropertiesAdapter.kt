package com.zap.zaprealestate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zap.zaprealestate.R
import com.zap.zaprealestate.model.BusinessType
import com.zap.zaprealestate.model.Property
import java.text.DecimalFormat

class PropertiesAdapter(private var properties: List<Property> = emptyList()): RecyclerView.Adapter<PropertiesAdapter.PropertyViewHolder>() {

    class PropertyViewHolder(private val layout: ConstraintLayout) :
        RecyclerView.ViewHolder(layout) {

        fun setProperty(property: Property) {
            val propertyResume = " ${property.usableAreas}m2, ${property.bathrooms} banheiro(s), ${property.bedrooms} quarto(s)"
            layout.findViewById<TextView>(R.id.text_view_property_resume).text = propertyResume

            layout.findViewById<TextView>(R.id.text_view_business_type).text = property.businessType.run {
                when(this) {
                    BusinessType.RENT -> "Apartamento para Aluguel"
                    else -> "Apartamento para Venda"
                }
            }

            val dec = DecimalFormat("#,###.##")
            layout.findViewById<TextView>(R.id.text_view_price).text = dec.format(property.price.toLong())

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

    fun updateProperties(properties: List<Property>){
        this.properties = properties
        notifyDataSetChanged()
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