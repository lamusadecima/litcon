import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.mozilla.universalchardet.UniversalDetector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/*
 Concordancias -> BuscarEnArchivo.java
 Listado -> BuscarConcordancias.java
 WordList -> Wordlist.java
 Contraste -> Exclusiones.java
 Palabras Clave -> Wordkey.java
 */

public class g {
	// variables globales ...
	public static File fileTxt;
	public static File fDir;
	public static String sTipFile="";
	public static String sBOM="";
	public static int numWords;
	public static int iLineas;
	public static int numPoet;
	public static int numWords2;
	public static int iLineas2;
	public static int numPoet2;
	public static int iWordsFound;
	public static String sPuntua="[\",;.\t()¿?!¡«»:\\[\\]\\{\\}]";
	public static String sTemp="";
	
	// función que salva resultados OBSOLETA
	public static void GuardarFicheroOLD(char cTipo, JTable tDatos, JTextArea tArea) {
		 FileFilter filter;
		 FileWriter  save;
		 String sWrite;
		 
		 try
		 {
		  JFileChooser file=new JFileChooser();
		  if(cTipo=='t') {
			filter = new FileNameExtensionFilter("Txt File","txt");
		  }
		  else {
		    filter = new FileNameExtensionFilter("Csv File","csv");
		  }
		  if(fDir != null)
			  file.setCurrentDirectory(fDir);
		  file.setFileFilter(filter);
		  file.showSaveDialog(null);
		  File guarda = file.getSelectedFile();
		  fDir = file.getCurrentDirectory();
		  
		  
		  if(guarda !=null)
		  {
		   // preparamos el fichero 
			if(cTipo=='t' && !guarda.getName().contains(".txt")) {
				save=new FileWriter(guarda+".txt");
		    }
		    else if(cTipo=='e' && !guarda.getName().contains(".csv")) {
		    	save=new FileWriter(guarda+".csv");
		    }
		    else {
		    	save=new FileWriter(guarda);
		    }

		    if(tDatos!=null) {
		      // recorremos la tabla y guardamos	
		      for(int i=0; i<tDatos.getRowCount(); i++) //recorro las filas
		      {
		       for(int a=0; a<tDatos.getColumnCount(); a++) //recorro las columnas
		       {
		    	   // quitamos html si lo hay
		    	   sWrite=quitarHTML(tDatos.getModel().getValueAt(i ,a).toString());
		    	   if(cTipo=='t') {
		    		   save.write(sWrite);
		    	   }
		    	   else {
		    		   save.write("\"" + sWrite.trim() + "\";");
		    	   }
		       }
		       save.write("\r\n");
		      }
		    }
		    else if(tArea!=null) {
		    	save.write(tArea.getText());	
		    }
		    
		    //save.write(areaDeTexto.getText());
		    save.close();
		    JOptionPane.showMessageDialog(null,
		    		Idiomas.FileSaved[ventana.iId],
		             "Información",JOptionPane.INFORMATION_MESSAGE);
		    }
		 }
		  catch(IOException ex)
		  {
		   JOptionPane.showMessageDialog(null,
				   Idiomas.FileNoSaved[ventana.iId],
		           "Advertencia",JOptionPane.WARNING_MESSAGE);
		  }
		 }

