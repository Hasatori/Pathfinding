package pathfinding.view;

import pathfinding.model.Signs;

import java.util.*;

public class Prim {

    public static void main(String[] args) {
        // dimensions of generated maze
        int r = 10, c = 10;


        int[][] maz = new int[r][c];
        for (int x = 0; x < r; x++) {
            Arrays.fill(maz[x], Signs.WALL_SIGN.getSignValue());
        }



        // print final maze
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++)
                System.out.print(maz[i][j]);
            System.out.println();
        }
    }


}

