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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.clearforest.calais.common.CalaisJavaIf;
import com.clearforest.calais.common.JSONUtils;
import com.clearforest.calais.common.Property;
import com.clearforest.calais.common.StringUtils;


/*
 * Java class to return Calais Web service results in JSON
 */
public class CalaisJSONIf implements ErrorHandler {

	private CalaisJavaIf 			m_calaisIf 			= null;
	private boolean		 			m_isLastErr			= false;
	private String		 			m_lastErr			= null;
	private int						m_level				= 0;
	private ArrayList<Property>		m_infoMap			= null;
	private Entities				m_entities			= null;
	private EventsAndFacts			m_eventsAndFacts	= null;
	private	HashMap<String,IdentifiedElement>
									m_elementsHash		= null;
	private Description				m_currentDesc		= null;
	private Element					m_currentElem		= null;
	private ArrayList<Description>	m_descriptions		= null;

	/**
	 * Pass a valid API key on construction. To obtain an API key please see
	 * http://www.opencalais.com
	 */
	public CalaisJSONIf(String apiKey, String invokeTimeout,
			String overrideDefURL, String serviceURL,
			String verifyCerts) {

		m_calaisIf = new CalaisJavaIf(apiKey);
		m_calaisIf.setTimeout(Integer.parseInt(invokeTimeout));
		if (overrideDefURL.equals("true"))
		{
			m_calaisIf.setCalaisURL(serviceURL);
		}
		if (verifyCerts.equals("false"))
		{
			m_calaisIf.setVerifyCert(false);
		}
	}
	
	public boolean isLastErr() {
		return m_isLastErr;
	}

	public String getLastErr() {
		return m_lastErr;
	}

	/**
	 * Submit content to the OpenCalaisAPI, requesting output in
	 * RDF format. Return the results in full JSON
	 * 
	 * @param content - content to analyze
	 * @return semantic data in full (simplified) JSON
	 */
	public String getFullJSON(String content) {

		String 			rdf 	= null;
		StringBuilder 	json 	= null;
		
		/*
		 * Call the Web service on content
		 */
		m_calaisIf.setOutputFormat("xml/rdf");
		rdf = m_calaisIf.callEnlighten(content);

		/*
		 * Analyze response errors
		 */
		if (m_calaisIf.isLastErr())
		{
			return err(m_calaisIf.getLastErr());
		}

		if (rdf.indexOf("Enlighten ERROR:") != -1)
		{
			return err(rdf);
		}

		if (rdf.indexOf("<Exception>") != -1)
		{
			return err("Enlighten ERROR: " + rdf);
		}

		if (rdf.indexOf("<h1>403 Developer Inactive</h1>") != -1)
		{
			return err("Enlighten ERROR: " + rdf);
		}

		rdf = StringUtils.unescapeHTML(rdf);
		
		/*
		 * Response is valid - parse XML
		 */
		try
		{
			m_isLastErr = false;
			m_lastErr = "";
			m_level = 0;
			m_infoMap = new ArrayList<Property>();
			m_entities = new Entities();
			m_eventsAndFacts = new EventsAndFacts();
			m_currentDesc = null;
			m_currentElem = null;
			m_elementsHash = new HashMap<String,IdentifiedElement>();
			m_descriptions = new ArrayList<Description>();

			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new ContentHandler());
			reader.setErrorHandler(this);
			reader.parse(new InputSource(new StringReader(rdf)));
		}
		catch(IOException e)
		{
			return err(e.getMessage());
		}
		catch (SAXException e)
		{
			return err(e.getMessage());
		}
		
		if (m_isLastErr)
		{
			return m_lastErr;
		}
		
		parseDescriptions();
		
		if (m_isLastErr)
		{
			return err(m_lastErr);
		}

		json = new StringBuilder();
		json.append("{ \"info\":  ");	// info object
		json.append(JSONUtils.propertyListToJSON(m_infoMap));
		json.append(", \"entities\": "); // entities array
		json.append(m_entities.toJSON());
		json.append(", \"eventsAndFacts\": ");
		json.append(m_eventsAndFacts.toJSON());
		json.append(" }");
		
