package com.example.healthhelper.ui.screens.main.analysisDetails

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.ui.viewModels.AnalysisDetailsScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnalysisDetailsScreen(
    navController: NavController,
    viewModel: AnalysisDetailsScreenViewModel,
) {

    val analysis by viewModel.analysis.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
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
                text = stringResource(id = R.string.detailed_description),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
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
                            text = stringResource(id = R.string.name),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = analysis.name,
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
                            text = stringResource(id = R.string.unit),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = analysis.unit,
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
                            text = stringResource(id = R.string.result),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = analysis.result.toString(),
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
                            text = stringResource(id = R.string.lowerLimit),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = analysis.lowerLimit.toString(),
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
                            text = stringResource(id = R.string.upperLimit),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = analysis.upperLimit.toString(),
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
                            text = analysis.date,
                            fontSize = 20.sp,
                        )
                    }
                }
            }
        }
    }

}