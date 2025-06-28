package com.example.fish_weight_prediction

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ArchitectureActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_architecture)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Arsitektur Model"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, android.R.color.white)

        navView.setNavigationItemSelectedListener(this)
        navView.setCheckedItem(R.id.nav_architecture)

        setBoldText(
            R.id.textView1,
            "Arsitektur model yang digunakan dalam aplikasi ini adalah sebuah Artificial Neural Network (ANN) yang terdiri dari beberapa lapisan (layers). Model ini dibuat menggunakan pustaka TensorFlow dan Keras.",
            listOf("Artificial Neural Network (ANN)", "TensorFlow dan Keras")
        )

        setBoldText(
            R.id.textView2,
            "Penjelasan tiap layer:\n• Input Layer: menerima 6 inputan fitur hasil proses pre-processing.\n• Hidden Layer 1: berisi 64 neuron dengan aktivasi ReLU.\n• Hidden Layer 2: berisi 32 neuron dengan aktivasi ReLU.\n• Output Layer: 1 neuron tanpa aktivasi karena prediksi berupa nilai kontinu (regresi).",
            listOf("• Input Layer:", "6 inputan fitur", "• Hidden Layer 1:", "• Hidden Layer 2:", "• Output Layer:")
        )

        setBoldText(
            R.id.textView3,
            "Pada dataset, terdapat kolom Species yang merupakan data bertipe objek (string). Karena model hanya dapat menerima data numerik, maka kolom ini perlu diubah menjadi bentuk numerik menggunakan Label Encoder.",
            listOf("Label Encoder")
        )

        setBoldText(
            R.id.textView4,
            "Fitur-fitur dinormalisasi menggunakan StandardScaler agar berada dalam skala yang seragam. Ini penting agar proses pelatihan menjadi stabil dan konvergen dengan cepat.",
            listOf("StandardScaler")
        )

        setBoldText(
            R.id.textView5,
            "Dataset dibagi menjadi:\n• Training Set (80%): untuk melatih model.\n• Testing Set (20%): untuk menguji performa model.",
            listOf("• Training Set (80%):", "• Testing Set (20%):")
        )

        setBoldText(
            R.id.textView6,
            "Model dikompilasi dengan:\n• Optimizer: Adam, digunakan karena cepat dan adaptif terhadap data.\n• Loss Function: Mean Squared Error (MSE), digunakan untuk regresi.\n• Epochs: 100 iterasi pelatihan.\n• Batch size: 8 data per update bobot.\n• Validation split: 20% data training digunakan untuk validasi model.",
            listOf("• Optimizer:", "• Loss Function:", "• Epochs:", "• Batch size:", "• Validation split:")
        )

        setBoldText(
            R.id.textView7,
            "• MSE: ~600–1000 (semakin kecil, semakin baik).\n• R² Score: ~0.90–0.95 → model dapat menjelaskan lebih dari 90% variasi data target.",
            listOf("• MSE:", "• R² Score:")
        )
    }

    private fun setBoldText(textViewId: Int, fullText: String, boldWords: List<String>) {
        val textView = findViewById<TextView>(textViewId)
        val spannable = SpannableString(fullText)
        for (word in boldWords) {
            var start = fullText.indexOf(word)
            while (start >= 0) {
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    start + word.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                start = fullText.indexOf(word, start + word.length)
            }
        }
        textView.text = spannable
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent = when (item.itemId) {
            R.id.nav_about -> Intent(this, AboutActivity::class.java)
            R.id.nav_feature -> Intent(this, FeatureActivity::class.java)
            R.id.nav_simulation -> Intent(this, MainActivity::class.java)
            R.id.nav_dataset -> Intent(this, DatasetActivity::class.java)
            R.id.nav_simulation -> Intent(this, SimulasiActivity::class.java)
            R.id.nav_beranda -> Intent(this, MainActivity::class.java)
            R.id.nav_architecture -> null
            else -> null
        }

        intent?.let {
            startActivity(it)
            finish()
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
