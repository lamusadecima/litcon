
public class Idiomas {
	public static final int IDIOMAS = 2; 

	// tabuladores
	//public static final String [][] tabula = {{"Créditos", "Visor fichero", "Concordancia", "Listado", "Wordlist", "Contraste", "Palabras clave", "Etiq.", "Etiq. II", "POS", "POS II", "POS III", "Corpus"}, 
	//		 {"Credits", "File view", "Concord", "List", "Wordlist", "Contrast", "Keywords", "Label", "Label II", "POS", "POS II", "POS III", "Corpus"}};
	public static final String [][] tabula = {{"Créditos", "Visor fichero", "Concordancia", "Listado", "Wordlist", "Contraste", "Palabras clave", "Etiq.", "POS", "POS II", "POS III", "Corpus"}, 
			 {"Credits", "File view", "Concord", "List", "Wordlist", "Contrast", "Keywords", "Label", "POS", "POS II", "POS III", "Corpus"}};
	// Visualizador de fichero
	public static final String [] FileView = {"Fichero a visualizar", "File input"};
	public static final String [] Format = {"Formato:", "Format:"};
	public static final String [] tFiletoView = {"Visualizar un fichero", "View a file"};
	
	// Concordancias
	public static final String [] FileInput = {"Fichero de Entrada:", "File Input:"};
	public static final String [] tFileInput = {"Fichero a procesar", "File to process"};
	public static final String [] FileInputs = {"Fichero/s de Entrada:", "File/s Input:"};
	public static final String [] tFileInputs = {"Múltiples ficheros a procesar", "Multiple files to process"};
	public static final String [] ColShow = {"3. Columnas a mostrar:", "3. Columns to show:"};
	public static final String [] Poem = {"Datos de poema", "Poem data"};
	public static final String [] tPoem = {"Incluir nombre y n\u00FAmero de verso", "Included vers name and number"};
	public static final String [] tSeek = {"Criterio de búsqueda", "Type of seek"};
	public static final String [] HelpSeek = {"Ayuda de buscar", "Seek help"};
	public static final String [] tHelpSeek = {"Máscaras de búsqueda:" + "\r\n" + "* cero o más caracteres \r\n (derecha o izquierda)" + "\r\n" + "+ 0-1 carácter" + "\r\n" 
			  + "? un carácter" + "\r\n" + "@ 0-1 palabra" + "\r\n" + "| or (max. 9)" + "\r\n" + "& and (max. 9)",
              "Search mark:" + "\r\n" + "* zero or more character \r\n (left or right)" + "\r\n" + "+ 0-1 character" + "\r\n" 
					  + "? one character" + "\r\n" + "@ 0-1 word" + "\r\n" + "| or (max. 9)" + "\r\n" + "& and (max. 9)"};

	// Palabras
	public static final String [] Founds = {"encontradas", "results"};
	public static final String [] Words = {"Palabras", "Words"};
	public static final String [] Media = {"Media", "Average"};

	// comandos/botones
	public static final String [] Select = {"Seleccionar", "Select"};
	public static final String [] Cancel = {"Cancelar", "Cancel"};
	public static final String [] Seek = {"Buscar", "Search"};
	public static final String [] View = {"Visualizar", "View"};
	public static final String [] Colorear = {"Colorear", "Colour"};
	public static final String [] HideLabel = {"Ocultar etiquetas", "Hide label"};
	public static final String [] Alfabethic = {"Alfabética", "Alfabethic"};
	public static final String [] Counts = {"Por valores", "Frequency"};
	public static final String [] Execute = {"Ejecutar", "Generate"};
	public static final String [] SaveTxt = {"Guardar Txt", "Save Txt"};
	public static final String [] tSaveTxt = {"Guardar en formato texto", "Save in txt format"};
	public static final String [] SaveExcel = {"Guardar Excel", "Save Excel"};
	public static final String [] tSaveExcel = {"Guardar en formato csv (Excel)", "Save in csv format (Excel)"};
	public static final String [] SaveExcelD = {"Guardar detalle Excel", "Save Excel detail"};
	public static final String [] SaveFile = {"Salvar a Fichero", "Save file"};
	public static final String [] LoadFile = {"Cargar de fichero", "Load file"};
	public static final String [] Mayusculas = {"May./Min.", "Capital/Lowercase"};
	public static final String [] MilesCounts = {"Frec./1000", "Freq./1000"};
	public static final String [] GenLabel = {"Generar etiquetas", "Generate label"};
	public static final String [] Labeled = {"Etiquetar fichero", "Tagged file"};
	public static final String [] GenRelations = {"Generar relaciones", "Generate relations"};
	public static final String [] Clean = {"Limpiar", "Clean"};
	public static final String [] GenEstad = {"Generar estadísticas", "Generate statistics"};
	
