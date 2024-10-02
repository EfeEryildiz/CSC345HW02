package e;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class MyParser {
    public enum TYPE {
        INTDATATYPE
    }

    private static class SymbolTableItem {
        String name;
        TYPE type;

        SymbolTableItem(String name, TYPE type) {
            this.name = name;
            this.type = type;
        }
    }

    private Map<String, SymbolTableItem> symbolTable;
    private MyScanner scanner;
    private MyScanner.TOKEN nextToken;

    public MyParser() {
        this.symbolTable = new HashMap<>();
    }

    public boolean parse(String program) {
        scanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            nextToken = scanner.scan();
            return parseProgram();
        } catch (Exception e) {
            System.err.println("Parse error: " + e.getMessage());
            return false;
        }
    }

    private boolean match(MyScanner.TOKEN expectedToken) {
        if (nextToken == expectedToken) {
            System.out.println("Matched: " + expectedToken + " Buffer: " + scanner.getTokenBufferString());
            try {
                nextToken = scanner.scan();
                return true;
            } catch (Exception e) {
                System.err.println("Error scanning next token: " + e.getMessage());
                return false;
            }
        } else {
            System.err.println("Parse Error\nReceived: " + nextToken + " Buffer: " + scanner.getTokenBufferString() + "\nExpected: " + expectedToken);
            return false;
        }
    }

    private boolean parseProgram() {
        return parseDecls() && parseStmts() && match(MyScanner.TOKEN.SCANEOF);
    }

    private boolean parseDecls() {
        if (nextToken == MyScanner.TOKEN.DECLARE) {
            return parseDecl() && parseDecls();
        }
        return true; // Empty production
    }

    private boolean parseDecl() {
        if (match(MyScanner.TOKEN.DECLARE)) {
            String id = scanner.getTokenBufferString();
            if (match(MyScanner.TOKEN.ID)) {
                if (symbolTable.containsKey(id)) {
                    System.err.println("Error: Variable '" + id + "' is already declared.");
                    return false;
                }
                symbolTable.put(id, new SymbolTableItem(id, TYPE.INTDATATYPE));
                return true;
            }
        }
        return false;
    }

    private boolean parseStmts() {
        if (nextToken == MyScanner.TOKEN.PRINT || nextToken == MyScanner.TOKEN.SET ||
                nextToken == MyScanner.TOKEN.IF || nextToken == MyScanner.TOKEN.CALC) {
            return parseStmt() && parseStmts();
        }
        return true; // Empty production
    }

    private boolean parseStmt() {
        switch (nextToken) {
            case PRINT:
                return parsePrintStmt();
            case SET:
                return parseSetStmt();
            case IF:
                return parseIfStmt();
            case CALC:
                return parseCalcStmt();
            default:
                return false;
        }
    }

    private boolean parsePrintStmt() {
        return match(MyScanner.TOKEN.PRINT) && match(MyScanner.TOKEN.ID);
    }

    private boolean parseSetStmt() {
        return match(MyScanner.TOKEN.SET) && match(MyScanner.TOKEN.ID) &&
                match(MyScanner.TOKEN.EQUALS) && match(MyScanner.TOKEN.INTLITERAL);
    }

    private boolean parseIfStmt() {
        return match(MyScanner.TOKEN.IF) && match(MyScanner.TOKEN.ID) &&
                match(MyScanner.TOKEN.EQUALS) && match(MyScanner.TOKEN.ID) &&
                match(MyScanner.TOKEN.THEN) && parseStmts() && match(MyScanner.TOKEN.ENDIF);
    }

    private boolean parseCalcStmt() {
        return match(MyScanner.TOKEN.CALC) && match(MyScanner.TOKEN.ID) &&
                match(MyScanner.TOKEN.EQUALS) && parseSum();
    }

    private boolean parseSum() {
        return parseValue() && parseSumEnd();
    }

    private boolean parseSumEnd() {
        if (nextToken == MyScanner.TOKEN.PLUS) {
            return match(MyScanner.TOKEN.PLUS) && parseValue() && parseSumEnd();
        }
        return true; // Empty production
    }

    private boolean parseValue() {
        return match(MyScanner.TOKEN.ID) || match(MyScanner.TOKEN.INTLITERAL);
    }
}