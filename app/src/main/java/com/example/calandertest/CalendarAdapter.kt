package com.example.calandertest

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(private val context: Context, arr : ArrayList<String>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private val daysOfMonths = arr

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val dayOfMonth: TextView = view.findViewById(R.id.cellDayText)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonths[position]
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "clicked ${holder.dayOfMonth.text}", Toast.LENGTH_SHORT).show()
            addEventDialog()
        }
    }

    override fun getItemCount(): Int {
        return daysOfMonths.size
    }

    private fun addEventDialog(){
        val dialogBuilder = Dialog(context)
        dialogBuilder.setCancelable(true)
        dialogBuilder.setContentView(R.layout.add_newevent_layout)
        dialogBuilder.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val eventName = dialogBuilder.findViewById<EditText>(R.id.events_id)
        val eventTime = dialogBuilder.findViewById<TextView>(R.id.eventTime)
        val setTime = dialogBuilder.findViewById<ImageButton>(R.id.setEventTime)
        val addEvent = dialogBuilder.findViewById<Button>(R.id.addEvent)
        setTime.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            val tpD = TimePickerDialog(dialogBuilder.context,R.style.ThemeOverlay_AppCompat_Dialog
                , TimePickerDialog.OnTimeSetListener { timePicker, hr, min ->
                    val c = Calendar.getInstance()
                    c.set(Calendar.HOUR_OF_DAY, hr)
                    c.set(Calendar.MINUTE, min)
                    c.timeZone = TimeZone.getDefault()
                    val hformat = SimpleDateFormat("K:mm a", Locale.ENGLISH)
                    val event_time = hformat.format(c.time)
                    eventTime.text = event_time
                },hour, minute, false)
            tpD.show()
        }
        addEvent.setOnClickListener {

            val startMillis: Long = Calendar.getInstance().run {
                set(2021, 9, 14, 7, 30)
                timeInMillis
            }
            val endMillis: Long = Calendar.getInstance().run {
                set(2021, 9, 14, 8, 45)
                timeInMillis
            }
            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.TITLE, eventName.text.toString())
                put(CalendarContract.Events.DESCRIPTION, "Group workout")
                put(CalendarContract.Events.CALENDAR_ID, 1)
                put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Tokyo")
            }
            val cr: ContentResolver = context.applicationContext.contentResolver
            val uri: Uri = cr.insert(CalendarContract.Events.CONTENT_URI, values)!!
            val eventID: Long = uri.lastPathSegment!!.toLong()
            println(uri)
        }
        dialogBuilder.show()
    }

    private fun saveEvent(event: String, date: String, month:String, year: String){

    }
}