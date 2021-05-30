package com.zap.zaprealestate.mainscreen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zap.zaprealestate.R
import com.zap.zaprealestate.mainscreen.adpters.PropertiesAdapter
import com.zap.zaprealestate.model.remote.PropertiesRepositoryImpl
import com.zap.zaprealestate.model.Property

class MainActivity : AppCompatActivity(), MainScreenProtocols.View {

    private val propertiesList by lazy { findViewById<RecyclerView>(R.id.recycler_view_properties) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val presenter = MainScreenPresenter(this, PropertiesRepositoryImpl())
        presenter.getPropertiesList()
    }

    override fun showProperties(properties: List<Property>) {
        val viewAdapter = PropertiesAdapter(properties)
        val viewManager = LinearLayoutManager(this)

        propertiesList.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun showEmptyList() {
        Toast.makeText(this, "Empty List", Toast.LENGTH_SHORT).show()
    }
}