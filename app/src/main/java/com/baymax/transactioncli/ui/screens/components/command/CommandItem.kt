package com.baymax.transactioncli.ui.screens.components.command

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.transactioncli.data.model.command.Command
import com.baymax.transactioncli.data.model.transaction.TransactionResponse
import com.baymax.transactioncli.ui.theme.CLIInputColor
import com.baymax.transactioncli.ui.theme.CLIOutputErrorColor
import com.baymax.transactioncli.ui.theme.CLIOutputResultColor

/**
 * Show input command or the result
 *
 * @param command Command to display
 * @see Command
 */
@Composable
fun CommandItem(command: Command) {
    Box(
        modifier = Modifier
            .padding(vertical = 15.dp, horizontal = 16.dp)
    ) {
        when (command) {
            is Command.Input -> CommandInput(data = command.data)
            is Command.Output -> CommandOutput(response = command.response)
        }
    }
}

/**
 * Display input command
 *
 * @param data command with its parameters(e.g. SET abc 123, GET abc)
 */
@Composable
fun CommandInput(data: String) {
    Text(
        text = "> $data",
        color = CLIInputColor,
        fontSize = 18.sp
    )
}

/**
 * Display command result with different color(SUCCESS or FAILED)
 *
 * @param response Command result
 *
 * @see TransactionResponse
 *
 */
@Composable
fun CommandOutput(response: TransactionResponse) {
    when (response) {
        is TransactionResponse.SUCCESS -> {
            Text(
                text = response.value,
                color = CLIOutputResultColor,
                fontSize = 18.sp
            )
        }

        is TransactionResponse.FAILED -> {
            Text(
                text = response.message,
                color = CLIOutputErrorColor,
                fontSize = 18.sp
            )
        }
    }
}