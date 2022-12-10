import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class Visualizar {

	public static StringBuffer Colorear(StringBuffer sB) {
		StringBuffer sb = new StringBuffer();
		try {
			sB.insert(0, "<html>");
			int index = 0;
			while (index < sB.length()) {
				int next = sB.indexOf("_", index);
				if (next > -1 && Character.isLetter(sB.charAt(next+1))) {
					int end = next + 1;
					// buscamos donde acaba la etiqueta ...
					while (Character.isLetter(sB.charAt(end))) end++;  // && Character.isUpperCase(sB.charAt(end))
					sB.insert(next, "<span style=\"color: blue;\">");
					end = end + 27;
					sB.insert(end, "</span>");
					index = end + 7;
				} else {
					break;
				}
			}
			sB.append("</html>");		
			// ahora transformamos los retornos de carro
			Pattern p = Pattern.compile("\r\n");
			Matcher m = p.matcher(sB);
			while (m.find()) {
			     m.appendReplacement(sb, "<br>");
			}
			m.appendTail(sb);
		}
		catch (Exception e) {
        }
		return sb;
	}
	
	// FUNCIONS PARA EMERGENCIAS, NO LAS USA LA APLICACIÓN
	
	public void Descomponer(File fEntrada) {
		// esta función es sólo para descomponer los textos de Herrera, no se usará en el programa
		  String sLin = "";
		  String linea = "";
		  BufferedReader in=null;
		  String cTip="";
		  
		  try
		  {
		    // preparamos ficheros de salida ..
			OutputStreamWriter fSonetos = new OutputStreamWriter(new FileOutputStream("sonetos.txt"), Charset.forName("UTF-8").newEncoder() );  
			OutputStreamWriter fCanciones = new OutputStreamWriter(new FileOutputStream("canciones.txt"), Charset.forName("UTF-8").newEncoder() );  
			OutputStreamWriter fElegias = new OutputStreamWriter(new FileOutputStream("elegias.txt"), Charset.forName("UTF-8").newEncoder() );  
			OutputStreamWriter fSestines = new OutputStreamWriter(new FileOutputStream("sestines.txt"), Charset.forName("UTF-8").newEncoder() );  
			OutputStreamWriter fEstancas = new OutputStreamWriter(new FileOutputStream("estancas.txt"), Charset.forName("UTF-8").newEncoder() );  

			in = g.TipoFichero(fEntrada);
			// ahora nos posicionamos al principio del fichero ...
			while ((sLin = in.readLine()) != null) {
				//System.out.println("linea: " + sLin.toString());
				linea = sLin.trim();
				//if(linea=="") continue;
		    	
				if(linea.toUpperCase().contains("<SONETO")) cTip="s";
				else if(linea.toUpperCase().contains("<CANCION")) cTip="c";
				else if(linea.toUpperCase().contains("<ELEGIA")) cTip="e";
				else if(linea.toUpperCase().contains("<SESTINA")) cTip="a";
				else if(linea.toUpperCase().contains("<ESTAN")) cTip="t";
				
				if(cTip=="s") {
					fSonetos.write(sLin);
					fSonetos.write("\r\n");
				}
				else if(cTip=="c") {
					fCanciones.write(sLin);
					fCanciones.write("\r\n");
				}
				else if(cTip=="e") {
					fElegias.write(sLin);
					fElegias.write("\r\n");
				}
				else if(cTip=="a") {
					fSestines.write(sLin);
					fSestines.write("\r\n");
				}
				else if(cTip=="t") {
					fEstancas.write(sLin);
					fEstancas.write("\r\n");
				}
			
				
			}  // fin while sLin
			fSonetos.close();
			fCanciones.close();
			fElegias.close();
			fSestines.close();
			fEstancas.close();

			JOptionPane.showMessageDialog(null, Idiomas.I1[ventana.iId], Idiomas.Info[ventana.iId], JOptionPane.INFORMATION_MESSAGE);
		  }
		  catch(Exception ex)
		  {
		     JOptionPane.showMessageDialog(null,ex+" " + sLin.toString() +  
		           "\nError", Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		  }
	}
	
	
	public static void Etiquetas(File fEntrada) {
		// esta función es sólo para descomponer los textos de Herrera, no se usará en el programa
		String sLin = "";
		String sOut= "";
		String linea = "";
		BufferedReader in=null;
		String cTip="";
		String sPalabra;	
		String sAux;
		int i;

		try
		{
			// preparamos fichero de salida ..
			OutputStreamWriter fEtiq = new OutputStreamWriter(new FileOutputStream("etiquetado.txt"), Charset.forName("UTF-8").newEncoder() );  

			in = g.TipoFichero(fEntrada);
			// ahora nos posicionamos al principio del fichero ...
			while ((sLin = in.readLine()) != null) {
				// quitamos comas, puntos y coma, punto, etc
				sLin = sLin.replaceAll("[,;.\t()!¡¿?-]"," ").trim();
				//if(!lMay) sLin = sLin.toLowerCase();
				sAux = sLin;
				sAux.replaceAll(" ", "");
				if(sAux.length()==0) {
					// si la línea está vacia nos la saltamos
				}
				else {
					// cogemos palabras 
					while(sLin.length()>0) {
						// consideramos las etiquetas como una única palabra aunque tenga blancos
						if(sLin.charAt(0)=='<') {
							i=sLin.indexOf(">");
							if(i==-1) i=sLin.length()-1; // si la etiqueta no se acaba, consideramos toda la línea
							sPalabra = sLin.substring(0,i+1);
							sLin = sLin.substring(i+1).trim();
						} 
						else if(sLin.indexOf(" ")>0) {
							sPalabra = sLin.substring(0,sLin.indexOf(" "));
							sLin = sLin.substring(sLin.indexOf(" ")).trim();
						} else {
							sPalabra = sLin.trim();
							sLin="";
						}
						if(sPalabra.length()>2)
							sOut=sOut + sPalabra.substring(0, 2);
						else 
							sOut=sOut + sPalabra;
						sOut=sOut + " ";
					}  // fin linea
					fEtiq.write(sOut + "\r\n");
					sOut="";
				}  
			}  // fin while sLin
			fEtiq.close();


			JOptionPane.showMessageDialog(null, Idiomas.I1[ventana.iId], Idiomas.Info[ventana.iId], JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" " + sLin.toString() +  
					"\nError", Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public static void Lematizar(File fEntrada) {
		// esta función es sólo para escoger la segunda palabra de una salida freeling para lematizar, no se usará en el programa
		String sLin = "";
		BufferedReader in=null;
		String sAux;
		String sLin2 = "";
		int i,j;
		int numlin=0;

		try
		{
			// preparamos fichero de salida ..
			OutputStreamWriter fEtiq = new OutputStreamWriter(new FileOutputStream("out.txt"), Charset.forName("UTF-8").newEncoder() );  

			in = g.TipoFichero(fEntrada);
			// ahora nos posicionamos al principio del fichero ...
			while ((sLin = in.readLine()) != null) {
				numlin++;
				// quitamos comas, puntos y coma, punto, etc
				//sLin = sLin.replaceAll("[,;.\t()!¡¿?-]"," ").trim();
				//if(!lMay) sLin = sLin.toLowerCase();
				sAux = sLin;
				sAux.replaceAll(" ", "");
				if(sAux.length()==0) {
					// si la línea está vacia nos la saltamos
				}
				else {
					// cogemos la segunda palabra
					if(numlin>140) 
						sAux="";
					if(sLin.length()>0) {
						i=sLin.indexOf(" ")+1;
						if(i!=-1) {
							sLin2 = sLin.substring(i); 
							j=sLin2.indexOf(" ")+1;
							if(j!=-1) fEtiq.write(sLin.substring(i, i+j) + "\r\n");  
						}
					}  // fin linea
				}  
			}  // fin while sLin
			fEtiq.close();


			JOptionPane.showMessageDialog(null, Idiomas.I1[ventana.iId], Idiomas.Info[ventana.iId], JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" " + sLin.toString() +  
					"\nError", Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		}
	}	
	
	public static void TI(File fEntrada) {
		// esta función es sólo para etiquetar TI con indentación, no se usará en el programa
		String sLin = "";
		String sOut= "";
		BufferedReader in=null;
		String sAux;
		int i,j;
		int numlin=0;
		boolean lH=true;

		try
		{
			// preparamos fichero de salida ..
			OutputStreamWriter fEtiq = new OutputStreamWriter(new FileOutputStream("outTI.txt"), Charset.forName("UTF-8").newEncoder() );  

			in = g.TipoFichero(fEntrada);
			// ahora nos posicionamos al principio del fichero ...
			while ((sLin = in.readLine()) != null) {
				numlin++;
				// quitamos comas, puntos y coma, punto, etc
				//sLin = sLin.replaceAll("[,;.\t()!¡¿?-]"," ").trim();
				//if(!lMay) sLin = sLin.toLowerCase();
				sAux = sLin.trim();
				sOut=sLin.trim();

				if(sAux.length()==0) {
					// si la línea está vacia nos la saltamos
				}
				else if(sAux.charAt(0)=='<') { // es un título y lo escribimos como tal
					fEtiq.write("        " + "</lg>" + "\r\n"); 
					fEtiq.write("    " + "</div1>" + "\r\n"); 
					fEtiq.write("    " + "<div1 type=\"poem\">" + "\r\n"); 
					fEtiq.write("        " + sOut + "\r\n");
					lH=true;
				}
				else {
					for(i=0;i<sLin.length() && sLin.charAt(i)==' ';i++);
					if(i>5) {
						if(lH) lH=false;
						else	fEtiq.write("        " + "</lg>" + "\r\n"); 
						fEtiq.write("        " + "<lg>" + "\r\n"); 
					}
					fEtiq.write("          " + "<l>" + sOut + "</l>" + "\r\n");  
				}  
			}  // fin while sLin
			fEtiq.write("        " + "</lg>" + "\r\n"); 
			fEtiq.close();


			JOptionPane.showMessageDialog(null, Idiomas.I1[ventana.iId], Idiomas.Info[ventana.iId], JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" " + sLin.toString() +  
					"\nError", Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		}
	}	
	
	public static void TI2(File fEntrada) {
		// esta función es sólo para etiquetar TI con lineas en blanco, no se usará en el programa
		String sLin = "";
		String sOut= "";
		BufferedReader in=null;
		String sAux;
		int i,j;
		int numlin=0;
		boolean lLg=true;  // abro etiqueta lg
		boolean lVacia=false; // indica si linea vacia 

		try
		{
			// preparamos fichero de salida ..
			OutputStreamWriter fEtiq = new OutputStreamWriter(new FileOutputStream("outTI2.txt"), Charset.forName("UTF-8").newEncoder() );  

			in = g.TipoFichero(fEntrada);
			// ahora nos posicionamos al principio del fichero ...
			while ((sLin = in.readLine()) != null) {
				numlin++;
				// quitamos comas, puntos y coma, punto, etc
				//sLin = sLin.replaceAll("[,;.\t()!¡¿?-]"," ").trim();
				//if(!lMay) sLin = sLin.toLowerCase();
				sAux = sLin.trim();
				sOut=sLin.trim();

				if(sAux.length()==0) {
					// si la línea está vacia o viene indentación nos la saltamos cerrando párrafo si procede
					lVacia=true;
					if(lLg) {
						fEtiq.write("        " + "</lg>" + "\r\n"); 
						lLg=false;
					}
				}
				else if(sAux.charAt(0)=='<') { // es un título y lo escribimos como tal
					fEtiq.write("    " + "</div1>" + "\r\n"); 
					fEtiq.write("    " + "<div1 type=\"poem\">" + "\r\n"); 
					fEtiq.write("        " + sOut + "\r\n");  
				}
				else {
					if(lVacia) { // venimos de una línea vacía y esta no lo es, o hay indentación, luego párrafo
						fEtiq.write("        " + "<lg>" + "\r\n"); 
						lLg=true;
					}
					fEtiq.write("          " + "<l>" + sOut + "</l>" + "\r\n");  
					lVacia=false;
				}  
			}  // fin while sLin
			fEtiq.write("        " + "</lg>" + "\r\n"); 
			fEtiq.close();


			JOptionPane.showMessageDialog(null, Idiomas.I1[ventana.iId], Idiomas.Info[ventana.iId], JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" " + sLin.toString() +  
					"\nError", Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		}
	}		
}  // fin class Visualizar
