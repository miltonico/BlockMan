

package BlockMan;

public class CheckPoint {
    public int posicionPuntoX=0,posicionPuntoY=0,indice=0;
    public CheckPoint(int indice,int x,int y){
        this.posicionPuntoX=x*Tile.TILE_WIDTH;
        this.posicionPuntoY=y*Tile.TILE_HEIGHT;
        this.indice=indice;
    }
    public void updateCheckPoint(){
        for(int i=0;i<Juego.superJugador.size();i++){
            if(Juego.superJugador.get(i).deadDimension==false){
                for(int j=1;j<Juego.superJugador.get(i).positionParts.length;j++){
                    if((Juego.superJugador.get(i).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(i).positionParts[j][0][1])/Tile.TILE_WIDTH==this.posicionPuntoX/Tile.TILE_WIDTH
                       && (Juego.superJugador.get(i).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(i).positionParts[j][1][1])/Tile.TILE_HEIGHT==this.posicionPuntoY/Tile.TILE_HEIGHT){
                        Juego.superJugador.get(i).checkPointIndex=this.indice;
                    }
                }
            }
        }
    }
}
