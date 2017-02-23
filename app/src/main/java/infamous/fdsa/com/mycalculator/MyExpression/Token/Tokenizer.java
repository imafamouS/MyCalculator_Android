package infamous.fdsa.com.mycalculator.MyExpression.Token;

import java.util.ArrayList;
import java.util.List;

import infamous.fdsa.com.mycalculator.MyExpression.Operator.Operator;


public class Tokenizer {

    public ArrayList<Token> tokens;
    private String expression;

    public Tokenizer(String expression) {
        this.expression = expression;
        this.tokens = this.parse2ArrayTokens();
    }

    //Hàm chuyển đổi các toán hạng, toán tử thành các đối tượng tương ứng
    private ArrayList<Token> parse2ArrayTokens() {

        List<String> arrayToken = splitExpression2Tokens(this.expression);

        ArrayList<Token> resultTokens = new ArrayList<>();

        for (String tok : arrayToken) {
            if (tok.equals(""))
                continue;

            if (isNumber(tok)) {
                NumberToken number = new NumberToken(Double.parseDouble(tok));
                resultTokens.add(number);
            } else if (isOpen(tok)) {

                OpenToken opentoken = new OpenToken();
                resultTokens.add(opentoken);
            } else if (isClose(tok)) {

                CloseToken closetoken = new CloseToken();
                resultTokens.add(closetoken);
            } else if (Operator.isAllowedCharacter(tok)) {

                Operator o = Operator.buildOperator(tok);

                OperatorToken oToken = new OperatorToken(o);

                resultTokens.add(oToken);
            } else {

            }
        }
        return resultTokens;
    }

    //Tách chuỗi đầu vào thành cái toán hạng và toán tử
    private List<String> splitExpression2Tokens(String expression) {
        // Remove whitespace
        expression = expression.trim();
        expression = expression.replaceAll("\\s+", "");

        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {

            char currentCharacter = expression.charAt(i);

            if (Character.isDigit(currentCharacter) || currentCharacter == '.'
                    || (currentToken.length() == 0 && currentCharacter == '-')) {
                currentToken.append(currentCharacter);

            } else {
                tokens.add(currentToken.toString());
                currentToken.setLength(0);
                tokens.add(Character.toString(currentCharacter));
            }
        }
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }
        return tokens;
    }

    //Kiểm tra chuỗi có phải là số
    private boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Kiểm tra có phải là kí tự '("
    private boolean isOpen(String str) {
        return str.equals("(");
    }

    //Kiểm tra có phải là kí tự ')"
    private boolean isClose(String str) {
        return str.equals(")");
    }
}
