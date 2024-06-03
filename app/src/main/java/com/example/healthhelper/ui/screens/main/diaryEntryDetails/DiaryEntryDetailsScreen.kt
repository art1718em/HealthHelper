package com.example.healthhelper.ui.screens.main.diaryEntryDetails

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.healthhelper.ui.screens.AlertDialogWarning
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.viewModels.DiaryEntryDetailsScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryEntryDetailsScreen(
    navController: NavController,
    viewModel: DiaryEntryDetailsScreenViewModel,
) {
    val context = LocalContext.current

    val diaryEntry by viewModel.diaryEntry.collectAsState()

    val resultOfDeletionDiaryEntry = viewModel.resultOfDeletionDiaryEntry.collectAsState().value

    var openAlertDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.EditDiaryEntryScreen.route)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(id = R.string.icon_edit),
                )
            }
        }
    ) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.detailed_description),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    modifier = Modifier
                        .height(32.dp)
                        .width(32.dp),
                    onClick = { openAlertDialog = true },
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(4.dp),
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.icon_delete),
                    )

                }
            }
            Spacer(
                modifier = Modifier
                    .height(16.dp),
            )
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(4.dp),
                ) {
                    Row {
                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp),
                            text = stringResource(id = R.string.heading),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = diaryEntry.heading,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp),
                            text = stringResource(id = R.string.diary_entry),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = diaryEntry.description,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp),
                            text = stringResource(id = R.string.date),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = diaryEntry.date,
                            fontSize = 20.sp,
                        )
                    }
                }
            }

            if (openAlertDialog) {
                AlertDialogWarning(
                    onDismissRequest = { openAlertDialog = false },
                    onConfirmation = {
                        openAlertDialog = false
                        viewModel.deleteDiaryEntry()
                    },
                    dialogTitle = stringResource(id = R.string.warning),
                    dialogText = stringResource(id = R.string.want_delete_diary_entry),
                )
            }

            LaunchedEffect(resultOfDeletionDiaryEntry) {
                when (resultOfDeletionDiaryEntry) {
                    is ResultOfRequest.Success<Unit> -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.successful_deletion_diary_entry),
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(Screen.DiaryScreen.route)
                    }

                    is ResultOfRequest.Error -> {
                        Toast.makeText(
                            context,
                            resultOfDeletionDiaryEntry.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ResultOfRequest.Loading -> {}
                }
            }
        }
    }
}