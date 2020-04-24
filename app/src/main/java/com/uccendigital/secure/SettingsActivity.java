package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.SharedManager;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout notifications, hideapp, security_numbers, edit_security_code, whitelist, startInterface;
    Switch notifs;
    SharedManager AppShared;
    App app;

    String section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        security_numbers = findViewById(R.id.security_numbers);
        hideapp = findViewById(R.id.hideapp);

        AppShared = new SharedManager(getApplicationContext(), "app");
        app = new App(getApplicationContext());

        section = getIntent().getExtras().getString("SECTION");
        if (!section.equals("")) {
            checkSection(section);
        }

        initBTN();

    }

    private void initBTN() {

        // Notifications
        notifications = findViewById(R.id.notifications);
        notifs = findViewById(R.id.notifs);

        notifs.setChecked(AppShared.getBool("notifications"));
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifs.toggle();
                AppShared.putbool("notifications", notifs.isChecked());
            }
        });
        notifs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppShared.putbool("notifications", notifs.isChecked());
            }
        });

        // Hide application
        hideapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), Settings2Activity.class);
                in.putExtra("PAGE", "hide_app");
                in.putExtra("title", getResources().getString(R.string.hide_app));
                startActivity(in);
            }
        });

        // Numéro de securite
        security_numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Settings2Activity.class);
                in.putExtra("PAGE", "security_num");
                in.putExtra("title", getResources().getString(R.string.security_num));
                startActivity(in);
            }
        });

        // Modification de code d'acces
        edit_security_code = findViewById(R.id.edit_security_code);
        edit_security_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                intent.putExtra("CHANGE", true);
                startActivity(intent);
            }
        });

        // Liste blanche
        whitelist = findViewById(R.id.whitelist);
        whitelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings2Activity.class);
                i.putExtra("PAGE", "whitelist");
                i.putExtra("title", getResources().getString(R.string.whitelist));
                startActivity(i);
            }
        });

        // Interface de demarrage
        startInterface = findViewById(R.id.startInterface);
        startInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppShared.putStr("tuto", "notdone");
                Intent i = new Intent(getApplicationContext(), TutoActivity.class);
                startActivity(i);
            }
        });

        LinearLayout feedback = findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeEmail(new String[] {"uccen2019@gmail.com"}, getResources().getString(R.string.app_name) + " feedback");
            }
        });

        LinearLayout about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings2Activity.class);
                i.putExtra("PAGE", "about");
                i.putExtra("title", getResources().getString(R.string.about));
                startActivity(i);
            }
        });



    }

    private void checkSection(String section) {

        if (section.equals("security_num")) {
            security_numbers.setBackgroundColor(getResources().getColor(R.color.colorAccent,getTheme()));
        } else if (section.equals("hide_app")) {
            hideapp.setBackgroundColor(getResources().getColor(R.color.colorAccent,getTheme()));
        }

    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
