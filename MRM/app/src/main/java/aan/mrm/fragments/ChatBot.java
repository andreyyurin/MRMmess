package aan.mrm.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

import aan.mrm.BotDataBase;
import aan.mrm.MainActivity2;
import aan.mrm.R;

public class ChatBot extends Fragment  {

    private ListView botDialogList;
    private EditText messageToBot;
    private ImageButton sendMessageToBot;
    private ArrayList<String> listOfMessages;
    private ArrayAdapter<String> listOfMessagesAdapter;
    private BotDataBase botDataBase;
    public static SQLiteDatabase sqLiteDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chatBot = inflater.inflate(R.layout.chat_bot, container, false);

        //Инициализация переменных
        botDialogList = (ListView)chatBot.findViewById(R.id.bot_dialog);
        messageToBot = (EditText) chatBot.findViewById(R.id.message_to_bot);
        sendMessageToBot = (ImageButton)chatBot.findViewById(R.id.send_to_bot);

        //Подключение базы данных бота
        botDataBase = new BotDataBase(getActivity().getApplicationContext());
        sqLiteDatabase = botDataBase.getWritableDatabase();

        //Создание списка сообщений
        listOfMessages = new ArrayList<>();
        listOfMessagesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listOfMessages);
        botDialogList.setAdapter(listOfMessagesAdapter);


        //Назначается слушатель для кнопки отправки сообщений
        sendMessageToBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = messageToBot.getText().toString();
                messageToBot.setText(null);

                //Закрытие клавиатуры после отправки сообщения
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //Отправка сообщения
                if (!question.isEmpty()){
                    //Добавление сообщения пользователя
                    listOfMessagesAdapter.add(getActivity().getResources().getString(R.string.you)+"\n"+question);

                    //Избавление от пробелов
                    question = question.trim();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0;i<question.length();i++){
                        if (question.charAt(i)!=' '){
                            sb.append(question.charAt(i));
                        }
                    }
                    question = sb.toString().toLowerCase();

                    //Добавление сообщения от бота
                    listOfMessagesAdapter.add(getActivity().getResources().getString(R.string.bot)+"\n"+ answerAtQuestion(question));
                    //Оповещение адаптера об изменении массива
                    listOfMessagesAdapter.notifyDataSetChanged();
                }

            }
        });

        return chatBot;
    }


    //Бот отвечает на вопрос
    private String answerAtQuestion(String question){
        String answer = getActivity().getResources().getString(R.string.no_answer);

        //Вызов окна калькулятора
        if (question.equals("калькулятор") || question.equals("calculator")){
            new CalculatorDialogFragment().show(getFragmentManager(),"calculator_dialog");
            answer= "...";
        }

        //Вызов окна Обучения
        if (question.equals("обучить") || question.equals("teach")){
            new TeachDialogFragment().show(getFragmentManager(),"teach_dialog");
            answer = "...";

        }
        else{
            //Поиск ответа в БД
            String textQuery = "SELECT "+ BotDataBase.COLUMN_QUESTION+", "+BotDataBase.COLUMN_ANSWER+" FROM "+ BotDataBase.TABLE_NAME+
                    " WHERE ("+BotDataBase.COLUMN_QUESTION+" = '"+question+"');";
            Cursor cursor = sqLiteDatabase.rawQuery(textQuery,null);
            cursor.moveToNext();

            if (cursor.getCount()>0){
                answer = cursor.getString(cursor.getColumnIndex(BotDataBase.COLUMN_ANSWER));
            }
            cursor.close();

        }

        //Возвращает ответ
        return answer;
    }

}