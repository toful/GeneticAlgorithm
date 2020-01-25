import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void runGeneticAlgorithm(int generations, int population, Graph graph,
                                           GeneticAlgorithm.SelectionAlgorithm selectionAlgorithm,
                                           GeneticAlgorithm.FitnessFunction fitnessFunction,
                                           GeneticAlgorithm.Crossover crossover,
                                           double mutation_prob,
                                           boolean elitism){
        ArrayList p, next_p, aux_p;
        int i;
        //Setting best chromosome no null
        GeneticAlgorithm.getInstance().resetGeneticAlgorithm();
        //Generating the initial random Population
        p = GeneticAlgorithm.getInstance().generateRandomPopulation( graph, population);
        do{
            next_p = new ArrayList();
            for( i = 0; i < population / 2; i++ ){
                //selection of two individuals
                aux_p = GeneticAlgorithm.getInstance().selectIndividuals( selectionAlgorithm, fitnessFunction, graph, p, 2);
                //crossover of the individuals
                aux_p = GeneticAlgorithm.getInstance().performCrossover( crossover, aux_p );
                //mutation of the individuals
                aux_p = GeneticAlgorithm.getInstance().performMutation( aux_p, mutation_prob );
                //adding individuals to the next generation
                next_p.addAll( aux_p );

            }
            //selecting only the best chromosomes between both generations
            if( elitism ) p = GeneticAlgorithm.getInstance().performElitism( fitnessFunction, graph, p, next_p, 5);
            else p = next_p;
            GeneticAlgorithm.getInstance().bestIndividual( p, graph );
        } while( generations-- > 1 );
        /*System.out.println( "Fitness: " + GeneticAlgorithm.getInstance().bestFitness
                + "\n Chromosome: " + GeneticAlgorithm.getInstance().bestChromosome.toString() );*/
    }


    public static void runTests( Graph graph ){
        int generations = 200;
        int population = 20;
        ArrayList<Double> mutation_probs = new ArrayList<Double>(Arrays.asList( 0.00, 0.05, 0.10));
        String line="";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("tests.csv"));
            line = "selectionAlgorithm,fitnessFunction,crossover,mutation_prob,elitism,fitness,chromosome";
            writer.write( line );
            for (GeneticAlgorithm.SelectionAlgorithm selectionAlgorithm : GeneticAlgorithm.SelectionAlgorithm.values()) {
                for (GeneticAlgorithm.FitnessFunction fitnessFunction : GeneticAlgorithm.FitnessFunction.values()) {
                    for (GeneticAlgorithm.Crossover crossover : GeneticAlgorithm.Crossover.values()) {
                        for (double mutation_prob : mutation_probs) {
                            for (boolean elitism : Arrays.asList(true, false)) {
                                runGeneticAlgorithm(generations, population, graph, selectionAlgorithm, fitnessFunction, crossover, mutation_prob, elitism);
                                line = selectionAlgorithm + "," + fitnessFunction + "," + crossover + "," + mutation_prob
                                        + "," + elitism + "," + String.valueOf( GeneticAlgorithm.getInstance().bestFitness) + "," ;
                                for(int gene:GeneticAlgorithm.getInstance().bestChromosome){
                                    line += String.valueOf(gene);
                                }
                                writer.write( line );
                                System.out.println( line );
                            }
                        }
                    }
                }
            }
            writer.close();
        }
        catch (Exception e){
            System.out.println( "ERROR: Something happened when writing to file.");
        }
    }

    public static void main(String args[]) {
        int generations = 200;
        int population = 20;
        Graph graph;
        GeneticAlgorithm.SelectionAlgorithm selectionAlgorithm = GeneticAlgorithm.SelectionAlgorithm.RANK_SELECTION;
        GeneticAlgorithm.FitnessFunction fitnessFunction = GeneticAlgorithm.FitnessFunction.ABS;
        GeneticAlgorithm.Crossover crossover = GeneticAlgorithm.Crossover.TWO_POINTS;
        double mutation_prob = 0.05;
        boolean elitism = true;

        //Getting the graph we have to divide in two partitions
        graph = new Graph( "data/zachary_unwh.net");
        System.out.println( graph );

        //running tests with different configuration parameters for the Genetic Algorithm
        runTests( graph );
        //running the Genetic Algorithm
        //runGeneticAlgorithm( generations, population, graph, selectionAlgorithm, fitnessFunction, crossover, mutation_prob, elitism );

    }
}
