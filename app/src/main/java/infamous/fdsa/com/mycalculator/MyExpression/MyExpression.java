package infamous.fdsa.com.mycalculator.MyExpression;


import java.util.Stack;

import infamous.fdsa.com.mycalculator.MyExpression.RPN.RPN;
import infamous.fdsa.com.mycalculator.MyExpression.Token.NumberToken;
import infamous.fdsa.com.mycalculator.MyExpression.Token.OperatorToken;
import infamous.fdsa.com.mycalculator.MyExpression.Token.Token;


public class MyExpression {

    private Token[] tokens;
    private String expression;

    public MyExpression(String expression) {
        this.expression = expression;
        this.tokens = RPN.convertInfix2Postfix(expression);
    }

    public MyExpression() {

    }

    //Hàm tính giá trị của biểu thức
    public Double evaluate() {
        Stack<Double> result = new Stack<>();
        try {

            for (Token tok : tokens) {
                if (tok.getType() == Token.NUMBER_TOKEN) {
                    //Nếu token thuộc loại số thì lấy giá trị ra và đẩy vào trong stack
                    double valueNumber = ((NumberToken) tok).getValue();
                    result.push(valueNumber);
                } else if (tok.getType() == Token.OPERATOR_TOKEN) {
                    //Nếu token thuộc loại toán tử thì lấy giá trị ra và đẩy vào trong stack
                    OperatorToken o = (OperatorToken) tok;

                    double rightValue = result.pop();
                    double leftValue = result.pop();

                    result.push(o.getOperator().calculate(leftValue, rightValue));
                }
            }
            return result.pop();

        } catch (Exception e) {
            System.out.println("Bad expression");
            return null;
        }
    }

    //Hàm trả về biểu thức hậu tố
    public String getPostfix() {
        String result = "[";
        for (Token i : tokens) {
            result += i.toString() + ",";
        }
        return result + "]";
    }

    //Hàm trả về các toán hạng và toán tử dưới dạng hậu tố
    public Token[] getTokens() {
        return tokens;
    }

    //Hàm gán giá trị các tokens
    public void setTokens(Token[] tokens) {
        this.tokens = tokens;
    }

    //Hàm gán giá biểu thức
    public void setExpression(String expression) {
        this.expression = expression;
    }

}
