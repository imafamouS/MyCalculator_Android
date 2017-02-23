package infamous.fdsa.com.mycalculator.MyExpression.RPN;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import infamous.fdsa.com.mycalculator.MyExpression.Operator.Operator;
import infamous.fdsa.com.mycalculator.MyExpression.Token.OperatorToken;
import infamous.fdsa.com.mycalculator.MyExpression.Token.Token;
import infamous.fdsa.com.mycalculator.MyExpression.Token.Tokenizer;

public class RPN {

    public static Token[] convertInfix2Postfix(String expression) {
        Stack<Token> stackOperator = new Stack<>();
        List<Token> output = new ArrayList<>();

        Tokenizer tokenizer = new Tokenizer(expression);

        for (Token tok : tokenizer.tokens) {
            if (tok.getType() == Token.OPERATOR_TOKEN) {
                //Nếu token hiện tại là toán tử
                while (!stackOperator.empty() && stackOperator.peek().getType() == Token.OPERATOR_TOKEN) {

                    Operator o1 = ((OperatorToken) tok).getOperator();
                    Operator o2 = ((OperatorToken) stackOperator.peek()).getOperator();
                    //So sánh độ ưu tiên của
                    if ((o1.isleftAssociative() && o1.getPrecedence() <= o2.getPrecedence())
                            || (o1.getPrecedence() < o2.getPrecedence())) {
                        output.add(stackOperator.pop());
                        continue;
                    }
                    break;
                }
                stackOperator.push(tok);
            } else if (tok.getType() == Token.OPEN_TOKEN) {
                stackOperator.push(tok);
            } else if (tok.getType() == Token.CLOSE_TOKEN) {
                while (!stackOperator.empty() && !(stackOperator.peek().getType() == Token.OPEN_TOKEN)) {
                    output.add(stackOperator.pop());
                }
                stackOperator.pop();
            } else {
                output.add(tok);
            }
        }
        while (!stackOperator.empty()) {
            output.add(stackOperator.pop());
        }

        return (Token[]) output.toArray(new Token[output.size()]);
    }
}
