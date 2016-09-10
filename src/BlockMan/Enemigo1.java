
package BlockMan;
public class Enemigo1 {
    	
	public static boolean gano=false;
	
	private int nivel=1;
	Balas balas=new Balas();
	Personaje personaje=new Personaje();
        Enemigo enemigo=new Enemigo();
        Actualizaciones act=new Actualizaciones();
        
        private int alto=Juego.alto,ancho=Juego.ancho;
       
        
        
    
    public static int nEnemigo=0;//deberia ser segun el indice del enemigo.
    public static int contadorFrames=0;//uno para todos los enemigos.
    
    public void actualiza(int nEnemigo) {
    	
        if(this.contadorFrames==0){

            //metodos nuevos:
            this.nEnemigo=nEnemigo;
            //caminar:
            if(enemigo.quedoIgual[nEnemigo]==true){
                enemigo.quedoIgual[nEnemigo]=false;
                if(enemigo.direccionEnemigo[nEnemigo]==1){
                    enemigo.direccionEnemigo[nEnemigo]=2;
                    enemigo.izquierda[nEnemigo]=false;
                    enemigo.derecha[nEnemigo]=true;
                }else{
                    enemigo.direccionEnemigo[nEnemigo]=1;
                    enemigo.derecha[nEnemigo]=false;
                    enemigo.izquierda[nEnemigo]=true;
                }
            }else{
                if(enemigo.direccionEnemigo[nEnemigo]==1){
                    enemigo.izquierda[nEnemigo]=true;
                    enemigo.derecha[nEnemigo]=false;
                }else{
                    enemigo.derecha[nEnemigo]=true;
                    enemigo.izquierda[nEnemigo]=false;
                }
            }
            //disparar:
            if(hayAlguienEnfrente()){
                enemigo.disparo[nEnemigo]=true;
            }


            //actualizar de acuerdo a la accion:


    if(enemigo.contador[nEnemigo]==0) enemigo.accionAnterior[nEnemigo]=enemigo.accionActual[nEnemigo];

            //restablece el enemigo.contador[nEnemigo], el marca pasos, y la posicion por defecto si no hay movimiento.
            if(enemigo.izquierda[nEnemigo]==false && enemigo.derecha[nEnemigo]==false && estaBloqueado(0)==true){
                    if(enemigo.contador[nEnemigo]==1 || enemigo.contador[nEnemigo]==0){
                            enemigo.contador[nEnemigo]=-1;
                            enemigo.marcaPasos[nEnemigo]=0;
                            enemigo.estaCayendo[nEnemigo]=false;
                            enemigo.accionActual[nEnemigo]=0;
                            enemigo.animar(nEnemigo,0);
                            if(bloqueado()==true){
                                    enemigo.accionActual[nEnemigo]=enemigo.accionAnterior[nEnemigo];
                            }
                    }
            }

            //si esta saltando pero no moviendose, la posicion por defecto tambien es 0.
            if(enemigo.izquierda[nEnemigo]==false && enemigo.derecha[nEnemigo]==false && enemigo.contador[nEnemigo]==0){
                    enemigo.accionActual[nEnemigo]=0;
                    enemigo.animar(nEnemigo,0);
                    if(bloqueado()==true){
                                    enemigo.accionActual[nEnemigo]=enemigo.accionAnterior[nEnemigo];
                            }
            }

            if ((enemigo.izquierda[nEnemigo]==true) && enemigo.contador[nEnemigo]==0){
                    enemigo.auxX[nEnemigo]=enemigo.enemigoX[nEnemigo]-1;
                    if(enemigo.marcaPasos[nEnemigo]==0 || enemigo.marcaPasos[nEnemigo]==2){
                            enemigo.accionActual[nEnemigo]=3;
                    }else{
                            enemigo.accionActual[nEnemigo]=0;
                    }
                    if(enemigo.estaSaltando[nEnemigo] || enemigo.estaCayendo[nEnemigo]){
                            enemigo.accionActual[nEnemigo]=4;
                    }
            }

            if ((enemigo.derecha[nEnemigo]==true) && enemigo.contador[nEnemigo]==0){
                    enemigo.auxX[nEnemigo]=enemigo.enemigoX[nEnemigo]+1;
                    if(enemigo.marcaPasos[nEnemigo]==0 || enemigo.marcaPasos[nEnemigo]==2){
                            enemigo.accionActual[nEnemigo]=1;
                    }else{
                            enemigo.accionActual[nEnemigo]=0;
                    }
                    if(enemigo.estaSaltando[nEnemigo] || enemigo.estaCayendo[nEnemigo]){
                            enemigo.accionActual[nEnemigo]=2;
                    }
            }

            //si esta bloqueado o empalado restaura el eje x.
            if(enemigo.contador[nEnemigo]==0){
                            enemigo.animar(nEnemigo,enemigo.accionActual[nEnemigo]);
                    if(bloqueado()==true || MapaTiles.mapaFisico[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]+1]==1 || MapaTiles.jugadores[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]+1]==1){
                            enemigo.auxX[nEnemigo]=enemigo.enemigoX[nEnemigo];
                            enemigo.quedoIgual[nEnemigo]=true;
                            //para que no se pege a las paredes (2 y 4 en caso de que no caiga si no se mueve).
                            if(bloqueado()==true || enemigo.accionActual[nEnemigo]==4 || enemigo.accionActual[nEnemigo]==2){ 
                                    enemigo.animar(nEnemigo,enemigo.accionAnterior[nEnemigo]);
                                    enemigo.accionActual[nEnemigo]=enemigo.accionAnterior[nEnemigo];
                            }
                    }else{
                            if((enemigo.derecha[nEnemigo]==true) && enemigo.izquierda[nEnemigo]==false && enemigo.accionActual[nEnemigo]!=5){//si no puede mover el pie hacia la enemigo.derecha[nEnemigo].
                                    if((MapaTiles.jugadores[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]+2]==1 || MapaTiles.jugadores[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]+2]==1) || (MapaTiles.mapaFisico[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]+2]==1 || MapaTiles.mapaFisico[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]+2]==1)){
                                            enemigo.auxX[nEnemigo]=enemigo.enemigoX[nEnemigo];
                                            if(bloqueado()==true || enemigo.accionActual[nEnemigo]==4 || enemigo.accionActual[nEnemigo]==2){ 
                                            enemigo.animar(nEnemigo,enemigo.accionAnterior[nEnemigo]);
                                            enemigo.accionActual[nEnemigo]=enemigo.accionAnterior[nEnemigo];
                                    }
                                    }
                            }
                            if((enemigo.izquierda[nEnemigo]==true) && enemigo.derecha[nEnemigo]==false && enemigo.accionActual[nEnemigo]!=5){//si no puede mover el pie hacia la enemigo.izquierda[nEnemigo].
                                    if((MapaTiles.jugadores[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]]==1 || MapaTiles.jugadores[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]]==1) || (MapaTiles.mapaFisico[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]]==1 || MapaTiles.mapaFisico[enemigo.enemigoY[nEnemigo]+3][enemigo.enemigoX[nEnemigo]]==1)){
                                            enemigo.auxX[nEnemigo]=enemigo.enemigoX[nEnemigo];
                                            if(bloqueado()==true || enemigo.accionActual[nEnemigo]==4 || enemigo.accionActual[nEnemigo]==2){ 
                                            enemigo.animar(nEnemigo,enemigo.accionAnterior[nEnemigo]);
                                            enemigo.accionActual[nEnemigo]=enemigo.accionAnterior[nEnemigo];
                                    }
                                    }
                            }
                    }
            }


            //caida.
            if(estaBloqueado(0)==false && enemigo.estaSaltando[nEnemigo]==false && enemigo.contador[nEnemigo]==0){
                    //agregar a enemigo.estaCayendo[nEnemigo]==true: || hayAlgoDePiso()==false para que no se demore en caer cuando salta, tambien agregar al salto: enemigo.estaCayendo[nEnemigo]=true; adonde se indica.
                    if(estaAgarrado()==false){
                            if((enemigo.estaCayendo[nEnemigo]==true)){
                            enemigo.auxY[nEnemigo]=enemigo.enemigoY[nEnemigo]+1;
                            }
                            enemigo.estaCayendo[nEnemigo]=true;
                    }else{
                            enemigo.estaCayendo[nEnemigo]=false;
                    }

            }else{
                    if(enemigo.contador[nEnemigo]==0){
                            enemigo.estaCayendo[nEnemigo]=false;
                    }
            }

            if(enemigo.contador[nEnemigo]==0){
                    enemigo.animar(nEnemigo,enemigo.accionActual[nEnemigo]);
                    //si esta bloqueado restaura el eje y.
                    if(bloqueado()==true){
                            enemigo.auxY[nEnemigo]=enemigo.enemigoY[nEnemigo];
                    }
                    //se revisa de nuevo por si la posicion actual esta bloqueada para cambiarla por la anterior.
                    if(bloqueado()==true){
                            enemigo.accionActual[nEnemigo]=enemigo.accionAnterior[nEnemigo];
                            enemigo.animar(nEnemigo,enemigo.accionActual[nEnemigo]);
                    }
                    enemigo.enemigoX[nEnemigo]=enemigo.auxX[nEnemigo];
                    enemigo.enemigoY[nEnemigo]=enemigo.auxY[nEnemigo];
            }

            if(enemigo.izquierda[nEnemigo]==true && enemigo.derecha[nEnemigo]==false){
                    enemigo.direccionEnemigo[nEnemigo]=1;
            }

            if(enemigo.derecha[nEnemigo]==true  && enemigo.izquierda[nEnemigo]==false){
                    enemigo.direccionEnemigo[nEnemigo]=2;
            }

            if(enemigo.contador2[nEnemigo]==0 || enemigo.contador2[nEnemigo]==8){
                    enemigo.contador2[nEnemigo]=-1;
            }

            enemigo.contador2[nEnemigo]++;

            if((enemigo.contador2[nEnemigo]==0 && enemigo.disparo[nEnemigo]==true && enemigo.accionActual[nEnemigo]!=5)){//espera entre balas(debe ser mayor a 1 el contador para que no se dispare a si mismo(justo cuando se va a mover)).{
                    balas.nuevaBalaEnemiga(nEnemigo);
                    enemigo.contador2[nEnemigo]=1;
            }

            enemigo.contador[nEnemigo]++;

            if(enemigo.contador[nEnemigo]==1){
                    enemigo.contador[nEnemigo]=0;
                    enemigo.marcaPasos[nEnemigo]++;
            }

            if(quemandose()==true){
                    enemigo.contadorQuema[nEnemigo]++;
                    if(enemigo.contadorQuema[nEnemigo]==1){
                            enemigo.contadorQuema[nEnemigo]=0;
                            enemigo.vidaEnemigo[nEnemigo]--;
                    }
            }else{
                    enemigo.contadorQuema[nEnemigo]=0;
            }

            if(enemigo.contador[nEnemigo]==0){
                    enemigo.animar(nEnemigo,enemigo.accionActual[nEnemigo]);
                    enemigo.setAccion(1,enemigo.accionActual[nEnemigo]);
            }

            if(murio()){

            }

            if(enemigo.marcaPasos[nEnemigo]==2 || hayAlgoDePiso()==false){
                    enemigo.marcaPasos[nEnemigo]=0;
            }


            enemigo.izquierda[nEnemigo]=false;
            enemigo.derecha[nEnemigo]=false;
            enemigo.disparo[nEnemigo]=false;
        }
    }
    
    public boolean murio(){
        if(enemigo.vidaEnemigo[nEnemigo]<=0){
            enemigo.estaVivo[nEnemigo]=false;
            return true;
        }
        return false;
    }
    
    
    public boolean quemandose(){
    	if(enemigo.auxY[nEnemigo]+3<alto && enemigo.auxY[nEnemigo]>=0 && enemigo.auxX[nEnemigo]+2<ancho && enemigo.auxX[nEnemigo]>=0){
            
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(enemigo.enemigo[nEnemigo][i][j]==1){
    					if(MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+i][enemigo.auxX[nEnemigo]+j]==2){
    						return true;
    					}
    	}                       }
    	return false;
    }
    
    public void comprobacionX(){//este metodo debe ir en la comprobacion del eje x.
    	for(int i=0;i<4;i++){
    		for(int j=0;j<3;j++){
    			if(Enemigo.enemigo[1][i][j]==1 && MapaTiles.jugadores[enemigo.auxY[nEnemigo]+i][enemigo.auxX[nEnemigo]+j]==1){//si esta en contacto con el otro jugador.
    				enemigo.auxX[nEnemigo]=enemigo.enemigoX[nEnemigo];
    			}
    		}
    	}
    }
    public void comprobacionY(){//este metodo debe ir en la comprobacion del eje y.
    	for(int i=0;i<4;i++){
    		for(int j=0;j<3;j++){
    			if(Enemigo.enemigo[1][i][j]==1 && MapaTiles.jugadores[enemigo.auxY[nEnemigo]+i][enemigo.auxX[nEnemigo]+j]==1){//si esta en contacto con el otro jugador.
    				enemigo.auxY[nEnemigo]=enemigo.enemigoY[nEnemigo];
    			}
    		}
    	}
    }
    
    public boolean estaAgarrado(){
    	int accion=enemigo.accionActual[nEnemigo];
    	if(enemigo.derecha[nEnemigo]==true){
    		enemigo.animar(nEnemigo,1);
        	if(estaBloqueado(0)==true && bloqueado()==false){
        		enemigo.accionActual[nEnemigo]=1;
        		return true;
        	}
        	
    	}
    	if(enemigo.izquierda[nEnemigo]==true){
    		enemigo.animar(nEnemigo,3);
        	if(estaBloqueado(0)==true && bloqueado()==false){
        		enemigo.accionActual[nEnemigo]=3;
        		return true;
        	}
        	
    	}
    	
    	enemigo.accionActual[nEnemigo]=accion;
    	enemigo.animar(nEnemigo,enemigo.accionActual[nEnemigo]);
    	return false;
    }
    public boolean bloqueado(){
    	if(estaBloqueado(4)) return true;
    	
    	return false;
    }
    
    public boolean semiEncerrado(){
    	enemigo.animar(nEnemigo,0);
    	if(bloqueado()) return true;
    	return false;
    }
    public boolean encerrado(){
    	if((MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]][enemigo.auxX[nEnemigo]]==1 || MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+1][enemigo.auxX[nEnemigo]]==1 || MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+2][enemigo.auxX[nEnemigo]]==1
    		|| MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+3][enemigo.auxX[nEnemigo]]==1) && (MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+1][enemigo.auxX[nEnemigo]+2]==1
    		|| MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+2][enemigo.auxX[nEnemigo]+2]==1 || MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+3][enemigo.auxX[nEnemigo]+2]==1 || MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]][enemigo.auxX[nEnemigo]+2]==1))
    		return true;
    	return false;
    }
    public void reaparecer(int n){
        enemigo.direccionEnemigo[n]=2;
        enemigo.enemigoX[n]=enemigo.posIniX[n];
        enemigo.enemigoY[n]=enemigo.posIniY[n];
    	enemigo.auxX[n]=enemigo.enemigoX[n];
    	enemigo.auxY[n]=enemigo.enemigoY[n];
    }
    public boolean hayPiso(){
    	if(enemigo.auxY[nEnemigo]+4<alto && enemigo.auxY[nEnemigo]>=0 && enemigo.auxX[nEnemigo]+2<ancho && enemigo.auxX[nEnemigo]>=0){
    		if((MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]]==1 || MapaTiles.jugadores[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]]==1) && (MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]+1]==1 || MapaTiles.jugadores[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]+1]==1) && (MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]+2]==1 || MapaTiles.jugadores[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]+2]==1)){
    			return true;
    		}
    	}
    	return false;
    }
    public boolean hayAlgoDePiso(){
    	if(enemigo.auxY[nEnemigo]+4<alto && enemigo.auxY[nEnemigo]>=0 && enemigo.auxX[nEnemigo]+2<ancho && enemigo.auxX[nEnemigo]>=0){
    		if(MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]]==1 || MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]+1]==1 || MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]+2]==1){
    			return true;
    		}
    		if(MapaTiles.jugadores[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]]==1 || MapaTiles.jugadores[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]+1]==1 || MapaTiles.jugadores[enemigo.auxY[nEnemigo]+4][enemigo.auxX[nEnemigo]+2]==1){
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
    	if(enemigo.auxY[nEnemigo]+sumaY+3<alto && enemigo.auxY[nEnemigo]+sumaY>=0 && enemigo.auxX[nEnemigo]+sumaX+2<ancho && enemigo.auxX[nEnemigo]+sumaX>=0){
    		for(int i=0;i<4;i++)
    			for(int j=0;j<3;j++)
    				if(enemigo.enemigo[nEnemigo][i][j]==1){
    					if(MapaTiles.mapaFisico[enemigo.auxY[nEnemigo]+i+sumaY][enemigo.auxX[nEnemigo]+j+sumaX]==1 || MapaTiles.jugadores[enemigo.auxY[nEnemigo]+i+sumaY][enemigo.auxX[nEnemigo]+j+sumaX]==1){
    						return true;
                                        }
                                }
    	}else{
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean estaAlTope(){
        if(enemigo.direccionEnemigo[nEnemigo]==1){
            if(enemigo.enemigoX[nEnemigo]<=0){
                return true;
            }else{
                for(int i=0;i<4;i++){
                    if(MapaTiles.mapaFisico[enemigo.enemigoX[nEnemigo]-1][enemigo.enemigoY[nEnemigo]+i]==1){
                        return true;
                    }
                }
            }
        }else{
            if(enemigo.enemigoX[nEnemigo]>=Juego.ancho-4){
                return true;
            }else{
                for(int i=0;i<4;i++){
                    if(MapaTiles.mapaFisico[enemigo.enemigoX[nEnemigo]+3][enemigo.enemigoY[nEnemigo]+i]==1){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean estaAlBorde(){
        if(enemigo.direccionEnemigo[nEnemigo]==1){
            if(enemigo.enemigoX[nEnemigo]<=0){
                return true;
            }else{
                if(MapaTiles.mapaFisico[enemigo.enemigoX[nEnemigo]-1][enemigo.enemigoY[nEnemigo]+4]==0){
                    return true;
                }
            }
        }else{
            if(enemigo.enemigoX[nEnemigo]>=Juego.ancho-4){
                return true;
            }else{
                if(MapaTiles.mapaFisico[enemigo.enemigoX[nEnemigo]+3][enemigo.enemigoY[nEnemigo]+4]==0){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hayAlguienEnfrente(){
        if(enemigo.direccionEnemigo[nEnemigo]==1){
            for(int i=0;i<Teclas.jugadores;i++){
                if(enemigo.enemigoY[nEnemigo]>=personaje.getY(i)-3 && enemigo.enemigoX[nEnemigo]>personaje.getX(i)){
                    return true;
                }
            }
        }else{
            for(int i=0;i<Teclas.jugadores;i++){
                if(enemigo.enemigoY[nEnemigo]>=personaje.getY(i)-3 && enemigo.enemigoX[nEnemigo]<personaje.getX(i)){
                    return true;
                }
            }
        }
        return false;
    }
    
}
