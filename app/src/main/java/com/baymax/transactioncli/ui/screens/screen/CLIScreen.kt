package com.baymax.transactioncli.ui.screens.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.baymax.transactioncli.ui.screens.activity.MainActivityViewModel
import com.baymax.transactioncli.ui.screens.components.CLIInput
import com.baymax.transactioncli.ui.screens.components.command.CommandItem
import com.baymax.transactioncli.ui.theme.CLIBackgroundColor
import kotlinx.coroutines.launch

@Composable
fun CLIScreen(viewModel: MainActivityViewModel) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.commandHistory) {
        if (viewModel.commandHistory.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(viewModel.commandHistory.size - 1)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(CLIBackgroundColor)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            itemsIndexed(viewModel.commandHistory, key = { index, _ -> index }) { _, command ->
                CommandItem(command = command)
            }
            item {
                CLIInput(viewModel = hiltViewModel())
            }
        }
    }
}