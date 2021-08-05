package com.nejivicky.newsappnew.room

import androidx.room.TypeConverter
import com.nejivicky.newsappnew.models.Source

class Convertors {
    @TypeConverter
    fun fromSource(source: Source):String?{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }

}