	// Listado
	public static final String [] FileOutput = {"Fichero de Salida:", "File Output:"};
	public static final String [] tFileOutput = {"Fichero con resultados", "File with results:"};
	public static final String [] WordsEx = {"Palabras a excluir (opcional):", "Excluded tokens (optional):"};
	public static final String [] GenWords = {"Generar Palabras:", "Generate tokens:"};
	public static final String [] tGenWords = {"Generar palabras a buscar", "Generate token words"};
	public static final String [] GenConcord = {"Generar Concordancias:", "Launch concordances:"};

	// WodList
	public static final String [] Order = {"Ordenaci\u00F3n:", "Order:"};
	
	// Mensajes  
	public static final String [] LoadingFile = {"Cargando fichero ...", "Loading file ..."};
	public static final String [] FileLoaded = {"Fichero cargado", "File loaded"};
	public static final String [] ProcessingFile = {"Procesando fichero ...", "Processing file ..."};
	public static final String [] FileProcessed = {"Fichero procesado", "File processed"};
	public static final String [] ProccesInterrupt = {"Proceso interrumpido", "Interrupted process"};
	public static final String [] Processing = {"Procesando ", "Processing "};
	public static final String [] FileSaved = {"Fichero guardado", "File saved"};
	public static final String [] FileNoSaved = {"Fichero no guardado", "File not saved"};
	
	// Errores/infos
	public static final String [] Warning = {"Advertencia", "Warning"};
	public static final String [] Info = {"Información", "Information"};
	public static final String [] E1 = {"Espere que termine la tarea anterior", "Please wait until the first task is finished"};
	public static final String [] E2 = {"No se ha visualizado el fichero", "File cannot be viewed"};
	public static final String [] E3 = {"No se han generado datos a guardar", "Data was not saved"};
	public static final String [] E4 = {"Problema al seleccionar fichero de salida", "Problems in file output selection"};
	public static final String [] E5 = {"No se han generado datos a ordenar", "Not data to order"};
	public static final String [] E6 = {"No se ha seleccionado el archivo de entrada", "File input not selected"};
	public static final String [] E7 = {"No se ha seleccionado el archivo de salida", "File output not selected"};
	public static final String [] E8 = {"No se han recogido palabras", "Not procesed words"};
	public static final String [] E9 = {"No se ha introducido criterio de busqueda", "Search type not indicated"};
	public static final String [] E10 = {"Error de sintaxis en el criterio de busqueda", "Sintax error in search type"};
	public static final String [] E11 = {"No se ha seleccionado el archivo A", "File A not selected"};
	public static final String [] E12 = {"No se ha seleccionado el archivo B", "File B not selected"};
	public static final String [] E13 = {"No se ha encontrado el archivo", "File not found"};
	public static final String [] E14 = {"Error al leer el fichero", "Error while reading file"};
	public static final String [] E15 = {"No se ha seleccionado el diccionario", "File dictionary not selected"};
	public static final String [] E16 = {"Campo numérico no es número", "Numeric field is not number"};
	public static final String [] E17 = {"El texto tiene menos palabras que el máximo", "Text has less word than maximum words"};
	public static final String [] E18 = {"Máximo 20 ficheros", "20 Files maximum"};
	public static final String [] E19 = {"Criterio de busqueda incorrecto", "Search type wrong"};

