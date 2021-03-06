package com.EyVdeSW.TP.presentacion;

import com.EyVdeSW.TP.domainModel.Usuario;
import com.EyVdeSW.TP.domainModel.Usuario.TipoUsuario;
import com.EyVdeSW.TP.services.UsuarioService;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;


public class PantallaRegistro extends VerticalLayout implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final String NAME = "pantallaRegistro";
	
	private UsuarioService usuarioService = UsuarioService.getUsuarioService();
	
	private final TextField tfNombreUsuario;
	private final TextField tfNombreReal;
	private final TextField tfMail;

	private final PasswordField password;
	private final PasswordField verificacionPassword;

	private final Button logout; 
	private final Button registroButton;

	
	public PantallaRegistro(){
				
		//TituloPrincipal
		Label titulo = new Label("Registro en el Sistema");
		titulo.setStyleName(ValoTheme.LABEL_H1);
		HorizontalLayout hlTitulo = new HorizontalLayout(titulo);
		addComponent(hlTitulo);
		setComponentAlignment(hlTitulo, Alignment.MIDDLE_CENTER);

		//NombreUsuario
		tfNombreUsuario = new TextField("Nombre Usuario");
		tfNombreUsuario.setWidth("300px");
		tfNombreUsuario.setRequired(true);
		
		//NombreReal
		tfNombreReal = new TextField("Nombre Real");
		tfNombreReal.setWidth("300px");
		tfNombreReal.setRequired(true);
		
		//Mail
		tfMail = new TextField("Mail");
		tfMail.setWidth("300px");
		tfMail.setRequired(true);
		tfMail.setInputPrompt("Ejemplo: joe@email.com");
		tfMail.addValidator(new EmailValidator(
	            "La dirección de mail es errónea"));
		tfMail.setInvalidAllowed(false);
	    
		//Password
		password = new PasswordField("Password");
		password.setWidth("300px");
	    password.addValidator(new PasswordValidator());
	    password.setRequired(true);
	    password.setValue("");
	    password.setNullRepresentation("");
	    
	    //VerificacionPassword
		verificacionPassword = new PasswordField("Repita la Password");
		verificacionPassword.setWidth("300px");
		verificacionPassword.addValidator(new PasswordValidator());
		verificacionPassword.setRequired(true);
		verificacionPassword.setValue("");
		verificacionPassword.setNullRepresentation("");
		
		//Botones
		registroButton = new Button("Registrarse");
		logout	= new Button("Volver a Login");
			
		 
		 
		FormLayout fields = new FormLayout(tfNombreUsuario, tfNombreReal, tfMail, password, verificacionPassword, registroButton);
		    fields.setCaption("Por favor registrese para acceder a la aplicación");
		    fields.setSpacing(true);
		    
		HorizontalLayout botones = new HorizontalLayout(registroButton, logout);
		 	botones.setSpacing(true);
		    
		VerticalLayout viewLayout = new VerticalLayout(fields,botones);
		 	viewLayout.setSpacing(true);
		    viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
			
		HorizontalLayout hlPrincipal = new HorizontalLayout(viewLayout);
			hlPrincipal.setSpacing(true);
			hlPrincipal.setWidth("80%");
			addComponent(hlPrincipal);
			setComponentAlignment(hlPrincipal, Alignment.TOP_CENTER);
			setMargin(true);
			
		logout.addClickListener(e -> {
			getUI().getNavigator().navigateTo(PantallaLogin.NAME);
		
		});
		
		registroButton.addClickListener(e -> {
			
			if(tfNombreUsuario.getValue() == "" || tfNombreReal.getValue() == ""
			|| tfMail.getValue() == "" || password.getValue() == "") {
				Notification.show("Uno de los campos está vacío!", Type.WARNING_MESSAGE);
			}
			
			else if(usuarioService.existeUsuarioPorNombreUsuario(tfNombreUsuario.getValue())){
				Notification.show("Ya existe ese nombre de usuario!", Type.WARNING_MESSAGE);
			}
		
			else if(usuarioService.existeUsuarioPorMail(tfMail.getValue())){
				Notification.show("Ya existe un usuario asociado a ese mail!", Type.WARNING_MESSAGE);
			}
			
			else if (!password.getValue().equals(verificacionPassword.getValue())){
				Notification.show("Las contraseñas no coinciden!", Type.WARNING_MESSAGE);
				
			}

			else if (password.isValid()==false || verificacionPassword.isValid()==false){
				Notification.show("Las contraseñas deben tener 8 caracteres y 1 numero como minimo!", Type.WARNING_MESSAGE);
				
			}
				else{
				String nombreUsuario = tfNombreUsuario.getValue();
				String nombreReal = tfNombreReal.getValue();
				String mail = tfMail.getValue();
				String Password = password.getValue();
				
				Usuario usuario = new Usuario(nombreUsuario,nombreReal,mail,Password,TipoUsuario.CLIENTE);
				usuarioService.guardar(usuario);
				Notification.show("Usuario Guardado", Type.TRAY_NOTIFICATION);
				limpiarCampos(tfNombreUsuario, tfNombreReal, tfMail, password, verificacionPassword);
				getUI().getNavigator().navigateTo(PantallaLogin.NAME);
				}
			
		});
		    
	}

	private void limpiarCampos(TextField tfNombreUsuario, TextField tfNombreReal, TextField tfMail,
			PasswordField password, PasswordField verificacionPassword) {
		
		tfNombreUsuario.clear();
		tfNombreReal.clear();
		tfMail.clear();
		password.clear();
		verificacionPassword.clear();	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		limpiarCampos(tfNombreUsuario, tfNombreReal, tfMail, password, verificacionPassword);
		tfNombreUsuario.focus();
		
				
	}
	
	private static final class PasswordValidator extends
    AbstractValidator<String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public PasswordValidator() {
			super("La contraseña ingresada no es valida, debe poseer 8 caracteres y al menos un numero");
		}

	@Override
	protected boolean isValidValue(String value) {
    //
    // La contraseña debe tener 8 digitos como minimo y al menos un numero
    //
    if (value != null
            && (value.length() < 8 || !value.matches(".*\\d.*"))) {
    	   	Notification.show("La contraseña debe posee 8 caracteres y 1 numero como minimo!", Type.WARNING_MESSAGE);
        return false;
    	}
    return true;
	}

	@Override
	public Class<String> getType() {
		return String.class;
		}
	}

}
