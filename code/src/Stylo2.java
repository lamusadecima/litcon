import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Stylo2 {

	public int BuscarPatron (File fIn[], String sPatern, JTable tStylo2, boolean lGen, boolean lNum) {
		String linea=""; // linea leida
		boolean lFirst=true;
		BufferedReader in=null;
		int i=0, j=0, n=0, m=0;
		int iF=0, iD=0, iOk=0, iOk2=0;; // encontrada
		String sNomSoneto = new String("");
		boolean lFound=false; // indicadores de encontrado plantilla, parte y total
		String sLabel="", sPatern2="";
		int numLinea=0;
		String sNumLin="";
		int iAnt, iPos;
		int iD2=0;
		String sDepura="";
		  
		try
		{
			// comprobamos que se ha seleccionado fichero e introducido criterio de busqueda ...
			if(fIn == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(sPatern == null || sPatern.trim().length() == 0) {
				JOptionPane.showMessageDialog(null, Idiomas.E9[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(!sPatern.trim().contains(" ") && (lGen || lNum)) {
				JOptionPane.showMessageDialog(null, Idiomas.E19[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			
			// preparamos las tablas ..
			DefaultTableModel model = new ModelConsulta(); //DefaultTableModel();
			model.addColumn(Idiomas.FileName[ventana.iId]);
			model.addColumn(Idiomas.NumLin[ventana.iId]);
			model.addColumn(Idiomas.PoemTit[ventana.iId]);
			model.addColumn(Idiomas.PoemAnt[ventana.iId]);
			model.addColumn(Idiomas.Verso[ventana.iId]);
			model.addColumn(Idiomas.PoemPos[ventana.iId]);
			tStylo2.setModel(model);
			tStylo2.getColumn(Idiomas.FileName[ventana.iId]).setMaxWidth(200);
			tStylo2.getColumn(Idiomas.NumLin[ventana.iId]).setMaxWidth(60);
			tStylo2.getColumn(Idiomas.PoemTit[ventana.iId]).setMaxWidth(500);
			DefaultTableCellRenderer center = new DefaultTableCellRenderer();
			center.setHorizontalAlignment(JLabel.CENTER);
			tStylo2.getColumn(Idiomas.Verso[ventana.iId]).setCellRenderer(center);
			DefaultTableCellRenderer right = new DefaultTableCellRenderer();
			right.setHorizontalAlignment(JLabel.RIGHT);
			tStylo2.getColumn(Idiomas.PoemAnt[ventana.iId]).setCellRenderer(right);
			DefaultTableCellRenderer left = new DefaultTableCellRenderer();
			left.setHorizontalAlignment(JLabel.LEFT);
			tStylo2.getColumn(Idiomas.PoemPos[ventana.iId]).setCellRenderer(left);
			
			for(int k=0;k<fIn.length;k++) {	// leemos el/los fichero
				//fIn = new File("H pos tagged.txt");
				in = g.TipoFichero(fIn[k]);
				StringBuffer sB = new StringBuffer();
				while ((linea = in.readLine()) != null) {
					numLinea++;	
					if(lFirst) {
						if(g.sBOM != "¿no BOM?") {
							linea = linea.substring(1);  // quitamos marca BOM
						}
						lFirst=false;
					}
					sB.append( linea);
					sB.append( "|"+String.valueOf(numLinea)+"¬" );
					if(numLinea%1000==0) break;  // Leemos en grupos de 1000 líneas
				}

				lFound=false;
				iD=0;
				g.iWordsFound=0;
				sPatern2=sPatern.trim();
				while (iD<sB.length()) {
					// cogemos la etiqueta 
					if(sPatern2.indexOf(" ")>-1) sLabel=sPatern2.substring(0,sPatern2.indexOf(" "));
					else if (sPatern2.length()>0) sLabel=sPatern2;
					sPatern2=sPatern2.substring(sPatern2.indexOf(sLabel)+sLabel.length());
					sPatern2=sPatern2.trim();

					iF=sB.substring(iD).indexOf("_" + sLabel);
					//if(iF==-1) iF=sB.substring(iD).indexOf(" " + sLabel);  // lo buscamos tb con blanco
					if(iF==-1 && !lFound) { // no se encuentra y es el primero 
						if(linea==null)  break;  // no hay mas lineas, nos salimos
						else {
							iD=sB.length()-2; // provocamos que se lean más lineas
							sPatern2=sPatern.trim();
						}
					}
					else if(iF==-1 && lFound) { // no se encuentra y no es el primero, rompe busqueda y vuelve a empezar
						lFound=false;
						sPatern2=sPatern.trim();
					}
					else if(iF!=-1 && (lFound && (iF!=sB.substring(iD).indexOf("_") || sB.substring(iD,iD+iF).contains(".")))) { 
						// se encuentra pero no es el siguiente o hay un punto en medio
						lFound=false;  // rompe busqueda y vuelve a empezar
						sPatern2=sPatern.trim();
						iD=iOk + sB.substring(iOk).indexOf(" "); 
					}
					else {  // encontrado
						if(!lFound) iOk=iD+sB.substring(iD,iD+iF).lastIndexOf(" ")+1; // es el primero 
						lFound=true;
						iD=iD+iF+sLabel.length();
						//	if(sLabel.equals("DE0CN0"))
						//	sDepura=sB.substring(iD-100,iF);
						if(sPatern2.trim().length()==0) {  // se ha acabado la plantilla, grabamos 
							lFound=false;
							for(i=iD;Character.isLetter(sB.charAt(i+1)) || Character.isDigit(sB.charAt(i+1)) || sB.charAt(i+1)=='+';i++);
							iOk2=i+1;

							if((!lGen && !lNum) || !Ruptura(sB.substring(iOk, iOk2), lGen, lNum)) {

								// preparamos la linea a grabar
								if(sB.substring(0,iOk).lastIndexOf("|")>-1 && sB.substring(0,iOk).lastIndexOf("¬")>-1) 
									sNumLin = sB.substring(sB.substring(0,iOk).lastIndexOf("|")+1,sB.substring(0,iOk).lastIndexOf("¬"));
								else 
									sNumLin="1";
								
								//if(sB.substring(0,iOk).lastIndexOf("<")>-1 && sB.substring(0,iOk).lastIndexOf(">")>-1) 
								if((n=sB.substring(0,iOk).lastIndexOf("#"))>-1 && sB.substring(0,n-1).lastIndexOf("#")>-1) 
									//sNomSoneto = sB.substring(sB.substring(0,iOk).lastIndexOf("<")+1,sB.substring(0,iOk).lastIndexOf(">"));
									sNomSoneto = sB.substring(sB.substring(0,n-1).lastIndexOf("#")+1,sB.substring(0,iOk).lastIndexOf("#"));
								//else 
								//	sNomSoneto = "";
								//			if(sNumLin.contains("649"))
								//				sDepura="ok";

								iAnt=iOk-1;
								for(i=0;i<8;i++) {  // 8 etiquetas anteriores
									j=sB.substring(0,iAnt-1).lastIndexOf("_");
									if(j>0 && !sB.substring(j,iAnt).contains("#"))
										iAnt=sB.substring(0,j).lastIndexOf(" "); 
									else break;
								}
								iPos=iOk2;
								for(i=0;i<8;i++) {  // 8 etiquetas posteriores
									j=sB.substring(iPos).indexOf("_");
									if(j>0 && !sB.substring(iPos,iPos+j).contains("#"))
										iPos=iPos+j+1; 
									else break;
								}
								iPos=iPos+sB.substring(iPos).indexOf(" ");
								if(iPos<=iOk2) iPos=iOk2+1;

								StringBuilder sLin = new StringBuilder();
								sLin.append("<html>");
								sLin.append(g.ColorearEtiquetas(sB.substring(iOk, iOk2)));
								sLin.append("</html>");
								model.addRow(new Object[]{fIn[k].getName(), (String) String.format("%1$-6s",sNumLin), String.format("%1$-15s",sNomSoneto.toString()), g.quitarNum(g.quitarEtiquetasNum(sB.substring(iAnt,iOk))), g.quitarNum(sLin.toString()), g.quitarNum(g.quitarEtiquetasNum(sB.substring(iOk2,iPos)))});					
								g.iWordsFound++;
								//System.out.println(sNumLin);
							}

							sPatern2=sPatern.trim();
							// ahora nos posicionamos en el segundo para seguir buscando ...
							if(sB.substring(iOk).indexOf(" ")>-1)
								iD=iOk + sB.substring(iOk).indexOf(" "); 
						}  // fin de grabamos
					}  // fin de if encontrado  

					if(iD>sB.length()-20000 && !lFound && linea!=null) { // leemos mas lineas si no estamos en medio de una busqueda
						iD2=sB.substring(0,iD).lastIndexOf("<");
						if(iD2==-1)
							iD2=iD-1000;
						sB.delete(0,iD2);  // borramos hasta el poema actual o hasta un poco antes (1000 chars)
						while ((linea = in.readLine()) != null) {
							sB.append(linea);
							numLinea++;	
							sB.append( "|"+String.valueOf(numLinea)+"¬" );
							if(numLinea%1000==0) break;
						}
						iD=iD-iD2;
					} 
				}  // fin del while lectura lineas			
				// JOptionPane.showMessageDialog(null,"|" + String.valueOf(iD) + "| ", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
				in.close();
			}
			//fTemp.delete();
			return 1;		
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" |" + String.valueOf(iD) + "| " + sNumLin + " \nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}

	} // fin

	public int BuscarEstad (File fIn[], String sPatern, JTable tStylo2, boolean lAbs) {
		String linea=""; // linea leida
		boolean lFirst=true;
		BufferedReader in=null;
		int iF=0; // encontrada
		String sLabel="";
		int numLinea=0;
		int nPoe=0;  // número de líneas consecutivas, mínimo dos para ser poema
		int cont=0, tcont=0;  // veces que aparece la etiqueta en el poema y total fichero
		String sCount="";
		Map<String, Integer> mWord;
		int iPoe=0, tPoe=0;   // palabras del poema y total palabras
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.CEILING);
		
		try
		{
			// comprobamos que se ha seleccionado fichero e introducido criterio de busqueda ...
			if(fIn == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(sPatern == null || sPatern.trim().length() == 0) {
				JOptionPane.showMessageDialog(null, Idiomas.E9[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(sPatern.trim().contains(" ")) {
				JOptionPane.showMessageDialog(null, Idiomas.E19[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			
			// preparamos las tablas ..
			DefaultTableModel model = new ModelConsulta(); //DefaultTableModel();
			model.addColumn(Idiomas.Ocurren[ventana.iId]);
			model.addColumn(Idiomas.Frecuency[ventana.iId]);
			tStylo2.setModel(model);
			//tStylo2.getColumn(Idiomas.NumLin[ventana.iId]).setMaxWidth(80);
			//DefaultTableCellRenderer center = new DefaultTableCellRenderer();
			//center.setHorizontalAlignment(JLabel.CENTER);
			//tStylo2.getColumn(Idiomas.Verso[ventana.iId]).setCellRenderer(center);
			
			for(int i=0;i<fIn.length;i++) {	// leemos el/los fichero
				sLabel=sPatern.replace("_","").trim();
				in = g.TipoFichero(fIn[i]);
				mWord = new HashMap<String, Integer>();
				g.numWords2=0;
				while ((linea = in.readLine()) != null) {
					numLinea++;
					if(lFirst) {
						if(g.sBOM != "¿no BOM?") {
							linea = linea.substring(1);  // quitamos marca BOM
						}
						lFirst=false;
					}
					// controlamos cambio de verso
					if(linea.replaceAll(" ", "").length()==0) { // linea vacia
						if(cont>0 && nPoe>1) { // solo grabamos si es un poema y no un titulo
							if(lAbs) g.add((String) String.format("%1$6s",cont), mWord);
							else g.add((String) df.format((float) cont*100/iPoe), mWord);

							tcont+=cont;
							tPoe+=iPoe;
						}
						cont=0;
						nPoe=0;
						iPoe=0;
					}
					else nPoe++;
					g.numWords2 = g.numWords2 + linea.split("_",-1).length-1; // contamos palabras

					// ahora contamos palabras 	
					// quitamos comas, puntos y coma, punto, etc
					linea = linea.replaceAll(g.sPuntua," ").trim();
					String []parts = linea.split(" ");
					for( String w : parts)
					{
						if(!g.isNumeric(w) && w.indexOf("<") == -1) {
							//if(!w.contains("_")) {
							//	if(w.contains("en-tanto"))
							//		System.out.println("palabra sin guion1 " + parts);
							//	System.out.println("palabra sin guion " + w);
							//}
							iPoe++;
						}      
					}

					while ((iF=linea.indexOf("_" + sLabel))>-1) {  // contamos la etiqueta ...
						cont++;
						linea=linea.substring(iF+1);
					}
				}
				if(nPoe>1) {
					if(lAbs) g.add((String) String.format("%1$6s",cont), mWord);
					else g.add((String) df.format((float) cont*100/iPoe), mWord);
					tcont+=cont;
					tPoe+=iPoe;
				}

				// ahora pasamos las palabras a la tabla
				//mWord = g.sortByValues(mWord, 1);  // por valores descendente
				Set set = mWord.entrySet();
				Iterator iterator = set.iterator();
				while(iterator.hasNext()) {
					Map.Entry mentry = (Map.Entry) iterator.next();
					sCount = mentry.getKey().toString();
					Vector<Object> data = new Vector<Object>();
					data.add(sCount);
					data.add((String) String.format("%1$6s",String.valueOf(mentry.getValue()))); 
					//	data.add((String) df.format((float) (int) mentry.getValue()*1000/nTotal));
					model.addRow(data);
				}

				g.numWords=tcont;
				//g.numWords2=tPoe;
				//System.out.println(sNumLin);

				in.close();
			}  // cierre bucle for lectura fichero/s
			//fTemp.delete();
			return 1;		
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" |" + String.valueOf(numLinea) + "| " + " \nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}

	} // fin
	
	
	public static boolean Ruptura(String sCadena, boolean lGen, boolean lNum) {
		char cGen1=' ', cGen2=' ', cNum1=' ', cNum2=' ';
		int i,j;
		String sL1="", sL2="";
		
		// cogemos la primera etiqueta que es la que manda ...
    	j=sCadena.indexOf("_");
   		for(i=j+1; i<sCadena.length() && (Character.isLetter(sCadena.charAt(i)) || Character.isDigit(sCadena.charAt(i)) || sCadena.charAt(i)=='+'); i++);  // donde acaba la etiqueta
   		sL1 = sCadena.substring(j+1, i);
   		sCadena = sCadena.substring(i+1);

		// ahora ruptura por género o número ...
		if(sL1.length()<4 || (!lGen && !lNum)) return false;  // si no se puede ver genero/numero o no se piden, no se rompe
		else {  // guardamos los datos de la primera etiqueta
			if(sL1.charAt(0) == 'A' || sL1.charAt(0) == 'D' || sL1.charAt(0) == 'P') {
				cGen1 = sL1.charAt(3);
				cNum1= sL1.charAt(4);
			}
			else if(sL1.charAt(0) == 'N') {
				cGen1 = sL1.charAt(2);
				cNum1= sL1.charAt(3);
			}
			else if(sL1.charAt(0) == 'V') {
				cGen1 = sL1.charAt(6);
				cNum1= sL1.charAt(5);
			}
		}

		// ahora vamos cogiendo el resto de etiquetas y comprobando ...
		while ((j=sCadena.indexOf("_"))>-1) {
			for(i=j+1; i<sCadena.length() && (Character.isLetter(sCadena.charAt(i)) || Character.isDigit(sCadena.charAt(i)) || sCadena.charAt(i)=='+'); i++);  // donde acaba la etiqueta
			sL2 = sCadena.substring(j+1, i);
			sCadena = sCadena.substring(j+1);

			if(sL2.charAt(0) == 'A' || sL2.charAt(0) == 'D' || sL2.charAt(0) == 'P') {
				cGen2 = sL2.charAt(3);
				cNum2= sL2.charAt(4);
			}
			else if(sL2.charAt(0) == 'N') {
				cGen2 = sL2.charAt(2);
				cNum2= sL2.charAt(3);
			}
			else if(sL2.charAt(0) == 'V') {
				cGen2 = sL2.charAt(6);
				cNum2= sL2.charAt(5);
			}

			if(lGen) { // control de género 
				if((cGen1 == 'M' && cGen2 == 'F') || (cGen1 == 'F' && cGen2 == 'M')) return true;
			}
			if(lNum) { // control de número 
				if((cNum1 == 'P' && cNum2 == 'S') || (cNum1 == 'S' && cNum2 == 'P')) return true;
			} 
		}
		
		return false;
	}	
	
	// BuscarPatron2 funcion obsoleta que no se usa ...
	public int BuscarPatron2 (File fIn, String sPatern, JTable tStylo2) {
		String linea=""; // linea leida
		String linea2=""; // linea auxiliar
		String lineaAnt=""; // linea anterior
		String lineaPos=""; // linea posterior
		boolean lFirst=true;
		BufferedReader in=null;
		int i=0, j=0;
		int iF=0, iD=0, iOk=0, iOk2=0;; // encontrada
		String sNomSoneto = new String("");
		String sAux;
		String sPosibleSoneto = new String("");
		int iNumSoneto=0;
		String sUltLinea="";
		boolean lFound=false, lOk=false; // indicadores de encontrado plantilla, parte y total
		boolean lSalta=false;   
		String sLabel="", sPatern2="";
		int numLinea=0;
		  
		try
		{
			// comprobamos que se ha seleccionado fichero e introducido criterio de busqueda ...
/*			if(fIn == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(sPatern == null || sPatern.trim().length() == 0) {
				JOptionPane.showMessageDialog(null, Idiomas.E9[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
*/
			// preparamos las tablas ..
			DefaultTableModel model = new ModelConsulta(); //DefaultTableModel();
			model.addColumn(Idiomas.NumLin[ventana.iId]);
			model.addColumn(Idiomas.PoemTit[ventana.iId]);
			model.addColumn(Idiomas.PoemAnt[ventana.iId]);
			model.addColumn(Idiomas.Verso[ventana.iId]);
			model.addColumn(Idiomas.PoemPos[ventana.iId]);
			tStylo2.setModel(model);
			tStylo2.getColumn(Idiomas.NumLin[ventana.iId]).setMaxWidth(80);

			// abrimos el fichero
			fIn = new File("H pos tagged.txt");
			in = g.TipoFichero(fIn);
			// ahora nos posicionamos al principio del fichero ...
			g.numWords=0;
			lFound=false;
			lOk=false;
			while (true) {
				if(!lSalta) {
					if((linea = in.readLine()) == null) break;
				}
				else lSalta=false;
				//System.out.println("linea: " + linea.toString());
				linea = linea.trim();
				if(lFirst) {
					if(g.sBOM != "¿no BOM?") {
						linea = linea.substring(1);  // quitamos marca BOM
					}
					lFirst=false;
				}

				// controlamos nombre de soneto
		    	sAux = linea;
		    	sAux.replaceAll(" ", "");
		    	if(sAux.trim().length()==0) {
		    		// si la línea está vacia es que la anterior era el nombre del soneto
		    		sNomSoneto = sPosibleSoneto.trim();
		    		sNomSoneto = g.quitarEtiquetas(sNomSoneto); // le quito etiquetas
		    	}
		    	else {
		    		sPosibleSoneto = linea;
		    	}
				numLinea++;	
		    	if(linea=="" || linea.length()==0) {  // si está en blanco la saltamos
		    		lFound=false; // por si el patrón estaba a medias
		    		continue;
		    	}
				//g.numWords = g.numWords + linea.split("_",-1).length-1; // contamos palabras

				// buscamos el patron ...
				linea2=linea;
				if(!lFound) sPatern2=sPatern.trim(); 
				iD=0;
				while(linea2.substring(iD).indexOf("_")>-1) {
					// cogemos la etiqueta 
					if(sPatern2.indexOf(" ")>-1) sLabel=sPatern2.substring(0,sPatern2.indexOf(" "));
					else if (sPatern2.length()>1) sLabel=sPatern2;
					sPatern2=sPatern2.substring(sPatern2.indexOf(sLabel)+sLabel.length());
					sPatern2=sPatern2.trim();

					iF=linea2.substring(iD).indexOf("_" + sLabel);
					if(iF==-1 || (lFound && iF!=linea2.substring(iD).indexOf("_"))) { // no se encuentra o no es el primero
						lFound=false;
						break;
					}
					else {  // encontrado
						if(!lFound) iOk=linea2.substring(iD,iF).lastIndexOf(" ")+1; // es el primero 
						lFound=true;
						//linea2=linea2.substring(iF+sLabel.length()+1);
						iD=iD+iF+sLabel.length()+1;
						if(sPatern2.trim().length()==0) {  // se ha acabado la plantilla 
							iOk2=iD;
							lOk=true;
							break; 
						}
					}
				}   // se acabó la línea

				// si no se ha acabado el patrón miramos en la línea siguiente, si se ha acabado grabamos
				if(lOk) {
 					// preparamos la linea a grabar
 					//linea = linea.replaceAll(g.sPuntua,"").trim();
			    	StringBuilder sLin = new StringBuilder();
			        sLin.append("<html>");
			        sLin.append(g.ColorearEtiquetas(linea.substring(iOk, iOk2)));
	                sLin.append("</html>");	
                	model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea-1)), String.format("%1$-25s",sNomSoneto.toString()), lineaAnt + " " + linea.substring(0,iOk), sLin.toString(), linea.substring(iOk2) + " " + lineaPos});					
                	lOk=false;
                	lFound=false;
                	g.iWordsFound++;
                	if((linea = in.readLine()) == null) {
                		lineaPos = linea;
                		lSalta=true;
                	}
                	else lineaPos="";
				}
				
				lineaAnt=linea;
			}  // fin del while lectura lineas			

			return 1;		
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" " + linea + " " + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}		

	} // fin
	
}
