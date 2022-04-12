package com.crystal.timeisgold

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Target(@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var item: String = "",
                  var date: Date = Date(),
                  var duration: Int = 0,
                  var targetTime: Int = 0,
                  var time: Int = 0,
                  var timeOver: Boolean = false
)
