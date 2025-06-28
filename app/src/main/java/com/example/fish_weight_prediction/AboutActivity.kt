package com.example.fish_weight_prediction

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AboutActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Setup Toolbar dan Drawer
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.title = "Tentang Aplikasi"
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, android.R.color.white)

        navView.setNavigationItemSelectedListener(this)
        navView.setCheckedItem(R.id.nav_about)

        // Menampilkan teks HTML pada TextView untuk bold teks
        val datasetTextView = findViewById<TextView>(R.id.textViewDataset)
        val datasetHtml = """
            Dataset yang digunakan dalam aplikasi ini diambil dari website Kaggle yang dibuat oleh 
            <b>Sushant B Mujagule</b> dengan judul <b>Fish Weight Prediction Dataset</b>.
        """.trimIndent()
        datasetTextView.text = HtmlCompat.fromHtml(datasetHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
        datasetTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent = when (item.itemId) {
            R.id.nav_about -> null // already here
            R.id.nav_feature -> Intent(this, FeatureActivity::class.java)
            R.id.nav_architecture -> Intent(this, ArchitectureActivity::class.java)
            R.id.nav_simulation -> Intent(this, MainActivity::class.java)
            R.id.nav_dataset -> Intent(this, DatasetActivity::class.java)
            R.id.nav_simulation -> Intent(this, SimulasiActivity::class.java)
            R.id.nav_beranda -> Intent(this, MainActivity::class.java)
            else -> null
        }

        intent?.let {
            startActivity(it)
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
