package com.example.yzbkaka.things.jieba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yzbkaka.things.R;

import java.util.ArrayList;
import java.util.Objects;

import jackmego.com.jieba_android.JiebaSegmenter;

public class JiebaActivity extends AppCompatActivity {
    private Button back;
    private Button divideButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jieba);

        back = findViewById(R.id.back);
        divideButton = findViewById(R.id.divideButton);
        editText = findViewById(R.id.editText);

        back.setOnClickListener(v -> finish());

        divideButton.setOnClickListener(v -> {
            ArrayList<String> wordlist = Objects.requireNonNull(JiebaSegmenter.getJiebaSegmenterSingleton())
                                         .getDividedString(editText.getText().toString());
            CustomBottomSheetDialog customBottomSheetDialog = new CustomBottomSheetDialog(JiebaActivity.this,wordlist);
            customBottomSheetDialog.show();
        });
    }
}
