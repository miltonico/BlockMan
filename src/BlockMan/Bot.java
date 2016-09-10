package BlockMan;


public class Bot {
	
	public static boolean gano=false;
	
	private int nivel=1;
	//Juego j;
	Balas balas=new Balas();
	Personaje personaje=new Personaje();
        
        Actualizaciones act=new Actualizaciones();
        public static boolean reap=false;


	private boolean disparo=false,estaSaltando=false,estaCayendo=false,arriba2=false,abajo2=false,espacio2=false,izquierda2=false,derecha2=false,agacharse2=false,abajo=false;
	private int contadorQuema=0, quieto=0,saltando=0,izquierdando=0,derechando=0,sumaY,altoDelSalto=0,accionAnterior=0,poderDeSalto=5,direccion=1,contador2=0;
    private boolean techoAnterior=false,direccionado=false,valido=true,nulo=false,moviendose=false,apretando=false,techo=false,firme=true,salto=false,espacio, arriba, izquierda, derecha,agacharse;
    private int marcaPasos=0,altura=0,contador=0,accionActual=0,auxX=personaje.posIniX[1],auxY=personaje.posIniY[1],ancho=Juego.ancho,alto=Juego.alto;
    
    //Jugador2 j2=new Jugador2();
    
    //Jugador2 j2=new Jugador2();
    
