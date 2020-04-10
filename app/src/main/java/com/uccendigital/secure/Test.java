package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Test extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }


    public void lanch(View view) {

        Intent intent = new Intent(Test.this, LockActivity.class);
        intent.putExtra("CHANGE", false);
        startActivity(intent);
        finish();

    }

}