	public static final String [] I1 = {"Fichero generado", "File genetared"};
	public static final String [] I2 = {"Cambia a caracteres ASCII no extendido (128)", "Change to ASCII char not extended (128)"};
	public static final String [] I3 = {"Etiquetas generadas", "Labels genetared"};
	public static final String [] I4 = {"Generar items", "Generate items"};
	
	// Contraste o Exclusiones
	public static final String [] FileA = {"Fichero A:", "File A:"};
	public static final String [] FileAs = {"Fichero/s A:", "File/s A:"};
	public static final String [] FileB = {"Fichero B:", "File B:"};
	public static final String [] FileBs = {"Fichero/s B:", "File/s B:"};
	public static final String [] FileWordsEx = {"Fichero de exclusiones (opcional):", "File with excluded tokens (optional):"};
	public static final String [] tFileWordsEx = {"Seleccionar fichero de palabras a excluir", "Select file of excluded tokens"};
	public static final String [] WordsAnoB = {"Palabras de A que no est\u00E1n en B:", "Words in A but not in B:"};
	public static final String [] WordsBnoA = {"Palabras de B que no est\u00E1n en A:", "Words in B but not in A:"};
	public static final String [] WordsAB = {"Palabras que est\u00E1n en los dos:", "Words in A and B:"};
	public static final String [] GenContrast = {"Generar contraste", "Execute Contrast"};
	public static final String [] tGenContrast = {"Generar contraste entre el fichero A y el B", "Generate contrast between A and B"};
	
	// Palabras clave
	public static final String [] Corpus = {"Corpus de Referencia:", "Reference Corpus"};
	public static final String [] GenKeys = {"Generar claves", "Generate keywords"};
	public static final String [] tGenKeys = {"Generar claves del fichero a procesar", "Generate keywords of file input"};
	public static final String [] PositiveKeys = {"Claves positivas", "Positive keywords"};
	public static final String [] NegativeKeys = {"Claves negativas", "Negative keywords"};

	// cabeceras
	public static final String [] FileConcord = {"Palabra\r\n  NumLinea       Poema          NumVerso Linea\r\n", 
			"Word\r\n  NumLine       Poem          NumVerso Line\r\n"};
	public static final String [] FileName = {"NomFichero", "FileName"};
	public static final String [] NumLin = {"NumLinea", "NumLine"};
	public static final String [] NumFil = {"NumFil", "NumFile"};
	public static final String [] PoemTit = {"Título de poema", "Poem title"};
	public static final String [] NumVers = {"NumVerso", "NumVerse"};
	public static final String [] Verso = {"Verso", "Verse"};
	public static final String [] ListConcord = {"Listado de Concordancias", "Concordances list"};
	public static final String [] Orden = {"Orden", "Order"};
	public static final String [] Word = {"Palabra", "Word"};
	public static final String [] Ocurren = {"Ocurrencias", "Occurrences"};
	public static final String [] Frecuency = {"Frecuencia", "Frecuency"};
	public static final String [] Key = {"Clave", "Key"};
	public static final String [] Relation = {"Relación", "Relationship"};
	public static final String [] PoemAnt = {"Anterior", "Previous"};
	public static final String [] PoemPos = {"Posterior", "Later"};
	
	// etiquetado
	public static final String [] Ascii = {"Carac. Ascii", "Ascii char"};
	public static final String [] MaxCharLabel = {"Máx. Carac. Etiqueta", "Max. char label"};
	public static final String [] WordLabel = {"Palabras etiquetadas", "Words labeled"};
	public static final String [] WordnoLabel = {"Palabras sin etiquetar", "Words not labeled"};
	public static final String [] Dictionary = {"Diccionario", "Dictionary"};
	public static final String [] HelpDic = {"Ayuda de diccionario", "Dictionary help"};
	public static final String [] tHelpDic = {"Formato del fichero txt, ordenado alfabéticamente:" + "\r\n" + "cada linea: palabra en minusculas + espacio + etiqueta en mayúsculas" + "\r\n" + "ejemplo: amor NCMS000" + "\r\n", 
            "File txt format, alphabetic ordered:" + "\r\n" + "every line: word lowercase + space + label uppercase" + "\r\n" + "example: love NCMS000" + "\r\n"};	
	public static final String [] HelpFiles = {"Selección múltiple", "Multiple selection"};
	public static final String [] tHelpFiles = {"Permite seleccionar más de un fichero," + "\r\n" + "que se unirán formando el Corpus", 
			" You can selecet multiple files," + "\r\n" + "they will appended in a unique Corpus"};
	public static final String [] tHelpFiles2 = {"Permite seleccionar más de un fichero," + "\r\n" + "para ser procesados", 
			" You can selecet multiple files," + "\r\n" + "to be process"};
	public static final String [] Freeling = {"Fichero Freeling", "Freeling File"};
	
