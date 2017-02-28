package org.openmrs.module.rwandareports.web.controller;

import org.openmrs.module.rwandareports.util.CleanReportingTablesAndRegisterAllReports;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RwandaRegisterAllReportsFormController {
	@RequestMapping("/module/rwandareports/register_allReports")
	public ModelAndView registerAllReports() throws Exception{
		CleanReportingTablesAndRegisterAllReports.registerHIVReports();
		CleanReportingTablesAndRegisterAllReports.registerCentralReports();
		CleanReportingTablesAndRegisterAllReports.registerSiteReports();
		CleanReportingTablesAndRegisterAllReports.registerCHWReports();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}
	@RequestMapping("/module/rwandareports/register_allHIVReports")
	public ModelAndView registerAllHIVReports() throws Exception{
		CleanReportingTablesAndRegisterAllReports.registerHIVReports();	
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}
	
	@RequestMapping("/module/rwandareports/register_allCentralReports")
	public ModelAndView registerAllCentralReports() throws Exception{
		CleanReportingTablesAndRegisterAllReports.registerCentralReports();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}
	
	@RequestMapping("/module/rwandareports/register_allSiteReports")
	public ModelAndView registerAllSiteReports() throws Exception{
		CleanReportingTablesAndRegisterAllReports.registerSiteReports();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}
	
	@RequestMapping("/module/rwandareports/register_allCHWReport")
	public ModelAndView registerAllCHWReports() throws Exception{
		CleanReportingTablesAndRegisterAllReports.registerCHWReports();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

}
