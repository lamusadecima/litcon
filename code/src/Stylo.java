import java.io.BufferedReader;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class Stylo {
	public int BuscarRelacion (File fSt1, String sLabel1, String sLabel2, JTable tStylo, boolean lNomSoneto, String sSignos, boolean lGen, boolean lNum, boolean lCont, String sCont) {
		String linea=""; // linea leida
		String linea2=""; // linea auxiliar
		String lineaAnt=""; // linea anterior
		boolean lFirst=true;
		BufferedReader in=null;
		int i=0, j=0;
		int iF=0, iF2=0; // encontrada
		int iL=0, iL2=0;  // inicio primera y segunda etiqueta
		int iLf=0;  // fin primera etiqueta
		int iR=0, iR2=0;  // marca inicio y fin de la relacion
		int iX1=0, iX2=0;  // inicio y fin zona entre palabras etiquetadas
		String sNomSoneto = new String("");
		String sAux;
		String sPosibleSoneto = new String("");
		int iNumSoneto=0;
		String sUltLinea="";
		int[] iEst=new int[20];
		String sTipo="";
		boolean lFound=false;
		String sL1="", sL2="";
		int iCont=0;
		  
		try
		{
			// comprobamos que se ha seleccionado fichero e introducido criterio de busqueda ...
			if(fSt1 == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(sLabel1 == null || sLabel1.trim().length() == 0 || sLabel2 == null || sLabel2.trim().length() == 0) {
				JOptionPane.showMessageDialog(null, Idiomas.E9[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}

			try {  // comprobamos que si hay algo introducido es un número 
				if(lCont && sCont.trim().length()>0) iCont=Integer.valueOf(sCont);
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(null, Idiomas.E16[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}

			// Por si no llevan guión bajo
			if(sLabel1.charAt(0)!='_') sLabel1 = "_" + sLabel1;
			if(sLabel2.charAt(0)!='_') sLabel2 = "_" + sLabel2;

			// preparamos las tablas ..
			DefaultTableModel model = new ModelConsulta(); //DefaultTableModel();
			model.addColumn(Idiomas.NumLin[ventana.iId]);
			if (lNomSoneto) {
				model.addColumn(Idiomas.PoemTit[ventana.iId]);
				model.addColumn(Idiomas.NumVers[ventana.iId]);
			}			
			model.addColumn(Idiomas.Relation[ventana.iId]);
			//model.addColumn(Idiomas.Ocurren[ventana.iId]);
			tStylo.setModel(model);
			tStylo.getColumn(Idiomas.NumLin[ventana.iId]).setMaxWidth(80);
			if (lNomSoneto) {
				tStylo.getColumn(Idiomas.PoemTit[ventana.iId]).setPreferredWidth(200);
				//tStylo.getColumn(Idiomas.PoemTit[ventana.iId]).setMinWidth(300);
				tStylo.getColumn(Idiomas.NumVers[ventana.iId]).setMaxWidth(80);
				tStylo.getColumn(Idiomas.Relation[ventana.iId]).setPreferredWidth(500);
			}			
			//tStylo.getColumn(Idiomas.Ocurren[ventana.iId]).setMaxWidth(120);					

			// abrimos el fichero
			in = g.TipoFichero(fSt1);
			// ahora nos posicionamos al principio del fichero ...
			int numLinea=0;
			for(i=0;i<12;i++) iEst[i]=0;
			g.numWords=0;
			iF=-1;
			//if(sSignos=="") sSignos="";
			while ((linea = in.readLine()) != null) {
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
		    		iNumSoneto=0;
		    	}
		    	else {
		    		sPosibleSoneto = linea;
		    		iNumSoneto++; 
		    	}

				numLinea++;	
		    	if(linea=="" || linea.length()==0) {  // si está en blanco la saltamos
		    		iF=-1;
		    		lFound=false;
		    		continue;
		    	}
				g.numWords = g.numWords + linea.split("_",-1).length-1; // contamos palabras
//if(numLinea==3287)
//	j=0;
				// para las estadísticas ...
				linea2=linea;
				for(i=0;i<linea2.length() && (j=linea2.indexOf("_"))>-1;i++) {
					if(sTipo.contains(linea2.substring(j+1,j+2))) iEst[sTipo.indexOf(linea2.substring(j+1,j+2))]++;
					else { 
						sTipo = sTipo + linea2.substring(j+1,j+2);
						iEst[sTipo.length()-1]++;
					}
					linea2=linea2.substring(j+1);
				}
				
		    	// motor de busqueda
		    	i=0;
				j=0;

				// primero miramos si hay relación con el último de la línea anterior ...
				// en caso de lCont no lo miramos ya que está incluido
 				if(!lCont && sUltLinea.toUpperCase().indexOf(sLabel1.toString(),i)>-1 && linea.indexOf(" ")>0 && 
					linea.toUpperCase().substring(0,linea.indexOf(" ")).indexOf(sLabel2.toString())>-1) {

					// buscamos principios y final de B 
					iF2=linea.toUpperCase().indexOf(sLabel2.toString());
 					for(iX2=iF2;iX2>0 && linea.charAt(iX2) !=' ' && !g.sPuntua.contains(linea.substring(iX2,iX2+1));iX2--);
					for(iR2=iX2+1;iR2 < linea.length() && linea.charAt(iR2)!=' ' && !g.sPuntua.contains(linea.substring(iR2,iR2+1));iR2++);
					for(iL2=iX2;iL2 < linea.length() && linea.charAt(iL2)!='_';iL2++);
					sL2=linea.substring(iL2, iR2).replaceAll(g.sPuntua,"");
 					// preparamos la linea a grabar
 					//linea = linea.replaceAll(g.sPuntua,"").trim();
					if(!Ruptura(iX1, iX2, linea, lineaAnt, sSignos, lGen, lNum, sL1.substring(1), sL2.substring(1))) {
						//sL1= sUltLinea.substring(0, sUltLinea.indexOf("_"));
 						StringBuilder sLin = new StringBuilder();
						sLin.append("<html>");
						sLin.append(sUltLinea.substring(0, sUltLinea.indexOf("_")));
						sLin.append("<span style=\"color: red;\">");
						sLin.append(sUltLinea.substring(sUltLinea.indexOf("_")));
						sLin.append("</span> ");
						sLin.append(linea.substring(0, linea.indexOf("_")));
						sLin.append("<span style=\"color: red;\">");
						sLin.append(linea.substring(linea.indexOf("_"), linea.indexOf(" ")).replaceAll(g.sPuntua,""));
						sLin.append("</span>");
						sLin.append("</html>");	
						if (lNomSoneto) {
							model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea-1)), String.format("%1$-25s",sNomSoneto.toString()), (String) String.format("%1$-6s",String.valueOf(iNumSoneto)), sLin.toString()});
						} else {
							model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea-1)), sLin.toString()});
						}
					}
				}
				
 				// ahora, si en la linea anterior encontramos A pero no B, vemos si está en esta línea
 				if(lCont && iF>-1 && !lFound) {
					iF2=linea.toUpperCase().indexOf(sLabel2.toString());
					if(iF2>-1 && !linea.substring(0,iF2).contains(sLabel1) &&
						(sCont.trim().length()==0 || linea.substring(0,iF2).split("_",-1).length == iCont)) {
						// si encontramos B y no hay otro A en medio y no hay más de sCont palabras en medio
						// buscamos principios y final de B 
						for(iX2=iF2;iX2>0 && linea.charAt(iX2) !=' ' && !g.sPuntua.contains(linea.substring(iX2,iX2+1));iX2--);
						for(iR2=iX2+1;iR2 < linea.length() && linea.charAt(iR2)!=' ' && !g.sPuntua.contains(linea.substring(iR2,iR2+1));iR2++);
						for(iL2=iX2;iL2 < linea.length() && linea.charAt(iL2)!='_';iL2++);
						sL2=linea.substring(iL2, iR2).replaceAll(g.sPuntua,"");
						// buscamos final de etiqueta A ..
						//for(iLf=iL+1;iLf < lineaAnt.length() && lineaAnt.charAt(iLf)!=' ' && !g.sPuntua.contains(lineaAnt.substring(iLf,iLf+1));iLf++);
						//sL1=linea.substring(iL, iLf+1).replaceAll(g.sPuntua,"");
						
						if(!Ruptura(iX1, iX2, linea, lineaAnt, sSignos, lGen, lNum, sL1.substring(1), sL2.substring(1))) {
							// preparamos la linea a grabar 
							StringBuilder sLin = new StringBuilder();
							sLin.append("<html>");
							sLin.append(lineaAnt.substring(iR, iL));
							sLin.append("<span style=\"color: red;\">");
							sLin.append(lineaAnt.substring(iL, iLf));  
							sLin.append("</span> ");
							sLin.append(lineaAnt.substring(iLf));  
							sLin.append(linea.substring(iX2, iL2));
							sLin.append("<span style=\"color: red;\">");
							sLin.append(sL2);
							sLin.append("</span>");
							sLin.append("</html>");	
							if (lNomSoneto) {
								model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea-1)), String.format("%1$-25s",sNomSoneto.toString()), (String) String.format("%1$-6s",String.valueOf(iNumSoneto)), String.format("%1$-35s",sLin.toString())});
							} else {
								model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea-1)), sLin.toString()});
							}
						}
		                i=iX2;
					}  // fin de encontrada B,
 				} 
 				
				while(i<linea.toString().length()) {
					//  buscamos la etiqueta/palabra 1 ... 
					iF=linea.toUpperCase().indexOf(sLabel1.toString(),i);
					if(iF>-1) {  // encontrada ...
						// tiramos para atrás para encontrar donde empieza la relación (inicio de A) ...
						for(iR=iF;iR >0 && linea.charAt(iR) !=' ';iR--);
						// vemos donde empieza y acaba la etiqueta A
						for(iL=iR;iL < linea.length() && linea.charAt(iL)!='_';iL++);
						for(iLf=iL+1;iLf < linea.length() && linea.charAt(iLf)!=' ' && !g.sPuntua.contains(linea.substring(iLf,iLf+1));iLf++);
						if(iLf+1<linea.length())
							sL1=linea.substring(iL, iLf+1).replaceAll(g.sPuntua,"");
						else
							sL1=linea.substring(iL, iLf).replaceAll(g.sPuntua,"");
							
						// buscamos donde acaba A (iX1) y donde empieza la siguiente etiqueta (iX2) ...
						for(iX1=iF+1;iX1 < linea.length() && linea.charAt(iX1)!=' ' && !g.sPuntua.contains(linea.substring(iX1,iX1+1));iX1++);
						for(iX2=iX1;iX2 < linea.length() && (linea.charAt(iX2)==' ' || g.sPuntua.contains(linea.substring(iX2,iX2+1)));iX2++);

						// si no hay mas palabras/etiquetas nos salimos
						if(iX2==linea.length()) break;
						
						// buscamos final de la siguiente etiqueta (posible B) 
						for(iR2=iX2+1;iR2 < linea.length() && linea.charAt(iR2)!=' ';iR2++);

						// vemos si encontramos B ...
						lFound=false;
						if(linea.substring(iX2, iR2).toUpperCase().contains(sLabel2)) lFound=true; // si la siguiente coincide, encontrada
						else if(lCont) {  // en caso de continuidad miramos si B está mas adelante ...
							iF2=linea.toUpperCase().indexOf(sLabel2.toString(),iR2);
							if(iF2>-1 && !linea.substring(iR2,iF2).contains(sLabel1) &&
								(sCont.trim().length()==0 || linea.substring(iR2,iF2).split("_",-1).length == iCont)) {
								// si encontramos B y no hay otro A en medio y no hay más de sCont palabras en medio
								lFound=true;
								// buscamos principios y final de B 
								for(iX2=iF2;iX2>0 && linea.charAt(iX2) !=' ' && !g.sPuntua.contains(linea.substring(iX2,iX2+1));iX2--);
								for(iR2=iX2+1;iR2 < linea.length() && linea.charAt(iR2)!=' ' && !g.sPuntua.contains(linea.substring(iR2,iR2+1));iR2++);
							}
						}
						
						//for(iL2f=iL2+1;iL2f < linea.length() && linea.charAt(iL2f)!=' ';iL2f++);

						// buscamos la etiqueta de B ...	
						for(iL2=iX2;iL2 < linea.length() && linea.charAt(iL2)!='_';iL2++);
						
						// si no hay mas palabras/etiquetas nos salimos
						if(iL2==linea.length()) break;

						// si hay siguiente etiqueta, seguimos ...
						if(lFound) {
							sL2=linea.substring(iL2, iR2).replaceAll(g.sPuntua,"");

							if(!Ruptura(iX1, iX2, linea, null, sSignos, lGen, lNum, sL1.substring(1), sL2.substring(1))) {
								// preparamos la linea a grabar
								StringBuilder sLin = new StringBuilder();
								sLin.append("<html>");
								sLin.append(linea.substring(iR, iL));
								sLin.append("<span style=\"color: red;\">");
								sLin.append(linea.substring(iL, iLf)); // sL1);
								sLin.append("</span>");
								sLin.append(linea.substring(iLf, iL2));
								//sLin.append(linea.substring(iX2, iL2));
								sLin.append("<span style=\"color: red;\">");
								sLin.append(sL2);
								sLin.append("</span>");
								sLin.append("</html>");	
								//model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea)), linea.substring(iR,iR2)});
								if (lNomSoneto) {
									model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea)), String.format("%1$-25s",sNomSoneto.toString()), (String) String.format("%1$-6s",String.valueOf(iNumSoneto)), String.format("%1$-35s",sLin.toString())});
								} else {
									model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea)), sLin.toString()});
								}
							}
						}
						i=iX2; // la próxima vez buscaremos a partir de la segunda palabra/etiqueta ...
		    		}  // fin de encontrado A (iF>-1)
		    		else break; // si ya no encontramos más nos salimos
		    	}  // fin del while estudio linea
				
				// guardamos la ultima palabra de la linea por si relaciona con la primera de la siguiente
				if(linea.lastIndexOf(' ')>-1 && !sSignos.contains(linea.substring(linea.length()-1))) 
					sUltLinea = linea.substring(linea.lastIndexOf(' ')).replaceAll(g.sPuntua,"");
				else
					sUltLinea="";
				lineaAnt=linea;
			}  // fin del while lectura lineas			

			// guardamos estadísticas ...
			g.sTemp=Idiomas.Estad[ventana.iId];
			for(i=0;i<sTipo.length();i++) {
				g.sTemp = g.sTemp + sTipo.substring(i,i+1) + " -> " + String.valueOf(iEst[i])  + ", ";
			}

			return 1;		
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" " + linea + " " + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}		

	} // fin
	

	public static boolean Ruptura(int iX1, int iX2, String linea, String lineaAnt, String sSignos, boolean lGen, boolean lNum, String sL1, String sL2) {
		char cGen1=' ', cGen2=' ', cNum1=' ', cNum2=' ';
		int j;

		
		// controlamos ruptura por signos ..
		if(lineaAnt==null) {
			for(j=iX1;j<iX2;j++) {
				if(sSignos.contains(linea.substring(j,j+1))) return true;
			}
		}
		else {  // si son dos, primero controlamos la primera linea 
			for(j=iX1;j<lineaAnt.length();j++) {
				if(sSignos.contains(lineaAnt.substring(j,j+1))) return true;
			}
			for(j=0;j<iX2;j++) {  // y luego la segunda linea 
				if(sSignos.contains(linea.substring(j,j+1))) return true;
			}
		}
		
		// ahora ruptura por género o número ...
		if(sL1.length()<7 || sL2.length()<7 || (!lGen && !lNum)) return false;  
		// si no se puede ver genero/numero o no se piden, no se rompe
		
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

		return false;
	}

	
}
