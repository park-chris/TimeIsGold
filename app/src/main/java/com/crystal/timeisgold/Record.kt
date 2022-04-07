package com.crystal.timeisgold

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*

@Entity
data class Record(@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var item: String = "default",
                  var date: Date = Date(),
                  var durationTime: Int = 0,
                  var memo: String = ""
)


// 저장 날짜 , 운동 시작 시간, 소요 시간, 항목, 메모
