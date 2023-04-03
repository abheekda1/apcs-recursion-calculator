public class App {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("2 + (2 + 2) - (3 * 3) / 3");

        Calculator calculator = new Calculator(lexer);

        System.out.println(calculator.evalute());
    }
}