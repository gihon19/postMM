package view;


import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

public class Prueba2 {
	public static final int CLOSED_OPTION = -1;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JOptionPane panel=new JOptionPane();
		panel.putClientProperty(true,  Boolean.TRUE);
		
		Component fo = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		panel.setInitialValue(null);
		
		
		 JInternalFrame dialog = panel.createInternalFrame(null, "Uno Ventana InterDestopFrame");
		 
		 panel.selectInitialValue();
	     dialog.setVisible(true);
	     
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

	        if (null instanceof JInternalFrame) {
	            try {
	                ((JInternalFrame)null).setSelected(true);
	            } catch (java.beans.PropertyVetoException e) {
	            }
	        }

	        Object selectedValue = panel.getValue();

	        if (fo != null && fo.isShowing()) {
	            fo.requestFocus();
	        }
	       /* if (selectedValue == null) {
	            return CLOSED_OPTION;
	        }
	        if (options == null) {
	            if (selectedValue instanceof Integer) {
	                return ((Integer)selectedValue).intValue();
	            }
	            return CLOSED_OPTION;
	        }
	        for(int counter = 0, maxCounter = options.length;
	            counter < maxCounter; counter++) {
	            if (options[counter].equals(selectedValue)) {
	                return counter;
	            }
	        }
	        return CLOSED_OPTION;*/

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
