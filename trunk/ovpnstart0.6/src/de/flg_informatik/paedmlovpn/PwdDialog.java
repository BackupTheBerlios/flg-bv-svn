package de.flg_informatik.paedmlovpn;

import java.awt.*;
import de.flg_informatik.utils.FireButton;

class PwdDialog extends Dialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TextEingabe eingabe = new TextEingabe("",20);
	StartVPNControl svpnctrl;
	PwdDialog me;
	OkButton ok=new OkButton("ok");
		
	CancelButton cancel=new CancelButton("cancel");
	Panel buttons=new Panel(new FlowLayout());
	MyWindowAdapter  winadapter =new MyWindowAdapter();
	PwdDialog(StartVPNControl svpnctrl, String titel){
		this(svpnctrl,titel, null);
	}
	PwdDialog(StartVPNControl svpnctrl, String titel, String pw){
		super(svpnctrl.svpn, titel );
		this.addWindowListener(winadapter);
		this.me=this;
		this.svpnctrl=svpnctrl;
		this.setLayout(new GridLayout(3,0));
		this.add(new Label("Bitte "+titel+" eingeben"));
		this.add(eingabe);
		buttons.add(cancel);
		cancel.setLabel("Abbrechen");
		cancel.addActionListener(svpnctrl);
		ok.setLabel("Senden (oder RETURN-Taste)");
		buttons.add(ok);
		ok.addActionListener(svpnctrl);
		this.add(buttons);
		eingabe.addActionListener(svpnctrl);
		eingabe.setBackground(java.awt.Color.WHITE);
		eingabe.setText(pw);
		eingabe.setEchoChar('*');
		this.pack();
		if (pw!=null){
			ok.fire();
		}else{
			this.setVisible(true);
				
		}
	}
	void sendPwdTNC(){
		svpnctrl.stnvpn.send("password \"Private Key\" "+  eingabe.getText());
		svpnctrl.stnvpn.status=StartTelnet.stattnvpncheckpw;
		this.dispose();
	
	}
	void sendPwdMTD(){
		svpnctrl.mvpn.setPw(eingabe.getText());
		this.dispose();
	}
	private class MyWindowAdapter extends java.awt.event.WindowAdapter{
                public void windowClosing(java.awt.event.WindowEvent e){
                	    	e.getWindow().setVisible(true);
                	    	cancel.fire();
                            //dispose is done by StartVPNControl
                    
                    }
	}
   class OkButton extends FireButton{
    	private static final long serialVersionUID = 1L;
    	OkButton(String Title){
    		super(Title);
    	}
    	
    
    }
   class CancelButton extends FireButton{
   	private static final long serialVersionUID = 1L;
   	CancelButton(String Title){
   		super(Title);
   	}
   	
   
   }
   
   
   
	class TextEingabe extends TextField{
		private static final long serialVersionUID = 1L;
		TextEingabe(String str, int width){
			super(str,width);
		}
		
	}
	
	

}

