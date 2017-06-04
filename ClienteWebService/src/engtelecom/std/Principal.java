/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engtelecom.std;

/**
 *
 * @author cleber
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    private static int somar(int a, int b) {
        engtelecom.std.Calculadora_Service service = new engtelecom.std.Calculadora_Service();
        engtelecom.std.Calculadora port = service.getCalculadoraPort();
        return port.somar(a, b);
    }

    private static int subtrair(int a, int b) {
        engtelecom.std.Calculadora_Service service = new engtelecom.std.Calculadora_Service();
        engtelecom.std.Calculadora port = service.getCalculadoraPort();
        return port.subtrair(a, b);
    }
    
}
