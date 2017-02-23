package infamous.fdsa.com.mycalculator.MyExpression.Token;


import infamous.fdsa.com.mycalculator.MyExpression.Operator.Operator;

public class OperatorToken extends Token {

    private Operator operator;

    public OperatorToken(Operator operator) {
        super(Token.OPERATOR_TOKEN);
        // TODO Auto-generated constructor stub
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return operator.getSympol();
    }

}
