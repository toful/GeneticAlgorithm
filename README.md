# GeneticAlgorithm

[![License](https://img.shields.io/github/license/toful/GeneticAlgorithm?style=plastic)](https://github.com/toful/GeneticAlgorithm)

Optimization of Genetic Algorithms. Neural and Evolutionary Computation subject, MESIIA Master, URV

Implementation of a genetic algorithm (GA) for the clustering of the nodes of a graph, by means of the optimization of modularity.

## Implementation

Different Parameter Values for the Genetic Algorithm execution:
* **Fitness function**: ABS and POW of the Network Modularity value.
* **Selection schemes**: Roulette Wheel, Rank selection and Tournament selection.
* **Crossover**: One point crossover, Two points crossover and uniform crossover using middle of the chromosomes.
* **Mutation**: Set the probaility of a mutation in a Chromosome position to happen. 
* **Elitism**: Can be set to True or False, conserving the best 5 individuals of the previous generation.

A **test** variable can also set to true/false:
* True --> the Genetic algorithm is executed many times by using ifferent configuration parameters and the results are stored in a CSV file.
* False --> the Genetic algorithm is executed ones, with the parameter vaules specified before. 

### Input

The input must be a Network in a \*.net Pajek file format.

### Output

The output consists of:

- a CSV file with the result of all executions perfomed if the test variable is set to True.

- a  \*.clu Pajek format file with the partitions information if the test variable is set to False.

## Usage

Code has been implemented in Java using the IntelliJ IDEA Community Edition IDE.

Load the classes under src folder in a IDE and run the main function under the Main Java class.

## Author

* **Cristòfol Daudén Esmel** - [toful](https://github.com/toful)
