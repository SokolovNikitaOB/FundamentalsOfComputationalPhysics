package tasks;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;
import usefulClasses.Function;


/**
 * Задание 4 <br>
 * Используя интегральное представление для функций Бесселя целого индекса m и вычисляя производную с помощью конечной разности в тех же точках, что и сам
 * интеграл, продемонстрировать выполнение равенства: <br>
 * J<sup>'</sup><sub>0</sub>(x) + J<sub>1</sub>(x) = 0 <br>
 * с точностью не хуже 10<sup>-10</sup> на отрезке x ∈ [0, 2π].
 */
public class Task4 {
    public static void main(String[] args) {
        ValueAxis axis = new NumberAxis();
        axis.setRange(new Range(-2 * Math.pow(10,-14),2 * Math.pow(10,-14)));
        new LineChartEx("Programme",createDataset(400), "J'0(x)+J1(x)", "x", "y",2,  new NumberAxis()).setVisible(true);

    }
    public static XYDataset createDataset(int N) {
        XYSeries series = new XYSeries("Метод трапеции");
        Function integrandFunction = new IntegrandFunction();
        Function derivedFunctionOne = new DerivedFunctionOneByFiniteSubtract();
        for (int i = 1; i < N; i++) {
            double sum = (Task3.calculateIntegrateByTrapezoidMethod(17, 0 , Math.PI, integrandFunction, Math.PI * i / N, 1) + derivedFunctionOne.getValue(0, Math.PI * i / N, 0)) / Math.PI;
            series.add(i, sum);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    static class IntegrandFunction implements Function{

        @Override
        public double getValue(double point, double... parameters) {
            double x = parameters[0];
            double m = parameters[1];
            return Math.cos(m * point - x * Math.sin(point) );
        }
    }


    static class DerivedFunctionOneByFiniteSubtract implements Function{
        @Override
        public double getValue(double point, double... parameters) {
            double x = parameters[0];
            double m = parameters[1];
            Function function = new IntegrandFunction();
            double precision = Math.pow(2, -16);
            return (Task3.calculateIntegrateByTrapezoidMethod(15, 0 , Math.PI,function, x + precision, m) - Task3.calculateIntegrateByTrapezoidMethod(15, 0 , Math.PI,function, x - precision, m )) / 2 / precision;
        }
    }

}

