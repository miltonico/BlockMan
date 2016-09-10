package BlockMan;

import java.awt.event.*;

public class Jugador3 implements KeyListener {
	
	//metodos por jugador:
	//animar,getX,getY,setX,setY.
	
	public static int teclaIzquierda=Teclas.teclas[2][0];
	public static int teclaDerecha=Teclas.teclas[2][1];
	public static int teclaDisparo=Teclas.teclas[2][5];
	public static int teclaSalto=Teclas.teclas[2][4];
	public static int teclaAgacharse=Teclas.teclas[2][2];
	public static int teclaArriba=Teclas.teclas[2][3];
        public static int teclaCorriendo=Teclas.teclas[2][6];
        public static int teclaCambiar=Teclas.teclas[2][7];
	
	public static boolean gano=false;
	
	private int nivel=1;
	//Juego j;
	Balas balas=new Balas();
	Personaje personaje=new Personaje();
        Actualizaciones act=new Actualizaciones();
        
        public static boolean reap=false;
        
	//MapaTiles mapa=MapaTiles.DesdeArchivo("map"+nivel+".txt");
	public static boolean disparo=false,disparo2=false,arriba2=false,abajo2=false,espacio2=false,izquierda2=false,derecha2=false,agacharse2=false,espacio, arriba, izquierda, derecha,agacharse;
	public static int contadorQuema=0,contadorCura=0,sumaY,altoDelSalto=0,accionAnterior=0,poderDeSalto=5,direccion=1,contador2=0;
    private boolean techoAnterior=false,direccionado=false,valido=true,nulo=false,moviendose=false,apretando=false,techo=false,firme=true,salto=false,estaSaltando=false,estaCayendo=false;
    private int marcaPasos=0,contadorSprite=0,contador=0,accionActual=0,auxX=personaje.posIniX[2],auxY=personaje.posIniY[2];
    //nuevo actualizar:
    
    public static int mx=0,my=0,pos=0,posAnterior=0,altura=0,contadorFrames=0,contadorDisparo=0,retardoArmaActual=0;//contadorFrames hasta 8.
    public static boolean cayendo=false,paso=true,saltar=false,disparado=false,cambiado=false,corriendo=false,cambiar=false,cambiar2=false,
            corriendoAire=false,corriendo2=false,volar=false,hiperVelocidad=false,ralentizacion=true,paso2=false,paso3=false;//paso2=para graficos.
    public boolean[] contadorDeRetardo=new boolean[5];//0=quieto, 5 velocidades (2,1,2,4,8).
    
    public Jugador3(){
            teclaIzquierda=Teclas.teclas[2][0];
            teclaDerecha=Teclas.teclas[2][1];
            teclaDisparo=Teclas.teclas[2][5];
            teclaSalto=Teclas.teclas[2][4];
            teclaAgacharse=Teclas.teclas[2][2];
            teclaArriba=Teclas.teclas[2][3];
            teclaCorriendo=Teclas.teclas[2][6];
            teclaCambiar=Teclas.teclas[2][7];
        }
    
