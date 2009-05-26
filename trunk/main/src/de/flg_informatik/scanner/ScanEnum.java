package de.flg_informatik.scanner;

import java.io.File;

public enum ScanEnum implements ScanParams{
	
	usbserial (),
	serial ( ScanFile.getScanner(adapter,parameter)),
	file ( ScanFile.getScanner(adapter,parameter))
	;
	Scanner scanner;
	ScanEnum(){
		//linux only
			if (this.name().equals("usbserial")){
				for (int i=0; i<16; i++){
					if (ScanFile.getScanner(adapter, new File("/dev/ttyUSB"+i) )!=null){
						break;
					}
				}
				
					
			
			}
			if (this.name().equals("file")){
				
				}
				
					
			
			
		
	}
	ScanEnum(Scanner scanner){
		this.scanner=scanner;
		
		
	}
	

}
