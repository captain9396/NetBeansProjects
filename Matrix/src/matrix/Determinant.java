/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;



public class Determinant {
  
    private int totalMultiplications = 0; 
    
    
    
    
    public int solveDeterminant(Matrix dataMatrix){
        if(dataMatrix.getOrder() == 2){
            totalMultiplications++;
            return dataMatrix.twoByTwoDeterminant();
        }
        
        int value = 0;
        int[][] parentMatrix = dataMatrix.getSquareMatrix();
        
        int bit = 1;
        for(int j = 0 ; j < parentMatrix[0].length ; j++){
            Matrix childMatrix = new Matrix(dataMatrix.getOrder() - 1);
            
            childMatrix.generateMatrix(dataMatrix, j);
           
            value += (parentMatrix[0][j] * solveDeterminant(childMatrix) * bit);
            if(bit == 1) bit = -1;
            else bit = 1;
        }
        
        return value;
    }

    public int getTotalMultiplications() {
        return totalMultiplications;
    }
    
    
    
    
    
}
