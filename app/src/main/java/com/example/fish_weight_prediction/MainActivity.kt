package com.example.fish_weight_prediction

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var interpreter: Interpreter
    private val modelPath = "fish-weight-prediction-dataset.tflite"

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var resultText: TextView
    private lateinit var speciesSpinner: Spinner
    private lateinit var checkButton: Button

    private lateinit var seekLength1: SeekBar
    private lateinit var seekLength2: SeekBar
    private lateinit var seekLength3: SeekBar
    private lateinit var seekHeight: SeekBar
    private lateinit var seekWidth: SeekBar

    private lateinit var labelLength1: TextView
    private lateinit var labelLength2: TextView
    private lateinit var labelLength3: TextView
    private lateinit var labelHeight: TextView
    private lateinit var labelWidth: TextView

    private val minMaxValues = mapOf(
        "Length1" to Pair(7.5, 59.0),
        "Length2" to Pair(8.4, 63.4),
        "Length3" to Pair(8.8, 68.0),
        "Height" to Pair(1.73, 19.0),
        "Width" to Pair(1.05, 8.14)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
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
        navView.setCheckedItem(R.id.nav_simulation)

        resultText = findViewById(R.id.text_result)
        speciesSpinner = findViewById(R.id.spinner_species)
        checkButton = findViewById(R.id.button_predict)

        seekLength1 = findViewById(R.id.seek_length1)
        seekLength2 = findViewById(R.id.seek_length2)
        seekLength3 = findViewById(R.id.seek_length3)
        seekHeight = findViewById(R.id.seek_height)
        seekWidth = findViewById(R.id.seek_width)

        labelLength1 = findViewById(R.id.label_length1)
        labelLength2 = findViewById(R.id.label_length2)
        labelLength3 = findViewById(R.id.label_length3)
        labelHeight = findViewById(R.id.label_height)
        labelWidth = findViewById(R.id.label_width)

        val speciesList = listOf("Bream", "Roach", "Whitefish", "Parkki", "Perch", "Pike", "Smelt")
        speciesSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, speciesList)

        setSeekBar(seekLength1, labelLength1, minMaxValues["Length1"]!!)
        setSeekBar(seekLength2, labelLength2, minMaxValues["Length2"]!!)
        setSeekBar(seekLength3, labelLength3, minMaxValues["Length3"]!!)
        setSeekBar(seekHeight, labelHeight, minMaxValues["Height"]!!)
        setSeekBar(seekWidth, labelWidth, minMaxValues["Width"]!!)

        initInterpreter()

        checkButton.setOnClickListener {
            try {
                val inputArray = floatArrayOf(
                    speciesSpinner.selectedItemPosition.toFloat(),
                    getSeekValue(seekLength1, minMaxValues["Length1"]!!.first),
                    getSeekValue(seekLength2, minMaxValues["Length2"]!!.first),
                    getSeekValue(seekLength3, minMaxValues["Length3"]!!.first),
                    getSeekValue(seekHeight, minMaxValues["Height"]!!.first),
                    getSeekValue(seekWidth, minMaxValues["Width"]!!.first),
                    0f
                )
                val input = arrayOf(inputArray)
                val output = Array(1) { FloatArray(1) }
                interpreter.run(input, output)

                val predicted = "<b>%.2f gram</b>".format(output[0][0])
                resultText.text = android.text.Html.fromHtml("Berat ikan diprediksi: $predicted", android.text.Html.FROM_HTML_MODE_LEGACY)

            } catch (e: Exception) {
                Log.e("PredictionError", "${e.message}", e)
                Toast.makeText(this, "Terjadi kesalahan saat prediksi", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent = when (item.itemId) {
            R.id.nav_about -> Intent(this, AboutActivity::class.java)
            R.id.nav_feature -> Intent(this, FeatureActivity::class.java)
            R.id.nav_architecture -> Intent(this, ArchitectureActivity::class.java)
            R.id.nav_dataset -> Intent(this, DatasetActivity::class.java)
            R.id.nav_simulation -> null
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
            super.onBackPressed()
        }
    }

    private fun setSeekBar(seekBar: SeekBar, label: TextView, range: Pair<Double, Double>) {
        val min = range.first
        val max = range.second
        seekBar.max = ((max - min) * 10).toInt()
        seekBar.progress = ((20.0 - min) * 10).toInt().coerceIn(0, seekBar.max)
        label.text = "Value: %.1f".format(getSeekValue(seekBar, min))
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                label.text = "Value: %.1f".format(getSeekValue(seekBar!!, min))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun getSeekValue(seekBar: SeekBar, min: Double): Float {
        return (seekBar.progress.toDouble() / 10.0 + min).toFloat()
    }

    private fun initInterpreter() {
        val options = Interpreter.Options().apply {
            setNumThreads(4)
            setUseNNAPI(true)
        }
        interpreter = Interpreter(loadModelFile(assets, modelPath), options)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
