package aan.mrm.fragments;

import android.app.*;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import aan.mrm.R;

public class CalculatorDialogFragment extends DialogFragment {

    private View form=null;
    private StringBuilder example;
    private boolean needFirstStep,needSecondStep,needSecondStepBrackets, solved;
    private Button num1,num2,num3,num4,num5,num6,num7,num8,num9,num0,btn_plus,btn_minus,btn_multiply,btn_split,btn_equals,btn_open,btn_close,btn_back,btn_dot,btn_clear;
    private TextView primerTV;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form= getActivity().getLayoutInflater()
                .inflate(R.layout.calculator_dialog, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        example = new StringBuilder();
        primerTV = (TextView)form.findViewById(R.id.calculate_text);

        num0 = (Button) form.findViewById(R.id.calc_zero);
        num1 = (Button) form.findViewById(R.id.calc_one);
        num2 = (Button) form.findViewById(R.id.calc_two);
        num3 = (Button) form.findViewById(R.id.calc_three);
        num4 = (Button) form.findViewById(R.id.calc_four);
        num5 = (Button) form.findViewById(R.id.calc_five);
        num6 = (Button) form.findViewById(R.id.calc_six);
        num7 = (Button) form.findViewById(R.id.calc_seven);
        num8 = (Button) form.findViewById(R.id.calc_eight);
        num9 = (Button) form.findViewById(R.id.calc_nine);
        btn_plus = (Button) form.findViewById(R.id.calc_plus);
        btn_minus = (Button) form.findViewById(R.id.calc_minus);
        btn_multiply = (Button) form.findViewById(R.id.calc_multiply);
        btn_split = (Button) form.findViewById(R.id.calc_split);
        btn_equals = (Button) form.findViewById(R.id.calc_equals);
        btn_open = (Button) form.findViewById(R.id.calc_open);
        btn_close = (Button) form.findViewById(R.id.calc_close);
        btn_back = (Button) form.findViewById(R.id.calc_back);
        btn_dot = (Button) form.findViewById(R.id.cacl_dot);
        btn_clear = (Button)form.findViewById(R.id.calc_clear);

        useButtons();

        return(builder.setTitle(R.string.calc_title).setView(form)
                .setPositiveButton(R.string.calc_done,null).create());

    }

//метод решения примера
    private void solve(){

        while (!solved){

            for (int i = 0;i< example.length();i++){
                if (example.charAt(i) == '+' || example.charAt(i) == '–' || example.charAt(i) == '&' || example.charAt(i) == '/' ){
                    break;
                }
                if (i == example.length()-1){
                    solved = true;
                }
            }

//первый шаг - поиск скобок и избавление от них
            if (!needFirstStep){

                for (int i = 0;i< example.length();i++){

                    if (example.charAt(i) == '('){

                        foundOpen(i);
                        break;

                    }

                    if (i == example.length()-1){
                        needFirstStep = true;
                    }

                }

            }
            else {
//второй шаг  - умножение/деление
                if (!needSecondStep){
                    for (int i = 0;i< example.length();i++){
                        if (example.charAt(i) == '*' || example.charAt(i) == '/' ){
                            action(i);
                            break;
                        }
                        if (i == example.length()-1){
                            needSecondStep = true;
                        }
                    }
                }
                else{
//полседний шаг - сложение/вычитание
                    for (int i = 0;i< example.length();i++){
                        if (example.charAt(i) == '+' || example.charAt(i) == '–' ){
                            action(i);
                            break;
                        }
                    }
                }
            }

            for (int i = 0;i<example.length();i++){
                if (example.charAt(i) == '-'){
                    if (i>1) example.replace(i-1,i+1,"–");
                }
            }

        }

    }

//метод избавления от скобок
    private void foundOpen(int position) {

        int closePosition = 0;
        //Поиск скобок внутри,Вызывает рекурсию
        for (int i = position+1;i<example.length();i++){
            if (example.charAt(i) == ')'){
                closePosition = i;
                break;
            }
            if (example.charAt(i)== '('){
                foundOpen(i);
                break;
            }
        }
        // Делает умножение и деление
        if (!needSecondStepBrackets){
            for (int i = position;i<closePosition;i++){
                if (example.charAt(i) == '*' || example.charAt(i) == '/' ){
                    action(i);
                    break;
                }
                if (i == closePosition-1){
                    needSecondStepBrackets = true;
                }
            }
        }
        //Делает сложение и вычитание
        else{
            for (int i = position;i<closePosition-1;i++){
                if (example.charAt(i) == '+' || example.charAt(i) == '–' ){
                    action(i);
                    break;
                }
            }
        }

        boolean done = false;
        for (int i = position;i<closePosition;i++){
            if (example.charAt(i) == '+' || example.charAt(i) == '–' || example.charAt(i) == '&' || example.charAt(i) == '/' ){
                break;
            }
            if (i == closePosition-1){
                done = true;
            }
        }

        if (done){
            example.deleteCharAt(position);
            example.deleteCharAt(closePosition - 1);
            needSecondStepBrackets = false;
        }



    }

//метод вычисления
    private void action(int charPosition) {
        int charPos1 = 0;
        int charPos2 = example.length();

        //Определение границ первого и второго числа
        for (int i = charPosition-1;i>0;i--){
            if (example.charAt(i) == '+' || example.charAt(i) == '–' || example.charAt(i) == '&' || example.charAt(i) == '/' || example.charAt(i) == '(' ){
                charPos1 = i;
                break;
            }
        }
        for (int i = charPosition+1;i<example.length();i++){
            if (example.charAt(i) == '+' || example.charAt(i) == '–' || example.charAt(i) == '&' || example.charAt(i) == '/' || example.charAt(i) == ')' ){
                charPos2 = i;
                break;
            }
        }



        double solveExample = 0;
        String first,second;

        //Выделение двух чисел для дальнейших действий
        if (charPos1 == 0){
            first = example.substring(0,charPosition);
        }
        else{
            first = example.substring(charPos1+1,charPosition);
        }

        second = example.substring(charPosition+1,charPos2);

        switch (example.charAt(charPosition)){
            case '+':
                //Действие сложения
                solveExample = Double.parseDouble(first)+Double.parseDouble(second);
                break;
            case '–':
                //Действие вычитания
                solveExample = Double.parseDouble(first)-Double.parseDouble(second);
                break;
            case '*':
                //Действие умножения
                solveExample = Double.parseDouble(first)*Double.parseDouble(second);
                break;
            case '/':
                //Действие деления
                solveExample = Double.parseDouble(first)/Double.parseDouble(second);
                break;
        }

        if (charPos1 == 0){
            example.replace(charPos1, charPos2, String.valueOf(solveExample));
        }
        else{
            example.replace(charPos1 + 1, charPos2, String.valueOf(solveExample));
        }

    }

//назначение действий на кнопки
    private void useButtons(){

        //Настройка функциональности кнопок
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.calc_zero:
                        example.append('0');
                        break;
                    case R.id.calc_one:
                        example.append('1');
                        break;
                    case R.id.calc_two:
                        example.append('2');
                        break;
                    case R.id.calc_three:
                        example.append('3');
                        break;
                    case R.id.calc_four:
                        example.append('4');
                        break;
                    case R.id.calc_five:
                        example.append('5');
                        break;
                    case R.id.calc_six:
                        example.append('6');
                        break;
                    case R.id.calc_seven:
                        example.append('7');
                        break;
                    case R.id.calc_eight:
                        example.append('8');
                        break;
                    case R.id.calc_nine:
                        example.append('9');
                        break;
                    case R.id.calc_back:
                        example.deleteCharAt(example.length()-1);
                        break;
                    case R.id.calc_clear:
                        example = new StringBuilder();
                        break;
                    case R.id.calc_close:
                        example.append(')');
                        break;
                    case R.id.calc_open:
                        example.append('(');
                        break;
                    case R.id.calc_plus:
                        example.append('+');
                        break;
                    case R.id.calc_minus:
                        example.append('–');
                        break;
                    case R.id.calc_multiply:
                        example.append('*');
                        break;
                    case R.id.calc_split:
                        example.append('/');
                        break;
                    case R.id.calc_equals:
                        example.append("+0");
                        try {
                            solve();
                        }
                        catch (Exception e){
                            example = new StringBuilder();
                            Toast.makeText(getActivity(), R.string.setts_field_post4, Toast.LENGTH_SHORT).show();

                        }
                        //Обнуление переменных
                        solved = false;
                        needFirstStep = false;
                        needSecondStep = false;
                        needSecondStepBrackets = false;
                        break;
                    case R.id.cacl_dot:
                        example.append('.');
                        break;
                }

                primerTV.setText(example.toString());

            }
        };

        //Назначение функционала кнопок
        num0.setOnClickListener(onClickListener);
        num1.setOnClickListener(onClickListener);
        num2.setOnClickListener(onClickListener);
        num3.setOnClickListener(onClickListener);
        num4.setOnClickListener(onClickListener);
        num5.setOnClickListener(onClickListener);
        num6.setOnClickListener(onClickListener);
        num7.setOnClickListener(onClickListener);
        num8.setOnClickListener(onClickListener);
        num9.setOnClickListener(onClickListener);
        btn_equals.setOnClickListener(onClickListener);
        btn_dot.setOnClickListener(onClickListener);
        btn_close.setOnClickListener(onClickListener);
        btn_open.setOnClickListener(onClickListener);
        btn_back.setOnClickListener(onClickListener);
        btn_minus.setOnClickListener(onClickListener);
        btn_plus.setOnClickListener(onClickListener);
        btn_multiply.setOnClickListener(onClickListener);
        btn_split.setOnClickListener(onClickListener);
        btn_clear.setOnClickListener(onClickListener);

    }

}
