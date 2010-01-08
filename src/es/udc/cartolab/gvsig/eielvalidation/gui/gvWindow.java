package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class gvWindow extends JPanel implements IWindow {

	protected WindowInfo viewInfo = null;
	private boolean INITIAL_SIZE = false;
	private int width = 400;
	private int height = 400;

	public gvWindow(){
		this(400, 400, true);
	}

	public gvWindow(int width, int height){
		this(width, height, true);
	}
	
	public gvWindow(int width, int height, boolean resizable){
		INITIAL_SIZE = true;
		if (resizable) {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
		} else {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
		}
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
