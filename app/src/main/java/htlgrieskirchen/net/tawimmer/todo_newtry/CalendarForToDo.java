package htlgrieskirchen.net.tawimmer.todo_newtry;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class CalendarForToDo {

    private int mYear, mMonth, mDay, mHour, mMinute;

    public CalendarForToDo() {
    }

    public void createDatePickerDialog(Activity activity, EditText txtDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String temp ="";
                            if((""+monthOfYear).length()<2){
                                temp = "0";
                            }
                            String tempDay ="";
                            if((""+dayOfMonth).length()<2){
                                tempDay = "0";
                            }
                            txtDate.setText(tempDay+dayOfMonth + "." +temp +(monthOfYear + 1) + "." + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        public void createTimePickerDialog(Activity activity, EditText txtTime) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            txtTime.setText(formatTime(hourOfDay, minute));
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }




    private String formatTime(int hour, int minute) {
        StringBuilder sb = new StringBuilder();
        String tHour = String.valueOf(hour);
        String tMinute = String.valueOf(minute);
        if (tHour.length() < 2) {
            tHour = "0" + tHour;
        } else if (tMinute.length() < 2) {
            tMinute = "0" + tMinute;
        }
        return tHour + ":" + tMinute;
    }
}
