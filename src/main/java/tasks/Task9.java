package tasks;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Задача 9<br>
 * Методом прогонки решить разностный аналог граничной задачи для уравнения <br>
 * y'' = sin x <br>
 * на промежутке 0 < x < π. Рассмотреть различные варианты граничных условий.
 */
public class Task9 {
    public static void main(String[] args) {

        new LineChartEx(
                "Programme",
                createDataset(),
                "Решение уравнения y''=sin x",
                "x",
                "y",
                1,
                new NumberAxis()
        ).setVisible(true);
    }

    private static XYDataset createDataset() {
        XYSeries series = new XYSeries("y(x)");
        List<List<Double>> xy = getDecision(1000, 0, 2*Math.PI, 2, -2);
        List<Double> x = xy.get(0);
        List<Double> y = xy.get(1);
        for (int i = 0; i < x.size(); i++) {
            series.add(x.get(i), y.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    public static List<List<Double>> getDecision(int n, double xMin, double xMax, double yMin, double yMax){
        List<Double> alpha = new ArrayList<>();
        List<Double> beta = new ArrayList<>();
        List<Double> x = new ArrayList<>();
        double h = (xMax - xMin) / n;

        for (int i = 0; i <= n; i++) {
            x.add(xMin + h * i);
        }

        alpha.add(1./2);
        beta.add((yMin - h * h * Math.sin(x.get(1))) / 2);
        for (int i = 0; i < n - 1; i++) {
            alpha.add(1./(2-alpha.get(i)));
            beta.add((beta.get(i) - h * h * Math.sin(x.get(i + 1)))/(2 - alpha.get(i)));
        }

        List<Double> y = new ArrayList<>();
        y.add(yMax);
        for (int i = n - 1; i > 0 ; i--) {
            y.add(alpha.get(i) * y.get(n - 1 - i) + beta.get(i));
        }
        y.add(yMin);
        Collections.reverse(y);

        return List.of(x,y);
    }
}
