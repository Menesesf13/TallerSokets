package co.edu.unbosque.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import co.edu.unbosque.model.persistence.FileHandler;

public class ChatBot {
	
	
	private HashMap<String, String> lresponse;
	
	public ChatBot() {
		lresponse=loadFile();
	}
	
	public String generateResponse(String question) {
		if(!question.matches(".*[^()0-9+\\-*/\\., ].*")) {
			question=question.replace(",", ".").replaceAll("\\s+", "");
			question=question.replace(")(", ")*(");
			try {
				String num=Double.toString(generateNum(question));
				return num;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			String compile=question.trim().toLowerCase().replaceAll("[^a-z]", "");
			if(lresponse.containsKey(compile)) {
				String response=lresponse.get(compile);
				if(response.equals("El dia de hoy es"))response+=(" "+LocalDate.now().toString()+".");
				else if(response.equals("La hora actual es")) {
					response+=(" "+LocalTime.now().toString()+".");
					response=response.substring(0,response.lastIndexOf(":"));
				}
				return response;
			}
		}
		return "Lo lamento no tengo respuesta para lo que acabas de introducir.";
	}
	
	public double generateNum(String question) {
		String expre=question;
		while (expre.contains("(")&&expre.contains(")")) {
			int start=expre.lastIndexOf("(")+1;
			int end=0;
			boolean s=false,e=false;
			if(start-2!=-1&&Character.isDigit(expre.charAt(start-2)))s=true;
			for (int i = start; i < expre.length(); i++) {
				if(expre.charAt(i)==')') {
					end=i;
					if(i+1!=expre.length()&&Character.isDigit(expre.charAt(i+1)))e=true;
					break;
				}
			}
			String calc=expre.substring(start,end);
			String replace=(s?"*":"")+Double.toString(calculate(calc))+(e?"*":"");
			expre=expre.substring(0,start-1)+replace+expre.substring(end+1);
		}
		while(expre.contains("*")||expre.contains("/")) {
			int mul=expre.indexOf("*");
			int div=expre.indexOf("/");
			int bas=0,start=0,end=expre.length();
			if(mul==-1)bas=div;
			else if(div==-1)bas=mul;
			else if(mul<div)bas=mul;
			else bas=div;
			for(int i=bas+1;i<expre.length();i++) {
				if(!Character.isDigit(expre.charAt(i))&&expre.charAt(i)!='.') {
					end=i;
					break;
				}
			}
			for(int i=bas-1;i>=0;i--) {
				if(!Character.isDigit(expre.charAt(i))&&expre.charAt(i)!='.') {
					start=i+1;
					break;
				}
			}
			String calc=expre.substring(start,end);
			expre=expre.replace(calc, Double.toString(calculate(calc)));
		}
		return calculate(expre);
	}
	
	public Double calculate(String calc) {
		calc=calc.replaceAll("[^0-9+\\-*/\\.,]", "");
		String[] parts=calc.split("[+\\-*/]");
		String operators=calc.replaceAll("[0-9\\.,]", "");
		while (operators.length()>=parts.length&&operators.length()!=0&&parts.length!=0)operators=operators.substring(0,operators.length()-1);
		double num=Double.parseDouble(parts[0]);
		for (int i = 0; i < operators.length(); i++) {
			if(operators.charAt(i)=='+') num+=Double.parseDouble(parts[i+1]);
			else if(operators.charAt(i)=='-') num-=Double.parseDouble(parts[i+1]);
			else if(operators.charAt(i)=='*') num*=Double.parseDouble(parts[i+1]);
			else if(operators.charAt(i)=='/') num/=Double.parseDouble(parts[i+1]);
		}
		return num;
	}

	private HashMap<String, String> loadFile() {
		HashMap<String, String> aux=new HashMap<>();
		String[] lines=FileHandler.reedTextFile("Response.txt").split("\n");
		for(String line : lines) {
			int sep=line.indexOf(":");
			String[] ques=line.substring(0,sep).split(",");
			String res=line.substring(sep+1);
			for(String s : ques)aux.put(s, res);
		}
		return aux;
	}

}
