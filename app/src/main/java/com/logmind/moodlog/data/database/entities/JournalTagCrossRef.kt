package com.logmind.moodlog.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "journal_tag_cross_ref",
    primaryKeys = ["journal_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = JournalEntity::class,
            parentColumns = ["id"],
            childColumns = ["journal_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["journal_id"]),
        Index(value = ["tag_id"])
    ]
)
data class JournalTagCrossRef(
    @ColumnInfo(name = "journal_id")
    val journalId: Int,

    @ColumnInfo(name = "tag_id")
    val tagId: Int
)