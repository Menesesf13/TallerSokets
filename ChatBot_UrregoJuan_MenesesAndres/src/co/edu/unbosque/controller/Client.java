package co.edu.unbosque.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	private final int PUERTO=8000;
	private final String HOST="127.0.0.1";
	
	private Socket cs;
	private BufferedReader inServer,inClient;
	private PrintWriter pw;
	
	public Client() {
		try {
			cs=new Socket(HOST, PUERTO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initClient() {
		try {
			inServer=new BufferedReader(new InputStreamReader(cs.getInputStream()));
			pw=new PrintWriter(cs.getOutputStream(),true);
			inClient=new BufferedReader(new InputStreamReader(System.in));
			String message,reponse;
			do {
				System.out.print("Mensaje a enviar al servidor: ");
				message=inClient.readLine();
				pw.println(message);
				reponse=inServer.readLine();
				System.out.println("Respuesta del servidor: "+ reponse);
			}while(!message.equalsIgnoreCase("salir"));
			inServer.close();
            pw.close();
            inClient.close();
            cs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
