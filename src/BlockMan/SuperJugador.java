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
public class SuperJugador implements KeyListener {
    
    public int index=0;//indice del tile con el que se dibuja al jugador.
    
    public int potencia_wall_jump_x=10,dx_wall_jump,x,x_anterior,y,inicioX,inicioY,dx,dy,limite,jugador,subX,subY,posicion,posicionAnterior,posicionCaminando,incremento,incrementoCorriendo,incrementoAire,fase_salto,posCamX,posCamY,nadando,quemandose,curandose,direccion;//,cambiar_y
    
    //variables de posicionamiento y movimiento:
    public int seccionador=(4/(Tile.TILE_WIDTH/4));//min 1 cuando es 16 el tamaño del tile, max 4 cuando es 4 el tamaño del tile.
    public int seccionadorX=4*seccionador,seccionadorY=4*seccionador;
    //seccionador: es inversamente proporcional al tamaño del tile
    
    //cuerpo del jugador:
    public String nada,cabeza,torso,cintura,brazoIzq,brazoDer,pieIzq,pieDer;
    
    //posiciones de las partes del cuerpo del jugador:
    public int[][][] positionParts;//la posicion dentro del jugador en el eje y usa el seccionador del eje x
    //porque la velocidad debe ser constante al cambiar de posicion para ambos ejes
    
    public boolean[][] partesBloqueadas;
    
    public boolean puede_saltar,dirigida,impulso,fase_completa,fase_completa_real,retraso,atorado,tocandoLava,tocandoAgua,bloqueado_eje_x=false;//dirigida: sigue al jugador,impulso: el jugador la potencia con solo tocarla.
    
    public boolean espacio,arriba,izquierda,derecha,agacharse,disparo,cambiar,corriendo,disparado,cambiado,saltando,saltando2,deslizando;
    
    //bloqueos:
    public boolean bloqueado_izquierda,bloqueado_derecha,bloqueado_arriba,bloqueado_abajo,brazoDerBloqueado,brazoIzqBloqueado;
    
    public static Tile tile=new Tile();
    
    public String[][] cuerpo=new String[4][5];
    
    public int vidas,limiteVidas=3,baseHealth=16,healthMultiplier=64,health=healthMultiplier*baseHealth,healthLimit=healthMultiplier*baseHealth;
    
    //energia del arma:
    public int energy=0,energyLimit=8;//retraso de ataque.
    
    public String arma_actual="bullet";//bullet,axe.d
    
    //municiones:
    public int axeMunition=0,misileMunition=0;
    
    
    //checkPoint:
    public int checkPointIndex=0;
    
    public int teclaIzquierda;
    public int teclaDerecha;
    public int teclaDisparo;
    public int teclaSalto;
    public int teclaAgacharse;
    public int teclaArriba;
    public int teclaCorriendo;
    public int teclaCambiar;
    
    //variables de control:
    public boolean vladimir=false;//hasta abajo.
    public boolean bounce=false;//rebotar.
    public boolean deslizar=false;//su nombre lo dice.
    public boolean wall_jump=true;//saltar por las paredes.
    public boolean salto_continuo=true;//que salte continuamente manteniendo presionado saltar.
    public boolean siempreCorriendo=false;//correr sin apretar correr.
    public boolean rebotar_eje_y=false;//rebotar hacia abajo al tocar el techo.
    public boolean deadDimension=false;//si esta en la dimension de los muertos.
    public boolean alive=true;//si esta vivo o muerto.
    
    public int min_dy_wall_jump=-2;//delta y minimo para volver a saltar en una pared (al caer).
    
    
    public SuperJugador(int x, int y, int jugador){
        
        switch(jugador){
            case 0:
                this.index=3;
                break;
            case 1:
                this.index=5;
                break;
            case 2:
                this.index=7;
                break;
            case 3:
                this.index=9;
                break;
        }
        
        this.jugador=jugador;//0 a 3.
        
        this.setBounce(false);
        
        
        this.vidas=this.limiteVidas;
        this.direccion=2;
        this.retraso=false;
        this.incremento=(tile.TILE_WIDTH*seccionador);//incremento en subPixeles
        this.incrementoCorriendo=(tile.TILE_WIDTH*seccionador)*2;//incremento en subPixeles
        this.incrementoAire=1;
        this.inicioX=x*tile.TILE_WIDTH;
        this.inicioY=y*tile.TILE_HEIGHT;
        this.x=x*tile.TILE_WIDTH;
        this.y=y*tile.TILE_HEIGHT;
        this.subX=x*tile.TILE_WIDTH*seccionadorX;
        this.subY=y*tile.TILE_HEIGHT*seccionadorY;
        this.dx=0;
        this.dx_wall_jump=0;
        this.dy=0;
        this.dirigida=true;
        this.impulso=false;
        this.fase_completa=false;
        this.fase_completa_real=false;
        this.atorado=false;
        this.posicionCaminando=2;//1 o 2 (posicion quieto o caminando).
        //this.cambiar_y=0;
        this.nadando=0;
        this.quemandose=0;
        this.tocandoAgua=false;
        this.tocandoLava=false;
        
        /*
        DESCRIPCION:
        cambios de posicion:
        el jugador es una especie de matriz en donde cada casilla que esten ocupadas se pueden superponer.
        cada casilla ocupada ademas tiene un identificador para diferenciarlas.
        cada posicion distinta indica el lugar que ocupa un bloque determinado por el identificador.
        al cambiar de posicion los bloques la adoptaran moviendose 4 o mas veces hasta su nuevo destino.
        el numerode veces que se mueven se puede cambiar en la variable numero_cambios.
        si el jugador es bloqueado durante esta transformacion, esta quedara inconclusa (no progresara mas).
        
        otra opcion es hacer que las piezas se muevan a una velocidad constante, por ejemplo:
        TILE_WIDTH/4(en el eje x),TILE_HEIGHT/4(en el eje y)
        que siempre se este intentando mover las piezas que no han llegado a su destino, es decir, sin frames.
        */
        
        //variables del jugador:
        
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
        
        this.posicion=11;
        
        this.bloqueado_izquierda=true;
        this.bloqueado_derecha=true;
        this.bloqueado_arriba=true;
        this.bloqueado_abajo=true;
        
        this.teclaIzquierda=Teclas.teclas[this.jugador][0];
        this.teclaDerecha=Teclas.teclas[this.jugador][1];
        this.teclaDisparo=Teclas.teclas[this.jugador][5];
        this.teclaSalto=Teclas.teclas[this.jugador][4];
        this.teclaAgacharse=Teclas.teclas[this.jugador][2];
        this.teclaArriba=Teclas.teclas[this.jugador][3];
        this.teclaCorriendo=Teclas.teclas[this.jugador][6];
        this.teclaCambiar=Teclas.teclas[this.jugador][7];
        
        setPosicion(this.posicion,true);
    }
    
