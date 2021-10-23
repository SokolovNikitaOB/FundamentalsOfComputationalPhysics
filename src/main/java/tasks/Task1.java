package tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Задача 1<br>
 * Машинным ε называется такое число, что 1 + ε/2 = 1, но 1 + ε != 1.<br>
 * Найти машинное ε, число разрядов в мантиссе, максимальную и минимальную степени,<br>
 * при вычислениях с обычной и двойной точностью. <br>
 * Сравнить друг с другом четыре числа: 1, 1 + ε/2, 1 + ε и 1 + ε + ε/2.
 */
public class Task1 {
    public static int mantisFloat = 0;
    public static int mantisDouble = 0;

    public static void main(String[] args) {
        double epsilonDouble =  calculateEpsilonDouble(1.);
        float epsilonFloat = (float) calculateEpsilonFloat(1f);
        System.out.println("Машинное эпсилон double: " + epsilonDouble);
        System.out.println("Число разрядов в мантиссе double: " + (mantisDouble - 1) );
        System.out.println("Машинное эпсилон float: " +  epsilonFloat);
        System.out.println("Число разрядов в мантиссе float: " + (mantisFloat - 1) );

        System.out.println("1 and 1 + E/2 " + (1 == 1 + epsilonFloat/2));
        System.out.println("1 + E and 1 + E/2 + E " + (1 + epsilonFloat == 1 + epsilonFloat + epsilonFloat/2));
        System.out.println("1 + E + E/2 and 1 " + (1 + epsilonFloat + epsilonFloat/2 == 1));
    }

    public static double calculateEpsilonDouble(double epsilon){
        mantisDouble++;
        return 1 + epsilon/2 == 1 && 1 + epsilon != 1 ? epsilon : calculateEpsilonDouble(epsilon/2);
    }
    public static double calculateEpsilonFloat(float epsilon){
        mantisFloat++;
        return 1 + epsilon/2 == 1 && 1 + epsilon != 1 ? epsilon : calculateEpsilonFloat(epsilon/2);
    }
}



