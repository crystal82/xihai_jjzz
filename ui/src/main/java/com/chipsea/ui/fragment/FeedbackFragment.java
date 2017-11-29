package com.chipsea.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.chipsea.code.listener.WIFIVoidCallback;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.ui.R;
import com.chipsea.view.edit.CustomEditText;
import com.chipsea.view.text.CustomTextView;


public class FeedbackFragment extends LazyFragment{
    private static final String TAG = "FeedbackActivity";

    private CustomEditText feedbackContent;
    private CustomTextView submit;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentSub(R.layout.activity_feed_back,R.string.settingFeedback);
        feedbackContent=(CustomEditText)mParentView.findViewById(R.id.feed_back_content);
        submit=(CustomTextView)mParentView.findViewById(R.id.feed_back_send);
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

               //    }

               //    @Override
               //    public void onFailure(String msg, int code) {
               //        showToast(StandardUtil.getInstance(instance).getMessage(code,msg));
               //    }
               //});
            }
        });

    }
}
