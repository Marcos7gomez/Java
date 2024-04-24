
package ejercicios;
//Cada porcion de pan cuesta $5
// Si el cliente compra mas de 50 piezas, le costará $4.50

import java.util.Scanner;

// Si el cliente compra mas de 100 piezas, le costará $4
//Programa que pida capturar la cantidad de piezas que el cliente compró
// y el total que pagará

public class totalPago {
    public static void main(String[] args) {
        int cantPiezas;
        double totalpagar;
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese la cantidad de panes que desea comprar: ");
        cantPiezas = sc.nextInt();
        if(cantPiezas <50){
            totalpagar = (cantPiezas * 5);
    }
        else if(cantPiezas>50 && cantPiezas<100 ){
            totalpagar = (cantPiezas * 4.5);
    }
        else{
            totalpagar = cantPiezas * 4;
    }
        System.out.println("Usted compró "+cantPiezas+" panes y el total a pagar es de: "+totalpagar);
    }
}
