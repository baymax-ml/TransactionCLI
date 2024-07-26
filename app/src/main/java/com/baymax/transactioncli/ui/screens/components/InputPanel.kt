package com.baymax.transactioncli.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baymax.transactioncli.ui.screens.activity.MainActivityViewModel
import com.baymax.transactioncli.util.Constants

/**
 * Panel to select command and set key and value in Wizard Tab
 * If SET is selected,enables both KEY and VALUE TextField
 * If GET or DELETE is selected, disables VALUE TextField
 * If COUNT is selected, disable KEY TextField
 * Otherwise, disable both KEY and VALUE textField
 *
 * If SUBMIT button is pressed, check to confirm DELETE, COMMIT and ROLLBACK command
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputPanel(viewModel: MainActivityViewModel) {
    var command by rememberSaveable {
        mutableStateOf("SET")
    }

    var key by rememberSaveable {
        mutableStateOf("")
    }

    var value by rememberSaveable {
        mutableStateOf("")
    }

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    var keyFieldEnabled by rememberSaveable {
        mutableStateOf(true)
    }

    var valueFieldEnabled by rememberSaveable {
        mutableStateOf(true)
    }

    var confirmCommand by rememberSaveable {
        mutableStateOf(false)
    }

    //Available Commands
    val commandList = arrayListOf(
        Constants.COMMAND_SET,
        Constants.COMMAND_GET,
        Constants.COMMAND_DELETE,
        Constants.COMMAND_COUNT,
        Constants.COMMAND_BEGIN,
        Constants.COMMAND_COMMIT,
        Constants.COMMAND_ROLLBACK,
    )

    /**
     * Enable or disable KEY or VALUE TextField with the selected COMMAND
     */
    fun setCurrentCommand(newCommand: String) {
        command = newCommand
        when (command) {
            Constants.COMMAND_COMMIT, Constants.COMMAND_BEGIN, Constants.COMMAND_ROLLBACK -> {
                keyFieldEnabled = false
                valueFieldEnabled = false
            }

            Constants.COMMAND_COUNT -> {
                keyFieldEnabled = false
                valueFieldEnabled = true
            }

            Constants.COMMAND_GET, Constants.COMMAND_DELETE -> {
                keyFieldEnabled = true;
                valueFieldEnabled = false;
            }

            Constants.COMMAND_SET -> {
                keyFieldEnabled = true;
                valueFieldEnabled = true;
            }
        }
    }

    /**
     * If command is COMMIT, ROLLBACK or DELETE, show the confirm dialog
     * Otherwise, send the command to the pool
     */
    fun submitCommand(command: String, key: String, value: String) {
        when (command) {
            Constants.COMMAND_COMMIT, Constants.COMMAND_ROLLBACK, Constants.COMMAND_DELETE -> {
                confirmCommand = true
            }

            else -> {
                viewModel.submitCommand(command, key, value)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                modifier = Modifier.weight(2f),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = command,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    commandList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                setCurrentCommand(item)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                onClick = {
                    submitCommand(command, key, value)
                }
            ) {
                Text(text = "Submit")
            }
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = if (!keyFieldEnabled) "" else key,
            singleLine = true,
            enabled = keyFieldEnabled,
            onValueChange = { key = it },
            label = { Text(text = "Key") }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            singleLine = true,
            value = if (!valueFieldEnabled) "" else value,
            enabled = valueFieldEnabled,
            onValueChange = { value = it },
            label = { Text(text = "Value") }
        )
    }

    ConfirmDialog(
        open = confirmCommand,
        command = command,
        onConfirm = {
            confirmCommand = false
            viewModel.submitCommand(command, key, value)
        },
        onCancel = {
            confirmCommand = false
        }
    )
}
