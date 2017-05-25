package aan.mrm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.exit;

public class Main extends FragmentActivity {
    private Button reg_btn, log_btn;
    private String inpLogin_txt, inpPassword_txt;
    private EditText inpLogin, inpPassword;
    private String full_url, result;
    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;
    private String url_regAcc;
    public static final String app_login = "Login";
    public static final String app_password = "Password";
    private Locale myLocale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        logandpass = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        url_regAcc = getString(R.string.java_url)+"auth_acc.php?";
        final Intent intent2 = new Intent(this, Register.class);

        //Проверка на автоматический вход, входил ли пользователь до этого или нет
        if(logandpass.contains(app_login) && logandpass.contains(app_password)) {
            full_url = url_regAcc+"login="+logandpass.getString(app_login, "")+"&"+"pass="+logandpass.getString(app_password, "");
            //Проверка данных
            new Test().execute();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorisation);

        reg_btn = (Button) findViewById(R.id.reg_btn);
        log_btn = (Button) findViewById(R.id.log_btn);

        inpLogin = (EditText) findViewById(R.id.log_auth);
        inpPassword = (EditText) findViewById(R.id.pass_auth);

        //Переход к регистрации
        reg_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(intent2);
                overridePendingTransition(R.anim.go_from_down,R.anim.go_to_top);

            }
        });

        //Вход в приложение
        log_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Считываем логин, пароль
                inpLogin_txt = inpLogin.getText().toString();
                inpPassword_txt = inpPassword.getText().toString();

                inpLogin.setText("");
                inpPassword.setText("");
                //Проверка на заполнение полей
                if(!inpLogin_txt.isEmpty() && !inpPassword_txt.isEmpty()){
                    full_url = url_regAcc+"login="+inpLogin_txt+"&"+"pass="+inpPassword_txt;
                    //Проверка данных
                    new Connect().execute();
                }else{
                    //Если не заполнены поля
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                    builder.setMessage(R.string.java_auth_alert1)
                            .setCancelable(false)
                            .setPositiveButton(R.string.java_auth_alert2, null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        //Загрузка локализации
        loadLocale();

    }

    public class Connect extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {

                URL url = new URL(full_url);
                Scanner in = new Scanner((InputStream) url.getContent());
                result = "[" + in.nextLine() + "]";

            } catch (Exception e) {
                result = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            if(Objects.equals(result, "[5]")) //Подходят данные
            {

                SharedPreferences.Editor editor = logandpass.edit();
                editor.putString(app_login, inpLogin_txt);
                editor.putString(app_password, inpPassword_txt);
                editor.apply();

                final Intent main_open = new Intent(Main.this, MainActivity2.class);
                startActivity(main_open);
                overridePendingTransition(R.anim.show,R.anim.hide);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);
            }else if(Objects.equals(result, "[6]")) //Не подходят данные
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                builder.setMessage(R.string.java_auth_alert3)
                        .setCancelable(false)
                        .setPositiveButton(R.string.java_auth_alert4, null);
                AlertDialog alert = builder.create();
                alert.show();
            }else{ //Проблемы с подключением
                AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                builder.setMessage(R.string.java_auth_alert7)
                        .setCancelable(false)
                        .setPositiveButton(R.string.java_auth_alert4, null);
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
    }

    public class Test extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(full_url);
                Scanner in = new Scanner((InputStream) url.getContent());
                result = "[" + in.nextLine() + "]";
            } catch (Exception e) {
                result = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            if(Objects.equals(result, "[5]")) //Подходят данные
            {
                final Intent main_open = new Intent(Main.this, MainActivity2.class);
                startActivity(main_open);
                overridePendingTransition(R.anim.show,R.anim.hide);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);
            }
        }
    }

    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

