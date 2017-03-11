package infamous.fdsa.com.mycalculator.MyExpression.Token;


import infamous.fdsa.com.mycalculator.MyExpression.Operator.Operator;

public class OperatorToken extends Token {

    private Operator operator; //Toán tử

    /**
     * Hàm khởi tạo đối tượng toán tử
     *
     * @param operator: toán tử truyền vào
     */
    public OperatorToken(Operator operator) {
        super(Token.OPERATOR_TOKEN);
        // TODO Auto-generated constructor stub
        this.operator = operator;
    }

    //Hàm trả về kiểu toán tử của đối tượng token toán tử
    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return operator.getSympol();
    }

}
