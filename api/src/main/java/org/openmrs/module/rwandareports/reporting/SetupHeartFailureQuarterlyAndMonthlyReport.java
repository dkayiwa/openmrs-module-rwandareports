/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.rwandareports.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Program;
import org.openmrs.api.PatientSetService.TimeModifier;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.NumericObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.ProgramEnrollmentCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
import org.openmrs.module.reporting.query.encounter.definition.SqlEncounterQuery;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.module.rwandareports.dataset.EncounterIndicatorDataSetDefinition;
import org.openmrs.module.rwandareports.dataset.LocationHierachyIndicatorDataSetDefinition;
import org.openmrs.module.rwandareports.indicator.EncounterIndicator;
import org.openmrs.module.rwandareports.util.Cohorts;
import org.openmrs.module.rwandareports.util.GlobalPropertiesManagement;
import org.openmrs.module.rwandareports.util.Indicators;
import org.openmrs.module.rwandareports.widget.AllLocation;
import org.openmrs.module.rwandareports.widget.LocationHierarchy;

/**
 *
 */
public class SetupHeartFailureQuarterlyAndMonthlyReport {
	
	public Helper h = new Helper();
	
	GlobalPropertiesManagement gp = new GlobalPropertiesManagement();
	
	// properties
	private Program heartFailureProgram;
	
	private List<Program> HFPrograms = new ArrayList<Program>();
	
	private EncounterType heartFailureEncounterType;
	
	private List<String> onOrAfterOnOrBefore = new ArrayList<String>();
	
	private Form heartFailureDDBform;
	
	private Form postoperatoireCardiaqueRDV;
	
	private Form insuffisanceCardiaqueRDV;
	
	private List<Form> postoperatoireAndinsuffisanceRDV = new ArrayList<Form>();
	
    private Concept NYHACLASS;
    
    private Concept NYHACLASS4;
	
	private Concept serumCreatinine;	
	
	private Concept systolicBP;
	
	private Concept carvedilol;  
	
	private Concept atenolol; 
	
    private Concept lisinopril;
	
	private Concept captopril;
	
	private Concept furosemide;
	
	private Concept aldactone;
	
	private List<Concept> carvedilolAndAtenolol = new ArrayList<Concept>();
	
	private List<Concept> lisinoprilAndCaptopril = new ArrayList<Concept>();
	
	public void setup() throws Exception {
		
		setUpProperties();
		
		// Monthly report set-up
		ReportDefinition monthlyRd = new ReportDefinition();
		monthlyRd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		monthlyRd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		Properties properties = new Properties();
		properties.setProperty("hierarchyFields", "countyDistrict:District");
		monthlyRd.addParameter(new Parameter("location", "Location", AllLocation.class, properties));
		
		monthlyRd.setName("NCD-Heart Failure Indicator Report-Monthly");
		
		monthlyRd.addDataSetDefinition(createMonthlyLocationDataSet(),
		    ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate},location=${location}"));
		
		ProgramEnrollmentCohortDefinition patientEnrolledInHF = new ProgramEnrollmentCohortDefinition();
		patientEnrolledInHF.addParameter(new Parameter("enrolledOnOrBefore", "enrolledOnOrBefore", Date.class));
		patientEnrolledInHF.setPrograms(HFPrograms);
		
		monthlyRd.setBaseCohortDefinition(patientEnrolledInHF,
		    ParameterizableUtil.createParameterMappings("enrolledOnOrBefore=${endDate}"));
		
		h.saveReportDefinition(monthlyRd);
		
		ReportDesign monthlyDesign = h.createRowPerPatientXlsOverviewReportDesign(monthlyRd,
		    "HF_Monthly_Indicator_Report.xls", "Heart Failure Indicator Monthly Report (Excel)", null);
		Properties monthlyProps = new Properties();
		monthlyProps.put("repeatingSections", "sheet:1,dataset:Encounter Monthly Data Set");
		
		monthlyDesign.setProperties(monthlyProps);
		h.saveReportDesign(monthlyDesign);
		
	}
	
