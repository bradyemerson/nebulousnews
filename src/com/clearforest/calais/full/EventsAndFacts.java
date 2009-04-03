/*
 * Copyright (c) 2008, ClearForest Ltd.
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 		- 	Redistributions of source code must retain the above 
 * 			copyright notice, this list of conditions and the 
 * 			following disclaimer.
 * 
 * 		- 	Redistributions in binary form must reproduce the above 
 * 			copyright notice, this list of conditions and the 
 * 			following disclaimer in the documentation and/or other 
 * 			materials provided with the distribution. 
 * 
 * 		- 	Neither the name of ClearForest Ltd. nor the names of 
 * 			its contributors may be used to endorse or promote 
 * 			products derived from this software without specific prior 
 * 			written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.clearforest.calais.full;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.clearforest.calais.common.JSONUtils;

/**
 * Class that holds all events and facts included in the RDF and 
 * can return its content in JSON format
 *
 */
public class EventsAndFacts {

	HashMap<String,String> 					
		m_pluralMap 		= null; 
		// map to get plural form of known entities
	HashMap<String,ArrayList<EventOrFact>> 	
		m_eventsAndFactsMap = null;
		// map to the events/facts arrays
	ArrayList<ArrayList<EventOrFact>>
		m_eventsAndFactsList = null;

	public EventsAndFacts() {
		super();
		initPluralMap();
		initEventsAndFactsMapAndList();
	}

	public void addEventOrFact(EventOrFact eventOrFact) {
		
		ArrayList<EventOrFact> array = null;
		
		array = m_eventsAndFactsMap.get(eventOrFact.getType());
		if (array == null)
		{
			array = new ArrayList<EventOrFact>();
			m_eventsAndFactsMap.put(eventOrFact.getType(), array);
			m_eventsAndFactsList.add(array);
		}
		
		array.add(eventOrFact);
	}
	
	/**
	 * Return all events/facts encoded as a JSON object of 
	 * event/fact arrays, e.g.
	 * 
	 * 		{
	 * 			"Bankruptcies": [ ... ],
	 * 			"CompanyLocations": [ ... ],
	 * 			...
	 * 			"PersonsPoliticalPast": [ ... ]
	 * 		}
	 * 
	 * Only non-empty arrays appear in the list
	 * 
	 * @return a JSON object with event/fact arrays encoded in 
	 * 			JSON
	 */
	public String toJSON() {

		StringBuilder 	json 	= new StringBuilder();
		boolean			first	= true;
		boolean			effirst 	= true;
		
		json.append("{ ");
		
		Iterator<ArrayList<EventOrFact>> iter =
			m_eventsAndFactsList.iterator();

		/*
		 * Go over all event/facts, array by array 
		 */
		while (iter.hasNext())
		{
			ArrayList<EventOrFact> array = iter.next();
			if (array.size() == 0)
			{
				// only non-empty arrays
				continue;
			}
			
			if (!first)
			{
				json.append(", ");
			}

			/*
			 * Put the event/fact in plural form as the name of 
			 * this particular array
			 */
			json.append("\"" + 
					JSONUtils.escapeForJSON(toPlural(array.get(0).getType())) + "\": [");
			
			/*
			 * Add this particular array's events/facts
			 */
			Iterator<EventOrFact> efiter = array.iterator();
			effirst = true;
			
			while (efiter.hasNext())
			{
				EventOrFact eventOrFact = efiter.next();
				
				if (!effirst)
				{
					json.append(", ");
				}
				
				json.append(eventOrFact.toJSON());
				
				effirst = false;
			}
			json.append("]");
			
			first = false;
		}
		
		json.append("}");
		
		return json.toString();
	}

