package infamous.fdsa.com.mycalculator;

import infamous.fdsa.com.mycalculator.MyExpression.MyExpression;

/**
 * Created by FDSA
 */

public class CalculatorEvalutor {
    private String postfix;


    public CalculatorEvalutor() {

    }

    //Hàm tính giá trị
    public Double evaluate(String expression) {
        MyExpression eval = new MyExpression(expression);
        this.postfix = eval.getPostfix();
        Double result = eval.evaluate();
        return result;
    }

    //Hàm trả về hậu tố
    public String getPostfix() {
        return postfix;
    }

    //Hàm reset postfix
    public void reset() {
        this.postfix = "";
    }
}
