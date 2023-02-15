package com.example.yzbkaka.things.jieba;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.example.yzbkaka.things.R;

import java.util.ArrayList;

public class CustomBottomSheetDialog extends BottomSheetDialog {
    private final Context context;

    public CustomBottomSheetDialog(@NonNull Context context, ArrayList<String> targetItems) {
        super(context);
        this.context = context;

        create(targetItems);
    }

    public void create(ArrayList<String> targetItems) {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.divided_sentence_layout, null);
        setContentView(bottomSheetView);

        ((View)bottomSheetView.getParent()).setBackgroundColor(context.getResources().getColor(R.color.transparent));

        DivideCard divideCard = bottomSheetView.findViewById(R.id.divide_layout);
        divideCard.setWords(targetItems);
    }
}