    public void actualiza() {
    	//metodos nuevos:
    	
    	int rbi=revisarBalasIzq();
    	int rbd=revisarBalasDer();
    	//comienzo de la reaccion:
    	
    	if(saltando==0 && quieto==0 && izquierdando==0 && derechando==0 && estaBloqueado(0)==true){
    		
    		if(rbi==0 && rbd==0){
        		
    			//evadir o atacar:
    			int azar=(int)(Math.random()*3);
    			int azar2=(int)(Math.random()*5);
    			if(azar==0){
    				derechando=azar2;
    			}else{
    				if(azar==1){
    					izquierdando=azar2;
    				}else{
    					quieto=azar2;
    				}	
    			}
    			
        	}else{
        		saltando=6;
        		arriba=true;
        		derechando=0;
        		izquierdando=0;
        		quieto=0;
        		if(rbi<=9 && rbi!=0){
        			derechando=1;
        		}
        		if(rbd<=9 && rbd!=0){
        			izquierdando=1;
        		}
        	}
    	}else{
    		
    		if((rbi!=0 || rbd!=0)){
    			if(estaBloqueado(0)==true){
    				saltando=6;
    			}
        		arriba=true;
        		derechando=0;
        		izquierdando=0;
        		quieto=0;
        		if(rbi<=9 && rbi!=0){
        			derechando=1;
        		}
        		if(rbd<=9 && rbd!=0){
        			izquierdando=1;
        		}
    		}
    	}
    	if(contador==0){
			if(saltando>0){
	    		saltando--;
	    	}
	    	if(izquierdando>0){
				if(personaje.getX(0)<personaje.getX(1)){
    				disparo=true;
	    		}
	    		izquierdando--;
	    		izquierda=true;
	    	}
	    	if(derechando>0){
				if(personaje.getX(0)>personaje.getX(1)){
    				disparo=true;
	    		}
	    		derechando--;
	    		derecha=true;
	    	}
	    	if(quieto>0){
	    		quieto--;
	    	}
		}
		if(saltando>0){
			espacio=true;//pa que no caiga.
                        arriba=true;
		}
		if(estaBloqueado(0)==false){
			arriba=true;
		}
if(contador==0) accionAnterior=accionActual;
    	
    	//restablece el contador, el marca pasos, y la posicion por defecto si no hay movimiento.
    	if(izquierda==false && derecha==false && arriba==false && arriba==false && espacio==false && estaSaltando==false && agacharse==false 
    			&& izquierda2==false && derecha2==false && abajo2==false && arriba2==false && espacio2==false && agacharse2==false && estaBloqueado(0)==true){
    		if(contador==2 || contador==0){
    			contador=-1;
    			marcaPasos=0;
    			estaCayendo=false;
    			accionActual=0;
    			personaje.animar(1,0);
    			if(bloqueado()==true){
    				accionActual=accionAnterior;
    			}
    		}
    	}
    	
    	//si esta saltando pero no moviendose, la posicion por defecto tambien es 0.
    	if(izquierda==false && derecha==false && contador==0){
    		accionActual=0;
    		personaje.animar(1,0);
    		if(bloqueado()==true){
				accionActual=accionAnterior;
			}
    	}
    	
    	if ((izquierda==true || izquierda2==true) && contador==0){
    		auxX=personaje.getX(1)-1;
    		if(marcaPasos==0 || marcaPasos==2){
    			accionActual=3;
    		}else{
    			accionActual=0;
    		}
    	 	if(estaSaltando || estaCayendo){
    	 		accionActual=4;
    		}
    	}
    	 
    	if ((derecha==true || derecha2==true) && contador==0){
    		auxX=personaje.getX(1)+1;
    		if(marcaPasos==0 || marcaPasos==2){
    			accionActual=1;
    		}else{
    			accionActual=0;
    		}
    		if(estaSaltando || estaCayendo){
    			accionActual=2;
    		}
    	}
    	
    	//se pone de lado.
    	if(((izquierda && derecha) || (arriba==true || abajo2==true || arriba==true || arriba2==true)) && contador==0){
    		if(izquierda && derecha){
        		auxX=personaje.getX(1);
    		}
    		accionActual=5;
    	}
    	
    	//se agacha.
    	if((agacharse==true || agacharse2==true) && contador==0){
    		accionActual=6;
    	}
    	
    	//si esta bloqueado o empalado restaura el eje x.
    	if(contador==0){
			personaje.animar(1,accionActual);
    		if(bloqueado()==true || MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)+1]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)+1]==1){
    			auxX=personaje.getX(1);
    			//para que no se pege a las paredes (2 y 4 en caso de que no caiga si no se mueve).
        		if(bloqueado()==true || accionActual==4 || accionActual==2){ 
        			personaje.animar(1,accionAnterior);
        			accionActual=accionAnterior;
        		}
    		}else{
    			if((derecha==true || derecha2==true) && izquierda==false && izquierda2==false && accionActual!=5){//si no puede mover el pie hacia la derecha.
    				if((MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)+2]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)+2]==1) || (MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)+2]==1 || MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)+2]==1)){
    					auxX=personaje.getX(1);
    					if(bloqueado()==true || accionActual==4 || accionActual==2){ 
    	        			personaje.animar(1,accionAnterior);
    	        			accionActual=accionAnterior;
    	        		}
    				}
    			}
    			if((izquierda==true || izquierda2==true) && derecha==false && derecha2==false && accionActual!=5){//si no puede mover el pie hacia la izquierda.
    				if((MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)]==1 || MapaTiles.jugadores[personaje.getY(1)+3][personaje.getX(1)]==1) || (MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)]==1 || MapaTiles.mapaFisico[personaje.getY(1)+3][personaje.getX(1)]==1)){
    					auxX=personaje.getX(1);
    					if(bloqueado()==true || accionActual==4 || accionActual==2){ 
    	        			personaje.animar(1,accionAnterior);
    	        			accionActual=accionAnterior;
    	        		}
    				}
    			}
    		}
    	}
    	
    	//salto.
    	if ((espacio==true || espacio2==true) && contador==0){
    		if((estaBloqueado(0) || (estaCayendo==false)) && estaSaltando==false){
    			altoDelSalto=0;
    			sumaY=1;
    		}
    		if(estaBloqueado(3)){
    			if((izquierda==false && derecha==false) || (estaBloqueado(1) || estaBloqueado(2))){
    				altoDelSalto=5;
    			}else{
    				sumaY=0;
    			}
    		}
    		if(altoDelSalto<5){
    			auxY=personaje.getY(1)-sumaY;
    			altoDelSalto=altoDelSalto+1;
    			if(estaBloqueado(0)==false){
    				estaSaltando=true;
    			}else{
    				estaSaltando=false;
    			}
    		}else{
    			estaSaltando=false;
    			//espacio para agregar estaCayendo=true; si se necesita.
    		}
    		
    	}else{
    		if(contador==0){
    			estaSaltando=false;
    			altoDelSalto=5;
    		}
    	}
    	
    	//caida.
    	if(estaBloqueado(0)==false && estaSaltando==false && contador==0){
    		//agregar a estaCayendo==true: || hayAlgoDePiso()==false para que no se demore en caer cuando salta, tambien agregar al salto: estaCayendo=true; adonde se indica.
    		if(estaAgarrado()==false){
    			if((estaCayendo==true)){
    			auxY=personaje.getY(1)+1;
    			}
    			estaCayendo=true;
    		}else{
    			estaCayendo=false;
    		}
    		
    	}else{
    		if(contador==0){
    			estaCayendo=false;
    		}
    	}
    	
    	if(contador==0){
    		personaje.animar(1,accionActual);
    		//si esta bloqueado restaura el eje y.
    		if(bloqueado()==true){
    			auxY=personaje.getY(1);
    		}
    		//se revisa de nuevo por si la posicion actual esta bloqueada para cambiarla por la anterior.
    		if(bloqueado()==true){
        		accionActual=accionAnterior;
        		personaje.animar(1,accionActual);
        	}
    		personaje.setX(1,auxX);
        	personaje.setY(1,auxY);
    	}
    	
    	if(izquierda==true && derecha==false){
    		direccion=1;
    	}
    	
    	if(derecha==true  && izquierda==false){
    		direccion=2;
    	}
    	
    	if(contador2==0 || contador2==8){
    		contador2=-1;
    	}
    	
    	contador2++;
    	
    	if(contador2==0 && disparo==true && accionActual!=5){
    		balas.nuevaBala(1, direccion,personaje.getX(1),personaje.getY(1));
    		contador2=1;
    	}
    	
    	contador++;
    	
    	if(contador==1){
    		contador=0;
    		marcaPasos++;
    	}
    	
    	if(quemandose()==true){
    		contadorQuema++;
    		if(contadorQuema==1){
    			contadorQuema=0;
    			personaje.vidas[1]--;
    		}
    	}else{
    		contadorQuema=0;
    	}
    	
    	if(contador==0){
    		personaje.animar(1,accionActual);
    		personaje.setAccion(1,accionActual);
    	}
    	
    	if(contador==0){
    		espacio2=false;
    		izquierda2=false;
    		derecha2=false;
    		arriba2=false;
    		abajo2=false;
    		agacharse2=false;
    	}
    	
    	
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
    	//fin de la reaccion:
    	espacio=false;
    	abajo=false;
    	izquierda=false;
    	derecha=false;
    	disparo=false;
    	arriba=false;
    }
    
    //nuevos metodos:
    
    public int revisarBalasIzq(){
    	for(int i=0;i<4;i++){
    		if(Balas.bala[0][0][2]==(personaje.getY(1)+i)){
    			if(Balas.bala[0][0][0]==2){
    				if(personaje.getX(1)+1>Balas.bala[0][0][1]){
    					if((personaje.getX(1)+1-Balas.bala[0][0][1])<=9){
    						return (personaje.getX(1)+1-Balas.bala[0][0][1]);
    					}
    				}
    			}
    		}
    		if(Balas.bala[1][0][2]==(personaje.getY(1)+i)){
    			if(Balas.bala[1][0][0]==2){
    				if(personaje.getX(1)+1>Balas.bala[1][0][1]){
    					if((personaje.getX(1)+1-Balas.bala[1][0][1])<=9){
    						return (personaje.getX(1)+1-Balas.bala[1][0][1]);
    					}
    				}
    			}
    		}
    	}
    	return 0;
    }
    
    public int revisarBalasDer(){
    	for(int i=0;i<4;i++){
    		if(Balas.bala[0][0][2]==(personaje.getY(1)+i)){
    			if(Balas.bala[0][0][0]==1){
    				if(personaje.getX(1)+1<Balas.bala[0][0][1]){
    					if((Balas.bala[0][0][1]-personaje.getX(1)+1)<=9){
    						return (Balas.bala[0][0][1]-personaje.getX(1)+1);
    					}
    				}
    			}
    		}
    		if(Balas.bala[1][0][2]==(personaje.getY(1)+i)){
    			if(Balas.bala[1][0][0]==1){
    				if(personaje.getX(1)+1<Balas.bala[1][0][1]){
    					if((Balas.bala[1][0][1]-personaje.getX(1)+1)<=9){
    						return (Balas.bala[1][0][1]-personaje.getX(1)+1);
    					}
    				}
    			}
    		}
    	}
    	return 0;
    }
    
