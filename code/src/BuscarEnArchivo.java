import java.io.BufferedReader;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.sun.xml.internal.ws.util.StringUtils;


public class BuscarEnArchivo {


	public int Buscar(String sBusqueda, boolean lNomSoneto, JTable tResult, File fBusca, Boolean lMay) {
	  String sNomSoneto = new String("");
	  String sAux;
	  String sPosibleSoneto = new String("");
	  int iNumSoneto=0;
	  int iF=0, iF2=0;  // donde se inicia y acaba el texto a buscar
	  int iTip[]= {0,0,0,0,0,0,0,0,0,0}; // por defecto busqueda normal sin plantillas
	  String linea=""; // linea leida
	  String lineaA ="";  // linea en ASCII
	  String sBusca[] ={"","","","","","","","","",""};   // texto a buscar sin plantillas
	  String sResto[] ={"","","","","","","","","",""}; // resto de texto a buscar si hay plantilla intermedia
	  int iLen[]= {0,0,0,0,0,0,0,0,0,0};
	  boolean lTrata[] = {false,false,false,false,false,false,false,false,false,false};
	  int iMaxLin=0;
	  BufferedReader in=null;
	  int iaFound[][] = new int [5][2];
	  int ia=0, i=0, j=0, k=0;
	  boolean lFirst=true;
	  int iCar[]= {0,0,0,0,0,0,0,0,0,0};  // indica si hay asterisco delante
	  String sBusque="";
	  String sHexa="";
	  
	  try
	  {
		// comprobamos que se ha seleccionado fichero e introducido criterio de busqueda ...
		if(fBusca == null) {
			   JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
			     return 0;
		}
		else if(sBusqueda == null || sBusqueda.trim().length() == 0) {
			   JOptionPane.showMessageDialog(null, Idiomas.E9[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
			     return 0;
		}	

    	g.iWordsFound=0;
	    // preparamos la tabla ..
		DefaultTableModel model = new ModelConsulta();
		model.addColumn(Idiomas.NumLin[ventana.iId]);
		if (lNomSoneto) {
			model.addColumn(Idiomas.PoemTit[ventana.iId]);
			model.addColumn(Idiomas.NumVers[ventana.iId]);
		}
		model.addColumn(Idiomas.Verso[ventana.iId]);

		tResult.setModel(model);
		tResult.getColumn(Idiomas.NumLin[ventana.iId]).setMaxWidth(80);
		if (lNomSoneto) {
		  tResult.getColumn(Idiomas.PoemTit[ventana.iId]).setPreferredWidth(250);
		  tResult.getColumn(Idiomas.NumVers[ventana.iId]).setMaxWidth(80);
		}
		//tResult.getColumn("Verso").setPreferredWidth(500);
		//tResult.getColumn("NumLinea").setHeaderValue("Núm. línea");
		
		// contamos palabras
		sBusqueda = sBusqueda.trim().replace("  ", " ");
		// comprobamos que si hay | o & no vayan pegados a palabras ...
		i=0;
		while(i<sBusqueda.length()) {
			if(sBusqueda.charAt(i) == '|' || sBusqueda.charAt(i) == '&') {
				if(i>0 && sBusqueda.charAt(i-1)!=' ') {
					sBusqueda = sBusqueda.substring(0,i) + " " + sBusqueda.substring(i);
					continue;
				}
				if(i<sBusqueda.length()-1 && sBusqueda.charAt(i+1)!=' ') sBusqueda = sBusqueda.substring(0,i+1) + " " + sBusqueda.substring(i+1);
			}
			i++;
		}
		
		int iPalabras = sBusqueda.split(" ",-1).length;
		String sPalabra;
		//System.out.println(String.valueOf(iPalabras));
		if(iPalabras%2==0 || (iPalabras > 1 && (sBusqueda.indexOf(" | ")==-1 && sBusqueda.indexOf(" & ")==-1)) || iPalabras > 19) {

			JOptionPane.showMessageDialog(null, Idiomas.E10[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);

		     return 0;
		}
		
		// analizamos la/s pablabra/s a buscar
		sBusque = sBusqueda;
		int iPal=0;
		while(sBusque.trim().length()>0) {
			if(sBusque.indexOf(" ")>-1) {
				sPalabra=sBusque.trim().substring(0,sBusque.indexOf(" "));
				sBusque=sBusque.trim().substring(sBusque.indexOf(" ")+1);
			}	
			else {
				sPalabra=sBusque.trim();
				sBusque="";
			}
			if(sPalabra.contains("|") || sPalabra.contains("&")) continue;
			
			iLen[iPal]=sPalabra.length();
			sBusca[iPal]=sPalabra;
			if(sPalabra.charAt(0)=='*') {  // empieza en asterisco
				// cero o mas caracteres delante ...
				iCar[iPal]=1; // nos saltamos el asterisco y empezamos por el siguiente caracter
			}
			else {
				iCar[iPal]=0;
			}

			if(sPalabra.charAt(iLen[iPal]-1)=='*') {  // finaliza en asterisco
				// cero o mas caracteres ...
				iTip[iPal]=1;
				iLen[iPal]--;
				sBusca[iPal] = sPalabra.substring(iCar[iPal],iLen[iPal]);
			}
			else if(sPalabra.charAt(iLen[iPal]-1)=='+') {  // finaliza en +
				// 0-1 carácter
				iTip[iPal]=2;
				iLen[iPal]--;
				sBusca[iPal] = sPalabra.substring(iCar[iPal],iLen[iPal]);
			}
			else if(sPalabra.contains("?")) {  // contiene ?
				// 1 carácter en algún sitio
				iTip[iPal]=3;
				sBusca[iPal] = sPalabra.substring(iCar[iPal],sPalabra.indexOf("?"));
				sResto[iPal] = sPalabra.substring(sPalabra.indexOf("?")+1);
				iLen[iPal]=sBusca[iPal].length();
			}
			else if(sPalabra.contains("@")) {  // contiene @
				// 0-1 palabra a continuación
				iTip[iPal]=4;
				sBusca[iPal] = sPalabra.substring(iCar[iPal],sPalabra.indexOf("@"));
				sResto[iPal] = sPalabra.substring(sPalabra.indexOf("@")+1);
			}
			else sBusca[iPal] = sPalabra.substring(iCar[iPal]);
			iPal++;
		}
		//System.out.println("abrimos");
		
		in = g.TipoFichero(fBusca);
		// ahora nos posicionamos al principio del fichero ...
		int numLinea=0;
		while ((linea = in.readLine()) != null) {
			//System.out.println("linea: " + linea.toString());
//			if(linea.contains("quebrantado")) {
//				i=0;
//			}

			if(lFirst) {
				if(g.sBOM != "¿no BOM?") {
					linea = linea.substring(1);  // quitamos marca BOM
				}
				lFirst=false;
			}
			//if(linea.contains("En  tal  suerte con  l'alma'l")) // esto es sólo para depurar
			//	System.out.println(linea);;
			linea = linea.trim();
			if(linea=="") continue;

			numLinea++;	
	    	// controlamos nombre de soneto
	    	sAux = linea;
	    	sAux.replaceAll(" ", "");
	    	if(sAux.trim().length()==0) {
	    		// si la línea está vacia es que la anterior era el nombre del soneto
	    		sNomSoneto = sPosibleSoneto.trim();
	    		iNumSoneto=0;
	    	}
	    	else {
	    		sPosibleSoneto = linea;
	    		iNumSoneto++; 
	    	}
//	    	if(numLinea==711)
//	    		i=0;
	    	
	    	// nos saltamos los títulos
	    	if(linea.indexOf("#") > -1)
	    		continue;

			// quitamos posibles números al principio (nº de verso)
			for(i=0;i<linea.length() && Character.isDigit(linea.charAt(i));i++);
			if(i>0) linea = linea.substring(i);

	    	// motor de busqueda
	    	for(k=0;k<5;k++) {  // consideramos hasta cinco veces encontrado en la misma línea
				iaFound[k][0]= 9999;
				iaFound[k][1]= 9999;
			}
			ia=0;
	    	for(iPal=0;iPal<iPalabras;iPal+=2) {
				lTrata[iPal/2]=false; 
				i=0;
				j=0;
				while(i<linea.toString().length()) {
					//  buscamos la palabra ... 
					if(lMay)
						iF=linea.indexOf(sBusca[iPal/2].toString(),i);
					else
						iF=linea.toUpperCase().indexOf(sBusca[iPal/2].toString().toUpperCase(),i);
					if(iF!=linea.length()-1) sHexa = Integer.toHexString((int) linea.charAt(iF+1));
					else sHexa=" "; // controlamos las vocales con puntito arriba de Herrera
					if(iF>-1) { // && !sHexa.equals("307")) {  // encontrada ...
						i=iF+1; // la próxima vez buscaremos al menos a partir de ésta
						//lineaA = g.soloASCII(linea);
						lineaA = linea;
						if (iF>0 && Character.isLetter(lineaA.charAt(iF-1)) && iCar[iPal/2]!=1) { 
							continue; // si no empieza palabra y no lleva asterisco delante, nos la saltamos	
						}
					  	// buscamos donde acaba la palabra  
					  	for(iF2=iF;iF2 < linea.length() && esCaracter(linea.charAt(iF2));iF2++);
						
					  	if (iTip[iPal/2]==0 && (iF + iLen[iPal/2] < linea.toString().length() && Character.isLetter(linea.charAt(iF + iLen[iPal/2])))) {
							continue; // si es busqueda normal y no finaliza palabra, nos la saltamos	
						}
						else if (iTip[iPal/2]==2 && (iF + iLen[iPal/2] < linea.toString().length()-1 && Character.isLetter(linea.charAt(iF + iLen[iPal/2]+1)))) {
							continue; // si es busqueda "+" (0-1 carácter) y vienen más, nos la saltamos	
						}
						else if ((iTip[iPal/2]==3 || iTip[iPal/2]==4) && (iF + sBusca[iPal/2].length() > linea.toString().length())) {
							continue; // si es busqueda "?" o "#" y la plantilla no cabe en la línea nos la saltamos 
						}
						else if (iTip[iPal/2]==3 && (!Character.isLetter(linea.charAt(iF + iLen[iPal/2])) || !linea.substring(iF + iLen[iPal/2] + 1, iF + iLen[iPal/2] + 1 + sResto[iPal/2].length()).toUpperCase().equals(sResto[iPal/2].toUpperCase())
								|| Character.isLetter(linea.charAt(iF + iLen[iPal/2] + 1 + sResto[iPal/2].length())) )) {
							continue; // si es busqueda "?" (1 carácter en cualquier sitio) y detrás no coincide, nos la saltamos	
						}
						else if (iTip[iPal/2]==4) {
							// si es busqueda "#" (0-1 palabra a continuación) y detrás no coincide, nos la saltamos	
							for(k=iF2+1;k < linea.length() && !Character.isLetter(linea.charAt(k));k++); // buscamos la siguiente palabra
							if(k>=linea.length()) continue; // no hay más palabras (es la última)
							for(iLen[iPal/2]=k;iLen[iPal/2] < linea.length() && Character.isLetter(linea.charAt(iLen[iPal/2]));iLen[iPal/2]++); // buscamos donde acaba la palabra siguiente 
							if(iLen[iPal/2]>linea.length() || !linea.substring(iF2, iLen[iPal/2]).toUpperCase().contains(sResto[iPal/2].toUpperCase())) continue; // si no contiene el texto buscado lo saltamos
							else iF2=iLen[iPal/2];
						}
						// si llegamos hasta aquí es que vale
					  	lTrata[iPal/2]=true;
						iaFound[ia][0]=iF;
						iaFound[ia++][1]=iF2;
						i=iF2+1; // la próxima vez buscaremos a partir de ésta palabra
		    		}
		    		else break; // si ya no encontramos más nos salimos
		    	}  // fin del while 
			} // fin del for de varias palabras	

	    	
	    	// analizamos la lógica boleana si procede
	    	int iOr=sBusqueda.split("\\|",-1).length-1;
	    	int iAnd=sBusqueda.split("\\&",-1).length-1;
	    	int iTrata=0;
    	
	    	if(iOr>0 || iAnd>0) {
	    		for(i=0;i<lTrata.length;i++)
	    			if(lTrata[i]) iTrata++;
	    		if(iOr>0 && iTrata==0) continue;  // si es OR tiene que ser true alguno
	    		if(iAnd>0 && iTrata<iAnd+1) continue;  // si es AND tienen que ser true todos
	    	}

	    	if(iPalabras==1 && !lTrata[0])
	    		continue; // si solo una palabra y no se encuentra, siguiente línea	    
    		else {
    			// ahora tratamos si procede 
    			// ordenamos los encontrados
    			g.sortArray(iaFound);
   			
		    	StringBuilder sLin = new StringBuilder();
		        sLin.append("<html>");
	        	sLin.append(linea.substring(0, iaFound[0][0]));
		        for (i=0;i<ia && i<5;i++) {
		        	sLin.append("<span style=\"color: red;\">");
		        	sLin.append(linea.substring(iaFound[i][0], iaFound[i][1]));
		        	sLin.append("</span>");
					g.iWordsFound++;
		        	if(i+1<ia) sLin.append(linea.substring(iaFound[i][1], iaFound[i+1][0]));
		        }
		        sLin.append(linea.substring(iaFound[i-1][1], linea.length()));
                sLin.append("</html>");		        
				
                if (lNomSoneto) {
                	//sNomSoneto.replace("<", "&lt;").replace(">", "&gt;");
					model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea)), String.format("%1$-25s",sNomSoneto.toString()), (String) String.format("%1$-6s",String.valueOf(iNumSoneto)), sLin.toString()});
				} else {	
					model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numLinea)), sLin.toString()});
				}
				if(iMaxLin<linea.length()) iMaxLin = linea.length();
	    	}

		}  // fin while readLine
        in.close();
        return iMaxLin;
	  }
	  catch(Exception ex)
	  {
	     JOptionPane.showMessageDialog(null,ex+" " + linea + " " + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
	     return 0;
	  }
	}

    public static boolean esCaracter(char cChar) {
    	//if(Character.isLetter(cChar) || cChar=='\'') return true;
    	if(Character.isLetter(cChar)) return true;
    	else return false;
    }
	

}
