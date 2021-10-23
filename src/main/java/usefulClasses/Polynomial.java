package usefulClasses;

import java.util.List;

public class Polynomial {
    private double externalCoefficient;
    private List<Double> coefficients;

    public Polynomial(List<Double> coefficients, double externalCoefficient) {
        this.coefficients = coefficients;
        this.externalCoefficient = externalCoefficient;
    }

    public List<Double> getCoefficient() {
        return coefficients;
    }

    public double getValueAtPoint(double point){
        double value = externalCoefficient;
        for (Double c : coefficients){
            value *= point - c ;
        }
        return value;
    }

    public static double getValueAtPointOfSeveralPolynomial(List<Polynomial> polynomialList, double point){
        double value = 0.;
        for (Polynomial p : polynomialList){
            value += p.getValueAtPoint(point);
        }
        return value;
    }
}
