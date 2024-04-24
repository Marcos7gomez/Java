 
package ejercicios;

import java.util.Scanner;

// Crear un programa que pida numeros enteros hasta que se ingrese el num 0
// y cuando esto ocurra sumar todos los numeros ingresados y mostrarlos
 
public class pedirNum {
    public static void main(String[] args) {
        
        int num;
        int suma = 0;
        
        Scanner sc = new Scanner(System.in);
        
        do{
            System.out.print("Introducir un numero: ");
            num = sc.nextInt();
            suma = suma+num;
        }while(num!=0);
        
        System.out.println("El total es: "+suma);
    
    }
}