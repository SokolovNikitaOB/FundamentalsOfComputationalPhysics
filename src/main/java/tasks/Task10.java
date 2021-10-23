package tasks;

import org.jfree.chart.axis.LogAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;

import java.util.*;

/**
 * Задача 10<br>
 * Решить задачу Коши для одномерного уравнения диффузии по схеме Кранка-Николсон <br>
 * ∂u/∂t = ∂<sup>2</sup>u/∂x<sup>2</sup>, 0 < x < L, L = 1 <br>
 * u(0,t) = 0, u(L,t) = 0, u(x,0) = x(1 − x/L)<sup>2</sup><br>
 * На каждом шаге по времени найти максимальное значение температуры и нарисовать<br>
 * зависимость максимальной температуры от времени. Показать, что на больших<br>
 * временах она убывает экспоненциально.
 */
public class Task10 {
    public static void main(String[] args) {
        new LineChartEx(
                "Programme",
                createDataset(),
                "U(xMax,t)",
                "t",
                "U",
                2,
                new LogAxis()
        ).setVisible(true);
    }

    private static XYDataset createDataset() {
        XYSeries series = new XYSeries("U(t)");
        XYSeries series1 = new XYSeries("RealU(t)");
        HashMap<Double,Double> dependenceOfMaxTemperatureOnTime = getDependenceOfMaxTemperatureOnTime(10000, 100,20);

        List<Double> times = new ArrayList<>();
        for(Double time : dependenceOfMaxTemperatureOnTime.keySet()){
            series.add(time, dependenceOfMaxTemperatureOnTime.get(time));
            times.add(time);
        }

        for (int i = 0; i < times.size(); i++) {
            double real = ut0(1./3) * Math.exp(-Math.PI * Math.PI * times.get(i));
            double time = times.get(i);
            series1.add(time, real);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);
        return dataset;
    }

    public static HashMap<Double,Double> getDependenceOfMaxTemperatureOnTime(int nForT, int nForX, double tMax){
        double t = tMax / nForT;
        double h = 1. / nForX;
        List<List<Double>> uxjtm = new ArrayList<>();

        List<Double> uxjt0 = new ArrayList<>();
        for (int i = 0; i <= nForX; i++) {
            uxjt0.add(ut0(i * h));
        }
        uxjtm.add(uxjt0);

        for (int i = 1; i <= nForT; i++) {
            uxjtm.add(getUForConstTm(uxjtm.get(i-1),nForT,nForX,tMax));
        }

        HashMap<Double,Double> dependenceOfMaxTemperatureOnTime = new HashMap<>();
        for (int i = 0; i < nForT; i++) {
            double max = uxjtm.get(i).stream().max((a1,a2) -> a1.compareTo(a2)).get();
            dependenceOfMaxTemperatureOnTime.put(i * t, max);
        }

        return dependenceOfMaxTemperatureOnTime;
    }

    public static List<Double> getUForConstTm(List<Double> um, int nForT, int nForX, double tMax){
        double t = tMax / nForT;
        double h = 1. / nForX;
        double a = -2. - 2. * h * h / t;
        double b = a + 4;

        List<Double> beta = new ArrayList<>();
        List<Double> alpha = new ArrayList<>();
        for (int i = 1; i < um.size() - 1; i++) {
            alpha.add(a);
            beta.add(-um.get(i-1) + b * um.get(i) - um.get(i+1));
        }

        List<Double> newBeta = new ArrayList<>();
        List<Double> newAlpha = new ArrayList<>();
        int size = alpha.size();
        for (int i = 0; i < size; i++) {
            if(i == 0){
                newAlpha.add(alpha.get(0));
                newBeta.add(beta.get(0));
            }else {
                newAlpha.add(alpha.get(i) - 1. / newAlpha.get(i-1));
                newBeta.add(beta.get(i) - newBeta.get(i-1) / newAlpha.get(i-1));
            }
        }

        List<Double> umPlusOne = new ArrayList<>();
        umPlusOne.add(0.);
        for (int i = size - 1; i >= 0; i--) {
            if(i == size - 1 ){
                umPlusOne.add(newBeta.get(i) / newAlpha.get(i));
            }else {
                umPlusOne.add((newBeta.get(i) - umPlusOne.get(size - 1 - i)) / newAlpha.get(i));
            }
        }
        umPlusOne.add(0.);
        Collections.reverse(umPlusOne);

        return umPlusOne;
    }

    public static double ut0(double x){
        return x * (1 - x) * (1 - x);
    }
}
