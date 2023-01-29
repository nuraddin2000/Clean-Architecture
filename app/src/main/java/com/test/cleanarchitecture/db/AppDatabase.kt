package com.test.cleanarchitecture.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.cleanarchitecture.db.contacts.ContactsDao
import com.test.cleanarchitecture.db.contacts.DbContact

@Database(entities = [DbContact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ContactsDao
}
