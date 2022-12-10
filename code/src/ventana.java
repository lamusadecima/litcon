import java.awt.EventQueue;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import java.lang.String;
import java.math.RoundingMode;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.JProgressBar;
import java.awt.Rectangle;
import javax.swing.JEditorPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JRadioButton;

public class ventana {

	// idiomas
	public String[] paises = {"Español", "English"};
	public ImageIcon[] iconos = new ImageIcon[paises.length];;
	public static int iId;
	// Busquedas	
	public JFrame frmConcord;
	private JTextField tFileView;
	private JTextField tFileTxt;
	private JTextField tBuscar;
	private JTable tResult;
	//	private String sBusqueda;
	private Collection<String> cPalabras;
	private JTextField tFileIn;
	private JTextField tFileOut;
	private JTable tPalabras;
	private File fVer = null;
	private File fBusca = null;
	private File fEntrada = null;
	private File fSalida = null;
	private File fRet=null;
	private File[] fRets=null;
	private int totPalabras=0;
	JProgressBar prBarra;
	BuscarConcordancias BC;
	private int iMaxLin=0;
	private String sTexto; // visualización de fichero
	private int iBusca=0; // posición desde la última busqueda
	private String sTextoHTML; // visualización de fichero coloreado
	private JDialog dialog;
	private Thread thTrabajo=new Thread();
	private JTextField txBuscaTexto;
	private char cOrden; // A albafetico asc, a desc, V valores asc, v desc 
	// WordList
	Wordlist WL;
	private JTextField tFileWord;
	private File fWord=null; 
	private JTable tWords;	
	private int totWords=0;
	private JTextField txBuscWord;
	private String sBuscWord;
	// Exclusiones
	private JTextField tFileCon1;
	private File[] fCon1=null; 
	private JTable tCon1;	
	private JTextField tFileCon2;
	private File[] fCon2=null; 
	private JTable tCon2;	
	private JTable tCon3;	
	private JTextField tExCon;
	private File fExCon=null;
	private String sExCon="";
	// Wordkey
	private JTextField tFileKey1;
	private File[] fKey1=null; 
	private JTextField tFileKey2;
	private File[] fKey2=null; 
	private JTable tKey;	
	private JTable tKey2;	
	private JTextField tExKey;
	private File fExKey=null;
	private String sExKey="";	
	// Label
	private File fLb1, fLb2, fLb3;
	private JTable tLabel, tLabel2;
	JProgressBar prLabel;
	Label LB;
	JCheckBox chLabelAscii;
	private JTextField tMaxLabel;
	// Label 2
	private File fLbF1, fLbF2, fLbF3;
	Label2 LB2;
	// Stylometry	
	private File fSt1;
	private JTable tStylo;
	Stylo ST;
	JCheckBox chStyloAscii;
	private JTextField tStRel1, tStRel2, tStSig, tStCont;	
	float fMiles;
	private JTextField tFileSt1;
	// Stylometry II
	Stylo2 ST2;
	private File[] fSt2=null; 
	//private File fSt2;
	private JTextField tSt2In;
	private Boolean lSt2Item=true;
	// Stylometry III
	Stylo3 ST3;
	private List<File> fSt3 = new ArrayList<>();
	private JTextField tSt3In;
	private String sMFiles = null;
	// Corpus 
	Corpus CO;
	private File fCorpus1=null; 
	private File fCorpus2=null; 
	private JTextField tCorpus1;
	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ventana w = new ventana();
					w.frmConcord.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ventana() {

		iId=0; // por defecto español
		frmConcord = new JFrame();
		frmConcord.setTitle("LitCon 1.5 Literary Concordances");
		frmConcord.setBounds(100, 100, 1345, 773);
		frmConcord.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConcord.getContentPane().setLayout(null);
		//ImageIcon img = new ImageIcon("/images/concord.ico");
		//frmConcord.setIconImage(img.getImage()); 
		//frmConcord.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/concord.ico")));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if(screenSize.width<1900 || screenSize.height<1000) {
			frmConcord.pack();
			frmConcord.setSize(screenSize.width,screenSize.height);
		}
		initialize();
	}



	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		iconos[0] = new ImageIcon(ventana.class.getResource("/images/es.png"));
		iconos[1] = new ImageIcon(ventana.class.getResource("/images/en.png"));
		Integer[] intArray = new Integer[paises.length];
		for (int i = 0; i < paises.length; i++) {
			intArray[i] = new Integer(i);
			iconos[i].setDescription(paises[i]);
		}

		JComboBox cbLanguage = new JComboBox(intArray);
		ComboBoxRenderer renderer = new ComboBoxRenderer();
		//renderer.setPreferredSize(new Dimension(200, 130));
		cbLanguage.setRenderer(renderer);
		cbLanguage.setBounds(1177, 2, 124, 39);
		cbLanguage.setSelectedIndex(iId);
		frmConcord.getContentPane().add(cbLanguage);

		cbLanguage.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				iId = cbLanguage.getSelectedIndex();
				//System.out.println(String.valueOf(iId));
				frmConcord.getContentPane().removeAll();
				initialize();
			}
		});

		/*		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1204, 30);
		frmConcord.getContentPane().add(menuBar);

		JMenu mnBusquedas = new JMenu("Idiomas");
		menuBar.add(mnBusquedas);

		JMenuItem mntmBusqueda = new JMenuItem("Busqueda");
		mntmBusqueda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			  Buscar b = new Buscar();
			  b.setVisible (true); 
		    }
		});
		mnBusquedas.add(mntmBusqueda);

		JMenu mnConcordancias = new JMenu("Concordancias");
		menuBar.add(mnConcordancias);

		JMenuItem mntmBusquedaSimple = new JMenuItem("Listado Concordancias");
		mntmBusquedaSimple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  Concordancias c = new Concordancias();
				  c.setVisible (true); 
			}
		});
		mnConcordancias.add(mntmBusquedaSimple);

		JMenu mnVisualizar = new JMenu("Visualizar");
		menuBar.add(mnVisualizar);

		JMenuItem mntmNewMenuItem = new JMenuItem("Ver Fichero");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  Visualizar v = new Visualizar();
				  v.setVisible (true); 
			}
		});		
		mnVisualizar.add(mntmNewMenuItem);
		 */	

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tabbedPane.setBounds(0, 0, 1279, 702);
		//if(frmConcord.getHeight()<800) {
		//	tabbedPane.setBounds(44, 50, 1279, frmConcord.getHeight()-260);
		//}
		frmConcord.getContentPane().add(tabbedPane);



		// tab Créditos ---------------------------------------------
		JPanel pInicio = new JPanel();
		pInicio.setForeground(Color.BLUE);
		tabbedPane.addTab(Idiomas.tabula[iId][0], null, pInicio, null);
		tabbedPane.setForegroundAt(0, new Color(0, 128, 0));
		pInicio.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setBounds(526, 59, 646, 597);
		pInicio.add(lblNewLabel);
		lblNewLabel.setIcon(new ImageIcon(ventana.class.getResource("/images/LogoUSTransparente.gif")));

		JLabel lbIcono = new JLabel("");
		lbIcono.setBounds(27, 59, 64, 64);
		pInicio.add(lbIcono);
		lbIcono.setIcon(new ImageIcon(ventana.class.getResource("/images/LitCon.gif")));

		JLabel lbNombre = new JLabel("LitCon 1.5 Literary Concordances");
		lbNombre.setFont(new Font("Georgia", Font.BOLD, 18));
		lbNombre.setBounds(114, 76, 318, 47);
		pInicio.add(lbNombre);


		JTextArea txtrLauraHernndezLorenzo = new JTextArea();
		txtrLauraHernndezLorenzo.setEditable(false);
		txtrLauraHernndezLorenzo.setFont(new Font("Constantia", Font.PLAIN, 15));
		txtrLauraHernndezLorenzo.setForeground(Color.BLUE);
		txtrLauraHernndezLorenzo.setBackground(SystemColor.menu);
		txtrLauraHernndezLorenzo.setText("Desarrollado por:\r\nLaura Hern\u00E1ndez Lorenzo\r\nDoctoranda FPU\r\nDepartamento de Literatura Espa\u00F1ola e Hispanoamericana\r\nUniversidad de Sevilla, Espa\u00F1a\r\nlhernandez1@us.es\r\nProgramador: Luis Hern\u00E1ndez Molin\u00ED\r\n\r\nDeveloped by:\r\nLaura Hern\u00E1ndez Lorenzo\r\nPhD candidate in Spanish Literature\r\nFPU Researcher\r\nDepartment of Spanish Literature\r\nUniversity of Seville, Spain\r\nlhernandez1@us.es\r\nProgrammer: Luis Hern\u00E1ndez Molin\u00ED");
		txtrLauraHernndezLorenzo.setBounds(27, 176, 396, 325);
		pInicio.add(txtrLauraHernndezLorenzo);


		// tab Visualizar ----------------------------------------------
		JPanel pVisualizar = new JPanel();
		tabbedPane.addTab(Idiomas.tabula[iId][1], null, pVisualizar, Idiomas.tFiletoView[iId]);
		tabbedPane.setForegroundAt(1, new Color(0, 128, 0));
		pVisualizar.setLayout(null);

		JLabel label = new JLabel(Idiomas.FileView[iId] + ":");
		label.setBounds(35, 24, 126, 19);
		label.setForeground(Color.BLUE);
		label.setFont(new Font("Tahoma", Font.PLAIN, 15));
		pVisualizar.add(label);

		tFileView = new JTextField();
		tFileView.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileView.setForeground(Color.RED);
		tFileView.setBounds(192, 23, 488, 22);
		tFileView.setEditable(false);
		tFileView.setColumns(10);
		pVisualizar.add(tFileView);

		JLabel lblFormato = new JLabel(Idiomas.Format[iId]);
		lblFormato.setBounds(635, 622, 56, 16);
		pVisualizar.add(lblFormato);

		JLabel lbFormato = new JLabel("");
		lbFormato.setForeground(Color.RED);
		lbFormato.setBounds(703, 622, 97, 16);
		pVisualizar.add(lbFormato);

		JLabel lblBom = new JLabel("BOM:");
		lblBom.setBounds(841, 622, 39, 16);
		pVisualizar.add(lblBom);

		JLabel lbBOM = new JLabel("");
		lbBOM.setForeground(Color.RED);
		lbBOM.setBounds(888, 622, 80, 16);
		pVisualizar.add(lbBOM);

		JLabel lbInfo = new JLabel("");
		lbInfo.setForeground(Color.RED);
		lbInfo.setBounds(245, 706, 151, 16);
		pVisualizar.add(lbInfo);

		JButton bCancelar = new JButton(Idiomas.Cancel[iId]);
		bCancelar.setBounds(416, 618, 97, 25);
		pVisualizar.add(bCancelar);
		bCancelar.setVisible(false);

		JButton button = new JButton(Idiomas.Select[iId]);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fVer = fRet;
						tFileView.setText(fVer.getName());	
						sTexto=null;
						sTextoHTML=null;
						// si no hay fichero seleccionado en los demas lo ponemos
						if(fEntrada == null) {
							fEntrada = fVer;
							tFileIn.setText(fEntrada.getName());
						}
						if(fBusca == null) {
							fBusca = fVer;
							tFileTxt.setText(fBusca.getName());
						}	
						if(fWord == null) {
							fWord = fVer;
							tFileWord.setText(fWord.getName());
						}	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		button.setBounds(711, 22, 117, 25);
		pVisualizar.add(button);

		JScrollPane scVisor = new JScrollPane();
		scVisor.setBounds(224, 100, 940, 505);
		pVisualizar.add(scVisor);
		JEditorPane edTexto = new JEditorPane();
		scVisor.setViewportView(edTexto);
		edTexto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		edTexto.setEditable(false);

		JButton btnVisualizar = new JButton(Idiomas.View[iId]);
		btnVisualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(fVer != null && !thTrabajo.isAlive()) { // si se ha seleccionado fichero y no hay nada trabajando
					try {
						StringBuffer sB = new StringBuffer();
						String lin;
						int ilin=0;
						BufferedReader in = g.TipoFichero(fVer);
						lbFormato.setText(g.sTipFile);
						lbBOM.setText(g.sBOM);
						while ((lin = in.readLine()) != null) {
							sB.append( lin);
							sB.append( "\r\n" );
							ilin++;
						}
						//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						//Date date;

						// coloreamos ?
						//if(sB.indexOf("_")>0) {
						//	sB = Visualizar.Colorear(sB);
						//	edTexto.setContentType("text/html");
						//}
						//else {

						//date = new Date();
						//System.out.println(dateFormat.format(date));								
						//}
						thTrabajo = new Thread() {
							public void run() {
								bCancelar.setVisible(true);
								lbInfo.setText(Idiomas.LoadingFile[iId]);
								edTexto.setContentType("text/plain");
								sTexto = sB.toString();	
								edTexto.setText(sTexto);
								edTexto.setCaretPosition(0);  // nos posicionamos al principio
								lbInfo.setText(Idiomas.FileLoaded[iId]);
								bCancelar.setVisible(false);
							}
						};
						thTrabajo.start();
						//date = new Date();
						//System.out.println(dateFormat.format(date));
						//System.out.println(String.valueOf(ilin));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}  // fin if
				else if(thTrabajo.isAlive()) {
					JOptionPane.showMessageDialog(null,Idiomas.E1[iId], Idiomas.Warning[iId],JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnVisualizar.setBounds(45, 95, 97, 25);
		pVisualizar.add(btnVisualizar);
		/*		
		JButton bColorear = new JButton(Idiomas.Colorear[iId]);
		bColorear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// miramos si hay etiquetas
				boolean lPro=false;
				int nP=0;
				while((nP=edTexto.getText().substring(nP).indexOf("_"))>0) {
					if(Character.isLetter(edTexto.getText().charAt(nP+1))) {
						lPro=true;
						break;
					}
					else nP++;
				}
				if(lPro && !thTrabajo.isAlive()) { // si hay y no hay un proceso activo seguimos ...
					thTrabajo = new Thread() {
						   public void run() {
							    bCancelar.setVisible(true);
							    lbInfo.setText(Idiomas.ProcessingFile[iId]);
								StringBuffer sB = new StringBuffer(edTexto.getText());
							    sB = Visualizar.Colorear(sB);
								edTexto.setContentType("text/html");
								sTextoHTML=sB.toString();
								edTexto.setText(sTextoHTML);
								lbInfo.setText(Idiomas.FileProcessed[iId]);
							    bCancelar.setVisible(false);
						   }
					    };
					thTrabajo.start();
				} // fin de if
				else if(thTrabajo.isAlive()) {
					JOptionPane.showMessageDialog(null,Idiomas.E1[iId],Idiomas.Warning[iId],JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bColorear.setBounds(45, 179, 97, 25);
		pVisualizar.add(bColorear);
		 */		
		JButton btnNewButton = new JButton(Idiomas.HideLabel[iId]);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sT = new String(sTexto);
				// ahora quitamos etiquetas
				sT = g.quitarEtiquetasNum(sT);
				edTexto.setContentType("text/plain");
				edTexto.setText(sT.toString());

			}
		});
		btnNewButton.setBounds(30, 263, 146, 25);
		pVisualizar.add(btnNewButton);

		txBuscaTexto = new JTextField();
		txBuscaTexto.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				iBusca=0;
				//System.out.println("focus gained");
			}
		});
		txBuscaTexto.setBounds(888, 23, 146, 22);
		pVisualizar.add(txBuscaTexto);
		txBuscaTexto.setColumns(10);

		JButton bBuscaTexto = new JButton(Idiomas.Seek[iId]);
		bBuscaTexto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  // buscamos en el fichero un texto ...
				try {
					if(sTexto != null) {
						//System.out.println("a:" + String.valueOf(iBusca) + txBuscaTexto.getText());						
						int found = sTexto.substring(iBusca).toUpperCase().indexOf(txBuscaTexto.getText().toUpperCase());
						//System.out.println("b:" + String.valueOf(found));						
						int pos=0;
						if(found>-1) { 
							pos=iBusca + found;  // posicion absoluta
							iBusca=pos + txBuscaTexto.getText().length(); // lo guardamos para la próxima busqueda
							if(iBusca<0) iBusca=0;
							//System.out.println("c:" + String.valueOf(iBusca) + "|" + sTexto.substring(pos,pos+10));
							int newline = sTexto.substring(0,pos).split("\r",-1).length-1;
							pos = pos - newline;  // le quitamos los retornos de carro
							edTexto.setCaretPosition(pos);
							edTexto.moveCaretPosition(pos + txBuscaTexto.getText().length());
							Rectangle rView = edTexto.modelToView(pos); // muevo la línea al centro aprox.
							rView.setSize((int)rView.getWidth(),(int)rView.getHeight() + 300);
							edTexto.scrollRectToVisible(rView);
							edTexto.requestFocusInWindow();
						}
						else if(iBusca>0) iBusca=0;  //  si hemos llegado al final empezamos por el principio
					}
					else
						JOptionPane.showMessageDialog(null,Idiomas.E2[iId],Idiomas.Warning[iId],JOptionPane.WARNING_MESSAGE);						
				} catch (Exception ex) {
					iBusca=0;
					ex.printStackTrace();
				}	
			}
		});
		bBuscaTexto.setBounds(1060, 22, 97, 25);
		pVisualizar.add(bBuscaTexto);
		
		// botón oculto para llamara a funciones de emergencia
		/*
		JButton btnOtros = new JButton("Otros");
		btnOtros.setVisible(false);
		btnOtros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Visualizar.Etiquetas(fVer);
			}
		});
		btnOtros.setBounds(45, 170, 97, 25);
		pVisualizar.add(btnOtros);
		*/
		
		// botón oculto para coger la segunda palabra de una salida de freeling para lematizar
		/*JButton btnOtros = new JButton("Lematizar");
		btnOtros.setVisible(false);
		btnOtros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Visualizar.Lematizar(fVer);
			}
		});
		btnOtros.setBounds(45, 170, 97, 25);
		pVisualizar.add(btnOtros);*/
		
		// botón oculto para etiquetar TI
		/*JButton btnTI = new JButton("Etiq TI");
		btnTI.setVisible(false);
		btnTI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Visualizar.TI(fVer);
			}
		});
		btnTI.setBounds(45, 170, 97, 25);
		pVisualizar.add(btnTI);*/
				
		// botón oculto para etiquetar ET2 masiva
		/*JButton btnET2 = new JButton("ET2 masiva");
		btnET2.setVisible(true);
		btnET2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// obtenemos el total de ficheros txt del directorio
				String sF="";
				Label2 Lb2b = new Label2();
				String sDir="d:/temp/freeling";
				List<String> tFiles = g.textFiles(sDir, ".txt");
				for (int i = 0; i < tFiles.size(); i++) {
					if(tFiles.get(i).indexOf("freeling")==-1) {
						int j = tFiles.get(i).indexOf(".txt");
						sF=tFiles.get(i).substring(0,j);
						if(new File(sDir + "/" + sF + "_freeling.txt") == null) {
							System.out.println(sDir + "/" + sF + "_freeling.txt");
						}
						else {
							Lb2b.Freeling(new File(sDir + "/" + tFiles.get(i)), 
								new File(sDir + "/" + sF + "_freeling.txt"), 
								new File(sDir + "/" + sF + "_out.txt"));
						}
					}
				}
				JOptionPane.showMessageDialog(null,"Fin",Idiomas.Warning[iId],JOptionPane.WARNING_MESSAGE);						
			}
		});
		btnET2.setBounds(45, 170, 97, 25);
		pVisualizar.add(btnET2);*/

		
		/*
		Visualizar VI = new Visualizar();
		JButton btnDescomponer = new JButton("Descomponer");
		btnDescomponer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VI.Descomponer(fEntrada);
			}
		});
		btnDescomponer.setBounds(45, 342, 116, 25);
		pVisualizar.add(btnDescomponer);
		 */
		bCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(thTrabajo.isAlive()) {
					thTrabajo.interrupt();
					lbInfo.setText(Idiomas.ProccesInterrupt[iId]);
					bCancelar.setVisible(false);
				}
			}
		});


		// tab Concordancias (BuscarEnArchivo.java) ------------------------------------------------
		BuscarEnArchivo BA = new BuscarEnArchivo();

		JPanel pBuscar = new JPanel();
		tabbedPane.addTab(Idiomas.tabula[iId][2], null, pBuscar, null);
		tabbedPane.setForegroundAt(2, new Color(0, 128, 0));
		pBuscar.setLayout(null);

		JLabel label_1 = new JLabel("1. " + Idiomas.FileInput[iId]);
		label_1.setBounds(38, 30, 145, 19);
		label_1.setForeground(Color.BLUE);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		pBuscar.add(label_1);

		tFileTxt = new JTextField();
		tFileTxt.setToolTipText(Idiomas.tFileInput[iId]);
		tFileTxt.setBounds(209, 27, 685, 25);
		tFileTxt.setForeground(Color.RED);
		tFileTxt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileTxt.setEditable(false);
		tFileTxt.setColumns(10);
		pBuscar.add(tFileTxt);

		JButton button_1 = new JButton(Idiomas.Select[iId]);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fBusca = fRet;
						tFileTxt.setText(fBusca.getName());	
						// si no hay fichero selecionado en los demas ponemos éste
						if(fEntrada == null) {
							fEntrada = fBusca;
							tFileIn.setText(fEntrada.getName());
						}
						if(fVer == null) {
							fVer = fBusca;
							tFileView.setText(fVer.getName());
						}						
						if(fWord == null) {
							fWord = fBusca;
							tFileWord.setText(fWord.getName());
						}	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		button_1.setBounds(925, 28, 145, 25);
		pBuscar.add(button_1);

		JLabel label_2 = new JLabel(Idiomas.ColShow[iId]);
		label_2.setForeground(Color.BLUE);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label_2.setBounds(12, 249, 156, 19);
		pBuscar.add(label_2);

		JCheckBox lNomSoneto = new JCheckBox(Idiomas.Poem[iId]);
		lNomSoneto.setToolTipText(Idiomas.tPoem[iId]);
		lNomSoneto.setForeground(Color.BLUE);
		lNomSoneto.setBounds(12, 277, 156, 25);
		pBuscar.add(lNomSoneto);

		//JCheckBox chEtiquetasBuscar = new JCheckBox(Idiomas.HideLabel[iId]);
		//chEtiquetasBuscar.setBounds(812, 28, 137, 25);
		//pBuscar.add(chEtiquetasBuscar);

		JCheckBox chBuscarMay = new JCheckBox(Idiomas.Mayusculas[iId]);
		chBuscarMay.setForeground(Color.BLUE);
		chBuscarMay.setBounds(12, 171, 157, 25);
		pBuscar.add(chBuscarMay);
		
		JLabel label_3 = new JLabel("2. " + Idiomas.Seek[iId] + ":");
		label_3.setForeground(Color.BLUE);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label_3.setBounds(12, 92, 91, 25);
		pBuscar.add(label_3);

		tBuscar = new JTextField();
		tBuscar.setToolTipText(Idiomas.tSeek[iId]);
		/*
		tBuscar.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				sBusqueda = new String( tBuscar.getText() );
			}
		});
		 */						
		tBuscar.setColumns(10);
		tBuscar.setBounds(12, 134, 116, 22);
		pBuscar.add(tBuscar);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(209, 136, 942, 491);
		pBuscar.add(scrollPane_1);		
		tResult = new JTable();
		tResult.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !thTrabajo.isAlive()) {  // doble click, pasamos a Visualizar y buscamos la linea en el fichero
					try {
						tabbedPane.setSelectedIndex(1);  // cambio a la carpeta de visualizar
						//frmConcord.repaint();
						thTrabajo = new Thread() {
							public void run() {
								try {
									String sCelda = (String) tResult.getModel().getValueAt(tResult.getSelectedRow(), 3);
									sCelda = g.quitarHTML(sCelda);
									if(fVer != fBusca || edTexto.getText().trim().length() == 0) {  // si es un fichero diferente, hay que cargarlo
										fVer = fBusca;
										tFileView.setText(fVer.getName());
										StringBuffer sB = new StringBuffer();
										String lin;
										BufferedReader in = g.TipoFichero(fVer);
										lbFormato.setText(g.sTipFile);
										lbBOM.setText(g.sBOM);
										while ((lin = in.readLine()) != null) {
											sB.append( lin);
											sB.append( "\r\n" );
										}	  						

										bCancelar.setVisible(true);
										lbInfo.setText(Idiomas.LoadingFile[iId]);
										edTexto.setContentType("text/plain");
										sTexto = sB.toString();	
										edTexto.setText(sTexto);
										edTexto.setCaretPosition(0);  // nos posicionamos al principio
										lbInfo.setText(Idiomas.FileLoaded[iId]);
										bCancelar.setVisible(false);
									}  // fin if

									// seleccionamos la línea en cuestión 
									int pos = sTexto.indexOf(sCelda.toString());
									//System.out.println(sCelda + "|" + sTexto.substring(0,20) + "|" + String.valueOf(pos));
									if(pos>-1) {
										int newline = sTexto.substring(0,pos).split("\r",-1).length-1;
										pos = pos - newline;  // le quitamos los retornos de carro
										edTexto.setCaretPosition(pos);
										edTexto.moveCaretPosition(pos + sCelda.length());
										Rectangle rView;
										rView = edTexto.modelToView(pos);
										rView.setSize((int)rView.getWidth(),(int)rView.getHeight() + 300);
										edTexto.scrollRectToVisible(rView);
										edTexto.requestFocusInWindow();
									}

								} catch (Exception e) {
									e.printStackTrace();
								} // muevo la línea al centro aprox.
							}
						};
						thTrabajo.start();
						//							while(thTrabajo.isAlive()); // esperamos que acabe
					} catch (Exception ex) {
						ex.printStackTrace();
					}	
				}
			}
		});
		tResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tResult.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane_1.setViewportView(tResult);
		//tResult.setDefaultRenderer(Object.class, new MyCellRenderer());

		JLabel lbEncontradas = new JLabel("0 " + Idiomas.Founds[iId]);
		lbEncontradas.setHorizontalAlignment(SwingConstants.RIGHT);
		lbEncontradas.setForeground(Color.RED);
		lbEncontradas.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbEncontradas.setBounds(738, 97, 227, 20);
		pBuscar.add(lbEncontradas);		

		int iFound=0;
		JButton bEjecutar = new JButton("4. " + Idiomas.Execute[iId]);
		bEjecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iMaxLin = BA.Buscar(tBuscar.getText(), lNomSoneto.isSelected(), tResult, fBusca, chBuscarMay.isSelected());
				if(iMaxLin>0) {
					tResult.getColumn(Idiomas.Verso[iId]).setPreferredWidth(iMaxLin*8);
					lbEncontradas.setText(String.valueOf(g.iWordsFound) + " " + Idiomas.Founds[iId]);
					//System.out.println(String.valueOf(iMaxLin));
				}
				else
					lbEncontradas.setText("0 " + Idiomas.Founds[iId]);
			}
		});
		bEjecutar.setForeground(Color.BLUE);
		bEjecutar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bEjecutar.setBounds(26, 353, 142, 25);
		pBuscar.add(bEjecutar);

		JButton bGuardarTxt = new JButton("5. " + Idiomas.SaveTxt[iId]);
		bGuardarTxt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bGuardarTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tResult.getRowCount()>0) {
					g.GuardarFichero('t', tResult, null);
				} else {
					JOptionPane.showMessageDialog(null,Idiomas.E3[iId],Idiomas.Warning[iId],JOptionPane.WARNING_MESSAGE);					
				}		
			}
		});
		bGuardarTxt.setForeground(Color.BLUE);
		bGuardarTxt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGuardarTxt.setBounds(23, 448, 156, 25);
		pBuscar.add(bGuardarTxt);

		JButton bGuardarExcel = new JButton("5. " + Idiomas.SaveExcel[iId]);
		bGuardarExcel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bGuardarExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tResult.getRowCount()>0) {
					g.GuardarFichero('e', tResult, null);
				} else {
					JOptionPane.showMessageDialog(null,Idiomas.E3[iId],Idiomas.Warning[iId],JOptionPane.WARNING_MESSAGE);					
				}
			}
		});
		bGuardarExcel.setForeground(Color.BLUE);
		bGuardarExcel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGuardarExcel.setBounds(23, 515, 156, 25);
		pBuscar.add(bGuardarExcel);

		JLabel lbHelpBuscar = new JLabel("");
		lbHelpBuscar.setToolTipText(Idiomas.HelpSeek[iId]);
		lbHelpBuscar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.tHelpSeek[iId],Idiomas.HelpSeek[iId], JOptionPane.INFORMATION_MESSAGE );	
				//JOptionPane.showMessageDialog(null,"Máscaras de busqueda:" + "\r\n" + "* cero o más caracteres" + "\r\n" + "+ 0-1 carácter" + "\r\n" 
				//		  + "? un carácter" + "\r\n" + "@ 0-1 palabra" + "\r\n" + "| or" + "\r\n" + "& and",
				//           "Ayuda de Buscar",JOptionPane.INFORMATION_MESSAGE );					  
			}
		});
		lbHelpBuscar.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lbHelpBuscar.setBounds(140, 130, 28, 32);
		pBuscar.add(lbHelpBuscar);



		// tab Listado (BuscarConcordancias.java) -------------------------------------------------------------------------

		JPanel pConcordancia = new JPanel();
		tabbedPane.addTab(Idiomas.tabula[iId][3], null, pConcordancia, null);
		tabbedPane.setForegroundAt(3, new Color(0, 128, 0));
		pConcordancia.setLayout(null);
		cPalabras = new TreeSet<String>(Collator.getInstance());

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(42, 150, 379, 236);
		pConcordancia.add(scrollPane_2);
		JTextArea tExcluir = new JTextArea();
		scrollPane_2.setViewportView(tExcluir);

		JButton bSalvarFichero = new JButton(Idiomas.SaveFile[iId]);
		bSalvarFichero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.GuardarFichero('t', null, tExcluir);
			}
		});
		bSalvarFichero.setBounds(254, 407, 137, 25);
		pConcordancia.add(bSalvarFichero);

		JButton bCargarFichero = new JButton(Idiomas.LoadFile[iId]);
		bCargarFichero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String sText = new String(g.CargarFichero('t'));
				if(sText!="") {
					tExcluir.setText(sText);
				}
			}
		});
		bCargarFichero.setBounds(43, 407, 137, 25);
		pConcordancia.add(bCargarFichero);

		JLabel label_4 = new JLabel("1. " + Idiomas.FileInput[iId]);
		label_4.setForeground(Color.BLUE);
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label_4.setBounds(12, 24, 157, 16);
		pConcordancia.add(label_4);

		tFileIn = new JTextField();
		tFileIn.setToolTipText(Idiomas.tFileInput[iId]);
		tFileIn.setForeground(Color.RED);
		tFileIn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileIn.setEditable(false);
		tFileIn.setColumns(10);
		tFileIn.setBounds(178, 23, 486, 22);
		pConcordancia.add(tFileIn);

		JButton bFileIn = new JButton(Idiomas.Select[iId]);
		bFileIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fEntrada = fRet;
						tFileIn.setText(fEntrada.getName());	
						// si no hay fichero seleccionado en los demas ponemos éste ...
						if(fBusca == null) {
							fBusca = fEntrada;
							tFileTxt.setText(fBusca.getName());	
						}
						if(fVer == null) {
							fVer = fEntrada;
							tFileView.setText(fVer.getName());	
						}						
						if(fWord == null) {
							fWord = fEntrada;
							tFileWord.setText(fWord.getName());
						}	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bFileIn.setBounds(699, 21, 123, 25);
		pConcordancia.add(bFileIn);

		JLabel label_5 = new JLabel("2. " + Idiomas.FileOutput[iId]);
		label_5.setForeground(Color.BLUE);
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label_5.setBounds(12, 70, 150, 16);
		pConcordancia.add(label_5);

		tFileOut = new JTextField();
		tFileOut.setToolTipText(Idiomas.tFileOutput[iId]);
		tFileOut.setForeground(Color.RED);
		tFileOut.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileOut.setEditable(false);
		tFileOut.setColumns(10);
		tFileOut.setBounds(178, 67, 486, 22);
		pConcordancia.add(tFileOut);

		JButton bFileOut = new JButton(Idiomas.Select[iId]);
		bFileOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					JFileChooser jfSalida=new JFileChooser();
					if(g.fDir != null)
						jfSalida.setCurrentDirectory(g.fDir);
					jfSalida.showSaveDialog(null);
					g.fDir = jfSalida.getCurrentDirectory();
					fSalida = jfSalida.getSelectedFile();
					tFileOut.setText(fSalida.getName());
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, Idiomas.E4[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bFileOut.setBounds(692, 67, 130, 25);
		pConcordancia.add(bFileOut);

		JTextPane textPane = new JTextPane();
		textPane.setBounds(24, 115, 407, 343);
		pConcordancia.add(textPane);
		textPane.setText("3. " + Idiomas.WordsEx[iId]);
		textPane.setForeground(Color.BLUE);
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textPane.setBackground(SystemColor.inactiveCaption);

		JLabel lbTotPalabras = new JLabel("0 " + Idiomas.Founds[iId]);
		lbTotPalabras.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotPalabras.setForeground(Color.RED);
		lbTotPalabras.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotPalabras.setBounds(854, 129, 144, 25);
		pConcordancia.add(lbTotPalabras);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(625, 167, 373, 435);
		pConcordancia.add(scrollPane_3);
		tPalabras = new JTable();
		scrollPane_3.setViewportView(tPalabras);

		JCheckBox chEtiquetasConcord = new JCheckBox(Idiomas.HideLabel[iId]);
		chEtiquetasConcord.setForeground(Color.BLUE);
		chEtiquetasConcord.setBounds(883, 67, 137, 25);
		pConcordancia.add(chEtiquetasConcord);

		JCheckBox chConcordMay = new JCheckBox(Idiomas.Mayusculas[iId]);
		chConcordMay.setForeground(Color.BLUE);
		chConcordMay.setBounds(1066, 67, 157, 25);
		pConcordancia.add(chConcordMay);

		BC = new BuscarConcordancias();
		JButton bGenerarPalabras = new JButton("4. " + Idiomas.GenWords[iId]);
		bGenerarPalabras.setToolTipText(Idiomas.tGenWords[iId]);
		bGenerarPalabras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cPalabras.clear();
				totPalabras = BC.RecogerPalabras(cPalabras, fEntrada, tPalabras, tExcluir, chEtiquetasConcord.isSelected(), chConcordMay.isSelected());
				lbTotPalabras.setText(String.valueOf(totPalabras) + " " + Idiomas.Words[iId]);
			}
		});
		bGenerarPalabras.setForeground(Color.BLUE);
		bGenerarPalabras.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGenerarPalabras.setBounds(625, 130, 197, 25);
		pConcordancia.add(bGenerarPalabras);

		prBarra = new JProgressBar();
		prBarra.setStringPainted(true);
		prBarra.setForeground(Color.RED);
		prBarra.setBounds(647, 625, 327, 25);
		pConcordancia.add(prBarra);

		JButton bGenerarConcordancias = new JButton("5. " + Idiomas.GenConcord[iId]);
		bGenerarConcordancias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// llamamos a la función mediante una tarea nueva para que se actualiza la barra de progreso
				final Task task = new Task("Tarea Concordancias", 'C');
				task.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equalsIgnoreCase("progress")) {
							int progress = task.getProgress();
							if (progress == 0) {
								prBarra.setIndeterminate(true);
							} else {
								prBarra.setIndeterminate(false);
								prBarra.setValue(progress);
							}
						}
					}
				});
				task.execute();
			}
		});
		bGenerarConcordancias.setForeground(Color.BLUE);
		bGenerarConcordancias.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGenerarConcordancias.setBounds(342, 625, 259, 25);
		pConcordancia.add(bGenerarConcordancias);



		// tab Wordlist (Wordlist.java) ------------------------------------------

		JPanel pWordlist = new JPanel();
		pWordlist.setForeground(Color.BLACK);
		tabbedPane.addTab(Idiomas.tabula[iId][4], null, pWordlist, null);
		tabbedPane.setForegroundAt(4, new Color(0, 128, 0));
		pWordlist.setLayout(null);

		WL = new Wordlist();

		JScrollPane scExcluir = new JScrollPane();
		scExcluir.setBounds(42, 150, 379, 236);
		pWordlist.add(scExcluir);
		JTextArea tExcluir2 = new JTextArea();
		scExcluir.setViewportView(tExcluir2);

		JButton bSalvarExcluir2 = new JButton(Idiomas.SaveFile[iId]);
		bSalvarExcluir2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.GuardarFichero('t', null, tExcluir2);
			}
		});
		bSalvarExcluir2.setBounds(254, 407, 137, 25);
		pWordlist.add(bSalvarExcluir2);

		JButton bCargarExcluir2 = new JButton(Idiomas.LoadFile[iId]);
		bCargarExcluir2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String sText = new String(g.CargarFichero('t'));
				if(sText!="") {
					tExcluir2.setText(sText);
				}
			}
		});
		bCargarExcluir2.setBounds(43, 407, 137, 25);
		pWordlist.add(bCargarExcluir2);

		JLabel lbFileWord = new JLabel("1. " + Idiomas.FileInput[iId]);
		lbFileWord.setForeground(Color.BLUE);
		lbFileWord.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbFileWord.setBounds(12, 24, 157, 16);
		pWordlist.add(lbFileWord);

		tFileWord = new JTextField();
		tFileWord.setToolTipText(Idiomas.tFileInput[iId]);
		tFileWord.setForeground(Color.RED);
		tFileWord.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileWord.setEditable(false);
		tFileWord.setColumns(10);
		tFileWord.setBounds(178, 23, 541, 22);
		pWordlist.add(tFileWord);

		JButton bFileWord = new JButton(Idiomas.Select[iId]);
		bFileWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fWord = fRet;
						tFileWord.setText(fWord.getName());	
						// si no hay fichero seleccionado en los demas ponemos éste ...
						if(fBusca == null) {
							fBusca = fWord;
							tFileTxt.setText(fBusca.getName());	
						}
						if(fVer == null) {
							fVer = fWord;
							tFileView.setText(fVer.getName());	
						}						
						if(fEntrada == null) {
							fEntrada = fWord;
							tFileIn.setText(fEntrada.getName());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bFileWord.setBounds(740, 21, 123, 25);
		pWordlist.add(bFileWord);

		JTextPane tpExcluir = new JTextPane();
		tpExcluir.setBounds(24, 115, 407, 343);
		pWordlist.add(tpExcluir);
		tpExcluir.setText("2. " + Idiomas.WordsEx[iId]);
		tpExcluir.setForeground(Color.BLUE);
		tpExcluir.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tpExcluir.setBackground(SystemColor.inactiveCaption);

		JLabel lbTotWord = new JLabel("0 "+ Idiomas.Words[iId]);
		lbTotWord.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotWord.setForeground(Color.RED);
		lbTotWord.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotWord.setBounds(748, 103, 214, 25);
		pWordlist.add(lbTotWord);

		JScrollPane scWords = new JScrollPane();
		scWords.setName("v");
		scWords.setBounds(520, 140, 510, 402);
		pWordlist.add(scWords);
		tWords = new JTable();
		scWords.setViewportView(tWords);
		//tWords.setDefaultRenderer(Object.class, new HighlightRenderer());

		JCheckBox chNoEtiquetas = new JCheckBox(Idiomas.HideLabel[iId]);
		chNoEtiquetas.setForeground(Color.BLUE);
		chNoEtiquetas.setBounds(664, 58, 140, 25);
		pWordlist.add(chNoEtiquetas);

		JCheckBox chMayusculas = new JCheckBox(Idiomas.Mayusculas[iId]);
		chMayusculas.setForeground(Color.BLUE);
		chMayusculas.setBounds(848, 58, 175, 25);
		pWordlist.add(chMayusculas);

		JCheckBox chFrecuenciaMiles = new JCheckBox(Idiomas.MilesCounts[iId]);
		chFrecuenciaMiles.setForeground(Color.BLUE);
		chFrecuenciaMiles.setBounds(1068, 58, 150, 25);
		pWordlist.add(chFrecuenciaMiles);

		JCheckBox chWordlistAscii = new JCheckBox(Idiomas.Ascii[iId]);
		chWordlistAscii.setForeground(Color.BLUE);
		chWordlistAscii.setToolTipText(Idiomas.I2[iId]);
		chWordlistAscii.setBounds(502, 58, 113, 25);
		pWordlist.add(chWordlistAscii);

		JButton bGenerarWord = new JButton("3. " + Idiomas.GenWords[iId]);
		bGenerarWord.setToolTipText(Idiomas.tGenWords[iId]);
		bGenerarWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				totWords = WL.ListWords(fWord, tWords, tExcluir2, chNoEtiquetas.isSelected(), chMayusculas.isSelected(), chFrecuenciaMiles.isSelected(), chWordlistAscii.isSelected());
				lbTotWord.setText(String.valueOf(totWords) + "/" + String.valueOf(g.numWords) + " " + Idiomas.Words[iId]);
				cOrden='v';
			}
		});
		bGenerarWord.setForeground(Color.BLUE);
		bGenerarWord.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGenerarWord.setBounds(529, 104, 197, 25);
		pWordlist.add(bGenerarWord);

		JButton bWordTxt = new JButton("4. "+ Idiomas.SaveTxt[iId]);
		bWordTxt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bWordTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tWords.getRowCount()>0) {
					g.GuardarFichero('t', tWords, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bWordTxt.setForeground(Color.BLUE);
		bWordTxt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bWordTxt.setBounds(553, 555, 156, 25);
		pWordlist.add(bWordTxt);

		JButton bWordExcel = new JButton("4. " + Idiomas.SaveExcel[iId]);
		bWordExcel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bWordExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tWords.getRowCount()>0) {
					g.GuardarFichero('e', tWords, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bWordExcel.setForeground(Color.BLUE);
		bWordExcel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bWordExcel.setBounds(784, 555, 156, 25);
		pWordlist.add(bWordExcel);

		JButton bOrdAlfa = new JButton(Idiomas.Alfabethic[iId]);
		bOrdAlfa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tWords.getRowCount()>0) {
					if(cOrden == 'A') {
						g.Ordenados(tWords, 1, false);
						cOrden ='a';
					}
					else { 
						g.Ordenados(tWords, 1, true);
						cOrden ='A';
					}
					//g.Ordenar('A', tWords);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E5[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bOrdAlfa.setBounds(1103, 205, 97, 25);
		pWordlist.add(bOrdAlfa);

		JButton bOrdVal = new JButton(Idiomas.Counts[iId]);
		bOrdVal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tWords.getRowCount()>0) {
					if(cOrden == 'V') {
						g.Ordenados(tWords, 2, false);
						cOrden ='v';
					}
					else { 
						g.Ordenados(tWords, 2, true);
						cOrden ='V';
					}
					//g.Ordenar('V', tWords);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E5[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bOrdVal.setBounds(1102, 261, 116, 25);
		pWordlist.add(bOrdVal);

		JLabel lblOrdenacin = new JLabel(Idiomas.Order[iId]);
		lblOrdenacin.setForeground(Color.BLUE);
		lblOrdenacin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblOrdenacin.setBounds(1117, 167, 88, 16);
		pWordlist.add(lblOrdenacin);

		JLabel lblBuscar = new JLabel(Idiomas.Seek[iId] + ":");
		lblBuscar.setForeground(Color.BLUE);
		lblBuscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblBuscar.setBounds(1117, 364, 56, 16);
		pWordlist.add(lblBuscar);

		txBuscWord = new JTextField();
		txBuscWord.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				sBuscWord = new String( txBuscWord.getText() );				
			}
		});
		txBuscWord.setBounds(1102, 393, 116, 22);
		pWordlist.add(txBuscWord);
		txBuscWord.setColumns(10);

		JButton bBuscWord = new JButton(Idiomas.Seek[iId]);
		bBuscWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//sMarca = sBuscWord;
				//System.out.println(sMarca);
				for(int i=1;i<tWords.getModel().getRowCount();i++) {
					if(tWords.getValueAt(i, 1).equals(sBuscWord)) {  
						//System.out.println(String.valueOf(i));
						Rectangle rect = tWords.getCellRect(i, 2, true);  // si lo encuentra nos posicionamos
						tWords.scrollRectToVisible(rect);
						tWords.setRowSelectionInterval(i, i);  // lo seleccionamos
						break;
					}
				}
				tWords.repaint();
			}
		});
		bBuscWord.setForeground(Color.BLACK);
		bBuscWord.setBounds(1103, 433, 97, 25);
		pWordlist.add(bBuscWord);




		// tab Contrastes (Exclusiones.java) ---------------------------------------------

		Exclusiones EX = new Exclusiones();

		JPanel pContraste = new JPanel();
		pContraste.setForeground(Color.BLACK);
		tabbedPane.addTab(Idiomas.tabula[iId][5], null, pContraste, null);
		tabbedPane.setForegroundAt(5, new Color(0, 128, 0));
		pContraste.setLayout(null);

		JLabel lbCon1 = new JLabel("1. " + Idiomas.FileAs[iId]);
		lbCon1.setForeground(Color.BLUE);
		lbCon1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbCon1.setBounds(37, 26, 104, 16);
		pContraste.add(lbCon1);

		tFileCon1 = new JTextField();
		tFileCon1.setToolTipText(Idiomas.FileAs[iId]);
		tFileCon1.setForeground(Color.RED);
		tFileCon1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileCon1.setEditable(false);
		tFileCon1.setColumns(10);
		tFileCon1.setBounds(138, 23, 317, 22);
		pContraste.add(tFileCon1);

		JButton bSel1 = new JButton(Idiomas.Select[iId]);
		bSel1.setToolTipText(Idiomas.Select[iId] + " " + Idiomas.FileA[iId]);
		bSel1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