		return json.toString();
	}
	
	private void parseDescriptions() {
		
		Description desc 	= null;
		Attribute	attr	= null;
		Element		elem	= null;
		int			i		= 0;
		
		if (m_descriptions == null || m_descriptions.size() == 0)
		{
			err("Failed to parse RDF: Internal error");
			return;
		}
		
		Iterator<Description> iter = m_descriptions.iterator();
		
		/*
		 * First pass - parse info elements and entities (wait
		 * with event/facts and instances, as event/facts
		 * refer to entities and instances refer to event/facts)
		 */
		while (iter.hasNext())
		{
			desc = iter.next();
			
			switch(desc.getType())
			{
				case Description.TYPE_DOC_INFO:

					if (m_isLastErr)
					{
						return;
					}
					
					/*
					 * Set the description's ID
					 */
					m_infoMap.add(new Property("id", desc.getAbout()));
					
					/*
					 * Set the alternative ID from c:id
					 */
					attr = desc.getAttribute("c:id");
					if (attr == null)
					{
						err("Failed to parse RDF: missing c:id attribute in description");
						return;
					}
					
					m_infoMap.add(new Property("alt_id", attr.getValue()));
					
					/*
					 * Insert the rest of the c:x attributes
					 */
					for (i = 0; i < desc.getNumAttributes(); i++)
					{
						attr = desc.getAttribute(i);
						if (attr.getName().startsWith("c:") &&
								!attr.getName().equals("c:id"))
						{
							m_infoMap.add(new Property(attr.getName().substring(2),
									attr.getValue()));
						}
					}
					
					/*
					 * Handle the c:document element
					 */
					elem = desc.getChild("c:document");
					if (elem == null)
					{
						err("Failed to parse RDF: missing c:document element under DocInfo description");
						return;
					}
					
					m_infoMap.add(new Property("document", elem.getValueNotNull()));
					
					/*
					 * Handle the c:externalMetadata element
					 */
					elem = desc.getChild("c:externalMetadata");
					if (elem == null)
					{
						err("Failed to parse RDF: missing c:externalMetadata element under DocInfo description");
						return;
					}
					
					m_infoMap.add(new Property("externalMetadata", elem.getValueNotNull()));

					/*
					 * Handle the c:submitter element
					 */
					elem = desc.getChild("c:submitter");
					if (elem == null)
					{
						err("Failed to parse RDF: missing c:submitter element under DocInfo description");
						return;
					}
					
					m_infoMap.add(new Property("submitter", elem.getValueNotNull()));
					break;
					
				case Description.TYPE_DOC_INFO_META:

					if (m_isLastErr)
					{
						return;
					}

					/*
					 * Insert all c:x attributes
					 */
					for (i = 0; i < desc.getNumAttributes(); i++)
					{
						attr = desc.getAttribute(i);
						if (attr.getName().startsWith("c:"))
						{
							m_infoMap.add(new Property(attr.getName().substring(2),
									attr.getValue()));
						}
					}
					
					/*
					 * Handle the c:submitterCode element
					 */
					elem = desc.getChild("c:submitterCode");
					if (elem == null)
					{
						err("Failed to parse RDF: missing c:submitterCode element under DocInfoMeta description");
						return;
					}
					
					m_infoMap.add(new Property("submitterCode", elem.getValueNotNull()));

					/*
					 * Handle the c:signature element
					 */
					elem = desc.getChild("c:signature");
					if (elem == null)
					{
						err("Failed to parse RDF: missing c:signature element under DocInfoMeta description");
						return;
					}
					
					m_infoMap.add(new Property("signature", elem.getValueNotNull()));
					break;
					
				case Description.TYPE_DEFAULT_LANG_ID:

					if (m_isLastErr)
					{
						return;
					}
					
					/*
					 * Handle the c:lang element
					 */
					elem = desc.getChild("c:lang");
					if (elem == null)
					{
						err("Failed to parse RDF: missing c:lang element under DefaultLangId description");
						return;
					}

					attr = elem.getAttribute("rdf:resource");
					if (attr == null)
					{
						err("Failed to parse RDF: missing rdf:resource attribute in c:lang element under DefaultLangId description");
						return;
					}

					m_infoMap.add(new Property("lang", attr.getValue()));
					break;
					
				case Description.TYPE_ENTITY:
					parseEntity(desc);
					break;
					
				case Description.TYPE_EVENT_OR_FACT:
					/*
					 * Wait for 2nd pass
					 */
					break;
					
				case Description.TYPE_INSTANCE_INFO:
					/*
					 * Wait for 2nd pass
					 */
					break;
					
				case Description.TYPE_RELEVANCE_INFO:
					/*
					 * Wait for 2nd pass
					 */
					break;
					
				default:
					// no-op - unknown description type
					break;
			}
		}

		iter = m_descriptions.iterator();
		
		/*
		 * Second pass - parse event/facts and instance elements 
		 * now that all entities are in hash
		 */
		while (iter.hasNext())
		{
			desc = iter.next();
			
			switch(desc.getType())
			{
				case Description.TYPE_DOC_INFO:
					/*
					 * Done in 1st pass
					 */
					break;
					
				case Description.TYPE_DOC_INFO_META:
					/*
					 * Done in 1st pass
					 */
					break;
					
				case Description.TYPE_DEFAULT_LANG_ID:
					/*
					 * Done in 1st pass
					 */
					break;
					
				case Description.TYPE_ENTITY:
					/*
					 * Done in 1st pass
					 */
					break;
					
				case Description.TYPE_EVENT_OR_FACT:
					parseEventOrFact(desc);
					break;
					
				case Description.TYPE_INSTANCE_INFO:
					parseInstanceInfo(desc);
					break;
					
				case Description.TYPE_RELEVANCE_INFO:
					parseRelevanceInfo(desc);
					break;
					
				default:
					// no-op - unknown description type
					break;
			}
		}
	}
	
	/**
	 * Parse a description containing an entity identified by 
	 * Calais - add to m_entities
	 * @param desc - the description containing the entity
	 */
	private void parseEntity(Description desc) {
		
		Entity		entity	= null;
		Element		elem 	= null;
		
		if (m_isLastErr)
		{
			return;
		}
		
		entity = new Entity();
		entity.setType(StringUtils.getSubstringBySeparator(
				desc.getTypeStr(), "/", -1));
		entity.setId(desc.getAbout());

		m_elementsHash.put(entity.getId(), entity);
		
		/*
		 * Get the c:name element 
		 */
		elem = desc.getChild("c:name");
		if (elem == null)
		{
			err("Failed to parse RDF: entity of type " + entity.getType() +
					" with no c:name element");
			return;
		}
		
		if (elem.getValue() == null)
		{
			err("Failed to parse RDF: entity of type " + entity.getType() +
				" with invalid/empty c:name element");
			return;
		}
		
		entity.setName(elem.getValueNotNull().trim());
		
		/*
		 * Add to the entities object
		 */
		m_entities.addEntity(entity);
	}
	
	/**
	 * Parse a description containing an event/fact identified
	 * by Calais - add to m_eventsAndFacts
	 * @param desc - the description containing the event/fact
	 */
	private void parseEventOrFact(Description desc) {
		
		EventOrFact			eventOrFact	= null;
		Element				elem 		= null;
		Attribute			attr		= null;
		Entity				entity		= null;
		int					i			= 0;

		if (m_isLastErr)
		{
			return;
		}

		eventOrFact = new EventOrFact();
		eventOrFact.setType(StringUtils.getSubstringBySeparator(
				desc.getTypeStr(), "/", -1));
		eventOrFact.setId(desc.getAbout());

		m_elementsHash.put(eventOrFact.getId(), eventOrFact);
		
		/*
		 * Add all c:x elements 
		 */
		for (i = 0; i < desc.getNumChildren(); i++)
		{
			elem = desc.getChild(i);
			
			if (!elem.getTag().startsWith("c:"))
			{
				/*
				 * Only c:x elements
				 */
				continue;
			}
			
			attr = elem.getAttribute("rdf:resource");
			if (attr != null)
			{
				/*
				 * Value of this element indicated by an 
				 * rdf:resource attribute - get from hash
				 */
				entity = getEntityByID(attr.getValue());
				if (entity == null)
				{
					err("Failed to parse RDF: invalid reference to id " +
							attr.getValue() + 
							" in " + elem.getTag() + 
							" element of event/fact");
					return;
				}
				eventOrFact.addProperty(elem.getTag().substring(2),
						entity.getName());
			}
			else
			{
				/*
				 * Value of this element provided inside
				 * element - get value
				 */
				if (elem.getValue() == null)
				{
					err("Failed to parse RDF: no value for element " + 
							elem.getTag() + " in event/fact");
					return;
				}
				
				eventOrFact.addProperty(elem.getTag().substring(2),
						elem.getValueNotNull());
			}
		}
		
		/*
		 * Add to eventsAndFacts object
		 */
		m_eventsAndFacts.addEventOrFact(eventOrFact);
	}
	
	/**
	 * Return entity from the elements hash based on the provided
	 * ID. Element is returned only if it is an entity
	 * @param id - hash ID of entity to return
	 * @return - the found entity or null if not found
	 */
	public Entity getEntityByID(String id) {
		
		IdentifiedElement ielem = null;
		
		ielem = m_elementsHash.get(id);
		if (ielem != null && ielem instanceof Entity)
		{
			return (Entity)ielem;
		}
		
		return null;
	}
	
	/**
	 * Parse a description containing instance information 
	 * for an entity/event/fact identified by Calais - add to
	 * the appropriate element in m_entities or m_eventsAndFacts
	 * @param desc - the description containing the instance info
	 */
	private void parseInstanceInfo(Description desc) {
		
		Instance 			instance 	= null;
		Element				elem		= null;
		IdentifiedElement 	ielem 		= null;
		Attribute			attr		= null;
		int					i			= 0;
		
		if (m_isLastErr)
		{
			return;
		}

		instance = new Instance();
		
		/*
		 * Go over all description elements and extract text,
		 * offset, length. When c:subject is found (the id of 
		 * the element whose instance is described here) - 
		 * attach the new instance to the entity/event/fact
		 */
		for (i = 0; i < desc.getNumChildren(); i++)
		{
			elem = desc.getChild(i);
			
			if (elem.getTag().equals("c:subject"))
			{
				/*
				 * subject - the entity/event/fact whose instance
				 * is being processed here
				 */
				attr = elem.getAttribute("rdf:resource");
				if (attr == null)
				{
					err("Failed to parse RDF: c:subject element missing rdf:resource attribute");
					return;
				}
				
				ielem = m_elementsHash.get(attr.getValue());
				if (ielem == null)
				{
					err("Failed to parse RDF: InstanceInfo refers to id " + 
							attr.getValue() + " - not found in hash");
					return;
				}
				
				/*
				 * Add the instance to the identified element
				 */
				ielem.getInstances().addInstance(instance);
			}
			else if (elem.getTag().equals("c:detection"))
			{
				/*
				 * Add the detection value to the Instance
				 */
				if (elem.getValue() == null)
				{
					err("Failed to parse RDF: InstanceInfo with invalid detection");
					return;
				}
				
				instance.setText(elem.getValueNotNull().trim());
			}
			else if (elem.getTag().equals("c:offset"))
			{
				/*
				 * Add the offset value to the Instance
				 */
				if (elem.getValue() == null)
				{
					err("Failed to parse RDF: InstanceInfo with invalid offset");
					return;
				}
				
				instance.setOffset(Integer.parseInt(elem.getValue().trim()));
				
			}
			else if (elem.getTag().equals("c:length"))
			{
				/*
				 * Add the length value to the Instance
				 */
				if (elem.getValue() == null)
				{
					err("Failed to parse RDF: InstanceInfo with invalid length");
					return;
				}
				
				instance.setLength(Integer.parseInt(elem.getValue().trim()));
				
			}
		}
		
		/*
		 * Make sure all fields are there
		 */
		if (!instance.isValid())
		{
			err("Failed to parse RDF: InstanceInfo with missing fields");
			return;
		}
	}
	
	/**
	 * Parse a description containing relevance information 
	 * for an entity identified by Calais - add to
	 * the appropriate element in m_entities 
	 * @param desc - the description containing the relevance info
	 */
	private void parseRelevanceInfo(Description desc) {
		
		Element				elem		= null;
		IdentifiedElement 	ielem 		= null;
		float				relevance	= -1.0F;
		Attribute			attr		= null;
		int					i			= 0;
		
		if (m_isLastErr)
		{
			return;
		}

		/*
		 * Go over all description elements and extract relevance. 
		 * When c:subject is found (the id of 
		 * the element whose relevance is described here) - 
		 * set the relevance of the entity
		 */
		for (i = 0; i < desc.getNumChildren(); i++)
		{
			elem = desc.getChild(i);
			
			if (elem.getTag().equals("c:subject"))
			{
				/*
				 * subject - the entity whose instance
				 * is being processed here
				 */
				attr = elem.getAttribute("rdf:resource");
				if (attr == null)
				{
					err("Failed to parse RDF: c:subject element missing rdf:resource attribute");
					return;
				}
				
				ielem = m_elementsHash.get(attr.getValue());
				if (ielem == null)
				{
					err("Failed to parse RDF: RelevanceInfo refers to id " + 
							attr.getValue() + " - not found in hash");
					return;
				}
				
			}
			else if (elem.getTag().equals("c:relevance"))
			{
				/*
				 * Get the relevance value
				 */
				if (elem.getValue() == null)
				{
					err("Failed to parse RDF: RelevanceInfo with invalid relevance");
					return;
				}
				
				relevance = Float.parseFloat(elem.getValueNotNull().trim());
			}
		}
		
		/*
		 * Make sure all fields are there
		 */
		if (ielem == null || relevance == -1.0F)
		{
			err("Failed to parse RDF: RelevanceInfo with missing fields");
			return;
		}
		
		((Entity)ielem).setRelevance(relevance);
	}
	

	
	/**
	 * Parse error handler functions
	 */
	public void warning(SAXParseException e)
	{
		m_isLastErr = true;
		m_lastErr = "Failed to parse response: " + e.getMessage();
	}
	
	public void error(SAXParseException e) 
	{
		m_isLastErr = true;
		m_lastErr = "Failed to parse response: " + e.getMessage();
	}
	
	public void fatalError(SAXParseException e)
	{
		m_isLastErr = true;
		m_lastErr = "Failed to parse response: " + e.getMessage();
	}
	
	private String err(String err)
	{
		m_isLastErr = true;
		m_lastErr = err;
		return m_lastErr;
	}

	/*
	 * XML parsing of RDF Format
	 */
	public class ContentHandler extends DefaultHandler {

		public void startElement(
			String 		namespaceURI,
			String 		localName,
			String 		qName,
			Attributes 	attributes)
		{

			Attribute 	attr 	= null;
			int			i 		= 0;
			
			if (m_isLastErr)
			{
				return;
			}

			if (m_level == 0)
			{
				/*
				 * Level 0 - the string tag
				 */
				if (!qName.equals("string"))
				{
					err("Failed to parse RDF - root tag is not string - " + qName);
					return;		
				}
				
			}
			else if (m_level == 1)
			{
				/*
				 * Level 1 - the rdf:RDF tag
				 */
				if (!qName.equals("rdf:RDF"))
				{
					err("Failed to parse RDF - below root tag is not rdf:RDF - " + qName);
					return;		
				}
				
			}
			else if (m_level == 2)
			{
				/*
				 * Level 2 - rdf:Description 
				 */
				if (!qName.equals("rdf:Description")) 
				{
					err("Failed to parse RDF - level 2 tag is not rdf:Description - " + qName);
					return;		
				}
				
				m_currentDesc = new Description();
				
				for (i = 0; i < attributes.getLength(); i++)
				{
					if (attributes.getQName(i).equals("rdf:about"))
					{
						m_currentDesc.setAbout(attributes.getValue(i));
					}
					else
					{
						attr = new Attribute();
						attr.setName(attributes.getQName(i));
						attr.setValue(attributes.getValue(i));
						m_currentDesc.addAttribute(attr);
					}
				}
			}
			else if (m_level == 3)
			{
				/*
				 * Level 3 - Description elements
				 */
				if (qName.equals("rdf:type"))
				{
					/*
					 * Special handling for the type element
					 */
					if (attributes.getLength() != 1)
					{
						err("Failed to parse RDF - rdf:type has " + attributes.getLength() +
								" attributes - should be 1");
						return;		
					}
					
					if (!attributes.getQName(0).equals("rdf:resource"))
					{
						err("Failed to parse RDF - rdf:type attribute is not rdf:resource " + attributes.getQName(0));
						return;		
					}
					
					m_currentDesc.setType(attributes.getValue(0));
				}
				else
				{
					/*
					 * All other (non-type) elements
					 */
					m_currentElem = new Element();
					m_currentElem.setTag(qName);
					for (i = 0; i < attributes.getLength(); i++)
					{
						attr = new Attribute();
						attr.setName(attributes.getQName(i));
						attr.setValue(attributes.getValue(i));
						m_currentElem.addAttribute(attr);
					}
				}
			}
			else if (m_level > 3)
			{
				// no-op
			}
			
			m_level++;
	
		}

		public void characters(
			char[] ch,
			int start,
			int length)
		{
		
			if (m_isLastErr)
			{
				return;
			}  	
			
			String data = new String(ch,start,length);

			if (m_level >= 4 && m_currentElem != null)
			{
				m_currentElem.appendToValue(data.trim());
			}
		
		}

		public void endElement(
			String namespaceURI,
			String localName,
			String qName)
		{
			
			if (m_isLastErr)
			{
				return;
			}
			
			if (m_level == 0)
			{
				err("Failed to parse RDF - internal error");
				return;		
			}
			
			if (m_level == 1)
			{
				/*
				 * Level 0 - closing the string tag 
				 */
				if (!qName.equals("string"))
				{
					err("Failed to parse RDF - root closing tag is not string - " + qName);
					return;		
				}
				
			}
			else if (m_level == 2)
			{
				/*
				 * Level 1 - closing the rdf:RDF tag
				 */
				if (!qName.equals("rdf:RDF"))
				{
					err("Failed to parse RDF - below root closing tag is not rdf:RDF - " + qName);
					return;		
				}
			}
			else if (m_level == 3)
			{
				/*
				 * Level 2 - closing rdf:Description 
				 */
				if (!qName.equals("rdf:Description")) 
				{
					err("Failed to parse RDF - closing of level 2 tag is not rdf:Description - " + qName);
					return;		
				}

				m_descriptions.add(m_currentDesc);
				m_currentDesc = null;
			}
			else if (m_level == 4)
			{
				/*
				 * Level 3 - closing Description elements
				 */
				if (qName.equals("rdf:type"))
				{
					/*
					 * Special handling for the type element - no-op
					 */
				}
				else
				{
					/*
					 * All other (non-type) elements
					 */
					m_currentDesc.addChild(m_currentElem);
					m_currentElem = null;
				}
			}
			else if (m_level > 4)
			{
				/*
				 * Level 4+ - no-op
				 */
			}

			m_level--;
			
		}
		
	}

	public Entities getEntities() {
		return this.m_entities;
	}	
	
}

