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

import com.clearforest.calais.common.StringUtils;


public class Description extends Element {

	public static final int		TYPE_NONE			= 0;
	public static final int		TYPE_DOC_INFO		= 1;
	public static final int		TYPE_DOC_INFO_META	= 2;
	public static final int		TYPE_DEFAULT_LANG_ID= 3;
	public static final int		TYPE_ENTITY			= 4;
	public static final int		TYPE_EVENT_OR_FACT	= 5;
	public static final int		TYPE_INSTANCE_INFO	= 6;
	public static final int		TYPE_RELEVANCE_INFO	= 7;
	
	private int 		m_type 		= TYPE_NONE;
	private String		m_typeStr	= null;
	private String 		m_about 	= null;
	
	public Description() {
		super();
	}

	public String getAbout() {
		return m_about;
	}

	public void setAbout(String about) {
		m_about = about;
	}

	public int getType() {
		return m_type;
	}
	
	public String getTypeStr() {
		return m_typeStr;
	}

	public void setType(int type) {
		m_type = type;
	}

	/**
	 * Set the type of the description from a string. The
	 * m_type member is extracted from the string
	 * @param type - the type as a string
	 */
	public void setType(String type) {
		
		String typeIndicator 	= null;	// e, r, sys, etc.
		String typeStr			= null;	// Person, InstanceInfo, etc.
		
		m_type = TYPE_NONE;
		m_typeStr = null;

		if (type == null)
		{
			return;
		}
		
		typeIndicator = StringUtils.getSubstringBySeparator(
				type, "/", -2);
		if (typeIndicator == null)
		{
			return;
		}
		
		if (typeIndicator.equals("e"))
		{
			/* 
			 * An entity
			 */
			m_type = TYPE_ENTITY;
			m_typeStr = type;
		}
		else if (typeIndicator.equals("r"))
		{
			/*
			 * An event or fact
			 */
			m_type = TYPE_EVENT_OR_FACT;
			m_typeStr = type;
		}
		else if (typeIndicator.equals("sys"))
		{
			/*
			 * An instance / RelevanceInfo / DocInfo / DocInfoMeta
			 */
			typeStr = StringUtils.getSubstringBySeparator(
					type, "/", -1);
			if (typeStr == null)
			{
				return;
			}
			
			if (typeStr.equals("DocInfo"))
			{
				m_type = TYPE_DOC_INFO;
				m_typeStr = type;
			}
			else if (typeStr.equals("DocInfoMeta"))
			{
				m_type = TYPE_DOC_INFO_META;
				m_typeStr = type;
			}
			else if (typeStr.equals("InstanceInfo"))
			{
				m_type = TYPE_INSTANCE_INFO;
				m_typeStr = type;
			}
			else if (typeStr.equals("RelevanceInfo"))
			{
				m_type = TYPE_RELEVANCE_INFO;
				m_typeStr = type;
			}
		}
		else if (typeIndicator.equals("lid"))
		{
			/*
			 * Language ID
			 */
			typeStr = StringUtils.getSubstringBySeparator(
					type, "/", -1);
			if (typeStr == null)
			{
				return;
			}
			
			if (typeStr.equals("DefaultLangId"))
			{
				m_type = TYPE_DEFAULT_LANG_ID;
				m_typeStr = type;
			}
		}
		else
		{
			return;
		}
	}
	
}
