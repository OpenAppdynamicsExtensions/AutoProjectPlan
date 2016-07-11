package com.appdynamics.jrbronet.projectplan;

import java.util.ArrayList;

// Program arguments: <controller_URL> <account>@<user> <password> <input_XLS> <output_XLS>
// Program arguments example: https://aviva1.saas.appdynamics.com josebronet@aviva1 myPassword "/Users/joseramonbronetcampos/Google Drive/Trabajo/AppDynamics/Customers/Aviva/AvivaProjectPlan.xlsx" /Users/joseramonbronetcampos/Desktop/AvivaProjectPlan.new.xlsx
// Program arguments example: http://appdynamics.local:8090 jrbronet@customer1 Password01! "/Users/joseramonbronetcampos/Google Drive/Trabajo/AppDynamics/Customers/Aviva/AvivaProjectPlan.xlsx" /Users/joseramonbronetcampos/Desktop/AvivaProjectPlan.new.xlsx

public class AutoPPMain {

	public static void main(String[] args) {
		
		if(args.length == 0){
			System.out.println("Usage: <controller_URL> <account>@<user> <password> <input_XLS> <output_XLS>");
			System.out.println("Example: https://aviva1.saas.appdynamics.com josebronet@customer1 myPassword DeploymentStatus.xlsx DeploymentStatus.new.xlsx");
			System.exit(0);
		}
		
		AppDynamicsControllerRESTClient a = new AppDynamicsControllerRESTClient();
		
		// Aviva
		// With my account
		a.buildAppList(args[0],args[1],args[2]);
		//a.buildAppList("http://appdynamics.local:8090","jrbronet@customer1","Password01!");
		
		//BSkyB
		//Location:https://OTTCLOUDPLATFORM.saas.appdynamics.com:443/controller/#action=LOGIN&ert=vqgjc0%7Bwkucowjplu&bgtr=OUQIVDNSFXQOEWVR&yui=h%3Ch83i49%3Bdf%3A
		//a.buildAppList("https://OTTCLOUDPLATFORM.saas.appdynamics.com:443/controller/#action=LOGIN&ert=vqgjc0%7Bwkucowjplu&bgtr=OUQIVDNSFXQOEWVR&yui=h%3Ch83i49%3Bdf%3A","","");	
		
		
		//a.getAppNodes("https://aviva1.saas.appdynamics.com","josebronet@aviva1","cbf250","GI-ECOMM-SYS4");
		//a.getOnlineStatus("https://aviva1.saas.appdynamics.com","josebronet@aviva1","cbf250","LIFE-ECOMM-SYS","UKLAP_SYS_WP","UKLAP_SYS_WP_POR_C1_S1");
		a.calculateLicenses();
		a.printLicenses();
				
		ExcelManager em = new ExcelManager(args[3]);
		ArrayList appList = a.getAppList();
		
		for(int i = 0; i < appList.size(); i++){
			ADApplication app = (ADApplication)appList.get(i);		
			em.AddUpdateAppLicenseData(app.getName(), app.getLicenseCount());
		}
		
		em.saveChangesAndClose(args[4]);
		
	}

	

}
