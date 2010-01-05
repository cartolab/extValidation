package es.udc.cartolab.gvsig.eielvalidation.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class gvWindow extends JPanel implements IWindow {

	private WindowInfo viewInfo = null;

	public WindowInfo getWindowInfo(){
		JFrame aux = new JFrame();
		aux.add(this);
		aux.pack();

		if (viewInfo == null) {
			viewInfo=new WindowInfo();
		}
		viewInfo.setHeight(aux.getHeight());
		viewInfo.setWidth(aux.getWidth());
		return viewInfo;
	}
}
