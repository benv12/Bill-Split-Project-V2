package com.example.bill_split_project

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("DELETE FROM friend_requests")
    suspend fun deleteAllFriends()

    @Query("SELECT user_name FROM user_table")
    fun getUsername(): Array<String>

    @Query("SELECT pass_word FROM user_table")
    fun getPassword(): Array<String>

    @Query("SELECT * FROM user_table WHERE user_name = :username")
    suspend fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFriend(friendRequest: FriendRequest)

    @Query("SELECT sender_name FROM friend_requests")
    fun getSendUsername(): Array<String>

    @Query("SELECT receiver_name FROM friend_requests")
    fun getReceiverUsername(): Array<String>


}