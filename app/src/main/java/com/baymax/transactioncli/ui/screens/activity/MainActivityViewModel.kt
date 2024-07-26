package com.baymax.transactioncli.ui.screens.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baymax.transactioncli.data.model.command.Command
import com.baymax.transactioncli.data.model.transaction.TransactionPool
import com.baymax.transactioncli.data.model.transaction.TransactionResponse
import com.baymax.transactioncli.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel() {

    /**
     * Commands and result history
     */
    var commandHistory by mutableStateOf(listOf<Command>())

    /**
     * Append input command to the history
     *
     * @param command Command to append to the history
     */
    private fun insertInputCommand(command: String) {
        commandHistory += Command.Input(command)
    }

    /**
     * Append the command to the history
     * Send Commit command to the pool.
     * Append the result to the history
     */
    private fun commitTransaction() {
        viewModelScope.launch {
            insertInputCommand(Constants.COMMAND_COMMIT)
            when (val result = TransactionPool.commitTransaction()) {
                is TransactionResponse.FAILED -> commandHistory += Command.Output(result)
                else -> {}
            }
        }
    }

    /**
     * Append the command to the history
     * Send Begin command in the pool.
     * Append the result to the history
     */
    private fun beginTransaction() {
        viewModelScope.launch {
            insertInputCommand(Constants.COMMAND_BEGIN)
            TransactionPool.beginTransaction()
        }
    }

    /**
     * Append the command to the history
     * Send Rollback command in the pool.
     * Append the result to the history
     */
    private fun rollbackTransaction() {
        viewModelScope.launch {
            insertInputCommand(Constants.COMMAND_ROLLBACK)
            when (val result = TransactionPool.rollbackTransaction()) {
                is TransactionResponse.FAILED -> commandHistory += Command.Output(result)
                else -> {}
            }
        }
    }

    /**
     * Append the command to the history
     * Send count command to the pool.
     * Append the result to the history
     *
     * @param value Value to count in the transaction
     */
    private fun countValue(value: String) {
        viewModelScope.launch {
            insertInputCommand("${Constants.COMMAND_COUNT} $value")
            val result = TransactionPool.countValue(value)
            commandHistory += Command.Output(result)
        }
    }

    /**
     * Append the command to the history
     * Send set command to the pool.
     * Append the result to the history
     *
     * @param key key to set
     * @param value value to set
     */
    private fun setKeyValue(key: String, value: String) {
        viewModelScope.launch {
            insertInputCommand("${Constants.COMMAND_SET} $key $value")
            when (val result = TransactionPool.setKeyValue(key, value)) {
                is TransactionResponse.FAILED -> commandHistory += Command.Output(result)
                else -> {}
            }
        }
    }

    /**
     * Append the command to the history
     * Send get command to the pool
     * Append the result to the history
     *
     * @param key key to get value
     */
    private fun getKeyValue(key: String) {
        viewModelScope.launch {
            insertInputCommand("${Constants.COMMAND_GET} $key")
            when (val result = TransactionPool.getKeyValue(key)) {
                is TransactionResponse.FAILED -> commandHistory += Command.Output(result)
                is TransactionResponse.SUCCESS -> commandHistory += Command.Output(result)
                else -> {}
            }
        }
    }

    /**
     * Append the command to the history
     * Send delete command to the pool
     * Append the result to the history
     *
     * @param key key to delete
     */
    private fun deleteKeyValue(key: String) {
        viewModelScope.launch {
            insertInputCommand("${Constants.COMMAND_DELETE} $key")
            when (val result = TransactionPool.deleteKeyValue(key)) {
                is TransactionResponse.FAILED -> commandHistory += Command.Output(result)
                else -> {}
            }
        }
    }

    /**
     * Analyze the command to send command to the pool
     *
     * @param command command value(see util/Constants.kt)
     * @param key key of the command(can be Empty)
     * @param value value of the command(can be Empty)
     */
    fun submitCommand(command: String, key: String = "", value: String = "") {
        when (command.uppercase()) {
            // Command from wizard or parsed command
            Constants.COMMAND_SET -> setKeyValue(key, value)
            Constants.COMMAND_GET -> getKeyValue(key)
            Constants.COMMAND_DELETE -> deleteKeyValue(key)
            Constants.COMMAND_COUNT -> countValue(value)
            Constants.COMMAND_BEGIN -> beginTransaction()
            Constants.COMMAND_COMMIT -> commitTransaction()
            Constants.COMMAND_ROLLBACK -> rollbackTransaction()
            // Command from CLi or invalid commands
            else -> {
                parseCommand(command)
            }
        }
    }

    /**
     * Append the invalid command and message to the history
     *
     * @param command Invalid command to append to the history
     * @param message Error message for invalid command
     */
    private fun invalidCommand(command: String, message: String) {
        insertInputCommand(command)
        commandHistory += Command.Output(TransactionResponse.FAILED(message))
    }

    /**
     * Validate the command
     * If valid, send the command to the pool
     * If invalid, append the command to the history with error message
     *
     * @param commandString Command t send the pool(e.g. set abc 123, delete 123, get,...)
     */
    private fun parseCommand(commandString: String) {
        if (commandString.isEmpty()) {
            insertInputCommand("")
            return
        }
        val args = commandString.split(" ")
        val command = args[0]
        when (command.uppercase()) {
            Constants.COMMAND_SET -> {
                if (args.size == 3) {
                    submitCommand(command, args[1], args[2])
                } else {

                    invalidCommand(commandString, "Usage: SET \$key \$value")
                }
            }

            Constants.COMMAND_GET, Constants.COMMAND_DELETE -> {
                if (args.size == 2) {
                    submitCommand(command, args[1])
                } else {
                    invalidCommand(commandString, "Usage: ${command.uppercase()} \$key")
                }
            }

            Constants.COMMAND_COUNT -> {
                if (args.size == 2) {
                    submitCommand(command, "", args[1])
                } else {
                    invalidCommand(commandString, "Usage: ${command.uppercase()} \$key")
                }
            }

            Constants.COMMAND_BEGIN, Constants.COMMAND_COMMIT, Constants.COMMAND_ROLLBACK -> {
                if (args.size == 1) {
                    submitCommand(command)
                } else {
                    invalidCommand(commandString, "Usage: ${command.uppercase()}")
                }
            }

            else -> {
                invalidCommand(commandString, "Invalid Syntax: $command")
            }
        }
    }
}