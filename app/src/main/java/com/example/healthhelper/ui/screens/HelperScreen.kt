package com.example.healthhelper.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.example.healthhelper.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DateMaterialDialog(
    dateDialogState: MaterialDialogState,
    updateData: (localDate: LocalDate) -> Unit,
) {
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.choose),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            )
            negativeButton(
                text = stringResource(id = R.string.cancel),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            )
        },
    ) {
        datepicker(
            title = stringResource(id = R.string.select_date),
            initialDate = LocalDate.now(),
            allowedDateValidator = {
                it <= LocalDate.now()
            },
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                calendarHeaderTextColor = MaterialTheme.colorScheme.primary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                dateActiveTextColor = MaterialTheme.colorScheme.onSecondary,
                dateInactiveTextColor = MaterialTheme.colorScheme.onBackground,
            ),
        ) {
            updateData(it)
        }
    }
}

@Composable
fun TimeMaterialDialog(
    timeDialogState: MaterialDialogState,
    updateData: (localTime: LocalTime) -> Unit,
) {
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.choose),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            )
            negativeButton(
                text = stringResource(id = R.string.cancel),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            )
        },
    ) {
        timepicker(
            title = stringResource(id = R.string.select_date),
            is24HourClock = true,
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                activeTextColor = MaterialTheme.colorScheme.onSecondary,
                inactiveTextColor = MaterialTheme.colorScheme.onBackground,
                selectorColor = MaterialTheme.colorScheme.primary,
                selectorTextColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            updateData(it)
        }
    }
}

@Composable
fun AlertDialogWarning(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        icon = {
            Icon(
                Icons.Filled.Warning,
                contentDescription = stringResource(id = R.string.icon_warning),
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}