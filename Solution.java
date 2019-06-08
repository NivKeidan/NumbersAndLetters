// Solution.java
// Represents a solution


import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;

public class Solution {

    private int[] numbers;
    private ArrayList<Integer> freeNumbers;  // Keeps track of the un-used numbers
    private long fitnessScore;

    public Solution(Equation equation) {
        // Creates a completely random solution
        fitnessScore = 0;
        Character[] chars = equation.getChars();
        numbers = new int[chars.length];
        initializeFreeNumbersArray();
        for (int i = 0; i < numbers.length; i++) {
            if (!setNumber(i, getFreeNumber())) {
                initializeFreeNumbersArray();
                i = -1;
            }
        }
    }

    public Solution(Solution solutionFather, Solution solutionMother) {
        // Create an offspring solution and mutates

        // Basic initialization
        initializeFreeNumbersArray();
        fitnessScore = 0;
        numbers = new int[solutionFather.getNumbersArray().length];

        // Choose splitting point (see documentation)
        Random rand = new Random();
        int splitIndex = rand.nextInt(numbers.length);

        // Take parent's allele (see documentation)
        boolean inheritFromFather = rand.nextBoolean();
        for (int i = 0; i < numbers.length; i++) {
            if (i == splitIndex)
                inheritFromFather = !inheritFromFather;
            if (inheritFromFather) {
                if (!this.setNumber(i, solutionFather.getNumber(i))) {
                    initializeFreeNumbersArray();
                    splitIndex = rand.nextInt(numbers.length);
                    inheritFromFather = rand.nextBoolean();
                    i = -1;
                }
            }
            else {
                if (!this.setNumber(i, solutionMother.getNumber(i))) {
                    initializeFreeNumbersArray();
                    splitIndex = rand.nextInt(numbers.length);
                    inheritFromFather = rand.nextBoolean();
                    i = -1;
                }
            }
        }

        // Mutate
        this.mutate();
    }

    public int[] getNumbersArray() {
        return numbers;
    }

    public ArrayList<Integer> getFreeNumbersArray() {
        return this.freeNumbers;
    }

    public void setFitnessScore(long score) {
        this.fitnessScore = score;
    }

    public long getFitnessScore() {
        return this.fitnessScore;
    }

    private boolean isNumberValid(int newNumber) {
        // Check if argument number is free to use
        return getFreeNumbersArray().contains(newNumber);
    }

    private void initializeFreeNumbersArray() {
        // Fills free numbers array with all numbers and shuffles
        freeNumbers = new ArrayList<>();
        for (int i = 0; i < MMN12.MAX_NUMBER_OF_LETTERS; i++)
                freeNumbers.add(i);
        Collections.shuffle(freeNumbers);
        }

    private int getFreeNumber() {
        // Get a number free to use
        return freeNumbers.get(0);
    }

    private int getNumber(int index) {
        // Get number in specific position
        if (index < numbers.length)
            return numbers[index];
        else
            throw new IndexOutOfBoundsException();
    }

    private boolean setNumber (int index, int number) {
        // Inserts the number according to index
        // Validates
        int numberToSet = number;
        if (!isNumberValid(numberToSet))
            numberToSet = getRandomClosestFreeNumber(number);
        if (numberToSet == 0 && Equation.indexesThatCantBeZero.contains(index))
            numberToSet = getRandomClosestFreeNumber(number);
        if (numberToSet < 0 || numberToSet > 9)
            return false;
        numbers[index] = numberToSet;
        removeFreeNumber(numberToSet);
        return true;
    }

    private void mutate() {
        // Performs mutation, read documentation for detailed explanation of process

        // Basic initialization
        Random rand = new Random();
        int indexToMutate;
        int newNumber;
        int previousNumber;

        if (rand.nextDouble() <= MMN12.MUTATION_PROBABILITY) { // Should mutate?
            do {
                // Get allele to mutate
                indexToMutate = rand.nextInt(numbers.length);
                previousNumber = numbers[indexToMutate];
                if (freeNumbers.size() == 0) { // Swap instead of mutate
                    int indexToSwap;
                    int previousNumberOfSwap;
                    addFreeNumber(numbers[indexToMutate]);
                    do {
                        indexToSwap = rand.nextInt(numbers.length);
                        previousNumberOfSwap = numbers[indexToSwap];
                    } while (!setNumber(indexToSwap, previousNumberOfSwap));
                    addFreeNumber(previousNumberOfSwap);
                } // Numbers array has duplicate now!
                newNumber = getRandomClosestFreeNumber(previousNumber);
            } while (!setNumber(indexToMutate, newNumber)); // Sets the number!
            if (!(freeNumbers.size() == 0)) // In case of swap and not mutation
                addFreeNumber(previousNumber);
        }
    }

    private int getRandomClosestFreeNumber(int number) {
        // Gets closest free number to argument.
        // Validates according to rules, see documentation for further inquiry
        Random rand = new Random();
        boolean startFromMinus = rand.nextBoolean(); // Randomly decide if to prefer higher or lower numbers
        for (int distance = 1; distance < 10; distance++) {
            for (int i = 0; i < 2; i++) {
                int numToCheck;
                if (startFromMinus)
                    numToCheck = number - distance;
                else
                    numToCheck = number + distance;

                if (isNumberValid(numToCheck))
                        return numToCheck;
                startFromMinus = !startFromMinus;
            }
        }
        return -1; // Code for no possible number
    }

    private void addFreeNumber(int num) {
        // Adds a number to the free numbers array
        freeNumbers.add(num);
        Collections.shuffle(freeNumbers);
    }

    private void removeFreeNumber(int num) {
        // Removes a number from the free numbers array
        // Validates
        if (!freeNumbers.remove(new Integer(num)))
            throw new NoSuchElementException();
    }

}