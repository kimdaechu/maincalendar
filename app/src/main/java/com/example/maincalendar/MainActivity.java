package com.example.maincalendar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private Set<CalendarDay> selectedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        selectedDates = new HashSet<>();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                checkDateAvailability(date.getDate());
            }
        });

        // 토요일과 일요일 표시
        calendarView.addDecorator(new SaturdayDecorator());
        calendarView.addDecorator(new SundayDecorator());
        calendarView.addDecorator(new EventDecorator());
    }

    private void checkDateAvailability(Date date) {
        CalendarDay day = CalendarDay.from(date);
        if (selectedDates.contains(day)) {
            selectedDates.remove(day);
        } else {
            selectedDates.add(day);
        }
        calendarView.invalidateDecorators(); // 데코레이터 갱신
    }

    private class SaturdayDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            Calendar calendar = day.getCalendar();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE)); // 날짜 숫자 색상 변경
        }
    }

    private class SundayDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            Calendar calendar = day.getCalendar();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED)); // 날짜 숫자 색상 변경
        }
    }

    private class EventDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return selectedDates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(getResources().getDrawable(R.drawable.green_circle));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        calendarView.invalidateDecorators(); // 데코레이터 갱신
    }
}
