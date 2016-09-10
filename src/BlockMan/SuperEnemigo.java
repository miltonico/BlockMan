/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BlockMan;

import java.awt.event.*;

/**
 *
 * @author TIMBERWOLF
 */
public class SuperEnemigo {
    
    public int x,y,inicioX,inicioY,dx,dy,limite,enemigo,subX,subY,posicion,posicionAnterior,posicionCaminando,incremento,incrementoCorriendo,fase_salto,posCamX,posCamY,nadando,quemandose,curandose,direccion;//,cambiar_y
    
    //variables de la inteligencia artificial:
    //direccion enfoque prioriza ataques de armas
    public int direccionEnfoqueActual=0;//0=ninguna,1=izq,2=der (si esta activa(1 o 2), busca al enemigo en esa direccion).
    public int tipoEnfoqueActual=0;//puede no tener enfoque(0=tranquilo), ser enfoque por deteccion de enemigo(1) o por ataques(2).
    public int duracionEnfoque=200;//en frames.
    public int duracionEnfoqueActual=0;//en frames.
    public int contBalas=0,retrasoBalas=10;
    
    //variables de posicionamiento y movimiento:
    public int seccionador=(4/(Tile.TILE_WIDTH/4));//min 1 cuando es 16 el tamaño del tile, max 4 cuando es 4 el tamaño del tile.
    public int seccionadorX=4*seccionador,seccionadorY=4*seccionador;
    //seccionador: es inversamente proporcional al tamaño del tile
    
    //cuerpo del enemigo:
    public String nada,cabeza,torso,cintura,brazoIzq,brazoDer,pieIzq,pieDer;
    
    //posiciones de las partes del cuerpo del enemigo:
    public int[][][] positionParts;//la posicion dentro del enemigo en el eje y usa el seccionador del eje x
    //porque la velocidad debe ser constante al cambiar de posicion para ambos ejes
    
    public boolean[][] partesBloqueadas;
    
    public boolean dirigida,impulso,fase_completa,retraso,atorado,tocandoLava,tocandoAgua,bloqueado_eje_x=false;//dirigida: sigue al enemigo,impulso: el enemigo la potencia con solo tocarla.
    
    public boolean espacio,arriba,izquierda,derecha,agacharse,disparo,cambiar,corriendo,disparado,cambiado,saltando,deslizando;
    
    //bloqueos:
    public boolean bloqueado_izquierda,bloqueado_derecha,bloqueado_arriba,bloqueado_abajo,brazoDerBloqueado,brazoIzqBloqueado;
    
    public static Tile tile=new Tile();
    
    public String[][] cuerpo=new String[4][5];
    
    public int healthMultiplier=32,health=healthMultiplier*8,healthLimit=healthMultiplier*8;
    
    public boolean alive=true;
    public boolean deadDimension=false;
    
    //variables de control:
    public boolean vladimir=false;
    public boolean deslizar=false;
    public boolean siempreCorriendo=false;
    public boolean rebotar_eje_y=false;
    
    
    public SuperEnemigo(int x, int y, int enemigo){
        this.direccion=2;
        this.retraso=false;
        this.incremento=(tile.TILE_WIDTH*seccionador);//incremento en subPixeles
        this.incrementoCorriendo=(tile.TILE_WIDTH*seccionador)*2;//incremento en subPixeles
        this.inicioX=x*tile.TILE_WIDTH;
        this.inicioY=y*tile.TILE_HEIGHT;
        this.x=x*tile.TILE_WIDTH;
        this.y=y*tile.TILE_HEIGHT;
        this.subX=x*tile.TILE_WIDTH*seccionadorX;
        this.subY=y*tile.TILE_HEIGHT*seccionadorY;
        this.dx=0;
        this.dy=0;
        this.enemigo=enemigo;//0 a 3.
        this.dirigida=true;
        this.impulso=false;
        this.fase_completa=false;
        this.atorado=false;
        this.posicionCaminando=2;//1 o 2 (posicion quieto o caminando).
        //this.cambiar_y=0;
        this.nadando=0;
        this.quemandose=0;
        this.tocandoAgua=false;
        this.tocandoLava=false;
        
        
        //variables del enemigo:
        
        this.nada="0";
        this.torso="4";
        this.cintura="3";
        this.brazoIzq="6";
        this.brazoDer="5";
        this.pieIzq="2";
        this.pieDer="1";
        this.cabeza="7";
        
        //positionParts:
        //8=(Nro de partes),2=(x,y),2=(posActual,posAnterior).
        this.positionParts=new int[8][2][2];
        
        //8=(Nro de partes),4=(direcciones).
        this.partesBloqueadas=new boolean[8][4];
        
        this.posicion=0;
        
        //setPosicion(1);
        
        this.bloqueado_izquierda=true;
        this.bloqueado_derecha=true;
        this.bloqueado_arriba=true;
        this.bloqueado_abajo=true;
        
    }
    
