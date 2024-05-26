package com.example.healthhelper.ui.screens.main.addAnalysis

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.viewModels.AddAnalysisScreenViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun AddAnalysisScreen(
    navController: NavController,
    viewModel: AddAnalysisScreenViewModel,
) {

    val context = LocalContext.current

    val addAnalysisScreenUiState by viewModel.addAnalysisScreenUiState.collectAsState()

    val resultOfRequest = viewModel.resultOfAddingAnalysis.collectAsState().value

    val dateDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .height(16.dp),
        )
        Text(
            text = stringResource(id = R.string.add_analysis),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addAnalysisScreenUiState.name,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateName(name = it)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.input_analysis_name),
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addAnalysisScreenUiState.unit,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateUnit(unit = it)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.input_analysis_unit),
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addAnalysisScreenUiState.result,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateResult(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = {
                Text(
                    text = stringResource(id = R.string.input_analysis_result),
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addAnalysisScreenUiState.lowerLimit,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateLowerLimit(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = {
                Text(
                    text = stringResource(id = R.string.input_analysis_lower_limit),
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addAnalysisScreenUiState.upperLimit,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateUpperLimit(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = {
                Text(
                    text = stringResource(id = R.string.input_analysis_upper_limit),
                )
            },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = addAnalysisScreenUiState.formattedDate,
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
                viewModel.addAnalysis()
            },
        ) {
            Text(
                text = stringResource(id = R.string.add),
            )
        }
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.choose))
            negativeButton(stringResource(id = R.string.cancel))
        },
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            allowedDateValidator = {
                it <= LocalDate.now()
            },
        ) {
            viewModel.updateDate(it)
        }
    }

    LaunchedEffect(resultOfRequest) {
        when (resultOfRequest) {
            is ResultOfRequest.Success<Unit> -> {
                navController.navigate(Screen.AnalysisScreen.route)
            }

            is ResultOfRequest.Error -> {
                Toast.makeText(
                    context,
                    resultOfRequest.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ResultOfRequest.Loading -> {}
        }
    }
}
