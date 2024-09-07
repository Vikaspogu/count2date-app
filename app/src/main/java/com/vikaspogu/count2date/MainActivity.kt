package com.vikaspogu.count2date

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.vikaspogu.count2date.ui.home.HomeScreen
import com.vikaspogu.count2date.ui.theme.Count2DateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Count2DateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
