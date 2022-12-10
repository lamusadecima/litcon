import java.io.BufferedReader;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Stylo3 {
	private Map<String, Integer> mLabel;
	private HashMap<String, int[]> mTotal;
	DefaultTableModel model;
	int numFiles=0;
	private List<Integer> iLabel = new ArrayList<>();
	int nTotal;

	public int ProcesarFiles (List<File> fSt3, JTable tStylo3, boolean lTrio, boolean lGen, boolean lNum) {
		int i=-1;
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		try
		{
			// comprobamos que se ha seleccionado fichero e introducido criterio de busqueda ...
			if(fSt3 == null) {
				JOptionPane.showMessageDialog(null, Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				return 0;
			}
			
			// preparamos las tablas ..
			model = new ModelConsulta();
			model.addColumn(Idiomas.Relation[ventana.iId]);
			model.addColumn(Idiomas.Frecuency[ventana.iId] + " (1/1000)");
			tStylo3.setModel(model);
			//tStylo3.getColumn(Idiomas.NumLin[ventana.iId]).setMaxWidth(80);
			mLabel = new HashMap<String, Integer>();
			mTotal = new HashMap<String, int[]>();
			numFiles=fSt3.size();
			nTotal=0;

			// ahora procesamos todos los ficheros 	
			for(i=0;i<fSt3.size();i++) {
				model.addColumn("F" + String.valueOf(i+1) + " (1/1000)");
				// BuscarTresLabel(fSt3.get(i), tStylo3, i, lTrio, lGen, lNum);
				BuscarRelacion(fSt3.get(i), tStylo3, i, lTrio, lGen, lNum);
			}
			// ahora pasamos las palabras a la tabla
			mLabel = sortByValues(mLabel, 1);  // por valores descendente
			String sClave;
			Set set = mLabel.entrySet();
		    Iterator iterator = set.iterator();
		    while(iterator.hasNext()) {
		        Map.Entry mentry = (Map.Entry) iterator.next();
		        sClave = mentry.getKey().toString();
		        Vector<Object> data = new Vector<Object>();
		        data.add(sClave);
		        data.add((String) String.format("%1$6s",String.valueOf(mentry.getValue())) + " (" +
		        	df.format((float) (int) mentry.getValue()*1000/nTotal) + ")");
		        for(i=0;i<fSt3.size();i++) {
		        	data.add((String) String.format("%1$6s",String.valueOf(mTotal.get(sClave)[i])) + " (" +
		              df.format((float) (int) mTotal.get(sClave)[i]*1000/iLabel.get(i)) + ")");
		        	//data.add((String) String.format("%1$6s",String.valueOf(mTotal.get(sClave)[i])));
		        }
		        model.addRow(data);
		    }
		    
			return 1;	
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" P1 |" + String.valueOf(i) + "| " + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}			
	}
	
	
	public int BuscarRelacion (File fIn, JTable tStylo3, int iFile, boolean lTrios, boolean lGen, boolean lNum) {
		String linea=""; // linea leida
		boolean lFirst=true;
		BufferedReader in=null;
		int i=0, j=0;
		int iF=0, iD=0;  
		int numLinea=0;
		int iF2=0, iL=0;
		String sTrio;
		int numTrios=0;
		String sCadena="";
		String sDepura="";
		
		try
		{
			sTrio="";
			iL=0;
			// leemos el fichero
			in = g.TipoFichero(fIn);
			StringBuffer sB = new StringBuffer();
			while ((linea = in.readLine()) != null) {
				numLinea++;	
				if(lFirst) {
					if(g.sBOM != "¿no BOM?") {
						linea = linea.substring(1);  // quitamos marca BOM
					}
					lFirst=false;
				}
				sB.append(linea);
				if(numLinea%1000==0) break;  // Leemos en grupos de 1000 líneas
			}

			iD=0;
			// empezamos a coger trios de etiquetas o duos ...
			while (iD<sB.length()) {
				if(sB.substring(iD).indexOf("_")>-1) {  // encontrado
					iF=iD+sB.substring(iD).indexOf("_");
					if(sB.substring(iD,iF).contains(".")) iL=0; // si hay punto empezamos trio

					// donde acaba la etiqueta ...
					for(i=iF;i+1<sB.length() && i<iF+2 && (Character.isLetter(sB.charAt(i+1)) || Character.isDigit(sB.charAt(i+1)) || sB.charAt(i+1)=='+');i++);
					for(j=iF;j+1<sB.length() && (Character.isLetter(sB.charAt(j+1)) || Character.isDigit(sB.charAt(j+1)) || sB.charAt(j+1)=='+');j++);
					if(iL==0) {  // primera etiqueta
						sTrio = sB.substring(iF+1, i+1);
						sCadena= sB.substring(iF, j+1);
					}
					else {
						sTrio = sTrio + " " + sB.substring(iF+1, i+1);
						sCadena = sCadena + " " + sB.substring(iF, j+1);
					}
					iD=i+1;
					iL++;
					//if(sTrio.equals("AQ DA RG"))
					//	sDepura = sB.substring(iD-100,iF);  // esto es sólo para depurar
					if((lTrios && iL==3) || (!lTrios && iL==2)) {
						if((!lGen && !lNum) || !Ruptura(sCadena, lGen, lNum)) {
							add(sTrio.toString(), mLabel);
							add2(sTrio.toString(),mTotal,iFile,numFiles);
							numTrios++;
						}
						sTrio=sTrio.substring(sTrio.indexOf(" ")+1); // quitamos la primera etiqueta
						if(lTrios) iL=2;
						else iL=1;
						sCadena = sCadena.substring(sCadena.indexOf(" ")+1);
					}
				}  // fin de encontrado
				else if(linea==null) break;  // ya no queda nada, nos salimos
				else { // leemos mas lineas 
					sB.delete(0,iD);  // borramos hasta antes de por donde vamos
					while ((linea = in.readLine()) != null) {
						sB.append(linea);
						numLinea++;	
						if(numLinea%1000==0) break;
					}
					iD=0;
				} 
			} // fin de while no hay mas etiquetas
			iLabel.add(numTrios);
			nTotal=nTotal+numTrios;
			
			return 1;		
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" P2 |" + String.valueOf(iD) + "| " + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}		

	} // fin
	
	public int BuscarTresLabel (File fIn, JTable tStylo3, int iFile, boolean lTrios, boolean lGen, boolean lNum) {
		String linea=""; // linea leida
		boolean lFirst=true;
		BufferedReader in=null;
		int i=0, iF=0, iF2=0, iL=0;
		String sTrio;
		int numLinea=0;
		int numTrios=0;
		int iOk=0, iOk2=0;
		
		try
		{
			sTrio="";
			iL=0;
			
			// abrimos el fichero
			in = g.TipoFichero(fIn);
			// ahora nos posicionamos al principio del fichero ...
			while ((linea = in.readLine()) != null) {
				numLinea++;
				//System.out.println("linea: " + linea.toString());
				linea = linea.trim();
				if(lFirst) {
					if(g.sBOM != "¿no BOM?") {
						linea = linea.substring(1);  // quitamos marca BOM
					}
					lFirst=false;
				}

		    	if(linea=="" || linea.length()==0) {  // si está en blanco la saltamos
		    		continue;
		    	}
				// a coger trios de etiquetas o duos ...
				while((iF=linea.indexOf("_"))>-1) {
					if(linea.substring(0,iF).contains(".")) iL=0; // si hay punto empezamos trio 
					
					for(i=iF;i+1<linea.length() && i<iF+2 && (Character.isLetter(linea.charAt(i+1)) || Character.isDigit(linea.charAt(i+1)) || linea.charAt(i+1)=='+');i++);
					if(i<iF+1) { // por si acaso
						linea=linea.substring(iF+1);
						continue;
					}
					
					if(iL==0) {  // primera etiqueta
						sTrio = linea.substring(iF+1, i+1);
						iOk=iF;
					}
					else {
						sTrio = sTrio + " " + linea.substring(iF+1, i+1);
						//for(i=iF+1;Character.isLetter(linea.charAt(i+1)) || Character.isDigit(linea.charAt(i+1)) || linea.charAt(i+1)=='+';i++);
						//iOk2=i+1;
					}
					iL++;
					if((lTrios && iL==3) || (!lTrios && iL==2)) {
//	if(sTrio.compareTo("VM VM SP")==0) System.out.println(linea.toString());
						//if((!lGen && !lNum) || !Ruptura(linea.substring(iOk, iOk2), lGen, lNum)) {
							add(sTrio.toString(), mLabel);
							add2(sTrio.toString(),mTotal,iFile,numFiles);
							numTrios++;
						//}
						sTrio=sTrio.substring(sTrio.indexOf(" ")+1); // quitamos la primera etiqueta
						if(lTrios) iL=2;
						else iL=1;
					}
					linea=linea.substring(iF+1);
				} // fin de while no hay mas etiquetas
				if(linea.contains(".")) iL=0; // si acaba en punto a empezar
				
			} // fin while readLine
			//iLabel.add(linea.split("_",-1).length-1); // contamos palabras
			iLabel.add(numTrios);
			nTotal=nTotal+numTrios;
			
			return 1;		
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+" P2 |" + linea.toString() + "| " + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return 0;
		}			
	}

	public static void add(String name, Map<String, Integer> mWord) { // añade/modifica una clave con contador de repeticiones 
	     Integer value = mWord.get(name);
	     if (value == null) {
		    value = new Integer(0);
		    mWord.put(name, value);
		 }
	     mWord.put(name, ++value);		
	}

	public static void add2(String name, HashMap<String, int[]> mWord, int j, int nF) { // añade/modifica una clave con contador de repeticiones 
		int[] value = mWord.get(name);

		if (value == null) {
			value = new int[nF];
			for(int i=0;i<nF;i++) value[i]=0;
		    mWord.put(name, value);
		}
		++value[j];
	    mWord.put(name, value);		
	}

	public static <K, V extends Comparable<V>> Map<K, V> sortByValues
    (final Map<K, V> map, int ascending)
	{
		Comparator<K> valueComparator =  new Comparator<K>() {         
			private int ascending;
			public int compare(K k1, K k2) {
				int compare = map.get(k2).compareTo(map.get(k1));
				if (compare == 0) return 1;
				else return ascending*compare;
			}
			public Comparator<K> setParam(int ascending)
			{
				this.ascending = ascending;
				return this;
			}
		}.setParam(ascending);

		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}	

	public static boolean Ruptura(String sCadena, boolean lGen, boolean lNum) {
		char cGen1=' ', cGen2=' ', cNum1=' ', cNum2=' ';
		int i,j;
		String sL1="", sL2="";
		//if(sCadena.contains("fiera_AQ0FS000 el_"))
		//	i=0;
		//System.out.println(sCadena);
		try {
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

				//if(sL2.length()<7) continue; // si no se puede ver genero/numero salto a la siguiente 
				//System.out.println(sL2);
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
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null," Label error: " + sCadena + "| " + "\nError", Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
			return false;
		}	
		return false;
	}	
}
