package BlockMan;

public class Balas {
	
        public static int cantidadBalas=20;//total balas por jugador.
        public static int cantidadBalasEnemigas=10;//total de balas de los enemigos.
        Personaje personaje=new Personaje();
        Enemigo enemigo=new Enemigo();
	static int[] numeroBala=new int[4];
	int contador=0;
        static int balaEnemiga[][]=new int[cantidadBalasEnemigas][3];//(numero de balas),(0=direccion,1=x,2=y).
        public static int duracionBala=200;
        static int bala[][][]=new int[4][cantidadBalas][7];
        
        
        public static int cantidadPelotas=50;//50 es el limite de pelotas que pueden existir por jugador en el mapa.
        static int[] numeroPelota=new int[4];
        public static int duracionPelota=400;
        static int pelota[][][]=new int[4][cantidadPelotas][7];//solo rebotan en los personajes cuando estan en ellos...
        
        static int[] numeroBloque=new int[4];
        public static int cantidadBloques=100;//si se unen son una
        static int bloque[][][]=new int[4][cantidadBloques][5];//dispara un bloque que se une a otros de su tipo,para adquirir fuerza.
        static int[] moverJugadorIzq=new int[4];
        static int[] moverJugadorDer=new int[4];
	//4=jugadores
	
	//10=cantidad de balas
	
	//7=caracteristicas:
	//0=direccion eje x(0=no existe, 1=izq, 2=der)
	//1=x
	//2=y
	//3=distancia, largo y fuerza en caso de los bloques(si estan unidos>1).
        //4=fuerza(solo balas especiales(1,2,3)). en caso de bloques es si fue comprobado ya o no en cada iteracion.(ya no es necesario)
        //5=direccion eje y(0=no existe, 1=arr, 2=aba)(solo balas especiales), para los bloques es si es el estado de "fue lanzado recien o no"(0 o 1).
        //6=daño(1=hace daño, 2=no hace daño (para cuando el jugador lanza bala,esta no haga daño ni se absorba hasta q deje de tocarlo(solo se aplica al jugador q la lanzo...)))
        public void borrarBalas(){
            for(int i=0;i<Teclas.jugadores;i++){
                for(int j=0;j<cantidadBalas;j++){
                    bala[i][j][0]=0;
                    bala[i][j][1]=0;
                    bala[i][j][2]=0;
                }
                for(int j=0;j<cantidadPelotas;j++){
                    pelota[i][j][0]=0;
                    pelota[i][j][5]=0;
                    pelota[i][j][4]=1;
                    pelota[i][j][1]=0;
                    pelota[i][j][2]=0;
                }
                for(int j=0;j<cantidadBloques;j++){
                    bloque[i][j][0]=0;
                    bloque[i][j][1]=0;
                    bloque[i][j][2]=0;
                    bloque[i][j][3]=0;
                    bloque[i][j][4]=0;
                }
            }
            for(int i=0;i<cantidadBalasEnemigas;i++){
                balaEnemiga[i][0]=0;
                balaEnemiga[i][1]=0;
                balaEnemiga[i][2]=0;
            }
        }
        public void nuevaBalaEnemiga(int enemigo){
            for(int i=0;i<cantidadBalasEnemigas;i++){
                if(balaEnemiga[i][0]==0 && this.enemigo.estaVivo[enemigo]==true){
                    if(this.enemigo.direccionEnemigo[enemigo]==1){
                        balaEnemiga[i][0]=1;
                        balaEnemiga[i][1]=this.enemigo.enemigoX[enemigo];
                        balaEnemiga[i][2]=this.enemigo.enemigoY[enemigo]+1;
                    }
                    if(this.enemigo.direccionEnemigo[enemigo]==2){
                        balaEnemiga[i][0]=2;
                        balaEnemiga[i][1]=this.enemigo.enemigoX[enemigo]+2;
                        balaEnemiga[i][2]=this.enemigo.enemigoY[enemigo]+1;
                    }
                    i=cantidadBalasEnemigas;
                }
            }
        }
	public void nuevaBala(int jugador, int direccion, int x, int y){
                for(int i=0;i<cantidadBalas;i++){
                    if(bala[jugador][i][0]==0){//si ya no esta se crea la nueva bala
                        numeroBala[jugador]=i;
                        i=cantidadBalas;
                    }
                }
		if(bala[jugador][numeroBala[jugador]][0]==0 && personaje.estaVivo[jugador]==true){
			bala[jugador][numeroBala[jugador]][0]=direccion;
			if(direccion==1){
				bala[jugador][numeroBala[jugador]][1]=x+1;
				if(personaje.getAccion(jugador)==6){
					bala[jugador][numeroBala[jugador]][2]=y+2;
				}else{
                                    if(personaje.getAccion(jugador)==8){
					bala[jugador][numeroBala[jugador]][2]=y+2;
                                        bala[jugador][numeroBala[jugador]][1]=x;
                                    }else{
                                        bala[jugador][numeroBala[jugador]][2]=y+1;
                                    }
				}
			}
			if(direccion==2){
				bala[jugador][numeroBala[jugador]][1]=x+3;
				if(personaje.getAccion(jugador)==6){
                                    bala[jugador][numeroBala[jugador]][2]=y+2;
				}else{
                                    if(personaje.getAccion(jugador)==7){
					bala[jugador][numeroBala[jugador]][2]=y+2;
                                        bala[jugador][numeroBala[jugador]][1]=x+4;
                                    }else{
                                        bala[jugador][numeroBala[jugador]][2]=y+1;
                                    }
				}
			}
                        bala[jugador][numeroBala[jugador]][6]=0;//la bala no le hara daño al jugador (q la lanzo) hasta q rebote o este fuera de este minimo por 2 cuadros
			bala[jugador][numeroBala[jugador]][3]=0;
			numeroBala[jugador]++;
			if(numeroBala[jugador]==cantidadBalas){
				numeroBala[jugador]=0;
			}
		}
	}
        public void nuevoBloque(int jugador, int direccion, int x, int y){
            for(int i=0;i<cantidadBloques;i++){
                if(bloque[jugador][i][0]==0){//si ya no esta se crea el nuevo bloque
                    numeroBloque[jugador]=i;
                    i=cantidadBloques;
                }
            }
            bloque[jugador][numeroBloque[jugador]][3]=1;
            bloque[jugador][numeroBloque[jugador]][4]=1;//marcado como que esta debajo del jugador...
            if(bloque[jugador][numeroBloque[jugador]][0]==0 && personaje.estaVivo[jugador]==true){
                bloque[jugador][numeroBloque[jugador]][0]=direccion;
                if(direccion==1){
                    bloque[jugador][numeroBloque[jugador]][1]=x+1;
                    if(personaje.getAccion(jugador)==6){
                        bloque[jugador][numeroBloque[jugador]][2]=y+2;
                    }else{
                        if(personaje.getAccion(jugador)==8){
                            bloque[jugador][numeroBloque[jugador]][2]=y+2;
                            bloque[jugador][numeroBloque[jugador]][1]=x;
                        }else{
                            bloque[jugador][numeroBloque[jugador]][2]=y+1;
                        }
                    }
                }
                if(direccion==2){
                    bloque[jugador][numeroBloque[jugador]][1]=x+3;
                    if(personaje.getAccion(jugador)==6){
                        bloque[jugador][numeroBloque[jugador]][2]=y+2;
                    }else{
                        if(personaje.getAccion(jugador)==7){
                            bloque[jugador][numeroBloque[jugador]][2]=y+2;
                            bloque[jugador][numeroBloque[jugador]][1]=x+4;
                        }else{
                            bloque[jugador][numeroBloque[jugador]][2]=y+1;
                        }
                    }
                }

                numeroBloque[jugador]++;
                if(numeroBloque[jugador]==cantidadBloques){
                        numeroBloque[jugador]=0;
                }

            }
            
	}
        public int cortarBloqueNuevo(int jugador, int direccion, int x, int y,int largo){//divide bloques si esta bloqueado...
            
            if(largo>0){
                for(int i=0;i<cantidadBloques;i++){
                    if(bloque[jugador][i][0]==0){//si ya no esta se crea el nuevo bloque
                        numeroBloque[jugador]=i;
                        i=cantidadBloques;
                    }
                }
                bloque[jugador][numeroBloque[jugador]][4]=0;
                bloque[jugador][numeroBloque[jugador]][3]=largo;
                if(bloque[jugador][numeroBloque[jugador]][0]==0){
                    bloque[jugador][numeroBloque[jugador]][0]=direccion;
                    bloque[jugador][numeroBloque[jugador]][1]=x;
                    bloque[jugador][numeroBloque[jugador]][2]=y;
                    numeroBloque[jugador]++;
                    if(numeroBloque[jugador]==cantidadBloques){
                        numeroBloque[jugador]=0;
                    }
                    /*
                    if(direccion==1){
                        bloque[jugador][numeroBloque[jugador]][0]=2;
                        bloque[jugador][numeroBloque[jugador]][1]+=(bloque[jugador][numeroBloque[jugador]][3]-1);
                    }
                    if(direccion==2){
                        bloque[jugador][numeroBloque[jugador]][0]=1;
                        bloque[jugador][numeroBloque[jugador]][1]-=(bloque[jugador][numeroBloque[jugador]][3]-1);
                    }
                    */
                }
                return numeroBloque[jugador];
            }
            return -1;
	}
        
