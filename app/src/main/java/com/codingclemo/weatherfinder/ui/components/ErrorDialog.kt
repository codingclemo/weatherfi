package com.codingclemo.weatherfinder.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.codingclemo.weatherfinder.R
import com.codingclemo.weatherfinder.util.NetworkUtils
import androidx.compose.ui.res.stringResource

@Composable
fun ErrorDialog(onRefresh: () -> Unit) {
    val isInternetAvailable = NetworkUtils(LocalContext.current).isNetworkAvailable()

    val errorText = if (!isInternetAvailable) {
        stringResource(R.string.error_dialog_no_internet)
    } else {
        stringResource(R.string.error_dialog_generic)
    }

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(R.string.error_dialog_title)) },
        text = { Text(text = errorText) },
        confirmButton = {
            Button(
                onClick = {
                    onRefresh()
                }
            ) {
                Text(stringResource(R.string.cta_try_again))
            }
        }
    )
}