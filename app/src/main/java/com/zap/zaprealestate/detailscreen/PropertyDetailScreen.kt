package com.zap.zaprealestate.detailscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zap.zaprealestate.R
import com.zap.zaprealestate.model.Property

class PropertyDetailScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_detail_screen)
    }

    companion object {

        private const val propertyKey = "property"

        fun getIntent(context: Context, property: Property) =
            Intent(context, PropertyDetailScreen::class.java).apply {
                Bundle().apply { putExtra(propertyKey, property) }.also {
                    putExtras(it)
                }
            }
    }
}