    public void actualiza2(){
        
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
        
        if((contadorDeRetardo[3]==true && ralentizacion==false) || contadorDeRetardo[1]==true || contadorDeRetardo[4]==true || (contadorDeRetardo[2]==true && (corriendoAire==true || (saltar==false && cayendo==false)))){
            
            //obtenemos la accion anterior:
            posAnterior=Personaje.accion[2];
            pos=0;
            
            //comprobamos los cambios en el eje x:
            if(!((izquierda || izquierda2) && (derecha || derecha2)) && (izquierda || izquierda2 || derecha || derecha2)){//si solo esta activada 1 direccion de las 2 y por lo menos 1:
                if(derecha || derecha2){
                    if(paso==true){
                        if(paso2==false){
                            Personaje.contadorSprite[2]=7;
                            paso2=true;
                        }else{
                            Personaje.contadorSprite[2]=9;
                            paso2=false;
                        }
                        paso=false;
                        pos=1;
                    }else{
                        if(paso3==false){
                            Personaje.contadorSprite[2]=8;
                            paso3=true;
                        }else{
                            Personaje.contadorSprite[2]=10;
                            paso3=false;
                        }
                        paso=true;
                        pos=0;
                    }

                    mx++;
                }
                if(izquierda || izquierda2){
                    if(paso==true){
                        if(paso2==false){
                            Personaje.contadorSprite[2]=1;
                            paso2=true;
                        }else{
                            Personaje.contadorSprite[2]=3;
                            paso2=false;
                        }
                        paso=false;
                        pos=3;
                    }else{
                        if(paso3==false){
                            Personaje.contadorSprite[2]=2;
                            paso3=true;
                        }else{
                            Personaje.contadorSprite[2]=4;
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
                    Personaje.contadorSprite[2]=5;
                }else{
                    Personaje.contadorSprite[2]=11;
                }
            }

            //comprobacion de otras posiciones:

            if(saltar && (espacio || espacio2) || cayendo){
                if(mx==1){
                    pos=2;
                    Personaje.contadorSprite[2]=7;
                }
                if(mx==-1){
                    pos=4;
                    Personaje.contadorSprite[2]=1;
                }
            }

            if(agacharse || agacharse2){
                pos=6;
                if(mx==1){
                    Personaje.contadorSprite[2]=13;
                }else{
                    if(mx==-1){
                        Personaje.contadorSprite[2]=12;
                    }else{
                        if(direccion==2){
                            Personaje.contadorSprite[2]=13;
                        }else{
                            Personaje.contadorSprite[2]=12;
                        }
                    }
                }
            }

            //la comprobacion de la paleta:
            if(arriba || arriba2){
                pos=5;
                if(mx==1){
                    Personaje.contadorSprite[2]=6;
                }else{
                    if(mx==-1){
                        Personaje.contadorSprite[2]=0;
                    }else{
                        if(direccion==2){
                            Personaje.contadorSprite[2]=6;
                        }else{
                            Personaje.contadorSprite[2]=0;
                        }
                    }
                }
            }
        }
        
        //comprobamos los cambios en el eje y:
        if(contadorDeRetardo[3]==true){
            if((espacio || espacio2) && (((cayendo==false  && saltar==true) && altura<Actualizaciones.saltoJugador[2]) || volar==true)){
                my--;
                if(volar==false){
                    altura++;
                }else{
                    altura=0;
                    saltar=true;
                    cayendo=false;
                    if(corriendo || corriendo2){
                        corriendoAire=true;
                    }else{
                        corriendoAire=false;
                    }
                }
            }else{
                if(desbloqueado(posAnterior,Personaje.x[2],Personaje.y[2]+1)){
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
                if((espacio || espacio2) && cayendo==false && saltar==true && altura<Actualizaciones.saltoJugador[2]){
                    my--;
                    if(volar==false){
                        altura++;
                    }else{
                        altura=0;
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
        if(!(MapaTiles.mapaFisico[personaje.getY(2)+3][personaje.getX(2)+1+mx]==1 || MapaTiles.jugadores[personaje.getY(2)+3][personaje.getX(2)+1+mx]==1 || MapaTiles.mapaFisico[personaje.getY(2)+3][personaje.getX(2)+1]==1 || MapaTiles.jugadores[personaje.getY(2)+3][personaje.getX(2)+1]==1) && (desbloqueado(pos,Personaje.x[2]+mx,Personaje.y[2]+my) || desbloqueado(posAnterior,Personaje.x[2]+mx,Personaje.y[2]+my))){//si ademas esta empalado.
            Personaje.x[2]+=mx;
            Personaje.y[2]+=my;
        }else{
            if(!(MapaTiles.mapaFisico[personaje.getY(2)+3][personaje.getX(2)+1+mx]==1 || MapaTiles.jugadores[personaje.getY(2)+3][personaje.getX(2)+1+mx]==1 || MapaTiles.mapaFisico[personaje.getY(2)+3][personaje.getX(2)+1]==1 || MapaTiles.jugadores[personaje.getY(2)+3][personaje.getX(2)+1]==1) && (desbloqueado(pos,Personaje.x[2]+mx,Personaje.y[2]))){//si ademas esta empalado.
                Personaje.x[2]+=mx;
            }else{
                if(desbloqueado(posAnterior,Personaje.x[2]+mx,Personaje.y[2])){
                    pos=posAnterior;
                    switch(pos){
                        case 0:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=5;
                            }else{
                                Personaje.contadorSprite[2]=11;
                            }
                            break;
                        case 1:
                            Personaje.contadorSprite[2]=7;
                            break;
                        case 2:
                            Personaje.contadorSprite[2]=7;
                            break;
                        case 3:
                            Personaje.contadorSprite[2]=1;
                            break;
                        case 4:
                            Personaje.contadorSprite[2]=1;
                            break;
                        case 5:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=0;
                            }else{
                                Personaje.contadorSprite[2]=6;
                            }
                            break;
                        case 6:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=12;
                            }else{
                                Personaje.contadorSprite[2]=13;
                            }
                            break;
                    }
                    Personaje.x[2]+=mx;
                }else{
                    //corriendoAire=false;
                }
            }
            if(desbloqueado(pos,Personaje.x[2],Personaje.y[2]+my)){
                Personaje.y[2]+=my;
            }else{
                if(desbloqueado(posAnterior,Personaje.x[2],Personaje.y[2]+my)){
                    pos=posAnterior;
                    switch(pos){
                        case 0:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=5;
                            }else{
                                Personaje.contadorSprite[2]=11;
                            }
                            break;
                        case 1:
                            Personaje.contadorSprite[2]=7;
                            break;
                        case 2:
                            Personaje.contadorSprite[2]=7;
                            break;
                        case 3:
                            Personaje.contadorSprite[2]=1;
                            break;
                        case 4:
                            Personaje.contadorSprite[2]=1;
                            break;
                        case 5:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=0;
                            }else{
                                Personaje.contadorSprite[2]=6;
                            }
                            break;
                        case 6:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=12;
                            }else{
                                Personaje.contadorSprite[2]=13;
                            }
                            break;
                    }
                    Personaje.y[2]+=my;
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
            if(!desbloqueado(pos,Personaje.x[2],Personaje.y[2])){
                pos=posAnterior;
                switch(pos){
                        case 0:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=5;
                            }else{
                                Personaje.contadorSprite[2]=11;
                            }
                            break;
                        case 1:
                            Personaje.contadorSprite[2]=7;
                            break;
                        case 2:
                            Personaje.contadorSprite[2]=7;
                            break;
                        case 3:
                            Personaje.contadorSprite[2]=1;
                            break;
                        case 4:
                            Personaje.contadorSprite[2]=1;
                            break;
                        case 5:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=0;
                            }else{
                                Personaje.contadorSprite[2]=6;
                            }
                            break;
                        case 6:
                            if(direccion==1){
                                Personaje.contadorSprite[2]=12;
                            }else{
                                Personaje.contadorSprite[2]=13;
                            }
                            break;
                    }
            }

            //establece la direccion:
            switch(pos){
                case 0:
                    if(derecha || derecha2) direccion=2;
                    if(izquierda || izquierda2) direccion=1;
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
                    if(derecha || derecha2) direccion=2;
                    if(izquierda || izquierda2) direccion=1;
                    break;
                case 6:
                    if(derecha || derecha2) direccion=2;
                    if(izquierda || izquierda2) direccion=1;
                    break;
            }
            //quemandose y curandose:
        
            if(quemandose2()){
                if(personaje.vidas[2]>0){
                    personaje.vidas[2]--;
                }
                ralentizacion=true;
                volar=true;
            }
            if(aguandose()){
                ralentizacion=true;
                volar=true;
            }
            if(!quemandose2() && !aguandose()){
                ralentizacion=false;
                volar=false;
            }
            if(curandose2()){
                if(personaje.vidas[2]<act.vidaJugador[2]){
                    personaje.vidas[2]++;
                }
            }
            
            switch(tomoUnItem()){
                case 0://nada.
                    break;
                case 26://pelotas.
                    act.municionPelotas[2]=act.municionPelotas[2]+act.tamañoCartuchoPelotas;
                    act.armaActual[2]=2;
                    break;
                case 33://misiles.
                    act.municionMisiles[2]=act.municionMisiles[2]+act.tamañoCartuchoMisiles;
                    break;
                case 37://capsula curativa.
                    Personaje.vidas[2]+=act.curacionCapsulas;
                    if(Personaje.vidas[2]>act.vidaJugador[2]) Personaje.vidas[2]=act.vidaJugador[2];
                    break;
                case 44://mejora de vida.
                    for(int i=0;i<Teclas.jugadores;i++){
                        if(act.vidaJugador[i]<act.vidaMaxima){
                            act.vidaJugador[i]+=act.mejoraDeVida;
                            Personaje.vidas[i]+=act.mejoraDeVida;//cura solo lo q aumento.
                        }
                    }
                    break;
            }
            int indiceCheck=tomoUnCheckPoint();
            if(indiceCheck!=-1){
                act.checkJugador[2]=indiceCheck;
            }
            
            if(pos==5){
                switch(sePuedeAgarrar()){
                    case 1://izquierda:
                        pos=3;
                        break;
                    case 2://izquierda:
                        pos=1;
                        break;
                }
            }
            
            //reestablecer las variables:
            espacio2=false;
            izquierda2=false;
            derecha2=false;
            arriba2=false;
            abajo2=false;
            agacharse2=false;
            corriendo2=false;
        }
        
        //otras comprobaciones:
        if(murio()){
            reaparecer();
        }
        
        if(perdio()){
            
        }
        
    	if(gano()){
    		
    	}
    	
    	if(reap==true){
            reaparecer();
    	}
        
        //asignar la posicion:
        personaje.animar(2,pos);
        
        //cambio de arma:
        if((cambiar || cambiar2) && cambiado==false){
            cambiado=true;
            boolean cambio=false;
            while(cambio==false){
                if(act.armaActual[2]<(act.limiteArmas-1)){
                    act.armaActual[2]++;
                }else{
                    act.armaActual[2]=1;
                }
                switch(act.armaActual[2]){
                    case 1:
                        cambio=true;
                        break;
                    case 2:
                        if(act.municionPelotas[2]>0){
                            cambio=true;
                        }
                        break;
                    case 3:
                        if(act.municionMisiles[2]>0){
                            cambio=true;
                        }
                        break;
                }
            }
        }
        
        //se define el retardo de acuerdo al arma.
        switch(act.armaActual[2]){
            case 0://nada
                retardoArmaActual=0;
                break;
            case 1://pistola(la ametralladora aparece cuando tiene mejora de 2x velocidad ataque)
                retardoArmaActual=act.retardoBalas/act.velocidadAtaque[2];
                break;
            case 2://arma boteadora.
                retardoArmaActual=act.retardoPelotas/act.velocidadAtaque[2];
                break;
            case 3://lanza misiles
                retardoArmaActual=act.retardoMisiles/act.velocidadAtaque[2];
                break;
            
        }
        
        if(contadorDisparo>0){
            contadorDisparo++;
            if(contadorDisparo>=retardoArmaActual){
                contadorDisparo=0;
            }
        }
        
        if((disparo || disparo2) && pos!=5 && contadorDisparo==0){// && disparado==false(para q dispare estilo megaman)
            //disparado=true;(para q dispare estilo megaman)
            switch(act.armaActual[2]){
                case 0://nada
                break;
            case 1://pistola(la ametralladora aparece cuando tiene mejora de 2x velocidad ataque)
                balas.nuevaBala(2,direccion,Personaje.x[2],Personaje.y[2]);
                contadorDisparo++;//(1)
                break;
            case 2://arma boteadora.
                balas.nuevaPelota(2,direccion,Personaje.x[2],Personaje.y[2]);
                contadorDisparo++;//(1)
                break;
            case 3://lanza misiles
                //balas.nuevoMisil(2,direccion,Personaje.x[2],Personaje.y[2]);
                contadorDisparo++;//(1)
                break;
            }
    	}
        
        disparo2=false;
        cambiar2=false;
        
        //incrementa y resetea el contador si es necesario:
        contadorFrames++;
        if(contadorFrames>=8){
            contadorFrames=0;
        }
        personaje.direccion[2]=this.direccion;
        
    }

    //nuevo metodo:
    public void reaparecer(){
        this.reap=false;
        if(act.checkJugador[2]==-1){
            personaje.setX(2,personaje.posIniX[2]);
            personaje.setY(2,personaje.posIniY[2]);
        }else{
            personaje.setX(2,MapaTiles.punto.get(act.checkJugador[2]).posicionPuntoX-1);
            personaje.setY(2,MapaTiles.punto.get(act.checkJugador[2]).posicionPuntoY-3);
        }
    	auxX=personaje.getX(2);
    	auxY=personaje.getY(2);
    }
    
    public int tomoUnItem(){
        for(int k=0;k<MapaTiles.items.size();k++){
            for(int i=0;i<4;i++){
                for(int j=0;j<3;j++){
                    if(personaje.personaje[2][i][j]==1){
    			if((Personaje.y[2]+i)==MapaTiles.items.get(k).posicionItemY && (Personaje.x[2]+j)==MapaTiles.items.get(k).posicionItemX){
                            int item=MapaTiles.items.get(k).tipoItem;
                            MapaTiles.items.remove(k);
                            return item;
    			}
                    }
                }
            }
        }
        return 0;
    }
    
    public int tomoUnCheckPoint(){
        for(int k=0;k<MapaTiles.punto.size();k++){
            for(int i=0;i<4;i++){
                for(int j=0;j<3;j++){
                    if(personaje.personaje[2][i][j]==1){
    			if((Personaje.y[2]+i)==MapaTiles.punto.get(k).posicionPuntoY && (Personaje.x[2]+j)==MapaTiles.punto.get(k).posicionPuntoX){
                            int indice=MapaTiles.punto.get(k).indice;
                            return indice;
    			}
                    }
                }
            }
        }
        return -1;
    }
    
    public boolean desbloqueado(int posicion, int x, int y){
        personaje.animar(2,posicion);
        if(y+3<Juego.alto && y>=0 && x+2<Juego.ancho && x>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[2][i][j]==1)
    					if(MapaTiles.mapaFisico[y+i][x+j]==1 || MapaTiles.jugadores[y+i][x+j]==1)
    						return false;
    	}else{
    		return false;
    	}
    	return true;
    }
    public int sePuedeAgarrar(){//0=no,1=izq,2=der.
        if((izquierda || izquierda2) && MapaTiles.mapaFisico[Personaje.y[2]+1][Personaje.x[2]]==0 && MapaTiles.jugadores[Personaje.y[2]+1][Personaje.x[2]]==0 && (MapaTiles.mapaFisico[Personaje.y[2]+2][Personaje.x[2]]==1 || MapaTiles.jugadores[Personaje.y[2]+2][Personaje.x[2]]==1)){
            return 1;
        }
        if((derecha || derecha2) && MapaTiles.mapaFisico[Personaje.y[2]+1][Personaje.x[2]+2]==0 && MapaTiles.jugadores[Personaje.y[2]+1][Personaje.x[2]+2]==0 && (MapaTiles.mapaFisico[Personaje.y[2]+2][Personaje.x[2]+2]==1 || MapaTiles.jugadores[Personaje.y[2]+2][Personaje.x[2]+2]==1)){
            return 2;
        }
        return 0;
    }
    
