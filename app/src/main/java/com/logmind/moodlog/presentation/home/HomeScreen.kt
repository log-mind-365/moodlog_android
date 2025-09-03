package com.logmind.moodlog.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.logmind.moodlog.R
import com.logmind.moodlog.presentation.home.components.JournalCard
import com.logmind.moodlog.presentation.home.components.ModernDateHeader
import com.logmind.moodlog.presentation.home.components.ModernMonthlyCalendar
import com.logmind.moodlog.presentation.navigation.Screen
import com.logmind.moodlog.ui.components.MdlAppBar
import com.logmind.moodlog.ui.components.MdlAvatar
import com.logmind.moodlog.ui.components.MdlCard
import com.logmind.moodlog.ui.components.MdlScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    MdlScaffold(
        navController = navController,
        topBar = {
            MdlAppBar(
                title = stringResource(R.string.app_name),
                actions = {
                    MdlAvatar(onClick = {
                        navController.navigate(Screen.Profile.route)
                    })
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.horizontal_padding))
        ) {
            ModernDateHeader(selectedDate = selectedDate)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_l)))
            ModernMonthlyCalendar(
                selectedDate = selectedDate,
                monthlyJournals = uiState.monthlyJournals,
                onDateSelected = viewModel::selectDate
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_l)))
            // Today's Journals
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "오늘의 기록",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Journal List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.selectedDateJournals) { journal ->
                        JournalCard(
                            journal = journal,
                            onDelete = { viewModel.deleteJournal(journal.id) }
                        )
                    }
                    if (uiState.selectedDateJournals.isEmpty()) {
                        item {
                            MdlCard {
                                Text(
                                    text = "아직 기록이 없어요\n첫 번째 무드를 기록해보세요!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

