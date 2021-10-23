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
 * Задача 8<br>
 * Решить жесткую систему уравнений по явной и неявной схемам Эйлера<br>
 * u' = 998u + 1998v, v = −999u − 1999v
 */
public class Task8 {
    public static void main(String[] args) {
        List<XYDataset> xyDatasetsExplicit = createDataset(new explicitEulerMethodForU(), new explicitEulerMethodForV());
        new LineChartEx(
                "Programme",
                xyDatasetsExplicit.get(0),
                "Решение системы явным методом Эйлера",
                "t",
                "u,v",
                2,
                new NumberAxis()
        ).setVisible(true);

        new LineChartEx(
                "Programme",
                xyDatasetsExplicit.get(1),
                "Погрешность решения системы явным методом Эйлера",
                "t",
                "u,v",
                2,
                new NumberAxis()
        ).setVisible(true);

        List<XYDataset> xyDatasetsImplicit = createDataset(new implicitEulerMethodForU(), new implicitEulerMethodForV());
        new LineChartEx(
                "Programme",
                xyDatasetsImplicit.get(0),
                "Решение системы неявным методом Эйлера",
                "t",
                "u,v",
                2,
                new NumberAxis()
        ).setVisible(true);

        new LineChartEx(
                "Programme",
                xyDatasetsImplicit.get(1),
                "Погрешность решения системы неявным методом Эйлера",
                "t",
                "u,v",
                2,
                new NumberAxis()
        ).setVisible(true);
    }

    private static List<XYDataset> createDataset(MethodForTwoDimensionalDifferentialEquation methodU, MethodForTwoDimensionalDifferentialEquation methodV) {
        XYSeries series = new XYSeries("u(t)");
        XYSeries series1 = new XYSeries("v(t)");
        XYSeries series2 = new XYSeries("Погрешность u(t)");
        XYSeries series3 = new XYSeries("Погрешность v(t)");

        double un = 4;
        double unMinusOne;

        double vn = -2;
        double vnMinusOne;

        for (int i = 0; i <= 10000; i++) {

            double t =  1. * i / 1000;
            unMinusOne = un;
            vnMinusOne = vn;
            series.add(t, un);
            series1.add(t, vn);
            series2.add(t, un - 4 * Math.exp(-t));
            series3.add(t, vn + 2 * Math.exp(-t));
            un = methodU.calculateNPlusOne(unMinusOne,vnMinusOne,1./1000);
            vn = methodV.calculateNPlusOne(unMinusOne,vnMinusOne, 1./1000);

        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);
        XYSeriesCollection dataset1 = new XYSeriesCollection();
        dataset1.addSeries(series2);
        dataset1.addSeries(series3);
        return List.of(dataset,dataset1);
    }

    static class explicitEulerMethodForU implements MethodForTwoDimensionalDifferentialEquation {
        @Override
        public double calculateNPlusOne(double xn, double yn, double step, double... parameters) {
            TwoDimensionalFunction functionU = new FunctionDerivedU();
            return xn + functionU.getValue(xn, yn) * step;
        }

        @Override
        public String getName() {
            return "U";
        }
    }
    static class explicitEulerMethodForV implements MethodForTwoDimensionalDifferentialEquation {
        @Override
        public double calculateNPlusOne(double xn, double yn, double step, double... parameters) {
            TwoDimensionalFunction functionV = new FunctionDerivedV();
            return yn + functionV.getValue(xn, yn) * step;
        }

        @Override
        public String getName() {
            return "V";
        }
    }

    static class implicitEulerMethodForU implements MethodForTwoDimensionalDifferentialEquation {
        @Override
        public double calculateNPlusOne(double xn, double yn, double step, double... parameters) {
            return (xn * (1999. * step + 1) + yn * 1998. * step) / ((998. * step - 1) * (-1999. * step - 1) + 1998. * 999 * step * step);
        }

        @Override
        public String getName() {
            return "U";
        }
    }
    static class implicitEulerMethodForV implements MethodForTwoDimensionalDifferentialEquation {
        @Override
        public double calculateNPlusOne(double xn, double yn, double step, double... parameters) {
            return ((1 - 998. * step) * yn - xn * 999. * step) / ((998. * step - 1) * (-1999. * step - 1) + 1998. * 999 * step * step);
        }

        @Override
        public String getName() {
            return "V";
        }
    }

    static class FunctionDerivedU implements TwoDimensionalFunction {
        @Override
        public double getValue(double u, double v,  double ... parameters) {
            return 998 * u + 1998 * v;
        }
    }

    static class FunctionDerivedV implements TwoDimensionalFunction{
        @Override
        public double getValue(double u, double v, double ... parameters) {
            return -999 * u - 1999 * v;

        }
    }
}