	// función que salva resultados en UTF8
	public static void GuardarFichero(char cTipo, JTable tDatos, JTextArea tArea) {
		 FileFilter filter;
		 String sWrite;
		 OutputStreamWriter out;
		 
		 try
		 {
		  JFileChooser file=new JFileChooser();
		  if(cTipo=='t') {
			filter = new FileNameExtensionFilter("Txt File","txt");
		  }
		  else {
		    filter = new FileNameExtensionFilter("Csv File","csv");
		  }
		  if(fDir != null)
			  file.setCurrentDirectory(fDir);
		  file.setFileFilter(filter);
		  file.showSaveDialog(null);
		  File guarda = file.getSelectedFile();
		  fDir = file.getCurrentDirectory();
		  
		  
		  if(guarda !=null)
		  {
		    // preparamos el fichero 
			if((cTipo=='t') && !guarda.getName().contains(".txt")) {
				out = new OutputStreamWriter(new FileOutputStream(guarda+".txt"), Charset.forName("UTF-8").newEncoder() );  
		    }
		    else if(cTipo=='e' && !guarda.getName().contains(".csv")) {
				out = new OutputStreamWriter(new FileOutputStream(guarda+".csv"), Charset.forName("UTF-8").newEncoder() );  
		    }
		    else {
				out = new OutputStreamWriter(new FileOutputStream(guarda), Charset.forName("UTF-8").newEncoder() );  
		    }
			
		    if(tDatos!=null) {
		      // recorremos la tabla y guardamos	
		      for(int i=0; i<tDatos.getRowCount(); i++) //recorro las filas
		      {
		       for(int a=0; a<tDatos.getColumnCount(); a++) //recorro las columnas
		       {
		    	   // quitamos html si lo hay
		    	   //System.out.println("|" + tDatos.getModel().getValueAt(i ,a).toString() + "|");
		    	   sWrite=tDatos.getModel().getValueAt(i ,a).toString();
		    	   if(sWrite.toUpperCase().contains("HTML")) sWrite=quitarHTML(sWrite);
		    	   
		    	   if(cTipo=='t') {
		    		   out.write(String.format("%1$-20s",sWrite));
		    	   }
		    	   else {
		    		   out.write("\"" + sWrite.trim() + "\";");
		    	   }
		       }
		       out.write("\r\n");
		      }
		    }
		    else if(tArea!=null) {
		    	out.write(tArea.getText());	
		    }
		    
		    out.close();
		    JOptionPane.showMessageDialog(null,
		    		Idiomas.FileSaved[ventana.iId],
		             "Información",JOptionPane.INFORMATION_MESSAGE);
		    }
		 }
		  catch(IOException ex)
		  {
		   JOptionPane.showMessageDialog(null,
				   Idiomas.FileNoSaved[ventana.iId],
		           "Advertencia",JOptionPane.WARNING_MESSAGE);
		  }
		 }
	
