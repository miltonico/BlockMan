
package BlockMan;
public class Actualizaciones {
    public static int vidasJugadorA=100,vidasJugadorB=3,limiteVida=16,limiteBalas=20,limiteSalto=5,limiteArmas=5,tamañoCartuchoPelotas=5,tamañoCartuchoMisiles=2,
    retardoBalas=16,retardoPelotas=16,retardoMisiles=32,curacionCapsulas=4,mejoraDeVida=2,vidaMaxima=32;//limite balas tambien esta creada en Balas
    
    public static int[] balasJugador=new int[4];//limite
    public static int[] misilesJugador=new int[4];//limite
    public static int[] armaActual=new int[4];//arma seleccionada
    public static int[] checkJugador=new int[4];//indice del checkpoint de cada jugador.
    
    public static int[] saltoJugador=new int[4];//se puede mejorar con items
    public static int[] vidaJugador=new int[4];//se puede mejorar con items
    public static int[] municionPelotas=new int[4];//se puede mejorar con items
    public static int[] municionMisiles=new int[4];//se puede mejorar con items
    public static int[] velocidadAtaque=new int[4];//se puede mejorar con items
    public static boolean[] balasHielo=new boolean[4];//se puede mejorar con items
    
    public void iniciar(){
        
        if(Teclas.modoJuego==1){
            limiteVida=16;
        }else{
            limiteVida=20;
        }
        
        municionPelotas[0]=0;
        municionPelotas[1]=0;
        municionPelotas[2]=0;
        municionPelotas[3]=0;
        
        armaActual[0]=1;
        armaActual[1]=1;
        armaActual[2]=1;
        armaActual[3]=1;
        
        vidaJugador[0]=limiteVida;
        vidaJugador[1]=limiteVida;
        vidaJugador[2]=limiteVida;
        vidaJugador[3]=limiteVida;

        balasJugador[0]=limiteBalas;
        balasJugador[1]=limiteBalas;
        balasJugador[2]=limiteBalas;
        balasJugador[3]=limiteBalas;

//        misilesJugador[0]=0;
//        misilesJugador[1]=0;
//        misilesJugador[2]=0;
//        misilesJugador[3]=0;

        saltoJugador[0]=limiteSalto;
        saltoJugador[1]=limiteSalto;
        saltoJugador[2]=limiteSalto;
        saltoJugador[3]=limiteSalto;
        
        municionMisiles[0]=0;
        municionMisiles[1]=0;
        municionMisiles[2]=0;
        municionMisiles[3]=0;
        
        velocidadAtaque[0]=1;
        velocidadAtaque[1]=1;
        velocidadAtaque[2]=1;
        velocidadAtaque[3]=1;
        
        balasHielo[0]=false;
        balasHielo[1]=false;
        balasHielo[2]=false;
        balasHielo[3]=false;
        
        
    }
    public void iniciarNivel(){
        checkJugador[0]=-1;
        checkJugador[1]=-1;
        checkJugador[2]=-1;
        checkJugador[3]=-1;
    }
}
