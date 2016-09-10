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

public class WeaponAxe {
    
    int jugador,tipo,x,y,dx,dy,subX,subY,inicio=4,limite=8,maxSpeed=16,maxAceleration=24;
    
    public int seccionador=(4/(Tile.TILE_WIDTH/4));//min 1 cuando es 16 el tamaño del tile, max 4 cuando es 4 el tamaño del tile.
    
    public int seccionadorX=4*seccionador,seccionadorY=4*seccionador;
    
    //vida:
    public boolean broken=false,deadDimension=false;
    
    int daño=64,brokenParts=0;
    
    int centro=1,filoA=2,filoB=3,fase,giro;//fase: 1,2,3 o 4 (de giro), giro: sentido de giro (1=atornillando, 2=desatornillando).
    
    int[][] matrix=new int[3][3];//y,x.
    
    int[][][] positionParts=new int[4][2][2];//cantidad de partes, ejes x e y, subpixeles y pixeles.
    boolean[] visibleParts=new boolean[4];
    
    public WeaponAxe(int jugador,int tipo){//tipo: 1=jugador, 2=enemigo;
        
        this.jugador=jugador;
        this.tipo=tipo;
        this.fase=0;
        for(int i=0;i<this.visibleParts.length;i++){
            this.visibleParts[i]=true;
        }
        if(tipo==1) this.deadDimension=Juego.superJugador.get(jugador).deadDimension;
        if(tipo==2) this.deadDimension=Juego.superEnemigo.get(jugador).deadDimension;
        //generar posicion de acuerdo al brazo del jugador.
        if((tipo==1 && Juego.superJugador.get(jugador).direccion==1) || (tipo==2 && Juego.superEnemigo.get(jugador).direccion==1)){
            this.giro=2;
            this.dx=-maxSpeed;//en subpixeles
            this.dy=maxSpeed;//en subpixeles
            this.x=(tipo==1)?Juego.superJugador.get(jugador).x-1*Tile.TILE_WIDTH+Juego.superJugador.get(jugador).positionParts[Integer.parseInt(Juego.superJugador.get(jugador).brazoDer)][0][1]:Juego.superEnemigo.get(jugador).x-1*Tile.TILE_WIDTH+Juego.superEnemigo.get(jugador).positionParts[Integer.parseInt(Juego.superEnemigo.get(jugador).brazoIzq)][0][1];
            this.y=(tipo==1)?Juego.superJugador.get(jugador).y-1*Tile.TILE_HEIGHT+Juego.superJugador.get(jugador).positionParts[Integer.parseInt(Juego.superJugador.get(jugador).brazoDer)][1][1]:Juego.superEnemigo.get(jugador).y-1*Tile.TILE_HEIGHT+Juego.superEnemigo.get(jugador).positionParts[Integer.parseInt(Juego.superEnemigo.get(jugador).brazoIzq)][1][1];
            //System.out.println("x="+this.x+",y="+this.y);
            this.fase=2;
            setPosition(this.fase);
            //filoA:
            positionParts[matrix[0][0]][0][0]=1*Tile.TILE_WIDTH;//0*Tile.TILE_WIDTH;
            positionParts[matrix[0][0]][1][0]=1*Tile.TILE_HEIGHT;//0*Tile.TILE_HEIGHT;
            //centro:
            positionParts[matrix[1][1]][0][0]=1*Tile.TILE_WIDTH;
            positionParts[matrix[1][1]][1][0]=1*Tile.TILE_HEIGHT;
            //filoB:
            positionParts[matrix[2][2]][0][0]=1*Tile.TILE_WIDTH;//2*Tile.TILE_WIDTH;
            positionParts[matrix[2][2]][1][0]=1*Tile.TILE_HEIGHT;//2*Tile.TILE_HEIGHT;
        }else{
            this.giro=1;
            this.dx=maxSpeed;//en subpixeles
            this.dy=maxSpeed;//en subpixeles
            this.x=(tipo==1)?Juego.superJugador.get(jugador).x-1*Tile.TILE_WIDTH+Juego.superJugador.get(jugador).positionParts[Integer.parseInt(Juego.superJugador.get(jugador).brazoDer)][0][1]:Juego.superEnemigo.get(jugador).x-1*Tile.TILE_WIDTH+Juego.superEnemigo.get(jugador).positionParts[Integer.parseInt(Juego.superEnemigo.get(jugador).brazoDer)][0][1];
            this.y=(tipo==1)?Juego.superJugador.get(jugador).y-1*Tile.TILE_HEIGHT+Juego.superJugador.get(jugador).positionParts[Integer.parseInt(Juego.superJugador.get(jugador).brazoDer)][1][1]:Juego.superEnemigo.get(jugador).y-1*Tile.TILE_HEIGHT+Juego.superEnemigo.get(jugador).positionParts[Integer.parseInt(Juego.superEnemigo.get(jugador).brazoDer)][1][1];
            this.fase=1;
            setPosition(this.fase);
            //filoA:
            positionParts[matrix[0][2]][0][0]=1*Tile.TILE_WIDTH;//2*Tile.TILE_WIDTH;
            positionParts[matrix[0][2]][1][0]=1*Tile.TILE_HEIGHT;//0*Tile.TILE_HEIGHT;
            //centro:
            positionParts[matrix[1][1]][0][0]=1*Tile.TILE_WIDTH;
            positionParts[matrix[1][1]][1][0]=1*Tile.TILE_HEIGHT;
            //filoB:
            positionParts[matrix[2][0]][0][0]=1*Tile.TILE_WIDTH;//0*Tile.TILE_WIDTH;
            positionParts[matrix[2][0]][1][0]=1*Tile.TILE_HEIGHT;//2*Tile.TILE_HEIGHT;
        }
        this.subX=this.x*seccionadorX;
        this.subY=this.y*seccionadorY;
    }
    
