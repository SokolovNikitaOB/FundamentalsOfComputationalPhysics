package tasks;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;

/**
 * Задача 12<br>
 * Сигнал, состоящий из двух гармонических осцилляций с различными частотами и<br>
 * амплитудами, f(t) = a<sub>0</sub>sinω<sub>0</sub>t + a<sub>1</sub>sinω<sub>1</sub>t регистрируется на некотором интервале T.<br>
 * Вычислить и построить график спектра мощности.<br>
 * Сравнить спектры, полученные с прямоугольным окном и окном Ханна, при следующих параметрах: <br>
 * a0 = 1, a1 = 0.002, ω0 = 5.1, ω1 = 5ω0 = 25.5, T = 2π.
 */
public class Task12 {
    public static double a0 = 1;
    public static double a1 = 1;
    public static double frequency0 = 20.1;
    public static double frequency1 = 10.1;
    public static double period = 10 * Math.PI;
    public static int N = 1000;

    public static void main(String[] args) {
        new LineChartEx(
                "Programme",
                createDataset(),
                "f(ω)",
                "f",
                "ω",
                2,
                new NumberAxis()
        ).setVisible(true);
    }

    private static XYDataset createDataset() {
        XYSeries series = new XYSeries("f(ω) with rectangular window");
        XYSeries series1 = new XYSeries("f(ω) with window Hanna");

        int maxJ = (int)(period / 2. / Math.PI * (frequency0 > frequency1 ? frequency0 + 1 : frequency1 + 1)) + 5;

        for (int j = 0; j < maxJ; j++) {
            double point = 2 * Math.PI * j / period;
            series.add(point, fFourierJ(j));
            series1.add(point, fFourierJWindowHanna(j));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);
        return dataset;
    }

    public static double f(double t){
        return a0 * Math.sin(frequency0 * t) + a1 * Math.sin(frequency1 * t);
    }

    public static double windowHanna(int k, double size){
        return (1 - Math.cos(2 * Math.PI * k / size)) / 2;
    }

    public static double fFourierJWindowHanna(double j){
        double fjReal = 0.;
        double fjComplex = 0.;
        for (int k = 0; k < N; k++) {
            double point = k * period / N;
            fjReal += f(point) * Math.cos(2. * Math.PI * k * j / N ) * windowHanna(k, N);
            fjComplex += f(point) * Math.sin(2. * Math.PI * k * j / N ) * windowHanna(k, N);
        }
        return Math.sqrt(fjReal * fjReal + fjComplex * fjComplex);
    }

    public static double fFourierJ(double j){
        double fjReal = 0.;
        double fjComplex = 0.;
        for (int k = 0; k < N; k++) {
            double point = k * period / N;
            fjReal += f(point) *  Math.cos(2. * Math.PI * k * j / N );
            fjComplex += f(point) * Math.sin(2. * Math.PI * k * j / N );
        }
        return Math.sqrt(fjReal * fjReal + fjComplex * fjComplex);
    }
}
