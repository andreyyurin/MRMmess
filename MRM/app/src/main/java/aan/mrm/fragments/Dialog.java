package aan.mrm.fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.DTDHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import aan.mrm.DatabaseHelper;
import aan.mrm.MainActivity2;
import aan.mrm.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Andrew on 14.03.2016.
 */
public class Dialog extends Fragment {
    private String url_regAcc, url_regAcc_bar;
    private String full_url, full_url_bar;
    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;

    public static final String app_login = "Login";
    public static final String app_password = "Password";
    public static final String app_user = "User_click";
    private String name_prof_txt, surname_prof_txt;
    private String result;
    private int temp_length = 0;
    private DatabaseHelper mDatabaseHelper;
    private URL myURL;

    private ListView listView;

    private static final String KEY_TYPE = "AES";

    private EditText send_mess;
    private TextView hist_mess_tv, actionbar_name, hist_mess_tv2;
    private String send_mess_txt;
    private ImageButton send_mess_btn;
    private Stack<String> all_mess;
    private ArrayAdapter<String> adapter;
    private CircleImageView img_load;
    private boolean delMess = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View dialog = inflater.inflate(R.layout.dialog, container, false);
        final Context context = getActivity().getApplicationContext();



        logandpass = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);

        //Разрешение действия перехода к списку сообщений при нажатии на Back
        MainActivity2.needBack = true;

        listView = (ListView) dialog.findViewById(R.id.list_mess);
        send_mess = (EditText) dialog.findViewById(R.id.get_mess);
      //  hist_mess_tv = (TextView) dialog.findViewById(R.id.hist_mess);
        send_mess_btn = (ImageButton) dialog.findViewById(R.id.get_mess_btn);

        actionbar_name = (TextView) dialog.findViewById(R.id.name_surname_bar);
        url_regAcc_bar = getString(R.string.java_url)+"find_user_mess.php?";
        full_url_bar = url_regAcc_bar+"login="+logandpass.getString(app_user, "");
        new action_name_bar().execute();

        img_load = (CircleImageView) dialog.findViewById(R.id.img_load_dia);

        all_mess = new Stack<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, all_mess);
        listView.setAdapter(adapter);


        url_regAcc = getString(R.string.java_url)+"chat.php?";
        full_url = url_regAcc + "login1=" + logandpass.getString(app_login, "") + "&login2=" + logandpass.getString(app_user, "") + "&pass=" + logandpass.getString(app_password, "");
        new Show().execute();

        try {
            String urlImage = "http://94.251.109.165/upload/"+logandpass.getString(app_user, "")+".jpeg";
            myURL = new URL(urlImage);
            new MyAsyncTask(img_load).execute(myURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Обновление сообщений
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(getView()!=null) {
                    if(delMess) {
                        full_url = url_regAcc + "login1=" + logandpass.getString(app_login, "") + "&login2=" + logandpass.getString(app_user, "") + "&pass=" + logandpass.getString(app_password, "") + "&delmess=0";
                    }else{
                        full_url = url_regAcc + "login1=" + logandpass.getString(app_login, "") + "&login2=" + logandpass.getString(app_user, "") + "&pass=" + logandpass.getString(app_password, "") + "&delmess=1";
                        delMess = !delMess;
                    }
                    new Show().execute();
                }
            }
        }, 0, 3, TimeUnit.SECONDS);


        //Считывание текста
        send_mess_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                send_mess_txt = send_mess.getText().toString();
                send_mess.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //Если сообщение не пустое
                if(!send_mess_txt.isEmpty()) {
                    try {
                        full_url = url_regAcc + "login1=" + logandpass.getString(app_login, "") + "&login2=" + logandpass.getString(app_user, "") + "&text=" + URLEncoder.encode(send_mess_txt, "UTF-8") + "&pass=" + logandpass.getString(app_password, "");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Отправка сообщения на сервер
                    new Show().execute();
                }
            }
        });

        return dialog;
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
            if(Objects.equals(result, "-11")) {
                hist_mess_tv.setText("");
            }else {
                JSONObject dataJsonObj;
                String userName;
                String messUser;

                String log_mess = "";
                if (getView()!=null) {
                    try {
                        //Получаем все сообщения, выводим все сообщения
                        dataJsonObj = new JSONObject(result);
                        JSONArray messes = dataJsonObj.getJSONArray("messes");

                        if (messes.length() > 1 && messes.length()>temp_length) {
                            clearTable();
                            if(messes.length()<=9) {
                                for (int i = 0; i < messes.length(); i++) {
                                    JSONObject user = messes.getJSONObject(i);
                                    userName = user.getString("name");
                                    messUser = user.getString("message");
                                    log_mess = user.getString("login");



                                    if (!userName.isEmpty()) {
                                        UpdateBD(userName + ": \n" + messUser + "\n");
                                    }
                                }
                                ShowDB();
                                temp_length = messes.length();
                            }else{
                                delMess = !delMess;
                                for (int i = messes.length()-9; i < messes.length(); i++) {
                                    JSONObject user = messes.getJSONObject(i);
                                    userName = user.getString("name");
                                    messUser = user.getString("message");
                                    log_mess = user.getString("login");

                                    if (!userName.isEmpty()) {
                                        UpdateBD(userName + ": \n" + messUser + "\n");
                                    }
                                }
                                ShowDB();
                                temp_length = messes.length();
                            }

                        } else if(messes.length()<=1) {
                            clearTable();
                            JSONObject user = messes.getJSONObject(0);
                            userName = user.getString("name");
                            messUser = user.getString("message");
                            log_mess = user.getString("login");

                            if (!userName.isEmpty() && getView()!=null) {
                                UpdateBD(userName + ": \n" + messUser + "\n");
                            }
                            ShowDB();
                            temp_length = messes.length();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class action_name_bar extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(full_url_bar);
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
            //Вывод в тулбар имя и фамилию пользователя
            try {
                JSONObject jObject = new JSONObject(result);
                name_prof_txt = jObject.getString("name");
                surname_prof_txt = jObject.getString("surname");
                actionbar_name.setText(name_prof_txt+" "+surname_prof_txt);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Отмена действия перехода к списку сообщений при нажатии на Back
    @Override
    public void onDestroyView() {
        new Show().cancel(false);
        MainActivity2.needBack = false;
        clearTable();
        super.onDestroyView();
    }

    void UpdateBD(String mess)
    {
        mDatabaseHelper = new DatabaseHelper(getActivity(), "messesdb.db", null, 1);
        SQLiteDatabase sdb;
        sdb = mDatabaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.MESS_COLUMN, mess);
        sdb.insert("messes_"+app_user, null, values);
    }

    void ShowDB()
    {
        mDatabaseHelper = new DatabaseHelper(getActivity(), "messesdb.db", null, 1);
        SQLiteDatabase sdb;
        sdb = mDatabaseHelper.getReadableDatabase();
        int mem_id = 0;
        Cursor cursor = sdb.query(DatabaseHelper.DATABASE_TABLE+"_"+app_user, new String[]{DatabaseHelper.MESS_COLUMN, DatabaseHelper.COLUMN_ID}, null,
                null, null, null, null);
        while (cursor.moveToNext())
        {
                String mess = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MESS_COLUMN));
                all_mess.add(mess);
        }

        Parcelable state = listView.onSaveInstanceState();
        adapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(state);
        cursor.close();
        mDatabaseHelper.close();

    }

    void clearTable()
    {
        adapter.clear();
        mDatabaseHelper = new DatabaseHelper(getActivity(), "messesdb.db", null, 1);
        SQLiteDatabase sdb = mDatabaseHelper.getWritableDatabase();
        sdb.delete(DatabaseHelper.DATABASE_TABLE+"_"+app_user, null, null);
        mDatabaseHelper.close();
    }

    private class MyAsyncTask extends AsyncTask<URL, Void, Bitmap> {

        CircleImageView ivLogo;

        public MyAsyncTask(CircleImageView iv) {
            ivLogo = iv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        ///Загружаем
        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap networkBitmap = null;

            URL networkUrl = urls[0];
            try {
                networkBitmap = BitmapFactory.decodeStream(networkUrl
                        .openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return networkBitmap;
        }

        //Выводим изображения
        protected void onPostExecute(Bitmap result) {
            if(result!=null) {
                Bitmap bm = Bitmap.createScaledBitmap(result, ivLogo.getHeight(), ivLogo.getHeight(), false);
                ivLogo.setImageBitmap(bm);
            }else{
                ivLogo.setImageResource(R.drawable.unkava);
            }
        }
    }




}
