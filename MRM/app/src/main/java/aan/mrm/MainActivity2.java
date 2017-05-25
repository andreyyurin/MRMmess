package aan.mrm;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import aan.mrm.fragments.ChatBot;
import aan.mrm.fragments.FindUser;
import aan.mrm.fragments.MessagesUser;
import aan.mrm.fragments.ProfileUser;
import aan.mrm.fragments.SettingsUser;
import de.hdodenhof.circleimageview.CircleImageView;

import static aan.mrm.R.*;

public class MainActivity2 extends AppCompatActivity {

    private FindUser findUser_frg = new FindUser();
    private ProfileUser profileUser_frg = new ProfileUser();
    private MessagesUser messagesUser_frg = new MessagesUser();
    private SettingsUser settingsUser_frg = new SettingsUser();
    private ChatBot chatBot_frg = new ChatBot();

    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;

    public static final String app_login = "Login";
    public static final String app_password = "Password";
    public static final String app_user = "User_click";

    private TextView userLogin;
    private CircleImageView userPhoto;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static boolean needBack;
    private URL myURL;
    private FragmentManager myFragmentManager;
    private ImageButton btn_prof, btn_find, btn_sett, btn_bot, btn_mess;
    private int img_selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        myFragmentManager = getFragmentManager();
        needBack = false;

        btn_find = (ImageButton) findViewById(id.btn_find);
        btn_prof = (ImageButton) findViewById(id.btn_prof);
        btn_mess = (ImageButton) findViewById(id.btn_mess);
        btn_sett = (ImageButton) findViewById(id.btn_sett);
        btn_bot = (ImageButton) findViewById(id.btn_bot);

        logandpass = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        if (savedInstanceState == null) {
            img_selected = 2;
            btn_prof.setImageResource(drawable.prof_white);
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.replace(id.container, profileUser_frg);
            fragmentTransaction.commit();
        }



        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (img_selected)
                {
                    case 2:
                        btn_prof.setImageResource(drawable.prof_black);
                        break;
                    case 3:
                        btn_mess.setImageResource(drawable.mess_black);
                        break;
                    case 4:
                        btn_bot.setImageResource(drawable.bot_black);
                        break;
                    case 5:
                        btn_sett.setImageResource(drawable.setts_black);
                        break;
                }
                img_selected = 1;
                btn_find.setImageResource(drawable.find_white);
                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, findUser_frg);
                fragmentTransaction.commit();
            }
        });

        btn_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (img_selected)
                {
                    case 1:
                        btn_find.setImageResource(drawable.find_black);
                        break;
                    case 3:
                        btn_mess.setImageResource(drawable.mess_black);
                        break;
                    case 4:
                        btn_bot.setImageResource(drawable.bot_black);
                        break;
                    case 5:
                        btn_sett.setImageResource(drawable.setts_black);
                        break;
                }
                img_selected = 2;
                btn_prof.setImageResource(drawable.prof_white);
                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, profileUser_frg);
                fragmentTransaction.commit();
            }
        });
        btn_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (img_selected)
                {
                    case 2:
                        btn_prof.setImageResource(drawable.prof_black);
                        break;
                    case 1:
                        btn_find.setImageResource(drawable.find_black);
                        break;
                    case 4:
                        btn_bot.setImageResource(drawable.bot_black);
                        break;
                    case 5:
                        btn_sett.setImageResource(drawable.setts_black);
                        break;
                }
                img_selected = 3;
                btn_mess.setImageResource(drawable.mess_white);
                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, messagesUser_frg);
                fragmentTransaction.commit();
            }
        });
        btn_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (img_selected)
                {
                    case 2:
                        btn_prof.setImageResource(drawable.prof_black);
                        break;
                    case 3:
                        btn_mess.setImageResource(drawable.mess_black);
                        break;
                    case 1:
                        btn_find.setImageResource(drawable.find_black);
                        break;
                    case 5:
                        btn_sett.setImageResource(drawable.setts_black);
                        break;
                }
                img_selected = 4;
                btn_bot.setImageResource(drawable.bot_white);
                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, chatBot_frg);
                fragmentTransaction.commit();
            }
        });
        btn_sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (img_selected)
                {
                    case 2:
                        btn_prof.setImageResource(drawable.prof_black);
                        break;
                    case 3:
                        btn_mess.setImageResource(drawable.mess_black);
                        break;
                    case 4:
                        btn_bot.setImageResource(drawable.bot_black);
                        break;
                    case 1:
                        btn_find.setImageResource(drawable.find_black);
                        break;
                }
                img_selected = 5;
                btn_sett.setImageResource(drawable.setts_white);
                FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, settingsUser_frg);
                fragmentTransaction.commit();
            }
        });

    }
}
