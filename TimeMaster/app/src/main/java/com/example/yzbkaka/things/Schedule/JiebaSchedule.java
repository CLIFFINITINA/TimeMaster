package com.example.yzbkaka.things.Schedule;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yzbkaka.things.R;
import com.example.yzbkaka.things.db.Plan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class JiebaSchedule extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private Button back;

    Plan plan = new Plan();
    Date mdate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        back = (Button)findViewById(R.id.back);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        Calendar calendar = Calendar.getInstance();  //得到今天的时间
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        Intent intent = getIntent();
        final String todayYear = String.valueOf(calendar.get(Calendar.YEAR));
        final String todayMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
        final String todayDay = String.valueOf(calendar.get(Calendar.DATE));

        back.setOnClickListener(v -> finish());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String year = String.valueOf(date.getYear());  //得到选中的时间
                String month = String.valueOf(date.getMonth() + 1);
                String day = String.valueOf(date.getDay());

                if (((year.equals(todayYear) && month.equals(todayMonth) && Integer.parseInt(day) < Integer.parseInt(todayDay)))
                        || (year.equals(todayYear) && Integer.parseInt(month) < Integer.parseInt(todayMonth)) ||
                        (Integer.parseInt(year) < Integer.parseInt(todayYear))) {  //如果选中的是今天，则会创建失败
                    Toast.makeText(JiebaSchedule.this, "请选择未来的时间点哦", Toast.LENGTH_SHORT).show();
                } else {
                    final String content = intent.getStringExtra("content");
                    plan.setWritePlan(content);
                    plan.setYear(year);
                    plan.setMonth(month);
                    plan.setDay(day);
                    mdate = new Date(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
                    plan.save();
                    Toast.makeText(JiebaSchedule.this, "创建成功，记得要完成哦", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
        setLightMode();
    }

    private void setLightMode(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
