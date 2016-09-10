package BlockMan;

import java.awt.event.*;

public class Jugador2 implements KeyListener {
	
	//metodos por jugador:
	//animar,getX,getY,setX,setY.
	
	public static int teclaIzquierda=Teclas.teclas[1][0];
	public static int teclaDerecha=Teclas.teclas[1][1];
	public static int teclaDisparo=Teclas.teclas[1][5];
	public static int teclaSalto=Teclas.teclas[1][4];
	public static int teclaAgacharse=Teclas.teclas[1][2];
	public static int teclaArriba=Teclas.teclas[1][3];
        public static int teclaCorriendo=Teclas.teclas[1][6];
        public static int teclaCambiar=Teclas.teclas[1][7];
	
	//nuevo actualizar:
    
    
    public boolean deslizar=false;//Juego.megaman;//si no corre.
        
	public static boolean gano=false;
	
	private int nivel=1;
	//Juego j;
	Balas balas=new Balas();
	Personaje personaje=new Personaje();
        Actualizaciones act=new Actualizaciones();
        
        public static boolean reap=false;
        public static int accionActual=0,indiceRobot=-1;
	//MapaTiles mapa=MapaTiles.DesdeArchivo("map"+nivel+".txt");
	public static boolean disparo=false,disparo2=false,estaSaltando=false,estaCayendo=false,arriba2=false,abajo2=false,espacio2=false,izquierda2=false,derecha2=false,agacharse2=false;
	public static int contadorQuema=0,contadorCura=0,sumaY,altoDelSalto=0,accionAnterior=0,poderDeSalto=5,direccion=2,contador2=0;
        public static boolean techoAnterior=false,direccionado=false,valido=true,nulo=false,moviendose=false,apretando=false,techo=false,firme=true,salto=false,espacio, arriba, izquierda, derecha,agacharse;
        private int marcaPasos=0,contadorSprite=0,contador=0,auxX=personaje.posIniX[0],auxY=personaje.posIniY[0];

        //posiciones de los brazos con respecto al cuerpo del jugador cuando esta de lado(palito).
        public static int brazoIzqX=1,brazoIzqY=1,brazoDerX=3,brazoDerY=1;
        
    public Jugador2(){
        teclaIzquierda=Teclas.teclas[1][0];
        teclaDerecha=Teclas.teclas[1][1];
        teclaDisparo=Teclas.teclas[1][5];
        teclaSalto=Teclas.teclas[1][4];
        teclaAgacharse=Teclas.teclas[1][2];
        teclaArriba=Teclas.teclas[1][3];
        teclaCorriendo=Teclas.teclas[1][6];
        teclaCambiar=Teclas.teclas[1][7];
    }
    public void actualizar(){
        switch(Personaje.cuerpo[0]){
            case 0:
                actualiza2();
                break;
            case 1:
                boolean disparo,espacio,derecha,izquierda,agacharse,arriba;
                disparo=this.disparo && this.disparo2;
                espacio=this.espacio && this.espacio2;
                derecha=this.derecha && this.derecha2;
                izquierda=this.izquierda && this.izquierda2;
                agacharse=this.agacharse && this.agacharse2;
                arriba=this.arriba && this.arriba2;
                MapaTiles.robots.get(this.indiceRobot).actualizar(disparo,espacio,derecha,izquierda,agacharse,arriba);
                break;
            case 2:
                //actualiza3();
                break;
        }
    }
    
    public static int mx=0,my=0,pos=0,posAnterior=0,altura=0,contadorFrames=0,contadorDisparo=0,retardoArmaActual=0;//contadorFrames hasta 8.
    public static boolean cayendo=false,paso=true,saltar=false,disparado=false,cambiado=false,corriendo=false,cambiar=false,cambiar2=false,
            corriendoAire=false,corriendo2=false,volar=false,hiperVelocidad=false,ralentizacion=true,paso2=false,paso3=false;//paso2=para graficos.
    public boolean[] contadorDeRetardo=new boolean[5];//0=quieto, 5 velocidades (0,1,2,4,8).
    
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
            posAnterior=Personaje.accion[1];
            pos=0;
            
