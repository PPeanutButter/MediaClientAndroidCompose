package com.peanut.nas.compose.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.peanut.nas.compose.model.SharedViewModel
import com.peanut.nas.compose.ui.component.AlbumListRequester
import com.peanut.nas.compose.ui.component.MyTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(viewModel: SharedViewModel, onLogout: ()->Unit){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { MyTopAppBar(scrollBehavior = scrollBehavior, onLogout = onLogout) }
    ){
        AlbumListRequester(paddingValues = it, viewModel = viewModel)
    }
}