package tasks;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;
import usefulClasses.MethodForTwoDimensionalDifferentialEquation;
import usefulClasses.TwoDimensionalFunction;

import java.util.List;

/**
 * Задача 7<br>
 * Решить систему уравнений хищник-жертва<br>
 * x' = ax − bxy, y' = cxy − dy<br>
 * методом Рунге-Кутты второго порядка точности при a = 10, b = 2, c = 2, d = 10.<br>
 * Нарисовать фазовую траекторию.
 */
public class Task7 {
    public static void main(String[] args) {

        List<XYDataset> xyDatasets = createDataset();
        new LineChartEx(
                "Programme",
                xyDatasets.get(0),
                "Решение системы",
                "x",
                "y",
                2,
                new NumberAxis()
        ).setVisible(true);

        new LineChartEx(
                "Programme",
                xyDatasets.get(1),
                "Фазовая траектория",
                "x",
                "y",
                1,
                new NumberAxis()
        ).setVisible(true);
    }

    private static List<XYDataset> createDataset() {
        XYSeries series = new XYSeries("x(t)");
        XYSeries series1 = new XYSeries("y(t)");
        XYSeries series2 = new XYSeries("y(x)", false);

        double yn = 1;
        double ynMinusOne;

        double xn = 1;
        double xnMinusOne;

        for (int i = 0; i <= 10000; i++) {

            double t =  1. * i / 1000;
            ynMinusOne = yn;
            xnMinusOne = xn;
            series.add(t, yn);
            series1.add(t, xn);
            series2.add(xn,yn);
            yn = new RungeKutty2ForY().calculateNPlusOne(xnMinusOne,ynMinusOne,1./1000,5,5,3./4);
            xn = new RungeKutty2ForX().calculateNPlusOne(xnMinusOne,ynMinusOne,1./1000,6,5,3./4);

        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);
        XYSeriesCollection dataset1 = new XYSeriesCollection();
        dataset1.addSeries(series2);

        return List.of(dataset, dataset1);
    }

    static class RungeKutty2ForY implements MethodForTwoDimensionalDifferentialEquation {
        @Override
        public double calculateNPlusOne(double xn, double yn, double step, double... parameters) {
            TwoDimensionalFunction functionY = new FunctionDerivedY();
            double c = parameters[0];
            double d = parameters[1];
            double lambda = parameters[2];
            return yn + step * ((1 - lambda) * functionY.getValue(xn,yn,c,d) + lambda * functionY.getValue(xn + step / 2 / lambda * functionY.getValue(xn,yn,c,d), yn + step / 2 / lambda * functionY.getValue(xn,yn,c,d),c,d));
        }

        @Override
        public String getName() {
            return "Y";
        }
    }

    static class RungeKutty2ForX implements MethodForTwoDimensionalDifferentialEquation{
        @Override
        public double calculateNPlusOne(double xn, double yn, double step, double... parameters) {
            TwoDimensionalFunction functionX = new FunctionDerivedX();
            double a = parameters[0];
            double b = parameters[1];
            double lambda = parameters[2];
            return xn + step * ((1 - lambda) * functionX.getValue(xn,yn,a,b) + lambda * functionX.getValue(xn + step / 2 / lambda * functionX.getValue(xn,yn,a,b), yn + step / 2 / lambda * functionX.getValue(xn,yn,a,b),a,b));
        }

        @Override
        public String getName() {
            return "X";
        }
    }

    static class FunctionDerivedX implements TwoDimensionalFunction {
        @Override
        public double getValue(double x, double y, double... parameters) {
            double a = parameters[0];
            double b = parameters [1];
            return a * x - b * x * y;
        }
    }

    static class FunctionDerivedY implements TwoDimensionalFunction {
        @Override
        public double getValue(double x, double y, double... parameters) {
            double c = parameters [0];
            double d = parameters [1];
            return c * x * y - d * y;

        }
    }
}
