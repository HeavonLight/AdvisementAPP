package com.example.heavon.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heavon.dao.FeedbackDao;
import com.example.heavon.dao.UserDao;
import com.example.heavon.interfaceClasses.HttpResponse;

import java.util.Map;

public class FeedbackActivity extends BasicActivity {

    private TextView mCancelView;
    private TextView mSendView;
    private EditText mFeedbackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initUI();
    }

    protected void initUI(){
        mCancelView = (TextView) findViewById(R.id.link_cancel);
        mSendView = (TextView) findViewById(R.id.link_send);
        mFeedbackView = (EditText) findViewById(R.id.feedback);

        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                    int uid = new UserDao().getUid(FeedbackActivity.this);
                    String content = mFeedbackView.getText().toString().trim();

                    FeedbackDao dao = new FeedbackDao();
                    dao.send(uid, content, new HttpResponse<Map<String, Object>>() {
                        @Override
                        public void getHttpResponse(Map<String, Object> result) {

                            Toast.makeText(FeedbackActivity.this, result.get("msg").toString(), Toast.LENGTH_SHORT).show();
                            //反馈成功
                            if(! (Boolean)result.get("error")){
                                mFeedbackView.setText("");
                            }

                        }
                    });
                }
            }
        });
    }
}
