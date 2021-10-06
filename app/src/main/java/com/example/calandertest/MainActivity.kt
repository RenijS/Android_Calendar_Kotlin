package com.example.calandertest

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    val READ_CALENDAR_RQ = 101
    val WRITE_CALENDAR_RQ = 102

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var selectedDate: LocalDate

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForPermission(android.Manifest.permission.READ_CALENDAR, "read calendar", READ_CALENDAR_RQ)
        checkForPermission(android.Manifest.permission.WRITE_CALENDAR, "write calendar", WRITE_CALENDAR_RQ)
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


    private fun checkForPermission(permission: String, name: String, requestCode: Int){
        when{
            ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED ->{
                Toast.makeText(this, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> showDialog(permission, name, requestCode)

            else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String){
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }
        when(requestCode){
            READ_CALENDAR_RQ -> innerCheck("read calendar")
            WRITE_CALENDAR_RQ -> innerCheck("write calendar")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showDialog(permission: String, name: String, requestCode: Int){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access $name is required")
            setTitle("Permission Required")
            setPositiveButton("OK"){dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.show()
        dialog.show()
    }

}