package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.WIFIVoidCallback;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.ui.R;
import com.chipsea.view.edit.CustomEditText;
import com.chipsea.view.text.CustomTextView;

import java.util.List;


public class FeedbackActivity extends CommonActivity{
    private static final String TAG = "FeedbackActivity";

    private CustomEditText feedbackContent;
    private CustomTextView submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_feed_back,R.string.settingFeedback);
        feedbackContent=(CustomEditText)findViewById(R.id.feed_back_content);
        submit=(CustomTextView)findViewById(R.id.feed_back_send);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDesc=feedbackContent.getText().toString().trim();
                if(sDesc.length()==0){
                    return;
                }
               //UserUtils.submitFeedback(sDesc, new WIFIVoidCallback() {
               //    @Override
               //    public void onSuccess() {
               //        ActivityBusiness.getInstance().finishActivity(FeedbackActivity.this);
               //    }

               //    @Override
               //    public void onFailure(String msg, int code) {
               //        showToast(StandardUtil.getInstance(FeedbackActivity.this).getMessage(code,msg));
               //    }
               //});
            }
        });
        ActivityBusiness.getInstance().addActivity(this);

    }
}
