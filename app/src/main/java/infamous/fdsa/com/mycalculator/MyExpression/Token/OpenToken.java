package infamous.fdsa.com.mycalculator.MyExpression.Token;

public class OpenToken extends Token {

    //Hàm khởi tạo đối tượng (
    public OpenToken() {
        super(Token.OPEN_TOKEN);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "(";
    }

}