/*				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fCon1 = fRet;
						tFileCon1.setText(fCon1.getName());	
						// si no hay fichero seleccionado en los demas ponemos éste ...
						if(fBusca == null) {
							fBusca = fCon1;
							tFileTxt.setText(fBusca.getName());	
						}
						if(fVer == null) {
							fVer = fCon1;
							tFileView.setText(fVer.getName());	
						}						
						if(fEntrada == null) {
							fEntrada = fCon1;
							tFileIn.setText(fEntrada.getName());
						}
						if(fWord == null) {
							fWord = fCon1;
							tFileWord.setText(fWord.getName());
						}
					} 
				} catch (Exception e) {
					e.printStackTrace();
				} */
				try {
					fRets = g.abrirMArchivo('t');
					if(fRets != null) {
						fCon1 = fRets;
						String sFile = "";
						for(int i=0;i<fCon1.length;i++) sFile = sFile + fCon1[i].getName() + " ";
						tFileCon1.setText(sFile);	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
		bSel1.setBounds(475, 23, 123, 25);
		pContraste.add(bSel1);

		JLabel lbCon2 = new JLabel("2. " + Idiomas.FileBs[iId]);
		lbCon2.setForeground(Color.BLUE);
		lbCon2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbCon2.setBounds(644, 26, 97, 16);
		pContraste.add(lbCon2);

		tFileCon2 = new JTextField();
		tFileCon2.setToolTipText(Idiomas.FileBs[iId]);
		tFileCon2.setForeground(Color.RED);
		tFileCon2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileCon2.setEditable(false);
		tFileCon2.setColumns(10);
		tFileCon2.setBounds(743, 23, 329, 22);
		pContraste.add(tFileCon2);

		JButton bSel2 = new JButton(Idiomas.Select[iId]);
		bSel2.setToolTipText(Idiomas.Select[iId] + " " + Idiomas.FileB[iId]);
		bSel2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
/*				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fCon2 = fRet;
						tFileCon2.setText(fCon2.getName());	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				} */
				try {
					fRets = g.abrirMArchivo('t');
					if(fRets != null) {
						fCon2 = fRets;
						String sFile = "";
						for(int i=0;i<fCon2.length;i++) sFile = sFile + fCon2[i].getName() + " ";
						tFileCon2.setText(sFile);	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
		bSel2.setBounds(1080, 23, 123, 25);
		pContraste.add(bSel2);

		JLabel lblFicheroDe = new JLabel("3. " + Idiomas.FileWordsEx[iId]);
		lblFicheroDe.setForeground(Color.BLUE);
		lblFicheroDe.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFicheroDe.setBounds(38, 73, 247, 16);
		pContraste.add(lblFicheroDe);

		tExCon = new JTextField();
		tExCon.setEditable(false);
		tExCon.setBounds(282, 71, 116, 22);
		pContraste.add(tExCon);
		tExCon.setColumns(10);

		JButton btnSeleccionar = new JButton(Idiomas.Select[iId]);
		btnSeleccionar.setToolTipText(Idiomas.tFileWordsEx[iId]);
		btnSeleccionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fExCon = fRet;
						tExCon.setText(fExCon.getName());
						// ahora leemos el fichero ...
						StringBuilder sB = new StringBuilder();
						Scanner scanner = new Scanner(fExCon);
						while (scanner.hasNextLine()) {
							sB.append( scanner.nextLine());
							//sB.append( ls );
						}	  
						scanner.close();
						sExCon = sB.toString();
					}
					else {
						sExCon="";
						tExCon.setText("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSeleccionar.setBounds(410, 70, 116, 25);
		pContraste.add(btnSeleccionar);

		JLabel lbTotCon1 = new JLabel("0 " + Idiomas.Words[iId]);
		lbTotCon1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotCon1.setForeground(Color.RED);
		lbTotCon1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotCon1.setBounds(231, 210, 123, 25);
		pContraste.add(lbTotCon1);

		JScrollPane scCon1 = new JScrollPane();
		scCon1.setName("a");
		scCon1.setBounds(37, 248, 317, 367);
		pContraste.add(scCon1);
		tCon1 = new JTable();
		//tCon1.setAutoCreateRowSorter(true);  // ordenar por columnas
		scCon1.setViewportView(tCon1);

		JLabel lbTotCon2 = new JLabel("0 " + Idiomas.Words[iId]);
		lbTotCon2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotCon2.setForeground(Color.RED);
		lbTotCon2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotCon2.setBounds(1087, 210, 132, 25);
		pContraste.add(lbTotCon2);

		JScrollPane scCon2 = new JScrollPane();
		scCon2.setName("A");
		scCon2.setBounds(890, 248, 329, 367);
		pContraste.add(scCon2);
		tCon2 = new JTable();
		//tCon2.setAutoCreateRowSorter(true);  // ordenar por columnas
		scCon2.setViewportView(tCon2);

		JLabel lbTotCon3 = new JLabel("0 " + Idiomas.Words[iId]);
		lbTotCon3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotCon3.setForeground(Color.RED);
		lbTotCon3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotCon3.setBounds(649, 210, 139, 25);
		pContraste.add(lbTotCon3);

		JScrollPane scCon3 = new JScrollPane();
		scCon3.setName("A");
		scCon3.setBounds(457, 248, 329, 367);
		pContraste.add(scCon3);
		tCon3 = new JTable();
		//tCon3.setAutoCreateRowSorter(true);  // ordenar por columnas
		scCon3.setViewportView(tCon3);

		JButton bCon1Txt = new JButton("5. " + Idiomas.SaveTxt[iId]);
		bCon1Txt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bCon1Txt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon1.getRowCount()>0) {
					g.GuardarFichero('t', tCon1, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bCon1Txt.setForeground(Color.BLUE);
		bCon1Txt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCon1Txt.setBounds(37, 628, 132, 25);
		pContraste.add(bCon1Txt);

		JButton bCon1Excel = new JButton("5. " + Idiomas.SaveExcel[iId]);
		bCon1Excel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bCon1Excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon1.getRowCount()>0) {
					g.GuardarFichero('e', tCon1, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bCon1Excel.setForeground(Color.BLUE);
		bCon1Excel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCon1Excel.setBounds(215, 628, 139, 25);
		pContraste.add(bCon1Excel);

		JButton bCon2Txt = new JButton("5. " + Idiomas.SaveTxt[iId]);
		bCon2Txt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bCon2Txt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon2.getRowCount()>0) {
					g.GuardarFichero('t', tCon2, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bCon2Txt.setForeground(Color.BLUE);
		bCon2Txt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCon2Txt.setBounds(900, 628, 132, 25);
		pContraste.add(bCon2Txt);

		JButton bCon2Excel = new JButton("5. " + Idiomas.SaveExcel[iId]);
		bCon2Excel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bCon2Excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon2.getRowCount()>0) {
					g.GuardarFichero('e', tCon2, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bCon2Excel.setForeground(Color.BLUE);
		bCon2Excel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCon2Excel.setBounds(1070, 628, 147, 25);
		pContraste.add(bCon2Excel);

		JButton bCon3Txt = new JButton("5. " + Idiomas.SaveTxt[iId]);
		bCon3Txt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bCon3Txt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon3.getRowCount()>0) {
					g.GuardarFichero('t', tCon3, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bCon3Txt.setForeground(Color.BLUE);
		bCon3Txt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCon3Txt.setBounds(466, 628, 132, 25);
		pContraste.add(bCon3Txt);

		JButton bCon3Excel = new JButton("5. " + Idiomas.SaveExcel[iId]);
		bCon3Excel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bCon3Excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon3.getRowCount()>0) {
					g.GuardarFichero('e', tCon3, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bCon3Excel.setForeground(Color.BLUE);
		bCon3Excel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCon3Excel.setBounds(634, 628, 147, 25);
		pContraste.add(bCon3Excel);

		JLabel lblPalabrasDeA = new JLabel(Idiomas.WordsAnoB[iId]);
		lblPalabrasDeA.setBounds(37, 219, 196, 16);
		pContraste.add(lblPalabrasDeA);

		JLabel lblPalabrasDeB = new JLabel(Idiomas.WordsBnoA[iId]);
		lblPalabrasDeB.setBounds(890, 216, 200, 16);
		pContraste.add(lblPalabrasDeB);

		JLabel lblPalabrasQueEstn = new JLabel(Idiomas.WordsAB[iId]);
		lblPalabrasQueEstn.setBounds(457, 216, 182, 16);
		pContraste.add(lblPalabrasQueEstn);

		JCheckBox chQuitarEtiquetas = new JCheckBox(Idiomas.HideLabel[iId]);
		chQuitarEtiquetas.setForeground(Color.BLUE);
		chQuitarEtiquetas.setBounds(636, 70, 139, 25);
		pContraste.add(chQuitarEtiquetas);

		JCheckBox chConMay = new JCheckBox(Idiomas.Mayusculas[iId]);
		chConMay.setForeground(Color.BLUE);
		chConMay.setBounds(816, 70, 161, 25);
		pContraste.add(chConMay);

		JCheckBox chWordsAscii = new JCheckBox(Idiomas.Ascii[iId]);
		chWordsAscii.setForeground(Color.BLUE);
		chWordsAscii.setBounds(993, 70, 113, 25);
		chWordsAscii.setToolTipText(Idiomas.I2[iId]);
		pContraste.add(chWordsAscii);

		JButton bGenCon = new JButton("4. " + Idiomas.GenContrast[iId]);
		bGenCon.setToolTipText(Idiomas.tGenContrast[iId]);
		bGenCon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// iniciamos el proceso
				if(EX.Contraste(fCon1, fCon2, tCon1, tCon2, tCon3, sExCon, chQuitarEtiquetas.isSelected(), chConMay.isSelected(), chWordsAscii.isSelected())>0) {
					lbTotCon1.setText(String.valueOf(tCon1.getModel().getRowCount()) + " " + Idiomas.Words[iId]);
					lbTotCon2.setText(String.valueOf(tCon2.getModel().getRowCount()) + " " + Idiomas.Words[iId]);
					lbTotCon3.setText(String.valueOf(tCon3.getModel().getRowCount()) + " " + Idiomas.Words[iId]);
				}
			}
		});
		bGenCon.setForeground(Color.BLUE);
		bGenCon.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGenCon.setBounds(476, 119, 175, 25);
		pContraste.add(bGenCon);

		JButton btnA = new JButton("Alf.");
		btnA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tCon1.getRowCount()>0) g.Ordenar('A', tCon1);
			}
		});
		btnA.setBounds(108, 174, 66, 25);
		pContraste.add(btnA);

		JButton btnNum = new JButton("Num.");
		btnNum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon1.getRowCount()>0) g.Ordenar('V', tCon1);
			}
		});
		btnNum.setBounds(188, 174, 66, 25);
		pContraste.add(btnNum);

		JButton btnAlf = new JButton("Alf.");
		btnAlf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon3.getRowCount()>0) g.Ordenar('A', tCon3);
			}
		});
		btnAlf.setBounds(541, 174, 57, 25);
		pContraste.add(btnAlf);

		JButton btnNum_1 = new JButton("Num.");
		btnNum_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon3.getRowCount()>0) g.Ordenar('V', tCon3);
			}
		});
		btnNum_1.setBounds(621, 174, 66, 25);
		pContraste.add(btnNum_1);

		JButton btnAlf_1 = new JButton("Alf.");
		btnAlf_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon2.getRowCount()>0) g.Ordenar('A', tCon2);
			}
		});
		btnAlf_1.setBounds(976, 174, 66, 25);
		pContraste.add(btnAlf_1);

		JButton btnNum_2 = new JButton("Num.");
		btnNum_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tCon2.getRowCount()>0) g.Ordenar('V', tCon2);
			}
		});
		btnNum_2.setBounds(1054, 174, 66, 25);
		pContraste.add(btnNum_2);

		JLabel lblOrdenar = new JLabel(Idiomas.Order[iId]);
		lblOrdenar.setBounds(40, 178, 56, 16);
		pContraste.add(lblOrdenar);

		JLabel lblOrdenar_1 = new JLabel(Idiomas.Order[iId]);
		lblOrdenar_1.setBounds(457, 178, 56, 16);
		pContraste.add(lblOrdenar_1);

		JLabel lblOrdenar_2 = new JLabel(Idiomas.Order[iId]);
		lblOrdenar_2.setBounds(890, 178, 56, 16);
		pContraste.add(lblOrdenar_2);

		JLabel lbConFiles = new JLabel("");
		lbConFiles.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lbConFiles.setBounds(605, 20, 28, 32);
		lbConFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.tHelpFiles2[iId],Idiomas.HelpFiles[iId], JOptionPane.INFORMATION_MESSAGE );	
			}
		});
		pContraste.add(lbConFiles);

		JLabel lbConFiles2 = new JLabel("");
		lbConFiles2.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lbConFiles2.setBounds(1210, 20, 28, 32);
		lbConFiles2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.tHelpFiles2[iId],Idiomas.HelpFiles[iId], JOptionPane.INFORMATION_MESSAGE );	
			}
		});
		pContraste.add(lbConFiles2);



		// tab Palabras Clave (Wordkey.java) ---------------------------------------------

		Wordkey WK = new Wordkey();

		JPanel pWordKey = new JPanel();
		pWordKey.setForeground(Color.BLACK);
		tabbedPane.addTab(Idiomas.tabula[iId][6], null, pWordKey, null);
		tabbedPane.setForegroundAt(6, new Color(0, 128, 0));
		pWordKey.setLayout(null);

		JLabel lbKey1 = new JLabel("1. " + Idiomas.FileInputs[iId]);
		lbKey1.setForeground(Color.BLUE);
		lbKey1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbKey1.setBounds(24, 26, 164, 16);
		pWordKey.add(lbKey1);

		tFileKey1 = new JTextField();
		tFileKey1.setToolTipText(Idiomas.tFileInputs[iId]);
		tFileKey1.setForeground(Color.RED);
		tFileKey1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileKey1.setEditable(false);
		tFileKey1.setColumns(10);
		tFileKey1.setBounds(200, 23, 286, 22);
		pWordKey.add(tFileKey1);

		JButton bKey1 = new JButton(Idiomas.Select[iId]);
		//bKey1.setToolTipText("Seleccionar fichero A");
		bKey1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
