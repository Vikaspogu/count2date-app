package com.vikaspogu.count2date.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vikaspogu.count2date.R
import com.vikaspogu.count2date.data.model.Event
import com.vikaspogu.count2date.ui.utils.getDaysLeft
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(modifier: Modifier, viewModel: HomeScreenViewModel = hiltViewModel()) {
    val eventsUiState by viewModel.eventsUiState.collectAsStateWithLifecycle()
    var openDialog by remember {
        mutableStateOf(false)
    }
    var isVisible by remember { mutableStateOf(true) }

    // Nested scroll for control FAB
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Hide FAB
                if (available.y < -1) {
                    isVisible = false
                }

                // Show FAB
                if (available.y > 1) {
                    isVisible = true
                }

                return Offset.Zero
            }
        }
    }


    Scaffold(modifier = modifier
        .fillMaxSize()
        .padding(top = 10.dp), topBar = {
        CenterAlignedTopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayMedium
                )
                Image(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size))
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .clip(CircleShape),
                    painter = painterResource(id = R.drawable.count2date_icon),
                    contentDescription = null
                )
            }
        })
    }, floatingActionButton = {
        AnimatedVisibility(
            visible = isVisible, enter = slideInVertically(initialOffsetY = { it * 2 }),
            exit = slideOutVertically(targetOffsetY = { it * 2 }),
        ) {
            FloatingActionButton(
                onClick = {
                    openDialog = true
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add)
                )
            }
        }
        when {
            (openDialog) -> {
                DialogManageEvent(
                    onDismissRequest = { openDialog = false },
                    onConfirmation = { openDialog = false },
                    event = Event(0, "", Calendar.getInstance().timeInMillis),
                    actionType = stringResource(id = R.string.add),
                    viewModel = viewModel
                )
            }
        }


    }) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 125.dp)
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            itemsIndexed(
                items = eventsUiState.eventsList,
                key = { _, item -> item.hashCode() }) { _, event ->
                EventItem(event = event, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DialogManageEvent(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    event: Event,
    actionType: String,
    viewModel: HomeScreenViewModel
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        val context = LocalContext.current

        viewModel.updateEventDescription(event.description)
        viewModel.updateEventDate(event.eventDate)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.manage_event),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(25.dp))
                OutlinedTextField(
                    value = viewModel.eventDescription.value,
                    onValueChange = { viewModel.updateEventDescription(it) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.description),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedTextField(
                        value = viewModel.eventDate.value.formatDate(),
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                        label = {
                            Text(
                                text = stringResource(R.string.date),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                showDatePicker(
                                    Calendar.getInstance()
                                        .apply { timeInMillis = viewModel.eventDate.value },
                                    context,
                                    onDateSelected = {
                                        viewModel.updateEventDate(it)
                                    }
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.DateRange,
                                    contentDescription = stringResource(R.string.date)
                                )
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = { onDismissRequest() },
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            onConfirmation()
                            if (actionType == "Add") {
                                viewModel.addEvent(
                                    Event(
                                        0,
                                        viewModel.eventDescription.value,
                                        viewModel.eventDate.value
                                    )
                                )
                            } else {
                                viewModel.updateEvent(event.id)
                            }

                        },
                    ) {
                        Text(
                            if (actionType == "Add") {
                                stringResource(R.string.save)
                            } else {
                                stringResource(R.string.update)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color(0xFFFF1744)
    } else {
        Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(event: Event, viewModel: HomeScreenViewModel) {
    val context = LocalContext.current
    val eventId by remember {
        mutableIntStateOf(event.id)
    }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    viewModel.deleteEvent(eventId)
                    Toast.makeText(context,
                        context.getString(R.string.event_deleted), Toast.LENGTH_SHORT).show()
                }
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier,
        backgroundContent = { DismissBackground(dismissState) },
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        content = {
            EventCard(event = event, viewModel = viewModel)
        })
}

@Composable
fun EventCard(event: Event, viewModel: HomeScreenViewModel) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shadowElevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
    ) {
        when {
            (openDialog) -> {
                DialogManageEvent(
                    onDismissRequest = { openDialog = false },
                    onConfirmation = { openDialog = false },
                    event = Event(event.id, event.description, event.eventDate),
                    actionType = stringResource(id = R.string.update),
                    viewModel = viewModel
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.clickable {
                openDialog = true
            }
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            ) {
                Text(
                    text = event.description,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.inverseSurface,
                )
                Text(
                    text = event.eventDate.formatDate(),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.inverseSurface,
                )

            }
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            ) {
                Text(
                    text = getDaysLeft(event.eventDate).toString(),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.inverseSurface,
                )
                Text(
                    text = "days",
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                )

            }
        }
    }
}

private fun showDatePicker(
    date: Calendar,
    context: Context,
    onDateSelected: (Long) -> Unit
) {

    val tempDate = Calendar.getInstance()
    val datePicker = android.app.DatePickerDialog(
        context,
        { _, year, month, day ->
            tempDate[Calendar.YEAR] = year
            tempDate[Calendar.MONTH] = month
            tempDate[Calendar.DAY_OF_MONTH] = day
            onDateSelected(tempDate.timeInMillis)
        },
        date[Calendar.YEAR],
        date[Calendar.MONTH],
        date[Calendar.DAY_OF_MONTH]
    )
    datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis - 1000
    datePicker.show()
}

@Composable
fun Long.formatDate(): String {
    val sdf = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
    return sdf.format(this)
}
