package aan.mrm;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BotDataBase extends SQLiteOpenHelper {

    final public static String DATABASE_NAME = "Bot";
    final public static int DATABASE_VERSION = 1;

    final public static String TABLE_NAME = "tblQuestions";
    final public static String COLUMN_QUESTION_ID = "_id";
    final public static String COLUMN_QUESTION = "Questions";
    final public static String COLUMN_ANSWER = "Answer";


    final private static String CREATE_DATABASE = "CREATE TABLE "+TABLE_NAME+
            " ("+COLUMN_QUESTION_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUMN_QUESTION+" TEXT, "+COLUMN_ANSWER+" TEXT);";


    public BotDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteDatabase database;

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Добавление стандартного набора функций
        database = db;
        db.execSQL(CREATE_DATABASE);
        insert("hi", "hi");
        insert("hello", "hi");
        insert("howareyou","I am fine");
        insert("whatareyoudoing", "Iam trying to become more helpful");
        insert("whereareyou", "Iam in Russia");
        insert("whoisyourcreator", "I was created by Sovest2 and fastvepik");
        insert("whatyouwant","I am want to be more helpful");

        insert("привет", "Привет");
        insert("ку","Привет");
        insert("какдела","Все хорошо");
        insert("чемзанимаешься","Стараюсь стать более полезным");
        insert("гдеты","Я в России");
        insert("ктотебясделал","Я сделан Sovest2 и fastvepik");
        insert("чтотыхочешь","Я хочу быть более полезным");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Реализация вставки данных
    private static void insert(String question,String answer){
        String INSERT_QUESTION = "INSERT INTO "+TABLE_NAME+
                " ("+COLUMN_QUESTION+", "+COLUMN_ANSWER+ ") VALUES ('"+question+"', '"+answer+"');";
        database.execSQL(INSERT_QUESTION);
    }

}

