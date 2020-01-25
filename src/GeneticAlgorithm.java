import java.util.ArrayList;

public class GeneticAlgorithm {
    private static GeneticAlgorithm instance = new GeneticAlgorithm();

    enum FitnessFunction { ABS, POW }
    enum SelectionAlgorithm { ROULETTE_WHEEL, RANK_SELECTION, TOURNAMENT_SELECTION }
    enum Crossover { ONE_POINT, TWO_POINTS, MIDDLE }

    public int[] bestChromosome;
    public float bestFitness;

    private GeneticAlgorithm() {
        bestFitness = 0;
    }


    public static GeneticAlgorithm getInstance() {
        return instance;
    }


    public ArrayList<int[]> generateRandomPopulation( Graph graph, int population){
        int[] s;
        ArrayList p = new ArrayList<int[]>();
        for( int i = 0; i < population; i++  ){
            s = new int[ graph.getNum_nodes() ];
            for (int j = 0; j < graph.getNum_nodes(); j++ )
                s[j] = (int) Math.round( Math.random() );
            p.add( s );
        }
        return p;
    }


    public ArrayList<int[]> performElitism( FitnessFunction fitness, Graph g, ArrayList<int[]> p, ArrayList<int[]> next_p, int elitism_num ){
        float[] f, next_f;
        //calculating their fitness
        f = getFitness(fitness, g, p);
        next_f = getFitness(fitness, g, next_p);
        //sort the population list depending on its fitness value
        quickSort( p, f, 0, p.size()-1 );
        quickSort( next_p, next_f, 0, next_p.size()-1 );
        //selecting best elitism_num individuals from previous generation
        for( int i=0; i<elitism_num; i++){
            next_p.set( i, p.get( p.size() -1 - i ) );
        }
        return next_p;
    }


    public void bestIndividual( ArrayList<int[]> p, Graph g ){
        float[] f, next_f;
        //calculating their fitness
        f = getFitness( FitnessFunction.ABS, g, p);
        for( int i = 0; i < f.length; i++ ){
            if( f[i] > bestFitness ){
                bestFitness = f[i];
                bestChromosome = p.get(i).clone();
            }
        }
    }


    public void resetGeneticAlgorithm(){
        bestChromosome = null;
        bestFitness = 0;
    }


    public ArrayList<int[]> performMutation( ArrayList<int[]> p, double prob ){
        double mut;
        for (int[] s : p) {
            for(int i = 0; i < s.length; i++ ){
                mut = Math.random();
                if( mut < prob ){
                    s[i] = s[i] ^ 1;
                }
            }
        }
        return p;
    }


    public ArrayList<int[]> performCrossover( Crossover crossover, ArrayList<int[]> p ) {
        switch (crossover){
            case ONE_POINT:
                return onePointCrossover( p );
            case TWO_POINTS:
                return twoPointCrossover( p );
            default:
                //Default MIDDLE
                return middleCrossover( p );
        }
    }


    public ArrayList<int[]> onePointCrossover( ArrayList<int[]> p ){
        int i, r;
        int[] aux = new int[ p.get(0).length ];

        //1-point crossover
        r = (int)( Math.random() * ( p.get(0).length ) );
        if( r == p.get(0).length ) r = p.get(0).length - 1;

        for( i = 0; i < r; i++ ){
            aux[i] = p.get(1)[i];
            p.get(1)[i] = p.get(0)[i];
            p.get(0)[i] = aux[i];
        }
        return p;
    }


    public ArrayList<int[]> twoPointCrossover( ArrayList<int[]> p ){
        int i, r1, r2;
        int[] aux = new int[ p.get(0).length ];

        //2-point crossover
        r1 = (int)( Math.random() * ( p.get(0).length-1 ) );
        r2 = (int)( Math.random() * ( p.get(0).length-1 ) );
        if( r1 > r2 ){
            i = r1;
            r1 = r2;
            r2 = i;
        }

        for( i = 0; i < r1; i++ ){
            aux[i] = p.get(1)[i];
            p.get(1)[i] = p.get(0)[i];
            p.get(0)[i] = aux[i];
        }

        for( i = r1; i < r2; i++ ){
            aux[i] = p.get(1)[i];
            p.get(1)[i] = p.get(0)[i];
            p.get(0)[i] = aux[i];
        }

        return p;
    }


