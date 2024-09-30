package com.example.prueba1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun ComponentsScreen(navController: NavController){
    Column {
        var component by remember { mutableStateOf("") }//Es para hacer reactiva la variable commo en vue
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,//Current state of drawer
            //Drawer content
            drawerContent = {
                ModalDrawerSheet {
                    Text("Menu", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text = "Content 1") },
                        selected = false,
                        onClick = {
                            component = "Content1"
                            scope.launch {
                                drawerState.apply {
                                    close()
                                }
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Buttons") },
                        selected = false,
                        onClick = {
                            component = "Buttons"
                            scope.launch {
                                drawerState.apply {
                                    close()
                                }
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Floating Buttons") },
                        selected = false,
                        onClick = {
                            component = "FloatingButtons"
                            scope.launch {
                                drawerState.apply {
                                    close()
                                }
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Chips") },
                        selected = false,
                        onClick = {
                            component = "Chips"
                            scope.launch {
                                drawerState.apply {
                                    close()
                                }
                            }
                        }
                    )
                }
            }
        ) {
            Column {
                when(component){
                    "Content1" -> {
                        Content1()
                    }
                    "Content2" -> {
                        content2()
                    }
                    "Buttons" -> {
                        Buttons()
                    }
                    "FloatingButtons" -> {
                        FloatingButtons()
                    }
                    "Chips" -> {
                        Chips()
                    }
                }
            }
        }
        Button(onClick = {navController.navigate("home")}) { }
    }
}

@Composable
fun Content1(){
    Text(text = "Content 1")
}

@Composable
fun content2(){
    Text(text = "Content 2")
}

@Composable
fun Buttons(){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
    ){
        Button(onClick = {}) {//Color primary
            Text("filled")
        }
        FilledTonalButton(onClick = {}) {//Color secondary
            Text("Tonal")
        }
        OutlinedButton(onClick = {}) {
            Text("Outlined")
        }
        ElevatedButton(onClick = {}) {//Color secondary
            Text("Elevated")
        }
        TextButton(onClick = {}) {//Color secondary
            Text("Text")
        }
    }
}

@Composable
fun FloatingButtons(){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
    ){
        FloatingActionButton(onClick = {}) {
            Icon(Icons.Filled.Check, contentDescription = "")
        }
        SmallFloatingActionButton(onClick = {}) {
            Icon(Icons.Filled.Check, contentDescription = "")
        }
        LargeFloatingActionButton(onClick = {}) {
            Icon(Icons.Filled.Check, contentDescription = "")
        }
        ExtendedFloatingActionButton(onClick = {},
            icon = { Icon(Icons.Filled.Check, contentDescription = "") },
            text = { Text(text = "Extended FAB") })
    }
}


@Composable
fun Chips() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
    ) {
        AssistChip(
            onClick = {},
            label = { Text("Assist Chip") },
            leadingIcon = { Icon(Icons.Filled.Check, contentDescription = "",
                modifier = Modifier.size(AssistChipDefaults.IconSize))},
        )
        var selected by remember { mutableStateOf(false) }
        FilterChip(
            selected = selected,
            onClick = {selected=!selected},
            label = { Text("Filter Chip") },
            leadingIcon = if(selected){
                {
                    Icon(
                        Icons.Filled.Check, contentDescription = "",
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
                }else{
                null
            }
        )
    }
}