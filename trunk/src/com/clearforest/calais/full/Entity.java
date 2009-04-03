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

import com.clearforest.calais.common.JSONUtils;


/*
 * A class that represents a single entity identified by
 * Calais and can return itself in JSON format
 */
public class Entity extends IdentifiedElement {
	
	private String 				m_name 		= null;
	private float				m_relevance	= 0.0F;
	
	public Entity() {
		super();
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}
	
	public float getRelevance() {
		return m_relevance;
	}

	public void setRelevance(float relevance) {
		m_relevance = relevance;
	}

	/**
	 * Convert entity to JSON format
	 * @return entity in JSON format
	 */
	public String toJSON() {
		
		StringBuilder json = new StringBuilder();
		
		json.append("{ \"type\":\"" + JSONUtils.escapeForJSON(m_type) + "\",");
		json.append("\"name\":\"" + JSONUtils.escapeForJSON(m_name) + "\",");
		json.append("\"id\":\"" + JSONUtils.escapeForJSON(m_id) + "\",");
		json.append("\"relevance\":" + Float.toString(m_relevance) + ",");
		json.append("\"instances\": ");
		json.append(m_instances.toJSON());
		json.append(" }");
		
		return json.toString();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("type:" + m_type);
		sb.append(", name: " + m_name);
		sb.append(", id: " + m_id);
		sb.append(", relevance: " + Float.toString(m_relevance));
		sb.append(", num instances: " + m_instances.getNumInstances());
		
		return sb.toString();
	}

}