            //deslizar:
            if(deslizar && (corriendo || corriendo2) && !desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1)){
                if(izquierda==true || izquierda2==true){
                    direccion=1;
                }
                if(derecha==true || derecha2==true){
                    direccion=2;
                }
                if(direccion==1){
                    izquierda2=true;
                }
                if(direccion==2){
                    derecha2=true;
                }
            }
            
            //comprobamos los cambios en el eje x:
            if(!((izquierda || izquierda2) && (derecha || derecha2)) && (izquierda || izquierda2 || derecha || derecha2)){//si solo esta activada 1 direccion de las 2 y por lo menos 1:
                if(derecha || derecha2){
                    if(deslizar==true && (corriendo || corriendo2) && !desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1)){
                        pos=7;
                    }else{
                        if(paso==true){
                            if(paso2==false){
                                Personaje.contadorSprite[1]=7;
                                paso2=true;
                            }else{
                                Personaje.contadorSprite[1]=9;
                                paso2=false;
                            }
                            paso=false;
                            pos=1;
                        }else{
                            if(paso3==false){
                                Personaje.contadorSprite[1]=8;
                                paso3=true;
                            }else{
                                Personaje.contadorSprite[1]=10;
                                paso3=false;
                            }
                            paso=true;
                            pos=0;
                        }
                    }
                    mx++;
                }
                if(izquierda || izquierda2){
                    if(deslizar==true && (corriendo || corriendo2) && !desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1)){
                        pos=8;
                    }else{
                        if(paso==true){
                            if(paso2==false){
                                Personaje.contadorSprite[1]=1;
                                paso2=true;
                            }else{
                                Personaje.contadorSprite[1]=3;
                                paso2=false;
                            }
                            paso=false;
                            pos=3;
                        }else{
                            if(paso3==false){
                                Personaje.contadorSprite[1]=2;
                                paso3=true;
                            }else{
                                Personaje.contadorSprite[1]=4;
                                paso3=false;
                            }
                            paso=true;
                            pos=0;
                        }
                    }
                    mx--;
                }
            }else{
                paso=true;
                paso2=false;
                paso3=false;
                if(direccion==1){//izq
                    Personaje.contadorSprite[1]=5;
                }else{
                    Personaje.contadorSprite[1]=11;
                }
            }

            //comprobacion de otras posiciones:

            if(saltar && (espacio || espacio2) || cayendo){
                if(mx==1){
                    pos=2;
                    Personaje.contadorSprite[1]=7;
                }
                if(mx==-1){
                    pos=4;
                    Personaje.contadorSprite[1]=1;
                }
            }

            if((agacharse || agacharse2) && !(deslizar && (corriendo || corriendo2) && !desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1))){
                pos=6;
                if(mx==1){
                    Personaje.contadorSprite[1]=13;
                }else{
                    if(mx==-1){
                        Personaje.contadorSprite[1]=12;
                    }else{
                        if(direccion==2){
                            Personaje.contadorSprite[1]=13;
                        }else{
                            Personaje.contadorSprite[1]=12;
                        }
                    }
                }
            }

            //la comprobacion de la paleta:
            if((arriba || arriba2) && !(deslizar && (corriendo || corriendo2) && !desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1))){
                pos=5;
                if(mx==1){
                    Personaje.contadorSprite[1]=6;
                }else{
                    if(mx==-1){
                        Personaje.contadorSprite[1]=0;
                    }else{
                        if(direccion==2){
                            Personaje.contadorSprite[1]=6;
                        }else{
                            Personaje.contadorSprite[1]=0;
                        }
                    }
                }
            }
            
            //reestablecer las variables:
            izquierda2=false;
            derecha2=false;
            arriba2=false;
            abajo2=false;
            agacharse2=false;
            corriendo2=false;
        }
        int posAux;
        //comprobamos los cambios en el eje y:
        if(contadorDeRetardo[3]==true || (((contadorDeRetardo[3]==true && ralentizacion==false) || contadorDeRetardo[1]==true || contadorDeRetardo[4]==true || (contadorDeRetardo[2]==true)) && !desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1))){
            if(!desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1)){//si esta pisando o sostenido en algo se restaura la altura(puede saltar con toda la potencia).
                altura=0;
                if(deslizar && (corriendo || corriendo2)){
                    if(direccion==1){// && posAnterior==8
                        if(personaje.getX(1)>=0 && personaje.getY(1)-1>0 && ((MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)]==1)) && desbloqueado(8,Personaje.x[1],Personaje.y[1]-1)){//error
                            my=-1;
                        }
                    }
                    if(direccion==2){// && posAnterior==7
                        if(personaje.getX(1)+4<Juego.ancho && personaje.getY(1)-1>0 && ((MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)+4]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)+4]==1)) && desbloqueado(7,Personaje.x[1],Personaje.y[1]-1)){
                            my=-1;
                        }
                    }
                }
            }
            if((espacio || espacio2) && (((cayendo==false  && saltar==true) && altura<Actualizaciones.saltoJugador[1]) || volar==true)){
                my=-1;
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
                if(desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1)){
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
                if((espacio || espacio2) && cayendo==false && saltar==true && altura<Actualizaciones.saltoJugador[1]){
                    my=-1;
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
                my=1;
            }
            //el espacio2 se desactiva aparte (para que pueda saltar si es que se apreto saltar por lo menos un cuadrito siempre)
            espacio2=false;
            
            //si no esta deslizando se le quita esta posicion(y ademas esta quieto):
            if(((posAnterior==7 || posAnterior==8) && mx==0 && my==0) || ((derecha || derecha2) && (izquierda || izquierda2) && (deslizar && (corriendo || corriendo2) && !desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+1)))){
                
                posAux=pos;
                pos=6;
                
                if(!desbloqueado(pos,Personaje.x[1],Personaje.y[1])){
                    pos=posAux;
                }
            }
        }
        
        
        
        if(contadorDeRetardo[1]==true || contadorDeRetardo[2]==true || contadorDeRetardo[3]==true || contadorDeRetardo[4]==true){
            
            //comprobaciones de bloqueos:
            if((personaje.getX(1)+2+mx>0) && (personaje.getX(1)+2+mx<Juego.ancho) && !(MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)+2+mx]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)+2+mx]==1 || MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)+2]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)+2]==1) && (desbloqueado(pos,Personaje.x[1]+mx,Personaje.y[1]+my) || desbloqueado(posAnterior,Personaje.x[1]+mx,Personaje.y[1]+my))){//si ademas esta empalado.
                Personaje.x[1]+=mx;
                Personaje.y[1]+=my;
            }else{
                if((personaje.getX(1)+2+mx>0) && (personaje.getX(1)+2+mx<Juego.ancho) && !(MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)+2+mx]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)+2+mx]==1 || MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)+2]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)+2]==1) && (desbloqueado(pos,Personaje.x[1]+mx,Personaje.y[1]))){//si ademas esta empalado.
                    Personaje.x[1]+=mx;
                }else{
                    if(desbloqueado(posAnterior,Personaje.x[1]+mx,Personaje.y[1])){
                        pos=posAnterior;
                        if(pos==7 || pos==8){
                            pos=6;
                            if(desbloqueado(pos,Personaje.x[1]+mx,Personaje.y[1])){
                                Personaje.x[1]+=mx;
                            }else{
                                if(!desbloqueado(pos,Personaje.x[1],Personaje.y[1])){
                                    pos=posAnterior;
                                }
                            }
                        }
                        Personaje.x[1]+=mx;
                    }else{

                        if(posAnterior==7 || posAnterior==8){
                            posAux=pos;
                            pos=6;
                            if(desbloqueado(pos,Personaje.x[1]+mx,Personaje.y[1])){
                                Personaje.x[1]+=mx;
                            }else{
                                if(!desbloqueado(pos,Personaje.x[1],Personaje.y[1])){
                                    pos=posAux;
                                }
                            }
                        }
                        //corriendoAire=false;
                    }
                    switch(pos){
                        case 0:
                            if(direccion==1){
                                Personaje.contadorSprite[1]=5;
                            }else{
                                Personaje.contadorSprite[1]=11;
                            }
                            break;
                        case 1:
                            Personaje.contadorSprite[1]=7;
                            break;
                        case 2:
                            Personaje.contadorSprite[1]=7;
                            break;
                        case 3:
                            Personaje.contadorSprite[1]=1;
                            break;
                        case 4:
                            Personaje.contadorSprite[1]=1;
                            break;
                        case 5:
                            if(direccion==1){
                                Personaje.contadorSprite[1]=0;
                            }else{
                                Personaje.contadorSprite[1]=6;
                            }
                            break;
                        case 6:
                            if(direccion==1){
                                Personaje.contadorSprite[1]=12;
                            }else{
                                Personaje.contadorSprite[1]=13;
                            }
                            break;
                    }
                }
                if(desbloqueado(pos,Personaje.x[1],Personaje.y[1]+my)){
                    Personaje.y[1]+=my;
                }else{
                    if(desbloqueado(posAnterior,Personaje.x[1],Personaje.y[1]+my)){
                        pos=posAnterior;
                        if(pos==7 || pos==8){
                            pos=6;
                            if(desbloqueado(pos,Personaje.y[1]+my,Personaje.y[1])){
                                Personaje.y[1]+=my;
                            }else{
                                if(!desbloqueado(pos,Personaje.x[1],Personaje.y[1])){
                                    pos=posAnterior;
                                }
                            }
                        }
                        switch(pos){
                            case 0:
                                if(direccion==1){
                                    Personaje.contadorSprite[1]=5;
                                }else{
                                    Personaje.contadorSprite[1]=11;
                                }
                                break;
                            case 1:
                                Personaje.contadorSprite[1]=7;
                                break;
                            case 2:
                                Personaje.contadorSprite[1]=7;
                                break;
                            case 3:
                                Personaje.contadorSprite[1]=1;
                                break;
                            case 4:
                                Personaje.contadorSprite[1]=1;
                                break;
                            case 5:
                                if(direccion==1){
                                    Personaje.contadorSprite[1]=0;
                                }else{
                                    Personaje.contadorSprite[1]=6;
                                }
                                break;
                            case 6:
                                if(direccion==1){
                                    Personaje.contadorSprite[1]=12;
                                }else{
                                    Personaje.contadorSprite[1]=13;
                                }
                                break;
                        }
                        Personaje.y[1]+=my;
                    }else{
                        if(volar==false){
                            saltar=false;
                        }
                        corriendoAire=true;//si no esta saltando puede moverse rapidamente en el aire (si esta corriendo y salta).
                    }

                }
            }
            
            //reestablecer la posicion si es necesario:
            if(!desbloqueado(pos,Personaje.x[1],Personaje.y[1])){
                pos=posAnterior;
                switch(pos){
                    case 0:
                        if(direccion==1){
                            Personaje.contadorSprite[1]=5;
                        }else{
                            Personaje.contadorSprite[1]=11;
                        }
                        break;
                    case 1:
                        Personaje.contadorSprite[1]=7;
                        break;
                    case 2:
                        Personaje.contadorSprite[1]=7;
                        break;
                    case 3:
                        Personaje.contadorSprite[1]=1;
                        break;
                    case 4:
                        Personaje.contadorSprite[1]=1;
                        break;
                    case 5:
                        if(direccion==1){
                            Personaje.contadorSprite[1]=0;
                        }else{
                            Personaje.contadorSprite[1]=6;
                        }
                        break;
                    case 6:
                        if(direccion==1){
                            Personaje.contadorSprite[1]=12;
                        }else{
                            Personaje.contadorSprite[1]=13;
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
                if(personaje.vidas[1]>0){
                    personaje.vidas[1]--;
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
                if(personaje.vidas[1]<act.vidaJugador[1]){
                    personaje.vidas[1]++;
                }
            }
            
            //items
            switch(tomoUnItem()){
                case 0://nada.
                    break;
                case 26://pelotas.
                    act.municionPelotas[1]=act.municionPelotas[1]+act.tamañoCartuchoPelotas;
                    act.armaActual[1]=2;//se auto cambia el arma...
                    break;
                case 33://misiles.
                    act.municionMisiles[1]=act.municionMisiles[1]+act.tamañoCartuchoMisiles;
                    break;
                case 37://capsula curativa.
                    Personaje.vidas[1]+=act.curacionCapsulas;
                    if(Personaje.vidas[1]>act.vidaJugador[1]) Personaje.vidas[1]=act.vidaJugador[1];
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
                act.checkJugador[1]=indiceCheck;
            }
            
            if(arriba || arriba2){
                switch(sePuedeAgarrar()){
                    case 1://izquierda:
                        pos=3;
                        Personaje.contadorSprite[1]=1;
                        break;
                    case 2://izquierda:
                        pos=1;
                        Personaje.contadorSprite[1]=7;
                        break;
                }
            }
            
            
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
        
        posAnterior=pos;
        //asignar la posicion:
        personaje.animar(1,pos);
        
        //cambio de arma:
        if((cambiar || cambiar2) && cambiado==false){
            cambiado=true;
            boolean cambio=false;
            while(cambio==false){
                if(act.armaActual[1]<(act.limiteArmas-1)){
                    act.armaActual[1]++;
                }else{
                    act.armaActual[1]=1;
                }
                switch(act.armaActual[1]){
                    case 1:
                        cambio=true;
                        break;
                    case 2:
                        if(act.municionPelotas[1]>0){
                            cambio=true;
                        }
                        break;
                    case 3:
                        if(act.municionMisiles[1]>0){
                            cambio=true;
                        }
                        break;
                }
            }
        }
        
        //se define el retardo de acuerdo al arma.
        switch(act.armaActual[1]){
            case 0://nada
                retardoArmaActual=0;
                break;
            case 1://pistola(la ametralladora aparece cuando tiene mejora de 2x velocidad ataque)
                retardoArmaActual=act.retardoBalas/act.velocidadAtaque[1];
                break;
            case 2://arma boteadora.
                retardoArmaActual=act.retardoPelotas/act.velocidadAtaque[1];
                break;
            case 3://lanza misiles
                retardoArmaActual=act.retardoMisiles/act.velocidadAtaque[1];
                break;
            
        }
        
        if(contadorDisparo>0){
            contadorDisparo++;
            if(contadorDisparo>=retardoArmaActual){
                contadorDisparo=0;
            }
        }
        
        if((disparo || disparo2) && pos!=5 && contadorDisparo==0 && disparado==false){// (para q dispare estilo megaman)
            
            if(Juego.megaman){
                disparado=true;
            }else{
                disparado=false;
            }
            switch(act.armaActual[1]){
                case 0://nada
                break;
            case 1://pistola(la ametralladora aparece cuando tiene mejora de 2x velocidad ataque)
                balas.nuevaBala(0,direccion,Personaje.x[1],Personaje.y[1]);
                contadorDisparo++;//(1)
                break;
            case 2://arma boteadora.
                balas.nuevaPelota(0,direccion,Personaje.x[1],Personaje.y[1]);
                contadorDisparo++;//(1)
                break;
            case 3://lanza misiles
                //balas.nuevoMisil(0,direccion,Personaje.x[1],Personaje.y[1]);
                contadorDisparo++;//(1)
                break;
            }
    	}
        if(Juego.megaman){
            contadorDisparo=0;
        }
        disparo2=false;
        cambiar2=false;
        
        //incrementa y resetea el contador si es necesario:
        contadorFrames++;
        if(contadorFrames>=8){
            contadorFrames=0;
        }
        personaje.direccion[1]=this.direccion;
    }
//nuevo metodo:
    public void reaparecer(){
        this.reap=false;
        if(act.checkJugador[1]==-1){
            personaje.setX(1,personaje.posIniX[1]-1);//x
            personaje.setY(1,personaje.posIniY[1]);
        }else{
            personaje.setX(1,MapaTiles.punto.get(act.checkJugador[1]).posicionPuntoX-1-1);//x
            personaje.setY(1,MapaTiles.punto.get(act.checkJugador[1]).posicionPuntoY-3);
        }
    	auxX=personaje.getX(1);
    	auxY=personaje.getY(1);
    }
    
    public int tomoUnItem(){
        for(int k=0;k<MapaTiles.items.size();k++){
            for(int i=0;i<4;i++){
                for(int j=0;j<5;j++){
                    if(personaje.personaje[1][i][j]==1){
    			if((Personaje.y[1]+i)==MapaTiles.items.get(k).posicionItemY && (Personaje.x[1]+j)==MapaTiles.items.get(k).posicionItemX){
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
                for(int j=0;j<5;j++){
                    if(personaje.personaje[1][i][j]==1){
    			if((Personaje.y[1]+i)==MapaTiles.punto.get(k).posicionPuntoY && (Personaje.x[1]+j)==MapaTiles.punto.get(k).posicionPuntoX){
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
        personaje.animar(1,posicion);
        
        for(int i=0;i<4;i++)
            for(int j=0;j<5;j++)
                if(personaje.personaje[1][i][j]==1){
                    if(!(y+i<Juego.alto && y+i>=0 && x+j<Juego.ancho && x+j>=0)) return false;//solo si se sale de los limites
                        if(MapaTiles.mapaFisico[y+i][x+j]==1 || MapaTiles.jugadores[y+i][x+j]==1) return false;
                    }
    	return true;
    }
    public int sePuedeAgarrar(){//0=no,1=izq,2=der.
        if((Personaje.x[1]+this.brazoIzqX)>=0 && (izquierda || izquierda2) && MapaTiles.mapaFisico[Personaje.y[1]+this.brazoIzqY][Personaje.x[1]+this.brazoIzqX]==0 && MapaTiles.jugadores[Personaje.y[1]+this.brazoIzqY][Personaje.x[1]+this.brazoIzqX]==0 && (MapaTiles.mapaFisico[Personaje.y[1]+this.brazoIzqY+1][Personaje.x[1]+this.brazoIzqX]==1 || MapaTiles.jugadores[Personaje.y[1]+this.brazoIzqY+1][Personaje.x[1]+this.brazoIzqY]==1)){
            return 1;
        }
        if((Personaje.x[1]+this.brazoDerX)<Juego.ancho && (derecha || derecha2) && MapaTiles.mapaFisico[Personaje.y[1]+this.brazoDerY][Personaje.x[1]+this.brazoDerX]==0 && MapaTiles.jugadores[Personaje.y[1]+this.brazoDerY][Personaje.x[1]+this.brazoDerX]==0 && (MapaTiles.mapaFisico[Personaje.y[1]+this.brazoDerY+1][Personaje.x[1]+this.brazoDerX]==1 || MapaTiles.jugadores[Personaje.y[1]+this.brazoDerY+1][Personaje.x[1]+this.brazoDerX]==1)){
            return 2;
        }
        return 0;
    }
    
    public boolean quemandose2(){
        if(personaje.estaVivo[1]==true){
    	//if(Personaje.y[1]+3<Juego.alto && Personaje.y[1]>=0 && Personaje.x[1]+2<Juego.ancho && Personaje.x[1]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<5;j++)
    				if(personaje.personaje[1][i][j]==1)
                                    if((Personaje.y[1]+i<Juego.alto && Personaje.y[1]+i>=0 && Personaje.x[1]+j<Juego.ancho && Personaje.x[1]+j>=0))
    					if(MapaTiles.mapaFisico[Personaje.y[1]+i][Personaje.x[1]+j]==2){
    						return true;
    					}
    	//}
        }
    	return false;
    }
    
    public boolean curandose2(){
        if(personaje.estaVivo[1]==true){
    	//if(Personaje.y[1]+3<Juego.alto && Personaje.y[1]>=0 && Personaje.x[1]+2<Juego.ancho && Personaje.x[1]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<5;j++)
    				if(personaje.personaje[1][i][j]==1)
                                    if((Personaje.y[1]+i<Juego.alto && Personaje.y[1]+i>=0 && Personaje.x[1]+j<Juego.ancho && Personaje.x[1]+j>=0))
    					if(MapaTiles.mapaFisico[Personaje.y[1]+i][Personaje.x[1]+j]==3){
    						return true;
    					}
    	//}
        }
    	return false;
    }
    public boolean aguandose(){
        if(personaje.estaVivo[1]==true){
    	//if(Personaje.y[1]+3<Juego.alto && Personaje.y[1]>=0 && Personaje.x[1]+2<Juego.ancho && Personaje.x[1]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[1][i][j]==1)
                                    if((Personaje.y[1]+i<Juego.alto && Personaje.y[1]+i>=0 && Personaje.x[1]+j<Juego.ancho && Personaje.x[1]+j>=0))
    					if(MapaTiles.mapaFisico[Personaje.y[1]+i][Personaje.x[1]+j]==4 || MapaTiles.mapaFisico[Personaje.y[1]+i][Personaje.x[1]+j]==5){//si es agua.
    						return true;
    					}
    	//}
        }
    	return false;
    }
    
    
    
    public boolean perdio(){
        if(Teclas.modoJuego==1){
            if(personaje.puntos[1]<=0){
                personaje.estaVivo[1]=false;
                return true;
            }
        }else{
            if(personaje.puntos[1]<=0){
                personaje.estaVivo[1]=false;
                return true;
            }
        }
        return false;
    }
    
    public boolean gano(){
    	//if(j2.estaVivo()==false) return true;
        if(Teclas.modoJuego==1){
            
            for(int i=0;i<5;i++){
                for(int j=0;j<4;j++){
                    int jr=jugadoresRestantes();
                    if(((personaje.getX(1)+i)==Teclas.estrellaX && (personaje.getY(1)+j)==Teclas.estrellaY && personaje.estaVivo[1]==true) || jr<=0){
                        Jugador1.reap=true;
                        Jugador2.reap=true;
                        Jugador3.reap=true;
                        Jugador4.reap=true;
                        personaje.vidas[0]=act.vidaJugador[0];
                        personaje.vidas[1]=act.vidaJugador[1];
                        personaje.vidas[2]=act.vidaJugador[2];
                        personaje.vidas[3]=act.vidaJugador[3];
                        if(Teclas.jugadores>=1){
                            personaje.estaVivo[0]=true;
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
                        personaje.puntos[0]=3;
                        personaje.puntos[1]=3;
                        personaje.puntos[2]=3;
                        personaje.puntos[3]=3;
                        Balas.bala[0][0][0]=0;
                        Balas.bala[1][0][0]=0;
                        Balas.bala[2][0][0]=0;
                        Balas.bala[3][0][0]=0;
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
            /*
            if(gano==true){
    		gano=false;
    		personaje.puntos[1]++;
    		if(personaje.puntos[1]==10) {
    			reaparecer();
    			Juego.corriendo=false;
    			personaje.puntos[1]=0;
	    		personaje.puntos[1]=0;
	    		personaje.puntos[2]=0;
	    		personaje.puntos[3]=0;
	    		personaje.vidas[1]=act.limiteVida;
	    		Balas.bala[0][0][0]=0;
	    		Balas.bala[1][0][0]=0;
                        Balas.bala[2][0][0]=0;
	    		Balas.bala[3][0][0]=0;
	    		direccion=2;
    		}
    		return true;
            }
            */
            if(jugadoresRestantes()<=1 && Juego.espera==false){
                reaparecer();
                Juego.corriendo=false;
                return true;
            }
        }
    	
    	return false;
    	
    }
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
    	/*
        if(Teclas.modoJuego==1){
            if(personaje.vidas[1]<=0){
    		direccion=2;
    		personaje.vidas[1]=act.limiteVida;
    		return true;
            }
        }else{
            if(personaje.vidas[1]<=0){
    		if(Juego.computadora==true){
    			Bot.gano=true;
    		}else{
        		Jugador2.gano=true;
    		}
    		direccion=2;
    		personaje.vidas[1]=act.limiteVida;
    		return true;
            }
        }
    	
    	return false;
    	*/
        if(Teclas.modoJuego==1){
            
            if(personaje.vidas[1]<=0){
                personaje.puntos[1]--;
    		direccion=2;
    		personaje.vidas[1]=act.vidaJugador[1];
                if(perdio()){//para q aparezca en el inicio y no en el check...
                    act.checkJugador[1]=-1;
                }
    		return true;
            }
        }else{
            if(personaje.vidas[1]<=0){
    		personaje.puntos[1]--;
    		direccion=2;
    		personaje.vidas[1]=act.vidaJugador[1];
    		return true;
            }
        }
    	
    	return false;
    }
    //antiguos meodos.
    /*
    public void cargarNivel(){
    	nivel=nivel+1;
    	j.actualizarNivel(nivel);
    	j.actualizarCorriendo(false);
    }
    */
    
//metodos antiguos:
    public boolean quemandose(){
        if(personaje.estaVivo[1]==true){
    	//if(auxY+3<Juego.alto && auxY>=0 && auxX+2<Juego.ancho && auxX>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<5;j++)
    				if(personaje.personaje[1][i][j]==1)
    					if(MapaTiles.mapaFisico[auxY+i][auxX+j]==2){
    						return true;
    					}
    	//}
        }
    	return false;
    }
    
    public boolean curandose(){
        if(personaje.estaVivo[1]==true){
    	//if(auxY+3<Juego.alto && auxY>=0 && auxX+2<Juego.ancho && auxX>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<5;j++)
    				if(personaje.personaje[1][i][j]==1)
    					if(MapaTiles.mapaFisico[auxY+i][auxX+j]==3){
    						return true;
    					}
    	//}
        }
    	return false;
    }
    /*
    public void comprobacionX(){//este metodo debe ir en la comprobacion del eje x.
    	for(int i=0;i<4;i++){
    		for(int j=0;j<3;j++){
    			if(Personaje.personaje[1][i][j]==1 && MapaTiles.jugadores[auxY+i][auxX+j]==1){//si esta en contacto con el otro jugador.
    				auxX=personaje.getX(1);
    			}
    		}
    	}
    }
    public void comprobacionY(){//este metodo debe ir en la comprobacion del eje y.
    	for(int i=0;i<4;i++){
    		for(int j=0;j<3;j++){
    			if(Personaje.personaje[1][i][j]==1 && MapaTiles.jugadores[auxY+i][auxX+j]==1){//si esta en contacto con el otro jugador.
    				auxY=personaje.getY(1);
    			}
    		}
    	}
    }
    */
    public boolean estaAgarrado(){
    	int accion=accionActual;
    	if(derecha==true){
    		personaje.animar(1,1);
        	if(estaBloqueado(0)==true && bloqueado()==false){
        		accionActual=1;
        		return true;
        	}
        	
    	}
    	if(izquierda==true){
    		personaje.animar(1,3);
        	if(estaBloqueado(0)==true && bloqueado()==false){
        		accionActual=3;
        		return true;
        	}
        	
    	}
    	
    	accionActual=accion;
    	personaje.animar(1,accionActual);
    	return false;
    }
    public boolean bloqueado(){
    	if(estaBloqueado(4)) return true;
    	
    	return false;
    }
    public void desbloquear(){
    	if(techoAnterior==true){
    		auxY=personaje.getY(1);
        	auxX=personaje.getX(1);
    	}else{
    		auxY=(auxY-1);
        	if(bloqueado()==false) return;
        	auxY=(auxY+1);
        	auxY=(auxY+1);
        	if(bloqueado()==false) return;
        	auxY=(auxY-1);
        	auxX=(auxX+1);
        	if(bloqueado()==false) return;
        	auxX=(auxX-1);
        	auxX=(auxX-1);
        	if(bloqueado()==false) return;
        	auxX=(auxX+1);
        	if(bloqueado()==false) return;
        	reaparecer();
    	}
    }
    public boolean semiEncerrado(){
    	personaje.animar(1,0);
    	if(bloqueado()) return true;
    	return false;
    }
    /*
    public boolean encerrado(){
    	if((MapaTiles.mapaFisico[auxY][auxX]==1 || MapaTiles.mapaFisico[auxY+1][auxX]==1 || MapaTiles.mapaFisico[auxY+2][auxX]==1
    		|| MapaTiles.mapaFisico[auxY+3][auxX]==1) && (MapaTiles.mapaFisico[auxY+1][auxX+2]==1
    		|| MapaTiles.mapaFisico[auxY+2][auxX+2]==1 || MapaTiles.mapaFisico[auxY+3][auxX+2]==1 || MapaTiles.mapaFisico[auxY][auxX+2]==1))
    		return true;
    	return false;
    }
    */
    /*
    public boolean hayPiso(){
    	if(auxY+4<Juego.alto && auxY>=0 && auxX+2<Juego.ancho && auxX>=0){
    		if((MapaTiles.mapaFisico[auxY+4][auxX]==1 || MapaTiles.jugadores[auxY+4][auxX]==1) && (MapaTiles.mapaFisico[auxY+4][auxX+1]==1 || MapaTiles.jugadores[auxY+4][auxX+1]==1) && (MapaTiles.mapaFisico[auxY+4][auxX+2]==1 || MapaTiles.jugadores[auxY+4][auxX+2]==1)){
    			return true;
    		}
    	}
    	return false;
    }
    */
    public boolean hayAlgoDePiso(){
    	//if(auxY+4<Juego.alto && auxY>=0 && auxX+2<Juego.ancho && auxX>=0){
    		if(MapaTiles.mapaFisico[auxY+4][auxX+1]==1 || MapaTiles.mapaFisico[auxY+4][auxX+2]==1 || MapaTiles.mapaFisico[auxY+4][auxX+3]==1){//x
    			return true;
    		}
    		if(MapaTiles.jugadores[auxY+4][auxX+1]==1 || MapaTiles.jugadores[auxY+4][auxX+2]==1 || MapaTiles.jugadores[auxY+4][auxX+3]==1){//x
    			return true;
    		}
    	//}
    	return false;
    }
    public boolean estaBloqueado(int direccion){
    	int sumaX=0,sumaY=0;
    	switch(direccion){
    	case 0://abajo
    		sumaY=1;
    		break;
    	case 1://a la der
    		sumaX=1;
    		break;
    	case 2://a la izq
    		sumaX=-1;
    		break;
    	case 3://arriba
    		sumaY=-1;
    		break;
    	case 4://el mismo personaje
    		break;
    	}
    	
    	for(int i=0;i<4;i++)
            for(int j=0;j<5;j++)
    		if(personaje.personaje[1][i][j]==1){
                    if(!(auxY+sumaY+i<Juego.alto && auxY+sumaY+i>=0 && auxX+sumaX+j<Juego.ancho && auxX+sumaX+j>=0)){ return true;}
                    if(MapaTiles.mapaFisico[auxY+i+sumaY][auxX+j+sumaX]==1 || MapaTiles.jugadores[auxY+i+sumaY][auxX+j+sumaX]==1){ return true;}//if(MapaTiles.mapaFisico[auxY+i+sumaY][auxX+j+sumaX]==1) System.out.println("auxX="+auxX+" - auxY="+auxY);
                }
    	//}else{
    	//	return true;
    	//}
    	
    	return false;
    }
    
    public boolean seguirAgachado(){
        
        if((MapaTiles.mapaFisico[personaje.getY(1)][personaje.getX(1)+2]==1 || MapaTiles.jugadores[personaje.getY(1)][personaje.getX(1)+2]==1) && (MapaTiles.mapaFisico[personaje.getY(1)][personaje.getX(1)]==1 || MapaTiles.mapaFisico[personaje.getY(1)][personaje.getX(1)+2]==1 || MapaTiles.jugadores[personaje.getY(1)][personaje.getX(1)]==1 || MapaTiles.jugadores[personaje.getY(1)][personaje.getX(1)+2]==1)){//si esta bloqueado.
            return true;
        }
        
        return false;
    }
    /*
    //nuevo metodo:
    public void reaparecer(){
        this.reap=false;
        if(act.checkJugador[1]==-1){
            personaje.setX(1,personaje.posIniX[1]);
            personaje.setY(1,personaje.posIniY[1]);
        }else{
            personaje.setX(1,MapaTiles.punto.get(act.checkJugador[1]).posicionPuntoX-1);
            personaje.setY(1,MapaTiles.punto.get(act.checkJugador[1]).posicionPuntoY-3);
        }
    	auxX=personaje.getX(1);
    	auxY=personaje.getY(1);
    }
    
    public int tomoUnItem(){
        for(int k=0;k<MapaTiles.items.size();k++){
            for(int i=0;i<4;i++){
                for(int j=0;j<3;j++){
                    if(personaje.personaje[1][i][j]==1){
    			if((Personaje.y[1]+i)==MapaTiles.items.get(k).posicionItemY && (Personaje.x[1]+j)==MapaTiles.items.get(k).posicionItemX){
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
                    if(personaje.personaje[1][i][j]==1){
    			if((Personaje.y[1]+i)==MapaTiles.punto.get(k).posicionPuntoY && (Personaje.x[1]+j)==MapaTiles.punto.get(k).posicionPuntoX){
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
        personaje.animar(1,posicion);
        if(y+3<Juego.alto && y>=0 && x+2<Juego.ancho && x>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[1][i][j]==1)
    					if(MapaTiles.mapaFisico[y+i][x+j]==1 || MapaTiles.jugadores[y+i][x+j]==1)
    						return false;
    	}else{
    		return false;
    	}
    	return true;
    }
    public int sePuedeAgarrar(){//0=no,1=izq,2=der.
        if((izquierda || izquierda2) && MapaTiles.mapaFisico[Personaje.y[1]+1][Personaje.x[1]]==0 && MapaTiles.jugadores[Personaje.y[1]+1][Personaje.x[1]]==0 && (MapaTiles.mapaFisico[Personaje.y[1]+2][Personaje.x[1]]==1 || MapaTiles.jugadores[Personaje.y[1]+2][Personaje.x[1]]==1)){
            return 1;
        }
        if((derecha || derecha2) && MapaTiles.mapaFisico[Personaje.y[1]+1][Personaje.x[1]+2]==0 && MapaTiles.jugadores[Personaje.y[1]+1][Personaje.x[1]+2]==0 && (MapaTiles.mapaFisico[Personaje.y[1]+2][Personaje.x[1]+2]==1 || MapaTiles.jugadores[Personaje.y[1]+2][Personaje.x[1]+2]==1)){
            return 2;
        }
        return 0;
    }
    
    public boolean quemandose2(){
        if(personaje.estaVivo[1]==true){
    	if(Personaje.y[1]+3<Juego.alto && Personaje.y[1]>=0 && Personaje.x[1]+2<Juego.ancho && Personaje.x[1]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[1][i][j]==1)
    					if(MapaTiles.mapaFisico[Personaje.y[1]+i][Personaje.x[1]+j]==2){
    						return true;
    					}
    	}
        }
    	return false;
    }
    
    public boolean curandose2(){
        if(personaje.estaVivo[1]==true){
    	if(Personaje.y[1]+3<Juego.alto && Personaje.y[1]>=0 && Personaje.x[1]+2<Juego.ancho && Personaje.x[1]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[1][i][j]==1)
    					if(MapaTiles.mapaFisico[Personaje.y[1]+i][Personaje.x[1]+j]==3){
    						return true;
    					}
    	}
        }
    	return false;
    }
    public boolean aguandose(){
        if(personaje.estaVivo[1]==true){
    	if(Personaje.y[1]+3<Juego.alto && Personaje.y[1]>=0 && Personaje.x[1]+2<Juego.ancho && Personaje.x[1]>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[1][i][j]==1)
    					if(MapaTiles.mapaFisico[Personaje.y[1]+i][Personaje.x[1]+j]==4){//si es agua.
    						return true;
    					}
    	}
        }
    	return false;
    }
    
    
    
    public boolean perdio(){
        if(Teclas.modoJuego==1){
            if(personaje.puntos[1]<=0){
                personaje.estaVivo[1]=false;
                return true;
            }
        }else{
            if(personaje.puntos[1]<=0){
                personaje.estaVivo[1]=false;
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
                    if(((personaje.getX(1)+i)==Teclas.estrellaX && (personaje.getY(1)+j)==Teclas.estrellaY && personaje.estaVivo[1]==true) || jr<=0){
                        Jugador1.reap=true;
                        Jugador2.reap=true;
                        Jugador3.reap=true;
                        Jugador4.reap=true;
                        //no reemplazar:
                        personaje.vidas[1]=act.vidaJugador[1];//no reemplazar
                        personaje.vidas[1]=act.vidaJugador[1];
                        personaje.vidas[2]=act.vidaJugador[2];
                        personaje.vidas[3]=act.vidaJugador[3];
                        if(Teclas.jugadores>=1){
                            personaje.estaVivo[1]=true;//no reemplazar
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
                        personaje.puntos[1]=3;//no reemplazar
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
            
            if(personaje.vidas[1]<=0){
                personaje.puntos[1]--;
    		direccion=2;
    		personaje.vidas[1]=act.vidaJugador[1];
                if(perdio()){//para q aparezca en el inicio y no en el check...
                    act.checkJugador[1]=-1;
                }
    		return true;
            }
        }else{
            if(personaje.vidas[1]<=0){
    		personaje.puntos[1]--;
    		direccion=2;
    		personaje.vidas[1]=act.vidaJugador[1];
    		return true;
            }
        }
    	
    	return false;
    }
    */
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
