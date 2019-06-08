// Equation.java
// Represents an equation

/*
    Keeps an array of all chars used in the equation.
    Order must be set and un touched.
    Keeps track of indexes in chars array that can not be zero (see known rules in documentation)
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Equation {
    private final Character[] chars;
    private String Expression1;
    private String ExpressionOperator;
    private String Expression2;
    private String ExpressionResult;
    private final String equationRegEx = "^([A-Z]+)([-+*])([A-Z]+)=([A-Z]+)$";
    public static ArrayList<Integer> indexesThatCantBeZero;


    public Equation(String equation) {
        indexesThatCantBeZero = new ArrayList<>();
        if (!validateSyntax(equation))
            throw new IllegalArgumentException();
        this.chars = extractLetters(equation);
        if (chars.length > MMN12.MAX_NUMBER_OF_LETTERS)
            throw new IllegalArgumentException();
        extractExpressions(equation);
    }

    public long translateResult(Solution sol) {
        // Translates the result string according to a specific solution
        return Long.parseLong(translateToNumbers(ExpressionResult, sol));
    }

    public String printFinalAnswer(Solution sol) {
        // Prints final answer as requested
        return translateToNumbers(Expression1, sol) + ExpressionOperator + translateToNumbers(Expression2, sol) + "=" + translateToNumbers(ExpressionResult, sol);
    }

    public long getDifferenceBetweenTranslatedAndCalculatedResults(Solution sol) {
        // Returns the difference between the calculated result and the translated one
        // Used in Solution for fitness
        return Math.abs(calculateResult(sol) - translateResult(sol));
    }

    public Character[] getChars() {
        return this.chars;
    }

    public long calculateResult(Solution sol) {
        // Translates first two expressions of equation, and returns the numeric result according to the operator
        long expression1 = Long.parseLong(translateToNumbers(Expression1, sol));
        long expression2 = Long.parseLong(translateToNumbers(Expression2, sol));
        long toReturn;
        switch (ExpressionOperator) {
            case "+": toReturn = expression1 + expression2; break;
            case "-": toReturn = expression1 - expression2; break;
            default: toReturn = expression1 * expression2; break;
        }
        return toReturn;
    }

    private void extractExpressions(String equation) {
        // Extracts appropriate variables from the expression
        // Keeps indexes of numbers that can not be zero (see known rule in documentation)
        Pattern equationPattern = Pattern.compile(equationRegEx);
        Matcher m = equationPattern.matcher(equation);
        if (m.find()) {
            Expression1 = m.group(1);
            ExpressionOperator = m.group(2);
            Expression2 = m.group(3);
            ExpressionResult = m.group(4);
        }
        indexesThatCantBeZero.add(0);
        indexesThatCantBeZero.add(Arrays.asList(chars).indexOf(Expression2.toCharArray()[0]));
        indexesThatCantBeZero.add(Arrays.asList(chars).indexOf(ExpressionResult.toCharArray()[0]));
    }

    private Character[] extractLetters(String equation) {
        // Extracts letters by order from equation
        // Every letter appears once and there is a limit
        ArrayList<Character> chars = new ArrayList<>();
        for (Character currentChar : equation.toCharArray()) {
            if (Character.isLetter(currentChar) && !chars.contains(currentChar))
                chars.add(currentChar);
        }
        Character[] charArray = new Character[chars.size()];
        charArray = chars.toArray(charArray);
        return charArray;
    }

    private boolean validateSyntax(String equation) {
        return equation.matches(equationRegEx);
    }

    private String translateToNumbers(String exp, Solution sol) {
        // Translates an alphabet expression to numbers according to a specific solution.
        String tempString = "";
        for (char c : exp.toCharArray()) {
            int index = Arrays.asList(chars).indexOf(c);
            tempString = tempString + sol.getNumbersArray()[index];
        }
        return tempString;
    }


}

