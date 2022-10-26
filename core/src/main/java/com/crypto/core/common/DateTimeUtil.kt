package com.crypto.core.common

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateTimeUtil {
  fun now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
  }

  fun toEpochMillis(dateTime: LocalDateTime): Long {
    return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
  }
}
