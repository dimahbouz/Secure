package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout edit_security_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        edit_security_code = findViewById(R.id.edit_security_code);

        edit_security_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                intent.putExtra("ACTION", 1);
                intent.putExtra("CHANGE", true);
                startActivity(intent);
            }
        });
    }
}
