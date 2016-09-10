/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BlockMan;

import java.io.*;
import java.net.*;
import java.util.Arrays;

class gameClient 
{
	public static void main(String args[]) throws Exception
	{
		int port = 0;
		if(args.length < 2)
		{
			System.err.println("Error: Please a server name or address and a port number");
			System.exit(1);
		}
		try
		{
			port = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException e)
		{
			System.err.println("Error: Argument could not be parsed into an integer, please include port number as second argument");
			System.exit(1);
		}
		ClientGame game = new ClientGame(args[0], port);
		game.start();
	}

}

class ClientGame {
	ClientNetWorkIO IO;
	BufferedReader inFromUser;
	public String username;
	boolean activeGame;
	ClientGame(String address, int port)
	{
		IO = new ClientNetWorkIO(address, port);
		inFromUser = new BufferedReader(new InputStreamReader(System.in));

	}

	void start()
	{
		username = CreateName();
		System.out.println("Username is: "+username);
		IO.sendMessage(username+":join");
		if(IO.getMessage().equals("Server full"))
		{
			System.out.println("Server full, try again later");
			System.exit(0);
		}
		else
		{
			startGame();
		}
		IO.sendMessage(username+":quit");
	}

	private String CreateName()
	{
		while(true)
		{
			System.out.print("Please enter a user name: ");
			try
			{
				return inFromUser.readLine();
			}
			catch(IOException e)
			{
				System.err.println("Error: Invalid text entry");
			}
		}
	}

	private void startGame()
	{
			activeGame = true;
			IO.sendMessage(username+":"+getShips());
			ProcessCommand(IO.getMessage());
			while(activeGame)
			{
				IO.sendMessage(username+":"+getLine());
				System.out.println("sent package. waiting for replay...");
				ProcessCommand(IO.getMessage());
			}
	}

	private String getLine()
	{
		String command = "";
		while(true)
		{
			try
			{
				System.out.println("Please entered a letter and number combination");
				command = inFromUser.readLine();
			}
			catch(IOException e)
			{
				System.err.println("Error:Invalid text entry");
			}
			if(command.matches("[a-jA-J]{1}[0-9]{1}"))
			{
				return command.toLowerCase();
			}
			System.out.println("Try again!");
		}
	}

	private String getShips()
	{
		char[][] board = new char[10][10];
		for(int i=0; i < 10; i++)
		{
			Arrays.fill(board[i], '-');
		}
		System.out.println("Please enter the location of ships you wish to place");
		String[] ships = new String[5];
		int currentShip = 0;
		int length = 0;
		while(currentShip != 5)
		{		
			switch(currentShip)
			{
				case 0:
					System.out.println("Placeing the carrier(5)!"); length = 5; break;
				case 1:
					System.out.println("Placeing the battleship(4)!"); length = 4; break;
				case 2:
					System.out.println("Placeing the destoryer(3)!"); length = 3; break;
				case 3:
					System.out.println("Placeing the submarine(3)!"); length = 3; break;
				case 4:
					System.out.println("Placeing the patrol boat(2)!"); length = 2; break;
			}
			DisplayShips(board);
			System.out.println("Enter the first corner of the ship");
			String shipLoc = getLine();
			if(checkLoc(shipLoc, board))
			{
				System.out.println("Location invalid, it conflicts with another ship");
				continue;
			}
			System.out.println("Enter the last square of the ship's location");
			String endLoc = getLine();
			if(shipLoc.charAt(0) == endLoc.charAt(0))
			{
				if(shipLoc.charAt(1) - endLoc.charAt(1) < 0)
				{
					if(Math.abs(shipLoc.charAt(1) - endLoc.charAt(1)-1) != length)
					{
						System.out.println("Location invalid, incorrect length specified");
						continue;
					}
					for(int j = 0; j < length-2; j++)
					{
						shipLoc += shipLoc.charAt(0);
						shipLoc += (char)(shipLoc.charAt(1)+j+1);
					}
				}
				else
				{
					if(Math.abs(shipLoc.charAt(1) - endLoc.charAt(1)+1) != length)
					{
						System.out.println("Location invalid, incorrect length specified");
						continue;
					}
					for(int j = 0; j < length-2; j++)
					{
						shipLoc += shipLoc.charAt(0);
						shipLoc += (char)(endLoc.charAt(1)+j+1);
					}
				}
				shipLoc += endLoc;
				if(isColliding(shipLoc, board))
				{
					System.out.println("Location invalid, two or more ships are colliding");
					continue;
				}
				ships[currentShip] = shipLoc;
				for(int i = 0; i < shipLoc.length(); i+=2)
				{
					String loc = shipLoc.substring(i, i+2);
					board[loc.charAt(0)-97][loc.charAt(1)-48] = 'S';
				}
				currentShip++;
				continue;
				
			}
			if(shipLoc.charAt(1) == endLoc.charAt(1))
			{
				if(shipLoc.charAt(0) - endLoc.charAt(0) < 0)
				{
					if(Math.abs(shipLoc.charAt(0) - endLoc.charAt(0)-1) != length)
					{
						System.out.println("Location invalid, incorrect length specified");
						continue;
					}
					for(int j = 0; j < length-2; j++)
					{
						shipLoc += (char)(shipLoc.charAt(0)+j+1); 
						shipLoc += shipLoc.charAt(1);
					}
				}
				else
				{
					if(Math.abs(shipLoc.charAt(0) - endLoc.charAt(0)+1) != length)
					{
						System.out.println("Location invalid, incorrect length specified");
						continue;
					}
					for(int j = 0; j < length-2; j++)
					{
						shipLoc += (char)(endLoc.charAt(0)+j+1);
						shipLoc += shipLoc.charAt(1);
					}
				}
				shipLoc += endLoc;
				if(isColliding(shipLoc, board))
				{
					System.out.println("Location invalid, two or more ships are colliding");
					continue;
				}
				ships[currentShip] = shipLoc;
				for(int i = 0; i < shipLoc.length(); i += 2)
				{
					String loc = shipLoc.substring(i, i+2);
					board[loc.charAt(0)-97][loc.charAt(1)-48] = 'S';
				}
				currentShip++;
				continue;		
			}
			System.out.println("Location invalid, ships must be placed in streight lines");
		}
		return ships[0]+ships[1]+ships[2]+ships[3]+ships[4];
	}

