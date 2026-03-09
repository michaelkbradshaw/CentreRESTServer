package simpleRESTServer;

import java.util.ArrayList;
import java.util.HashMap;

import tools.jackson.databind.JsonNode;

public class RTeam
{

	
	String name;
	String description;
	String uri;
	//long nextId=1;
	
	
	HashMap<String,RClass> classes = new HashMap<>(); 
	//HashMap<String,RObject> objects = new HashMap<>();
	
	
	
	/**
	 * @param name
	 * @param description
	 */
	public RTeam(String name, String description,String uri)
	{
		super();
		this.name = name;
		this.description = description;
		this.uri = uri;
	}
	
	
	
	public String getURI()
	{
		return uri;
	}
	
	public RDesc getRDesc()
	{
		return new RDesc(name,description,uri);
	}
	
	/*public RClass getClass(String className)
	{
		return classes.get(className);
	}*/
	
	public ArrayList<RDesc> getClassDescriptions()
	{
		ArrayList<RDesc> descs = new ArrayList<>();
		
		for(RClass rClass:classes.values())
		{
			descs.add(rClass.getRDesc());
		}
		
		return descs;
		
	}
	
	
	/* CRUD for Classes */
	
	public RResponse createClass(String request,String className,String desc)
	{
		if(classes.containsKey(className))
		{
			return new RResponse(request,false,"Class "+className+" already exists");
		}
		
		RClass rClass = new RClass(className,desc,this);
		
		classes.put(className,rClass);
		
		return new RResponse(request,true,"Class "+className+" successfully created",rClass.getRDesc());

	}
	
	public RResponse readClass(String request,String className)
	{
		RClass rClass = classes.get(className);
		
		if(rClass == null)
		{
			return new RResponse(request,false,"Class "+className+" does not exist");
		}
		
		
		ArrayList<RDesc> descs = rClass.getDescs();
		
		return new RResponse(request,true,rClass.description,descs);
	}
	
	public RResponse updateClass(String request,String className,String newClassName, String newDesc)
	{
		RClass rClass = classes.get(className);
		
		if(rClass == null)
		{
			return new RResponse(request,false,"Class "+className+" does not exist");
		}
		
		
		if(!className.equals(newClassName) && classes.containsKey(newClassName))
		{
			return new RResponse(request,false,"Class "+newClassName+" already exists");
		}
		
		
		
		rClass.description=newDesc;
		rClass.name=newClassName;
		
		
		
		classes.remove(className);
		classes.put(newClassName,rClass);
		
		return new RResponse(request,true,"Class "+className+" has been updated",rClass.getRDesc());
	}
	
	
	
	public RResponse deleteClass(String request,String className)
	{
		RClass rClass = classes.get(className);
		
		if(rClass == null)
		{
			return new RResponse(request,false,"Class "+className+" does not exist");
		}
		
				
		classes.remove(className);
		
		return new RResponse(request,true,"Class "+className+" has been removed");
	}
	
	
	/* CRUD for Objects */
	
		
	public RResponse createObject(String request,String key,String className,JsonNode data)
	{
		RClass rClass = classes.get(className);
		
		if(rClass == null)
		{
			return new RResponse(request,false,"Class "+className+" does not exist");
		}
		
		if(rClass.objects.containsKey(key))
		{
			return new RResponse(request,false,"Object "+key+" already does not exists");
			
		}
		
			
		RObject obj = new RObject(key,data,rClass);
		
		rClass.objects.put(key,obj);
		
		return new RResponse(request,true,"Object "+key+" successfully created",
				obj.getRDesc());

	}
	
	public RResponse readObject(String request,String className, String objName)
	{
		
		RClass rClass = classes.get(className);
		
		if(rClass == null)
		{
			return new RResponse(request,false,"Class "+className+" does not exist");
		}
		
		RObject obj = rClass.objects.get(objName);
		
		if(obj == null)
		{
			return new RResponse(request,false,"Object "+objName+" does not exist");
		}
		
		String desc = obj.parent.description; 
		
		
		return new RResponse(request,true,desc,obj.data);
	}
	
	public RResponse updateObject(String request,String className,String objName, JsonNode data)
	{
		RClass rClass = classes.get(className);
		
		if(rClass == null)
		{
			return new RResponse(request,false,"Class "+className+" does not exist");
		}
		
		RObject obj = rClass.objects.get(objName);
		
		if(obj == null)
		{
			return new RResponse(request,false,"Object "+objName+" does not exist");
		}
		obj.data=data;
		
		return new RResponse(request,true,"Object "+objName+" has been updated");
	}
	
	
	
	public RResponse deleteObject(String request,String className, String objName)
	{
		RClass rClass = classes.get(className);
		
		if(rClass == null)
		{
			return new RResponse(request,false,"Class "+className+" does not exist");
		}
		
		RObject obj = rClass.objects.get(objName);
		
		if(obj == null)
		{
			return new RResponse(request,false,"Object "+objName+" does not exist");
		}
		
		obj.parent.objects.remove(objName);
		
		return new RResponse(request,true,"Object "+objName+" has been removed");
	}
	
	
	
	
	
	
	
	
	
	
	
}
