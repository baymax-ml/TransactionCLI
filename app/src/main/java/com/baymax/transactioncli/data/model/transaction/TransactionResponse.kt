package com.baymax.transactioncli.data.model.transaction

/**
 * Response to the command
 *
 * [SUCCESS] Indicate that the command is executed successfully. Holds the execution result if necessary
 * [FAILED] Indicate that the command is failed. Holds the error message
 */
sealed class TransactionResponse {
    data class SUCCESS(val value: String = "") : TransactionResponse()
    data class FAILED(val message: String) : TransactionResponse()
}