package tasks;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import plotting.LineChartEx;
import usefulClasses.MethodForDifferentialEquation;

import java.util.List;
import java.util.Scanner;

/**
 * Задание 6 <br>
 * Решить задачу Коши: <br>
 * dx/dt = −x, x(0) = 1, 0 < t < 3 <br>
 * методом Эйлера первого порядка точности и методами Рунге-Кутты второго и
 * четвертого порядка точности.
 */
public class Task6 {
    public static void main(String[] args) throws Exception {
        System.out.println("Введите имя метода согласно шаблону:");
        System.out.println("(Эйлер - E, Рунге-Кутты 2-го порядка - RK2, Рунге-Кутты 4-го порядка - RK4)");
        Scanner in = new Scanner(System.in);
        String nameMethod = in.nextLine();

        MethodForDifferentialEquation method;
        switch (nameMethod){
            case ("E"):
                method = new Euler();
                break;
            case ("RK2"):
                method = new RungeKutty2();
                break;
            case ("RK4"):
                method = new RungeKutty4();
                break;
            default:
                throw new Exception("Incorrect input");
        }

        List<XYDataset> xyDatasets = createDataset(method);
        new LineChartEx(
                "Programme",
                xyDatasets.get(0),
                "График решений уравнения графически и аналитически",
                "t",
                "x",
                2,
                new NumberAxis()
        ).setVisible(true);

        new LineChartEx(
                "Programme",
                xyDatasets.get(1),
                "Погрешность",
                "t",
                "x",
                1,
                new NumberAxis()
        ).setVisible(true);
    }

    private static List<XYDataset> createDataset(MethodForDifferentialEquation method) {
        XYSeries series = new XYSeries(method.getName());
        XYSeries series1 = new XYSeries("Analytically");
        XYSeries series2 = new XYSeries("Error");

        double xn = 1;
        double xnMinusOne;

        for (int i = 0; i <= 3000; i++) {

            double t =  1. * i / 1000;
            xnMinusOne = xn;
            series.add(t, xn);
            series1.add(t, Math.exp(-t));
            series2.add(t,xn-Math.exp(-t));
            xn = method.calculateNPlusOne(xnMinusOne, 1. / 1000,3./4);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);
        XYSeriesCollection dataset1 = new XYSeriesCollection();
        dataset1.addSeries(series2);

        return List.of(dataset,dataset1);
    }

    static class Euler implements MethodForDifferentialEquation {
        private final String name = "Euler";

        @Override
        public String getName() {
            return name;
        }

        @Override
        public double calculateNPlusOne(double xn, double step, double... parameters) {
            return xn + step * (-xn);
        }
    }

    static class RungeKutty2 implements MethodForDifferentialEquation{
        private final String name = "Runge-Kutty 2";

        @Override
        public String getName() {
            return name;
        }

        @Override
        public double calculateNPlusOne(double xn, double step, double... parameters) {
            double alpha = parameters[0];
            return xn + step * (( 1- alpha) * (-xn) - alpha * (xn + step * (-xn) / 2 / alpha));
        }
    }

    static class RungeKutty4 implements MethodForDifferentialEquation{
        private final String name = "Runge-Kutty 4";

        @Override
        public String getName() {
            return name;
        }

        @Override
        public double calculateNPlusOne(double xn, double step, double... parameters) {
            double k1 = -xn;
            double k2 = -(xn + step * k1 / 2);
            double k3 = -(xn + step * k2 / 2);
            double k4 = -(xn + step * k3);
            return xn + step * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
        }
    }
}


