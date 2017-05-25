package aan.mrm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ScrollView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.Scanner;

import java.util.Timer;
import java.util.TimerTask;

public class Register extends AppCompatActivity {
    private EditText inpLogin, inpName, inpSurname, inpAge, inpCity, inpCountry, inpPass, inpRepPass;
    private String inpLogin_txt, inpName_txt, inpSurname_txt, inpAge_txt, inpCity_txt, inpCountry_txt, inpPass_txt, inpRepPass_txt;
    private String url_regAcc; //Ссылка на сайт
    private String u = "&", full_url;
    private Button regAccount_btn,cancel_btn;
    private String result;
    private ScrollView scroll;
    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;

    public static final String app_login = "Login";
    public static final String app_password = "Password";

    private boolean test_basebool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        url_regAcc = getString(R.string.java_url)+"reg_acc.php?";
        logandpass = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_act);

        inpLogin = (EditText) findViewById(R.id.inputLogin);
        inpName = (EditText) findViewById(R.id.inputName);
        inpSurname = (EditText) findViewById(R.id.inputSurname);
        inpAge = (EditText) findViewById(R.id.inputAge);
        inpCity = (EditText) findViewById(R.id.inputCity);
        inpCountry = (EditText) findViewById(R.id.inputCountry);
        inpPass = (EditText) findViewById(R.id.inputPassword);
        inpRepPass = (EditText) findViewById(R.id.inputRepPassword);
        scroll =(ScrollView)findViewById(R.id.scroll_reg);

        regAccount_btn = (Button) findViewById(R.id.btnRegAcc);
        cancel_btn = (Button) findViewById(R.id.btnRegCancel);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.setSmoothScrollingEnabled(true);
                scroll.smoothScrollTo(0,0);

                startActivity(new Intent(Register.this,Main.class));
                overridePendingTransition(R.anim.go_from_top,R.anim.go_to_down);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);

            }
        });


        regAccount_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Считывание данных
                inpLogin_txt = inpLogin.getText().toString();
                inpName_txt = inpName.getText().toString();
                inpSurname_txt = inpSurname.getText().toString();
                inpAge_txt = inpAge.getText().toString();
                inpCity_txt = inpCity.getText().toString();
                inpCountry_txt = inpCountry.getText().toString();
                inpPass_txt = inpPass.getText().toString();
                inpRepPass_txt = inpRepPass.getText().toString();

                //Проверка на пустоту, проверка паролей, проверка символов, проверка длины
                if(!inpLogin_txt.isEmpty() && !inpName_txt.isEmpty() && !inpSurname_txt.isEmpty() && !inpAge_txt.isEmpty() && !inpCity_txt.isEmpty() && !inpCountry_txt.isEmpty() && !inpPass_txt.isEmpty() && !inpRepPass_txt.isEmpty()) {
                    if(Objects.equals(inpPass_txt, inpRepPass_txt)) {
                        try {
                            if(inpLogin_txt.length()>=6) {
                                test_base(inpLogin_txt);
                                if (!test_basebool) {
                                    test_base(inpName_txt);
                                    if (!test_basebool) {
                                        test_base(inpSurname_txt);
                                        if (!test_basebool) {
                                            test_base(inpCity_txt);
                                            if (!test_basebool) {
                                                test_base(inpCountry_txt);
                                                if (!test_basebool) {
                                                    full_url = url_regAcc + "login=" + URLEncoder.encode(inpLogin_txt, "UTF-8") + u + "pass=" + URLEncoder.encode(inpPass_txt, "UTF-8") + u + "reppass=" + URLEncoder.encode(inpRepPass_txt, "UTF-8") + u + "name=" + URLEncoder.encode(inpName_txt, "UTF-8") + u + "surname=" + URLEncoder.encode(inpSurname_txt, "UTF-8") + u + "age=" + URLEncoder.encode(inpAge_txt, "UTF-8") + u + "city=" + URLEncoder.encode(inpCity_txt, "UTF-8") + u + "country=" + URLEncoder.encode(inpCountry_txt, "UTF-8");
                                                    regAccount_btn.setEnabled(false);
                                                    //Передача данных на сервер
                                                    new MyTask().execute();
                                                } else {
                                                    //Введены запрещенные символы
                                                    Toast.makeText(Register.this, getString(R.string.reg_char_country), Toast.LENGTH_LONG).show();
                                                    test_basebool = false;
                                                }
                                            } else {
                                                //Введены запрещенные символы
                                                Toast.makeText(Register.this, getString(R.string.reg_char_city), Toast.LENGTH_LONG).show();
                                                test_basebool = false;
                                            }
                                        } else {
                                            //Введены запрещенные символы
                                            Toast.makeText(Register.this, getString(R.string.reg_char_surname), Toast.LENGTH_LONG).show();
                                            test_basebool = false;
                                        }
                                    } else {
                                        //Введены запрещенные символы
                                        Toast.makeText(Register.this, getString(R.string.reg_char_name), Toast.LENGTH_LONG).show();
                                        test_basebool = false;
                                    }
                                } else {
                                    //Введены запрещенные символы
                                    Toast.makeText(Register.this, getString(R.string.reg_char_login), Toast.LENGTH_LONG).show();
                                    test_basebool = false;
                                }
                            }else{
                                Toast.makeText(Register.this, getString(R.string.reg_char_length), Toast.LENGTH_LONG).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //Пароли не совпадают
                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                        builder.setMessage(R.string.java_reg_alert3)
                                .setCancelable(false)
                                .setPositiveButton(R.string.java_reg_alert4, null);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    //Не все поля заполнены
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage(R.string.java_reg_alert1)
                            .setCancelable(false)
                            .setPositiveButton(R.string.java_reg_alert2, null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

    }

    public class MyTask extends AsyncTask<Void, Void, String> {

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
            //Если логин занят
            if(Objects.equals(result, "[4]"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setMessage(R.string.java_reg_alert5)
                        .setCancelable(false)
                        .setPositiveButton(R.string.java_reg_alert6, null);
                AlertDialog alert = builder.create();
                alert.show();
                regAccount_btn.setEnabled(true);
            }else if(Objects.equals(result, "[1]")){
                //Запоминаем логин пароль, если можно зарегестрироваться, переходим в основную активность
                final Intent open_main = new Intent(Register.this, MainActivity2.class);
                SharedPreferences.Editor editor = logandpass.edit();
                editor.putString(app_login, inpLogin_txt);
                editor.putString(app_password, inpPass_txt);
                editor.apply();
                startActivity(open_main);
                overridePendingTransition(R.anim.show,R.anim.hide);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);

            }else{
                //Проблемы с подключением
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setMessage(R.string.java_reg_alert7)
                        .setCancelable(false)
                        .setPositiveButton(R.string.java_reg_alert8, null);
                AlertDialog alert = builder.create();
                alert.show();
                regAccount_btn.setEnabled(true);
            }
        }
    }
    //Проверка на символы
    void test_base(String text)
    {
        for(int i = 0; i<text.length(); i++)
        {
            if((text.charAt(i)>=48 && text.charAt(i)<=57)||(text.charAt(i)>=65 && text.charAt(i)<=90) || (text.charAt(i)>=97 && text.charAt(i)<=122))
            {
            }else{
                test_basebool = true;
                break;
            }
        }
    }
}
