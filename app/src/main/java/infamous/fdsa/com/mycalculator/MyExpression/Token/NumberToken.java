package infamous.fdsa.com.mycalculator.MyExpression.Token;

public class NumberToken extends Token {

    private double value;
    //Khởi tạo đối tượng là số
    public NumberToken(double value) {
        super(Token.NUMBER_TOKEN);
        // TODO Auto-generated constructor stub
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }

}
