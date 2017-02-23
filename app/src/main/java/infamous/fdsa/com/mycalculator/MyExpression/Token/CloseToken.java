package infamous.fdsa.com.mycalculator.MyExpression.Token;

public class CloseToken extends Token {

    //Khởi tạo lớp đối tượng }
    public CloseToken() {
        super(Token.CLOSE_TOKEN);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return ")";
    }

}
