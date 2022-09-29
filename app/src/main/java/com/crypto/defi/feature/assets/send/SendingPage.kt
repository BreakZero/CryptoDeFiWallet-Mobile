package com.crypto.defi.feature.assets.send

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.crypto.core.ui.composables.DeFiAppBar

@Composable
fun SendingPager(
    slug: String
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            DeFiAppBar(title = slug) {

            }
        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column() {
                Column() {
                    TextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {})
                    Text("")
                    TextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {})
                    TextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {})

                    Text(text = "Miner Fee")
                }
                Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                    Text(text = "Next")
                }
            }
        }
    }
}