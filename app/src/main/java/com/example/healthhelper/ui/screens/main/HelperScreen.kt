package com.example.healthhelper.ui.screens.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.example.healthhelper.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

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