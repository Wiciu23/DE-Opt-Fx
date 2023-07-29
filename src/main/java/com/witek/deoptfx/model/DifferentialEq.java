package com.witek.deoptfx.model;

public class DifferentialEq {
    public static double R = 8.314;

    public static void main(String args[]){

        //Strategia raczej, umozliwia ona hermetyzacje całkowicie działana algorytmu.
        
        if(false) {
            //Rozwiązywanie metodą Eulera pierwszego równania
            double a[] = {3.5, 5, 1};//{30, 20, 6, 0.5};// {12.5, 10, 9.5, 0.5};
            double p_cr = 0.4; //1.1;
            double a8 = 0;
            double p[] = new double[200];
            double e_dot = 1;
            double delta_h = 0.01;
            p[0] = 0;
            double t_cr = (1 / a[1]) * Math.log((p[0] - (a[0] / a[1])) / (p_cr - (a[0] / a[1])));
            int j = 0;
            for (double i = 0; i < 1; i = i + delta_h) {

                p[j + 1] = p[j] + delta_h * function(a, p[j], p_cr, a8, e_dot, i, p, p[0], j, delta_h);
                System.out.println(p[j]);
                j++;

            }
        } else {
            //Rozwiązywanie metodą Eulera drugiego równania
            double t = 0.1;  //Czas rozwiązywania
            double delta_h = 0.0001; // przypisanie czasu prowadzenia pomiaru
            double a[] = {2.1E-3,176.0,19.5E3,0.000148*3E10,151E3,0.973,5.77,1.0,0.0,0.262,0.0E13,0.000605E13,0.167};
            double p[] = new double[(int)(t/delta_h)+2];
            double e_dot = 10;
            double T = 675+273;
            double Q = 238000;
            double t_cr = t; //Maksymalny czas na początku żeby znaleźć t_cr
            p[0] = 1E4;
            double ro_cr = -a[10]+a[11]*Math.pow(ZenerHollomon(e_dot,Q,T,R),a[9]);
            //Obliczenie t_cr
            int j = 0;
            for (double i = 0; i < t; i = i + delta_h) {
                if(p[j] >= ro_cr) {
                    t_cr = i;
                    break;
                }
                    p[j + 1] = p[j] + delta_h * functionA(p[j], e_dot, i, p, j, delta_h,Q,T,a,t_cr);
                j++;
            }
            j = 0;
            for (double i = 0; i < t; i = i + delta_h) {
                p[j + 1] = p[j] + delta_h * functionA(p[j], e_dot, i, p, j, delta_h,Q,T,a,t_cr);
                System.out.println(p[j]);
                j++;
            }
            System.out.println(t_cr);
        }

    }

    static double[] Euler(double czas_pomiaru, double delta_h, double[] a,double e_dot, double T, double Q,double t){
        int j = 0;
        double[] p = new double[100002];  // (int) (czas_pomiaru/delta_h)+2
        p[0] = 10000;
        double t_cr = czas_pomiaru; //czas równy czasowi prowadzenia obliczeń.
        double ro_cr = -a[10]+a[11]*Math.pow(ZenerHollomon(e_dot,Q,T,R),a[9]);
        for (double i = 0; i < czas_pomiaru; i = i + delta_h) {
            if(p[j] >= ro_cr) {
                t_cr = i;
                break;
            }
            p[j + 1] = p[j] + delta_h * functionA(p[j], e_dot, i, p, j, delta_h,Q,T,a,t_cr);
            j++;
        }
        j = 0;
        double i = 0;
        for (double g = 0; g < 100000; g++) {
            p[j + 1] = p[j] + delta_h * functionA(p[j], e_dot, i, p, j, delta_h,Q,T,a,t_cr);
            i = i + delta_h;
            j++;
        }


        return p;
    }

    static double ZenerHollomon(double epsilon_dot, double Q, double R, double T){
        double Z = epsilon_dot*Math.exp((Q)/(R*T));
        return Z;
    }

    static double function(double A[],double m_p0, double m_p_cr,double m_a8, double epsilon_dot, double t, double p[],double p00,int j, double delta_h){
        double p0 = m_p0;
        double p_cr = m_p_cr;
        double a8 = m_a8;
        double A1 = A[0];
        double A2 = A[1];
        double A3 = A[2];
        double t_cr = (1/A2)*Math.log((p00-(A1/A2))/(p_cr-(A1/A2)));
        double p_prim;
        double part_1;
        int licznik = j;

        if(t > t_cr){
            int numer = (int) (t_cr/ delta_h);
            part_1 = p[licznik-numer];
        } else part_1 =0;

        p_prim = A1*epsilon_dot-A2*p0*epsilon_dot-A3*Math.pow(p0,a8)*part_1;

        return p_prim;
    }


    static double functionA(double m_p0, double epsilon_dot, double t, double p[],int j, double delta_h, double Q, double T, double a[], double mt_cr){
        //Dodanie stałych współczynników
        //a[7] = 0.452;
        //a[9] = 0.409;
        //a[10] = 0.0;
        //a[11] = 0.000042E13;
        //a[4] = 123.12E3;
        //a[3] = 0.05317*3E10;
        double p0 = m_p0;
        double b = 0.25E-9;
        double Kirchoff = 43500;
        double D = 30;
        double Z = ZenerHollomon(epsilon_dot,Q,R,T);
        double l = (a[0])/(Math.pow(Z,a[12]));
        double tau = (1E6*Kirchoff*Math.pow(b,2))/(2);
        double A1 = 1/(b*l);
        double A2 = a[1]*Math.pow(epsilon_dot,-a[8])*Math.exp((-a[2])/(R*T));
        double A3 = a[3]*(tau/D)*Math.exp((-a[4])/(R*T));
        double t_cr = mt_cr;
        double p_prim;
        double part_1;

        int licznik = j;
        if(t > t_cr){
            int numer = (int) (t_cr/ delta_h);
            part_1 = p[licznik-numer];
        } else part_1 =0;

        p_prim = A1*epsilon_dot-A2*p0*epsilon_dot-A3*Math.pow(p0,a[7])*part_1;

        return p_prim;
    }

    static double functionB(double m_p0, double epsilon_dot, double t, double p[],int j, double delta_h, double T, double a[], double mt_cr){
        double b = 0.25E-9;
        double D = 30;
        double Q = 312000;
        double Z = ZenerHollomon(epsilon_dot,Q,R,T);
        double l = (a[0])/(Math.pow(Z,a[12]));
        double Kirchoff = 43500;
        double tau = (1E6*Kirchoff*Math.pow(b,2))/(2);

        double p0 = m_p0;
        double A1 = 1/(b*l);
        double A2 = a[1]*Math.pow(epsilon_dot,-a[8])*Math.exp((-a[2])/(R*T));
        double A3 = a[3]*(tau/D)*Math.exp((-a[4])/(R*T));
        double t_cr = mt_cr;
        double p_prim;
        double part_1;

        int licznik = j;
        if(t > t_cr){
            int numer = (int) (t_cr/ delta_h);
            part_1 = p[licznik-numer];
        } else part_1 =0;

        p_prim = A1*epsilon_dot-A2*p0*epsilon_dot-A3*Math.pow(p0,a[7])*part_1;

        return p_prim;




    }
}


// metoda eulera będzie obliczała funkcję różniczkową
//zwraca tablice z eulera -> oblicza wartość błędów w każdym kroku