	public void delete() {
		ReportService rs = Context.getService(ReportService.class);
		for (ReportDesign rd : rs.getAllReportDesigns(false)) {
			if ("Heart Failure Indicator Monthly Report (Excel)".equals(rd.getName())) {
				rs.purgeReportDesign(rd);
			}
		}
		h.purgeReportDefinition("NCD-Heart Failure Indicator Report-Monthly");
		
	}
	
	private void createMonthlyIndicators(EncounterIndicatorDataSetDefinition dsd) {
		
		SqlEncounterQuery patientVisitsToHFClinic = new SqlEncounterQuery();
		
		patientVisitsToHFClinic.setQuery("select distinct e.encounter_id from encounter e where e.encounter_type="
	        + heartFailureEncounterType.getEncounterTypeId() +" and e.encounter_datetime>= :startDate and e.encounter_datetime<= :endDate and e.voided=0");
		patientVisitsToHFClinic.setName("patientVisitsToHFClinic");
		patientVisitsToHFClinic.addParameter(new Parameter("startDate", "startDate", Date.class));
		patientVisitsToHFClinic.addParameter(new Parameter("endDate", "endDate", Date.class));
		
		EncounterIndicator patientVisitsToHFClinicMonthIndicator = new EncounterIndicator();
		patientVisitsToHFClinicMonthIndicator.setName("patientVisitsToHFClinicMonthIndicator");
		patientVisitsToHFClinicMonthIndicator.setEncounterQuery(new Mapped<EncounterQuery>(patientVisitsToHFClinic,
		        ParameterizableUtil.createParameterMappings("endDate=${endDate},startDate=${endDate-1m+1d}")));
		
		dsd.addColumn(patientVisitsToHFClinicMonthIndicator);
		
		//==============================================================
		// C2: % of Patient visits in the last quarter with documented BP
		//==============================================================
		SqlEncounterQuery patientVisitsWithDocumentedBP = new SqlEncounterQuery();
		
		patientVisitsWithDocumentedBP
		.setQuery("select e.encounter_id from encounter e,obs o where o.encounter_id=e.encounter_id and o.concept_id="+systolicBP.getConceptId()+" and e.encounter_datetime>= :startDate and e.encounter_datetime<= :endDate and e.voided=0 group by e.encounter_datetime, e.patient_id");
		patientVisitsWithDocumentedBP.setName("patientVisitsToHypertensionClinic");
		patientVisitsWithDocumentedBP.addParameter(new Parameter("startDate", "startDate", Date.class));
		patientVisitsWithDocumentedBP.addParameter(new Parameter("endDate", "endDate", Date.class));
		
		EncounterIndicator patientVisitsWithDocumentedBPIndicator = new EncounterIndicator();
		patientVisitsWithDocumentedBPIndicator.setName("patientVisitsWithDocumentedBPIndicator");
		patientVisitsWithDocumentedBPIndicator.setEncounterQuery(new Mapped<EncounterQuery>(
				patientVisitsWithDocumentedBP, ParameterizableUtil
				.createParameterMappings("startDate=${startDate},endDate=${endDate}")));
		
		dsd.addColumn(patientVisitsWithDocumentedBPIndicator);
		
		
	}
	
	// Create Monthly Location Data set
	public LocationHierachyIndicatorDataSetDefinition createMonthlyLocationDataSet() {
		
		LocationHierachyIndicatorDataSetDefinition ldsd = new LocationHierachyIndicatorDataSetDefinition(
		        createMonthlyEncounterBaseDataSet());
		ldsd.addBaseDefinition(createMonthlyBaseDataSet());
		ldsd.setName("Encounter Monthly Data Set");
		ldsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		ldsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		ldsd.addParameter(new Parameter("location", "District", LocationHierarchy.class));
		
		return ldsd;
	}
	
	private EncounterIndicatorDataSetDefinition createMonthlyEncounterBaseDataSet() {
		
		EncounterIndicatorDataSetDefinition eidsd = new EncounterIndicatorDataSetDefinition();
		
		eidsd.setName("eidsd");
		eidsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		eidsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		createMonthlyIndicators(eidsd);
		return eidsd;
	}
	
