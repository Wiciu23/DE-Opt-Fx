package com.witek.deoptfx.model;

public class FunctionMgr implements OptimizationFunction {
    @Override
    public double optimize(double[] arguments) {
        return function(arguments);
    }

    public FunctionMgr() {
        this.dataTable = ExcelReader.getObjectPropertiesExcel("Dane_lab5.xlsx");
    }

    private ObjectProperties[] dataTable;

    private double function(double[] a){
        ObjectProperties[] dataModel = dataTable;
        double totalError = 0;
        double Q = 312000;

        for (ObjectProperties data:
                dataModel) {
            try{
                double[] obliczone = DifferentialEq.Euler(data.epsilon[100000],data.epsilon[1],a,data.dot_epsilon,data.temperature + 273,Q,data.epsilon[100001]);
                for (int i = 0 ; i < obliczone.length; i++){
                    totalError += Math.pow((data.sigma[i] - obliczone[i])/(data.sigma[i]+0.0001),2);
                    //System.out.println(funkcja(data.epsilon[i], data.dot_epsilon, data.temperature,a));
                }
                totalError = totalError/(data.sigma.length);
            }catch (ArrayIndexOutOfBoundsException e){
                throw new ArrayIndexOutOfBoundsException("Wymiar wektora nie odpowiada funkcji gęstości dyslokacji");
            }
        }
        totalError = totalError/(dataModel.length);
        return totalError;
    }
}
