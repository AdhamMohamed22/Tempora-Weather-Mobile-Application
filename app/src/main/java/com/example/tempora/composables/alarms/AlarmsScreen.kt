package com.example.tempora.composables.alarms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tempora.R

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import com.example.tempora.composables.alarms.notification.workmanager.scheduleNotification
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsScreen(
    showFAB: MutableState<Boolean>,
    favouritesFAB: MutableState<Boolean>,
    showAlarmsBottomSheet: MutableState<Boolean>,
    context: Context
) {
    showFAB.value = true
    favouritesFAB.value = false

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedNotifyMe by remember { mutableStateOf("Alarm") }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedTime by remember { mutableStateOf(Calendar.getInstance()) }

    Column(
        modifier = Modifier.fillMaxSize().background(colorResource(R.color.white)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

        if (showAlarmsBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showAlarmsBottomSheet.value = false },
                sheetState = sheetState,
                containerColor = colorResource(R.color.secondaryColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.alarm),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AlarmOptionItem(Icons.Default.DateRange, stringResource(R.string.date), SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)) {
                        showDatePicker(context) { date -> selectedDate = date }
                    }

                    AlarmOptionItem(Icons.Default.Notifications,
                        stringResource(R.string.start_duration), SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime.time)) {
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
                                    scheduleNotification(context, delayInMillis)
                                    showAlarmsBottomSheet.value = false
                                } else {
                                    Toast.makeText(context, context.getString(R.string.please_select_a_valid_future_time), Toast.LENGTH_SHORT).show()
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
