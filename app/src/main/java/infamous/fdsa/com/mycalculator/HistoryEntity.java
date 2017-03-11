package infamous.fdsa.com.mycalculator;

import android.text.SpannableStringBuilder;

/**
 * Created by FDSA on 3/8/2017.
 */

public class HistoryEntity {

    private SpannableStringBuilder expression; //Biểu thức
    private String result; //Kết quả

    //Hàm khởi tạo lớp chứa lịch sử
    public HistoryEntity(SpannableStringBuilder expression, String result) {
        this.expression = expression;
        this.result = result;
    }

    public HistoryEntity() {

    }

    //Hàm trả về biểu thức
    public SpannableStringBuilder getExpression() {
        return expression;
    }

    //Hàm gán biểu thức
    public void setExpression(SpannableStringBuilder expression) {
        this.expression = expression;
    }

    //Hàm trả về kết quả
    public String getResult() {
        return result;
    }

    //Hàm gán giá trị
    public void setResult(String result) {
        this.result = result;
    }
}
