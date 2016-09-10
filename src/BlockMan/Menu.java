
package BlockMan;


import com.centralnexus.input.Joystick;
import com.centralnexus.input.JoystickListener;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class Menu extends javax.swing.JFrame implements KeyListener,JoystickListener {
    /**
     * Creates new form Menu
     */
    
    //por defecto es 2 (16 pixel/tile (normal))
    static int resolucionPorDefecto=2;
    static int resolucionConfigurada=2;
    static int resolucionCancelada=2;
    
    public boolean botones[][]=new boolean[4][16];//[jugadores][todos los botones].
    
    public static final int[][] teclas=new int[4][8];
    static int[][] controlesJugadores=new int[4][8];
    static int[][] controlesPorDefecto=new int[4][8];
    static int[][] controlesConfigurados=new int[4][8];
    static int[][] controlesCancelados=new int[4][8];
    static int[][] teclasPorDefecto;
    static int[][] teclasConfiguradas;
    static int[][] teclasCanceladas;
    
    static int[] dispositivosRealesCancelados;
    static int[] dispositivosCancelados;
    static int[] ordenDispositivosReal;
    static int[] ordenDispositivos;
    static int[] joyIDX;
    static int indiceJugadorTeclado=0;
    static boolean[] POVActivado;
    
    private int boton;
    private int modoJuego;
    
    DefaultComboBoxModel modelo=new DefaultComboBoxModel();
    
    
private boolean yaHayJoy1=false;
private boolean yaHayJoy2=false;
private boolean yaHayJoy3=false;
private boolean yaHayJoy4=false;
private boolean POVJ1=true;
private boolean POVJ2=false;
private boolean POVJ3=false;
private boolean POVJ4=false;

public final int
BUTTON1 = 0, 
BUTTON2 = 1, 
BUTTON3 = 2, 
BUTTON4 = 3,
UP = 4,
DOWN = 5,
LEFT = 6,
RIGHT = 7,
P_UP = 8,
P_DOWN = 9,
P_LEFT = 10,
P_RIGHT = 11,
P_UP_RIGHT = 12,//NO SE PUEDE ASIGNAR PARA MOVER AL PERSONAJE
P_DOWN_RIGHT = 13,//NO SE PUEDE ASIGNAR PARA MOVER AL PERSONAJE
P_DOWN_LEFT = 14,//NO SE PUEDE ASIGNAR PARA MOVER AL PERSONAJE
P_UP_LEFT = 15;//NO SE PUEDE ASIGNAR PARA MOVER AL PERSONAJE

// Variable members 
private Joystick[] pJoy=new Joystick[4];
private int nNumDevices;
    
    public Menu() {
        super();
        initComponents();
        
        
        ordenDispositivos=new int[4];//un dispositivo para cada jugador.
        ordenDispositivosReal=new int[4];//un dispositivo para cada jugador.
        dispositivosCancelados=new int[4];//un dispositivo para cada jugador.
        dispositivosRealesCancelados=new int[4];//un dispositivo para cada jugador.
        
        POVActivado=new boolean[4];
        
        for(int i=0;i<4;i++){
            ordenDispositivos[i]=-1;//teclado, el cero puede ser ocupado por el id de un joystick.
            ordenDispositivosReal[i]=0;
        }
        
        presione.addKeyListener(this);
        
        
        teclasConfiguradas=new int[4][8];
        
        teclasCanceladas=new int[4][8];
        
        
        obtenerConfiguracion();
        
        //ya se asigno la resolucionConfigurada.
        resolucionPorDefecto=2;
        
        
        
        teclasPorDefecto=new int[4][8] ;
        teclasPorDefecto[0][0]=KeyEvent.VK_LEFT;
        teclasPorDefecto[0][1]=KeyEvent.VK_RIGHT;
        teclasPorDefecto[0][2]=KeyEvent.VK_DOWN;
        teclasPorDefecto[0][3]=KeyEvent.VK_UP;
        teclasPorDefecto[0][4]=KeyEvent.VK_SPACE;
        teclasPorDefecto[0][5]=KeyEvent.VK_F;
        teclasPorDefecto[0][6]=KeyEvent.VK_S;
        teclasPorDefecto[0][7]=KeyEvent.VK_O;
        teclasPorDefecto[1][0]=KeyEvent.VK_LEFT;
        teclasPorDefecto[1][1]=KeyEvent.VK_RIGHT;
        teclasPorDefecto[1][2]=KeyEvent.VK_DOWN;
        teclasPorDefecto[1][3]=KeyEvent.VK_UP;
        teclasPorDefecto[1][4]=KeyEvent.VK_SPACE;
        teclasPorDefecto[1][5]=KeyEvent.VK_F;
        teclasPorDefecto[1][6]=KeyEvent.VK_S;
        teclasPorDefecto[1][7]=KeyEvent.VK_O;
        teclasPorDefecto[2][0]=KeyEvent.VK_LEFT;
        teclasPorDefecto[2][1]=KeyEvent.VK_RIGHT;
        teclasPorDefecto[2][2]=KeyEvent.VK_DOWN;
        teclasPorDefecto[2][3]=KeyEvent.VK_UP;
        teclasPorDefecto[2][4]=KeyEvent.VK_SPACE;
        teclasPorDefecto[2][5]=KeyEvent.VK_F;
        teclasPorDefecto[2][6]=KeyEvent.VK_S;
        teclasPorDefecto[2][7]=KeyEvent.VK_O;
        teclasPorDefecto[3][0]=KeyEvent.VK_LEFT;
        teclasPorDefecto[3][1]=KeyEvent.VK_RIGHT;
        teclasPorDefecto[3][2]=KeyEvent.VK_DOWN;
        teclasPorDefecto[3][3]=KeyEvent.VK_UP;
        teclasPorDefecto[3][4]=KeyEvent.VK_SPACE;
        teclasPorDefecto[3][5]=KeyEvent.VK_F;
        teclasPorDefecto[3][6]=KeyEvent.VK_S;
        teclasPorDefecto[3][7]=KeyEvent.VK_O;
        
        controlesPorDefecto[0][0]=10;//izquierda
        controlesPorDefecto[0][1]=11;//derecha
        controlesPorDefecto[0][2]=9;//abajo
        controlesPorDefecto[0][3]=8;//arriba
        controlesPorDefecto[0][4]=2;//saltar(1)
        controlesPorDefecto[0][5]=3;//disparar(0)
        controlesPorDefecto[0][6]=1;//correr(2)
        controlesPorDefecto[0][7]=0;//cambiar(3)
        
        controlesPorDefecto[1][0]=10;//izquierda
        controlesPorDefecto[1][1]=11;//derecha
        controlesPorDefecto[1][2]=9;//abajo
        controlesPorDefecto[1][3]=8;//arriba
        controlesPorDefecto[1][4]=2;//saltar(1)
        controlesPorDefecto[1][5]=3;//disparar(0)
        controlesPorDefecto[1][6]=1;//correr(2)
        controlesPorDefecto[1][7]=0;//cambiar(3)
        
        controlesPorDefecto[2][0]=10;//izquierda
        controlesPorDefecto[2][1]=11;//derecha
        controlesPorDefecto[2][2]=9;//abajo
        controlesPorDefecto[2][3]=8;//arriba
        controlesPorDefecto[2][4]=2;//saltar(1)
        controlesPorDefecto[2][5]=3;//disparar(0)
        controlesPorDefecto[2][6]=1;//correr(2)
        controlesPorDefecto[2][7]=0;//cambiar(3)
        
        controlesPorDefecto[3][0]=10;//izquierda
        controlesPorDefecto[3][1]=11;//derecha
        controlesPorDefecto[3][2]=9;//abajo
        controlesPorDefecto[3][3]=8;//arriba
        controlesPorDefecto[3][4]=2;//saltar(1)
        controlesPorDefecto[3][5]=3;//disparar(0)
        controlesPorDefecto[3][6]=1;//correr(2)
        controlesPorDefecto[3][7]=0;//cambiar(3)
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        menu.setVisible(true);
        
        this.pJoy[0] = null;
        this.pJoy[1] = null; 
        this.pJoy[2] = null; 
        this.pJoy[3] = null;  
        this.nNumDevices = Joystick.getNumDevices(); 
        if (this.nNumDevices > 0) 
        { 
            try 
            {

                for (int idx = 0; idx < Joystick.getNumDevices(); idx++) {
                    if (Joystick.isPluggedIn(idx)) {
                        if(yaHayJoy1==false){
                            pJoy[0] = Joystick.createInstance(idx);
                            yaHayJoy1=true;
                        }else{
                            if(yaHayJoy2==false){
                                pJoy[1] = Joystick.createInstance(idx);
                                yaHayJoy2=true;
                            }else{
                                if(yaHayJoy3==false){
                                    pJoy[2] = Joystick.createInstance(idx);
                                    yaHayJoy3=true;
                                }else{
                                    if(yaHayJoy4==false){
                                        pJoy[3] = Joystick.createInstance(idx);
                                        yaHayJoy4=true;
                                    }
                                }
                            }
                        }

                    }
                }
                if(pJoy[0]!=null){
                    this.pJoy[0].addJoystickListener(this); 
                }
                if(pJoy[1]!=null){
                    this.pJoy[1].addJoystickListener(this); 
                }
                if(pJoy[2]!=null){
                    this.pJoy[2].addJoystickListener(this); 
                }
                if(pJoy[3]!=null){
                    this.pJoy[3].addJoystickListener(this); 
                }

            } 
            catch (IOException e) 
            { 
                e.printStackTrace(); 
                this.pJoy[0] = null;
                this.pJoy[1] = null; 
                this.pJoy[2] = null; 
                this.pJoy[3] = null; 
            } 
        } 
        
        modelo=new DefaultComboBoxModel();
        modelo.addElement("TECLADO");
        try{
            int contador=0;
            Joystick joy=null;
            for (int idx = 0; idx < Joystick.getNumDevices(); idx++) {
                if (Joystick.isPluggedIn(idx)) {
                    contador++;
                }
            }
            
            joyIDX=new int[contador];//numero de joysticks conectados.
            
            contador=0;
            
            for (int idx = 0; idx < Joystick.getNumDevices(); idx++) {
                if (Joystick.isPluggedIn(idx)) {
                    joyIDX[contador]=idx;
                    joy=Joystick.createInstance(idx);
                    modelo.addElement("JOYSTICK  "+joy.getID());
                    contador++;
                }
            }
        }catch (IOException e){ 
            e.printStackTrace();
        }
        boxDispositivo.setModel(modelo);
    }
        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        menu = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        botonJugar = new javax.swing.JButton();
        botonOpciones = new javax.swing.JButton();
        controles = new javax.swing.JPanel();
        boxJugadores = new javax.swing.JComboBox();
        botonRestaurar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        botonAceptar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();
        botonIzquierda = new javax.swing.JButton();
        botonDerecha = new javax.swing.JButton();
        botonAgacharse = new javax.swing.JButton();
        botonPaleta = new javax.swing.JButton();
        botonSaltar = new javax.swing.JButton();
        botonDisparar = new javax.swing.JButton();
        boxDispositivo = new javax.swing.JComboBox();
        botonBorrar = new javax.swing.JButton();
        botonCorrer = new javax.swing.JButton();
        botonCambiar = new javax.swing.JButton();
        botonDesconocido1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        botonDesconocido2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        presione = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        opciones = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        botonControles = new javax.swing.JButton();
        botonConfiguracion = new javax.swing.JButton();
        botonInicio3 = new javax.swing.JButton();
        modoDeJuego = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        botonNormal = new javax.swing.JButton();
        botonBatalla = new javax.swing.JButton();
        botonInicio2 = new javax.swing.JButton();
        jugadores = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        unJugadores = new javax.swing.JButton();
        dosJugadores = new javax.swing.JButton();
        tresJugadores = new javax.swing.JButton();
        cuatroJugadores = new javax.swing.JButton();
        botonInicio1 = new javax.swing.JButton();
        configuracion = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        boxPixeles = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        sliderSonido = new javax.swing.JSlider();
        botonAceptar2 = new javax.swing.JButton();
        botonCancelar2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BLOCKMAN LAUNCHER");
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 360));

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(380, 338));

        menu.setBackground(new java.awt.Color(255, 255, 0));
        menu.setPreferredSize(new java.awt.Dimension(380, 338));

        jLabel7.setFont(new java.awt.Font("Showcard Gothic", 0, 48)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("BLOCKMAN");

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(190, 157));

        jPanel6.setBackground(new java.awt.Color(255, 0, 0));

        botonJugar.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        botonJugar.setText("JUGAR");
        botonJugar.setPreferredSize(new java.awt.Dimension(150, 60));
        botonJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonJugarActionPerformed(evt);
            }
        });

        botonOpciones.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        botonOpciones.setText("OPCIONES");
        botonOpciones.setPreferredSize(new java.awt.Dimension(180, 60));
        botonOpciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonOpcionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(botonOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(botonJugar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(botonJugar, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(95, 95, 95))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );

        controles.setBackground(new java.awt.Color(255, 255, 0));
        controles.setPreferredSize(new java.awt.Dimension(380, 338));

        boxJugadores.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        boxJugadores.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jugador  1", "Jugador  2", "Jugador  3", "Jugador  4" }));
        boxJugadores.setPreferredSize(new java.awt.Dimension(170, 20));
        boxJugadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxJugadoresActionPerformed(evt);
            }
        });

        botonRestaurar.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        botonRestaurar.setText("Restaurar");
        botonRestaurar.setPreferredSize(new java.awt.Dimension(170, 27));
        botonRestaurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRestaurarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Izquierda");
        jLabel1.setPreferredSize(new java.awt.Dimension(80, 27));

        jLabel2.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Derecha");
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 27));

        jLabel3.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Paleta");
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 27));

        jLabel4.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Saltar");
        jLabel4.setMaximumSize(new java.awt.Dimension(100, 14));
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 27));

        jLabel5.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Disparar");
        jLabel5.setPreferredSize(new java.awt.Dimension(80, 27));

        jLabel6.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Agacharse");
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 27));

        botonAceptar.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        botonAceptar.setText("Aceptar");
        botonAceptar.setPreferredSize(new java.awt.Dimension(170, 23));
        botonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAceptarActionPerformed(evt);
            }
        });

        botonCancelar.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        botonCancelar.setText("Cancelar");
        botonCancelar.setPreferredSize(new java.awt.Dimension(170, 27));
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        botonIzquierda.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonIzquierda.setPreferredSize(new java.awt.Dimension(80, 27));
        botonIzquierda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonIzquierdaActionPerformed(evt);
            }
        });

        botonDerecha.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonDerecha.setPreferredSize(new java.awt.Dimension(80, 27));
        botonDerecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDerechaActionPerformed(evt);
            }
        });

        botonAgacharse.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonAgacharse.setPreferredSize(new java.awt.Dimension(80, 27));
        botonAgacharse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgacharseActionPerformed(evt);
            }
        });

        botonPaleta.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonPaleta.setPreferredSize(new java.awt.Dimension(80, 27));
        botonPaleta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPaletaActionPerformed(evt);
            }
        });

        botonSaltar.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonSaltar.setPreferredSize(new java.awt.Dimension(80, 27));
        botonSaltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSaltarActionPerformed(evt);
            }
        });

        botonDisparar.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonDisparar.setPreferredSize(new java.awt.Dimension(80, 27));
        botonDisparar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDispararActionPerformed(evt);
            }
        });

        boxDispositivo.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        boxDispositivo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Teclado" }));
        boxDispositivo.setPreferredSize(new java.awt.Dimension(170, 20));
        boxDispositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxDispositivoActionPerformed(evt);
            }
        });

        botonBorrar.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        botonBorrar.setText("BORRAR");
        botonBorrar.setPreferredSize(new java.awt.Dimension(170, 27));
        botonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarActionPerformed(evt);
            }
        });

        botonCorrer.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonCorrer.setPreferredSize(new java.awt.Dimension(80, 27));
        botonCorrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCorrerActionPerformed(evt);
            }
        });

        botonCambiar.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonCambiar.setPreferredSize(new java.awt.Dimension(80, 27));
        botonCambiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCambiarActionPerformed(evt);
            }
        });

        botonDesconocido1.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonDesconocido1.setPreferredSize(new java.awt.Dimension(80, 27));
        botonDesconocido1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDesconocido1ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("CORRER");
        jLabel14.setPreferredSize(new java.awt.Dimension(80, 27));

        botonDesconocido2.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        botonDesconocido2.setPreferredSize(new java.awt.Dimension(80, 27));
        botonDesconocido2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDesconocido2ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("?");
        jLabel17.setPreferredSize(new java.awt.Dimension(80, 27));

        jLabel18.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("?");
        jLabel18.setMaximumSize(new java.awt.Dimension(100, 14));
        jLabel18.setPreferredSize(new java.awt.Dimension(80, 27));

        jLabel19.setFont(new java.awt.Font("Showcard Gothic", 0, 10)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("CAMBIAR");
        jLabel19.setPreferredSize(new java.awt.Dimension(80, 27));

        javax.swing.GroupLayout controlesLayout = new javax.swing.GroupLayout(controles);
        controles.setLayout(controlesLayout);
        controlesLayout.setHorizontalGroup(
            controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlesLayout.createSequentialGroup()
                        .addComponent(boxJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(boxDispositivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlesLayout.createSequentialGroup()
                        .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(controlesLayout.createSequentialGroup()
                                .addComponent(botonAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, controlesLayout.createSequentialGroup()
                                .addComponent(botonRestaurar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botonBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(controlesLayout.createSequentialGroup()
                        .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(botonDerecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botonAgacharse, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botonIzquierda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(botonPaleta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botonSaltar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonDisparar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(botonCorrer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botonCambiar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(botonDesconocido1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botonDesconocido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(10, 10, 10))
        );
        controlesLayout.setVerticalGroup(
            controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxDispositivo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonRestaurar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlesLayout.createSequentialGroup()
                        .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(controlesLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(controlesLayout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(controlesLayout.createSequentialGroup()
                        .addComponent(botonDisparar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonCorrer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonCambiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonDesconocido1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonDesconocido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlesLayout.createSequentialGroup()
                        .addComponent(botonIzquierda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonDerecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonAgacharse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonPaleta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonSaltar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        presione.setBackground(new java.awt.Color(255, 255, 0));
        presione.setPreferredSize(new java.awt.Dimension(380, 338));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setPreferredSize(new java.awt.Dimension(190, 157));

        jPanel7.setBackground(new java.awt.Color(255, 0, 0));
        jPanel7.setPreferredSize(new java.awt.Dimension(170, 135));

        jLabel8.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("UNA TECLA");
        jLabel8.setPreferredSize(new java.awt.Dimension(133, 50));

        jLabel9.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("PRESIONA");
        jLabel9.setPreferredSize(new java.awt.Dimension(133, 50));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout presioneLayout = new javax.swing.GroupLayout(presione);
        presione.setLayout(presioneLayout);
        presioneLayout.setHorizontalGroup(
            presioneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, presioneLayout.createSequentialGroup()
                .addContainerGap(95, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(95, 95, 95))
        );
        presioneLayout.setVerticalGroup(
            presioneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(presioneLayout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );

        opciones.setBackground(new java.awt.Color(255, 255, 0));
        opciones.setPreferredSize(new java.awt.Dimension(380, 338));

        jLabel10.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("OPCIONES");

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setPreferredSize(new java.awt.Dimension(190, 157));

        jPanel8.setBackground(new java.awt.Color(255, 0, 0));

        botonControles.setFont(new java.awt.Font("Showcard Gothic", 0, 14)); // NOI18N
        botonControles.setText("CONTROLES");
        botonControles.setPreferredSize(new java.awt.Dimension(150, 60));
        botonControles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonControlesActionPerformed(evt);
            }
        });

        botonConfiguracion.setFont(new java.awt.Font("Showcard Gothic", 0, 14)); // NOI18N
        botonConfiguracion.setText("configuracion");
        botonConfiguracion.setPreferredSize(new java.awt.Dimension(180, 60));
        botonConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConfiguracionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(botonConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(botonControles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(botonControles, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        botonInicio3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        botonInicio3.setIconTextGap(-3);
        botonInicio3.setPreferredSize(new java.awt.Dimension(60, 60));
        botonInicio3.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        botonInicio3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        botonInicio3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonInicio3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInicio3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout opcionesLayout = new javax.swing.GroupLayout(opciones);
        opciones.setLayout(opcionesLayout);
        opcionesLayout.setHorizontalGroup(
            opcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(opcionesLayout.createSequentialGroup()
                .addGroup(opcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(opcionesLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addGroup(opcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(opcionesLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(botonInicio3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(95, 95, 95))
        );
        opcionesLayout.setVerticalGroup(
            opcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(opcionesLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonInicio3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        modoDeJuego.setBackground(new java.awt.Color(255, 255, 0));
        modoDeJuego.setPreferredSize(new java.awt.Dimension(380, 338));

        jLabel11.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("MODO DE JUEGO");

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setPreferredSize(new java.awt.Dimension(190, 157));

        jPanel9.setBackground(new java.awt.Color(255, 0, 0));

        botonNormal.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        botonNormal.setText("NORMAL");
        botonNormal.setPreferredSize(new java.awt.Dimension(150, 60));
        botonNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNormalActionPerformed(evt);
            }
        });

        botonBatalla.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        botonBatalla.setText("BATALLA");
        botonBatalla.setPreferredSize(new java.awt.Dimension(180, 60));
        botonBatalla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBatallaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(botonBatalla, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(botonNormal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(botonNormal, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonBatalla, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        botonInicio2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        botonInicio2.setIconTextGap(-3);
        botonInicio2.setPreferredSize(new java.awt.Dimension(60, 60));
        botonInicio2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        botonInicio2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        botonInicio2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonInicio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInicio2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout modoDeJuegoLayout = new javax.swing.GroupLayout(modoDeJuego);
        modoDeJuego.setLayout(modoDeJuegoLayout);
        modoDeJuegoLayout.setHorizontalGroup(
            modoDeJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modoDeJuegoLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(95, 95, 95))
            .addGroup(modoDeJuegoLayout.createSequentialGroup()
                .addGroup(modoDeJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(modoDeJuegoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(modoDeJuegoLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(botonInicio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        modoDeJuegoLayout.setVerticalGroup(
            modoDeJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modoDeJuegoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonInicio2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jugadores.setBackground(new java.awt.Color(255, 255, 0));
        jugadores.setPreferredSize(new java.awt.Dimension(380, 338));

        jLabel12.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("JUGADORES");

        jPanel11.setBackground(new java.awt.Color(0, 0, 0));
        jPanel11.setPreferredSize(new java.awt.Dimension(190, 157));

        jPanel12.setBackground(new java.awt.Color(255, 0, 0));

        unJugadores.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        unJugadores.setText("1");
        unJugadores.setPreferredSize(new java.awt.Dimension(150, 60));
        unJugadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unJugadoresActionPerformed(evt);
            }
        });

        dosJugadores.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        dosJugadores.setText("2");
        dosJugadores.setPreferredSize(new java.awt.Dimension(150, 60));
        dosJugadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosJugadoresActionPerformed(evt);
            }
        });

        tresJugadores.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        tresJugadores.setText("3");
        tresJugadores.setPreferredSize(new java.awt.Dimension(150, 60));
        tresJugadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tresJugadoresActionPerformed(evt);
            }
        });

        cuatroJugadores.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        cuatroJugadores.setText("4");
        cuatroJugadores.setPreferredSize(new java.awt.Dimension(150, 60));
        cuatroJugadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuatroJugadoresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(unJugadores, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(tresJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dosJugadores, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(cuatroJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unJugadores, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(dosJugadores, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tresJugadores, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(cuatroJugadores, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        botonInicio1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        botonInicio1.setIconTextGap(-3);
        botonInicio1.setPreferredSize(new java.awt.Dimension(60, 60));
        botonInicio1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/inicio.png"))); // NOI18N
        botonInicio1.setSelectedIcon(new javax.swing.ImageIcon("C:\\blockman\\inicio.png")); // NOI18N
        botonInicio1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonInicio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInicio1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jugadoresLayout = new javax.swing.GroupLayout(jugadores);
        jugadores.setLayout(jugadoresLayout);
        jugadoresLayout.setHorizontalGroup(
            jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jugadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jugadoresLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jugadoresLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94))))
            .addGroup(jugadoresLayout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(botonInicio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jugadoresLayout.setVerticalGroup(
            jugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jugadoresLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonInicio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        configuracion.setBackground(new java.awt.Color(255, 255, 0));
        configuracion.setPreferredSize(new java.awt.Dimension(380, 338));

        jLabel13.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("CONFIGURACION");

        boxPixeles.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        boxPixeles.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "4 pixel/tile (small)", "8 pixel/tile (medium)", "16 pixel/tile (normal)" }));
        boxPixeles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxPixelesActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("resolucion");
        jLabel15.setPreferredSize(new java.awt.Dimension(350, 20));

        jLabel16.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("VOLUMEN DE SONIDO");
        jLabel16.setPreferredSize(new java.awt.Dimension(360, 20));

        sliderSonido.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N

        botonAceptar2.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        botonAceptar2.setText("ACEPTAR");
        botonAceptar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAceptar2ActionPerformed(evt);
            }
        });

        botonCancelar2.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        botonCancelar2.setText("CANCELAR");
        botonCancelar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout configuracionLayout = new javax.swing.GroupLayout(configuracion);
        configuracion.setLayout(configuracionLayout);
        configuracionLayout.setHorizontalGroup(
            configuracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configuracionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configuracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configuracionLayout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addGroup(configuracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boxPixeles, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sliderSonido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(configuracionLayout.createSequentialGroup()
                        .addGroup(configuracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(configuracionLayout.createSequentialGroup()
                                .addComponent(botonAceptar2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(botonCancelar2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        configuracionLayout.setVerticalGroup(
            configuracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configuracionLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(boxPixeles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(sliderSonido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(configuracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCancelar2)
                    .addComponent(botonAceptar2))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 10, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(controles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(presione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(opciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(modoDeJuego, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 10, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(configuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 10, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(controles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 11, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(presione, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(opciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(modoDeJuego, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(jugadores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(configuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 11, Short.MAX_VALUE)))
        );
        jLayeredPane1.setLayer(menu, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(controles, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(presione, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(opciones, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(modoDeJuego, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jugadores, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(configuracion, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonOpcionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonOpcionesActionPerformed
        menu.setVisible(false);
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(true);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        
    }//GEN-LAST:event_botonOpcionesActionPerformed

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        //reemplazar teclas por teclas de lasteclasCanceladas
        for(int i=0;i<8;i++){
            teclasConfiguradas[0][i]=teclasCanceladas[0][i];
            teclasConfiguradas[1][i]=teclasCanceladas[1][i];
            teclasConfiguradas[2][i]=teclasCanceladas[2][i];
            teclasConfiguradas[3][i]=teclasCanceladas[3][i];
            controlesConfigurados[0][i]=controlesCancelados[0][i];
            controlesConfigurados[1][i]=controlesCancelados[1][i];
            controlesConfigurados[2][i]=controlesCancelados[2][i];
            controlesConfigurados[3][i]=controlesCancelados[3][i];
        }
        
        ordenDispositivos[0]=dispositivosCancelados[0];
        ordenDispositivos[1]=dispositivosCancelados[1];
        ordenDispositivos[2]=dispositivosCancelados[2];
        ordenDispositivos[3]=dispositivosCancelados[3];
        
        ordenDispositivosReal[0]=dispositivosRealesCancelados[0];
        ordenDispositivosReal[1]=dispositivosRealesCancelados[1];
        ordenDispositivosReal[2]=dispositivosRealesCancelados[2];
        ordenDispositivosReal[3]=dispositivosRealesCancelados[3];
        
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        menu.setVisible(true);
    }//GEN-LAST:event_botonCancelarActionPerformed

    private void botonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAceptarActionPerformed
        //no es necesario guardar configuracion (si se cancela se restauran las teclas).
        //es necesario traspasar los datos a un documento de texto.
        guardarConfiguracion();
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        menu.setVisible(true);
    }//GEN-LAST:event_botonAceptarActionPerformed

    private void botonRestaurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRestaurarActionPerformed
        if(boxDispositivo.getSelectedIndex()==0){
            //restaurar configuracion de teclas por defecto
            restaurar(boxJugadores.getSelectedIndex());
        }else{
            //restaurar configuracion de controles por defecto
            restaurarControles(boxJugadores.getSelectedIndex());
        }
        
    }//GEN-LAST:event_botonRestaurarActionPerformed

    private void botonJugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonJugarActionPerformed
        menu.setVisible(false);
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(true);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
    }//GEN-LAST:event_botonJugarActionPerformed

    private void botonIzquierdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonIzquierdaActionPerformed
        menu.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        presione.setVisible(true);
        presione.setFocusable(true);
        controles.setVisible(false);
        if(boxDispositivo.getSelectedIndex()!=0){
            jLabel8.setText("UN BOTON");
        }else{
            jLabel8.setText("UNA TECLA");
        }
        boton=0;
    }//GEN-LAST:event_botonIzquierdaActionPerformed

    private void boxJugadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxJugadoresActionPerformed
        int jugador = boxJugadores.getSelectedIndex();
        boxDispositivo.setSelectedIndex(ordenDispositivosReal[jugador]);
        mostrarControlesSegunJugador(jugador);
    }//GEN-LAST:event_boxJugadoresActionPerformed

    private void botonDerechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDerechaActionPerformed
        menu.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        presione.setVisible(true);
        presione.setFocusable(true);
        controles.setVisible(false);
        if(boxDispositivo.getSelectedIndex()!=0){
            jLabel8.setText("UN BOTON");
        }else{
            jLabel8.setText("UNA TECLA");
        }
        boton=1;
    }//GEN-LAST:event_botonDerechaActionPerformed

    private void botonAgacharseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgacharseActionPerformed
        menu.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        presione.setVisible(true);
        presione.setFocusable(true);
        controles.setVisible(false);
        if(boxDispositivo.getSelectedIndex()!=0){
            jLabel8.setText("UN BOTON");
        }else{
            jLabel8.setText("UNA TECLA");
        }
        boton=2;
    }//GEN-LAST:event_botonAgacharseActionPerformed

    private void botonPaletaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPaletaActionPerformed
        menu.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        presione.setVisible(true);
        presione.setFocusable(true);
        controles.setVisible(false);
        if(boxDispositivo.getSelectedIndex()!=0){
            jLabel8.setText("UN BOTON");
        }else{
            jLabel8.setText("UNA TECLA");
        }
        boton=3;
    }//GEN-LAST:event_botonPaletaActionPerformed

    private void botonSaltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSaltarActionPerformed
        menu.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        presione.setVisible(true);
        presione.setFocusable(true);
        controles.setVisible(false);
        if(boxDispositivo.getSelectedIndex()!=0){
            jLabel8.setText("UN BOTON");
        }else{
            jLabel8.setText("UNA TECLA");
        }
        boton=4;
    }//GEN-LAST:event_botonSaltarActionPerformed

    private void botonDispararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDispararActionPerformed
        menu.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        presione.setVisible(true);
        presione.setFocusable(true);
        controles.setVisible(false);
        if(boxDispositivo.getSelectedIndex()!=0){
            jLabel8.setText("UN BOTON");
        }else{
            jLabel8.setText("UNA TECLA");
        }
        boton=5;
    }//GEN-LAST:event_botonDispararActionPerformed

    private void botonControlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonControlesActionPerformed
        for(int i=0;i<8;i++){
            teclasCanceladas[0][i]=teclasConfiguradas[0][i];
            teclasCanceladas[1][i]=teclasConfiguradas[1][i];
            teclasCanceladas[2][i]=teclasConfiguradas[2][i];
            teclasCanceladas[3][i]=teclasConfiguradas[3][i];
            controlesCancelados[0][i]=controlesConfigurados[0][i];
            controlesCancelados[1][i]=controlesConfigurados[1][i];
            controlesCancelados[2][i]=controlesConfigurados[2][i];
            controlesCancelados[3][i]=controlesConfigurados[3][i];
        }
        
        //se guardan los dispositivos en el orden en que estaban por si se cancela.
        dispositivosCancelados[0]=ordenDispositivos[0];
        dispositivosCancelados[1]=ordenDispositivos[1];
        dispositivosCancelados[2]=ordenDispositivos[2];
        dispositivosCancelados[3]=ordenDispositivos[3];
        
        dispositivosRealesCancelados[0]=ordenDispositivosReal[0];
        dispositivosRealesCancelados[1]=ordenDispositivosReal[1];
        dispositivosRealesCancelados[2]=ordenDispositivosReal[2];
        dispositivosRealesCancelados[3]=ordenDispositivosReal[3];
        
        boxJugadores.setSelectedIndex(0);
        int jugador = boxJugadores.getSelectedIndex();
        for(int i=0;i<8;i++){
            boton=i;
            if(boxDispositivo.getSelectedIndex()==0){
                nombrarBoton(nombrarTecla(teclasConfiguradas[jugador][i]));
            }else{
                nombrarBoton(nombrarControles(controlesConfigurados[jugador][i]));
            }
        }
        
        menu.setVisible(false);
        controles.setVisible(true);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
    }//GEN-LAST:event_botonControlesActionPerformed

    private void botonConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonConfiguracionActionPerformed
        resolucionCancelada=resolucionConfigurada;
        boxPixeles.setSelectedIndex(resolucionConfigurada);
        menu.setVisible(false);
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(true);
    }//GEN-LAST:event_botonConfiguracionActionPerformed

    private void botonNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNormalActionPerformed
        menu.setVisible(false);
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(true);
        configuracion.setVisible(false);
        modoJuego=1;
    }//GEN-LAST:event_botonNormalActionPerformed

    private void botonBatallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBatallaActionPerformed
        menu.setVisible(false);
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(true);
        configuracion.setVisible(false);
        modoJuego=2;
    }//GEN-LAST:event_botonBatallaActionPerformed

    private void unJugadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unJugadoresActionPerformed
        //traspasar la configuracion de las teclas al juego     
        Tile tile=new Tile();
        Teclas teclas=new Teclas();
        
        for(int i=0;i<4;i++){
            for(int j=0;j<8;j++){
                this.teclas[i][j]=teclasConfiguradas[i][j];
                teclas.teclas[i][j]=this.teclas[i][j];
                controlesJugadores[i][j]=controlesConfigurados[i][j];
            }
        }
        teclas.modoJuego=this.modoJuego;
        teclas.jugadores=1;
        teclas.jugadoresTotales=1;
        switch(resolucionConfigurada){
            case 0:
                Teclas.resolucion=0;
                tile.TILE_WIDTH=4;
                tile.TILE_HEIGHT=4;
                break;
            case 1:
                Teclas.resolucion=1;
                tile.TILE_WIDTH=8;
                tile.TILE_HEIGHT=8;
                break;
            case 2:
                Teclas.resolucion=2;
                tile.TILE_WIDTH=16;
                tile.TILE_HEIGHT=16;
                break;
        }
        
        
        inicio();
        
        //crear un nuevo juego:
        Juego.permiso=false;
        Juego.nivel=0;
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Juego.permiso=true;
        if(Juego.corriendo==true){
            Juego.nivel=1;
        }else{
            Juego.nivel=1;
        }
        Juego.corriendo=false;
        //this.dispose();
        Juego.estaCerrada=true;
    }//GEN-LAST:event_unJugadoresActionPerformed

    private void dosJugadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosJugadoresActionPerformed
        //traspasar la configuracion de las teclas al juego     
        Tile tile=new Tile();
        Teclas teclas=new Teclas();
        
        for(int i=0;i<4;i++){
            for(int j=0;j<8;j++){
                this.teclas[i][j]=teclasConfiguradas[i][j];
                teclas.teclas[i][j]=this.teclas[i][j];
                controlesJugadores[i][j]=controlesConfigurados[i][j];
            }
        }
        teclas.modoJuego=this.modoJuego;
        teclas.jugadores=2;
        teclas.jugadoresTotales=2;
        switch(resolucionConfigurada){
            case 0:
                Teclas.resolucion=0;
                tile.TILE_WIDTH=4;
                tile.TILE_HEIGHT=4;
                break;
            case 1:
                Teclas.resolucion=1;
                tile.TILE_WIDTH=8;
                tile.TILE_HEIGHT=8;
                break;
            case 2:
                Teclas.resolucion=2;
                tile.TILE_WIDTH=16;
                tile.TILE_HEIGHT=16;
                break;
        }
        
        inicio();
        
        //crear un nuevo juego:
        Juego.permiso=false;
        Juego.nivel=0;
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Juego.permiso=true;
        if(Juego.corriendo==true){
            Juego.nivel=1;
        }else{
            Juego.nivel=1;
        }
        Juego.corriendo=false;
        Juego.estaCerrada=true;
    }//GEN-LAST:event_dosJugadoresActionPerformed

    private void tresJugadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tresJugadoresActionPerformed
        //traspasar la configuracion de las teclas al juego     
        Tile tile=new Tile();
        Teclas teclas=new Teclas();
        
        for(int i=0;i<4;i++){
            for(int j=0;j<8;j++){
                this.teclas[i][j]=teclasConfiguradas[i][j];
                teclas.teclas[i][j]=this.teclas[i][j];
                controlesJugadores[i][j]=controlesConfigurados[i][j];
            }
        }
        teclas.modoJuego=this.modoJuego;
        teclas.jugadores=3;
        teclas.jugadoresTotales=3;
        switch(resolucionConfigurada){
            case 0:
                Teclas.resolucion=0;
                tile.TILE_WIDTH=4;
                tile.TILE_HEIGHT=4;
                break;
            case 1:
                Teclas.resolucion=1;
                tile.TILE_WIDTH=8;
                tile.TILE_HEIGHT=8;
                break;
            case 2:
                Teclas.resolucion=2;
                tile.TILE_WIDTH=16;
                tile.TILE_HEIGHT=16;
                break;
        }
        inicio();
        
        //crear un nuevo juego:
        Juego.permiso=false;
        Juego.nivel=0;
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Juego.permiso=true;
        if(Juego.corriendo==true){
            Juego.nivel=1;
        }else{
            Juego.nivel=1;
        }
        Juego.corriendo=false;
        Juego.estaCerrada=true;
    }//GEN-LAST:event_tresJugadoresActionPerformed
    
    private void cuatroJugadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuatroJugadoresActionPerformed
        //traspasar la configuracion de las teclas al juego     
        Tile tile=new Tile();
        Teclas teclas=new Teclas();
        
        for(int i=0;i<4;i++){
            for(int j=0;j<8;j++){
                this.teclas[i][j]=teclasConfiguradas[i][j];
                teclas.teclas[i][j]=this.teclas[i][j];
                controlesJugadores[i][j]=controlesConfigurados[i][j];
            }
        }
        teclas.modoJuego=this.modoJuego;
        teclas.jugadores=4;
        teclas.jugadoresTotales=4;
        switch(resolucionConfigurada){
            case 0:
                Teclas.resolucion=0;
                tile.TILE_WIDTH=4;
                tile.TILE_HEIGHT=4;
                break;
            case 1:
                Teclas.resolucion=1;
                tile.TILE_WIDTH=8;
                tile.TILE_HEIGHT=8;
                break;
            case 2:
                Teclas.resolucion=2;
                tile.TILE_WIDTH=16;
                tile.TILE_HEIGHT=16;
                break;
        }
        
        inicio();
        
        //crear un nuevo juego:
        
        Juego.permiso=false;
        Juego.nivel=0;
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Juego.permiso=true;
        if(Juego.corriendo==true){
            Juego.nivel=1;
        }else{
            Juego.nivel=1;
        }
        
        Juego.corriendo=false;
        Juego.estaCerrada=true;
    }//GEN-LAST:event_cuatroJugadoresActionPerformed

    private void inicio(){
        
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        menu.setVisible(true);
        
    }
    
    private void botonInicio3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInicio3ActionPerformed
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        menu.setVisible(true);
    }//GEN-LAST:event_botonInicio3ActionPerformed

    private void botonInicio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInicio2ActionPerformed
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        menu.setVisible(true);
    }//GEN-LAST:event_botonInicio2ActionPerformed

    private void botonInicio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInicio1ActionPerformed
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        menu.setVisible(true);
    }//GEN-LAST:event_botonInicio1ActionPerformed

    private void botonAceptar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAceptar2ActionPerformed
        
        guardarConfiguracion();
        
        menu.setVisible(false);
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(true);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
    }//GEN-LAST:event_botonAceptar2ActionPerformed

    private void botonCancelar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelar2ActionPerformed
        
        resolucionConfigurada=resolucionCancelada;
        
        menu.setVisible(false);
        controles.setVisible(false);
        presione.setVisible(false);
        opciones.setVisible(true);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
    }//GEN-LAST:event_botonCancelar2ActionPerformed

    private void boxDispositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxDispositivoActionPerformed
        int index=boxDispositivo.getSelectedIndex();
        if(index==0){
            indiceJugadorTeclado=boxJugadores.getSelectedIndex();//de 0 a 3.
            ordenDispositivosReal[boxJugadores.getSelectedIndex()]=index;
            ordenDispositivos[boxJugadores.getSelectedIndex()]=-1;//se maneja con el teclado.
        }else{
            ordenDispositivosReal[boxJugadores.getSelectedIndex()]=index;
            ordenDispositivos[boxJugadores.getSelectedIndex()]=joyIDX[index-1];
//            if(casillaActivarPOV.isSelected()){
//                botonIzquierda.setText("Izquierda  (POV)");
//                botonDerecha.setText("Derecha  (POV)");
//                botonAgacharse.setText("Abajo  (POV)");
//                botonPaleta.setText("Arriba  (POV)");
//            }else{
//                botonIzquierda.setText("Izquierda");
//                botonDerecha.setText("Derecha");
//                botonAgacharse.setText("Abajo");
//                botonPaleta.setText("Arriba");
//            }
            //ya no se pueden mostrar los controles segun dispositivos ya que se restauran tambien al
            // seleccionar otro jugador...(setSelectedIndex activa esta funcion).
        }
        mostrarControlesSegunJugador(boxJugadores.getSelectedIndex());
    }//GEN-LAST:event_boxDispositivoActionPerformed

    private void botonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarActionPerformed
        for(int i=0;i<8;i++){
            controlesConfigurados[boxJugadores.getSelectedIndex()][i]=controlesPorDefecto[boxJugadores.getSelectedIndex()][i];
            boton=i;
            teclasConfiguradas[boxJugadores.getSelectedIndex()][i]=0;
            nombrarBoton("");
        }
    }//GEN-LAST:event_botonBorrarActionPerformed

    private void boxPixelesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxPixelesActionPerformed
        resolucionConfigurada=boxPixeles.getSelectedIndex();
    }//GEN-LAST:event_boxPixelesActionPerformed

    private void botonCorrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCorrerActionPerformed
        menu.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        presione.setVisible(true);
        presione.setFocusable(true);
        controles.setVisible(false);
        if(boxDispositivo.getSelectedIndex()!=0){
            jLabel8.setText("UN BOTON");
        }else{
            jLabel8.setText("UNA TECLA");
        }
        boton=6;
    }//GEN-LAST:event_botonCorrerActionPerformed

    private void botonCambiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCambiarActionPerformed
        menu.setVisible(false);
        opciones.setVisible(false);
        modoDeJuego.setVisible(false);
        jugadores.setVisible(false);
        configuracion.setVisible(false);
        presione.setVisible(true);
        presione.setFocusable(true);
        controles.setVisible(false);
        if(boxDispositivo.getSelectedIndex()!=0){
            jLabel8.setText("UN BOTON");
        }else{
            jLabel8.setText("UNA TECLA");
        }
        boton=7;
    }//GEN-LAST:event_botonCambiarActionPerformed

    private void botonDesconocido1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDesconocido1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonDesconocido1ActionPerformed

    private void botonDesconocido2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDesconocido2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonDesconocido2ActionPerformed

    public void restaurar(int jugador){
        //restaurando de acuerdo al jugador seleccionado
        for(int i=0;i<8;i++){
            teclasConfiguradas[jugador][i]=teclasPorDefecto[jugador][i];
            boton=i;
            nombrarBoton(nombrarTecla(teclasPorDefecto[jugador][i]));
        }
    }
    
    public void restaurarControles(int jugador){
        //restaurando de acuerdo al jugador seleccionado
        for(int i=0;i<8;i++){
            controlesConfigurados[jugador][i]=controlesPorDefecto[jugador][i];
            boton=i;
            nombrarBoton(nombrarControles(controlesConfigurados[jugador][i]));
        }
    }
    
    public String nombrarTecla(int codigo){
        String nombre;
        nombre=KeyEvent.getKeyText(codigo);
        if(codigo==0){
            nombre="";
        }
        return nombre;
    }
    public void nombrarBoton(String nombre){
        
        switch(boton){
            case 0:
                botonIzquierda.setText(nombre);
                break;
            case 1:
                botonDerecha.setText(nombre);
                break;
            case 2:
                botonAgacharse.setText(nombre);
                break;
            case 3:
                botonPaleta.setText(nombre);
                break;
            case 4:
                botonSaltar.setText(nombre);
                break;
            case 5:
                botonDisparar.setText(nombre);
                break;
            case 6:
                botonCorrer.setText(nombre);
                break;
            case 7:
                botonCambiar.setText(nombre);
                break;
        }
    }

    public String nombrarControles(int boton){
        String nombre="";
        switch(boton){
            case 0:
                nombre="BOTON 1";
                break;
            case 1:
                nombre="BOTON 2";
                break;
            case 2:
                nombre="BOTON 3";
                break;
            case 3:
                nombre="BOTON 4";
                break;
            case 4:
                nombre="ARRIBA (EJE)";
                break;
            case 5:
                nombre="ABAJO (EJE)";
                break;
            case 6:
                nombre="IZQUIERDA (EJE)";
                break;
            case 7:                
                nombre="DERECHA (EJE)";
                break;
            case 8:
                nombre="ARRIBA (POV)";
                break;
            case 9:
                nombre="ABAJO (POV)";
                break;
            case 10:
                nombre="IZQUIERDA (POV)";
                break;
            case 11:
                nombre="DERECHA (POV)";
                break;
        }
        return nombre;
    }
    public void mostrarControlesSegunJugador(int jugador){
        for(int i=0;i<8;i++){
            boton=i;
            if(ordenDispositivosReal[jugador]==0){
                nombrarBoton(nombrarTecla(teclasConfiguradas[jugador][i]));
            }else{
                nombrarBoton(nombrarControles(controlesConfigurados[jugador][i]));
            }
            
        }
    }

    public void obtenerConfiguracion(){
        //System.out.println("new configuration!!");
        //String name="Archivos/blockman_config.txt";
        //String name = "C:/blockman/blockman_config.txt";
        String name="blockman_config.txt";
//        URL url;
//        url=this.getClass().getClassLoader().getResource(name);
//        if (url == null) {
//            System.out.println("Can't find ref: "+name);
//        }
        try{
            name = Menu.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if(name.substring(name.length()-12,name.length()).equals("BlockMan.jar")){
                name=name.substring(0,name.length()-12)+"blockman_config.txt";
            }else{
                name = "C:/blockman/blockman_config.txt";
            }
//            String path=url.toString();
//            path=path.substring(6,path.length());
//            File arch=new File(path);
            FileReader fr=new FileReader(name);
            BufferedReader buf=new BufferedReader(fr);
            //BufferedReader buf=new BufferedReader(new InputStreamReader(MapaTiles.class.getClassLoader().getResourceAsStream(name)));
            StringTokenizer stk;
            String jugador="";
            boolean controles=false;
            String resolucion="";
            while(buf.ready()){
                String reglon=buf.readLine();
                stk=new StringTokenizer(reglon);
                while(stk.hasMoreTokens()){
                    String palabra = stk.nextToken();
                    int tecla=-1;
                    String valor="";
                    if(palabra.length()>=11 && palabra.substring(0,11).equals("Resolucion=")){
                        resolucion=palabra.substring(11,palabra.length());
                    }
                    if(palabra.length()>=7 && palabra.substring(0,7).equals("Teclas:")){
                        controles=false;
                    }
                    if(palabra.length()>=10 && palabra.substring(0,10).equals("Controles:")){
                        controles=true;
                    }
                    if(palabra.length()>=7 && palabra.substring(0,7).equals("Jugador")){
                        jugador=(palabra.substring(7,palabra.length()));
                    }
                    //con el igual...
                    if(palabra.length()>=10 && palabra.substring(0,10).equals("izquierda=")){
                        tecla=0;
                        valor=(palabra.substring(10,palabra.length()));
                    }
                    if(palabra.length()>=8 && palabra.substring(0,8).equals("derecha=")){
                        tecla=1;
                        valor=(palabra.substring(8,palabra.length()));
                    }
                    if(palabra.length()>=6 && palabra.substring(0,6).equals("abajo=")){
                        tecla=2;
                        valor=(palabra.substring(6,palabra.length()));
                    }
                    if(palabra.length()>=7 && palabra.substring(0,7).equals("arriba=")){
                        tecla=3;
                        valor=(palabra.substring(7,palabra.length()));
                    }
                    if(palabra.length()>=7 && palabra.substring(0,7).equals("saltar=")){
                        tecla=4;
                        valor=(palabra.substring(7,palabra.length()));
                    }
                    if(palabra.length()>=9 && palabra.substring(0,9).equals("disparar=")){
                        tecla=5;
                        valor=(palabra.substring(9,palabra.length()));
                    }
                    if(palabra.length()>=7 && palabra.substring(0,7).equals("correr=")){
                        tecla=6;
                        valor=(palabra.substring(7,palabra.length()));
                    }
                    if(palabra.length()>=8 && palabra.substring(0,8).equals("cambiar=")){
                        tecla=7;
                        valor=(palabra.substring(8,palabra.length()));
                    }
                    int resolucion2=2;
                    if(resolucion.equals("")){
                        resolucion2=2;
                        resolucionConfigurada=2;
                    }else{
                        resolucion2=Integer.parseInt(resolucion);
                        resolucionConfigurada=resolucion2;
                    }
                    
                    int valor2=0;
                    if(valor.equals("")){
                        valor2=0;
                    }else{
                        valor2=Integer.parseInt(valor);
                    }
                    
                    int jugador2=-1;
                    if(jugador.equals("")){
                        jugador2=-1;
                    }else{
                        jugador2=Integer.parseInt(jugador)-1;
                    }
                    if(jugador2!=-1 && tecla!=-1){
                        if(controles==true){
                            controlesConfigurados[jugador2][tecla]=valor2;
                        }else{
                            teclasConfiguradas[jugador2][tecla]=valor2;
                        }
                    }
                }
            }
            buf.close();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Could not load cfg file: "+name);
        }
    }
    public void guardarConfiguracion(){
        //String name = ClassLoader.getSystemClassLoader().getResource(".").getPath()+"blockman_config.txt";
        String name="blockman_config.txt";
        try{
            name = Menu.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if(name.substring(name.length()-12,name.length()).equals("BlockMan.jar")){
                name=name.substring(0,name.length()-12)+"blockman_config.txt";
            }else{
                name = "C:/blockman/blockman_config.txt";
            }
            //String name="Archivos/blockman config.txt";
            FileWriter w = new FileWriter(name);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);
            wr.write("");//escribimos en el archivo.
            wr.append("Teclas:"+"\r\n");
            wr.append("\r\n");
            for(int i=0;i<4;i++){//agregamos al archivo:
                wr.append("Jugador"+(i+1)+"\r\n");
                wr.append("izquierda="+teclasConfiguradas[i][0]+"\r\n");
                wr.append("derecha="+teclasConfiguradas[i][1]+"\r\n");
                wr.append("abajo="+teclasConfiguradas[i][2]+"\r\n");
                wr.append("arriba="+teclasConfiguradas[i][3]+"\r\n");
                wr.append("saltar="+teclasConfiguradas[i][4]+"\r\n");
                wr.append("disparar="+teclasConfiguradas[i][5]+"\r\n");
                wr.append("correr="+teclasConfiguradas[i][6]+"\r\n");
                wr.append("cambiar="+teclasConfiguradas[i][7]+"\r\n");
                wr.append("\r\n");
            }
            
            wr.append("Controles:"+"\r\n");
            wr.append("\r\n");
            for(int i=0;i<4;i++){//agregamos al archivo:
                wr.append("Jugador"+(i+1)+"\r\n");
                wr.append("izquierda="+controlesConfigurados[i][0]+"\r\n");
                wr.append("derecha="+controlesConfigurados[i][1]+"\r\n");
                wr.append("abajo="+controlesConfigurados[i][2]+"\r\n");
                wr.append("arriba="+controlesConfigurados[i][3]+"\r\n");
                wr.append("saltar="+controlesConfigurados[i][4]+"\r\n");
                wr.append("disparar="+controlesConfigurados[i][5]+"\r\n");
                wr.append("correr="+controlesConfigurados[i][6]+"\r\n");
                wr.append("cambiar="+controlesConfigurados[i][7]+"\r\n");
                wr.append("\r\n");
            }
            
            wr.append("Resolucion="+resolucionConfigurada);
            
            wr.close();
            bw.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Could not load cfg file: "+name);
        }
    }
    
//--------------------------------------------------------------- 
// Is Available 
//--------------------------------------------------------------- 
public boolean isAvailable (int index)
{
return (this.pJoy[index] != null);
}

//--------------------------------------------------------------- 
// getAxis (UP, DOWN, LEFT, RIGHT) 
//--------------------------------------------------------------- 
public boolean getAxis (int index, int nDir)
{
return isAvailable(index) && this.botones[index][nDir+4];
}

//--------------------------------------------------------------- 
// getAxisAlt (UP, DOWN, LEFT, RIGHT) resets the direction 
//--------------------------------------------------------------- 
public boolean getAxisAlt (int index, int nDir) 
{
boolean bRet = isAvailable(index) && this.botones[index][nDir+4];
if (bRet)
this.botones[index][nDir+4] = false;
return bRet;
}


//--------------------------------------------------------------- 
// getButton (BUTTON1..4) 
//--------------------------------------------------------------- 
public boolean getButton (int index, int nButton)
{
return isAvailable(index) && this.botones[index][nButton];
}

//--------------------------------------------------------------- 
// getButtonAlt (BUTTON1..4) resets the button 
//--------------------------------------------------------------- 
public boolean getButtonAlt (int index,int nButton)
{
boolean bRet = isAvailable(index) && this.botones[index][nButton];
if (bRet)
this.botones[index][nButton] = false;
return bRet;
}

//--------------------------------------------------------------- 
// Event: joystickAxisChanged 
//--------------------------------------------------------------- 
public void joystickAxisChanged(Joystick pJoy)
{
    if(presione.isVisible()==true && boxDispositivo.getSelectedIndex()!=0){
        int index=-1;
        if (this.pJoy[0] == pJoy){
            index=0;
        }
        if (this.pJoy[1] == pJoy){
            index=1;
        }
        if (this.pJoy[2] == pJoy){
            index=2;
        }
        if (this.pJoy[3] == pJoy){
            index=3;
        }
        if(boxDispositivo.getSelectedIndex()==(index+1)){//+1 por el teclado que esta al principio.
            if(index!=-1){
                boolean captura=false;
                if(this.pJoy[index].getPOV()== 0.0){
                    controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=8;
                    String nombre=("ARRIBA (POV)");
                    nombrarBoton(nombre);
                    captura=true;
                }
                if(this.pJoy[index].getPOV()== 180.0){
                    controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=9;
                    String nombre=("ABAJO (POV)");
                    nombrarBoton(nombre);
                    captura=true;
                }
                if(this.pJoy[index].getPOV()== 270.0){
                    controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=10;
                    String nombre=("IZQUIERDA (POV)");
                    nombrarBoton(nombre);
                    captura=true;
                }
                if(this.pJoy[index].getPOV()== 90.0){
                    controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=11;
                    String nombre=("DERECHA (POV)");
                    nombrarBoton(nombre);
                    captura=true;
                }
                
                if(this.pJoy[index].getY()==-1.0){
                    controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=4;
                    String nombre=("ARRIBA (EJE)");
                    nombrarBoton(nombre);
                    captura=true;
                }
                if(this.pJoy[index].getY()==1.0){
                    controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=5;
                    String nombre=("ABAJO (EJE)");
                    nombrarBoton(nombre);
                    captura=true;
                }
                if(this.pJoy[index].getX()==1.0){
                    controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=7;
                    String nombre=("DERECHA (EJE)");
                    nombrarBoton(nombre);
                    captura=true;
                }
                if(this.pJoy[index].getX()==-1.0){
                    controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=6;
                    String nombre=("IZQUIERDA (EJE)");
                    nombrarBoton(nombre);
                    captura=true;
                }
                if(captura==true){
                    menu.setVisible(false);
                    controles.setVisible(true);
                    presione.setVisible(false);
                    opciones.setVisible(false);
                    modoDeJuego.setVisible(false);
                    jugadores.setVisible(false);
                    configuracion.setVisible(false);
                }
            }
        }
    }
} 

public void joystickButtonChanged(Joystick pJoy) 
{ 
    if(presione.isVisible()==true && boxDispositivo.getSelectedIndex()!=0){
        int index=-1;
        if (this.pJoy[0] == pJoy){
            index=0;
        }
        if (this.pJoy[1] == pJoy){
            index=1;
        }
        if (this.pJoy[2] == pJoy){
            index=2;
        }
        if (this.pJoy[3] == pJoy){
            index=3;
        }
        if(boxDispositivo.getSelectedIndex()==(index+1)){//+1 por el teclado que esta al principio.
            if (index!=-1){
                if((this.pJoy[index].getButtons() & Joystick.BUTTON1) != 0)controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=0; 
                if((this.pJoy[index].getButtons() & Joystick.BUTTON2) != 0)controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=1;
                if((this.pJoy[index].getButtons() & Joystick.BUTTON3) != 0)controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=2;
                if((this.pJoy[index].getButtons() & Joystick.BUTTON4) != 0)controlesConfigurados[boxJugadores.getSelectedIndex()][boton]=3;
                if(controlesConfigurados[boxJugadores.getSelectedIndex()][boton]<4){//si esta dentro de los botones disponibles.
                    String nombre=("BOTON "+(controlesConfigurados[boxJugadores.getSelectedIndex()][boton]+1));
                    nombrarBoton(nombre);
                }
                menu.setVisible(false);
                controles.setVisible(true);
                presione.setVisible(false);
                opciones.setVisible(false);
                modoDeJuego.setVisible(false);
                jugadores.setVisible(false);
                configuracion.setVisible(false);
            }
        }
    }
}
    
    
    
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAceptar;
    private javax.swing.JButton botonAceptar2;
    private javax.swing.JButton botonAgacharse;
    private javax.swing.JButton botonBatalla;
    private javax.swing.JButton botonBorrar;
    private javax.swing.JButton botonCambiar;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonCancelar2;
    private javax.swing.JButton botonConfiguracion;
    private javax.swing.JButton botonControles;
    private javax.swing.JButton botonCorrer;
    private javax.swing.JButton botonDerecha;
    private javax.swing.JButton botonDesconocido1;
    private javax.swing.JButton botonDesconocido2;
    private javax.swing.JButton botonDisparar;
    private javax.swing.JButton botonInicio1;
    private javax.swing.JButton botonInicio2;
    private javax.swing.JButton botonInicio3;
    private javax.swing.JButton botonIzquierda;
    private javax.swing.JButton botonJugar;
    private javax.swing.JButton botonNormal;
    private javax.swing.JButton botonOpciones;
    private javax.swing.JButton botonPaleta;
    private javax.swing.JButton botonRestaurar;
    private javax.swing.JButton botonSaltar;
    private javax.swing.JComboBox boxDispositivo;
    private javax.swing.JComboBox boxJugadores;
    private javax.swing.JComboBox boxPixeles;
    private javax.swing.JPanel configuracion;
    private javax.swing.JPanel controles;
    private javax.swing.JButton cuatroJugadores;
    private javax.swing.JButton dosJugadores;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jugadores;
    private javax.swing.JPanel menu;
    private javax.swing.JPanel modoDeJuego;
    private javax.swing.JPanel opciones;
    private javax.swing.JPanel presione;
    private javax.swing.JSlider sliderSonido;
    private javax.swing.JButton tresJugadores;
    private javax.swing.JButton unJugadores;
    // End of variables declaration//GEN-END:variables
    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
            teclasConfiguradas[boxJugadores.getSelectedIndex()][boton]=0;
            presione.setVisible(false);
            controles.setVisible(true);
            String nombre=nombrarTecla(teclasConfiguradas[boxJugadores.getSelectedIndex()][boton]);
            nombrarBoton(nombre);
        }else{
            if(boxDispositivo.getSelectedIndex()==0){
                presione.setVisible(false);
                controles.setVisible(true);
                teclasConfiguradas[boxJugadores.getSelectedIndex()][boton]=e.getKeyCode();
                String nombre=nombrarTecla(teclasConfiguradas[boxJugadores.getSelectedIndex()][boton]);
                nombrarBoton(nombre);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Menu().setVisible(true);
//            }
//        });
//    }

    
}
