package com.harshalv.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.harshalv.jetpackcompose.components.GmailDrawerMenu
import com.harshalv.jetpackcompose.components.GmailFab
import com.harshalv.jetpackcompose.components.HomeAppBar
import com.harshalv.jetpackcompose.components.HomeBottomMenu
import com.harshalv.jetpackcompose.components.MailList
import com.harshalv.jetpackcompose.ui.theme.ComposeMaterialDesignSamplesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMaterialDesignSamplesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GmailApp()
                }
            }
        }
    }
}

@Composable
fun GmailApp() {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val openDialog = remember {
        mutableStateOf(false)
    }

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            HomeAppBar(scaffoldState, coroutineScope, openDialog)
        },
        drawerContent = {
            GmailDrawerMenu(scrollState = scrollState)
        },
        bottomBar = {
            HomeBottomMenu()
        },
        floatingActionButton = {
            GmailFab(scrollState)
        }) {
        MailList(it, scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeMaterialDesignSamplesTheme {
        GmailApp()
    }
}