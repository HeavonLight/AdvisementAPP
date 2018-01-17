package com.example.heavon.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    private TextView mCancelView;
    private TextView mSendView;
    private EditText mFeedbackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        init();
    }

    protected void init(){
        mCancelView = (TextView) findViewById(R.id.link_cancel);
        mSendView = (TextView) findViewById(R.id.link_save);
        mFeedbackView = (EditText) findViewById(R.id.feedback);

        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交反馈
                String feedbackStr = mFeedbackView.getText().toString().trim();
                if(TextUtils.isEmpty(feedbackStr)){
                    Toast.makeText(FeedbackActivity.this, "请填写反馈", Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        });
    }
}
