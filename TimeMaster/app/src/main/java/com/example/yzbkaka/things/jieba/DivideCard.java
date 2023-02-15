package com.example.yzbkaka.things.jieba;


import static android.support.v4.content.ContextCompat.startActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.things.R;

import com.example.yzbkaka.things.Schedule.JiebaSchedule;
import com.example.yzbkaka.things.Schedule.ScheduleActivity;
import com.example.yzbkaka.things.Schedule.ScheduleCreateActivity;
import com.example.yzbkaka.things.db.Log;
import com.example.yzbkaka.things.db.Plan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DivideCard extends LinearLayout implements TagFlowLayout.OnSelectListener, View.OnClickListener {

    private Context mContext;
    private TextView mCopy;
    private TextView mSelectAll;
    private TextView msetplan;
    private TextView msetlog;
    private TagFlowLayout mFlowLayout;
    private List<String> mAllWords;
    private Set<Integer> mSelectPosSet;

    private MaterialCalendarView calendarView;

    Log log = new Log();
    Plan plan = new Plan();
    Date mdate;
    Calendar calendar = Calendar.getInstance();

    public DivideCard(Context context) {
        this(context, null);
    }

    public DivideCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.jieba_ui_divider, this);

        mCopy = findViewById(R.id.divide_action_copy);
        mSelectAll = findViewById(R.id.divide_action_all);
        msetplan = findViewById(R.id.divide_action_setplan);
        msetlog = findViewById(R.id.divide_action_search);
        mFlowLayout = findViewById(R.id.ui_divide_flow);

        mFlowLayout.setOnSelectListener(this);
        mCopy.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        msetplan.setOnClickListener(this);
        msetlog.setOnClickListener(this);
    }

    public void setWords(List<String> words) {
        this.mAllWords = words;
        mFlowLayout.setAdapter(new TagAdapter<String>(words) {
            @Override
            public View getView(FlowLayout flowLayout, int i, String word) {
                TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.jieba_ui_divider_item, flowLayout, false);
                view.setText(word);
                return view;
            }
        });
    }

    @Override
    public void onSelected(Set<Integer> selectPosSet) {
        this.mSelectPosSet = selectPosSet;
        applyActionsState();
    }

    @Override
    public void onClick(View v) {
        if (v == mSelectAll) {
            if (mSelectPosSet != null && mAllWords.size() == mSelectPosSet.size()) {
                selectNone();
                return;
            }
            selectAll();
        } else if (v == mCopy) {
            copyToClipboard();
        } else if (v == msetplan) {
            setplan();
        } else if (v == msetlog) {
            setlog();
        }
    }

    private void selectAll() {
        mSelectPosSet = new HashSet<>();
        for (int i = 0; i < mAllWords.size(); i++) {
            mSelectPosSet.add(i);
        }
        mFlowLayout.getAdapter().setSelectedList(mSelectPosSet);
        mSelectAll.setText(R.string.divide_action_none);
        applyActionsState();
    }

    private void selectNone() {
        mSelectPosSet.clear();
        mFlowLayout.getAdapter().setSelectedList(mSelectPosSet);
        mSelectAll.setText(R.string.divide_action_all);
        applyActionsState();
    }

    private String getSelectedWords() {
        StringBuilder sb = new StringBuilder("");
        for (Integer integer : mSelectPosSet) {
            sb.append(mAllWords.get(integer));
        }
        return sb.toString();
    }

    private void applyActionsState() {
        if (mSelectPosSet == null || mSelectPosSet.isEmpty()) {
            mCopy.setEnabled(false);
            msetplan.setEnabled(false);
            msetlog.setEnabled(false);
        } else {
            mCopy.setEnabled(true);
            msetplan.setEnabled(true);
            msetlog.setEnabled(true);
        }
    }

    private void copyToClipboard() {
        String content = getSelectedWords();
        ClipboardManager cbm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("trio_divided", content);
        cbm.setPrimaryClip(data);
        Toast.makeText(mContext, "已复制到剪切板", Toast.LENGTH_SHORT).show();
    }

    private void setplan() {
        String content = getSelectedWords();
        if(!content.isEmpty()) {
            Intent intent = new Intent(mContext, JiebaSchedule.class);
            intent.putExtra("content",content);
            mContext.startActivity(intent);
        }
    }

    private void setlog() {
        String content = getSelectedWords();
        if(!content.isEmpty()) {
            log.setLogWrite(content);
            log.save();
            Toast.makeText(mContext,"添加成功！",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    private boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


}

