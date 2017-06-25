package com.EyVdeSW.TP.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.EyVdeSW.TP.Daos.CampañaDAO;
import com.EyVdeSW.TP.Daos.impl.CampañaDAONeodatis;
import com.EyVdeSW.TP.domainModel.AccionPublicitaria;
import com.EyVdeSW.TP.domainModel.Campania;

public class MailScheduler {
	private static MailScheduler mailScheduler;
	private CampañaDAO campañaDAO;
	private Scheduler sc;
	
	private MailScheduler(){
		campañaDAO= new CampañaDAONeodatis();
		try {
			sc=StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static MailScheduler getMailScheduler(){
		if(mailScheduler==null){
			mailScheduler= new MailScheduler();
		}
		return mailScheduler;
	}
	
	public void encender(){
		try {
			sc.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void apagar(){
		try {
			sc.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void agregarAccion(String fechaInicio, String fechaFin, String destinatario, String encabezado, String mensaje, String hora,
			String minuto, String perioricidad){
	
		try {
			String horan="0 "+minuto+" "+hora;
			//horan=horan+" 1/1 * ? *";
			horan=horan+" 1/"+perioricidad;
			horan=horan+" * ? *";
			JobDetail j1=JobBuilder.newJob(MailSender.class)
					.usingJobData("destinatario",destinatario)
					.usingJobData("encabezado",encabezado)
					.usingJobData("mensaje", mensaje)
					.build();
			
			Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(fechaInicio);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(fechaFin);
			
			String nombreUnico=UUID.randomUUID().toString();
			
			Trigger t = TriggerBuilder.newTrigger().withIdentity(nombreUnico)
					.startAt(startDate)
					.withSchedule(CronScheduleBuilder.cronSchedule(horan))
					.endAt(endDate)
					.build();
			sc.scheduleJob(j1, t);
		} catch (SchedulerException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void agregarAccionesDeCampañas(List<Campania>campañas){
			if(campañas.size() != 0 && campañas != null){
				for(Campania c:campañas){
						agregarAccionesDeCampaña(c);
					}
				}
	}

	
	public void agregarAccionesDeCampaña(Campania campaña){
		if(campaña.getAccionesPublicitarias()!=null && campaña.getAccionesPublicitarias().size()!=0){
			for(AccionPublicitaria ac:campaña.getAccionesPublicitarias()){
				String fechaIncio=transformarFecha(campaña.getFechaDeInicio());
				String fechaFin=transformarFecha(campaña.getFechaDeFin());
				agregarAccion(fechaIncio, fechaFin, ac.getDestinatario(), ac.getTitulo(),
						ac.getTexto(), ac.getHoraInicio(), ac.getMinutoInicio(), Integer.toString(ac.getPeriodicidad()));
			}
		}
	}
	
	private static String transformarFecha(Date fecha){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String ret = formatter.format(fecha);
		ret= ret+" 00:00:00.0";
		return ret;
	}

	//TODO delete
	public static void main(String[] args) {
//		MailScheduler ms=MailScheduler.getMailScheduler();
//		ms.encender();
//		String startDateStr = "2017-06-23 00:00:00.0";
//        String endDateStr = "2017-06-24 00:00:00.0";
//        
//		try {
//			ms.agregarAccion(startDateStr, endDateStr, "deidelson@mail.com", 
//						"Prueba del service", "exito", "14","43","1");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		
		
		
		
	}
}
