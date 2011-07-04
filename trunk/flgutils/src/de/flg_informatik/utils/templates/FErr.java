package de.flg_informatik.utils.templates;

public class FErr extends Err {
/**
	 * 
	 */
	private static final long serialVersionUID = 8750665169721274864L;
	private static final String fatal="\nBitte verständigen Sie einen Computerbetreuer -- bitte klicken Sie auf Details\n" +
								"und machen Sie mit der Tastenkombination 'Alt-Druck' einen  'Screenshot' in die Zwischenablage\n" +
								"öffnen in Office eine neue Textdatei, und fügen den 'Screenshot' dort mit Strg+v ein;\n " +
								"speichern Sie diese Datei dann und machen sie dem Computerbetreuer zugänglich!\n" +
								"Klicken Sie dann auf OK -- das Programm wird beendet!";
	public FErr(Throwable e){
		super(fatal,e);
	}

	public FErr(String text){
		super(text+fatal);
		
	}
	public FErr(String text,Throwable e){
		//text="Fehlermeldung der JVM: "+e.getMessage();
		super (text+fatal,e);
	}
	public FErr(){
		super("Interner Fehler im Programm!\n" +
				"Wahrscheinlich ist es unser Fehler -- Entschuldigung!");
	}
	
}
