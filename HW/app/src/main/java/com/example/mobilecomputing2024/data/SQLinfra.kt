package com.example.mobilecomputing2024.data
/*
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "userdata")
data class User(
    @PrimaryKey val userName: String = "John Doe",
    val profilePic: Uri? = null,
    val userId: Int = 1,
    val timestamp: Long = 0
)


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: User)

    @Update
    suspend fun update(data: User)

    @Delete
    suspend fun delete(data: User)

    @Query("Select * from userdata")
    fun getAllUsers(): Flow<List<User>>

    @Query("Select * from userdata WHERE userId = :id")
    fun getUserById(id: Int)

    @Query("Select * from userdata ORDER BY timestamp DESC LIMIT 1")
    fun getLatestUser(): Flow<User>

    @Query("Update userdata SET userName = :name, timestamp = :ts WHERE userId = :id")
    suspend fun updateUsername(name: String, ts: Long, id: Int)

    @Query("Update userdata SET profilePic = :pic, timestamp = :ts WHERE userId = :id")
    suspend fun updateProfilepic(pic: Uri, ts: Long, id: Int)
}

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
*/