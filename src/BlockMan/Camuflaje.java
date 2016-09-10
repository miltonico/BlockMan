
package BlockMan;
public class Camuflaje {
    static int indices[]=new int[5];
    static int octavaPosicion=0;
    static int quintaPosicion=0;
    static int contadorInicio=0;
    static int contadorMover=0;
    static int mapaCamuflaje[][]=new int[40][40];
    static int movimientoActual=0;//hasta 7.
    
    public void iniciarMapa(){
        indices[0]=3+12;
        indices[1]=5+12;
        indices[2]=7+12;
        indices[3]=9+12;
        indices[4]=12;
        for(int i=0;i<(Juego.superJugador.size());i++){
            for(int j=0;j<(Math.floor((double)40/Juego.superJugador.size()));j++){
                for(int k=0;k<40;k++){
                    mapaCamuflaje[i*(int)((Math.floor((double)40/Juego.superJugador.size())))+j][k]=indices[i];
                }
            }
        }
        for(int j=0;j<(40%Juego.superJugador.size());j++){
            for(int k=0;k<40;k++){
                mapaCamuflaje[(40-(40%Juego.superJugador.size()))+j][k]=indices[4];
            }
        }
    }
    
    public void moverMapa(){
        int indiceFinal=0;
        if(contadorInicio>80){//cuando los jugadores llegan al suelo tienen un poco mas de tiempo.
            if(contadorMover==0){
                //mover mapa:
                for(int i=39;i>=0;i--){
                    for(int j=0;j<40;j++){
                        if(i==39) indiceFinal=mapaCamuflaje[i][j];
                        if(i==0){
                            mapaCamuflaje[i][j]=indiceFinal;
                        }else{
                            mapaCamuflaje[i][j]=mapaCamuflaje[i-1][j];
                        }
                    }
                }
            }
            contadorMover++;
            if(contadorMover==20){
                contadorMover=0;
            }
                    
        }else{
            contadorInicio++;
        }
    }
}
