package com.accord.Nmea.ui.live

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.accord.Nmea.ui.log.LogFragment
import com.accord.Nmea.ui.sky.SkyviewFragment

class ViewpagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragment, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> LiveFragment()
            1 -> LogFragment()
            2 -> SkyviewFragment()
            else -> Fragment()
        }
    }


}