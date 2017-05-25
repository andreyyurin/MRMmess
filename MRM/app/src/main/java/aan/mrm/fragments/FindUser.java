package aan.mrm.fragments;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

import aan.mrm.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class FindUser extends Fragment {

    private Button find_btn;
    private EditText find_txt;
    private String url_regAcc;
    private String full_url;
    private String login_txt_find;
    public static String result;
    public static String login_prof_txt, name_prof_txt, surname_prof_txt, age_prof_txt, city_prof_txt, country_prof_txt;
    private String info_login, info_name_surname, info_age, info_city, info_country;
    private LinearLayout foundPanel;
    private FloatingActionButton fab;
    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;

    public static final String app_login = "Login";
    public static final String app_password = "Password";
    public static final String app_user = "User_click";
    private Dialog dialog_frg = new Dialog();
    private ImageView img_load;
    private URL myURL;
    private ValueAnimator vAnimator;

    private TextView login_tv, name_tv, city_country_tv, age_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_user, container, false);
        final Context context = getActivity().getApplicationContext();

        login_tv = (TextView) v.findViewById(R.id.find_login);
        name_tv = (TextView) v.findViewById(R.id.find_name);
        city_country_tv = (TextView) v.findViewById(R.id.find_country_city);
        age_tv = (TextView) v.findViewById(R.id.find_age);
        foundPanel = (LinearLayout)v.findViewById(R.id.founded_user_layout);
        fab = (FloatingActionButton)v.findViewById(R.id.fab);
        img_load = (CircleImageView) v.findViewById(R.id.img_see);

        //Создаем анимацию появления панели после нажатия на кнопку
        foundPanel.setVisibility(View.GONE);
        foundPanel.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        foundPanel.getViewTreeObserver().removeOnPreDrawListener(this);
                        foundPanel.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                        foundPanel.measure(widthSpec, heightSpec);

                        vAnimator = slideAnimator(0,foundPanel.getMeasuredHeight());
                        return true;
                    }
                });

        logandpass = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
        fab.setVisibility(View.INVISIBLE);

        url_regAcc = getString(R.string.java_url)+"find_user.php?";
        find_btn = (Button) v.findViewById(R.id.find_user_btn);
        find_txt = (EditText) v.findViewById(R.id.find_user_edit);

        find_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Считываем логин при нажатии
                login_txt_find = find_txt.getText().toString();
                find_txt.setText("");
                full_url = url_regAcc+"login="+login_txt_find;
                new Show().execute();

            }
        });

        //Назначение слушателя для кнопки отправки сообщения
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_regAcc = getString(R.string.java_url)+"test_user.php?";
                full_url = url_regAcc+"login="+info_login;
                new test_user().execute();

            }
        });

        return v;
    }

    public class Show extends AsyncTask<Void, Void, String> {
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
            try {
                //Проверка логина в БД
                if(Objects.equals(result, "7")) //Не подходит
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.java_find_txt1)
                            .setCancelable(false)
                            .setPositiveButton(R.string.java_find_txt2, null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    //Получаем данные в JSON, выводим профиль
                    JSONObject jObject = new JSONObject(result);
                    login_prof_txt = jObject.getString("login");
                    name_prof_txt = jObject.getString("name");
                    surname_prof_txt = jObject.getString("surname");
                    age_prof_txt = jObject.getString("age");
                    city_prof_txt = jObject.getString("city");
                    country_prof_txt = jObject.getString("country");

                    info_login = login_prof_txt;
                    info_name_surname = name_prof_txt+" "+surname_prof_txt;
                    info_age = age_prof_txt;
                    info_city = city_prof_txt;
                    info_country = country_prof_txt;
                    show_user();

                    try {
                        String urlImage = "http://94.251.109.165/upload/"+info_login+".jpeg";
                        myURL = new URL(urlImage);
                        new MyAsyncTask(img_load).execute(myURL);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void show_user()
    {
        name_tv.setText(info_name_surname);
        age_tv.setText(info_age);
        city_country_tv.setText(info_country+", "+info_city);
        login_tv.setText(info_login);

        //Показываем панель
        if (foundPanel.getVisibility() == View.GONE) {
            expand();
            fab.setVisibility(View.VISIBLE);
        }

        fab.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.show));

    }

    //Переход к диалогу к профилю после нажатия на FAB
    public class test_user extends AsyncTask<Void, Void, String> {
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

            if(Objects.equals(result, "1"))
            {
                //Если нашел пользователя
                SharedPreferences.Editor editor = logandpass.edit();
                editor.putString(app_user, info_login);
                editor.apply();

                FragmentManager myFragmentManager = getFragmentManager();
                //Переход к диалогу
                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, dialog_frg);
                fragmentTransaction.commit();
                new Show().cancel(false);

            }else if(Objects.equals(result, "-1")){
                //Не нашло пользователя
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.java_mess_alert1)
                        .setCancelable(false)
                        .setPositiveButton(R.string.java_reg_alert2, null);
                AlertDialog alert = builder.create();
                alert.show();
            }else{//Проблема с соединением
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.java_mess_alert3)
                        .setCancelable(false)
                        .setPositiveButton(R.string.java_reg_alert2, null);
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
    }

    //Загрузка аватара
    private class MyAsyncTask extends AsyncTask<URL, Void, Bitmap> {

        ImageView ivLogo;

        public MyAsyncTask(ImageView iv) {
            ivLogo = iv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap networkBitmap = null;
            //Загрузка аватара
            URL networkUrl = urls[0];
            try {
                networkBitmap = BitmapFactory.decodeStream(networkUrl
                        .openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return networkBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            //Вывод фотографии профиля
            if(result!=null) {
                Bitmap bm = Bitmap.createScaledBitmap(result, ivLogo.getHeight(), ivLogo.getHeight(), false);
                ivLogo.setImageBitmap(bm);
            }else{
                ivLogo.setImageResource(R.drawable.unkava);
            }
        }
    }

    private void expand() {
        foundPanel.setVisibility(View.VISIBLE);
        vAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = foundPanel.getLayoutParams();
                layoutParams.height = value;
                foundPanel.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }

}