//nuevos metodos.
    /*
    public void cargarNivel(){
    	nivel=nivel+1;
    	j.actualizarNivel(nivel);
    	j.actualizarCorriendo(false);
    }
    */
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
                        personaje.vidas[0]=act.limiteVida;
                        personaje.vidas[1]=act.limiteVida;
                        personaje.vidas[2]=act.limiteVida;
                        personaje.vidas[3]=act.limiteVida;
                        personaje.estaVivo[0]=true;
                        personaje.estaVivo[1]=true;
                        personaje.estaVivo[2]=true;
                        personaje.estaVivo[3]=true;
                        personaje.puntos[0]=3;
                        personaje.puntos[1]=3;
                        personaje.puntos[2]=3;
                        personaje.puntos[3]=3;
                        Balas.bala[0][0][0]=0;
                        Balas.bala[1][0][0]=0;
                        Balas.bala[2][0][0]=0;
                        Balas.bala[3][0][0]=0;
                        //solo este jugador:
                        balas.borrarBalas();
                        
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
    		personaje.puntos[0]++;
    		if(personaje.puntos[0]==10) {
    			reaparecer();
    			Juego.corriendo=false;
    			personaje.puntos[0]=0;
	    		personaje.puntos[1]=0;
	    		personaje.puntos[2]=0;
	    		personaje.puntos[3]=0;
	    		personaje.vidas[0]=act.limiteVida;
	    		Balas.bala[0][0][0]=0;
	    		Balas.bala[1][0][0]=0;
                        Balas.bala[2][0][0]=0;
	    		Balas.bala[3][0][0]=0;
	    		direccion=2;
    		}
    		return true;
            }
            */
            if(jugadoresRestantes()<=1){
                reaparecer();
                Juego.corriendo=false;
                personaje.vidas[0]=act.limiteVida;
                personaje.vidas[1]=act.limiteVida;
                personaje.vidas[2]=act.limiteVida;
                personaje.vidas[3]=act.limiteVida;
                personaje.puntos[0]=3;
                personaje.puntos[1]=3;
                personaje.puntos[2]=3;
                personaje.puntos[3]=3;
                personaje.vidas[0]=act.limiteVida;
                Balas.bala[0][0][0]=0;
                Balas.bala[1][0][0]=0;
                Balas.bala[2][0][0]=0;
                Balas.bala[3][0][0]=0;
                direccion=1;
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
            if(personaje.vidas[0]<=0){
    		direccion=2;
    		personaje.vidas[0]=act.limiteVida;
    		return true;
            }
        }else{
            if(personaje.vidas[0]<=0){
    		if(Juego.computadora==true){
    			Bot.gano=true;
    		}else{
        		Jugador2.gano=true;
    		}
    		direccion=2;
    		personaje.vidas[0]=act.limiteVida;
    		return true;
            }
        }
    	
    	return false;
    	*/
        if(Teclas.modoJuego==1){
            if(personaje.vidas[1]<=0){
                personaje.puntos[1]--;
    		direccion=1;
    		personaje.vidas[1]=act.limiteVida;
    		return true;
            }
        }else{
            if(personaje.vidas[1]<=0){
    		personaje.puntos[1]--;
    		direccion=1;
    		personaje.vidas[1]=act.limiteVida;
    		return true;
            }
        }
    	
    	return false;
    }
    
    public boolean quemandose(){
    	if(auxY+3<alto && auxY>=0 && auxX+2<ancho && auxX>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[1][i][j]==1)
    					if(MapaTiles.mapaFisico[auxY+i][auxX+j]==2){
    						return true;
    					}
    	}
    	return false;
    }
    
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
    public boolean encerrado(){
    	if((MapaTiles.mapaFisico[auxY][auxX]==1 || MapaTiles.mapaFisico[auxY+1][auxX]==1 || MapaTiles.mapaFisico[auxY+2][auxX]==1
    		|| MapaTiles.mapaFisico[auxY+3][auxX]==1) && (MapaTiles.mapaFisico[auxY+1][auxX+2]==1
    		|| MapaTiles.mapaFisico[auxY+2][auxX+2]==1 || MapaTiles.mapaFisico[auxY+3][auxX+2]==1 || MapaTiles.mapaFisico[auxY][auxX+2]==1))
    		return true;
    	return false;
    }
    public void reaparecer(){
    	this.reap=false;
        personaje.setX(1,personaje.posIniX[1]);
        personaje.setY(1,personaje.posIniY[1]);
    	auxX=personaje.getX(1);
    	auxY=personaje.getY(1);
    }
    public boolean hayPiso(){
    	if(auxY+4<alto && auxY>=0 && auxX+2<ancho && auxX>=0){
    		if((MapaTiles.mapaFisico[auxY+4][auxX]==1 || MapaTiles.jugadores[auxY+4][auxX]==1) && (MapaTiles.mapaFisico[auxY+4][auxX+1]==1 || MapaTiles.jugadores[auxY+4][auxX+1]==1) && (MapaTiles.mapaFisico[auxY+4][auxX+2]==1 || MapaTiles.jugadores[auxY+4][auxX+2]==1)){
    			return true;
    		}
    	}
    	return false;
    }
    public boolean hayAlgoDePiso(){
    	if(auxY+4<alto && auxY>=0 && auxX+2<ancho && auxX>=0){
    		if(MapaTiles.mapaFisico[auxY+4][auxX]==1 || MapaTiles.mapaFisico[auxY+4][auxX+1]==1 || MapaTiles.mapaFisico[auxY+4][auxX+2]==1){
    			return true;
    		}
    		if(MapaTiles.jugadores[auxY+4][auxX]==1 || MapaTiles.jugadores[auxY+4][auxX+1]==1 || MapaTiles.jugadores[auxY+4][auxX+2]==1){
    			return true;
    		}
    	}
    	return false;
    }
    public boolean estaBloqueado(int direccion){
    	int sumaX=0,sumaY=0;
    	switch(direccion){
    	case 0:
    		sumaY=1;
    		break;
    	case 1:
    		sumaX=1;
    		break;
    	case 2:
    		sumaX=-1;
    		break;
    	case 3:
    		sumaY=-1;
    		break;
    	case 4:
    		break;
    	}
    	if(auxY+sumaY+3<alto && auxY+sumaY>=0 && auxX+sumaX+2<ancho && auxX+sumaX>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(personaje.personaje[1][i][j]==1)
    					if(MapaTiles.mapaFisico[auxY+i+sumaY][auxX+j+sumaX]==1 || MapaTiles.jugadores[auxY+i+sumaY][auxX+j+sumaX]==1)
    						return true;
    	}else{
    		return true;
    	}
    	
    	return false;
    }

}
