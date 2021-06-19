package com.zap.zaprealestate.detailscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.zap.zaprealestate.R
import com.zap.zaprealestate.mainscreen.formattedBusinessTypeName
import com.zap.zaprealestate.mainscreen.formattedPrice
import com.zap.zaprealestate.model.Property
import kotlinx.android.synthetic.main.activity_property_detail_screen.*
import kotlinx.android.synthetic.main.tool_bar.*

class PropertyDetailScreen : AppCompatActivity(), PropertyDetailScreenProtocol.View {

    private lateinit var presenter: PropertyDetailPresenter
    private val property: Property? get() = intent.extras?.getParcelable(propertyKey)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_detail_screen)

        setupToolbar()

        presenter = PropertyDetailPresenter(property, this)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        presenter.loadProperty()
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

    override fun showProperty(property: Property) {

        with(property){
            image_view_property_image.run {
                Picasso.get()
                    .load(images.firstOrNull())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .resize(256, 256)
                    .centerCrop()
                    .into(this)
            }

            text_view_property_resume.text = "${usableAreas}m2, $bathrooms banheiro(s), $bedrooms quarto(s)"

            text_view_business_type.text = formattedBusinessTypeName()

            text_view_price.text = formattedPrice()
        }
    }
}