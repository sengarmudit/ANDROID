package com.example.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.matrimony.payment.AvenuesParams;
import com.matrimony.payment.ServiceUtility;

public class InitialScreenActivity extends AppCompatActivity {
    EditText accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;

    private void init() {
        accessCode = (EditText) findViewById(R.id.accessCode);
        merchantId = (EditText) findViewById(R.id.merchantId);
        orderId = (EditText) findViewById(R.id.orderId);
        currency = (EditText) findViewById(R.id.currency);
        amount = (EditText) findViewById(R.id.amount);
        rsaKeyUrl = (EditText) findViewById(R.id.rsaUrl);
        redirectUrl = (EditText) findViewById(R.id.redirectUrl);
        cancelUrl = (EditText) findViewById(R.id.cancelUrl);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);
        init();

        //generating order number
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId.setText(randomNum.toString());
    }

    public void onClick(View view) {
        //Mandatory parameters. Other parameters can be added if required.
        String vAccessCode = ServiceUtility.chkNull(accessCode.getText()).toString().trim();
        String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
        String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
        String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
        if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
            intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount.getText()).toString().trim());

            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());

            startActivity(intent);
        } else {
            showToast("All parameters are mandatory.");
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
}