/*				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fKey1 = fRet;
						tFileKey1.setText(fKey1.getName());	
						if(fBusca == null) {
							fBusca = fKey1;
							tFileTxt.setText(fBusca.getName());	
						}
						if(fVer == null) {
							fVer = fKey1;
							tFileView.setText(fVer.getName());	
						}						
						if(fEntrada == null) {
							fEntrada = fKey1;
							tFileIn.setText(fEntrada.getName());
						}
						if(fWord == null) {
							fWord = fKey1;
							tFileWord.setText(fWord.getName());
						}
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}  */
				try {
					fRets = g.abrirMArchivo('t');
					if(fRets != null) {
						fKey1 = fRets;
						String sFile = "";
						for(int i=0;i<fKey1.length;i++) sFile = sFile + fKey1[i].getName() + " ";
						tFileKey1.setText(sFile);	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
		bKey1.setBounds(498, 23, 123, 25);
		pWordKey.add(bKey1);

		JLabel lbKey2 = new JLabel("2. " + Idiomas.Corpus[iId]);
		lbKey2.setForeground(Color.BLUE);
		lbKey2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbKey2.setBounds(687, 26, 169, 16);
		pWordKey.add(lbKey2);

		tFileKey2 = new JTextField();
		//tFileKey2.setToolTipText("Fichero B");
		tFileKey2.setForeground(Color.RED);
		tFileKey2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileKey2.setEditable(false);
		tFileKey2.setColumns(10);
		tFileKey2.setBounds(858, 23, 175, 22);
		pWordKey.add(tFileKey2);

		JButton bKey2 = new JButton(Idiomas.Select[iId]);
		//bKey2.setToolTipText("Seleccionar fichero B");
		bKey2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRets = g.abrirMArchivo('t');
					if(fRets != null) {
						fKey2 = fRets;
						String sCorpus = "";
						for(int i=0;i<fKey2.length;i++) sCorpus = sCorpus + fKey2[i].getName() + " ";
						tFileKey2.setText(sCorpus);	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bKey2.setBounds(1045, 23, 123, 25);
		pWordKey.add(bKey2);

		JLabel lbExKey = new JLabel("3. " + Idiomas.FileWordsEx[iId]);
		lbExKey.setForeground(Color.BLUE);
		lbExKey.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbExKey.setBounds(24, 73, 261, 16);
		pWordKey.add(lbExKey);

		tExKey = new JTextField();
		tExKey.setEditable(false);
		tExKey.setBounds(282, 70, 204, 22);
		pWordKey.add(tExKey);
		tExKey.setColumns(10);

		JButton bSelExKey = new JButton(Idiomas.Select[iId]);
		bSelExKey.setToolTipText(Idiomas.tFileWordsEx[iId]);
		bSelExKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fExKey = fRet;
						tExKey.setText(fExKey.getName());
						// ahora leemos el fichero ...
						StringBuilder sB = new StringBuilder();
						Scanner scanner = new Scanner(fExKey);
						while (scanner.hasNextLine()) {
							sB.append( scanner.nextLine());
							//sB.append( ls );
						}	  
						scanner.close();
						sExKey = sB.toString();
					}
					else {
						sExKey="";
						tExKey.setText("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bSelExKey.setBounds(498, 70, 116, 25);
		pWordKey.add(bSelExKey);

		JScrollPane scKey1 = new JScrollPane();
		scKey1.setName("V");
		scKey1.setBounds(79, 192, 485, 371);
		pWordKey.add(scKey1);
		tKey = new JTable();
		scKey1.setViewportView(tKey);

		tKey.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && fKey1.length==1) {  // doble click, pasamos a Buscar Concordancias
					try {
						tabbedPane.setSelectedIndex(2);  // cambio a la carpeta de Concordancias
						tBuscar.setText((String) tKey.getModel().getValueAt(tKey.getSelectedRow(), 1));
						lNomSoneto.setSelected(true);
						if(fBusca != fKey1[0]) {  // si es un fichero diferente, hay que cargarlo
							fBusca = fKey1[0];
							tFileTxt.setText(fBusca.getName());
						}	
						iMaxLin = BA.Buscar(tBuscar.getText(), true, tResult, fBusca, false);
						if(iMaxLin>0) {
							tResult.getColumn("Verso").setPreferredWidth(iMaxLin*8);
							lbEncontradas.setText(String.valueOf(g.iWordsFound) + " " + Idiomas.Founds[iId]);
						}
						else
							lbEncontradas.setText("0 " + Idiomas.Founds[iId]);			
					} catch (Exception ex) {
						ex.printStackTrace();
					}	
				}
			}
		});    	

		JLabel lbTotKey = new JLabel("0 " + Idiomas.Founds[iId]);
		lbTotKey.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotKey.setForeground(Color.RED);
		lbTotKey.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotKey.setBounds(389, 154, 132, 25);
		pWordKey.add(lbTotKey);		

		JLabel lbTotKey2 = new JLabel("0 " + Idiomas.Founds[iId]);
		lbTotKey2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotKey2.setForeground(Color.RED);
		lbTotKey2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotKey2.setBounds(1005, 154, 132, 25);
		pWordKey.add(lbTotKey2);

		JScrollPane scKey2 = new JScrollPane();
		scKey2.setName("A");
		scKey2.setBounds(669, 192, 499, 371);
		pWordKey.add(scKey2);
		tKey2 = new JTable();
		scKey2.setViewportView(tKey2);

		tKey2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && fKey1.length==1) {  // doble click, pasamos a Buscar Concordancias
					try {
						tabbedPane.setSelectedIndex(2);  // cambio a la carpeta de Concordancias
						tBuscar.setText((String) tKey2.getModel().getValueAt(tKey2.getSelectedRow(), 1));
						lNomSoneto.setSelected(true);
						if(fBusca != fKey1[0]) {  // si es un fichero diferente, hay que cargarlo
							fBusca = fKey1[0];
							tFileTxt.setText(fBusca.getName());
						}	
						iMaxLin = BA.Buscar(tBuscar.getText(), true, tResult, fBusca, false);
						if(iMaxLin>0) {
							tResult.getColumn("Verso").setPreferredWidth(iMaxLin*8);
							lbEncontradas.setText(String.valueOf(g.iWordsFound) + " " + Idiomas.Founds[iId]);
						}
						else
							lbEncontradas.setText("0 " + Idiomas.Founds[iId]);							
					} catch (Exception ex) {
						ex.printStackTrace();
					}	
				}
			}
		});      	

		JCheckBox chEtiqKey = new JCheckBox(Idiomas.HideLabel[iId]);
		chEtiqKey.setForeground(Color.BLUE);
		chEtiqKey.setBounds(703, 70, 139, 25);
		pWordKey.add(chEtiqKey);

		JCheckBox chListMay = new JCheckBox(Idiomas.Mayusculas[iId]);
		chListMay.setForeground(Color.BLUE);
		chListMay.setBounds(871, 70, 132, 25);
		pWordKey.add(chListMay);

		JCheckBox chListAscii = new JCheckBox(Idiomas.Ascii[iId]);
		chListAscii.setForeground(Color.BLUE);
		chListAscii.setBounds(1043, 70, 155, 25);
		chListAscii.setToolTipText(Idiomas.I2[iId]);
		pWordKey.add(chListAscii);

		JButton bGenKey = new JButton("4. " + Idiomas.GenKeys[iId]);
		bGenKey.setToolTipText(Idiomas.tGenKeys[iId]);
		bGenKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// iniciamos el proceso
				if(WK.ListaClaves(fKey1, fKey2, tKey, tKey2, sExKey, chEtiqKey.isSelected(), chListMay.isSelected(), chListAscii.isSelected())>0) {
					lbTotKey.setText(String.valueOf(tKey.getModel().getRowCount()) + " " + Idiomas.Words[iId]);
					lbTotKey2.setText(String.valueOf(tKey2.getModel().getRowCount()) + " " + Idiomas.Words[iId]);
					if(tKey.getRowCount()>0) g.Ordenados(tKey, 3, false); // g.Ordenar('V', tKey);
					if(tKey2.getRowCount()>0) g.Ordenados(tKey2, 3, false);
				}

			}
		});
		bGenKey.setForeground(Color.BLUE);
		bGenKey.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGenKey.setBounds(512, 108, 184, 25);
		pWordKey.add(bGenKey);    

		JButton bKey1Txt = new JButton("5. " + Idiomas.SaveTxt[iId]);
		bKey1Txt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bKey1Txt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tKey.getRowCount()>0) {
					g.GuardarFichero('t', tKey, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bKey1Txt.setForeground(Color.BLUE);
		bKey1Txt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bKey1Txt.setBounds(120, 576, 132, 25);
		pWordKey.add(bKey1Txt);

		JButton bKey1Excel = new JButton("5. " + Idiomas.SaveExcel[iId]);
		bKey1Excel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bKey1Excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tKey.getRowCount()>0) {
					g.GuardarFichero('e', tKey, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bKey1Excel.setForeground(Color.BLUE);
		bKey1Excel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bKey1Excel.setBounds(371, 576, 139, 25);
		pWordKey.add(bKey1Excel);

		JButton bKey2Txt = new JButton("5. " + Idiomas.SaveTxt[iId]);
		bKey2Txt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bKey2Txt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tKey2.getRowCount()>0) {
					g.GuardarFichero('t', tKey2, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bKey2Txt.setForeground(Color.BLUE);
		bKey2Txt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bKey2Txt.setBounds(724, 576, 132, 25);
		pWordKey.add(bKey2Txt);

		JButton bKey2Excel = new JButton("5. " + Idiomas.SaveExcel[iId]);
		bKey2Excel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bKey2Excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tKey2.getRowCount()>0) {
					g.GuardarFichero('e', tKey2, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bKey2Excel.setForeground(Color.BLUE);
		bKey2Excel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bKey2Excel.setBounds(990, 576, 147, 25);
		pWordKey.add(bKey2Excel);

		JLabel lblClavesPositivas = new JLabel(Idiomas.PositiveKeys[iId]);
		lblClavesPositivas.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblClavesPositivas.setForeground(Color.RED);
		lblClavesPositivas.setBounds(98, 154, 155, 25);
		pWordKey.add(lblClavesPositivas);

		JLabel lblClavesNegativas = new JLabel(Idiomas.NegativeKeys[iId]);
		lblClavesNegativas.setForeground(Color.RED);
		lblClavesNegativas.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblClavesNegativas.setBounds(678, 157, 143, 22);
		pWordKey.add(lblClavesNegativas);

		JLabel lbHelpFiles = new JLabel("");
		lbHelpFiles.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lbHelpFiles.setBounds(633, 20, 28, 32);
		lbHelpFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.tHelpFiles2[iId],Idiomas.HelpFiles[iId], JOptionPane.INFORMATION_MESSAGE );	
			}
		});
		pWordKey.add(lbHelpFiles);

		JLabel lbHelpFiles2 = new JLabel("");
		lbHelpFiles2.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lbHelpFiles2.setBounds(1184, 20, 28, 32);
		lbHelpFiles2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.tHelpFiles[iId],Idiomas.HelpFiles[iId], JOptionPane.INFORMATION_MESSAGE );	
			}
		});
		pWordKey.add(lbHelpFiles2);

		// tab EtiquetasI (Label.java) pLabel ---------------------------------------------
		// esta opción se pone no visible
 
		LB = new Label();

		JPanel pLabel = new JPanel();
		pLabel.setForeground(Color.BLACK);
		//tabbedPane.addTab(Idiomas.tabula[iId][7], null, pLabel, null);
		//tabbedPane.setForegroundAt(7, new Color(0, 128, 0));
		pLabel.setLayout(null);

		JLabel lbLabel1 = new JLabel("1. " + Idiomas.FileInput[iId]);
		lbLabel1.setForeground(Color.BLUE);
		lbLabel1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbLabel1.setBounds(37, 26, 151, 16);
		pLabel.add(lbLabel1);

		JTextField tFileLb1 = new JTextField();
		tFileLb1.setToolTipText(Idiomas.tFileInput[iId]);
		tFileLb1.setForeground(Color.RED);
		tFileLb1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileLb1.setEditable(false);
		tFileLb1.setColumns(10);
		tFileLb1.setBounds(200, 23, 524, 22);
		pLabel.add(tFileLb1);

		JButton bLabel1 = new JButton(Idiomas.Select[iId]);
		//bKey1.setToolTipText("Seleccionar fichero A");

		bLabel1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fLb1 = fRet;
						tFileLb1.setText(fLb1.getName());	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bLabel1.setBounds(757, 23, 123, 25);
		pLabel.add(bLabel1);

		JLabel lbLabel2 = new JLabel("2. " + Idiomas.Dictionary[iId] + ":");
		lbLabel2.setForeground(Color.BLUE);
		lbLabel2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbLabel2.setBounds(37, 73, 106, 16);
		pLabel.add(lbLabel2);

		JTextField tFileLb2 = new JTextField();
		//tFileLb2.setToolTipText("Fichero B");
		tFileLb2.setForeground(Color.RED);
		tFileLb2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileLb2.setEditable(false);
		tFileLb2.setColumns(10);
		tFileLb2.setBounds(155, 70, 569, 22);
		pLabel.add(tFileLb2);

		JLabel lbHelpDic = new JLabel("");
		lbHelpDic.setToolTipText(Idiomas.HelpDic[iId]);
		lbHelpDic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.tHelpDic[iId],Idiomas.HelpDic[iId], JOptionPane.INFORMATION_MESSAGE );	
			}
		});
		lbHelpDic.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lbHelpDic.setBounds(911, 60, 28, 32);
		pLabel.add(lbHelpDic);


		JButton bLabel2 = new JButton(Idiomas.Select[iId]);
		//bKey2.setToolTipText("Seleccionar fichero B");
		bLabel2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fLb2 = fRet;
						tFileLb2.setText(fLb2.getName());	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bLabel2.setBounds(757, 70, 123, 25);
		pLabel.add(bLabel2);

		JLabel lbLabel3 = new JLabel("4. " + Idiomas.FileOutput[iId]);
		lbLabel3.setForeground(Color.BLUE);
		lbLabel3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbLabel3.setBounds(37, 612, 144, 16);
		pLabel.add(lbLabel3);

		JTextField tFileLb3 = new JTextField();
		//tFileLb3.setToolTipText("Fichero B");
		tFileLb3.setForeground(Color.RED);
		tFileLb3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileLb3.setEditable(false);
		tFileLb3.setColumns(10);
		tFileLb3.setBounds(180, 609, 175, 22);
		pLabel.add(tFileLb3);

		JButton bLabel3 = new JButton(Idiomas.Select[iId]);
		//bKey2.setToolTipText("Seleccionar fichero B");
		bLabel3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fLb3 = fRet;
						if(!fLb3.getName().contains(".")) fLb3 = new File(fLb3.getPath() + ".txt"); 
						tFileLb3.setText(fLb3.getName());	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bLabel3.setBounds(367, 609, 123, 25);
		pLabel.add(bLabel3);

		JLabel lbTotLabel = new JLabel("0 " + Idiomas.Founds[iId]);
		lbTotLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotLabel.setForeground(Color.RED);
		lbTotLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotLabel.setBounds(389, 154, 132, 25);
		pLabel.add(lbTotLabel);	

		JScrollPane scLabel = new JScrollPane();
		scLabel.setBounds(79, 192, 485, 370);
		pLabel.add(scLabel);
		tLabel = new JTable();
		scLabel.setViewportView(tLabel);

		JLabel lbTotLabel2 = new JLabel("0 " + Idiomas.Founds[iId]);
		lbTotLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotLabel2.setForeground(Color.RED);
		lbTotLabel2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotLabel2.setBounds(1005, 154, 132, 25);
		pLabel.add(lbTotLabel2);

		JScrollPane scLabel2 = new JScrollPane();
		scLabel2.setName("A");
		scLabel2.setBounds(669, 192, 499, 350);
		pLabel.add(scLabel2);
		tLabel2 = new JTable();
		scLabel2.setViewportView(tLabel2);

		JButton bNoLabelTxt = new JButton("Guardar Txt");
		bNoLabelTxt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bNoLabelTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tLabel2.getRowCount()>0) {
					g.GuardarFichero('t', tLabel2, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bNoLabelTxt.setForeground(Color.BLUE);
		bNoLabelTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bNoLabelTxt.setBounds(708, 555, 132, 25);
		pLabel.add(bNoLabelTxt);

		chLabelAscii = new JCheckBox(Idiomas.Ascii[iId]);
		chLabelAscii.setForeground(Color.BLUE);
		chLabelAscii.setBounds(856, 109, 113, 25);
		chLabelAscii.setToolTipText(Idiomas.I2[iId]);
		pLabel.add(chLabelAscii);

		JLabel lblMximoEtiqueta = new JLabel(Idiomas.MaxCharLabel[iId]);
		lblMximoEtiqueta.setForeground(Color.BLUE);
		lblMximoEtiqueta.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMximoEtiqueta.setBounds(994, 113, 132, 16);
		pLabel.add(lblMximoEtiqueta);

		tMaxLabel = new JTextField();
		tMaxLabel.setForeground(Color.RED);
		tMaxLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tMaxLabel.setText("8");
		tMaxLabel.setBounds(1141, 109, 27, 22);
		pLabel.add(tMaxLabel);
		tMaxLabel.setColumns(10);

		JButton bGenLb = new JButton("3. " + Idiomas.GenLabel[iId]);
		bGenLb.setToolTipText(Idiomas.I4[iId]);
		bGenLb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// iniciamos el proceso
				if(LB.Etiquetado(fLb1, fLb2, tLabel, tLabel2, null, "", true, chLabelAscii.isSelected(), tMaxLabel.getText())>0) {
					lbTotLabel.setText(String.valueOf(tLabel.getModel().getRowCount()) + " " + Idiomas.Words[iId]);
					lbTotLabel2.setText(String.valueOf(tLabel2.getModel().getRowCount()) + " " + Idiomas.Words[iId]);
					JOptionPane.showMessageDialog(null, Idiomas.I3[iId], Idiomas.Info[iId], JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		bGenLb.setForeground(Color.BLUE);
		bGenLb.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGenLb.setBounds(512, 108, 184, 25);
		pLabel.add(bGenLb);        	

		JLabel lblPalabrasEtiquetadas = new JLabel(Idiomas.WordLabel[iId] + ":");
		lblPalabrasEtiquetadas.setForeground(Color.RED);
		lblPalabrasEtiquetadas.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPalabrasEtiquetadas.setBounds(89, 160, 204, 16);
		pLabel.add(lblPalabrasEtiquetadas);

		JLabel lblPalabrasSinEtiquetar = new JLabel(Idiomas.WordnoLabel[iId] + ":");
		lblPalabrasSinEtiquetar.setForeground(Color.RED);
		lblPalabrasSinEtiquetar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPalabrasSinEtiquetar.setBounds(701, 160, 190, 16);
		pLabel.add(lblPalabrasSinEtiquetar);

		prLabel = new JProgressBar();
		prLabel.setStringPainted(true);
		prLabel.setForeground(Color.RED);
		prLabel.setBounds(795, 610, 327, 25);
		pLabel.add(prLabel);

		JButton btnEtiquetar = new JButton("5. " + Idiomas.Labeled[iId]);
		btnEtiquetar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// etiquetamos el fichero 
				// llamamos a la función mediante una tarea nueva para que se actualiza la barra de progreso
				final Task tkLabel = new Task("Etiquetando ...", 'E');
				tkLabel.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equalsIgnoreCase("progress")) {
							int progress = tkLabel.getProgress();
							if (progress == 0) {
								prLabel.setIndeterminate(true);
							} else {
								prLabel.setIndeterminate(false);
								prLabel.setValue(progress);
							}
						}
					}
				});
				tkLabel.execute();

			}
		});
		btnEtiquetar.setForeground(Color.BLUE);
		btnEtiquetar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEtiquetar.setBounds(584, 609, 184, 25);
		pLabel.add(btnEtiquetar);
 
		// tab Etiquetas (Label2.java) pLabel2 etiquetado con Freeling  ---------------------------------------------

		LB2 = new Label2();

		JPanel pLabel2 = new JPanel();
		pLabel2.setForeground(Color.BLACK);
		tabbedPane.addTab(Idiomas.tabula[iId][7], null, pLabel2, null);
		tabbedPane.setForegroundAt(7, new Color(0, 128, 0));
		pLabel2.setLayout(null);

		JLabel lbLabelIn = new JLabel("1. " + Idiomas.FileInput[iId]);
		lbLabelIn.setForeground(Color.BLUE);
		lbLabelIn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbLabelIn.setBounds(37, 26, 151, 16);
		pLabel2.add(lbLabelIn);

		JTextField tFileLbIn = new JTextField();
		tFileLbIn.setToolTipText(Idiomas.tFileInput[iId]);
		tFileLbIn.setForeground(Color.RED);
		tFileLbIn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileLbIn.setEditable(false);
		tFileLbIn.setColumns(10);
		tFileLbIn.setBounds(200, 23, 524, 22);
		pLabel2.add(tFileLbIn);

		JButton bLabelIn = new JButton(Idiomas.Select[iId]);
		bLabelIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fLbF1 = fRet;
						tFileLbIn.setText(fLbF1.getName());	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bLabelIn.setBounds(757, 23, 123, 25);
		pLabel2.add(bLabelIn);

		JLabel lbLabelF = new JLabel("2. " + Idiomas.Freeling[iId] + ":");
		lbLabelF.setForeground(Color.BLUE);
		lbLabelF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbLabelF.setBounds(37, 73, 151, 16);
		pLabel2.add(lbLabelF);

		JTextField tFileLbF = new JTextField();
		tFileLbF.setForeground(Color.RED);
		tFileLbF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileLbF.setEditable(false);
		tFileLbF.setColumns(10);
		tFileLbF.setBounds(200, 70, 524, 22);
		pLabel2.add(tFileLbF);

		JButton bLabelF = new JButton(Idiomas.Select[iId]);
		bLabelF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fLbF2 = fRet;
						tFileLbF.setText(fLbF2.getName());	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bLabelF.setBounds(757, 70, 123, 25);
		pLabel2.add(bLabelF);

		JLabel lbLabelO= new JLabel("3. " + Idiomas.FileOutput[iId]);
		lbLabelO.setForeground(Color.BLUE);
		lbLabelO.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbLabelO.setBounds(37, 126, 144, 16);
		pLabel2.add(lbLabelO);

		JTextField tFileLbO = new JTextField();
		tFileLbO.setForeground(Color.RED);
		tFileLbO.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileLbO.setEditable(false);
		tFileLbO.setColumns(10);
		tFileLbO.setBounds(200, 123, 524, 22);
		pLabel2.add(tFileLbO);

		JButton bLabelO = new JButton(Idiomas.Select[iId]);
		bLabelO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fLbF3 = fRet;
						if(!fLbF3.getName().contains(".")) fLbF3 = new File(fLbF3.getPath() + ".txt"); 
						tFileLbO.setText(fLbF3.getName());	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bLabelO.setBounds(757, 123, 123, 25);
		pLabel2.add(bLabelO);

		JButton btnFreeling = new JButton("4. " + Idiomas.Labeled[iId]);
		btnFreeling.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(LB2.Freeling(fLbF1, fLbF2, fLbF3)>0)
					JOptionPane.showMessageDialog(null, Idiomas.I3[iId], Idiomas.Info[iId], JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnFreeling.setForeground(Color.BLUE);
		btnFreeling.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnFreeling.setBounds(37, 183, 184, 25);
		pLabel2.add(btnFreeling);    	

// tab POS (Stylo.java) Estilometria -----------------------------------

		ST = new Stylo();

		JPanel pStylo = new JPanel();
		pStylo.setForeground(Color.BLACK);
		tabbedPane.addTab(Idiomas.tabula[iId][8], null, pStylo, null);
		tabbedPane.setForegroundAt(8, new Color(0, 128, 0));
		pStylo.setLayout(null);

		JLabel stLabel1 = new JLabel("1. " + Idiomas.FileInput[iId]);
		stLabel1.setForeground(Color.BLUE);
		stLabel1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		stLabel1.setBounds(37, 26, 151, 16);
		pStylo.add(stLabel1);

		tFileSt1 = new JTextField();
		tFileSt1.setToolTipText(Idiomas.tFileInput[iId]);
		tFileSt1.setForeground(Color.RED);
		tFileSt1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFileSt1.setEditable(false);
		tFileSt1.setColumns(10);
		tFileSt1.setBounds(200, 23, 601, 22);
		pStylo.add(tFileSt1);

		JButton bStylo1 = new JButton(Idiomas.Select[iId]);
		//bKey1.setToolTipText("Seleccionar fichero A");

		bStylo1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fSt1 = fRet;
						tFileSt1.setText(fSt1.getName());	
						/*if(fSt2 == null) {
							fSt2 = fSt1;
							tSt2In.setText(fSt1.getName());
						}*/	
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bStylo1.setBounds(832, 23, 123, 25);
		pStylo.add(bStylo1);

		// palabras y etiquetas ...
		JLabel lblStRel1 = new JLabel("2. " + Idiomas.Label1[iId] + ":");
		lblStRel1.setForeground(Color.BLUE);
		lblStRel1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStRel1.setBounds(37, 70, 158, 16);
		pStylo.add(lblStRel1);

		tStRel1 = new JTextField();
		tStRel1.setForeground(Color.RED);
		tStRel1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tStRel1.setBounds(210, 67, 107, 22);
		pStylo.add(tStRel1);
		tStRel1.setColumns(10);

		JLabel lblStRel2 = new JLabel(Idiomas.Label2[iId] + ":");
		lblStRel2.setForeground(Color.BLUE);
		lblStRel2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStRel2.setBounds(389, 70, 166, 16);
		pStylo.add(lblStRel2);

		tStRel2 = new JTextField();
		tStRel2.setForeground(Color.RED);
		tStRel2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tStRel2.setBounds(544, 67, 107, 22);
		pStylo.add(tStRel2);
		tStRel2.setColumns(10);

		JLabel lblStSig = new JLabel(Idiomas.Signos[iId] + ":");
		lblStSig.setForeground(Color.BLUE);
		lblStSig.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStSig.setToolTipText(Idiomas.tSignos[iId]);
		lblStSig.setBounds(798, 111, 236, 16);
		pStylo.add(lblStSig);

		tStSig = new JTextField();
		tStSig.setForeground(Color.RED);
		tStSig.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tStSig.setBounds(1046, 108, 107, 22);
		pStylo.add(tStSig);
		tStSig.setColumns(10);
		tStSig.setText(".");

		JLabel lbTotStylo = new JLabel("0 " + Idiomas.Founds[iId]);
		lbTotStylo.setHorizontalAlignment(SwingConstants.RIGHT);
		lbTotStylo.setForeground(Color.RED);
		lbTotStylo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbTotStylo.setBounds(413, 212, 521, 25);
		pStylo.add(lbTotStylo);	

		JCheckBox lStSoneto = new JCheckBox(Idiomas.Poem[iId]);
		lStSoneto.setForeground(Color.BLUE);
		lStSoneto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lStSoneto.setToolTipText(Idiomas.tPoem[iId]);
		lStSoneto.setBounds(1024, 213, 151, 25);
		pStylo.add(lStSoneto);

		JCheckBox lStCont = new JCheckBox("4. " + Idiomas.Continuo[iId]);
		lStCont.setForeground(Color.BLUE);
		lStCont.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lStCont.setToolTipText(Idiomas.tContinuo[iId]);
		lStCont.setBounds(37, 154, 353, 25);
		pStylo.add(lStCont);

		tStCont = new JTextField();
		tStCont.setForeground(Color.RED);
		tStCont.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tStCont.setBounds(398, 155, 36, 22);
		pStylo.add(tStCont);
		tStCont.setColumns(2);

		JLabel lblSpanish = new JLabel(Idiomas.Español[iId] + ":");
		lblSpanish.setForeground(Color.BLUE);
		lblSpanish.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSpanish.setBounds(798, 140, 204, 16);
		pStylo.add(lblSpanish);

		JCheckBox lStGenero = new JCheckBox(Idiomas.Genero[iId]);
		lStGenero.setForeground(Color.BLUE);
		lStGenero.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lStGenero.setToolTipText(Idiomas.tGenero[iId]);
		lStGenero.setBounds(786, 165, 198, 25);
		pStylo.add(lStGenero);

		JCheckBox lStNumero = new JCheckBox(Idiomas.Numero[iId]);
		lStNumero.setForeground(Color.BLUE);
		lStNumero.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lStNumero.setToolTipText(Idiomas.tNumero[iId]);
		lStNumero.setBounds(1011, 165, 166, 25);
		pStylo.add(lStNumero);

		JTextPane txtSoloEsp = new JTextPane();
		txtSoloEsp.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtSoloEsp.setText(" 3. " + Idiomas.BreakRel[iId] + ":");
		txtSoloEsp.setForeground(Color.BLUE);
		txtSoloEsp.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSoloEsp.setBounds(741, 71, 452, 128);
		txtSoloEsp.setBackground(SystemColor.control);
		pStylo.add(txtSoloEsp);

		JLabel lbStEsta = new JLabel("");
		lbStEsta.setForeground(Color.RED);
		lbStEsta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbStEsta.setBounds(78, 242, 1115, 30);
		pStylo.add(lbStEsta);

		// tabla
		JScrollPane scStylo = new JScrollPane();
		scStylo.setToolTipText(Idiomas.tClickView[iId]);
		scStylo.setBounds(77, 274, 1115, 342);
		pStylo.add(scStylo);
		tStylo = new JTable();
		tStylo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !thTrabajo.isAlive()) {  // doble click, pasamos a Visualizar y buscamos la linea en el fichero
					try {
						tabbedPane.setSelectedIndex(1);  // cambio a la carpeta de visualizar
						thTrabajo = new Thread() {
							public void run() {
								try {
									String sCelda = "";
									if(lStSoneto.isSelected()) {
										sCelda = (String) tStylo.getModel().getValueAt(tStylo.getSelectedRow(), 3);
									} else {
										sCelda = (String) tStylo.getModel().getValueAt(tStylo.getSelectedRow(), 1);
									}
									sCelda = g.quitarHTML(sCelda);
									if(fVer != fSt1 || edTexto.getText().trim().length() == 0) {  // si es un fichero diferente, hay que cargarlo
										fVer = fSt1;
										tFileView.setText(fVer.getName());
										StringBuffer sB = new StringBuffer();
										String lin;
										BufferedReader in = g.TipoFichero(fVer);
										lbFormato.setText(g.sTipFile);
										lbBOM.setText(g.sBOM);
										while ((lin = in.readLine()) != null) {
											sB.append( lin);
											sB.append( "\r\n" );
										}	  						

										bCancelar.setVisible(true);
										lbInfo.setText(Idiomas.LoadingFile[iId]);
										edTexto.setContentType("text/plain");
										sTexto = sB.toString();	
										edTexto.setText(sTexto);
										edTexto.setCaretPosition(0);  // nos posicionamos al principio
										lbInfo.setText(Idiomas.FileLoaded[iId]);
										bCancelar.setVisible(false);
									}  // fin if

									// seleccionamos la línea en cuestión 
									int pos = sTexto.indexOf(sCelda.toString());
									//System.out.println(sCelda + "|" + sTexto.substring(0,20) + "|" + String.valueOf(pos));
									if(pos>-1) {
										int newline = sTexto.substring(0,pos).split("\r",-1).length-1;
										pos = pos - newline;  // le quitamos los retornos de carro
										edTexto.setCaretPosition(pos);
										edTexto.moveCaretPosition(pos + sCelda.length());
										Rectangle rView;
										rView = edTexto.modelToView(pos);
										rView.setSize((int)rView.getWidth(),(int)rView.getHeight() + 300);
										edTexto.scrollRectToVisible(rView);
										edTexto.requestFocusInWindow();
									}

								} catch (Exception e) {
									e.printStackTrace();
								} // muevo la línea al centro aprox.
							}
						};
						thTrabajo.start();
						//							while(thTrabajo.isAlive()); // esperamos que acabe
					} catch (Exception ex) {
						ex.printStackTrace();
					}	
				}
			}
		});    	
		scStylo.setViewportView(tStylo);

		JButton bNoStyloTxt = new JButton(Idiomas.SaveTxt[iId]);
		bNoStyloTxt.setToolTipText(Idiomas.tSaveTxt[iId]);
		bNoStyloTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tStylo.getRowCount()>0) {
					g.GuardarFichero('t', tStylo, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bNoStyloTxt.setForeground(Color.BLUE);
		bNoStyloTxt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bNoStyloTxt.setBounds(125, 629, 132, 25);
		pStylo.add(bNoStyloTxt);    	

		JButton bGenSt = new JButton("5. " + Idiomas.GenRelations[iId]);
		bGenSt.setToolTipText(Idiomas.I4[iId]);
		bGenSt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// iniciamos el proceso
				if(ST.BuscarRelacion(fSt1, tStRel1.getText().toUpperCase(), tStRel2.getText().toUpperCase(), tStylo, lStSoneto.isSelected(), tStSig.getText(), lStGenero.isSelected(), lStNumero.isSelected(), lStCont.isSelected(), tStCont.getText())>0) {
					fMiles = (float) (int) tStylo.getModel().getRowCount()*1000/g.numWords;
					lbTotStylo.setText(String.valueOf(tStylo.getModel().getRowCount()) + " " + Idiomas.Founds[iId] + " ... " + (String) String.format("%10.2f%n",fMiles).trim() + " " + Idiomas.MilesCounts[iId] + " (" + String.valueOf(g.numWords) + " " + Idiomas.Words[iId] + ") " );
					lbStEsta.setText(g.sTemp);
					JOptionPane.showMessageDialog(null, Idiomas.I3[iId], Idiomas.Info[iId], JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		bGenSt.setForeground(Color.BLUE);
		bGenSt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGenSt.setBounds(514, 154, 184, 25);
		pStylo.add(bGenSt);        	

		JLabel lblStylo1 = new JLabel(Idiomas.Relations1[iId] + ":");
		lblStylo1.setForeground(Color.RED);
		lblStylo1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStylo1.setBounds(87, 216, 123, 16);
		pStylo.add(lblStylo1);

		JLabel label_6 = new JLabel("New label");
		label_6.setBounds(798, 113, 56, 16);
		pStylo.add(label_6);


// tab POS2 (Stylo2.java) -------------------------------------------------------------------------
		ST2 = new Stylo2();
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.CEILING);

		JPanel pStylo2 = new JPanel();
		tabbedPane.addTab(Idiomas.tabula[iId][9], null, pStylo2, null);
		tabbedPane.setForegroundAt(9, new Color(0, 128, 0));
		pStylo2.setLayout(null);

		JLabel lSt2In = new JLabel("1. " + Idiomas.FileInputs[iId]);
		lSt2In.setForeground(Color.BLUE);
		lSt2In.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lSt2In.setBounds(12, 24, 178, 16);
		pStylo2.add(lSt2In);

		tSt2In = new JTextField();
		tSt2In.setToolTipText(Idiomas.tFileInputs[iId]);
		tSt2In.setForeground(Color.RED);
		tSt2In.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tSt2In.setEditable(false);
		tSt2In.setColumns(10);
		tSt2In.setBounds(190, 21, 477, 22);
		pStylo2.add(tSt2In);

		JButton bSt2In = new JButton(Idiomas.Select[iId]);
		bSt2In.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					//fRet = g.abrirArchivo('t');
					fRets = g.abrirMArchivo('t');
					if(fRets != null) {
						fSt2 = fRets;
						String sFile = "";
						for(int i=0;i<fSt2.length;i++) sFile = sFile + fSt2[i].getName() + " ";
						tSt2In.setText(sFile);	
					} 

					/*if(fRet != null) {
						fSt2 = fRet;
						tSt2In.setText(fSt2.getName());	
						// si no hay fichero seleccionado en los demas ponemos éste ...
						if(fBusca == null) {
							fBusca = fSt2;
							tFileTxt.setText(fBusca.getName());	
						}
						if(fVer == null) {
							fVer = fSt2;
							tFileView.setText(fVer.getName());	
						}						
						if(fWord == null) {
							fWord = fSt2;
							tFileWord.setText(fWord.getName());
						}	
						if(fSt1 == null) {
							fSt1 = fSt2;
							tFileSt1.setText(fSt2.getName());
						}	
					}*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bSt2In.setBounds(679, 19, 123, 25);
		pStylo2.add(bSt2In);

		JLabel lSt2HelpFiles = new JLabel("");
		lSt2HelpFiles.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lSt2HelpFiles.setBounds(814, 18, 28, 32);
		lSt2HelpFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.tHelpFiles2[iId],Idiomas.HelpFiles[iId], JOptionPane.INFORMATION_MESSAGE );	
			}
		});
		pStylo2.add(lSt2HelpFiles);

		JLabel lSt2HelpFiles2 = new JLabel("");
		lSt2HelpFiles2.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lSt2HelpFiles2.setBounds(1132, 229, 28, 32);
		lSt2HelpFiles2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.wCounts[iId],Idiomas.iCounts[iId], JOptionPane.INFORMATION_MESSAGE );	
			}
		});
		pStylo2.add(lSt2HelpFiles2);

		
		JLabel lSt2Pat = new JLabel("2. " + Idiomas.St2PatLabel[iId] + ":");
		lSt2Pat.setForeground(Color.BLUE);
		lSt2Pat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lSt2Pat.setBounds(11, 73, 167, 16);
		pStylo2.add(lSt2Pat);

		JTextField tSt2Pat = new JTextField();
		tSt2Pat.setForeground(Color.RED);
		tSt2Pat.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tSt2Pat.setBounds(188, 70, 634, 22);
		pStylo2.add(tSt2Pat);
		tSt2Pat.setColumns(10);

		JCheckBox lStGenero2 = new JCheckBox(Idiomas.Genero[iId]);
		lStGenero2.setForeground(Color.BLUE);
		lStGenero2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lStGenero2.setToolTipText(Idiomas.tGenero[iId]);
		lStGenero2.setBounds(21, 142, 157, 25);
		pStylo2.add(lStGenero2);

		JCheckBox lStNumero2 = new JCheckBox(Idiomas.Numero[iId]);
		lStNumero2.setForeground(Color.BLUE);
		lStNumero2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lStNumero2.setToolTipText(Idiomas.tNumero[iId]);
		lStNumero2.setBounds(213, 142, 166, 25);
		pStylo2.add(lStNumero2);

		JTextPane txtSoloEsp2 = new JTextPane();
		txtSoloEsp2.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtSoloEsp2.setText(" 3A. " + Idiomas.Relation[iId] + ". " + Idiomas.Español[iId] + ":");
		txtSoloEsp2.setForeground(Color.BLUE);
		txtSoloEsp2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSoloEsp2.setBounds(12, 108, 380, 72);
		txtSoloEsp2.setBackground(SystemColor.control);
		pStylo2.add(txtSoloEsp2);
		
		JRadioButton rbSt2Abs = new JRadioButton(Idiomas.Absolut[iId], true);
		rbSt2Abs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbSt2Abs.setForeground(Color.BLUE);
		rbSt2Abs.setBounds(654, 142, 157, 25);
		pStylo2.add(rbSt2Abs);
		
		JRadioButton rbSt2Rel = new JRadioButton(Idiomas.Porcent[iId]);
		rbSt2Rel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbSt2Rel.setForeground(Color.BLUE);
		rbSt2Rel.setBounds(815, 142, 186, 25);
		pStylo2.add(rbSt2Rel);
		
		//Group the radio buttons.
        ButtonGroup bgSt2Est = new ButtonGroup();
        bgSt2Est.add(rbSt2Abs);
        bgSt2Est.add(rbSt2Rel);

		JTextPane txtEstad2 = new JTextPane();
		txtEstad2.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtEstad2.setText(" 3B. " + Idiomas.Estad[iId]);
		txtEstad2.setForeground(Color.BLUE);
		txtEstad2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtEstad2.setBounds(642, 108, 380, 72);
		txtEstad2.setBackground(SystemColor.control);
		pStylo2.add(txtEstad2);		

		JLabel lbSt2Found = new JLabel("0 " + Idiomas.Founds[iId]);
		lbSt2Found.setHorizontalAlignment(SwingConstants.CENTER);
		lbSt2Found.setForeground(Color.RED);
		lbSt2Found.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbSt2Found.setBounds(38, 236, 1063, 25);
		pStylo2.add(lbSt2Found);	

		// tabla
		JScrollPane scStylo2 = new JScrollPane();
		scStylo2.setToolTipText(Idiomas.tClickView[iId]);
		scStylo2.setBounds(12, 274, 1238, 342);
		pStylo2.add(scStylo2);
		JTable tStylo2 = new JTable();
		tStylo2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {  // fSt2.length == 1 &&
				if (lSt2Item && e.getClickCount() == 2 && !thTrabajo.isAlive()) {  // doble click, pasamos a Visualizar y buscamos la linea en el fichero
					try {
						tabbedPane.setSelectedIndex(1);  // cambio a la carpeta de visualizar
						thTrabajo = new Thread() {
							public void run() {
								try {
									String sCelda = "";
									int iFil=0;
									sCelda = (String) tStylo2.getModel().getValueAt(tStylo2.getSelectedRow(), 4);
									sCelda = g.quitarHTML(sCelda);
									String sFileSelected = (String) tStylo2.getModel().getValueAt(tStylo2.getSelectedRow(), 0);
									for(int i=0;i<fSt2.length;i++) {
										String sFileuno = fSt2[i].getName();
										if(sFileuno.equals(sFileSelected)) {
											iFil=i;
										}
									}
									if(fVer != fSt2[iFil] || edTexto.getText().trim().length() == 0) {  // si es un fichero diferente, hay que cargarlo
										fVer = fSt2[iFil];
										tFileView.setText(fVer.getName());
										StringBuffer sB = new StringBuffer();
										String lin;
										BufferedReader in = g.TipoFichero(fVer);
										lbFormato.setText(g.sTipFile);
										lbBOM.setText(g.sBOM);
										while ((lin = in.readLine()) != null) {
											sB.append( lin);
											sB.append( "\r\n" );
										}	  						

										bCancelar.setVisible(true);
										lbInfo.setText(Idiomas.LoadingFile[iId]);
										edTexto.setContentType("text/plain");
										sTexto = sB.toString();	
										edTexto.setText(sTexto);
										edTexto.setCaretPosition(0);  // nos posicionamos al principio
										lbInfo.setText(Idiomas.FileLoaded[iId]);
										bCancelar.setVisible(false);
									}  // fin if

									// seleccionamos la línea en cuestión 
									int pos = sTexto.indexOf(sCelda.toString());
									//System.out.println(sCelda + "|" + sTexto.substring(0,20) + "|" + String.valueOf(pos));
									if(pos>-1) {
										int newline = sTexto.substring(0,pos).split("\r",-1).length-1;
										pos = pos - newline;  // le quitamos los retornos de carro
										edTexto.setCaretPosition(pos);
										edTexto.moveCaretPosition(pos + sCelda.length());
										Rectangle rView;
										rView = edTexto.modelToView(pos);
										rView.setSize((int)rView.getWidth(),(int)rView.getHeight() + 300);
										edTexto.scrollRectToVisible(rView);
										edTexto.requestFocusInWindow();
									}


								} catch (Exception e) {
									e.printStackTrace();
								} // muevo la línea al centro aprox.
							}
						};
						thTrabajo.start();
						//							while(thTrabajo.isAlive()); // esperamos que acabe
					} catch (Exception ex) {
						ex.printStackTrace();
					}	
				}
			}
		});    	
		scStylo2.setViewportView(tStylo2);

		JButton bNoStyloTxt2 = new JButton("Guardar Txt");
		bNoStyloTxt2.setToolTipText(Idiomas.tSaveTxt[iId]);
		bNoStyloTxt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tStylo2.getRowCount()>0) {
					g.GuardarFichero('t', tStylo2, null);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bNoStyloTxt2.setForeground(Color.BLUE);
		bNoStyloTxt2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bNoStyloTxt2.setBounds(125, 629, 132, 25);
		pStylo2.add(bNoStyloTxt2);    	
		
		JButton bSt2Excel = new JButton(Idiomas.SaveExcel[iId]);
		bSt2Excel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bSt2Excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!lSt2Item && tStylo2.getRowCount()>0) {
					g.SaveFile('e', tStylo2, null, Idiomas.Ocurren[iId] + ";" + Idiomas.Frecuency[iId]);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bSt2Excel.setForeground(Color.BLUE);
		bSt2Excel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bSt2Excel.setBounds(371, 629, 139, 25);
		pStylo2.add(bSt2Excel);		
		
		JButton bSt2ExcelD = new JButton(Idiomas.SaveExcelD[iId]);
		bSt2ExcelD.setToolTipText(Idiomas.tSaveExcel[iId]);
		bSt2ExcelD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!lSt2Item && tStylo2.getRowCount()>0) {
					g.SaveFile('d', tStylo2, null, Idiomas.Ocurren[iId]);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bSt2ExcelD.setForeground(Color.BLUE);
		bSt2ExcelD.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bSt2ExcelD.setBounds(647, 629, 186, 25);
		pStylo2.add(bSt2ExcelD);		

		
		JButton bSt2Gen = new JButton("4A. " + Idiomas.I4[iId]);
		bSt2Gen.setToolTipText(Idiomas.tGenCorpus[iId]);
		bSt2Gen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lSt2Item=true;
				lbSt2Found.setText(Idiomas.ProcessingFile[iId]);
				ST2.BuscarPatron(fSt2, tSt2Pat.getText(), tStylo2, lStGenero2.isSelected(), lStNumero2.isSelected());
				lbSt2Found.setText(String.valueOf(g.iWordsFound) + " " + Idiomas.Founds[iId]);
			}
		});
		bSt2Gen.setForeground(Color.BLUE);
		bSt2Gen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bSt2Gen.setBounds(404, 127, 197, 25);
		pStylo2.add(bSt2Gen);

		JButton bSt2Est = new JButton("4B. " + Idiomas.GenEstad[iId]);
		//bSt2Est.setToolTipText(Idiomas.tGenCorpus[iId]);
		bSt2Est.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lSt2Item=false;
				lbSt2Found.setText(Idiomas.ProcessingFile[iId]);
				ST2.BuscarEstad(fSt2, tSt2Pat.getText(), tStylo2, rbSt2Abs.isSelected());
				lbSt2Found.setText(String.valueOf(g.numWords) + " " + Idiomas.Founds[iId] + ",    " +
						String.valueOf(g.numWords2) + " " + Idiomas.Words[iId]	+ ",    " +
						(String) df.format((float) g.numWords*100/g.numWords2) + " " + Idiomas.Media[iId]);
				g.Order(tStylo2, 0, true);
			}
		});
		bSt2Est.setForeground(Color.BLUE);
		bSt2Est.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bSt2Est.setBounds(1039, 127, 197, 25);
		pStylo2.add(bSt2Est);

		
		// tab POS3 (Stylo3.java) -------------------------------------------------------------------------

		JPanel pStylo3 = new JPanel();
		tabbedPane.addTab(Idiomas.tabula[iId][10], null, pStylo3, null);
		tabbedPane.setForegroundAt(10, new Color(0, 128, 0));
		pStylo3.setLayout(null);
	
		JLabel lSt3In = new JLabel("1. " + Idiomas.FileInputs[iId]);
		lSt3In.setForeground(Color.BLUE);
		lSt3In.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lSt3In.setBounds(12, 24, 169, 16);
		pStylo3.add(lSt3In);

		tSt3In = new JTextField();
		//tFileKey2.setToolTipText("Fichero B");
		tSt3In.setForeground(Color.RED);
		tSt3In.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tSt3In.setEditable(false);
		tSt3In.setColumns(10);
		tSt3In.setBounds(202, 23, 581, 22);
		pStylo3.add(tSt3In);

		// tabla ficheros
		JScrollPane scMFile = new JScrollPane();
		scMFile.setToolTipText(Idiomas.tClickView[iId]);
		scMFile.setBounds(12, 58, 771, 144);
		pStylo3.add(scMFile);
		JTable tMFile = new JTable();
		scMFile.setViewportView(tMFile);
		DefaultTableModel mFile = new ModelConsulta(); //DefaultTableModel();
		mFile.addColumn(Idiomas.NumFil[iId]);
		mFile.addColumn(Idiomas.FileInputs[iId]);
		tMFile.setModel(mFile);
		tMFile.getColumn(Idiomas.NumFil[iId]).setMaxWidth(80);
		
		JButton bSt3In = new JButton(Idiomas.Select[iId]);
		bSt3In.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRets = g.abrirMArchivo('t');
					if(fRets != null) {
							for(int i=0;i<fRets.length;i++) {
								fSt3.add(fRets[i]);
								if(sMFiles==null) sMFiles = fRets[i].getName();
								else sMFiles = sMFiles + "; ();" + fRets[i].getName();
								mFile.addRow(new Object[]{(String) String.format("%1$3s",String.valueOf(fSt3.size())),
										fRets[i].getAbsolutePath()});
							}
							tSt3In.setText(sMFiles);
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bSt3In.setBounds(809, 21, 123, 25);
		pStylo3.add(bSt3In);
		
		JButton bSt3Clean = new JButton(Idiomas.Clean[iId]);
		bSt3Clean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					tSt3In.setText("");
					sMFiles=null;
					mFile.setRowCount(0);
					fSt3.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bSt3Clean.setBounds(809, 70, 123, 25);
		pStylo3.add(bSt3Clean);		
		
		JLabel lbHMFiles = new JLabel("");
		lbHMFiles.setIcon(new ImageIcon(ventana.class.getResource("/images/help2.png")));
		lbHMFiles.setBounds(946, 20, 28, 32);
		lbHMFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null,Idiomas.tHelpFiles2[iId],Idiomas.HelpFiles[iId], JOptionPane.INFORMATION_MESSAGE );	
			}
		});
		pStylo3.add(lbHMFiles);

		JCheckBox lSt3Genero = new JCheckBox(Idiomas.Genero[iId]);
		lSt3Genero.setForeground(Color.BLUE);
		lSt3Genero.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lSt3Genero.setToolTipText(Idiomas.tGenero[iId]);
		lSt3Genero.setBounds(853, 194, 157, 25);
		pStylo3.add(lSt3Genero);

		JCheckBox lSt3Numero = new JCheckBox(Idiomas.Numero[iId]);
		lSt3Numero.setForeground(Color.BLUE);
		lSt3Numero.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lSt3Numero.setToolTipText(Idiomas.tNumero[iId]);
		lSt3Numero.setBounds(1038, 194, 166, 25);
		pStylo3.add(lSt3Numero);

		JTextPane txtSoloEsp3 = new JTextPane();
		txtSoloEsp3.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtSoloEsp3.setText(" 3. " + Idiomas.Español[iId] + ":");
		txtSoloEsp3.setForeground(Color.BLUE);
		txtSoloEsp3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSoloEsp3.setBounds(836, 164, 380, 72);
		txtSoloEsp3.setBackground(SystemColor.control);
		pStylo3.add(txtSoloEsp3);
		
		JLabel lbSt3Found = new JLabel(Idiomas.ClicHeader[iId]);
		lbSt3Found.setHorizontalAlignment(SwingConstants.RIGHT);
		lbSt3Found.setForeground(Color.BLUE);
		lbSt3Found.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbSt3Found.setBounds(347, 236, 413, 25);
		pStylo3.add(lbSt3Found);	

		// tabla etiquetas
		JScrollPane scStylo3 = new JScrollPane();
		scStylo3.setToolTipText(Idiomas.tClickView[iId]);
		scStylo3.setBounds(12, 274, 1238, 342);
		pStylo3.add(scStylo3);
		JTable tStylo3 = new JTable();
		scStylo3.setViewportView(tStylo3);
		tStylo3.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
		        int col = tStylo3.columnAtPoint(e.getPoint());
		        g.Order(tStylo3,col,false);
		        String name = tStylo3.getColumnName(col);
		        //System.out.println("Column index selected " + col + " " + name);
			}
		});

		JButton bNoStyloTxt3 = new JButton(Idiomas.SaveTxt[iId]);
		bNoStyloTxt3.setToolTipText(Idiomas.tSaveTxt[iId]);
		bNoStyloTxt3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tStylo3.getRowCount()>0) {
					g.SaveFile('t', tStylo3, null, Idiomas.Relation[iId] + ";" + "Total;" + sMFiles);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}		
			}
		});
		bNoStyloTxt3.setForeground(Color.BLUE);
		bNoStyloTxt3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bNoStyloTxt3.setBounds(125, 629, 132, 25);
		pStylo3.add(bNoStyloTxt3);    
		
		JButton bSt3Excel = new JButton(Idiomas.SaveExcel[iId]);
		bSt3Excel.setToolTipText(Idiomas.tSaveExcel[iId]);
		bSt3Excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tStylo3.getRowCount()>0) {
					g.SaveFile('e', tStylo3, null, Idiomas.Relation[iId] + ";" + "Total;();" + sMFiles);
				} else {
					JOptionPane.showMessageDialog(null, Idiomas.E3[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bSt3Excel.setForeground(Color.BLUE);
		bSt3Excel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bSt3Excel.setBounds(371, 629, 139, 25);
		pStylo3.add(bSt3Excel);		
		
		JRadioButton rbSt3Dos = new JRadioButton(Idiomas.DosLabel[iId]);
		rbSt3Dos.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbSt3Dos.setForeground(Color.BLUE);
		rbSt3Dos.setBounds(985, 110, 127, 25);
		pStylo3.add(rbSt3Dos);
		
		JRadioButton rbSt3Tres = new JRadioButton(Idiomas.TresLabel[iId], true);
		rbSt3Tres.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbSt3Tres.setForeground(Color.BLUE);
		rbSt3Tres.setBounds(1116, 110, 127, 25);
		pStylo3.add(rbSt3Tres);		
		
		//Group the radio buttons.
        ButtonGroup bgRel = new ButtonGroup();
        bgRel.add(rbSt3Dos);
        bgRel.add(rbSt3Tres);
		
		JTextPane txtSt3Tip = new JTextPane();
		txtSt3Tip.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtSt3Tip.setText(" 2. " + Idiomas.TipRelation[iId] + ":");
		txtSt3Tip.setForeground(Color.BLUE);
		txtSt3Tip.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSt3Tip.setBounds(968, 65, 282, 83);
		txtSt3Tip.setBackground(SystemColor.control);
		pStylo3.add(txtSt3Tip);

		ST3 = new Stylo3();
		JButton bSt3Gen = new JButton("4. " + Idiomas.GenRelations[iId]);
		bSt3Gen.setToolTipText(Idiomas.GenRelations[iId]);
		bSt3Gen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ST3.ProcesarFiles(fSt3, tStylo3, rbSt3Tres.isSelected(), lSt3Genero.isSelected(), lSt3Numero.isSelected());
				//lbSt3Found.setText(String.valueOf(totPalabras) + " " + Idiomas.Founds[iId]);
			}
		});
		bSt3Gen.setForeground(Color.BLUE);
		bSt3Gen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bSt3Gen.setBounds(12, 216, 197, 25);
		pStylo3.add(bSt3Gen);
		
			
// tab Corpus (Corpus.java) -------------------------------------------------------------------------

		JPanel pCorpus = new JPanel();
		tabbedPane.addTab(Idiomas.tabula[iId][11], null, pCorpus, null);
		tabbedPane.setForegroundAt(11, new Color(0, 128, 0));
		pCorpus.setLayout(null);

		JLabel lCorpus1 = new JLabel("1. " + Idiomas.FileInput[iId]);
		lCorpus1.setForeground(Color.BLUE);
		lCorpus1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lCorpus1.setBounds(12, 24, 157, 16);
		pCorpus.add(lCorpus1);

		JTextField tCorpusIn = new JTextField();
		tCorpusIn.setToolTipText(Idiomas.tFileInput[iId]);
		tCorpusIn.setForeground(Color.RED);
		tCorpusIn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tCorpusIn.setEditable(false);
		tCorpusIn.setColumns(10);
		tCorpusIn.setBounds(178, 23, 486, 22);
		pCorpus.add(tCorpusIn);

		JButton bCorpusIn = new JButton(Idiomas.Select[iId]);
		bCorpusIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					fRet = g.abrirArchivo('t');
					if(fRet != null) {
						fEntrada = fRet;
						tCorpusIn.setText(fEntrada.getName());	
						// si no hay fichero seleccionado en los demas ponemos éste ...
						if(fBusca == null) {
							fBusca = fEntrada;
							tFileTxt.setText(fBusca.getName());	
						}
						if(fVer == null) {
							fVer = fEntrada;
							tFileView.setText(fVer.getName());	
						}						
						if(fWord == null) {
							fWord = fEntrada;
							tFileWord.setText(fWord.getName());
						}	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bCorpusIn.setBounds(699, 21, 123, 25);
		pCorpus.add(bCorpusIn);

		JLabel lCorpus2 = new JLabel("2. " + Idiomas.FileOutput[iId]);
		lCorpus2.setForeground(Color.BLUE);
		lCorpus2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lCorpus2.setBounds(12, 70, 150, 16);
		pCorpus.add(lCorpus2);

		JTextField tCorpusOut = new JTextField();
		tCorpusOut.setToolTipText(Idiomas.tFileOutput[iId]);
		tCorpusOut.setForeground(Color.RED);
		tCorpusOut.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tCorpusOut.setEditable(false);
		tCorpusOut.setColumns(10);
		tCorpusOut.setBounds(178, 67, 486, 22);
		pCorpus.add(tCorpusOut);

		JButton bCorpusOut = new JButton(Idiomas.Select[iId]);
		bCorpusOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					JFileChooser jfSalida=new JFileChooser();
					if(g.fDir != null)
						jfSalida.setCurrentDirectory(g.fDir);
					jfSalida.showSaveDialog(null);
					g.fDir = jfSalida.getCurrentDirectory();
					fSalida = jfSalida.getSelectedFile();
					tCorpusOut.setText(fSalida.getName());
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, Idiomas.E4[iId], Idiomas.Warning[iId], JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		bCorpusOut.setBounds(692, 67, 130, 25);
		pCorpus.add(bCorpusOut);

		JLabel lCorpus3 = new JLabel("3. " + Idiomas.MaxCorpus[iId] + ":");
		lCorpus3.setForeground(Color.BLUE);
		lCorpus3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lCorpus3.setBounds(12, 120, 158, 16);
		pCorpus.add(lCorpus3);

		tCorpus1 = new JTextField();
		tCorpus1.setForeground(Color.RED);
		tCorpus1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tCorpus1.setBounds(178, 117, 107, 22);
		pCorpus.add(tCorpus1);
		tCorpus1.setColumns(10);    	

		JLabel lCorpus5 = new JLabel(Idiomas.InCorpus[iId] + ":");
		lCorpus5.setForeground(Color.BLUE);
		lCorpus5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lCorpus5.setBounds(12, 169, 158, 16);
		pCorpus.add(lCorpus5);

		JLabel lbWordCorpus = new JLabel("0 " + Idiomas.WordCorpus[iId]);
		lbWordCorpus.setHorizontalAlignment(SwingConstants.RIGHT);
		lbWordCorpus.setForeground(Color.RED);
		lbWordCorpus.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbWordCorpus.setBounds(188, 164, 164, 25);
		pCorpus.add(lbWordCorpus);	    	    	

		JLabel lbLinCorpus = new JLabel("0 " + Idiomas.LinCorpus[iId]);
		lbLinCorpus.setHorizontalAlignment(SwingConstants.RIGHT);
		lbLinCorpus.setForeground(Color.RED);
		lbLinCorpus.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbLinCorpus.setBounds(386, 164, 164, 25);
		pCorpus.add(lbLinCorpus);	    	    	

		JLabel lbPoeCorpus = new JLabel("0 " + Idiomas.PoeCorpus[iId]);
		lbPoeCorpus.setHorizontalAlignment(SwingConstants.RIGHT);
		lbPoeCorpus.setForeground(Color.RED);
		lbPoeCorpus.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbPoeCorpus.setBounds(600, 164, 150, 25);
		pCorpus.add(lbPoeCorpus);	    	    	

		JLabel lCorpus6 = new JLabel(Idiomas.OutCorpus[iId] + ":");
		lCorpus6.setForeground(Color.BLUE);
		lCorpus6.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lCorpus6.setBounds(12, 210, 158, 16);
		pCorpus.add(lCorpus6);

		JLabel lbWordCorpus2 = new JLabel("0 " + Idiomas.WordCorpus[iId]);
		lbWordCorpus2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbWordCorpus2.setForeground(Color.RED);
		lbWordCorpus2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbWordCorpus2.setBounds(188, 205, 164, 25);
		pCorpus.add(lbWordCorpus2);	    	    	

		JLabel lbLinCorpus2 = new JLabel("0 " + Idiomas.LinCorpus[iId]);
		lbLinCorpus2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbLinCorpus2.setForeground(Color.RED);
		lbLinCorpus2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbLinCorpus2.setBounds(386, 205, 164, 25);
		pCorpus.add(lbLinCorpus2);	    	    	

		JLabel lbPoeCorpus2 = new JLabel("0 " + Idiomas.PoeCorpus[iId]);
		lbPoeCorpus2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbPoeCorpus2.setForeground(Color.RED);
		lbPoeCorpus2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbPoeCorpus2.setBounds(600, 205, 150, 25);
		pCorpus.add(lbPoeCorpus2);	    	    	    	

		CO = new Corpus();
		JButton bGenerarCorpus = new JButton("4. " + Idiomas.GenCorpus[iId]);
		bGenerarCorpus.setToolTipText(Idiomas.tGenCorpus[iId]);
		bGenerarCorpus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				totPalabras = CO.ObtenerCorpus(fEntrada, fSalida, tCorpus1.getText());
				lbWordCorpus.setText(String.valueOf(g.numWords) + " " + Idiomas.WordCorpus[iId]);
				lbLinCorpus.setText(String.valueOf(g.iLineas) + " " + Idiomas.LinCorpus[iId]);
				lbPoeCorpus.setText(String.valueOf(g.numPoet) + " " + Idiomas.PoeCorpus[iId]);
				lbWordCorpus2.setText(String.valueOf(g.numWords2) + " " + Idiomas.WordCorpus[iId]);
				lbLinCorpus2.setText(String.valueOf(g.iLineas2) + " " + Idiomas.LinCorpus[iId]);
				lbPoeCorpus2.setText(String.valueOf(g.numPoet2) + " " + Idiomas.PoeCorpus[iId]);
			}
		});
		bGenerarCorpus.setForeground(Color.BLUE);
		bGenerarCorpus.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bGenerarCorpus.setBounds(331, 116, 197, 25);
		pCorpus.add(bGenerarCorpus);



	}  // fin initialize


	class Task extends SwingWorker<Void, Void> {
		// clase tarea para que se pinte la barra de progreso
		private String text;
		private char Tip;

		public Task(String text, char cTip) {
			this.text = text;
			this.Tip = cTip;
		}

		@Override
		public Void doInBackground() {
			setProgress(0);
			try {
				if(Tip == 'C')
					BC.Concordancias(cPalabras, fEntrada, fSalida, prBarra);
				//BC.BuscarConcorda(cPalabras, fEntrada, fSalida, prBarra);
				else if(Tip == 'E')
					if(LB.Etiquetar(fLb1, fLb3, tLabel, prLabel, chLabelAscii.isSelected(), tMaxLabel.getText())>0) {
						JOptionPane.showMessageDialog(null, Idiomas.I1[iId], Idiomas.Info[iId], JOptionPane.INFORMATION_MESSAGE);
					}
			} catch (Exception e) {
			}
			//setProgress(100);
			return null;
		}
		/*
		   @Override
		   public void done() {
		       System.out.println(text + " is done");
		   }
		 */
	}	// fin class Task

	private class HighlightRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

			// everything as usual
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			// added behavior
			//if (value!=null && sMarca !=null && value.toString().matches(".*"+Pattern.quote(sMarca)+".*")) {
			if(isSelected) {	
				// this will customize that kind of border that will be use to highlight a row
				setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
			} else
				setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

			return this;
		}
	}


	private class ComboBoxRenderer extends JLabel implements ListCellRenderer {


		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		public  Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			//Get the selected index. (The index param isn't
			//always valid, so just use the value.)
			int selectedIndex = ((Integer)value).intValue();

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			//Set the icon and text.  If icon was null, say so.
			ImageIcon icon = iconos[selectedIndex];
			String pet = paises[selectedIndex];
			setIcon(icon);
			if (icon != null) {
				setText(pet);
				setFont(list.getFont());
			} else {
				//setUhOhText(pet + " (no image available)",
				//		list.getFont());
			}

			return this;
		}
	}	
}  // fin ventana.java

