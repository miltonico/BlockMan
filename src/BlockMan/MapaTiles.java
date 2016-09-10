package BlockMan;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class MapaTiles {
        public static ArrayList<Generador> generadores=new ArrayList<>();
        public static ArrayList<Items> items=new ArrayList<>();
        public static ArrayList<CheckPoint> punto=new ArrayList<>();
        public static ArrayList<Exoesqueleto> robots=new ArrayList<>();
	public static Personaje personaje=new Personaje();
        public static Actualizaciones act=new Actualizaciones();
        Enemigo enemigo=new Enemigo();
	Balas balas=new Balas();
	public static int map[][];
        public static int mapI[][];
        public static int mapaFisico[][];//para que puedan haber mas bloques de colores con masa (0=nada, 1=bloque, 2=lava).
	public static int lava[][];
	public static int jugadores[][];
	public static int jugadoresDibujados[][];
	public static int objetos[][];
	public static boolean revisado[][];
	boolean cae[];
	boolean flota[];
	private BufferedImage tile;
	private BufferedImage bala;
	private BufferedImage bala2;
        private BufferedImage corazon;
        private BufferedImage barras;
        private BufferedImage estrella;
        private BufferedImage barras2;
        private static InputStream url;
        private static BufferedImage[][] sprites=new BufferedImage[4][14];
	private boolean buscarObjetos=true,buscarLagos=true;
	private int tiempo=-1;
	private int alturaLava;
	private int numeroDeObjetos=0,numeroDeLagos=0,contadorRetardo=0,contador=0, binario=0,flotaX=12,flotaY=34,tiempoLava1=0,tiempoLava2=0,tiempoLava3=0;
	private boolean lavaLista=false;
	int[] contador2=new int[10];//maximo 10 contadores...(lagos de lava).
	int[] altura=new int[10];//maximo 10 alturas de crecimiento para 10 lagos de lava diferentes.
	int[] demora=new int[10];//maximo 10 demoras para 10 lagos diferentes.
	boolean[] sube=new boolean[10];
	boolean[] hayLavaDebajo;
	boolean[] puedeFlotar;
	int[]azar=null;
	int[]limites=null;
	int dormir=0;
	int contadorObjetos=0;
	int eliminarLava=0;
	boolean buscandoLava=false;
	public static boolean cambio=false;
	public static boolean cambio2=false;
        public static int parpadear=0;
	Random r=new Random();
	
	public MapaTiles(int[][] existingMap) {
		
		map = new int[existingMap.length][existingMap[0].length];
                mapI = new int[existingMap.length][existingMap[0].length];
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				map[y][x] = existingMap[y][x];
			}
		}
		
		/*
		if(map.length==20){
			tile = CargarTile("Imagenes/Users/milton/workspace/TetrisMan/tile.png");
			tile2 = CargarTile("tile2.png");
			Tile.TILE_WIDTH=16;
			Tile.TILE_HEIGHT=16;
		}else{
			tile = CargarTile("Imagenes/Users/milton/workspace/TetrisMan/miniTile.png");
			tile2 = CargarTile("Imagenes/Users/milton/workspace/TetrisMan/miniTile2.png");
			Tile.TILE_WIDTH=8;
			Tile.TILE_HEIGHT=8;
		}
		*/
                if(Tile.TILE_WIDTH==16){
                    tile = CargarTile("Imagenes/TILES16.png");
                    bala = CargarTile("Imagenes/16BALA.png");
                    bala2 = CargarTile("Imagenes/16BALA2.png");
                    corazon = CargarTile("Imagenes/16CORAZON.png");
                    barras = CargarTile("Imagenes/16BARRAS.png");
                    estrella = CargarTile("Imagenes/16ESTRELLA.png");
                    barras2 = CargarTile("Imagenes/16BARRAS2.png");
                    //tile2 = CargarTile("tile2.png");
                }
		if(Tile.TILE_WIDTH==8){
                    tile = CargarTile("Imagenes/TILES8.png");
                    bala = CargarTile("Imagenes/8BALA.png");
                    bala2 = CargarTile("Imagenes/8BALA2.png");
                    corazon = CargarTile("Imagenes/8CORAZON.png");
                    barras = CargarTile("Imagenes/8BARRAS.png");
                    estrella = CargarTile("Imagenes/8ESTRELLA.png");
                    barras2 = CargarTile("Imagenes/8BARRAS2.png");
                    //tile2 = CargarTile("tile2.png");
                }
		if(Tile.TILE_WIDTH==4){
                    tile = CargarTile("Imagenes/TILES4.png");
                    bala = CargarTile("Imagenes/4BALA.png");
                    bala2 = CargarTile("Imagenes/4BALA2.png");
                    corazon = CargarTile("Imagenes/4CORAZON.png");
                    barras = CargarTile("Imagenes/4BARRAS.png");
                    estrella = CargarTile("Imagenes/4ESTRELLA.png");
                    barras2 = CargarTile("Imagenes/4BARRAS2.png");
                    //tile2 = CargarTile("tile2.png");
                }
		
	}
	public MapaTiles(int width,int height){
		map = new int[height][width];
                mapI = new int[height][width];
	}
	
	public static MapaTiles DesdeArchivo(String fileName){
	
            
            
            MapaTiles layer = null;
            
            
//            layer.url=MapaTiles.class.getClassLoader().getResourceAsStream(fileName);
//            if (layer.url == null) {
//                System.out.println("Can't find InputStream: "+fileName);
//            }
//            String path=layer.url.toString();
//            path=path.substring(6,path.length());
            
		ArrayList<ArrayList<Integer>> tempLayout = new ArrayList<>();
		try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(MapaTiles.class.getClassLoader().getResourceAsStream(fileName)));
                    String currentLine;

                    while((currentLine = br.readLine()) != null){
                        if(currentLine.isEmpty()){
                            continue;
                        }
                        ArrayList<Integer> row = new ArrayList<>();

                        String[] values = currentLine.trim().split(" ");

                        for(String string : values){
                            if(!string.isEmpty()){
                                int id = Integer.parseInt(string);
                                row.add(id);
                            }
                        }
                        tempLayout.add(row);
                    }
		}catch(Exception e){
                    System.out.println("Could not load map: "+fileName);
                    return null;
		}
		
		int width = tempLayout.get(0).size();
		int height = tempLayout.size();
		
		layer = new MapaTiles(width,height);
		
		lava = new int[height][width];//la matriz de lava.
		revisado = new boolean[height][width];
		jugadores = new int[height][width];
                jugadoresDibujados = new int[height][width];
		objetos = new int[height][width];
                mapaFisico = new int[height][width];
		for(int i=0;i<Teclas.jugadores;i++){
                    if(Teclas.modoJuego==1){
                        switch(i){
                            case 0:
                                Personaje.posIniX[i]=1;
                                break;
                            case 1:
                                Personaje.posIniX[i]=5;
                                break;
                            case 2:
                                Personaje.posIniX[i]=9;
                                break;
                            case 3:
                                Personaje.posIniX[i]=13;
                                break;
                        }
                        Personaje.posIniY[i]=1;
                    }else{
                        switch(i){
                            case 0:
                                Personaje.posIniX[i]=1;
                                break;
                            case 1:
                                Personaje.posIniX[i]=Juego.ancho-4;
                                break;
                            case 2:
                                Personaje.posIniX[i]=11;
                                break;
                            case 3:
                                Personaje.posIniX[i]=Juego.ancho-14;
                                break;

                        }
                        Personaje.posIniY[i]=1;
                    }
                    
                }
                Teclas.estrellaX=-1;
                Teclas.estrellaY=-1;
                int contEnemigo=0;
                int contSuperEnemigo=0;
                int indicePunto=1;
                int indiceGenerador=0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				layer.map[y][x] = tempLayout.get(y).get(x);
                                if(layer.map[y][x]>=100){
                                    String cadenaMapa=Integer.toString(layer.map[y][x]);
                                    int jugador=Integer.parseInt(cadenaMapa.substring(0,cadenaMapa.length()-2));
                                    layer.map[y][x]=Integer.parseInt(Integer.toString(layer.map[y][x]).substring(cadenaMapa.length()-2,cadenaMapa.length()));
                                    if(jugador-1<Teclas.jugadores){
                                        Personaje.posIniX[jugador-1]=x;
                                        Personaje.posIniY[jugador-1]=y;
                                    }
                                    if(jugador==5){//estrella
                                        Teclas.estrellaX=x;
                                        Teclas.estrellaY=y;
                                    }
                                    if(jugador==8){//enemigo
                                        Juego.superEnemigo.add(new SuperEnemigo(x,y,contSuperEnemigo));
                                        contSuperEnemigo++;
                                        /*
                                        Enemigo.enemigoX[contEnemigo]=x;
                                        Enemigo.enemigoY[contEnemigo]=y;
                                        Enemigo.posIniX[contEnemigo]=x;
                                        Enemigo.posIniY[contEnemigo]=y;
                                        contEnemigo++;
                                        */
                                    }
                                    if(jugador==9){//super enemigo
                                        //Juego.superEnemigo.add(new SuperEnemigo(contSuperEnemigo,x,y));
                                        //contSuperEnemigo++;
                                    }
                                    if(jugador>=10 && jugador<45){//items
                                        items.add(new Items(jugador,x,y));//jugador en verdad es un item en este caso.
                                    }
                                    if(jugador==48){//check point.
                                        punto.add(new CheckPoint(indicePunto,x,y));
                                        indicePunto++;
                                    }
                                    if(jugador==49 || jugador==50 || jugador==51 || jugador==52){//generadores.
                                        generadores.add(new Generador(jugador,x,y));//jugador en realidad es el tipo de item del generador.
                                        indiceGenerador++;
                                    }
                                }
                                switch(layer.map[y][x]){
                                    case 0:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 1:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 2:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 3:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 4:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 5:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 6:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 7:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 8:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 9:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 10:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 11:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 12:
                                        mapaFisico[y][x]=0;
                                        break;
                                    case 13:
                                        mapaFisico[y][x]=0;
                                        break;
                                    case 14:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 15:
                                        mapaFisico[y][x]=0;
                                        break;
                                    case 16:
                                        mapaFisico[y][x]=0;
                                        break;
                                    case 17:
                                        mapaFisico[y][x]=2;//lava
                                        break;
                                    case 18:
                                        mapaFisico[y][x]=3;//curacion
                                        break;
                                    case 19:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 20:
                                        mapaFisico[y][x]=0;
                                        break;
                                    case 21:
                                        mapaFisico[y][x]=4;//agua
                                        break;
                                    case 22:
                                        mapaFisico[y][x]=1;
                                        break;
                                    case 23:
                                        mapaFisico[y][x]=5;//barro(agua oscura).
                                        break;
                                    case 24:
                                        mapaFisico[y][x]=1;//tetris skin bloque.
                                        break;
                                    case 25:
                                        mapaFisico[y][x]=0;//tetris skin espacio vacio.
                                        break;
                                }
				lava[y][x]=10;
				revisado[y][x]=false;
				jugadores[y][x]=0;
                                jugadoresDibujados[y][x]=0;
				objetos[y][x]=0;
			}
                        
		}
                Juego.cargoPosiciones=true;
		Enemigo.cantidadEnemigos=contEnemigo;
		/*
		if(height==20){
			layer.tile = layer.CargarTile("Imagenes/Users/milton/workspace/TetrisMan/tile.png");
			layer.tile2 = layer.CargarTile("Imagenes/Users/milton/workspace/TetrisMan/tile2.png");
			Tile.TILE_WIDTH=16;
			Tile.TILE_HEIGHT=16;
		}else{
			layer.tile = layer.CargarTile("Imagenes/Users/milton/workspace/TetrisMan/miniTile.png");
			layer.tile2 = layer.CargarTile("Imagenes/Users/milton/workspace/TetrisMan/miniTile2.png");
			Tile.TILE_WIDTH=8;
			Tile.TILE_HEIGHT=8;
		}
		*/
		if(Tile.TILE_WIDTH==16){
                    layer.tile = layer.CargarTile("Imagenes/TILES16.png");
                    layer.bala = layer.CargarTile("Imagenes/16BALA.png");
                    layer.bala2 = layer.CargarTile("Imagenes/16BALA2.png");
                    layer.corazon = layer.CargarTile("Imagenes/16CORAZON.png");
                    layer.barras = layer.CargarTile("Imagenes/16BARRAS.png");
                    layer.estrella = layer.CargarTile("Imagenes/16ESTRELLA.png");
                    layer.barras2 = layer.CargarTile("Imagenes/16BARRAS2.png");
                    //layer.tile2 = layer.CargarTile("Imagenes/Users/milton/workspace/TetrisMan/tile2.png");
                }
		if(Tile.TILE_WIDTH==8){
                    layer.tile = layer.CargarTile("Imagenes/TILES8.png");
                    layer.bala = layer.CargarTile("Imagenes/8BALA.png");
                    layer.bala2 = layer.CargarTile("Imagenes/8BALA2.png");
                    layer.corazon = layer.CargarTile("Imagenes/8CORAZON.png");
                    layer.barras = layer.CargarTile("Imagenes/8BARRAS.png");
                    layer.estrella = layer.CargarTile("Imagenes/8ESTRELLA.png");
                    layer.barras2 = layer.CargarTile("Imagenes/8BARRAS2.png");
                    //layer.tile2 = layer.CargarTile("Imagenes/Users/milton/workspace/TetrisMan/tile2.png");
                }
		if(Tile.TILE_WIDTH==4){
                    layer.tile = layer.CargarTile("Imagenes/TILES4.png");
                    layer.bala = layer.CargarTile("Imagenes/4BALA.png");
                    layer.bala2 = layer.CargarTile("Imagenes/4BALA2.png");
                    layer.corazon = layer.CargarTile("Imagenes/4CORAZON.png");
                    layer.barras = layer.CargarTile("Imagenes/4BARRAS.png");
                    layer.estrella = layer.CargarTile("Imagenes/4ESTRELLA.png");
                    layer.barras2 = layer.CargarTile("Imagenes/4BARRAS2.png");
                    //layer.tile2 = layer.CargarTile("Imagenes/Users/milton/workspace/TetrisMan/tile2.png");
                }
                for(int i=0;i<4;i++){
                    for(int j=0;j<14;j++){
                        sprites[i][j]=layer.CargarTile("Imagenes/jugador"+(i+1)+"/sprite"+j+".png");
                    }
		}
		return layer;
		
	}
	
        public void DibujaTile(Graphics g,int x,int y,int index,int jugador){
            if(Juego.tetrisSkin==true){
                int bloque=obtenerPropiedadBloque(index);//mapaFisico[y/Tile.TILE_HEIGHT][x/Tile.TILE_WIDTH];
                if(bloque==1){
                    index=24;//bloque
                }else{
                    if(bloque==2){
                        index=56;//lava
                    }else{
                        if(bloque==3){
                            index=57;//curacion
                        }else{
                            if(bloque==4){
                                index=55;//agua
                            }else{
                                if(bloque==5){
                                    index=24;//barro
                                }else{
                                    index=25;//nada
                                }
                            }
                        }
                    }
                }
            }
            if(Teclas.modoJuego==2 && (Juego.nivelCamuflaje==true || Juego.nivel>=5) && jugador!=-1){
                index=Juego.superJugador.get(jugador).index+12;//color solido del camuflaje.
            }
            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
            if(Juego.camara==false){
                x+=3*Tile.TILE_WIDTH;
            }
            int x2=x+Tile.TILE_WIDTH;
            int y2=y+Tile.TILE_HEIGHT;
            int x3=corX*Tile.TILE_WIDTH;
            int y3=corY*Tile.TILE_HEIGHT;
            int x4=(corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH;
            int y4=(corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT;
            
            g.drawImage(tile,x,y,x2,y2,x3,y3,x4,y4,null);
        }
        
        public int obtenerPropiedadBloque(int index){
            
            int propiedad=0;
            
            switch(index){
                case 0:
                    propiedad=1;
                    break;
                case 1:
                    propiedad=1;
                    break;
                case 2:
                    propiedad=1;
                    break;
                case 3:
                    propiedad=1;
                    break;
                case 4:
                    propiedad=1;
                    break;
                case 5:
                    propiedad=1;
                    break;
                case 6:
                    propiedad=1;
                    break;
                case 7:
                    propiedad=1;
                    break;
                case 8:
                    propiedad=1;
                    break;
                case 9:
                    propiedad=1;
                    break;
                case 10:
                    propiedad=1;
                    break;
                case 11:
                    propiedad=1;
                    break;
                case 12:
                    propiedad=0;
                    break;
                case 13:
                    propiedad=0;
                    break;
                case 14:
                    propiedad=1;
                    break;
                case 15:
                    propiedad=0;
                    break;
                case 16:
                    propiedad=0;
                    break;
                case 17:
                    propiedad=2;//lava
                    break;
                case 18:
                    propiedad=3;//curacion
                    break;
                case 19:
                    propiedad=1;
                    break;
                case 20:
                    propiedad=0;
                    break;
                case 21:
                    propiedad=4;//agua
                    break;
                case 22:
                    propiedad=1;
                    break;
                case 23:
                    propiedad=5;//barro(agua oscura).
                    break;
                case 26://bounce
                    propiedad=1;
                    break;
                case 27://bounce
                    propiedad=1;
                    break;
                case 28://bounce
                    propiedad=1;
                    break;
                case 46:
                    propiedad=1;//brazo arma(descargada).
                    break;
                case 47:
                    propiedad=1;//brazo arma(cargada).
                    break;
                case 48:
                    propiedad=1;//bullet.
                    break;
                case 49:
                    propiedad=1;//axe enemigo.
                    break;
                case 50:
                    propiedad=1;//axe amarilla
                    break;
                case 51:
                    propiedad=1;//axe roja
                    break;
                case 52:
                    propiedad=1;//axe verde
                    break;
                case 53:
                    propiedad=1;//axe azul
                    break;
            }
            
            return propiedad;
        }
        
        public void DibujaTileCamara(Graphics g,int x,int y,int index, int camara, boolean tetrisSkin){
            if(Juego.tetrisSkin==true || tetrisSkin){
                int bloque=obtenerPropiedadBloque(index);//mapaFisico[y/Tile.TILE_HEIGHT][x/Tile.TILE_WIDTH];
                if(bloque==1){
                    index=24;//bloque
                }else{
                    if(bloque==2){
                        index=56;//lava
                    }else{
                        if(bloque==3){
                            index=57;//curacion
                        }else{
                            if(bloque==4){
                                index=55;//agua
                            }else{
                                if(bloque==5){
                                    index=24;//barro
                                }else{
                                    index=25;//nada
                                }
                            }
                        }
                    }
                }
                
            }
            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
            
            //cortar el tile si esta entrando o saliendo de la camara.
            
            int sumaX=0;
            int sumaY=0;
            switch(camara){
                case 0:
                    sumaX=0;
                    sumaY=0;
                    break;
                case 1:
                    sumaX=Camara.anchoCamara+3;
                    sumaY=0;
                    break;
                case 2:
                    sumaX=0;
                    sumaY=Camara.altoCamara+1;
                    break;
                case 3:
                    sumaX=Camara.anchoCamara+3;
                    sumaY=Camara.altoCamara+1;
                    break;

            }
            
            //offset del tile con respecto a la entrada o salida en la camara.
            int offsetIzqX=(Juego.superJugador.get(camara).posCamX)-x;
            int offsetArrY=(Juego.superJugador.get(camara).posCamY)-y;
            int offsetDerX=(Juego.superJugador.get(camara).posCamX)+(Camara.anchoCamara)*Tile.TILE_WIDTH-(x+Tile.TILE_WIDTH);
            int offsetAbaY=(Juego.superJugador.get(camara).posCamY)+(Camara.altoCamara)*Tile.TILE_HEIGHT-(y+Tile.TILE_HEIGHT);
            
            if(offsetIzqX>16) offsetIzqX=16;
            if(offsetIzqX<0) offsetIzqX=0;
            
            if(offsetArrY>16) offsetArrY=16;
            if(offsetArrY<0) offsetArrY=0;
            
            if(offsetDerX<-16) offsetDerX=-16;
            if(offsetDerX>0) offsetDerX=0;
            
            if(offsetAbaY<-16) offsetAbaY=-16;
            if(offsetAbaY>0) offsetAbaY=0;
            //System.out.println("offsetX="+x%Tile.TILE_WIDTH);
            //System.out.println("oix="+offsetIzqX+"odx="+offsetDerX+"oarry="+offsetArrY+"oabaY="+offsetAbaY);
            int x1=3*Tile.TILE_WIDTH+x+(sumaX)*Tile.TILE_WIDTH-Juego.superJugador.get(camara).posCamX+offsetIzqX;
            int y1=y+(sumaY)*Tile.TILE_HEIGHT-Juego.superJugador.get(camara).posCamY+offsetArrY;
            int x2=3*Tile.TILE_WIDTH+x+(sumaX)*Tile.TILE_WIDTH-Juego.superJugador.get(camara).posCamX+Tile.TILE_WIDTH+offsetDerX;
            int y2=y+(sumaY)*Tile.TILE_HEIGHT-Juego.superJugador.get(camara).posCamY+Tile.TILE_HEIGHT+offsetAbaY;
            //if(x%Tile.TILE_WIDTH!=0) System.out.println("offsetX="+x%Tile.TILE_WIDTH+" y el offsetIzqX="+offsetIzqX);
            //else System.out.println(" y el offsetIzqX="+offsetIzqX);
            //System.out.println("x1="+x1+",y1="+y1);
            int x3=corX*Tile.TILE_WIDTH+offsetIzqX;
            int y3=corY*Tile.TILE_HEIGHT+offsetArrY;
            int x4=(corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH+offsetDerX;
            int y4=(corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT+offsetAbaY;
            
            g.drawImage(tile,x1,y1,x2,y2,x3,y3,x4,y4,null);
        }
        
	public BufferedImage CargarTile(String fileName){
		BufferedImage img = null;
//		this.url=this.getClass().getClassLoader().getResourceAsStream(fileName);
		try{
//                    if (url == null) {
//                        System.out.println("Can't find InputStream: "+fileName);
//                    }else{
//                        String path=url.toString();
//                        path=path.substring(6,path.length());
//                        img = ImageIO.read(new File(path));
//                    }
                    img=ImageIO.read(MapaTiles.class.getClassLoader().getResourceAsStream(fileName));
                    
		}catch(IOException e){
                    System.out.println("Could not load image: "+fileName);
		}
		
		return img;
	}
        
        public void DibujarElementos(Graphics g){
            for(int i=0;i<(Juego.camara==true?Teclas.jugadores:1);i++){
                for(int j=0;j<Juego.superJugador.size();j++){
                    //dibujar cuerpo de cada jugador:
                    if(Juego.camara){
                        //si estan en la misma dimension:
                        if(Juego.superJugador.get(i).deadDimension==Juego.superJugador.get(j).deadDimension){
                            /*
                            if(Juego.superJugador.get(j).direccion==1){
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1])/Tile.TILE_WIDTH]==25) 
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                }else{DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                    if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                        if(Juego.tetrisSkin==false && Juego.superJugador.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }else{
                                        if(Juego.tetrisSkin==false && Juego.superJugador.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }
                                }
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                            }else{
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                }else{DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                    if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                        if(Juego.tetrisSkin==false && Juego.superJugador.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }else{
                                        if(Juego.tetrisSkin==false && Juego.superJugador.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }
                                }
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1])/Tile.TILE_WIDTH]==25) 
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                            }
                            */
                            if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1])/Tile.TILE_WIDTH]==25)
                                DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                            else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                            if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1])/Tile.TILE_WIDTH]==25) 
                                DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                            else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                            if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1])/Tile.TILE_WIDTH]==25)
                                DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                            else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                            if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1])/Tile.TILE_WIDTH]==25)
                                DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                            else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                            if(Juego.superJugador.get(j).posicion!=11){//si no es un cuadradito:
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                }else{DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                    if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                        if(Juego.tetrisSkin==false && Juego.superJugador.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }else{
                                        if(Juego.tetrisSkin==false && Juego.superJugador.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }
                                }
                            }
                            if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1])/Tile.TILE_WIDTH]==25)
                                DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                            else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                            if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1])/Tile.TILE_WIDTH]==25)
                                DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                            else DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],(Juego.superJugador.get(j).energy>=Juego.superJugador.get(j).energyLimit?47:46),i,Juego.superJugador.get(i).deadDimension);
                            if(Juego.superJugador.get(j).posicion==11){//si es un cuadradito:
                                if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                    DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                }else{DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],Juego.superJugador.get(j).index,i,Juego.superJugador.get(i).deadDimension);
                                    if(map[(Juego.superJugador.get(j).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superJugador.get(j).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                        if(Juego.tetrisSkin==false && Juego.superJugador.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }else{
                                        if(Juego.tetrisSkin==false && Juego.superJugador.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }
                                }
                            }
                        }
                    }else{
                        if(Juego.superJugador.get(j).alive){
                            DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],Juego.superJugador.get(j).index,j);
                            DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],Juego.superJugador.get(j).index,j);
                            DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],Juego.superJugador.get(j).index,j);
                            DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],Juego.superJugador.get(j).index,j);
                            if(Juego.superJugador.get(j).posicion!=11){//si no es un cuadradito:
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],Juego.superJugador.get(j).index,j);
                                if(Juego.tetrisSkin==false) DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,j);
                            }
                            DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],Juego.superJugador.get(j).index,j);
                            DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],(Juego.superJugador.get(j).energy>=Juego.superJugador.get(j).energyLimit?47:46),j);
                            if(Juego.superJugador.get(j).posicion==11){//si es un cuadradito:
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],Juego.superJugador.get(j).index,j);
                                if(Juego.tetrisSkin==false) DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54,j);
                            }
                            /*
                            if(Juego.superJugador.get(j).direccion==1){
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],Juego.superJugador.get(j).index);
                                if(Juego.tetrisSkin==false) DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],Juego.superJugador.get(j).index);
                            }else{
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieDer)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoDer)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cintura)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).torso)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],Juego.superJugador.get(j).index);
                                if(Juego.tetrisSkin==false) DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).cabeza)][1][1],54);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).pieIzq)][1][1],Juego.superJugador.get(j).index);
                                DibujaTile(g,Juego.superJugador.get(j).x+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][0][1],Juego.superJugador.get(j).y+Juego.superJugador.get(j).positionParts[Integer.parseInt(Juego.superJugador.get(j).brazoIzq)][1][1],Juego.superJugador.get(j).index);
                            }
                            */
                        }
                    }
                    
                }
                for(int j=0;j<Juego.superEnemigo.size();j++){
                    //dibujar cuerpo de cada enemigo:
                    if(Juego.camara){
                        //si estan en la misma dimension:
                        if(Juego.superJugador.get(i).deadDimension==Juego.superEnemigo.get(j).deadDimension){
                            if(Juego.superEnemigo.get(j).direccion==1){
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][0][1])/Tile.TILE_WIDTH]==25) 
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                }else{DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                    if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                        if(Juego.tetrisSkin==false && Juego.superEnemigo.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }else{
                                        if(Juego.tetrisSkin==false && Juego.superEnemigo.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }
                                }
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                            }else{
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                }else{DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                    if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1])/Tile.TILE_WIDTH]==25){
                                        if(Juego.tetrisSkin==false && Juego.superEnemigo.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }else{
                                        if(Juego.tetrisSkin==false && Juego.superEnemigo.get(j).deadDimension==false) DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],54,i,Juego.superJugador.get(i).deadDimension);
                                    }
                                }
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][0][1])/Tile.TILE_WIDTH]==25)
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                                if(map[(Juego.superEnemigo.get(j).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][1][1])/Tile.TILE_HEIGHT][(Juego.superEnemigo.get(j).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][0][1])/Tile.TILE_WIDTH]==25) 
                                    DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                else DibujaTileCamara(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][1][1],11,i,Juego.superJugador.get(i).deadDimension);
                            }
                        }
                    }else{
                        if(Juego.superEnemigo.get(j).alive){
                            if(Juego.superEnemigo.get(j).direccion==1){
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],11,-1);
                                if(Juego.tetrisSkin==false) DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],54,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][1][1],11,-1);
                            }else{
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieDer)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoDer)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cintura)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).torso)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],11,-1);
                                if(Juego.tetrisSkin==false) DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).cabeza)][1][1],54,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).pieIzq)][1][1],11,-1);
                                DibujaTile(g,Juego.superEnemigo.get(j).x+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][0][1],Juego.superEnemigo.get(j).y+Juego.superEnemigo.get(j).positionParts[Integer.parseInt(Juego.superEnemigo.get(j).brazoIzq)][1][1],11,-1);
                            }
                        }
                    }
                }
                
                for(int j=0;j<Juego.weaponAxe.size();j++){
                    //System.out.println("i="+i+"j="+j);
                    if((Juego.camara && Juego.superJugador.get(i).deadDimension==Juego.weaponAxe.get(j).deadDimension) || (!Juego.camara && !Juego.weaponAxe.get(j).deadDimension)){
                        for(int c=0;c<(Juego.weaponAxe.get(j).positionParts.length-1);c++){
                            if(Juego.weaponAxe.get(j).visibleParts[c+1]==true){
                                if(Juego.camara==true){
                                    //si esta en area tetris se dibuja estilo tetris:
                                    if(map[(Juego.weaponAxe.get(j).y+Tile.TILE_HEIGHT/2+Juego.weaponAxe.get(j).positionParts[c+1][1][1])/Tile.TILE_HEIGHT][(Juego.weaponAxe.get(j).x+Tile.TILE_WIDTH/2+Juego.weaponAxe.get(j).positionParts[c+1][0][1])/Tile.TILE_WIDTH]==25){
                                        DibujaTileCamara(g,Juego.weaponAxe.get(j).x+Juego.weaponAxe.get(j).positionParts[c+1][0][1],Juego.weaponAxe.get(j).y+Juego.weaponAxe.get(j).positionParts[c+1][1][1],24,i,Juego.superJugador.get(i).deadDimension);
                                    }else{
                                        DibujaTileCamara(g,Juego.weaponAxe.get(j).x+Juego.weaponAxe.get(j).positionParts[c+1][0][1],Juego.weaponAxe.get(j).y+Juego.weaponAxe.get(j).positionParts[c+1][1][1],(Juego.weaponAxe.get(j).tipo==1?Juego.superJugador.get(Juego.weaponAxe.get(j).jugador).jugador+50:49),i,Juego.superJugador.get(i).deadDimension);
                                    }
                                }else{
                                    DibujaTile(g,Juego.weaponAxe.get(j).x+Juego.weaponAxe.get(j).positionParts[c+1][0][1],Juego.weaponAxe.get(j).y+Juego.weaponAxe.get(j).positionParts[c+1][1][1],(Juego.weaponAxe.get(j).tipo==1?Juego.superJugador.get(Juego.weaponAxe.get(j).jugador).jugador+50:49),Juego.weaponAxe.get(j).jugador);
                                }
                            }
                        }
                    }
		}
                for(int j=0;j<Juego.weaponBullet.size();j++){
                    //System.out.println("i="+i+"j="+j);
                    if((Juego.camara && Juego.superJugador.get(i).deadDimension==Juego.weaponBullet.get(j).deadDimension) || (!Juego.camara && !Juego.weaponBullet.get(j).deadDimension)){
                        if(Juego.camara==true){
                            //si esta en area tetris se dibuja estilo tetris:
                            if(map[(Tile.TILE_HEIGHT/2+Juego.weaponBullet.get(j).y)/Tile.TILE_HEIGHT][(Tile.TILE_WIDTH/2+Juego.weaponBullet.get(j).x)/Tile.TILE_WIDTH]==25){
                                DibujaTileCamara(g,Juego.weaponBullet.get(j).x,Juego.weaponBullet.get(j).y,24,i,Juego.superJugador.get(i).deadDimension);
                            }else{
                                DibujaTileCamara(g,Juego.weaponBullet.get(j).x,Juego.weaponBullet.get(j).y,48,i,Juego.superJugador.get(i).deadDimension);
                            }
                        }else{
                            DibujaTile(g,Juego.weaponBullet.get(j).x,Juego.weaponBullet.get(j).y,48,Juego.weaponBullet.get(j).jugador);
                        }
                    }
		}
                for(int j=0;j<items.size();j++){
                    if(Juego.camara==true){
                        if(Juego.superJugador.get(i).deadDimension==false){
                            DibujaTileCamara(g,items.get(j).posicionItemX,items.get(j).posicionItemY,items.get(j).tipoItem,i,Juego.superJugador.get(i).deadDimension);
                        }
                    }else{
                        DibujaTile(g,items.get(j).posicionItemX,items.get(j).posicionItemY,items.get(j).tipoItem,-1);
                    }
                }
                for(int j=0;j<punto.size();j++){
                    if(Juego.camara==true){
                        if(Juego.superJugador.get(i).deadDimension==false){
                            DibujaTileCamara(g,punto.get(j).posicionPuntoX,punto.get(j).posicionPuntoY,34,i,Juego.superJugador.get(i).deadDimension);
                        }
                    }else{
                        DibujaTile(g,punto.get(j).posicionPuntoX,punto.get(j).posicionPuntoY,34,-1);
                    }
                }
            }
        }
        
        public void DibujarBarro(Graphics g){
            int index=0;
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[0].length; x++) {
                    index=map[y][x];
                    if(index==23) DibujaTile(g,x*Tile.TILE_WIDTH,y*Tile.TILE_HEIGHT,index,-1);
                }
            }
        }
        
        public void DibujarBarroCamara(Graphics g){
            
            for(int i=0;i<Juego.superJugador.size();i++){
                
                int sumaX=0;
                int sumaY=0;
                switch(i){
                    case 0:
                        sumaX=0;
                        sumaY=0;
                        break;
                    case 1:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=0;
                        break;
                    case 2:
                        sumaX=0;
                        sumaY=Camara.altoCamara+1;
                        break;
                    case 3:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=Camara.altoCamara+1;
                        break;

                }
                
		for (int y = 0; y <Camara.altoCamara+(((Juego.superJugador.get(i).posCamY/Tile.TILE_HEIGHT+Camara.altoCamara)>=Juego.alto)?0:1); y++) {
                    for (int x = 0; x <Camara.anchoCamara+(((Juego.superJugador.get(i).posCamX/Tile.TILE_WIDTH+Camara.anchoCamara)>=Juego.ancho)?0:1); x++) {
                        
                        int index=map[y+(Juego.superJugador.get(i).posCamY/Tile.TILE_HEIGHT)][x+(Juego.superJugador.get(i).posCamX/Tile.TILE_WIDTH)];
                        //System.out.println(index);
                        int tile_x=(Juego.superJugador.get(i).posCamX%Tile.TILE_WIDTH);
                        int tile_y=(Juego.superJugador.get(i).posCamY%Tile.TILE_HEIGHT);
                        if(index==23) DibujaTileCamara(g,Juego.superJugador.get(i).posCamX-tile_x+(x)*Tile.TILE_WIDTH,Juego.superJugador.get(i).posCamY-tile_y+(y)*Tile.TILE_HEIGHT,index,i,Juego.superJugador.get(i).deadDimension);
                        
                    }
		}
            }
            
        }
        
	public void mostrarMatriz(){
		for(int i=0;i<40;i++){
			for(int j=0;j<40;j++){
				if(lava[i][j]==10) {
					System.out.print(" X");
				}else{
					System.out.print(" "+lava[i][j]);
				}
			}
			System.out.println("");
		}
	}
	
        public void gravedadItems(){
            for(int j=0;j<items.size();j++){
                if((items.get(j).posicionItemY+1)<Juego.alto && mapaFisico[items.get(j).posicionItemY+1][items.get(j).posicionItemX]!=1){
                    mapI[items.get(j).posicionItemY][items.get(j).posicionItemX]=0;//este mapa es lo que se muestra en pantalla.
                    items.get(j).posicionItemY++;
                }
            }
        }
        
        public void generarItems(double porcentaje){//porcentaje/100 posibilidades de generar un item...
            porcentaje*=10;
            for(int i=0;i<generadores.size();i++){
                int numero=(int)(Math.random()*1000+1);//genera un numero del 1 al 1000...
                if(numero<=porcentaje){
                    int tipo=0;
                    switch(generadores.get(i).tipo){
                        case 49:
                            tipo=26;//curacion
                            break;
                        case 50:
                            tipo=37;//pelotas
                            break;
                    }
                    if(tipo!=0){
                        items.add(new Items(tipo,generadores.get(i).x,generadores.get(i).y));
                    }
                }
            }
            
        }
        
        public void DibujarItemsCamara(Graphics g){
            for(int j=0;j<items.size();j++){
                for(int i=0;i<Teclas.jugadores;i++){
                    if(itemEstaEnCamara(i,j)){
                        int sumaX=0;
                        int sumaY=0;
                        switch(i){
                            case 0:
                                sumaX=0;
                                sumaY=0;
                                break;
                            case 1:
                                sumaX=Camara.anchoCamara+3;
                                sumaY=0;
                                break;
                            case 2:
                                sumaX=0;
                                sumaY=Camara.altoCamara+1;
                                break;
                            case 3:
                                sumaX=Camara.anchoCamara+3;
                                sumaY=Camara.altoCamara+1;
                                break;

                        }

                        int index=items.get(j).tipoItem;

                        if(Juego.tetrisSkin==true){
                            /*
                            if(mapaFisico[items.get(j).posicionItemY+Camara.posCamY[i]][items.get(j).posicionItemX+Camara.posCamX[i]]==1 || jugadores[items.get(j).posicionItemY+Camara.posCamY[i]][items.get(j).posicionItemX+Camara.posCamX[i]]==1){
                                index=24;
                            }else{
                                index=25;
                            }
                            */
                            index=24;
                        }

                        int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                        int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                        
                        g.drawImage(tile,
                        3*Tile.TILE_WIDTH+(items.get(j).posicionItemX-Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH,
                        (items.get(j).posicionItemY-Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT,
                        3*Tile.TILE_WIDTH+((items.get(j).posicionItemX-Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                        ((items.get(j).posicionItemY-Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                        corX*Tile.TILE_WIDTH,
                        corY*Tile.TILE_HEIGHT,
                        (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                        (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                        null);
                    }
		}
            }
        }
        public void DibujarPelotasDinamicas(Graphics g){//dibuja pelotas que se mueven dinamicamente.
            for(int j=0;j<Juego.pelota.size();j++){
                for(int i=0;i<Teclas.jugadores;i++){
                    DibujaTileCamara(g,Juego.pelota.get(j).x,Juego.pelota.get(j).y,28,i,Juego.superJugador.get(i).deadDimension);
                    /*
                    if(pelotaEstaEnCamara(i,j)){
                        int sumaX=0;
                        int sumaY=0;
                        switch(i){
                            case 0:
                                sumaX=0;
                                sumaY=0;
                                break;
                            case 1:
                                sumaX=Camara.anchoCamara+3;
                                sumaY=0;
                                break;
                            case 2:
                                sumaX=0;
                                sumaY=Camara.altoCamara+1;
                                break;
                            case 3:
                                sumaX=Camara.anchoCamara+3;
                                sumaY=Camara.altoCamara+1;
                                break;

                        }
                    
                        int index=28;

                        if(Juego.tetrisSkin==true){
                            index=28;
                        }

                        int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                        int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                        
                        g.drawImage(tile,
                        3*Tile.TILE_WIDTH+(Juego.pelota.get(j).x-(Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH),
                        Juego.pelota.get(j).y-(Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT,
                        3*Tile.TILE_WIDTH+(Juego.pelota.get(j).x-(Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                        Juego.pelota.get(j).y-((Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                        corX*Tile.TILE_WIDTH,
                        corY*Tile.TILE_HEIGHT,
                        (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                        (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                        null);
                    }
            */
		}
            }
        }
        public void DibujarCheckPointCamara(Graphics g){
            for(int j=0;j<punto.size();j++){
                for(int i=0;i<Teclas.jugadores;i++){//4 camaras...
                    if(checkEstaEnCamara(i,j)){
                        int sumaX=0;
                        int sumaY=0;
                        switch(i){
                            case 0:
                                sumaX=0;
                                sumaY=0;
                                break;
                            case 1:
                                sumaX=Camara.anchoCamara+3;
                                sumaY=0;
                                break;
                            case 2:
                                sumaX=0;
                                sumaY=Camara.altoCamara+1;
                                break;
                            case 3:
                                sumaX=Camara.anchoCamara+3;
                                sumaY=Camara.altoCamara+1;
                                break;

                        }

                        boolean encendido=false;
                        for(int k=0;k<6;k++){
                            int index=0;
                            switch(k){
                                case 0:
                                    index=48;//checkpoint
                                    break;
                                case 1:
                                    index=49;//j1
                                    break;
                                case 2:
                                    index=50;//j2
                                    break;
                                case 3:
                                    index=51;//j3
                                    break;
                                case 4:
                                    index=52;//j4
                                    break;
                                case 5:
                                    index=53;//luz de encendido
                                    break;
                            }
                            
                            if(Juego.tetrisSkin==true){
                                /*if(mapaFisico[punto.get(j).posicionPuntoY-Camara.posCamY[i]][punto.get(j).posicionPuntoX-Camara.posCamX[i]]==1 || jugadores[punto.get(j).posicionPuntoY-Camara.posCamY[i]][punto.get(j).posicionPuntoX-Camara.posCamX[i]]==1){
                                    index=24;
                                }else{
                                    index=25;
                                }
                                */
                                index=24;
                            }
                            
                            if(k==0 || (k==5 && encendido)){
                                
                                int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                                g.drawImage(tile,
                                3*Tile.TILE_WIDTH+(punto.get(j).posicionPuntoX-Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH,
                                (punto.get(j).posicionPuntoY-Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT,
                                3*Tile.TILE_WIDTH+((punto.get(j).posicionPuntoX-Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                ((punto.get(j).posicionPuntoY-Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                corX*Tile.TILE_WIDTH,
                                corY*Tile.TILE_HEIGHT,
                                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                null);
                            }else{
                                if(k!=5 && k!=0 && punto.get(j).indice==act.checkJugador[k-1]){
                                    encendido=true;
                                    int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                    int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                                    g.drawImage(tile,
                                    3*Tile.TILE_WIDTH+(punto.get(j).posicionPuntoX-Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH,
                                    (punto.get(j).posicionPuntoY-Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT,
                                    3*Tile.TILE_WIDTH+((punto.get(j).posicionPuntoX-Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                    ((punto.get(j).posicionPuntoY-Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                    corX*Tile.TILE_WIDTH,
                                    corY*Tile.TILE_HEIGHT,
                                    (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                    (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                    null);
                                }
                            }
                        }
                    }
		}
            }
        }
        public boolean itemEstaEnCamara(int jugador, int item){
            if(items.get(item).posicionItemX>=Camara.posCamX[jugador] && items.get(item).posicionItemX<Camara.posCamX[jugador]+Camara.anchoCamara && items.get(item).posicionItemY>=Camara.posCamY[jugador] && items.get(item).posicionItemY<Camara.posCamY[jugador]+Camara.altoCamara){
                return true;
            }
            
            return false;
        }
        
        public boolean pelotaEstaEnCamara(int jugador, int pelota){
            if(((int)((Juego.pelota.get(pelota).x+Tile.TILE_WIDTH)/Tile.TILE_WIDTH))>=Camara.posCamX[jugador] && (int)(Juego.pelota.get(pelota).x/Tile.TILE_WIDTH)<Camara.posCamX[jugador]+Camara.anchoCamara && ((int)(Juego.pelota.get(pelota).y+Tile.TILE_HEIGHT)/Tile.TILE_HEIGHT)>=Camara.posCamY[jugador] && (int)(Juego.pelota.get(pelota).y/Tile.TILE_HEIGHT)<Camara.posCamY[jugador]+Camara.altoCamara){
                return true;
            }
            
            return false;
        }
        
        public boolean checkEstaEnCamara(int jugador, int indice){
            if(punto.get(indice).posicionPuntoX>=Camara.posCamX[jugador] && punto.get(indice).posicionPuntoX<Camara.posCamX[jugador]+Camara.anchoCamara && punto.get(indice).posicionPuntoY>=Camara.posCamY[jugador] && punto.get(indice).posicionPuntoY<Camara.posCamY[jugador]+Camara.altoCamara){
                return true;
            }
            
            return false;
        }
        
        public void DibujarCheckPoint(Graphics g){
            
            for(int i=0;i<punto.size();i++){
                int index=0;
                if(Juego.tetrisSkin==true){
                    
                    index=24;
                    
                    int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                    int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                    
                    g.drawImage(tile,
                    3*Tile.TILE_WIDTH+punto.get(i).posicionPuntoX*Tile.TILE_WIDTH,
                    punto.get(i).posicionPuntoY*Tile.TILE_HEIGHT,
                    3*Tile.TILE_WIDTH+(punto.get(i).posicionPuntoX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                    (punto.get(i).posicionPuntoY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                    corX*Tile.TILE_WIDTH,
                    corY*Tile.TILE_HEIGHT,
                    (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                    (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                    null);
                    
                }else{
                    boolean encendido=false;
                    for(int j=0;j<6;j++){
                        index=0;
                        switch(j){
                            case 0:
                                index=48;//checkpoint
                                break;
                            case 1:
                                index=49;//j1
                                break;
                            case 2:
                                index=50;//j2
                                break;
                            case 3:
                                index=51;//j3
                                break;
                            case 4:
                                index=52;//j4
                                break;
                            case 5:
                                index=53;//luz de encendido
                                break;
                        }

                        if(j==0 || (j==5 && encendido)){
                            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                            g.drawImage(tile,
                            3*Tile.TILE_WIDTH+punto.get(i).posicionPuntoX*Tile.TILE_WIDTH,
                            punto.get(i).posicionPuntoY*Tile.TILE_HEIGHT,
                            3*Tile.TILE_WIDTH+(punto.get(i).posicionPuntoX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                            (punto.get(i).posicionPuntoY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                            corX*Tile.TILE_WIDTH,
                            corY*Tile.TILE_HEIGHT,
                            (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                            (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                            null);
                        }else{

                            if(j!=5 && j!=0 && punto.get(i).indice==act.checkJugador[j-1]){
                                encendido=true;
                                int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                                g.drawImage(tile,
                                3*Tile.TILE_WIDTH+punto.get(i).posicionPuntoX*Tile.TILE_WIDTH,
                                punto.get(i).posicionPuntoY*Tile.TILE_HEIGHT,
                                3*Tile.TILE_WIDTH+(punto.get(i).posicionPuntoX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                (punto.get(i).posicionPuntoY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                corX*Tile.TILE_WIDTH,
                                corY*Tile.TILE_HEIGHT,
                                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                null);
                            }
                        }
                    }
                }
            }
        }
        public void DibujarItems(Graphics g){
            
            for(int i=0;i<items.size();i++){
                
                int index=items.get(i).tipoItem;
                
                if(Juego.tetrisSkin==true){
                    
                    index=24;
                    
                }
                
                int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                g.drawImage(tile,
                3*Tile.TILE_WIDTH+items.get(i).posicionItemX*Tile.TILE_WIDTH,
                items.get(i).posicionItemY*Tile.TILE_HEIGHT,
                3*Tile.TILE_WIDTH+(items.get(i).posicionItemX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                (items.get(i).posicionItemY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                corX*Tile.TILE_WIDTH,
                corY*Tile.TILE_HEIGHT,
                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                null);
            }
        }
        
	public void DibujarCapa(Graphics g) {
            parpadear++;
            if(parpadear==4){
                parpadear=0;
                if(cambio==true)
                cambio=true;
                else cambio=false;
            }
            
            int index=0;
            int corX=0;
            int corY=0;
            for (int y = 0; y < map.length; y++) {
                    for (int x = 0; x < map[0].length; x++) {
                            index=map[y][x];

                            if(Teclas.modoJuego==2 && (Juego.nivelCamuflaje==true || Juego.nivel>=5)){

                                if(index!=11){
                                    //efecto del mapa:
                                    index=Camuflaje.mapaCamuflaje[y][x];
                                }

                                if(jugadoresDibujados[y][x]==1){//si es parte del jugador 1;
                                    index=15;
                                }
                                if(jugadoresDibujados[y][x]==2){//si es parte del jugador 2;
                                    index=16;
                                }
                                if(jugadoresDibujados[y][x]==3){//si es parte del jugador 3;
                                    index=20;
                                }
                                if(jugadoresDibujados[y][x]==4){//si es parte del jugador 4;
                                    index=21;
                                }

                            }else{
//                                if(index==20 && Juego.graficos==true){
//                                    for(int i=0;i<4;i++){
//                                        if(x>=Personaje.x[i] && x<Personaje.x[i]+3 && y>=Personaje.y[i] && y<Personaje.y[i]+4){
//                                            index=-1;
//                                        }
//                                    }
//                                }
                                if(jugadoresDibujados[y][x]==1 && Juego.graficos==false){//si es parte del jugador 1;
                                    index=3;
                                }
                                if(jugadoresDibujados[y][x]==2 && Juego.graficos==false){//si es parte del jugador 2;
                                    index=5;
                                }
                                if(jugadoresDibujados[y][x]==3 && Juego.graficos==false){//si es parte del jugador 3;
                                    index=7;
                                }
                                if(jugadoresDibujados[y][x]==4 && Juego.graficos==false){//si es parte del jugador 4;
                                    index=9;
                                }
                                if(jugadoresDibujados[y][x]==8){//si es parte del enemigo;
                                    index=2;
                                }
                            }
                            if(Juego.tetrisSkin==true){
                                if((mapaFisico[y][x]==1 || jugadores[y][x]==1) || ((mapaFisico[y][x]==2 || jugadores[y][x]==2) && (parpadear==0 || parpadear==2)) || ((mapaFisico[y][x]==3 || jugadores[y][x]==3) && (parpadear==0)) || ((mapaFisico[y][x]==4 || jugadores[y][x]==4) && (parpadear==2))){
                                    if(mapaFisico[y][x]>1){
                                        if(mapaFisico[y][x]==2){
                                            if((y+x)%2==0 && parpadear==0){
                                                index=24;
                                            }else{
                                                index=25;
                                            }
                                        }else{
                                            if((y+x)%2==0 && cambio==true){
                                                index=24;
                                            }else{
                                                index=25;
                                            }
                                        }
                                    }else{
                                        index=24;
                                    }
                                    
                                }else{
                                    index=25;
                                }
                            }
                            if(index!=-1){
                                corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                                g.drawImage(tile,
                                3*Tile.TILE_WIDTH+x*Tile.TILE_WIDTH,
                                y*Tile.TILE_HEIGHT,
                                3*Tile.TILE_WIDTH+(x*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                (y*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                corX*Tile.TILE_WIDTH,
                                corY*Tile.TILE_HEIGHT,
                                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                null);
                            }
                            

                            /*
                            int index = map[y][x];
                            if(index==1 || jugadores[y][x]==1){
                                    g.drawImage(tile,2*x+x*Tile.TILE_WIDTH,2*y+y*Tile.TILE_HEIGHT,null);
                            }
                            */
                    }
            }
            if(Juego.graficos==true && Tile.TILE_HEIGHT==16){
                for(int i=0;i<Teclas.jugadores;i++){//en base a 16x16...
                    if(Personaje.estaVivo[i]==true){
                        int xx=3*Tile.TILE_WIDTH+Personaje.x[i]*Tile.TILE_WIDTH-((sprites[i][Personaje.spriteActual[i]].getWidth()-48)/2);
                        int yy=Personaje.y[i]*Tile.TILE_HEIGHT-((sprites[i][Personaje.spriteActual[i]].getHeight()-64));
                        g.drawImage(sprites[i][Personaje.spriteActual[i]],xx,yy,null);
                    }
                    
                }
            }
            
            //dibujar la estrella:
            if(Juego.tetrisSkin==true){
                index=24;
            }else{
                index=34;
            }
            corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
            corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

            if(Teclas.estrellaX!=-1){//si hay estrella, x o y no deberian ser -1.
                g.drawImage(tile,
                3*Tile.TILE_WIDTH+Teclas.estrellaX*Tile.TILE_WIDTH,
                Teclas.estrellaY*Tile.TILE_HEIGHT,
                (3*Tile.TILE_WIDTH+Teclas.estrellaX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                (Teclas.estrellaY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                corX*Tile.TILE_WIDTH,
                corY*Tile.TILE_HEIGHT,
                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                null);
            }
                
	}
        /*
        public void DibujarSuperEnemigos(Graphics g){
            int index=0;
            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
            for(int i=0;i<Juego.superEnemigo.size();i++){
                for(int j=0;j<4;j++){
                    for(int k=0;k<3;k++){
                        if(index!=mapI[Juego.superEnemigo.get(i).y+j][Juego.superEnemigo.get(i).x+k]){
                                mapI[Juego.superEnemigo.get(i).y+j][Juego.superEnemigo.get(i).x+k]=index;//este mapa es lo que se muestra en pantalla.
                        }
                        if(Juego.superEnemigo.get(i).personaje[j][k]==1){
                            g.drawImage(tile,
                            3*Tile.TILE_WIDTH+(Juego.superEnemigo.get(i).x+k)*Tile.TILE_WIDTH,
                            (Juego.superEnemigo.get(i).y+j)*Tile.TILE_HEIGHT,
                            3*Tile.TILE_WIDTH+((Juego.superEnemigo.get(i).x+k)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                            ((Juego.superEnemigo.get(i).y+j)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                            corX*Tile.TILE_WIDTH,
                            corY*Tile.TILE_HEIGHT,
                            (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                            (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                            null);
                        }
                    }
                }
            }
        }
        public void DibujarSuperEnemigosCamara(Graphics g){
            int index=0;
            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
            for(int l=0;l<Teclas.jugadores;l++){//cada 1 de las 4 camaras.
                int sumaX=0;
                int sumaY=0;
                switch(l){
                    case 0:
                        sumaX=0;
                        sumaY=0;
                        break;
                    case 1:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=0;
                        break;
                    case 2:
                        sumaX=0;
                        sumaY=Camara.altoCamara+1;
                        break;
                    case 3:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=Camara.altoCamara+1;
                        break;

                }
                for(int i=0;i<Juego.superEnemigo.size();i++){
                    for(int j=0;j<4;j++){
                        for(int k=0;k<3;k++){
                            if(Juego.superEnemigo.get(i).personaje[j][k]==1){
                                if(cuadroEnemigoEstaEnCamara(Juego.superEnemigo.get(i).x+k,Juego.superEnemigo.get(i).y+j,l)){
                                    g.drawImage(tile,
                                    3*Tile.TILE_WIDTH+(Juego.superEnemigo.get(i).x+k-Camara.posCamX[l]+sumaX)*Tile.TILE_WIDTH,
                                    (Juego.superEnemigo.get(i).y+j-Camara.posCamY[l]+sumaY)*Tile.TILE_HEIGHT,
                                    3*Tile.TILE_WIDTH+((Juego.superEnemigo.get(i).x+k-Camara.posCamX[l]+sumaX)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                    ((Juego.superEnemigo.get(i).y+j-Camara.posCamY[l])*Tile.TILE_HEIGHT+sumaY)+Tile.TILE_HEIGHT,
                                    corX*Tile.TILE_WIDTH,
                                    corY*Tile.TILE_HEIGHT,
                                    (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                    (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                    null);
                                }
                            }
                        }
                    }
                }
            }
        }
        */
        public boolean cuadroEnemigoEstaEnCamara(int x, int y, int jugador){
            if(x>=Camara.posCamX[jugador] && x<Camara.posCamX[jugador]+Camara.anchoCamara && y>=Camara.posCamY[jugador] && y<Camara.posCamY[jugador]+Camara.altoCamara){
                return true;
            }
            return false;
        }
        
        public void DibujarInteligente(Graphics g) {
            
            parpadear++;
            if(parpadear>=16){
                parpadear=0;
                if(cambio==true)
                cambio=false;
                else cambio=true;
            }
            if(parpadear%4==0){
                if(cambio2==true)
                cambio2=false;
                else cambio2=true;
            }
            int index=0;
            int corX=0;
            int corY=0;
            for (int y = 0; y < map.length; y++) {
                    for (int x = 0; x < map[0].length; x++) {
                            index=map[y][x];

                            if(Teclas.modoJuego==2 && (Juego.nivelCamuflaje==true || Juego.nivel>=5)){

                                if(index!=11){
                                    //efecto del mapa:
                                    index=Camuflaje.mapaCamuflaje[y][x];
                                }

                                if(jugadoresDibujados[y][x]==1){//si es parte del jugador 1;
                                    index=15;
                                }
                                if(jugadoresDibujados[y][x]==2){//si es parte del jugador 2;
                                    index=16;
                                }
                                if(jugadoresDibujados[y][x]==3){//si es parte del jugador 3;
                                    index=20;
                                }
                                if(jugadoresDibujados[y][x]==4){//si es parte del jugador 4;
                                    index=21;
                                }

                            }else{
//                                if(index==20 && Juego.graficos==true){
//                                    for(int i=0;i<4;i++){
//                                        if(x>=Personaje.x[i] && x<Personaje.x[i]+3 && y>=Personaje.y[i] && y<Personaje.y[i]+4){
//                                            index=-1;
//                                        }
//                                    }
//                                }
                                if(jugadoresDibujados[y][x]==1 && Juego.graficos==false){//si es parte del jugador 1;
                                    index=3;
                                }
                                if(jugadoresDibujados[y][x]==2 && Juego.graficos==false){//si es parte del jugador 2;
                                    index=5;
                                }
                                if(jugadoresDibujados[y][x]==3 && Juego.graficos==false){//si es parte del jugador 3;
                                    index=7;
                                }
                                if(jugadoresDibujados[y][x]==4 && Juego.graficos==false){//si es parte del jugador 4;
                                    index=9;
                                }
                                if(jugadoresDibujados[y][x]==8){//si es parte del enemigo;
                                    index=2;
                                }
                            }
                            if(Juego.tetrisSkin==true){
                                if((mapaFisico[y][x]==1 || jugadores[y][x]==1)){// && mapaFisico[y][x]!=4(para que no se vea en el agua)
                                    index=24;
                                }else{
                                    index=25;
                                }
                                if(((mapaFisico[y][x]==2 && jugadores[y][x]!=1)) || ((mapaFisico[y][x]==3 && jugadores[y][x]!=1)) || ((mapaFisico[y][x]==4 && jugadores[y][x]!=1))){
                                    if(mapaFisico[y][x]>1){
                                        if(mapaFisico[y][x]==2){
                                            if(cambio2==true){
                                                if((y+x)%2==0){
                                                    index=24;
                                                }else{
                                                    index=25;
                                                }
                                            }else{
                                                if((y+x)%2==1){
                                                    index=24;
                                                }else{
                                                    index=25;
                                                }
                                            }
                                        }else{
                                            if(cambio==true){
                                                if((y+x)%2==0){
                                                    index=24;
                                                }else{
                                                    index=25;
                                                }
                                            }else{
                                                if((y+x)%2==1){
                                                    index=24;
                                                }else{
                                                    index=25;
                                                }
                                            }
                                        }
                                    }else{
                                        index=24;
                                    }
                                }
                                if(mapaFisico[y][x]==5){
                                    index=24;
                                }
                            }
                            if(index!=mapI[y][x]){
                                mapI[y][x]=index;//este mapa es lo que se muestra en pantalla.
                                /*
                                if(Juego.tetrisSkin==true){
                                    if(mapaFisico[y][x]==1 || jugadores[y][x]==1){
                                        index=24;
                                    }else{
                                        index=25;
                                    }
                                }
                                */
                                
                                if(index!=-1){
                                    corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                    corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                                    g.drawImage(tile,
                                    3*Tile.TILE_WIDTH+x*Tile.TILE_WIDTH,
                                    y*Tile.TILE_HEIGHT,
                                    3*Tile.TILE_WIDTH+(x*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                    (y*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                    corX*Tile.TILE_WIDTH,
                                    corY*Tile.TILE_HEIGHT,
                                    (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                    (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                    null);
                                }
                            }
                            
                            

                            /*
                            int index = map[y][x];
                            if(index==1 || jugadores[y][x]==1){
                                    g.drawImage(tile,2*x+x*Tile.TILE_WIDTH,2*y+y*Tile.TILE_HEIGHT,null);
                            }
                            */
                    }
            }
            if(Juego.graficos==true && Tile.TILE_HEIGHT==16){
                for(int i=0;i<Teclas.jugadores;i++){//en base a 16x16...
                    if(Personaje.estaVivo[i]==true){
                        int xx=3*Tile.TILE_WIDTH+Personaje.x[i]*Tile.TILE_WIDTH-((sprites[i][Personaje.spriteActual[i]].getWidth()-48)/2);
                        int yy=Personaje.y[i]*Tile.TILE_HEIGHT-((sprites[i][Personaje.spriteActual[i]].getHeight()-64));
                        g.drawImage(sprites[i][Personaje.spriteActual[i]],xx,yy,null);
                    }
                    
                }
            }
            
            //dibujar la estrella:
            if(Juego.tetrisSkin==true){
                index=24;
            }else{
                index=34;
            }
            corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
            corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

            if(Teclas.estrellaX!=-1){//si hay estrella, x o y no deberian ser -1.
                g.drawImage(tile,
                3*Tile.TILE_WIDTH+Teclas.estrellaX*Tile.TILE_WIDTH,
                Teclas.estrellaY*Tile.TILE_HEIGHT,
                (3*Tile.TILE_WIDTH+Teclas.estrellaX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                (Teclas.estrellaY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                corX*Tile.TILE_WIDTH,
                corY*Tile.TILE_HEIGHT,
                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                null);
            }
                
	}
        public void DibujarCamara(Graphics g) {
            parpadear++;
            if(parpadear>=16){
                parpadear=0;
                if(cambio==true)
                cambio=false;
                else cambio=true;
            }
            if(parpadear%4==0){
                if(cambio2==true)
                cambio2=false;
                else cambio2=true;
            }
            for(int i=0;i<Teclas.jugadores;i++){
                
                int sumaX=0;
                int sumaY=0;
                switch(i){
                    case 0:
                        sumaX=0;
                        sumaY=0;
                        break;
                    case 1:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=0;
                        break;
                    case 2:
                        sumaX=0;
                        sumaY=Camara.altoCamara+1;
                        break;
                    case 3:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=Camara.altoCamara+1;
                        break;

                }
                
		for (int y = 0; y < Camara.altoCamara; y++) {
                    for (int x = 0; x < Camara.anchoCamara; x++) {
                        
                        int index=map[y+Camara.posCamY[i]][x+Camara.posCamX[i]];

                        if(Teclas.modoJuego==2 && (Juego.nivelCamuflaje==true || Juego.nivel>=5)){

                            if(index!=11){
                                //efecto del mapa:
                                index=Camuflaje.mapaCamuflaje[y+Camara.posCamY[i]][x+Camara.posCamX[i]];
                            }

                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==1){//si es parte del jugador 1;
                                index=15;
                            }
                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==2){//si es parte del jugador 2;
                                index=16;
                            }
                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==3){//si es parte del jugador 3;
                                index=20;
                            }
                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==4){//si es parte del jugador 4;
                                index=21;
                            }

                        }else{
                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==1 && Juego.graficos==false){//si es parte del jugador 1;
                                index=3;
                            }
                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==2 && Juego.graficos==false){//si es parte del jugador 2;
                                index=5;
                            }
                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==3 && Juego.graficos==false){//si es parte del jugador 3;
                                index=7;
                            }
                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==4 && Juego.graficos==false){//si es parte del jugador 4;
                                index=9;
                            }
                            if(jugadoresDibujados[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==8){//si es parte del enemigo;
                                index=2;
                            }
                        }
                        if(Juego.tetrisSkin==true){
                            if((mapaFisico[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==1 || jugadores[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==1)){// && mapaFisico[y+Camara.posCamY[i]][x+Camara.posCamX[i]]!=4(para que no se vea en el agua)
                                index=24;
                            }else{
                                index=25;
                            }
                            if(((mapaFisico[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==2 && jugadores[y+Camara.posCamY[i]][x+Camara.posCamX[i]]!=1)) || ((mapaFisico[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==3 && jugadores[y+Camara.posCamY[i]][x+Camara.posCamX[i]]!=1)) || ((mapaFisico[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==4 && jugadores[y+Camara.posCamY[i]][x+Camara.posCamX[i]]!=1))){
                                if(mapaFisico[y+Camara.posCamY[i]][x+Camara.posCamX[i]]>1){
                                    if(mapaFisico[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==2){
                                        if(cambio2==true){
                                            if((y+Camara.posCamY[i]+x+Camara.posCamX[i])%2==0){
                                                index=24;
                                            }else{
                                                index=25;
                                            }
                                        }else{
                                            if((y+Camara.posCamY[i]+x+Camara.posCamX[i])%2==1){
                                                index=24;
                                            }else{
                                                index=25;
                                            }
                                        }
                                    }else{
                                        if(cambio==true){
                                            if((y+Camara.posCamY[i]+x+Camara.posCamX[i])%2==0){
                                                index=24;
                                            }else{
                                                index=25;
                                            }
                                        }else{
                                            if((y+Camara.posCamY[i]+x+Camara.posCamX[i])%2==1){
                                                index=24;
                                            }else{
                                                index=25;
                                            }
                                        }
                                    }
                                }else{
                                    index=24;
                                }
                            }
                            if(mapaFisico[y+Camara.posCamY[i]][x+Camara.posCamX[i]]==5){
                                index=24;
                            }
                        }
                        
                        int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                        int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                        
                        g.drawImage(tile,
                        3*Tile.TILE_WIDTH+(x+sumaX)*Tile.TILE_WIDTH,
                        (y+sumaY)*Tile.TILE_HEIGHT,
                        3*Tile.TILE_WIDTH+((x+sumaX)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                        ((y+sumaY)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                        corX*Tile.TILE_WIDTH,
                        corY*Tile.TILE_HEIGHT,
                        (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                        (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                        null);

                    }
		}
                if(Juego.graficos==true && Tile.TILE_HEIGHT==16){
                    if(Personaje.estaVivo[i]==true){
                        for(int j=0;j<Teclas.jugadoresTotales;j++){
                            if(jugadorEstaEnCamara(i,j)){
                                int xx=3*Tile.TILE_WIDTH+((Personaje.x[j]-Camara.posCamX[i])+sumaX)*Tile.TILE_WIDTH-((sprites[j][Personaje.spriteActual[j]].getWidth()-48)/2);
                                int yy=((Personaje.y[j]-Camara.posCamY[i])+sumaY)*Tile.TILE_HEIGHT-((sprites[j][Personaje.spriteActual[j]].getHeight()-64));
                                int xxx=(Personaje.x[j])*Tile.TILE_WIDTH-((sprites[j][Personaje.spriteActual[j]].getWidth()-48)/2);
                                int yyy=(Personaje.y[j])*Tile.TILE_HEIGHT-((sprites[j][Personaje.spriteActual[j]].getHeight()-64));
                                int inicioX=0;//del sprite del jugador visita...
                                int finX=52;//ancho del sprite...
                                int inicioY=0;
                                int finY=70;//alto del sprite...
                                if((Camara.posCamX[i]*Tile.TILE_WIDTH)-xxx>0){//si se corta el sprite por la izquierda...
                                    inicioX=(Camara.posCamX[i]*Tile.TILE_WIDTH)-xxx;
                                }
                                if(((Camara.posCamX[i]+Camara.anchoCamara)*Tile.TILE_WIDTH)-xxx>0){//si se corta el sprite por la derecha...
                                    finX=((Camara.posCamX[i]+Camara.anchoCamara)*Tile.TILE_WIDTH)-xxx;
                                }
                                if((Camara.posCamY[i]*Tile.TILE_HEIGHT)-yyy>0){//si se corta el sprite por la arriba...
                                    inicioY=(Camara.posCamY[i]*Tile.TILE_HEIGHT)-yyy;
                                }
                                if(((Camara.posCamY[i]+Camara.altoCamara)*Tile.TILE_HEIGHT)-yyy>0){//si se corta el sprite por la abajo...
                                    finY=((Camara.posCamY[i]+Camara.altoCamara)*Tile.TILE_HEIGHT)-yyy;
                                }
                                g.drawImage(sprites[j][Personaje.spriteActual[j]],xx+inicioX,yy+inicioY,xx+inicioX+finX,yy+inicioY+finY,inicioX,inicioY,finX,finY,null);
                            }
                        }
                    }
                }
                //dibujar la estrella:
                if(Teclas.estrellaX!=-1 && estaEnCamara(i)==true){//si hay estrella, x o y no deberian ser -1.
                    g.drawImage(estrella,3*Tile.TILE_WIDTH+((Teclas.estrellaX-Camara.posCamX[i]+sumaX)*Tile.TILE_WIDTH),((Teclas.estrellaY-Camara.posCamY[i]+sumaY)*Tile.TILE_HEIGHT),null);
                }
            }
	}
        
        public void DibujarSuperCamara(Graphics g){
            for(int i=0;i<Juego.superJugador.size();i++){
                
                int sumaX=0;
                int sumaY=0;
                switch(i){
                    case 0:
                        sumaX=0;
                        sumaY=0;
                        break;
                    case 1:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=0;
                        break;
                    case 2:
                        sumaX=0;
                        sumaY=Camara.altoCamara+1;
                        break;
                    case 3:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=Camara.altoCamara+1;
                        break;

                }
                
		for (int y = 0; y <Camara.altoCamara+(((Juego.superJugador.get(i).posCamY/Tile.TILE_HEIGHT+Camara.altoCamara)>=Juego.alto)?0:1); y++) {
                    for (int x = 0; x <Camara.anchoCamara+(((Juego.superJugador.get(i).posCamX/Tile.TILE_WIDTH+Camara.anchoCamara)>=Juego.ancho)?0:1); x++) {
                        
                        int index=map[y+(Juego.superJugador.get(i).posCamY/Tile.TILE_HEIGHT)][x+(Juego.superJugador.get(i).posCamX/Tile.TILE_WIDTH)];
                        //System.out.println(index);
                        int tile_x=(Juego.superJugador.get(i).posCamX%Tile.TILE_WIDTH);
                        int tile_y=(Juego.superJugador.get(i).posCamY%Tile.TILE_HEIGHT);
                        DibujaTileCamara(g,Juego.superJugador.get(i).posCamX-tile_x+(x)*Tile.TILE_WIDTH,Juego.superJugador.get(i).posCamY-tile_y+(y)*Tile.TILE_HEIGHT,index,i,Juego.superJugador.get(i).deadDimension);
                        //dibujar estrella:
                        if(y+(Juego.superJugador.get(i).posCamY/Tile.TILE_HEIGHT)==Teclas.estrellaY && x+(Juego.superJugador.get(i).posCamX/Tile.TILE_WIDTH)==Teclas.estrellaX){
                            DibujaTileCamara(g,Juego.superJugador.get(i).posCamX-tile_x+(x)*Tile.TILE_WIDTH,Juego.superJugador.get(i).posCamY-tile_y+(y)*Tile.TILE_HEIGHT,35,i,Juego.superJugador.get(i).deadDimension);
                        }
                    }
		}
            }
        }
        
        public void DibujarSuperCamaraInteligente(Graphics g){
            for(int i=0;i<Juego.superJugador.size();i++){
                
                int sumaX=0;
                int sumaY=0;
                switch(i){
                    case 0:
                        sumaX=3;
                        sumaY=0;
                        break;
                    case 1:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=0;
                        break;
                    case 2:
                        sumaX=3;
                        sumaY=Camara.altoCamara+1;
                        break;
                    case 3:
                        sumaX=Camara.anchoCamara+3;
                        sumaY=Camara.altoCamara+1;
                        break;

                }
                
                g.drawImage(Juego.mapaCompleto,
                sumaX*Tile.TILE_WIDTH,
                sumaY*Tile.TILE_HEIGHT,
                (sumaX+Camara.anchoCamara)*Tile.TILE_WIDTH,
                (sumaY+Camara.altoCamara)*Tile.TILE_HEIGHT,
                Juego.superJugador.get(i).posCamX,
                Juego.superJugador.get(i).posCamY,
                Juego.superJugador.get(i).posCamX+Camara.anchoCamara*Tile.TILE_WIDTH,
                Juego.superJugador.get(i).posCamY+Camara.altoCamara*Tile.TILE_HEIGHT,
                null);
            }
        }
        
        public void DibujarMapaCompleto(Graphics g){
            int index=0;
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[0].length; x++) {
                    index=map[y][x];
                    if(Teclas.modoJuego==2 && (Juego.nivelCamuflaje==true || Juego.nivel>=5)){

                        if(index!=11){
                            //efecto del mapa:
                            index=Camuflaje.mapaCamuflaje[y][x];
                        }

                        if(jugadoresDibujados[y][x]==1){//si es parte del jugador 1;
                            index=15;
                        }
                        if(jugadoresDibujados[y][x]==2){//si es parte del jugador 2;
                            index=16;
                        }
                        if(jugadoresDibujados[y][x]==3){//si es parte del jugador 3;
                            index=20;
                        }
                        if(jugadoresDibujados[y][x]==4){//si es parte del jugador 4;
                            index=21;
                        }

                    }
                    DibujaTile(g,x*Tile.TILE_WIDTH,y*Tile.TILE_HEIGHT,index,-1);
                }
            }
            //estrella:
            DibujaTile(g,Teclas.estrellaX*Tile.TILE_WIDTH,Teclas.estrellaY*Tile.TILE_HEIGHT,35,-1);
        }
        
        public boolean jugadorEstaEnCamara(int jugadorCamara, int jugadorVisita){
            //se le resta el ancho y el alto del personaje a la posicion x e y de la camara respectivamente, para que este dentro del recuadro aunque no completamente...
            if(Personaje.x[jugadorVisita]>=Camara.posCamX[jugadorCamara]-3 && Personaje.x[jugadorVisita]<Camara.posCamX[jugadorCamara]+Camara.anchoCamara && Personaje.y[jugadorVisita]>=Camara.posCamY[jugadorCamara]-4 && Personaje.y[jugadorVisita]<Camara.posCamY[jugadorCamara]+Camara.altoCamara){
                return true;
            }
            
            return false;
        }
        
	public boolean estaEnCamara(int jugador){
            
            if(Teclas.estrellaX>=Camara.posCamX[jugador] && Teclas.estrellaX<Camara.posCamX[jugador]+Camara.anchoCamara && Teclas.estrellaY>=Camara.posCamY[jugador] && Teclas.estrellaY<Camara.posCamY[jugador]+Camara.altoCamara){
                return true;
            }
            
            return false;
        }
	
	public void DibujarObjetos(){
		if(buscarObjetos==true){
			buscarObjetos=false;
			int indice=0;
			for(int i=0;i<40;i++){
				for(int j=0;j<40;j++){
					if(map[i][j]==3 && objetos[i][j]==0){
						indice++;
						numeroDeObjetos++;
						CrearObjeto(i,j,indice);
					}
				}
			}
			cae=new boolean[numeroDeObjetos+1];
			flota=new boolean[numeroDeObjetos+1];
			hayLavaDebajo=new boolean[numeroDeObjetos+1];
			puedeFlotar=new boolean[numeroDeObjetos+1];
			for(int i=1;i<=numeroDeObjetos;i++){
				puedeFlotar[i]=true;
			}
		}
		//caida:
		for(int i=1;i<=numeroDeObjetos;i++){
			puedeFlotar[i]=true;
			cae[i]=true;
			flota[i]=false;
		}
		for(int i=0;i<40;i++){
			for(int j=0;j<40;j++){
				if(objetos[i][j]!=0){
					if(map[i-1][j]==1 || jugadores[i-1][j]==1){
						puedeFlotar[objetos[i][j]]=false;
					}
					if(map[i+1][j]==1 && objetos[i+1][j]==0 && lava[i+1][j]==10){
						cae[objetos[i][j]]=false;
						//System.out.println("hola");
					}
					if(lava[i+1][j]!=10){
						//System.out.println(lava[i+1][j]);
						flota[objetos[i][j]]=true;
						cae[objetos[i][j]]=false;
						//System.out.println("llegue");
					}
				}
			}
		}
		//System.out.println(flota[1]);
		for(int i=1;i<=numeroDeObjetos;i++){
			if(cae[i]==true){
				puedeFlotar[i]=true;//modificar cuando hayan mas cosas encima.
				for(int j=39;j>=0;j--){
					for(int k=0;k<40;k++){
						if(objetos[j][k]==i){
							objetos[j+1][k]=i;
							objetos[j][k]=0;
							map[j+1][k]=1;
							map[j][k]=0;
						}
					}
				}
			}
		}
		
	}
	
	public void CrearObjeto(int i, int j, int indice){
		
		objetos[i][j]=indice;
		map[i][j]=1;//el cuadro de este objeto se almacena en una matriz exclusiva y ademas equivale a 1 en el mapa.
		//lava[i][j]=indice;//indice=ya reviso este cuadrado para el correspondiente lago de lava.
		//revisado[i][j]=true;
		
		//if(i<39 && map[i+1][j]==2) map[i+1][j]=4;//la lava normal se transforma en lava que crece.
		if(i<39 && map[i+1][j]==3 && objetos[i+1][j]==0){//abajo.
			CrearObjeto(i+1,j,indice);
		}
		//if(j>0 && map[i][j-1]==2) map[i][j-1]=4;
		if(i<39 && map[i][j-1]==3 && objetos[i][j-1]==0){//izquierda.
			CrearObjeto(i,j-1,indice);
		}
		//if(j<39 && map[i][j+1]==2) map[i][j+1]=4;
		if(i<39 && map[i][j+1]==3 && objetos[i][j+1]==0){//derecha.
			CrearObjeto(i,j+1,indice);
		}
		//if(i>0 && map[i-1][j]==2) map[i-1][j]=4;
		if(i<39 && map[i-1][j]==3 && objetos[i-1][j]==0){//arriba.
			CrearObjeto(i-1,j,indice);
		}
	}
	
	public void flotarObjetos(int indice){
		for(int i=1;i<=numeroDeObjetos;i++){
			boolean coincide=false;//si corresponde la lava al objeto.
			//System.out.println(indice);
			if(flota[i]==true){
				//System.out.println(indice);
				for(int j=39;j>=0;j--){
					for(int k=0;k<40;k++){
						if(objetos[j][k]==i){
							//System.out.println(lava[j+1][k]+"="+indice);
							if(lava[j+1][k]==indice){
								//System.out.println("hola");
								coincide=true;
								j=-1;
								k=40;
							}
						}
					}
				}
			}
			//System.out.println(coincide);
			if(coincide==true){
				hayLavaDebajo[i]=true;
			}
		}
		
	}
	
	public void revisarSiFlota(int indice){
		
		for(int i=1;i<=numeroDeObjetos;i++){
			if(hayLavaDebajo[i]==true && puedeFlotar[i]==true){
				hayLavaDebajo[i]=false;
				if(sube[indice]==true){//revisa si la lava sube.
					//System.out.println(altura[indice]);
					for(int x=0;x<40;x++){//recorre el eje x.
						if(objetos[altura[indice]-1][x]==i){//revisa si esta el objeto correspondiente sobre la altura en la que la lava deberia subir).
							for(int j=0;j<40;j++){
								for(int k=0;k<40;k++){
									if(objetos[j][k]==i){
										objetos[j-1][k]=i;
										objetos[j][k]=0;
										map[j-1][k]=1;
										map[j][k]=4;
									}
								}
							}
							x=40;
						}
					}
				}
			}
		}
		
	}
	
	public void buscarLagos(){
		//System.out.println("hola");
		buscandoLava=true;
		numeroDeLagos=0;
		int indice=0;
		for(int i=39;i>=0;i--){
			for(int j=0;j<40;j++){
				if(map[i][j]==2){
					lava[i][j]=0;
				}
				if(map[i][j]>=4 && map[i][j]<=7 && lava[i][j]>=numeroDeLagos){
					numeroDeLagos++;
					indice++;//parte en 1 porque el 0 es para la lava normal (2 ene le mapa).
					CrecerLava(i,j,indice);
					
				}
			}
		}
		//System.out.println(numeroDeLagos);
		buscandoLava=false;
		indice=0;
	}
	
	public void DibujarCapa2(Graphics g) {
		
		if(lavaLista==true){// retardo para el comienzo del avance de la lava.

			if(buscarLagos==true){
				buscarLagos=false;
				buscarLagos();
			}
		}
		
		contadorRetardo++;
		
		if(contadorRetardo==8){
			
			contadorRetardo=0;
			for(int i=1;i<=numeroDeLagos;i++){
				/*
				if(dormir>=100){
					dormir=0;
					mostrarMatriz();
				}
				if(contador2[i]==2){
					contador2[i]=0;
					LagosDeLava(i);
					CrecerLava2(i);
					ActualizarLava(i);
				}
				*/
				
				contador2[i]++;
				//System.out.println(demora[i]);
				if(contador2[i]>=(demora[i])){//demora = retardo de avance del acido (de acuerdo a cantidad de cuadros avanzados).
					contador2[i]=0;
					revisarSiFlota(i);
					ActualizarLava(i);
					//mostrarMatriz();
					
				}
				if(contador2[i]==0){
					LagosDeLava(i);
					flotarObjetos(i);//debe ir justo aqui (aqui el acido debajo del objeto tiene un indice especifico).
					//si hay acido debajo, antes de actualizar el acido, revisa si este sube en la correspondiente altura.
					CrecerLava2(i);
				}
			}
			
		}
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				int index = lava[y][x];//no se usa este (porque la lava avanzaria instantaneamente).
				if(map[y][x]==2 || map[y][x]==4){//si es mayor o igual a 0 y menor que 10 (es lava).
					if(alterna(y,x)==true){
						g.drawImage(tile,2*x+x*Tile.TILE_WIDTH,2*y+y*Tile.TILE_HEIGHT,null);
					}
				}
				
			}
		}
		
		contador++;
		if(contador==4) {
			contador=0;
			binario++;
			if(binario==2) binario=0;
		}
		
	}
	
	public void acido(){
		
	}
	
	/*
	public boolean seDibuja(int i){
		
		if(tiempo==-1){
			contador2++;
		}else{
			if(limites==null){
				limites=new int[contador2];
				azar=new int[contador2];
				for(int j=0;j<contador2;j++){
					limites[j]=0;
					azar[j]=0;
				}
			}
			if(azar[i]==limites[i] || azar[i]>=16){//aumentar el segundo para aumentar la espera de revisado si no salio premiado.
				int azar1=(r.nextInt(128));//azar para posibilidad (1/128/4).
				int azar2=8;//mayor que cualquier limite (limite).
				if(azar1==0) azar2=0;
				if(azar1==7) azar2=1;
				if(azar1==15) azar2=2;
				if(azar1==23) azar2=3;
				azar[i]=azar2;
				limites[i]=azar2+4;
				
			}
			if(tiempo==azar[i] && azar[i]<limites[i]){//el parpadeo dura 4 frames (120 milisegundos(?)).
				azar[i]++;//azar2 generado (4) + el tiempo de parpadeo (4) = tiempo (8).
				return false;
			}else{
				if(azar[i]>=8){//limite (mayor o igual al tiempo).
					azar[i]++;
				}
			}
		}
		
		return true;
		
		
	}
	*/
	public boolean alterna(int y, int x){
		return (x+y)%2==binario;
	}
	public void limpiarJugadores(){// ya no se usa
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				int index = map[y][x];//no se usa.
				if(jugadores[y][x]==1){
					jugadores[y][x]=0;
				}
			}
		}
	}
	public void DibujaJugador(int jugador){
            //en los jugadores:
            //el 0 representa que no hay jugador.
            //el 1 representa a todos los jugadores.
            
            //en los jugadoresDibujados:
            //el 1 representa al jugador 1.
            //el 2 representa al jugador 2.
            //el 3 representa al jugador 3.
            //el 4 representa al jugador 4.
            
            if(personaje.estaVivo[jugador]==true){
		for(int i=0;i<4;i++){
			for(int j=0;j<5;j++){
				if(personaje.personaje[jugador][i][j]==1){
					jugadores[i+personaje.getY(jugador)][j+personaje.getX(jugador)]=1;
                                        jugadoresDibujados[i+personaje.getY(jugador)][j+personaje.getX(jugador)]=jugador+1;
				}
			}
		}
            }
	}
	public void borrarJugador(int jugador){
            if(personaje.estaVivo[jugador]==true){
                for(int i=0;i<4;i++){
			for(int j=0;j<5;j++){
				if(personaje.personaje[jugador][i][j]==1){//podria compararse la matriz de jugadores para borrarlos.
					jugadores[i+personaje.getY(jugador)][j+personaje.getX(jugador)]=0;
                                        jugadoresDibujados[i+personaje.getY(jugador)][j+personaje.getX(jugador)]=0;
				}
			}
		}
            }
		
	}
        public void borrarJugador2(int jugador){//para online
            
            for(int i=0;i<Juego.ancho;i++){
                for(int j=0;j<Juego.alto;j++){
                    if(jugadoresDibujados[j][i]==(jugador+1)){
                        jugadores[j][i]=0;
                        jugadoresDibujados[j][i]=0;
                    }
                }
            }
		
	}
        public void dibujarBloques(){
            //el 5 representa a los bloques.
            for(int j=0;j<Teclas.jugadores;j++){//ademas materializa los bloques:
                for(int i=0;i<balas.cantidadBloques;i++){
                    if(balas.bloque[j][i][0]!=0){
                        for(int n=0;n<balas.bloque[j][i][3];n++){
                            int naux=n;
                            if(balas.bloque[j][i][0]==2){
                                naux=-1*n;
                            }
                            jugadores[balas.bloque[j][i][2]][balas.bloque[j][i][1]+naux]=1;
                            jugadoresDibujados[balas.bloque[j][i][2]][balas.bloque[j][i][1]+naux]=5;
                        }
                    }
                }
            }
        }
        public void borrarBloques(){
            for(int i=0;i<Juego.ancho;i++){
                for(int j=0;j<Juego.alto;j++){
                    if(jugadores[j][i]!=0 && jugadoresDibujados[j][i]==5){
                        jugadores[j][i]=0;
                    }
                }
            }
        }
        
	public void DibujarBalas(Graphics g){//ahora se puede usar con el renderizado inteligente XD
            for(int j=0;j<Teclas.jugadores;j++){
                for(int i=0;i<balas.cantidadBalas;i++){
                    if(balas.bala[j][i][0]!=0){
                        mapI[balas.bala[j][i][2]][balas.bala[j][i][1]]=-1;//si es diferente, se actualiza el tile...por eso
                        if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                            int index=0;
                            switch(j){
                                case 0:
                                    index=15;
                                    break;
                                case 1:
                                    index=16;
                                    break;
                                case 2:
                                    index=20;
                                    break;
                                case 3:
                                    index=21;
                                    break;
                            }
                            if(Juego.tetrisSkin==true){
                                index=24;
                            }
                            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                            g.drawImage(tile,
                            3*Tile.TILE_WIDTH+(balas.bala[j][i][1])*Tile.TILE_WIDTH,
                            (balas.bala[j][i][2])*Tile.TILE_HEIGHT,
                            3*Tile.TILE_WIDTH+((balas.bala[j][i][1])*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                            ((balas.bala[j][i][2])*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                            corX*Tile.TILE_WIDTH,
                            corY*Tile.TILE_HEIGHT,
                            (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                            (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                            null);
                        }else{
                            //g.drawImage(bala,3*Tile.TILE_WIDTH+(balas.bala[j][i][1])*Tile.TILE_WIDTH,(balas.bala[j][i][2])*Tile.TILE_HEIGHT,null);
                            int index=0;
                            if(balas.bala[j][i][0]==2){
                                index=31;
                            }else{//direccion=1(izq).
                                index=41;
                            }
                            if(Juego.tetrisSkin==true){
                                index=24;
                            }
                            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                            g.drawImage(tile,
                            3*Tile.TILE_WIDTH+(balas.bala[j][i][1])*Tile.TILE_WIDTH,
                            (balas.bala[j][i][2])*Tile.TILE_HEIGHT,
                            3*Tile.TILE_WIDTH+((balas.bala[j][i][1])*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                            ((balas.bala[j][i][2])*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                            corX*Tile.TILE_WIDTH,
                            corY*Tile.TILE_HEIGHT,
                            (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                            (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                            null);
                        }
                    }
                }
            }

            for(int i=0;i<balas.cantidadBalasEnemigas;i++){
                if(balas.balaEnemiga[i][0]!=0){
                    g.drawImage(bala2,3*Tile.TILE_WIDTH+(balas.balaEnemiga[i][1])*Tile.TILE_WIDTH,(balas.balaEnemiga[i][2])*Tile.TILE_HEIGHT,null);
                    mapI[balas.balaEnemiga[i][2]][balas.balaEnemiga[i][1]]=-1;//si es diferente, se actualiza el tile...por eso
                    int index=0;
                    if(balas.balaEnemiga[i][0]==2){
                        index=31;
                    }else{//direccion=1(izq).
                        index=41;
                    }
                    if(Juego.tetrisSkin==true){
                        index=24;
                    }
                    int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                    int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                    g.drawImage(tile,
                    3*Tile.TILE_WIDTH+(balas.balaEnemiga[i][1])*Tile.TILE_WIDTH,
                    (balas.balaEnemiga[i][2])*Tile.TILE_HEIGHT,
                    3*Tile.TILE_WIDTH+((balas.balaEnemiga[i][1])*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                    ((balas.balaEnemiga[i][2])*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                    corX*Tile.TILE_WIDTH,
                    corY*Tile.TILE_HEIGHT,
                    (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                    (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                    null);
                }
            }
        }
        
        public void DibujarBloques(Graphics g){//ahora se puede usar con el renderizado inteligente XD
            for(int j=0;j<Teclas.jugadores;j++){
                for(int i=0;i<balas.cantidadBloques;i++){
                    if(balas.bloque[j][i][0]!=0){
                        for(int n=0;n<balas.bloque[j][i][3];n++){
                            int naux=n;
                            if(balas.bloque[j][i][0]==2){
                                naux=-1*n;
                            }
                            mapI[balas.bloque[j][i][2]][balas.bloque[j][i][1]+naux]=-1;//si es diferente, se actualiza el tile...por eso
                            if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                                int index=0;
                                switch(j){
                                    case 0:
                                        index=15;
                                        break;
                                    case 1:
                                        index=16;
                                        break;
                                    case 2:
                                        index=20;
                                        break;
                                    case 3:
                                        index=21;
                                        break;
                                }
                                if(Juego.tetrisSkin==true){
                                    index=24;
                                }
                                int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                                g.drawImage(tile,
                                3*Tile.TILE_WIDTH+(balas.bloque[j][i][1]+naux)*Tile.TILE_WIDTH,
                                (balas.bloque[j][i][2])*Tile.TILE_HEIGHT,
                                3*Tile.TILE_WIDTH+((balas.bloque[j][i][1]+naux)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                ((balas.bloque[j][i][2])*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                corX*Tile.TILE_WIDTH,
                                corY*Tile.TILE_HEIGHT,
                                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                null);
                            }else{
                                //g.drawImage(bala,3*Tile.TILE_WIDTH+(balas.bloque[j][i][1]+naux)*Tile.TILE_WIDTH,(balas.bloque[j][i][2])*Tile.TILE_HEIGHT,null);
                                int index=0;
                                if(balas.bloque[j][i][0]==2){
                                    index=31;
                                }else{//direccion=1(izq).
                                    index=41;
                                }
                                if(Juego.tetrisSkin==true){
                                    index=24;
                                }
                                int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                                g.drawImage(tile,
                                3*Tile.TILE_WIDTH+(balas.bloque[j][i][1]+naux)*Tile.TILE_WIDTH,
                                (balas.bloque[j][i][2])*Tile.TILE_HEIGHT,
                                3*Tile.TILE_WIDTH+((balas.bloque[j][i][1]+naux)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                ((balas.bloque[j][i][2])*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                corX*Tile.TILE_WIDTH,
                                corY*Tile.TILE_HEIGHT,
                                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                null);
                            }
                        }
                    }
                }
            }
        }
        
        public void DibujarBalasCamara(Graphics g){
		for(int j=0;j<Teclas.jugadores;j++){
                    int sumaX=0;
                    int sumaY=0;
                    switch(j){
                        case 0:
                            sumaX=0;
                            sumaY=0;
                            break;
                        case 1:
                            sumaX=Camara.anchoCamara+3;
                            sumaY=0;
                            break;
                        case 2:
                            sumaX=0;
                            sumaY=Camara.altoCamara+1;
                            break;
                        case 3:
                            sumaX=Camara.anchoCamara+3;
                            sumaY=Camara.altoCamara+1;
                            break;

                    }
                    for(int i=0;i<balas.cantidadBalas;i++){
                        
                        for(int k=0;k<Teclas.jugadores;k++){
                            int sumaX2=0;
                            int sumaY2=0;
                            switch(k){
                                case 0:
                                    sumaX2=0;
                                    sumaY2=0;
                                    break;
                                case 1:
                                    sumaX2=Camara.anchoCamara+3;
                                    sumaY2=0;
                                    break;
                                case 2:
                                    sumaX2=0;
                                    sumaY2=Camara.altoCamara+1;
                                    break;
                                case 3:
                                    sumaX2=Camara.anchoCamara+3;
                                    sumaY2=Camara.altoCamara+1;
                                    break;

                            }
                            if(balaEnCamara(k,j,i)==true){
                                if(balas.bala[j][i][0]!=0){
                                    if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                                        int index=0;
                                        switch(j){
                                            case 0:
                                                index=15;
                                                break;
                                            case 1:
                                                index=16;
                                                break;
                                            case 2:
                                                index=20;
                                                break;
                                            case 3:
                                                index=21;
                                                break;
                                        }
                                        if(Juego.tetrisSkin==true){
                                            index=24;
                                        }
                                        int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                        int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                                        g.drawImage(tile,
                                        3*Tile.TILE_WIDTH+(balas.bala[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH,
                                        (balas.bala[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT,
                                        3*Tile.TILE_WIDTH+((balas.bala[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                        ((balas.bala[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                        corX*Tile.TILE_WIDTH,
                                        corY*Tile.TILE_HEIGHT,
                                        (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                        (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                        null);

                                    }else{
                                        if(Tile.TILE_WIDTH==16){
                                            int index=0;
                                            if(balas.bala[j][i][0]==2){
                                                index=31;
                                            }else{//direccion=1(izq).
                                                index=41;
                                            }
                                            if(Juego.tetrisSkin==true){
                                                index=24;
                                            }
                                            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                                            
                                            g.drawImage(tile,
                                            3*Tile.TILE_WIDTH+(balas.bala[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH,
                                            (balas.bala[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT,
                                            3*Tile.TILE_WIDTH+((balas.bala[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                            ((balas.bala[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                            corX*Tile.TILE_WIDTH,
                                            corY*Tile.TILE_HEIGHT,
                                            (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                            (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                            null);
                                            
                                        }else{
                                            g.drawImage(bala,3*Tile.TILE_WIDTH+(balas.bala[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH,(balas.bala[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT,null);
                                        }
                                        
                                    }
                                }
                            }
                        }
                    }
                    for(int i=0;i<balas.cantidadBalasEnemigas;i++){
                        if(balaEnemigaEnCamara(j,i)==true){
                            if(balas.balaEnemiga[i][0]!=0){
                                int index=0;
                                if(balas.bala[j][i][0]==2){
                                    index=31;
                                }else{//direccion=1(izq).
                                    index=41;
                                }
                                if(Juego.tetrisSkin==true){
                                    index=24;
                                }
                                int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                                g.drawImage(tile,
                                3*Tile.TILE_WIDTH+(balas.balaEnemiga[i][1]-Camara.posCamX[j]+sumaX)*Tile.TILE_WIDTH,
                                (balas.balaEnemiga[i][2]-Camara.posCamY[j]+sumaY+1)*Tile.TILE_HEIGHT,
                                3*Tile.TILE_WIDTH+(balas.balaEnemiga[i][1]-Camara.posCamX[j]+sumaX+1)*Tile.TILE_WIDTH,
                                (balas.balaEnemiga[i][2]-Camara.posCamY[j]+sumaY)*Tile.TILE_HEIGHT,
                                corX*Tile.TILE_WIDTH,
                                corY*Tile.TILE_HEIGHT,
                                (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                null);
                            }
                        }
                    }
		}
        }
        
        public void DibujarPelotasCamara(Graphics g){
		for(int j=0;j<Teclas.jugadores;j++){
                    int sumaX=0;
                    int sumaY=0;
                    switch(j){
                        case 0:
                            sumaX=0;
                            sumaY=0;
                            break;
                        case 1:
                            sumaX=Camara.anchoCamara+3;
                            sumaY=0;
                            break;
                        case 2:
                            sumaX=0;
                            sumaY=Camara.altoCamara+1;
                            break;
                        case 3:
                            sumaX=Camara.anchoCamara+3;
                            sumaY=Camara.altoCamara+1;
                            break;

                    }
                    for(int i=0;i<balas.cantidadPelotas;i++){
                        
                        for(int k=0;k<Teclas.jugadores;k++){
                            int sumaX2=0;
                            int sumaY2=0;
                            switch(k){
                                case 0:
                                    sumaX2=0;
                                    sumaY2=0;
                                    break;
                                case 1:
                                    sumaX2=Camara.anchoCamara+3;
                                    sumaY2=0;
                                    break;
                                case 2:
                                    sumaX2=0;
                                    sumaY2=Camara.altoCamara+1;
                                    break;
                                case 3:
                                    sumaX2=Camara.anchoCamara+3;
                                    sumaY2=Camara.altoCamara+1;
                                    break;

                            }
                            if(pelotaEnCamara(k,j,i)==true){
                                if(balas.pelota[j][i][0]!=0){
                                    if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                                        int index=0;
                                        switch(j){
                                            case 0:
                                                index=15;
                                                break;
                                            case 1:
                                                index=16;
                                                break;
                                            case 2:
                                                index=20;
                                                break;
                                            case 3:
                                                index=21;
                                                break;
                                        }
                                        if(Juego.tetrisSkin==true){
                                            index=24;
                                        }
                                        int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                        int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                                        g.drawImage(tile,
                                        3*Tile.TILE_WIDTH+(balas.pelota[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH,
                                        (balas.pelota[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT,
                                        3*Tile.TILE_WIDTH+((balas.pelota[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                        ((balas.pelota[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                        corX*Tile.TILE_WIDTH,
                                        corY*Tile.TILE_HEIGHT,
                                        (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                        (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                        null);

                                    }else{
                                        if(Tile.TILE_WIDTH==16){
                                            int index=0;
                                            if(balas.pelota[j][i][4]==1 || balas.pelota[j][i][4]==0){//segun la fuerza.
                                                index=26;
                                            }else{
                                                if(balas.pelota[j][i][4]==2){
                                                    index=27;
                                                }else{
                                                    index=28;
                                                }
                                            }
                                            if(Juego.tetrisSkin==true){
                                                index=24;
                                            }
                                            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                                            
                                            g.drawImage(tile,
                                            3*Tile.TILE_WIDTH+(balas.pelota[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH,
                                            (balas.pelota[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT,
                                            3*Tile.TILE_WIDTH+((balas.pelota[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                            ((balas.pelota[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                            corX*Tile.TILE_WIDTH,
                                            corY*Tile.TILE_HEIGHT,
                                            (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                            (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                            null);
                                            
                                        }else{
                                            g.drawImage(bala,3*Tile.TILE_WIDTH+(balas.pelota[j][i][1]-Camara.posCamX[k]+sumaX2)*Tile.TILE_WIDTH,(balas.pelota[j][i][2]-Camara.posCamY[k]+sumaY2)*Tile.TILE_HEIGHT,null);
                                        }
                                        
                                    }
                                }
                            }
                        }
                    }
		}
                
                
        }
        
        public void DibujarPelotas(Graphics g){//ahora se puede usar con el renderizado inteligente XD
            
		for(int j=0;j<Teclas.jugadores;j++){
			for(int i=0;i<balas.cantidadBalas;i++){
				if(balas.pelota[j][i][0]!=0){
                                        mapI[balas.pelota[j][i][2]][balas.pelota[j][i][1]]=-1;//si es diferente, se actualiza el tile...por eso
                                        if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                                            int index=0;
                                            switch(j){
                                                case 0:
                                                    index=15;
                                                    break;
                                                case 1:
                                                    index=16;
                                                    break;
                                                case 2:
                                                    index=20;
                                                    break;
                                                case 3:
                                                    index=21;
                                                    break;
                                            }
                                            if(Juego.tetrisSkin==true){
                                                index=24;
                                            }
                                            
                                            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));

                                            g.drawImage(tile,
                                            3*Tile.TILE_WIDTH+(balas.pelota[j][i][1])*Tile.TILE_WIDTH,
                                            (balas.pelota[j][i][2])*Tile.TILE_HEIGHT,
                                            3*Tile.TILE_WIDTH+((balas.pelota[j][i][1])*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                            ((balas.pelota[j][i][2])*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                            corX*Tile.TILE_WIDTH,
                                            corY*Tile.TILE_HEIGHT,
                                            (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                            (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                            null);
                                        }else{
                                            int index=0;
                                            if(balas.pelota[j][i][4]==1 || balas.pelota[j][i][4]==0){//segun la fuerza.
                                                index=26;
                                            }else{
                                                if(balas.pelota[j][i][4]==2){
                                                    index=27;
                                                }else{
                                                    index=28;
                                                }
                                            }
                                            if(Juego.tetrisSkin==true){
                                                index=24;
                                            }
                                            int corX=((index)%(tile.getWidth() / Tile.TILE_WIDTH));
                                            int corY=((index)/(tile.getWidth() / Tile.TILE_WIDTH));
                                            g.drawImage(tile,
                                            3*Tile.TILE_WIDTH+(balas.pelota[j][i][1])*Tile.TILE_WIDTH,
                                            (balas.pelota[j][i][2])*Tile.TILE_HEIGHT,
                                            3*Tile.TILE_WIDTH+((balas.pelota[j][i][1])*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                            ((balas.pelota[j][i][2])*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                            corX*Tile.TILE_WIDTH,
                                            corY*Tile.TILE_HEIGHT,
                                            (corX*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                            (corY*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                            null);
                                        }
				}
			}
		}
        }
        
        public boolean balaEnemigaEnCamara(int jugador, int nbala){
            
            if(balas.balaEnemiga[nbala][1]>=Camara.posCamX[jugador] && balas.balaEnemiga[nbala][1]<Camara.posCamX[jugador]+Camara.anchoCamara && balas.balaEnemiga[nbala][2]>=Camara.posCamY[jugador] && balas.balaEnemiga[nbala][2]<Camara.posCamY[jugador]+Camara.altoCamara){
                return true;
            }
            
            return false;
        }
        public boolean balaEnCamara(int jugcam, int jugador, int nbala){
            
            if(balas.bala[jugador][nbala][1]>=Camara.posCamX[jugcam] && balas.bala[jugador][nbala][1]<Camara.posCamX[jugcam]+Camara.anchoCamara && balas.bala[jugador][nbala][2]>=Camara.posCamY[jugcam] && balas.bala[jugador][nbala][2]<Camara.posCamY[jugcam]+Camara.altoCamara){
                return true;
            }
            
            return false;
        }
        public boolean pelotaEnCamara(int jugcam, int jugador, int nbala){
            
            if(balas.pelota[jugador][nbala][1]>=Camara.posCamX[jugcam] && balas.pelota[jugador][nbala][1]<Camara.posCamX[jugcam]+Camara.anchoCamara && balas.pelota[jugador][nbala][2]>=Camara.posCamY[jugcam] && balas.pelota[jugador][nbala][2]<Camara.posCamY[jugcam]+Camara.altoCamara){
                return true;
            }
            
            return false;
        }
        
	public void DibujarNumeros(Graphics g){
		for(int k=0;k<Teclas.jugadores;k++){
			for(int i=0;i<5;i++){
				for(int j=0;j<3;j++){
					if(personaje.numeros[Personaje.puntos[k]][i][j]==1){
                                                //int index1=personaje.puntos[0]+1;
                                                //int index2=personaje.puntos[1]+1;
                                                int posicionX=0;
                                                switch(k){
                                                    case 0:
                                                        posicionX=3;
                                                        break;
                                                    case 1:
                                                        posicionX=Juego.ancho-6;
                                                        break;
                                                    case 2:
                                                        posicionX=13;
                                                        break;
                                                    case 3:
                                                        posicionX=Juego.ancho-16;
                                                        break;
                                                    
                                                }
                                            
                                                int index1=(k*2)+3;
                                                
                                                if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                                                    switch(k){
                                                        case 0:
                                                            index1=3;
                                                            break;
                                                        case 1:
                                                            index1=4;
                                                            break;
                                                        case 2:
                                                            index1=8;
                                                            break;
                                                        case 3:
                                                            index1=9;
                                                            break;
                                                    }
                                                }
                                                
                                                int corX1=((index1)%(tile.getWidth() / Tile.TILE_WIDTH));
                                                int corY1=((index1)/(tile.getWidth() / Tile.TILE_WIDTH));
                                                
                                                
						g.drawImage(tile,
                                                3*Tile.TILE_WIDTH+(j+posicionX)*Tile.TILE_WIDTH,
                                                (i+31)*Tile.TILE_HEIGHT,
                                                3*Tile.TILE_WIDTH+((j+posicionX)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                                ((i+31)*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                                corX1*Tile.TILE_WIDTH,
                                                corY1*Tile.TILE_HEIGHT,
                                                (corX1*Tile.TILE_WIDTH)+Tile.TILE_WIDTH,
                                                (corY1*Tile.TILE_HEIGHT)+Tile.TILE_HEIGHT,
                                                null);
                                                
                                                mapI[i+31][j+posicionX]=-1;
					}
				}
			}
		}
	}
	
	public void ActualizarLava(int indice){//para transformar los 5 en 4 y limpiar la matriz de cada lago de lava respecto al indice.
		//boolean floto=false;
		if(demora[indice]!=0){
			for(int i=39;i>=0;i--){
				for(int j=0;j<40;j++){
					if(map[i][j]==6 || map[i][j]==7){
						//lava[i][j]=indice;//para que funcione el metodo flotarObjetos.
						if(lava[i][j]==indice){
							map[i][j]=4;
						}
					}
					revisado[i][j]=false;
					//if(lava[i][j]==indice){
					//	lava[i][j]=10;
					//}
				}
			}
			demora[indice]=0;
		}
	}
	
	public void LagosDeLava(int indice){//crecimiento recursivo.
		
		
		for(int i=39;i>=0;i--){
			for(int j=0;j<40;j++){
				if(lava[i][j]==indice){
					CrecerLava(i,j,indice);
					j=40;
					i=-1;
				}
			}
		}
		
	}
	
	public void CrecerLava(int i, int j, int indice){//calcula donde debe crecer.
		
		if(lava[i][j]!=10 && lava[i][j]!=indice && buscandoLava==false){
			//eliminarLava=lava[i][j];
			buscarLagos();//actualiza los lagos de lava.
		}
		if(buscandoLava==true){
			map[i][j]=4;
			lava[i][j]=10;
			if(map[i+1][j]>=4 && map[i+1][j]<=7){
				map[i+1][j]=4;
			}
			if(map[i-1][j]>=4 && map[i-1][j]<=7){
				map[i-1][j]=4;
			}
			if(map[i][j+1]>=4 && map[i][j+1]<=7){
				map[i][j+1]=4;
			}
			if(map[i][j-1]>=4 && map[i][j-1]<=7){
				map[i][j-1]=4;
			}
		}
		//System.out.println(map[i][j+1]+"-"+i+"-"+(j+1));
		lava[i][j]=indice;//indice=ya reviso este cuadrado para el correspondiente lago de lava.
		revisado[i][j]=true;
		
		if(i<39 && map[i+1][j]==2) map[i+1][j]=4;//la lava normal se transforma en lava que crece.
		if(i<39 && (map[i+1][j]==4 || map[i+1][j]==0) && revisado[i+1][j]==false){//abajo.
			if(map[i+1][j]==0){
				map[i][j]=5;
			}else{
				CrecerLava(i+1,j,indice);
			}
		}
		if(j>0 && map[i][j-1]==2) map[i][j-1]=4;
		if(j>0 && (map[i][j-1]==4 || map[i][j-1]==0) && revisado[i][j-1]==false){//izquierda.
			if(map[i][j-1]==0){
				map[i][j]=5;
			}else{
				CrecerLava(i,j-1,indice);
			}
		}
		if(j<39 && map[i][j+1]==2) map[i][j+1]=4;
		if(j<39 && (map[i][j+1]==4 || map[i][j+1]==0) && revisado[i][j+1]==false){//derecha.
			if(map[i][j+1]==0){
				map[i][j]=5;
			}else{
				CrecerLava(i,j+1,indice);
			}
		}
		if(i>0 && map[i-1][j]==2) map[i-1][j]=4;
		if(i>0 && (map[i-1][j]==4 || map[i-1][j]==0) && revisado[i-1][j]==false){//arriba.
			if(map[i-1][j]==0){
				map[i][j]=5;
			}else{
				CrecerLava(i-1,j,indice);
			}
		}
		/*
		if(altura[indice]!=0 && i<altura[indice]){// a mas altura es mas abajo en el mapa (y esta al reves).
			map[i][j]=4;
		}else{
			if(map[i][j]==5){
				altura[indice]=i;
				//System.out.println(altura[indice]);
			}
		}
		*/
	}
	
	public void CrecerLava2(int indice){//aumenta la lava donde debe y calcula la demora, ademas de devolver la lava a 4.
		boolean encontro;
		altura[indice]=1;//se resetea el valor de la altura donde sube la lava a 1 por si se le resta para que no sea -1 como indice.
		for(int i=0;i<40;i++){
			encontro=false;
			sube[indice]=true;
			
			for(int j=0;j<40;j++){
				
				if(map[i][j]==5){
					if(lava[i][j]==indice){
						
						map[i][j]=4;
						
						encontro=true;
						
						if(i<39 && map[i+1][j]==0){//abajo.
							map[i+1][j]=6;
							lava[i+1][j]=indice;
							demora[indice]++;
							sube[indice]=false;
						}else{
							//izquierda o derecha:
							if((j>0 && map[i][j-1]==0) || (j<39 && map[i][j+1]==0)){
								if(j>0 && map[i][j-1]==0){//izquierda.
									map[i][j-1]=6;
									if(map[i+1][j]==1){
										map[i][j-1]=7;//si hay un bloque debajo avanza de todas formas.
									}
									lava[i][j-1]=indice;
									demora[indice]++;
									sube[indice]=false;
								}
								if(j<39 && map[i][j+1]==0){//derecha.
									map[i][j+1]=6;
									if(map[i+1][j]==1){
										map[i][j+1]=7;//si hay un bloque debajo avanza de todas formas.
										//j++;//para que no avance todos los que pueda hacia la derecha.
									}
									lava[i][j+1]=indice;
									demora[indice]++;
									sube[indice]=false;
								}
							}else{
								if(i>0 && (map[i-1][j]==0)){// || (objetos[i-1][j]!=0 && puedeFlotar[objetos[i-1][j]]==true))){// arriba (si hay un objeto la lava pasa para hacer que flote).
									
									if(objetos[i-1][j]!=0){
										lava[i-1][j]=indice;
									}else{
										lava[i-1][j]=indice;
										map[i-1][j]=6;
									}
									
									demora[indice]++;
								}
							}
						}
					}
				}
				
				
				
				/*
				if(map[altura[indice]][j]==5){
					if(lava[altura[indice]][j]==indice){
						map[altura[indice]][j]=4;
						if(altura[indice]<39 && map[altura[indice]+1][j]==0){//abajo.
							map[altura[indice]+1][j]=5;
							lava[altura[indice]+1][j]=indice;
							demora[indice]++;
						}else{
							//izquierda o derecha:
							if((j>0 && map[altura[indice]][j-1]==0) || (j<39 && map[altura[indice]][j+1]==0)){
								if(j>0 && map[altura[indice]][j-1]==0){//izquierda.
									map[altura[indice]][j-1]=5;
									lava[altura[indice]][j-1]=indice;
									demora[indice]++;
								}
								if(j<39 && map[altura[indice]][j+1]==0){//derecha.
									map[altura[indice]][j+1]=5;
									lava[altura[indice]][j+1]=indice;
									demora[indice]++;
								}
							}else{
								if(altura[indice]>0 && map[altura[indice]-1][j]==0){//arriba.
									map[altura[indice]-1][j]=5;
									lava[altura[indice]-1][j]=indice;
									demora[indice]++;
								}
							}
						}
					}
				}
				*/
			}
			if(sube[indice]==false){
				for(int j=0;j<40;j++){
					if(map[i-1][j]==6 && lava[i-1][j]==indice){
						map[i-1][j]=0;
						lava[i-1][j]=10;
						demora[indice]--;
					}
				}
			}else{
				if(encontro==true){
					altura[indice]=i;//altura donde sube la lava.
				}
			}
		}
	}
	/*
	public void CrecerLava1(){
		if(demora!=0){//por si ya crecio al maximo.
			demora=0;
			boolean paso1=true;
			for(int i=39;i>=0;i--){
				boolean paso2=true;
				for(int j=0;j<40;j++){
					if(map[i][j]==4){
						if(i<39 && map[i+1][j]!=1 && map[i+1][j]!=4 && map[i+1][j]!=7){
							map[i+1][j]=5;
							demora++;
							paso1=false;
						}else{
							if(j>0 && map[i][j-1]!=1 && map[i][j-1]!=4 && map[i][j-1]!=7){
								paso1=false;
								paso2=false;
								map[i][j-1]=5;
								demora++;
							}
							if(j<39 && map[i][j+1]!=1 && map[i][j+1]!=4 && map[i][j+1]!=7){
								paso1=false;
								paso2=false;
								map[i][j+1]=5;
								demora++;
							}
						}
					}
				}
				if(paso2==true){
					for(int j=0;j<40;j++){
						if(map[i][j]==2){
							if(i>0 && map[i-1][j]!=1 && map[i-1][j]!=4 && map[i-1][j]!=6){
								map[i-1][j]=5;
								demora++;
								paso1=false;
							}
						}
					}
				}
				
				if(paso1==false){
					i=-1;
				}
			}
		}
	}
	*/
        
        //barra de vida horizontal:
	public void DibujarVida(Graphics g){
		int bv=4;
                bv=Tile.TILE_WIDTH/4;
                
                for(int j=0;j<Teclas.jugadores;j++){
                    
                    int limiteVida=act.vidaJugador[j];
                    
                    int puntos=personaje.vidas[j];
                    int extra=0;
                    int indiceAlterado=0;
                    int sumar=0;
                    
                    if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                        sumar=4;
                    }
                    
                    switch(j){
                        case 0:
                            extra=0;
                            indiceAlterado=0;
                            break;
                        case 1:
                            extra=1;
                            indiceAlterado=3;
                            break;
                        case 2:
                            extra=0;
                            indiceAlterado=1;
                            break;
                        case 3:
                            extra=1;
                            indiceAlterado=2;
                            break;
                    }
                    for(int i=0;i<limiteVida;i++){
                        if(i==0){
                            g.drawImage(barras2,
                            (10*(indiceAlterado)+5+extra)*Tile.TILE_HEIGHT-bv,
                            ((37)*Tile.TILE_WIDTH)-bv,
                            (10*(indiceAlterado)+5+extra)*Tile.TILE_HEIGHT,
                            ((37)*Tile.TILE_WIDTH)+5*bv,
                            0,
                            0,
                            bv,
                            6*bv,
                            null);
                            
                            g.drawImage(barras2,
                            (10*(indiceAlterado)+5+extra)*Tile.TILE_HEIGHT+((limiteVida)*bv),
                            ((37)*Tile.TILE_WIDTH)-bv,
                            (10*(indiceAlterado)+5+extra)*Tile.TILE_HEIGHT+((limiteVida)*bv)+bv,
                            ((37)*Tile.TILE_WIDTH)+5*bv,
                            0,
                            0,
                            bv,
                            6*bv,
                            null);
                        }
			if((i)<(puntos)){
                            g.drawImage(barras2, 
                            (10*(indiceAlterado)+5+extra)*Tile.TILE_HEIGHT+(i*bv),
                            ((37)*Tile.TILE_WIDTH)-bv,
                            (10*(indiceAlterado)+5+extra)*Tile.TILE_HEIGHT+(i*bv)+bv,
                            ((37)*Tile.TILE_WIDTH)+5*bv,
                            (j+2+sumar)*bv,//se le agrega para cambiar colores si es el modo camuflaje.
                            0,
                            (j+3+sumar)*bv,
                            6*bv,
                            null);
			}else{
                            g.drawImage(barras2, 
                            (10*(indiceAlterado)+5+extra)*Tile.TILE_HEIGHT+(i*bv),
                            ((37)*Tile.TILE_WIDTH)-bv,
                            (10*(indiceAlterado)+5+extra)*Tile.TILE_HEIGHT+(i*bv)+bv,
                            ((37)*Tile.TILE_WIDTH)+5*bv,
                            bv,
                            0,
                            2*bv,
                            6*bv,
                            null);
                        }
                    }
                }
            
	}
        public void DibujarSuperVida(Graphics g){
            int bv=Tile.TILE_WIDTH/4;
            int fuente=8;
            switch(Tile.TILE_WIDTH){
                case 4:
                    fuente=8;
                    break;
                case 8:
                    fuente=16;
                    break;
                case 16:
                    fuente=24;
                    break;
            }
            int sumar=0;
            if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                sumar=4;
            }
            if(Juego.camara){
                for(int j=0;j<Teclas.jugadores;j++){
                    int limiteVida=Juego.superJugador.get(j).healthLimit/Juego.superJugador.get(j).healthMultiplier;
                    int puntos=Juego.superJugador.get(j).health/Juego.superJugador.get(j).healthMultiplier;
                    int sumaX=0;
                    int sumaY=0;
                    switch(j){
                        case 0:
                            sumaX=3;
                            sumaY=0;
                            break;
                        case 1:
                            sumaX=Camara.anchoCamara+6;
                            sumaY=0;
                            break;
                        case 2:
                            sumaX=3;
                            sumaY=Camara.altoCamara+1;
                            break;
                        case 3:
                            sumaX=Camara.anchoCamara+6;
                            sumaY=Camara.altoCamara+1;
                            break;

                    }

                    for(int i=0;i<limiteVida;i++){
                        if(i==0){
                            g.drawImage(barras2,
                            ((1+sumaX)*Tile.TILE_WIDTH)-bv,
                            ((1+sumaY)*Tile.TILE_HEIGHT)-bv,
                            ((1+sumaX)*Tile.TILE_WIDTH),
                            ((2+sumaY)*Tile.TILE_HEIGHT)+bv,
                            0,
                            0,
                            bv,
                            6*bv,
                            null);

                            g.drawImage(barras2,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((limiteVida)*bv),
                            ((1+sumaY)*Tile.TILE_HEIGHT)-bv,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((limiteVida+1)*bv),
                            ((2+sumaY)*Tile.TILE_HEIGHT)+bv,
                            0,
                            0,
                            bv,
                            6*bv,
                            null);
                        }

                        if((i)<(puntos)){
                            g.drawImage(barras2,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((i)*bv),
                            ((1+sumaY)*Tile.TILE_HEIGHT)-bv,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((i+1)*bv),
                            ((2+sumaY)*Tile.TILE_HEIGHT)+bv,
                            (j+2+sumar)*bv,//se le agrega para cambiar colores si es el modo camuflaje.
                            0,
                            (j+3+sumar)*bv,
                            6*bv,
                            null);
                        }else{
                            g.drawImage(barras2,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((i)*bv),
                            ((1+sumaY)*Tile.TILE_HEIGHT)-bv,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((i+1)*bv),
                            ((2+sumaY)*Tile.TILE_HEIGHT)+bv,
                            bv,
                            0,
                            2*bv,
                            6*bv,
                            null);
                        }

                    }
                    
                    g.setColor(Color.black);
                    g.setFont(new Font("Showcard Gothic",1,fuente));
                    g.drawString(String.valueOf(Juego.superJugador.get(j).vidas), (((1+sumaX)*Tile.TILE_WIDTH)+((limiteVida+1)*bv)+Tile.TILE_WIDTH/2),((2+sumaY)*Tile.TILE_HEIGHT));
                }
            }else{
                
                for(int j=0;j<Teclas.jugadores;j++){
                    
                    int limiteVida=Juego.superJugador.get(j).healthLimit/Juego.superJugador.get(j).healthMultiplier;
                    int puntos=Juego.superJugador.get(j).health/Juego.superJugador.get(j).healthMultiplier;
                    
                    for(int i=0;i<limiteVida;i++){
                        if(i==0){
                            g.drawImage(barras,
                            ((1)*Tile.TILE_WIDTH)-bv,
                            (9*(j+1)+2)*Tile.TILE_HEIGHT,
                            (((1)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH)+bv,
                            ((9*(j+1)+2)*Tile.TILE_HEIGHT)+bv,
                            0,
                            0,
                            6*bv,
                            1*bv,
                            null);
                            
                            g.drawImage(barras,
                            ((1)*Tile.TILE_WIDTH)-bv,
                            (9*(j+1)+2)*Tile.TILE_HEIGHT-((limiteVida+1)*bv),
                            (((1)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH)+bv,
                            ((9*(j+1)+2)*Tile.TILE_HEIGHT)-((limiteVida-1)*bv),
                            0,
                            0,
                            6*bv,
                            1*bv,
                            null);
                        }
			if((i)<(puntos)){
                            g.drawImage(barras,
                            ((1)*Tile.TILE_WIDTH)-bv,
                            (9*(j+1)+2)*Tile.TILE_HEIGHT-((i+1)*bv),
                            (((1)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH)+bv,
                            ((9*(j+1)+2)*Tile.TILE_HEIGHT)-((i)*bv),
                            0,
                            (j+2)*bv,
                            6*bv,
                            (j+3)*bv,
                            null);
			}else{
                            g.drawImage(barras,
                            ((1)*Tile.TILE_WIDTH)-bv,
                            (9*(j+1)+2)*Tile.TILE_HEIGHT-((i+1)*bv),
                            (((1)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH)+bv,
                            ((9*(j+1)+2)*Tile.TILE_HEIGHT)-((i)*bv),
                            0,
                            1*bv,
                            6*bv,
                            2*bv,
                            null);
                        }
                    }
                    g.setColor(Color.white);
                    g.setFont(new Font("Showcard Gothic",1,fuente));
                    g.drawString(String.valueOf(Juego.superJugador.get(j).vidas), ((1)*Tile.TILE_WIDTH),(9*(j+1)+3)*Tile.TILE_HEIGHT+Tile.TILE_HEIGHT/2);
                }
            }
        }
        //barra de vida vertical
	public void DibujarVida2(Graphics g){
                int bv=4;
                bv=Tile.TILE_WIDTH/4;
                for(int j=0;j<Teclas.jugadores;j++){
                    
                    int limiteVida=act.vidaJugador[j];
                    
                    int puntos=personaje.vidas[j];
                    for(int i=0;i<limiteVida;i++){
                        if(i==0){
                            g.drawImage(barras,
                            ((1)*Tile.TILE_WIDTH)-bv,
                            (9*(j+1)+2)*Tile.TILE_HEIGHT,
                            (((1)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH)+bv,
                            ((9*(j+1)+2)*Tile.TILE_HEIGHT)+bv,
                            0,
                            0,
                            6*bv,
                            1*bv,
                            null);
                            
                            g.drawImage(barras,
                            ((1)*Tile.TILE_WIDTH)-bv,
                            (9*(j+1)+2)*Tile.TILE_HEIGHT-((limiteVida+1)*bv),
                            (((1)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH)+bv,
                            ((9*(j+1)+2)*Tile.TILE_HEIGHT)-((limiteVida-1)*bv),
                            0,
                            0,
                            6*bv,
                            1*bv,
                            null);
                        }
			if((i)<(puntos)){
                            g.drawImage(barras,
                            ((1)*Tile.TILE_WIDTH)-bv,
                            (9*(j+1)+2)*Tile.TILE_HEIGHT-((i+1)*bv),
                            (((1)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH)+bv,
                            ((9*(j+1)+2)*Tile.TILE_HEIGHT)-((i)*bv),
                            0,
                            (j+2)*bv,
                            6*bv,
                            (j+3)*bv,
                            null);
			}else{
                            g.drawImage(barras,
                            ((1)*Tile.TILE_WIDTH)-bv,
                            (9*(j+1)+2)*Tile.TILE_HEIGHT-((i+1)*bv),
                            (((1)*Tile.TILE_WIDTH)+Tile.TILE_WIDTH)+bv,
                            ((9*(j+1)+2)*Tile.TILE_HEIGHT)-((i)*bv),
                            0,
                            1*bv,
                            6*bv,
                            2*bv,
                            null);
                        }
                    }
                }
		
	}
        public void DibujarVidaHud(Graphics g){
                int bv=4;
                if(Tile.TILE_HEIGHT==16){
                    bv=4;
                }
                if(Tile.TILE_HEIGHT==8){
                    bv=2;
                }
                if(Tile.TILE_HEIGHT==4){
                    bv=1;
                }
                
                int sumar=0;
                if((Juego.nivelCamuflaje==true || Juego.nivel>=5) && Teclas.modoJuego==2){
                    sumar=4;
                }
                for(int j=0;j<Teclas.jugadores;j++){
                    int sumaX=0;
                    int sumaY=0;
                    switch(j){
                        case 0:
                            sumaX=3;
                            sumaY=0;
                            break;
                        case 1:
                            sumaX=Camara.anchoCamara+6;
                            sumaY=0;
                            break;
                        case 2:
                            sumaX=3;
                            sumaY=Camara.altoCamara+1;
                            break;
                        case 3:
                            sumaX=Camara.anchoCamara+6;
                            sumaY=Camara.altoCamara+1;
                            break;

                    }
                    
                    int limiteVida=act.vidaJugador[j];
                    
                    int puntos=personaje.vidas[j];
                    for(int i=0;i<limiteVida;i++){
                        if(i==0){
                            g.drawImage(barras2,
                            ((1+sumaX)*Tile.TILE_WIDTH)-bv,
                            ((1+sumaY)*Tile.TILE_HEIGHT)-bv,
                            ((1+sumaX)*Tile.TILE_WIDTH),
                            ((2+sumaY)*Tile.TILE_HEIGHT)+bv,
                            0,
                            0,
                            bv,
                            6*bv,
                            null);
                            
                            g.drawImage(barras2,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((limiteVida)*bv),
                            ((1+sumaY)*Tile.TILE_HEIGHT)-bv,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((limiteVida+1)*bv),
                            ((2+sumaY)*Tile.TILE_HEIGHT)+bv,
                            0,
                            0,
                            bv,
                            6*bv,
                            null);
                        }
                        
			if((i)<(puntos)){
                            g.drawImage(barras2,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((i)*bv),
                            ((1+sumaY)*Tile.TILE_HEIGHT)-bv,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((i+1)*bv),
                            ((2+sumaY)*Tile.TILE_HEIGHT)+bv,
                            (j+2+sumar)*bv,//se le agrega para cambiar colores si es el modo camuflaje.
                            0,
                            (j+3+sumar)*bv,
                            6*bv,
                            null);
			}else{
                            g.drawImage(barras2,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((i)*bv),
                            ((1+sumaY)*Tile.TILE_HEIGHT)-bv,
                            ((1+sumaX)*Tile.TILE_WIDTH)+((i+1)*bv),
                            ((2+sumaY)*Tile.TILE_HEIGHT)+bv,
                            bv,
                            0,
                            2*bv,
                            6*bv,
                            null);
                        }
                        
                    }
                }
		
	}
        public void DibujaEnemigo(int enemigo){
            if(this.enemigo.estaVivo[enemigo]==true){
		for(int i=0;i<4;i++){
			for(int j=0;j<3;j++){
				if(this.enemigo.enemigo[enemigo][i][j]==1){
					jugadores[i+this.enemigo.getY(enemigo)][j+this.enemigo.getX(enemigo)]=1;
                                        jugadoresDibujados[i+this.enemigo.getY(enemigo)][j+this.enemigo.getX(enemigo)]=8;//indice de los enemigos
				}
			}
		}
            }
        }
        public void borrarEnemigo(int enemigo){
            if(this.enemigo.estaVivo[enemigo]==true){
                for(int i=0;i<4;i++){
			for(int j=0;j<3;j++){
				if(this.enemigo.enemigo[enemigo][i][j]==1){
					jugadores[i+this.enemigo.getY(enemigo)][j+this.enemigo.getX(enemigo)]=0;
                                        jugadoresDibujados[i+this.enemigo.getY(enemigo)][j+this.enemigo.getX(enemigo)]=0;//indice de los enemigos
				}
			}
		}
            }
		
	}
	
	
}