package BlockMan;

import com.centralnexus.input.Joystick; 
import com.centralnexus.input.JoystickListener; 
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Objects;

public class JoystickControl implements JoystickListener 
{ 
//--------------------------------------------------------------- 
// MEMBERS 
//--------------------------------------------------------------- 

// Class Constants 
    
private boolean yaHayJoy1=false;
private boolean yaHayJoy2=false;
private boolean yaHayJoy3=false;
private boolean yaHayJoy4=false;

public final int
BUTTON1 = 0, 
BUTTON2 = 1, 
BUTTON3 = 2, 
BUTTON4 = 3,
UP = 4,
DOWN = 5,
LEFT = 6,
RIGHT = 7,
P_UP = 8,
P_DOWN = 9,
P_LEFT = 10,
P_RIGHT = 11,
P_UP_RIGHT = 12,//NO SE PUEDE ASIGNAR PARA MOVER AL PERSONAJE
P_DOWN_RIGHT = 13,//NO SE PUEDE ASIGNAR PARA MOVER AL PERSONAJE
P_DOWN_LEFT = 14,//NO SE PUEDE ASIGNAR PARA MOVER AL PERSONAJE
P_UP_LEFT = 15;//NO SE PUEDE ASIGNAR PARA MOVER AL PERSONAJE

// Variable members 
private Joystick[] pJoy=new Joystick[4];
private int nNumDevices; 

public int controles[][]=new int[4][8];

public boolean botones[][]=new boolean[4][16];//[jugadores][todos los botones].

//
//public boolean abAxis0[] = {false, false, false, false}; 
//public boolean abButtons0[] = {false, false, false, false}; 
//public boolean abPOV0[] = {false, false, false, false,false, false, false, false}; 
//
//public boolean abAxis1[] = {false, false, false, false}; 
//public boolean abButtons1[] = {false, false, false, false}; 
//public boolean abPOV1[] = {false, false, false, false,false, false, false, false}; 
//
//public boolean abAxis2[] = {false, false, false, false}; 
//public boolean abButtons2[] = {false, false, false, false}; 
//public boolean abPOV2[] = {false, false, false, false,false, false, false, false}; 
//
//public boolean abAxis3[] = {false, false, false, false}; 
//public boolean abButtons3[] = {false, false, false, false}; 
//public boolean abPOV3[] = {false, false, false, false,false, false, false, false}; 



Jugador1 j1=new Jugador1();
Jugador2 j2=new Jugador2();
Jugador3 j3=new Jugador3();
Jugador4 j4=new Jugador4();

//--------------------------------------------------------------- 
// CONSTRUCTOR 
//--------------------------------------------------------------- 
public JoystickControl() 
{ 
super();


for(int i=0;i<4;i++){
    for(int j=0;j<16;j++){
        botones[i][j]=false;//se ponen todos los botones en falso.
    }
}

this.pJoy[0] = null;
this.pJoy[1] = null; 
this.pJoy[2] = null; 
this.pJoy[3] = null; 
this.nNumDevices = Joystick.getNumDevices(); 
if (this.nNumDevices > 0) 
{ 
try 
{

if(Menu.ordenDispositivos[0]!=-1) pJoy[0] = Joystick.createInstance(Menu.ordenDispositivos[0]);
if(Menu.ordenDispositivos[1]!=-1) pJoy[1] = Joystick.createInstance(Menu.ordenDispositivos[1]);
if(Menu.ordenDispositivos[2]!=-1) pJoy[2] = Joystick.createInstance(Menu.ordenDispositivos[2]);
if(Menu.ordenDispositivos[3]!=-1) pJoy[3] = Joystick.createInstance(Menu.ordenDispositivos[3]);
    
//for (int idx = 0; idx < Joystick.getNumDevices(); idx++) {
//    if (Joystick.isPluggedIn(idx)) {
//        if(yaHayJoy1==false){
//            pJoy[0] = Joystick.createInstance(idx);
//            yaHayJoy1=true;
//        }else{
//            if(yaHayJoy2==false){
//                pJoy[1] = Joystick.createInstance(idx);
//                yaHayJoy2=true;
//            }else{
//                if(yaHayJoy3==false){
//                    pJoy[2] = Joystick.createInstance(idx);
//                    yaHayJoy3=true;
//                }else{
//                    if(yaHayJoy4==false){
//                        pJoy[3] = Joystick.createInstance(idx);
//                        yaHayJoy4=true;
//                    }
//                }
//            }
//        }
//        
//    }
//}


if(pJoy[0]!=null){
    this.pJoy[0].addJoystickListener(this);
}
if(pJoy[1]!=null){
    this.pJoy[1].addJoystickListener(this);
}
if(pJoy[2]!=null){
    this.pJoy[2].addJoystickListener(this);
}
if(pJoy[3]!=null){
    this.pJoy[3].addJoystickListener(this);
}

/*
if (pJoy[2] == null) {
    pJoy[3] = pJoy[2] = pJoy[1];
}else{
    if(pJoy[3]==null){
        pJoy[3]=pJoy[1];
    }else{
        this.pJoy[3].addJoystickListener(this);
    }
    this.pJoy[2].addJoystickListener(this);
}
*/

} 
catch (IOException e) 
{ 
e.printStackTrace(); 
this.pJoy[0] = null;
this.pJoy[1] = null; 
this.pJoy[2] = null; 
this.pJoy[3] = null; 
} 
} 
} 

//--------------------------------------------------------------- 
// Is Available 
//--------------------------------------------------------------- 
public boolean isAvailable (int index) 
{ 
return (this.pJoy[index] != null); 
} 

//--------------------------------------------------------------- 
// getAxis (UP, DOWN, LEFT, RIGHT) 
//--------------------------------------------------------------- 
public boolean getAxis (int index, int nDir) 
{ 
return isAvailable(index) && this.botones[index][nDir+4]; 
} 

//--------------------------------------------------------------- 
// getAxisAlt (UP, DOWN, LEFT, RIGHT) resets the direction 
//--------------------------------------------------------------- 
public boolean getAxisAlt (int index, int nDir) 
{ 
boolean bRet = isAvailable(index) && this.botones[index][nDir+4]; 
if (bRet) 
this.botones[index][nDir+4] = false; 
return bRet; 
} 


//--------------------------------------------------------------- 
// getButton (BUTTON1..4) 
//--------------------------------------------------------------- 
public boolean getButton (int index, int nButton) 
{ 
return isAvailable(index) && this.botones[index][nButton]; 
} 

//--------------------------------------------------------------- 
// getButtonAlt (BUTTON1..4) resets the button 
//--------------------------------------------------------------- 
public boolean getButtonAlt (int index,int nButton) 
{ 
boolean bRet = isAvailable(index) && this.botones[index][nButton]; 
if (bRet) 
this.botones[index][nButton] = false; 
return bRet; 
} 




//
////--------------------------------------------------------------- 
//// Is Available 
////--------------------------------------------------------------- 
//public boolean isAvailable1 () 
//{ 
//return (this.pJoy[0] != null); 
//} 
//
////--------------------------------------------------------------- 
//// getAxis (UP, DOWN, LEFT, RIGHT) 
////--------------------------------------------------------------- 
//public boolean getAxis1 (int nDir) 
//{ 
//return isAvailable1() && this.abAxis0[nDir]; 
//} 
//
////--------------------------------------------------------------- 
//// getAxisAlt (UP, DOWN, LEFT, RIGHT) resets the direction 
////--------------------------------------------------------------- 
//public boolean getAxisAlt1 (int nDir) 
//{ 
//boolean bRet = isAvailable1() && this.abAxis0[nDir]; 
//if (bRet) 
//this.abAxis0[nDir] = false; 
//return bRet; 
//} 
//
//
////--------------------------------------------------------------- 
//// getButton (BUTTON1..4) 
////--------------------------------------------------------------- 
//public boolean getButton1 (int nButton) 
//{ 
//return isAvailable1() && this.abButtons0[nButton]; 
//} 
//
////--------------------------------------------------------------- 
//// getButtonAlt (BUTTON1..4) resets the button 
////--------------------------------------------------------------- 
//public boolean getButtonAlt1 (int nButton) 
//{ 
//boolean bRet = isAvailable1() && this.abButtons0[nButton]; 
//if (bRet) 
//this.abButtons0[nButton] = false; 
//return bRet; 
//} 
//
//
//
////--------------------------------------------------------------- 
//// Is Available 
////--------------------------------------------------------------- 
//public boolean isAvailable2 () 
//{ 
//return (this.pJoy[1] != null); 
//} 
//
////--------------------------------------------------------------- 
//// getAxis (UP, DOWN, LEFT, RIGHT) 
////--------------------------------------------------------------- 
//public boolean getAxis2 (int nDir) 
//{ 
//return isAvailable2() && this.abAxis1[nDir]; 
//} 
//
////--------------------------------------------------------------- 
//// getAxisAlt (UP, DOWN, LEFT, RIGHT) resets the direction 
////--------------------------------------------------------------- 
//public boolean getAxisAlt2 (int nDir) 
//{ 
//boolean bRet = isAvailable2() && this.abAxis1[nDir]; 
//if (bRet) 
//this.abAxis1[nDir] = false; 
//return bRet; 
//} 
//
//
////--------------------------------------------------------------- 
//// getButton (BUTTON1..4) 
////--------------------------------------------------------------- 
//public boolean getButton2 (int nButton) 
//{ 
//return isAvailable2() && this.abButtons1[nButton]; 
//} 
//
////--------------------------------------------------------------- 
//// getButtonAlt (BUTTON1..4) resets the button 
////--------------------------------------------------------------- 
//public boolean getButtonAlt2 (int nButton) 
//{ 
//boolean bRet = isAvailable2() && this.abButtons1[nButton]; 
//if (bRet) 
//this.abButtons1[nButton] = false; 
//return bRet; 
//} 
//
//
//
//
//
////--------------------------------------------------------------- 
//// Is Available 
////--------------------------------------------------------------- 
//public boolean isAvailable3 () 
//{ 
//return (this.pJoy[2] != null); 
//} 
//
////--------------------------------------------------------------- 
//// getAxis (UP, DOWN, LEFT, RIGHT) 
////--------------------------------------------------------------- 
//public boolean getAxis3 (int nDir) 
//{ 
//return isAvailable3() && this.abAxis2[nDir]; 
//} 
//
////--------------------------------------------------------------- 
//// getAxisAlt (UP, DOWN, LEFT, RIGHT) resets the direction 
////--------------------------------------------------------------- 
//public boolean getAxisAlt3 (int nDir) 
//{ 
//boolean bRet = isAvailable3() && this.abAxis2[nDir]; 
//if (bRet) 
//this.abAxis2[nDir] = false; 
//return bRet; 
//} 
//
//
////--------------------------------------------------------------- 
//// getButton (BUTTON1..4) 
////--------------------------------------------------------------- 
//public boolean getButton3 (int nButton) 
//{ 
//return isAvailable3() && this.abButtons2[nButton]; 
//} 
//
////--------------------------------------------------------------- 
//// getButtonAlt (BUTTON1..4) resets the button 
////--------------------------------------------------------------- 
//public boolean getButtonAlt3 (int nButton) 
//{ 
//boolean bRet = isAvailable3() && this.abButtons2[nButton]; 
//if (bRet) 
//this.abButtons2[nButton] = false; 
//return bRet; 
//} 
//
//
//
//
//
//
////--------------------------------------------------------------- 
//// Is Available 
////--------------------------------------------------------------- 
//public boolean isAvailable4() 
//{ 
//return (this.pJoy[3] != null); 
//} 
//
////--------------------------------------------------------------- 
//// getAxis (UP, DOWN, LEFT, RIGHT) 
////--------------------------------------------------------------- 
//public boolean getAxis4 (int nDir) 
//{ 
//return isAvailable4() && this.abAxis3[nDir]; 
//} 
//
////--------------------------------------------------------------- 
//// getAxisAlt (UP, DOWN, LEFT, RIGHT) resets the direction 
////--------------------------------------------------------------- 
//public boolean getAxisAlt4 (int nDir) 
//{ 
//boolean bRet = isAvailable4() && this.abAxis3[nDir]; 
//if (bRet) 
//this.abAxis3[nDir] = false; 
//return bRet; 
//} 
//
//
////--------------------------------------------------------------- 
//// getButton (BUTTON1..4) 
////--------------------------------------------------------------- 
//public boolean getButton4 (int nButton) 
//{ 
//return isAvailable4() && this.abButtons3[nButton]; 
//} 
//
////--------------------------------------------------------------- 
//// getButtonAlt (BUTTON1..4) resets the button 
////--------------------------------------------------------------- 
//public boolean getButtonAlt4 (int nButton) 
//{ 
//boolean bRet = isAvailable4() && this.abButtons3[nButton]; 
//if (bRet) 
//this.abButtons3[nButton] = false; 
//return bRet; 
//} 

//--------------------------------------------------------------- 
// Event: joystickAxisChanged 
//--------------------------------------------------------------- 
public void joystickAxisChanged(Joystick pJoy) 
{ 
    int index=-1;
    if (this.pJoy[0] == pJoy){
        index=0;
    }
    if (this.pJoy[1] == pJoy){
        index=1;
    }
    if (this.pJoy[2] == pJoy){
        index=2;
    }
    if (this.pJoy[3] == pJoy){
        index=3;
    }
    if(index!=-1){// && this.pJoy[index].getPOV()!=-0.009999999776482582
        this.botones[index][UP] = this.pJoy[index].getY()==-1.0; 
        this.botones[index][DOWN] = this.pJoy[index].getY()==1.0; 
        this.botones[index][RIGHT] = this.pJoy[index].getX() == 1.0; 
        this.botones[index][LEFT] = this.pJoy[index].getX() == -1.0;
        this.botones[index][P_UP] = (this.pJoy[index].getPOV()== 0.0);//up
        this.botones[index][P_DOWN] = (this.pJoy[index].getPOV()== 180.0);//down
        this.botones[index][P_LEFT] = (this.pJoy[index].getPOV()== 270.0);//left
        this.botones[index][P_RIGHT] = (this.pJoy[index].getPOV()== 90.0);//right
        this.botones[index][P_UP_RIGHT] = (this.pJoy[index].getPOV()== 45.0);//up right
        this.botones[index][P_DOWN_RIGHT] = (this.pJoy[index].getPOV()== 135.0);//down right
        this.botones[index][P_DOWN_LEFT] = (this.pJoy[index].getPOV()== 225.0);//down left
        this.botones[index][P_UP_LEFT] = (this.pJoy[index].getPOV()== 315.0);//up left
        botonEjeCambiado(index);
    }
}

//--------------------------------------------------------------- 
// Event: joystickButtonChanged 
//--------------------------------------------------------------- 
public void joystickButtonChanged(Joystick pJoy) 
{
    int index=-1;
    if (this.pJoy[0] == pJoy){
        index=0;
    }
    if (this.pJoy[1] == pJoy){
        index=1;
    }
    if (this.pJoy[2] == pJoy){
        index=2;
    }
    if (this.pJoy[3] == pJoy){
        index=3;
    }
    if(index!=-1){// && this.pJoy[index].getPOV()!=-0.009999999776482582
        this.botones[index][BUTTON1] = (this.pJoy[index].getButtons() & Joystick.BUTTON1) != 0;
        this.botones[index][BUTTON2] = (this.pJoy[index].getButtons() & Joystick.BUTTON2) != 0;
        this.botones[index][BUTTON3] = (this.pJoy[index].getButtons() & Joystick.BUTTON3) != 0;
        this.botones[index][BUTTON4] = (this.pJoy[index].getButtons() & Joystick.BUTTON4) != 0;
        this.botones[index][P_UP] = (this.pJoy[index].getPOV()== 0.0);//up
        this.botones[index][P_DOWN] = (this.pJoy[index].getPOV()== 180.0);//down
        this.botones[index][P_LEFT] = (this.pJoy[index].getPOV()== 270.0);//left
        this.botones[index][P_RIGHT] = (this.pJoy[index].getPOV()== 90.0);//right
        this.botones[index][P_UP_RIGHT] = (this.pJoy[index].getPOV()== 45.0);//up right
        this.botones[index][P_DOWN_RIGHT] = (this.pJoy[index].getPOV()== 135.0);//down right
        this.botones[index][P_DOWN_LEFT] = (this.pJoy[index].getPOV()== 225.0);//down left
        this.botones[index][P_UP_LEFT] = (this.pJoy[index].getPOV()== 315.0);//up left
        botonEjeCambiado(index);
    }
}

public void botonEjeCambiado(int index){
    //COMPROBACION DE LAS DIAGONALES.
    if(botones[index][P_UP_RIGHT]==true){//arriba derecha
        botones[index][P_UP]=true;
        botones[index][P_RIGHT]=true;
    }
    if(botones[index][P_DOWN_RIGHT]==true){//abajo derecha
        botones[index][P_DOWN]=true;
        botones[index][P_RIGHT]=true;
    }
    if(botones[index][P_DOWN_LEFT]==true){//abajo izquierda
        botones[index][P_DOWN]=true;
        botones[index][P_LEFT]=true;
    }
    if(botones[index][P_UP_LEFT]==true){//arriba izquierda
        botones[index][P_UP]=true;
        botones[index][P_LEFT]=true;
    }
    
    
    if(botones[index][controles[index][0]]){
        Juego.superJugador.get(index).espacio=true;
    }else{
        Juego.superJugador.get(index).espacio=false;
    }
    if(botones[index][controles[index][1]]){
        Juego.superJugador.get(index).disparo=true;
    }else{
        Juego.superJugador.get(index).disparo=false;
        Juego.superJugador.get(index).disparado=false;
    }

    if(botones[index][controles[index][2]]){//arriba
        Juego.superJugador.get(index).arriba=true;
    }else{
        Juego.superJugador.get(index).arriba=false;
    }
    if(botones[index][controles[index][3]]){//derecha
        Juego.superJugador.get(index).derecha=true;
    }else{
        Juego.superJugador.get(index).derecha=false;
    }
    if(botones[index][controles[index][4]]){//izquierda
        Juego.superJugador.get(index).izquierda=true;
        Juego.superJugador.get(index).izquierda=true;
    }else{
        Juego.superJugador.get(index).izquierda=false;
    }
    if(botones[index][controles[index][5]]){//abajo
        Juego.superJugador.get(index).agacharse=true;
    }else{
        Juego.superJugador.get(index).agacharse=false;
    }
    if(botones[index][controles[index][6]]){//correr
        Juego.superJugador.get(index).corriendo=true;
    }else{
        Juego.superJugador.get(index).corriendo=false;
    }
    if(botones[index][controles[index][7]]){//cambiar
        Juego.superJugador.get(index).cambiar=true;
    }else{
        Juego.superJugador.get(index).cambiar=false;
        Juego.superJugador.get(index).cambiado=false;
    }
    /*
    switch(index){
        case 0:
            if(botones[index][controles[index][0]]){
                j1.espacio=true;
                j1.espacio2=true;
            }else{
                j1.espacio=false;
            }
            if(botones[index][controles[index][1]]){
                j1.disparo=true;
                j1.disparo2=true;
            }else{
                j1.disparo=false;
                j1.disparado=false;
            }

            if(botones[index][controles[index][2]]){//arriba
                j1.arriba=true;
                j1.arriba2=true;
            }else{
                j1.arriba=false;
            }
            if(botones[index][controles[index][3]]){//derecha
                j1.derecha=true;
                j1.derecha2=true;
            }else{
                j1.derecha=false;
            }
            if(botones[index][controles[index][4]]){//izquierda
                j1.izquierda=true;
                j1.izquierda=true;
            }else{
                j1.izquierda=false;
            }
            if(botones[index][controles[index][5]]){//abajo
                j1.agacharse=true;
                j1.agacharse2=true;
            }else{
                j1.agacharse=false;
            }
            if(botones[index][controles[index][6]]){//correr
                j1.corriendo=true;
                j1.corriendo2=true;
            }else{
                j1.corriendo=false;
            }
            if(botones[index][controles[index][7]]){//cambiar
                j1.cambiar=true;
                j1.cambiar2=true;
            }else{
                j1.cambiar=false;
                j1.cambiado=false;
            }
            break;
        case 1:
            if(botones[index][controles[index][0]]){
                j2.espacio=true;
                j2.espacio2=true;
            }else{
                j2.espacio=false;
            }
            if(botones[index][controles[index][1]]){
                j2.disparo=true;
                j2.disparo2=true;
            }else{
                j2.disparo=false;
                j2.disparado=false;
            }

            if(botones[index][controles[index][2]]){//arriba
                j2.arriba=true;
                j2.arriba2=true;
            }else{
                j2.arriba=false;
            }
            if(botones[index][controles[index][3]]){//derecha
                j2.derecha=true;
                j2.derecha2=true;
            }else{
                j2.derecha=false;
            }
            if(botones[index][controles[index][4]]){//izquierda
                j2.izquierda=true;
                j2.izquierda=true;
            }else{
                j2.izquierda=false;
            }
            if(botones[index][controles[index][5]]){//abajo
                j2.agacharse=true;
                j2.agacharse2=true;
            }else{
                j2.agacharse=false;
            }
            if(botones[index][controles[index][6]]){//correr
                j2.corriendo=true;
                j2.corriendo2=true;
            }else{
                j2.corriendo=false;
            }
            if(botones[index][controles[index][7]]){//cambiar
                j2.cambiar=true;
                j2.cambiar2=true;
            }else{
                j2.cambiar=false;
                j2.cambiado=false;
            }
            break;
        case 2:
            if(botones[index][controles[index][0]]){
                j3.espacio=true;
                j3.espacio2=true;
            }else{
                j3.espacio=false;
            }
            if(botones[index][controles[index][1]]){
                j3.disparo=true;
                j3.disparo2=true;
            }else{
                j3.disparo=false;
                j3.disparado=false;
            }

            if(botones[index][controles[index][2]]){//arriba
                j3.arriba=true;
                j3.arriba2=true;
            }else{
                j3.arriba=false;
            }
            if(botones[index][controles[index][3]]){//derecha
                j3.derecha=true;
                j3.derecha2=true;
            }else{
                j3.derecha=false;
            }
            if(botones[index][controles[index][4]]){//izquierda
                j3.izquierda=true;
                j3.izquierda=true;
            }else{
                j3.izquierda=false;
            }
            if(botones[index][controles[index][5]]){//abajo
                j3.agacharse=true;
                j3.agacharse2=true;
            }else{
                j3.agacharse=false;
            }
            if(botones[index][controles[index][6]]){//correr
                j3.corriendo=true;
                j3.corriendo2=true;
            }else{
                j3.corriendo=false;
            }
            if(botones[index][controles[index][7]]){//cambiar
                j3.cambiar=true;
                j3.cambiar2=true;
            }else{
                j3.cambiar=false;
                j3.cambiado=false;
            }
            break;
        case 3:
            if(botones[index][controles[index][0]]){
                j4.espacio=true;
                j4.espacio2=true;
            }else{
                j4.espacio=false;
            }
            if(botones[index][controles[index][1]]){
                j4.disparo=true;
                j4.disparo2=true;
            }else{
                j4.disparo=false;
                j4.disparado=false;
            }

            if(botones[index][controles[index][2]]){//arriba
                j4.arriba=true;
                j4.arriba2=true;
            }else{
                j4.arriba=false;
            }
            if(botones[index][controles[index][3]]){//derecha
                j4.derecha=true;
                j4.derecha2=true;
            }else{
                j4.derecha=false;
            }
            if(botones[index][controles[index][4]]){//izquierda
                j4.izquierda=true;
                j4.izquierda=true;
            }else{
                j4.izquierda=false;
            }
            if(botones[index][controles[index][5]]){//abajo
                j4.agacharse=true;
                j4.agacharse2=true;
            }else{
                j4.agacharse=false;
            }
            if(botones[index][controles[index][6]]){//correr
                j4.corriendo=true;
                j4.corriendo2=true;
            }else{
                j4.corriendo=false;
            }
            if(botones[index][controles[index][7]]){//cambiar
                j4.cambiar=true;
                j4.cambiar2=true;
            }else{
                j4.cambiar=false;
                j4.cambiado=false;
            }
            break;
    }
    */
    
}

public void actualizarControles(){
    for(int i=0;i<4;i++){
        controles[i][0]=Menu.controlesJugadores[i][4];//saltar
        controles[i][1]=Menu.controlesJugadores[i][5];//disparar
        controles[i][2]=Menu.controlesJugadores[i][3];//arriba
        controles[i][3]=Menu.controlesJugadores[i][1];//derecha
        controles[i][4]=Menu.controlesJugadores[i][0];//izquierda
        controles[i][5]=Menu.controlesJugadores[i][2];//abajo
        controles[i][6]=Menu.controlesJugadores[i][6];//correr
        controles[i][7]=Menu.controlesJugadores[i][7];//cambiar
    }
}
}