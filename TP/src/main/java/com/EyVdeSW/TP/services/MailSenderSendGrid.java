package com.EyVdeSW.TP.services;

import java.io.IOException;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.EyVdeSW.TP.domainModel.AccionPublicitaria;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import properties.Parametros;

public class MailSenderSendGrid implements MessageSender {

	@Override
	public void enviarMensaje(String destinatario, String encabezado, String mensaje) {		
		Email from = new Email("noResponder@Megafono.com");
	    String subject = encabezado;
	    Email to = new Email(destinatario);
	    Content content = new Content("text/plain", mensaje);
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid(Parametros.getProperty("sendgrid.api.key"));
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
	      System.out.println(response.getBody());
	      System.out.println(response.getHeaders());
	      System.out.println("Exito..");
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }		
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		 JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		 if(dataMap.size() != 0){
			 String destinatario = dataMap.getString("destinatario");
			 String encabezado = dataMap.getString("encabezado");
			 String mensaje = dataMap.getString("mensaje");
			 
			 enviarMensaje(destinatario, encabezado, mensaje);
		 }else{
			 System.out.println("algo salio mal");
		 }
		
	}

	@Override
	public void enviarMensaje(AccionPublicitaria accion) {
		enviarMensaje(accion.getDestinatario(), accion.getTitulo(), accion.getTexto());
		
	}
		
}