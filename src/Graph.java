import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Graph {
    private String filename;
    private int[][] graph;
    private int num_nodes, num_edges;
    private int[] nodes_degree;

    public Graph(String filename) {
        this.filename = filename;
        loadGraph( filename );
    }

    public int[][] getGraph() {
        return graph;
    }

    public int getNum_nodes() {
        return num_nodes;
    }

    public int getNum_edges() {
        return num_edges;
    }

    public int[] getNodes_degree(){
        return nodes_degree;
    }

    public void loadGraph(String filename) throws Error{
        String line="";
        int i, j;

        try{
            FileReader input = new FileReader( filename );
            BufferedReader bufRead = new BufferedReader(input);

            try {
                //Reading the first line: Vertices* num_vertices
                line = bufRead.readLine();
                String[] array1 = line.split(" ");
                num_nodes = Integer.parseInt( array1[1] );
                num_edges = 0;
                graph = new int[ num_nodes ][ num_nodes ];
                nodes_degree = new int[ num_nodes ];
                for( i = 0; i < num_nodes; i++ ){
                    nodes_degree[ i ] = 0;
                    for( j = 0; j < num_nodes ; j++){
                        graph[i][j] = 0;
                    }
                }
                //Discard nodes info
                for ( i = 0; i < num_nodes; i++ ){
                    line = bufRead.readLine();
                }

                //Discard first Edges line: Edges*
                line = bufRead.readLine();

                //Reading all vertices
                while ( (line = bufRead.readLine() ) != null) {
                    array1 = line.split(" ");
                    num_edges ++;
                    graph[ Integer.parseInt( array1[0] ) - 1 ][ Integer.parseInt( array1[1] ) - 1 ] = 1;
                    graph[ Integer.parseInt( array1[1] ) - 1 ][ Integer.parseInt( array1[0] ) - 1 ] = 1;
                    nodes_degree[ Integer.parseInt( array1[0] ) - 1 ] ++;
                    nodes_degree[ Integer.parseInt( array1[1] ) - 1 ] ++;
                }
            }
            catch (Exception e ){
                System.out.println("ERROR: when reading line after" + line);
                e.printStackTrace();
                System.exit(1);
            }
        }
        catch (Exception e){
            System.out.println("ERROR: when trying to open the file");
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        int i;
        String string = "Num of nodes: " + num_nodes;
        string = string + "\nNum of edges: " +num_edges;
        string = string + "\nDegree of the nodes: ";
        for( i = 0; i < num_nodes; i ++ ) string = string + nodes_degree[i] + " ";
        for ( i = 0; i < num_nodes; i ++ ){
            string = string + "\n" + i + "\t";
            for (int j = 0; j < num_nodes; j ++ ){
                string = string + graph[i][j] + " ";
            }
        }
        return string;
        //return super.toString();
    }
}
