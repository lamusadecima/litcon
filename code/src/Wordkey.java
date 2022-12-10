import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Wordkey {

	private Map<String, Integer> mKey1;
	private Map<String, Integer> mKey2;
	

	public int ListaClaves(File fKey1[], File fKey2[], JTable tKey, JTable tKey2, String sExKey, Boolean lTag, Boolean lMay, Boolean lAscii) {

		int a,b,c=0,d=0;
		double k, e1, e2;
		File fTemp = null;
		
		try
		{
			// comprobamos que se han seleccionado los ficheros ..
			if(fKey1 == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E11[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
			   return 0;
			}
			else if(fKey2 == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E12[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return 0;
			}

			//mKey1 = g.RecogePalabras(fKey1, sExKey, lTag, lMay, lAscii);
			if(fKey1.length>1) {  // si hay mas de uno preparamos el fichero temporal
				fTemp=new File("tempIn.txt");
				FileOutputStream out = new FileOutputStream(fTemp);
			    byte[] buf = new byte[100000];
			    for (File file : fKey1) {
			    	//System.out.println(file.getName());
			    	InputStream in = new FileInputStream(file);
			        int bx = 0;
			        while ( (bx = in.read(buf)) >= 0) {
			            out.write(buf, 0, bx);
			            out.flush();
			        }
			        in.close();
			    }
			    out.close();
				mKey1 = g.RecogePalabras(fTemp, sExKey, lTag, lMay, lAscii);
				fTemp.delete();
			}
			else mKey1 = g.RecogePalabras(fKey1[0], sExKey, lTag, lMay, lAscii);			
			
			if(fKey2.length>1) {  // si hay mas de uno preparamos el fichero temporal
				fTemp=new File("tempCorpus.txt");
				FileOutputStream out = new FileOutputStream(fTemp);
			    byte[] buf = new byte[100000];
			    for (File file : fKey2) {
			    	System.out.println(file.getName());
			    	InputStream in = new FileInputStream(file);
			        int bx = 0;
			        while ( (bx = in.read(buf)) >= 0) {
			            out.write(buf, 0, bx);
			            out.flush();
			        }
			        in.close();
			    }
			    out.close();
				mKey2 = g.RecogePalabras(fTemp, sExKey, lTag, lMay, lAscii);
				fTemp.delete();
			}
			else mKey2 = g.RecogePalabras(fKey2[0], sExKey, lTag, lMay, lAscii);

			if(mKey1 != null && mKey2 != null) {
				DefaultTableModel model = new ModelConsulta();
				model.addColumn(Idiomas.Orden[ventana.iId]);
				model.addColumn(Idiomas.Word[ventana.iId]);
				model.addColumn(Idiomas.Frecuency[ventana.iId]);
				model.addColumn(Idiomas.Key[ventana.iId]);
				tKey.setModel(model);
				//model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(1)), "prueba", "99"});
				tKey.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);			
				tKey.getColumn(Idiomas.Frecuency[ventana.iId]).setMaxWidth(150);	
				tKey.getColumn(Idiomas.Key[ventana.iId]).setMaxWidth(150);	

				DefaultTableModel model2 = new ModelConsulta();
				model2.addColumn(Idiomas.Orden[ventana.iId]);
				model2.addColumn(Idiomas.Word[ventana.iId]);
				model2.addColumn(Idiomas.Frecuency[ventana.iId]);				
				model2.addColumn(Idiomas.Key[ventana.iId]);
				tKey2.setModel(model2);
				tKey2.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);	
				tKey2.getColumn(Idiomas.Frecuency[ventana.iId]).setMaxWidth(150);					
				tKey2.getColumn(Idiomas.Key[ventana.iId]).setMaxWidth(150);	
				// ordenamos por ocurrencias ...
				mKey1 = new TreeMap<String, Integer>(mKey1);  // alfabética ascendente
				mKey2 = new TreeMap<String, Integer>(mKey2);  // alfabética ascendente
							
				// ahora realizamos los cálculos de los wordkeys
				Set set1 = mKey1.entrySet();
			    Iterator iterator1 = set1.iterator();
				Set set2 = mKey2.entrySet();
			    Iterator iterator2 = set2.iterator();
			    Map.Entry mentry1 = null;
			    Map.Entry mentry2 = null;

			    // primero obtenemos las sumas ...
			    while(iterator1.hasNext()) {
			        mentry1 = (Map.Entry)iterator1.next();
		    		c = c + (int) mentry1.getValue();
		    	}
			    while(iterator2.hasNext()) {
			        mentry2 = (Map.Entry)iterator2.next();
		    		d = d + (int) mentry2.getValue();
		    	}
		//System.out.println("c: " + String.valueOf(c) + " d: " + String.valueOf(d));		

				// ahora confrontamos los dos ficheros
	    		int num=0, num2=0; boolean lFirst=true;
	    		iterator1 = set1.iterator();
	    		iterator2 = set2.iterator();
	    		String sKey1 = null, sKey2 = null;

			    while(sKey1 != "====" || sKey2 != "====") {
			    	if(lFirst) {
			    		mentry1 = (Map.Entry) iterator1.next();
			    		sKey1= mentry1.getKey().toString();
			    		mentry2 = (Map.Entry) iterator2.next();
			    		sKey2= mentry2.getKey().toString();
			    		lFirst=false;
			    	}
			    	while(sKey1 != "====" && (sKey1.compareTo(sKey2)<0 || sKey2 == "====")) { // A < B
			    		//System.out.println("while 1|" + sKey1.toString() + "|" + sKey2.toString() + "|");
			    		//num1++;
				        //model1.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(num1)), sKey1, (String) String.format("%1$6s",String.valueOf(mentry1.getValue()))});
			        	a = (int) mentry1.getValue();
			        	b = 0;
			        	e1=(double) (c*(a+b))/(c+d);
			        	e2=(double) (d*(a+b))/(c+d);
		        		k = 2*(a*Math.log(a/e1));  // funcion log-likelihood
			        	//System.out.println(sKey1 + " a: " + String.valueOf(a) + " b: " + String.valueOf(b) + " k: " + String.valueOf(k));		
			        	num++;
		        		model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(num)), sKey1, (String) String.valueOf(a) + "/" + String.valueOf(b), (String) String.format("%10.3f%n",k)});
			    		if(iterator1.hasNext()) {
				        	mentry1 = (Map.Entry) iterator1.next();
				        	sKey1= mentry1.getKey().toString();
				        }
				        else sKey1= "====";
			    	}
			        while(sKey2 != "====" && (sKey1.compareTo(sKey2)>0 || sKey1 == "====")) {  // A > B
			        	//System.out.println("while 2|" + sKey1.toString() + "|" + sKey2.toString() + "|");
			        	if(iterator2.hasNext()) {
				        	mentry2 = (Map.Entry) iterator2.next();
				        	sKey2= mentry2.getKey().toString();
				        }
				        else sKey2= "====";
			        }
			        while(sKey1 != "====" && sKey1.compareTo(sKey2)==0) {  // A = B
			        	//System.out.println("while 3|" + sKey1.toString() + "|");
			        	a = (int) mentry1.getValue();
			        	b = (int) mentry2.getValue();
			        	e1=(double) (c*(a+b))/(c+d);
			        	e2=(double) (d*(a+b))/(c+d);
			        	//System.out.println(sKey1 + " e1: " + String.valueOf(e1) + " e2: " + String.valueOf(e2));
		        		k = 2*((a*Math.log(a/e1)) + (b*Math.log(b/e2)));  // funcion log-likelihood
			        	//System.out.println(sKey1 + " a: " + String.valueOf(a) + " b: " + String.valueOf(b) + " k: " + String.valueOf(k));		
			        	if(Math.log(a/e1)<0) {  // clave negativa
				        	num2++;
			        		model2.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(num2)), sKey1, (String) String.valueOf(a) + "/" + String.valueOf(b), (String) String.format("%10.3f%n",k)});
			        	}
			        	else {  // clave positiva
				        	num++;
			        		model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(num)), sKey1, (String) String.valueOf(a) + "/" + String.valueOf(b), (String) String.format("%10.3f%n",k)});
			        	}
			        	if(iterator1.hasNext()) {
				        	mentry1 = (Map.Entry) iterator1.next();
				        	sKey1= mentry1.getKey().toString();
				        }
				        else sKey1= "====";
				        if(iterator2.hasNext()) {
				        	mentry2 = (Map.Entry) iterator2.next();
				        	sKey2= mentry2.getKey().toString();
				        }
				        else sKey2= "====";
			        }
			    }  // fin del while de los dos ficheros
 
   				return 1;
	
			} // fin if mKey1 y mKey2 != null
			else return 0;
		 }
		 catch(Exception ex)
		 {
		     JOptionPane.showMessageDialog(null,ex+"" + "\nError",
		           Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
		     return 0;
		 }
	}
}

