package com.baymax.transactioncli.ui.screens.components.command

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baymax.transactioncli.ui.screens.activity.MainActivityViewModel
import com.baymax.transactioncli.ui.theme.CLIBackgroundColor
import kotlinx.coroutines.launch

/**
 * List of input commands and its result
 */
@Composable
fun CommandView(viewModel: MainActivityViewModel) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.commandHistory) {
        // When new command or new result is appended, scroll to bottom automatically
        if (viewModel.commandHistory.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(viewModel.commandHistory.size - 1)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(CLIBackgroundColor, shape = RoundedCornerShape(10.dp))
                .fillMaxSize()
                .padding(vertical = 5.dp)
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(viewModel.commandHistory, key = { index, _ -> index }) { _, command ->
                CommandItem(command = command)
            }
        }
    }
}