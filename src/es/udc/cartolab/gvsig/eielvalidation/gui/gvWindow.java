package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class gvWindow extends JPanel implements IWindow {

	protected WindowInfo viewInfo = null;
	
	
	public gvWindow(){
		viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	}

	public WindowInfo getWindowInfo(){
//		JFrame aux = new JFrame();
//		aux.add(this);
//		aux.pack();
//		if (viewInfo == null) {
//			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
//		}
//		viewInfo.setHeight(aux.getHeight());
//		viewInfo.setWidth(aux.getWidth());
//		aux.remove(this);
//		aux.dispose();
		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
		}
		viewInfo.setHeight(500);
		viewInfo.setWidth(600);
		return viewInfo;
	}


	public void setTitle(String title){
		viewInfo.setTitle(title);
	}
}
