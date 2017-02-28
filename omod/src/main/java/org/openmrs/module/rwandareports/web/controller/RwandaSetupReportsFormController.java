package org.openmrs.module.rwandareports.web.controller;

import org.openmrs.module.rwandareports.reporting.SetupAdultHIVConsultationSheet;
import org.openmrs.module.rwandareports.reporting.SetupAdultLateVisitAndCD4Report;
import org.openmrs.module.rwandareports.reporting.SetupAsthmaQuarterlyAndMonthReport;
import org.openmrs.module.rwandareports.reporting.SetupCROWNReports;
import org.openmrs.module.rwandareports.reporting.SetupCombinedHFCSPConsultationReport;
import org.openmrs.module.rwandareports.reporting.SetupDataEntryDelayReport;
import org.openmrs.module.rwandareports.reporting.SetupDataQualityIndicatorReport;
import org.openmrs.module.rwandareports.reporting.SetupEligibleForViralLoadReport;
import org.openmrs.module.rwandareports.reporting.SetupExposedClinicInfantMonthly;
import org.openmrs.module.rwandareports.reporting.SetupGenericDrugReport;
import org.openmrs.module.rwandareports.reporting.SetupGenericEncounterReport;
import org.openmrs.module.rwandareports.reporting.SetupGenericPatientByProgramReport;
import org.openmrs.module.rwandareports.reporting.SetupHMISRwandaReportBySite;
import org.openmrs.module.rwandareports.reporting.SetupIDProgramQuarterlyIndicatorReport;
import org.openmrs.module.rwandareports.reporting.SetupPBFReport;
import org.openmrs.module.rwandareports.reporting.SetupPMTCTCombinedClinicMotherMonthlyReport;
import org.openmrs.module.rwandareports.reporting.SetupPMTCTPregnancyConsultationReport;
import org.openmrs.module.rwandareports.reporting.SetupPMTCTPregnancyMonthlyReport;
import org.openmrs.module.rwandareports.reporting.SetupPediHIVConsultationSheet;
import org.openmrs.module.rwandareports.reporting.SetupPediatricLateVisitAndCD4Report;
import org.openmrs.module.rwandareports.reporting.SetupPrimaryCareRegistrationReport;
import org.openmrs.module.rwandareports.reporting.SetupQuarterlyCrossSiteIndicatorByDistrictReport;
import org.openmrs.module.rwandareports.reporting.SetupQuarterlyViralLoadReport;
import org.openmrs.module.rwandareports.reporting.SetupTBConsultationSheet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RwandaSetupReportsFormController {

	@RequestMapping("/module/rwandareports/remove_quarterlyCrossDistrictIndicator")
	public ModelAndView removeQuarterlyCrossDistrictIndicator()
			throws Exception {
		new SetupQuarterlyCrossSiteIndicatorByDistrictReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_quarterlyCrossDistrictIndicator")
	public ModelAndView registerQuarterlyCrossDistrictIndicator()
			throws Exception {
		new SetupQuarterlyCrossSiteIndicatorByDistrictReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_adulthivartregister")
	public ModelAndView registerAdultHivArtRegiser() throws Exception {
		// new SetupHivArtRegisterReport(false).setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_adulthivartregister")
	public ModelAndView removeAdultHivArtRegister() throws Exception {
		// new SetupHivArtRegisterReport(false).delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_pedihivartregister")
	public ModelAndView registerPediHivArtRegiser() throws Exception {
		// new SetupHivArtRegisterReport(true).setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_pedihivartregister")
	public ModelAndView removePediHivArtRegister() throws Exception {
		// new SetupHivArtRegisterReport(true).delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_combinedHSCSPConsultation")
	public ModelAndView registerCombinedHSCSPConsultation() throws Exception {
		new SetupCombinedHFCSPConsultationReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_combinedHSCSPConsultation")
	public ModelAndView removeCombinedHSCSPConsultation() throws Exception {
		new SetupCombinedHFCSPConsultationReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_EncounterAndObsReport")
	public ModelAndView registerEncounterAndObsReport() throws Exception {
		new SetupGenericEncounterReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_EncounterAndObsReport")
	public ModelAndView removeEncounterAndObsReport() throws Exception {
		new SetupGenericEncounterReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	/*
	 * @RequestMapping("/module/rwandareports/register_pmtctFoodDistributionSheet"
	 * ) public ModelAndView registerPmtctFoodDistribution() throws Exception {
	 * new SetupPMTCTFoodDistributionReport().setup(); return new
	 * ModelAndView(new RedirectView("rwandareports.form")); }
	 * 
	 * @RequestMapping("/module/rwandareports/remove_pmtctFoodDistributionSheet")
	 * public ModelAndView removePmtctFoodDistribution() throws Exception { new
	 * SetupPMTCTFoodDistributionReport().delete(); return new ModelAndView(new
	 * RedirectView("rwandareports.form")); }
	 * 
	 * @RequestMapping(
	 * "/module/rwandareports/register_pmtctFormulaDistributionSheet") public
	 * ModelAndView registerPmtctFormulaDistribution() throws Exception { new
	 * SetupPMTCTFormulaDistributionReport().setup(); return new
	 * ModelAndView(new RedirectView("rwandareports.form")); }
	 * 
	 * @RequestMapping("/module/rwandareports/remove_pmtctFormulaDistributionSheet"
	 * ) public ModelAndView removePmtctFormulaDistribution() throws Exception {
	 * new SetupPMTCTFormulaDistributionReport().delete(); return new
	 * ModelAndView(new RedirectView("rwandareports.form")); }
	 */
	@RequestMapping("/module/rwandareports/register_pmtctPregnancyConsultationSheet")
	public ModelAndView registerPmtctPregnancyConsultation() throws Exception {
		new SetupPMTCTPregnancyConsultationReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_pmtctPregnancyConsultationSheet")
	public ModelAndView removePmtctPregnanacyConsultation() throws Exception {
		new SetupPMTCTPregnancyConsultationReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	/*
	 * @RequestMapping("/module/rwandareports/register_pmtctFormCompletionSheet")
	 * public ModelAndView registerPmtctFormCompletionSheet() throws Exception {
	 * new SetupPMTCTFormCompletionSheet().setup(); return new ModelAndView(new
	 * RedirectView("rwandareports.form")); }
	 * 
	 * @RequestMapping("/module/rwandareports/remove_pmtctFormCompletionSheet")
	 * public ModelAndView removePmtctFormCompletionSheet() throws Exception {
	 * new SetupPMTCTFormCompletionSheet().delete(); return new ModelAndView(new
	 * RedirectView("rwandareports.form")); }
	 */

	// Consult sheets
	@RequestMapping("/module/rwandareports/register_pediHIVConsultationSheet")
	public ModelAndView registerPediHIVConsultationSheet() throws Exception {
		new SetupPediHIVConsultationSheet().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_pediHIVConsultationSheet")
	public ModelAndView removePediHIVConsultationSheet() throws Exception {
		new SetupPediHIVConsultationSheet().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_adultHIVConsultationSheet")
	public ModelAndView registerAdultHIVConsultationSheet() throws Exception {
		new SetupAdultHIVConsultationSheet().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_adultHIVConsultationSheet")
	public ModelAndView removeAdultHIVConsultationSheet() throws Exception {
		new SetupAdultHIVConsultationSheet().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}
	
	@RequestMapping("/module/rwandareports/register_CROWNReports")
	public ModelAndView registerCROWNReports() throws Exception {
		new SetupCROWNReports().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_CROWNReports")
	public ModelAndView removeCROWNReports() throws Exception {
		new SetupCROWNReports().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_tbConsultationSheet")
	public ModelAndView registerTbConsultationSheet() throws Exception {
		new SetupTBConsultationSheet().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_tbConsultationSheet")
	public ModelAndView removeTbConsultationSheet() throws Exception {
		new SetupTBConsultationSheet().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Remove/Register Adult Late visit And CD4

	@RequestMapping("/module/rwandareports/register_adultLatevisitAndCD4")
	public ModelAndView registerAdultLatevisitAndCD4() throws Exception {
		new SetupAdultLateVisitAndCD4Report().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_adultLatevisitAndCD4")
	public ModelAndView removeAdultLatevisitAndCD4() throws Exception {
		new SetupAdultLateVisitAndCD4Report().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Remove/Register Pediatric Late visit And CD4
	@RequestMapping("/module/rwandareports/register_pediatricLatevisitAndCD4")
	public ModelAndView registerPediatricLatevisitAndCD4() throws Exception {
		new SetupPediatricLateVisitAndCD4Report().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_pediatricLatevisitAndCD4")
	public ModelAndView removePediatricLatevisitAndCD4() throws Exception {
		new SetupPediatricLateVisitAndCD4Report().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_dataQualityReport")
	public ModelAndView registerDataQualityReport() throws Exception {
		new SetupDataQualityIndicatorReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_dataQualityReport")
	public ModelAndView removeDataQualityReport() throws Exception {
		new SetupDataQualityIndicatorReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_viralLoad")
	public ModelAndView registerViralLoadReport() throws Exception {
		new SetupQuarterlyViralLoadReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_viralLoad")
	public ModelAndView removeViralLoadReport() throws Exception {
		new SetupQuarterlyViralLoadReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Remove/Register HMIS report
	@RequestMapping("/module/rwandareports/remove_hmisReport")
	public ModelAndView removeHMISIndicator() throws Exception {
		new SetupHMISRwandaReportBySite().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_hmisReport")
	public ModelAndView registerHMISIndicator() throws Exception {
		new SetupHMISRwandaReportBySite().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Remove/Register PMTCT Combined Clinic Mother Monthly Report report

	@RequestMapping("/module/rwandareports/register_pmtctCombinedClinicMotherMonthlyReport")
	public ModelAndView registerPMTCTCombinedClinicMotherMonthly()
			throws Exception {
		new SetupPMTCTCombinedClinicMotherMonthlyReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_pmtctCombinedClinicMotherMonthlyReport")
	public ModelAndView removePMTCTCombinedClinicMotherMonthly()
			throws Exception {
		new SetupPMTCTCombinedClinicMotherMonthlyReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Remove/Register Asthma Quarterly And Monthly Report

	@RequestMapping("/module/rwandareports/register_asthmaQuarterlyAndMonthReport")
	public ModelAndView registerAsthmaQuarterlyAndMonthReport()
			throws Exception {
		new SetupAsthmaQuarterlyAndMonthReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_asthmaQuarterlyAndMonthReport")
	public ModelAndView removeAsthmaQuarterlyAndMonthReport() throws Exception {
		new SetupAsthmaQuarterlyAndMonthReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Remove/Register PMTCT Pregnancy Monthly Report

	@RequestMapping("/module/rwandareports/register_pmtctPregMonthlyReport")
	public ModelAndView registerPMTCTPregMonthlyVisit() throws Exception {
		new SetupPMTCTPregnancyMonthlyReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_pmtctPregMonthlyReport")
	public ModelAndView removePMTCTPregMonthlyVisit() throws Exception {
		new SetupPMTCTPregnancyMonthlyReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Remove/Register Combined Infant Monthly Report

	@RequestMapping("/module/rwandareports/register_pmtctCombinedClinicInfantReport")
	public ModelAndView registerPMTCTCombinedInfantReport() throws Exception {
		new SetupExposedClinicInfantMonthly().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_pmtctCombinedClinicInfantReport")
	public ModelAndView removePMTCTCombinedInfantReport() throws Exception {
		new SetupExposedClinicInfantMonthly().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Data Delay
	@RequestMapping("/module/rwandareports/register_dataDelay")
	public ModelAndView registerDataDelay() throws Exception {
		new SetupDataEntryDelayReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_dataDelay")
	public ModelAndView removeDataDelay() throws Exception {
		new SetupDataEntryDelayReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}


	// ID Program Quarterly Indicators
	@RequestMapping("/module/rwandareports/register_IDProgramQuarterlyIndicators")
	public ModelAndView registerIDProgramQuarterlyIndicators() throws Exception {
		new SetupIDProgramQuarterlyIndicatorReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_IDProgramQuarterlyIndicators")
	public ModelAndView removeIDProgramQuarterlyIndicators() throws Exception {
		new SetupIDProgramQuarterlyIndicatorReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// PrimaryCare Data
	@RequestMapping("/module/rwandareports/register_PrimaryCareRegistrationData")
	public ModelAndView registerPrimaryCareRegistrationData() throws Exception {
		new SetupPrimaryCareRegistrationReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_PrimaryCareRegistrationData")
	public ModelAndView removePrimaryCareRegistrationData() throws Exception {
		new SetupPrimaryCareRegistrationReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}





	// Remove/Register Eligible for viral load Reports
	@RequestMapping("/module/rwandareports/register_eligibleForViralLoadReport")
	public ModelAndView registerEligibleForViralLoadReport() throws Exception {
		new SetupEligibleForViralLoadReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_eligibleForViralLoadReport")
	public ModelAndView removeEligibleForViralLoadReport() throws Exception {
		new SetupEligibleForViralLoadReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	// Remove/Register PBF report
	@RequestMapping("/module/rwandareports/remove_PBFReport")
	public ModelAndView removePBFIndicator() throws Exception {
		new SetupPBFReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_PBFReport")
	public ModelAndView registerPBFIndicator() throws Exception {
		new SetupPBFReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/register_PatientByProgramReport.form")
	public ModelAndView registerGenericPatientsByProgramReport()
			throws Exception {
		new SetupGenericPatientByProgramReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_PatientByProgramReport.form")
	public ModelAndView removeGenericPatientsByProgramReport() throws Exception {
		new SetupGenericPatientByProgramReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}
	@RequestMapping("/module/rwandareports/register_DrugReport")
	public ModelAndView registerDrugReport() throws Exception {
		new SetupGenericDrugReport().setup();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}

	@RequestMapping("/module/rwandareports/remove_DrugReport")
	public ModelAndView removeDrugReport() throws Exception {
		new SetupGenericDrugReport().delete();
		return new ModelAndView(new RedirectView("rwandareports.form"));
	}
}
