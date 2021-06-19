package com.zap.zaprealestate.zapScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zap.zaprealestate.PropertiesScreenProtocols
import com.zap.zaprealestate.R
import com.zap.zaprealestate.adapters.PropertiesAdapter
import com.zap.zaprealestate.detailscreen.PropertyDetailScreen
import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.remote.PropertiesRepositoryImpl
import kotlinx.android.synthetic.main.properties_fragment.*

class ZapFragment : Fragment(), PropertiesScreenProtocols.View {

    private lateinit var presenter: ZapScreenPresenter
    private val propertyAdapter = PropertiesAdapter(onPropertyClick = ::onPropertyClick)
    private val viewManager = LinearLayoutManager(this.activity)

    companion object {
        fun getInstance() : ZapFragment {
            return ZapFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.properties_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = ZapScreenPresenter(this, PropertiesRepositoryImpl())

        swipe_container.setOnRefreshListener { presenter.getPropertiesList() }

        swipe_container.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)


        recycler_view_properties.apply {
            layoutManager = viewManager
            adapter = propertyAdapter
            setHasFixedSize(true)
        }

        recycler_view_properties.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val canScrollVerticallyFromTopToBottom = recyclerView.canScrollVertically(1)

                if(!canScrollVerticallyFromTopToBottom) {
                    presenter.loadNextPropertiesOffset()
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()

        presenter.getPropertiesList()
    }

    override fun showProperties(properties: List<Property>) {
        propertyAdapter.updateProperties(properties)
    }

    override fun showEmptyList() {
        Toast.makeText(this.activity, "Empty List", Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage() {
        Toast.makeText(this.activity, "Error when loading List", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        swipe_container.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_container.isRefreshing = false
    }

    override fun showPropertyDetail(property: Property) {
        this.context?.let { startActivity(PropertyDetailScreen.getIntent(it, property)) }
    }

    private fun onPropertyClick(property: Property){
        presenter.onPropertySelected(property)
    }
}
