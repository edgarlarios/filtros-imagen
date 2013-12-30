/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagened;


//librerias
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.File;
import java.text.AttributedCharacterIterator;
import javax.swing.*;
import java.awt.Color;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *x
 * @author ragde
 */
public class ImagenEd extends JFrame implements ActionListener{

    private JButton botonImagen, botonAplicar, botonSalir, botonMostrar; //botones
    private JTextField c1, c2, c3, c4, c5, c6, c7, c8, c9;  //
    private BufferedImage FotoB, ImgBuff, BufferIm; //buffers utilizados para la imagen elegida
    private Image Imagenn; //
    private File ArchivoImg; // 
    double[][] Convolucion = new double[3][3];  //matriz utilizada para los calculos 
    int r,g,b, rojo, verde, azul; //colores de pixeles
    int[] red = new int[9]; //matrices utlizadas para almacenar los valores de los pixeles
    int[] green = new int[9];
    int[] blue = new int[9];
    Color PixelColor; //usado para  extraer el color del pixel
    
    
    ImagenEd(){
         //botones con asignacion de posicion,tamaño y nombre
        botonImagen = new JButton("1-Elegir Imagen ");
        botonImagen.setBounds(30,40,140,25);
        
        botonMostrar = new JButton("2-Mostrar Imagen ");
        botonMostrar.setBounds(30,80,140,25);
        
        botonAplicar = new JButton("3-Aplicar valores");
        botonAplicar.setBounds(30,250,140,25);
        
        botonSalir = new JButton("Salir");
        botonSalir.setBounds(30,290,140,25);
        
        //cuadros de texto iniciados como vacios y asignacion de posicion y tamaño
        c1 = new JTextField("");
        c1.setBounds(40,120,35,35);
        c1.setEditable(false);
        c2 = new JTextField("");
        c2.setBounds(80,120,35,35);
        c2.setEditable(false);
        c3 = new JTextField("");
        c3.setBounds(120,120,35,35);
        c3.setEditable(false);
        c4 = new JTextField("");
        c4.setBounds(40,160,35,35);
        c4.setEditable(false);
        c5 = new JTextField("");
        c5.setBounds(80,160,35,35);
        c5.setEditable(false);
        c6 = new JTextField("");
        c6.setBounds(120,160,35,35);
        c6.setEditable(false);
        c7 = new JTextField("");
        c7.setBounds(40,200,35,35);
        c7.setEditable(false);
        c8 = new JTextField("");
        c8.setBounds(80,200,35,35);
        c8.setEditable(false);
        c9 = new JTextField("");
        c9.setBounds(120,200,35,35);
        c9.setEditable(false);
        
        //accion de los botones al dar clic
        botonImagen.addActionListener(this);
        botonMostrar.addActionListener(this);
        botonAplicar.addActionListener(this);
        botonSalir.addActionListener(this);
        //Agrega los elementos a la ventana
        getContentPane().add(botonImagen);
        getContentPane().add(botonMostrar);
        getContentPane().add(c1);
        getContentPane().add(c2);
        getContentPane().add(c3);
        getContentPane().add(c4);
        getContentPane().add(c5);
        getContentPane().add(c6);
        getContentPane().add(c7);
        getContentPane().add(c8);
        getContentPane().add(c9);
        getContentPane().add(botonAplicar);
        getContentPane().add(botonSalir);
        repaint();
        
        getContentPane().setLayout(null);
	   setSize(700,500);//tamaño de la ventana
	   setVisible(true);//visibilidad
    }
    
    
    @Override
  public void paint (Graphics g){       
     super.paint(g);
         if(FotoB == null){
             g.drawImage(Imagenn, 200, 100, rootPane);//muestra imagen elegida
         }
         else{
             g.drawImage(FotoB, 200, 100, rootPane);//muestra nueva imagen
         }
    }
    
  public void MatrizConvolucion(){ //almacena los valores ingresados de los textfield a la matriz convolucion
    Convolucion[0][0] = Double.parseDouble(c1.getText());
    Convolucion[0][1] = Double.parseDouble(c2.getText());
    Convolucion[0][2] = Double.parseDouble(c3.getText());
    Convolucion[1][0] = Double.parseDouble(c4.getText());
    Convolucion[1][1] = Double.parseDouble(c5.getText());
    Convolucion[1][2] = Double.parseDouble(c6.getText());
    Convolucion[2][0] = Double.parseDouble(c7.getText());
    Convolucion[2][1] = Double.parseDouble(c8.getText());
    Convolucion[2][2] = Double.parseDouble(c9.getText());
  }  
     
