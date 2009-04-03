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
import java.util.Iterator;

import com.clearforest.calais.common.JSONUtils;
import com.clearforest.calais.common.Property;

/**
 * A class that represents a single event or fact identified
 * by Calais
 *
 */
public class EventOrFact extends IdentifiedElement {

	private ArrayList<Property>		m_properties 	= null;
	
	public EventOrFact() {
		super();
		m_properties = new ArrayList<Property>();
	}
	
	public ArrayList<Property> getProperties() {
		return m_properties;
	}

	public void addProperty(String name, String value) {
		m_properties.add(new Property(name, value));
	}

	public String toJSON() {
		
		StringBuilder json = new StringBuilder();
		
		json.append("{ \"type\":\"" + JSONUtils.escapeForJSON(m_type) + "\",");
		json.append("\"id\":\"" + JSONUtils.escapeForJSON(m_id) + "\",");
		
		Iterator<Property> iter = m_properties.iterator(); 
		while (iter.hasNext())
		{
			Property prop = iter.next();
			
			json.append("\"" + 
					JSONUtils.escapeForJSON(prop.getName()) +
					"\":");
			
			json.append("\"" +
					JSONUtils.escapeForJSON(prop.getValue()) +
					"\", ");
		}
		
		json.append("\"instances\": ");
		json.append(m_instances.toJSON());
		json.append(" }");
		
		return json.toString();
	}

}
