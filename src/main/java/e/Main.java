package e;

public class Main {
    public static void main(String[] args) {
        MyParser parser = new MyParser();

        // Test with a syntactically correct program
        String correctProgram =
                "declare w int\n" +
                        "declare x int\n" +
                        "declare y int\n" +
                        "declare z int\n" +
                        "set w = 5\n" +
                        "set x = 10\n" +
                        "set y = 15\n" +
                        "set z = 20\n" +
                        "calc w = x + y + z\n" +
                        "if x = y then\n" +
                        "print w\n" +
                        "print x\n" +
                        "endif\n" +
                        "print y\n" +
                        "print z";

        System.out.println("Parsing correct program:");
        boolean result = parser.parse(correctProgram);
        System.out.println("Parse result: " + result);

        // Test with a syntactically incorrect program
        String incorrectProgram =
                "declare w int\n" +
                        "declare x int\n" +
                        "set w = 5\n" +
                        "set x = 10\n" +
                        "if w x then\n" +
                        "print w\n" +
                        "endif";

        System.out.println("\nParsing incorrect program:");
        result = parser.parse(incorrectProgram);
        System.out.println("Parse result: " + result);
    }
}