package com.example.newsapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class dateformate

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val dateTime = LocalDateTime.parse(dateString, formatter)

    val today = LocalDate.now(ZoneId.of("UTC"))
    val date = dateTime.toLocalDate()

    return when {
        date.isEqual(today) -> "Today"
        date.isEqual(today.minusDays(1)) -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
    }
}