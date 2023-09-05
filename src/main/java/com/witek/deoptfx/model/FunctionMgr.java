package com.witek.deoptfx.model;

public class FunctionMgr implements OptimizationFunction {

    private String functioName = "Objective function of dislocation density";

    public ObjectProperties[] getDataTable() {
        return dataTable;
    }

    private ObjectProperties[] dataTable;
    @Override
    public double optimize(double[] arguments) {
        return function(arguments);
    }
    @Override
    public String toString(){
        return functioName;
    }

    public FunctionMgr() {
        this.dataTable = ExcelReader.getObjectPropertiesExcel("Dane_lab5.xlsx");
    }

    private double function(double[] a){
        ObjectProperties[] dataModel = dataTable;
        double totalError = 0;
        double Q = 312000;
        try{
        for (ObjectProperties data:
                dataModel) {

                double[] obliczone = DifferentialEq.Euler(data.epsilon[100000],data.epsilon[1],a,data.dot_epsilon,data.temperature + 273,Q,data.epsilon[100001]);
                for (int i = 0 ; i < obliczone.length; i++){
                    /*if(i%50 == 0){
                        double diff = obliczone[i] - data.sigma[i];
                        double point = Math.abs(diff);
                        //dodanie wartosci różnicy bezwzględnej co 50 punktu
                        totalError += point;
                    }*/
                    totalError += Math.pow((data.sigma[i] - obliczone[i])/(data.sigma[i]+0.0001),2);
                }
                totalError = (totalError/(obliczone.length));
            }
            totalError = totalError/(dataModel.length);
            return totalError;

            }catch (ArrayIndexOutOfBoundsException e){
            throw new ArrayIndexOutOfBoundsException("Wymiar wektora nie odpowiada funkcji gęstości dyslokacji");
        }
    }
}