        public void reemplazarBloque(int jugador,int indice1,int indice2){//cambia el indice del bloque para que sea revisado si es creado en el for.
            int direccion=bloque[jugador][indice1][0];
            int x=bloque[jugador][indice1][1];
            int y=bloque[jugador][indice1][2];
            int largo=bloque[jugador][indice1][3];
            int estado=bloque[jugador][indice1][4];
            bloque[jugador][indice1][0]=bloque[jugador][indice2][0];
            bloque[jugador][indice1][1]=bloque[jugador][indice2][1];
            bloque[jugador][indice1][2]=bloque[jugador][indice2][2];
            bloque[jugador][indice1][3]=bloque[jugador][indice2][3];
            bloque[jugador][indice1][4]=bloque[jugador][indice2][4];
            bloque[jugador][indice2][0]=direccion;
            bloque[jugador][indice2][1]=x;
            bloque[jugador][indice2][2]=y;
            bloque[jugador][indice2][3]=largo;
            bloque[jugador][indice2][4]=estado;
        }
        
        public void nuevaPelota(int jugador, int direccion, int x, int y){
            if(Actualizaciones.municionPelotas[jugador]>0){
                for(int i=0;i<cantidadPelotas;i++){
                    if(bloque[jugador][i][0]==0){//si ya no esta se crea la nueva bloque
                        numeroPelota[jugador]=i;
                        i=cantidadPelotas;
                    }
                }
		if(bloque[jugador][numeroPelota[jugador]][0]==0 && personaje.estaVivo[jugador]==true){
			bloque[jugador][numeroPelota[jugador]][0]=direccion;
                        bloque[jugador][numeroPelota[jugador]][5]=2;//hacia abajo...
			if(direccion==1){
				bloque[jugador][numeroPelota[jugador]][1]=x+1;
				if(personaje.getAccion(jugador)==6){
					bloque[jugador][numeroPelota[jugador]][2]=y+2;
				}else{
                                    if(personaje.getAccion(jugador)==8){
					bloque[jugador][numeroPelota[jugador]][2]=y+2;
                                        bloque[jugador][numeroPelota[jugador]][1]=x;
                                    }else{
                                        bloque[jugador][numeroPelota[jugador]][2]=y+1;
                                    }
				}
			}
			if(direccion==2){
                                bloque[jugador][numeroPelota[jugador]][1]=x+3;
				if(personaje.getAccion(jugador)==6){
                                    bloque[jugador][numeroPelota[jugador]][2]=y+2;
				}else{
                                    if(personaje.getAccion(jugador)==7){
					bloque[jugador][numeroPelota[jugador]][2]=y+2;
                                        bloque[jugador][numeroPelota[jugador]][1]=x+4;
                                    }else{
                                        bloque[jugador][numeroPelota[jugador]][2]=y+1;
                                    }
				}
			}
                        bloque[jugador][numeroPelota[jugador]][4]=1;//fuerza=1.
			bloque[jugador][numeroPelota[jugador]][3]=0;
                        bloque[jugador][numeroPelota[jugador]][6]=0;//ver implementacion en balas para mas detalles.
                        Actualizaciones.municionPelotas[jugador]--;
                        if(Actualizaciones.municionPelotas[jugador]<=0){
                            Actualizaciones.armaActual[jugador]=1;
                        }
                        
			numeroPelota[jugador]++;
			if(numeroPelota[jugador]==cantidadPelotas){
				numeroPelota[jugador]=0;
			}
                        
		}
            }
	}
        public void actualizarBloques(){//todas se actualizan a velocidad del jugador caminando para que no hayan problemas.
            
            for(int k=0;k<Teclas.jugadores;k++){//jugadores
                moverJugadorIzq[k]=0;
                moverJugadorDer[k]=0;
            }
            for(int k=0;k<Teclas.jugadores;k++){//jugadores
                for(int i=0;i<cantidadBloques;i++){//bloques
                    int indiceNuevoBloque=-1;
                    //2 limites:
                    if(bloque[k][i][0]==1){
                        if(bloque[k][i][1]<=0){
                            bloque[k][i][0]=0;
                            bloque[k][i][1]=0;
                            bloque[k][i][2]=0;
                            bloque[k][i][3]=0;
                            bloque[k][i][4]=0;
                        }
                    }
                    if(bloque[k][i][0]==2){
                        if(bloque[k][i][1]>=Juego.ancho-1){
                            bloque[k][i][0]=0;
                            bloque[k][i][1]=0;
                            bloque[k][i][2]=0;
                            bloque[k][i][3]=0;
                            bloque[k][i][4]=0;
                        }
                    }
                    
                    //solo se mueven si no cambian de direccion.
                    //la bala guia es la primera de un conjunto de balas en linea (horizontal).
                    //si la bala guia cambia de direccion todas las de atras tambien.
                    //la fuerza de la bala guia se determina por la cantidad de balas que posea atras.
                    //si se detecta una colision entre balas (horizontal),
                    //estas pueden quedarse inmoviles, o cambiar si una tiene menor fuerza que la otra,
                    //en ambos casos se transformaran en un solo conjunto de balas que sera detectado
                    //en la siguiente comprobacion.
                    //las balas se mueven a la misma velocidad, pero su fuerza es acumulativa y puede
                    //llegar a mover personajes.
                    //una vez que se unen son inseparables.(si desaparece una bala es la del final del grupo)
                    //osea pueden intercambiar posiciones.
                    //al rebotar se cambia el bloque la cola se transforma en el bloque guia.

                    //posibles ideas (no se usan):
                    //la velocidad de las balas se determina por las uniones entre estas
                    //a mayor cantidad de balas unidas menor sera su velocidad y viceversa.
                    //una bala por separado tiene una velocidad inicial de 1 (un personaje caminando).


                    if(bloque[k][i][0]!=0 && bloque[k][i][3]>0) {//si existe y tiene fuerza...
                        
                        
                        int bloqueados=0;
                        for(int j=0;j<bloque[k][i][3];j++){//si esta bloqueado completamente se elimina...
                            if((bloque[k][i][0]==1 && (MapaTiles.mapaFisico[bloque[k][i][2]][bloque[k][i][1]+j]==1 || (MapaTiles.jugadores[bloque[k][i][2]][bloque[k][i][1]+j]==1 && MapaTiles.jugadoresDibujados[bloque[k][i][2]][bloque[k][i][1]+j]!=5))) || (bloque[k][i][0]==2 && (MapaTiles.mapaFisico[bloque[k][i][2]][bloque[k][i][1]-j]==1 || (MapaTiles.jugadores[bloque[k][i][2]][bloque[k][i][1]-j]==1 && MapaTiles.jugadoresDibujados[bloque[k][i][2]][bloque[k][i][1]-j]!=5)))){
                                bloqueados++;
                            }
                        }
                        if(bloque[k][i][3]==bloqueados){//si estan todos bloqueados...chao bloques!
                            if(bloque[k][i][4]==0){
                                //System.out.println("chao bloques!");
                                bloque[k][i][0]=0;
                                bloque[k][i][1]=0;
                                bloque[k][i][2]=0;
                                bloque[k][i][3]=0;
                                bloque[k][i][4]=0;
                            }
                        }else{
                            
                            bloque[k][i][4]=0;
                            if(bloqueados>0 && bloque[k][i][4]==0){
                                if(bloque[k][i][0]==1){
                                    for(int j=0;j<bloque[k][i][3];j++){//si esta bloqueado completamente se elimina...
                                        if(((MapaTiles.mapaFisico[bloque[k][i][2]][bloque[k][i][1]+j]==1 || (MapaTiles.jugadores[bloque[k][i][2]][bloque[k][i][1]+j]==1 && MapaTiles.jugadoresDibujados[bloque[k][i][2]][bloque[k][i][1]+j]!=5)))){
                                            //System.out.println("bloque bloqueado");
                                            indiceNuevoBloque=cortarBloqueNuevo(k,bloque[k][i][0],bloque[k][i][1],bloque[k][i][2],bloque[k][i][3]-(j));
                                            System.out.println("1:"+j);
                                            
                                            bloque[k][i][3]=(j);
                                            if(bloque[k][i][3]<=0){//se elimina
                                                bloque[k][i][0]=0;
                                                bloque[k][i][1]=0;
                                                bloque[k][i][2]=0;
                                                bloque[k][i][3]=0;
                                                bloque[k][i][4]=0;
                                            }
                                            j=0;
                                        }
                                    }
                                }else{
                                    if(bloque[k][i][0]==2){
                                        for(int j=0;j<bloque[k][i][3];j++){//si esta bloqueado completamente se elimina...
                                            if(((MapaTiles.mapaFisico[bloque[k][i][2]][bloque[k][i][1]-j]==1 || (MapaTiles.jugadores[bloque[k][i][2]][bloque[k][i][1]-j]==1 && MapaTiles.jugadoresDibujados[bloque[k][i][2]][bloque[k][i][1]-j]!=5)))){
                                                //System.out.println("bloque bloqueado 2");
                                                indiceNuevoBloque=cortarBloqueNuevo(k,bloque[k][i][0],bloque[k][i][1],bloque[k][i][2],bloque[k][i][3]-(j));
                                                System.out.println("2:"+j);
                                                
                                                bloque[k][i][3]=(j);
                                                if(bloque[k][i][3]<=0){//se elimina
                                                    bloque[k][i][0]=0;
                                                    bloque[k][i][1]=0;
                                                    bloque[k][i][2]=0;
                                                    bloque[k][i][3]=0;
                                                }
                                                j=0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(indiceNuevoBloque==-1){
                            //identificar si le pisan la cola:
                            int direccion=bloque[k][i][0];
                            int x=bloque[k][i][1];
                            int y=bloque[k][i][2];
                            int largo=lePisanLaCola(bloque[k][i][1],bloque[k][i][2],bloque[k][i][0]);//el algoritmo retorna la fuerza de los bloques que le pisan la cola a este(lo incluye).

                            bloque[k][i][0]=direccion;
                            bloque[k][i][1]=x;
                            bloque[k][i][2]=y;
                            bloque[k][i][3]=largo;
                            bloque[k][i][4]=0;
                            //(para unirlos y eliminar los que estan pisandole la cola).


                            //identificar si es guia:
                            /*
                            boolean esGuia=false;
                            for(int l=0;l<Teclas.jugadores;l++){
                                for(int m=0;m<cantidadBloques;m++){
                                    if(!(bloque[l][m][0]!=0 && bloque[l][m][3]==0 && bloque[l][m][2]==bloque[k][i][2] && ((bloque[k][i][0]==1 && bloque[l][m][1]==bloque[k][i][1]-1 && bloque[l][m][0]==1) || (bloque[k][i][0]==2 && bloque[l][m][1]==bloque[k][i][1]+1 && bloque[l][m][0]==2)))){// si no tiene un bloque delante con la misma direccion es guia...
                                        esGuia=true;
                                        //
                                        //falta identificar la fuerza (bloques atras del guia).
                                    }
                                }
                            }
                            */
                            //cambiar direccion:
                            int jugadorEmpuje=0;
                            if(bloque[k][i][0]==1 && (MapaTiles.mapaFisico[bloque[k][i][2]][bloque[k][i][1]-1]==1 || MapaTiles.jugadores[bloque[k][i][2]][bloque[k][i][1]-1]==1)){
                                if(MapaTiles.jugadores[bloque[k][i][2]][bloque[k][i][1]-1]==1){
                                    jugadorEmpuje=MapaTiles.jugadoresDibujados[bloque[k][i][2]][bloque[k][i][1]-1];
                                    if(jugadorEmpuje>=1 && jugadorEmpuje<=4){
                                        switch(jugadorEmpuje){
                                            case 1:
                                                Jugador1.empujandole=true;
                                                break;
                                            case 2:
                                                //Jugador2.empujandole=true;
                                                break;
                                            case 3:
                                                //Jugador3.empujandole=true;
                                                break;
                                            case 4:
                                                //Jugador4.empujandole=true;
                                                break;
                                        }
                                        if(jugadorBloqueado(jugadorEmpuje-1,personaje.getX(jugadorEmpuje-1),personaje.getY(jugadorEmpuje-1),personaje.getAccion(jugadorEmpuje-1),2,false)==false){
                                            //System.out.println("desbloqueado");
                                            //personaje.setX(jugadorEmpuje-1,personaje.getX(jugadorEmpuje-1)-1);
                                            moverJugadorIzq[jugadorEmpuje-1]=1;
                                            if(bloque[k][i][0]==1) bloque[k][i][1]-=1;
                                            if(bloque[k][i][0]==2) bloque[k][i][1]+=1;
                                        }else{
                                            //System.out.println("bloqueado");
                                            bloque[k][i][0]=2;
                                            bloque[k][i][1]+=(bloque[k][i][3]-1);
                                        }
                                    }else{
                                        bloque[k][i][0]=2;
                                        bloque[k][i][1]+=(bloque[k][i][3]-1);
                                    }
                                }else{
                                    bloque[k][i][0]=2;
                                    bloque[k][i][1]+=(bloque[k][i][3]-1);
                                }
                            }else{
                                if(bloque[k][i][0]==2 && (MapaTiles.mapaFisico[bloque[k][i][2]][bloque[k][i][1]+1]==1 || MapaTiles.jugadores[bloque[k][i][2]][bloque[k][i][1]+1]==1)){
                                    if(MapaTiles.jugadores[bloque[k][i][2]][bloque[k][i][1]+1]==1){
                                        jugadorEmpuje=MapaTiles.jugadoresDibujados[bloque[k][i][2]][bloque[k][i][1]+1];
                                        if(jugadorEmpuje>=1 && jugadorEmpuje<=4){
                                            switch(jugadorEmpuje){
                                                case 1:
                                                    Jugador1.empujandole=true;
                                                    break;
                                                case 2:
                                                    //Jugador2.empujandole=true;
                                                    break;
                                                case 3:
                                                    //Jugador3.empujandole=true;
                                                    break;
                                                case 4:
                                                    //Jugador4.empujandole=true;
                                                    break;
                                            }
                                            if(jugadorBloqueado(jugadorEmpuje-1,personaje.getX(jugadorEmpuje-1),personaje.getY(jugadorEmpuje-1),personaje.getAccion(jugadorEmpuje-1),1,false)==false){
                                                //personaje.setX(jugadorEmpuje-1,personaje.getX(jugadorEmpuje-1)+1);
                                                moverJugadorDer[jugadorEmpuje-1]=1;
                                                if(bloque[k][i][0]==1) bloque[k][i][1]-=1;
                                                if(bloque[k][i][0]==2) bloque[k][i][1]+=1;
                                            }else{
                                                bloque[k][i][0]=1;
                                                bloque[k][i][1]-=(bloque[k][i][3]-1);
                                            }
                                        }else{
                                            bloque[k][i][0]=1;
                                            bloque[k][i][1]-=(bloque[k][i][3]-1);
                                        }
                                    }else{
                                        bloque[k][i][0]=1;
                                        bloque[k][i][1]-=(bloque[k][i][3]-1);
                                    }
                                }else{
                                    if(bloque[k][i][0]==1) bloque[k][i][1]-=1;
                                    if(bloque[k][i][0]==2) bloque[k][i][1]+=1;
                                }
                            }
                        }
                    }
                    if(indiceNuevoBloque!=-1 && indiceNuevoBloque<i){
                        //System.out.println(indiceNuevoBloque+","+i);
                        reemplazarBloque(k,indiceNuevoBloque,i);
                        i--;
                    }
                }
            }
        }

        public void actualizarJugadoresBloques(){
            for(int k=0;k<Teclas.jugadores;k++){//jugadores
                if((moverJugadorIzq[k]==1 || moverJugadorDer[k]==1) && !(moverJugadorIzq[k]==1 && moverJugadorDer[k]==1)){
                    if(moverJugadorIzq[k]==1 && jugadorBloqueado(k,personaje.getX(k),personaje.getY(k),personaje.getAccion(k),2,true)==false){
                        personaje.setX(k,personaje.getX(k)-1);
                    }
                    if(moverJugadorDer[k]==1 && jugadorBloqueado(k,personaje.getX(k),personaje.getY(k),personaje.getAccion(k),1,true)==false){
                        personaje.setX(k,personaje.getX(k)+1);
                    }
                }
            }
        }
        
        public boolean jugadorBloqueado(int jugador,int x,int y, int accion,int direccion, boolean bloque){//bloque: true=se validan.
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
            for(int i=0;i<4;i++){
                for(int j=0;j<5;j++){
                    if(personaje.personaje[jugador][i][j]==1){
                        if(!(y+sumaY+i<Juego.alto && y+sumaY+i>=0 && x+sumaX+j<Juego.ancho && x+sumaX+j>=0)){ return true;}
                        if(bloque==true){
                            if(MapaTiles.mapaFisico[y+i+sumaY][x+j+sumaX]==1 || (MapaTiles.jugadores[y+i+sumaY][x+j+sumaX]==1 && MapaTiles.jugadoresDibujados[y+i+sumaY][x+j+sumaX]==5)){
                                //System.out.println("jugadores[y][x]="+MapaTiles.jugadores[y+i+sumaY][x+j+sumaX]);
                                //System.out.println("bloqueado"+MapaTiles.jugadoresDibujados[y+i+sumaY][x+j+sumaX]);
                                return true;
                            }
                        }else{
                            if(MapaTiles.mapaFisico[y+i+sumaY][x+j+sumaX]==1 || (MapaTiles.jugadores[y+i+sumaY][x+j+sumaX]==1 && MapaTiles.jugadoresDibujados[y+i+sumaY][x+j+sumaX]!=jugador+1)){
                                //System.out.println("jugadores[y][x]="+MapaTiles.jugadores[y+i+sumaY][x+j+sumaX]);
                                //System.out.println("bloqueado fisico"+MapaTiles.mapaFisico[y+i+sumaY][x+j+sumaX]);
                                return true;
                            }
                        }
                    }
                }
            }
            //System.out.println("desbloqueado");
            return false;
        }
        
        public int lePisanLaCola(int x,int y, int direccion){
            int largo=0;
            for(int n=0;n<=largo;n++){
                for(int l=0;l<Teclas.jugadores;l++){
                    for(int m=0;m<cantidadBloques;m++){
                        if(bloque[l][m][0]!=0 && bloque[l][m][3]>0 && bloque[l][m][2]==y && ((bloque[l][m][0]==1 && bloque[l][m][0]==direccion && bloque[l][m][1]==(x+n) || (bloque[l][m][0]==2 && bloque[l][m][0]==direccion && bloque[l][m][1]==(x-n))))){
                            //if(!hayBloqueEncerrado(bloque[l][m][1],bloque[l][m][2],direccion) || largo==0){
                            largo+=bloque[l][m][3];
                            //}
                            //se elimina el bloque:
                            bloque[l][m][0]=0;
                            bloque[l][m][1]=0;
                            bloque[l][m][2]=0;
                            bloque[l][m][3]=0;
                            bloque[l][m][4]=0;
                        }
                    }
                }
            }
            return largo;
        }
        
        public boolean hayBloqueEncerrado(int x,int y, int direccion){//recibe las pos x e y de la cola + la direccion.
            if(direccion==1){//si la cola tiene un bloque al lado que corresponde la direccion contraria a la que va el conjunto de balas, esta bloqueado.
                if(MapaTiles.mapaFisico[y][x+1]==1 || MapaTiles.jugadores[y][x+1]==1){
                    return true;
                }
            }else{
                if(MapaTiles.mapaFisico[y][x-1]==1 || MapaTiles.jugadores[y][x-1]==1){
                    return true;
                }
            }
            return false;
        }
        
        
        public void actualizarPelotas(){
		//if(contador==2) contador=0;
		for(int k=0;k<Teclas.jugadores;k++){//jugadores
			for(int i=0;i<cantidadPelotas;i++){//pelotas
                            boolean esPropia=false;
                                //cuatro limites:
				if(pelota[k][i][0]==1){
                                    if(pelota[k][i][1]<=0){
                                        pelota[k][i][0]=0;
                                        pelota[k][i][5]=0;
                                    }
                                }
                                if(pelota[k][i][0]==2){
                                    if(pelota[k][i][1]>=Juego.ancho-1){
                                        pelota[k][i][0]=0;
                                        pelota[k][i][5]=0;
                                    }
                                }
                                if(pelota[k][i][5]==1){
                                    if(pelota[k][i][2]<=0){
                                        pelota[k][i][0]=0;
                                        pelota[k][i][5]=0;
                                    }
                                }
                                if(pelota[k][i][5]==2){
                                    if(pelota[k][i][2]>=Juego.alto-1){
                                        pelota[k][i][0]=0;
                                        pelota[k][i][5]=0;
                                    }
                                }
                                
				if(pelota[k][i][0]!=0 && pelota[k][i][5]!=0) {// && contador==0
					//cambiar direccion:
                                        boolean rebota=false;
                                        if(MapaTiles.mapaFisico[pelota[k][i][2]][pelota[k][i][1]]==1){//se devuelve por donde vino si esta bloqueada...
                                            if(pelota[k][i][0]==1){
                                                pelota[k][i][0]=2;
                                            }else{
                                                pelota[k][i][0]=1;
                                            }
                                            if(pelota[k][i][5]==1){
                                                pelota[k][i][5]=2;
                                            }else{
                                                pelota[k][i][5]=1;
                                            }
                                            rebota=true;
                                        }else{//sino se comprueban los lados de la pelota para ver hacia donde rebotara:
                                            if(pelota[k][i][0]==1){
                                                if(MapaTiles.mapaFisico[pelota[k][i][2]][pelota[k][i][1]-1]==1){
                                                    pelota[k][i][0]=2;
                                                    rebota=true;
                                                }
                                                //pelota[k][i][1]=pelota[k][i][1]-1;
                                            }else{
                                                if(pelota[k][i][0]==2){
                                                    if(MapaTiles.mapaFisico[pelota[k][i][2]][pelota[k][i][1]+1]==1){
                                                        pelota[k][i][0]=1;
                                                        rebota=true;
                                                    }
                                                    //pelota[k][i][1]=pelota[k][i][1]+1;
                                                }
                                            }

                                            if(pelota[k][i][5]==1){
                                                if(MapaTiles.mapaFisico[pelota[k][i][2]-1][pelota[k][i][1]]==1){
                                                    pelota[k][i][5]=2;
                                                    rebota=true;
                                                }
                                            }else{
                                                if(pelota[k][i][5]==2){
                                                    if(MapaTiles.mapaFisico[pelota[k][i][2]+1][pelota[k][i][1]]==1){
                                                        pelota[k][i][5]=1;
                                                        rebota=true;
                                                    }
                                                }
                                            }
                                        }
					if(rebota==true){
                                                pelota[k][i][6]=1;//la pelota le hara daño a cualquiera, incluso el jugador q la lanzo.
						if(pelota[k][i][3]>=duracionPelota){
							pelota[k][i][0]=0;
						}else{
                                                    if(pelota[k][i][4]<3){
                                                        pelota[k][i][4]++;//+1 fuerza.
                                                    }
                                                    
						}
						
					}else{
                                                //comprobacion de pelotas en enemigos:
                                                int enemigoHerido=0;
                                                for(int j=0;j<enemigo.cantidadEnemigos;j++){
                                                    for(int ii=0;ii<3;ii++){
                                                        for(int jj=0;jj<4;jj++){
                                                            if(enemigo.enemigo[j][jj][ii]==1 && enemigo.estaVivo[j]==true){
                                                                if(pelota[k][i][1]==enemigo.getX(j)+ii && pelota[k][i][2]==enemigo.getY(j)+jj){
                                                                    enemigoHerido=j+1;
                                                                    jj=4;
                                                                    ii=3;
                                                                    j=enemigo.cantidadEnemigos;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                int daño=0;
                                                switch(pelota[k][i][4]){
                                                    case 0:
                                                        daño=2;
                                                        break;
                                                    case 1:
                                                        daño=2;
                                                        break;
                                                    case 2:
                                                        daño=4;
                                                        break;
                                                    case 3:
                                                        daño=6;
                                                        break;
                                                }
                                                if(enemigoHerido>0){//si hay un enemigo herido con esta pelota no hay otro ser herido con la misma.
                                                    
                                                    enemigo.vidaEnemigo[enemigoHerido-1]=enemigo.vidaEnemigo[enemigoHerido-1]-daño;
                                                    pelota[k][i][0]=0;
                                                    pelota[k][i][3]=0;
                                                }else{
                                                    //si la pelota es del propio jugador y es igual a 0 su distancia, no le hace daño.
                                                    //(primero se revisa si esta en el area del jugador para que no se salga de sus limites).
                                                    
                                                    if((pelota[k][i][2]-personaje.getY(k))>=0 && (pelota[k][i][2]-personaje.getY(k))<4 && (pelota[k][i][1]-personaje.getX(k))>=0 && (pelota[k][i][1]-personaje.getX(k))<3){
                                                            if(personaje.personaje[k][pelota[k][i][2]-personaje.getY(k)][pelota[k][i][1]-personaje.getX(k)]==1){
                                                                    esPropia=true;
                                                            }
                                                    }
                                                    if((pelota[k][i][3]>0)){
                                                        pelota[k][i][6]=1;//si la distancia recorrida en mayor a cero, queire decir q ya salio del jugador.
                                                    }
                                                    //comprobacion de las pelotas en los personajes:
                                                    if(MapaTiles.jugadores[pelota[k][i][2]][pelota[k][i][1]]==1 && (pelota[k][i][6]==1 || esPropia==false)){//(pelota[k][i][3]>0 || (pelota[k][i][3]==0 && esPropia==false))


                                                            int jugadorHerido=0;
                                                            for(int ii=0;ii<3;ii++){
                                                                for(int jj=0;jj<4;jj++){
                                                                    if(personaje.personaje[0][jj][ii]==1 && personaje.estaVivo[0]==true){
                                                                        if(pelota[k][i][1]==personaje.getX(0)+ii && pelota[k][i][2]==personaje.getY(0)+jj){
                                                                            jugadorHerido=1;
                                                                        }
                                                                    }
                                                                    if(personaje.personaje[1][jj][ii]==1 && personaje.estaVivo[1]==true){
                                                                        if(pelota[k][i][1]==personaje.getX(1)+ii && pelota[k][i][2]==personaje.getY(1)+jj){
                                                                            jugadorHerido=2;
                                                                        }
                                                                    }
                                                                    if(personaje.personaje[2][jj][ii]==1 && personaje.estaVivo[2]==true){
                                                                        if(pelota[k][i][1]==personaje.getX(2)+ii && pelota[k][i][2]==personaje.getY(2)+jj){
                                                                            jugadorHerido=3;
                                                                        }
                                                                    }
                                                                    if(personaje.personaje[3][jj][ii]==1 && personaje.estaVivo[3]==true){
                                                                        if(pelota[k][i][1]==personaje.getX(3)+ii && pelota[k][i][2]==personaje.getY(3)+jj){
                                                                            jugadorHerido=4;
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            if(jugadorHerido>0){
                                                                if(personaje.getAccion(jugadorHerido-1)==5){
                                                                        if(pelota[k][i][4]<3){
                                                                            pelota[k][i][4]++;//+1 fuerza.
                                                                        }
                                                                        //cambio de direccion:
                                                                        if(pelota[k][i][0]==1){
                                                                            pelota[k][i][0]=2;
                                                                        }else{
                                                                            pelota[k][i][0]=1;
                                                                        }
                                                                        if(pelota[k][i][5]==1){
                                                                            pelota[k][i][5]=2;
                                                                        }else{
                                                                            pelota[k][i][5]=1;
                                                                        }
                                                                        if(Juego.friendlyFire==true){
                                                                            personaje.vidas[jugadorHerido-1]=personaje.vidas[jugadorHerido-1]-(daño/2);
                                                                        }
                                                                }else{
                                                                    if(Juego.friendlyFire==true){
                                                                        personaje.vidas[jugadorHerido-1]=personaje.vidas[jugadorHerido-1]-(daño);
                                                                    }
                                                                    pelota[k][i][0]=0;
                                                                    pelota[k][i][5]=0;
                                                                }
                                                            }
                                                            pelota[k][i][3]=0;
                                                    }
                                                }
                                            
						
					}
                                        if(esPropia==false || pelota[k][i][6]==1){
                                            pelota[k][i][3]++;//la pelota no recorrera mas distancia si la bala no tiene daño...
                                            //eso es para que se pueda comprobar si la bala lleva mas de un cuadro recorrido, y entonces dañe al jugador o a cualquiera.
                                        }
					
				}
				if(pelota[k][i][0]==1){
                                    pelota[k][i][1]--;
                                }else{
                                    pelota[k][i][1]++;
                                }
                                if(pelota[k][i][5]==1){
                                    pelota[k][i][2]--;
                                }else{
                                    pelota[k][i][2]++;
                                }
			}
		}
		//contador++;
	}
        
	public void actualizarBalasEnemigas(){
            //if(contador==1){//es uno por que nunca es cero(la actualizacion de las otras balas ocupa el cero).
                for(int i=0;i<cantidadBalasEnemigas;i++){
                    if(balaEnemiga[i][0]==1){
                        if(balaEnemiga[i][1]<=0){
                            balaEnemiga[i][0]=0;
                        }
                    }
                    if(balaEnemiga[i][0]==2){
                        if(balaEnemiga[i][1]>=Juego.ancho-1){
                            balaEnemiga[i][0]=0;
                        }
                    }
                    if(balaEnemiga[i][0]==1){
                        balaEnemiga[i][1]--;
                    }
                    if(balaEnemiga[i][0]==2){
                        balaEnemiga[i][1]++;
                    }
                    if(MapaTiles.mapaFisico[balaEnemiga[i][2]][balaEnemiga[i][1]]==1){
                        balaEnemiga[i][0]=0;
                    }else{
                        //comprobacion de balas en enemigos:
                        int enemigoHerido=0;
                        for(int j=0;j<enemigo.cantidadEnemigos;j++){
                            for(int ii=0;ii<3;ii++){
                                for(int jj=0;jj<4;jj++){
                                    if(enemigo.enemigo[j][jj][ii]==1 && enemigo.estaVivo[j]==true){
                                        if(balaEnemiga[i][1]==enemigo.getX(j)+ii && balaEnemiga[i][2]==enemigo.getY(j)+jj){
                                            enemigoHerido=j+1;
                                            jj=4;
                                            ii=3;
                                            j=enemigo.cantidadEnemigos;
                                        }
                                    }
                                }
                            }
                        }
                        
                        if(enemigoHerido>0){//si hay un enemigo herido con esta bala no hay otro ser herido con la misma.
                            enemigo.vidaEnemigo[enemigoHerido-1]=enemigo.vidaEnemigo[enemigoHerido-1]-0;//no le hacen daño sus propias balas.
                            balaEnemiga[i][0]=0;
                        }else{
                            //si la bala es del enemigo y es igual a 0 su distancia, no le hace daño.
                            //(primero se revisa si esta en el area del enemigo para que no se salga de sus limites).
                            
                            int jugadorHerido=0;
                            if(balaEnemiga[i][0]!=0){
                                for(int ii=0;ii<3;ii++){
                                    for(int jj=0;jj<4;jj++){
                                        if(personaje.personaje[0][jj][ii]==1 && personaje.estaVivo[0]==true){
                                            if(balaEnemiga[i][1]==personaje.getX(0)+ii && balaEnemiga[i][2]==personaje.getY(0)+jj){
                                                jugadorHerido=1;
                                            }
                                        }
                                        if(personaje.personaje[1][jj][ii]==1 && personaje.estaVivo[1]==true){
                                            if(balaEnemiga[i][1]==personaje.getX(1)+ii && balaEnemiga[i][2]==personaje.getY(1)+jj){
                                                jugadorHerido=2;
                                            }
                                        }
                                        if(personaje.personaje[2][jj][ii]==1 && personaje.estaVivo[2]==true){
                                            if(balaEnemiga[i][1]==personaje.getX(2)+ii && balaEnemiga[i][2]==personaje.getY(2)+jj){
                                                jugadorHerido=3;
                                            }
                                        }
                                        if(personaje.personaje[3][jj][ii]==1 && personaje.estaVivo[3]==true){
                                            if(balaEnemiga[i][1]==personaje.getX(3)+ii && balaEnemiga[i][2]==personaje.getY(3)+jj){
                                                jugadorHerido=4;
                                            }
                                        }
                                    }
                                }

                                if(jugadorHerido>0){
                                    if(personaje.getAccion(jugadorHerido-1)==5){
                                            if(balaEnemiga[i][0]==1){
                                                    balaEnemiga[i][1]++;
                                                    balaEnemiga[i][0]=2;
                                            }else{
                                                balaEnemiga[i][1]--;
                                                balaEnemiga[i][0]=1;
                                            }
                                            personaje.vidas[jugadorHerido-1]--;
                                    }else{
                                            personaje.vidas[jugadorHerido-1]=personaje.vidas[jugadorHerido-1]-2;
                                            balaEnemiga[i][0]=0;
                                    }
                                }
                            }
                        }
                    }
                }
            //}
            
        }
        
	public void actualizar(){
		//if(contador==2) contador=0;
		for(int k=0;k<Teclas.jugadores;k++){//jugadores
			for(int i=0;i<cantidadBalas;i++){//balas
                                boolean esPropia=false;
				if(bala[k][i][0]==1){
                                    if(bala[k][i][1]<=0){
                                        bala[k][i][0]=0;
                                    }
                                }
                                if(bala[k][i][0]==2){
                                    if(bala[k][i][1]>=Juego.ancho-1){
                                        bala[k][i][0]=0;
                                    }
                                }
                                
				if(bala[k][i][0]!=0) {// && contador==0
					
					if(bala[k][i][0]==1){
						bala[k][i][1]=bala[k][i][1]-1;
					}
					if(bala[k][i][0]==2){
						bala[k][i][1]=bala[k][i][1]+1;
					}
					if(MapaTiles.mapaFisico[bala[k][i][2]][bala[k][i][1]]==1){//comprobacion del bote o desaparicion de la pelota:
						if(bala[k][i][3]>=duracionBala){
							bala[k][i][0]=0;
						}else{
							if(bala[k][i][0]==1){
								bala[k][i][1]++;
								bala[k][i][0]=2;
							}else{
								bala[k][i][1]--;
								bala[k][i][0]=1;
							}
                                                        bala[k][i][6]=1;//la pelota le hara daño a cualquiera, incluso el jugador q la lanzo.
						}
                                            //bala[k][i][0]=0;
						
					}else{
                                                //comprobacion de balas en enemigos:
                                                int enemigoHerido=0;
                                                for(int j=0;j<enemigo.cantidadEnemigos;j++){
                                                    for(int ii=0;ii<3;ii++){
                                                        for(int jj=0;jj<4;jj++){
                                                            if(enemigo.enemigo[j][jj][ii]==1 && enemigo.estaVivo[j]==true){
                                                                if(bala[k][i][1]==enemigo.getX(j)+ii && bala[k][i][2]==enemigo.getY(j)+jj){
                                                                    enemigoHerido=j+1;
                                                                    jj=4;
                                                                    ii=3;
                                                                    j=enemigo.cantidadEnemigos;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if(enemigoHerido>0){//si hay un enemigo herido con esta bala no hay otro ser herido con la misma.
                                                    enemigo.vidaEnemigo[enemigoHerido-1]=enemigo.vidaEnemigo[enemigoHerido-1]-1;
                                                    bala[k][i][0]=0;
                                                    bala[k][i][3]=0;
                                                }else{
                                                    //si la bala es del propio jugador y es igual a 0 su distancia, no le hace daño.
                                                    //(primero se revisa si esta en el area del jugador para que no se salga de sus limites).
                                                    
                                                    if((bala[k][i][2]-personaje.getY(k))>=0 && (bala[k][i][2]-personaje.getY(k))<4 && (bala[k][i][1]-personaje.getX(k))>=0 && (bala[k][i][1]-personaje.getX(k))<3){
                                                            if(personaje.personaje[k][bala[k][i][2]-personaje.getY(k)][bala[k][i][1]-personaje.getX(k)]==1){
                                                                    esPropia=true;
                                                            }
                                                    }
                                                    if((bala[k][i][3]>0)){
                                                        bala[k][i][6]=1;//si la distancia recorrida en mayor a cero, queire decir q ya salio del jugador.
                                                    }
                                                    //comprobacion de las balas en los personajes:
                                                    if(MapaTiles.jugadores[bala[k][i][2]][bala[k][i][1]]==1 && (bala[k][i][6]==1 || esPropia==false)){
                                                            
                                                            int jugadorHerido=0;
                                                            for(int ii=0;ii<3;ii++){
                                                                for(int jj=0;jj<4;jj++){
                                                                    if(personaje.personaje[0][jj][ii]==1 && personaje.estaVivo[0]==true){
                                                                        if(bala[k][i][1]==personaje.getX(0)+ii && bala[k][i][2]==personaje.getY(0)+jj){
                                                                            jugadorHerido=1;
                                                                        }
                                                                    }
                                                                    if(personaje.personaje[1][jj][ii]==1 && personaje.estaVivo[1]==true){
                                                                        if(bala[k][i][1]==personaje.getX(1)+ii && bala[k][i][2]==personaje.getY(1)+jj){
                                                                            jugadorHerido=2;
                                                                        }
                                                                    }
                                                                    if(personaje.personaje[2][jj][ii]==1 && personaje.estaVivo[2]==true){
                                                                        if(bala[k][i][1]==personaje.getX(2)+ii && bala[k][i][2]==personaje.getY(2)+jj){
                                                                            jugadorHerido=3;
                                                                        }
                                                                    }
                                                                    if(personaje.personaje[3][jj][ii]==1 && personaje.estaVivo[3]==true){
                                                                        if(bala[k][i][1]==personaje.getX(3)+ii && bala[k][i][2]==personaje.getY(3)+jj){
                                                                            jugadorHerido=4;
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            if(jugadorHerido>0){
                                                                if(personaje.getAccion(jugadorHerido-1)==5){//rebote de las pelotas en los jugadores:
                                                                    if(bala[k][i][0]==1){
                                                                            bala[k][i][1]++;
                                                                            bala[k][i][0]=2;
                                                                    }else{
                                                                            bala[k][i][1]--;
                                                                            bala[k][i][0]=1;
                                                                    }
                                                                    //bala[k][i][0]=0;
                                                                    if(Juego.friendlyFire==true){
                                                                        personaje.vidas[jugadorHerido-1]--;
                                                                    }
                                                                }else{
                                                                    if(Juego.friendlyFire==true){
                                                                        personaje.vidas[jugadorHerido-1]--;
                                                                    }
                                                                    bala[k][i][0]=0;
                                                                }
                                                            }
                                                            //bala[k][i][3]=0;
                                                    }
                                                }
                                            
						
					}
					if(esPropia==false || bala[k][i][6]==1){
                                            bala[k][i][3]++;//la pelota no recorrera mas distancia si la bala no tiene daño...
                                            //eso es para que se pueda comprobar si la bala lleva mas de un cuadro recorrido, y entonces dañe al jugador o a cualquiera.
                                        }
				}
			}
		}
		//contador++;
	}
	
}
