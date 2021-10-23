package tasks;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;
import usefulClasses.Polynomial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Задача 5<br>
 * Провести интерполяционный полином P<sub>n</sub>(x) через точки: <br>
 * x<sub>k</sub> = 1 + k / n, y<sub>k</sub> = ln (x<sub>k</sub>), k = 0, ..., n
 * при n = 4, ..., 15. <br>
 * Нарисовать графики P<sub>n</sub>(x) − ln (x).
 */
public class Task5 {
    public static void main(String[] args) {
        System.out.println("Введите n: ");
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        new LineChartEx(
                "Programme",
                createDataset(n),
                "График Pn(x)-ln(x)",
                "x",
                "y",
                1,
                new NumberAxis()
        ).setVisible(true);

        new LineChartEx(
                "Programme",
                createDatasetPn(n),
                "График Pn(x)",
                "x",
                "y",
                1,
                new NumberAxis()
        ).setVisible(true);

    }

    private static XYDataset createDataset(int n) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Pn(x)-ln(x)");
        List<Polynomial> polynomialList = getPolynomials(n);
        for (int j = 0; j < 1000; j++) {
            double x = 1 + 1. * j / 1000;
            series.add(x, Math.abs(Polynomial.getValueAtPointOfSeveralPolynomial(polynomialList, x) - Math.log(x)));
        }
        dataset.addSeries(series);
        return dataset;
    }

    private static XYDataset createDatasetPn(int n) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Pn(x)");
        List<Polynomial> polynomialList = getPolynomials(n);
        for (int j = 0; j < 1000; j++) {
            double x = 1 + 1. * j / 1000;
            series.add(x, Polynomial.getValueAtPointOfSeveralPolynomial(polynomialList, x));
        }
        dataset.addSeries(series);
        return dataset;
    }


    public static List<Polynomial> getPolynomials(int countPoint){
        HashMap<Double, Double> xkyk = getMapOfValues(countPoint);
        List<Polynomial> polynomialList = new ArrayList<>();
        for (int i = 0; i <= countPoint; i++) {
            List<Double> xList = new ArrayList<>(xkyk.keySet());
            double keyX = xList.get(i);
            xList.remove(keyX);
            Polynomial polynomial = new Polynomial(xList, 1);
            polynomialList.add(new Polynomial(xList, xkyk.get(keyX)/polynomial.getValueAtPoint(keyX) ));
        }
        return polynomialList;
    }

    public static HashMap<Double,Double> getMapOfValues(int countPoint){
        HashMap<Double, Double> map = new HashMap<>();
        for (int k = 0; k <= countPoint; k++) {
            double xk = 1 + 1. * k / countPoint;
            map.put(xk,Math.log(xk));
        }
        return map;
    }
}