    public void setPosition(int posicion){
        switch(posicion){
            case 0:
                matrix[0][0]=0;
                matrix[1][0]=0;
                matrix[2][0]=0;
                
                matrix[0][1]=0;
                matrix[1][1]=0;
                matrix[2][1]=0;
                
                matrix[0][2]=0;
                matrix[1][2]=0;
                matrix[2][2]=0;
                break;
            case 1:
                matrix[0][0]=this.filoA;
                matrix[1][0]=0;
                matrix[2][0]=0;
                
                matrix[0][1]=0;
                matrix[1][1]=this.centro;
                matrix[2][1]=0;
                
                matrix[0][2]=0;
                matrix[1][2]=0;
                matrix[2][2]=this.filoB;
                break;
                
            case 2:
                matrix[0][0]=0;
                matrix[1][0]=0;
                matrix[2][0]=this.filoA;
                
                matrix[0][1]=0;
                matrix[1][1]=this.centro;
                matrix[2][1]=0;
                
                matrix[0][2]=this.filoB;
                matrix[1][2]=0;
                matrix[2][2]=0;
                break;
            case 3:
                matrix[0][0]=this.filoB;
                matrix[1][0]=0;
                matrix[2][0]=0;
                
                matrix[0][1]=0;
                matrix[1][1]=this.centro;
                matrix[2][1]=0;
                
                matrix[0][2]=0;
                matrix[1][2]=0;
                matrix[2][2]=this.filoA;
                break;
            case 4:
                matrix[0][0]=0;
                matrix[1][0]=0;
                matrix[2][0]=this.filoB;
                
                matrix[0][1]=0;
                matrix[1][1]=this.centro;
                matrix[2][1]=0;
                
                matrix[0][2]=this.filoA;
                matrix[1][2]=0;
                matrix[2][2]=0;
                break;
        }
    }
    
