package com.uccendigital.secure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.uccendigital.secure.adapters.MyRVAdapter;
import com.uccendigital.secure.app.App;
import com.uccendigital.secure.app.Functions;
import com.uccendigital.secure.app.Hadher;
import com.uccendigital.secure.app.SharedManager;
import com.uccendigital.secure.elements.Sim;

import java.util.ArrayList;
import java.util.List;

public class Settings2Activity extends AppCompatActivity {

    Functions functions;
    SharedManager AppShared, hadShared;
    Hadher hadher;
    App app;

    String PAGE, title;
    LinearLayout addWhitelistBox, hideAppBox, numberBox, numberEditor, linearHelp;
    Switch hideAppSwitch;
    TextView titlesettings2, whitelistAddBtn, whitelistCancel, whitelistAdd, securityNumHelp, numberInt, editNumber, cancelNumberEditText, validNumberEditText;
    EditText whitelistAddName, whitelistAddSerial, numberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        functions = new Functions(getApplicationContext());
        hadShared = new SharedManager(getApplicationContext(), "hadher");
        app = new App(getApplicationContext());
        hadher = new Hadher(getApplicationContext());

        PAGE = getIntent().getExtras().getString("PAGE");
        title = getIntent().getExtras().getString("title");

        titlesettings2 = findViewById(R.id.title_settings2);
        titlesettings2.setText(title);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewTo(PAGE);
    }

    private void setViewTo (String view) {

        AppShared = new SharedManager(getApplicationContext(), "app");
        numberBox = findViewById(R.id.numberBox);

        if (view.equals("whitelist")) {
            setupWhiteList();
            addWhitelistBox = findViewById(R.id.addwhitelistbox);
            addWhitelistBox.setVisibility(View.VISIBLE);

        } else if (view.equals("security_num")) {
            // Set security number informations

            String num = hadher.extractIffer("achegue3");
            setNumber("security_num", num);

            addNumber("security_num", num);

            securityNumHelp = findViewById(R.id.securityNumHelp);
            securityNumHelp.setVisibility(View.VISIBLE);
            numberBox.setVisibility(View.VISIBLE);

        } else if (view.equals("hide_app")) {
            // Set Luancher number informations

            String num = hadher.extractIffer("assiwel");
            setNumber("luancher_num", num);

            setupHideApp();

            addNumber("luancher_num", num);

            hideAppBox = findViewById(R.id.hideAppBox);
            hideAppBox.setVisibility(View.VISIBLE);

        } else if (view.equals("about")) {

            linearHelp = findViewById(R.id.linearHelp);
            linearHelp.setVisibility(View.VISIBLE);
        }

    }

    private void setNumber (String titleR, String num) {

        String title = "";
        if (titleR.equals("security_num")) {
            title = getResources().getString(R.string.security_num);
        } else if (titleR.equals("luancher_num")) {
            title = getResources().getString(R.string.luancher_num);
        }

        TextView numberType = findViewById(R.id.numberType);
        numberType.setText(title);

        numberInt = findViewById(R.id.numberInt);
        editNumber = findViewById(R.id.editNumber);
        if (num.equals("")) {
            numberInt.setText(getResources().getString(R.string.nothing_num));
            editNumber.setText(getResources().getString(R.string.add));
        } else {
            numberInt.setText(num);
            editNumber.setText(getResources().getString(R.string.edit));
        }

    }

    private void setupWhiteList() {

        final List<Sim> simList = functions.getWhiteList();
        showList(simList);

        final LinearLayout whitelistAddBox = findViewById(R.id.whitelistAddBox);
        whitelistAddBtn = findViewById(R.id.whitelistAddBtn);
        whitelistAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whitelistAddBox.setVisibility(View.VISIBLE);
                whitelistAddBtn.setVisibility(View.GONE);
            }
        });

        whitelistAddName = findViewById(R.id.whitelistAddName);
        whitelistAddSerial = findViewById(R.id.whitelistAddSerial);

        whitelistCancel = findViewById(R.id.whitelistCancel);
        whitelistCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                whitelistAddBox.setVisibility(View.GONE);
                whitelistAddBtn.setVisibility(View.VISIBLE);
                whitelistAddName.setText("");
                whitelistAddSerial.setText("");
            }
        });

        whitelistAdd = findViewById(R.id.whitelistAdd);
        whitelistAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = whitelistAddName.getText().toString();
                String serial = whitelistAddSerial.getText().toString();
                if (!name.equals("") && !serial.equals("")) {

                    functions.addWhiteList(new Sim(name, serial));
                    simList.add(new Sim(name, serial));
                    showList(simList);

                    hideKeyboard();
                    whitelistAddBox.setVisibility(View.GONE);
                    whitelistAddBtn.setVisibility(View.VISIBLE);
                    whitelistAddName.setText("");
                    whitelistAddSerial.setText("");
                } else Toast.makeText(getApplicationContext(), "Veuillez saisir les données correctement!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showList (List<Sim> simList) {

            TextView whitelistEmpty = findViewById(R.id.whitelistEmpty);
            RecyclerView listView = findViewById(R.id.listviewnum);

            if (simList.size() > 0) {

                ArrayList<Object> mList = new ArrayList<Object>();

                for (int i = 0; i < simList.size(); i++) {
                    mList.add(simList.get(i));
                }

                MyRVAdapter adapter = new MyRVAdapter(this, R.layout.list_item, mList, new String[] {"delete"});
                listView.setHasFixedSize(true);
                listView.setLayoutManager(new LinearLayoutManager(this));
                listView.setAdapter(adapter);

                whitelistEmpty.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            } else {
                whitelistEmpty.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }

    }

    private void setupHideApp() {

        hideAppSwitch = findViewById(R.id.hideAppSwitch);
        final String num = hadher.extractIffer("assiwel");

        hideAppSwitch.setChecked(AppShared.getBool("hide_app"));
        if (hideAppSwitch.isChecked()) {
            numberBox.setVisibility(View.VISIBLE);
        } else {
            numberBox.setVisibility(View.GONE);
        }

        hideAppSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    if (app.checkPerm(new String[] {Manifest.permission.READ_PHONE_STATE})) {

                        numberBox.setVisibility(View.VISIBLE);

                        if (!num.equals("")) {
                            AppShared.putbool("hide_app", isChecked);
                            app.hideApp(true);
                        }
                    } else {
                        hideAppSwitch.setChecked(!isChecked);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.permissions_notdone), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AppShared.putbool("hide_app", isChecked);
                    numberBox.setVisibility(View.GONE);
                    app.hideApp(false);
                }

            }
        });

    }

    private void addNumber(final String addTo, final String num) {

        numberEditor = findViewById(R.id.numberEditor);
        numberEditText = findViewById(R.id.numberEditText);
        cancelNumberEditText = findViewById(R.id.cancelNumberEditText);
        validNumberEditText = findViewById(R.id.validNumberEditText);

        editNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num.equals("")) {
                    numberEditText.setText("");
                    if (addTo.equals("security_num")) {
                        numberEditText.setHint(getResources().getString(R.string.security_num));
                    } else if (addTo.equals("luancher_num")) {
                        numberEditText.setHint(getResources().getString(R.string.luancher_num));
                    }
                } else {
                    numberEditText.setText(num);
                }

                numberEditor.setVisibility(View.VISIBLE);
                editNumber.setVisibility(View.GONE);
            }
        });

        cancelNumberEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num.equals("")) {
                    numberEditText.setText("");
                } else {
                    numberEditText.setText(num);
                }

                hideKeyboard();
                numberEditor.setVisibility(View.GONE);
                editNumber.setVisibility(View.VISIBLE);
            }
        });

        validNumberEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newNum = numberEditText.getText().toString();
                if (addTo.equals("luancher_num")) {
                    hadher.putIffer("assiwel",newNum);
                } else if (addTo.equals("security_num")) {
                    hadher.putIffer("achegue3",newNum);
                    if (newNum.equals("")) AppShared.putbool("enable", false);
                }

                numberInt.setText(newNum);
                numberEditText.setText(newNum);
                editNumber.setText(getResources().getString(R.string.edit));

                if (addTo.equals("luancher_num")) {
                    if (!newNum.equals("")) {
                        app.hideApp(true);
                    } else app.hideApp(false);
                }
                hideKeyboard();
                numberEditor.setVisibility(View.GONE);
                editNumber.setVisibility(View.VISIBLE);

            }
        });

    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
