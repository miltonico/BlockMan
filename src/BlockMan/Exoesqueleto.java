
package BlockMan;


public class Exoesqueleto {
    public int[][] esqueleto;//(7=alto),(7=ancho).
    public int posicionPiernaIzq=0;//0=guardada,1=medio,2=afuera,3=hacia adentro.
    public int posicionPiernaDer=0;//0=guardada,1=medio,2=afuera,3=hacia adentro.
    public int posIniX;
    public int posIniY;
    public int x;
    public int y;
    public int accion;
    public int puntosVida;
    public boolean estaVivo,activado=false;
    public int direccion;
    //int[] hola={0,0,0};
    public int accionActual=0;
	//MapaTiles mapa=MapaTiles.DesdeArchivo("map"+nivel+".txt");
	public boolean estaSaltando=false,estaCayendo=false;
	public int contadorQuema=0,contadorCura=0,sumaY,altoDelSalto=0,accionAnterior=0,poderDeSalto=5,contador2=0;
        public boolean techoAnterior=false,direccionado=false,valido=true,nulo=false,moviendose=false,apretando=false,techo=false,firme=true,salto=false;
        private int marcaPasos=0,contadorSprite=0,contador=0;
        
    public int mx=0,my=0,pos=0,posAnterior=0,altura=0,contadorFrames=0,contadorDisparo=0,retardoArmaActual=0;//contadorFrames hasta 8.
    public boolean cayendo=false,paso=true,saltar=false,disparado=false,cambiado=false,corriendo=false,cambiar=false,cambiar2=false,
    corriendoAire=false,corriendo2=false,volar=false,hiperVelocidad=false,ralentizacion=true,paso2=false,paso3=false;//paso2=para graficos.
    public boolean[] contadorDeRetardo=new boolean[5];//0=quieto, 5 velocidades (0,1,2,4,8).
    
    
    public void animar(int accion){
        
        this.accion=accion;
        
    	switch(accion){
    	case 0://normal
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,1,0,0,0,1,0},
                {1,0,0,0,0,0,1},
                {0,0,1,1,1,0,0},
                {0,1,0,0,0,1,0},
                {0,1,0,0,0,1,0}};
    		break;
    	case 1://agachandose 1:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,1,0,0,0,1,0},
                {1,0,0,0,0,0,1},
                {0,1,1,1,1,1,0},
                {0,1,0,0,0,1,0}};
    		break;
    	case 2://agachandose 2:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,1,0,0,0,1,0},
                {1,1,0,0,0,1,1},
                {0,1,1,1,1,1,0}};
    		break;
    	case 3://caminando 1:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,1,0,0,0,1,0},
                {1,0,0,0,0,0,1},
                {0,0,1,1,1,0,0},
                {0,0,1,0,0,1,0},
                {0,0,1,0,0,1,0}};
    		break;
    	case 4://caminando 2:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,1,0,0,0,1,0},
                {1,0,0,0,0,0,1},
                {0,0,1,1,1,0,0},
                {0,1,0,0,1,0,0},
                {0,1,0,0,1,0,0}};
    		break;
    	case 5://arriba 1:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,1,0,0,0,1,0},
                {0,1,0,0,0,1,0},
                {0,0,1,1,1,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0}};
    		break;
    	case 6://arriba 2:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,1,1,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0}};
    		break;
    	case 7://desactivandose 1:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,1,0,0,0,1,0},
                {0,1,0,0,0,1,0},
                {0,0,1,1,1,0,0},
                {0,1,0,0,0,1,0},
                {0,1,0,0,0,1,0}};
    		break;
    	case 8://desactivandose 2:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,1,1,0,0},
                {0,1,0,0,0,1,0},
                {0,1,0,0,0,1,0}};
    		break;
    	case 9://desactivandose 3:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0},
                {0,1,1,1,1,1,0},
                {0,1,0,0,0,1,0}};
    		break;
    	case 10://desactivandose 4:
    		esqueleto=new int[][]{
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,1,0,1,0,0},
                {0,0,1,0,1,0,0},
                {0,1,1,0,1,1,0},
                {0,1,1,1,1,1,0}};
    		break;
    	}
    }
    
    public void actualizar( boolean disparo, boolean espacio, boolean derecha, boolean izquierda, boolean agacharse, boolean arriba){
        
        //reiniciamos las variables:
        mx=my=0;
        
        if((contadorFrames==0 || contadorFrames==4)){
            contadorDeRetardo[3]=true;
        }else{
            contadorDeRetardo[3]=false;
        }
        
        if((contadorFrames==2 || contadorFrames==6) && (corriendo==true || corriendo2==true)){
            contadorDeRetardo[2]=true;
        }else{
            contadorDeRetardo[2]=false;
        }
        
        if((contadorFrames==1 || contadorFrames==2 || contadorFrames==3 || contadorFrames==5 || contadorFrames==6 || contadorFrames==7) && (hiperVelocidad==true)){
            contadorDeRetardo[1]=true;//maxima velocidad
        }else{
            contadorDeRetardo[1]=false;//maxima velocidad
        }
        
        if(ralentizacion==true){
            contadorDeRetardo[1]=false;
            contadorDeRetardo[2]=false;
            //contadorDeRetardo[3]=false;
            if(contadorFrames==0 || (contadorFrames==4 && ralentizacion==true && (corriendo==true || corriendo2==true))){
                contadorDeRetardo[4]=true;//mas lentoooooo.
            }else{
                contadorDeRetardo[4]=false;
            }
        }else{
            contadorDeRetardo[4]=false;
        }
        
        if(activado==true && ((contadorDeRetardo[3]==true && ralentizacion==false) || contadorDeRetardo[1]==true || contadorDeRetardo[4]==true || (contadorDeRetardo[2]==true && (corriendoAire==true || (saltar==false && cayendo==false))))){
            
            //obtenemos la accion anterior:
            posAnterior=Personaje.accion[0];
            pos=0;
            
            //comprobamos los cambios en el eje x:
            if(!((izquierda) && (derecha)) && (izquierda || derecha)){//si solo esta activada 1 direccion de las 2 y por lo menos 1:
                if(derecha){
                    if(paso==true){
                        if(paso2==false){
                            Personaje.contadorSprite[0]=7;
                            paso2=true;
                        }else{
                            Personaje.contadorSprite[0]=9;
                            paso2=false;
                        }
                        paso=false;
                        pos=1;
                    }else{
                        if(paso3==false){
                            Personaje.contadorSprite[0]=8;
                            paso3=true;
                        }else{
                            Personaje.contadorSprite[0]=10;
                            paso3=false;
                        }
                        paso=true;
                        pos=0;
                    }
                    mx++;
                }
                if(izquierda){
                    if(paso==true){
                        if(paso2==false){
                            Personaje.contadorSprite[0]=1;
                            paso2=true;
                        }else{
                            Personaje.contadorSprite[0]=3;
                            paso2=false;
                        }
                        paso=false;
                        pos=3;
                    }else{
                        if(paso3==false){
                            Personaje.contadorSprite[0]=2;
                            paso3=true;
                        }else{
                            Personaje.contadorSprite[0]=4;
                            paso3=false;
                        }
                        paso=true;
                        pos=0;
                    }
                    mx--;
                }
            }else{
                paso=true;
                paso2=false;
                paso3=false;
                if(direccion==1){//izq
                    Personaje.contadorSprite[0]=5;
                }else{
                    Personaje.contadorSprite[0]=11;
                }
            }

            //comprobacion de otras posiciones:

            if(saltar && (espacio) || cayendo){
                if(mx==1){
                    pos=2;
                    Personaje.contadorSprite[0]=7;
                }
                if(mx==-1){
                    pos=4;
                    Personaje.contadorSprite[0]=1;
                }
            }

            if(agacharse){
                pos=6;
                if(mx==1){
                    Personaje.contadorSprite[0]=13;
                }else{
                    if(mx==-1){
                        Personaje.contadorSprite[0]=12;
                    }else{
                        if(direccion==2){
                            Personaje.contadorSprite[0]=13;
                        }else{
                            Personaje.contadorSprite[0]=12;
                        }
                    }
                }
            }

            //la comprobacion de la paleta:
            if(arriba){
                pos=5;
                if(mx==1){
                    Personaje.contadorSprite[0]=6;
                }else{
                    if(mx==-1){
                        Personaje.contadorSprite[0]=0;
                    }else{
                        if(direccion==2){
                            Personaje.contadorSprite[0]=6;
                        }else{
                            Personaje.contadorSprite[0]=0;
                        }
                    }
                }
            }
        }
        
        //comprobamos los cambios en el eje y:
        if(contadorDeRetardo[3]==true){
            if((espacio) && ((cayendo==false  && saltar==true) || volar==true) && altura<Actualizaciones.saltoJugador[0]){
                my--;
                if(volar==false){
                    altura++;
                }else{
                    saltar=true;
                    cayendo=false;
                    if(corriendo || corriendo2){
                        corriendoAire=true;
                    }else{
                        corriendoAire=false;
                    }
                }
            }else{
                if(desbloqueado(posAnterior,Personaje.x[0],Personaje.y[0]+1)){
                    cayendo=true;
                }else{
                    saltar=true;
                    altura=0;
                    cayendo=false;
                    if(corriendo==false){
                        corriendoAire=false;//no puede moverse rapido en el aire si no estaba corriendo antes de saltar.
                    }else{
                        corriendoAire=true;
                    }
                }
                if((espacio) && cayendo==false && saltar==true && altura<Actualizaciones.saltoJugador[0]){
                    my--;
                    if(volar==false){
                        altura++;
                    }else{
                        saltar=true;
                        cayendo=false;
                        if(corriendo || corriendo2){
                            corriendoAire=true;
                        }else{
                            corriendoAire=false;
                        }
                    }
                }
            }

            if(cayendo){
                my++;
            }
        }
        //comprobaciones de bloqueos:
        if(!(MapaTiles.mapaFisico[this.y+3][this.x+1+mx]==1 || MapaTiles.jugadores[this.y+3][this.x+1+mx]==1 || MapaTiles.mapaFisico[this.y+3][this.x+1]==1 || MapaTiles.jugadores[this.y+3][this.x+1]==1) && (desbloqueado(pos,Personaje.x[0]+mx,Personaje.y[0]+my) || desbloqueado(posAnterior,Personaje.x[0]+mx,Personaje.y[0]+my))){//si ademas esta empalado.
            
            Personaje.x[0]+=mx;
            Personaje.y[0]+=my;
        }else{
            if(!(MapaTiles.mapaFisico[this.y+3][this.x+1+mx]==1 || MapaTiles.jugadores[this.y+3][this.x+1+mx]==1 || MapaTiles.mapaFisico[this.y+3][this.x+1]==1 || MapaTiles.jugadores[this.y+3][this.x+1]==1) && (desbloqueado(pos,Personaje.x[0]+mx,Personaje.y[0]))){//si ademas esta empalado.
                Personaje.x[0]+=mx;
            }else{
                if(desbloqueado(posAnterior,Personaje.x[0]+mx,Personaje.y[0])){
                    pos=posAnterior;
                    switch(pos){
                        case 0:
                            if(direccion==1){
                                Personaje.contadorSprite[0]=5;
                            }else{
                                Personaje.contadorSprite[0]=11;
                            }
                            break;
                        case 1:
                            Personaje.contadorSprite[0]=7;
                            break;
                        case 2:
                            Personaje.contadorSprite[0]=7;
                            break;
                        case 3:
                            Personaje.contadorSprite[0]=1;
                            break;
                        case 4:
                            Personaje.contadorSprite[0]=1;
                            break;
                        case 5:
                            if(direccion==1){
                                Personaje.contadorSprite[0]=0;
                            }else{
                                Personaje.contadorSprite[0]=6;
                            }
                            break;
                        case 6:
                            if(direccion==1){
                                Personaje.contadorSprite[0]=12;
                            }else{
                                Personaje.contadorSprite[0]=13;
                            }
                            break;
                    }
                    Personaje.x[0]+=mx;
                }else{
                    //corriendoAire=false;
                }
            }
            if(desbloqueado(pos,Personaje.x[0],Personaje.y[0]+my)){
                Personaje.y[0]+=my;
            }else{
                if(desbloqueado(posAnterior,Personaje.x[0],Personaje.y[0]+my)){
                    pos=posAnterior;
                    switch(pos){
                        case 0:
                            if(direccion==1){
                                Personaje.contadorSprite[0]=5;
                            }else{
                                Personaje.contadorSprite[0]=11;
                            }
                            break;
                        case 1:
                            Personaje.contadorSprite[0]=7;
                            break;
                        case 2:
                            Personaje.contadorSprite[0]=7;
                            break;
                        case 3:
                            Personaje.contadorSprite[0]=1;
                            break;
                        case 4:
                            Personaje.contadorSprite[0]=1;
                            break;
                        case 5:
                            if(direccion==1){
                                Personaje.contadorSprite[0]=0;
                            }else{
                                Personaje.contadorSprite[0]=6;
                            }
                            break;
                        case 6:
                            if(direccion==1){
                                Personaje.contadorSprite[0]=12;
                            }else{
                                Personaje.contadorSprite[0]=13;
                            }
                            break;
                    }
                    Personaje.y[0]+=my;
                }else{
                    if(volar==false){
                        saltar=false;
                    }
                    corriendoAire=true;//si no esta saltando puede moverse rapidamente en el aire (si esta corriendo y salta).
                }
                
            }
        }
        
        
        if(contadorDeRetardo[1]==true || contadorDeRetardo[2]==true || contadorDeRetardo[3]==true || contadorDeRetardo[4]==true){
            //reestablecer la posicion si es necesario:
            if(!desbloqueado(pos,Personaje.x[0],Personaje.y[0])){
                pos=posAnterior;
                switch(pos){
                    case 0:
                        if(direccion==1){
                            Personaje.contadorSprite[0]=5;
                        }else{
                            Personaje.contadorSprite[0]=11;
                        }
                        break;
                    case 1:
                        Personaje.contadorSprite[0]=7;
                        break;
                    case 2:
                        Personaje.contadorSprite[0]=7;
                        break;
                    case 3:
                        Personaje.contadorSprite[0]=1;
                        break;
                    case 4:
                        Personaje.contadorSprite[0]=1;
                        break;
                    case 5:
                        if(direccion==1){
                            Personaje.contadorSprite[0]=0;
                        }else{
                            Personaje.contadorSprite[0]=6;
                        }
                        break;
                    case 6:
                        if(direccion==1){
                            Personaje.contadorSprite[0]=12;
                        }else{
                            Personaje.contadorSprite[0]=13;
                        }
                        break;
                }
            }

            //establece la direccion:
            switch(pos){
                case 0:
                    if(derecha) direccion=2;
                    if(izquierda) direccion=1;
                    break;
                case 1:
                    direccion=2;
                    break;
                case 2:
                    direccion=2;
                    break;
                case 3:
                    direccion=1;
                    break;
                case 4:
                    direccion=1;
                    break;
                case 5:
                    if(derecha) direccion=2;
                    if(izquierda) direccion=1;
                    break;
                case 6:
                    if(derecha) direccion=2;
                    if(izquierda) direccion=1;
                    break;
            }
            //quemandose y curandose:
            
            
            //tomar items:
            
            //sprites otras cosas:
            if(pos==5){
                switch(sePuedeAgarrar(izquierda, derecha)){
                    case 1://izquierda:
                        pos=3;
                        Personaje.contadorSprite[0]=1;
                        break;
                    case 2://izquierda:
                        pos=1;
                        Personaje.contadorSprite[0]=7;
                        break;
                }
            }
            
            //reestablecer las variables:
//            espacio2=false;
//            izquierda2=false;
//            derecha2=false;
//            arriba2=false;
//            abajo2=false;
//            agacharse2=false;
//            corriendo2=false;
        }
        
        //otras comprobaciones:
        
        
        //asignar la posicion:
        this.animar(pos);
        
        //cambio de arma:
        
        //se define el retardo de acuerdo al arma:
        
        
        //disparo:
        
        
        //incrementa y resetea el contador si es necesario:
        contadorFrames++;
        if(contadorFrames>=8){
            contadorFrames=0;
        }
     
    }
    
    
    public boolean desbloqueado(int posicion, int x, int y){
        this.animar(posicion);
        if(y+3<Juego.alto && y>=0 && x+2<Juego.ancho && x>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(esqueleto[i][j]==1)
    					if(MapaTiles.mapaFisico[y+i][x+j]==1 || MapaTiles.jugadores[y+i][x+j]==1)
    						return false;
    	}else{
    		return false;
    	}
    	return true;
    }
    public int sePuedeAgarrar(boolean izquierda, boolean derecha){//0=no,1=izq,2=der.
        if((izquierda) && MapaTiles.mapaFisico[Personaje.y[0]+1][Personaje.x[0]]==0 && MapaTiles.jugadores[Personaje.y[0]+1][Personaje.x[0]]==0 && (MapaTiles.mapaFisico[Personaje.y[0]+2][Personaje.x[0]]==1 || MapaTiles.jugadores[Personaje.y[0]+2][Personaje.x[0]]==1)){
            return 1;
        }
        if((derecha) && MapaTiles.mapaFisico[Personaje.y[0]+1][Personaje.x[0]+2]==0 && MapaTiles.jugadores[Personaje.y[0]+1][Personaje.x[0]+2]==0 && (MapaTiles.mapaFisico[Personaje.y[0]+2][Personaje.x[0]+2]==1 || MapaTiles.jugadores[Personaje.y[0]+2][Personaje.x[0]+2]==1)){
            return 2;
        }
        return 0;
    }
    
}
