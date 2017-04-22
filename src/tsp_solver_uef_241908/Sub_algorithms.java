package tsp_solver_uef_241908;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * 
 * The sub algorithm class.
 * 
 * NetBeans 8
 * 
 * @author tuohy
 * @version 14-3-17
 */
public class Sub_algorithms {
    
    /**
     * Calculate real Euclidean distance. 
     * @param x1 double
     * @param y1 double
     * @param x2 double
     * @param y2 double
     * @return d double
     */
    public static double Euclidean_distance(
            double x1, double y1, double x2, double y2) {
        double d = Math.sqrt(((x2 - x1)*(x2 - x1))+((y2 - y1)*(y2 - y1)));
        return d;
    }
    
    /**
     * Calculate Euclidean distance without sqrt. 
     * @param x1 double
     * @param y1 double
     * @param x2 double
     * @param y2 double
     * @return d double
     */
    public static double Euclidean_distance_squared(
            double x1, double y1, double x2, double y2) {
        double d = ((x2 - x1)*(x2 - x1))+((y2 - y1)*(y2 - y1));
        return d;
    }
    
    /**
     * Generate convex hull with Graham scan.
     * @param min integer
     * @param max integer
     * @param x_coordinates ArrayList
     * @param y_coordinates ArrayList
     * @return res String
     */
    public static String ConvexHull(int min, int max, 
            ArrayList x_coordinates, ArrayList y_coordinates) {
        double tempX;
        double tempY;
        for(int i = 0; i < y_coordinates.size()-1; i++) {
            for(int j = i+1; j < y_coordinates.size(); j++) {
                if((double)y_coordinates.get(i) > (double)y_coordinates.get(j)) {
                    tempX = (double)x_coordinates.get(j);
                    tempY = (double)y_coordinates.get(j);
                    x_coordinates.set(j, (double)x_coordinates.get(i));
                    y_coordinates.set(j, (double)y_coordinates.get(i));
                    x_coordinates.set(i, tempX);
                    y_coordinates.set(i, tempY);
                }
                if((double)x_coordinates.get(i) < (double)x_coordinates.get(j)) {
                    tempX = (double)x_coordinates.get(j);
                    tempY = (double)y_coordinates.get(j);
                    x_coordinates.set(j, (double)x_coordinates.get(i));
                    y_coordinates.set(j, (double)y_coordinates.get(i));
                    x_coordinates.set(i, tempX);
                    y_coordinates.set(i, tempY);
                }
            }
        }
        
        double[][][] result = new double[max][2][2];  // pointer, isY?, isEnd? 
        String[][][] result2;
        double[][] handleVerticesInThisOrder = new double[max][2];
        double temp_max_x = Double.MIN_VALUE;
        for(int i = 0; i < max; i++) {  // search for the rightmost vertice
            if((double)x_coordinates.get(i) > temp_max_x){
                temp_max_x = (double)x_coordinates.get(i);
            }
        }
        double temp_min_y = Double.MAX_VALUE;
        int index_of_downmost_of_rightmosts = -1;
        for(int i = 0; i < max; i++) {
            if((double)x_coordinates.get(i) == temp_max_x) {
                if((double)y_coordinates.get(i) < temp_min_y) {
                    temp_min_y = (double)y_coordinates.get(i);
                    index_of_downmost_of_rightmosts = i;
                }
            }
        }
        
        // calculate the slopes by comparing every other point 
        // with the first ("rightmostdownmost") point 
        // (y2 - y1) / (x2 - x1) from smallest to biggest 
        //                       but first the ones with same x coordinates 
        //                       and from min y to max y 
        
        for(int j = 0; j < max-1; j++) {
            int tempminindex = j;
            for(int k = j+1; k < max; k++) {
                if((double)y_coordinates.get(k) < (double)y_coordinates.get(tempminindex)) {
                    if((double)x_coordinates.get(k) == temp_max_x) {
                        tempminindex = k;
                    }
                }
            }
            if(tempminindex != j && (double)x_coordinates.get(j) == temp_max_x) {   
            // if a change to tempminindex was made, swap 
                if((double)y_coordinates.get(tempminindex) < 
                   (double)y_coordinates.get(index_of_downmost_of_rightmosts)) {
                    double pivot;

                    pivot = (double)y_coordinates.get(tempminindex);
                    y_coordinates.set(tempminindex, y_coordinates.get(j));
                    y_coordinates.set(j, pivot);

                    pivot = (double)x_coordinates.get(tempminindex);
                    x_coordinates.set(tempminindex, x_coordinates.get(j));
                    x_coordinates.set(j, pivot);
                    
                    index_of_downmost_of_rightmosts = tempminindex;
                }
            }
        } 
        //System.out.println("\n\n\nDownmost of rightmosts is " + 
        //   (double) x_coordinates.get(index_of_downmost_of_rightmosts) + 
        //   ", " + (double) y_coordinates.get(index_of_downmost_of_rightmosts));
        
        handleVerticesInThisOrder[0][0] = 
                (double) x_coordinates.get(index_of_downmost_of_rightmosts);
        handleVerticesInThisOrder[0][1] = 
                (double) y_coordinates.get(index_of_downmost_of_rightmosts);
        
        // take the max Xs coordinates and after that,
        // calculate slopes and take them also
        int index_with_handleVerticesInThisOrder = 0;
        for(int i = 0; i < max; i++) {
            if((double)x_coordinates.get(i) == temp_max_x){
                handleVerticesInThisOrder[index_with_handleVerticesInThisOrder]
                        [0] = (double)x_coordinates.get(i);
                handleVerticesInThisOrder[index_with_handleVerticesInThisOrder]
                        [1] = (double)y_coordinates.get(i);
                index_with_handleVerticesInThisOrder++;
            }
        }
        boolean no_slope = false;
        for(int j = 0; j < max-1; j++) {
            if((double)x_coordinates.get(j) != temp_max_x) {
                
                double slope_min = ((double)y_coordinates.get(j) - 
                        (double)y_coordinates.get(index_of_downmost_of_rightmosts)) / 
                                   ((double)x_coordinates.get(j) - 
                        (double)x_coordinates.get(index_of_downmost_of_rightmosts));
                int temp_min_index = j;
                for(int k = j+1; k < max; k++) {
                    double slope = ((double)y_coordinates.get(k) - 
                            (double)y_coordinates.get(index_of_downmost_of_rightmosts)) / 
                                   ((double)x_coordinates.get(k) - 
                            (double)x_coordinates.get(index_of_downmost_of_rightmosts));
                    if(slope < slope_min && no_slope == false) {
                        if((double)x_coordinates.get(k) != temp_max_x) {
                            slope_min = slope;
                            temp_min_index = k;
                            //System.out.println("slope_min = " + 
                            //slope_min + " between (" + 
                            //        (double)x_coordinates.get(temp_min_index) 
                            //+ ", " + 
                            //        (double)y_coordinates.get(temp_min_index) 
                            //+ ") and Downmost of rightmost");
                        }
                    }
                    no_slope = false;
                }
                if(temp_min_index != j && 
                        (double)x_coordinates.get(j) != temp_max_x ) {
                    double temp;
                    temp = (double)y_coordinates.get(temp_min_index);
                    y_coordinates.set(temp_min_index, y_coordinates.get(j));
                    y_coordinates.set(j, temp);
                    temp = (double)x_coordinates.get(temp_min_index);
                    x_coordinates.set(temp_min_index, x_coordinates.get(j));
                    x_coordinates.set(j, temp);
                    //System.out.println("swapped " + x_coordinates.get(temp_min_index) + 
                    //", " + y_coordinates.get(temp_min_index) + 
                    //        " AND " + x_coordinates.get(j) + ", " + y_coordinates.get(j));
                    //System.out.println("\n\n\ndownmost of rightmosts is " + 
                    //(double) x_coordinates.get(index_of_downmost_of_rightmosts) + 
                    //", " + (double) y_coordinates.get(index_of_downmost_of_rightmosts));
                }
            }
        }
        for(int i = 0; i < max; i++) {
            if((double)x_coordinates.get(i) != temp_max_x ) {
                handleVerticesInThisOrder[index_with_handleVerticesInThisOrder][0] = 
                        (double)x_coordinates.get(i);
                handleVerticesInThisOrder[index_with_handleVerticesInThisOrder][1] = 
                        (double)y_coordinates.get(i);
                index_with_handleVerticesInThisOrder++;
            }
        }
        for(int i = 0; i < handleVerticesInThisOrder.length -1; i++) {
            for(int j = i; j < handleVerticesInThisOrder.length; j++) {
                // if some slopes are equal (or there was no slope), sort by distance 
                // (y2 - y1) / (x2 - x1) 
                double slope1 = (handleVerticesInThisOrder[i][1] - 
                        (double)y_coordinates.get(index_of_downmost_of_rightmosts)) / 
                                (handleVerticesInThisOrder[i][0] - 
                        (double)x_coordinates.get(index_of_downmost_of_rightmosts)); 
                double slope2 = (handleVerticesInThisOrder[j][1] - 
                        (double)y_coordinates.get(index_of_downmost_of_rightmosts)) / 
                                (handleVerticesInThisOrder[j][0] - 
                        (double)x_coordinates.get(index_of_downmost_of_rightmosts));
                if(slope1 == slope2) {
                    double distance1 = Euclidean_distance_squared( 
                        // x1, y1, x2, y2 
                            (double)x_coordinates.get(index_of_downmost_of_rightmosts), 
                            (double)y_coordinates.get(index_of_downmost_of_rightmosts), 
                            handleVerticesInThisOrder[i][0], 
                            handleVerticesInThisOrder[i][1]);
                    double distance2 = Euclidean_distance_squared( 
                            (double)x_coordinates.get(index_of_downmost_of_rightmosts), 
                            (double)y_coordinates.get(index_of_downmost_of_rightmosts), 
                            handleVerticesInThisOrder[j][0], 
                            handleVerticesInThisOrder[j][1]);
                    //System.out.println("slope1: " + slope1 + ", slope2: " + slope2);
                    if((distance1 < distance2) &&   // vai > ? 
                        (handleVerticesInThisOrder[i][0] != temp_max_x) && 
                        (handleVerticesInThisOrder[j][0] != temp_max_x)) {
                        // swap "handleVerticesInThisOrder[i][0], 
                        //       handleVerticesInThisOrder[i][1]" 
                        //      and "handleVerticesInThisOrder[j][0], 
                        //           handleVerticesInThisOrder[j][1]" 
                        double temp;
                        temp = handleVerticesInThisOrder[i][0];
                        handleVerticesInThisOrder[i][0] = handleVerticesInThisOrder[j][0];
                        handleVerticesInThisOrder[j][0] = temp;
                        temp = handleVerticesInThisOrder[i][1];
                        handleVerticesInThisOrder[i][1] = handleVerticesInThisOrder[j][1];
                        handleVerticesInThisOrder[j][1] = temp;
                    }
                }
            }
        }
        /*for (double[] handleVerticesInThisOrder1 : handleVerticesInThisOrder){
            System.out.println("handleVerticesInThisOrder: " 
                    + handleVerticesInThisOrder1[0] + ", " + 
                    handleVerticesInThisOrder1[1]);
        }*/
        // the sorting part ends here
    
        // handle vertices and if not a counter-clockwise turn or 
        // 0 (straight line), repair connections 
        DoubleStack hulls_x_coordinates = new DoubleStack();
        DoubleStack hulls_y_coordinates = new DoubleStack();
        hulls_x_coordinates.push(handleVerticesInThisOrder[0][0]);
        hulls_y_coordinates.push(handleVerticesInThisOrder[0][1]);
        hulls_x_coordinates.push(handleVerticesInThisOrder[1][0]);
        hulls_y_coordinates.push(handleVerticesInThisOrder[1][1]);
        for(int i = 2; i < max; i++) {
            double top_x = hulls_x_coordinates.top();
            hulls_x_coordinates.pop();
            double top_y = hulls_y_coordinates.top();
            hulls_y_coordinates.pop();
            while (counterClockwiseTurn(hulls_x_coordinates.top(), 
                    hulls_y_coordinates.top(), 
                    top_x, top_y, handleVerticesInThisOrder[i][0], 
                    // discard if cw 
                    handleVerticesInThisOrder[i][1]) <= 0 &&
                    hulls_y_coordinates.isEmpty() == false) {
                
                        top_x = hulls_x_coordinates.top();
                        hulls_x_coordinates.pop();
                        top_y = hulls_y_coordinates.top();
                        hulls_y_coordinates.pop();
            }
            hulls_x_coordinates.push(top_x);
            hulls_y_coordinates.push(top_y);
            hulls_x_coordinates.push(handleVerticesInThisOrder[i][0]);
            hulls_y_coordinates.push(handleVerticesInThisOrder[i][1]);
        }
        for(int i = 0; i < max; i++) {
            result[i][0][0] = hulls_x_coordinates.top();
            hulls_x_coordinates.pop();
            result[i][1][0] = hulls_y_coordinates.top(); 
            hulls_y_coordinates.pop();
        }
        // generate a cycle of the vertices 
        int lastIndex = 0;
        for(int i = 0; i < max -1; i++) {
            if(result[i+1][0][0] < Double.MAX_VALUE && 
                    result[i+1][1][0] < Double.MAX_VALUE){
                lastIndex = i+1;
                result[i][0][1] = result[i+1][0][0]; // index, isY, isEnd 
                result[i][1][1] = result[i+1][1][0];
            }
        }
        // complete the cycle:
        result[lastIndex][0][1] = result[0][0][0];
        result[lastIndex][1][1] = result[0][1][0];
        
        // if 3 on the same line at the end, delete the center:
        for(int i = 0; i < lastIndex -1; i++) {
            for(int j = 0; j < lastIndex; j++){
                for(int k = 0; k < lastIndex +1; k++){
                    if(     result[i][0][1] < Double.MAX_VALUE && 
                            result[j][0][1] < Double.MAX_VALUE && 
                            result[k][0][1] < Double.MAX_VALUE &&
                            i < j && j < k) {
                        if (Sub_algorithms.counterClockwiseTurn(
                                    result[i][0][1], result[i][1][1], 
                                    result[j][0][1], result[j][1][1], 
                                    result[k][0][1], result[k][1][1]) == 0){
                            result[k][0][0] = Double.MAX_VALUE;
                            result[k][1][0] = Double.MAX_VALUE;
                            result[i][0][0] = result[j][0][1];
                            result[i][1][0] = result[j][1][1];
                            result[k][0][1] = Double.MAX_VALUE;
                            result[k][1][1] = Double.MAX_VALUE;
                            // must remember to decrease the last index:
                            lastIndex--;
                        }
                    }
                }
            }
        }
        result2 = new String[lastIndex+1][2][2];
        
        for(int i = 0; i < max; i++) {
            if(result[i][0][1] < Double.MAX_VALUE && 
                    result[i][1][1] < Double.MAX_VALUE && 
                    result[i][0][0] < Double.MAX_VALUE && 
                    result[i][1][0] < Double.MAX_VALUE){
                result2[i][0][0] = String.valueOf(result[i][0][0]);
                result2[i][0][1] = String.valueOf(result[i][1][0]);
                result2[i][1][0] = String.valueOf(result[i][0][1]);
                result2[i][1][1] = String.valueOf(result[i][1][1]);
            }
        }
        String res = Arrays.deepToString(result2);
        return res;
    }
    
