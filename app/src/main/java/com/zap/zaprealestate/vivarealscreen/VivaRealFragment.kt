package com.zap.zaprealestate.vivarealscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zap.zaprealestate.PropertyScreenProtocols
import com.zap.zaprealestate.R
import com.zap.zaprealestate.adapters.PropertiesAdapter
import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.remote.PropertiesRepositoryImpl

class VivaRealFragment : Fragment(), PropertyScreenProtocols.View {

    private lateinit var propertiesList: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var presenter: VivaRealScreenPresenter

    companion object {
        fun getInstance() : VivaRealFragment {
            return VivaRealFragment()
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
        propertiesList = view.findViewById(R.id.recycler_view_properties)
        swipeLayout = view.findViewById(R.id.swipe_container)
        presenter = VivaRealScreenPresenter(this, PropertiesRepositoryImpl())

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
        val viewManager = LinearLayoutManager(this.activity)

        propertiesList.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            setHasFixedSize(true)
        }

        propertiesList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
        Toast.makeText(this.activity, "Empty List", Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage() {
        Toast.makeText(this.activity, "Error when loading List", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        swipeLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeLayout.isRefreshing = false
    }
}