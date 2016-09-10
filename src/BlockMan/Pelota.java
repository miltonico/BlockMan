/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BlockMan;

/**
 *
 * @author TIMBERWOLF
 */
public class Pelota {
    
    public int x,y,inicioX,inicioY,dx,dy,limite,jugador,subX,subY,seccionador;
    public boolean dirigida,impulso;//dirigida: sigue al jugador,impulso: el jugador la potencia con solo tocarla.
    
    public static Tile tile=new Tile();
    
    public Pelota(int x, int y, int jugador){
        this.inicioX=x*tile.TILE_WIDTH;
        this.inicioY=y*tile.TILE_HEIGHT;
        this.seccionador=10;
        this.x=x*tile.TILE_WIDTH;
        this.y=y*tile.TILE_HEIGHT;
        this.subX=x*tile.TILE_WIDTH*seccionador;
        this.subY=y*tile.TILE_HEIGHT*seccionador;
        this.dx=0;
        this.dy=0;
        this.limite=8*seccionador;
        this.jugador=jugador;//0 a 3.
        this.dirigida=true;
        this.impulso=false;
    }
    
    public void actualizarPelota(){
        if(this.x+tile.TILE_WIDTH>Juego.ancho*tile.TILE_WIDTH){
            this.x=Juego.ancho*tile.TILE_WIDTH-tile.TILE_WIDTH;
            this.subX=(Juego.ancho*tile.TILE_WIDTH-tile.TILE_WIDTH)*seccionador;
            this.dx=-dx;
        }
        if(this.y+tile.TILE_HEIGHT>Juego.alto*tile.TILE_HEIGHT){
            this.y=Juego.alto*tile.TILE_HEIGHT-tile.TILE_HEIGHT;
            this.subY=(Juego.alto*tile.TILE_HEIGHT-tile.TILE_HEIGHT)*seccionador;
            this.dy=-dy;
        }
        if(this.x<0){
            this.x=0;
            this.subX=0;
            this.dx=-dx;
        }
        if(this.y<0){
            this.y=0;
            this.subY=0;
            this.dy=-dy;
        }
        
        //posicion aproximada.
        int quad_x=(int)Math.floor((this.x+(tile.TILE_WIDTH/2))/tile.TILE_WIDTH);
        int quad_y=(int)Math.floor((this.y+(tile.TILE_HEIGHT/2))/tile.TILE_HEIGHT);
        
        //System.out.println("qx="+(((double)this.x+((double)tile.TILE_WIDTH/(double)2))/(double)tile.TILE_WIDTH-(double)1));
        //System.out.println("qy="+(((double)this.y+((double)tile.TILE_HEIGHT/(double)2))/(double)tile.TILE_HEIGHT-(double)1));
        
        int direccion_x=0;
        int direccion_y=0;
        
        //colisiones.
        int[][] espacios=new int[3][3];//y,x
        
        espacios[0][0]=(quad_x-1>0 && quad_y-1>0)?MapaTiles.mapaFisico[quad_y-1][quad_x-1]:1;
        espacios[0][1]=((quad_y-1)>0)?MapaTiles.mapaFisico[quad_y-1][quad_x]:1;
        espacios[0][2]=(quad_x+1<Juego.ancho && quad_y-1>0)?MapaTiles.mapaFisico[quad_y-1][quad_x+1]:1;
        espacios[1][0]=(quad_x-1>0)?MapaTiles.mapaFisico[quad_y][quad_x-1]:1;
        espacios[1][1]=MapaTiles.mapaFisico[quad_y][quad_x];
        espacios[1][2]=(quad_x+1<Juego.ancho)?MapaTiles.mapaFisico[quad_y][quad_x+1]:1;
        espacios[2][0]=(quad_x-1>0 && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x-1]:1;
        espacios[2][1]=(quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x]:1;
        espacios[2][2]=(quad_x+1<Juego.ancho && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x+1]:1;
        int jugador_centro=MapaTiles.jugadoresDibujados[quad_y][quad_x];//es el neo de jugador que esta bloqueando la pelota, si es 0 es por que no hay.
        
        
        if(((quad_x-1>0 && quad_y-1>0)?MapaTiles.jugadores[quad_y-1][quad_x-1]:0)!=0)espacios[0][0]=2;
        if(((quad_y-1>0)?MapaTiles.jugadores[quad_y-1][quad_x]:0)!=0)espacios[0][1]=2;
        if(((quad_x+1<Juego.ancho && quad_y-1>0)?MapaTiles.jugadores[quad_y-1][quad_x+1]:0)!=0)espacios[0][2]=2;
        if(((quad_x-1>0)?MapaTiles.jugadores[quad_y][quad_x-1]:0)!=0)espacios[1][0]=2;
        if((MapaTiles.jugadores[quad_y][quad_x])!=0)espacios[1][1]=2;
        if(((quad_x+1<Juego.ancho)?MapaTiles.jugadores[quad_y][quad_x+1]:0)!=0)espacios[1][2]=2;
        if(((quad_x-1>0 && quad_y+1<Juego.alto)?MapaTiles.jugadores[quad_y+1][quad_x-1]:0)!=0)espacios[2][0]=2;
        if(((quad_y+1<Juego.alto)?MapaTiles.jugadores[quad_y+1][quad_x]:0)!=0)espacios[2][1]=2;
        if(((quad_x+1<Juego.ancho && quad_y+1<Juego.alto)?MapaTiles.jugadores[quad_y+1][quad_x+1]:0)!=0)espacios[2][2]=2;
        /*
        System.out.println("["+espacios[0][0]+","+espacios[0][1]+","+espacios[0][2]+"]");
        System.out.println("["+espacios[1][0]+","+espacios[1][1]+","+espacios[1][2]+"]");
        System.out.println("["+espacios[2][0]+","+espacios[2][1]+","+espacios[2][2]+"]");
        System.out.println("");
        */
        int pos_quad_x=(this.x+(tile.TILE_WIDTH)/2)%tile.TILE_WIDTH;//de 0 a 15.
        int pos_quad_y=(this.y+(tile.TILE_HEIGHT)/2)%tile.TILE_HEIGHT;//de 0 a 15.
        
        int espacio1=0;
        int espacio2=0;
        int espacio3=0;
        
        int invertir=0;//0=nada,1=x,2=y,3=x e y.
        
        int suma_x=0;
        int suma_y=0;
        
        //diferencia q se produce en el eje y al ajustar la pelota a la superficie del bloque.
        int diferencia=0;
        int diferencia_x=0;
        
        boolean en_el_piso=false;
        
        if(pos_quad_x<(tile.TILE_WIDTH/2)){
            if(pos_quad_y==(tile.TILE_HEIGHT/2)){
                if(espacios[1][0]==1 || espacios[1][0]==2){
                    //System.out.println("holi");
                    direccion_x=2;
                    direccion_y=0;
                    if((espacios[1][0]==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;
                    }
                }
            }
            if(pos_quad_y<(tile.TILE_HEIGHT/2)){
                espacio1=espacios[0][0];//esq
                espacio2=espacios[0][1];//der esq
                espacio3=espacios[1][0];//aba esq
                if((espacio1==1 || espacio1==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    //la linea perpendicular a la diagonal formada por hacia donde apunta la esquina es lineaX.
                    //si la pelota incide por el frente de esta linea, rebota en el angulo correspondiente.
                    //si rebota por detras, no se calcula por que la perlota esta en el centro de los nueve bloques.
                    //por ahora se invierten ambos ejes.
                    if((espacio1==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;
                    }
                    invertir=3;
                    direccion_x=2;//derecha.
                    direccion_y=2;//abajo.
                }
                if(espacio2==1 || espacio2==2){
                    invertir=2;
                    direccion_x=0;//se mantiene.
                    direccion_y=2;//abajo.
                    if((espacio2==2 && this.impulso==true)){
                        suma_y=1;
                    }
                }
                if((espacio3==1 || espacio3==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    invertir=1;
                    direccion_x=2;//derecha.
                    direccion_y=0;//se mantiene.
                    if((espacio3==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;//siempre salta
                    }
                }
                if((espacio2==1 || espacio2==2) && (espacio3==1 || espacio3==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    invertir=3;
                    direccion_x=2;//derecha.
                    direccion_y=2;//abajo.
                    if((espacio2==2 && this.impulso==true) || this.dx==0){
                        suma_y=1;
                    }
                    if((espacio3==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        suma_y=1;//siempre salta
                    }
                }
                //diferencia_x=this.x-(quad_x*tile.TILE_WIDTH);
                //this.x=quad_x*tile.TILE_WIDTH;//alinear la pelota en el eje x.
            }
            if(pos_quad_y>(tile.TILE_HEIGHT/2)){
                espacio1=espacios[2][0];//esq
                espacio2=espacios[1][0];//arr esq
                espacio3=espacios[2][1];//der esq
                if((espacio1==1 || espacio1==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    //la linea perpendicular a la diagonal formada por hacia donde apunta la esquina es lineaX.
                    //si la pelota incide por el frente de esta linea, rebota en el angulo correspondiente.
                    //si rebota por detras, no se calcula por que la perlota esta en el centro de los nueve bloques.
                    //por ahora se invierten ambos ejes.
                    invertir=3;
                    direccion_x=2;//derecha.
                    direccion_y=1;//arriba.
                    if((espacio1==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;
                    }
                }
                if((espacio2==1 || espacio2==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    //System.out.println("hola");
                    invertir=1;
                    direccion_x=2;//derecha.
                    direccion_y=0;//se mantiene.
                    if((espacio2==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;//siempre salta
                    }
                }
                if(espacio3==1 || espacio3==2){
                    invertir=2;
                    direccion_x=0;//se mantiene.
                    direccion_y=1;//arriba.
                    if((espacio3==2 && this.impulso==true)){
                        suma_y=1;
                    }else{
                        if(this.dx!=0) suma_x=-1;
                        //if(this.dy!=0) suma_y=-1;
                    }
                    diferencia=this.y-(quad_y*tile.TILE_HEIGHT);
                    //this.subY=quad_y*tile.TILE_HEIGHT*this.seccionador;//poner la pelota sobre el cuadrado que esta traspasando.
                    en_el_piso=true;
                    /*
                    if(Personaje.estaVivo[this.jugador]==true && this.dirigida==true){
                        if((Personaje.y[this.jugador]+1)*tile.TILE_HEIGHT<this.y){
                            //direccion_y=2;
                        }else{
                            direccion_y=1;
                            suma_y=1;
                        }
                        
                    }
                    */
                }
                if((espacio2==1 || espacio2==2) && pos_quad_x!=(tile.TILE_WIDTH/2) && (espacio3==1 || espacio3==2)){
                    invertir=3;
                    direccion_x=2;//derecha.
                    direccion_y=1;//arriba.
                    if(espacio2==2 && this.impulso==true){
                        suma_y=1;
                    }
                    if((espacio3==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;//siempre salta
                    }
                }
                //diferencia_x=this.x-(quad_x*tile.TILE_WIDTH);
                //this.x=quad_x*tile.TILE_WIDTH;//alinear la pelota en el eje x.
            }
        }
        if(pos_quad_x>(tile.TILE_WIDTH/2)){
            if(pos_quad_y==(tile.TILE_HEIGHT/2)){
                if(espacios[1][2]==1 || espacios[1][2]==2){
                    direccion_x=1;
                    direccion_y=0;
                    if((espacios[1][2]==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;
                    }
                }
            }
            if(pos_quad_y<(tile.TILE_HEIGHT/2)){
                espacio1=espacios[0][2];//esq
                espacio2=espacios[0][1];//izq esq
                espacio3=espacios[1][2];//aba esq
                if((espacio1==1 || espacio1==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    //la linea perpendicular a la diagonal formada por hacia donde apunta la esquina es lineaX.
                    //si la pelota incide por el frente de esta linea, rebota en el angulo correspondiente.
                    //si rebota por detras, no se calcula por que la perlota esta en el centro de los nueve bloques.
                    //por ahora se invierten ambos ejes.
                    invertir=3;
                    direccion_x=1;//izquierda.
                    direccion_y=2;//abajo.
                    if((espacio1==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;
                    }
                }
                if(espacio2==1 || espacio2==2){
                    invertir=2;
                    direccion_x=0;//se mantiene.
                    direccion_y=2;//abajo.
                    if(espacio2==2 && this.impulso==true){
                        suma_y=1;
                    }
                }
                if((espacio3==1 || espacio3==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    invertir=1;
                    direccion_x=1;//izquierda.
                    direccion_y=0;//se mantiene.
                    if((espacio3==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;//siempre salta
                    }
                }
                if((espacio2==1 || espacio2==2) && (espacio3==1 || espacio3==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    invertir=3;
                    direccion_x=1;//izquierda.
                    direccion_y=2;//abajo.
                    if(espacio2==2 && this.impulso==true){
                        suma_y=1;
                    }
                    if((espacio3==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;//siempre salta
                    }
                }
                //diferencia_x=this.x-(quad_x*tile.TILE_WIDTH);
                //this.x=quad_x*tile.TILE_WIDTH;//alinear la pelota en el eje x.
            }
            if(pos_quad_y>(tile.TILE_HEIGHT/2)){
                espacio1=espacios[2][2];//esq
                espacio2=espacios[1][2];//arr esq
                espacio3=espacios[2][1];//izq esq
                if((espacio1==1 || espacio1==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    //la linea perpendicular a la diagonal formada por hacia donde apunta la esquina es lineaX.
                    //si la pelota incide por el frente de esta linea, rebota en el angulo correspondiente.
                    //si rebota por detras, no se calcula por que la perlota esta en el centro de los nueve bloques.
                    //por ahora se invierten ambos ejes.
                    invertir=3;
                    direccion_x=1;//izquierda.
                    direccion_y=1;//arriba.
                    /*
                    System.out.println("["+espacios[0][0]+","+espacios[0][1]+","+espacios[0][2]+"]");
                    System.out.println("["+espacios[1][0]+","+espacios[1][1]+","+espacios[1][2]+"]");
                    System.out.println("["+espacios[2][0]+","+espacios[2][1]+","+espacios[2][2]+"]");
                    System.out.println("");
                    */
                    if((espacio1==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;
                    }
                }
                if((espacio2==1 || espacio2==2) && pos_quad_x!=(tile.TILE_WIDTH/2)){
                    invertir=2;
                    direccion_x=1;//izquierda.
                    direccion_y=0;//se mantiene.
                    if((espacio2==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;//siempre salta
                    }
                }
                if(espacio3==1 || espacio3==2){
                    invertir=1;
                    direccion_x=0;//se mantiene.
                    direccion_y=1;//arriba.
                    if(espacio3==2 && this.impulso==true){
                        suma_y=1;
                    }else{
                        if(this.dx!=0) suma_x=-1;
                        //if(this.dy!=0) suma_y=-1;
                    }
                    diferencia=this.y-(quad_y*tile.TILE_HEIGHT);
                    //this.subY=quad_y*tile.TILE_HEIGHT*this.seccionador;//poner la pelota sobre el cuadrado que esta traspasando.
                    en_el_piso=true;
                    /*
                    if(Personaje.estaVivo[this.jugador]==true && this.dirigida==true){
                        if((Personaje.y[this.jugador]+1)*tile.TILE_HEIGHT<this.y){
                            //direccion_y=2;
                        }else{
                            direccion_y=1;
                            suma_y=1;
                        }
                        
                    }
                    */
                }
                if((espacio2==1 || espacio2==2) && pos_quad_x!=(tile.TILE_WIDTH/2) && (espacio3==1 || espacio3==2)){
                    invertir=3;
                    direccion_x=1;//izquierda.
                    direccion_y=1;//arriba.
                    if(espacio2==2 && this.impulso==true){
                        suma_y=1;
                    }
                    if((espacio3==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;//siempre salta
                    }
                }
                //diferencia_x=this.x-(quad_x*tile.TILE_WIDTH);
                //this.x=quad_x*tile.TILE_WIDTH;//alinear la pelota en el eje x.
            }
        }
        
        if(pos_quad_x==(tile.TILE_HEIGHT/2)){
            if(pos_quad_y==(tile.TILE_HEIGHT/2)){
                //se mantiene:
                direccion_x=0;
                direccion_y=0;
            }
            if(pos_quad_y<(tile.TILE_HEIGHT/2)){
                if(espacios[0][1]==1 || espacios[0][1]==2){
                    direccion_x=0;
                    direccion_y=2;
                    if((espacios[0][1]==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;
                    }
                }
            }
            if(pos_quad_y>(tile.TILE_HEIGHT/2)){
                if(espacios[2][1]==1 || espacios[2][1]==2){
                    direccion_x=0;
                    direccion_y=1;
                    if((espacios[2][1]==2 && this.impulso==true) || this.dx==0){
                        suma_x=1;
                        if(this.impulso==true) suma_y=1;
                    }else{
                        if(this.dx!=0) suma_x=-1;
                        //if(this.dy!=0) suma_y=-1;
                    }
                    diferencia=this.y-(quad_y*tile.TILE_HEIGHT);
                    //this.subY=quad_y*tile.TILE_HEIGHT*this.seccionador;//poner la pelota sobre el cuadrado que esta traspasando.
                    en_el_piso=true;
                }
            }
        }
        
        //si es bloque normal y esta bloqueando a la pelota, esta sigue al jugador.
        
        if(espacios[1][1]==1){
            direccion_y=1;
            this.dy=limite;
            //System.out.println("ocupado 1");
            if(Personaje.estaVivo[this.jugador]==true && this.dirigida==true){
                
                if((Personaje.x[this.jugador]+2)*tile.TILE_WIDTH>this.x){
                    direccion_x=2;
                }else{
                    direccion_x=1;
                }
                
                if((Personaje.y[this.jugador]+1)*tile.TILE_HEIGHT>this.y){
                    direccion_y=2;
                }else{
                    direccion_y=1;
                }
                if(direccion_x==1) this.dx=-limite;
                if(direccion_x==2) this.dx=limite;
                if(direccion_y==1) this.dy=-limite;
                if(direccion_y==2) this.dy=limite;
                
            }else{
                direccion_y=1;
                this.dy=limite;
            }
        }
        
        /*
        if(Personaje.estaVivo[this.jugador]==true && this.dirigida==true && direccion_x!=0 && direccion_y!=0){
            System.out.println("desocupado 1");
            if((Personaje.x[this.jugador]+2)*tile.TILE_WIDTH>this.x){
                direccion_x=2;
            }else{
                direccion_x=1;
            }
            suma_x=1;
        }else{
            System.out.println("desocupado 2");
        }
        */
        //si es un jugador y esta bloqueando a la pelota, esta va hacia arriba.
        if(espacios[1][1]==2){
            direccion_y=1;
            //System.out.println("ocupado 2");
            
            if(jugador_centro>=1 && jugador_centro<=4) direccion_x=Personaje.direccion[jugador_centro-1];
            this.dy=-limite;
            if(direccion_x==1) this.dx=-limite;
            if(direccion_x==2) this.dx=limite;
            
        }
        
        if(direccion_x==1 && this.dx>0) this.dx=-this.dx;
        else if(direccion_x==2 && this.dx<0) this.dx=-this.dx;
        
        if(direccion_y==1 && this.dy>0) this.dy=-this.dy;
        else if(direccion_y==2 && this.dy<0) this.dy=-this.dy;
        
        //if(this.dy<0)
        if(!((espacios[2][1]==1 || espacios[2][1]==2) && pos_quad_y>=(tile.TILE_HEIGHT/2) && this.dy==0))this.dy+=2;//gravedad
        else{
            if(dx!=0) suma_x=-1;
        }
        
        if(this.dx>0 || direccion_x==2) this.dx+=suma_x;
        else if(this.dx<0 || direccion_x==1) this.dx-=suma_x;
        
        if(this.dy>0 ||direccion_y==2) this.dy+=suma_y;
        else if(this.dy<0 || direccion_y==1) this.dy-=suma_y;
        
        if(this.dx>this.limite) this.dx=this.limite;
        if(this.dy>this.limite) this.dy=this.limite;
        if(this.dx<(-this.limite)) this.dx=(-this.limite);
        if(this.dy<(-this.limite)) this.dy=(-this.limite);
        
        //mover pelota:
        this.subX+=this.dx;
        this.subY+=(this.dy);//-diferencia
        
        this.x=this.subX/this.seccionador;
        this.y=this.subY/this.seccionador;
        
    }
}