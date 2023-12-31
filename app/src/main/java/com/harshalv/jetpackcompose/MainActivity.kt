package com.harshalv.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harshalv.jetpackcompose.ui.theme.StateSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
//Step 1: Create a new compose Activity and change the content of Greeting method
fun Greeting() {
    //Step 8: create a variable to be stored in memory with remember
    var nameState by remember {
        mutableStateOf("")
    }
    //Step 11: create a remember variable to hold data for when button is clicked
    //Step 14:change to rememberSaveable so the data can survive configuration changes
    var name by rememberSaveable {
        mutableStateOf("")
    }
    //Step 2: Add a column composable to arrange the composable vertically
    //Step 3: Add horizontal alignment to center the elements horizontally
    //Step 4: Add modifier and set to fill max width
    //Step 5: Add vertical arrangement to center element vertically
    //Step 6: change fill max width to fill max size to cover for height and width
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        //Step 10: set nameState to Text element
        //Step 13: set name to Text element
        Text(text = "Hello $name")
        //Step 7:Add spacer with height for spaces between elements
        Spacer(modifier = Modifier.height(20.dp))
        //Step 9: pass in collect changed value with namestate
        TextField(value = nameState, onValueChange = {
            nameState = it
        })
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            //Step 12: reassign the value from TextField to it when the button is clicked
            name = nameState
        }) {
            Text(text = "Display")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StateSampleTheme {
        Greeting()
    }
}