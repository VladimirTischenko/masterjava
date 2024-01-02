package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        List<Callable<int[]>> callables = new ArrayList<>();

        for (int i = 0; i < matrixSize; i++) {
            int finalI = i;

            Callable<int[]> task = () -> {
                final int[] matrixT = new int[matrixSize];

                for (int j = 0; j < matrixSize; j++) {
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += matrixA[finalI][k] * matrixB[k][j];
                    }
                    matrixT[j] = sum;
                }
                return matrixT;
            };

            callables.add(task);

        }

        List<Future<int[]>> futures = executor.invokeAll(callables);
        for (int i = 0; i < matrixSize; i++) {
            matrixC[i] = futures.get(i).get();
        }

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            final int[] columnB = new int[matrixSize];
            for (int l = 0; l < matrixSize; l++) {
                columnB[l] = matrixB[l][i];
            }
            for (int j = 0; j < matrixSize; j++) {
                final int[] rowA = matrixA[j];

                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += rowA[k] * columnB[k];
                }
                matrixC[j][i] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}