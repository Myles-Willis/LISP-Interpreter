import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The Interpreteer class holds the main function for the program.
 * This is where the program takes user input and outputs the results of each expression
 */
public class Interpreter {

    public static void main(String[] args) throws Throwable {

        String Expression = null;
        Scanner in = new Scanner(System.in);
        Token result;
        HashMap<String, Token> hashMap = new HashMap<>();
        boolean validInput;


        try {
            File outputFile = new File("results.file");
            outputFile.createNewFile();
        } catch (IOException e ) {
            e.printStackTrace();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("results.file"));


        System.out.println("\n\n\nWelcome to the fancy new Prompt LISP INTERPRETER, type in LISP commands!");
        writer.write("\n\n\nWelcome to the fancy new Prompt LISP INTERPRETER, type in LISP commands!\n\n");
        System.out.print(">");
        writer.write(">");

        // Take user input in a loop until quit command is written
        while(in.hasNextLine()) {
            Expression = in.nextLine();
            writer.write(Expression + "\n");

            if(Expression.equals("(quit)")) {
                break;
            }
            Parser parser = new Parser(Expression);
            validInput = parser.parseInput();


            if(!validInput) {
                System.out.println("(Error: Invalid parenthesis; Re-enter expression)");
                writer.write("(Error: Invalid parenthesis; Re-enter expression)\n");
            } else {
                Evaluator evaluator = new Evaluator(parser.getExpression());
                try {
                    evaluator.setResult(evaluator.evaluate(parser.getExpression(), hashMap));
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    writer.write(exception.getMessage() + "\n");
                    System.out.print(">");
                    writer.write(">" + "\n");
                    continue;
                }
                result = evaluator.getResult();

                System.out.println(result.toTokenString());
                writer.write(result.toTokenString() + "\n");

            }
            System.out.print(">");
            writer.write(">" + "\n");
        }
        writer.close();
        in.close();
    }
}

