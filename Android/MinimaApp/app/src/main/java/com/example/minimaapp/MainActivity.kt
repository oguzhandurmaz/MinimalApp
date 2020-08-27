package com.example.minimaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View.LAYOUT_DIRECTION_RTL
import android.widget.Button
import android.widget.EditText
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimaapp.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.host_fragment)

        /*
        val config = applicationContext.resources.configuration
        val layoutDirection = config.layoutDirection
        val isLayoutRtl = layoutDirection == LAYOUT_DIRECTION_RTL
        if(!isLayoutRtl){
            drawer_layout.closeDrawer(Gravity.LEFT)
        }else{
            drawer_layout.closeDrawer(Gravity.RIGHT)
        }*/

        drawer_layout.closeDrawer(GravityCompat.START)

        return NavigationUI.onNavDestinationSelected(item,navController)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.actionBar))

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val navController = findNavController(R.id.host_fragment)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        NavigationUI.setupWithNavController(nav_view,navController)
        nav_view.setNavigationItemSelectedListener(this)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.host_fragment)
        return NavigationUI.navigateUp(navController,drawer_layout)
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
