import java.util.ArrayList;

public class Calculator {
    private Lexer lexer;

    public Calculator(Lexer lexer) {
        this.lexer = lexer;
    }

    // evaluate the expression in the lexer property
    public int evalute() {
        // while it has parentheses handle them
        while (hasParens(lexer)) {
            handleParens(lexer);
        }

        // while it has * or / handle them
        while (hasMultDiv(lexer)) {
            handleMultDiv(lexer);
        }

        // while it has + or - handle them
        while (hasAddSub(lexer)) {
            handleAddSub(lexer);
        }

        if (lexer.tokens.get(1).type != Lexer.Token.Type.EOF) throw new RuntimeException("Failed to parse");

        // return the final singular token evaluated as an integer
        return Integer.parseInt(lexer.tokens.get(0).value);
    }

    // evaluate the expression in the lexer parameter
    public int evalute(Lexer lexer) {
        while (hasParens(lexer)) {
            handleParens(lexer);
        }

        while (hasMultDiv(lexer)) {
            handleMultDiv(lexer);
        }

        while (hasAddSub(lexer)) {
            handleAddSub(lexer);
        }

        return Integer.parseInt(lexer.tokens.get(0).value);
    }

    // clear all null tokens to prevent gaps
    private void clearNull(Lexer lexer) {
        ArrayList<Lexer.Token> cleared = new ArrayList<Lexer.Token>();

        // loop through tokens and only add non-null ones to "cleared"
        for (final Lexer.Token t : lexer.tokens) {
            if (t != null)
                cleared.add(t);
        }

        lexer.tokens = cleared;
    }

    // functions to check whether it has a certain type of token still in the lexer
    private boolean hasParens(Lexer lexer) {
        // loop through tokens
        for (final Lexer.Token t : lexer.tokens) {
            // return true if it is there
            if (t.type == Lexer.Token.Type.OPEN_PAREN || t.type == Lexer.Token.Type.CLOSE_PAREN)
                return true;
        }

        // return false if the loop completes without returning
        return false;
    }

    private boolean hasAddSub(Lexer lexer) {
        for (final Lexer.Token t : lexer.tokens) {
            if (t.type == Lexer.Token.Type.ADD || t.type == Lexer.Token.Type.SUBTRACT)
                return true;
        }

        return false;
    }

    private boolean hasMultDiv(Lexer lexer) {
        for (final Lexer.Token t : lexer.tokens) {
            if (t.type == Lexer.Token.Type.MULTIPLY || t.type == Lexer.Token.Type.DIVIDE)
                return true;
        }

        return false;
    }

    // function to handle addition and subtraction
    private void handleAddSub(Lexer lexer) {
        // loop through tokens, skipping the first one since it can't be an operator
        for (int i = 1; i < lexer.tokens.size(); i++) {
            // get operator, left, and right side
            Lexer.Token t = lexer.tokens.get(i);
            Lexer.Token lhs = lexer.tokens.get(i - 1);
            Lexer.Token rhs = lexer.tokens.get(i + 1);
            // todo: check if they are numbers
            if (t.type == Lexer.Token.Type.ADD) {
                if (lhs.type != Lexer.Token.Type.NUMBER ||  lhs.type != Lexer.Token.Type.NUMBER) throw new RuntimeException("Invalid expression");
                // if addition

                // turn this token into the sum of the lhs and rhs
                t.type = Lexer.Token.Type.NUMBER;
                t.value = Integer.toString(Integer.parseInt(lhs.value) + Integer.parseInt(rhs.value));

                // make lhs and rhs null to be cleared
                lexer.tokens.set(i - 1, null);
                lexer.tokens.set(i + 1, null);

                // clear null
                clearNull(lexer);

                return;
            } else if (t.type == Lexer.Token.Type.SUBTRACT) {
                if (lhs.type != Lexer.Token.Type.NUMBER ||  lhs.type != Lexer.Token.Type.NUMBER) throw new RuntimeException("Invalid expression");
                // if subtraction

                // turn this token into the difference between the lhs and rhs
                t.type = Lexer.Token.Type.NUMBER;
                t.value = Integer.toString(Integer.parseInt(lhs.value) - Integer.parseInt(rhs.value));

                // make lhs and rhs null to be cleared
                lexer.tokens.set(i - 1, null);
                lexer.tokens.set(i + 1, null);

                // clear null
                clearNull(lexer);

                return;
            }
        }
    }

    private void handleMultDiv(Lexer lexer) {
        for (int i = 1; i < lexer.tokens.size(); i++) {
            Lexer.Token t = lexer.tokens.get(i);
            Lexer.Token lhs = lexer.tokens.get(i - 1);
            Lexer.Token rhs = lexer.tokens.get(i + 1);
            // todo: check if they are numbers
            if (t.type == Lexer.Token.Type.MULTIPLY) {
                if (lhs.type != Lexer.Token.Type.NUMBER ||  lhs.type != Lexer.Token.Type.NUMBER) throw new RuntimeException("Invalid expression");
                t.type = Lexer.Token.Type.NUMBER;
                t.value = Integer.toString(Integer.parseInt(lhs.value) * Integer.parseInt(rhs.value));

                lexer.tokens.set(i - 1, null);
                lexer.tokens.set(i + 1, null);

                clearNull(lexer);

                return;
            } else if (t.type == Lexer.Token.Type.DIVIDE) {
                if (lhs.type != Lexer.Token.Type.NUMBER ||  lhs.type != Lexer.Token.Type.NUMBER) throw new RuntimeException("Invalid expression");
                t.type = Lexer.Token.Type.NUMBER;
                t.value = Integer.toString(Integer.parseInt(lhs.value) / Integer.parseInt(rhs.value));

                lexer.tokens.set(i - 1, null);
                lexer.tokens.set(i + 1, null);

                clearNull(lexer);

                return;
            }
        }
    }

    private void handleParens(Lexer lexer) {
        // set arbitrary start and end idxs for parenthesis expr
        int startIdx = 0;
        int endIdx = lexer.tokens.size();

        // depth is how many parentheses we are currently within
        int depth = 0;

        for (int i = startIdx; i < endIdx; i++) {
            // first parenthesis
            if (depth == 0 && lexer.tokens.get(i).type == Lexer.Token.Type.OPEN_PAREN)
                startIdx = i;

            // matching parenthesis
            if (depth == 1 && lexer.tokens.get(i).type == Lexer.Token.Type.CLOSE_PAREN) {
                endIdx = i;

                // create new lexer for tokens within these two parens
                ArrayList<Lexer.Token> inParensTokens = new ArrayList<Lexer.Token>();
                for (int j = startIdx + 1; j < endIdx; j++)
                    inParensTokens.add(lexer.tokens.get(j));
                Lexer inParens = new Lexer(inParensTokens);

                // new expression to replace parens
                int newExpr = evalute(inParens);
                lexer.tokens.set(startIdx, lexer.new Token(Integer.toString(newExpr), Lexer.Token.Type.NUMBER));
                
                // make rest of the inside of parentheses (including parentheses) null and clear them
                for (int j = startIdx + 1; j <= endIdx; j++)
                    lexer.tokens.set(j, null);
                clearNull(lexer);

                return;
            }

            // increment and decrement depth based on current location
            if (lexer.tokens.get(i).type == Lexer.Token.Type.OPEN_PAREN)
                depth++;
            if (lexer.tokens.get(i).type == Lexer.Token.Type.CLOSE_PAREN)
                depth--;
        }
    }
}
