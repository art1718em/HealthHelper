package com.example.healthhelper.ui.screens.main.analysis

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.theme.analysis_bad
import com.example.healthhelper.ui.theme.analysis_medium
import com.example.healthhelper.ui.theme.analysis_normal
import com.example.healthhelper.ui.viewModels.AnalysisScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnalysisScreen(
    navController: NavController,
    viewModel: AnalysisScreenViewModel,
) {

    val analyzes by viewModel.analyzesUiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddAnalysisScreen.route)
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
                text = stringResource(id = R.string.analyzes),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = R.string.indicator),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.outline,
                )
                Text(
                    text = stringResource(id = R.string.result),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(analyzes) { index, analysis ->
                    AnalysisCard(
                        analysis = analysis,
                        index = index,
                        addAnalysisToPresenter = viewModel::addAnalysisToPresenter,
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
fun AnalysisCard(
    analysis: AnalysisUiState,
    index: Int,
    addAnalysisToPresenter: (index: Int) -> Unit,
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
            addAnalysisToPresenter(index)
            navigateToDetails(Screen.AnalysisDetailsScreen.route)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${analysis.name}, ${analysis.unit}",
                fontSize = 20.sp,
            )
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = analysis.result.toString(),
                    fontSize = 20.sp,
                )
                TextOfNorm(
                    analysis.normality,
                )
            }
        }
    }
}

@Composable
fun TextOfNorm(
    normality: AnalysisNormalityUiState,
) {
    val color = when (normality) {
        is AnalysisNormalityUiState.Normal -> analysis_normal
        is AnalysisNormalityUiState.Deviation.Medium -> analysis_medium
        is AnalysisNormalityUiState.Deviation.Bad -> analysis_bad
    }
    val text = when (normality) {
        is AnalysisNormalityUiState.Normal -> stringResource(id = R.string.normal)
        is AnalysisNormalityUiState.Deviation -> if (normality.isUpper) {
            stringResource(id = R.string.upper_by) + " ${normality.diff}"
        } else {
            stringResource(id = R.string.lower_by) + " ${normality.diff}"
        }
    }

    Text(
        text = text,
        fontSize = 20.sp,
        color = color,
    )
}
