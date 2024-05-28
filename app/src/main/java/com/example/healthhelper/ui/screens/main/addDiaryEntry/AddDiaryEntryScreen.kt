package com.example.healthhelper.ui.screens.main.addDiaryEntry

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.screens.main.DateMaterialDialog
import com.example.healthhelper.ui.viewModels.AddDiaryEntryScreenViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun AddDiaryEntryScreen(
    navController: NavController,
    viewModel: AddDiaryEntryScreenViewModel,
) {

    val addDiaryEntryScreenUiState by viewModel.addDiaryEntryScreenUiState.collectAsState()

    val context = LocalContext.current

    val dateDialogState = rememberMaterialDialogState()

    val resultOfAddingDiaryEntry = viewModel.resultOfAddingDiaryEntry.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .height(16.dp)
        )
        Text(
            text = stringResource(id = R.string.add_diary_entry),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addDiaryEntryScreenUiState.heading,
            onValueChange = {
                viewModel.updateHeading(heading = it)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.input_heading),
                )
            },
            isError = addDiaryEntryScreenUiState.headingErrorMessage != null,
            supportingText = {
                Text(
                    text = addDiaryEntryScreenUiState.headingErrorMessage?.let {
                        stringResource(id = it)
                    } ?: ""
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addDiaryEntryScreenUiState.description,
            onValueChange = {
                viewModel.updateDescription(description = it)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.input_diary_entry),
                )
            },
            isError = addDiaryEntryScreenUiState.descriptionErrorMessage != null,
            supportingText = {
                Text(
                    text = addDiaryEntryScreenUiState.descriptionErrorMessage?.let {
                        stringResource(id = it)
                    } ?: ""
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = addDiaryEntryScreenUiState.formattedDate,
                fontSize = 20.sp,
            )
            IconButton(
                onClick = {
                    dateDialogState.show()
                },
            ) {
                Icon(
                    modifier = Modifier
                        .padding(4.dp),
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = stringResource(id = R.string.icon_calendar),
                )
            }
        }
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                if (addDiaryEntryScreenUiState.headingErrorMessage == null &&
                    addDiaryEntryScreenUiState.descriptionErrorMessage == null
                ) {
                    viewModel.addDiaryEntry()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.wrong_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
        ) {
            Text(
                text = stringResource(id = R.string.add),
            )
        }
        DateMaterialDialog(
            dateDialogState = dateDialogState,
            updateData = viewModel::updateDate,
        )
    }

    LaunchedEffect(resultOfAddingDiaryEntry) {
        when (resultOfAddingDiaryEntry) {
            is ResultOfRequest.Success<Unit> -> {
                navController.navigate(Screen.DiaryScreen.route)
            }

            is ResultOfRequest.Error -> {
                Toast.makeText(
                    context,
                    resultOfAddingDiaryEntry.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ResultOfRequest.Loading -> {}
        }
    }
}
