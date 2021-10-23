package tasks;

import java.util.Scanner;

/**
 * Задача 2 <br>
 * Используя методы дихотомии, простых итераций и Ньютона, найти уровень энергии E
 * основного состояния квантовой частицы в прямоугольной потенциальной яме:<br>
 * −1/2ψ''(x) + U(x)ψ(x) = Eψ(x), U(x) = {−U0, |x| ≤ a, 0, |x| > a}.
 */
public class Task2 {
    public static double m = 9.1 * Math.pow(10,-31); // масса электрона, кг
    public static double h = Math.pow(10,-34); // постоянная Планка, Дж*с
    public static double e = 1.6 * Math.pow(10, -19); // заряд электрон, Кл
    public static double a = Math.pow(10,-10); // границы барьера, м
    public static double u =  100 * e; // высота барьера, Дж
    public static double option = 2 * m * a * a * u / h / h ;

    public static double precisionOfDerivative = Math.pow(10,-10);

    public static int iterationDichotomy = 0;
    public static int iterationSimpleIteration = 0;
    public static int iterationNewton = 0;

    public static void main(String[] args) {

        try {

            Scanner in = new Scanner(System.in);
            System.out.println("Введите точность: ");
            int precision = in.nextInt();

            double point;
            if(calculatePointOfMaximum() < 1 && calculatePointOfMaximum() > 0){
                point = calculatePointOfMaximum() - 0.01;
            }else {
                point = 0.95;
            }

            System.out.println("Энергия основного состояния (Дихотомия) = -" + calculateRootByDichotomy(0.00001,point, -precision) * u / e + " эВ");
            System.out.println(calculateRootByDichotomy(0.00001,point, -precision));
            System.out.println("Число итераций в дихотомии = " + iterationDichotomy / 2 );

            System.out.println("Энергия основного состояния (Простые итерации) = -" + calculateRootBySimpleIteration(point, -precision) * u / e + " эВ");
            System.out.println(calculateRootBySimpleIteration(point, -precision));
            System.out.println("Число итераций в простых итерациях = " + iterationSimpleIteration / 2);

            System.out.println("Энергия основного состояния (Ньютон) = -" + calculateRootByNewtonMethod(point, -precision) * u / e + " эВ");
            System.out.println(calculateRootByNewtonMethod(point, -precision));
            System.out.println("Число итераций в Ньютоне = " + iterationNewton / 2);


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Метод Ньютона
     */
    public static double calculateRootByNewtonMethod(double startingPoint, int precision){
        iterationNewton++;
        double nextPoint = startingPoint - calculateValueOfFunction(startingPoint) / calculateValueOfDerivedFunction(startingPoint);
        if(Math.abs(nextPoint - startingPoint) < Math.pow(10, precision)){
            return nextPoint;
        }else {
            return calculateRootByNewtonMethod(nextPoint, precision);
        }
    }

    /**
     * Метод простых итераций
     */
    public static double calculateRootBySimpleIteration(double startingPoint, int precision){
        iterationSimpleIteration++;
        double nextPoint = calculateValueOfFunctionPlusX(startingPoint,1./calculateValueOfDerivedFunction(0.06));
        if(Math.abs(nextPoint - startingPoint) < Math.pow(10, precision)){
            return nextPoint;
        }else {
           return calculateRootBySimpleIteration(nextPoint, precision);
        }
    }

    /**
     * Метод дихотомии
     */
    public static double calculateRootByDichotomy(double min, double max, int precision) throws Exception {
        if(calculateValueOfFunction(min) * calculateValueOfFunction(max) > 0){
            throw new Exception("Произведение в крайних точках больше нуля");
        }
        iterationDichotomy++;
        double medium = (min + max) / 2;
        if(max - min < Math.pow(10,precision)){
            return medium;
        }
        if(calculateValueOfFunction(min) * calculateValueOfFunction(medium) < 0){
            return calculateRootByDichotomy(min, medium, precision);
        }else {
            return calculateRootByDichotomy(medium, max, precision);
        }
    }

    public static double calculatePointOfMaximum(){
        return 1. - Math.PI * Math.PI / option;
    }

    /**
     * ctg(sqrt(2ma<sup>2</sup>U<sub>0</sub>/h<sup>2</sup> * (1-ep)))-sqrt(1/ep-1) <br>
     * ep = -E/U<sub>0</sub> <br>
     */
    public static double calculateValueOfFunction(double point){
        return Math.pow(Math.tan(Math.pow((option * (1 - point)), 1./2)),-1) - Math.pow(1/point - 1, 1./2);
    }

    public static double calculateValueOfDerivedFunction(double point){
        return (- calculateValueOfFunction(point) + calculateValueOfFunction(point + precisionOfDerivative))/precisionOfDerivative;
    }

    public static double calculateValueOfFunctionPlusX(double point, double lambda){
        return - lambda * calculateValueOfFunction(point) + point;
    }

}
