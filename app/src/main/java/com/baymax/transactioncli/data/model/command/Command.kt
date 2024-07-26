package com.baymax.transactioncli.data.model.command

import com.baymax.transactioncli.data.model.transaction.TransactionResponse

/**
 * Type of commands
 *
 * [Input]: Indicates command input with its data
 * [Output]: Indicates command output with its result
 */
sealed class Command {
    data class Input(val data: String) : Command()
    data class Output(val response: TransactionResponse) : Command()
}