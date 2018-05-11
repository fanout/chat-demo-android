package com.app.fanout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.username_input_layout);
        final TextInputEditText editText = (TextInputEditText) findViewById(R.id.username_edt);

        findViewById(R.id.start_chat_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Please enter your username");
                } else {
                    textInputLayout.setErrorEnabled(false);
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("Username", editText.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