	// función que salva resultados en UTF8 y cabeceras personalizadas
	public static void SaveFile(char cTipo, JTable tDatos, JTextArea tArea, String sCab) {
		FileFilter filter;
		String sWrite;
		OutputStreamWriter out;
		String sDato="";
		int iW=0;

		try
		{
			JFileChooser file=new JFileChooser();
			if(cTipo=='t') {
				filter = new FileNameExtensionFilter("Txt File","txt");
			}
			else {  // cTipo 'e' o 'd'
				filter = new FileNameExtensionFilter("Csv File","csv");
			}
			if(fDir != null)
				file.setCurrentDirectory(fDir);
			file.setFileFilter(filter);
			file.showSaveDialog(null);
			File guarda = file.getSelectedFile();
			fDir = file.getCurrentDirectory();


			if(guarda !=null)
			{
				// preparamos el fichero 
				if((cTipo=='t') && !guarda.getName().contains(".txt")) {
					out = new OutputStreamWriter(new FileOutputStream(guarda+".txt"), Charset.forName("UTF-8").newEncoder() );  
				}
				else if((cTipo=='e' || cTipo=='d') && !guarda.getName().contains(".csv")) {
					out = new OutputStreamWriter(new FileOutputStream(guarda+".csv"), Charset.forName("UTF-8").newEncoder() );  
				}
				else {
					out = new OutputStreamWriter(new FileOutputStream(guarda), Charset.forName("UTF-8").newEncoder() );  
				}

				if(tDatos!=null) {
					if(sCab!=null) {
						out.write(sCab.trim());
						out.write("\r\n");
					}
					// recorremos la tabla y guardamos	
					for(int i=0; i<tDatos.getRowCount(); i++) //recorro las filas
					{
						for(int a=0; a<tDatos.getColumnCount(); a++) //recorro las columnas
						{
							// quitamos html si lo hay
							//System.out.println("|" + tDatos.getModel().getValueAt(i ,a).toString() + "|");
							sWrite=tDatos.getModel().getValueAt(i ,a).toString();
							if(sWrite.toUpperCase().contains("HTML")) sWrite=quitarHTML(sWrite);

							if(cTipo=='t') {
								out.write(String.format("%1$-20s",sWrite));
							}
							else if(cTipo=='e' || (cTipo=='d' && a==0)) {  // excel normal
								if(sWrite.contains("(")) {
									sDato=sWrite.substring(0, sWrite.indexOf("("))  + "\";\"" + sWrite.substring(sWrite.indexOf("(")+1,sWrite.indexOf(")"));
								}
								else
									sDato=sWrite.trim();
								out.write("\"" + sDato + "\";");
							}
							else if(cTipo=='d' && a==1) {  // excel detalle
								iW=0;
								try {
									iW= Integer.parseInt(sWrite.trim());
								} catch(NumberFormatException en) {iW=0;}
								for(int j=1;j<iW;j++) {
									out.write("\r\n");
									out.write("\"" + sDato.trim() + "\";");
								}
							}
						}
						out.write("\r\n");
					}
				}
				else if(tArea!=null) {
					out.write(tArea.getText());	
				}

				out.close();
				JOptionPane.showMessageDialog(null,
						Idiomas.FileSaved[ventana.iId],
						"Información",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(null,
					Idiomas.FileNoSaved[ventana.iId],
					"Advertencia",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	// función que abre archivo
	public static File abrirArchivo(char cTipo) {
		FileFilter filter;
		File fChoose = null;

		try
		{
			JFileChooser fileChooser=new JFileChooser();
			if(cTipo=='t') {	
				filter = new FileNameExtensionFilter("Txt File","txt");
			}
			else {
				filter = new FileNameExtensionFilter("Txt File","txt"); 
			}
			if(fDir != null)
				fileChooser.setCurrentDirectory(fDir);
			fileChooser.setFileFilter(filter);
			//fileChooser.showOpenDialog(null);
			int returnVal = fileChooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {  // si se da aceptar
				fDir = fileChooser.getCurrentDirectory();
				/**abrimos el archivo seleccionado*/
				fChoose=fileChooser.getSelectedFile();
			} 
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+"" + "\n" + Idiomas.E13[ventana.iId], Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
		}
		return fChoose;
	}
	
	// función que abre multiples ficheros y los une en uno temporal
	public static File[] abrirMArchivo(char cTipo) {
		FileFilter filter;
		File[] files = null;

		try
		{
			JFileChooser fileChooser=new JFileChooser();
			if(cTipo=='t') {	
				filter = new FileNameExtensionFilter("Txt File","txt");
			}
			else {
				filter = new FileNameExtensionFilter("Txt File","txt"); 
			}
			if(fDir != null)
				fileChooser.setCurrentDirectory(fDir);
			fileChooser.setFileFilter(filter);
			fileChooser.setMultiSelectionEnabled(true);
			int returnVal = fileChooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {  // si se da aceptar
				fDir = fileChooser.getCurrentDirectory();
				files=fileChooser.getSelectedFiles();
			} 

		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex+"" + "\n" + Idiomas.E13[ventana.iId], Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
		}
		return files;
	}

	// función que carga datos de un archivo
	public static String CargarFichero(char cTipo) {
		FileFilter filter;
		StringBuilder sB = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		try
		{
		  JFileChooser file=new JFileChooser();
		  if(cTipo=='t') {
			filter = new FileNameExtensionFilter("Txt File","txt");
		  }
		  else {
		    filter = new FileNameExtensionFilter("Csv File","csv");
		  }
		  if(fDir != null)
			  file.setCurrentDirectory(fDir);
		  file.setFileFilter(filter);
		  file.showSaveDialog(null);
		  fDir = file.getCurrentDirectory();		  
		  File guarda = file.getSelectedFile();

		  if(guarda !=null)
		  {
			  // ahora nos posicionamos al principio del fichero ...
			  Scanner scanner = new Scanner(guarda);
			  while (scanner.hasNextLine()) {
				  sB.append( scanner.nextLine());
			      sB.append( ls );
			  }	  
			  scanner.close();
			  return sB.toString();
		  }
		  else return "";
		  
		 }
		  catch(IOException ex)
		  {
		   JOptionPane.showMessageDialog(null, Idiomas.E14[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		   return "";
		  }
	 }

	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
    public static BufferedReader TipoFichero(File fBusca) {
    	BufferedReader in=null;
    	// función que detecta el tipo de fichero 
    	try {
            // valores posibles en la página https://code.google.com/p/juniversalchardet/
    		byte[] buf = new byte[4096];
    		java.io.FileInputStream fis = new java.io.FileInputStream(fBusca);
    		UniversalDetector detector = new UniversalDetector(null);
    		int nread;
    		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
    			detector.handleData(buf, 0, nread);
    		}
    		detector.dataEnd();
    		String encoding = detector.getDetectedCharset();
    		if (encoding == null || encoding == "WINDOW-1252") {  // si no lo detecta 
    			in = new BufferedReader(new InputStreamReader(new FileInputStream(fBusca), StandardCharsets.ISO_8859_1));
    			sTipFile = "WINDOW-1252";
    		} 
    		else if (encoding == "UTF-8"){
    			in = new BufferedReader(new InputStreamReader(new FileInputStream(fBusca), StandardCharsets.UTF_8));
    			sTipFile = "UTF-8";
    		}
    		else if (encoding == "UTF-16BE"){
    			in = new BufferedReader(new InputStreamReader(new FileInputStream(fBusca), StandardCharsets.UTF_16BE));
    			sTipFile = "UTF-16BE";
    		}
    		else if (encoding == "UTF-16LE"){
    			in = new BufferedReader(new InputStreamReader(new FileInputStream(fBusca), StandardCharsets.UTF_16LE));
    			sTipFile = "UTF-16LE";
    		}
    		else {  // por defecto el normal
    			in = new BufferedReader(new InputStreamReader(new FileInputStream(fBusca), StandardCharsets.ISO_8859_1));
    			sTipFile = encoding;
    		}
    		fis.close();
    		detector.reset();

    		// ahora miramos si tiene BOM
    		byte bom[] = new byte[4];
    		//int unread;
    		PushbackInputStream pushbackStream = new PushbackInputStream(new FileInputStream(fBusca), 4);
    		int n = pushbackStream.read(bom, 0, bom.length);

    		// Read ahead four bytes and check for BOM marks.
    		if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
    			sBOM = "UTF-8";
    			in.skip(3);
    		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
    			sBOM = "UTF-16BE";
    			in.skip(2);
    		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
    			sBOM = "UTF-16LE";
    			in.skip(2);
    		} else if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
    			sBOM= "UTF-32BE";
    			in.skip(4);
    		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
    			sBOM = "UTF-32LE";
    			in.skip(4);
    		} else {
    			sBOM = "¿no BOM?";
    			//      unread = n;
    		}
    		pushbackStream.close();

    		return in;
    	}
    	catch(Exception ex)
    	{
    		JOptionPane.showMessageDialog(null,ex+" " +
    				"\nError",Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
    		return in;
    	}
   }
    
    public static String quitarHTML(String sCadena) {
    	String sReturn = sCadena.replaceAll("<br>", "\r\n");
    	while(sReturn.indexOf("<")>-1 && sReturn.indexOf(">")>-1) {
    		sReturn = sReturn.substring(0, sReturn.indexOf("<")) + sReturn.substring(sReturn.indexOf(">")+1);
    	}
    	return sReturn;
    }

    public static String quitarEtiquetas(String sCadena) {
    	String sReturn = sCadena;
    	int i,j;
    	while((j=sReturn.indexOf("_"))>-1 && Character.isLetter(sReturn.charAt(j+1))) {
    		for(i=j+1; i<sReturn.length() && Character.isLetter(sReturn.charAt(i)); i++);  // donde acaba la etiqueta
    		sReturn = sReturn.substring(0, j) + sReturn.substring(i);
    	}
    	return sReturn;
    }
    
    
    public static String quitarEtiquetasNum(String sCadena) {
    	String sReturn = sCadena;
    	int i,j;

    	if(sReturn.indexOf("_")==sReturn.length()-1) return sReturn;  // si la última es un guión nos salimos
    	while((j=sReturn.indexOf("_"))>-1 && (Character.isLetter(sReturn.charAt(j+1)) || Character.isDigit(sReturn.charAt(j+1)))) {
    		for(i=j+1; i<sReturn.length() && (Character.isLetter(sReturn.charAt(i)) || Character.isDigit(sReturn.charAt(i)) || sReturn.charAt(i)=='+'); i++);  // donde acaba la etiqueta
    		sReturn = sReturn.substring(0, j) + sReturn.substring(i);
    		if(sReturn.indexOf("_")==sReturn.length()-1) break;  // si la última es un guión nos salimos
    	}
    	return sReturn;
    }        
    
    public static String ColorearEtiquetas(String sCadena) {
    	String sReturn = "";
    	int i,j;
    	while((j=sCadena.indexOf("_"))>-1 && (Character.isLetter(sCadena.charAt(j+1)))) {
    		for(i=j+1; i<sCadena.length() && (Character.isLetter(sCadena.charAt(i)) || Character.isDigit(sCadena.charAt(i)) || sCadena.charAt(i)=='+'); i++);  // donde acaba la etiqueta
    		sReturn = sReturn + sCadena.substring(0, j) + "<span style=\"color: red;\">" + sCadena.substring(j, i) + "</span>";
    		sCadena= sCadena.substring(i);
    	}
    	return sReturn;
    }


    public static String quitarNum(String sCadena) {
    	int i;
    	String sReturn=sCadena;
   		while((i=sReturn.indexOf("|"))>-1) {
   			sReturn=sReturn.substring(0,i) + sReturn.substring(sReturn.indexOf("¬")+1);
   		}
    	return sReturn;
    }

    public static void sortArray(int myArray[][]) {
	    Arrays.sort(myArray, new Comparator<int[]>() {

	        @Override
	        public int compare(int[] o1, int[] o2) {
	            return Integer.valueOf(o1[0]).compareTo(Integer.valueOf(o2[0]));
	        }

	    });
	}
    
	
	public static Map<String, Integer> RecogePalabras(File fEntrada, String sExcluir, Boolean lTag, Boolean lMay, Boolean lAscii) {
		String sPalabra;	
		String sAux;
		Map<String, Integer> mWord;
		int i=0;
		String sPrueba;
		
		try
		{
			// comprobamos que se ha seleccionado fichero de entrada ...
			if(fEntrada == null) {
			   JOptionPane.showMessageDialog(null,Idiomas.E6[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
			   return null;
			}

			// preparamos exclusiones
			if(sExcluir == null) sExcluir= " ";
			else sExcluir = " " + sExcluir.toString().replaceAll("\r\n", " ").replaceAll("\n", " ") + " ";
			
			mWord = new HashMap<String, Integer>();
			// ahora nos posicionamos al principio del fichero ...
			String sLin;
			BufferedReader in = g.TipoFichero(fEntrada);
			numWords=0;
			iLineas=0;
			while ((sLin = in.readLine()) != null) {  // leemos la linea
				iLineas++;
				// quitamos comas, puntos y coma, punto, etc
				sLin = sLin.replaceAll(sPuntua," ").trim();
				// pasamos a minúsculas
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
							if(i==-1) {
								i=sLin.indexOf(" "); // si la etiqueta no se acaba, la cerramos en el primer blanco
								if(i==-1) {
									i=sLin.length()-1; // si no hay blanco, consideramos toda la línea
								}
							}
							sPalabra = sLin.substring(0,i+1);
							sLin = sLin.substring(i+1).trim();
						} 
						else if(sLin.indexOf("'") > -1 && 
								(sLin.indexOf("'")<sLin.indexOf(" ") || sLin.indexOf(" ")==-1)) { // el apóstrofe indica nueva palabra
							sPalabra = sLin.substring(0,sLin.indexOf("'"));
							sLin = sLin.substring(sLin.indexOf("'")+1).trim(); 
						}	
						else if(sLin.indexOf(" ")>0) { 
							sPalabra = sLin.substring(0,sLin.indexOf(" "));
							sLin = sLin.substring(sLin.indexOf(" ")).trim();
						} else {  // ultima palabra 
							sPalabra = sLin.trim();
							sLin=""; 
						}
						//System.out.println("tratar " + sPalabra.toString());
						if(sExcluir.indexOf(" " + sPalabra.toString() + " ")>-1 || g.isNumeric(sPalabra)
							|| (lTag && sPalabra.indexOf("#") > -1)	) {
							// esta palabra esta excluida o es un número o es una etiqueta y no debe salir
						}
						else {
							numWords++;
							//if(sPalabra.equals("a"))
							//	System.out.println(sLin);
							// quitamos acentos?
							if(lAscii) sPalabra = soloASCII(sPalabra); 
							add(sPalabra.toString(), mWord);
						}
					}  // fin while
				}			  
		  
			} // fin while readline
			in.close(); // cerramos el fichero
			return mWord;
		  }
		  catch(Exception ex)
		  {
		     JOptionPane.showMessageDialog(null,ex+"" +
		           "\nError", Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
		     return null;
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
	

    public static String titulo(String sText) {
	    String regex = "#(.*)#";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(sText);
	        
	    if (matcher.matches()) {
	    	return matcher.group(1);
	    }
	    else return "";
	}

	
	public static void Ordenar(char cTipo, JTable tResult) {
		// funcion que ordena tabla de claves y valores
		int numPalabras=0;
		
		// recogemos los datos
		String[][] data = new String[tResult.getRowCount()][2];
	    for(int i=0; i<tResult.getRowCount(); i++) //recorro las filas
	    {
	    	data[i][0] = tResult.getModel().getValueAt(i,1).toString();
	    	data[i][1] = tResult.getModel().getValueAt(i,2).toString();
	    }
	    
		if(cTipo=='A') {  // ordenación alfabética de claves
			if(tResult.getName()=="A") {
			    Arrays.sort(data, new ArrayComparator(0, false));  // alfabético descendente
				tResult.setName("a");
			}
			else {
			    Arrays.sort(data, new ArrayComparator(0, true));  // alfabético ascendente
			    tResult.setName("A");
			}
		}
		else {  // ordenación numérica de valores
			if(tResult.getName() == null || tResult.getName()=="V") {
			    Arrays.sort(data, new ArrayComparator(1, false));  // valores descendente
			    tResult.setName("v");
			}
			else {
			    Arrays.sort(data, new ArrayComparator(1, true));  // valores ascendente
			    tResult.setName("V");
			}
		}
		
		// ahora rellenamos y refrescamos ...
		DefaultTableModel model = new ModelConsulta();
		model.addColumn(Idiomas.Orden[ventana.iId]);
		model.addColumn(Idiomas.Word[ventana.iId]);
		model.addColumn(Idiomas.Ocurren[ventana.iId]);
		tResult.setModel(model);
		tResult.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);			
		tResult.getColumn(Idiomas.Ocurren[ventana.iId]).setMaxWidth(120);			
	    for(int i=0; i<data.length; i++) { //recorro las filas
			numPalabras++;
	        model.addRow(new Object[]{String.valueOf(numPalabras), data[i][0].toString(), data[i][1].toString()});
	    }
	}
	
	public static void Ordenados(JTable tResult, int nColumna, boolean lAsc) {
		// funcion que ordena tabla por la columna nColumna, hasta cuatro columnas?
		int numPalabras=0;
		int iColumn=tResult.getModel().getColumnCount()-1;
		
		// recogemos los datos
		String[][] data = new String[tResult.getRowCount()][iColumn];
	    for(int i=0; i<tResult.getRowCount(); i++) //recorro las filas
	    {
	    	for(int j=0; j<iColumn;j++) {
	    		data[i][j] = tResult.getModel().getValueAt(i,j+1).toString();
	    	}	
	    }

	    Arrays.sort(data, new ArrayComparator(nColumna-1, lAsc));  // ordenamos
	
		// ahora rellenamos y refrescamos ...
		DefaultTableModel model = (DefaultTableModel) tResult.getModel();
		model.setRowCount(0);
	
	    for(int i=0; i<data.length; i++) { //recorro las filas
			numPalabras++;
	        if(iColumn==2) model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numPalabras)), data[i][0].toString(), data[i][1].toString()});
	        else if(iColumn==3) model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numPalabras)), data[i][0].toString(), data[i][1].toString(), data[i][2].toString()});
	        else if(iColumn==4) model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numPalabras)), data[i][0].toString(), data[i][1].toString(), data[i][2].toString(), data[i][3].toString()});
	    }
	}

	public static void Order(JTable tResult, int nColumna, boolean lAsc) {
		// funcion que ordena tabla por la columna nColumna, sin límite de columnas
		int numPalabras=0;
		int iColumn=tResult.getModel().getColumnCount();
		int i,j;
		
		// recogemos los datos
		String[][] data = new String[tResult.getRowCount()][iColumn];
	    for(i=0; i<tResult.getRowCount(); i++) //recorro las filas
	    {
	    	for(j=0; j<iColumn;j++) {
	    		data[i][j] = tResult.getModel().getValueAt(i,j).toString();
	    	}	
	    }

	    Arrays.sort(data, new ArrayComparator(nColumna, lAsc));  // ordenamos
	
		// ahora rellenamos y refrescamos ...
		DefaultTableModel model = (DefaultTableModel) tResult.getModel();
		model.setRowCount(0);
	
        Vector<Object> Columnas;
        
		for(i=0; i<data.length; i++) { //recorro las filas
			numPalabras++;
			Columnas = new Vector<Object>();
			for(j=0; j<iColumn; j++) 	Columnas.add(data[i][j].toString());
			model.addRow(Columnas);
	    }
	}	
	
	public static String soloASCII(String input) {
	    //if(!input.contains("ç")) {  // si tiene cerilla no lo tocamos
	    	// Descomposición canónica
	    	String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
	    	// Nos quedamos únicamente con los caracteres ASCII
	    	Pattern pattern = Pattern.compile("\\P{ASCII}+");
	    	return pattern.matcher(normalized).replaceAll("");
	    //}
	    //else return input;
	} // fin soloASCII

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
	
	public static List<String> textFiles(String directory, String ext) {
		// función que devuelve los ficheros de un directorio con una extensión concreta
		List<String> textFiles = new ArrayList<String>();
		File dir = new File(directory);
		for (File file : dir.listFiles()) {
		   if (file.getName().endsWith((ext))) {
		      textFiles.add(file.getName());
		   }
		}
		return textFiles;
	}

}


class ArrayComparator implements Comparator<Comparable[]> {
    private final int columnToSort;
    private final boolean ascending;

    public ArrayComparator(int columnToSort, boolean ascending) {
        this.columnToSort = columnToSort;
        this.ascending = ascending;
    }

    public int compare(Comparable[] c1, Comparable[] c2) {
        int cmp = c1[columnToSort].compareTo(c2[columnToSort]);
        return ascending ? cmp : -cmp;
    }
   
}



class ModelConsulta extends DefaultTableModel
{
   public boolean isCellEditable (int row, int column)
   {
	   // no dejamos editar ninguna final ni columna
       return false;
   }
}





