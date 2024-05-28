package com.example.healthhelper.ui.screens.main.analysis

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
    val context = LocalContext.current

    var analyzes by remember {
        mutableStateOf(listOf<AnalysisUiState>())
    }

    val resultOfLoadAnalyzes = viewModel.resultOfLoadAnalyzes.collectAsState().value

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
                    text = stringResource(id = R.string.analyzes),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
                IconButton(
                    modifier = Modifier
                        .height(32.dp)
                        .width(32.dp),
                    onClick = { /*TODO*/ },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = stringResource(id = R.string.icon_filter),
                    )

                }
            }
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
                items(analyzes) { analysis ->
                    AnalysisCard(
                        analysis = analysis,
                    )
                }
            }
        }

        LaunchedEffect(resultOfLoadAnalyzes) {
            when (resultOfLoadAnalyzes) {
                is ResultOfRequest.Success ->
                    analyzes = resultOfLoadAnalyzes.result

                is ResultOfRequest.Error -> {
                    Toast.makeText(
                        context,
                        resultOfLoadAnalyzes.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                }


                is ResultOfRequest.Loading -> {}
            }
        }
    }
}

@Composable
fun AnalysisCard(
    analysis: AnalysisUiState,
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
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
