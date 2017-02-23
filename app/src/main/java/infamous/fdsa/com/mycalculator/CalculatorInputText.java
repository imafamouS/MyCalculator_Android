package infamous.fdsa.com.mycalculator;

import android.util.Log;

/**
 * Created by FDSA
 */

public class CalculatorInputText {


    /*
    * output_show Biểu thức để hiện lên EditText
    * output_expression Biểu thức để thực hiện tính toán
    * */
    private StringBuilder output_show;
    private String output_expression;

    /**
     * @open:Biến đếm số lượng "("
     * close:Biến đếm số lượng ")"
     */
    private int open = 0;
    private int close = 0;

    //Hàm khởi tạo Class
    public CalculatorInputText() {
        this.output_show = new StringBuilder();

    }

    //Hàm khởi tạo Class
    public CalculatorInputText(String input) {
        this.set(input);
    }

    public int getClose() {
        return close;
    }

    public int getOpen() {
        return open;
    }

    public void minusClose() {
        if (this.close <= 0)
            return;
        this.close--;
    }

    public void minusOpen() {
        if (this.open <= 0)
            return;
        this.open--;
    }

    public void reset() {
        this.set("");
        open = 0;
        close = 0;
    }

    /**
     * Hàm để gán giá trị cho Class
     *
     * @input: Chuổi đầu vào
     */
    public void set(String input) {
        this.output_show = new StringBuilder();
        this.output_show.setLength(0);
        this.output_show.append(input);
        this.output_expression = "";
    }

    //Hàm trả về biểu thức
    public String getExpresionShow() {
        return this.output_show.toString();
    }

    public String getExpression() {
        output_expression = this.getExpresionShow().replaceAll("×", "*");
        output_expression = output_expression.replaceAll("÷", "/");

        return this.output_expression.toString();
    }

