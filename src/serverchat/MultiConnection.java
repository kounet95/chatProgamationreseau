package serverchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiConnection extends Thread {
	private int nbclient;
	private boolean active;
	private List<ConnectionTread> clients = new ArrayList<ConnectionTread>();

	public static void main(String[] args) {
		new MultiConnection().start();

	}
	
	@Override
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(1010);
			System.out.println("attend la connection");
			while (true) {
				Socket s = ss.accept();
				++nbclient;
				System.out.println("le client:"+ s.getRemoteSocketAddress());
				System.out.println("creation d'un tread pour ce client");
			ConnectionTread connectionTread	= new ConnectionTread(s, nbclient);
			clients.add(connectionTread);
			connectionTread.start();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public class ConnectionTread extends Thread{
		protected Socket s;
		protected int nbclient;
		private String ip;
		public ConnectionTread(Socket s, int nbclient) {
			this.s = s;
			this.nbclient = nbclient;
		}
		public void broadcasteMessage(String message, Socket s, int numeroclient) {
			try {
			for (ConnectionTread client:clients) {
				
					if (client.s!=clients) {
						if (client.nbclient==numeroclient || numeroclient ==-1) {
							PrintWriter printWriter = new PrintWriter(client.s.getOutputStream(),true);
							
							printWriter.println(message);
						}
						
					}
			}	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			}
				
			
			  
		
		@Override
		public void run() {
			 InputStream is;
			try {
				is = s.getInputStream();
				//System.err.println("vous devez donne une chaine de caractere ");
				InputStreamReader  isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
				ip = s.getRemoteSocketAddress().toString();
				pw.println("bien vennue monsieur "+nbclient+"votre adresse est "+ ip);
				System.out.println("la connection d'un client"+ nbclient);
				while (true) {
					String req = br.readLine();
					if (req.contains("=>")) {
						String [] reqParam = req.split("=>");
						if (reqParam.length==2);
						String message = reqParam[1];
						int numeroclient = Integer.parseInt(reqParam[0]);
						broadcasteMessage(req, s,numeroclient);
					}
					else {
						//System.out.println("le clien dont ladresse est:"+ip+"a envoyer un message");
					    broadcasteMessage(req, s, -1);	
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
		}
	
	
	

}
