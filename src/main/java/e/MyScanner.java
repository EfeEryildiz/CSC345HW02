package e;

import java.io.IOException;
import java.io.PushbackReader;

public class MyScanner {
    public enum TOKEN {
        SCANEOF, ID, INTLITERAL, INTDATATYPE, DECLARE, PRINT, SET, EQUALS, IF, THEN, ENDIF, CALC, PLUS
    }

    private PushbackReader reader;
    private StringBuilder tokenBuffer;
    private String[] reservedWords = {"declare", "int", "print", "set", "if", "then", "endif", "calc"};

    public MyScanner(PushbackReader reader) {
        this.reader = reader;
        this.tokenBuffer = new StringBuilder();
    }

    public TOKEN scan() throws IOException {
        tokenBuffer.setLength(0); // Clear the buffer before each scan
        int c = reader.read();

        // Skip whitespace
        while (c != -1 && Character.isWhitespace(c)) {
            c = reader.read();
        }

        if (c == -1) {
            return TOKEN.SCANEOF;
        }

        if (c == '+') {
            tokenBuffer.append((char) c);
            return TOKEN.PLUS;
        } else if (c == '=') {
            tokenBuffer.append((char) c);
            return TOKEN.EQUALS;
        } else if (Character.isDigit(c)) {
            do {
                tokenBuffer.append((char) c);
                c = reader.read();
            } while (Character.isDigit(c));
            if (c != -1) {
                reader.unread(c);
            }
            return TOKEN.INTLITERAL;
        } else if (Character.isLetter(c)) {
            do {
                tokenBuffer.append((char) c);
                c = reader.read();
            } while (Character.isLetter(c));
            if (c != -1) {
                reader.unread(c);
            }

            String word = tokenBuffer.toString();
            for (String reservedWord : reservedWords) {
                if (word.equals(reservedWord)) {
                    return TOKEN.valueOf(word.toUpperCase());
                }
            }
            return TOKEN.ID;
        }

        throw new IOException("Unexpected character: " + (char) c);
    }

    public String getTokenBufferString() {
        return tokenBuffer.toString();
    }
}