    /**
     * This method checks if a turn with 3 points is 
     * counterclockwise ( more than 0, return +1 ), 
     * clockwise ( less than 0, return -1 ) or collinear ( =0, return 0 ),
     * called by Graham ConvexHull generation.
     * @param x1 double
     * @param y1 double
     * @param x2 double
     * @param y2 double
     * @param x3 double
     * @param y3 double
     * @return ret_value integer
     */
    public static int counterClockwiseTurn(double x1, double y1, 
                                           double x2, double y2, 
                                           double x3, double y3) {
        int ret_value = -2;
        try {
            int area2 = (int) (((x2 - x1)*(y3 - y1)) - ((y2 - y1)*(x3 - x1)));
            if(area2 < 0) {
                ret_value =  -1;    //cw
            }
            else {
                if(area2 > 0) {
                    ret_value =  1; //ccw
                }
                else {
                    ret_value = 0;
                }
            }
        }
        catch(Exception e) {
            System.err.println(e);
        }
        return ret_value;
    }
    
    /**
     * Unused.
     * Checks if the first point has a smaller "angle" than the second point, 
     * from Skiena & Revilla (2003) page 318.
     * @param d1 double
     * @param d2 double
     * @param d3 double
     * @param d4 double
     * @param d5 double
     * @param d6 double
     * @return boolean
     */
    private static boolean smaller_angle(double d1, double d2, 
            double d3, double d4, double d5, double d6) {
        if(counterClockwiseTurn(d1, d2, d3, d4, d5, d6) == 0) {
            return Euclidean_distance_squared(d1, d2, d3, d4) > 
                   Euclidean_distance_squared(d1, d2, d5, d6);
        }
        return counterClockwiseTurn(d1, d2, d3, d4, d5, d6) != 1;
    }
    
