package com.example.maincalendar;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private Set<CalendarDay> selectedDates;

    //파이어베이스 실시간 데이터베이스 선언
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //카카오톡 해시키를 얻는 메소드
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }

        //파이어베이스 실시간 데이터베이스
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
// Git 연동 테스트용 주석
//아이고난1 아이고난2 흐헤헤
