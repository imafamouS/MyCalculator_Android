package infamous.fdsa.com.mycalculator.MyExpression.Token;

public abstract class Token {
    public static final int NUMBER_TOKEN = 1;//Đối tượng toán hạng
    public static final int OPERATOR_TOKEN = 2;//Đổi tượng (
    public static final int OPEN_TOKEN = 3;//Đối tượng }
    public static final int CLOSE_TOKEN = 4;//Đối tượng toán tử

    private int typeToken; //Kiểu đối tượng

    public Token(int typeToken) {
        this.typeToken = typeToken;
    }

    public int getType() {
        return typeToken;
    }

    public abstract String toString();

}