    public void setBounce(boolean bounce){
        this.bounce=bounce;
        if(bounce){
            this.index=28;
        }else{
            switch(jugador){
                case 0:
                    this.index=3;
                    break;
                case 1:
                    this.index=5;
                    break;
                case 2:
                    this.index=7;
                    break;
                case 3:
                    this.index=9;
                    break;
            }
        }
    }
    
    public void setPosCuerpo(boolean instantaneo){//posiciona las piezas de acuerdo a la nueva posicion (gradualmente).
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
            if(!instantaneo){
                this.bloqueado_arriba=bloqueado(3,true,false);
                this.bloqueado_abajo=bloqueado(4,true,false);
                this.bloqueado_izquierda=bloqueado(1,false,false);
                this.bloqueado_derecha=bloqueado(2,false,false);
                
                this.bloqueado_arriba=this.bloqueado_arriba || bloqueadoOtrosBloques(3,true);
                this.bloqueado_abajo=this.bloqueado_abajo || bloqueadoOtrosBloques(4,true);
                this.bloqueado_izquierda=this.bloqueado_izquierda || bloqueadoOtrosBloques(1,true);
                this.bloqueado_derecha=this.bloqueado_derecha || bloqueadoOtrosBloques(2,true);
                
            }
            this.brazoIzqBloqueado=this.partesBloqueadas[Integer.parseInt(this.brazoIzq)][0];
            this.brazoDerBloqueado=this.partesBloqueadas[Integer.parseInt(this.brazoDer)][1];
            for(int y=0;y<this.cuerpo.length;y++){
                for(int x=0;x<this.cuerpo[0].length;x++){
                    String[] superpuestas = this.cuerpo[y][x].split(",");
                    for(int i=0;i<superpuestas.length;i++){
                        
                        if(!superpuestas[i].equals("0")){
                            if(instantaneo==true){
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
                            
                            //this.x=this.subX/this.seccionadorX;
                            //this.y=this.subY/this.seccionadorY;
                            
                            this.positionParts[Integer.parseInt(superpuestas[i])][0][1]=this.positionParts[Integer.parseInt(superpuestas[i])][0][0]/this.seccionadorX;
                            this.positionParts[Integer.parseInt(superpuestas[i])][1][1]=this.positionParts[Integer.parseInt(superpuestas[i])][1][0]/this.seccionadorX;
                            
                            quad_x=(int)Math.floor(((this.x+this.positionParts[Integer.parseInt(superpuestas[i])][0][0]/this.seccionadorX+(tile.TILE_WIDTH/2))/tile.TILE_WIDTH));
                            quad_y=(int)Math.floor(((this.y+this.positionParts[Integer.parseInt(superpuestas[i])][1][0]/this.seccionadorX+(tile.TILE_HEIGHT/2))/tile.TILE_HEIGHT));
                            //System.out.println("qx="+quad_x+"qy="+quad_y);
                            //System.out.println("x="+MapaTiles.mapaFisico[quad_y][quad_x]);
                            if(!instantaneo){
                                centro=MapaTiles.mapaFisico[quad_y][quad_x];
                                //comprobar estrella:
                                if(Teclas.estrellaX==quad_x && Teclas.estrellaY==quad_y && this.deadDimension==false){
                                    Juego.corriendo=false;
                                }
                                //comprobar si esta nadando o quemandose:
                                if(centro==5) nadando++;
                                if(centro==4) nadando++;
                                if(centro==2) quemandose++;
                                if(centro==3) curandose++;
                            }
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
        this.fase_completa_real=fase_completa;
        this.nadando=nadando;
        this.quemandose=quemandose;
        this.curandose=curandose;
    }
    
    public void reaparecer(){
        this.vidas=this.limiteVidas;
        this.deadDimension=false;
        this.alive=true;
        this.health=this.healthLimit;
        this.posicion=11;
        this.setPosCuerpo(true);
        this.checkPointIndex=0;
        if(this.checkPointIndex!=0){
            this.x=MapaTiles.punto.get(this.checkPointIndex-1).posicionPuntoX-(2*Tile.TILE_WIDTH);
            this.y=MapaTiles.punto.get(this.checkPointIndex-1).posicionPuntoY-(2*Tile.TILE_HEIGHT);
            this.subX=this.x*this.seccionadorX;
            this.subY=this.y*this.seccionadorY;
        }else{
            this.x=this.inicioX*tile.TILE_WIDTH;
            this.y=this.inicioY*tile.TILE_HEIGHT;
            this.subX=this.inicioX*tile.TILE_WIDTH*this.seccionadorX;
            this.subY=this.inicioY*tile.TILE_HEIGHT*this.seccionadorY;
        }
    }
    
    public void changeHealth(int health){
        if(this.alive){
            this.health+=health;
            if(this.health>healthLimit) this.health=healthLimit;
            if(this.health<=0){
                this.vidas--;
                if(vidas<=0){
                    vidas=0;
                    this.health=0;
                    this.deadDimension=true;
                    this.alive=false;
                }else{
                    this.deadDimension=false;
                    this.alive=true;
                    this.health=this.healthLimit;
                    this.posicion=11;
                    this.setPosCuerpo(true);
                    if(this.checkPointIndex!=0){
                        this.x=MapaTiles.punto.get(this.checkPointIndex-1).posicionPuntoX-(2*Tile.TILE_WIDTH);
                        this.y=MapaTiles.punto.get(this.checkPointIndex-1).posicionPuntoY-(2*Tile.TILE_HEIGHT);
                        this.subX=this.x*this.seccionadorX;
                        this.subY=this.y*this.seccionadorY;
                    }else{
                        this.x=this.inicioX*tile.TILE_WIDTH;
                        this.y=this.inicioY*tile.TILE_HEIGHT;
                        this.subX=this.inicioX*tile.TILE_WIDTH*this.seccionadorX;
                        this.subY=this.inicioY*tile.TILE_HEIGHT*this.seccionadorY;
                    }
                }
            }
        }
    }
    
    public void changeAxeMunition(int munition){
        this.axeMunition+=munition;
        if(this.axeMunition<=0){
            this.axeMunition=0;
            this.arma_actual="bullet";
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
    
    public void setPosicion(int posicion,boolean instantaneo){
        this.posicionAnterior=this.posicion;
        this.posicion=posicion;
        switch(posicion){
            case 0://normal izq:
                this.cuerpo[0][0]=this.nada;
                this.cuerpo[0][1]=this.nada;
                this.cuerpo[0][2]=this.cabeza;
                this.cuerpo[0][3]=this.nada;
                this.cuerpo[0][4]=this.nada;
                
                this.cuerpo[1][0]=this.nada;
                this.cuerpo[1][1]=this.brazoDer;
                this.cuerpo[1][2]=this.torso;
                this.cuerpo[1][3]=this.brazoIzq;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.pieDer;
                this.cuerpo[3][2]=this.nada;
                this.cuerpo[3][3]=this.pieIzq;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 1://normal der:
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
                this.cuerpo[1][1]=this.brazoDer;
                this.cuerpo[1][2]=this.torso+","+brazoIzq;
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
                this.cuerpo[1][1]=this.brazoDer;
                this.cuerpo[1][2]=this.torso+","+brazoIzq;
                this.cuerpo[1][3]=this.nada;
                this.cuerpo[1][4]=this.nada;
                
                this.cuerpo[2][0]=this.nada;
                this.cuerpo[2][1]=this.nada;
                this.cuerpo[2][2]=this.cintura;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.nada;
                this.cuerpo[3][2]=this.pieDer;
                this.cuerpo[3][3]=this.pieIzq;
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
            case 7://agacharse izq:
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
                    this.cuerpo[2][1]=this.pieDer+","+this.brazoDer;
                    this.cuerpo[2][2]=this.cintura+","+this.torso+","+this.cabeza;
                    this.cuerpo[2][3]=this.pieIzq+","+this.brazoIzq;
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
                    this.cuerpo[1][1]=this.brazoDer;
                    this.cuerpo[1][2]=this.torso+","+this.cabeza;
                    this.cuerpo[1][3]=this.brazoIzq;
                    this.cuerpo[1][4]=this.nada;
                    
                    this.cuerpo[2][0]=this.nada;
                    this.cuerpo[2][1]=this.pieDer;
                    this.cuerpo[2][2]=this.cintura;
                    this.cuerpo[2][3]=this.pieIzq;
                    this.cuerpo[2][4]=this.nada;
                    
                    this.cuerpo[3][0]=this.nada;
                    this.cuerpo[3][1]=this.nada;
                    this.cuerpo[3][2]=this.nada;
                    this.cuerpo[3][3]=this.nada;
                    this.cuerpo[3][4]=this.nada;
                }
                
                
                break;
            case 8://agacharse der:
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
            case 9://deslizar izq:
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
                
                this.cuerpo[2][0]=this.brazoDer;
                this.cuerpo[2][1]=this.torso;
                this.cuerpo[2][2]=this.brazoIzq;
                this.cuerpo[2][3]=this.nada;
                this.cuerpo[2][4]=this.nada;
                
                this.cuerpo[3][0]=this.nada;
                this.cuerpo[3][1]=this.pieDer;
                this.cuerpo[3][2]=this.cintura;
                this.cuerpo[3][3]=this.pieIzq;
                this.cuerpo[3][4]=this.nada;
                
                break;
            case 10://deslizar der:
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
            case 11://cuadradito:
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
        //la posicion del jugador tendra fases, cuando se complete una fase para una accion, podra continuar con la siguiente.
        setPosCuerpo(instantaneo);//siempre se actualiza la posicion del jugador, las piezas siempre estaran en movimiento.
        
        if(this.corriendo){
            setPosCuerpo(instantaneo);
        }
        
    }
    
    public void actualizar(){
        
        if(this.fase_completa==true && this.x!=this.x_anterior){
            if(posicionCaminando==1){
                posicionCaminando=2;
            }else{
                posicionCaminando=1;
            }
        }else if(this.x==this.x_anterior){
            //posicionCaminando=2;
        }
        this.x_anterior=this.x;
        
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
        if(this.bounce) incremento=this.incrementoAire*2;
        if((this.nadando>0 || this.curandose>0 || this.quemandose>0) && !this.bounce) incremento/=2;//se mueve a la mitad de la velocidad en el eje x.
        //System.out.println("incremento="+incremento);
        
        int posicion;
        if(this.direccion==1){
            posicion=0;//izq
        }else{
            posicion=1;//der
        }
        
        if(this.posicion==11 && !this.vladimir) posicion=11;//es un cuadrado hasta q aprete una direccion.
        
        if((this.posicion==2 && bloqueado_izquierda) || (this.posicion==3 && bloqueado_derecha)){
            posicion=this.posicion;
        }
        
        if((this.bloqueado_abajo || ((!this.derecha && !this.izquierda)) || this.dx_wall_jump!=0) && !this.bounce){//resetear el dx...
            this.dx=0;
            //this.dx=this.dx_wall_jump;
        }
        
        if(!this.espacio || (this.dx_wall_jump>0 && bloqueado_derecha) || (this.dx_wall_jump<0 && bloqueado_izquierda)){
            this.dx_wall_jump=0;
        }
        
        //variables que verifican que si esta bloqueado y no se mueve se corriga las posiciones dentro del personaje:
        int x_inicial=this.x,y_inicial=this.y;
        if(((this.derecha || this.izquierda || this.deslizando) && !(this.derecha && this.izquierda)) || this.dx_wall_jump!=0){
            int i=0;
            while(i<veces){
                //corregir y:(deben estar en este orden para q se corriga el eje y, y luego se compruebe el x).
                this.bloqueado_izquierda=bloqueado(1,true,false);
                this.bloqueado_derecha=bloqueado(2,true,false);
                this.bloqueado_arriba=bloqueado(3,true,false);
                this.bloqueado_abajo=bloqueado(4,true,false);
                
                this.bloqueado_arriba=this.bloqueado_arriba || bloqueadoOtrosBloques(3,true);
                this.bloqueado_abajo=this.bloqueado_abajo || bloqueadoOtrosBloques(4,true);
                this.bloqueado_izquierda=this.bloqueado_izquierda || bloqueadoOtrosBloques(1,true);
                this.bloqueado_derecha=this.bloqueado_derecha || bloqueadoOtrosBloques(2,true);
                
                if(this.izquierda==true || (this.deslizando && this.direccion==1)){
                    this.direccion=1;
                    if((!this.fase_completa==true || !this.retraso) && bloqueado_izquierda==false) if(this.bounce) this.dx-=incrementoAire*2; else this.dx=-incremento;
                    if(!bloqueado_abajo){
                        if(bloqueado_izquierda){
                            posicion=2;
                        }else{
                            posicion=4;
                        }
                    }else{
                        if(posicionCaminando==1 && !this.brazoIzqBloqueado && !this.bloqueado_izquierda){// && !atorado && !bloqueado_izquierda
                            posicion=0;
                        }else{
                            if(this.brazoIzqBloqueado && !this.partesBloqueadas[Integer.parseInt(this.cintura)][0] && !this.partesBloqueadas[Integer.parseInt(this.pieDer)][0]){
                                posicion=6;
                            }else{
                                posicion=2;
                            }
                        }
                    }
                }
                if(this.derecha==true || (this.deslizando && this.direccion==2)){
                    this.direccion=2;
                    if((!this.fase_completa==true || !this.retraso) && bloqueado_derecha==false) if(this.bounce) this.dx+=this.incrementoAire*2; else this.dx=incremento;
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
                            if(this.brazoDerBloqueado && !this.partesBloqueadas[Integer.parseInt(this.cintura)][1] && !this.partesBloqueadas[Integer.parseInt(this.pieIzq)][1]){
                                posicion=6;
                            }else{
                                posicion=3;
                            }
                        }
                    }
                }
                if(this.wall_jump){
                    if(this.dx_wall_jump!=0 && !((this.dx>0 && this.dx_wall_jump>0) || (this.dx<0 && this.dx_wall_jump<0))){
                        //if(this.dx_wall_jump>0) this.dx=((this.dx_wall_jump>=8)?(i==0?(8):(this.dx_wall_jump-8)):(i==0?(this.dx_wall_jump):0));
                        //if(this.dx_wall_jump<0) this.dx=((this.dx_wall_jump<=-8)?(i==0?(-8):(this.dx_wall_jump+8)):(i==0?(this.dx_wall_jump):0));
                        this.dx=this.dx_wall_jump;
                        
                    }
                    this.subX+=this.dx;
                    
                    if(i==0){
                        if(this.dx_wall_jump>0){
                            this.dx_wall_jump--;
                        }else if(this.dx_wall_jump<0){
                            this.dx_wall_jump++;
                        }
                    }
                }else{
                    this.subX+=this.dx;
                }
                i++;
            }
        }else{
            if(this.nadando>0 || this.quemandose>0 || this.curandose>0){
                if(this.bounce){
                    if(this.dx>0){
                        this.dx-=1;
                    }
                    if(this.dx<0){
                        this.dx+=1;
                    }
                }
            }
            //corregir y:(deben estar en este orden para q se corriga el eje y, y luego se compruebe el x).
            this.bloqueado_izquierda=bloqueado(1,true,false);
            this.bloqueado_derecha=bloqueado(2,true,false);
            this.bloqueado_arriba=bloqueado(3,true,false);
            this.bloqueado_abajo=bloqueado(4,true,false);
            if((!bloqueado_izquierda && !bloqueado_derecha) && arriba==true && this.fase_completa_real){//corregir desfase:
                if(bloqueado_abajo){
                    this.subY+=this.seccionadorX;
                    bloqueado(1,true,true);
                    bloqueado(2,true,true);
                    this.subY-=this.seccionadorX;
                }
                if(bloqueado_arriba){
                    this.subY-=this.seccionadorX;
                    bloqueado(1,true,true);
                    bloqueado(2,true,true);
                    this.subY-=this.seccionadorX;
                }
            }
        }
        
        
        if(this.bounce){
            if(this.dx>this.incrementoCorriendo){
                this.dx=this.incrementoCorriendo;
            }
            if(this.dx<-this.incrementoCorriendo){
                this.dx=-this.incrementoCorriendo;
            }
            if(this.nadando>0 || this.quemandose>0 || this.curandose>0){
                if(this.dx>this.incremento){
                    this.dx-=1;
                }
                if(this.dx<-this.incremento){
                    this.dx+=1;
                }
            }
            if(bloqueado_derecha && this.dx!=0){
                this.dx=-Math.abs(this.dx)+((this.derecha || this.izquierda)?0:1);
            }
            if(bloqueado_izquierda && this.dx!=0){
                this.dx=Math.abs(this.dx)-((this.derecha || this.izquierda)?0:1);
            }
            int signo=(this.dx<0?-1:(this.dx>0?1:0));
            if((bloqueado_abajo || bloqueado_arriba) && !(this.derecha || this.izquierda)) this.dx-=signo;
            if(this.dx>Tile.TILE_WIDTH/2 || this.dx<-Tile.TILE_WIDTH/2){
                int dxaux=this.dx;
                this.subX+=Tile.TILE_WIDTH/2*signo;
                this.x=subX/Tile.TILE_WIDTH;
                this.bloqueado_izquierda=bloqueado(1,true,false);
                this.bloqueado_derecha=bloqueado(2,true,false);
                dxaux-=Tile.TILE_WIDTH/2*signo;
                if(bloqueado_derecha && this.dx!=0){
                    this.dx=-Math.abs(this.dx)+((this.derecha || this.izquierda)?0:1);
                    dxaux=-Math.abs(dxaux)+((this.derecha || this.izquierda)?0:1);
                }
                if(bloqueado_izquierda && this.dx!=0){
                    this.dx=Math.abs(this.dx)-((this.derecha || this.izquierda)?0:1);
                    dxaux=Math.abs(dxaux)-((this.derecha || this.izquierda)?0:1);
                }
                this.subX+=dxaux;
                this.x=subX/Tile.TILE_WIDTH;
            }else{
                this.subX+=this.dx;
            }
        }
        
        if(this.arriba){
            posicion=6;
        }
        
        if(this.deslizando){
            if(this.direccion==1){
                posicion=9;//izq
            }else{
                posicion=10;//der
            }
        }
        if(this.agacharse){
            if(this.direccion==1){
                posicion=7;//izq
            }else{
                posicion=8;//der
            }
        }
        if(this.arriba && this.agacharse && this.vladimir){
            posicion=11;
        }
        //cambiar esto cuando el personaje se mueva en una funcion aparte:
        if(x_inicial==this.x && y_inicial==this.y && this.bloqueado_eje_x){
            //System.out.println("bloqueado");
        }
        
        int potencia=32;//(tile.TILE_WIDTH*seccionador)*2;
        int limite=32;//(tile.TILE_WIDTH*seccionador)*2;
        if((this.derecha && this.bloqueado_derecha) || (izquierda && this.bloqueado_izquierda)){
            limite/=2;//deslizandose sobre una pared...
        }
        
        int potenciaNado=0;
        if(this.nadando>0 || this.curandose>0 || this.quemandose>0){//mientras mas sumergido esta mas potencia de nado tendra.
            //ahora la potencia siempre sera la máxima, pero si supera el limite, disminuira gradualmente.//potencia=(int)((this.nadando+this.quemandose)/2);
            potenciaNado=((int)((this.nadando+this.quemandose+this.curandose)/2));
            //limite=limite-((int)((this.nadando+this.quemandose)*2));//(int)((this.nadando+this.quemandose)/2);
            //potenciaNado=((int)((this.nadando+this.quemandose)/2));
            //limite=limite-((int)((this.nadando+this.quemandose)/2));//(int)((this.nadando+this.quemandose)/2);
            limite=limite-8;
        }else{
            if(!this.bounce && false) this.subX=(int)Math.floor((this.subX)/(incremento))*incremento;//se ajusta al incremento normal
            /*
            if(this.corriendo){
                this.subX=(int)Math.floor((this.subX)/8)*8;//se ajusta a un medio del tile.
            }else{
                this.subY=(int)Math.floor((this.subY)/4)*4;//se ajusta a un cuarto del tile.
            }
            */
        }
        
        if(this.bounce && this.agacharse && this.dy<0) this.dy+=1;
         
        if(this.dy>0){
            //this.saltando=false;
            saltando2=false;
            this.puede_saltar=false;
            if(bloqueado_abajo){
                if(this.bounce && this.dy!=0){
                    this.dy=-Math.abs(this.dy)+1;
                }else{
                    this.dy=0;
                }
            }
        }

        if(bloqueado_arriba){
            this.puede_saltar=false;
            //this.saltando=false;
            saltando2=false;
            if(this.dy<0){
                if(bloqueado_abajo){
                    this.dy=0;
                }else{
                    if(this.rebotar_eje_y || this.bounce){
                         this.dy=Math.abs(this.dy)-1;
                    }else{
                        this.dy=0;
                    }
                }
            }
        }
        boolean primer_salto=false;
        if(this.bloqueado_abajo && !this.espacio) this.puede_saltar=false;
        if(this.saltando==true && this.espacio){
            saltando=false;
            this.puede_saltar=true;
            primer_salto=true;
        }
        
        if(this.espacio && ((!this.salto_continuo && (this.puede_saltar)) || this.salto_continuo)){
            
            if((bloqueado_abajo || (wall_jump && (bloqueado_derecha || bloqueado_izquierda) && ((primer_salto || (salto_continuo)) && this.dy>=this.min_dy_wall_jump)) || this.nadando>0 || this.curandose>0 || this.quemandose>0)){//this.saltando==false && 

                if(bloqueado_abajo || (wall_jump && (bloqueado_derecha || bloqueado_izquierda) && ((primer_salto || (salto_continuo)) && this.dy>=this.min_dy_wall_jump))){
                    if(!bloqueado_abajo && (wall_jump && (bloqueado_derecha || bloqueado_izquierda))){
                        if(bloqueado_derecha && !bloqueado_izquierda){
                            this.dx_wall_jump=-this.potencia_wall_jump_x;
                        }
                        if(bloqueado_izquierda && !bloqueado_derecha){
                            this.dx_wall_jump=this.potencia_wall_jump_x;
                        }
                        this.dy=-potencia;
                    }else{
                        this.dy=-potencia;
                    }
                }else{
                    if(this.dy<-limite){
                        if(this.nadando>0 || this.curandose>0 || this.quemandose>0){
                            this.dy+=potenciaNado;//si la velocidad supera el limite decrece gradualmente.
                        }else{
                            this.dy+=this.incrementoAire*2;//si la velocidad supera el limite decrece gradualmente.
                        }
                    }else{
                        if(this.nadando>0 || this.curandose>0 || this.quemandose>0){
                            this.dy-=potenciaNado;//si no, aumenta gradualmente.
                            if(!(bloqueado_abajo || this.atorado) && potenciaNado<=0){//gravedad
                                if(this.dy<limite){
                                    //if(!this.fase_completa_real && bloqueado_derecha && bloqueado_izquierda) this.dy-=(this.dy<0?-1:(this.dy>0?1:0)); else 
                                    this.dy+=this.incrementoAire*2;
                                    //System.out.println(!this.fase_completa_real+"-"+bloqueado_derecha+"-"+bloqueado_izquierda);
                                }else if(this.dy>limite){
                                    this.dy-=this.incrementoAire*2;
                                }
                            }else{
                                if(bloqueado_abajo || atorado){
                                    if(this.bounce){
                                        this.dy=-Math.abs(this.dy);
                                    }else{
                                        this.dy=0;
                                    }
                                }
                            }
                        }else{//gravedad
                            if(!(bloqueado_abajo || this.atorado)){
                                if(this.dy<limite){
                                    //if(!this.fase_completa_real && bloqueado_derecha && bloqueado_izquierda) this.dy-=(this.dy<0?-1:(this.dy>0?1:0)); else
                                    this.dy+=this.incrementoAire*2;
                                }else if(this.dy>limite){
                                    this.dy-=this.incrementoAire*2;
                                }
                            }else{
                                if(bloqueado_abajo || atorado){
                                    if(this.bounce){
                                        this.dy=-Math.abs(this.dy);
                                    }else{
                                        this.dy=0;
                                    }
                                }
                            }
                        }
                    }
                }
                this.saltando2=true;
            }else{//gravedad
                if(!(bloqueado_abajo || this.atorado)){
                    
                    if(this.dy<limite){
                        //if(!this.fase_completa_real && bloqueado_derecha && bloqueado_izquierda) this.dy-=(this.dy<0?-1:(this.dy>0?1:0)); else 
                        this.dy+=(nadando>0 || this.curandose>0 || quemandose>0)?potenciaNado:this.incrementoAire*2;
                    }else if(this.dy>limite){
                        this.dy-=(nadando>0 || this.curandose>0 || quemandose>0)?potenciaNado:this.incrementoAire*2;
                    }

                }else{
                    if(bloqueado_abajo || atorado){
                        if(this.bounce){
                            this.dy=-Math.abs(this.dy);
                        }else{
                            this.dy=0;
                        }
                    }
                }
                /*
                if(!(bloqueado_abajo || this.atorado || this.nadando>0 || this.curandose>0 || this.quemandose>0) && this.cambiar_y==0){

                }
                */
            }
        }else{
            if(saltando2 && !this.bounce)this.dy=0;
            if(!(bloqueado_abajo || this.atorado)){//gravedad
                
                if(this.dy<limite){
                    //if(!this.fase_completa_real && bloqueado_derecha && bloqueado_izquierda) this.dy-=(this.dy<0?-1:(this.dy>0?1:0)); else 
                    this.dy+=this.incrementoAire*2;
                }else if(this.dy>limite){
                    this.dy-=this.incrementoAire*2;
                }
            }else{
                if(bloqueado_abajo || atorado){
                    if(this.bounce){
                        this.dy=-Math.abs(this.dy);
                    }else{
                        this.dy=0;
                    }
                }
            }
        }
        
        if(!(this.bloqueado_arriba && this.dy<0)) this.subY+=this.dy;
        //System.out.println("dy="+dy);
        this.x=this.subX/seccionadorX;
        this.y=this.subY/seccionadorY;
        
        //cambio de arma:
        if(this.cambiar==true && this.cambiado==false){
            this.cambiado=true;
            switch(this.arma_actual){
                case "bullet":
                    if(this.axeMunition>0){
                        this.arma_actual="axe";
                    }else{
                        this.arma_actual="bullet";
                    }
                    break;
                case "axe":
                    this.arma_actual="bullet";
                    break;
            }
        }
        
        
        if(this.disparo==true && this.energy>=energyLimit){// && this.disparado==false
            this.disparado=true;
            switch(this.arma_actual){
                case "bullet":
                    Juego.weaponBullet.add(new WeaponBullet(this.jugador,1));
                    this.energy=0;
                    break;
                case "axe":
                    Juego.weaponAxe.add(new WeaponAxe(this.jugador,1));
                    this.changeAxeMunition(-1);
                    this.energy=0;
                    break;
            }
        }
        
        //vida:
        this.changeHealth((curandose-quemandose)*2);
        
        if(this.energy>=this.energyLimit){
            this.energy=this.energyLimit;
        }else{
            this.energy++;
        }
        
        /*
        //corregir y:(deben estar en este orden para q se corriga el eje y, y luego se compruebe el x).
        this.bloqueado_arriba=bloqueado(3,true);
        this.bloqueado_abajo=bloqueado(4,true);
        this.bloqueado_izquierda=bloqueado(1,true);
        this.bloqueado_derecha=bloqueado(2,true);
        */
        this.posicion=posicion;
        
        this.setPosicion(posicion,false);
        
        /*
        this.x=this.subX/this.seccionador;
        this.y=this.subY/this.seccionador;
        */
        
    }
    
    /*actualizar antiguo:
    public void actualizar(){
        this.cambiar_y++;
        if(this.cambiar_y>2){
            this.cambiar_y=0;
        }
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
            veces=2;//incremento=this.incrementoCorriendo;
        }
        
        if(this.nadando>0 || this.curandose>0 || this.quemandose>0) incremento/=2;//se mueve a la mitad de la velocidad en el eje x.
        //System.out.println("incremento="+incremento);
        int posicion=1;
        if((this.posicion==2 && bloqueado_izquierda) || (this.posicion==3 && bloqueado_derecha)){
            posicion=this.posicion;
        }
        
        if((this.derecha || this.izquierda) && !(this.derecha && this.izquierda)){
            if(this.izquierda==true){
                this.direccion=1;
                if((!this.fase_completa==true || !this.retraso) && bloqueado_izquierda==false) this.x-=incremento;
                if(veces==2){
                    this.bloqueado_izquierda=bloqueado(1,false);
                    this.bloqueado_derecha=bloqueado(2,false);
                    if((!this.fase_completa==true || !this.retraso) && bloqueado_izquierda==false) this.x-=incremento;
                }
                if(!bloqueado_abajo){
                    if(bloqueado_izquierda){
                        posicion=2;
                    }else{
                        posicion=4;
                    }
                }else{
                    if(posicionCaminando==1 && !atorado && !bloqueado_izquierda){
                        posicion=1;
                    }else{
                        posicion=2;
                    }
                }
            }
            if(this.derecha==true){
                this.direccion=2;
                if((!this.fase_completa==true || !this.retraso) && bloqueado_derecha==false) this.x+=incremento;
                if(veces==2){
                    this.bloqueado_izquierda=bloqueado(1,false);
                    this.bloqueado_derecha=bloqueado(2,false);
                    if((!this.fase_completa==true || !this.retraso) && bloqueado_derecha==false) this.x+=incremento;
                }
                if(!bloqueado_abajo){
                    if(bloqueado_derecha){
                        posicion=3;
                    }else{
                        posicion=5;
                    }
                }else{
                    if(posicionCaminando==1 && !atorado && !bloqueado_derecha){
                        posicion=1;
                    }else{
                        posicion=3;
                    }
                }
            }
        }
        
        if(this.arriba){
            posicion=6;
        }
        if(this.agacharse){
            posicion=7;
        }
        if(this.arriba && this.agacharse && this.vladimir){
            posicion=10;
        }
        
        int potencia=8;
        int limite=8;
        int potenciaNado=0;
        if(this.nadando>0 || this.curandose>0 || this.quemandose>0){//mientras mas sumergido esta mas potencia de nado tendra.
            //ahora la potencia siempre sera la máxima, pero si supera el limite, disminuira gradualmente.//potencia=(int)((this.nadando+this.quemandose)/2);
            potenciaNado=((int)((this.nadando+this.quemandose)/2));
            limite=limite-((int)((this.nadando+this.quemandose)/2));//(int)((this.nadando+this.quemandose)/2);
        }else{
            if(this.corriendo){
                this.x=(int)Math.floor((this.x)/8)*8;//se ajusta a un medio del tile.
            }else{
                this.x=(int)Math.floor((this.x)/4)*4;//se ajusta a un cuarto del tile.
            }
        }
        
        boolean salto=false;
        int restoSalto=0;
        while(salto==false){
            
            if(restoSalto!=0){
                //System.out.println("restoSalto="+restoSalto);
            }
            
            if(this.dy>0){
                this.saltando=false;
                if(bloqueado_abajo){
                    this.dy=0;
                }
            }

            if(bloqueado_arriba){
                this.saltando=false;
                if(bloqueado_abajo){
                    this.dy=0;
                }else{
                    this.dy=-this.dy;
                }
            }
            if(restoSalto==0){
                if(this.espacio){
                    if((bloqueado_abajo || this.nadando>0 || this.curandose>0 || this.quemandose>0)){//this.saltando==false && 

                        if(bloqueado_abajo){
                            if(restoSalto==0) {
                                this.dy=-potencia;
                                this.cambiar_y=0;
                            }//pasa a ser la maxima velocidad de salto hacia arriba.
                            
                        }else{
                            if(this.dy<-limite){
                                if(restoSalto==0) this.dy+=1;//si la velocidad supera el limite decrece gradualmente.
                            }else{
                                if(this.nadando>0 || this.curandose>0 || this.quemandose>0){
                                    if(this.cambiar_y==0){
                                        if(restoSalto==0) this.dy-=potenciaNado;//si no, aumenta gradualmente.
                                    }
                                }else{//gravedad
                                    if(!(bloqueado_abajo || this.atorado) && this.cambiar_y==0){
                                        
                                        if(restoSalto==0){

                                            if(this.dy<limite){
                                                this.dy+=1;
                                            }else if(this.dy>limite){
                                                this.dy-=1;
                                            }
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
                        if(!(bloqueado_abajo || this.atorado) && this.cambiar_y==0){
                            if(restoSalto==0){
                                if(this.dy<limite){
                                    this.dy+=(nadando>0 || this.curandose>0 || quemandose>0)?potenciaNado:1;
                                }else if(this.dy>limite){
                                    this.dy-=(nadando>0 || this.curandose>0 || quemandose>0)?potenciaNado:1;
                                }
                            }
                        }else{
                            if(bloqueado_abajo || atorado){
                                this.dy=0;
                            }
                        }
                    }
                }else{
                    if(this.saltando)this.dy=0;
                    this.saltando=false;
                    if(!(bloqueado_abajo|| this.atorado) && this.cambiar_y==0){//gravedad
                        if(restoSalto==0){
                            if(this.dy<limite){
                                this.dy+=1;
                            }else if(this.dy>limite){
                                this.dy-=1;
                            }
                        }
                    }else{
                        if(bloqueado_abajo || atorado){
                            this.dy=0;
                        }
                    }
                }
            }
            
            if(restoSalto!=0){
                if(restoSalto>4){
                    restoSalto=restoSalto-4;
                    this.y+=4;
                    this.bloqueado_arriba=bloqueado(3,true);
                    this.bloqueado_abajo=bloqueado(4,true);
                }else{
                    if(restoSalto<-4){
                        restoSalto=restoSalto+4;
                        this.y-=4;
                        this.bloqueado_arriba=bloqueado(3,true);
                        this.bloqueado_abajo=bloqueado(4,true);
                    }else{
                        this.y+=restoSalto;
                        salto=true;
                    }
                }
            }else{
                
                if(this.dy>4){
                    restoSalto=this.dy-4;
                    this.y+=4;
                    this.bloqueado_arriba=bloqueado(3,true);
                    this.bloqueado_abajo=bloqueado(4,true);
                }else{
                    if(this.dy<-4){
                        restoSalto=this.dy+4;
                        this.y-=4;
                        this.bloqueado_arriba=bloqueado(3,true);
                        this.bloqueado_abajo=bloqueado(4,true);
                    }else{
                        this.y+=this.dy;
                        salto=true;
                    }
                }
            }
            
            //System.out.println("bloqArr="+this.bloqueado_arriba);
        }
        
        
        if(this.disparo==true && this.disparado==false){
            this.disparado=true;
            Juego.weaponAxe.add(new WeaponAxe(this.jugador));
        }
        
        
        
        //corregir y:(deben estar en este orden para q se corriga el eje y, y luego se compruebe el x).
        this.bloqueado_arriba=bloqueado(3,true);
        this.bloqueado_abajo=bloqueado(4,true);
        this.bloqueado_izquierda=bloqueado(1,false);
        this.bloqueado_derecha=bloqueado(2,false);
        
        
        this.setPosicion(posicion);
        
        
    }
    */
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
    
    public boolean bloqueado(int direccion, boolean corregir, boolean correccionMinima){
        //1=izq,2=der,3=arr,4=aba.
        for(int i=0;i<(this.partesBloqueadas.length-1);i++){
            this.partesBloqueadas[i+1][direccion-1]=false;
        }
        boolean bloqueado=false;//para estabilizar el eje x...
        boolean retorno=false;
        for(int i=0;i<(this.positionParts.length-1);i++){
            
            this.positionParts[i+1][0][0]=(this.positionParts[i+1][0][0]/(4/this.seccionador))*(4/this.seccionador);
            this.positionParts[i+1][1][0]=(this.positionParts[i+1][1][0]/(4/this.seccionador))*(4/this.seccionador);
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
            x=((this.x+(int)this.positionParts[i+1][0][0]/this.seccionadorX));///(4/this.seccionador))*(4/this.seccionador);
            y=((this.y+(int)this.positionParts[i+1][1][0]/this.seccionadorX));///(4/this.seccionador))*(4/this.seccionador);
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
                        if(corregir && pos_quad_x<(Tile.TILE_WIDTH/2) && ((correccionMinima && pos_quad_x<Tile.TILE_WIDTH/4) || !correccionMinima)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_y<(Tile.TILE_HEIGHT/2) && ((espacio1==1 && espacio5==0) || espacio2==1)){
                        if(corregir && pos_quad_x<(Tile.TILE_WIDTH/2) && ((correccionMinima && pos_quad_x<Tile.TILE_WIDTH/4) || !correccionMinima)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                        if(pos_quad_x!=(Tile.TILE_WIDTH/2))bloqueado=true;
                    }
                    if(pos_quad_y>(Tile.TILE_HEIGHT/2) && ((espacio3==1 && espacio4==0) || espacio2==1)){
                        if(corregir && pos_quad_x<(Tile.TILE_WIDTH/2) && ((correccionMinima && pos_quad_x<Tile.TILE_WIDTH/4) || !correccionMinima)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
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
                        if(corregir && pos_quad_x>(Tile.TILE_WIDTH/2) && ((correccionMinima && pos_quad_x>Tile.TILE_WIDTH/4) || !correccionMinima)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_y<(Tile.TILE_HEIGHT/2) && ((espacio1==1 && espacio5==0) || espacio2==1)){
                        if(corregir && pos_quad_x>(Tile.TILE_WIDTH/2) && ((correccionMinima && pos_quad_x>Tile.TILE_WIDTH/4) || !correccionMinima)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                        if(pos_quad_x!=(Tile.TILE_WIDTH/2))bloqueado=true;
                    }
                    if(pos_quad_y>(Tile.TILE_HEIGHT/2) && ((espacio3==1 && espacio4==0) || espacio2==1)){
                        if(corregir && pos_quad_x>(Tile.TILE_WIDTH/2) && ((correccionMinima && pos_quad_x>Tile.TILE_WIDTH/4) || !correccionMinima)) this.subX=(int)(this.subX>0?Math.floor(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX:Math.ceil(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX);
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
                        if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=y2;
                        retorno=true;
                        this.partesBloqueadas[i+1][direccion-1]=true;
                    }
                    if(pos_quad_x>(Tile.TILE_WIDTH/2) && ((espacio3==1 && espacio5==0) || espacio2==1)){
                        //if(corregir) this.y=(int)Math.floor((this.y+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT;
                        int y2=(int)(Math.floor((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                        //if(corregir && this.y<y2) this.y=y2;
                        if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=y2;
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
            this.x=((this.subX/seccionadorX));
            this.y=((this.subY/seccionadorY));
        }
        //this.x=((this.subX/seccionadorX)/(4/this.seccionador))*(4/this.seccionador);
        //this.y=((this.subY/seccionadorY)/(4/this.seccionador))*(4/this.seccionador);
        
        this.bloqueado_eje_x=bloqueado;
        return retorno;
    }
    
    public boolean bloqueadoOtrosBloques(int direccion, boolean corregir){
        return false;
        /*
        boolean retorno=false;
        for(int i=0;i<(this.positionParts.length-1);i++){
            
            this.positionParts[i+1][0][0]=(this.positionParts[i+1][0][0]/(4/this.seccionador))*(4/this.seccionador);
            this.positionParts[i+1][1][0]=(this.positionParts[i+1][1][0]/(4/this.seccionador))*(4/this.seccionador);
            int x=this.subX+this.positionParts[i+1][0][0];//subx en verdad
            int y=this.subY+this.positionParts[i+1][1][0];//suby en verdad
            
            //ahora pasan a ser las coordenadas en pixeles:
            x=((this.x+(int)this.positionParts[i+1][0][0]/this.seccionadorX));///(4/this.seccionador))*(4/this.seccionador);
            y=((this.y+(int)this.positionParts[i+1][1][0]/this.seccionadorX));///(4/this.seccionador))*(4/this.seccionador);
            
            for(int j=0;j<Juego.superJugador.size();j++){
                if(j!=this.jugador){
                    for(int a=0;a<(Juego.superJugador.get(j).positionParts.length-1);a++){
                        
                        //ahora pasan a ser las coordenadas en pixeles:
                        int xx=((Juego.superJugador.get(j).x+(int)Juego.superJugador.get(j).positionParts[a+1][0][0]/Juego.superJugador.get(j).seccionadorX));
                        int yy=((Juego.superJugador.get(j).y+(int)Juego.superJugador.get(j).positionParts[a+1][1][0]/Juego.superJugador.get(j).seccionadorX));
                        
                        if(direccion==1){
                            if((x-xx)<Tile.TILE_WIDTH && (x-xx)>0 && (y-yy)<Tile.TILE_HEIGHT && (y-yy)>-(Tile.TILE_HEIGHT)){
                                retorno=true;
                                if(corregir && !this.bloqueado_derecha){
                                    corregir=false;
                                    this.subX+=(Tile.TILE_WIDTH-(x-xx))*seccionadorX;
                                }
                            }
                        }
                        if(direccion==2){
                            if((x-xx)>-(Tile.TILE_WIDTH) && (x-xx)<0 && (y-yy)<Tile.TILE_HEIGHT && (y-yy)>-(Tile.TILE_HEIGHT)){
                                retorno=true;
                                if(corregir && !this.bloqueado_izquierda){
                                    corregir=false;
                                    this.subX-=(Tile.TILE_WIDTH+(x-xx))*seccionadorX;
                                }
                            }
                        }
                        if(direccion==3){
                            if((x-xx)<Tile.TILE_WIDTH && (x-xx)>-(Tile.TILE_WIDTH) && (y-yy)<Tile.TILE_HEIGHT && (y-yy)>0){
                                retorno=true;
                                if(corregir && !this.bloqueado_abajo){
                                    corregir=false;
                                    this.subY+=(Tile.TILE_HEIGHT-(y-yy))*seccionadorY;
                                }
                            }
                        }
                        if(direccion==4){
                            if((x-xx)<Tile.TILE_WIDTH && (x-xx)>-(Tile.TILE_WIDTH) && (y-yy)<0 && (y-yy)>-(Tile.TILE_HEIGHT)){
                                retorno=true;
                                if(corregir && !this.bloqueado_arriba){
                                    corregir=false;
                                    this.subY-=(Tile.TILE_HEIGHT+(y-yy))*seccionadorY;
                                }
                            }
                        }
                    }
                }
            }
            this.x=((this.subX/seccionadorX));
            this.y=((this.subY/seccionadorY));
        }
        
        return retorno;
        */
    }
    
    public void keyReleased(KeyEvent e) {
    	int k=e.getKeyCode();
    	if(k==teclaDisparo){
            disparo=false;
            disparado=false;
    	}
    	if(k==teclaSalto){
            espacio=false;
            saltando=true;
    	}
    	if(k==teclaIzquierda){
            izquierda=false;
    	}
    	if(k==teclaDerecha){
            derecha=false;
    	}
    	if(k==teclaArriba){
            arriba=false;
    	}
    	if(k==teclaAgacharse){
            agacharse=false;
    	}
        if(k==teclaCorriendo){
            corriendo=false;
            deslizando=false;
    	}
        if(k==teclaCambiar){
            cambiar=false;
            cambiado=false;
    	}
    }
 
    //tecla presionada
    public void keyPressed(KeyEvent e) {
    	
    	int k=e.getKeyCode();
    	if(k==teclaDisparo){
            disparo=true;
    	}
    	if(k==teclaSalto){
            espacio=true;
    	}
    	if(k==teclaIzquierda){
            izquierda=true;
    	}
    	if(k==teclaDerecha){
            derecha=true;
    	}
    	if(k==teclaArriba){
            arriba=true;
    	}
    	if(k==teclaAgacharse){
            agacharse=true;
    	}
        if(k==teclaCorriendo){
            corriendo=true;
            if(deslizar==true){
                deslizando=true;
            }
    	}
        if(k==teclaCambiar){
            cambiar=true;
    	}
    }
    
    public void keyTyped(KeyEvent e) {
        
    }
}