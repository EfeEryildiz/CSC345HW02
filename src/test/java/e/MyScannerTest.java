package e;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

public class MyScannerTest {

    @Test
    void testScanIdentifier() throws IOException {
        MyScanner scanner = new MyScanner(new PushbackReader(new StringReader("abc")));
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals("abc", scanner.getTokenBufferString());
    }

    @Test
    void testScanInteger() throws IOException {
        MyScanner scanner = new MyScanner(new PushbackReader(new StringReader("123")));
        assertEquals(MyScanner.TOKEN.INTLITERAL, scanner.scan());
        assertEquals("123", scanner.getTokenBufferString());
    }

    @Test
    void testScanReservedWords() throws IOException {
        String[] reservedWords = {"declare", "int", "print", "set", "if", "then", "endif", "calc"};
        for (String word : reservedWords) {
            MyScanner scanner = new MyScanner(new PushbackReader(new StringReader(word)));
            assertEquals(MyScanner.TOKEN.valueOf(word.toUpperCase()), scanner.scan());
            assertEquals(word, scanner.getTokenBufferString());
        }
    }

    @Test
    void testScanOperators() throws IOException {
        MyScanner scanner = new MyScanner(new PushbackReader(new StringReader("+")));
        assertEquals(MyScanner.TOKEN.PLUS, scanner.scan());
        assertEquals("+", scanner.getTokenBufferString());

        scanner = new MyScanner(new PushbackReader(new StringReader("=")));
        assertEquals(MyScanner.TOKEN.EQUALS, scanner.scan());
        assertEquals("=", scanner.getTokenBufferString());
    }

    @Test
    void testScanMultipleTokens() throws IOException {
        MyScanner scanner = new MyScanner(new PushbackReader(new StringReader("abc 123 + =")));
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals("abc", scanner.getTokenBufferString());
        assertEquals(MyScanner.TOKEN.INTLITERAL, scanner.scan());
        assertEquals("123", scanner.getTokenBufferString());
        assertEquals(MyScanner.TOKEN.PLUS, scanner.scan());
        assertEquals("+", scanner.getTokenBufferString());
        assertEquals(MyScanner.TOKEN.EQUALS, scanner.scan());
        assertEquals("=", scanner.getTokenBufferString());
    }

    @Test
    void testScanWithWhitespace() throws IOException {
        MyScanner scanner = new MyScanner(new PushbackReader(new StringReader("  abc  \n  123  ")));
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals("abc", scanner.getTokenBufferString());
        assertEquals(MyScanner.TOKEN.INTLITERAL, scanner.scan());
        assertEquals("123", scanner.getTokenBufferString());
    }

    @Test
    void testScanEOF() throws IOException {
        MyScanner scanner = new MyScanner(new PushbackReader(new StringReader("")));
        assertEquals(MyScanner.TOKEN.SCANEOF, scanner.scan());
    }

    @Test
    void testScanInvalidCharacter() {
        MyScanner scanner = new MyScanner(new PushbackReader(new StringReader("@")));
        assertThrows(IOException.class, scanner::scan);
    }

    @Test
    void testScanComplexInput() throws IOException {
        String input = "declare x int\nset x = 5\nif x = 5 then\nprint x\nendif";
        MyScanner scanner = new MyScanner(new PushbackReader(new StringReader(input)));

        assertEquals(MyScanner.TOKEN.DECLARE, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.INTDATATYPE, scanner.scan());
        assertEquals(MyScanner.TOKEN.SET, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(MyScanner.TOKEN.INTLITERAL, scanner.scan());
        assertEquals(MyScanner.TOKEN.IF, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(MyScanner.TOKEN.INTLITERAL, scanner.scan());
        assertEquals(MyScanner.TOKEN.THEN, scanner.scan());
        assertEquals(MyScanner.TOKEN.PRINT, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.ENDIF, scanner.scan());
        assertEquals(MyScanner.TOKEN.SCANEOF, scanner.scan());
    }
}