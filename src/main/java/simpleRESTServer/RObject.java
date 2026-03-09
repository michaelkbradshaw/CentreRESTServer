package simpleRESTServer;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

public class RObject
{
	
	String id;
	JsonNode data;
	RClass parent;
	
	
	

	/**
	 * @param name
	 * @param id
	 * @param data
	 * @param parent
	 */
	public RObject(String id, JsonNode data, RClass parent)
	{
		//data.elements().
		
		((ObjectNode)data).put("id", id);
		
		this.id = id;
		this.data = data;
		this.parent = parent;
	}


	public String getURI()
	{
		return parent.getURI()+"/"+id;
	}
	

	public RDesc getRDesc()
	{
		return new RDesc(id,parent.name,getURI());
	}

}
