/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

public class Matrix {
    private int order;
    private int[][] squareMatrix;
    private Matrix thisMatrix;

    public Matrix(int order) {
        this.order = order;
        squareMatrix = new int[order][order];
    }

    public void setSquareMatrix(int[][] squareMatrix) {
        this.squareMatrix = squareMatrix;
    }
    
    
    
    
    
    
    
    
    public void generateMatrix(Matrix parentMatrix, int exceptThisColumn){
        int x, y;
        x = y = 0;
        int[][] sqmat = parentMatrix.getSquareMatrix();
//        parentMatrix.printMatrix();
        
        for(int i = 1; i < sqmat.length ; i++){
            y = 0;
            for(int j = 0; j < sqmat[i].length ; j++ ){
                if(j != exceptThisColumn){
                    squareMatrix[x][y] = sqmat[i][j];
                    
                    y++;
                }
            }
            x++;
       
        }
    }
    
    
    
    
    public void printMatrix(){
        for(int[] x: squareMatrix){
            for(int y: x){
                System.out.print(y + " ");
            }
            System.out.println("");
        }
    }
    
    
    public int[][] getSquareMatrix(){ return squareMatrix;}
    
    
    public int twoByTwoDeterminant(){
        
        return (squareMatrix[0][0] * squareMatrix[1][1]) - (squareMatrix[0][1] * squareMatrix[1][0]);
    }

    public int getOrder() {
        return order;
    }
    
    
}
