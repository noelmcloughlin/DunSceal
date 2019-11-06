package org.noel.dunsceal.activities.dun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.noel.dunsceal.R
import org.noel.dunsceal.main.DunScealApp

class DunReport : AppCompatActivity() {

    lateinit var app: DunScealApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
    }
}
