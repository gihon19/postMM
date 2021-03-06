package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modelo.AbstractJasperReports;
import modelo.Conexion;
import modelo.ReciboPago;
import modelo.dao.ReciboPagoDao;
import view.ViewListaPagos;

public class CtlPagoLista implements ActionListener, MouseListener, ChangeListener, WindowListener  {
	
	
	private ViewListaPagos view;
	private Conexion conexion=null;
	private ReciboPagoDao reciboDao=null;
	private ReciboPago myRecibo=null;
	
	//fila selecciona enla lista
	private int filaPulsada;

	public CtlPagoLista(ViewListaPagos v, Conexion conn) {
		// TODO Auto-generated constructor stub
		view =v;
		conexion=conn;
		view.conectarControlador(this);
		reciboDao=new ReciboPagoDao(conexion);
		cargarTabla(reciboDao.todosRecibo());
		myRecibo=new ReciboPago();
		view.setVisible(true);
	}
	public void cargarTabla(List<ReciboPago> pagos){
		//JOptionPane.showMessageDialog(view, " "+facturas.size());
		this.view.getModelo().limpiar();
		
		if(pagos!=null){
			for(int c=0;c<pagos.size();c++){
				this.view.getModelo().agregarPago(pagos.get(c));
				
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//Recoger qu� fila se ha pulsadao en la tabla
        filaPulsada = this.view.getTablaPagos().getSelectedRow();
        
        //si seleccion una fila
        if(filaPulsada>=0){
        	
        	//Se recoge el id de la fila marcada
            //int idFactura= (int)this.view.getModelo().getValueAt(filaPulsada, 0);
            
           // this.view.getBtnEliminar().setEnabled(true);
           // this.view.getBtnImprimir().setEnabled(true);
            this.myRecibo=this.view.getModelo().getRecibo(filaPulsada);
            //se consigue el proveedore de la fila seleccionada
            //myArticulo=this.view.getModelo().getArticulo(filaPulsada);
        
            
        	//si fue doble click mostrar modificar
        	if (e.getClickCount() == 2) {
        		
        		
        		
        		try {
    				
    				//AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Factura_Saint_Paul_Reimpresion.jasper",myFactura.getIdFactura() );
        			AbstractJasperReports.createReport(conexion.getPoolConexion().getConnection(), 5, myRecibo.getNoRecibo());
        			AbstractJasperReports.showViewer(null);
    				//AbstractJasperReports.imprimierFactura();
    				this.view.getBtnImprimir().setEnabled(false);
    				myRecibo=null;
    			} catch (SQLException ee) {
    				// TODO Auto-generated catch block
    				ee.printStackTrace();
    			}
        	
        		/*ViewFacturar viewFacturar=new ViewFacturar(this.view);
        		CtlFacturar ctlFacturar=new CtlFacturar(viewFacturar,conexion);
        		
        		//si se cobro la factura se debe eleminiar el temp por eso se guarda el id
        		int idFactura=myFactura.getIdFactura();
        		
        		//se llama al controlador de la factura para que la muestre 
        		ctlFacturar.viewFactura(myFactura);//actualizarFactura(myFactura);
        		
        		//si la factura se cobro se regresara null sino modificamos la factura en la lista
        		if(myFactura==null){
        			this.view.getModelo().eliminarFactura(filaPulsada);
        			myFacturaDao.EliminarTemp(idFactura);
        		}else{
        			this.view.getModelo().cambiarArticulo(filaPulsada, myFactura);
        			this.view.getTablaFacturas().getSelectionModel().setSelectionInterval(filaPulsada,filaPulsada);//se seleciona lo cambiado
        		}
        		viewFacturar.dispose();
        		ctlFacturar=null;*/
        		
	        	
			
				
				
				
	        }//fin del if del doble click
        	else{//si solo seleccion la fila se guarda el id de proveedor para accion de eliminar
        		
        		this.view.getBtnEliminar().setEnabled(true);
        		
        		
        	}
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		
		switch (comando){
		case "FECHA":
			this.view.getTxtBuscar2().setEditable(true);
			//JOptionPane.showMessageDialog(view, "Clip en fecha");
			break;
			
		case "TODAS":
			this.view.getTxtBuscar2().setEditable(false);
			this.view.getTxtBuscar2().setText("");
			this.view.getTxtBuscar1().setText("");
			
			break;
		case "ID":
			this.view.getTxtBuscar2().setEditable(false);
			this.view.getTxtBuscar2().setText("");
			this.view.getTxtBuscar1().setText("");
			
			break;
		case "BUSCAR":
			
			//si la busqueda es por id
			if(this.view.getRdbtnId().isSelected()){
				myRecibo=reciboDao.reciboPorNo(Integer.parseInt(this.view.getTxtBuscar1().getText()));
				if(myRecibo!=null){												
					this.view.getModelo().limpiar();
					this.view.getModelo().agregarPago(myRecibo); 
				}else{
					JOptionPane.showMessageDialog(view, "No se encuentro la factura");
				}
				
			}
			//si la busqueda es por fecha
			if(this.view.getRdbtnFecha().isSelected()){  
				String fecha1=this.view.getTxtBuscar1().getText();
				String fecha2=this.view.getTxtBuscar2().getText();
				cargarTabla(reciboDao.reciboPorFecha(fecha1,fecha2));
				//this.view.getTxtBuscar1().setText("");
				//this.view.getTxtBuscar2().setText("");
				}
			
			
			//si la busqueda son tadas
			if(this.view.getRdbtnTodos().isSelected()){  
				cargarTabla(reciboDao.todosRecibo());
				this.view.getTxtBuscar1().setText("");
				}
			break;
		/*case "ANULARFACTURA":
			
			//se verifica si la factura ya esta agregada al kardex
			if (myFactura.getAgregadoAkardex()==0){
				
					int resul=JOptionPane.showConfirmDialog(view, "�Desea anular la factura no "+myFactura.getIdFactura()+"?");
					//sin confirmo la anulacion
					if(resul==0){
						JPasswordField pf = new JPasswordField();
						int action = JOptionPane.showConfirmDialog(view, pf,"Escriba la contrase�a admin",JOptionPane.OK_CANCEL_OPTION);
						//String pwd=JOptionPane.showInputDialog(view, "Escriba la contrase�a admin", "Seguridad", JOptionPane.INFORMATION_MESSAGE);
						if(action < 0){
							
							
						}else{
							String pwd=new String(pf.getPassword());
							//comprabacion del permiso administrativo
							if(this.myUsuarioDao.comprobarAdmin(pwd)){
								//se anula la factura en la bd
								if(myFacturaDao.anularFactura(myFactura))
									myFactura.setEstado("NULA");
								//JOptionPane.showMessageDialog(view, "Usuario Valido");
							}else{
								JOptionPane.showMessageDialog(view, "Usuario Invalido");
							}
						}
						
					}
			}else{
				JOptionPane.showMessageDialog(view, "No se puede anular la compra porque ya esta en el Kardex!!!");
				this.view.getBtnEliminar().setEnabled(false);
			}
			break;
			
		case "IMPRIMIR":
			try {
				//this.view.setVisible(false);
				//this.view.dispose();
				AbstractJasperReports.createReportFactura( conexion.getPoolConexion().getConnection(), "Factura_Saint_Paul_Reimpresion.jasper",myFactura.getIdFactura() );
				//AbstractJasperReports.showViewer();
				AbstractJasperReports.showViewer(view);
				this.view.getBtnImprimir().setEnabled(false);
				myFactura=null;
			} catch (SQLException ee) {
				// TODO Auto-generated catch block
				ee.printStackTrace();
			}
			break;*/
		}

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		view.setVisible(false);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
