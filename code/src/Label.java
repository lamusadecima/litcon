import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class Label {
	
	private Map<String, Integer> mWord;
	
	public int Etiquetado(File fEntrada, File fDicc, JTable tResult, JTable tResult2, JProgressBar prBarra, String sExcluir, Boolean lTag, Boolean lAscii, String sMaxLabel) {
		  String sPalabra = " ";
		  String sLabel="";
		  String sLin = "";
		  int oldpercent=0;
		  int i=0;
		  int numP=1, numP2=1;
		  Boolean lFound=false;
		  BufferedReader in;
		  OutputStreamWriter out;
		  String sKey1 = null;
		  int iLabel;
		  
		  try
		  {
			// comprobamos que se han seleccionado los ficheros de entrada y salida, y de diccionario ...
			if(fEntrada == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return 0;
			}
			else if(fDicc == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E15[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return 0;
			}			
			// preparamos las tablas
			DefaultTableModel model = new ModelConsulta(); //DefaultTableModel();
			model.addColumn(Idiomas.Orden[ventana.iId]);
			model.addColumn(Idiomas.Word[ventana.iId]);
			model.addColumn("Etiqueta");
			tResult.setModel(model);
			tResult.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);			

			DefaultTableModel model2 = new ModelConsulta(); //DefaultTableModel();
			model2.addColumn(Idiomas.Orden[ventana.iId]);
			model2.addColumn(Idiomas.Word[ventana.iId]);
			model2.addColumn("Etiqueta");
			tResult2.setModel(model2);
			tResult2.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);			
			
			// leemos el fichero de entrada y sacamos todas las palabras distintas
			mWord = g.RecogePalabras(fEntrada, sExcluir, lTag, false, lAscii);
			mWord = new TreeMap<String, Integer>(mWord);  // alfabética ascendente

			// preparamos el fichero de salida ..
			//out = new OutputStreamWriter(new FileOutputStream(fSalida), Charset.forName("UTF-8").newEncoder() );  

			// ahora lo vamos confrontando con el diccionario para realizar el etiquetado ...
			Set set = mWord.entrySet();
		    Iterator iterator = set.iterator();
		    Map.Entry mentry;

		    // abrimos el diccionario ...
		    iLabel = Integer.parseInt(sMaxLabel);	
			in = g.TipoFichero(fDicc);
			while((sLin = in.readLine()) != null) { // quitamos las líneas que no son palabras
				if(sLin.charAt(0) == 'a' && sLin.indexOf(" ")>0) break;
			}
			if(sLin != null) {
				sPalabra = sLin.substring(0,sLin.indexOf(" "));
				//out.write(sLin + "\r\n");
				//if(sPalabra.compareTo(g.soloASCII(sPalabra))!=0)
				//	out.write(g.soloASCII(sPalabra) + sLin.substring(sLin.indexOf(" ")) + "\r\n");
			}
			// empieza el baile ...
	        while(sKey1 != "====") { 
			    // cogemos la primera palabra
		        if(iterator.hasNext()) {  
		        	mentry = (Map.Entry) iterator.next();
		        	sKey1= mentry.getKey().toString();
		        }
		        else sKey1 = "====";  // se acabaron las palabras
		        // buscamos en el diccionario ...
		        lFound=false;
				while (!lFound && sPalabra != "====" && sKey1 != "====") {  
					if(sPalabra.compareTo(sKey1)==0) {  // encontrado en el diccionario
						lFound=true;
						// buscamos la etiqueta en mayúsculas
						for(i=0;!Character.isUpperCase(sLin.charAt(i)) && i<sLin.length();i++);
						sLabel = sLin.substring(i);
						if(sLabel.indexOf(" ")>0) sLabel = sLabel.substring(0, sLabel.indexOf(" "));
						if(iLabel<=sLabel.length()) sLabel = sLabel.substring(0,iLabel);
						model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numP++)), (String) sKey1, (String) sLabel});
						//out.write(sKey1 + "_" + sLabel);
						//out.write("\r\n");
						break;
					}
					else if(sKey1.compareTo(sPalabra)<0) { // no encontrado en el diccionario
				        model2.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numP2++)), (String) sKey1, (String) "_NO"});
						//out.write(sKey1);
						//out.write("\r\n");
						break;
					}
					// leemos la linea 
					while((sLin = in.readLine()) != null && (sLin.charAt(0)=='<' || sLin.indexOf(" ")<=0));

					if(sLin != null && sLin.charAt(0)!='<' && sLin.indexOf(" ")>0) {   
						sPalabra = sLin.substring(0,sLin.indexOf(" "));
						//out.write(sLin + "\r\n");
						//if(sPalabra.compareTo(g.soloASCII(sPalabra))!=0)
						//	out.write(g.soloASCII(sPalabra) + sLin.substring(sLin.indexOf(" ")) + "\r\n");
					}
					else {  // fin del diccionario
				        model2.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numP2++)), (String) sKey1, (String) "_NO"});
						//out.write(sKey1);
						//out.write("\r\n");
						sPalabra = "====";
						break;
					}
				}
		    }
			
			//prBarra.setValue(0);
			in.close();
			//out.close();
		    return 1;
		  }
		  catch(Exception ex)
		  {
		     JOptionPane.showMessageDialog(null,ex+"" + "\nError",
				           Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			  //JOptionPane.showMessageDialog(null, "key1: " + sKey1 + " word: " + sPalabra, "info error", JOptionPane.INFORMATION_MESSAGE);
			  return 0;
		  }
	}  // fin etiquetado

	public int Etiquetar(File fEntrada, File fSalida, JTable tResult, JProgressBar prBarra, Boolean lAscii, String sMaxLabel) {
		  String sPalabra = " ";
		  String sLabel;
		  String sAux;
		  String sLin = "";
		  String sLinOut="";
		  int i=0, j=0, k=0;
		  int numP=0;
		  Boolean lFound=false;
		  BufferedReader in;
		  OutputStreamWriter out;
		  int percent=0, oldpercent=0, nLineas=0;
		  int iLabel;
		  
		  try
		  {
			// comprobamos que se han seleccionado los ficheros de entrada y salida, y de diccionario ...
			if(fEntrada == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return 0;
			}
			else if(fSalida == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E7[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return 0;
			}

			iLabel = Integer.parseInt(sMaxLabel);	
		    // abrimos el fichero de entrada ...
			in = g.TipoFichero(fEntrada);
			// preparamos el fichero de salida ..
			out = new OutputStreamWriter(new FileOutputStream(fSalida), Charset.forName("UTF-8").newEncoder() );
			//if(g.sBOM.compareTo("¿no BOM?")!=0)  out.write(g.sBOM + "\r\n");
			
			prBarra.setValue(0);
			// empezamos a leer el fichero de entrada ...
			while ((sLin = in.readLine()) != null) {  // leemos la linea
				nLineas++;
				sAux = sLin.trim();
				sAux.replaceAll(" ", "");
				if(sAux.length()==0) {
					out.write(sLin + "\r\n");
					continue; // si la línea está vacia nos la saltamos
				}
				numP=0;
				sLinOut="";
				
				while (numP<sLin.length()) {
					// rastreamos el inicio de una palabra ...
					//for(i=numP;i<sLin.length() && new String(caract).indexOf(sLin.charAt(i))>-1;i++);
					for(i=numP;i<sLin.length() && !Character.isAlphabetic(sLin.charAt(i));i++);
//System.out.println("i: " + String.valueOf(i));
					if(i>numP) sLinOut = sLinOut + sLin.substring(numP,i);  // copiamos lo que hay antes 

					if(i<sLin.length()) {
						// rastreamos su final ...
						//for(j=i;j<sLin.length() && new String(caract).indexOf(sLin.charAt(j))==-1;i++);
						for(j=i;j<sLin.length() && Character.isAlphabetic(sLin.charAt(j));j++);
//System.out.println("j: " + String.valueOf(j));
						numP=j;

						// si es una etiqueta la saltamos
						if(i==0 || sLin.charAt(i-1) != '<') {
							sPalabra = sLin.substring(i,j).toLowerCase();
							if(lAscii) sPalabra = g.soloASCII(sPalabra);
							// buscamos en palabras etiquetadas ...
							lFound=false;
							for(k=0;k<tResult.getModel().getRowCount();k++) {
								if(tResult.getValueAt(k, 1).equals(sPalabra)) {  
									lFound=true;
									break;
								}
							}
							if(lFound) {
								sLabel = (String) tResult.getValueAt(k, 2);
								if(iLabel<=sLabel.length())
									sLinOut = sLinOut + sLin.substring(i,j) + "_" + sLabel.substring(0,iLabel);
								else
									sLinOut = sLinOut + sLin.substring(i,j) + "_" + sLabel;
							} else
								sLinOut = sLinOut + sLin.substring(i,j) + "_NO";
						}
						else {  // la etiqueta la ponemos tal cual
							numP=sLin.indexOf('>')+1;
							sLinOut = sLinOut + sLin.substring(i,numP);
						}
					}
					else break;
				} // fin while
				//if(i>=sLin.length()) 
				//	sLinOut = sLinOut + sLin.substring(numP);  // copiamos resto de linea
				
				// escribimos la linea
				out.write(sLinOut + "\r\n");
		    	// actualizamos la barra de progreso
				if(g.iLineas>0) {
					percent = nLineas * 100 / g.iLineas;
					if(percent != oldpercent) {
						prBarra.setValue(percent);
						prBarra.setString(Idiomas.Processing[ventana.iId] + percent + "%");
						oldpercent = percent;
					}
				}

			}  // fin while readLine
			prBarra.setValue(100);
			prBarra.setString(Idiomas.Processing[ventana.iId] + "100%");

			in.close();
			out.close();
		    //JOptionPane.showMessageDialog(null, Idiomas.I1[ventana.iId], Idiomas.Info[ventana.iId], JOptionPane.INFORMATION_MESSAGE);
		    return 1;
		  }
		  catch(Exception ex)
		  {
		     JOptionPane.showMessageDialog(null,ex+"" + "\nError",Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			 // JOptionPane.showMessageDialog(null, "linea: " + sLin + "word: " + sPalabra, "info error", JOptionPane.INFORMATION_MESSAGE);
			 return 0;
		  }
	} // fin etiquetar
	
} // fin Label.java
