package co.edu.unbosque.model.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {
	
	private static Scanner reader;
	private static File file;
	
	public FileHandler() {}
	
	public static String reedTextFile(String path) {
		StringBuffer sb = new StringBuffer();
		file=new File("src/co/edu/unbosque/model/persistence/"+path);
		try {
			reader=new Scanner(file);
			while(reader.hasNextLine()) {
				sb.append(reader.nextLine()+"\n");
			}
		} 
		catch (FileNotFoundException e) {
			System.out.println("El archivo no exite.");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("No tiene los permisos requeridos para acceder al archivo.");
			e.printStackTrace();
		}
		return sb.toString();
	}

}
