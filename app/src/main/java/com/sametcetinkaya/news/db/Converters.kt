package com.sametcetinkaya.news.db

import androidx.room.TypeConverter
import com.sametcetinkaya.news.model.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source) = source.name

    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }
}