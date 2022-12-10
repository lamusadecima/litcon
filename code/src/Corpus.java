import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

public class Corpus {

	public int ObtenerCorpus (File fIn, File fOut, String sMax) {
		String linea=""; // linea leida
		boolean lFirst=true;
		BufferedReader in=null;
		String sAux, sX, sNomSoneto;
		boolean lPoema=false;
		int iMax;
		OutputStreamWriter out;
		ArrayList<Integer> elegidos = new ArrayList<Integer>();
		boolean lSalir=false;

		try
		{
			// comprobamos que se ha seleccionado fichero de entrada, de salida o máx palabras ...
			if(fIn == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(fOut == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E7[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(sMax == null || sMax.trim().length() == 0) {
				JOptionPane.showMessageDialog(null, Idiomas.E16[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}

			try {  // comprobamos que si hay algo introducido es un número 
				iMax=Integer.valueOf(sMax);
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(null, Idiomas.E16[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}

			// abrimos el fichero
			in = g.TipoFichero(fIn);
			// ahora nos posicionamos al principio del fichero ...
			g.numWords=0;
			g.numPoet=0;
			g.iLineas=0;
			sAux=null;
			sNomSoneto=null;
			while ((linea = in.readLine()) != null) {  // bucle lineas
				//System.out.println("linea: " + linea.toString());
				if(lFirst) {
					if(g.sBOM != "¿no BOM?") {
						linea = linea.substring(1);  // quitamos marca BOM
					}
					lFirst=false;
				}
				linea = linea.trim();

				// contamos sonetos
				sAux = linea;
				sAux.replaceAll(" ", "");
				if(sAux.trim().length()==0) {
					// si la línea está vacia es posible que la siguiente empiece un soneto 
					lPoema=true;
				}
				else if (lPoema && sAux.trim().substring(0).indexOf("#")==-1) {  // si la anterior estaba vacia y ésta no empieza por #
					g.numPoet++;
					lPoema=false;
				}

				g.iLineas++;

				if(linea=="" || linea.length()==0) {  // si está en blanco la saltamos
					continue;
				}
				// ahora contamos palabras si no son etiquetas
				if(linea.indexOf("#") == -1) {
					// quitamos comas, puntos y coma, punto, etc
					linea = linea.replaceAll(g.sPuntua," ").trim();
					String []parts = linea.split(" ");
					for( String w : parts)
					{
						if(!g.isNumeric(w)) { // si no es número
							g.numWords++;
							//System.out.println(w);
							//if(w.indexOf("'") > 0 && !w.endsWith("'") ) g.numWords++;
							if(w.length() > 1 && w.indexOf("'",1) > 0 && w.indexOf("'",1) < w.length()-1) g.numWords++;
						}      
					}
				}
			}  // fin del while lectura lineas			

			if(g.numWords<iMax) {
				JOptionPane.showMessageDialog(null, Idiomas.E17[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			
			// preparamos el fichero de salida ..
			out = new OutputStreamWriter(new FileOutputStream(fOut), Charset.forName("UTF-8").newEncoder() );
			// empezamos el sorteo ...
			g.numWords2=0;
			g.iLineas2=0;
			g.numPoet2=0;
			Random randomGenerator = new Random();
			while(g.numWords2<iMax) {
				int iPoem = randomGenerator.nextInt(g.numPoet)+1;
				// si ya ha salido lo saltamos
				lSalir=false;
				for (int i : elegidos) {
	                if(i==iPoem)
	                	lSalir=true;
	                else
	                	elegidos.add(iPoem);
	            }
				if(lSalir) continue;
				
				// abrimos el fichero y buscamos el poema iPoem
				in = g.TipoFichero(fIn);
				int nPoem=0;
				sAux=null;
				boolean lSigue=true;
				while ((linea = in.readLine()) != null && lSigue) {  // bucle lineas
					//linea = linea.trim();
					if(lFirst) {
						if(g.sBOM != "¿no BOM?") {
							linea = linea.substring(1);  // quitamos marca BOM
						}
						lFirst=false;
					}

					// contamos sonetos
					sAux = linea;
					sAux.replaceAll(" ", "");
					if(sAux.trim().length()==0) {
						// si la línea está vacia es posible que la siguiente empiece un soneto 
						lPoema=true;
					}
					else if((lPoema && sAux.trim().substring(0).indexOf("#")>-1)) {
						sNomSoneto=linea;
					}
					else if (lPoema && sAux.trim().substring(0).indexOf("#")==-1) {  // si la anterior estaba vacia y ésta no empieza por #
						nPoem++;
						lPoema=false;
					}

					if(nPoem==iPoem) {  // es el poema con suerte
						g.numPoet2++;
						// escribimos ..
						out.write(" " + "\r\n");	// linea en blanco
						out.write(sNomSoneto + "\r\n");	// nombre del soneto					
						out.write(" " + "\r\n");	// linea en blanco	
						out.write(linea + "\r\n");
						g.iLineas2=g.iLineas2+4;
						linea = linea.replaceAll(g.sPuntua," ").trim();
						String []parts = linea.split(" ");
						for( String w : parts)
						{
							if(!g.isNumeric(w) && w.indexOf("#") == -1) {
								g.numWords2++;
							}      
						}
						while ((linea = in.readLine()) != null && linea.trim()!="" && linea.trim().length()!=0) {  // bucle poema
							out.write(linea + "\r\n");
							linea = linea.replaceAll(g.sPuntua," ").trim();
							String []parts2 = linea.split(" ");
							for( String w : parts2)
							{
								if(!g.isNumeric(w) && w.indexOf("#") == -1) {
									g.numWords2++;
								}      
							}
							g.iLineas2++;
						}
						lSigue=false;
						//g.iLineas2=g.iLineas2+2;
					} // fin if poema con suerte

				}  // fin del while lectura lineas		
			}  // salida por numero máximo de lineas superadas

			in.close();
			out.close();
			return 1;		
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" " + linea + " " + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}		

	} // fin
	
} // fin clase Corpus


