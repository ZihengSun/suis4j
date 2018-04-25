package org.suis4j;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;

import java.util.regex.*;


public class MessageDataTree
{
	// this is the root element of an XML doc that contains our message parameters tree
	private Element root;

	private String INDEX_KEY_RE = ".*\\[(\\d+)]$"; // element[0]
	private String INDEX_STRIP_RE = "\\[(\\d+)]$"; // [0]

	public MessageDataTree()
	{
		Document doc = DocumentHelper.createDocument();
		root = doc.addElement("__suismessage__");
	}

	// set value from string
	public void setValue(String key, String value)
	{
		Element node = findOrCreateElement(key);
		node.setText(value);

	}

	// get value string
	public String getValue(String key)
	{
		Element node = findElement(key);

		if(node != null)
			return (node.getText());

		return "";
	}

	// Example: values("img format", ["jpeg", "png", "tiff"]
	// Produces: <img><format>jpeg</format><format>png</format><format>tiff</format></img>
	public void setValuesList(String key, List<String> values)
	{
		String ancestorsKey = extractAncestorsKey(key); // Ex: "img"
		String leafKey = extractLeafKey(key); // Ex: "format"

		Element container = findOrCreateElement(ancestorsKey);

		if(container != null)
		{
			for (String v : values)
			{
				container.addElement(leafKey).setText((v));
			}
		}
	}

	// get a list of values
	public List<String> getValuesList(String key)
	{
		List<String> values = new ArrayList<String>();

		String ancestorsKey = extractAncestorsKey(key);
		String leafKey = extractLeafKey(key);

		Element container = findElement(ancestorsKey);
		if(container != null)
		{
			List children = container.elements(leafKey);

			for(Object c : children)
			{
				Element e = (Element)c;
				values.add(e.getText());
			}
		}

		return(values);
	}

	// get a list of keys for nested keys
	public List<String> keys(String key)
	{
		Set<String> keys = new HashSet<String>();
		Element node = findElement(key);

		if(node != null)
		{
			List children = node.elements();
			for (Object i : children)
			{
				Element e = (Element)i;
				keys.add(e.getName());
			}
		}

		return(Lists.newArrayList(keys));
	}

	private Element findElement(String key)
	{
		key = key.trim();
		if(key.equals(""))
			return root;

		String ancestorsKey = extractAncestorsKey(key);
		String leafKey = extractLeafKey(key);

		Element parent = findElement(ancestorsKey);

		if(parent == null)
			return null;

		// handle keys that are like arrays, for example: "formats[0]"
		if(hasKeyIndex(leafKey))
		{
			int leafIndex = extractKeyIndex(leafKey);
			leafKey = removeKeyIndex(leafKey);

			List elements = parent.elements(leafKey);
			if(elements.size() > leafIndex)
			{
				return (Element)elements.get(leafIndex);
			}
			else
			{
				// index out of bounds
				return null;
			}
		}

		return parent.element(leafKey);
	}

	private Element findOrCreateElement(String key)
	{
		key = key.trim();

		Element node = findElement(key);
		if(node == null)
		{
			String ancestorsKey = extractAncestorsKey(key);
			String leafKey = extractLeafKey(key);

			Element parent = findOrCreateElement(ancestorsKey);

			node = createElement(parent, leafKey);
		}

		return node;
	}

	// Note: creating an element with index N, will create elements with index 0..N-1 if they don't exist
	private Element createElement(Element parent, String leafKey)
	{
		// handle keys that are like arrays, for example: "formats[10]"
		// will create formats[0], formats[1]...formats[9]
		if(hasKeyIndex(leafKey))
		{
			int leafIndex = extractKeyIndex(leafKey);
			leafKey = removeKeyIndex(leafKey);

			// add missing elements
			while(parent.elements(leafKey).size() < leafIndex)
				parent.addElement(leafKey);
		}

		return parent.addElement(leafKey);
	}

	private String extractLeafKey(String key)
	{
		String[] parts = key.trim().split("\\s+");

		return parts[parts.length - 1];
	}

	private String extractAncestorsKey(String key)
	{
		String[] parts = key.trim().split("\\s+");
		String[] ancestorParts = Arrays.copyOfRange(parts, 0, parts.length - 1);

		return StringUtils.join(ancestorParts, " ");
	}

	// Example: "element[10]" -> 10
	// Example: "element" -> -1
	private int extractKeyIndex(String key)
	{
		Pattern re = Pattern.compile(INDEX_KEY_RE);
		Matcher m = re.matcher(key);
		if(m.find())
			return Integer.parseInt(m.group(1));

		return -1;
	}

	private boolean hasKeyIndex(String key)
	{
		return key.matches(INDEX_KEY_RE);
	}

	private String removeKeyIndex(String key)
	{
		return key.replaceFirst(INDEX_STRIP_RE, "");
	}

}