	/**
	 * Initialize the array of singluar to plural conversion
	 * of entity names. Names not in the map will be converted
	 * into plural by adding s
	 *
	 */
	private void initPluralMap() {
		
		m_pluralMap = new HashMap<String,String>();
		
		m_pluralMap.put("Acquisition", "Acquisitions");
		m_pluralMap.put("Alliance", "Alliances");
		m_pluralMap.put("AnalystEarningsEstimate", "AnalystEarningsEstimates");
		m_pluralMap.put("AnalystRecommendation", "AnalystRecommendations");
		m_pluralMap.put("Bankruptcy", "Bankruptcies");
		m_pluralMap.put("BusinessRelation", "BusinessRelations");
		m_pluralMap.put("Buybacks", "Buybacks");
		m_pluralMap.put("CompanyAffiliates", "CompanyAffiliates");
		m_pluralMap.put("CompanyCustomer", "CompanyCustomers");
		m_pluralMap.put("CompanyEarningsAnnouncement", "CompanyEarningsAnnouncements");
		m_pluralMap.put("CompanyEarningsGuidance", "CompanyEarningsGuidances");
		m_pluralMap.put("CompanyInvestment", "CompanyInvestments");
		m_pluralMap.put("CompanyLegalIssues", "CompanyLegalIssues");
		m_pluralMap.put("CompanyLocation", "CompanyLocations");
		m_pluralMap.put("CompanyMeeting", "CompanyMeetings");
		m_pluralMap.put("CompanyReorganization", "CompanyReorganizations");
		m_pluralMap.put("CompanyTechnology", "CompanyTechnologies");
		m_pluralMap.put("ConferenceCall", "ConferenceCalls");
		m_pluralMap.put("CreditRating", "CreditRatings");
		m_pluralMap.put("FamilyRelation", "FamilyRelations");
		m_pluralMap.put("IPO", "IPOs");
		m_pluralMap.put("JointVenture", "JointVentures");
		m_pluralMap.put("ManagementChange", "ManagementChanges");
		m_pluralMap.put("Merger", "Mergers");
		m_pluralMap.put("PersonEducation", "PersonsEducation");
		m_pluralMap.put("PersonPolitical", "PersonsPolitical");
		m_pluralMap.put("PersonPoliticalPast", "PersonsPoliticalPast");
		m_pluralMap.put("PersonProfessional", "PersonsProfessional");
		m_pluralMap.put("PersonProfessionalPast", "PersonsProfessionalPast");
		m_pluralMap.put("Quotation", "Quotations");
		m_pluralMap.put("StockSplit", "StockSplits");

	}
	
	/**
	 * Initialize the map of events and facts with empty arrays and
	 * add the new arrays to the events and facts list. 
	 * This ensures that later when the events/facts list is 
	 * traversed, known entities will appear in the order we 
	 * insert them here  
	 *
	 */
	private void initEventsAndFactsMapAndList() {
		
		ArrayList<EventOrFact> array = null;
		
		m_eventsAndFactsMap = new HashMap<String,ArrayList<EventOrFact>>();
		m_eventsAndFactsList = new ArrayList<ArrayList<EventOrFact>>();
		
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("Acquisition", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("Alliance", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("AnalystEarningsEstimate", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("AnalystRecommendation", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("Bankruptcy", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("BusinessRelation", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("Buybacks", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyAffiliates", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyCustomer", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyEarningsAnnouncement", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyEarningsGuidance", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyInvestment", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyLegalIssues", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyLocation", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyMeeting", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyReorganization", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CompanyTechnology", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("ConferenceCall", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("CreditRating", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("FamilyRelation", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("IPO", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("JointVenture", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("ManagementChange", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("Merger", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("PersonEducation", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("PersonPolitical", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("PersonPoliticalPast", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("PersonProfessional", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("PersonProfessionalPast", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("Quotation", array);
		m_eventsAndFactsList.add(array);
		array = new ArrayList<EventOrFact>();
		m_eventsAndFactsMap.put("StockSplit", array);
		m_eventsAndFactsList.add(array);
	}

	/**
	 * Return the event/fact name in plural form. If in pluralMap
	 * return the corresponding value. Otherwise add 's'
	 * @param singular - the event/fact type in singular form
	 */
	private String toPlural(String singular) {
		
		String ret = null;
		
		ret = m_pluralMap.get(singular);
		if (ret == null)
		{
			ret = singular + "s";
		}
		
		return ret;
	}

}