	// estilometria
	public static final String [] Relations1 = {"Relación A-B", "Relations A-B"};	
	public static final String [] Label1 = {"Etiqueta o palabra A", "Label or word A"};	
	public static final String [] Label2 = {"Etiqueta o palabra B", "Label or word B"};	
	public static final String [] tClickView = {"Doble click para ver en fichero", "Double click to view in file"};	
	public static final String [] Signos = {"Signos de puntuación en medio", "Punctuation marks"};	
	public static final String [] tSignos = {"Si encuentra estos signos, la relación se descarta", "If it find these signs, the relation is discarded"};	
	public static final String [] Estad = {"Estadística: ", "Statistics: "};	
	public static final String [] Español = {"Solo en textos en español", "Only for Spanish texts"};
	public static final String [] BreakRel = {"Descartar relación (opcional)", "Discard relationship (optional)"};
	public static final String [] Genero = {"Ruptura por género", "Gender concordance"};
	public static final String [] tGenero = {"Si no coincide género, no se considera", "If gender does not match, it is not considered"};
	public static final String [] Numero = {"Ruptura por número", "Number concordance"};
	public static final String [] tNumero = {"Si no coincide número, no se considera", "If number does not match, it is not considered"};
	public static final String [] Continuo = {"Relación con palabras en medio (opcional):", "Relationship with words in between (optional):"};
	public static final String [] tContinuo = {"Busca la relación con n palabras en medio", "Look for the relationship with n words in between"};

	// Estilometria II
	public static final String [] St2PatLabel = {"Patrón de etiquetado", "Label pattern"};
	public static final String [] Absolut = {"Valores absolutos", "Absolute values"};
	public static final String [] tAbsolut = {"Muestra valores aboslutos", "Show absolute values"};
	public static final String [] Porcent = {"Valores en porcentaje", "Percentage values"};
	public static final String [] tPorcent = {"Muestra valores en tanto por ciento", "Show values in percent"};
	public static final String [] iCounts = {"Conteo de palabras", "Word count"};
	public static final String [] wCounts = {"El cómputo de palabras se realiza considerando que una palabra consiste en un elemento más una etiqueta", "For word count, a word is considered every element that appears with a tag"};

	// Estilometria III
	public static final String [] ClicHeader = {"Clic en cabecera para ordenar por esa columna", "Clic on header to order this column"};
	public static final String [] TipRelation = {"Tipo de relación", "Type of relationship"};
	public static final String [] DosLabel = {"Dos etiquetas", "Two labels"};
	public static final String [] TresLabel = {"Tres etiquetas", "Three labels"};
	
	// Corpus
	public static final String [] GenCorpus = {"Generar Corpus", "Generate Corpus"};
	public static final String [] tGenCorpus = {"Generar Corpus de texto", "Generate Corpus from text"};
	public static final String [] MaxCorpus = {"Palabras Corpus", "Corpus words"};
	public static final String [] WordCorpus = {"Palabras", "Words"};
	public static final String [] LinCorpus = {"Líneas", "Lines"};
	public static final String [] PoeCorpus = {"Poemas", "Poems"};
	public static final String [] InCorpus = {"Estadísticas de entrada", "Input statictics"};
	public static final String [] OutCorpus = {"Estadísticas de salida", "Output statictics"};

}
