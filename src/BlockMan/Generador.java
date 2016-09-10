
package BlockMan;
public class Generador {
    public int x=0,y=0,tipo=0;//no es estatico para que puedan haber muchas instancias de esta clase...
    public Generador(int tipo,int x, int y){
        this.tipo=tipo;
        this.x=x;
        this.y=y;
    }
}
