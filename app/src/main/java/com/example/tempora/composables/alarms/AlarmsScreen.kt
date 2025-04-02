package com.example.tempora.composables.alarms

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.R
import com.example.tempora.composables.alarms.notification.workmanager.scheduleNotification
import com.example.tempora.composables.home.components.LoadingIndicator
import com.example.tempora.data.local.WeatherDatabase
import com.example.tempora.data.local.WeatherLocalDataSource
import com.example.tempora.data.models.Alarm
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.responsestate.AlarmsResponseState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsScreen(
    showFAB: MutableState<Boolean>,
    favouritesFAB: MutableState<Boolean>,
    showAlarmsBottomSheet: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState,
) {
    showFAB.value = true
    favouritesFAB.value = false
    val context = LocalContext.current

    val alarmsScreenViewModelFactory =
        AlarmsScreenViewModel.AlarmsScreenViewModelFactory(
            Repository.getInstance(
                WeatherRemoteDataSource(RetrofitHelper.retrofit),
                WeatherLocalDataSource(WeatherDatabase.getInstance(context).getWeatherDao())
            )
        )
    val viewModel: AlarmsScreenViewModel = viewModel(factory = alarmsScreenViewModelFactory)

    val alarmsState by viewModel.alarm.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.message.collect {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllAlarms()
    }

    when (alarmsState) {
        is AlarmsResponseState.Loading -> LoadingIndicator()
        is AlarmsResponseState.Failed -> Text("Failed !")
        is AlarmsResponseState.Success -> DisplayAlarmsScreen(
            viewModel = viewModel,
            showAlarmsBottomSheet = showAlarmsBottomSheet,
            alarmsList = (alarmsState as AlarmsResponseState.Success).alarms,
            deleteAction = { viewModel.deleteAlarm(it, context) },
            snackBarHostState = snackBarHostState,
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayAlarmsScreen(
    viewModel: AlarmsScreenViewModel,
    showAlarmsBottomSheet: MutableState<Boolean>,
    alarmsList: List<Alarm>,
    deleteAction: (Alarm) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedTime by remember { mutableStateOf(Calendar.getInstance()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (alarmsList.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.noalarms),
                contentDescription = "No Alarms Image",
                modifier = Modifier.size(300.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.no_alarms_has_been_added_yet),
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.primaryColor),
                fontWeight = FontWeight.Bold
            )
        } else {
            ListOfAlarmsCards(alarmsList, deleteAction, viewModel, context)
        }
    }

    // ✅ Always show the modal sheet when `showAlarmsBottomSheet.value` is true
    if (showAlarmsBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showAlarmsBottomSheet.value = false },
            sheetState = sheetState,
            containerColor = colorResource(R.color.secondaryColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.alarm),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                AlarmOptionItem(
                    Icons.Default.DateRange,
                    stringResource(R.string.date),
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                ) {
                    showDatePicker(context) { date -> selectedDate = date }
                }

                AlarmOptionItem(
                    Icons.Default.Notifications,
                    stringResource(R.string.start_duration),
                    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime.time)
                ) {
                    showTimePicker(context) { time -> selectedTime = time }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { showAlarmsBottomSheet.value = false },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.red))
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            val now = Calendar.getInstance().timeInMillis
                            val notificationTime = Calendar.getInstance().apply {
                                set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                                set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                                set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))
                                set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY))
                                set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE))
                                set(Calendar.SECOND, 0)
                            }.timeInMillis

                            val delayInMillis = notificationTime - now

                            if (delayInMillis > 0) {
                                val alarm = Alarm(
                                    selectedDate = selectedDate.toFormattedString("yyyy-MM-dd"),
                                    selectedTime = selectedTime.toFormattedString("hh:mm a")
                                )
                                viewModel.insertAlarm(alarm, context)
                                scheduleNotification(context, delayInMillis)
                                showAlarmsBottomSheet.value = false
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.please_select_a_valid_future_time),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primaryColor))
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}


@Composable
fun AlarmOptionItem(
    icon: ImageVector,
    title: String,
    hint: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, tint = colorResource(R.color.primaryColor))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold, color = colorResource(R.color.primaryColor))
            Spacer(modifier = Modifier.weight(1f))
            Text(hint, color = colorResource(R.color.secondaryColor))
        }
    }
}


@Composable
fun AlarmCard(
    alarm: Alarm,
    deleteAction: (Alarm) -> Unit,
    viewModel: AlarmsScreenViewModel,
    context: Context
) {
    val alarmInsertedTime = alarm.selectedTime
    val timeNow = System.currentTimeMillis()

    val now = millisToTime(timeNow)
    if (now > alarmInsertedTime) {
        viewModel.deleteAlarm(alarm, context)
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.white)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = alarm.selectedDate)
            Text(text = alarm.selectedTime)
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { deleteAction(alarm) },
                colors = ButtonDefaults.buttonColors(colorResource(R.color.primaryColor)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.white),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ListOfAlarmsCards(
    alarmsList: List<Alarm>,
    deleteAction: (Alarm) -> Unit,
    viewModel: AlarmsScreenViewModel,
    context: Context
) {
    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painter = painterResource(id = R.drawable.settingsbackground),
            contentDescription = "SettingsScreen Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.8f)
        )
        LazyColumn(modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(vertical = 32.dp))
        {
            items(alarmsList.size)
            {
                AlarmCard(alarmsList[it], deleteAction, viewModel, context)
            }
        }
    }
}


fun showDatePicker(context: Context, onDateSelected: (Calendar) -> Unit) {
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.CustomDatePickerDialog,  // Apply custom theme
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            onDateSelected(selectedCalendar) // Pass the full Calendar object
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.datePicker.minDate = calendar.timeInMillis // Disable past dates
    datePickerDialog.show()
}


fun showTimePicker(context: Context, onTimeSelected: (Calendar) -> Unit) {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        R.style.CustomTimePickerDialog, // Apply the custom style
        { _, hourOfDay, minute ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            onTimeSelected(selectedCalendar) // Pass the full Calendar object
        },
        currentHour,
        currentMinute + 1, // Start at least 1 minute ahead
        false // Use 12-hour format
    )

    timePickerDialog.show()
}


// Convert Calendar to String (12-hour format)
fun Calendar.toFormattedString(pattern: String = "yyyy-MM-dd hh:mm a"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this.time)
}

fun millisToTime(millis: Long): String {
    val date = Date(millis)
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return format.format(date)
}
