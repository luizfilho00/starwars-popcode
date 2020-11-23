package br.com.mouzinho.starwarspopcode.ui.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.mouzinho.starwarspopcode.data.dao.DbPeopleDao
import br.com.mouzinho.starwarspopcode.data.dao.DbRemoteKeyDao
import br.com.mouzinho.starwarspopcode.data.entity.DbPeople
import br.com.mouzinho.starwarspopcode.data.entity.DbRemoteKey

@Database(
    entities = [DbPeople::class, DbRemoteKey::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun peopleDao(): DbPeopleDao
    abstract fun remoteKeyDao(): DbRemoteKeyDao

    companion object {
        private const val DATABASE_NAME = "star-wars-database"

        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }
}