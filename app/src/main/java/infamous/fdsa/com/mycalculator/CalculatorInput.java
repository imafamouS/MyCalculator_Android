package infamous.fdsa.com.mycalculator;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.util.StringTokenizer;

/**
 * Created by FDSA on 3/3/2017.
 */

public class CalculatorInput {

    private final String LOG_INPUT_TAG = "INPUT ";

    private SpannableStringBuilder builder;
    private Context context;

    private int open = 0; //Đếm số lượng (
    private int close = 0; //Đếm số lượng )
    //Hàm khởi tạo
    public CalculatorInput(Context context) {
        builder = new SpannableStringBuilder();
        this.context = context;
    }
    //Hàm trả về giá trị của biến close
    public int getClose() {
        return close;
    }

    //Hàm trả về giá trị của biến open
    public int getOpen() {
        return open;
    }

    //Hàm giảm giá trị 1 biến open
    public void minusClose() {
        if (this.close <= 0)
            return;
        this.close--;
    }

    //Hàm giảm giá trị 1 biến open
    public void minusOpen() {
        if (this.open <= 0)
            return;
        this.open--;
    }

    //Hàm reset CalculatorInputText
    public void reset() {
        this.set("");
        open = 0;
        close = 0;
        ;
    }
    //Hàm đặt lại biểu thức
    public void set(String input) {
        this.builder = new SpannableStringBuilder(input);
    }
    //Hàm thêm kí tự vào chuỗi hiện tại của biểu thức
    public String getExpresstion() {
        //return (this.builder.toString().replaceAll("×", "*")).replaceAll("÷", "/");
        StringBuilder out=new StringBuilder("");
        String in=this.getBuilder().toString();
        for(int i=0;i<in.length();i++){
            char ch=in.charAt(i);
            if(ch=='×')
                out.append("*");
            else if(ch=='÷'){
                out.append("/");
            }else if(ch==','){
                out.append("");
            }else
                out.append(ch);
        }
        return out.toString();
    }
    //Hàm gọi biểu thức để hiển thị
    public SpannableStringBuilder getBuilder() {
        return this.builder;
    }
    //Hàm thêm vào biểu thức hiện tại toán tự hoặc toán hạng mới
    public void append(String character) {
        int lengthCurrentString = this.builder.length();
        String appendStringShow = "";
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
                case "+/-":
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
            char lastCharacter = builder.charAt(lengthCurrentString - 1); //@lastCharacter: kí tự cuối cùng trong biểu thức

            switch (character) {
                //Nếu kí tự tiếp theo +-*/
                case "+":
                case "×":
                case "÷":
                case "-":
                    if (lastCharacter == '+' && character.charAt(0) == '-') {
                        //+ nhập - => -
                        removeLast(lengthCurrentString);
                        appendStringShow = "-";
                        break;
                    } else if (lastCharacter == '-' && character.charAt(0) == '-') {
                        if (lengthCurrentString >= 2 && builder.charAt(lengthCurrentString - 2) == '(') {
                            //(- nhập- => (
                            removeLast(lengthCurrentString);
                            appendStringShow = "";
                            break;
                        } else {
                            //- nhập - => +
                            removeLast(lengthCurrentString);
                            appendStringShow = "+";
                            break;
                        }
                    } else if (lastCharacter == '-' && character.charAt(0) == '+') {
                        if (lengthCurrentString >= 2 && builder.charAt(lengthCurrentString - 2) == '(') {
                            //(- Nhập + => (-
                            removeLast(lengthCurrentString);
                            appendStringShow = "-";
                            break;
                        } else {
                            // (- nhập + => (
                            appendStringShow = "";
                            break;
                        }
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
                            if (lengthCurrentString >= 2 && builder.charAt(lengthCurrentString - 2) == '(') {
                                appendStringShow = "";
                                break;
                            }else{
                                removeLast(lengthCurrentString);
                                appendStringShow = character;
                                break;
                            }
                        } else {
                            removeLast(lengthCurrentString);
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
                case "+/-":
                    if (lastCharacter == '-') {
                        if (lengthCurrentString >= 2 && builder.charAt(lengthCurrentString - 2) == '(') {
                            //(- nhập ± => (
                            removeLast(lengthCurrentString);
                            appendStringShow = "";

                        } else {
                            //- nhập ± => +
                            removeLast(lengthCurrentString);
                            appendStringShow = "+";

                        }
                        break;
                    } else if (lastCharacter == '+') {
                        //+ nhập ± => -
                        removeLast(lengthCurrentString);
                        appendStringShow = "-";

                        break;
                    } else if (lastCharacter == '×') {
                        //× nhập ± => ""
                        appendStringShow = "(-";
                        open++;
                        break;

                    }else if(lastCharacter == '÷'){
                        appendStringShow = "(-";
                        open++;
                        break;
                    }
                    else if (lastCharacter == '(') {
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
                                if (lastCharacter == '-') {
                                    if (lengthCurrentString >= 2 && builder.charAt(lengthCurrentString - 2) == '(') {
                                        removeLast(lengthCurrentString);
                                        appendStringShow = "0-(";
                                        open++;
                                        break;
                                    } else {
                                        appendStringShow = "(";
                                        open++;
                                        break;
                                    }
                                } else {
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
                            char currentChar = builder.charAt(i);
                            char previousChar = builder.charAt(i - 1);
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
                       if (lastCharacter == '0' && character.equals("0")) {
                            //Không cho nhập 00000000000000
                            int countDot = 0; //Đếm số . trong số
                            for (int i = lengthCurrentString - 1; i > 0; i--) {
                                char currentChar = builder.charAt(i);
                                char previousChar = builder.charAt(i - 1);
                                if (Character.isDigit(currentChar)) {
                                    if (previousChar == '.')
                                        countDot++;
                                    else if ("+-×÷".indexOf(Character.toString(previousChar)) != -1) {
                                        break;
                                    } else
                                        continue;
                                }
                            }
                            if (countDot > 0)
                                appendStringShow = "0";
                            else if(countDot==0 && builder.charAt(0)=='0')
                                appendStringShow = "";
                           else if(countDot==0 && builder.charAt(0)!='0' )
                                appendStringShow="0";
                            break;
                        } else{
                           appendStringShow = character;
                           break;
                       }


                    }
            }
        }

        builder.append(changeColorOperator(appendStringShow));
        showDecimalFormat();
        Log.d(LOG_INPUT_TAG,this.getExpresstion());
    }

    //Hàm hiện phân cách phần ngàn
    public void showDecimalFormat() {
        int countDigit=0;
        for(int i=builder.length()-1;i>-1;i--){
            //Đếm số lượng chữ số
            char ch=builder.charAt(i);
            if(Character.isDigit(ch)||ch=='.'||ch==','){
                countDigit++;
            }else{
                break;
            }
        }
        String tt="";
        if(countDigit>0){
            String temp=builder.toString().substring(builder.length()-countDigit);
            temp=temp.replaceAll(",","");
            tt=getDecimalFormattedFromString(temp);
            int index2insert=builder.length()-countDigit;
            builder.delete(builder.length()-countDigit,builder.length());
            builder.insert(index2insert,tt);
        }
    }

    //Thêm , giữa các phần ngàn
    public String getDecimalFormattedFromString(String value)
    {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3) {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }
    //Xoá kí tự cuối cùng
    private void removeLast(int len) {
        this.builder = this.builder.delete(len - 1, len);
    }

    //Đổi màu các toán tử
    private SpannableString changeColorOperator(String str) {
        int index = -1;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '+' || ch == '-' || ch == '×' || ch == '÷') {
                index = i;
                break;
            }
        }

        SpannableString result = new SpannableString(str);
        if (index != -1) {
            result.setSpan(new ForegroundColorSpan(Color.parseColor("#12a4c2")), index, index + 1, 0);
        }
        return result;
    }
}