    public ArrayList<int[]> middleCrossover( ArrayList<int[]> p ){
        int i, r;
        int[] aux = new int[ p.get(0).length ];

        //1-point crossover
        r = p.get(0).length / 2;

        for( i = 0; i < r; i++ ){
            aux[i] = p.get(1)[i];
            p.get(1)[i] = p.get(0)[i];
            p.get(0)[i] = aux[i];
        }
        return p;
    }


    public ArrayList<int[]> selectIndividuals( SelectionAlgorithm selectionAlgorithm, FitnessFunction fitness, Graph g, ArrayList<int[]> p, int n ){
        switch (selectionAlgorithm) {
            case ROULETTE_WHEEL:
                return rouletteWheelSelection( fitness, g, p, n);
            case RANK_SELECTION:
                return rankSelection( fitness, g, p, n);
            default:
                //default case: TOURNAMENT_SELECTION
                return tournamentSelection( fitness, g, p, n );
        }
    }


    public ArrayList<int[]> tournamentSelection( FitnessFunction fitness, Graph g, ArrayList<int[]> p, int n ){
        int i, r;
        float[] f;
        ArrayList<int[]> new_p = new ArrayList<>();
        ArrayList<int[]> aux_p = (ArrayList<int[]>) p.clone();

        //Randomly selecting half of the individuals
        for( i = 0; i < aux_p.size()/2; i++ ) {
            r = (int) (Math.random() * (aux_p.size() - 1));
            aux_p.remove(r);
        }
        //calculating their fitness
        f = getFitness(fitness, g, aux_p);
        //sort the population list depending on its fitness value
        quickSort( aux_p, f, 0, aux_p.size()-1 );

        for( i = 0; i < n; i++ ){
            new_p.add( aux_p.get( aux_p.size()-1 ) );
            aux_p.remove( aux_p.size()-1 );
        }
        return new_p;
    }


    public ArrayList<int[]> rankSelection( FitnessFunction fitness, Graph g, ArrayList<int[]> p, int n ){
        int i, j;
        float[] probs;
        ArrayList<int[]> new_p = new ArrayList<>();
        ArrayList<int[]> aux_p = (ArrayList<int[]>) p.clone();
        float r;

        // Draw new population
        for( i = 0; i < n; i++ ){
            // Generate probability intervals for each individual
            probs = generateProbIntervals_rankSelection( fitness, g, aux_p );
            r = (float) Math.random();
            for( j = 0; j < aux_p.size(); j++ ){
                if( r <= probs[j] ) {
                    new_p.add(aux_p.get(j).clone());
                    aux_p.remove(j);
                    break;
                }
            }
        }
        return new_p;
    }


    public float[] generateProbIntervals_rankSelection( FitnessFunction fitness, Graph g, ArrayList<int[]> p ){
        int i, j, total_rank=0;
        float[] probs, f;
        int[] ranks = new int[ p.size() ];

        //fitness values of the population
        f = getFitness(fitness, g, p);
        //sort the population list depending on its fitness value
        quickSort( p, f, 0, p.size()-1 );
        //generating the ranks list
        for( i=0; i<p.size(); i++){
            ranks[i] = i+1;
            total_rank += ranks[i];
        }
        // Generate probability intervals for each individual
        probs = new float[ f.length ];
        for( i = 0; i < f.length; i++ ){
            probs[i] = 0;
            for( j = 0; j < i+1; j++){
                probs[i] += (float) ranks[j]/total_rank;
            }
        }
        probs[ probs.length -1 ] = 1;
        return probs;
    }


