package disease; 
//Disease Simulation
import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class simulation { 
	private static char [][] grid;
	private static int N;
	private static int T;
	private static double alpha;
	private static double beta;
	private static int size;
	
	public static void main(String[] args) 
	{
		
		Scanner scan = new Scanner(System.in);
		// Number of individuals  
		System.out.println("Enter the number of individuals (N): ");  
		while(!(scan.hasNextInt())) {  
			System.out.println("Please enter an integer.");  
			scan.next(); 
		}  
		N = scan.nextInt(); 
		size = (int) Math.sqrt(N);  
		while (size * size != N)  
		{  
			System.out.println("Invalid input\nPlease enter a perfect square: ");  
			while(!(scan.hasNextInt())) {  
				System.out.println("Please enter an integer.");  
				scan.next(); 
			} 
			N = scan.nextInt();  
			size = (int) Math.sqrt(N);  
		}
		//number of time steps 
		System.out.println("Enter the number of time steps (T): ");  
		while(!(scan.hasNextInt())) {  
			System.out.println("Please enter an integer.");  
			scan.next(); 
		} 
		T = scan.nextInt();  
		while (T < 0)  
		{  
			System.out.println("Invalid input\nPlease enter a number greater than 0: ");  
			while(!(scan.hasNextInt())) {  
				System.out.println("Please enter an integer.");  
				scan.next(); 
			} 
			T = scan.nextInt();  
		}  
		// infection rate input (alpha)
		System.out.println("Enter the infection rate (α): ");  
		while(!(scan.hasNextDouble())) {  
			System.out.println("Please enter a double.");  
			scan.next(); 
		} 
		alpha = scan.nextDouble();  
		while (alpha < 0 || alpha > 1)  
		{  
			System.out.println("Invalid input\nPlease enter a number between 0 and 1: ");  
			while(!(scan.hasNextDouble())) {System.out.println("Please enter a double.");  
				scan.next(); 
			} 
			alpha = scan.nextDouble(); 
		}  
		// Recover rate input  
		System.out.println("Enter the recovery rate (β): ");  
		while(!(scan.hasNextDouble())) {  
			System.out.println("Please enter a double.");  
			scan.next(); 
		} 
		beta = scan.nextDouble();  
		while (beta < 0 || beta > 1)
		{  
			System.out.println("Invalid input\nPlease enter a number between 0 and 1: ");  
			while(!(scan.hasNextDouble())) { 
				System.out.println("Please enter a double.");  
				scan.next(); 
			} 
			beta = scan.nextDouble();  
		}  
		grid = new char [size][size];
		createGrid();
		writeToFile(0);
		printGrid(readStep(0));
		// SIMULATE
		for (int i = 1; i <= T; i ++)
			{
				System.out.println("Time Step: " + (i));
				simulateTimeStep(i);
				writeToFile(i);
	            printGrid(readStep(i));
				int infected = countInfected(i);
				int recovered = countRecovered(i);
	            double ratioInfected = (double) infected / N;

	            System.out.println("Number of Infected Individuals: " + infected);
	            System.out.println("Number of Recovered Individuals: " + recovered);
	            System.out.println("Ratio of Infected Individuals to Total: " + ratioInfected);
	            System.out.println("");
			}
		scan.close();
	} 
    public static void createGrid() 
    {
        // Set all individuals to 'S'
        for (int i = 0; i < size; i++) 
        {
            for (int j = 0; j < size; j++) 
            	{ grid[i][j] = 'S'; }
        }
        // Set patient zero
        Random random = new Random();
        grid[random.nextInt(size)][random.nextInt(size)] = 'I';
    }
    public static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char status : row) {
                System.out.print(status + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void writeToFile(int timeStep)
    {
        try (FileWriter writer = new FileWriter("timestep_" + timeStep + ".txt")) 
        {
            for (int i = 0; i < size; i++) 
            {
                for (int j = 0; j < size; j++) 
                	{writer.write(grid[i][j] + " ");}
                writer.write("\n");
            }
        } 
        catch (IOException e) 
            {e.printStackTrace();}
    }
    public static char[][] readStep(int timestep)

    {
    	try (BufferedReader reader = new BufferedReader(new FileReader("timestep_" + timestep + ".txt")))
    	{
    		char[][] grid = new char[size][size];
    		for (int i = 0; i < size; i++) {
                String line = reader.readLine();
                String[] chars = line.split("\\s+");
                for (int j = 0; j < size; j++) {
                    grid[i][j] = chars[j].charAt(0);
                }
            }

            return grid;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int numNeighbors(int row, int col, int timeStep )
    {  
    	int infected = 0;  
    	char [][] lastGrid = readStep(timeStep-1);  
    	for (int i = row - 1; i <= row + 1; i++) {  
	    	for (int j = col - 1; j <= col + 1; j++) {  
	    			if (i == row && j == col) 
	    				{continue;}  
	    			if (i == row - 1 && j == col - 1)  
	    				{continue;}
	    			if (i == row - 1 && j == col + 1)
	    				{continue;}  
	    			if (i == row + 1 && j == col - 1) 
	    				{continue;} 
	    			if (i == row + 1 && j == col + 1) 
	    				{continue;} 
	    			// Check if the neighbor is within bounds 
	    			if (i >= 0 && i < size && j >= 0 && j < size) {
	    				if (lastGrid[i][j] == 'I') { 
	    						infected++;  
	    				}  
	    			}  
	    	}
    	}
    	return infected;  
    }  
    public static int countRecovered(int timestep)
    {
    	char [][] lastGrid = readStep(timestep);
    	int numRecovered = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (lastGrid[i][j] == 'R') {
                    numRecovered++;
                }
            }
        }

        return numRecovered;
    }
    public static int countInfected(int timestep)
    {
    	char [][] lastGrid = readStep(timestep);
    	int numInfected = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (lastGrid[i][j] == 'I')
                    numInfected++;
            }
        }

        return numInfected;
    }
    
    public static void simulateTimeStep(int timeStep) {

        for (int i = 0; i < size; i++) 
        {
            for (int j = 0; j < size; j++) 
            {
                if (grid[i][j] == 'S') 
                {
                    double probability = numNeighbors(i, j, timeStep) * alpha;
                    if (Math.random() < probability) 
                    {
                        grid[i][j] = 'I';  
                    }
                } 
                else if (grid[i][j] == 'I') 
                {
                    if (Math.random() < beta) 
                    	grid[i][j] = 'R'; 
                }
            }
        }
    }
 

}