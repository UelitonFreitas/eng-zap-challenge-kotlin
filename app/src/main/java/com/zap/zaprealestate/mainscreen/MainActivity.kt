package com.zap.zaprealestate.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.zap.zaprealestate.R
import com.zap.zaprealestate.mainscreen.adpters.PropertiesAdapter
import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.remote.PropertiesRepositoryImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        pager.adapter = DemoCollectionAdapter(this)
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Toast.makeText(this@MainActivity, "onPageSelected", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

class DemoCollectionAdapter(appCompatActivity: AppCompatActivity) :
    FragmentStateAdapter(appCompatActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = ZapFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}

private const val ARG_OBJECT = "object"

// Instances of this class are fragments representing a single
// object in our collection.
class ZapFragment : Fragment(), MainScreenProtocols.View {

    private lateinit var propertiesList: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var presenter: ZapScreenPresenter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_collection_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        propertiesList = view.findViewById<RecyclerView>(R.id.recycler_view_properties)
        swipeLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_container)
        presenter = ZapScreenPresenter(this, PropertiesRepositoryImpl())

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
