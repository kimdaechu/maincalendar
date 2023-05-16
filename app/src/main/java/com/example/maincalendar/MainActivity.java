package com.example.maincalendar;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                checkDateAvailability(date.getDate());
            }
        });
    }

    private void checkDateAvailability(Date date) {
        // 서버에서 해당 날짜의 가능 여부를 가져온 후 UI에 반영하는 코드를 작성합니다.
        // 이 부분은 서버와 통신하는 방법에 따라 다를 수 있습니다.
        // 서버에서 가져온 가능 여부를 isAvailable 변수에 저장합니다.

        boolean isAvailable = true; // 예시로 true로 설정했습니다.

        CalendarDay day = CalendarDay.from(date);
        if (isAvailable) {
            calendarView.setDateSelected(day, true);
            calendarView.setSelectionColor(Color.GREEN);
        } else {
            calendarView.setDateSelected(day, false);
            calendarView.setSelectionColor(Color.TRANSPARENT);
        }
    }
}