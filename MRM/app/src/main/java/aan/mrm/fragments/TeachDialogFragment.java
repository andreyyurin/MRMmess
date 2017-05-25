package aan.mrm.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

import aan.mrm.BotDataBase;
import aan.mrm.R;

public class TeachDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private View form=null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form= getActivity().getLayoutInflater()
                .inflate(R.layout.teach_dialog, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return(builder.setView(form)
                .setPositiveButton("Add", this)
                .setNegativeButton("Cancel", null).create());

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        EditText question = (EditText)form.findViewById(R.id.your_question);
        EditText answer = (EditText)form.findViewById(R.id.your_answer);

        StringBuilder sb =new StringBuilder();

        //избавление от пробелов
        for (int i = 0;i<question.length();i++){
            if (question.getText().charAt(i)!=' '){
                sb.append(question.getText().charAt(i));
            }
        }

        String quest = sb.toString();

        String textQuery = "SELECT "+ BotDataBase.COLUMN_QUESTION+", "+BotDataBase.COLUMN_ANSWER+" FROM "+ BotDataBase.TABLE_NAME+
                " WHERE ("+BotDataBase.COLUMN_QUESTION+" = '"+quest+"');";
        Cursor cursor = ChatBot.sqLiteDatabase.rawQuery(textQuery,null);
        String exist;

        int tmp = cursor.getCount();

        if (tmp>0){
            //Заменяет схожий вариант
            cursor.moveToNext();
            exist = cursor.getString(cursor.getColumnIndex( BotDataBase.COLUMN_ANSWER));
            String update = "UPDATE "+BotDataBase.TABLE_NAME+" SET "+BotDataBase.COLUMN_ANSWER+" = '"+answer.getText().toString()+"' WHERE "
                    +BotDataBase.COLUMN_ANSWER+" = '"+exist+"';";
            ChatBot.sqLiteDatabase.execSQL(update);
        }
        else {
            //Добавляет новый вариант
            String insert ="INSERT INTO "+ BotDataBase.TABLE_NAME+
                    " ("+BotDataBase.COLUMN_QUESTION+", "+BotDataBase.COLUMN_ANSWER+ ") VALUES ('"+sb.toString().toLowerCase()+"', " +
                    "'"+answer.getText()+"');";

            ChatBot.sqLiteDatabase.execSQL(insert);
        }

        cursor.close();

    }
}
