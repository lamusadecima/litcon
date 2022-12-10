import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class Exclusiones { /* Opción Contraste */

	private Map<String, Integer> mCon1;
	private Map<String, Integer> mCon2;
	
	public int Contraste(File[] fCon1, File[] fCon2, JTable tCon1, JTable tCon2, JTable tCon3, String sExCon, Boolean lTag, Boolean lMay, Boolean lAscii) {

		File fTemp = null;
		
		try
		{
			// comprobamos que se han seleccionado los fichero ..
			if(fCon1 == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E11[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return 0;
			}
			else if(fCon2 == null) {
				   JOptionPane.showMessageDialog(null, Idiomas.E12[ventana.iId], Idiomas.Warning[ventana.iId], JOptionPane.WARNING_MESSAGE);
				   return 0;
			}			
			
			//mCon1 = g.RecogePalabras(fCon1, sExCon, lTag, lMay, lAscii);
			if(fCon1.length>1) {  // si hay mas de uno preparamos el fichero temporal
				fTemp=new File("temp.txt");
				FileOutputStream out = new FileOutputStream(fTemp);
			    byte[] buf = new byte[100000];
			    for (File file : fCon1) {
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
				mCon1 = g.RecogePalabras(fTemp, sExCon, lTag, lMay, lAscii);
				fTemp.delete();
			}
			else mCon1 = g.RecogePalabras(fCon1[0], sExCon, lTag, lMay, lAscii);		
			
			//mCon2 = g.RecogePalabras(fCon2, sExCon, lTag, lMay, lAscii);
			if(fCon2.length>1) {  // si hay mas de uno preparamos el fichero temporal
				fTemp=new File("temp2.txt");
				FileOutputStream out = new FileOutputStream(fTemp);
			    byte[] buf = new byte[100000];
			    for (File file : fCon2) {
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
				mCon2 = g.RecogePalabras(fTemp, sExCon, lTag, lMay, lAscii);
				fTemp.delete();
			}
			else mCon2 = g.RecogePalabras(fCon2[0], sExCon, lTag, lMay, lAscii);		
			
			if(mCon1 != null && mCon2 != null) {
				DefaultTableModel model1 = new ModelConsulta();
				model1.addColumn(Idiomas.Orden[ventana.iId]);
				model1.addColumn(Idiomas.Word[ventana.iId]);
				model1.addColumn(Idiomas.Ocurren[ventana.iId]);
				tCon1.setModel(model1);
				tCon1.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);			
				tCon1.getColumn(Idiomas.Ocurren[ventana.iId]).setMaxWidth(120);	

				DefaultTableModel model2 = new ModelConsulta();
				model2.addColumn(Idiomas.Orden[ventana.iId]);
				model2.addColumn(Idiomas.Word[ventana.iId]);
				model2.addColumn(Idiomas.Ocurren[ventana.iId]);
				tCon2.setModel(model2);
				tCon2.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);			
				tCon2.getColumn(Idiomas.Ocurren[ventana.iId]).setMaxWidth(120);			

				DefaultTableModel model3 = new ModelConsulta();
				model3.addColumn(Idiomas.Orden[ventana.iId]);
				model3.addColumn(Idiomas.Word[ventana.iId]);
				model3.addColumn(Idiomas.Ocurren[ventana.iId]);
				tCon3.setModel(model3);
				tCon3.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);			
				tCon3.getColumn(Idiomas.Ocurren[ventana.iId]).setMaxWidth(120);			
				
				// ordenamos por ocurrencias ...
				mCon1 = new TreeMap<String, Integer>(mCon1);  // alfabética ascendente
				mCon2 = new TreeMap<String, Integer>(mCon2);  // alfabética ascendente
				//mCon1 = sortByValues(mCon1, 1);  // por valores descendente
				//mWord = sortByValues(mWord, -1);   // por valores ascendente
				//mWord = new TreeMap<String, Integer>(mWord).descendingMap();  // alfabética descendente

				// ahora confrontamos los dos ficheros
				Set set1 = mCon1.entrySet();
			    Iterator iterator1 = set1.iterator();
				Set set2 = mCon2.entrySet();
			    Iterator iterator2 = set2.iterator();

			    int num1=0, num2=0, num3=0; boolean lFirst=true;
			    Map.Entry mentry1 = null;
			    Map.Entry mentry2 = null;
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
			    		num1++;
				        model1.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(num1)), sKey1, (String) String.format("%1$6s",String.valueOf(mentry1.getValue()))});
				        if(iterator1.hasNext()) {
				        	mentry1 = (Map.Entry) iterator1.next();
				        	sKey1= mentry1.getKey().toString();
				        }
				        else sKey1= "====";
				        // System.out.println("while 1b|" + sKey1.toString() + "|" + sKey2.toString() + "|");
				        // System.out.println("while 1c|" + String.valueOf(Character.codePointAt(sKey1, 0)) + "|" + String.valueOf(Character.codePointAt(sKey2, 0)) + "|");
			    	}
			        while(sKey2 != "====" && (sKey1.compareTo(sKey2)>0 || sKey1 == "====")) {  // A > B
			        	// System.out.println("while 2|" + sKey1.toString() + "|" + sKey2.toString() + "|");
			        	num2++;
				        model2.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(num2)), sKey2, (String) String.format("%1$6s",String.valueOf(mentry2.getValue()))});
				        if(iterator2.hasNext()) {
				        	mentry2 = (Map.Entry) iterator2.next();
				        	sKey2= mentry2.getKey().toString();
				        }
				        else sKey2= "====";
			        	// System.out.println("while 2b|" + sKey1.toString() + "|" + sKey2.toString() + "|");
				        // System.out.println("while 2c|" + String.valueOf(Character.codePointAt(sKey1, 0)) + "|" + String.valueOf(Character.codePointAt(sKey2, 0)) + "|");
			        }
			        while(sKey1 != "====" && sKey1.compareTo(sKey2)==0) {  // A = B
			        	// System.out.println("while 3|" + sKey1.toString() + "|");
			        	num3++;
				        model3.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(num3)), sKey1, (String) String.format("%1$6s",String.valueOf(mentry1.getValue())) +"/" + String.valueOf(mentry2.getValue())});
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
				
			} // fin if mCon1 y mCon2 != null
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
