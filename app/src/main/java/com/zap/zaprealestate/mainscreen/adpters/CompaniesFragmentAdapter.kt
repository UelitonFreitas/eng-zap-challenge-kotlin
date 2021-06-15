package com.zap.zaprealestate.mainscreen.adpters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

typealias companyFragmentCreator = () -> Fragment

class CompaniesFragmentAdapter(appCompatActivity: AppCompatActivity, private val companyFragmentCreator: List<companyFragmentCreator>) :
    FragmentStateAdapter(appCompatActivity) {

    override fun getItemCount(): Int = companyFragmentCreator.size

    override fun createFragment(position: Int): Fragment {
        return companyFragmentCreator[position].invoke()
    }
}
