import java.util.ArrayList;
import java.util.Arrays;

/**
 * The parser class is responsible for turning the user inputted string
 * into a useable expression of tokens to be used by the evaluator class
 */
public class Parser {

    private String input;
    private ArrayList<String> stringList = new ArrayList<>();
    private Token expression;

    // Getters
    public Token getExpression() {
        return expression;
    }
    public ArrayList<String> getStringList() {
        return stringList;
    }

    //Setters
    public void setExpression(Token expression) {
        this.expression = expression;
    }
    public void setExpression(String expression) {
        input = expression;
    }
    public void setStringList(ArrayList<String> stringList) {
        this.stringList = stringList;
    }

    public Parser(String input) {
        this.input = input;
    }

    /**
     * This function delimits then sends user input to the parse token list function
     * @return true or false for parse success
     */
    public boolean parseInput() {

        String[] splitStrings;

        input = input.replaceAll("\\(", " " + "(" + " ");
        input = input.replaceAll("\\)", " " + ")" + " ");
        input = input.replaceAll("\\s+", " ");
        input = input.trim();

        splitStrings = input.split(" ");

        // Add the split strings in to the stringList ArrayList
        this.stringList.addAll(Arrays.asList(splitStrings));

        if (this.validateParenthesis()) {
            // Use parseTokenList() to turn user input in to tokenList Token expression
            this.expression = Token.parseTokenList((ArrayList<String>) stringList.clone());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check a given expression for matching parenthesis
     * @return boolean representing valid or invalid parenthesized expression
     */
    public Boolean validateParenthesis() {
        int leftParenthesis = 0;
        int rightParenthesis = 0;

        for (String s : this.stringList) {

            if(s.equals("(")) {
                leftParenthesis++;
            } else if(s.equals(")")) {
                rightParenthesis++;
            }
        }

        return (leftParenthesis == rightParenthesis);
    }
}
