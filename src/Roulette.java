// Roulette.java
// Handles parent and offspring handling using roulette

import java.util.Random;

public class Roulette {
    private double[] roulette;
    private Solution[] currentPopulation;

    public Roulette(Solution[] population, int startIndex, int endIndex, long maxConstant) {
        roulette = new double[endIndex - startIndex];
        currentPopulation = new Solution[endIndex - startIndex];
        System.arraycopy(population, startIndex, currentPopulation, 0, (endIndex - startIndex));

        // Calculate sum of fitness
        double sumOfFitnesses = 0.0;
        for (int i = 0; i < currentPopulation.length; i++) {
            Solution sol = currentPopulation[i];
            sumOfFitnesses += maxConstant - sol.getFitnessScore();  // Minimization
        }

        // Create roulette array
        for (int i = 0; i < roulette.length; i++) {
            try {
                roulette[i] = roulette[i - 1] + ((maxConstant - currentPopulation[i].getFitnessScore()) / sumOfFitnesses);
            } catch (IndexOutOfBoundsException e) {  // For i = 0
                roulette[i] = (maxConstant - currentPopulation[i].getFitnessScore()) / sumOfFitnesses;
            }
        }
    }

    public Solution getOffspringAndMutate() {
        int fatherSolutionIndex = getParentSolutionIndex();
        int motherSolutionIndex;
        do {  // Validate different parents
            motherSolutionIndex = getParentSolutionIndex();
        } while (motherSolutionIndex == fatherSolutionIndex);
        return new Solution(currentPopulation[fatherSolutionIndex], currentPopulation[motherSolutionIndex]);
    }

    private int getParentSolutionIndex() {
        // Get random parent index
        Random rand = new Random();
        double random = rand.nextDouble();
        for (int j = 0; j < roulette.length; j++)
            if (random <= roulette[j]) {
                return j;
            }
        return roulette.length - 1;
    }
}
