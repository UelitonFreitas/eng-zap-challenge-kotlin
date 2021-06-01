package com.zap.zaprealestate.mainscreen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zap.zaprealestate.R
import com.zap.zaprealestate.mainscreen.adpters.PropertiesAdapter
import com.zap.zaprealestate.model.remote.PropertiesRepositoryImpl
import com.zap.zaprealestate.model.Property

class MainActivity : AppCompatActivity(), MainScreenProtocols.View {

    private val propertiesList by lazy { findViewById<RecyclerView>(R.id.recycler_view_properties) }
    private val swipeLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipe_container) }
    private val presenter = MainScreenPresenter(this, PropertiesRepositoryImpl())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.getPropertiesList()

        swipeLayout.setOnRefreshListener {
            presenter.getPropertiesList()
        }

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)
    }

    override fun onResume() {
        super.onResume()

        presenter.getPropertiesList()
    }

    override fun showProperties(properties: List<Property>) {
        val viewAdapter = PropertiesAdapter(properties)
        val viewManager = LinearLayoutManager(this)

        propertiesList.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            setHasFixedSize(true)
        }

        propertiesList.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val canScrollVerticallyFromTopToBottom = recyclerView.canScrollVertically(1)

                if(!canScrollVerticallyFromTopToBottom) {
                    presenter.loadNextPropertiesOffset()
                }
            }
        })
    }

    override fun showEmptyList() {
        Toast.makeText(this, "Empty List", Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage() {
        Toast.makeText(this, "Error when loading List", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        swipeLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeLayout.isRefreshing = false
    }
}