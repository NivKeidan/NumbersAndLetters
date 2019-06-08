# NumbersAndLetters

This was an exercise to my university class

This program solves numbers and letters riddle.
(For example, SEND+MORE=MONEY, a solution is: 9567+1085=10652)

### How to run
compile the java files and run MMN12

The equations are solved using [genetic algorithms](https://en.wikipedia.org/wiki/Genetic_algorithm)
The equations solved can have addition, subtraction and multiplaction (no division at the moment)

## The algorithm

I assume you know how genetic algorithms work, so I will just touch some important details:

* Parents are selected using classic roulette, while the most weak member is never selected
* When creating new offsprings or performing mutations, several validations are done to ensure that new members are valid
* The best member is always kept for the next generation

### Avoidiing early convergence
Since early ocnvergence is highly likely in this case, we use:
* Niches (sub populations) are used to avoid quick convergence and to keep the population varied
* High mutation chances
* Population restart when early convergence is detected

### Evaluation function
The evaluation function is the difference between the result of the left side of the equations and the translation of the right side.
Meaning, a solution to the equation has a value of 0
