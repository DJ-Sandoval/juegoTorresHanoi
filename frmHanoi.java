import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class frmHanoi extends JPanel {
   
   // Declaramos nuestras variables
   private Stack<Integer>[] torres;
   private JLabel lblMovimientos;
   private JButton btnJugar;
   private JButton btnLimpiar;
   private MovimientoHanoi movimientoHanoi;
   private int discoSeleccionado = -1; // El disco que el usuario está arrastrando
   private Point mousePos;
   private Point posicionDiscoArrastrado;
   private Point puntoInicial;
   private int torreOrigen = -1;
   private int numeroDiscos;
   private int contadorMovimientos = 0;  
   
   // Definir colores en RGB para los discos
   private Color[] coloresDiscos = {
       new Color(255, 165, 0),   // Rojo
       new Color(255, 255, 0),   // Verde
       new Color(0, 0, 255),   // Azul
       new Color(0, 255, 0), // Amarillo
       new Color(255, 0, 0)
       
   };
   
   public frmHanoi(int numeroDiscos) {
      setPreferredSize(new Dimension(600, 500));
      numeroDiscos = 0; // Iniciar sin discos 
      inicializarTorres(numeroDiscos);
      initComponents();
      addMouseListeners();
   }   
    
    
     // Método para inicializar las torres con los discos
   private void inicializarTorres(int numeroDiscos) {
      this.numeroDiscos = numeroDiscos;
      this.torres = new Stack[3];
      for (int i = 0; i < 3; i++) {
         torres[i] = new Stack<>();
      }
      for (int i = numeroDiscos; i > 0; i--) {
         torres[0].push(i);
      }
      contadorMovimientos = 0; // Reiniciar el contador de movimientos
   }
    
    // Añadimos los listeners para arrastrar y soltar discos
   private void addMouseListeners() {
      addMouseListener(
         new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               mousePos = e.getPoint();
               detectarDiscoSeleccionado(mousePos);
            }
         
            @Override
            public void mouseReleased(MouseEvent e) {
               if (discoSeleccionado != -1) {
                  Point releasePoint = e.getPoint();
                  moverDisco(releasePoint);
               }
            }
         });
   
      addMouseMotionListener(
         new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
               if (discoSeleccionado != -1) {
                  mousePos = e.getPoint();
                  repaint();
               }
            }
         });
   }
    
     // Método para detectar qué disco ha sido seleccionado
   private void detectarDiscoSeleccionado(Point mousePos) {
      for (int i = 0; i < 3; i++) {
         if (!torres[i].isEmpty()) {
            int topDisco = torres[i].peek();
                // Obtener coordenadas y dimensiones del disco superior
            int baseX = getBaseX(i);
            int baseY = getHeight() - 100 - (torres[i].size() * 20);
            int discoWidth = getDiscoWidth(topDisco);
            int discoHeight = 20;
         
                // Verificar si el mouse está sobre el disco superior
            if (mousePos.x >= baseX && mousePos.x <= baseX + discoWidth && mousePos.y >= baseY && mousePos.y <= baseY + discoHeight) {
               discoSeleccionado = topDisco;
               torreOrigen = i;
               break;
            }
         }
      }
   }
   
   

   // Método para mover el disco a otra torre
   private void moverDisco(Point releasePoint) {
      int torreDestino = obtenerTorrePorPosicion(releasePoint);
   
      if (torreDestino != -1 && torreDestino != torreOrigen) {
         if (torres[torreDestino].isEmpty() || torres[torreDestino].peek() > discoSeleccionado) {
            torres[torreOrigen].pop();
            torres[torreDestino].push(discoSeleccionado);
         
            // Incrementamos el contador de movimientos y actualizamos la etiqueta
            contadorMovimientos++;
            lblMovimientos.setText("Movimientos: " + contadorMovimientos);
         
            // Verificamos si el jugador ha ganado
            verificarGanador();
         }
      }
   
      discoSeleccionado = -1; // Resetear después de soltar
      torreOrigen = -1;
      repaint();
   }   
   
   
   private void verificarGanador() {
    // Calcula el número de movimientos necesarios para ganar con el número de discos actual
      int movimientosNecesarios = (int) Math.pow(2, numeroDiscos) - 1;
    
      if (contadorMovimientos == movimientosNecesarios && torres[2].size() == numeroDiscos) {
         JOptionPane.showMessageDialog(this, "¡Felicidades! Ganaste en " + movimientosNecesarios + " movimientos.");
         reiniciarJuego();
         numeroDiscos = 0;
      }
   }

   
    // Método para regresar los discos a la torre 1 y reiniciar el juego
   private void reiniciarJuego() {
      inicializarTorres(0);
      lblMovimientos.setText("Movimientos: 0");
      repaint();
   }

    // Determina a qué torre corresponde la posición del mouse
   private int obtenerTorrePorPosicion(Point p) {
      int width = getWidth();
      if (p.x < width / 3) {
         return 0;
      } else if (p.x < 2 * width / 3) {
         return 1;
      } else {
         return 2;
      }
   }
    
   private void initComponents() {
      setLayout(null); // Usa el diseño nulo para posicionar el botón manualmente
        
      lblMovimientos = new JLabel("Movimientos: ");
      lblMovimientos.setBounds(10, 20, 150, 30);
      lblMovimientos.setForeground(Color.BLACK);
      lblMovimientos.setFont(new Font("Arial", Font.BOLD, 18));
        
      add(lblMovimientos);
        
      // Configura el botón Jugar
      btnJugar = new JButton("Jugar");
      btnJugar.setBounds(500, 20, 80, 30); // Posiciona el botón en la parte superior derecha
      btnJugar.setBackground(Color.BLUE); // Establece el color de fondo a azul
      btnJugar.setForeground(Color.WHITE); // Establece el color del texto a blanco
      btnJugar.setFont(new Font("Arial", Font.BOLD, 14)); // Establece la fuente y tamaño
      btnJugar.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llama al método jugar cuando se hace clic en el botón
               solicitarNumeroDiscos();
            }
         });
   
      add(btnJugar); // Añade el botón al panel
   
        // Configura el botón Limpiar discos
      btnLimpiar = new JButton("Limpiar discos");
      btnLimpiar.setBounds(330, 20, 150, 30); // Posiciona el botón en la parte superior derecha, debajo del botón Jugar
      btnLimpiar.setBackground(Color.BLUE); // Establece el color de fondo a azul
      btnLimpiar.setForeground(Color.WHITE); // Establece el color del texto a blanco
      btnLimpiar.setFont(new Font("Arial", Font.BOLD, 14)); // Establece la fuente y tamaño
      btnLimpiar.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpia los discos
               limpiarDiscos();
               numeroDiscos = 0;
            }
         });
   
      add(btnLimpiar); // Añade el botón al panel
   
        // Llama al método para pedir la cantidad de discos
       
   }
   
   private void limpiarDiscos() {
      numeroDiscos = 0;
      inicializarTorres(numeroDiscos); // Inicializa las torres vacías
      lblMovimientos.setText("Movimientos: 0");
      repaint();
   }
       
   @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      int width = getWidth();
      int height = getHeight();
        
        // Dibuja la base y las torres
      g.setColor(Color.GRAY);
      g.fillRect(width / 6, height - 100, width * 2 / 3, 10);
        
      g.setColor(Color.BLACK);
      int torreWidth = 10;
      int torreHeight = 150;
      for (int i = 0; i < 3; i++) {
         int baseX = getBaseX(i);
         g.fillRect(baseX, height - 100 - torreHeight, torreWidth, torreHeight);
      }
   
        // Dibuja los discos
      for (int i = 0; i < 3; i++) {
         Stack<Integer> torre = torres[i];
         for (int j = 0; j < torre.size(); j++) {
            int disco = torre.get(j);
            int discoWidth = getDiscoWidth(disco);
            int discoHeight = 20;
            int baseX = getBaseX(i) + (torreWidth - discoWidth) / 2;
            int baseY = height - 100 - (j + 1) * discoHeight;
                
            g.setColor(coloresDiscos[disco - 1]);
            g.fillRect(baseX, baseY, discoWidth, discoHeight);
         }
      }
   
        // Dibuja el disco que se está arrastrando
      if (discoSeleccionado != -1 && mousePos != null) {
         int discoWidth = getDiscoWidth(discoSeleccionado);
         int discoHeight = 20;
         g.setColor(coloresDiscos[discoSeleccionado - 1]);
         g.fillRect(mousePos.x - discoWidth / 2, mousePos.y - discoHeight / 2, discoWidth, discoHeight);
      }
   
        // Dibuja las etiquetas debajo de las torres
      g.setColor(Color.BLACK);
      g.setFont(new Font("Arial", Font.BOLD, 14));
      g.drawString("A", width / 4, height - 70);
      g.drawString("B", width / 2, height - 70);
      g.drawString("C", width * 3 / 4, height - 70);
   }

    // Obtener la posición X de una torre
   private int getBaseX(int torreIndex) {
      int width = getWidth();
      switch (torreIndex) {
         case 0: 
            return width / 4 - 5;
         case 1: 
            return width / 2 - 5;
         case 2: 
            return width * 3 / 4 - 5;
      }
      return 0;
   }

    // Obtener el ancho del disco basado en su tamaño
   private int getDiscoWidth(int disco) {
        //int[] discoWidths = {130, 110, 90, 70, 50};
      int[] discoWidths = {50, 70, 90, 110, 130};
      return discoWidths[disco - 1];
   }   
       
   // Método para solicitar al usuario el número de discos para jugar
   private void solicitarNumeroDiscos() {
      String input = JOptionPane.showInputDialog(this, "Ingrese el número de discos (entre 3 y 5):", "Seleccionar Discos", JOptionPane.QUESTION_MESSAGE);
      try {
         int discos = Integer.parseInt(input);
         if (discos >= 3 && discos <= 5) {
            inicializarTorres(discos); // Inicializa las torres con los discos seleccionados
            repaint(); // Redibuja el panel para mostrar los discos
         } else {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido entre 3 y 5.");
         }
      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
      }
   }
    
      
   public static void main(String[] args) {
      JFrame frame = new JFrame("Hanoi prueba");
      frmHanoi panel = new frmHanoi(5);
      frame.add(panel);
      frame.pack(); // Ajusta el tamaño del marco basado en el panel
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      frame.setLocationRelativeTo(null);
   }
}
