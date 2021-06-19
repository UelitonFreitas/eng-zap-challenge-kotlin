package com.zap.zaprealestate.mainscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.zap.zaprealestate.R
import com.zap.zaprealestate.mainscreen.adpters.CompaniesFragmentAdapter
import com.zap.zaprealestate.vivarealscreen.VivaRealFragment
import com.zap.zaprealestate.zapScreen.ZapFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val companies = listOf("Zap", "Viva Real")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        setupPageAdapter()
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    private fun setupPageAdapter() {
        with(pager) {

            adapter = CompaniesFragmentAdapter(
                this@MainActivity,
                listOf(ZapFragment::getInstance, VivaRealFragment::getInstance)
            )

            orientation = ViewPager2.ORIENTATION_HORIZONTAL

            TabLayoutMediator(tab_layout, this) { tab, position ->
                tab.text = companies[position]
            }.attach()
        }
    }
}
