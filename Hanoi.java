import java.util.Scanner;

public class Hanoi {
 // Método recursivo para resolver las Torres de Hanoi
    public void resolverHanoi(int nDiscos, char torreInicial, char torreCentral, char torreFinal) {
        // Caso base: si solo hay un disco, simplemente moverlo de la torreInicial a la torreFinal
        if (nDiscos == 1) {
            System.out.printf("Mover disco 1 de %c a %c%n", torreInicial, torreFinal);
        } else {
            // Mover los n-1 discos de torreInicial a torreCentral usando torreFinal como auxiliar
            resolverHanoi(nDiscos - 1, torreInicial, torreFinal, torreCentral);
            
            // Mover el disco n de torreInicial a torreFinal
            System.out.printf("Mover disco %d de %c a %c%n", nDiscos, torreInicial, torreFinal);
            
            // Mover los n-1 discos de torreCentral a torreFinal usando torreInicial como auxiliar
            resolverHanoi(nDiscos - 1, torreCentral, torreInicial, torreFinal);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hanoi hanoi = new Hanoi();
        
        System.out.println("Introduce el número de discos:");
        int nDiscos = scanner.nextInt();
        
        // Llamada al método con las torres A (inicial), B (central) y C (final)
        hanoi.resolverHanoi(nDiscos, 'A', 'B', 'C');
        
        scanner.close();
    }  
}