package infamous.fdsa.com.mycalculator.MyExpression.Token;

import java.util.ArrayList;
import java.util.List;

import infamous.fdsa.com.mycalculator.MyExpression.Operator.Operator;


public class Tokenizer {

    public ArrayList<Token> tokens;
    private String expression;

    //Hàm khởi tạo
    public Tokenizer(String expression) {
        this.expression = expression;
        this.tokens = this.parse2ArrayTokens();
    }


    /**
     * Hàm chuyển đổi các toán hạng, toán tử thành các đối tượng tương ứng
     * @return Mảng các đối tượng Token
     */
    private ArrayList<Token> parse2ArrayTokens() {

        List<String> arrayToken = splitExpression2Tokens(this.expression);

        ArrayList<Token> resultTokens = new ArrayList<>();

        for (String tok : arrayToken) {
            if (tok.equals(""))
                //Bỏ qua cái kí tự rỗng
                continue;

            if (isNumber(tok)) {
                //Nếu là số thì khởi tạo đối tượng Token số
                NumberToken number = new NumberToken(Double.parseDouble(tok));
                resultTokens.add(number);
            } else if (isOpen(tok)) {
                //Nếu là ( khởi tạo đối tượng token open
                OpenToken opentoken = new OpenToken();
                resultTokens.add(opentoken);
            } else if (isClose(tok)) {
                //Nếu là ( khởi tạo đối tượng token open
                CloseToken closetoken = new CloseToken();
                resultTokens.add(closetoken);
            } else if (Operator.isAllowedCharacter(tok)) {
                //Kiểm tra xem toán tử đầu vào có nằm trong danh sách các toán tử cho phép ko
                //
                //Khởi tạo toán tử dựa trên biểu tượng (symbol)
                Operator o = Operator.buildOperator(tok);
                //Khởi tạo đối tượng token toán tử
                OperatorToken oToken = new OperatorToken(o);

                resultTokens.add(oToken);
            } else {

            }
        }
        return resultTokens;
    }

    /**
     *
     * Tách chuỗi đầu vào thành cái toán hạng và toán tử
     * Ví dụ: 2+(3*4): sau khi thực hiện hàm trả về 2,+,(,*,4,)
     * @param expression: biểu thức truyền vào
     * @return List<String>
     */
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
