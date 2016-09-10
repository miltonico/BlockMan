
package BlockMan;
public class Enemigo {
    //variables:
    
    public static int limiteVidaEnemigo=5;
    
    public static int[]contador=new int[50];
    public static int[]contador2=new int[50];
    public static int[]contadorQuema=new int[50];
    public static int[]accionAnterior=new int[50];
    public static int[]accionActual=new int[50];
    public static int[]marcaPasos=new int[50];
    public static int[]auxX=new int[50];
    public static int[]auxY=new int[50];
    
    public static boolean[]izquierda=new boolean[50];
    public static boolean[]derecha=new boolean[50];
    public static boolean[]estaCayendo=new boolean[50];
    public static boolean[]estaSaltando=new boolean[50];
    public static boolean[]disparo=new boolean[50];
    public static boolean[]quedoIgual=new boolean[50];
    
    public static int[]enemigoX=new int[50];
    public static int[]enemigoY=new int[50];
    public static int[]direccionEnemigo=new int[50];
    public static int[]vidaEnemigo=new int[50];
    public static int[][][] enemigo=new int[50][4][3];//(enemigos),(alto),(ancho).
    public static int[] accion=new int[50];
    public static int[] posIniX=new int[50];
    public static int[] posIniY=new int[50];
    public static int cantidadEnemigos=0;
    public static boolean[] estaVivo=new boolean[50];
    
    public void iniciar(){
        for(int i=0;i<50;i++){
            vidaEnemigo[i]=limiteVidaEnemigo;
            estaVivo[i]=true;
        }
    }
    
    public void animar(int nEnemigo, int accion){
    	switch(accion){
    	case 0://normal
    		enemigo[nEnemigo][0][0]=0;
    		enemigo[nEnemigo][0][1]=1;
    		enemigo[nEnemigo][0][2]=0;
    		enemigo[nEnemigo][1][0]=1;
    		enemigo[nEnemigo][1][1]=1;
    		enemigo[nEnemigo][1][2]=1;
    		enemigo[nEnemigo][2][0]=0;
    		enemigo[nEnemigo][2][1]=1;
    		enemigo[nEnemigo][2][2]=0;
    		enemigo[nEnemigo][3][0]=1;
    		enemigo[nEnemigo][3][1]=0;
    		enemigo[nEnemigo][3][2]=1;
    		break;
    	case 1://derecha
    		enemigo[nEnemigo][0][0]=0;
    		enemigo[nEnemigo][0][1]=1;
    		enemigo[nEnemigo][0][2]=0;
    		enemigo[nEnemigo][1][0]=0;
    		enemigo[nEnemigo][1][1]=1;
    		enemigo[nEnemigo][1][2]=1;
    		enemigo[nEnemigo][2][0]=0;
    		enemigo[nEnemigo][2][1]=1;
    		enemigo[nEnemigo][2][2]=0;
    		enemigo[nEnemigo][3][0]=0;
    		enemigo[nEnemigo][3][1]=1;
    		enemigo[nEnemigo][3][2]=0;
    		break;
    	case 2://salto derecha
    		enemigo[nEnemigo][0][0]=0;
    		enemigo[nEnemigo][0][1]=1;
    		enemigo[nEnemigo][0][2]=0;
    		enemigo[nEnemigo][1][0]=0;
    		enemigo[nEnemigo][1][1]=1;
    		enemigo[nEnemigo][1][2]=1;
    		enemigo[nEnemigo][2][0]=0;
    		enemigo[nEnemigo][2][1]=1;
    		enemigo[nEnemigo][2][2]=0;
    		enemigo[nEnemigo][3][0]=1;
    		enemigo[nEnemigo][3][1]=1;
    		enemigo[nEnemigo][3][2]=0;
    		break;
    	case 3://izquierda
    		enemigo[nEnemigo][0][0]=0;
    		enemigo[nEnemigo][0][1]=1;
    		enemigo[nEnemigo][0][2]=0;
    		enemigo[nEnemigo][1][0]=1;
    		enemigo[nEnemigo][1][1]=1;
    		enemigo[nEnemigo][1][2]=0;
    		enemigo[nEnemigo][2][0]=0;
    		enemigo[nEnemigo][2][1]=1;
    		enemigo[nEnemigo][2][2]=0;
    		enemigo[nEnemigo][3][0]=0;
    		enemigo[nEnemigo][3][1]=1;
    		enemigo[nEnemigo][3][2]=0;
    		break;
    	case 4://salto izquierda
    		enemigo[nEnemigo][0][0]=0;
    		enemigo[nEnemigo][0][1]=1;
    		enemigo[nEnemigo][0][2]=0;
    		enemigo[nEnemigo][1][0]=1;
    		enemigo[nEnemigo][1][1]=1;
    		enemigo[nEnemigo][1][2]=0;
    		enemigo[nEnemigo][2][0]=0;
    		enemigo[nEnemigo][2][1]=1;
    		enemigo[nEnemigo][2][2]=0;
    		enemigo[nEnemigo][3][0]=0;
    		enemigo[nEnemigo][3][1]=1;
    		enemigo[nEnemigo][3][2]=1;
    		break;
    	case 5://paleta(antiguo abajo)
    		enemigo[nEnemigo][0][0]=0;
    		enemigo[nEnemigo][0][1]=1;
    		enemigo[nEnemigo][0][2]=0;
    		enemigo[nEnemigo][1][0]=0;
    		enemigo[nEnemigo][1][1]=1;
    		enemigo[nEnemigo][1][2]=0;
    		enemigo[nEnemigo][2][0]=0;
    		enemigo[nEnemigo][2][1]=1;
    		enemigo[nEnemigo][2][2]=0;
    		enemigo[nEnemigo][3][0]=0;
    		enemigo[nEnemigo][3][1]=1;
    		enemigo[nEnemigo][3][2]=0;
    		break;
    	case 6://agacharse
    		enemigo[nEnemigo][0][0]=0;
    		enemigo[nEnemigo][0][1]=0;
    		enemigo[nEnemigo][0][2]=0;
    		enemigo[nEnemigo][1][0]=0;
    		enemigo[nEnemigo][1][1]=1;
    		enemigo[nEnemigo][1][2]=0;
    		enemigo[nEnemigo][2][0]=1;
    		enemigo[nEnemigo][2][1]=1;
    		enemigo[nEnemigo][2][2]=1;
    		enemigo[nEnemigo][3][0]=1;
    		enemigo[nEnemigo][3][1]=1;
    		enemigo[nEnemigo][3][2]=1;
    		break;
    	}
    }
    
    public int getAccion(int enemigo) // Obtenemos la coordenada horizontal actual del cuadrado
    {
        return accion[enemigo];
    }
	
	public void setAccion(int enemigo, int accion)//se determina la posicion o accion del enemigo
    {
        this.accion[enemigo]=accion;
    }
    public int getX(int jugador) // Obtenemos la coordenada horizontal actual del cuadrado
    {
        return enemigoX[jugador];
    }
 
    public void setX(int jugador, int valor) //Asignamos la coordenada horizontal actual del cuadrado
    {
    	enemigoX[jugador]=valor;
    }
 
    public int getY(int jugador) // Obtenemos la coordenada vertical actual del cuadrado
    {
        return enemigoY[jugador];
    }
 
    public void setY(int jugador, int valor) // Asignamos la coordenada vertical actual del cuadrado
    {
        enemigoY[jugador]=valor;
    }
}
