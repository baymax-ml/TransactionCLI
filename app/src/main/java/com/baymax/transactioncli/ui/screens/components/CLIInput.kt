package com.baymax.transactioncli.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.baymax.transactioncli.ui.screens.activity.MainActivityViewModel
import com.baymax.transactioncli.ui.theme.CLIInputColor
import com.baymax.transactioncli.util.Constants

/**
 * TextField to manually input command in CLI tab
 * When ENTER is pressed, check to confirm DELETE, COMMIT, and ROLLBACK command
 */
@Composable
fun CLIInput(viewModel: MainActivityViewModel) {

    var command by rememberSaveable {
        mutableStateOf("")
    }

    val focusRequester = remember {
        FocusRequester()
    }

    var confirmCommand by rememberSaveable {
        mutableStateOf(false)
    }

    //Keep focus on the TextField
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    /**
     * Parse the command string
     * If the command is DELETE, COMMIT or ROLLBACK, it shows the confirm dialog
     * Otherwise, send the command to the pool
     */
    fun submitCommand() {
        if (command.isEmpty()) {
            viewModel.submitCommand(command)
            return
        }
        val args = command.split(" ")
        val commandValue = args[0]
        when (commandValue.uppercase()) {
            Constants.COMMAND_DELETE, Constants.COMMAND_COMMIT, Constants.COMMAND_ROLLBACK -> {
                confirmCommand = true
            }

            else -> {
                viewModel.submitCommand(command)
            }
        }
    }

    Box {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onKeyEvent { keyEvent: KeyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                        submitCommand()
                        true
                    } else {
                        false
                    }
                },

            colors = TextFieldDefaults.colors(
                cursorColor = CLIInputColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            prefix = {
                Text(
                    text = "> ",
                    color = CLIInputColor,
                    fontSize = 18.sp
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                color = CLIInputColor,
                fontSize = 18.sp
            ),
            value = command,
            onValueChange = { command = it },
        )
    }

    ConfirmDialog(
        open = confirmCommand,
        command = command,
        onConfirm = {
            confirmCommand = false
            viewModel.submitCommand(command)
        },
        onCancel = {
            confirmCommand = false
        }
    )
}