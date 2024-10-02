package e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyParserTest {

    private MyParser parser;

    @BeforeEach
    void setUp() {
        parser = new MyParser();
    }

    @Test
    void testCorrectProgram() {
        String program =
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

        assertTrue(parser.parse(program), "Correct program should parse successfully");
    }

    @Test
    void testIncorrectProgram() {
        String program =
                "declare w int\n" +
                        "declare x int\n" +
                        "set w = 5\n" +
                        "set x = 10\n" +
                        "if w x then\n" +
                        "print w\n" +
                        "endif";

        assertFalse(parser.parse(program), "Incorrect program should fail to parse");
    }

    @Test
    void testEmptyProgram() {
        String program = "";
        assertTrue(parser.parse(program), "Empty program should parse successfully");
    }

    @Test
    void testDuplicateDeclaration() {
        String program =
                "declare x int\n" +
                        "declare x int";

        assertFalse(parser.parse(program), "Duplicate declaration should fail to parse");
    }

    @Test
    void testMissingEndif() {
        String program =
                "declare x int\n" +
                        "set x = 5\n" +
                        "if x = 5 then\n" +
                        "print x";

        assertFalse(parser.parse(program), "Missing endif should fail to parse");
    }

    @Test
    void testComplexCalcStatement() {
        String program =
                "declare x int\n" +
                        "declare y int\n" +
                        "declare z int\n" +
                        "set x = 5\n" +
                        "set y = 10\n" +
                        "calc z = x + y + 15 + 20";

        assertTrue(parser.parse(program), "Complex calc statement should parse successfully");
    }

    @Test
    void testNestedIfStatements() {
        String program =
                "declare x int\n" +
                        "declare y int\n" +
                        "set x = 5\n" +
                        "set y = 10\n" +
                        "if x = 5 then\n" +
                        "  if y = 10 then\n" +
                        "    print x\n" +
                        "  endif\n" +
                        "endif";

        assertTrue(parser.parse(program), "Nested if statements should parse successfully");
    }

    @Test
    void testInvalidToken() {
        String program = "invalid token";
        assertFalse(parser.parse(program), "Invalid token should fail to parse");
    }
}