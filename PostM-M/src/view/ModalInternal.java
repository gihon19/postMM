package view;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class ModalInternal extends JInternalFrame {
	private JRootPane rootPane;
	private Component desktop;

	public ModalInternal(JRootPane r, Component d) {
		// TODO Auto-generated constructor stub
		rootPane=r;
		desktop=d;
		setModal();
	}
	
	public void setModal( ){
		final JPanel glass = new JPanel();
	    glass.setOpaque(false);

	    // Attach mouse listeners
	    MouseInputAdapter adapter = new MouseInputAdapter(){};
	    glass.addMouseListener(adapter);
	    glass.addMouseMotionListener(adapter);

	    // Add in option pane
	    //getContentPane().add(pane, BorderLayout.CENTER);
	    
	    JButton btnOtro = new JButton("Otro");
	    getContentPane().add(btnOtro, BorderLayout.EAST);

	    // Define close behavior
	    PropertyChangeListener pcl = new PropertyChangeListener() {
	    	
	    	public void propertyChange(PropertyChangeEvent event) {
	    		
	    		if (isVisible() && (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) ||event.getPropertyName().equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
	    			
	    			try {
	    					setClosed(true);
					    } 
	    			catch (PropertyVetoException ignored) {
					 }
	    			ModalInternal.this.setVisible(false);
	    			glass.setVisible(false);
	    		}
	    	}
	    };
	    //pane.addPropertyChangeListener(pcl);
	    getContentPane().addPropertyChangeListener(pcl);

	    // Change frame border
	    putClientProperty("JInternalFrame.frameType","optionDialog");

	    // Size frame
	    Dimension size = getPreferredSize();
	    Dimension rootSize = desktop.getSize();

	    setBounds((rootSize.width - size.width) / 2, (rootSize.height - size.height) / 2,  size.width, size.height); 
	    desktop.validate(); 
	    
	    try {
	    	setSelected(true);
	    } 
	    catch (PropertyVetoException ignored) {
	    }

	    // Add modal internal frame to glass pane
	    glass.add(this);
	
	    // Change glass pane to our panel
	    rootPane.setGlassPane(glass);
	
	    // Show glass pane, then modal dialog
	    glass.setVisible(true);
	}
	
	public void setVisible(boolean value) {
		
		super.setVisible(value);
		if (value) {
			startModal();
		} else {
			stopModal();
		}
	}

	private synchronized void startModal() {
		
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				EventQueue theQueue =  getToolkit().getSystemEventQueue();
				while (isVisible()) {
					
					AWTEvent event = theQueue.getNextEvent();
					Object source = event.getSource();
					if (event instanceof ActiveEvent) {
						((ActiveEvent)event).dispatch();
					} else if (source instanceof Component) {
						
						((Component)source).dispatchEvent(event);
						} else if (source instanceof MenuComponent) {
							((MenuComponent)source).dispatchEvent(event);
							} else {
								System.err.println("Unable to dispatch: " + event);
							}
				}
			} else {
				while (isVisible()) {
					wait();
				}
			}
		} 
		catch (InterruptedException ignored) {
		}
	}

	private synchronized void stopModal() {
		notifyAll();
	}

}