    /**
     * Create a minimal spanning tree with Prim's algorithm.
     * 
     * @param min integer
     * @param max integer
     * @param x_coordinates ArrayList
     * @param y_coordinates ArrayList
     * @return result double[][][]
     */
    public static double[][][] MST_Prim(int min, int max, 
            ArrayList x_coordinates, ArrayList y_coordinates) {
        double[][][] result = new double[20000][2][2];// pointer, isY?, isEnd? 
        Random rand = new Random();
        int random = rand.nextInt((max - min) + 1) + min;
        boolean[] booltable = new boolean[max];
        result[0][0][0] = (double) x_coordinates.get(random -1);
        result[0][1][0] = (double) y_coordinates.get(random -1);
        int pointer = 0; 
        //int pointer_start = random -1;
        double X1 = 0;
        double Y1 = 0;
        double X2 = 0;
        double Y2 = 0;
        for(int i = 1; i < max; i++) {
            // check every connected node and find a min distance to 
            // the next unconnected node 
            booltable[random - 1] = true;
            double min_distance = Double.MAX_VALUE;
            double temp_distance;
            for(int h = 0; h < booltable.length; h++) {
                for(int j = 0; j < booltable.length; j++) {
                    if(booltable[j] == false && booltable[h] == true) {
                        double x1 = (double) x_coordinates.get(h);
                        double y1 = (double) y_coordinates.get(h);
                        double x2 = (double) x_coordinates.get(j);
                        double y2 = (double) y_coordinates.get(j);
                        temp_distance = Sub_algorithms.
                                Euclidean_distance_squared(x1, y1, x2, y2);
                        if(temp_distance < min_distance) {
                            min_distance = temp_distance;
                            pointer = j;
                            X1 = x1; Y1 = y1; X2 = x2; Y2 = y2; 
                        }
                    }
                }
            }
            booltable[pointer] = true; 
            result[i-1][0][0] = X1; 
            result[i-1][1][0] = Y1; 
            result[i-1][0][1] = X2; 
            result[i-1][1][1] = Y2; 
        }
        return result; 
    }
    
