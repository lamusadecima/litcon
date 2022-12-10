import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;



public class Wordlist {
	
	private Map<String, Integer> mWord;
	private char cOrdena;

	public int ListWords(File fEntrada, JTable tResult, JTextArea tExcluir, Boolean lTag, Boolean lMay, Boolean lMiles, Boolean lAscii) {

		try
		{
			float fMiles;
			mWord = g.RecogePalabras(fEntrada, tExcluir.getText(), lTag, lMay, lAscii);
			
			if(mWord != null) {
				DefaultTableModel model = new ModelConsulta(); //DefaultTableModel();
				model.addColumn(Idiomas.Orden[ventana.iId]);
				model.addColumn(Idiomas.Word[ventana.iId]);
				model.addColumn(Idiomas.Ocurren[ventana.iId]);
				if(lMiles) {
					model.addColumn(Idiomas.MilesCounts[ventana.iId]);					
				}
				tResult.setModel(model);
				tResult.getColumn(Idiomas.Orden[ventana.iId]).setMaxWidth(80);			
				tResult.getColumn(Idiomas.Ocurren[ventana.iId]).setMaxWidth(120);			

				// ordenamos por ocurrencias ...
				cOrdena = 'v';
				mWord = sortByValues(mWord, 1);  // por valores descendente
				//mWord = sortByValues(mWord, -1);   // por valores ascendente
				//mWord = new TreeMap<String, Integer>(mWord);  // alfabética ascendente
				//mWord = new TreeMap<String, Integer>(mWord).descendingMap();  // alfabética descendente

				// ahora pasamos las palabras a la tabla
				Set set = mWord.entrySet();
			    Iterator iterator = set.iterator();
			    int numPalabras=0;
			    while(iterator.hasNext()) {
			        Map.Entry mentry = (Map.Entry)iterator.next();
					numPalabras++;
					fMiles = (float) (int) mentry.getValue()*1000/g.numWords;
			        model.addRow(new Object[]{(String) String.format("%1$-6s",String.valueOf(numPalabras)), 
			        	mentry.getKey().toString(), (String) String.format("%1$6s",String.valueOf(mentry.getValue())), 
			        	(String) String.format("%10.2f%n",fMiles) });
			    }
				return numPalabras;
				
			} // fin if mWord != null
			else return 0;
		  }
		  catch(Exception ex)
		  {
		     JOptionPane.showMessageDialog(null,ex+"" + "\nError",
		           Idiomas.Warning[ventana.iId],JOptionPane.WARNING_MESSAGE);
		     return 0;
		  }
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
/*	
	public void Ordena(char cTipo, JTable tResult) {
		int numPalabras=0;
		
		// recogemos los datos
		String[][] data = new String[tResult.getRowCount()][2];
	    for(int i=0; i<tResult.getRowCount(); i++) //recorro las filas
	    {
	    	data[i][0] = tResult.getModel().getValueAt(i,1).toString();
	    	data[i][1] = tResult.getModel().getValueAt(i,2).toString();
	    }
	    
		if(cTipo=='A') {  // ordenación alfabética de claves
			if(cOrdena=='A') {
			    Arrays.sort(data, new ArrayComparator(0, false));  // alfabético descendente
				cOrdena ='a';
			}
			else {
			    Arrays.sort(data, new ArrayComparator(0, true));  // alfabético ascendente
				cOrdena = 'A';
			}
		}
		else {  // ordenación numérica de valores
			if(cOrdena=='V') {
			    Arrays.sort(data, new ArrayComparator(1, false));  // valores descendente
				cOrdena ='v';
			}
			else {
			    Arrays.sort(data, new ArrayComparator(1, true));  // valores ascendente
			    cOrdena = 'V';
			}
		}
		
		// ahora rellenamos y refrescamos ...
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Orden");
		model.addColumn("Palabra");
		model.addColumn("Ocurrencias");
		tResult.setModel(model);
		tResult.getColumn("Orden").setMaxWidth(80);			
		tResult.getColumn("Ocurrencias").setMaxWidth(120);			
	    for(int i=0; i<data.length; i++) { //recorro las filas
			numPalabras++;
	        model.addRow(new Object[]{String.valueOf(numPalabras), data[i][0].toString(), data[i][1].toString()});
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
*/
}
