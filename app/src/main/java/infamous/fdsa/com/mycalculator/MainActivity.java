package infamous.fdsa.com.mycalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String LOG_BUTTON_TAG = "Button ";
    private final CalculatorInputText mCalcInputText = new CalculatorInputText();
    private final CalculatorEvalutor mCalcEval = new CalculatorEvalutor();

    private String expression2show;
    private EditText viewExp;

    private EditText viewResult;
    private double finalResult;

    private CalculatorState currentState;

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        //Tính toán sau khi nhấn các nút
        @Override
        public void afterTextChanged(Editable editable) {

            //Toast.makeText(getApplicationContext(),"Đang tính",Toast.LENGTH_SHORT).show();
            Double result = mCalcEval.evaluate(mCalcInputText.getExpression());
            if (result == null) {
                if (mCalcInputText.getExpression().length() <= 0)
                    viewResult.setText("");
                return;
            }
            viewResult.setText(result + "");
            setState(CalculatorState.INPUT);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewExp = (EditText) findViewById(R.id.textExpression);
        viewExp.addTextChangedListener(mTextWatcher);

        viewResult = (EditText) findViewById(R.id.textResult);

    }

    //Event button click
    public void onButtonClick(View view) {
        switch (view.getId()) {

            case R.id.btnClear:
                //Button Clear => C
                onClearClick();
                break;
            case R.id.btnDelete:
                //Button Delete => DEL
                onDeleteClick();
                break;
            case R.id.btnEqual:
                //Button Equal => =
                onEqualClick();
                break;
            default:
                //Button Number,Dot,OpenorClose,Operator => 0.12345679()
                Button b = (Button) view;
                Log.d(LOG_BUTTON_TAG, b.getText() + " clicked");

                mCalcInputText.append(b.getText().toString());

                Log.d("EXPRESSION ", mCalcInputText.getExpression());

                expression2show = mCalcInputText.getExpresionShow();
                viewExp.setText(Html.fromHtml(changeColor(expression2show)));

                //Trỏ tới vị trí hiện tại của text trong eddittext
                focusView(viewExp);
                break;
        }
    }

    //Khi nhấn nút delete
    private void onDeleteClick() {
        Log.d(LOG_BUTTON_TAG, " delete clicked");
        if (mCalcInputText.getExpresionShow() == null || mCalcInputText.getExpresionShow().length() <= 0) {

            return;
        }
        //Lấy biểu thức hiện tại
        String currentExpression = mCalcInputText.getExpresionShow();
        int currentLength = currentExpression.length();
        //Lấy kí tự cuối cùng
        String deleteChar = currentExpression.charAt((currentLength - 1)) + "";


        if (deleteChar.equals("("))
            mCalcInputText.minusOpen();
        else if (deleteChar.equals(")"))
            mCalcInputText.minusClose();
        //Xoá kí tự cuối cùng
        currentExpression = currentExpression.substring(0, currentLength - 1);
        mCalcInputText.set(currentExpression);

        viewExp.setText(Html.fromHtml(changeColor(currentExpression)));

        focusView(viewExp);
    }

    //Khi nhấn nút bằng
    private void onEqualClick() {
        Log.d(LOG_BUTTON_TAG, " equals clicked");

        if (isEndWithOperator()) {
            //Lỗi kết thúc bởi toán tử
            setState(CalculatorState.ERROR);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.ERROR_ENDWITHOPERATOR), Toast.LENGTH_SHORT).show();
            return;
        } else if (!isDoneWithBracket()) {
            //Thiếu đóng ngoặc
            setState(CalculatorState.ERROR);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.ERROR_MISSING_BRACKETS), Toast.LENGTH_SHORT).show();
            return;
        } else {
            //
            Double result = mCalcEval.evaluate(mCalcInputText.getExpression());
            if (result == null) {
                setState(CalculatorState.ERROR);
                return;
            }
            finalResult = result;
            Log.d("Result ", result + "");
            String outResult = "<font color='#FFA500'> = </font>" + "<font color='#42aa46'>" + finalResult + "</font>";

            viewResult.setText("");
            viewResult.setText(Html.fromHtml(outResult));
            focusView(viewResult);
            setState(CalculatorState.RESULT);
        }

    }

    //Nhấn nút clear
    private void onClearClick() {
        Log.d(LOG_BUTTON_TAG, " Clear clicked");

        mCalcInputText.reset();
        mCalcEval.reset();

        viewResult.setText("");
        viewExp.setText("");

    }

    //Hàm đổi màu của biểu thức
    private String changeColor(String input) {
        String str = "";
        for (int i = 0; i < input.length(); i++) {
            String currentChar = input.charAt(i) + "";
            if (currentChar.toString().equals("+") || currentChar.toString().equals("-")
                    || currentChar.toString().equals("×") || currentChar.toString().equals("÷")) {
                str = str + " <font color='#12a4c2'>" + currentChar + "</font> ";
            } else {
                str = str + currentChar;
            }

        }
        return str;
    }

    //Trỏ tới vị trí hiện tại của text trong eddittext
    private void focusView(View view) {
        if (view == null)
            return;
        final EditText et = (EditText) view;
        int positionLastCharacter = et.getText().length();
        if (positionLastCharacter <= 0)
            return;
        et.setSelection(positionLastCharacter);
    }

    //Thay đổi trang thái
    private void setState(CalculatorState state) {
        if (currentState != state) {
            currentState = state;

            if (currentState == CalculatorState.RESULT) {

                //Save to history

            } else if (currentState == CalculatorState.ERROR) {
                final int errorColor = getResources().getColor(R.color.colorCalculatorError);
                final String errorString = getResources().getString(R.string.ERROR_EVALUE);
                viewResult.setTextColor(errorColor);
                viewResult.setText(errorString);
            } else {
                final int colorDefaultText = getResources().getColor(R.color.colorABC);
                viewResult.setTextColor(colorDefaultText);
            }
        }
    }

    //Kiểm tra xem biểu thức có kết thúc bằng dấu +-/* không
    private boolean isEndWithOperator() {
        return expression2show.endsWith("+") || expression2show.endsWith("-")
                || expression2show.endsWith("×") || expression2show.endsWith("÷");
    }
    //Kiểm tra xem có thiếu dấu )
    private boolean isDoneWithBracket() {
        return mCalcInputText.getOpen() == mCalcInputText.getClose();
    }
    //Trạng thái của Calculator
    public enum CalculatorState {
        INPUT, ERROR, RESULT
    }

}