    /**
     * Do an Eulerian walk in MST and embed the tour.
     * @param edges double[][][]
     * @param edges_in_tsp_solution integer
     * @param max integer
     * @return result String[]
     */
    public static String[] Euler_and_embedded_tour(double edges[][][], 
            int edges_in_tsp_solution, int max) {
        String[] result = new String[edges_in_tsp_solution +1];
        String[] sub_result = new String[max];
        boolean[] isTaken = new boolean[max -1];
        boolean isTaken_has_all_true = false;
        int pointer = 0;
        isTaken[pointer] = true;
        sub_result[pointer] = String.valueOf(edges[pointer][0][1]) + " " + 
                String.valueOf(edges[pointer][1][1]); 
        String last_value = sub_result[pointer];
        String start_value = "";
        double last_x = edges[pointer][0][1]; 
        double last_y = edges[pointer][1][1]; 
        //for(int i = 0; i < max -1; i++) {
        //    System.out.println("from (" + edges[i][0][0] + ", " + edges[i][1][0]
        //    + ") to (" + edges[i][0][1] + ", " + edges[i][1][1] + ")");
        //} 
        
        // the first edge is ok, now connect the others: 
        while (isTaken_has_all_true == false) {
            for(int i = 1; i < max -1; i++) {
                if((((edges[i][0][0] == last_x) && 
                      (edges[i][1][0] == last_y))) && (isTaken[i] == false)) {
                    //check that a connection is made so that line doesn't cut 
                    isTaken[i] = true;
                    sub_result[pointer] = String.valueOf(edges[i][0][1]) + 
                            " " + String.valueOf(edges[i][1][1]); 
                    last_x = edges[i][0][1]; 
                    last_y = edges[i][1][1]; 
                    pointer++;
                }
                if(i == 1) {
                        start_value = String.valueOf(edges[i][0][0]) + 
                                " " + String.valueOf(edges[i][1][0]); 
                }
            }
            isTaken_has_all_true = true;
            for(int i = 0; i < max -1; i++) {
                if(isTaken[i] == false) {
                    isTaken_has_all_true = false;
                }
            }
        }
        // adding the last value and the first value + swapping the table 
        sub_result[max - 2] = last_value; 
        sub_result[max - 1] = start_value;
        String help_var;
        String first_val = "";
        for(int i = sub_result.length -2; i > -1; i--) { 
            if(i == sub_result.length -2) {
                first_val = sub_result[i];
            }
            help_var = sub_result[i+1];
            sub_result[i+1] = sub_result[i];
            sub_result[i] = help_var;
        }
        sub_result[0] = first_val;
        
        // embedding the tour:
        boolean first_has_appeared = false;
        for(int i = 0; i < sub_result.length; i++) {
            for(int j = i+1; j < sub_result.length; j++) {
                if(sub_result[i].equals(sub_result[j]) && 
                   !sub_result[i].equals("del")) {
                    if(first_has_appeared == false && sub_result[j].equals(
                            sub_result[0]) && j == sub_result.length -1) {
                        first_has_appeared = true;
                    }
                    else {
                        sub_result[j] = "del";
                    }
                }
            }
        }
        int j = 0;
        for (String sub_result1 : sub_result) {
            if (sub_result1.equals("del") == false) {
                result[j] = sub_result1;
                j++;
            }
        }
        return result;
    }
} 