    public void setPosCuerpo(){//posiciona las piezas de acuerdo a la nueva posicion (gradualmente).
        if(this.siempreCorriendo){
            this.corriendo=true;
        }
        boolean fase_completa=true;
        int pies_bloqueados=0;//0=ninguno,1=un pie,2=dos pies.
        int fase_completa_pies=0;//0=ninguno,1=un pie,2=dos pies.
        boolean atorado=false;
        int incremento=this.incremento;
        int veces=1;
        if(this.corriendo){
            //incremento=this.incrementoCorriendo;
            //incremento/=2;
            //veces=2;
        }
        if(this.nadando>0 || this.curandose>0 || this.quemandose>0) incremento/=2;//se mueve a la mitad de la velocidad en el eje x.
        int nadando=0,quemandose=0,curandose=0,quad_x=0,quad_y=0,centro=0,centro2=0;
        
        for(int v=0;v<veces;v++){
            this.bloqueado_arriba=bloqueado(3,true);
            this.bloqueado_abajo=bloqueado(4,true);
            this.bloqueado_izquierda=bloqueado(1,false);
            this.bloqueado_derecha=bloqueado(2,false);
            this.brazoIzqBloqueado=this.partesBloqueadas[Integer.parseInt(this.brazoIzq)][0];
            this.brazoDerBloqueado=this.partesBloqueadas[Integer.parseInt(this.brazoDer)][1];
            for(int y=0;y<this.cuerpo.length;y++){
                for(int x=0;x<this.cuerpo[0].length;x++){
                    String[] superpuestas = this.cuerpo[y][x].split(",");
                    for(int i=0;i<superpuestas.length;i++){
                        
                        if(!superpuestas[i].equals("0")){
                            if(this.posicionAnterior==0){
                                //toma la posicion instantaneamente.
                                this.positionParts[Integer.parseInt(superpuestas[i])][0][0]=tile.TILE_WIDTH*x*seccionadorX;
                                this.positionParts[Integer.parseInt(superpuestas[i])][1][0]=tile.TILE_HEIGHT*y*seccionadorX;
                            }else{
                                /*
                                //ajustar eje y primero:
                                if(this.positionParts[Integer.parseInt(superpuestas[i])][1][0]<y*tile.TILE_HEIGHT){
                                    if(!bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0],this.positionParts[Integer.parseInt(superpuestas[i])][1][0]+incremento,Integer.parseInt(superpuestas[i]),false)){
                                        this.positionParts[Integer.parseInt(superpuestas[i])][1][0]+=incremento;
                                    }
                                    fase_completa=false;
                                }else if(this.positionParts[Integer.parseInt(superpuestas[i])][1][0]>y*tile.TILE_HEIGHT){
                                    if(!bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0],this.positionParts[Integer.parseInt(superpuestas[i])][1][0]-incremento,Integer.parseInt(superpuestas[i]),false)){
                                        this.positionParts[Integer.parseInt(superpuestas[i])][1][0]-=incremento;
                                    }
                                    fase_completa=false;
                                }
                                if(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]<x*tile.TILE_WIDTH){
                                    if(!bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]+incremento,this.positionParts[Integer.parseInt(superpuestas[i])][1][0],Integer.parseInt(superpuestas[i]),false)){
                                        this.positionParts[Integer.parseInt(superpuestas[i])][0][0]+=incremento;
                                    }
                                    fase_completa=false;
                                }else if(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]>x*tile.TILE_WIDTH){
                                    if(!bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]-incremento,this.positionParts[Integer.parseInt(superpuestas[i])][1][0],Integer.parseInt(superpuestas[i]),false)){
                                        this.positionParts[Integer.parseInt(superpuestas[i])][0][0]-=incremento;
                                    }
                                    fase_completa=false;
                                }
                                */
                                //ajustar eje y primero:
                                    if(this.positionParts[Integer.parseInt(superpuestas[i])][1][0]/this.seccionadorX<y*tile.TILE_HEIGHT){
    //                                    if(!bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0],this.positionParts[Integer.parseInt(superpuestas[i])][1][0]+incremento,Integer.parseInt(superpuestas[i]),true)){
    //                                        this.positionParts[Integer.parseInt(superpuestas[i])][1][0]+=incremento;
    //                                    }
                                        if(!this.partesBloqueadas[Integer.parseInt(superpuestas[i])][3]){
                                            this.positionParts[Integer.parseInt(superpuestas[i])][1][0]+=incremento;
                                        }
                                        fase_completa=false;
                                    }else if(this.positionParts[Integer.parseInt(superpuestas[i])][1][0]/this.seccionadorX>y*tile.TILE_HEIGHT){
    //                                    if(!bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0],this.positionParts[Integer.parseInt(superpuestas[i])][1][0]-incremento,Integer.parseInt(superpuestas[i]),true)){
    //                                        this.positionParts[Integer.parseInt(superpuestas[i])][1][0]-=incremento;
    //                                    }
                                        if(!this.partesBloqueadas[Integer.parseInt(superpuestas[i])][2]){
                                            this.positionParts[Integer.parseInt(superpuestas[i])][1][0]-=incremento;
                                        }
                                        fase_completa=false;
                                    }
                                    if(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]/this.seccionadorX<x*tile.TILE_WIDTH){
    //                                    if(!bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]+incremento,this.positionParts[Integer.parseInt(superpuestas[i])][1][0],Integer.parseInt(superpuestas[i]),false)){
    //                                        this.positionParts[Integer.parseInt(superpuestas[i])][0][0]+=incremento;
    //                                    }
                                        if(!this.partesBloqueadas[Integer.parseInt(superpuestas[i])][1]){
                                            this.positionParts[Integer.parseInt(superpuestas[i])][0][0]+=incremento;
                                        }else{
                                            if(superpuestas[i].equals(this.pieIzq) || superpuestas[i].equals(this.pieDer)) pies_bloqueados++;
                                        }
                                        fase_completa=false;
                                    }else if(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]/this.seccionadorX>x*tile.TILE_WIDTH){
    //                                    if(!bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]-incremento,this.positionParts[Integer.parseInt(superpuestas[i])][1][0],Integer.parseInt(superpuestas[i]),false)){
    //                                        this.positionParts[Integer.parseInt(superpuestas[i])][0][0]-=incremento;
    //                                    }
                                        if(!this.partesBloqueadas[Integer.parseInt(superpuestas[i])][0]){
                                            this.positionParts[Integer.parseInt(superpuestas[i])][0][0]-=incremento;
                                        }else{
                                            if(superpuestas[i].equals(this.pieIzq) || superpuestas[i].equals(this.pieDer)) pies_bloqueados++;
                                        }
                                        fase_completa=false;
                                    }else{
                                        if(!this.partesBloqueadas[Integer.parseInt(superpuestas[i])][0] && (superpuestas[i].equals(this.pieIzq) || superpuestas[i].equals(this.pieDer))) fase_completa_pies++;
                                    }
                            }
                            
                            this.positionParts[Integer.parseInt(superpuestas[i])][0][0]=(int)Math.floor((this.positionParts[Integer.parseInt(superpuestas[i])][0][0])/(incremento))*incremento;//se ajusta en el eje x.
                            this.positionParts[Integer.parseInt(superpuestas[i])][1][0]=(int)Math.floor((this.positionParts[Integer.parseInt(superpuestas[i])][1][0])/(incremento))*incremento;//se ajusta en el eje y.
                            
                            this.x=this.subX/this.seccionadorX;
                            this.y=this.subY/this.seccionadorY;
                            
                            this.positionParts[Integer.parseInt(superpuestas[i])][0][1]=this.positionParts[Integer.parseInt(superpuestas[i])][0][0]/this.seccionadorX;
                            this.positionParts[Integer.parseInt(superpuestas[i])][1][1]=this.positionParts[Integer.parseInt(superpuestas[i])][1][0]/this.seccionadorX;
                            
                            quad_x=(int)Math.floor(((this.x+this.positionParts[Integer.parseInt(superpuestas[i])][0][0]/this.seccionadorX+(tile.TILE_WIDTH/2))/tile.TILE_WIDTH));
                            quad_y=(int)Math.floor(((this.y+this.positionParts[Integer.parseInt(superpuestas[i])][1][0]/this.seccionadorX+(tile.TILE_HEIGHT/2))/tile.TILE_HEIGHT));
                            //System.out.println("qx="+quad_x+"qy="+quad_y);
                            //System.out.println("x="+MapaTiles.mapaFisico[quad_y][quad_x]);
                            centro=MapaTiles.mapaFisico[quad_y][quad_x];
                            //comprobar estrella:
                            if(Teclas.estrellaX==quad_x && Teclas.estrellaY==quad_y){
                                Juego.corriendo=false;
                            }
                            //comprobar si esta nadando o quemandose:
                            if(centro==4) nadando++;
                            if(centro==2) quemandose++;
                            if(centro==3) curandose++;
                            //if((this.positionParts[Integer.parseInt(superpuestas[i])][0][0]+this.x*this.seccionadorX)<0)System.out.println("x="+(this.positionParts[Integer.parseInt(superpuestas[i])][0][0]+this.x*this.seccionadorX));
                        }
                    }
                }
            }
        }
        this.atorado=false;//atorado;
        if(fase_completa==true || pies_bloqueados>0 || fase_completa_pies>0){// || (fase_completa_pies==1 && this.posicion==1) || (this.posicion!=1 && pies_bloqueados>0)
            this.fase_completa=true;
            /*
            //Michael jackson.
            if(this.posicion>9){
                this.posicion=1;
            }else{
                this.posicion++;
            }
            */
        }else{
            this.fase_completa=false;
        }
        this.nadando=nadando;
        this.quemandose=quemandose;
        this.curandose=curandose;
    }
    
    public void reaparecer(){
        this.deadDimension=false;
        this.health=this.healthLimit;
        this.x=this.inicioX*tile.TILE_WIDTH;
        this.y=this.inicioY*tile.TILE_HEIGHT;
        this.subX=this.inicioX*tile.TILE_WIDTH*this.seccionadorX;
        this.subY=this.inicioY*tile.TILE_HEIGHT*this.seccionadorY;
    }
    
    public void changeHealth(int health){
        this.health+=health;
        if(this.health>healthLimit) this.health=healthLimit;
        if(this.health<=0){
            this.alive=false;
            this.deadDimension=true;
            MapaTiles.items.add(new Items(49,(this.x+this.positionParts[Integer.parseInt(this.cabeza)][0][0]/this.seccionadorX)/Tile.TILE_WIDTH,(this.y+this.positionParts[Integer.parseInt(this.cabeza)][1][1])/Tile.TILE_HEIGHT));
            //System.out.println((this.x+this.positionParts[Integer.parseInt(this.cabeza)][0][1])/16);
            //System.out.println((this.y+this.positionParts[Integer.parseInt(this.cabeza)][1][1])/16);
            //this.reaparecer();
        }
    }
    
    public boolean bloqueBloqueado(int x, int y, int indice, boolean corregir){
        int auxX=x;
        int auxY=y;
        int desfaseX=0;
        int desfaseY=0;
        x=this.x+x;
        y=this.y+y;
        int centro=0;
        boolean bloqX=false;
        boolean bloqY=false;
        boolean retorno=false;
        for(int i=1;i<=4;i++){
            int direccion=i;
            //posicion aproximada.
            int quad_x=(int)Math.floor((x+(tile.TILE_WIDTH/2))/tile.TILE_WIDTH);
            int quad_y=(int)Math.floor((y+(tile.TILE_HEIGHT/2))/tile.TILE_HEIGHT);
            int pos_quad_x=(x+(tile.TILE_WIDTH)/2)%tile.TILE_WIDTH;//de 0 a 15.
            int pos_quad_y=(y+(tile.TILE_HEIGHT)/2)%tile.TILE_HEIGHT;//de 0 a 15.
            int espacio1,espacio2,espacio3;
            centro=MapaTiles.mapaFisico[quad_y][quad_x];
            
            if(direccion==1){
                espacio1=(quad_x-1>0 && quad_y-1>0)?MapaTiles.mapaFisico[quad_y-1][quad_x-1]:1;
                espacio2=(quad_x-1>0)?MapaTiles.mapaFisico[quad_y][quad_x-1]:1;
                espacio3=(quad_x-1>0 && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x-1]:1;
                if(pos_quad_x<(tile.TILE_WIDTH/2)){
                    desfaseX=-1;
                    
                    if(pos_quad_y==(tile.TILE_HEIGHT/2)){
                        if(espacio2==1){
                            bloqX=true;
                            //desfaseX=-1;
                            retorno=true;
                        }
                        if(espacio2==2) this.tocandoLava=true;
                        if(espacio2==4) this.tocandoAgua=true;
                    }
                    if(pos_quad_y<(tile.TILE_HEIGHT/2)){
                        if(espacio1==1 || espacio2==1){
                            desfaseY=-1;
                            if(espacio1==1){
                                //bloqX=true;(para q solo se cambie el eje y).
                                bloqY=true;
                                //desfaseY=-1;
                            }
                            if(espacio2==1){
                                bloqX=true;
                                //desfaseX=-1;
                            }
                            retorno=true;
                        }
                        if(espacio1==2 || espacio2==2) this.tocandoLava=true;
                        if(espacio1==4 || espacio2==4) this.tocandoAgua=true;
                    }
                    if(pos_quad_y>(tile.TILE_HEIGHT/2)){
                        if(espacio3==1 || espacio2==1){
                            desfaseY=1;
                            if(espacio3==1){
                                //bloqX=true;(para q solo se cambie el eje y).
                                bloqY=true;
                                //desfaseX=-1;
                                //desfaseY=1;
                            }
                            if(espacio2==1){
                                bloqX=true;
                                //desfaseX=-1;
                            }
                            retorno=true;
                        }
                        if(espacio3==2 || espacio2==2) this.tocandoLava=true;
                        if(espacio3==4 || espacio2==4) this.tocandoAgua=true;
                    }
                }
            }
            if(direccion==2){
                espacio1=(quad_x+1<Juego.ancho && quad_y-1>0)?MapaTiles.mapaFisico[quad_y-1][quad_x+1]:1;
                espacio2=(quad_x+1<Juego.ancho)?MapaTiles.mapaFisico[quad_y][quad_x+1]:1;
                espacio3=(quad_x+1<Juego.ancho && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x+1]:1;
                if(pos_quad_x>(tile.TILE_WIDTH/2)){
                    desfaseX=-1;
                    if(pos_quad_y==(tile.TILE_HEIGHT/2)){
                        if(espacio2==1){
                            bloqX=true;
                            //desfaseX=1;
                            retorno=true;
                        }
                        if(espacio2==2) this.tocandoLava=true;
                        if(espacio2==4) this.tocandoAgua=true;
                    }
                    if(pos_quad_y<(tile.TILE_HEIGHT/2)){
                        if(espacio1==1 || espacio2==1){
                            desfaseY=-1;
                            if(espacio1==1){
                                //bloqX=true;(para q solo se cambie el eje y).
                                bloqY=true;
                                //desfaseX=1;
                                //desfaseY=-1;
                            }
                            if(espacio2==1){
                                bloqX=true;
                                //desfaseX=1;
                            }
                            retorno=true;
                        }
                        if(espacio1==2 || espacio2==2) this.tocandoLava=true;
                        if(espacio1==4 || espacio2==4) this.tocandoAgua=true;
                    }
                    if(pos_quad_y>(tile.TILE_HEIGHT/2)){
                        if(espacio3==1 || espacio2==1){
                            desfaseY=1;
                            if(espacio3==1){
                                //bloqX=true;(para q solo se cambie el eje y).
                                bloqY=true;
                                //desfaseX=1;
                                //desfaseY=1;
                            }
                            if(espacio2==1){
                                bloqX=true;
                                //desfaseX=1;
                            }
                            retorno=true;
                        }
                        if(espacio3==2 || espacio2==2) this.tocandoLava=true;
                        if(espacio3==4 || espacio2==4) this.tocandoAgua=true;
                    }
                }
            }
            if(direccion==3){
                espacio1=(quad_x-1>0 && quad_y-1>0)?MapaTiles.mapaFisico[quad_y-1][quad_x-1]:1;
                espacio2=((quad_y-1)>0)?MapaTiles.mapaFisico[quad_y-1][quad_x]:1;
                espacio3=(quad_x+1<Juego.ancho && quad_y-1>0)?MapaTiles.mapaFisico[quad_y-1][quad_x+1]:1;
                if(pos_quad_y<(tile.TILE_HEIGHT/2)){
                    desfaseY=-1;
                    if(pos_quad_x==(tile.TILE_WIDTH/2)){
                        if(espacio2==1){
                            bloqY=true;
                            //desfaseY=-1;
                            retorno=true;
                        }
                        if(espacio2==2) this.tocandoLava=true;
                        if(espacio2==4) this.tocandoAgua=true;
                    }
                    if(pos_quad_x<(tile.TILE_WIDTH/2)){
                        if(espacio1==1 || espacio2==1){
                            desfaseX=-1;
                            if(espacio1==1){
                                //desfaseY=-1;
                                //desfaseX=-1;
                                //bloqX=true;(para q solo se cambie el eje y).
                                bloqY=true;
                            }
                            if(espacio2==1){
                                //desfaseY=-1;
                                bloqY=true;
                            }
                            retorno=true;
                        }
                        if(espacio1==2 || espacio2==2) this.tocandoLava=true;
                        if(espacio1==4 || espacio2==4) this.tocandoAgua=true;
                    }
                    if(pos_quad_x>(tile.TILE_WIDTH/2)){
                        if(espacio3==1 || espacio2==1){
                            desfaseX=1;
                            if(espacio3==1){
                                //desfaseY=-1;
                                //desfaseX=1;
                                //bloqX=true;(para q solo se cambie el eje y).
                                bloqY=true;
                            }
                            if(espacio2==1){
                                //desfaseY=1;
                                bloqY=true;
                            }
                            retorno=true;
                        }
                        if(espacio3==2 || espacio2==2) this.tocandoLava=true;
                        if(espacio3==4 || espacio2==4) this.tocandoAgua=true;
                    }
                }
            }
            if(direccion==4){
                espacio1=(quad_x-1>0 && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x-1]:1;
                espacio2=(quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x]:1;
                espacio3=(quad_x+1<Juego.ancho && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x+1]:1;
                if(pos_quad_y>(tile.TILE_HEIGHT/2)){
                    desfaseY=1;
                    if(pos_quad_x==(tile.TILE_WIDTH/2)){
                        if(espacio2==1){
                            //desfaseY=1;
                            bloqY=true;
                            retorno=true;
                        }
                        if(espacio2==2) this.tocandoLava=true;
                        if(espacio2==4) this.tocandoAgua=true;
                    }
                    if(pos_quad_x<(tile.TILE_WIDTH/2)){
                        if(espacio1==1 || espacio2==1){
                            desfaseX=-1;
                            if(espacio1==1){
                                //desfaseY=1;
                                //desfaseX=-1;
                                //bloqX=true;(para q solo se cambie el eje y).
                                bloqY=true;
                            }
                            if(espacio2==1){
                                //desfaseY=1;
                                bloqY=true;
                            }
                            retorno=true;
                        }
                        if(espacio1==2 || espacio2==2) this.tocandoLava=true;
                        if(espacio1==4 || espacio2==4) this.tocandoAgua=true;
                    }
                    if(pos_quad_x>(tile.TILE_WIDTH/2)){
                        if(espacio3==1 || espacio2==1){
                            desfaseX=1;
                            if(espacio3==1){
                                //desfaseY=1;
                                //desfaseX=1;
                                //bloqX=true;(para q solo se cambie el eje y).
                                bloqY=true;
                            }
                            if(espacio2==1){
                                //desfaseY=1;
                                bloqY=true;
                            }
                            retorno=true;
                        }
                        if(espacio3==2 || espacio2==2) this.tocandoLava=true;
                        if(espacio3==4 || espacio2==4) this.tocandoAgua=true;
                    }
                }
            }
        }
        if(bloqY && corregir){
            this.positionParts[indice][1][0]=(int)Math.floor((y+(tile.TILE_HEIGHT/2))/tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.y;
            retorno=false;
            //this.atorado=true;
        }
        if(bloqX && corregir){
            this.positionParts[indice][0][0]=(int)Math.floor((x+(tile.TILE_WIDTH/2))/tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.x;
            retorno=false;
            //this.atorado=true;
        }
        if(centro==1 && corregir){
            if(this.positionParts[indice][1][0]>=(this.y+(2*Tile.TILE_HEIGHT))){
                this.positionParts[indice][1][0]-=Tile.TILE_HEIGHT;
            }else{
                this.positionParts[indice][1][0]=Tile.TILE_HEIGHT;
            }
            //this.atorado=true;
            retorno=false;
        }else{
            if(centro==1){
                this.positionParts[indice][0][0]=this.positionParts[indice][0][1];
                this.positionParts[indice][1][0]=this.positionParts[indice][1][1];
            }
        }
        /*
        if(bloqY && corregir){
            this.positionParts[indice][1][0]=(int)Math.floor((y+(tile.TILE_HEIGHT/2))/tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.y;
            retorno=false;
            //this.atorado=true;
        }
        if(bloqX && corregir){
            this.positionParts[indice][0][0]=(int)Math.floor((x+(tile.TILE_WIDTH/2))/tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.x;
            retorno=false;
            //this.atorado=true;
        }
        */
        int divisor=1;
        if(this.quemandose>0 || this.nadando>0 || this.curandose>0) divisor=2;
        if(desfaseX!=0) this.positionParts[indice][0][0]+=(desfaseX*(this.positionParts[indice][0][0]%(this.incremento/divisor)));
        if(desfaseY!=0) this.positionParts[indice][1][0]+=(desfaseY*(this.positionParts[indice][1][0]%(this.incremento/divisor)));
        //System.out.println("retorno="+retorno);
        return retorno;
    }
    
    public void setPosicion(int posicion){
        this.posicionAnterior=this.posicion;
        this.posicion=posicion;
        switch(posicion){
            case 0://nada:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.nada;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.nada;
                this.cuerpo[1][2]=this.nada;
                this.cuerpo[1][3]=this.nada;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.nada;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.nada;
                this.cuerpo[3][2]=this.nada;
                this.cuerpo[3][3]=this.nada;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 1://normal:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.cabeza;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.brazoIzq;
                this.cuerpo[1][2]=this.torso;
                this.cuerpo[1][3]=this.brazoDer;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.pieIzq;
                this.cuerpo[3][2]=this.nada;
                this.cuerpo[3][3]=this.pieDer;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 2://caminar izq:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.cabeza;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.brazoIzq;
                this.cuerpo[1][2]=this.torso+","+brazoDer;
                this.cuerpo[1][3]=this.nada;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.nada;
                this.cuerpo[3][2]=this.pieIzq+","+pieDer;
                this.cuerpo[3][3]=this.nada;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 3://caminar der:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.cabeza;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.nada;
                this.cuerpo[1][2]=this.torso+","+brazoIzq;
                this.cuerpo[1][3]=this.brazoDer;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.nada;
                this.cuerpo[3][2]=this.pieIzq+","+pieDer;
                this.cuerpo[3][3]=this.nada;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 4://saltar izq:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.cabeza;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.brazoIzq;
                this.cuerpo[1][2]=this.torso+","+brazoDer;
                this.cuerpo[1][3]=this.nada;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.nada;
                this.cuerpo[3][2]=this.pieIzq;
                this.cuerpo[3][3]=this.pieDer;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 5://saltar der:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.cabeza;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.nada;
                this.cuerpo[1][2]=this.torso+","+brazoIzq;
                this.cuerpo[1][3]=this.brazoDer;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.pieIzq;
                this.cuerpo[3][2]=this.pieDer;
                this.cuerpo[3][3]=this.nada;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 6://de lado:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.cabeza;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.nada;
                this.cuerpo[1][2]=this.brazoIzq+","+this.torso+","+this.brazoDer;
                this.cuerpo[1][3]=this.nada;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.nada;
                this.cuerpo[3][2]=this.pieIzq+","+pieDer;
                this.cuerpo[3][3]=this.nada;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 7://agacharse:
                if(this.vladimir){
                    this.cuerpo[0][0]=this.nada;
                    this.cuerpo[0][1]=this.nada;
                    this.cuerpo[0][2]=this.nada;
                    this.cuerpo[0][3]=this.nada;
                    this.cuerpo[0][4]=this.nada;

                    this.cuerpo[1][0]=this.nada;
                    this.cuerpo[1][1]=this.nada;
                    this.cuerpo[1][2]=this.nada;
                    this.cuerpo[1][3]=this.nada;
                    this.cuerpo[1][4]=this.nada;

                    this.cuerpo[2][0]=this.nada;
                    this.cuerpo[2][1]=this.pieIzq+","+this.brazoIzq;
                    this.cuerpo[2][2]=this.cintura+","+this.torso+","+this.cabeza;
                    this.cuerpo[2][3]=this.pieDer+","+this.brazoDer;
                    this.cuerpo[2][4]=this.nada;
                    
                    this.cuerpo[3][0]=this.nada;
                    this.cuerpo[3][1]=this.nada;
                    this.cuerpo[3][2]=this.nada;
                    this.cuerpo[3][3]=this.nada;
                    this.cuerpo[3][4]=this.nada;
                }else{
                    this.cuerpo[0][0]=this.nada;
                    this.cuerpo[0][1]=this.nada;
                    this.cuerpo[0][2]=this.nada;
                    this.cuerpo[0][3]=this.nada;
                    this.cuerpo[0][4]=this.nada;

                    this.cuerpo[1][0]=this.nada;
                    this.cuerpo[1][1]=this.brazoIzq;
                    this.cuerpo[1][2]=this.torso+","+this.cabeza;
                    this.cuerpo[1][3]=this.brazoDer;
                    this.cuerpo[1][4]=this.nada;
                    
                    this.cuerpo[2][0]=this.nada;
                    this.cuerpo[2][1]=this.pieIzq;
                    this.cuerpo[2][2]=this.cintura;
                    this.cuerpo[2][3]=this.pieDer;
                    this.cuerpo[2][4]=this.nada;
                    
                    this.cuerpo[3][0]=this.nada;
                    this.cuerpo[3][1]=this.nada;
                    this.cuerpo[3][2]=this.nada;
                    this.cuerpo[3][3]=this.nada;
                    this.cuerpo[3][4]=this.nada;
                }
                
                
                break;
            case 8://deslizar izq:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.nada;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.cabeza;
                this.cuerpo[1][2]=this.nada;
                this.cuerpo[1][3]=this.nada;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.brazoIzq;
                this.cuerpo[2][1]=this.torso;
                this.cuerpo[2][2]=this.brazoDer;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.pieIzq;
                this.cuerpo[3][2]=this.cintura;
                this.cuerpo[3][3]=this.pieDer;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 9://deslizar der:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.nada;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.nada;
                this.cuerpo[1][2]=this.nada;
                this.cuerpo[1][3]=this.cabeza;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.brazoIzq;
                this.cuerpo[2][3]=this.torso;
                this.cuerpo[2][4]=this.brazoDer;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.pieIzq;
                this.cuerpo[3][2]=this.cintura;
                this.cuerpo[3][3]=this.pieDer;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 10://cuadradito:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.nada;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.nada;
                this.cuerpo[1][2]=this.nada;
                this.cuerpo[1][3]=this.nada;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura+","+this.brazoIzq+","+this.torso+","+this.brazoDer+","+this.cabeza+","+this.pieDer+","+this.pieIzq;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.nada;
                this.cuerpo[3][2]=this.nada;
                this.cuerpo[3][3]=this.nada;
                this.cuerpo[3][4]=this.nada;
                
                break;
        }
        //la posicion del enemigo tendra fases, cuando se complete una fase para una accion, podra continuar con la siguiente.
        setPosCuerpo();//siempre se actualiza la posicion del enemigo, las piezas siempre estaran en movimiento.
        
        if(this.corriendo){
            setPosCuerpo();
        }
        
    }
    
    public boolean jugadorEnLaMira(int direccion){
        
        int quad_x=(x+positionParts[Integer.parseInt(cabeza)][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH;
        int quad_y=(y+positionParts[Integer.parseInt(cabeza)][1][1]+Tile.TILE_HEIGHT/2)/Tile.TILE_HEIGHT;
        if(direccion==1){
            for(int y=0;y<3;y++){
                for(int x=0;x<8;x++){
                    if((quad_y+y)>Juego.alto || (quad_y+y)<0 || (quad_x+x)>Juego.ancho || (quad_x+x)<0 || MapaTiles.mapaFisico[quad_y+y][quad_x-x]==1){
                        x=8;//se continua con la siguiente fila.
                    }else{
                        for(int i=0;i<Juego.superJugador.size();i++){
                            if(Juego.superJugador.get(i).deadDimension==this.deadDimension){
                                for(int j=1;j<Juego.superJugador.get(i).positionParts.length;j++){
                                    int jx=(Juego.superJugador.get(i).x+Juego.superJugador.get(i).positionParts[j][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH;
                                    int jy=(Juego.superJugador.get(i).y+Juego.superJugador.get(i).positionParts[j][1][1]+Tile.TILE_HEIGHT/2)/Tile.TILE_HEIGHT;
                                    if(jx==(quad_x-x) && jy==(quad_y+y)){
                                        //System.out.println("miraIzq"+this.enemigo);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else{
            for(int y=0;y<3;y++){
                for(int x=0;x<8;x++){
                    if((quad_y+y)>=Juego.alto || (quad_y+y)<0 || (quad_x+x)>=Juego.ancho || (quad_x+x)<0 || MapaTiles.mapaFisico[quad_y+y][quad_x+x]==1){
                        x=8;//se continua con la siguiente fila.
                    }else{
                        for(int i=0;i<Juego.superJugador.size();i++){
                            if(Juego.superJugador.get(i).deadDimension==this.deadDimension){
                                for(int j=1;j<Juego.superJugador.get(i).positionParts.length;j++){
                                    int jx=(Juego.superJugador.get(i).x+Juego.superJugador.get(i).positionParts[j][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH;
                                    int jy=(Juego.superJugador.get(i).y+Juego.superJugador.get(i).positionParts[j][1][1]+Tile.TILE_HEIGHT/2)/Tile.TILE_HEIGHT;
                                    if(jx==(quad_x+x) && jy==(quad_y+y)){
                                        //System.out.println("miraDer"+this.enemigo);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean sePuedeAvanzar(int direccion){
        if(direccion==1){
            if(bloqueado_izquierda){
                return false;
            }
        }else{
            if(bloqueado_derecha){
                return false;
            }
        }
        return true;
    }
    
    public void tomarDecisiones(){
        boolean jugadorMira=jugadorEnLaMira(direccion);
        //System.out.println(jugadorMira);
        if(jugadorMira){
            duracionEnfoqueActual=duracionEnfoque;
            tipoEnfoqueActual=1;
            disparo=true;
        }
        contBalas++;
        if(contBalas>=retrasoBalas){
            disparado=false;
            contBalas=0;
        }
        
        //decidir adonde moverse:
        if(tipoEnfoqueActual>0){
            if(sePuedeAvanzar(direccion)){
                if(direccion==1){
                    izquierda=true;
                }else{
                    derecha=true;
                }
            }else{
                if(!jugadorMira){
                    if(direccion==1){
                        direccion=2;
                        derecha=true;
                    }else{
                        direccion=1;
                        izquierda=true;
                    }
                }
            }
        }
        //decidir si correr:
        if(tipoEnfoqueActual>0 && duracionEnfoqueActual>duracionEnfoque/2){
            corriendo=true;
        }
        //duracion del enfoque:
        duracionEnfoqueActual--;
        if(duracionEnfoqueActual<0){
            duracionEnfoqueActual=0;
            tipoEnfoqueActual=0;
        }
    }
    
    public void actualizar(){
        //if(!alive) return;
        //toma de decisiones:
        
        this.tomarDecisiones();
        
        
        if(this.fase_completa==true){
            if(posicionCaminando==1){
                posicionCaminando=2;
            }else{
                posicionCaminando=1;
            }
        }
        int incremento=this.incremento;
        int veces=1;
        //si ademas de correr esta a medio o justo en el bloque:(dejar como estaba antes?)
        if(corriendo==true){// && (((this.nadando>0 || this.curandose>0 || this.quemandose>0) && (this.x%Tile.TILE_WIDTH/2==0 || this.x%Tile.TILE_WIDTH/2==Tile.TILE_WIDTH/4)) || (!(this.nadando>0 || this.curandose>0 || this.quemandose>0) && (this.x%Tile.TILE_WIDTH==0 || this.x%Tile.TILE_WIDTH==Tile.TILE_WIDTH/2)))){
            //incremento=this.incrementoCorriendo;
            veces=2;
        }
        if(deslizando==true){
            incremento=this.incrementoCorriendo+this.incremento;
        }
        
        if(this.nadando>0 || this.curandose>0 || this.quemandose>0) incremento/=2;//se mueve a la mitad de la velocidad en el eje x.
        //System.out.println("incremento="+incremento);
        int posicion=1;
        if((this.posicion==2 && bloqueado_izquierda) || (this.posicion==3 && bloqueado_derecha)){
            posicion=this.posicion;
        }
        //variables que verifican que si esta bloqueado y no se mueve se corriga las posiciones dentro del personaje:
        int x_inicial=this.x,y_inicial=this.y;
        if((this.derecha || this.izquierda || this.deslizando) && !(this.derecha && this.izquierda)){
            int i=0;
            while(i<veces){
                //corregir y:(deben estar en este orden para q se corriga el eje y, y luego se compruebe el x).
                this.bloqueado_arriba=bloqueado(3,true);
                this.bloqueado_abajo=bloqueado(4,true);
                this.bloqueado_izquierda=bloqueado(1,true);
                this.bloqueado_derecha=bloqueado(2,true);
                if(this.izquierda==true || (this.deslizando && this.direccion==1)){
                    this.direccion=1;
                    if((!this.fase_completa==true || !this.retraso) && bloqueado_izquierda==false) this.subX-=incremento;
                    if(!bloqueado_abajo){
                        if(bloqueado_izquierda){
                            posicion=2;
                        }else{
                            posicion=4;
                        }
                    }else{
                        if(posicionCaminando==1 && !this.brazoIzqBloqueado && !this.bloqueado_izquierda){// && !atorado && !bloqueado_izquierda
                            posicion=1;
                        }else{
                            if(this.brazoIzqBloqueado && !this.partesBloqueadas[Integer.parseInt(this.cintura)][0]){
                                posicion=6;
                            }else{
                                posicion=2;
                            }
                        }
                    }
                }
                if(this.derecha==true || (this.deslizando && this.direccion==2)){
                    this.direccion=2;
                    if((!this.fase_completa==true || !this.retraso) && bloqueado_derecha==false) this.subX+=incremento;
                    if(!bloqueado_abajo){
                        if(bloqueado_derecha){
                            posicion=3;
                        }else{
                            posicion=5;
                        }
                    }else{
                        if(posicionCaminando==1 && !this.brazoDerBloqueado && !this.bloqueado_derecha){// && !atorado && !bloqueado_derecha
                            posicion=1;
                        }else{
                            if(this.brazoDerBloqueado && !this.partesBloqueadas[Integer.parseInt(this.cintura)][1]){
                                posicion=6;
                            }else{
                                posicion=3;
                            }
                        }
                    }
                }
                i++;
            }
        }
        
        if(this.arriba){
            posicion=6;
        }
        if(this.deslizando){
            if(this.direccion==1) posicion=8;
            if(this.direccion==2) posicion=9;
        }
        if(this.agacharse){
            posicion=7;
        }
        if(this.arriba && this.agacharse && this.vladimir){
            posicion=10;
        }
        //cambiar esto cuando el personaje se mueva en una funcion aparte:
        if(x_inicial==this.x && y_inicial==this.y && this.bloqueado_eje_x){
            //System.out.println("bloqueado");
        }
        
        int potencia=(tile.TILE_WIDTH*seccionador)*2;
        int limite=(tile.TILE_WIDTH*seccionador)*2;
        int potenciaNado=0;
        if(this.nadando>0 || this.curandose>0 || this.quemandose>0){//mientras mas sumergido esta mas potencia de nado tendra.
            //ahora la potencia siempre sera la máxima, pero si supera el limite, disminuira gradualmente.//potencia=(int)((this.nadando+this.quemandose)/2);
            potenciaNado=((int)((this.nadando+this.quemandose+this.curandose)/2));
            //limite=limite-((int)((this.nadando+this.quemandose)*2));//(int)((this.nadando+this.quemandose)/2);
            //potenciaNado=((int)((this.nadando+this.quemandose)/2));
            //limite=limite-((int)((this.nadando+this.quemandose)/2));//(int)((this.nadando+this.quemandose)/2);
            limite=limite-8;
        }else{
            this.subX=(int)Math.floor((this.subX)/(incremento))*incremento;//se ajusta a un cuarto del tile.
            /*
            if(this.corriendo){
                this.subX=(int)Math.floor((this.subX)/8)*8;//se ajusta a un medio del tile.
            }else{
                this.subY=(int)Math.floor((this.subY)/4)*4;//se ajusta a un cuarto del tile.
            }
            */
        }
        
        if(this.dy>0){
            this.saltando=false;
            if(bloqueado_abajo){
                this.dy=0;
            }
        }

        if(bloqueado_arriba){
            this.saltando=false;
            if(this.dy<0){
                if(bloqueado_abajo){
                    this.dy=0;
                }else{

                    if(this.rebotar_eje_y){
                        this.dy=-this.dy;
                    }else{
                        this.dy=0;
                    }

                }
            }
        }

        if(this.espacio){
            if((bloqueado_abajo || this.nadando>0 || this.curandose>0 || this.quemandose>0)){//this.saltando==false && 

                if(bloqueado_abajo){
                    this.dy=-potencia;
                }else{
                    if(this.dy<-limite){
                        if(this.nadando>0 || this.curandose>0 || this.quemandose>0){
                            this.dy+=potenciaNado;//si la velocidad supera el limite decrece gradualmente.
                        }else{
                            this.dy+=1;//si la velocidad supera el limite decrece gradualmente.
                        }
                    }else{
                        if(this.nadando>0 || this.curandose>0 || this.quemandose>0){
                            this.dy-=potenciaNado;//si no, aumenta gradualmente.
                            if(!(bloqueado_abajo || this.atorado) && potenciaNado<=0){//gravedad
                                if(this.dy<limite){
                                    this.dy+=1;
                                }else if(this.dy>limite){
                                    this.dy-=1;
                                }
                            }else{
                                if(bloqueado_abajo || atorado){
                                    this.dy=0;
                                }
                            }
                        }else{//gravedad
                            if(!(bloqueado_abajo || this.atorado)){
                                if(this.dy<limite){
                                    this.dy+=1;
                                }else if(this.dy>limite){
                                    this.dy-=1;
                                }
                            }else{
                                if(bloqueado_abajo || atorado){
                                    this.dy=0;
                                }
                            }
                        }
                    }
                }
                this.saltando=true;
            }else{//gravedad
                if(!(bloqueado_abajo || this.atorado)){

                    if(this.dy<limite){
                        this.dy+=(nadando>0 || this.curandose>0 || quemandose>0)?potenciaNado:1;
                    }else if(this.dy>limite){
                        this.dy-=(nadando>0 || this.curandose>0 || quemandose>0)?potenciaNado:1;
                    }

                }else{
                    if(bloqueado_abajo || atorado){
                        this.dy=0;
                    }
                }
                /*
                if(!(bloqueado_abajo || this.atorado || this.nadando>0 || this.curandose>0 || this.quemandose>0) && this.cambiar_y==0){

                }
                */
            }
        }else{
            if(this.saltando)this.dy=0;
            this.saltando=false;
            if(!(bloqueado_abajo || this.atorado)){//gravedad
                
                if(this.dy<limite){
                    this.dy+=1;
                }else if(this.dy>limite){
                    this.dy-=1;
                }
            }else{
                if(bloqueado_abajo || atorado){
                    this.dy=0;
                }
            }
        }
        
        if(!(this.bloqueado_arriba && this.dy<0)) this.subY+=this.dy;
        //System.out.println("dy="+dy);
        this.x=this.subX/seccionadorX;
        this.y=this.subY/seccionadorY;
        
        if(this.disparo==true && this.disparado==false){
            this.disparado=true;
            Juego.weaponAxe.add(new WeaponAxe(this.enemigo,2));
        }
        
        //vida:
        if((curandose-quemandose)!=0 && ((this.health>0 && (curandose-quemandose)>0) || (this.health>0 && (curandose-quemandose)>0))) this.changeHealth(curandose-quemandose);
        
        this.posicion=posicion;
        this.setPosicion(posicion);
        
        cambiar=false;
        //cambiado=false;
        corriendo=false;
        //deslizando=false;
        agacharse=false;
        arriba=false;
        derecha=false;
        izquierda=false;
        espacio=false;
        disparo=false;
        
        
    }
    
    public void piezasBloqueadas(){
        for(int y=0;y<this.cuerpo.length;y++){
            for(int x=0;x<this.cuerpo[0].length;x++){
                String[] superpuestas = this.cuerpo[y][x].split(",");
                for(int i=0;i<superpuestas.length;i++){
                    if(!superpuestas[i].equals("0")){
                        if(bloqueBloqueado(this.positionParts[Integer.parseInt(superpuestas[i])][0][0],this.positionParts[Integer.parseInt(superpuestas[i])][1][0],Integer.parseInt(superpuestas[i]),true)){
                            this.positionParts[Integer.parseInt(superpuestas[i])][0][0]=this.positionParts[Integer.parseInt(superpuestas[i])][0][1];
                            this.positionParts[Integer.parseInt(superpuestas[i])][1][0]=this.positionParts[Integer.parseInt(superpuestas[i])][1][1];
                        }
                    }
                }
            }
        }
    }
    
    public boolean bloqueado(int direccion, boolean corregir){
        //1=izq,2=der,3=arr,4=aba.
        for(int i=0;i<(this.partesBloqueadas.length-1);i++){
            this.partesBloqueadas[i+1][direccion-1]=false;
        }
        boolean bloqueado=false;//para estabilizar el eje x...
        boolean retorno=false;
        for(int i=0;i<(this.positionParts.length-1);i++){
            int x=this.subX+this.positionParts[i+1][0][0];//subx en verdad
            int y=this.subY+this.positionParts[i+1][1][0];//suby en verdad
            if(x<0){
                //System.out.println("this.positionParts[i+1][0][0]="+this.positionParts[i+1][0][0]+",subX="+subX);
                this.subX-=x;
            }
            if(x>(tile.TILE_WIDTH*(Juego.ancho-1)*seccionadorX)){
                this.subX-=(x-(tile.TILE_WIDTH*(Juego.ancho-1)*seccionadorX));
            }
            if(y<0){
                this.subY-=y;
            }
            if(y>(tile.TILE_HEIGHT*(Juego.alto-1)*seccionadorY)){
                this.subY-=(y-(tile.TILE_HEIGHT*(Juego.alto-1)*seccionadorY));
            }
            
            this.x=this.subX/seccionadorX;
            this.y=this.subY/seccionadorY;
            //ahora pasan a ser las coordenadas en pixeles:
            x=this.x+(int)this.positionParts[i+1][0][0]/this.seccionadorX;
            y=this.y+(int)this.positionParts[i+1][1][0]/this.seccionadorX;
            //if((this.positionParts[i+1][0][0]+this.x*this.seccionadorX)<0)System.out.println("x="+(this.positionParts[i+1][0][0]+this.x*this.seccionadorX));
            //posicion aproximada.
            int quad_x=(int)Math.floor((x+(tile.TILE_WIDTH/2))/tile.TILE_WIDTH);
            int quad_y=(int)Math.floor((y+(tile.TILE_HEIGHT/2))/tile.TILE_HEIGHT);
            int pos_quad_x=(x+(tile.TILE_WIDTH)/2)%tile.TILE_WIDTH;//de 0 a 15.
            int pos_quad_y=(y+(tile.TILE_HEIGHT)/2)%tile.TILE_HEIGHT;//de 0 a 15.
            
            int espacio1,espacio2,espacio3,espacio4,espacio5;
            int centro=MapaTiles.mapaFisico[quad_y][quad_x];
            if(centro==1){
                
                if((this.positionParts[i+1][0][0])<(2*tile.TILE_WIDTH)*this.seccionadorX){
                    this.positionParts[i+1][0][0]+=this.incremento;
                }
                if((this.positionParts[i+1][0][0])>(2*tile.TILE_WIDTH)*this.seccionadorX){
                    this.positionParts[i+1][0][0]-=this.incremento;
                }
            }
            if(direccion==1){
                espacio1=(quad_x-1>=0 && quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x-1]:1;
                espacio2=(quad_x-1>=0)?MapaTiles.mapaFisico[quad_y][quad_x-1]:1;
                espacio3=(quad_x-1>=0 && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x-1]:1;
                espacio4=(quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x]:1;
                espacio5=(quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x]:1;
                //espacio5=0;
                //espacio4=0;
                if(pos_quad_x<=(Tile.TILE_WIDTH/2)){
                    if(pos_quad_y==(Tile.TILE_HEIGHT/2) && espacio2==1){
                        //System.out.println("subX="+this.subX+"-->x="+this.x+"-->pp="+this.positionParts[i+1][0][1]);
                        if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_y<(Tile.TILE_HEIGHT/2) && ((espacio1==1 && espacio5==0) || espacio2==1)){
                        if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                        if(pos_quad_x!=(Tile.TILE_WIDTH/2))bloqueado=true;
                    }
                    if(pos_quad_y>(Tile.TILE_HEIGHT/2) && ((espacio3==1 && espacio4==0) || espacio2==1)){
                        if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                        if(pos_quad_x!=(Tile.TILE_WIDTH/2))bloqueado=true;
                    }
                }
            }
            if(direccion==2){
                espacio1=(quad_x+1<Juego.ancho && quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x+1]:1;
                espacio2=(quad_x+1<Juego.ancho)?MapaTiles.mapaFisico[quad_y][quad_x+1]:1;
                espacio3=(quad_x+1<Juego.ancho && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x+1]:1;
                espacio4=(quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x]:1;
                espacio5=(quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x]:1;
                //espacio5=0;
                //espacio4=0;
                if(pos_quad_x>=(Tile.TILE_WIDTH/2)){
                    if(pos_quad_y==(Tile.TILE_HEIGHT/2) && espacio2==1){
                        if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_y<(Tile.TILE_HEIGHT/2) && ((espacio1==1 && espacio5==0) || espacio2==1)){
                        if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                        if(pos_quad_x!=(Tile.TILE_WIDTH/2))bloqueado=true;
                    }
                    if(pos_quad_y>(Tile.TILE_HEIGHT/2) && ((espacio3==1 && espacio4==0) || espacio2==1)){
                        if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                        if(pos_quad_x!=(Tile.TILE_WIDTH/2))bloqueado=true;
                    }
                }
            }
            if(direccion==3){
                espacio1=(quad_x-1>=0 && quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x-1]:1;
                espacio2=(quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x]:1;
                espacio3=(quad_x+1<Juego.ancho && quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x+1]:1;
                espacio4=(quad_x-1>=0)?MapaTiles.mapaFisico[quad_y][quad_x-1]:1;
                espacio5=(quad_x+1<Juego.ancho)?MapaTiles.mapaFisico[quad_y][quad_x+1]:1;
                espacio5=0;
                espacio4=0;
                if(pos_quad_y<=(Tile.TILE_HEIGHT/2)){
                    if(pos_quad_x==(Tile.TILE_WIDTH/2) && espacio2==1){
                        int y2=(int)(Math.floor((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                        //if(corregir) this.subY=(int)(Math.floor((this.y+this.positionParts[i+1][1][1])/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                        //if(corregir && this.y<y2) this.y=y2;
                        if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=y2;
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_x<(Tile.TILE_WIDTH/2) && ((espacio1==1 && espacio4==0) || espacio2==1)){
                        //if(corregir) this.y=(int)Math.floor((this.y+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT;
                        int y2=(int)(Math.floor((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                        //if(corregir && this.y<y2) this.y=y2;
                        if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.y=y2;
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_x>(Tile.TILE_WIDTH/2) && ((espacio3==1 && espacio5==0) || espacio2==1)){
                        //if(corregir) this.y=(int)Math.floor((this.y+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT;
                        int y2=(int)(Math.floor((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                        //if(corregir && this.y<y2) this.y=y2;
                        if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.y=y2;
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                }
            }
            if(direccion==4){
                espacio1=(quad_x-1>=0 && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x-1]:1;
                espacio2=(quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x]:1;
                espacio3=(quad_x+1<Juego.ancho && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x+1]:1;
                espacio4=(quad_x-1>=0)?MapaTiles.mapaFisico[quad_y][quad_x-1]:1;
                espacio5=(quad_x+1<Juego.ancho)?MapaTiles.mapaFisico[quad_y][quad_x+1]:1;
                espacio5=0;
                espacio4=0;
                if(pos_quad_y>=(Tile.TILE_HEIGHT/2)){
                    if(pos_quad_x==(Tile.TILE_WIDTH/2) && espacio2==1){
                        if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(int)(Math.floor((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_x<(Tile.TILE_WIDTH/2) && ((espacio1==1 && espacio4==0) || espacio2==1)){
                        if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(int)(Math.floor((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_x>(Tile.TILE_WIDTH/2) && ((espacio3==1 && espacio5==0) || espacio2==1)){
                        if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(int)(Math.floor((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                }
            }
            this.x=this.subX/seccionadorX;
            this.y=this.subY/seccionadorY;
        }
        this.bloqueado_eje_x=bloqueado;
        return retorno;
    }
}