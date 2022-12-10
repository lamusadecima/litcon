import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Label2 {

	public int Freeling(File fIn1, File fIn2, File fOut) {
		BufferedReader in, fr;
		OutputStreamWriter out;
		String sAux;
		String sLin = "";
		String sLin2= ""; 
		String sLinOut="";
		String sPalabra = " ";
		String sPalabra2 = " ";
		String sLabel="";	    
		boolean lFirst=true, lFound=false, lLeido=false;
		int numP=0, i, j, i2, j2, k, m, n, o, y, z, y2, z2, y3, z3, iLabel;
		int iComp=0;
		int numLinea=0;
		int iLi2=0;
		boolean lEof=false, lPdte=false, lContra=false;
		  
		try
		{
			// comprobamos que se han seleccionado los fichero ..
			if(fIn1 == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			else if(fIn2 == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E12[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}			
			else if(fOut == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E7[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}			

			// preparamos ficheros
			in = g.TipoFichero(fIn1);
			fr = g.TipoFichero(fIn2);
			out = new OutputStreamWriter(new FileOutputStream(fOut), Charset.forName("UTF-8").newEncoder() );
			n=0; o=0;

			while ((sLin = in.readLine()) != null) {
				numLinea++;
//if(numLinea==44)
//	i=0;
				sAux = sLin.trim();
				sAux.replaceAll(" ", "");
				if(sAux.length()==0) {
					out.write(sLin + "\r\n");
					continue; // si la línea está vacia nos la saltamos
				}
				else if(sLin.indexOf("#")>-1) {  // es etiqueta, la saltamos 
//				else if(sLin.indexOf("<")>-1 && sLin.indexOf(">")>-1) {  // es etiqueta, la saltamos 
					out.write(sLin + "\r\n");
					continue;
				}

				numP=0;
				sLinOut="";
				// linea pendiente
				if(lPdte) {
					lPdte=false;
					for(o=n;o<iComp;o++) {
						for(y=numP;y<sLin.length() && !Character.isAlphabetic(sLin.charAt(y));y++);
						for(z=y;z<sLin.length() && Character.isAlphabetic(sLin.charAt(z));z++);
						numP=z;
						sLinOut = sLinOut + "-" + sLin.substring(y,z);
					}
					sLinOut = sLinOut + "_" + sLabel + " ";
				}
				
		    	// etiquetamos
		    	while (numP<sLin.length()) {
		    		// rastreamos el inicio de una palabra ...
		    		for(i=numP;i<sLin.length() && !Character.isAlphabetic(sLin.charAt(i));i++);
		    		if(i>numP) sLinOut = sLinOut + sLin.substring(numP,i);  // copiamos lo que hay antes 

		    		if(i<sLin.length()) {
		    			// rastreamos su final ...
		    			for(j=i;j<sLin.length() && Character.isAlphabetic(sLin.charAt(j));j++);
		    			if(j+1<sLin.length() && sLin.substring(j,j+1).indexOf("'")>-1 && Character.isAlphabetic(sLin.charAt(j+1))) {
		    				// si sigue apóstrofe y texto son dos palabras unidas
		    				for(m=j+1;m<sLin.length() && Character.isAlphabetic(sLin.charAt(m));m++);
		    				j=m;
		    			}
		    			numP=j;
		    			sPalabra = sLin.substring(i,j);
		    			// ahora por si acaso miro cual es la palabra siguiente
			    		for(i2=numP;i2<sLin.length() && !Character.isAlphabetic(sLin.charAt(i2));i2++);
		    			for(j2=i2;j2<sLin.length() && Character.isAlphabetic(sLin.charAt(j2));j2++);
		    			if(i2!=j2) sPalabra2 = sLin.substring(i2,j2);
		    			else sPalabra2="xxxxxxxxxxxx";
		    			lContra=false;

		    			// si es una contración
		    			if(sPalabra.toLowerCase().equals("del")) {
	    					lContra=true;
		    				sLin2 = fr.readLine();  // nos saltamos las dos siguientes líneas
		    				iLi2++;
		    				if(sLin2.indexOf("de_el")!=0) {
		    					sLin2 = fr.readLine(); 
		    					iLi2++;
		    				}
		    				if(sLin2.indexOf("de_el_")==0)  { // es una contración en palabra compuesta, prioridad a esto
    							iComp = ((sLin2.length() - sLin2.replace("_", "").length())/2)+1;
		    					sLinOut = sLinOut + sLin.substring(i,j);
		    					for(n=1;n<iComp;n++) {
		    						for(y=numP;y<sLin.length() && !Character.isAlphabetic(sLin.charAt(y));y++);
		    						if(y==numP) { // se ha acabado la linea, marcamos pendiente
		    							lPdte=true;
		    							break;
		    						}
		    						for(z=y;z<sLin.length() && Character.isAlphabetic(sLin.charAt(z));z++);
		    						numP=z;
		    						sLinOut = sLinOut + "-" + sLin.substring(y,z);
		    					}
		    					if(!lPdte) sLinOut = sLinOut + "_" + sLabel;

		    				}
		    				else {
		    					sLinOut = sLinOut + "de_SP el_DA0MS0";
			    				if(sLin2.indexOf("de de SP")==0) {
			    					sLin2 = fr.readLine(); // otra mas
			    					iLi2++;
			    				}
		    				}
		    			}
		    			else if(sPalabra.toLowerCase().equals("al")) {
		    				lContra=true;
		    				sLin2 = fr.readLine();  // nos saltamos las dos siguientes líneas
		    				iLi2++;
		    				if(sLin2.indexOf("a_el")!=0) {
		    					sLin2 = fr.readLine(); 
		    					iLi2++;
		    				}
		    				if(sLin2.indexOf("a_el_")==0)  { // es una contración en palabra compuesta, prioridad a esto
    							iComp = ((sLin2.length() - sLin2.replace("_", "").length())/2)+1;
		    					sLinOut = sLinOut + sLin.substring(i,j);
		    					for(n=1;n<iComp;n++) {
		    						for(y=numP;y<sLin.length() && !Character.isAlphabetic(sLin.charAt(y));y++);
		    						if(y==numP) { // se ha acabado la linea, marcamos pendiente
		    							lPdte=true;
		    							break;
		    						}
		    						for(z=y;z<sLin.length() && Character.isAlphabetic(sLin.charAt(z));z++);
		    						numP=z;
		    						sLinOut = sLinOut + "-" + sLin.substring(y,z);
		    					}
		    					if(!lPdte) sLinOut = sLinOut + "_" + sLabel;
		    				}
		    				else {
		    					sLinOut = sLinOut + "a_SP el_DA0MS0";
			    				if(sLin2.indexOf("a a SP")==0) {
			    					sLin2 = fr.readLine(); // otra mas
			    					iLi2++;
			    				}
		    				}
		    			}
		    			
		    			if(!lContra) {
		    				//if(lAscii) sPalabra = g.soloASCII(sPalabra);
		    				// buscamos en el fichero de etiquetadas de Freeling ...
		    				lFound=false; iComp=0;
		    				int iB=0;
		    				while (true) {
		    					iB++;
		    					if(!lLeido) {
		    						if ((sLin2 = fr.readLine()) == null) { // fin de fichero
		    							System.out.println(sLin.toString());
		    							lEof=true;
		    							break;
		    						}
		    						else iLi2++;
		    					}
		    					else lLeido=false;
		    					if(sLin2.indexOf(sPalabra)==0 || sLin2.indexOf(sPalabra + "_")==0) {
		    						// buscamos la etiqueta en mayúsculas
		    						for(k=sLin2.indexOf(" ");!Character.isUpperCase(sLin2.charAt(k)) && k<sLin2.length();k++);
		    						sLabel = sLin2.substring(k);
		    						if(sLabel.indexOf(" ")>0) sLabel = sLabel.substring(0, sLabel.indexOf(" "));
		    						//if(iLabel<=sLabel.length()) sLabel = sLabel.substring(0,iLabel);
		    						lFound=true;
		    						if(sLin2.charAt(sPalabra.length())=='_') {  // palabra compuesta
		    							iComp = ((sLin2.length() - sLin2.replace("_", "").length())/2)+1;
		    							//if(sLin2.substring(sPalabra.length()+1,sLin2.indexOf(" ")).contains("_")) iComp=3; // palabra triple
		    							//else iComp=2;  // palabra doble
		    						}
		    						else iComp=0;
		    						break;
		    					}
		    					else if(sLin2.indexOf(sPalabra2)==0 || sLin2.indexOf(sPalabra2 + "_")==0) {
		    						lLeido=true;  // si es la palabra siguiente me salgo
		    						lFound=false;
		    						break;
		    					}
		    					else if(sLin2.contains("_" + sPalabra)) {
		    						lLeido=true;  // si está metida malamente me salgo
		    						lFound=false;
		    						break;
		    					}
		    				}
		    				if(iB>10) System.out.println(sLin.toString());

		    				//if(iLabel<=sLabel.length())
		    				//	sLinOut = sLinOut + sLin.substring(i,j) + "_" + sLabel.substring(0,iLabel);
		    				//else
		    				if(!lFound)  sLinOut = sLinOut + sLin.substring(i,j) + "_NO";
		    				else if(iComp==0) sLinOut = sLinOut + sLin.substring(i,j) + "_" + sLabel;
		    				else {  // palabra compuesta, cogemos la/s siguiente/s palabra/s y la/s unimos
		    					sLinOut = sLinOut + sLin.substring(i,j);
		    					for(n=0;n<iComp;n++) {
		    						for(y=numP;y<sLin.replaceAll("\\s+$", "").length() && !Character.isAlphabetic(sLin.charAt(y));y++);
		    						if(y==numP) { // se ha acabado la linea, marcamos pendiente
		    							lPdte=true;
		    							break;
		    						}
		    						for(z=y;z<sLin.length() && Character.isAlphabetic(sLin.charAt(z));z++);
		    						numP=z;
		    						sLinOut = sLinOut + "-" + sLin.substring(y,z);
		    					}
		    					if(!lPdte) sLinOut = sLinOut + "_" + sLabel;
		    				}  // fin else palabra compuesta
		    			}

		    		}
		    		else break;
		    		if(lEof) {
		    			break;
		    		}
		    	} // fin while linea		    	
		    	out.write(sLinOut + "\r\n");
		    	if(lEof) {
		    		break;
		    	}
			} // fin de while linea fichero in
			in.close();
			fr.close();
			out.close();			
			return 1;

		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+"|"+sPalabra+"|"+sLin.toString()+ "|" + "\nError",
					Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}
	}
}
