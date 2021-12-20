import java.util.ArrayList;

/**
 * The token class' purpose is to create each type of input object type
 * with their specific parameters and usages. Types of tokens include:
 * number, boolean, operator, string, tokenlist tokens
 */
public class Token  {

    private Double number;
    private Boolean bool;
    private Operator operator;
    private String string;
    private ArrayList<Token> tokenList;
    private Boolean valid;
    private Boolean literal;

    // get() methods
    public double getNumber() {
        return number;
    }
    public Operator getOperator() {
        return operator;
    }
    public String getString() { return string; }
    public Boolean getBoolean() {return this.bool; }
    public ArrayList<Token> getTokenList() { return tokenList; }

    public String getBooleanString() {
        String boolString = Boolean.toString(this.bool);
        if(boolString.equals("true")) {
            return ("T");
        } else {
            return "NIL";
        }
    }

    // set() methods
    public void setValid(Boolean valid) { this.valid = valid; }
    public void setLiteral(Boolean literal) { this.literal = literal; }

    // is() methods
    public boolean isOperator() { return this.operator != null; }
    public boolean isNumber() { return this.number != null; }
    public boolean isString() { return this.string != null; }
    public boolean isBoolean() { return  this.bool != null; }
    public boolean isTokenList() { return this.tokenList != null; }
    public boolean isValid() { return this.valid; }
    public boolean isLiteral() { return this.literal; }

    //Constructors
    public Token(double number) {
        this.number = number;
        this.operator = null;
        this.string = null;
        this.literal = true;
        this.bool = null;
        this.valid = true;
    }

    public Token(Operator operator) {
        this.operator = operator;
        this.number = null;
        this.string = null;
        this.literal = null;
        this.bool = null;
        this.valid = true;
    }

    public Token(String string) {
        this.string = string;
        this.operator = null;
        this.number = null;
        this.literal = true;
        this.bool = null;
        this.valid = true;
    }

    public Token(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
        this.operator = null;
        this.string = null;
        this.number = null;
        this.literal = false;
        this.bool = null;
        this.valid = true;

    }

    public Token(Boolean bool) {
        this.tokenList = null;
        this.operator = null;
        this.string = null;
        this.number = null;
        this.literal = true;
        this.bool = bool;
        this.valid = true;
    }

    /**
     * Create a string representation of the given token
     * @return string representative of the token value
     */
    public String toTokenString() {

        String string = "";

        if(this.number != null) {
            double dub = this.number;
            Integer integer = (int) dub;

            if (dub % integer == 0) {
                string = string + integer.toString() + " ";
                return string;
            }
        }

        if (this.isString()) {
            string = string + this.string + " ";
        } else if(this.isNumber()) {
            string = string + this.number.toString() + " ";
        } else if(this.isBoolean()) {
            string = string + this.getBooleanString() + " ";
        } else if(this.isTokenList()) {
            string = string + "( ";
            for (Token token : this.tokenList) {
                string += token.toTokenString();
            }
            string = string + ") ";
        } else {
            string = string + this.operator.toString() + " ";
        }
        return string;
    }

    /**
     * Print the type of a given token
     */
    public void printTokenType() {

        if (this.isString()) {
            System.out.print("String, ");
        } else if(this.isNumber()) {
            System.out.print("number, ");
        } else if(this.isTokenList()) {
            System.out.print("(");
            for (Token token : this.tokenList) {
                token.printTokenType();
            }
            System.out.print(")");
        } else {
            System.out.print("Operator, ");
        }
    }

    /**
     * Turn a string into a corresponding token type (String, number, operator etc)
     * @param listItem the string object to convert to token form
     * @return the created token object
     */
    public static Token parseToken(String listItem) {
        Token token;

        // Check if the given list item is an operator
        for (Operator operator_set : Operator.values()) {

            if (listItem.equals(operator_set.toString())) {
                token = new Token(operator_set);
                return token;
            }

        }

        // If not an operator,  try to parse the item as a double.
        try {
            token = new Token(Double.parseDouble(listItem));
            return token;
        } catch (NumberFormatException e) {
            // Since the item is not a operator or a number, parse as string.
            if(listItem.equals("T")) {
                token = new Token(true);
                return token;
            } else if(listItem.equals("NIL")) {
                token = new Token(false);
                return token;
            }
            token = new Token(listItem);
            return token;
        }
    }

    /**
     * Turn a list of strings into a list of token objects using recursion
     * @param stringList the list of strings to convert to token form
     * @return A token holding a list of tokens
     */
    public static Token parseTokenList(ArrayList<String> stringList) {

        Token expression;
        ArrayList<Token> tokens = new ArrayList<>();

        if (stringList.size() ==  0) {
            System.out.print("Parse error: unexpected end of string");
            return null;
        }

        // After getting the first list element, remove it from the list.
        String token = stringList.get(0);
        stringList.remove(0);


        if (token.equals("'") && stringList.get(0).equals("(")) {
            stringList.remove(0);
            while(!stringList.get(0).equals(")")) {
                tokens.add(parseLiteralTokenList(stringList));
            }

            // Remove right parenthesis since post-while-loop
            stringList.remove(0);

            expression = new Token(tokens);
            expression.setLiteral(true);
            return expression;

        } else if(token.equals("'")){
            // Parse single token since the element is not a list of tokens
            return parseLiteralToken(stringList.get(0));

        } else if (token.equals("(")) {

            while(!stringList.get(0).equals(")")) {
                tokens.add(parseTokenList(stringList));
            }

            // Remove right parenthesis since post-while-loop
            stringList.remove(0);

            expression = new Token(tokens);
            return expression;

        } else {
            // Parse single token since the element is not a list of tokens
            return parseToken(token);
        }
    }

    /**
     * Turn a string into a corresponding literal token type (String, number, operator etc)
     * @param listItem the string object to convert to token form
     * @return the created literal token object
     */
    public static Token parseLiteralToken(String listItem) {
        Token token;
        token = new Token(listItem);
        token.setLiteral(true);
        return token;
    }

    /**
     * Turn a list of strings into a list of literal token objects using recursion
     * @param stringList the list of strings to convert to literal token form
     * @return A token holding a list of literal tokens
     */
    private static Token parseLiteralTokenList(ArrayList<String> stringList) {

        Token expression;
        ArrayList<Token> tokens = new ArrayList<>();

        if (stringList.size() ==  0) {
            System.out.print("Parse error: unexpected end of string");
            return null;
        }

        // After getting the first list element, remove it from the list.
        String token = stringList.get(0);
        stringList.remove(0);

        if (token.equals("(")) {

            while(!stringList.get(0).equals(")")) {
                tokens.add(parseLiteralTokenList(stringList));
            }

            // Remove right parenthesis since post-while-loop
            stringList.remove(0);

            expression = new Token(tokens);
            return expression;

        } else {
            // Parse single literal token since the element is not a list of tokens
            return parseLiteralToken(token);
        }


    }

}
