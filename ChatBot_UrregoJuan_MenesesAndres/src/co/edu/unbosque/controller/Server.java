package co.edu.unbosque.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import co.edu.unbosque.model.ChatBot;

public class Server {
	
	private final int PUERTO=8000;
	
	private ServerSocket ss;
	private Socket cs;
	private BufferedReader in;
	private PrintWriter pw;
	private ChatBot bot;
	
	public Server() {
		try {
			bot=new ChatBot();
			ss=new ServerSocket(PUERTO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initServer() {
		try {
			System.out.println("Servidor en línea. Esperando conexiones...");
			cs=ss.accept();
			in=new BufferedReader(new InputStreamReader(cs.getInputStream()));
			pw= new PrintWriter(cs.getOutputStream(),true);
			String message,response;
			while((message = in.readLine())!=null) {
				response=bot.generateResponse(message);
				System.out.println("Mensaje del cliente: " + message);
				System.out.println("Respuesta del bot: "+ response);
                pw.println(response);
                if (message.equalsIgnoreCase("salir")) {
                    break;
                }
			}
			in.close();
			pw.close();
			cs.close();
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
