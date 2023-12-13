package com.example.bill_split_project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(

    @PrimaryKey(autoGenerate = true)val id: Int?,
    @ColumnInfo(name = "user_name")val userName: String?,
    @ColumnInfo(name = "pass_word")val passWord: String?,

)

@Entity(tableName = "friend_requests")
data class FriendRequest(
    @PrimaryKey(autoGenerate = true)val id: Int?,
    @ColumnInfo(name = "sender_name")val senderName: String?,
    @ColumnInfo(name = "receiver_name")val receiverName: String?,
    @ColumnInfo(name = "status")val status: String = "pending"
)
