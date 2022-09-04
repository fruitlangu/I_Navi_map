package com.inavi.inavi_map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inavi.inavi_map.utils.Utils.startNewActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        setTheme(R.style.Theme_Inavi_Map)
        startNewActivity(MainActivity::class.java)

    }
}