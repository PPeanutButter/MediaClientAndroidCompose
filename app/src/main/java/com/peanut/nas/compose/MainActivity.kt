package com.peanut.nas.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.peanut.nas.compose.model.SharedViewModel
import com.peanut.nas.compose.ui.screens.LoginScreen
import com.peanut.nas.compose.ui.theme.NASTheme
import com.peanut.nas.compose.utils.SettingManager


class MainActivity : ComponentActivity() {
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingManager.init(this)
        setContent {
            NASTheme {
                LoginScreen(viewModel)
            }
        }
    }
}