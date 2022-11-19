package com.peanut.nas.compose.ui.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.nas.compose.data.Configuration
import com.peanut.nas.compose.model.ConfigurationDataStore
import com.peanut.nas.compose.model.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, onLogout:()->Unit){
    CenterAlignedTopAppBar(
        title = { AppTitleText() },
        scrollBehavior = scrollBehavior,
        actions = { SettingAction { onLogout() } },
        modifier = Modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MyTopAppBarPreview(){
    MyTopAppBar{}
}