package com.example.mobiory.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mobiory.data.model.Event
import com.example.mobiory.ui.viewModel.EventListViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.round


@Composable
fun SearchBar(
    eventListViewModel: EventListViewModel,
    setEvents: (List<Event>) -> Unit
) {
    val (searchString, setSearchString) = remember { mutableStateOf("") }
    val (searchClicked, setSearchClicked) = remember { mutableStateOf(false) }
    val (sortOption, setSortOption) = remember { mutableStateOf(SortOption.OPTION_A) }

    val (sortClicked, setSortClicked) = remember { mutableStateOf(false) }
    val (expandedFilter,setExpandedFilter) = remember { mutableStateOf(false) }
    var expandedSort by remember { mutableStateOf(false) }

    LaunchedEffect(searchClicked, searchString) {
        if (searchClicked) {
            eventListViewModel.getSearchedEvents(searchString).collect {
                setEvents(it)
                setSearchClicked(false)
            }
        }
    }
    LaunchedEffect(sortClicked, sortOption) {
        if (sortClicked) {
            val option = if (sortOption.label.contains("popularity")) {
                "popularity"
            } else {
                "date"
            }
            val order =
                if (sortOption.label.contains("Highest") or (sortOption.label.contains("Oldest"))) {
                    "asc"
                } else {
                    "desc"
                }
            eventListViewModel.getSortedEvents(option, order).collect {
                setEvents(it)
                setSearchClicked(false)
            }
        }
    }

    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchString,
                onValueChange = { setSearchString(it) },
                label = { Text("Search") },
                modifier = Modifier.width(250.dp)
            )

            IconButton(onClick = { setSearchClicked(true) }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }

            IconButton(onClick = { setExpandedFilter(true) }) {
                Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
            }

            IconButton(onClick = { expandedSort = true }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort")
            }
        }
        if (expandedFilter)
            FilterMenu(
                setExpandedFilter,
                eventListViewModel, setEvents
            )
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            // Sort menu
            DropdownMenu(
                expanded = expandedSort,
                onDismissRequest = { expandedSort = false },
                modifier = Modifier
                    .width(maxWidth / 2),
                offset = DpOffset(maxWidth / 2, 0.dp)

            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(text = { Text(option.label) }, onClick = {
                        setSortOption(option)
                        setSortClicked(true)
                        expandedSort = false
                    })
                }
            }
        }
    }
}

enum class SortOption(val label: String) {
    OPTION_A("Highest popularity"),
    OPTION_B("Lowest popularity"),
    OPTION_C("Most recent"),
    OPTION_D("Oldest")

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInDialog(
    state: DateRangePickerState,
    openDialog: MutableState<Boolean>
) {
    val dateFormatter = DatePickerFormatter(DatePickerDefaults.YearAbbrMonthDaySkeleton)

    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
                state.setSelection(null,null)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("CANCEL")
                }
            }
        ) {
            DateRangePicker(
                modifier = Modifier.weight(1f),
                state = state,

                title = {
                    DateRangePickerDefaults.DateRangePickerTitle(
                        state = state,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp),
                    )
                },
                headline = {
                    DateRangePickerDefaults.DateRangePickerHeadline(
                        state = state,
                        dateFormatter = dateFormatter,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterMenu(
    setExpandedFilter: (Boolean) -> Unit,
    eventListViewModel: EventListViewModel,
    setEvents: (List<Event>) -> Unit
) {
    val (searchString, setSearchString) = remember { mutableStateOf("") }
    val dateState = rememberDateRangePickerState()
    val openDateDialog = remember { mutableStateOf(false) }
    val (startPopularity, setStartPopularity) = remember { mutableFloatStateOf(0f) }
    val (endPopularity, setEndPopularity) = remember { mutableFloatStateOf(700000f) }
    val (filterClicked, setFilterClicked) = remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }

    LaunchedEffect(filterClicked, searchString,dateState.selectedStartDateMillis, dateState.selectedEndDateMillis, startPopularity,endPopularity) {
        if (filterClicked) {

            //date either both null/both have value otherwise error display
            if (!((dateState.selectedStartDateMillis == null) xor (dateState.selectedEndDateMillis == null))){
                eventListViewModel.getFilteredEvents(startPopularity, endPopularity, dateState.selectedStartDateMillis, dateState.selectedEndDateMillis, searchString).collect {
                    setEvents(it)
                    dateError = false
                    setExpandedFilter(false)
                    setFilterClicked(false)
                }
            } else {
                //setExpandedFilter(false)
                dateError = true
                setFilterClicked(false)
            }
        }
    }

    Dialog(onDismissRequest = {
        setExpandedFilter(false)
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Search by popularity range",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,

                    )
                PopularityRangePicker(startPopularity,setStartPopularity,endPopularity,setEndPopularity)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Search by date range",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (dateError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Start and end dates must be defined.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = {
                        openDateDialog.value = true
                        dateError = false
                    }) {
                        if (dateState.selectedStartDateMillis != null)
                            dateState.selectedStartDateMillis?.let { getFormattedDate(it) }
                                ?.let { Text(text = it) } ?: Text(text = "Start date")
                        else
                            Text(text = "Start date")
                    }
                    DatePickerInDialog(state = dateState, openDateDialog)
                    Text(
                        text = "to",
                    )
                    OutlinedButton(onClick = {
                        openDateDialog.value = true
                        dateError = false
                    }) {
                        if (dateState.selectedEndDateMillis != null)
                            dateState.selectedEndDateMillis?.let { getFormattedDate(it) }
                                ?.let { Text(text = it) }
                        else
                            Text(text = "End date")
                    }
                    DatePickerInDialog(state = dateState, openDateDialog)
                }
                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Search by country",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                OutlinedTextField(
                    value = searchString,
                    onValueChange = { setSearchString(it) },
                    label = { Text(if (searchString != "") searchString else "Search") },
                    modifier = Modifier.width(200.dp)
                )
                Spacer(modifier = Modifier.height(50.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { setExpandedFilter(false) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            setFilterClicked(true)
                                  },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
fun getFormattedDate(timeInMillis: Long): String {
    val calender = Calendar.getInstance()
    calender.timeInMillis = timeInMillis
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("FR"))
    return dateFormat.format(calender.timeInMillis)
}

@Composable
fun PopularityRangePicker(
    startValue: Float,
    setStartValue: (Float) -> Unit,
    endValue: Float,
    setEndValue: (Float) -> Unit,
    ) {
    var sliderPosition by remember { mutableStateOf(0f..700000f) }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            ) {
            Text("Start: ${round(startValue)}")
            Text("End: ${round(endValue)}")
        }
        RangeSlider(
            value = sliderPosition,
            steps = 10,
            onValueChange = { range -> sliderPosition = range },
            valueRange = 0f..700000f,
            onValueChangeFinished = {
                setStartValue(sliderPosition.start)
                setEndValue(sliderPosition.endInclusive)
            },
        )
    }
}