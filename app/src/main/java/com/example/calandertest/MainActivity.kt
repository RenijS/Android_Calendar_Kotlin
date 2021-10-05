package com.example.calandertest

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var selectedDate: LocalDate

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWidgets()
        selectedDate = LocalDate.now()
        setMonthView()
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calRV)
        monthYearText = findViewById(R.id.monthYearTV)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        var daysInMonth : ArrayList<String> = daysInMonthArray(selectedDate)
        calendarRecyclerView.adapter = CalendarAdapter(this,daysInMonth)
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
         var daysInMonthsArr = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        var daysInMonth = yearMonth.lengthOfMonth();

        val firstMonth: LocalDate = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstMonth.dayOfWeek.value
        for ( i in 1..42){
            if (i <= dayOfWeek || i > daysInMonth+dayOfWeek){
                daysInMonthsArr.add("");
            }else{
                daysInMonthsArr.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthsArr
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String{
        var formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonthAction(view: View){
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonthAction(view: View){
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    public fun getMonthYear() : String{
        return monthYearText.text.toString()
    }
}