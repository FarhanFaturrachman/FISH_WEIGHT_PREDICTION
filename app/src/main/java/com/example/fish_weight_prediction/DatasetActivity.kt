package com.example.fish_weight_prediction

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class DatasetActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var containerLayout: LinearLayout

    private val datasetHeader = listOf(
        "Species", "Tinggi", "Lebar", "Panjang Vertikal", "Panjang Diagonal", "Panjang Garis Lurus"
    )

    private val datasetRows = listOf(
        listOf("Bream", "11.52", "4.02", "23.2", "25.4", "30.0"),
        listOf("Roach", "12.48", "4.31", "24.0", "26.3", "31.2"),
        listOf("Parkki", "12.38", "4.70", "23.9", "26.5", "31.1"),
        listOf("Perch", "12.73", "4.46", "26.3", "29.0", "33.5"),
        listOf("Pike", "12.44", "5.13", "26.5", "29.0", "34.0"),
        listOf("Smelt", "13.60", "4.93", "26.8", "29.7", "34.7"),
        listOf("Whitefish", "14.18", "5.28", "26.8", "29.7", "34.5"),
        listOf("Roach", "12.67", "4.69", "27.6", "30.0", "35.0"),
        listOf("Perch", "14.00", "4.84", "27.6", "30.0", "35.1"),
        listOf("Pike", "14.23", "4.96", "28.5", "30.7", "36.2")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dataset)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.title = "Dataset"
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
        navView.setCheckedItem(R.id.nav_dataset)

        containerLayout = findViewById(R.id.tableLayout)
        populateTable()

        val titleText = findViewById<TextView>(R.id.textViewDatasetTitle)
        titleText.text = "Deskripsi Dataset"

        val descText = findViewById<TextView>(R.id.textViewDatasetDescription)
        descText.text = "Dataset ini digunakan untuk membangun model prediksi berat ikan berdasarkan fitur-fitur morfologis seperti panjang, tinggi, lebar, dan jenis spesies. Terdapat 6 fitur yaitu: Species (jenis ikan), tiga jenis panjang yaitu Panjang Vertikal, Panjang Diagonal, Panjang Garis Lurus, serta Tinggi dan Lebar. Dataset ini sangat penting dalam implementasi sistem prediksi berat ikan berbasis machine learning karena menyediakan variasi morfologi ikan yang representatif.\n\nData ini cocok untuk pengembangan aplikasi prediksi berat ikan yang praktis dan efisien, khususnya untuk pelaku industri perikanan yang ingin melakukan estimasi berat tanpa alat timbang."
    }

    private fun populateTable() {
        containerLayout.removeAllViews()
        val rowLayout = LinearLayout(this)
        rowLayout.orientation = LinearLayout.HORIZONTAL
        rowLayout.setPadding(8, 8, 8, 8)
        rowLayout.gravity = Gravity.CENTER

        for (i in datasetHeader.indices) {
            val featureLayout = LinearLayout(this)
            featureLayout.orientation = LinearLayout.VERTICAL
            featureLayout.setPadding(16, 16, 16, 16)
            featureLayout.setBackgroundColor(Color.parseColor("#f2f2f2"))
            featureLayout.gravity = Gravity.CENTER

            val headerText = TextView(this)
            headerText.text = datasetHeader[i]
            headerText.setTypeface(null, Typeface.BOLD)
            headerText.setTextColor(Color.BLACK)
            headerText.textSize = 16f
            headerText.setPadding(8, 8, 8, 8)
            headerText.gravity = Gravity.CENTER
            featureLayout.addView(headerText)

            for (row in datasetRows) {
                val valueText = TextView(this)
                valueText.text = row[i]
                valueText.setPadding(8, 4, 8, 4)
                valueText.setTextColor(Color.DKGRAY)
                valueText.gravity = Gravity.CENTER
                featureLayout.addView(valueText)
            }

            val outerWrapper = LinearLayout(this)
            outerWrapper.orientation = LinearLayout.VERTICAL
            outerWrapper.setPadding(8, 8, 8, 8)
            outerWrapper.setBackgroundColor(Color.WHITE)
            outerWrapper.addView(featureLayout)

            rowLayout.addView(outerWrapper)
        }

        containerLayout.addView(rowLayout)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent = when (item.itemId) {
            R.id.nav_about -> Intent(this, AboutActivity::class.java)
            R.id.nav_feature -> Intent(this, FeatureActivity::class.java)
            R.id.nav_architecture -> Intent(this, ArchitectureActivity::class.java)
            R.id.nav_simulation -> Intent(this, SimulasiActivity::class.java)
            R.id.nav_beranda -> Intent(this, MainActivity::class.java)
            R.id.nav_dataset -> null // already here
            else -> null
        }

        intent?.let { startActivity(it) }
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