  //extrae los colores del pixel indicado y los almacena en un arreglo
  public void Pixeles(BufferedImage img,int i, int j,int valor){
      ImgBuff = img;
      PixelColor = new Color(ImgBuff.getRGB(i,j));
      r = PixelColor.getRed();
      g = PixelColor.getGreen();
      b = PixelColor.getBlue();
      red[valor] = r;
      green[valor] = g;
      blue[valor] = b;
  }
  
  
    public void MatrizPixeles(BufferedImage img,int i, int j){
           ImgBuff = img;
           int n = ImgBuff.getWidth()-1;//ancho de la imagen
           int m = ImgBuff.getHeight()-1;//alto de la imagen         
           
           Pixeles(ImgBuff,i,j,0);       
           
           if(j-1<0)  Pixeles(ImgBuff,i,m,1); //pixeles lado izquierdo             
           else       Pixeles(ImgBuff,i,j-1,1);                                        
            //---------------------------------
           if(j+1>m)  Pixeles(ImgBuff,i,0,2);    //pixeles lado derecho            
           else       Pixeles(ImgBuff,i,j+1,2);                           
            //--------------------------------------
           if(i-1<0)  Pixeles(ImgBuff,n,j,3); //pixeles arriba            
           else       Pixeles(ImgBuff,i-1,j,3);                                             
            //------------------------------------
           if(i+1>n) Pixeles(ImgBuff,0,j,4); //pixeles abajo                           
           else      Pixeles(ImgBuff,i+1,j,4);                            
            //-------------------------------------------------
            //-------------------------------------------------
           if(i==0 && j==0)          Pixeles(ImgBuff,n,m,5);  //pixeles izquierda superior             
           else if(i != 0 && j == 0) Pixeles(ImgBuff,i-1,m,5);                                         
           else if(i==0 && j!=0)     Pixeles(ImgBuff,n,j-1,5);                           
           else                      Pixeles(ImgBuff,i-1,j-1,5);                                    
            //---------------------------------------------------
           if(i==0 && j==m)           Pixeles(ImgBuff,n,0,6);  //pixeles derecha superior                                 
           else if(i == n && j == m)  Pixeles(ImgBuff,i-1,0,6);                                        
           else if(i==0 && j!=m)      Pixeles(ImgBuff,n,j+1,6);                                   
           else
             if(j == m)     Pixeles(ImgBuff,i-1,0,6);                        
             else           Pixeles(ImgBuff,i-1,j+1,6);                                                        
            //------------------------------------------------
           if(i == n && j == 0)      Pixeles(ImgBuff,0,m,7); //pixeles izquierda inferior                             
           else if(i != n && j == 0) Pixeles(ImgBuff,i+1,m,7);                           
           else if(i==n && j!=0)     Pixeles(ImgBuff,0,j-1,7);                           
           else                      Pixeles(ImgBuff,i+1,j-1,7);                              
            //--------------------------------------------------
           if(i==n && j==m)           Pixeles(ImgBuff,0,0,8); //pixeles derecha inferior    
           else if(i != n && j == m)  Pixeles(ImgBuff,i+1,0,8);                            
           else if(i==n && j!=m)      Pixeles(ImgBuff,0,j+1,8);                               
           else                       Pixeles(ImgBuff,i+1,j+1,8);                                      
  } 
    //multiplica los valores de los pixeles obtenidos por los valores insertados y los va sumando
     public void multiplicacion(){ 
      int cont=0;
      rojo = 0;
      verde = 0;
      azul = 0;
      for(int i = 0; i <= 2; i++){
          for(int j = 0; j <= 2; j++){
            rojo  += (int)(Convolucion[i][j] * red[cont]);
            verde += (int)(Convolucion[i][j] * green[cont]);
            azul  += (int)(Convolucion[i][j] * blue[cont]);
            cont++;
          }
      }
     }
   //Metodo que sirve para que no haya desbordamiento
    public void pixelResultado(){ 
       if(rojo > 255) rojo = 255;
       if(verde > 255) verde = 255;
       if(azul > 255) azul = 255;
       if(rojo < 0) rojo = 0;
       if(verde < 0) verde = 0;
       if(azul < 0) azul = 0;              
    }
    
    //metodo principal para crear la nueva imagen
    public void NuevaImagen(BufferedImage f){
        FotoB = f;
        for(int x = 0; x < FotoB.getWidth(); x++){//comienza a recorrer los pixel de la imagen
          for(int y = 0; y < FotoB.getHeight(); y++){
             MatrizPixeles(FotoB, x, y);// Una vez encontrado la posicion de  la matriz se encuentra su submatriz.
             multiplicacion();//se opera para sacar el pixel central
             pixelResultado();
               
               //se actualiza el pixel con los nuevos valores en una copia de la imagen.
               FotoB.setRGB(x, y, new Color(rojo, verde, azul).getRGB());
               repaint();//se actualiza la pantalla con el resultado.
          }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(botonImagen)){
        JFileChooser fc = new JFileChooser();
	int buscar = fc.showOpenDialog(this);//abre el cuadro  para elegir la imagen
	if (buscar == JFileChooser.APPROVE_OPTION){
	  ArchivoImg= fc.getSelectedFile(); 
	  Imagenn = Toolkit.getDefaultToolkit().getImage(ArchivoImg.getPath());// carga la imagen a un objeto de tipo IMAGE
          
          try {
            
              BufferIm = ImageIO.read(ArchivoImg);//lee la ruta de la imagen y lo guarda en la variable
          } catch (IOException ex) {
                    Logger.getLogger(ImagenEd.class.getName()).log(Level.SEVERE, null, ex);
          } 
        }
      }
      //Muestra la imagen y habilita los JTextfield para agregar valores  
      if(e.getSource().equals(botonMostrar)){
         repaint();  
         c1.setEditable(true);
         c2.setEditable(true);
         c3.setEditable(true);
         c4.setEditable(true);
         c5.setEditable(true);
         c6.setEditable(true);
         c7.setEditable(true);
         c8.setEditable(true);
         c9.setEditable(true);
      }
      //Llama al metodo principal
      if(e.getSource().equals(botonAplicar)){ 
         MatrizConvolucion();
         NuevaImagen(BufferIm);
      }
      //Cierra el programa
      if(e.getSource().equals(botonSalir)){
          System.exit(0);
      }
    }
    
    public static void main(String[] args) {
       ImagenEd x = new ImagenEd();
        // TODO code application logic here
    }    
}
