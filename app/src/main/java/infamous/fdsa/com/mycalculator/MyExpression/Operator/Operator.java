package infamous.fdsa.com.mycalculator.MyExpression.Operator;

public class Operator {
    public static final String ALLOWED_OPERATOR = "+-*/";
    /* Priority Operator */ //Thứ tự uy tiên của các toán tử
    public final static int PRECEDENCE_PLUS = 1;
    public final static int PRECEDENCE_MINUS = 1;
    public final static int PRECEDENCE_MULTIPLIY = 2;
    public final static int PRECEDENCE_DIVIDE = 2;

    public final static boolean LEFT_ASSOC = true; //Toán tử có toán tử phía bên trái
    public final static boolean RIGHT_ASSOC = false;  //Toán tử có toán tử phía bên phải

    private String symbol;
    private int precedence;
    private boolean leftAssociative;

    //Hàm khởi tạo toán tử
    public Operator(String symbol, int precedence, boolean leftAssociative) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.leftAssociative = leftAssociative;
    }

    //Hàm kiểm tra 1 chuỗi có phải là toán tử
    public static boolean isAllowedCharacter(String ch) {
        return ALLOWED_OPERATOR.indexOf(ch) != -1;
    }

    //Hàm xây dựng 1 đối tượng toán tử với 1 ký hiệu đầu vào
    public static Operator buildOperator(String ch) {
        switch (ch) {
            case "+":
                return new Operator("+", Operator.PRECEDENCE_PLUS, Operator.LEFT_ASSOC);
            case "-":
                return new Operator("-", Operator.PRECEDENCE_MINUS, Operator.LEFT_ASSOC);
            case "*":
                return new Operator("*", Operator.PRECEDENCE_MULTIPLIY, Operator.LEFT_ASSOC);
            case "/":
                return new Operator("/", Operator.PRECEDENCE_DIVIDE, Operator.LEFT_ASSOC);
            default:
                return null;
        }
    }

    //Hàm trả về ký hiệu của toán tử
    public String getSympol() {
        return symbol;
    }

    //hàm trả về thứ tự ưu tiên của toán tử
    public int getPrecedence() {
        return precedence;
    }

    //Hàm kiểm tra xem toán tứ có toán tử phía bên trái ko
    public boolean isleftAssociative() {
        return leftAssociative;
    }

    //Hàm tính giá trị của toán tử
    public Double calculate(double left, double right) {

        if ("+".equals(symbol)) {
            return left + right;
        } else if ("-".equals(symbol)) {
            return left - right;
        } else if ("*".equals(symbol)) {
            return left * right;
        } else if ("/".equals(symbol)) {
            if (right == 0)
                throw new ArithmeticException("Division by zero!");
            return left / right;
        } else {
            return null;
        }

    }
}
