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

public class Element {

	private String 					m_tag 		= null;
	private ArrayList<Attribute> 	m_atts 		= null;
	private ArrayList<Element>		m_children 	= null;
	private String					m_value		= null;
	
	public Element() {
		super();
		m_atts = new ArrayList<Attribute>();
		m_children = new ArrayList<Element>();
	}

	public String getTag() {
		return m_tag;
	}

	public void setTag(String tag) {
		m_tag = tag;
	}

	public String getValue() {
		return m_value;
	}
	
	public String getValueNotNull() {
		
		if (m_value == null)
		{
			return "";
		}
		
		return m_value;
	}

	public void setValue(String value) {
		m_value = value;
	}
	
	public void appendToValue(String value) {
		
		if (m_value == null)
		{
			m_value = value;
		}
		else
		{
			m_value = m_value + value;
		}
	}

	public int getNumAttributes() {
		return m_atts.size();
	}
	
	public Attribute getAttribute(int index) {
		
		if (index < 0 || index >= getNumAttributes())
		{
			return null;
		}
		
		return m_atts.get(index);
	}
	
	public Attribute getAttribute(String name) {
		
		for (int i = 0; i < getNumAttributes(); i++)
		{
			Attribute attr = getAttribute(i);
			if (attr.getName().equals(name))
			{
				return attr;
			}
		}
		
		return null;
	}
	
	public void addAttribute(Attribute attr) {
		m_atts.add(attr);
	}
	
	public int getNumChildren() {
		return m_children.size();
	}
	
	public Element getChild(int index) {
		
		if (index < 0 || index >= getNumChildren())
		{
			return null;
		}
		
		return m_children.get(index);
	}
	
	public Element getChild(String name) {
		
		for (int i = 0; i < getNumChildren(); i++)
		{
			Element elem = getChild(i);
			if (elem.getTag().equals(name))
			{
				return elem;
			}
		}
		
		return null;
	}

	public void addChild(Element child) {
		m_children.add(child);
	}
}
