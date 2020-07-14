package com.example.minimalistapp

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max


class SQLiteDataBaseHelper private constructor(
    context: Context?,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    private val TABLE_COUNT: String = "TableCount"
    object ISqLiteInterface{
        fun getSQLite(context: Context?): SQLiteDataBaseHelper{
            val sqliteInstance: SQLiteDataBaseHelper by lazy {
                SQLiteDataBaseHelper(context,"minimalist.db",null,1)
            }
            return sqliteInstance
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val sqlTable = "CREATE TABLE IF NOT EXISTS $TABLE_COUNT(Id INTEGER PRIMARY KEY AUTOINCREMENT, Count TEXT, Date TEXT, MaxTime INTEGER, AllTime INTEGER);"
        db?.execSQL(sqlTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i("SQLite","Upgrade")
    }
    fun insertCount(count: Int, date: String,maxTime: Int,allTime: Int){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("Count",count)
        cv.put("Date",date)
        cv.put("MaxTime", maxTime)
        cv.put("AllTime",allTime)
        db.insertOrThrow(TABLE_COUNT,null,cv)
        db.close()
    }


    fun selectCount(): MutableList<Register>{
        var list = mutableListOf<Register>()
        val db = this.writableDatabase
        val c =
            db.rawQuery("SELECT * FROM $TABLE_COUNT",null)
        var i = 0
        while (c.moveToNext()) {
            list.add(Register(c.getString(2),c.getInt(1),c.getInt(3),c.getInt(4)))
        }
        c.close()
        db.close()
        if(list.size > 7){
            list = list.subList(list.size -7, list.size)
        }

        return list
    }
    fun getCountDataForChart() : BarDataSet {
        var entries = ArrayList<BarEntry>()
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM $TABLE_COUNT",null)
        var count = 0
        c.moveToLast()
        val a = c.count
        do{
            if(c.count == 0){
                break
            }
            entries.add(BarEntry(c.getInt(0).toFloat(),c.getInt(1).toFloat()))
            count++
            if(count == 7){
                break
            }
        }
        while(c.moveToPrevious())

        c.close()
        db.close()

        if(entries.size <7){
            for(i in entries.size+1 ..7){
                entries.add(BarEntry(i.toFloat(),0f))
            }

        }
        /*if(entries.size > 7){
            val tmp = entries.subList(entries.size - 7,entries.size)
            entries.addAll(tmp)
        }*/
        val barDataSet = BarDataSet(entries,"Counts")
        return barDataSet
    }

    fun getMaxTimeForChart(): BarDataSet{
        var entries = ArrayList<BarEntry>()
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT Id,MaxTime FROM $TABLE_COUNT", null)
        c.moveToLast()
        var count = 0
        do{
            if(c.count == 0){
                break
            }
            entries.add(BarEntry(c.getInt(0).toFloat(),c.getInt(1).toFloat()))
            count++
            if(count==7){
                break
            }
        }while(c.moveToPrevious())


        c.close()
        db.close()
        if(entries.size <7){
            for(i in entries.size+1 ..7){
                entries.add(BarEntry(i.toFloat(),0f))
            }

        }
        /*if(entries.size > 7){
            val tmp = entries.subList(entries.size - 7,entries.size)
            entries.addAll(tmp)
        }*/
        val barDataSet = BarDataSet(entries,"Max Time")
        barDataSet.color = R.color.colorBlue
        return barDataSet
    }

    fun getDate(index: Int): String{
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM $TABLE_COUNT", null)
        val count = c.count
        if(count == 0){
            c.close()
            db.close()
            return "0"
        }
        if(index>count){
            return index.toString()
        }
        val tmp = if(index >0) index-1 else 0
        c.moveToPosition(tmp)
        val date = c.getString(2)

        c.close()
        db.close()
        return date.substring(0,date.length-5)
    }


}