    /**
     * Hàm xử lí kí tự sẽ xuất hiện kế kiếp
     *
     * @character kí tự tiếp theo
     */
    public void append(String character) {
        int lengthCurrentString = this.output_show.length(); //Độ dài của biểu thức hiện tại
        Log.d("DEBUG: ", lengthCurrentString + "");
        String appendStringShow = ""; //chuỗi trả về để thêm vào biểu thức hiện tại
        //.+×÷±()

        if (lengthCurrentString <= 0) {
            //Todo: Nếu chuỗi biểu thức không có kí tự nào
            switch (character) {
                case ".":
                case "+":
                case "×":
                case "÷":
                case "-":
                    appendStringShow = "0" + character;
                    break;
                case "±":
                    appendStringShow = "-0";
                    break;
                case "()":
                    appendStringShow = "(";
                    open++;
                    break;
                default:
                    appendStringShow = character;
                    break;
            }

        } else {
            //Todo: Chuỗi đã có các kí tự
            char lastCharacter = output_show.charAt(lengthCurrentString - 1); //@lastCharacter: kí tự cuối cùng trong biểu thức

            switch (character) {
                //Nếu kí tự tiếp theo +-*/
                case "+":
                case "×":
                case "÷":
                case "-":
                    if (lastCharacter == '+' && character.charAt(0) == '-') {
                        //+ nhập - => -
                        output_show.setLength(lengthCurrentString - 1);
                        appendStringShow = "-";
                        break;
                    } else if (lastCharacter == '-' && character.charAt(0) == '-') {
                        if (lengthCurrentString >= 2 && output_show.charAt(lengthCurrentString - 2) == '(') {
                            //(- nhập- => (
                            output_show.setLength(lengthCurrentString - 1);
                            appendStringShow = "";
                        } else {
                            //- nhập - => +
                            output_show.setLength(lengthCurrentString - 1);
                            appendStringShow = "+";
                        }
                        break;
                    } else if (lastCharacter == '-' && character.charAt(0) == '+') {
                        if (lengthCurrentString >= 2 && output_show.charAt(lengthCurrentString - 2) == '(') {
                            //(- Nhập + => (-
                            output_show.setLength(lengthCurrentString - 1);
                            appendStringShow = "-";

                        } else {
                            // (- nhập + => (
                            appendStringShow = "";

                        }
                        break;

                    } else if (Character.isDigit(lastCharacter)) {
                        //hiện bình thường
                        appendStringShow = character;
                        break;
                    } else if (lastCharacter == ')') {
                        //) nhập + thành )+
                        appendStringShow = character;
                        break;
                    } else if (!Character.isDigit(lastCharacter) && lastCharacter != '(') {
                        //* nhập - => - hoặc / nhập + => +
                        if (lastCharacter == '-') {
                            if (lengthCurrentString >= 2 && output_show.charAt(lengthCurrentString - 2) == '(') {

                                appendStringShow = "";
                                break;
                            }

                        } else {
                            output_show.setLength(lengthCurrentString - 1);
                            appendStringShow = character;
                            break;
                        }

                    } else if (lastCharacter == '(' && character.charAt(0) == '-') {
                        //( nhập - thành (-
                        appendStringShow = "-";
                        break;
                    } else {

                        appendStringShow = "";
                        break;
                    }
                    //Kí tự +/-
                case "±":
                    if (lastCharacter == '-') {
                        if (lengthCurrentString >= 2 && output_show.charAt(lengthCurrentString - 2) == '(') {
                            //(- nhập ± => (
                            output_show.setLength(lengthCurrentString - 1);
                            appendStringShow = "";
                        } else {
                            //- nhập ± => +
                            output_show.setLength(lengthCurrentString - 1);
                            appendStringShow = "+";
                        }
                        break;
                    } else if (lastCharacter == '+') {
                        //+ nhập ± => -
                        output_show.setLength(lengthCurrentString - 1);
                        appendStringShow = "-";
                        break;
                    } else if (lastCharacter == '×' || lastCharacter == '÷') {
                        //× nhập ± => ""
                        appendStringShow = "";
                        break;
                    } else if (lastCharacter == '(') {
                        //( nhập ± => (-
                        appendStringShow = "-";
                        break;
                    } else if (Character.isDigit(lastCharacter)) {
                        //3 nhập ± => 3-
                        appendStringShow = "-";
                        break;
                    }
                    break;
                //Kí tự ()
                case "()":
                    if (lastCharacter == '.') {
                        //3. nhập () => 3.
                        appendStringShow = "";
                        break;
                    }
                    if (Character.isDigit(lastCharacter) && open == 0 && close == 0) {
                        //3 nhập () => 3*(
                        appendStringShow = "×(";
                        open++;
                        break;
                    } else if (!Character.isDigit(lastCharacter) && open == 0 && close == 0) {
                        //3+ nhập () => 3+(
                        appendStringShow = "(";
                        open++;
                        break;
                    } else {
                        if (open > close) {
                            if (Character.isDigit(lastCharacter)) {
                                //3+(3 nhập () => 3+(3)
                                appendStringShow = ")";
                                close++;
                                break;
                            } else if (lastCharacter == '(') {
                                //3+( nhập () => 3+((
                                appendStringShow = "(";
                                open++;
                                break;
                            } else if (lastCharacter == ')') {
                                //3+((3) nhập () => 3+((3))
                                appendStringShow = ")";
                                close++;
                                break;
                            } else if (!Character.isDigit(lastCharacter)) {
                                if(lastCharacter=='-'){
                                    if (lengthCurrentString >= 2 && output_show.charAt(lengthCurrentString - 2) == '('){
                                        output_show.setLength(lengthCurrentString - 1);
                                        appendStringShow = "0-(";
                                        open++;
                                        break;
                                    }
                                }else{
                                    //3*((3+ nhập () thành 3*((3+(
                                    appendStringShow = "(";
                                    open++;
                                    break;
                                }
                            }
                        } else if (open == close) {
                            if (lastCharacter == ')') {
                                //3+((3)) nhập () => 3+((3))*(
                                appendStringShow = "×(";
                                open++;
                                break;
                            } else if (!Character.isDigit(lastCharacter)) {
                                //3+((3))+ nhập () => 3+((3))+(
                                appendStringShow = "(";
                                open++;
                                break;
                            } else if (Character.isDigit(lastCharacter)) {
                                appendStringShow = "×(";
                                open++;
                                break;
                            }
                        }
                    }
                    break;
                //Kí tự .
                case ".":
                    if (lastCharacter == '.') {
                        //3. nhập . => 3.
                        appendStringShow = "";
                        break;
                    } else if (lastCharacter == ')') {
                        //(3) nhập . => (3)*0.
                        appendStringShow = "×0.";

                        break;
                    } else if (!Character.isDigit(lastCharacter)) {
                        //3+ nhập . => 3+0.
                        appendStringShow = "0.";
                        break;
                    } else {

                        int countDot = 0; //Đếm số . trong số
                        for (int i = lengthCurrentString - 1; i > 0; i--) {
                            char currentChar = output_show.charAt(i);
                            char previousChar = output_show.charAt(i - 1);
                            if (Character.isDigit(currentChar)) {
                                if (previousChar == '.')
                                    countDot++;
                                else if ("+-×÷".indexOf(Character.toString(previousChar)) != -1) {
                                    break;
                                } else
                                    continue;
                            }
                        }
                        if (countDot > 0) {
                            //3.123 nhập . => 3.123
                            appendStringShow = "";
                            break;
                        } else {
                            //3 nhập . => 3.
                            appendStringShow = ".";
                            break;
                        }
                    }
                    //Số
                default:
                    if (lastCharacter == ')') {
                        // ((3)) nhập 3=> ((3))*3
                        appendStringShow = "×" + character;

                        break;
                    } else {
                        //nhập số
                        appendStringShow = character;

                        break;
                    }
            }
        }
        this.output_show.append(appendStringShow);
    }
}
