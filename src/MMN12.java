// MMN12.java
// Main class for MMN 12

import java.util.Scanner;

public class MMN12 {
    private static final int NICHE_POPULATION_SIZE = 50;
    private static final int NICHE_NUMBER = 20;
    private static final int SUBPOPULATION_GENERATION_NUMBER = 25;
    private static final int POPULATION_GENERATION_NUMBER = 25;
    private static final int MAX_GENERATIONS = 100000;
    static double MUTATION_PROBABILITY = 0.5;
    static final int MAX_NUMBER_OF_LETTERS = 10;
    private static final int RESTART_EVERY_GENERATIONS = 100;

    public static void main(String[] args) {
        Equation equation;
        Solution[] population;
        Solution[] nextGenerationPopulation;
        Roulette roulette;
        int generationCounter;
        long bestFitness;
        Solution bestSolution;

        //Initialization
        equation = getEquation();
        generationCounter = 0;
        bestSolution = null;
        nextGenerationPopulation = new Solution[NICHE_NUMBER * NICHE_POPULATION_SIZE];
        population = createStartingPopulation(equation);

        do { // Main loop - new generation
            if (generationCounter % RESTART_EVERY_GENERATIONS == 0 && generationCounter > 0)  // Restart population
                population = createStartingPopulation(equation);
            int currentNicheSize;
            int currentNicheCounter;
            int currentNumOfNiches;
            bestFitness = Long.MAX_VALUE;
            generationCounter++;

            // Decide current niche
            if (generationCounter % (SUBPOPULATION_GENERATION_NUMBER + POPULATION_GENERATION_NUMBER) < SUBPOPULATION_GENERATION_NUMBER) {
                currentNumOfNiches = NICHE_NUMBER;
                currentNicheSize = NICHE_POPULATION_SIZE;
            } else { // Use full population
                currentNumOfNiches = 1;
                currentNicheSize = NICHE_POPULATION_SIZE * NICHE_NUMBER;
            }
            currentNicheCounter = 0;

            // Handle current niche
            for (int i = currentNicheCounter; i < currentNumOfNiches; i++) {
                long bestNicheFitness = Long.MAX_VALUE;
                Solution bestNicheSolution = null;
                int nicheStartIndex = currentNicheCounter * currentNicheSize;
                int nicheEndIndex = nicheStartIndex + currentNicheSize;
                long maxConstant = 0;

                // Calculate fitness, update best score, best solution and maximum constant
                for (int k = nicheStartIndex; k < nicheEndIndex; k++) {
                    Solution currentSolution = population[k];
                    long currentScore = equation.getDifferenceBetweenTranslatedAndCalculatedResults(currentSolution);
                    currentSolution.setFitnessScore(currentScore);
                    if (currentScore < bestNicheFitness) {
                        bestNicheFitness = currentScore;
                        bestNicheSolution = currentSolution;
                    }
                    if (currentScore > maxConstant)
                        maxConstant = currentScore;
                }

                // Create next generation
                roulette = new Roulette(population, nicheStartIndex, nicheEndIndex, maxConstant);
                for (int l = nicheStartIndex; l < nicheEndIndex - 1; l++)
                    nextGenerationPopulation[l] = roulette.getOffspringAndMutate();

                // Keep best solution to next generation
                nextGenerationPopulation[nicheEndIndex - 1] = bestNicheSolution;
                currentNicheCounter++;

                // Update global best solution and best fitness
                if (bestFitness > bestNicheFitness) {
                    bestFitness = bestNicheFitness;
                    bestSolution = bestNicheSolution;
                }
            } // End current niche
            population = nextGenerationPopulation.clone();
        } while (!(bestFitness == 0) && !(generationCounter > MAX_GENERATIONS)); // end of generation

        if (bestFitness != 0)
            System.out.println("No Solution Found!");
        else
            System.out.println(equation.printFinalAnswer(bestSolution));
    }

    private static Solution[] createStartingPopulation(Equation equation) {
        // Creates random starting population
        Solution[] population;
        population = new Solution[NICHE_NUMBER * NICHE_POPULATION_SIZE];
        for (int i = 0; i < population.length; i++)
            population[i] = new Solution(equation);
        return population;
    }

    private static Equation getEquation() {
        // Get equation input and validate
        Scanner input;
        input = new Scanner(System.in);
        Equation equation = null;
        do {
            System.out.println("Enter desired equation to solve");
            try {
                equation = new Equation(input.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (equation == null);
        return equation;
    }


}
