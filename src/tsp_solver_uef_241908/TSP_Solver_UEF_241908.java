package tsp_solver_uef_241908;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * The main class with the 3 main TSP algorithms.
 * 
 * NetBeans 8
 * 
 * @author tuohy
 * @version 14-3-17
 */
public class TSP_Solver_UEF_241908 {
    
    private final static String ERRORMSG = "The tsplib graph input has errors. "
        + "Check that\n"
        + "\t1) there are at least 4 vertices,\n"
        + "\t2) the edge weight type is EUC_2D,\n"
        + "\t3) there are not 2 (or more) vertices with equal coordinates and\n"
        + "\t4) all coordinates are less than 5 000 000"
        + " but not less than 0 and that E will not be needed.\n"
        + "The coordinate separator mark is space (' ') and "
        + "coordinate line starts with an integer ID. "
        + "\nExtra spaces and over 500 000 rows may cause errors. "
        + "Instead of commas (',') use points ('.') in decimal numbers.\n"
        + "\nThe algorithm run did not start.";
    
    /**
     * The main method.
     * @param args String, the command line arguments 
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new User_interface().setVisible(true);
            } 
        );
    }
    
    /**
     * This method checks that the input does have correct integers.
     * @param coordinates ArrayList
     * @return String
     */
    public static String checkCoordinateInputInt(ArrayList coordinates) {
        boolean inputOk = true;
        for (Object coordinate : coordinates) {
            try {
                if ((double) coordinate >= 5000000) {
                    inputOk = false; 
                    System.out.println("5 000 000 or more found");
                }
                if ((double) coordinate < 0) {
                    inputOk = false; 
                    System.out.println("less than 0 found");
                }
                if (coordinate.toString().contains("E") ||
                    coordinate.toString().contains("e")) {
                    inputOk = false;
                    System.out.println("ridiculous coordinate value found");
                }
                //if ((double) coordinate % 1 != 0) {
                //    inputOk = false; 
                //    System.out.println("not an integer");
                //} //uncomment if only integers wanted 
            }catch(Exception e) {
                System.out.println(e);
                return ERRORMSG;
            }
        }
        if(inputOk) {
            return "ok";
        }
        else {
            return ERRORMSG;
        }
    }
    
