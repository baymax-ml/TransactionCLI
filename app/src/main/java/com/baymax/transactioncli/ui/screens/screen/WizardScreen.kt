package com.baymax.transactioncli.ui.screens.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.baymax.transactioncli.ui.screens.components.command.CommandView
import com.baymax.transactioncli.ui.screens.components.InputPanel

@Composable
fun WizardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        InputPanel(viewModel = hiltViewModel())
        CommandView(viewModel = hiltViewModel())
    }
}