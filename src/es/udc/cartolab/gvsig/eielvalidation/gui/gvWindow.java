package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class gvWindow extends JPanel implements IWindow {

	protected WindowInfo viewInfo = null;
	private boolean INITIAL_SIZE = false;
	private int width = 0;
	private int height = 0;

	public gvWindow(){
		viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	}

	public gvWindow(int width, int height){
		INITIAL_SIZE = true;
		viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
		this.width = width;
		this.height = height;
	}

	public WindowInfo getWindowInfo(){
		if (INITIAL_SIZE){
			if (viewInfo == null) {
				viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
			}
			viewInfo.setHeight(height);
			viewInfo.setWidth(width);
		} else {
			JFrame aux = new JFrame();
			aux.add(this);
			aux.pack();
			if (viewInfo == null) {
				viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
			}
			viewInfo.setHeight(aux.getHeight());
			viewInfo.setWidth(aux.getWidth());
			aux.remove(this);
			aux.dispose();
		}
		return viewInfo;
	}


	public void setTitle(String title){
		viewInfo.setTitle(title);
	}
}