    public void updateAxe(){
        boolean fase_completa=true,bloqueado_derecha=false,bloqueado_izquierda=false,bloqueado_arriba=false,bloqueado_abajo=false;
        
        bloqueado_arriba=bloqueado(3,true);
        bloqueado_abajo=bloqueado(4,true);
        bloqueado_izquierda=bloqueado(1,true);
        bloqueado_derecha=bloqueado(2,true);
        
        for(int y=0;y<matrix.length;y++){
            for(int x=0;x<matrix[0].length;x++){
                int index=matrix[y][x];
                if(index!=0){
                    //System.out.println("index="+index);
                    //se usa el seccionadorX para cambiar las posiciones de los bloques dentro de la matrix del jugador...
                    if(positionParts[index][0][0]/seccionadorX<(x*Tile.TILE_WIDTH)){
                        //System.out.println(positionParts[index][0][0]+"<"+(x*Tile.TILE_HEIGHT)+"-->"+index);
                        positionParts[index][0][0]+=maxSpeed;
                        fase_completa=false;
                    }
                    if(positionParts[index][0][0]/seccionadorX>(x*Tile.TILE_WIDTH)){
                        //System.out.println(positionParts[index][0][0]+"<"+(x*Tile.TILE_HEIGHT)+"-->"+index);
                        positionParts[index][0][0]-=maxSpeed;
                        fase_completa=false;
                    }
                    if(positionParts[index][1][0]/seccionadorX<(y*Tile.TILE_HEIGHT)){
                        //System.out.println(positionParts[index][1][0]+"<"+(y*Tile.TILE_HEIGHT)+"-->"+index);
                        positionParts[index][1][0]+=maxSpeed;
                        fase_completa=false;
                    }
                    if(positionParts[index][1][0]/seccionadorX>(y*Tile.TILE_HEIGHT)){
                        //System.out.println(positionParts[index][1][0]+">"+(y*Tile.TILE_HEIGHT)+"-->"+index);
                        positionParts[index][1][0]-=maxSpeed;
                        fase_completa=false;
                        //System.out.println("4");
                    }
                    positionParts[index][1][1]=positionParts[index][1][0]/this.seccionadorX;
                    positionParts[index][0][1]=positionParts[index][0][0]/this.seccionadorX;
                }
            }
        }
        
        
        bloqueado_arriba=bloqueado(3,true);
        bloqueado_abajo=bloqueado(4,true);
        bloqueado_izquierda=bloqueado(1,true);
        bloqueado_derecha=bloqueado(2,true);
        
        //System.out.println(bloqueado_abajo);
        
        if(!(bloqueado_derecha && bloqueado_izquierda)){
            if(this.dx>0 && bloqueado_derecha){
                this.giro=2;
                this.dx=-this.dx;
            }else{
                if(this.dx<0 && bloqueado_izquierda){
                    this.giro=1;
                    this.dx=-this.dx;
                }
            }
            this.subX+=this.dx;
        }
        
        if(!(bloqueado_arriba && bloqueado_abajo)){
            if(this.dy<0 && bloqueado_arriba){
                this.dy=-this.dy;
            }else{
                if(this.dy>0 && bloqueado_abajo){
                    this.dy=-this.maxAceleration;
                }
            }
            this.subY+=this.dy;
        }
        //gravedad:
        this.dy+=1;
        if(this.dy>this.maxAceleration) this.dy=this.maxAceleration;
        if(this.dy<-this.maxAceleration) this.dy=-this.maxAceleration;
        
        if(fase_completa==true){
            if(this.giro==1){
                this.fase--;
                if(this.fase<=0) this.fase=4;
            }
            if(this.giro==2){
                this.fase++;
                if(this.fase>=5) this.fase=1;
            }
        }
        
        for(int i=0;i<Juego.superJugador.size();i++){
            if((this.jugador!=i || this.tipo==2) && (this.deadDimension==Juego.superJugador.get(i).deadDimension)){
                for(int j=1;j<Juego.superJugador.get(i).positionParts.length;j++){
                    for(int k=1;k<this.positionParts.length;k++){
                        if(this.visibleParts[k]==true && (Juego.superJugador.get(i).x+Tile.TILE_WIDTH/2+Juego.superJugador.get(i).positionParts[j][0][1])/Tile.TILE_WIDTH==(this.positionParts[k][0][1]+this.x+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH
                           && (Juego.superJugador.get(i).y+Tile.TILE_HEIGHT/2+Juego.superJugador.get(i).positionParts[j][1][1])/Tile.TILE_HEIGHT==(this.positionParts[k][1][1]+this.y+Tile.TILE_HEIGHT/2)/Tile.TILE_HEIGHT){
                            Juego.superJugador.get(i).changeHealth(-this.daño);
                            this.visibleParts[k]=false;
                            if(k==this.centro){
                                this.broken=true;
                            }
                            this.brokenParts++;
                        }
                    }
                }
            }
        }
        for(int i=0;i<Juego.superEnemigo.size();i++){
            if((this.tipo!=2 && Juego.superEnemigo.get(i).alive) && (this.deadDimension==Juego.superEnemigo.get(i).deadDimension)){
                for(int j=1;j<Juego.superEnemigo.get(i).positionParts.length;j++){
                    for(int k=1;k<this.positionParts.length;k++){
                        if(this.visibleParts[k]==true && (Juego.superEnemigo.get(i).x+Tile.TILE_WIDTH/2+Juego.superEnemigo.get(i).positionParts[j][0][1])/Tile.TILE_WIDTH==(this.positionParts[k][0][1]+this.x+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH
                           && (Juego.superEnemigo.get(i).y+Tile.TILE_HEIGHT/2+Juego.superEnemigo.get(i).positionParts[j][1][1])/Tile.TILE_HEIGHT==(this.positionParts[k][1][1]+this.y+Tile.TILE_HEIGHT/2)/Tile.TILE_HEIGHT){
                            Juego.superEnemigo.get(i).changeHealth(-this.daño);
                            this.visibleParts[k]=false;
                            if(k==this.centro){
                                this.broken=true;
                            }
                            this.brokenParts++;
                        }
                    }
                }
            }
        }
        if(this.brokenParts>=3){
            this.broken=true;
        }
        
        this.x=this.subX/seccionadorX;
        this.y=this.subY/seccionadorY;
        
        setPosition(this.fase);
    }
    
    public boolean bloqueado(int direccion, boolean corregir){
        //1=izq,2=der,3=arr,4=aba.
        boolean retorno=false;
        for(int i=0;i<(this.positionParts.length-1);i++){
            if(this.visibleParts[i+1]==true){
                
                boolean bloqueado=false;//para estabilizar el eje x...
                int x=this.subX+this.positionParts[i+1][0][0];//subx en verdad
                int y=this.subY+this.positionParts[i+1][1][0];//suby en verdad
                if(x<0){
                    //System.out.println("this.positionParts[i+1][0][0]="+this.positionParts[i+1][0][0]+",subX="+subX);
                    this.subX-=x;
                }
                if(x>(Tile.TILE_WIDTH*(Juego.ancho-1)*seccionadorX)){
                    this.subX-=(x-(Tile.TILE_WIDTH*(Juego.ancho-1)*seccionadorX));
                }
                if(y<0){
                    this.subY-=y;
                }
                if(y>(Tile.TILE_HEIGHT*(Juego.alto-1)*seccionadorY)){
                    this.subY-=(y-(Tile.TILE_HEIGHT*(Juego.alto-1)*seccionadorY));
                }

                this.x=this.subX/seccionadorX;
                this.y=this.subY/seccionadorY;
                //ahora pasan a ser las coordenadas en pixeles:
                x=this.x+(int)this.positionParts[i+1][0][0]/this.seccionadorX;
                y=this.y+(int)this.positionParts[i+1][1][0]/this.seccionadorX;
                //posicion aproximada.
                int quad_x=(int)Math.ceil((x+(Tile.TILE_WIDTH/2))/Tile.TILE_WIDTH);
                int quad_y=(int)Math.ceil((y+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT);
                int pos_quad_x=(x+(Tile.TILE_WIDTH)/2)%Tile.TILE_WIDTH;//de 0 a 15.
                int pos_quad_y=(y+(Tile.TILE_HEIGHT)/2)%Tile.TILE_HEIGHT;//de 0 a 15.
                int espacio1,espacio2,espacio3,espacio4,espacio5;
                int centro=MapaTiles.mapaFisico[quad_y][quad_x];
                if(direccion==1){
                    espacio1=(quad_x-1>=0 && quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x-1]:1;
                    espacio2=(quad_x-1>=0)?MapaTiles.mapaFisico[quad_y][quad_x-1]:1;
                    espacio3=(quad_x-1>=0 && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x-1]:1;
                    espacio4=(quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x]:1;
                    espacio5=(quad_y-1>=0)?MapaTiles.mapaFisico[quad_y-1][quad_x]:1;
                    if(pos_quad_x<=(Tile.TILE_WIDTH/2)){
                        if(pos_quad_y==(Tile.TILE_HEIGHT/2) && espacio2==1){
                            if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX;
                            retorno=true;
                        }
                        if(pos_quad_y<(Tile.TILE_HEIGHT/2) && ((espacio1==1 && espacio5==0) || espacio2==1)){
                            if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX;
                            retorno=true;
                            if(pos_quad_x!=(Tile.TILE_WIDTH/2))bloqueado=true;
                        }
                        if(pos_quad_y>(Tile.TILE_HEIGHT/2) && ((espacio3==1 && espacio4==0) || espacio2==1)){
                            if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX;
                            retorno=true;
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
                    if(pos_quad_x>=(Tile.TILE_WIDTH/2)){
                        if(pos_quad_y==(Tile.TILE_HEIGHT/2) && espacio2==1){
                            if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX;
                            retorno=true;
                        }
                        if(pos_quad_y<(Tile.TILE_HEIGHT/2) && ((espacio1==1 && espacio5==0) || espacio2==1)){
                            if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX;
                            retorno=true;
                            if(pos_quad_x!=(Tile.TILE_WIDTH/2))bloqueado=true;
                        }
                        if(pos_quad_y>(Tile.TILE_HEIGHT/2) && ((espacio3==1 && espacio4==0) || espacio2==1)){
                            if(corregir && pos_quad_x!=(Tile.TILE_WIDTH/2)) this.subX=(((this.x+this.positionParts[i+1][0][1]+Tile.TILE_WIDTH/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-this.positionParts[i+1][0][1])*seccionadorX;
                            retorno=true;
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
                    if(pos_quad_y<=(Tile.TILE_HEIGHT/2)){
                        if(pos_quad_x==(Tile.TILE_WIDTH/2) && espacio2==1){
                            if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;

                            retorno=true;
                        }
                        if(pos_quad_x<(Tile.TILE_WIDTH/2) && ((espacio1==1 && espacio4==0) || espacio2==1)){
                            if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                            retorno=true;
                        }
                        if(pos_quad_x>(Tile.TILE_WIDTH/2) && ((espacio3==1 && espacio5==0) || espacio2==1)){
                            if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                            retorno=true;
                        }
                    }
                }
                if(direccion==4){
                    espacio1=(quad_x-1>=0 && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x-1]:1;
                    espacio2=(quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x]:1;
                    espacio3=(quad_x+1<Juego.ancho && quad_y+1<Juego.alto)?MapaTiles.mapaFisico[quad_y+1][quad_x+1]:1;
                    espacio4=(quad_x-1>=0)?MapaTiles.mapaFisico[quad_y][quad_x-1]:1;
                    espacio5=(quad_x+1<Juego.ancho)?MapaTiles.mapaFisico[quad_y][quad_x+1]:1;
                    if(pos_quad_y>=(Tile.TILE_HEIGHT/2)){
                        if(pos_quad_x==(Tile.TILE_WIDTH/2) && espacio2==1){
                            if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                            retorno=true;
                        }
                        if(pos_quad_x<(Tile.TILE_WIDTH/2) && ((espacio1==1 && espacio4==0) || espacio2==1)){
                            if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                            retorno=true;
                        }
                        if(pos_quad_x>(Tile.TILE_WIDTH/2) && ((espacio3==1 && espacio5==0) || espacio2==1)){
                            if(corregir && pos_quad_y!=(Tile.TILE_HEIGHT/2)) this.subY=(((this.y+this.positionParts[i+1][1][1]+(Tile.TILE_HEIGHT/2))/Tile.TILE_HEIGHT)*Tile.TILE_HEIGHT-this.positionParts[i+1][1][1])*this.seccionadorY;
                            retorno=true;
                        }
                    }
                }
                this.x=this.subX/seccionadorX;
                this.y=this.subY/seccionadorY;
            }
        }
        return retorno;
    }
}
