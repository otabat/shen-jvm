package com.shenjvm;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

public class KLReader {
    public static ArrayList<Object> read(Reader r) {
        return readTokens(new Scanner(r).useDelimiter("(\\s|\\)|\")"));
    }

    public static ArrayList<Object> readTokens(Scanner sc) {
       ArrayList<Object> list = new ArrayList<Object>();
       while (true) {
           Object token = readToken(sc);
           if (token == null)
               break;
           list.add(token);
       }
      return (list.isEmpty()) ? KL.EMPTY_LIST : list;
    }

    public static Object readToken(Scanner sc) {
        skipWhitespaces(sc);
        if (sc.findWithinHorizon("\\(", 1) != null)
            return readTokens(sc);
        if (sc.findWithinHorizon("\\)", 1) != null)
         return null;
        if (sc.findWithinHorizon("\"", 1) != null)
         return readString(sc);
        if (sc.hasNextLong())
         return sc.nextLong();
        if (sc.hasNextDouble())
         return sc.nextDouble();
        if (sc.hasNext())
         return KLPrimitive.intern(sc.next());
        return null;
    }

    public static void skipWhitespaces(Scanner sc) {
        sc.skip("\\s*");
    }

    public static Object readString(Scanner sc) {
        String s = sc.findWithinHorizon("(?s).*?\"", 0);
       return s.substring(0, s.length() - 1);
    }
}