    /**
     * The Nearest Neighbor Heuristic.
     * @param input String
     * @return String
     */
    public static String NearestNeighbour_Algorithm(String input) {
        double tour_length = 0.0;
        String solution = "Solution: \n";
        int min = 1;
        int max = 0;
        String rows[] = new String[500000];    
        ArrayList coordinates = new ArrayList();
        int i = 0;
        String str;
        BufferedReader reader = new BufferedReader(new StringReader(input));
        boolean EUC_2D = false;
        boolean hasSameCoordinates = false;
        boolean rightAmountOfNumbers = false;
        try {
            while ((str = reader.readLine()) != null) {
                if (str.length() > 0) {
                    rows[i] = str; 
                    if(rows[i].charAt(0) == '0' || 
                            rows[i].charAt(0) == '1' ||
                            rows[i].charAt(0) == '2' || 
                            rows[i].charAt(0) == '3' ||
                            rows[i].charAt(0) == '4' || 
                            rows[i].charAt(0) == '5' ||
                            rows[i].charAt(0) == '6' || 
                            rows[i].charAt(0) == '7' ||
                            rows[i].charAt(0) == '8' || 
                            rows[i].charAt(0) == '9') {
                        max++;
                        try {
                            Double numberInput;
                            int endIndex;
                            for (int beginIndex = 0; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    numberInput = Double.parseDouble(
                                            numberString);
                                    coordinates.add(numberInput);   
                                    // second x, third y 
                                } 
                                catch (java.lang.NumberFormatException nfe) {
                                    System.err.println(nfe);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                    if(rows[i].contains("EUC_2D")) {
                        EUC_2D = true;
                        //System.out.println("Graph is EUC_2D");
                    }
                }
            }
        } 
        catch(IOException e) {
            System.err.println(e);
        }
        ArrayList coordinates_x = new ArrayList();
        ArrayList coordinates_y = new ArrayList();
        for(int j = 1; j < coordinates.size(); j++) {
            if(j%3 == 0) {
                rightAmountOfNumbers = false;
            }
            if((j - 1)%3 == 0) {
                coordinates_x.add(coordinates.get(j));
                rightAmountOfNumbers = false;
            }
            if((j - 2)%3 == 0) {
                coordinates_y.add(coordinates.get(j));
                rightAmountOfNumbers = true;
            }
        }
        for(int j = 0; j < coordinates_x.size()-1; j++) {
            for(int k = j + 1; k < coordinates_x.size(); k++) {
                if(coordinates_x.get(j).equals(coordinates_x.get(k)) &&
                   coordinates_y.get(j).equals(coordinates_y.get(k))) {
                    hasSameCoordinates = true;
                    System.out.println("equal coordinates" + 
                            coordinates_x.get(j) + ", " + 
                            coordinates_y.get(j) + " and " + 
                            coordinates_x.get(k) + ", " + 
                            coordinates_y.get(k));
                }
            }
        }
        // The input is read, now the algorithm starts if the input is ok.
        
        String inputresult = checkCoordinateInputInt(coordinates);
        if(inputresult.equals("ok") && max >=4 && EUC_2D && 
                !hasSameCoordinates && rightAmountOfNumbers) {
            System.gc(); // run garbage collector before starting 
            String[] args = new String[1]; 
            args[0] = "$ java -javaagent:tracker.jar TSP_Solver_UEF_241908 500 > /dev/null";
            ResourceTracker.premain(args[0]);
            // ^ Comment if ResourceTracker.java is not used 
            long startTime = System.nanoTime();
            
            Random rand = new Random();
            int random = rand.nextInt((max - min) + 1) + min;
            boolean[] booltable = new boolean[max];
            boolean booltable_has_false = true;
            solution = new StringBuilder(solution).append(
                    String.valueOf(random)).toString();
            int pointer = 0; 
            int pointer_start = random -1;
            double X1 = 0.0;
            double Y1 = 0.0;
            while(booltable_has_false) { 
                booltable[random - 1] = true;
                double min_distance = Double.MAX_VALUE;
                double temp_distance;
                double x1 = (double) coordinates_x.get(pointer_start);
                double y1 = (double) coordinates_y.get(pointer_start);
                X1 = (double) coordinates_x.get(random - 1); 
                Y1 = (double) coordinates_y.get(random - 1); 
                for(int j = 0; j < booltable.length; j++) {
                    if(booltable[j] == false) {
                        double x2 = (double) coordinates_x.get(j);
                        double y2 = (double) coordinates_y.get(j);
                        temp_distance = Sub_algorithms.
                                Euclidean_distance_squared(x1, y1, x2, y2);
                        if(temp_distance < min_distance) {
                            min_distance = temp_distance;
                            pointer = j;
                        }
                    }
                }
                // new vertice to the result:
                booltable[pointer] = true;
                solution = new StringBuilder(solution).append("-").toString();
                solution = new StringBuilder(solution).append(pointer + 1).toString();  
                tour_length += Math.sqrt(min_distance);

                booltable_has_false = false;
                for(int j = 0; j < booltable.length; j++) {
                    if(booltable[j] == false) {
                        booltable_has_false = true;
                    }
                }
                pointer_start = pointer;
            }
            // link back to the start node:
            solution = new StringBuilder(solution).append("-").toString();
            solution = new StringBuilder(solution).append(random).toString();
            tour_length += Sub_algorithms.Euclidean_distance(
                    (double) coordinates_x.get(pointer), 
                    (double) coordinates_y.get(pointer), X1, Y1);
            solution = new StringBuilder(solution).append("\nTour length: ").
                    toString();
            solution = new StringBuilder(solution).append(tour_length).
                    toString();
            
            long endTime = System.nanoTime();
            System.out.println("Time (millisec): " + (endTime - startTime)/1000000);
            System.out.println("Time (sec, rounded down): " + (endTime - startTime)/1000000000 + "\n");
            
            return solution;
        }
        else {
            return ERRORMSG;
        }
    }
    
    /**
     * The 2-MST algorithm.
     * @param input String
     * @return String
     */
    public static String DoubleMST_Algorithm_Prim(String input) {
        double tour_length = 0.0;
        String solution;
        int min = 1;
        int max = 0;
        String rows[] = new String[500000];    
        ArrayList coordinates = new ArrayList();
        int i = 0;
        String str;
        BufferedReader reader = new BufferedReader(new StringReader(input));
        boolean EUC_2D = false;
        boolean hasSameCoordinates = false;
        boolean rightAmountOfNumbers = false;
        try {
            while ((str = reader.readLine()) != null) {
                if (str.length() > 0) {
                    rows[i] = str;
                    if(rows[i].charAt(0) == '0' || 
                            rows[i].charAt(0) == '1' ||
                            rows[i].charAt(0) == '2' || 
                            rows[i].charAt(0) == '3' ||
                            rows[i].charAt(0) == '4' || 
                            rows[i].charAt(0) == '5' ||
                            rows[i].charAt(0) == '6' || 
                            rows[i].charAt(0) == '7' ||
                            rows[i].charAt(0) == '8' || 
                            rows[i].charAt(0) == '9') {
                        max++;
                        try {
                            Double numberInput;
                            int endIndex;
                            for (int beginIndex = 0; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    numberInput = Double.
                                            parseDouble(numberString);
                                    coordinates.add(numberInput);   
                                    // second x, third y 
                                } 
                                catch (java.lang.NumberFormatException nfe) {
                                    System.err.println(nfe);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                    if(rows[i].contains("EUC_2D")) {
                        EUC_2D = true;
                        //System.out.println("Graph is EUC_2D");
                    }
                }
            }
        } 
        catch(IOException e) {
            System.err.println(e);
        }
        ArrayList coordinates_x = new ArrayList();
        ArrayList coordinates_y = new ArrayList();
        for(int j = 1; j < coordinates.size(); j++) {
            if(j%3 == 0) {
                rightAmountOfNumbers = false;
            }
            if((j - 1)%3 == 0) {
                coordinates_x.add(coordinates.get(j));
                rightAmountOfNumbers = false;
            }
            if((j - 2)%3 == 0) {
                coordinates_y.add(coordinates.get(j));
                rightAmountOfNumbers = true;
            }
        }
        for(int j = 0; j < coordinates_x.size()-1; j++) {
            for(int k = j + 1; k < coordinates_x.size(); k++) {
                if(coordinates_x.get(j).equals(coordinates_x.get(k)) &&
                   coordinates_y.get(j).equals(coordinates_y.get(k))) {
                    hasSameCoordinates = true;
                    System.out.println("equal coordinates" + 
                            coordinates_x.get(j) + ", " + 
                            coordinates_y.get(j) + " and " + 
                            coordinates_x.get(k) + ", " + 
                            coordinates_y.get(k));
                }
            }
        }
        // The input is read, now the algorithm starts if the input is ok.
        
        String inputresult = checkCoordinateInputInt(coordinates);
        if(inputresult.equals("ok") && max >=4 && EUC_2D && 
                !hasSameCoordinates && rightAmountOfNumbers) {
            System.gc(); // run garbage collector before starting 
            String[] args = new String[1]; 
            args[0] = "$ java -javaagent:tracker.jar TSP_Solver_UEF_241908 500 > /dev/null";
            ResourceTracker.premain(args[0]);
            // ^ Comment if ResourceTracker.java is not used 
            long startTime = System.nanoTime();
            
            // call the MST Prim method: 
            double edges[][][] = Sub_algorithms.MST_Prim(min, max, coordinates_x, 
                    coordinates_y);
            // double the edges, in other words make a copy of each edge: 
            double doubled_edges[][][] = new double[(max*2)-1][2][2];
            for(int j = 1; j < max; j++) {
                doubled_edges[j-1][0][0] = edges[j-1][0][0];
                doubled_edges[j-1][1][0] = edges[j-1][1][0];
                doubled_edges[j-1][0][1] = edges[j-1][0][1];
                doubled_edges[j-1][1][1] = edges[j-1][1][1];
                doubled_edges[j+max-2][0][0] = edges[j-1][0][1];
                doubled_edges[j+max-2][1][0] = edges[j-1][1][1];
                doubled_edges[j+max-2][0][1] = edges[j-1][0][0];
                doubled_edges[j+max-2][1][1] = edges[j-1][1][0];
            }
            String connections_string = "";
            int prim_mst_connections = 0;
            // the next for loop helps to recognize 
            // what exactly are the current connections: 
            for(int j = 1; j < (max*2)-1; j++) {
                        String X1 = String.valueOf(doubled_edges[j-1][0][0]); 
                        String Y1 = String.valueOf(doubled_edges[j-1][1][0]); 
                        String X2 = String.valueOf(doubled_edges[j-1][0][1]); 
                        String Y2 = String.valueOf(doubled_edges[j-1][1][1]); 
                        connections_string = connections_string.concat( 
                                "\tFrom (" + X1 + ", " + Y1 + 
                                ")    to    (" + X2 + ", " + Y2 + ")\n");
                        prim_mst_connections++;
            }
            // finally, make the Euler tour and shortcuts: 
            String [] result = Sub_algorithms.Euler_and_embedded_tour(doubled_edges, 
                    max, (max*2)-1);
            // Hamiltonian tour and tour_length: 
            ArrayList numbers = new ArrayList();
            BufferedReader reader2 = new BufferedReader(new StringReader(input));
            try {
                while ((str = reader2.readLine()) != null) {
                    if (str.length() > 0) {
                        rows[i] = str;
                        if(rows[i].charAt(0) == '0' || 
                                rows[i].charAt(0) == '1' ||
                                rows[i].charAt(0) == '2' ||
                                rows[i].charAt(0) == '3' ||
                                rows[i].charAt(0) == '4' ||
                                rows[i].charAt(0) == '5' ||
                                rows[i].charAt(0) == '6' ||
                                rows[i].charAt(0) == '7' ||
                                rows[i].charAt(0) == '8' ||
                                rows[i].charAt(0) == '9') {
                            try {
                                double numberInput;
                                int endIndex;
                                for (int beginIndex = 0; beginIndex <
                                        rows[i].length(); 
                                        beginIndex = endIndex + 1) {
                                    endIndex = rows[i].indexOf(" ", 
                                            beginIndex);
                                    if (endIndex == -1) {
                                        endIndex = rows[i].length();
                                    }
                                    String numberString = rows[i].substring(
                                            beginIndex, endIndex);
                                    try {
                                        numberInput = Double.
                                                parseDouble(numberString);
                                        numbers.add(numberInput);
                                    } 
                                    catch (java.lang.
                                            NumberFormatException nfe) {
                                        System.err.println(nfe);
                                    }
                                }
                            }
                            catch(Exception e) {
                                System.err.println(e);
                            }
                        }   
                    }
                }
            } 
            catch(IOException e) {
                System.err.println(e);
            }
            
            String string_to_compare_with_numbers = Arrays.toString(result);
            String replaced1 = string_to_compare_with_numbers.replaceAll(",", "");
            String replaced2 = replaced1.replace("[", "");
            String replaced3 = replaced2.replace("]", "");
            String replaced4 = replaced3.replaceAll("\\^[0-9]+(\\.[0-9]{1,4})?$","");
            Matcher m = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(replaced4);
            String replaced5 = numbers.toString().replaceAll(",", "");
            String replaced6 = replaced5.replace("[", "");
            String replaced7 = replaced6.replace("]", "");
            String replaced8 = replaced7.replaceAll("\\^[0-9]+(\\.[0-9]{1,4})?$","");
            Matcher m2 = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(replaced8);
            
            double[] TSPsolutionCoord_x = new double[(numbers.size() / 2)-1];
            double[] TSPsolutionCoord_y = new double[(numbers.size() / 2)-1];
            double[] comp_x = new double[numbers.size() / 2];
            double[] comp_y = new double[numbers.size() / 2];
            int index = 1;
            for(int j = 0; m.find(); j++) {
                double value = Double.parseDouble(m.group());
                if(j % 2 == 0 || j == 0) {
                    index--;
                    TSPsolutionCoord_x[index] = value;
                }
                if(j % 2 == 1) {
                    TSPsolutionCoord_y[index-1] = value;
                }
                index++;
            }
            double start_node_x = TSPsolutionCoord_x[0];
            double start_node_y = TSPsolutionCoord_y[0];
            StringBuilder sb = new StringBuilder();
            index = 0;
            for(int j = 0; m2.find(); j++) {
                double value = Double.parseDouble(m2.group());
                if((j - 1)%3 == 0) {    // x 
                    index--;
                    comp_x[index] = value;
                }
                if((j - 2)%3 == 0) {    // y 
                    index--;
                    comp_y[index] = value;
                }
                index++;
            }
            
            boolean first = true;
            int first_index = 0;
            double last_node_x = Double.MAX_VALUE;
            double last_node_y = Double.MAX_VALUE;
            for(int j = 0; j < max; j++) {
                for(int k = 0; k < max; k++) {
                    if(TSPsolutionCoord_x[j] == comp_x[k] && 
                       TSPsolutionCoord_y[j] == comp_y[k]) { // matched
                        //System.out.println("MATCHED " + TSPsolutionCoord_x[j] + 
                        //        ", " + TSPsolutionCoord_y[j] + 
                        //        "\t" + comp_x[k] + ", " + comp_y[k]);
                        if(first) {
                            sb.append(k+1);
                            first_index = k+1;
                            first = false;
                            tour_length += Sub_algorithms.Euclidean_distance(
                                  start_node_x, start_node_y, 
                                  TSPsolutionCoord_x[j], TSPsolutionCoord_y[j]);
                        }
                        else {
                            sb.append("-").append(k+1);
                            tour_length += Sub_algorithms.Euclidean_distance(
                                  last_node_x, last_node_y, 
                                  TSPsolutionCoord_x[j], TSPsolutionCoord_y[j]);
                        }
                        last_node_x = TSPsolutionCoord_x[j];
                        last_node_y = TSPsolutionCoord_y[j];
                    }
                }
            }
            sb.append("-").append(first_index); // back to the first vertex 
            tour_length += Sub_algorithms.Euclidean_distance(
                    last_node_x, last_node_y, 
                    start_node_x, start_node_y);
            
            solution = sb.toString();
            
            long endTime = System.nanoTime();
            System.out.println("Time (millisec): " + (endTime - startTime)/1000000);
            System.out.println("Time (sec, rounded down): " + (endTime - startTime)/1000000000 + "\n");
            
            return "Doubled Prim MST connections:\n" + connections_string + 
                    "Total " + prim_mst_connections + " connections.\n\nSolution: \n" + 
                    solution + "\nTour length: " + tour_length;
        }
        else {
            return ERRORMSG;
        }
    }
    
    /**
     * The convex hull heuristic.
     * @param input String
     * @return String
     */
    public static String ConvexHull_Algorithm(String input) {
        double tour_length = 0.0; 
        String solution = "";
        int min = 1;
        int max = 0;
        String rows[] = new String[500000];    
        ArrayList coordinates = new ArrayList();
        int i = 0;
        String str;
        BufferedReader reader = new BufferedReader(new StringReader(input));
        boolean EUC_2D = false;
        boolean hasSameCoordinates = false;
        boolean rightAmountOfNumbers = false;
        try {
            while ((str = reader.readLine()) != null) {
                if (str.length() > 0) {
                    rows[i] = str;
                    if(rows[i].charAt(0) == '0' || 
                            rows[i].charAt(0) == '1' ||
                            rows[i].charAt(0) == '2' || 
                            rows[i].charAt(0) == '3' ||
                            rows[i].charAt(0) == '4' || 
                            rows[i].charAt(0) == '5' ||
                            rows[i].charAt(0) == '6' || 
                            rows[i].charAt(0) == '7' ||
                            rows[i].charAt(0) == '8' || 
                            rows[i].charAt(0) == '9') {
                        max++;
                        try {
                            Double numberInput;
                            int endIndex;
                            for (int beginIndex = 0; beginIndex < 
                                    rows[i].length(); 
                                    beginIndex = endIndex + 1) {
                                endIndex = rows[i].indexOf(" ", beginIndex);
                                if (endIndex == -1) {
                                    endIndex = rows[i].length();
                                }
                                String numberString = rows[i].substring(
                                        beginIndex, endIndex);
                                try {
                                    numberInput = Double.
                                            parseDouble(numberString);
                                    coordinates.add(numberInput);   
                                    // second x, third y 
                                } 
                                catch (java.lang.
                                        NumberFormatException nfe) {
                                    System.err.println(nfe);
                                }
                            }
                        }
                        catch(Exception e) {
                            System.err.println(e);
                        }
                    }
                    if(rows[i].contains("EUC_2D")) {
                        EUC_2D = true;
                        //System.out.println("Graph is EUC_2D");
                    }
                }
            }
        } 
        catch(IOException e) {
            System.err.println(e);
        }
        ArrayList coordinates_x = new ArrayList();
        ArrayList coordinates_y = new ArrayList();
        ArrayList coordinates_x2 = new ArrayList(); 
            // 2 = list where the inner vertices remain 
        ArrayList coordinates_y2 = new ArrayList();
        for(int j = 1; j < coordinates.size(); j++) {
            if(j%3 == 0) {
                rightAmountOfNumbers = false;
            }
            if((j - 1)%3 == 0) {
                coordinates_x.add(coordinates.get(j));
                coordinates_x2.add(coordinates.get(j));
                rightAmountOfNumbers = false;
            }
            if((j - 2)%3 == 0) {
                coordinates_y.add(coordinates.get(j));
                coordinates_y2.add(coordinates.get(j));
                rightAmountOfNumbers = true;
            }
        }
        for(int j = 0; j < coordinates_x.size()-1; j++) {
            for(int k = j + 1; k < coordinates_x.size(); k++) {
                if(coordinates_x.get(j).equals(coordinates_x.get(k)) &&
                   coordinates_y.get(j).equals(coordinates_y.get(k))) {
                    hasSameCoordinates = true;
                    System.out.println("equal coordinates" + 
                            coordinates_x.get(j) + ", " + 
                            coordinates_y.get(j) + " and " + 
                            coordinates_x.get(k) + ", " + 
                            coordinates_y.get(k));
                }
            }
        }
        // The input is read, now the algorithm starts if the input is ok.
        
        String inputresult = checkCoordinateInputInt(coordinates);
        if(inputresult.equals("ok") && max >=4 && EUC_2D && 
                !hasSameCoordinates && rightAmountOfNumbers) {
            System.gc(); // run garbage collector before starting 
            String[] args = new String[1]; 
            args[0] = "$ java -javaagent:tracker.jar TSP_Solver_UEF_241908 500 > /dev/null";
            ResourceTracker.premain(args[0]);
            // ^ Comment if ResourceTracker.java is not used 
            long startTime = System.nanoTime();
            
            String hull = Sub_algorithms.ConvexHull(min, max, 
                    coordinates_x, coordinates_y);
            String replace1 = hull.replace("]], [[", "]],\n[[");
            String[] lines = replace1.split("\r\n|\r|\n");
            String replace2 = replace1.replace("[[[", "\tFrom (");
            String replace3 = replace2.replace("[[", "\tFrom (");
            String replace4 = replace3.replace("], [", ")    to    (");
            String replace5 = replace4.replace("]]]", ")");
            String replace6 = replace5.replace("]]", ")");
            String replace7 = replace6.replace("),", ")");
            String hullResult = "Convex hull connections: \n" + replace7 + 
                    "\nTotal " + lines.length + " connections.\n";
            String replace8 = replace7.replace(",", ".");
            String replace9 = replace8.replace("(", "");
            String replace10 = replace9.replace(")", "");
            String replace11 = replace10.replace("\tFrom ", "");
            String replace12 = replace11.replace("    to    ", " ");
            String replace13 = replace12.replace(". ", " ");

            double[][][] edges = new double[max][2][2];
            for(int j = 0; j < max; j++) {
                edges[j][0][0] = Double.MAX_VALUE;
                edges[j][0][1] = Double.MAX_VALUE;
                edges[j][1][0] = Double.MAX_VALUE;
                edges[j][1][1] = Double.MAX_VALUE;
            }
            String rows2[] = new String[500000];    
            ArrayList values = new ArrayList();
            int a = 0;
            String str2;
            BufferedReader reader2 = new BufferedReader(new StringReader(replace13));
            try {
                while ((str2 = reader2.readLine()) != null) {
                    if (str2.length() > 0) {
                        rows2[a] = str2; 
                        if(rows2[a].charAt(0) == '0' || 
                                rows2[a].charAt(0) == '1' ||
                                rows2[a].charAt(0) == '2' || 
                                rows2[a].charAt(0) == '3' ||
                                rows2[a].charAt(0) == '4' || 
                                rows2[a].charAt(0) == '5' ||
                                rows2[a].charAt(0) == '6' || 
                                rows2[a].charAt(0) == '7' ||
                                rows2[a].charAt(0) == '8' || 
                                rows2[a].charAt(0) == '9' ||
                                rows2[a].charAt(0) == '-') {
                            // max will not be increased 
                            try {
                                Double numberInput;
                                int endIndex;
                                for (int beginIndex = 0; beginIndex < 
                                        rows2[a].length(); 
                                        beginIndex = endIndex + 1) {
                                    endIndex = rows2[a].indexOf(" ", beginIndex);
                                    if (endIndex == -1) {
                                        endIndex = rows2[a].length();
                                    }
                                    String numberString = rows2[a].substring(
                                            beginIndex, endIndex);
                                    try {
                                        numberInput = Double.parseDouble(
                                                numberString);
                                        values.add(numberInput);   
                                    } 
                                    catch (java.lang.NumberFormatException nfe) {
                                        System.err.println(nfe);
                                    }
                                }
                            }
                            catch(Exception e) {
                                System.err.println(e);
                            }
                        } 
                    }
                }
            } 
            catch(IOException e) {
                System.err.println(e);
            }
            
            int temp_index = 0;
            for(int j = 0; j < values.size(); j++) {
                if(j == 0 || j%4 == 0) {
                    edges[temp_index][0][0] = (double)values.get(j);
                }
                if((j + 1)%4 == 0) {
                    edges[temp_index][1][1] = (double)values.get(j);
                    temp_index++;
                }
                if((j + 2)%4 == 0) {
                    edges[temp_index][0][1] = (double)values.get(j);
                }
                if((j + 3)%4 == 0) {
                    edges[temp_index][1][0] = (double)values.get(j);
                }
            }
            boolean[] circumferenceVertices = new boolean[max]; 

            int match_count = 0;
            for(int j = 0; j < max; j++) {
                for(int k = 0; k < max; k++) {
                    if(coordinates_x2.get(j).equals(edges[k][0][0]) && 
                       coordinates_y2.get(j).equals(edges[k][1][0]) && 
                       edges[k][0][0] < Double.MAX_VALUE && 
                       edges[k][1][0] < Double.MAX_VALUE) {
                        match_count++;
                        circumferenceVertices[j] = true;
                        coordinates_x2.set(j, Double.MAX_VALUE);
                        coordinates_y2.set(j, Double.MAX_VALUE);
                    }
                }
            }   
            int inner_v_rounds = 0;
            if(match_count < max) {
                inner_v_rounds = max - match_count;
            }
            boolean more_than_1_inner = false;
            if(inner_v_rounds > 1) {
                more_than_1_inner = true;
            }
            //boolean circumferenceVertices[] currently knows what are in hull
            boolean only_1_selected_when_several_equal_min_dist = false;
            long amount_of_inner_vert_total = 0;
            long amount_of_cir_calcs_in_inn_calcs = 0;
            long amount_of_triangle_calc_total = 0;
            
            for(int z = 0; z < inner_v_rounds; z++) {
                /* choosing the next vertice: for each edge in edges[][][], 
                 define a line, and from each line, compare distances with each 
                 inner vertex (circumferenceVertices[] has false in the index) 
                 choose the minimum having the distance so that it has to have 
                 both a counter-clockwise AND a clockwise turn with the 
                 edge's vertices AND to the top of that, the distance with the 
                 edge has to be less than the distance between any circumference 
                 vertice. (The vertex to be chosen by line comparation
                 is wanted in between the two vertices of the edge.
                 In other words, compare with "x-closest-1" and "x-closest-2".)
                 If not possible, choose the closest inner vertice - 
                 circumference vertice pair.*/

                double next_vertice_x;   // next node to handle
                double next_vertice_y;   // next node to handle
                double closestCircEdgesVert1_x;
                double closestCircEdgesVert1_y;
                double closestCircEdgesVert2_x;
                double closestCircEdgesVert2_y;
                double closestVertOfLine1_2_x;   // the orthogonal point
                double closestVertOfLine1_2_y;   // the orthogonal point
                double lineGeneral_Ax_term; // y1 - y2 
                double lineGeneral_By_term; // x2 - x1 
                double lineGeneral_C_term;//(x1-x2)*y1+(y2-y1)*x1
                double min_distance = Double.MAX_VALUE; 
                int next_v_index_k = -1;
                
                for(int j = 0; j < max; j++) {  // finds min distance between 
                                                // any inner vertex - 
                                                // circumference vertex pair
                    if(edges[j][0][0] < Double.MAX_VALUE && 
                       edges[j][1][0] < Double.MAX_VALUE &&
                       edges[j][0][1] < Double.MAX_VALUE && 
                       edges[j][1][1] < Double.MAX_VALUE) {
                        for(int k = 0; k < max; k++) {
                            if((double)coordinates_x2.get(k) < Double.MAX_VALUE &&
                                    (double)coordinates_y2.get(k) < Double.MAX_VALUE) {
                                next_vertice_x = (double)coordinates_x2.get(k);
                                next_vertice_y = (double)coordinates_y2.get(k);
                                if(Sub_algorithms.Euclidean_distance(
                                        edges[j][0][0], edges[j][1][0], 
                                        next_vertice_x, next_vertice_y) < min_distance) {
                                    min_distance = Sub_algorithms.
                                        Euclidean_distance(
                                               edges[j][0][0], edges[j][1][0], 
                                               next_vertice_x, next_vertice_y);
                                    next_v_index_k = k;
                                }
                            }
                        }
                    }
                }
                //double tempdist = Sub_algorithms.Euclidean_distance(
                //        edges[min_dist_index_j][0][0], edges[min_dist_index_j][1][0], 
                //        (double)coordinates_x2.get(next_v_index_k), 
                //        (double)coordinates_y2.get(next_v_index_k));
                /*System.out.println("\t\tNEXT could be " + 
                (double)coordinates_x2.get(next_v_index_k) + 
                ", " + (double)coordinates_y2.get(next_v_index_k));
                System.out.println("Because min distance (circ-inn) is " + 
                tempdist + " from " + edges[min_dist_index_j][0][0] + ", " + 
                edges[min_dist_index_j][1][0] + " to " + 
                (double)coordinates_x2.get(next_v_index_k) + 
                ", " + (double)coordinates_y2.get(next_v_index_k));*/
                int amount_of_inner_vert = 0;
                for(int j = 0; j < circumferenceVertices.length; j++) {
                    if(!circumferenceVertices[j]) {
                        amount_of_inner_vert++;
                        amount_of_inner_vert_total++;
                    }
                }
                double innerVertices[][] = new double[amount_of_inner_vert][2];
                int index = 0;
                for(int j = 0; j < circumferenceVertices.length; j++) {
                    if(!circumferenceVertices[j]) {
                        innerVertices[index][0] = (double)coordinates_x2.get(j);
                        innerVertices[index][1] = (double)coordinates_y2.get(j);
                        index++;
                    }
                }
                double temp_X = 0.0;
                double temp_Y = 0.0;
                boolean orth_found = false;
                int amount_of_circ_vert = circumferenceVertices.length - 
                            amount_of_inner_vert;
                    //System.out.println("All: " + circumferenceVertices2.length + 
                    //        ", Cir: " + amount_of_circ_vert + 
                    //        ", Inn: " + amount_of_inner_vert);
                for(int h = 0; h < amount_of_inner_vert; h++) {                 
                    double next_vertice_x2 = innerVertices[h][0]; // x0
                    double next_vertice_y2 = innerVertices[h][1]; // y0
                    //System.out.println("Inner vertice candidate that is handled now " + temp + ": " + 
                    //        next_vertice_x + ", " + next_vertice_y);
                    for(int j = 0; j < amount_of_circ_vert; j++) {
                        amount_of_cir_calcs_in_inn_calcs++;
                        closestCircEdgesVert1_x = edges[j][0][0];
                        closestCircEdgesVert1_y = edges[j][1][0];
                        closestCircEdgesVert2_x = edges[j][0][1];
                        closestCircEdgesVert2_y = edges[j][1][1];
                        
                        //System.out.println("Now the edge from (" + closestCircEdgesVert1_x + 
                        //        ", " + closestCircEdgesVert1_y + ") to (" + closestCircEdgesVert2_x + 
                        //        ", " + closestCircEdgesVert2_y + ")");
                        lineGeneral_Ax_term = closestCircEdgesVert1_y - 
                                                     closestCircEdgesVert2_y; // y1 - y2 
                        lineGeneral_By_term = closestCircEdgesVert2_x - 
                                                     closestCircEdgesVert1_x; // x2 - x1 
                        lineGeneral_C_term = ((closestCircEdgesVert1_x - 
                                closestCircEdgesVert2_x) * closestCircEdgesVert1_y) + 
                                ((closestCircEdgesVert2_y - 
                                closestCircEdgesVert1_y) * 
                                closestCircEdgesVert1_x);//(x1-x2)*y1+(y2-y1)*x1
                        double temp_distance = Math.abs(lineGeneral_Ax_term * 
                                next_vertice_x2 + lineGeneral_By_term * next_vertice_y2 + 
                                lineGeneral_C_term) / Math.sqrt(lineGeneral_Ax_term * 
                                lineGeneral_Ax_term + lineGeneral_By_term * 
                                lineGeneral_By_term);
                        //Math.abs(|a*x0 + b*y0 + c |) / Math.sqrt(a*a + b*b);

                        // calculating the intersection point of "1to2" and "orthogonal"-next_v:
                        // wanted: closestVertOfLine1_2_x and closestVertOfLine1_2_y 

                        // http://stackoverflow.com/questions/1811549/perpendicular-on-a-line-from-a-given-point 
                        //(8.12.2016) 
                        /*double k = ((y2-y1) * (x3-x1) - (x2-x1) * (y3-y1)) / 
                                ((y2-y1)*(y2-y1) + (x2-x1)*(x2-x1));
                        x4 = x3 - k * (y2-y1)
                        y4 = y3 + k * (x2-x1) */
                        double sl = ((closestCircEdgesVert2_y-closestCircEdgesVert1_y) * 
                                (next_vertice_x2-closestCircEdgesVert1_x) - 
                                (closestCircEdgesVert2_x-closestCircEdgesVert1_x) * 
                                (next_vertice_y2-closestCircEdgesVert1_y)) / 
                                ((closestCircEdgesVert2_y-closestCircEdgesVert1_y)* 
                                (closestCircEdgesVert2_y-closestCircEdgesVert1_y) + 
                                (closestCircEdgesVert2_x-closestCircEdgesVert1_x)* 
                                (closestCircEdgesVert2_x-closestCircEdgesVert1_x));
                        closestVertOfLine1_2_x = next_vertice_x2 - sl * 
                                (closestCircEdgesVert2_y-closestCircEdgesVert1_y);
                        closestVertOfLine1_2_y = next_vertice_y2 + sl * 
                                (closestCircEdgesVert2_x-closestCircEdgesVert1_x);
                        //System.out.println("ORTH: (" + closestVertOfLine1_2_x + 
                        //", " + closestVertOfLine1_2_y + ")");
                        if(temp_distance < min_distance) {
                            if((Sub_algorithms.counterClockwiseTurn(
                                    next_vertice_x2, next_vertice_y2, 
                                    closestVertOfLine1_2_x, closestVertOfLine1_2_y, 
                                    closestCircEdgesVert1_x, closestCircEdgesVert1_y) >= 0) ^ (//xor 
                                    Sub_algorithms.counterClockwiseTurn(
                                    next_vertice_x2, next_vertice_y2, 
                                    closestVertOfLine1_2_x, closestVertOfLine1_2_y, 
                                    closestCircEdgesVert2_x, closestCircEdgesVert2_y) > 0)){
                                    // must be both cw and ccw turn, replace 0,0's 
                                if(temp_distance > 0.0) {
                                // vertices on the hull line will be ignored
                                    min_distance = temp_distance;
                                    temp_X = next_vertice_x2;
                                    temp_Y = next_vertice_y2;
                                    orth_found = true;
                                    /*System.out.println("Min distance (orthogonal): " + min_distance + 
                                        " between (" + next_vertice_x2 + ", " + next_vertice_y2 + ") and line from (" + 
                                        closestCircEdgesVert1_x + ", " + closestCircEdgesVert1_y + 
                                        ") to (" + closestCircEdgesVert2_x + ", " + closestCircEdgesVert2_y + ")");*/
                                }
                                if(temp_distance == 0.0) {
                                    //+ has to be in between the 2 points:
                                    if (Sub_algorithms.Euclidean_distance_squared(
                                            closestCircEdgesVert2_x, closestCircEdgesVert2_y, 
                                            next_vertice_x2, next_vertice_y2) + 
                                        Sub_algorithms.Euclidean_distance_squared(
                                                closestCircEdgesVert1_x, closestCircEdgesVert1_y, 
                                                next_vertice_x2, next_vertice_y2) == 
                                        Sub_algorithms.Euclidean_distance_squared(
                                                closestCircEdgesVert2_x, closestCircEdgesVert2_y, 
                                                closestCircEdgesVert1_x, closestCircEdgesVert1_y)){
                                        min_distance = temp_distance;
                                        temp_X = next_vertice_x2;
                                        temp_Y = next_vertice_y2;
                                        orth_found = true;
                                        /*System.out.println("Min distance (orthogonal): " + min_distance + 
                                            " between (" + next_vertice_x2 + ", " + next_vertice_y2 + ") and line from (" + 
                                            closestCircEdgesVert1_x + ", " + closestCircEdgesVert1_y + 
                                            ") to (" + closestCircEdgesVert2_x + ", " + closestCircEdgesVert2_y + ")");*/
                                    }
                                    //else {
                                    //    System.out.println("Distance was 0 but the vertice was not in between the line");
                                    //}
                                }
                            }
                            //else {
                                //System.out.println("Orthogonal point outside the circ.");
                            //}
                        }
                        if(temp_distance == min_distance) {
                            only_1_selected_when_several_equal_min_dist = true;
                        }
                    }
                }
                
                if(orth_found) {
                    next_vertice_x = temp_X;
                    next_vertice_y = temp_Y;
                }
                else {
                    next_vertice_x = (double)coordinates_x2.get(next_v_index_k);
                    next_vertice_y = (double)coordinates_y2.get(next_v_index_k);
                }
                //System.out.println("The next vertice is now decided: (" + 
                //        next_vertice_x + ", " + next_vertice_y + ")");
                //if(next_vertice_x == Double.MAX_VALUE || 
                //   next_vertice_y == Double.MAX_VALUE) {
                //    System.err.println("Next vertice not chosen");
                //} 
                for(int j = 0; j < max; j++) {
                    if(coordinates_x2.get(j).equals(next_vertice_x) && 
                       coordinates_y2.get(j).equals(next_vertice_y) ) {
                        circumferenceVertices[j] = true;
                        coordinates_x2.set(j, Double.MAX_VALUE);
                        coordinates_y2.set(j, Double.MAX_VALUE);
                        //System.out.println("MATCH: " + coordinates_x2.get(j) + 
                        //        ", " + coordinates_y2.get(j) + 
                        //        "\t " + j + " was marked true in the list");
                    }
                }
                
                // slope (y2 - y1) / (x2 - x1) where x2 - x1 != 0 
                // point's (x0, y0) distance from a line (a*x+b*y+c) 
                // is abs(|a*x0 + b*y0 + c |) / sqrt(a*a + b*b) 

                // The next vertice is now decided.
                // Next calculate (unrealistic squared) distances to the vertice
                // from 2 vertices and the edge "a" of "c+b-a" is in edges[][][], 
                // between these, discard the "a" of the the minimum "c+b-a" set
                double min_weight_triangle = Double.MAX_VALUE; 
                double c1_x;
                double c1_y;    // circumference vertice 1
                double c2_x;
                double c2_y;    // circumference vertice 2
                double c1_x_to_take = Double.MAX_VALUE;
                double c1_y_to_take = Double.MAX_VALUE;
                double c2_x_to_take = Double.MAX_VALUE;
                double c2_y_to_take = Double.MAX_VALUE;
                double C;
                double B;       // edge lengths
                double A;
                
                for(int j = 0; j < amount_of_circ_vert; j++) {
                    amount_of_triangle_calc_total++;
                    c1_x = edges[j][0][0]; //index, boolean isY, boolean isEnd
                    c1_y = edges[j][1][0];
                    c2_x = edges[j][0][1];
                    c2_y = edges[j][1][1];
                    C = Sub_algorithms.Euclidean_distance_squared(
                        next_vertice_x, next_vertice_y, 
                        c1_x, c1_y);
                    B = Sub_algorithms.Euclidean_distance_squared(
                        next_vertice_x, next_vertice_y, 
                        c2_x, c2_y);
                    A = Sub_algorithms.Euclidean_distance_squared(
                        c1_x, c1_y, c2_x, c2_y);
                    double triangle_weight = C + B - A;
                    if(triangle_weight < min_weight_triangle) {
                        min_weight_triangle = triangle_weight;
                        c1_x_to_take = c1_x;
                        c1_y_to_take = c1_y;
                        c2_x_to_take = c2_x;
                        c2_y_to_take = c2_y;
                    }
                }
                // new vertice (next_v_index) will be marked as a 
                // new circumference vertice,
                // two edges will be added to edges[][][] as one is left out
                
                /*find a line in edges that has both c1 and c2, delete it,
                move the rest of the list forward by 1 step each, 
                if c1 was first, add    c1_to_take -> next_vertice  to index
                                        next_vertice -> c2_to_take  to index+1
                if c2 was first, add    c2_to_take -> next_vertice  to index
                                        next_vertice -> c1_to_take  to index+1 
                mark circumferenceVertices[j] = true */
                
                boolean c1_was_first = false;
                for(int j = 0; j < max -1; j++) {
                    if(edges[j][0][0] == c1_x_to_take && 
                       edges[j][1][0] == c1_y_to_take && 
                       edges[j][0][1] == c2_x_to_take && 
                       edges[j][1][1] == c2_y_to_take) {        // c1 is first
                        c1_was_first = true;
                        edges[j][0][0] = Double.MAX_VALUE;
                        edges[j][0][1] = Double.MAX_VALUE;
                        edges[j][1][0] = Double.MAX_VALUE;
                        edges[j][1][1] = Double.MAX_VALUE;  // edge A deleted
                        for(int k = max -2; k >= j; k--) {
                            double temp_1 = edges[k+1][0][0];
                            double temp_2 = edges[k+1][1][0];
                            double temp_3 = edges[k+1][0][1];
                            double temp_4 = edges[k+1][1][1];
                            edges[k+1][0][0] = edges[k][0][0];
                            edges[k+1][1][0] = edges[k][1][0];
                            edges[k+1][0][1] = edges[k][0][1];
                            edges[k+1][1][1] = edges[k][1][1];
                            edges[k][0][0] = temp_1;
                            edges[k][1][0] = temp_2;
                            edges[k][0][1] = temp_3;
                            edges[k][1][1] = temp_4;
                        }
                    }
                    else {
                        if(edges[j][0][0] == c2_x_to_take && 
                           edges[j][1][0] == c2_y_to_take && 
                           edges[j][0][1] == c1_x_to_take && 
                           edges[j][1][1] == c1_y_to_take) {    // c2 is first
                            edges[j][0][0] = Double.MAX_VALUE;
                            edges[j][0][1] = Double.MAX_VALUE;
                            edges[j][1][0] = Double.MAX_VALUE;
                            edges[j][1][1] = Double.MAX_VALUE;// edge A deleted
                            for(int k = max -2; k >= j; k--) {
                                double temp_1 = edges[k+1][0][0];
                                double temp_2 = edges[k+1][1][0];
                                double temp_3 = edges[k+1][0][1];
                                double temp_4 = edges[k+1][1][1];
                                edges[k+1][0][0] = edges[k][0][0];
                                edges[k+1][1][0] = edges[k][1][0];
                                edges[k+1][0][1] = edges[k][0][1];
                                edges[k+1][1][1] = edges[k][1][1];
                                edges[k][0][0] = temp_1;
                                edges[k][1][0] = temp_2;
                                edges[k][0][1] = temp_3;
                                edges[k][1][1] = temp_4;
                            }
                            
                        }
                    }
                }
                for(int j = 0; j < max -1; j++) {
                    if(c1_was_first) {
                        if(edges[j][0][0] >= Double.MAX_VALUE) {
                            edges[j][0][0] = c1_x_to_take;
                            edges[j][1][0] = c1_y_to_take;
                            edges[j][0][1] = next_vertice_x;
                            edges[j][1][1] = next_vertice_y;
                            edges[j+1][0][0] = next_vertice_x;
                            edges[j+1][1][0] = next_vertice_y;
                            edges[j+1][0][1] = c2_x_to_take;
                            edges[j+1][1][1] = c2_y_to_take;
                            j = max;
                        }
                    }
                    else {
                        if(edges[j][0][0] >= Double.MAX_VALUE) {
                            edges[j][0][0] = c2_x_to_take;
                            edges[j][1][0] = c2_y_to_take;
                            edges[j][0][1] = next_vertice_x;
                            edges[j][1][1] = next_vertice_y;
                            edges[j+1][0][0] = next_vertice_x;
                            edges[j+1][1][0] = next_vertice_y;
                            edges[j+1][0][1] = c1_x_to_take;
                            edges[j+1][1][1] = c1_y_to_take;
                            j = max;
                        }
                    }
                }
            }
            
            // finally, make the solution string and calculate the tour length:
            String rows3[] = new String[500000];    
            ArrayList coordinates3 = new ArrayList();
            i = 0;
            String str3;
            BufferedReader reader3 = new BufferedReader(new StringReader(input));
            try {
                while ((str3 = reader3.readLine()) != null) {
                    if (str3.length() > 0) {
                        rows3[i] = str3;
                        if(rows3[i].charAt(0) == '0' || 
                                rows3[i].charAt(0) == '1' ||
                                rows3[i].charAt(0) == '2' ||
                                rows3[i].charAt(0) == '3' ||
                                rows3[i].charAt(0) == '4' ||
                                rows3[i].charAt(0) == '5' ||
                                rows3[i].charAt(0) == '6' ||
                                rows3[i].charAt(0) == '7' ||
                                rows3[i].charAt(0) == '8' ||
                                rows3[i].charAt(0) == '9') {
                            try {
                                double numberInput;
                                int endIndex;
                                for (int beginIndex = 0; beginIndex <
                                        rows3[i].length(); 
                                        beginIndex = endIndex + 1) {
                                    endIndex = rows3[i].indexOf(" ", 
                                            beginIndex);
                                    if (endIndex == -1) {
                                        endIndex = rows3[i].length();
                                    }
                                    String numberString = rows3[i].substring(
                                            beginIndex, endIndex);
                                    try {
                                        numberInput = Double.
                                                parseDouble(numberString);
                                        coordinates3.add(numberInput);
                                    } 
                                    catch (java.lang.
                                            NumberFormatException nfe) {
                                        System.err.println(nfe);
                                    }
                                }
                            }
                            catch(Exception e) {
                                System.err.println(e);
                            }
                        }   
                    }
                }
            } 
            catch(IOException e) {
                System.err.println(e);
            }
            // coordinates3 has now the original coordinates,
            // when they match with the tsp result in edges, add to solution,
            // at the same time += tour length
            
            ArrayList coordinates_x3 = new ArrayList();
            ArrayList coordinates_y3 = new ArrayList();
            for(int j = 0; j < coordinates3.size(); j++) {
                if(j%3 == 0 || j == 0) {    // do nothing 
                }
                if((j - 1)%3 == 0) {
                    coordinates_x3.add(coordinates3.get(j));
                }
                if((j - 2)%3 == 0) {
                    coordinates_y3.add(coordinates3.get(j));
                }
            }
            
            match_count = 0;
            int first_index = 0;
            double startX = Double.MAX_VALUE;
            double startY = Double.MAX_VALUE;
            double lastX = Double.MAX_VALUE;
            double lastY = Double.MAX_VALUE;
            for(int j = 0; j < max; j++) {
                for(int k = 0; k < max; k++) {
                    if(coordinates_x3.get(k).equals(edges[j][0][0]) && 
                       coordinates_y3.get(k).equals(edges[j][1][0]) && 
                       edges[j][0][0] < Double.MAX_VALUE && 
                       edges[j][1][0] < Double.MAX_VALUE) {
                        if(match_count > 0) {
                            solution = new StringBuilder(solution).append("-").toString();
                            tour_length += Sub_algorithms.Euclidean_distance(
                                    edges[j][0][0], edges[j][1][0], 
                                    lastX, lastY);
                        }
                        else {
                            first_index = k+1;
                            startX = edges[j][0][0];
                            startY = edges[j][1][0];
                        }
                        lastX = edges[j][0][0];
                        lastY = edges[j][1][0];
                        match_count++;
                        solution = new StringBuilder(solution).append(k+1).toString();
                    }
                }
            }
            // connection back to the last vertex 
            // and the last addition to tour length 
            solution = new StringBuilder(solution).append("-").toString();
            solution = new StringBuilder(solution).append(first_index).toString();
            tour_length += Sub_algorithms.Euclidean_distance(
                    lastX, lastY, startX, startY);
            
            long endTime = System.nanoTime();
            System.out.println("Time (millisec): " + (endTime - startTime)/1000000);
            System.out.println("Time (sec, rounded down): " + (endTime - startTime)/1000000000);
            long vspt = max * max * inner_v_rounds;
            System.out.println("Inner vertice rounds: " + inner_v_rounds);
            System.out.println("Vertices squared part total: " + vspt);
            System.out.println("Inner vertice handles total: " + amount_of_inner_vert_total);
            System.out.println("Handles in the for loop where cir in inn: " + 
                    amount_of_cir_calcs_in_inn_calcs);
            System.out.println("Triangle calculations total: " + 
                    amount_of_triangle_calc_total + "\n");
            
            if(only_1_selected_when_several_equal_min_dist == false
                    || more_than_1_inner == false) {
                return hullResult + "\nSolution: \n" + solution + 
                    "\nTour length: " + tour_length;
            }
            else {
                return hullResult + "\nSolution: \n" + solution + 
                    "\nTour length: " + tour_length + 
                    "\nNOTE: If non-hull-nodes would have been taken"
                    + " in different order when equal min distances"
                    + " exist, the result might be better";
            }
        }
        else {
            return ERRORMSG;
        }
    }
} 