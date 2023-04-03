import java.util.ArrayList;

public class Lexer {
    // token class
    public class Token {
        // token type
        public enum Type {
            NUMBER,
            ADD,
            SUBTRACT,
            MINUS,
            MULTIPLY,
            DIVIDE,
            OPEN_PAREN,
            CLOSE_PAREN,
            EOF,
        }

        // token value and type
        public String value;
        public Type type;

        // ctor to create a token obj
        public Token(String value, Type type) {
            this.value = value;
            this.type = type;
        }
    }

    // tokens list and raw expr
    public ArrayList<Token> tokens = new ArrayList<Token>();
    String expression;

    // ctor that takes a string and lexes it to tokens
    public Lexer(String expression) {
        this.expression = expression;

        lex();
    }

    // ctor that just takes the direct tokens
    public Lexer(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    // take raw expr and convert it into a list of tokens
    public void lex() {
        // ptr keeps track of current loc
        int ptr = 0;

        // clear all whitespace
        expression = expression.replace(" ", "");

        while (ptr < expression.length()) {
            // create token depending on operator
            if (expression.charAt(ptr) == '(') tokens.add(new Token("(", Token.Type.OPEN_PAREN));
            if (expression.charAt(ptr) == ')') tokens.add(new Token(")", Token.Type.CLOSE_PAREN));
            if (expression.charAt(ptr) == '*') tokens.add(new Token("*", Token.Type.MULTIPLY));
            if (expression.charAt(ptr) == '/') tokens.add(new Token("/", Token.Type.DIVIDE));
            if (expression.charAt(ptr) == '+') tokens.add(new Token("+", Token.Type.ADD));
            if (expression.charAt(ptr) == '-') tokens.add(new Token("-", Token.Type.SUBTRACT));

            // create number token
            if (Character.isDigit(expression.charAt(ptr))) {
                int start = ptr;
                while (ptr < expression.length() && Character.isDigit(expression.charAt(ptr))) ptr++;
                tokens.add(new Token(expression.substring(start, ptr), Token.Type.NUMBER));
                // decrement ptr bc we've gone past end of num
                ptr--;
            }

            // increment to next char
            ptr++;
        }

        // add eof token to signal end of expr
        tokens.add(new Token("", Token.Type.EOF));
    }
}
