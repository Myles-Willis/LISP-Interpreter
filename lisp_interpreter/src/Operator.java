import java.util.ArrayList;

/**
 * The Operator class is responsible for the definition of every implemented function
 * used for evaluation of user inputted expressions. Each operator has their associated
 * form along with type. There are arithmetic, boolean and lisp operators.
 */
public enum Operator {
    LPAREN("(", "arithmetic"),
    RPAREN(")","arithmetic"),
    ADD("+","arithmetic"),
    SUBTRACT("-","arithmetic"),
    MULTIPLY("*","arithmetic"),
    DIVIDE("/","arithmetic"),
    SQRT("sqrt","arithmetic"),
    POW("pow","arithmetic"),

    // Boolean operators
    LESS("<","boolean"),
    LEQ("<=","boolean"),
    GEQ(">=","boolean"),
    EQUAL("=","boolean"),
    NOTEQ("!=","boolean"),
    GREATER(">","boolean"),
    AND("and", "boolean"),
    OR("or", "boolean"),
    NOT("not", "boolean"),

    //Lisp operators
    CAR("car", "lisp"),
    CDR("cdr", "lisp"),
    CONS("cons", "lisp"),
    IF("if", "lisp"),
    DEFINE("define", "lisp"),
    SET("set!", "lisp");

    private String operator;
    private String type;


    Operator(String operator, String type) {
        this.operator = operator;
        this.type = type;
    }

    public String getType() { return this.type; }

    public String toString() {
        return this.operator;
    }

    /***
     * This Function contains the switch statements for the logic of each ARITHMETIC Operator
     * @param arithmeticArguments A array of doubles to be evaluated
     * @return Token containing the result of the solved argument
     */
    public Token evalArithmetic(ArrayList<Double> arithmeticArguments) throws Exception {
       String op = this.operator;
       double result;
       Token resultToken;

       switch (op) {

           case "*":
               if (arithmeticArguments.size() == 1) {
                   result = arithmeticArguments.get(0);
                   break;
               }

               Double multiplicand = arithmeticArguments.get(0);
               arithmeticArguments.remove(0);

               for (Double multiplier : arithmeticArguments) {
                   multiplicand *= multiplier;
               }
               result = multiplicand;
               break;

           case "/":
               Double dividend = arithmeticArguments.get(0);
               arithmeticArguments.remove(0);

               if(arithmeticArguments.contains(0.0)) {
                   throw new Exception("(ERROR: Division by zero)");
               } else {
                   for (Double divisor : arithmeticArguments) {
                       dividend /= divisor;
                   }
                   result = dividend;
               }
               break;

           case "+":

               if (arithmeticArguments.size() == 1) {
                   result = arithmeticArguments.get(0);
                   break;
               }

               Double sum = 0.0;

               for (Double addend : arithmeticArguments) {
                   sum += addend;
               }
               result = sum;
               break;

           case "-":

               if (arithmeticArguments.size() == 1) {
                   result =  0 - arithmeticArguments.get(0);
                   break;
               }

               Double minuend = arithmeticArguments.get(0);
               arithmeticArguments.remove(0);

               for (Double subtrahend : arithmeticArguments) {
                   minuend -= subtrahend;
               }
               result = minuend;
               break;

           case "sqrt":
               result = Math.sqrt(arithmeticArguments.get(0));
               break;

           case "pow":
               result = Math.pow(arithmeticArguments.get(0), arithmeticArguments.get(1));
               break;

           default:
               result = Double.NaN;
       }
       resultToken = new Token(result);
       return resultToken;
    }

    /**
     * This Function contains the switch statements for the logic of each BOOLEAN operator
     * @param booleanArguments A array of doubles to be evaluated
     * @return Token containing the result of the solved argument
     */
    public Token evalBool(ArrayList<Double> booleanArguments){
        String op = this.operator;
        Boolean result = true;
        Token resultToken;

        Double firstNumber = booleanArguments.get(0);
        booleanArguments.remove(0);

        switch (op) {
            case "<":
                for (Double otherNumbers : booleanArguments) {
                    if(otherNumbers <= firstNumber) {
                        resultToken = new Token(false);
                        return resultToken;
                    }
                }
                break;
            case ">":
                for (Double otherNumbers : booleanArguments) {
                    if(otherNumbers >= firstNumber) {
                        resultToken = new Token(false);
                        return resultToken;
                    }
                }
                break;
            case "<=":
                for (Double otherNumbers : booleanArguments) {
                    if(otherNumbers < firstNumber) {
                        resultToken = new Token(false);
                        return resultToken;
                    }
                }
                break;
            case ">=":
                for (Double otherNumbers : booleanArguments) {
                    if(otherNumbers > firstNumber) {
                        resultToken = new Token(false);
                        return resultToken;
                    }
                }
                break;
            case "=":
                for (Double otherNumbers : booleanArguments) {
                    if(!otherNumbers.equals(firstNumber)) {
                        resultToken = new Token(false);
                        return resultToken;
                    }
                }
                break;
            case "!=":
                for (Double otherNumbers : booleanArguments) {
                    if(otherNumbers.equals(firstNumber)) {
                        resultToken = new Token(false);
                        return resultToken;
                    }
                }
                break;
            default:
                result = null;
        }
        resultToken = new Token(result);
        return resultToken;
    }
    /***
     * This Function contains the switch statements for the logic of each LISP operator
     * @param lispArguments A list of tokens
     * @return Token containing the result of the solved argument
     */
    public Token evalLisp(ArrayList<Token> lispArguments){
        String op = this.operator;
        Token result;

        switch (op) {
            case "car":
                return lispArguments.get(0).getTokenList().get(0);
            case "cdr":
                ArrayList<Token> cdrList = new ArrayList<>(lispArguments.get(0).getTokenList());
                cdrList.remove(0);
                Token cdrToken = new Token(cdrList);
                cdrToken.setLiteral(true);
                return cdrToken;
            case "cons":
                ArrayList<Token> consList = new ArrayList<>(lispArguments.get(1).getTokenList());
                consList.add(0, lispArguments.get(0));
                return new Token(consList);
            case "if":
                Boolean testClause = lispArguments.get(0).getBoolean();
                if(testClause) {
                    return lispArguments.get(1);
                } else {
                    return lispArguments.get(2);
                }

            default:
                result = null;
        }
        return result;
    }
}
