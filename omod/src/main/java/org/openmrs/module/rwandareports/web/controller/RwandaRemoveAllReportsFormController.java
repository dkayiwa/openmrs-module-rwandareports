package org.openmrs.module.rwandareports.web.controller;

import org.openmrs.module.rwandareports.util.CleanReportingTablesAndRegisterAllReports;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RwandaRemoveAllReportsFormController {
	
	@RequestMapping("/module/rwandareports/remove_all")
	public ModelAndView removeAllReports() throws Exception{
		CleanReportingTablesAndRegisterAllReports.cleanTables();		
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}
}
