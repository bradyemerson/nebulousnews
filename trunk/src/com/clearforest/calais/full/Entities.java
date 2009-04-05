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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.clearforest.calais.common.JSONUtils;

/**
 * Class that holds all entities included in the RDF and can
 * return its content in JSON format
 *
 */
public class Entities implements Serializable{
	private static final long serialVersionUID = 1L;
	private HashMap<String,String> 				m_pluralMap 	= null; 
		// map to get plural form of known entities
	private HashMap<String,ArrayList<Entity>> 	m_entitiesMap 	= null;
		// map to the entities arrays
	private ArrayList<ArrayList<Entity>>		m_entitiesList	= null;
	

	public Entities() {
		super();
		initPluralMap();
		initEntitiesMapAndList();
	}
	
	public void addEntity(Entity entity) {
		
		ArrayList<Entity> array = null;
		
		array = m_entitiesMap.get(entity.getType());
		if (array == null)
		{
			array = new ArrayList<Entity>();
			m_entitiesMap.put(entity.getType(), array);
			m_entitiesList.add(array);
		}
		
		array.add(entity);
	}
	
	/**
	 * Return all entities encoded as a JSON object of entity 
	 * arrays, e.g.
	 * 
	 * 		{
	 * 			"Anniversaries": [ ... ],
	 * 			"Cities": [ ... ],
	 * 			...
	 * 			"URLs": [ ... ]
	 * 		}
	 * 
	 * Only non-empty arrays appear in the list
	 * 
	 * @return a JSON object with entity arrays encoded in JSON
	 */
	public String toJSON() {

		StringBuilder 	json 	= new StringBuilder();
		boolean			first	= true;
		boolean			efirst 	= true;
		
		json.append("{ ");
		
		Iterator<ArrayList<Entity>> iter =
			m_entitiesList.iterator();

		/*
		 * Go over all entities, array by array 
		 */
		while (iter.hasNext())
		{
			ArrayList<Entity> array = iter.next();
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
			 * Put the entity in plural form as the name of this
			 * particular array
			 */
			json.append("\"" + 
					JSONUtils.escapeForJSON(toPlural(array.get(0).getType())) + "\": [");
			
			/*
			 * Add this particular array's entities
			 */
			Iterator<Entity> eiter = array.iterator();
			efirst = true;
			
			while (eiter.hasNext())
			{
				Entity entity = eiter.next();
				
				if (!efirst)
				{
					json.append(", ");
				}
				
				json.append(entity.toJSON());
				
				efirst = false;
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
		
		m_pluralMap.put("Anniversary", "Anniversaries");
		m_pluralMap.put("City", "Cities");
		m_pluralMap.put("Company", "Companies");
		m_pluralMap.put("Continent", "Continents");
		m_pluralMap.put("Country", "Countries");
		m_pluralMap.put("Currency", "Currencies");
		m_pluralMap.put("EmailAddress", "EmailAddresses");
		m_pluralMap.put("Facility", "Facilities");
		m_pluralMap.put("FaxNumber", "FaxNumbers");
		m_pluralMap.put("Holiday", "Holidays");
		m_pluralMap.put("IndustryTerm", "IndustryTerms");
		m_pluralMap.put("NaturalDisaster", "NaturalDisasters");
		m_pluralMap.put("NaturalFeature", "NaturalFeatures");
		m_pluralMap.put("Organization", "Organizations");
		m_pluralMap.put("Person", "Persons");
		m_pluralMap.put("PhoneNumber", "PhoneNumbers");
		m_pluralMap.put("ProvinceOrState", "ProvincesOrStates");
		m_pluralMap.put("Region", "Regions");
		m_pluralMap.put("Technology", "Technologies");
		m_pluralMap.put("URL", "URLs");
	}

	/**
	 * Initialize the map of entities with empty arrays and add
	 * the new arrays to the array of entities. This
	 * ensures that later when the entities array is traversed,
	 * known entities will appear in the order we insert them
	 * here  
	 *
	 */
	private void initEntitiesMapAndList() {

		ArrayList<Entity> array = null;
		
		m_entitiesMap = new HashMap<String,ArrayList<Entity>>();
		m_entitiesList = new ArrayList<ArrayList<Entity>>();

		array = new ArrayList<Entity>();
		m_entitiesMap.put("Anniversary", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("City", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Company", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Continent", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Country", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Currency", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("EmailAddress", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Facility", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("FaxNumber", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Holiday", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("IndustryTerm", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("NaturalDisaster", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("NaturalFeature", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Organization", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Person", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("PhoneNumber", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("ProvinceOrState", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Region", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("Technology", array);
		m_entitiesList.add(array);
		array = new ArrayList<Entity>();
		m_entitiesMap.put("URL", array);
		m_entitiesList.add(array);
	}

	/**
	 * Return the entity name in plural form. If in pluralMap
	 * return the corresponding value. Otherwise add 's'
	 * @param singular - the entity type in singular form
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
	
	public String toString() {
		return m_entitiesMap.toString();
	}
}
