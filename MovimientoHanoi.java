public class MovimientoHanoi {
    public void moverDiscos(int n, char origen, char destino, char auxiliar) {
        // Caso base: Si solo hay un disco, muévelo del origen al destino
        if (n == 1) {
            System.out.println("Mover disco de " + origen + " a " + destino);
            return;
        }

        // Mueve (n-1) discos del origen al auxiliar, usando destino como auxiliar
        moverDiscos(n - 1, origen, auxiliar, destino);

        // Mueve el disco n del origen al destino
        System.out.println("Mover disco de " + origen + " a " + destino);

        // Mueve los (n-1) discos del auxiliar al destino, usando origen como auxiliar
        moverDiscos(n - 1, auxiliar, destino, origen);
    }
}