package tasks;

import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Задача 11<br>
 * Найти уровень энергии и волновую функцию ψ(x) основного состояния в потенциальной яме U(x), <br>
 * решая конечномерный аналог спектральной задачи для одномерного стационарного уравнения Шредингера<br>
 * (-1/2 ∂<sup>2</sup>/∂x<sup>2</sup> + U(x) - E) ψ(x,t) = 0, |ψ(x)| → 0 при |x| → ∞.<br>
 * Для поиска наименьшего собственного значения Hψ = Eψ трехдиагональной матрицы H использовать метод обратных итераций.<br>
 * Проверить работу программы, сравнив с точным решением для U(x) = x<sup>2</sup>/2
 */
public class Task11 {
    public static List<Double> pfiList = new ArrayList<>();
    public static List<Double> xList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Энергия основного состояния: " + getMinimumOfEnergy(-20.,20.,1000));
        List<XYDataset> xyDatasetList = createDataset();
        new LineChartEx(
                "Programme",
                xyDatasetList.get(0),
                "Wave function",
                "x",
                "pfi",
                2,
                new LogAxis()
        ).setVisible(true);
        new LineChartEx(
                "Programme",
                xyDatasetList.get(1),
                "Error",
                "x",
                "pfi",
                1,
                new NumberAxis()
        ).setVisible(true);
    }

    private static List<XYDataset> createDataset() {
        XYSeries series = new XYSeries("Pfi(x)");
        XYSeries series1 = new XYSeries("RealPfi(t)");
        XYSeries series2 = new XYSeries("Error");

        double pfiMax = pfiList.stream().min((a1,a2) -> a1.compareTo(a2)).get();

        for (int i = 0; i < xList.size(); i++) {
            double x = xList.get(i);

            double pfi = pfiList.get(i) * (Math.pow(Math.PI,-1./4) / pfiMax);
            series.add(x, pfi);

            double real = Math.exp(-x * x / 2) * Math.pow(Math.PI,-1./4);
            series1.add(x, real);

            series2.add(x, real - pfi);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);

        XYSeriesCollection dataset1 = new XYSeriesCollection();
        dataset1.addSeries(series2);

        return List.of(dataset, dataset1);
    }

    public static double getMinimumOfEnergy(double xMin, double xMax, int n){
        double h = (xMax - xMin) / n;

        List<Double> pfi0 = new ArrayList<>();
        List<Double> alpha = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            pfi0.add(1. / Math.sqrt(n));
            alpha.add(-2. - Math.pow(h * (xMin + i * h),2.));
        }

        List<Double> newAlpha = new ArrayList<>(); //Одинаковое для всех
        for (int i = 0; i < n; i++) {
            if(i == 0){
                newAlpha.add(alpha.get(i));
            }else {
                newAlpha.add(alpha.get(i) - 1. / newAlpha.get(i-1));
            }
        }

        List<List<Double>> pfi = getPfiK(pfi0,newAlpha,n,50);
        List<Double> pfiK = pfi.get(0);
        List<Double> pfiKMinusOne = pfi.get(1);

        double energy = -1. / 2 / h / h / n;

        double help = 0.;
        for (int i = 0; i < pfiK.size(); i++) {
            pfiList.add(pfiK.get(i));
            xList.add(xMin + i * h);
            help += pfiKMinusOne.get(i) / pfiK.get(i);
        }

        return energy * help;
    }

    public static List<List<Double>> getPfiK(List<Double> pfiKMinusOne, List<Double> alpha, int n, int deep){
        List<Double> pfiKMinusOneStreak = new ArrayList<>();
        for (int i = 0; i < pfiKMinusOne.size(); i++) {
            if(i == 0){
                pfiKMinusOneStreak.add(pfiKMinusOne.get(i));
            }else {
                pfiKMinusOneStreak.add(pfiKMinusOne.get(i) - pfiKMinusOneStreak.get(i-1)/alpha.get(i-1));
            }
        }

        List<Double> pfiK = new ArrayList<>();
        for (int i = n-1; i >= 0; i--) {
            if(i == n-1){
                pfiK.add(pfiKMinusOneStreak.get(i) / alpha.get(i));
            }else {
                pfiK.add((pfiKMinusOneStreak.get(i) - pfiK.get(n - 2 - i) ) / alpha.get(i));
            }
        }
        Collections.reverse(pfiK);

        if(deep > 0){
            deep--;
            return getPfiK(pfiK,alpha,n,deep);
        }else {
            return List.of(pfiK,pfiKMinusOne);
        }
    }

}




