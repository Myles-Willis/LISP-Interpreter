import java.util.*;

/**
 *The Evaluator structures a evaluator object which takes a given expression,
 * evaluates it, then returns a token containing the result
 */
public class Evaluator {

    Token expression;
    Stack<Token> stack = new Stack<>();
    HashMap<String, Double> hashMap;
    Token result; //to pass result back

    //Constructors
    public Evaluator(Token expression) {
        this.expression = expression;
        this.hashMap = null;
    }

    public Evaluator(Token expression, HashMap<String, Double> hashMap) {
        this.expression = expression;
        this.stack = null;
        this.hashMap = hashMap;
    }

    //Getter
    public Token getResult() {
        return this.result;
    }

    //Setter
    public void setResult(Token result) {
        this.result = result;
    }

    /**
     * Print out a representation of the given stack
     */
    public void printStackView() {

        Stack<Token> stackView = (Stack<Token>)this.stack.clone();
        System.out.println("Stack: ");
        System.out.println("-------------------------");
        for(int i = 0; i <= stackView.size(); i++) {
            System.out.println(stackView.pop().toTokenString());
            System.out.println("\n-------------------------");
        }
    }

    /**
     * Evaluate the given token of arguments into its final form using recursion
     * This uses the arguments in stack form then uses some of the logic in Operator
     * class to evaluate each argument
     * @param token a token to be evaluated
     * @param hashMap the hashmap of the system (Created in the interpreter file)
     * @return a token holding the evaluated expression.
     * @throws Exception
     */
    public Token evaluate(Token token, HashMap<String, Token> hashMap) throws Exception {

        if(token.isLiteral()) {
            if(token.isNumber() || token.isBoolean()) {
                return token;
            } else if(token.isString() && hashMap.containsKey(token.getString())) {
                return hashMap.get(token.getString());
            }
            return token;
        }

        ArrayList<Double> numberArguments = new ArrayList<>();
        ArrayList<Token> lispArguments = new ArrayList<>();
        Evaluator stackEvaluator;
        Operator operator;
        Token result =  new Token("NIL");

        if(!token.isTokenList()) {
            stack.add(token);
        } else {
            //Reverse the  a copy of the token list to have operator be the first thing popped in the stack.
            ArrayList<Token> reversedTokens = new ArrayList<>(token.getTokenList());
            Collections.reverse(reversedTokens);
            this.stack.addAll(reversedTokens);
        }

        if(stack.empty()) {
            result = new Token("NIL");
            return result;
        }

        Token top = stack.peek();

        if(top.getOperator() == null) {
            throw new Exception(("(ERROR: " + top.toTokenString() + "is not a function name; try using a symbol instead)"));
        }

        operator = stack.pop().getOperator();

        if(stack.empty()) {
            throw new Exception("(ERROR: Operator called with no arguments)");
        }

        /*
        Recursive Step:
        If the token after the operator is a list, replace
        the top of the stack with the evaluated token list
        */

        if (!stack.empty()) {

            for(int i = stack.size() - 1; i >= 0; i--) {

                 if(stack.elementAt(i).isTokenList()) {
                    stackEvaluator = new Evaluator((stack.get(i)));
                    stack.set(i, stackEvaluator.evaluate(stack.get(i), hashMap));
                }
            }

            switch (operator.getType()) {
                case "arithmetic":

                    if (operator.equals(Operator.SQRT)) {

                        //When a non-mumerical atom is found, check for a hashmap value and replace the variable.
                        if (!stack.peek().isNumber() && hashMap.containsKey(stack.peek().getString())) {
                            stack.set(0, checkHashMap(stack.peek(), hashMap));
                        }

                        if (!stack.peek().isNumber() || stack.size() != 1 || stack.empty()) {
                            throw new Exception("(ERROR: One argument required for sqrt)");
                        } else {
                            numberArguments.add(stack.pop().getNumber());
                            result = operator.evalArithmetic(numberArguments);
                        }
                        return result;

                    } else if (operator.equals(Operator.POW)) {

                        for (int i = 0; i < stack.size(); i++) {
                            if (!stack.get(i).isNumber() && hashMap.containsKey(stack.get(i).getString())) {
                                stack.set(i, checkHashMap(stack.get(i), hashMap));
                            }
                        }

                        if (!stack.peek().isNumber() && hashMap.containsKey(stack.peek().getString())) {
                            stack.set(0, checkHashMap(stack.peek(), hashMap));
                        }

                        if (stack.empty() || stack.size() != 2 ||
                                !(stack.elementAt(0).isNumber() && stack.elementAt(1).isNumber())) {
                            throw new Exception("(ERROR: Two arguments required for pow)");
                        } else {
                            numberArguments.add(stack.pop().getNumber());
                            numberArguments.add(stack.pop().getNumber());
                            result = operator.evalArithmetic(numberArguments);
                        }
                        return result;

                    } else {

                        for (int i = 0; i < stack.size(); i++) {
                            if (!stack.get(i).isNumber() && hashMap.containsKey(stack.get(i).getString())) {
                                stack.set(i, checkHashMap(stack.get(i), hashMap));
                            }
                        }
                        while (!stack.empty()) {

                            if (!stack.peek().isNumber()) {
                                throw new Exception("(ERROR: " + stack.peek().toTokenString() + "is not a number)");
                            }
                            numberArguments.add(stack.pop().getNumber());
                        }

                        result = operator.evalArithmetic(numberArguments);
                        return result;
                    }

                case "boolean":


                    if (operator.equals(Operator.AND)) {

                        for (Token booleanToken : stack) {
                            if (!booleanToken.getBoolean()) {
                                return new Token(false);
                            }
                        }
                        return new Token(true);

                    } else if (operator.equals(Operator.OR)) {

                        for (Token booleanToken : stack) {
                            if (booleanToken.getBoolean()) {
                                return new Token(true);
                            }
                        }
                        return new Token(false);

                    } else if (operator.equals(Operator.NOT)) {
                        if (stack.peek().getBoolean().equals(false)) {
                            return new Token(true);
                        } else {
                            return new Token(false);
                        }
                    }

                    for (int i = 0; i < stack.size(); i++) {
                        if (!stack.get(i).isNumber() && hashMap.containsKey(stack.get(i).getString())) {
                            stack.set(i, checkHashMap(stack.get(i), hashMap));
                        }
                    }

                    while (!stack.empty() && stack.peek().isNumber()) {
                        numberArguments.add(stack.pop().getNumber());
                    }

                    result = operator.evalBool(numberArguments);
                    return result;
                case "lisp":
                    if (operator.equals(Operator.CAR) || operator.equals(Operator.CDR)) {
                        //check hashmap for any matches
                        for (int i = 0; i < stack.size(); i++) {
                            if (hashMap.containsKey(stack.get(i).getString())) {
                                stack.set(i, checkHashMap(stack.get(i), hashMap));
                            }
                        }

                        if (!stack.peek().isTokenList() || !stack.peek().isLiteral()) {

                            throw new Exception("(ERROR: car/cdr must be called with list argument)");
                        } else {
                            lispArguments.add(stack.pop());

                            result = operator.evalLisp(lispArguments);
                        }
                    } else if (operator.equals(Operator.CONS)) {

                        for (int i = 0; i < stack.size(); i++) {
                            if (hashMap.containsKey(stack.get(i).getString())) {
                                stack.set(i, checkHashMap(stack.get(i), hashMap));
                            }
                        }

                        if (stack.size() != 2 || !(stack.elementAt(0).isLiteral() && stack.elementAt(1).isLiteral())) {
                            throw new Exception("(ERROR: cons must be called with two arguments; (cons atom/list list))");
                        } else {
                            lispArguments.add(stack.pop());
                            lispArguments.add(stack.pop());
                            result = operator.evalLisp(lispArguments);
                        }
                    } else if (operator.equals(Operator.IF)) {

                        if (stack.size() != 3 || !stack.peek().isBoolean()) {
                            throw new Exception("(ERROR: Invalid If call; required form: (if (boolean) (true statement) (false statement))");
                        } else {
                            lispArguments.add(stack.pop());
                            lispArguments.add(stack.pop());
                            lispArguments.add(stack.pop());
                            result = operator.evalLisp(lispArguments);
                        }
                    } else if (operator.equals(Operator.DEFINE) || operator.equals(Operator.SET)) {
                        if (!stack.peek().isString() || !stack.get(1).isLiteral()) {
                            throw new Exception("(ERROR: Ensure form: var exp)");
                        } else {
                            String variable = stack.pop().getString();
                            Token value = stack.pop();
                            hashMap.put(variable, value);
                            result = value;
                        }
                    }
                    break;
            }
        }

    return result;
    }

    public Token checkHashMap(Token stringToken, HashMap<String, Token> hashMap) {

        if(hashMap.containsKey(stringToken.getString())) {
            return hashMap.get(stringToken.getString());
        }

        return null;
    }

}
