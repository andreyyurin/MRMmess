package aan.mrm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import aan.mrm.Main;
import aan.mrm.R;

public class SettingsUser extends Fragment {
    private EditText namefi, surnamefi, countryfi, cityfi, agefi, oldpassfi, newpassfi, repnewpassfi;
    private Button editbtn, exitbtn,editLang;
    private String namefi_txt, surnamefi_txt, countryfi_txt, cityfi_txt, agefi_txt, oldpassfi_txt, newpassfi_txt, repnewpassfi_txt ;
    private String url_regAcc;
    private Spinner langswitcher;
    private String full_url;
    private String result;
    private TextView post;
    private Locale myLocale;
    private View settingsUser;


    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;

    public static final String app_login = "Login";
    public static final String app_password = "Password";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsUser = inflater.inflate(R.layout.settings_user, container, false);
        final Context context = getActivity().getApplicationContext();

        url_regAcc = getString(R.string.java_url)+"edit_user.php?";
        logandpass = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);

        oldpassfi = (EditText) settingsUser.findViewById(R.id.setts_old_pass);
        newpassfi = (EditText) settingsUser.findViewById(R.id.setts_new_pass);
        repnewpassfi = (EditText) settingsUser.findViewById(R.id.setts_rep_new_pass);

        namefi = (EditText) settingsUser.findViewById(R.id.setts_name);
        surnamefi = (EditText) settingsUser.findViewById(R.id.setts_surname);
        countryfi = (EditText) settingsUser.findViewById(R.id.setts_country);
        cityfi = (EditText) settingsUser.findViewById(R.id.setts_city);
        agefi = (EditText) settingsUser.findViewById(R.id.setts_age);

        editbtn = (Button) settingsUser.findViewById(R.id.setts_btn);
        exitbtn = (Button) settingsUser.findViewById(R.id.setts_btn_exit);

        langswitcher = (Spinner)settingsUser.findViewById(R.id.langswitcher);
        editLang = (Button)settingsUser.findViewById(R.id.edit_language);

        doTheSpinner();

        //Изменяем язык приложения
        editLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (langswitcher.getSelectedItemPosition()){
                    case 0:
                        changeLang("en");
                        break;
                    case 1:
                        changeLang("ru");
                        break;
                }
                getActivity().recreate();
            }
        });

        //Считываем что изменять
        editbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                namefi_txt = namefi.getText().toString();
                surnamefi_txt = surnamefi.getText().toString();
                cityfi_txt = cityfi.getText().toString();
                countryfi_txt = countryfi.getText().toString();
                agefi_txt = agefi.getText().toString();
                oldpassfi_txt = oldpassfi.getText().toString();
                newpassfi_txt = newpassfi.getText().toString();
                repnewpassfi_txt = repnewpassfi.getText().toString();

                namefi.setText("");
                surnamefi.setText("");
                cityfi.setText("");
                countryfi.setText("");
                agefi.setText("");
                oldpassfi.setText("");
                newpassfi.setText("");
                repnewpassfi.setText("");
                //Проверка данных
                if(Objects.equals(oldpassfi_txt, logandpass.getString(app_password, "")) || (oldpassfi_txt.isEmpty() && newpassfi_txt.isEmpty() && repnewpassfi_txt.isEmpty())) {
                    if(Objects.equals(newpassfi_txt, newpassfi_txt) || (oldpassfi_txt.isEmpty() && newpassfi_txt.isEmpty() && repnewpassfi_txt.isEmpty()) ){
                        try {
                            full_url = url_regAcc + "name=" + URLEncoder.encode(namefi_txt, "UTF-8") + "&surname=" + URLEncoder.encode(surnamefi_txt, "UTF-8") + "&country=" + URLEncoder.encode(countryfi_txt, "UTF-8") + "&city=" + URLEncoder.encode(cityfi_txt, "UTF-8") + "&age=" + agefi_txt + "&pass="+ newpassfi_txt + "&login=" + logandpass.getString(app_login, "") + "&password=" + logandpass.getString(app_password, "");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //Передаем данные
                        new Edit().execute();
                    }else{
                        //Не заполнены поля
                        Toast.makeText(getActivity(), getString(R.string.setts_field_post2), Toast.LENGTH_SHORT).show();
                    }
                }else{//Не заполнены поля
                    Toast.makeText(getActivity(), getString(R.string.setts_field_post1), Toast.LENGTH_SHORT).show();
                }
            }

        });

        //Выход из приложения
        exitbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent main_open = new Intent(context, Main.class);

                SharedPreferences.Editor editor = logandpass.edit();
                editor.clear();
                editor.apply();
                startActivity(main_open);
                getActivity().overridePendingTransition(R.anim.hide,R.anim.show);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                }, 500);

            }
        });

        loadLocale();

        return settingsUser;
    }

    //Передача данных
    public class Edit extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {

                URL url = new URL(full_url);
                Scanner in = new Scanner((InputStream) url.getContent());
                result = in.nextLine();

            } catch (Exception e) {
                result = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            //Получаем данные, проверяем, сохраняем
            if(Objects.equals(result, "8")) //Подходит
            {
                if(!newpassfi_txt.isEmpty())
                {
                    SharedPreferences.Editor editor = logandpass.edit();
                    editor.putString(app_password, newpassfi_txt);
                    editor.apply();
                }
               // post.setText(getString(R.string.setts_field_post3));
                Toast.makeText(getActivity(), getString(R.string.setts_field_post3), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), getString(R.string.setts_field_post4), Toast.LENGTH_SHORT).show();
            }
        }

    }

    //смена языка
    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }


    //сохранение языка
    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.apply();
    }


    //необходимо для загрузки новых данных
    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    private void doTheSpinner(){
        //Создается список с выбором языка
        ArrayList<String> langs = new ArrayList<>(2);
        langs.add(0,"English");
        langs.add(1, "Русский");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(settingsUser.getContext(), android.R.layout.simple_spinner_item, langs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langswitcher.setAdapter(adapter);

    }

}
