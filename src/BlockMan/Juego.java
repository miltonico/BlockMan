
package BlockMan;

import com.centralnexus.input.Joystick;
import com.centralnexus.input.JoystickListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class Juego extends Canvas implements KeyListener,JoystickListener {
	/**
	 * hecho por miltiton
	 */
    
    
        public static Juego juego=null;
        
        public static boolean primeraVez=true;
        
        public static ArrayList<SuperJugador> superJugador=new ArrayList<>();
        public static ArrayList<WeaponAxe> weaponAxe=new ArrayList<>();
        public static ArrayList<WeaponBullet> weaponBullet=new ArrayList<>();
        
        ArrayList<Enemigo1> enemigos=new ArrayList<>();
        public static ArrayList<SuperEnemigo> superEnemigo=new ArrayList<>();
        public static ArrayList<Pelota> pelota=new ArrayList<>();
        
        public static boolean permiso=true;
        public static boolean cargoPosiciones=false;
        public static boolean estaCerrada=false;
	public static boolean yaTermino=false;
	public static boolean espera=false;
        
        
        //variables de control:
        public static boolean nivelCamuflaje=false;
	public static boolean computadora=false;
        public static boolean camaraOficial=false;
        public static boolean camaraGrande=false;
        public static boolean friendlyFireOficial=true;
        public static boolean graficos=false;
        public static boolean megaman=true;
        public static boolean renderizadoInteligente=true;
        public static boolean renderizadoCamara=true;
        public static boolean tetrisSkin=false;
        
        public static double porcentajeGenerador=0.1;//0.1 = una posibilidad de 1000 de generar un item cada 1 frame.
        
        //variables del loop y dibujo:
        public static int retraso=30;//miliseconds
        public static int contadorDibuja=0;//contador de ciclos del juego.
        public static int divisorDibuja=1;//cada cuantos ciclos se dibuja.
        
        //variables de conexion:
        public static boolean server=false;
        public static boolean client=false;
        public static boolean client_connection_down=true;//falla en la conexion luego de 10 intentos.
        public static String udpData="";//enviado
        public static String s="";//recibido
        public static int contadorCon=0;//recibido
        public static boolean termino_conexion=false;
        public static int ancho=0;//ancho
        public static int alto=0;//alto
        
        public static int anchoTile;
        public static int altoTile;
        
        
        public static boolean camara;
        public static boolean friendlyFire;
        
	public static Menu menu;
        
        BufferedImage pantalla=null;
        
        public static BufferedImage mapaCompleto=null;
        
	private static JFrame ventana=null;
        
        
        
        
        
	//private Sprite sprite;
	
	//reemplazando:
	//private PersonajeJugable otroSprite=new PersonajeJugable();
	
	private Personaje sprite=new Personaje();
	
	long tiempo=System.currentTimeMillis();
        long tiempo2=System.currentTimeMillis();
	MapaTiles mapa;
	
	//numero de niveles (documentos de texto):
	int niveles=10;
        int contPelotas=0;
        int contBloques=0;
        int contGravedadItems=0;
	private int contador=0;
	public boolean seDibuja=true;
	public static boolean corriendo=false;
        public static boolean perdieron=false;
        public static boolean dibujarMapa=true;
        
	public static int nivel=1,contadorFrames=0;//nivel de partida.
        
        
        public static String modo="A";
	Personaje personaje=new Personaje();
        JoystickControl joy;
	
	
	
	Bot bot=new Bot();
        
	Enemigo enemigo=new Enemigo();
        
        Camuflaje cam=new Camuflaje();
        
        /*
	Jugador1 j1;
        
	Jugador2 j2;
	
        Jugador3 j3;
        
        Jugador4 j4;
        */
	Balas balas=new Balas();
        
        Camara camera=new Camara();
	
        Actualizaciones act=new Actualizaciones();
        
	public Juego(){
            
            System.out.println("Juego iniciado...");
            
            ventana=new JFrame();
            
            pantalla=null;
            
            ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                ventana.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    Object[] options = {"Si", "No!"};
                    int n = JOptionPane.showOptionDialog(ventana,
                                    "¿Desea Cerrar El Juego?",
                                    "CONFIRMAR",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]);
                    if (n == JOptionPane.YES_OPTION) {
                        //crear un nuevo juego:
                        Juego.permiso=false;
                        Juego.nivel=niveles+1;
                        System.exit(0);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Juego.permiso=true;
                        if(Juego.corriendo==true){
                            reiniciarVariables();
                        }else{
                            reiniciarVariables();
                        }
                        //this.dispose();
                    } else if (n == JOptionPane.NO_OPTION) {
                        //nada...
                    }
                }
                });
            /*
            if(server==true && client==false){
                DatagramSocket serverSocket = null;
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];
                udp_server udps=new udp_server();
                (new Thread(udps)).start();
            }//el cliente esta en el loop del juego por si pierde la conexion...
	    */
            menu.obtenerConfiguracion();
                
                if(joy==null){
                    /*
                    j1=new Jugador1();
                    j2=new Jugador2();
                    j3=new Jugador3();
                    j4=new Jugador4();
                    */

                    friendlyFire=friendlyFireOficial;
                    camara=camaraOficial;
                    
                    
                    act.iniciar();
                    personaje.iniciar();
                }
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //config ini:
                this.ancho=0;
                this.alto=0;
                
                if(this.ancho==0 || this.alto==0){
                    if(Teclas.modoJuego==1){
                        this.ancho=160;//40;//160;
                        this.alto=160;//40;//160;
                        if(Teclas.resolucion!=0){
                            camara=true;//false;//true;
                        }
                    }else{
                        this.ancho=40;
                        this.alto=40;
                        //si la camara es true entonces no se cambia, se deja tal cual...
                    }
                }

            
            
            anchoTile=Tile.TILE_WIDTH;
            altoTile=Tile.TILE_HEIGHT;
            
            if(camara==true){
                if(camaraGrande){
                    ventana.setSize((anchoTile*(Camara.anchoCamara+6)+10),(altoTile*(Camara.altoCamara))+28);
                }else{
                    ventana.setSize((anchoTile*(Camara.anchoCamara*2+9)+10),(altoTile*(Camara.altoCamara*2+1))+28);
                }
            }else{
                ventana.setSize(anchoTile*(ancho+6)+10,altoTile*(alto)+28);
            }
	    ventana.add(this);
	    ventana.setVisible(true);
	    ventana.setResizable(false);
	    this.setBackground(new Color(0,0,0));
            if(nivel==1){
                ventana.setLocationRelativeTo(null);
            }
	    
            
            //ventana.addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e){  System.exit(0); } });
            
	    // ATENCION ATENCION *************************************************
	    //(this.requestFocus();)
	    this.requestFocus(); // Focalizamos hacia nuestro objeto.
	    //reemplazando:
	    //this.addKeyListener(otroSprite); // Direccionamos la captura a nuestro Pj
            if(primeraVez==true){
                primeraVez=false;
                for(int i=0;i<Teclas.jugadores;i++){
                    superJugador.add(new SuperJugador(personaje.posIniX[i],personaje.posIniY[i],i));
                    if(Menu.ordenDispositivos[i]==-1){
                        this.addKeyListener(superJugador.get(i));
                    }
                }
            }
	    // *******************************************************************
            if(joy==null){
                joy=new JoystickControl();
                joy.actualizarControles();
            }

	    while(nivel<=niveles){
                //System.out.println("hola1");
	    /*
	    //reemplazar si es bot.
	    if(computadora==false && Menu.ordenDispositivos[1]==-1){
                this.addKeyListener(j2); // Direccionamos la captura a nuestro Pj
	    }
	    
            if(Menu.ordenDispositivos[2]==-1){ 
                this.addKeyListener(j3);
            }
            
            if(Menu.ordenDispositivos[3]==-1){
                this.addKeyListener(j4);
            }
            */
            if(Teclas.modoJuego==1){
                this.modo="A";
                //this.friendlyFire=false;
            }else{
                this.modo="B";
                this.friendlyFire=true;
            }
            //fin config ini
                
                
	    	corriendo=true;
                act.iniciarNivel();//restaura variables que se deben restaurar al iniciar otro nivel.
                
                //se resetean los arraylist.
                MapaTiles.items.clear();
                MapaTiles.punto.clear();
                MapaTiles.generadores.clear();
                balas.borrarBalas();
                
                Personaje.vidas[0]=act.vidaJugador[0];
		Personaje.vidas[1]=act.vidaJugador[1];
                Personaje.vidas[2]=act.vidaJugador[2];
		Personaje.vidas[3]=act.vidaJugador[3];
                
                if(this.modo=="A"){
                    Personaje.puntos[0]=act.vidasJugadorA;
                    Personaje.puntos[1]=act.vidasJugadorA;
                    Personaje.puntos[2]=act.vidasJugadorA;
                    Personaje.puntos[3]=act.vidasJugadorA;
                }else{
                    Personaje.puntos[0]=act.vidasJugadorB;
                    Personaje.puntos[1]=act.vidasJugadorB;
                    Personaje.puntos[2]=act.vidasJugadorB;
                    Personaje.puntos[3]=act.vidasJugadorB;
                }
		
                
                Jugador1.direccion=2;
                Jugador2.direccion=2;
                Jugador3.direccion=1;
                Jugador4.direccion=1;
                this.pelota.clear();
                this.superEnemigo.clear();
                if((nivelCamuflaje==true || nivel>=5) && Teclas.modoJuego==2){
                    mapa = MapaTiles.DesdeArchivo("Mapas/map40_B5.txt");
                    cam.iniciarMapa();
                    cam.contadorInicio=0;
                    cam.contadorMover=0;
                    cam.movimientoActual=0;
                }else{
                    //mapa = MapaTiles.DesdeArchivo("Mapas/map160_A3.txt");
                    mapa = MapaTiles.DesdeArchivo("Mapas/map"+Integer.toString(ancho)+"_"+modo+Integer.toString(nivel)+".txt");//este es el primero para los bots.3
                    
                    //mapa = MapaTiles.DesdeArchivo("Mapas/map40_Cancha.txt");
                }
	    	if(mapa==null){
                    System.exit(0);
                    permiso=false;
                    nivel=niveles+1;
                }
		//mapa = MapaTiles.DesdeArchivo("C:/mapa_prueba.txt");
                //sprite.setSprite("cara.png");
		    //sprite.setY(this.getHeight()/2);
	    	
		    //if(mapa.map.length==20){
		    //	otroSprite.setSprite("C:/Users/milton/workspace/TetrisMan/tile.png");
		    //}else{
		    //	otroSprite.setSprite("C:/Users/milton/workspace/TetrisMan/miniTile.png");
		    //}
	    	
	    	//reemplazando:
	    	//otroSprite.setSprite("C:/tile.png");
		    //otroSprite.setX(1);
		    //otroSprite.setY(1);
	    	
	    	//personaje.setX(0,1);
	    	//personaje.setY(0,1);
	    	for(int i=0;i<enemigo.cantidadEnemigos;i++){
                    enemigos.add(new Enemigo1());
                }
                /*
                for(int p=0;p<10;p++){
                    pelota.add(new Pelota(5+p,5,0));
                }
                */
	    	if(Teclas.modoJuego==2){
                    //personaje.setX(0,1);
                    //personaje.setY(0,1);
                }
                for(int p=0;p<superJugador.size();p++){
                    superJugador.get(p).inicioX=Personaje.posIniX[p];
                    superJugador.get(p).inicioY=Personaje.posIniY[p];
                    superJugador.get(p).reaparecer();
                }
                /*//cambio de superjugador
                j1.reaparecer();
                
	    	if(Teclas.jugadores>=2){
                    if(Teclas.modoJuego==2){
                        //personaje.setX(1,tamaño-4);
                        //personaje.setY(1,1);
                    }
                    if(computadora==false){
                        j2.reaparecer();
                    }else{
                        bot.reaparecer();
                    }
                    
                }
                if(Teclas.jugadores>=3){
                    j3.reaparecer();
                }
                if(Teclas.jugadores>=4){
                    j4.reaparecer();
                }
                */
                reaparecerEnemigos();
                
		
                enemigo.iniciar();
                boolean yaEntro=false;
		    while(corriendo==true && permiso==true) // BUCLE INFINITO DEL JUEGO
		    {
                        
                        //System.out.println("hola2");
//		    	if (System.currentTimeMillis()-tiempo>60 && yaEntro==false) {
//                            yaEntro=true;
//                            dibuja(this.getGraphics());
//                        }
                        if (System.currentTimeMillis()-tiempo>retraso) { // actualizamos cada 30 milisegundos.
                            if(client==true){
                                if(client_connection_down==true){
                                    client_connection_down=false;
                                    udp_client udpc=new udp_client();
                                    (new Thread(udpc)).start();
                                }
                            }
                            
                            tiempo=System.currentTimeMillis();
                            if(this.perdieron==true){
                                this.perdieron=false;
                                reaparecerEnemigos();
                                revivirEnemigos();
                                act.iniciarNivel();
                            }

                            if(cargoPosiciones){
                                for(int i=0;i<Teclas.jugadores;i++){
                                    /*
                                    mapa.borrarJugador2(i);
                                    if(i==0){
                                        j1.actualiza2();
                                    }
                                    if(i==1){
                                        if(computadora==false){
                                            j2.actualiza2();
                                        }else{
                                            bot.actualiza();
                                        }
                                    }
                                    if(i==2){
                                        j3.actualiza2();
                                    }
                                    if(i==3){
                                        j4.actualiza2();
                                    }
                                    if(server==true){
                                        udpData=Integer.toString(personaje.getX(0))+";"+Integer.toString(personaje.getY(0))+";"+Integer.toString(personaje.getAccion(0));

                                        String[] datosJ2 = s.split(";");
                                        if(datosJ2.length==3){
                                            personaje.setX(1,Integer.parseInt(datosJ2[0]));
                                            personaje.setY(1,Integer.parseInt(datosJ2[1]));
                                            personaje.animar(1,Integer.parseInt(datosJ2[2]));
                                        }
                                    }else if(client==true){
                                        String[] datosJ1 = s.split(";");
                                        if(datosJ1.length==3){
                                            personaje.setX(0,Integer.parseInt(datosJ1[0]));
                                            personaje.setY(0,Integer.parseInt(datosJ1[1]));
                                            personaje.animar(0,Integer.parseInt(datosJ1[2]));
                                            udpData=Integer.toString(personaje.getX(1))+";"+Integer.toString(personaje.getY(1))+";"+Integer.toString(personaje.getAccion(1));
                                        }
                                    }
                                    mapa.DibujaJugador(i);
                                    */
                                    /*
                                    if(camara==true){
                                        camera.moverCamara(i);//actualiza la posicion de la camara de cada jugador;
                                    }
                                    */
                                }
                                if(camara==true){
                                    camera.moverSuperCamara();//actualiza la posicion de la camara de cada jugador;
                                }
                                for(int i=0;i<enemigo.cantidadEnemigos;i++){
                                    mapa.borrarEnemigo(i);
                                    enemigos.get(i).actualiza(i);
                                    mapa.DibujaEnemigo(i);
                                }
                                
                                Enemigo1.contadorFrames++;
                                if(Enemigo1.contadorFrames==4){//numero de frames por actualizacion.
                                    Enemigo1.contadorFrames=0;
                                }
                                
                                for(int i=0;i<this.superEnemigo.size();i++){
                                    this.superEnemigo.get(i).actualizar();
                                }
                                
                                if(corriendo==false && Teclas.modoJuego==2){
                                    corriendo=true;
                                    tiempo2=System.currentTimeMillis();
                                    espera=true;
                                }
                                if(espera==true && System.currentTimeMillis()-tiempo2>4000){
                                    espera=false;
                                    corriendo=false;
                                    /*
                                    if(Teclas.jugadores>=1){
                                        personaje.estaVivo[0]=true;
                                    }
                                    if(Teclas.jugadores>=2){
                                        personaje.estaVivo[1]=true;
                                    }
                                    if(Teclas.jugadores>=3){
                                        personaje.estaVivo[2]=true;
                                    }
                                    if(Teclas.jugadores>=4){
                                        personaje.estaVivo[3]=true;
                                    }
                                    */
                                }

                            }

                            if(nivelCamuflaje==true || nivel>=5){
                                cam.moverMapa();
                            }
                            contadorDibuja++;
                            if(contadorDibuja>=divisorDibuja){
                                contadorDibuja=0;
                                dibuja(this.getGraphics());
                            }
                            //yaEntro=false;//se reinicia para las balas.
                            
                            //poner aqui las balas rapidas:
                            for(int p=0;p<pelota.size();p++){
                                pelota.get(p).actualizarPelota();
                            }
                            boolean revivir=true;
                            int vivos=0;
                            for(int j=0;j<superJugador.size();j++){
                                superJugador.get(j).actualizar();
                                if(superJugador.get(j).alive){
                                    vivos++;
                                    revivir=false;
                                }
                            }
                            if(revivir){
                                this.corriendo=false;
                                this.nivel--;//se resetea el nivel...
                                /*
                                for(int j=0;j<superJugador.size();j++){
                                    superJugador.get(j).reaparecer();
                                }
                                */
                            }
                            
                            if(vivos<=1 && Teclas.modoJuego==2){
                                this.corriendo=false;
                            }
                            
                            for(int j=0;j<weaponAxe.size();j++){
                                if(weaponAxe.get(j).broken){
                                    weaponAxe.remove(j);
                                    j--;//se reduce el indice:
                                }else{
                                    weaponAxe.get(j).updateAxe();
                                }
                            }
                            
                            for(int j=0;j<weaponBullet.size();j++){
                                if(weaponBullet.get(j).broken){
                                    weaponBullet.remove(j);
                                    j--;//se reduce el indice:
                                }else{
                                    weaponBullet.get(j).updateBullet();
                                }
                            }
                            for(int j=0;j<MapaTiles.items.size();j++){
                                if(MapaTiles.items.get(j).tomado){
                                    MapaTiles.items.remove(j);
                                    j--;//se reduce el indice:
                                }else{
                                    MapaTiles.items.get(j).actualizar();
                                }
                            }
                            for(int j=0;j<MapaTiles.punto.size();j++){
                                MapaTiles.punto.get(j).updateCheckPoint();
                            }
                            /*
                            if(contPelotas>=2){
                                //poner aqui las balas lentas:
                                balas.actualizar();// mueve las balas.
                                balas.actualizarBalasEnemigas();
                                contPelotas=0;
                                balas.actualizarPelotas();// mueve las pelotas XD.
                            }
                            if(contBloques>=4){//balas maaaas lentas.
                                balas.actualizarBloques();
                                mapa.borrarBloques();
                                mapa.dibujarBloques();
                                balas.actualizarJugadoresBloques();
                                contBloques=0;
                            }
                            contBloques++;
                            contPelotas++;
                            if(contGravedadItems>=4){
                                contGravedadItems=0;
                                mapa.gravedadItems();
                            }
                            contGravedadItems++;
                            mapa.generarItems(porcentajeGenerador);
                            */
                        }
                    }
                    //para que no se dibujen los jugadores hasta que se carge el mapa y se especifique la posicion de cada 1:
                    cargoPosiciones=false;
                    nivel++;
                    for(int j=0;j<superJugador.size();j++){
                        superJugador.get(j).reaparecer();
                    }
                    superEnemigo.clear();
                    weaponAxe.clear();
                    mapaCompleto=null;
	    }
            termino_conexion=true;
            this.reiniciarVariables();
            //ventana.setVisible(false);
            ventana.dispose();
	}
        
        public void matarEnemigos(){
            for(int i=0;i<enemigo.cantidadEnemigos;i++){
                enemigo.vidaEnemigo[i]=0;
                enemigo.estaVivo[i]=false;
            }
        }
        
        public void reaparecerEnemigos(){
            for(int i=0;i<enemigo.cantidadEnemigos;i++){
                mapa.borrarEnemigo(i);
                enemigos.get(i).reaparecer(i);
            }
        }
        public void revivirEnemigos(){
            for(int i=0;i<enemigo.cantidadEnemigos;i++){
                enemigo.estaVivo[i]=true;
                enemigo.vidaEnemigo[i]=enemigo.limiteVidaEnemigo;
            }
        }
	public static void actualizarNivel(int n){
		nivel=n;
	}
	public static void actualizarCorriendo(boolean estado){
		corriendo=estado;
	}
	public void dibuja(Graphics grafico)
	{
//            if(camara==true){
//                pantalla=new BufferedImage((anchoTile*(Camara.anchoCamara*2+9)),((altoTile*(Camara.altoCamara*2+1))),BufferedImage.TYPE_INT_RGB);
//            }else{
//                if(renderizadoInteligente==true){
//                    if(pantalla==null){
//                        pantalla=new BufferedImage(anchoTile*(ancho+6),altoTile*(alto), BufferedImage.TYPE_INT_RGB );
//                    }
//                }else{
//                    pantalla=new BufferedImage(anchoTile*(ancho+6),altoTile*(alto), BufferedImage.TYPE_INT_RGB );
//                }
//            }
            if(pantalla==null){
                if(camara==true){
                    if(camaraGrande){
                        pantalla=new BufferedImage((anchoTile*(Camara.anchoCamara+6)),((altoTile*(Camara.altoCamara))),BufferedImage.TYPE_INT_RGB);
                    }else{
                        pantalla=new BufferedImage((anchoTile*(Camara.anchoCamara*2+9)),((altoTile*(Camara.altoCamara*2+1))),BufferedImage.TYPE_INT_RGB);
                    }
                    //System.out.println("nuevaPantalla="+pantalla.getWidth()+","+pantalla.getHeight());
                }else{
                    pantalla=new BufferedImage(anchoTile*(ancho+6),altoTile*(alto), BufferedImage.TYPE_INT_RGB );
                }
            }
            if(mapaCompleto==null && (camara==false || renderizadoCamara)){
                mapaCompleto=new BufferedImage(anchoTile*(ancho+6),altoTile*(alto), BufferedImage.TYPE_INT_RGB );
                mapa.DibujarMapaCompleto(mapaCompleto.getGraphics());
            }else{
                if(Teclas.modoJuego==2 && (Juego.nivelCamuflaje==true || Juego.nivel>=5)){
                    mapa.DibujarMapaCompleto(mapaCompleto.getGraphics());
                }
            }
	    
	    //Graphics2D g = pantalla.createGraphics();
	    //g.setColor(new Color(140,150,130));
            //g.fillRect(0, 0, pantalla.getWidth(), pantalla.getHeight());
            //  g.dispose();
	    
            if(camara==true){
                if(renderizadoCamara==true){
                    mapa.DibujarSuperCamaraInteligente(pantalla.getGraphics());
                    mapa.DibujarElementos(pantalla.getGraphics());//jugadores dinamicos y armas dinamicas.
                    mapa.DibujarBarroCamara(pantalla.getGraphics());
                    mapa.DibujarSuperVida(pantalla.getGraphics());
                }else{
                    mapa.DibujarSuperCamara(pantalla.getGraphics());
                    //mapa.DibujarSuperEnemigosCamara(pantalla.getGraphics());
                    //mapa.DibujarPelotasCamara(pantalla.getGraphics());
                    //mapa.DibujarBalasCamara(pantalla.getGraphics());
                    //mapa.DibujarItemsCamara(pantalla.getGraphics());
                    //mapa.DibujarCheckPointCamara(pantalla.getGraphics());
                    //mapa.DibujarPelotasDinamicas(pantalla.getGraphics());
                    mapa.DibujarElementos(pantalla.getGraphics());//jugadores dinamicos y armas dinamicas.
                    mapa.DibujarBarroCamara(pantalla.getGraphics());
                    mapa.DibujarSuperVida(pantalla.getGraphics());
                }
            }else{
                if(renderizadoInteligente==true){
                    pantalla.getGraphics().drawImage(mapaCompleto,0,0,null);
                    mapa.DibujarElementos(pantalla.getGraphics());
                    mapa.DibujarBarro(pantalla.getGraphics());
                    mapa.DibujarSuperVida(pantalla.getGraphics());
                    //mapa.DibujarInteligente(pantalla.getGraphics());
                    //mapa.DibujarPelotas(pantalla.getGraphics());
                    //mapa.DibujarBalas(pantalla.getGraphics());
                    //mapa.DibujarBloques(pantalla.getGraphics());
                    //mapa.DibujarItems(pantalla.getGraphics());
                    //mapa.DibujarCheckPoint(pantalla.getGraphics());
                    //mapa.DibujarSuperEnemigos(pantalla.getGraphics());
                }else{
                    mapa.DibujarCapa(pantalla.getGraphics());
                    mapa.DibujarBalas(pantalla.getGraphics());
                    //mapa.DibujarSuperEnemigos(pantalla.getGraphics());
                }
            }
            /*
	    if(Teclas.modoJuego==2){
                mapa.DibujarNumeros(pantalla.getGraphics());
                mapa.DibujarVida(pantalla.getGraphics());
            }else{
                if(camara==true){
                    mapa.DibujarVidaHud(pantalla.getGraphics());
                }else{
                    mapa.DibujarVida2(pantalla.getGraphics());
                }
            }
            */
	    // El ENd
	    grafico.drawImage(pantalla, 0, 0, this);
	    
	}
        
        public void reiniciarVariables(){
            
            nivel=1;//se le suma 1...
            
        }
	
	public void dibujarPuntos(Graphics g){
            g.setFont(new Font("DigiFaceWide",1,36));
	    g.drawString(String.valueOf(personaje.puntos[0]), 35,300);
	    g.drawString(String.valueOf(personaje.puntos[1]), 840,300);
	}
	
	public void keyTyped(KeyEvent e) {
	    throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void keyPressed(KeyEvent e) {
	    throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void keyReleased(KeyEvent e) {
	    throw new UnsupportedOperationException("Not supported yet.");
	}
	public static void main(String[] args) throws InterruptedException{
		
		menu=new Menu();
                
                
		//para que cuando este listo se ejecute:
		while(yaTermino==false){
			Thread.sleep(1000);
			if(estaCerrada==true){
                            //estaCerrada=false;//new
                            if(juego!=null){
                                ventana.dispose();
                                juego.reiniciarVariables();
                            }
                            
                            Thread.sleep(1000);
                            permiso=true;
                            corriendo=true;
                            juego=new Juego();
                            //juego.ventana.setVisible(true);
                            //System.out.println("pase por aquí");
                            //yaTermino=true;
                            estaCerrada=false;
			}
                        //System.out.println("pase por acá");
		}
		
	}

    @Override
    public void joystickAxisChanged(Joystick jstck) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void joystickButtonChanged(Joystick jstck) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}