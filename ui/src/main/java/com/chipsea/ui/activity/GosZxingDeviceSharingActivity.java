package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chipsea.code.util.PrefsUtil;
import com.chipsea.ui.GosBaseActivity;
import com.chipsea.ui.R;
import com.chipsea.ui.utils.DateUtil;
import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;

public class GosZxingDeviceSharingActivity extends CommonActivity {

    private String code;
    private int time = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gos_devicesharing_zxing_activity);
        //setActionBar(true, true, R.string.QR_code);
        initData();
        initView();
    }

    private void initData() {

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        userName = intent.getStringExtra("userName");
        productName = intent.getStringExtra("productName");
        deviceAlias = intent.getStringExtra("deviceAlias");
        expiredAt = intent.getStringExtra("expiredAt");

        token = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, "");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (time > 0) {
            tip = split2s[0] + time + split2s[1];

            tiptext.setText(tip);
        } else {
            tiptext.setText(getResources().getString(R.string.requestoutoftime));
            yes.setClickable(false);
            yes.setTextColor(getResources().getColor(R.color.gray_text));
        }

        GizDeviceSharing.setListener(new GizDeviceSharingListener() {
            @Override
            public void didAcceptDeviceSharingByQRCode(GizWifiErrorCode result) {
                super.didAcceptDeviceSharingByQRCode(result);

                if (result.ordinal() == 0) {
                    Toast.makeText(GosZxingDeviceSharingActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(GosZxingDeviceSharingActivity.this, toastError(result), Toast.LENGTH_SHORT).show();

                    finish();
                }
            }

            @Override
            public void didCheckDeviceSharingInfoByQRCode(GizWifiErrorCode result, String userName, String productName,
                                                          String deviceAlias, String expiredAt) {
                super.didCheckDeviceSharingInfoByQRCode(result, userName, productName, deviceAlias, expiredAt);

                int errorcode = result.ordinal();

                if (8041 <= errorcode && errorcode <= 8050 || errorcode == 8308) {
                    tiptext.setVisibility(View.GONE);
                    yes.setClickable(false);
                    no.setClickable(false);
                    yes.setTextColor(getResources().getColor(R.color.gray_text));
                    no.setTextColor(getResources().getColor(R.color.gray_text));
                    zxingtext.setText(getResources().getString(R.string.sorry));
                } else if (errorcode != 0) {
                    tiptext.setVisibility(View.GONE);
                    yes.setClickable(false);
                    no.setClickable(false);
                    yes.setTextColor(getResources().getColor(R.color.gray_text));
                    no.setTextColor(getResources().getColor(R.color.gray_text));
                    zxingtext.setText(getResources().getString(R.string.verysorry));
                } else {
                    tiptext.setVisibility(View.VISIBLE);
                    yes.setClickable(true);
                    no.setClickable(true);
                    yes.setTextColor(getResources().getColor(R.color.black));
                    no.setTextColor(getResources().getColor(R.color.black));

                    whoshared = userName + splits[1] + productName + splits[splits.length - 1];
                    zxingtext.setText(whoshared);

                    String timeByFormat = DateUtil.getCurTimeByFormat("yyyy-MM-dd HH:mm:ss");
                    expiredAt = DateUtil.utc2Local(expiredAt);
                    long diff = DateUtil.getDiff(expiredAt, timeByFormat);

                    if (diff >= 0) {
                        time = (int) Math.ceil(diff / 60);
                    } else {

                    }
                    Toast.makeText(GosZxingDeviceSharingActivity.this, diff % 60 + "", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void initView() {

        zxingtext = (TextView) findViewById(R.id.zxingtext);

        yes = (TextView) findViewById(R.id.yes);

        no = (TextView) findViewById(R.id.no);

        whoshared = getResources().getString(R.string.whoshared);

        splits = whoshared.split("xxx");
        // [, 向你共享, ，你接受并绑定设备吗？]
        whoshared = userName + splits[1] + productName + splits[splits.length - 1];
        zxingtext.setText(whoshared);

        String timeByFormat = DateUtil.getCurTimeByFormat("yyyy-MM-dd HH:mm:ss");
        expiredAt = DateUtil.utc2Local(expiredAt);
        long diff = DateUtil.getDiff(expiredAt, timeByFormat);

        if (diff >= 0) {
            double c = diff / 60.0;
            time = (int) Math.ceil(c);
        } else {
            tiptext.setText(getResources().getString(R.string.requestoutoftime));
            yes.setClickable(false);
            yes.setTextColor(getResources().getColor(R.color.gray_text));
            return;
        }
        tiptext = (TextView) findViewById(R.id.tiptext);
        tip = getResources().getString(R.string.tipthings);
        split2s = tip.split("xx");

        tip = split2s[0] + time + split2s[1];

        tiptext.setText(tip);

        hand.sendEmptyMessageDelayed(1, diff % 60 * 1000);
    }

    public void yes(View v) {
        GizDeviceSharing.acceptDeviceSharingByQRCode(PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, ""), code);

    }

    public void no(View v) {
        finish();
    }

    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {

            time = time - 1;

            if (time > 0) {
                tip = split2s[0] + time + split2s[1];

                tiptext.setText(tip);
                hand.sendEmptyMessageDelayed(1, 60000);
            } else {
                tiptext.setText(getResources().getString(R.string.requestoutoftime));
                yes.setClickable(false);
                yes.setTextColor(getResources().getColor(R.color.gray_text));
            }

        }

        ;
    };
    private String[] split2s;
    private String   tip;
    private TextView tiptext;
    private String   token;
    private TextView yes;
    private TextView no;
    private TextView zxingtext;
    private String   whoshared;
    private String[] splits;
    private String   userName;
    private String   productName;
    private String   deviceAlias;
    private String   expiredAt;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