	// create monthly cohort Data set
	
	private CohortIndicatorDataSetDefinition createMonthlyBaseDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("Monthly Cohort Data Set");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		
		createMonthlyIndicators(dsd);
		return dsd;
	}
	
	private void createMonthlyIndicators(CohortIndicatorDataSetDefinition dsd) {
		// =======================================================
		// A2: Total # of patients seen in the last month
		// =======================================================
		EncounterCohortDefinition patientSeen = Cohorts.createEncounterParameterizedByDate("Patients seen",
		    onOrAfterOnOrBefore, heartFailureEncounterType);
		
		CohortIndicator patientsSeenIndicator = Indicators.newCountIndicator("patientsSeenIndicator", patientSeen,
		    ParameterizableUtil.createParameterMappings("onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		dsd.addColumn("A2M", "Total # of patients seen in the last month", new Mapped(patientsSeenIndicator,
		        ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		//=======================================================
		// A3: Total # of new patients enrolled in the last month
		//=======================================================
		ProgramEnrollmentCohortDefinition enrolledInHFProgram = Cohorts
		        .createProgramEnrollmentParameterizedByStartEndDate("enrolledInHFProgram", heartFailureProgram);
		
		CohortIndicator enrolledInHFProgramIndicator = Indicators.newCountIndicator(
		    "enrolledInHFProgramIndicator", enrolledInHFProgram,
		    ParameterizableUtil.createParameterMappings("enrolledOnOrAfter=${startDate},enrolledOnOrBefore=${endDate}"));
		
		dsd.addColumn(
		    "A3M",
		    "Total # of new patients enrolled in the last month",
		    new Mapped(enrolledInHFProgramIndicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		//=======================================================
		// B4: Of the new patients enrolled in the last month, % with NYHA class IV 
		//=======================================================
		
		SqlCohortDefinition patientsWithNYHAClassIV = Cohorts
        .getPatientsWithObservationInFormBetweenStartAndEndDate("patientsWithpatientsWithNYHAClassIV",
        	heartFailureDDBform, NYHACLASS, NYHACLASS4);
		
		CompositionCohortDefinition patientsEnrolledAndWithNYHAClassIV = new CompositionCohortDefinition();
		patientsEnrolledAndWithNYHAClassIV
		        .setName("patientsEnrolledAndWithNYHAClassIV");
		patientsEnrolledAndWithNYHAClassIV.addParameter(new Parameter("onOrAfter", "onOrAfter",
		        Date.class));
		patientsEnrolledAndWithNYHAClassIV.addParameter(new Parameter("onOrBefore", "onOrBefore",
		        Date.class));

		patientsEnrolledAndWithNYHAClassIV.addParameter(new Parameter("startDate", "startDate",
		        Date.class));
		patientsEnrolledAndWithNYHAClassIV.addParameter(new Parameter("endDate", "endDate", Date.class));
		patientsEnrolledAndWithNYHAClassIV.getSearches().put(
		    "1",
		    new Mapped<CohortDefinition>(patientsWithNYHAClassIV, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")));
		patientsEnrolledAndWithNYHAClassIV.getSearches().put(
		    "2",
		    new Mapped<CohortDefinition>(enrolledInHFProgram, ParameterizableUtil
		            .createParameterMappings("enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}")));
		patientsEnrolledAndWithNYHAClassIV.setCompositionString("1 AND 2");
		
		CohortIndicator patientsEnrolledAndWithNYHAClassIVIndicator = Indicators
		        .newCountIndicator(
		            "patientsEnrolledAndWithNYHAClassIVIndicator",
		            patientsEnrolledAndWithNYHAClassIV,
		            ParameterizableUtil
		                    .createParameterMappings("endDate=${endDate},startDate=${startDate},onOrBefore=${endDate},onOrAfter=${startDate}"));
		dsd.addColumn(
		    "B4M",
		    "New patients enrolled in the last month with NYHA class IV ",
		    new Mapped(patientsEnrolledAndWithNYHAClassIVIndicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		//=======================================================
		// B5: Of the patients newly enrolled in month with documented creatinine check in the last month, # and % with first creatinine >200
		
		//=======================================================
		NumericObsCohortDefinition patientsWithSRGreaterThan200 = Cohorts.createNumericObsCohortDefinition(
		    "patientsWithSRGreaterThan200", onOrAfterOnOrBefore, serumCreatinine, 200,
		    RangeComparator.GREATER_THAN, TimeModifier.FIRST);
		
		CompositionCohortDefinition enrolledWithWithCRGreaterThan200 = new CompositionCohortDefinition();
		enrolledWithWithCRGreaterThan200.setName("enrolledWithWithCRGreaterThan200");
		enrolledWithWithCRGreaterThan200.addParameter(new Parameter("onOrAfter", "onOrAfter", Date.class));
		enrolledWithWithCRGreaterThan200.addParameter(new Parameter("onOrBefore", "onOrBefore", Date.class));
		enrolledWithWithCRGreaterThan200.getSearches().put(
		    "1",
		    new Mapped<CohortDefinition>(patientsWithSRGreaterThan200, ParameterizableUtil
		            .createParameterMappings("onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}")));
		enrolledWithWithCRGreaterThan200.getSearches().put(
		    "2",
		    new Mapped<CohortDefinition>(enrolledInHFProgram, ParameterizableUtil
		            .createParameterMappings("enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}")));
		enrolledWithWithCRGreaterThan200
		        .setCompositionString("1 AND 2");
		
		CohortIndicator enrolledWithCRGreaterThan200Indicator = Indicators
		        .newCountIndicator(
		            "enrolledWithWithCRGreaterThan200Indicator",
		            enrolledWithWithCRGreaterThan200,
		            ParameterizableUtil
		                    .createParameterMappings("onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		dsd.addColumn(
		    "B5M",
		    "Newly enrolled in month with documented creatinine check in the last month, with first creatinine >200",
		    new Mapped(enrolledWithCRGreaterThan200Indicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		//=======================================================
		// C1: Of active patients, # and % who had Cr checked at a visit within the past 6 months 
		//=======================================================
		
		CohortIndicator activePatientsIndicator = Indicators.newCountIndicator("activePatientsIndicator", patientSeen,
		    ParameterizableUtil.createParameterMappings("onOrAfter=${endDate-12m+1d},onOrBefore=${endDate}"));
		
		dsd.addColumn("C1DM", "Total # of patients seen in the last 12 months", new Mapped(activePatientsIndicator,
		        ParameterizableUtil.createParameterMappings("endDate=${endDate}")), "");
		           
		NumericObsCohortDefinition patientsWithSRBetweenDates = Cohorts.createNumericObsCohortDefinition(
		    "patientsWithSRBetweenDates", onOrAfterOnOrBefore, serumCreatinine, 0,
		    null, TimeModifier.ANY);
		
		CompositionCohortDefinition activePatientsWithSC = new CompositionCohortDefinition();
		activePatientsWithSC.setName("activePatientsWithSC");
		activePatientsWithSC.addParameter(new Parameter("onOrAfter", "onOrAfter", Date.class));
		activePatientsWithSC.addParameter(new Parameter("onOrBefore", "onOrBefore", Date.class));
		activePatientsWithSC.getSearches().put(
		    "1",
		    new Mapped<CohortDefinition>(patientsWithSRBetweenDates, ParameterizableUtil
		            .createParameterMappings("onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}")));
		activePatientsWithSC.getSearches().put(
		    "2",
		    new Mapped<CohortDefinition>(patientSeen, ParameterizableUtil
		            .createParameterMappings("onOrAfter=${onOrBefore-12m+1d},onOrBefore=${onOrBefore}")));
		activePatientsWithSC.setCompositionString("1 AND 2");
		
		CohortIndicator activePatientsWithSRBetweenDatesIndicator = Indicators
        .newCountIndicator(
            "activePatientsWithSRBetweenDatesIndicator",
            activePatientsWithSC,
            ParameterizableUtil
                    .createParameterMappings("onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		dsd.addColumn(
		    "C1NM",
		    "Active patients who had Cr checked at a visit within the past 6 months ",
		    new Mapped(activePatientsWithSRBetweenDatesIndicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		//=======================================================
		// D1: Of total patients seen in the last month, % on carvedilol or atenolol as of last visit
		//=======================================================
		
		SqlCohortDefinition onCarvedilolOratenolol = Cohorts.getPatientsOnCurrentRegimenBasedOnEndDate("onCarvedilolOratenolol",
			carvedilolAndAtenolol);
		
		CompositionCohortDefinition patientsSeenOnCarvedilolOratenolol = new CompositionCohortDefinition();
		patientsSeenOnCarvedilolOratenolol.setName("patientsSeenOnCarvedilolOratenolol");
		patientsSeenOnCarvedilolOratenolol.addParameter(new Parameter("onOrAfter", "onOrAfter", Date.class));
		patientsSeenOnCarvedilolOratenolol.addParameter(new Parameter("onOrBefore", "onOrBefore", Date.class));
		patientsSeenOnCarvedilolOratenolol.getSearches().put(
		    "1",
		    new Mapped<CohortDefinition>(onCarvedilolOratenolol, ParameterizableUtil
		            .createParameterMappings("endDate=${onOrBefore}")));
		patientsSeenOnCarvedilolOratenolol.getSearches().put(
		    "2",
		    new Mapped<CohortDefinition>(patientSeen, ParameterizableUtil
		            .createParameterMappings("onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}")));
		patientsSeenOnCarvedilolOratenolol.setCompositionString("1 AND 2");
		
		CohortIndicator patientsSeenOnCarvedilolOratenololIndicator = Indicators.newCountIndicator(
		    "patientsSeenOnCarvedilolOratenolol",
		    patientsSeenOnCarvedilolOratenolol,
		    ParameterizableUtil.createParameterMappings("onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		dsd.addColumn(
		    "D1M",
		    "patients seen in the last month and are on carvedilol or atenolol",
		    new Mapped(patientsSeenOnCarvedilolOratenololIndicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		//=======================================================
		// D2: Of total patients seen in the last month, % on lisinopril or captopril as of last visit
		//=======================================================
		
		SqlCohortDefinition onLisinoprilOrCaptopril = Cohorts.getPatientsOnCurrentRegimenBasedOnEndDate("onLisinoprilOrCaptopril",
			carvedilolAndAtenolol);
		
		CompositionCohortDefinition patientsSeenOnLisinoprilOrCaptopril = new CompositionCohortDefinition();
		patientsSeenOnLisinoprilOrCaptopril.setName("patientsSeenOnLisinoprilOrCaptopril");
		patientsSeenOnLisinoprilOrCaptopril.addParameter(new Parameter("onOrAfter", "onOrAfter", Date.class));
		patientsSeenOnLisinoprilOrCaptopril.addParameter(new Parameter("onOrBefore", "onOrBefore", Date.class));
		patientsSeenOnLisinoprilOrCaptopril.getSearches().put(
		    "1",
		    new Mapped<CohortDefinition>(onLisinoprilOrCaptopril, ParameterizableUtil
		            .createParameterMappings("endDate=${onOrBefore}")));
		patientsSeenOnLisinoprilOrCaptopril.getSearches().put(
		    "2",
		    new Mapped<CohortDefinition>(patientSeen, ParameterizableUtil
		            .createParameterMappings("onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}")));
		patientsSeenOnLisinoprilOrCaptopril.setCompositionString("1 AND 2");
		
		CohortIndicator patientsSeenOnLisinoprilOrCaptoprilIndicator = Indicators.newCountIndicator(
		    "patientsSeenOnLisinoprilOrCaptoprilIndicator",
		    patientsSeenOnLisinoprilOrCaptopril,
		    ParameterizableUtil.createParameterMappings("onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		dsd.addColumn(
		    "D2M",
		    "patients seen in the last month and are on lisinopril or captopril",
		    new Mapped(patientsSeenOnLisinoprilOrCaptoprilIndicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		//=======================================================
		// D3: Of total patients seen in the last month, % on furosemide as of last visit
		//=======================================================
		
		SqlCohortDefinition onFurosemide = Cohorts.getPatientsOnCurrentRegimenBasedOnEndDate("onFurosemide",
			furosemide);
		
		CompositionCohortDefinition patientsSeenOnFurosemide = new CompositionCohortDefinition();
		patientsSeenOnFurosemide.setName("patientsSeenOnFurosemide");
		patientsSeenOnFurosemide.addParameter(new Parameter("onOrAfter", "onOrAfter", Date.class));
		patientsSeenOnFurosemide.addParameter(new Parameter("onOrBefore", "onOrBefore", Date.class));
		patientsSeenOnFurosemide.getSearches().put(
		    "1",
		    new Mapped<CohortDefinition>(onFurosemide, ParameterizableUtil
		            .createParameterMappings("endDate=${onOrBefore}")));
		patientsSeenOnFurosemide.getSearches().put(
		    "2",
		    new Mapped<CohortDefinition>(patientSeen, ParameterizableUtil
		            .createParameterMappings("onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}")));
		patientsSeenOnFurosemide.setCompositionString("1 AND 2");
		
		CohortIndicator patientsSeenOnFurosemideIndicator = Indicators.newCountIndicator(
		    "patientsSeenOnFurosemideIndicator",
		    patientsSeenOnFurosemide,
		    ParameterizableUtil.createParameterMappings("onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		dsd.addColumn(
		    "D3M",
		    "patients seen in the last month and are on furosemide",
		    new Mapped(patientsSeenOnFurosemideIndicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		//=======================================================
		// D4: Of total patients seen in the last month, % on aldactone as of last visit
		//=======================================================
		
		SqlCohortDefinition onAldactone = Cohorts.getPatientsOnCurrentRegimenBasedOnEndDate("onAldactone",
			aldactone);
		
		CompositionCohortDefinition patientsSeenOnAldactone = new CompositionCohortDefinition();
		patientsSeenOnAldactone.setName("patientsSeenOnAldactone");
		patientsSeenOnAldactone.addParameter(new Parameter("onOrAfter", "onOrAfter", Date.class));
		patientsSeenOnAldactone.addParameter(new Parameter("onOrBefore", "onOrBefore", Date.class));
		patientsSeenOnAldactone.getSearches().put(
		    "1",
		    new Mapped<CohortDefinition>(onAldactone, ParameterizableUtil
		            .createParameterMappings("endDate=${onOrBefore}")));
		patientsSeenOnAldactone.getSearches().put(
		    "2",
		    new Mapped<CohortDefinition>(patientSeen, ParameterizableUtil
		            .createParameterMappings("onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}")));
		patientsSeenOnAldactone.setCompositionString("1 AND 2");
		
		CohortIndicator patientsSeenOnAldactoneIndicator = Indicators.newCountIndicator(
		    "patientsSeenOnAldactoneIndicator",
		    patientsSeenOnAldactone,
		    ParameterizableUtil.createParameterMappings("onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		dsd.addColumn(
		    "D4M",
		    "patients seen in the last month and are on aldactone",
		    new Mapped(patientsSeenOnAldactoneIndicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");		
		
		//=======================================================
		// F3: Of total active patients, # and % with NYHA class IV at last visit (not including DDB)
		//=======================================================
		SqlCohortDefinition patientsWithPostoperatoireOrInsuffisanceRDV = Cohorts
        .getPatientsWithObservationInFormBetweenStartAndEndDate("patientsWithPostoperatoireOrInsuffisanceRDV",
        	postoperatoireAndinsuffisanceRDV, NYHACLASS, NYHACLASS4);
		
		CompositionCohortDefinition activePatientsWithPostoperatoireOrInsuffisanceRDV = new CompositionCohortDefinition();
		activePatientsWithPostoperatoireOrInsuffisanceRDV.setName("activePatientsWithSC");
		activePatientsWithPostoperatoireOrInsuffisanceRDV.addParameter(new Parameter("onOrAfter", "onOrAfter", Date.class));
		activePatientsWithPostoperatoireOrInsuffisanceRDV.addParameter(new Parameter("onOrBefore", "onOrBefore", Date.class));
		activePatientsWithPostoperatoireOrInsuffisanceRDV.getSearches().put(
		    "1",
		    new Mapped<CohortDefinition>(patientsWithPostoperatoireOrInsuffisanceRDV, ParameterizableUtil
		            .createParameterMappings("startDate=${onOrAfter},endDate=${onOrBefore}")));
		activePatientsWithPostoperatoireOrInsuffisanceRDV.getSearches().put(
		    "2",
		    new Mapped<CohortDefinition>(patientSeen, ParameterizableUtil
		            .createParameterMappings("onOrAfter=${onOrBefore-12m+1d},onOrBefore=${onOrBefore}")));
		activePatientsWithPostoperatoireOrInsuffisanceRDV.setCompositionString("1 AND 2");
		
		CohortIndicator activePatientsWithPostoperatoireOrInsuffisanceRDVIndicator = Indicators
        .newCountIndicator(
            "activePatientsWithSRBetweenDatesIndicator",
            activePatientsWithPostoperatoireOrInsuffisanceRDV,
            ParameterizableUtil
                    .createParameterMappings("onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		dsd.addColumn(
		    "F3NM",
		    "Active patients with NYHA class IV at last visit (not including DDB)",
		    new Mapped(activePatientsWithPostoperatoireOrInsuffisanceRDVIndicator, ParameterizableUtil
		            .createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
	}
	
	private void setUpProperties() {
		heartFailureProgram = gp.getProgram(GlobalPropertiesManagement.HEART_FAILURE_PROGRAM_NAME);
		HFPrograms.add(heartFailureProgram);
		
		heartFailureEncounterType = gp.getEncounterType(GlobalPropertiesManagement.HEART_FAILURE_ENCOUNTER);
		
		onOrAfterOnOrBefore.add("onOrAfter");
		onOrAfterOnOrBefore.add("onOrBefore");		
		heartFailureDDBform = gp.getForm(GlobalPropertiesManagement.HEART_FAILURE_DDB);		
	    NYHACLASS = gp.getConcept(GlobalPropertiesManagement.NYHA_CLASS);
	    NYHACLASS4 =gp.getConcept(GlobalPropertiesManagement.NYHA_CLASS_4);
	    serumCreatinine = gp.getConcept(GlobalPropertiesManagement.SERUM_CREATININE);
	    
	    systolicBP = gp.getConcept(GlobalPropertiesManagement.SYSTOLIC_BLOOD_PRESSURE);
	    
	    carvedilol = gp.getConcept(GlobalPropertiesManagement.CARVEDILOL);	    
	    atenolol = gp.getConcept(GlobalPropertiesManagement.ATENOLOL);
	    carvedilolAndAtenolol.add(carvedilol);
	    carvedilolAndAtenolol.add(atenolol);
	    lisinopril = gp.getConcept(GlobalPropertiesManagement.LISINOPRIL);
        captopril = gp.getConcept(GlobalPropertiesManagement.CAPTOPRIL);
        lisinoprilAndCaptopril.add(lisinopril);
        lisinoprilAndCaptopril.add(captopril);
        
        furosemide = gp.getConcept(GlobalPropertiesManagement.FUROSEMIDE);
        aldactone = gp.getConcept(GlobalPropertiesManagement.ALDACTONE);
        
        postoperatoireCardiaqueRDV = gp.getForm(GlobalPropertiesManagement.POSTOPERATOIRE_CARDIAQUERDV);		
        insuffisanceCardiaqueRDV = gp.getForm(GlobalPropertiesManagement.INSUFFISANCE_CARDIAQUE_RDV);
        
        postoperatoireAndinsuffisanceRDV.add(postoperatoireCardiaqueRDV);
        postoperatoireAndinsuffisanceRDV.add(insuffisanceCardiaqueRDV);      
       
		
	}
	
}