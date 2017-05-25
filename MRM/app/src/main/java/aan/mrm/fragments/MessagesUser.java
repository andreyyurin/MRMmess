package aan.mrm.fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import aan.mrm.DatabaseHelper;
import aan.mrm.MainActivity2;
import aan.mrm.R;

import static android.widget.Toast.*;
import static java.lang.System.exit;

public class MessagesUser extends Fragment {
    private Dialog dialog_frg = new Dialog();

    final String LOG_TAG = "myLogs";

    private EditText get_logfi;
    private Button get_logbtn;
    private String url_regAcc, url_show_messes;
    private String full_url, full_url_mess;
    private String get_log2_txt;
    private String result, result2;

    private TextView textView;
    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;

    public static final String app_login = "Login";
    public static final String app_password = "Password";
    public static final String app_user = "User_click";
    public static Vector<String> mass = new Vector<>();
    private int temp_length = -1;

    private Stack<String> all_mess;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private DatabaseHelper mDatabaseHelper;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View messagesUser = inflater.inflate(R.layout.messages_user, container, false);
        final Context context = getActivity().getApplicationContext();


        get_logfi = (EditText) messagesUser.findViewById(R.id.get_login_mess);
        get_logbtn = (Button) messagesUser.findViewById(R.id.button_open_dia);

        listView = (ListView) messagesUser.findViewById(R.id.list_messes);

        logandpass = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);

        textView = (TextView) messagesUser.findViewById(R.id.textView);

        url_regAcc = getString(R.string.java_url)+"test_user.php?";
        url_show_messes = getString(R.string.java_url)+"show_dia.php?";

        all_mess = new Stack<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, all_mess);
        listView.setAdapter(adapter);

        clearTable();
        //Вывод всех диалогов
        full_url_mess = url_show_messes + "login1=" + logandpass.getString(app_login, "") + "&pass=" + logandpass.getString(app_password, "");
        new Show_start().execute();
        if(getView()!=null) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (getView() != null) {
                        full_url_mess = url_show_messes + "login1=" + logandpass.getString(app_login, "") + "&pass=" + logandpass.getString(app_password, "");
                        new Show().execute();
                    }
                }
            }, 0, 3, TimeUnit.SECONDS);
        }

        //Узнаем на чей диалог нажали, проверяем пользователя, переход к диалогу
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                get_log2_txt = mass.get(position);
                full_url = url_regAcc + "login=" + get_log2_txt;
                new test_user().execute();

            }
        });

        //Считываем логин для диалога
        get_logbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                get_log2_txt = get_logfi.getText().toString();
                get_logfi.setText("");
                full_url = url_regAcc+"login="+get_log2_txt;
                //Проверяем логин
                new test_user().execute();
            }
        });



        return messagesUser;
    }

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

            if(Objects.equals(result, "1"))//Если нашел
            {
                SharedPreferences.Editor editor = logandpass.edit();
                editor.putString(app_user, get_log2_txt);
                editor.apply();
                //Переходим к диалогу
                FragmentManager myFragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, dialog_frg);
                fragmentTransaction.commit();
                new Show().cancel(true);
            }else if(Objects.equals(result, "-1")){
                //Не нашел логина
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.java_mess_alert1)
                        .setCancelable(false)
                        .setPositiveButton(R.string.java_reg_alert2, null);
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                //Проблемы с подключением
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.java_mess_alert3)
                        .setCancelable(false)
                        .setPositiveButton(R.string.java_reg_alert2, null);
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
    }

    public class Show extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(full_url_mess);
                Scanner in = new Scanner((InputStream) url.getContent());
                result2 = in.nextLine();
            } catch (Exception e) {
                result2 = e.toString();
            }
            return result2;
        }
        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            //Загрузка всех диалогов
            if (getActivity() != null) {
                //Считывание данных, вывод диалогов
                JSONObject dataJsonObj;
                String userName;
                String login;
                try {
                    dataJsonObj = new JSONObject(result2);
                    JSONArray messes = dataJsonObj.getJSONArray("messes");



                    if (messes.length() > 1 && messes.length()>temp_length) {
                        clearTable();
                        //temp_length = messes.length();
                        //Вывод
                        if(messes.length()<=9) {
                            for (int i = messes.length() - 1; i >= 0; i--) {
                                JSONObject user = messes.getJSONObject(i);
                                userName = user.getString("name") + " " + user.getString("surname");
                                login = user.getString("userlog");

                                String all_txt = userName + " (" + login + ")";

                                mass.add(login);
                                UpdateBD(all_txt);
                            }
                            ShowDB();
                            temp_length = messes.length();
                        }else{
                            for (int i = 9; i >= 0; i--) {
                                JSONObject user = messes.getJSONObject(i);
                                userName = user.getString("name") + " " + user.getString("surname");
                                login = user.getString("userlog");

                                String all_txt = userName + " (" + login + ")";

                                mass.add(login);
                                UpdateBD(all_txt);
                            }
                            ShowDB();
                            temp_length = messes.length();
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }


    public class Show_start extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(full_url_mess);
                Scanner in = new Scanner((InputStream) url.getContent());
                result2 = in.nextLine();
            } catch (Exception e) {
                result2 = e.toString();
            }
            return result2;
        }
        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            //Получаем данные
            if (getActivity() != null) {
                JSONObject dataJsonObj;
                String userName;
                String login;
                //Выводим данные
                try {
                    dataJsonObj = new JSONObject(result2);
                    JSONArray messes = dataJsonObj.getJSONArray("messes");

                    temp_length = messes.length();
                    for (int i = messes.length()-1; i>=0; i--) {
                        JSONObject user = messes.getJSONObject(i);
                        userName = user.getString("name") + " " + user.getString("surname");
                        login = user.getString("userlog");

                        String all_txt = userName + " (" + login + ")";


                        mass.add(login);
                        UpdateBD(all_txt);
                    }
                    ShowDB();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        mass.clear();
        clearTable();
        all_mess.clear();
        super.onDestroyView();


    }

    void UpdateBD(String mess)
    {
        mDatabaseHelper = new DatabaseHelper(getActivity(), "messesdb.db", null, 1);
        SQLiteDatabase sdb;
        sdb = mDatabaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.MESS_COLUMN_LOGS, mess);
        sdb.insert("messes_logs", null, values);
    }

    void ShowDB()
    {
        mDatabaseHelper = new DatabaseHelper(getActivity(), "messesdb.db", null, 1);
        SQLiteDatabase sdb;
        sdb = mDatabaseHelper.getReadableDatabase();
        int mem_id = 0;
        Cursor cursor = sdb.query(DatabaseHelper.DATABASE_TABLE_LOGS, new String[]{DatabaseHelper.MESS_COLUMN_LOGS, DatabaseHelper.COLUMN_ID}, null,
                null, null, null, null);
        while (cursor.moveToNext())
        {
            String mess = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MESS_COLUMN_LOGS));
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
        sdb.delete(DatabaseHelper.DATABASE_TABLE_LOGS, null, null);
        mDatabaseHelper.close();
    }
}
