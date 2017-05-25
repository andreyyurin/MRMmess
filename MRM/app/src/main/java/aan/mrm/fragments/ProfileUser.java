package aan.mrm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.JSONException;
import java.io.FileNotFoundException;
import org.json.JSONObject;

import android.provider.MediaStore.Images.Media;

import aan.mrm.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUser extends Fragment {

    private String url_regAcc;
    private String full_url;
    private CircleImageView img_load;
    public static final String APP_PREFERENCES = "logandpass";
    public SharedPreferences logandpass;

    private CameraPhoto cameraPhoto;
    private GalleryPhoto galleryPhoto;
    final int GALLERY_REQUEST = 22131;
    final int CAMERA_REQUEST = 13323;
    private String selectedPhoto;


    public static final String app_login = "Login";
    public static final String app_password = "Password";
    public static String result;
    private final String TAG = this.getClass().getName();

    private String info_login, info_name_surname, info_age, info_city, info_country;

    private TextView login_tv, name_tv, city_country_tv, age_tv;

    public static String login_prof_txt, name_prof_txt, surname_prof_txt, age_prof_txt, city_prof_txt, country_prof_txt;
    private URL myURL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileUser = inflater.inflate(R.layout.profile_user, container, false);
        Context context = getActivity().getApplicationContext();

        url_regAcc = getString(R.string.java_url)+"show_user.php?";

        galleryPhoto = new GalleryPhoto(getActivity());
        cameraPhoto = new CameraPhoto(getActivity());

        login_tv = (TextView) profileUser.findViewById(R.id.prof_login);
        name_tv = (TextView) profileUser.findViewById(R.id.prof_name);
        city_country_tv = (TextView) profileUser.findViewById(R.id.prof_country_city);
        age_tv = (TextView) profileUser.findViewById(R.id.prof_age);
        img_load = (CircleImageView) profileUser.findViewById(R.id.img_load);


        logandpass = context.getSharedPreferences(APP_PREFERENCES, context.MODE_PRIVATE);
        full_url = url_regAcc+"login="+logandpass.getString(app_login, "")+"&"+"pass="+logandpass.getString(app_password, "");
        //Вывод профиля
        new Show().execute();

        //Переходим к загрузке фотографии
        img_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Выбор откуда загружать фото
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.java_prof_img)
                        .setCancelable(false)
                        .setNeutralButton(R.string.java_prof_img_no, null)
                        .setPositiveButton(R.string.java_prof_img_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Из галереи
                                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                            }
                        })
                        .setNegativeButton(R.string.java_prof_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    //С камеры
                                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                                    cameraPhoto.addToGallery();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        return profileUser;
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
                //Получаем данные, вывод профиль
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void show_user()
    {
        //Выводим
        name_tv.setText(info_name_surname);
        age_tv.setText(info_age);
        city_country_tv.setText(info_country+", "+info_city);
        login_tv.setText(info_login);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) {//Проверка откуда была выбрана загрузка
            if (requestCode == GALLERY_REQUEST) {//Из галереи
                    Uri uri = data.getData();
                    galleryPhoto.setPhotoUri(uri);
                    String photoPath = galleryPhoto.getPath();
                    selectedPhoto = photoPath; //Получаем путь

                    //Загружаем на сервер
                    try {
                        Bitmap bitmap = ImageLoader.init().from(selectedPhoto).getBitmap();
                        String encodedImage = ImageBase64.encode(bitmap);

                        HashMap<String, String> postData = new HashMap<String, String>();

                        postData.put("image", encodedImage);

                        PostResponseAsyncTask task = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if (s.contains("success")) {
                                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        task.execute("http://94.251.109.165/upload_img.php?login=" + info_login + "&pass=" + logandpass.getString(app_password, ""));
                        task.setEachExceptionsHandler(new EachExceptionsHandler() {
                            @Override
                            public void handleIOException(IOException e) {
                                Toast.makeText(getActivity(), "Error with connect", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleMalformedURLException(MalformedURLException e) {
                                Toast.makeText(getActivity(), "Error with url", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleProtocolException(ProtocolException e) {
                                Toast.makeText(getActivity(), "Error with protocol", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                                Toast.makeText(getActivity(), "Error with encode", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        String urlImage = "http://94.251.109.165/upload/" + info_login + ".jpeg";
                        myURL = new URL(urlImage);
                        new MyAsyncTask(img_load).execute(myURL);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
            }else if(requestCode==CAMERA_REQUEST)
            {
                //С камеры
                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;//Получаем путь
                //Загружаем на сервер
                try {
                    Bitmap bitmap = ImageLoader.init().from(selectedPhoto).getBitmap();
                    String encodedImage = ImageBase64.encode(bitmap);

                    HashMap<String, String> postData = new HashMap<String, String>();

                    postData.put("image", encodedImage);

                    PostResponseAsyncTask task = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.contains("success")) {
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    task.execute("http://94.251.109.165/upload_img.php?login=" + info_login + "&pass=" + logandpass.getString(app_password, ""));
                    task.setEachExceptionsHandler(new EachExceptionsHandler() {
                        @Override
                        public void handleIOException(IOException e) {
                            Toast.makeText(getActivity(), "Error with connect", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleMalformedURLException(MalformedURLException e) {
                            Toast.makeText(getActivity(), "Error with url", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleProtocolException(ProtocolException e) {
                            Toast.makeText(getActivity(), "Error with protocol", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                            Toast.makeText(getActivity(), "Error with encode", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    String urlImage = "http://94.251.109.165/upload/" + info_login + ".jpeg";
                    myURL = new URL(urlImage);
                    new MyAsyncTask(img_load).execute(myURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    //Вывод изображения в профиль
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
