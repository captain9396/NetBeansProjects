/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

/**
 *
 * @author User
 */
public class DeterminantTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Matrix matrix = new Matrix(7);
        int[][] arr = {
                {1,2,3,4,5,6,7},
                {9,8,7,6,5,4,3},
                {2,1,6,8,9,0,7},
                {9,6,4,5,6,7,7},
                {8,6,4,2,-9,7,8},
                {9,0,1,4,6,9,9},
                {0,7,-6,8,9,0,0}
                      };
        
        matrix.setSquareMatrix(arr);
        
        Determinant determinant = new Determinant();
        System.out.println(determinant.solveDeterminant(matrix));
        System.out.println("Total Multiplication done = " + determinant.getTotalMultiplications());
    }
    
}
