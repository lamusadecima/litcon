import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextArea;

import javax.swing.table.DefaultTableModel;

public class BuscarConcordancias {

	public int RecogerPalabras(Collection<String> cPalabras, File fEntrada, JTable tResult, JTextArea tExcluir, Boolean lTag, Boolean lMay) {
		String sPalabra;	
		String sAux;
		int totPalabras=0;
		String sExcluir;
		int i=0;
		
		try
		{
			// comprobamos que se ha seleccionado ficheros de entrada ...
			if(fEntrada == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
			   return 0;
			}

			int numPalabras=0;
			String sLin;
			DefaultTableModel model = new ModelConsulta();
			model.addColumn("Palabra");
			tResult.setModel(model);

			// preparamos exclusiones
			sExcluir = " " + tExcluir.getText().replaceAll("\r\n", " ").replaceAll("\n", " ") + " ";

			// ahora nos posicionamos al principio del fichero ...
			BufferedReader in = g.TipoFichero(fEntrada);
			while ((sLin = in.readLine()) != null) {  // leemos la linea 
				// quitamos comas, puntos y coma, punto, etc
				sLin = sLin.replaceAll(g.sPuntua," ").trim();
				if(!lMay) sLin = sLin.toLowerCase();
				sAux = sLin;
				sAux.replaceAll(" ", "");
				if(sAux.length()==0) {
					// si la línea está vacia nos la saltamos
				}
				else {
					// cogemos palabras 
					while(sLin.length()>0) {
						// consideramos las etiquetas como una única palabra aunque tenga blancos
						if(sLin.charAt(0)=='#') {
							i=sLin.lastIndexOf("#");
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
						if(sExcluir.indexOf(" " + sPalabra.toString() + " ")>-1 || g.isNumeric(sPalabra)  
							|| (lTag && sPalabra.indexOf("#") > -1)	) {
							// esta palabra esta excluida o es un número o es una etiqueta y no debe salir
						}
						else {
							//cPalabras.add(sPalabra.toString().toLowerCase());
							cPalabras.add(sPalabra.toString());
						}
					}
				}			  
		  
			}
			in.close();  // cerramos fichero
			// ahora pasamos las palabras a la tabla
			for(Object o : cPalabras){
				model.addRow(new Object[]{o.toString()});
				numPalabras++;
			}
			totPalabras = numPalabras;
			return totPalabras;
		  }
		  catch(Exception ex)
		  {
		     JOptionPane.showMessageDialog(null,ex+"" + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
		     return 0;
		  }
		}

	public void Concordancias(Collection<String> cPalabras, File fEntrada, File fSalida, JProgressBar prBarra) {
		  String sNomSoneto = new String("");
		  String sAux;
		  String sPosibleSoneto = new String("");
		  int iNumSoneto = 0;
		  int numPalabras = 0;
		  String sPalabra = "";
		  String linea = "";
		  String linea2 = "";  // la línea sin puntuación
		  int oldpercent=0;
		  BufferedReader in;
		  OutputStreamWriter out;	
		  
		  try
		  {
			// comprobamos que se han seleccionado los ficheros de entrada y salida, y se han recogido las palabras ...
			if(fEntrada == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				  return;
			}
			else if(fSalida == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E7[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
			     return;
			}
			else if(cPalabras == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E8[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return;
			}
			
			// preparamos el fichero de salida ..
			out = new OutputStreamWriter(new FileOutputStream(fSalida), Charset.forName("UTF-8").newEncoder() );
		  
			prBarra.setValue(0);
			// recorremos las palabras a buscar concordancias ...
			for(Object o : cPalabras) {
				sPalabra = " " + o.toString() + " ";
				numPalabras++;
				out.write(sPalabra.toString());
				out.write("\r\n");
				// empezamos a leer el fichero de entrada ...
				in = g.TipoFichero(fEntrada);
				int numLinea=0;
				while ((linea = in.readLine()) != null) {  // leemos la linea
					numLinea++;
					// controlamos nombre de soneto
					sAux = linea;
					sAux.replaceAll(" ", "");
					if(sAux.length()==0) {
						// si la línea está vacia es que la anterior era el nombre del soneto
						sNomSoneto = sPosibleSoneto;
						iNumSoneto=0;
					}
					else {
						sPosibleSoneto = linea;
						iNumSoneto++; 
					}
					// buscamos la palabra ...
					linea2 = " " + linea.replaceAll(g.sPuntua," ").trim().toLowerCase() + " ";
					if(linea2.contains(sPalabra.toString())) { 
						out.write("    " + String.format("%1$-6s", String.valueOf(numLinea)) + String.format("%1$-25s",sNomSoneto.toString()) + String.format("%1$-6s",String.valueOf(iNumSoneto)) + linea.toString());
						out.write("\r\n");
					}
				}
				in.close();
		    	final int percent = numPalabras * 100 / cPalabras.size();
				if(percent != oldpercent) {
		        	prBarra.setValue(percent);
		        	prBarra.setString(Idiomas.Processing[ventana.iId] + percent + "%");
					//System.out.println("progressBar.getValue() := " + prBarra.getValue());
					oldpercent = percent;
				}
				
			}
	    	out.close();
		    JOptionPane.showMessageDialog(null, Idiomas.I1[ventana.iId], Idiomas.Info[ventana.iId], JOptionPane.INFORMATION_MESSAGE);
		  }
		  catch(Exception ex)
		  {
		     JOptionPane.showMessageDialog(null,ex+" " + sPalabra.toString() + " " + linea.toString() +  
		           "\nError", Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		  }
		}
	
	public void BuscarConcorda(Collection<String> cPalabras, File fEntrada, File fSalida, JProgressBar prBarra) {
		  String sNomSoneto = new String("");
		  String sAux;
		  String sPosibleSoneto = new String("");
		  int iNumSoneto = 0;
		  int numPalabras = 0;
		  Scanner scanner;
		  String sPalabra = "";
		  FileWriter save;
		  String linea = "";
		  String linea2 = "";  // la línea sin puntuación
		  int oldpercent=0;
		  
		  try
		  {
			// comprobamos que se han seleccionado los ficheros de entrada y salida, y se han recogido las palabras ...
			if(fEntrada == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				  return;
			}
			else if(fSalida == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E7[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
			     return;
			}
			else if(cPalabras == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E8[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return;
			}			

		    // preparamos el fichero de salida ..
			save=new FileWriter(fSalida);
			save.write(Idiomas.FileConcord[ventana.iId]);
			prBarra.setValue(0);
			// recorremos las palabras a buscar concordancias ...
			for(Object o : cPalabras) {
				sPalabra = " " + o.toString() + " ";
				numPalabras++;
				save.write(sPalabra.toString());
				save.write("\r\n");
			
				// ahora nos posicionamos al principio del fichero ...
				scanner = new Scanner(fEntrada);
				int numLinea=0;

				while (scanner.hasNextLine()) {
					numLinea++;	
					// leemos la linea 
					linea = scanner.nextLine();
					// controlamos nombre de soneto
					sAux = linea;
					sAux.replaceAll(" ", "");
					if(sAux.length()==0) {
						// si la línea está vacia es que la anterior era el nombre del soneto
						sNomSoneto = sPosibleSoneto;
						iNumSoneto=0;
					}
					else {
						sPosibleSoneto = linea;
						iNumSoneto++; 
					}
			  
					// buscamos la palabra ...
					linea2 = " " + linea.replaceAll(g.sPuntua," ").trim() + " ";
					if(linea2.contains(sPalabra.toString())) { 
						save.write("    " + String.format("%1$-6s", String.valueOf(numLinea)) + String.format("%1$-25s",sNomSoneto.toString()) + String.format("%1$-6s",String.valueOf(iNumSoneto)) + linea.toString());
						save.write("\r\n");
					}
				}
		    	scanner.close();
		    	final int percent = numPalabras * 100 / cPalabras.size();
				if(percent != oldpercent) {
		        	prBarra.setValue(percent);
		        	prBarra.setString(Idiomas.Processing[ventana.iId] + percent + "%");
					//System.out.println("progressBar.getValue() := " + prBarra.getValue());
					oldpercent = percent;
				}
			}  // siguiente palabra
	    	save.close();
		    JOptionPane.showMessageDialog(null, Idiomas.I1[ventana.iId], Idiomas.Info[ventana.iId], JOptionPane.INFORMATION_MESSAGE);
		  }
		  catch(Exception ex)
		  {
		     JOptionPane.showMessageDialog(null,ex+" " + sPalabra.toString() + " " + linea.toString() +  
		           "\nError", Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		  }
	}
	
}  // fin BuscarConcordancias.java