    public ArrayList<int[]> rouletteWheelSelection( FitnessFunction fitness, Graph g, ArrayList<int[]> p, int n ){
        int i, j;
        float[] probs;
        ArrayList<int[]> new_p = new ArrayList<>();
        ArrayList<int[]> aux_p = (ArrayList<int[]>) p.clone();
        float r;

        // Draw new population
        for( i = 0; i < n; i++ ){
            // Generate probability intervals for each individual
            probs = generateProbIntervals_rouletteWheelSelection( fitness, g, aux_p );
            r = (float) Math.random();
            for( j = 0; j < aux_p.size(); j++ ){
                if( r <= probs[j] ) {
                    new_p.add(aux_p.get(j).clone());
                    aux_p.remove(j);
                    break;
                }
            }
        }
        return new_p;
    }


    //Returns a probability intervals list for all elements in the population.
    public float[] generateProbIntervals_rouletteWheelSelection( FitnessFunction fitness, Graph g, ArrayList<int[]> p ){
        int i, j;
        float total_fitness = 0;
        float[] f, rel_fitness, probs;

        //Roulette wheel selection
        f = getFitness( fitness, g, p );
        for( i = 0; i < f.length; i++ ) total_fitness += f[i];

        rel_fitness = f.clone();
        for( i = 0; i < f.length; i++ ) rel_fitness[i] = rel_fitness[i] / total_fitness;

        // Generate probability intervals for each individual
        probs = new float[ f.length ];
        for( i = 0; i < f.length; i++ ){
            probs[i] = 0;
            for( j = 0; j < i+1; j++){
                probs[i] += rel_fitness[j];
            }
        }
        //ensuring the last prob is 1
        probs[ probs.length -1 ] = 1;
        return probs;
    }


    public float[] getFitness( FitnessFunction fitness, Graph g, ArrayList<int[]> p ){
        float[] f = new float[ p.size() ];
        switch (fitness) {
            case ABS:
                for( int i = 0; i < p.size(); i++) f[i] = Math.abs( getModularity( g, p.get(i) ) );
                break;
            default:
                //default case: POW
                for( int i = 0; i < p.size(); i++) f[i] = (float) Math.pow( getModularity( g, p.get(i) ), 2 );
                break;
        }
        return f;
    }


    public float getModularity( Graph g, int[] s ){
        float modularity = 0;
        int[][] a = g.getGraph();
        int n = g.getNum_nodes();
        int l = g.getNum_edges();
        int[] k = g.getNodes_degree();

        for( int i = 0; i < n; i++ ){
            for( int j = 0; j < n; j++ ){
                modularity += (a[i][j] - k[i]*k[j]/(2*l) ) * ( s[i]*s[j] + (1-s[i])*(1-s[j]) );
            }
        }
        modularity = modularity / (2*l);
        return modularity;
    }


    /* low  --> Starting index,  high  --> Ending index */
    public void quickSort(ArrayList<int[]> toSort, float[] f, int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is now at right place */
            int pi = partition(toSort, f, low, high);

            quickSort(toSort, f, low, pi - 1);  // Before pi
            quickSort(toSort, f, pi + 1, high); // After pi
        }
    }


    /* This function takes last element as pivot, places the pivot element at its correct position in sorted
    array, and places all smaller (smaller than pivot) to left of pivot and all greater elements to right of pivot */
    public int partition (ArrayList<int[]> toSort, float[] f, int low, int high){
        // pivot (Element to be placed at right position)
        float pivot = f[high];
        float auxf;
        int i = (low - 1);  // Index of smaller element
        int[] aux;

        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
            if (f[j] < pivot)
            {
                i++;    // increment index of smaller element
                auxf = f[i];
                f[i] = f[j];
                f[j] = auxf;
                aux = toSort.get(i);
                toSort.set(i, toSort.get(j) );
                toSort.set(j, aux );
            }
        }
        auxf = f[i+1];
        f[i+1] = f[high];
        f[high] = auxf;
        aux = toSort.get(i+1);
        toSort.set(i+1, toSort.get(high) );
        toSort.set(high, aux );
        return (i + 1);
    }

}