	private boolean checkLoc(String Loc, char[][] board)
	{
		return board[Loc.charAt(0)-97][Loc.charAt(1)-48] == 'S';
	}

	private boolean isColliding(String shipLoc, char[][] board)
	{
		for(int i = 0; i < shipLoc.length(); i += 2)
		{
			String loc = shipLoc.substring(i, i+2);
			if(checkLoc(loc, board))
			{
				return true;
			}
		}
		return false;
	}

	private void DisplayShips(char[][] board)
	{
		String out = " abcdefghij\n";
		for(int i = 0; i < 10; i++)
		{
			out += i;
			for(int j= 0; j < 10; j++)
			{
				out += board[j][i];
			}
			out += '\n';
		}
		System.out.println(out);
	}

	private void ProcessCommand(String Command)
	{
		String internal = Command.substring(0, Command.indexOf(":"));
		String external = Command.substring(Command.indexOf(":")+1, Command.indexOf(0));
		if(internal.equals("win")||internal.equals("lose")||internal.equals("reset"))
		{
			activeGame = false;
		}
		System.out.println(external);
	}
}

class ClientNetWorkIO
{
	DatagramSocket clientSocket;
	int port;
	DatagramPacket Packet;
	InetAddress ServerIPAddress;
	byte[] Data;

	ClientNetWorkIO(String address, int port)
	{
		this.port = port;
		try
		{
			ServerIPAddress = InetAddress.getByName(address);
		}
		catch(UnknownHostException e)
		{
			System.err.println("Error: Host invalid");
			System.exit(1);
		}
		try
		{
			clientSocket = new DatagramSocket();
		}
		catch(SocketException e)
		{
			System.err.println("Error: Socket could not be created");
			System.exit(1);
		}
	}

	public String getMessage()
	{
		Data = new byte[1024];
		Packet = new DatagramPacket(Data, Data.length);
		try
		{
			clientSocket.receive(Packet);
		}
		catch(IOException e)
		{
			System.err.println("Error: error while recieving packet");
			return "";
		};
		return new String(Packet.getData());
	}

	public void sendMessage(String line)
	{
		Data = line.getBytes();
		Packet = new DatagramPacket(Data, Data.length, ServerIPAddress, port);
		try
		{;
			clientSocket.send(Packet);
		}
		catch(IOException e)
		{
			System.err.println("Error: error while sending packet");
		}
	}
}