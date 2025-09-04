package com.logmind.moodlog.presentation.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.logmind.moodlog.R
import com.logmind.moodlog.presentation.statistics.components.StatisticsCards
import com.logmind.moodlog.presentation.statistics.components.TimePeriodFilter
import com.logmind.moodlog.presentation.statistics.components.charts.MoodDistributionChart
import com.logmind.moodlog.presentation.statistics.components.charts.MoodTrendChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier,
    topAppBar: @Composable () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()


    Column {
        topAppBar()
        Column(
            modifier = modifier
        ) {
            TimePeriodFilter(
                selectedPeriod = uiState.selectedPeriod,
                onPeriodSelected = viewModel::selectPeriod,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg)),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    StatisticsCards(
                        averageMood = uiState.averageMood,
                        totalEntries = uiState.totalEntries,
                        streakDays = uiState.streakDays,
                        bestMoodDay = uiState.bestMoodDay
                    )
                }
                item {
                    MoodTrendChart(data = uiState.moodTrends)
                }
                item {
                    MoodDistributionChart(data = uiState.moodDistribution)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Error message
            uiState.errorMessage?.let { error ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "데이터를 불러올 수 없습니다",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { viewModel.refreshStatistics() }
                        ) {
                            Text("다시 시도")
                        }
                    }
                }
            }
        }
    }
}
