package view;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class HijoJOptionPanel extends JOptionPane {

	public HijoJOptionPanel() {
		// TODO Auto-generated constructor stub
	}
	
	
	 public static JDesktopPane getDesktopPaneForComponent(Component parentComponent) {
	        if(parentComponent == null)
	            return null;
	        if(parentComponent instanceof JDesktopPane)
	            return (JDesktopPane)parentComponent;
	        return getDesktopPaneForComponent(parentComponent.getParent());
	    }
	 
	 public static JOptionPane getDesktopPaneForComponent2(Component parentComponent) {
	        if(parentComponent == null)
	            return null;
	        if(parentComponent instanceof JOptionPane)
	            return (JOptionPane)parentComponent;
	        return getDesktopPaneForComponent2(parentComponent.getParent());
	    }
	
	
	public JInternalFrame getJOptionIntenalModal(JInternalFrame frame,Component parentComponent,String title){
		Container parent;
		if(parentComponent instanceof JDesktopPane)
			parent =this.getDesktopPaneForComponent(parentComponent);
		else
			parent =this.getDesktopPaneForComponent2(parentComponent);

        if (parent == null && (parentComponent == null ||
                (parent = parentComponent.getParent()) == null)) {
            throw new RuntimeException("JOptionPane: parentComponent does " +
                    "not have a valid parent");
        }

        // Option dialogs should be closable only
        final JInternalFrame  iFrame = frame;

        iFrame.putClientProperty("JInternalFrame.frameType", "optionDialog");
        iFrame.putClientProperty("JInternalFrame.messageType",
                                 Integer.valueOf(getMessageType()));

        iFrame.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                if (getValue() == UNINITIALIZED_VALUE) {
                    setValue(null);
                }
            }
        });
        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                // Let the defaultCloseOperation handle the closing
                // if the user closed the iframe without selecting a button
                // (newValue = null in that case).  Otherwise, close the dialog.
                if (iFrame.isVisible() &&
                        event.getSource() == HijoJOptionPanel.this &&
                        event.getPropertyName().equals(VALUE_PROPERTY)) {
                // Use reflection to get Container.stopLWModal().
                try {
                    Method method = AccessController.doPrivileged(
                        new ModalPrivilegedAction(
                            Container.class, "stopLWModal"));
                    if (method != null) {
                        method.invoke(iFrame, (Object[])null);
                    }
                } catch (IllegalAccessException ex) {
                } catch (IllegalArgumentException ex) {
                } catch (InvocationTargetException ex) {
                }

                try {
                    iFrame.setClosed(true);
                }
                catch (java.beans.PropertyVetoException e) {
                }

                iFrame.setVisible(false);
                }
            }
        });
        iFrame.getContentPane().add(this, BorderLayout.CENTER);
        
        if (parent instanceof JDesktopPane) {
            parent.add(iFrame, JLayeredPane.MODAL_LAYER);
        } else {
            parent.add(iFrame, BorderLayout.CENTER);
        }
        
       /* if(parent instanceof JOptionPane){
        	 parent.add(iFrame, JLayeredPane.MODAL_LAYER);
        }else{
        	parent.add(iFrame, BorderLayout.CENTER);
        }*/
        
        Dimension iFrameSize = iFrame.getPreferredSize();
        Dimension rootSize = parent.getSize();
        Dimension parentSize = parentComponent.getSize();

        iFrame.setBounds((rootSize.width - iFrameSize.width) / 2,
                         (rootSize.height - iFrameSize.height) / 2,
                         iFrameSize.width, iFrameSize.height);
        // We want dialog centered relative to its parent component
        Point iFrameCoord =
          SwingUtilities.convertPoint(parentComponent, 0, 0, parent);
        int x = (parentSize.width - iFrameSize.width) / 2 + iFrameCoord.x;
        int y = (parentSize.height - iFrameSize.height) / 2 + iFrameCoord.y;

        // If possible, dialog should be fully visible
        int ovrx = x + iFrameSize.width - rootSize.width;
        int ovry = y + iFrameSize.height - rootSize.height;
        x = Math.max((ovrx > 0? x - ovrx: x), 0);
        y = Math.max((ovry > 0? y - ovry: y), 0);
        iFrame.setBounds(x, y, iFrameSize.width, iFrameSize.height);

        parent.validate();
        try {
            iFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}

        return iFrame;
    }
	

	
	
	public  int showInternalOptionDialog(JInternalFrame frame,Component parentComponent) {
		  
			Object initialValue=null;
			Object[] options1=null;
			Icon icon=null;
		 	String title="Hijo";
		 	
		 	
		 	int messageType1=super.INFORMATION_MESSAGE;
		 			 
			
				/*JOptionPane pane = new JOptionPane();//"Hola", messageType1,optionType, icon, options1, initialValue);
				pane.putClientProperty(true,Boolean.TRUE);
				Component fo = KeyboardFocusManager.getCurrentKeyboardFocusManager().
				getFocusOwner();*/
				
				//pane.setInitialValue(initialValue);
				
				JInternalFrame dialog =getJOptionIntenalModal(frame,parentComponent,title);
				//this..createInternalFrame(parentComponent, title);
				//pane.selectInitialValue();
				dialog.setVisible(true);
				
				/* Since all input will be blocked until this dialog is dismissed,
				* make sure its parent containers are visible first (this component
				* is tested below).  This is necessary for JApplets, because
				* because an applet normally isn't made visible until after its
				* start() method returns -- if this method is called from start(),
				* the applet will appear to hang while an invisible modal frame
				* waits for input.
				*/
				if (dialog.isVisible() && !dialog.isShowing()) {
				Container parent = dialog.getParent();
				while (parent != null) {
				if (parent.isVisible() == false) {
				parent.setVisible(true);
				}
				parent = parent.getParent();
				}
				}
				
				// Use reflection to get Container.startLWModal.
				try {
				Method method = AccessController.doPrivileged(new ModalPrivilegedAction(
				Container.class, "startLWModal"));
				if (method != null) {
				method.invoke(dialog, (Object[])null);
				}
				} catch (IllegalAccessException ex) {
				} catch (IllegalArgumentException ex) {
				} catch (InvocationTargetException ex) {
				}
				
				if (parentComponent instanceof JInternalFrame) {
					try {
						((JInternalFrame)parentComponent).setSelected(true);
						}
					catch (java.beans.PropertyVetoException e) {
					}
				}
				
				if (parentComponent instanceof JOptionPane) {
					((JOptionPane)parentComponent).selectInitialValue();//.setSelected(true);
				}
				
				/*
				Object selectedValue = pane.getValue();
				
				if (fo != null && fo.isShowing()) {
				fo.requestFocus();
				}
				if (selectedValue == null) {
				return CLOSED_OPTION;
				}
				if (options1 == null) {
				if (selectedValue instanceof Integer) {
				return ((Integer)selectedValue).intValue();
				}
				return CLOSED_OPTION;
				}
				for(int counter = 0, maxCounter = options1.length;
				counter < maxCounter; counter++) {
				if (options1[counter].equals(selectedValue)) {
				return counter;
				}
				}
				return CLOSED_OPTION;*/
				return 1;
		}
	
	private static class ModalPrivilegedAction implements PrivilegedAction<Method> {
        private Class<?> clazz;
        private String methodName;

        public ModalPrivilegedAction(Class<?> clazz, String methodName) {
            this.clazz = clazz;
            this.methodName = methodName;
        }

        public Method run() {
            Method method = null;
            try {
                method = clazz.getDeclaredMethod(methodName, (Class[])null);
            } catch (NoSuchMethodException ex) {
            }
            if (method != null) {
                method.setAccessible(true);
            }
            return method;
        }
    }

}
