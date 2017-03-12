package infamous.fdsa.com.mycalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String LOG_BUTTON_TAG = "Button ";

    private CalculatorInput mCalcInputText; //Lớp thực hiện việc xây dụng biểu thức
    private CalculatorEvalutor mCalcEval = new CalculatorEvalutor(); //Lớp thực hiện việc tính toán

    private TextView viewExp; //TextView hiện biểu thức
    private ScrollView scrollView; //Thnanh cuộn cho TextView

    private SpannableStringBuilder lastExpression = new SpannableStringBuilder(); //Biểu thức mới nhất
    private double lastResult; //Kết quả mới nhất
    private List<HistoryEntity> listHistory; //Danh sách các phép toán đã thực hiện
    boolean isResult; //Cờ hiệu kiểm tra xem đã biểu thức đã thực hiện chưa

    //Trạng thái của chương trình
    //ERORR: Bị lỗi
    //INPUT: Đang nhập
    //RESULT: Đã thực hiện biểu thức
    private CalculatorState currentState = CalculatorState.INPUT;


    //Hàm khởi tạo activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalcInputText = new CalculatorInput(this);

        viewExp = (TextView) findViewById(R.id.textExpression);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        listHistory = new ArrayList<>();
    }
    //Event button click
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btnSlideDown:
                onSlideDown();
                break;
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

                if (isOperator(b.getText().toString()) && currentState == CalculatorState.RESULT) {
                    //Khi đã có kết quả và nhấn tiếp dấu thì lấy kết quả tiếp tục thực hiện tính toán
                    mCalcInputText.set(formatResult(lastResult));
                }

                mCalcInputText.append(b.getText().toString());

                viewExp.setText(mCalcInputText.getBuilder());

                //Lăn thanh cuộn xuống phía dưới
                scrollView.fullScroll(View.FOCUS_DOWN);

                setState(CalculatorState.INPUT);
                break;
        }
    }


    //Khi nhấn nút delete
    private void onDeleteClick() {
        Log.d(LOG_BUTTON_TAG, " delete clicked");
        if (mCalcInputText.getBuilder() == null || mCalcInputText.getBuilder().length() <= 0) {
            return;
        }
        //Lấy biểu thức hiện tại
        SpannableStringBuilder currentExpression = mCalcInputText.getBuilder();
        int currentLength = currentExpression.length();
        //Lấy kí tự cuối cùng
        String deleteChar = currentExpression.charAt((currentLength - 1)) + "";
        //Kiểm tra kí tự xoá
        if (deleteChar.equals("("))
            mCalcInputText.minusOpen();
        else if (deleteChar.equals(")"))
            mCalcInputText.minusClose();
        //Xoá kí tự cuối cùng
        currentExpression = currentExpression.delete(currentLength - 1, currentLength);

        viewExp.setText(currentExpression);
    }

    //Khi nhấn nút bằng
    private void onEqualClick() {
        Log.d(LOG_BUTTON_TAG, " equals clicked");
        try{
            if (isResult == true)
                return;

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
                if(mCalcInputText.getExpresstion()==null||mCalcInputText.getExpresstion().length()<=0)
                    return;
                Double result = mCalcEval.evaluate(mCalcInputText.getExpresstion());
                if (result == null) {
                    setState(CalculatorState.ERROR);
                    return;
                }
                //Cập nhật 2 biến: lastExpression và lastResult
                lastExpression = mCalcInputText.getBuilder();
                lastResult=result;
                //Hiện kết quả
                showResult();
                //Lăn thanh cuộn
                autoScrollTextView();
                //Đặt lại trạng thái
                setState(CalculatorState.RESULT);
            }
        }catch (Exception e){
            setState(CalculatorState.ERROR);
        }

    }

    //Nhấn nút clear
    private void onClearClick() {

        //Reset lớp xây dụng biểu thức và lớp tính toán
        mCalcInputText.reset();
        mCalcEval.reset();
        //Reset TextView
        viewExp.setText("");
        //Đặt lại trạng thái
        setState(CalculatorState.INPUT);
    }

    //Nhấn nút SlideDown
    private void onSlideDown() {
        View v = (LinearLayout) findViewById(R.id.layoutNumber);

        if (listHistory.isEmpty()) {
            //Khi không có lịch sử hiển thị
            Toast.makeText(getApplicationContext(), getString(R.string.ERROR_HISTORY), Toast.LENGTH_SHORT).show();
            return;
        }
        if (v.getVisibility() == View.GONE) {
            //Khi Layout Number bị ẩn
            if (currentState == CalculatorState.RESULT) {
                viewExp.setText(lastExpression);
                showResult();
            } else {
                viewExp.setText(mCalcInputText.getBuilder());
            }
            v.setVisibility(View.VISIBLE);
            scrollView.fullScroll(View.FOCUS_DOWN);
        } else {
            //Khi Layout Number đang hiện
            showHistory();
            v.setVisibility(View.GONE);
        }

    }

    //Thay đổi trang thái
    private void setState(CalculatorState state) {
        if (currentState != state) {
            currentState = state;

            if (currentState == CalculatorState.INPUT) {
                isResult = false;
            }
            else if (currentState == CalculatorState.RESULT) {
                //Save to history
                isResult = true;

                HistoryEntity history = new HistoryEntity();
                history.setResult(lastResult+"");
                history.setExpression(this.lastExpression);

                listHistory.add(history);

            } else if (currentState == CalculatorState.ERROR) {

                final String errorString = "<font color='#F40056'>" + getResources().getString(R.string.ERROR_EVALUE) + "</font>";
                viewExp.append("\n");
                viewExp.append(Html.fromHtml(errorString));

            }
        }
    }

    //Hiện kết quả
    private void showResult() {
        viewExp.append("\n");
        viewExp.append(Html.fromHtml(changeColorResult(lastResult)));

    }

    //Đổi màu kết quả
    private String changeColorResult(double result) {

        return "<font color='#42aa46'> = " + formatResult(result) + "</font>";
    }

    //Hàm làm cho textView luôn cuộn về dưới đáy (bottom)
    private void autoScrollTextView() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
    //Chỉnh sửa định dạng kết quả (Bỏ .0)
    private String formatResult(Double result) {
        String text = String.valueOf(result);
        int lenDot = ".0".length();
        String out = "";
        if (text.length() > lenDot) {
            String end = text.substring(text.length() - lenDot, text.length());
            if (".0".equals(end)) {
                out = text.substring(0, text.length() - lenDot);
            } else {
                out = text;
            }
        }
        out=mCalcInputText.getDecimalFormattedFromString(out);
        return out;
    }
    //Hiện lịch sử
    private void showHistory() {
        viewExp.setText("");
        try{
            for(int i=listHistory.size()-1;i>-1;i--){
                HistoryEntity historyEntity=listHistory.get(i);
                viewExp.append(historyEntity.getExpression());
                viewExp.append("\n");
                viewExp.append(Html.fromHtml(changeColorResult(Double.parseDouble(historyEntity.getResult()))));
                viewExp.append("\n\n");
            }
        }catch (Exception e){

        }
        scrollView.fullScroll(View.FOCUS_UP);
    }



    //Kiểm tra xem biểu thức có kết thúc bằng dấu +-/* không
    private boolean isEndWithOperator() {
        String check=this.mCalcInputText.getBuilder().toString();
        return check.endsWith("+") || check.endsWith("-")
                || check.endsWith("×") || check.endsWith("÷");
    }

    //Kiểm tra xem có thiếu dấu )
    private boolean isDoneWithBracket() {
        return mCalcInputText.getOpen() == mCalcInputText.getClose();
    }

    //Hàm kiểm tra kí tự có phải là dấu
    private boolean isOperator(String str) {
        return "+-×÷".indexOf(str.charAt(0)) != -1;
    }

    //Trạng thái của Calculator
    public enum CalculatorState {
        INPUT, ERROR, RESULT
    }

}
