package com.baymax.transactioncli.ui.screens.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Alert Dialog to confirm DELETE, COMMIT and ROLLBACK
 */
@Composable
fun ConfirmDialog(open: Boolean, command: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
    if (open) {
        AlertDialog(
            title = { Text(text = "Confirm") },
            text = { Text(text = "Are you sure to submit $command") },
            onDismissRequest = { onCancel() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { onCancel() }) {
                    Text("Dismiss")
                }
            }
        )
    }
}