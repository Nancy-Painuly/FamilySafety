package com.example.myfamily

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ContactModel::class],version = 1, exportSchema = false)
public abstract class MyFamilyDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactDao


    companion object{ //like static in java

        @Volatile //make instance thread safe
        private var INSTANCE:MyFamilyDatabase? = null

        fun getDatabase(context: Context):MyFamilyDatabase{


            INSTANCE?.let{  //it: MyFamilyDatabase
                return it
            }

            return synchronized(MyFamilyDatabase::class.java) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyFamilyDatabase::class.java,
                    "my_family_db"
                ).build()

                INSTANCE = instance

                instance
            }
        }

    }

}