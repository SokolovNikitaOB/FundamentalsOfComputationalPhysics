package tasks;

import org.jfree.chart.axis.LogAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;
import usefulClasses.Function;

import java.util.Scanner;

/**
 * Задание 3 <br>
 * Вычислить интегралы<br>
 * Integrate(1/(1+x<sup>2</sup>)) from -1 to 1,<br>
 * Integrate(x<sup>1/3</sup>e<sup>sin x</sup>) from 0 to 1<br>
 * методами трапеций и Симпсона, разделив отрезок интегрирования на 4, 8, 16, ... интервалов.
 * Как убывает погрешность численного интегрирования с ростом числа интервалов?
 */
public class Task3 {

    public static void main(String[] args) {
        System.out.println("Введите степень двойки: ");
        Scanner in = new Scanner(System.in);
        int degreeOfTwo = in.nextInt();

        Function functionFirst = new FunctionFirst();
        System.out.println("Трапеция для первого интеграла - " + calculateIntegrateByTrapezoidMethod(degreeOfTwo,-1,1, functionFirst));
        System.out.println("Симпсон для первого интеграла - " + calculateIntegrateBySimpsonMethod(degreeOfTwo,-1,1, functionFirst));
        new LineChartEx("Programme for first integrate",createDatasetFirst(degreeOfTwo, functionFirst), "Зависимость погрешности от степени двойки", "Степень двойки", "Погрешность",2, new LogAxis()).setVisible(true);

        Function functionSecond = new FunctionSecond();
        System.out.println("Трапеция для второго интеграла - " + calculateIntegrateByTrapezoidMethod(degreeOfTwo,0,1,functionSecond));
        System.out.println("Симпсон для второго интеграла - " + calculateIntegrateBySimpsonMethod(degreeOfTwo,0,1,functionSecond));


    }

    public static XYDataset createDatasetFirst(int maxDegree, Function function) {
        XYSeries series = new XYSeries("Метод трапеции");
        XYSeries series1 = new XYSeries("Метод Симпсона");
        for (int i = 1; i < maxDegree; i++) {
            series.add(i,Math.abs(Math.PI / 2 - Task3.calculateIntegrateByTrapezoidMethod(i,-1,1,function)));
            series1.add(i,Math.abs(Math.PI / 2 - Task3.calculateIntegrateBySimpsonMethod(i,-1,1,function)));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);
        return dataset;
    }

    /**
     * Метод трапеции
     */
    public static double calculateIntegrateByTrapezoidMethod(int powerTwo, double min, double max, Function function, double ...parameters){
        double count = Math.pow(2,powerTwo);
        double lengthOne = (max - min)/count;
        double result = 0.;
        for (int i = 0; i < count; i++) {
            result += (function.getValue(min + i * lengthOne,parameters) + function.getValue(min + (i + 1) * lengthOne,parameters)) / 2;
        }
        return result * lengthOne;
    }

    /**
     * Метод Симпсона
     */
    public static double calculateIntegrateBySimpsonMethod(int powerTwo, double min, double max, Function function, double ... parameters){
        double count = Math.pow(2,powerTwo);
        double lengthOne = (max - min)/count;
        double result = 0.;
        result += function.getValue(min,parameters);
        result += function.getValue(max,parameters);
        for (int i = 1; i <= count/2 - 1; i++) {
            result += 2 * function.getValue(min + 2 * i * lengthOne,parameters);
        }
        for (int i = 1; i <= count/2; i++) {
            result += 4 * function.getValue(min + (2 * i - 1) * lengthOne,parameters);
        }
        return result * lengthOne / 3;
    }

    static class FunctionFirst implements Function{
        @Override
        public double getValue(double point, double... parameters) {
            return 1. / ( 1 + point * point);
        }
    }

    static class FunctionSecond implements Function{
        @Override
        public double getValue(double point, double... parameters) {
            return Math.pow(point,1./3) * Math.exp(Math.sin(point));
        }
    }
}