    public boolean quemandose2(){
        if(personaje.estaVivo[2]==true){
    	if(Personaje.y[2]+3<Juego.alto && Personaje.y[2]>=0 && Personaje.x[2]+2<Juego.ancho && Personaje.x[2]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[2][i][j]==1)
    					if(MapaTiles.mapaFisico[Personaje.y[2]+i][Personaje.x[2]+j]==2){
    						return true;
    					}
    	}
        }
    	return false;
    }
    
    public boolean curandose2(){
        if(personaje.estaVivo[2]==true){
    	if(Personaje.y[2]+3<Juego.alto && Personaje.y[2]>=0 && Personaje.x[2]+2<Juego.ancho && Personaje.x[2]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[2][i][j]==1)
    					if(MapaTiles.mapaFisico[Personaje.y[2]+i][Personaje.x[2]+j]==3){
    						return true;
    					}
    	}
        }
    	return false;
    }
    public boolean aguandose(){
        if(personaje.estaVivo[2]==true){
    	if(Personaje.y[2]+3<Juego.alto && Personaje.y[2]>=0 && Personaje.x[2]+2<Juego.ancho && Personaje.x[2]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[2][i][j]==1)
    					if(MapaTiles.mapaFisico[Personaje.y[2]+i][Personaje.x[2]+j]==4){//si es agua.
    						return true;
    					}
    	}
        }
    	return false;
    }
    
    
    
    public boolean perdio(){
        if(Teclas.modoJuego==1){
            if(personaje.puntos[2]<=0){
                personaje.estaVivo[2]=false;
                return true;
            }
        }else{
            if(personaje.puntos[2]<=0){
                personaje.estaVivo[2]=false;
                return true;
            }
        }
        return false;
    }
    
    public boolean gano(){
    	//if(j2.estaVivo()==false) return true;
        if(Teclas.modoJuego==1){
            
            for(int i=0;i<3;i++){
                for(int j=0;j<4;j++){
                    int jr=jugadoresRestantes();
                    if(((personaje.getX(2)+i)==Teclas.estrellaX && (personaje.getY(2)+j)==Teclas.estrellaY && personaje.estaVivo[2]==true) || jr<=0){
                        Jugador1.reap=true;
                        Jugador2.reap=true;
                        Jugador3.reap=true;
                        Jugador4.reap=true;
                        //no reemplazar:
                        personaje.vidas[0]=act.vidaJugador[0];//no reemplazar
                        personaje.vidas[1]=act.vidaJugador[1];
                        personaje.vidas[2]=act.vidaJugador[2];
                        personaje.vidas[3]=act.vidaJugador[3];
                        if(Teclas.jugadores>=1){
                            personaje.estaVivo[0]=true;//no reemplazar
                        }
                        if(Teclas.jugadores>=2){
                            personaje.estaVivo[1]=true;
                        }
                        if(Teclas.jugadores>=3){
                            personaje.estaVivo[2]=true;
                        }
                        if(Teclas.jugadores>=4){
                            personaje.estaVivo[3]=true;
                        }
                        personaje.puntos[0]=3;//no reemplazar
                        personaje.puntos[1]=3;
                        personaje.puntos[2]=3;
                        personaje.puntos[3]=3;
                        Balas.bala[0][0][0]=0;//no reemplazar
                        Balas.bala[1][0][0]=0;//no reemplazar
                        Balas.bala[2][0][0]=0;//no reemplazar
                        Balas.bala[3][0][0]=0;//no reemplazar
                        //solo este jugador:
                        
                        if(jr>0){
                            Juego.corriendo=false;//se carga el proximo nivel
                            return true;
                        }else{
                            Juego.perdieron=true;//reaparece a los enemigos.
                        }
                        
                    }
                }
            }
            
        }else{
            
            if(jugadoresRestantes()<=1 && Juego.espera==false){
                reaparecer();
                Juego.corriendo=false;
                return true;
            }
        }
    	
    	return false;
    	
    }
    //fin no reemplazar.
    public int jugadoresRestantes(){
        int contador=0;
        for(int i=0;i<Teclas.jugadores;i++){
            if(personaje.estaVivo[i]==true){
                contador++;
            }
        }
        return contador;
    }
    
    public boolean murio(){
    	
        if(Teclas.modoJuego==1){
            
            if(personaje.vidas[2]<=0){
                personaje.puntos[2]--;
    		direccion=2;
    		personaje.vidas[2]=act.vidaJugador[2];
                if(perdio()){//para q aparezca en el inicio y no en el check...
                    act.checkJugador[2]=-1;
                }
    		return true;
            }
        }else{
            if(personaje.vidas[2]<=0){
    		personaje.puntos[2]--;
    		direccion=2;
    		personaje.vidas[2]=act.vidaJugador[2];
    		return true;
            }
        }
    	
    	return false;
    }
    // tecla sin pulsar
    public void keyReleased(KeyEvent e) {
    	int k=e.getKeyCode();
    	if(k==teclaDisparo){
    		disparo=false;
                disparado=false;
    	}
    	if(k==teclaSalto){
    		espacio=false;
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
                disparo2=true;
    	}
    	if(k==teclaSalto){
    		espacio=true;
    		espacio2=true;
    	}
    	if(k==teclaIzquierda){
    		izquierda=true;
    		izquierda2=true;
    	}
    	if(k==teclaDerecha){
    		derecha=true;
    		derecha2=true;
    	}
    	if(k==teclaArriba){
    		arriba=true;
    		arriba2=true;
    	}
    	if(k==teclaAgacharse){
    		agacharse=true;
    		agacharse2=true;
    	}
        if(k==teclaCorriendo){
    		corriendo=true;
    		corriendo2=true;
    	}
        if(k==teclaCambiar){
    		cambiar=true;
    		cambiar2=true;
    	}
    }
 
    public void keyTyped(KeyEvent e) {
        
    }

}