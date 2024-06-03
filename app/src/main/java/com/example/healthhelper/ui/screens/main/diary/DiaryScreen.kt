package com.example.healthhelper.ui.screens.main.diary

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.domain.model.DiaryEntry
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.viewModels.DiaryScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryScreen(
    navController: NavController,
    viewModel: DiaryScreenViewModel,
) {

    val diaryEntries by viewModel.diaryEntryUiState.collectAsState()


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddDiaryEntryScreen.route)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.icon_add),
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
            Text(
                text = stringResource(id = R.string.diary_of_well_being),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(diaryEntries) { index, diaryEntry ->
                    DiaryCard(
                        diaryEntry = diaryEntry,
                        index = index,
                        addDiaryEntryToPresenter = viewModel::addDiaryEntryToPresenter,
                        navigateToDetails = navController::navigate,
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .height(56.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryCard(
    diaryEntry: DiaryEntry,
    index: Int,
    addDiaryEntryToPresenter: (index: Int) -> Unit,
    navigateToDetails: (route: String) -> Unit,
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
        onClick = {
            addDiaryEntryToPresenter(index)
            navigateToDetails(Screen.DiaryEntryDetailsScreen.route)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = diaryEntry.date,
                        fontSize = 16.sp,
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = if (diaryEntry.heading.length <= 20) {
                            diaryEntry.heading
                        } else {
                            diaryEntry.heading.substring(0, 20) + "..."
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,

                        )
                }
                Text(
                    text = if (diaryEntry.description.length <= 30) {
                        diaryEntry.description
                    } else {
                        diaryEntry.description.substring(0, 30) + "..."
                    },
                    fontSize = 16.sp,
                )

            }
        }
    }
}