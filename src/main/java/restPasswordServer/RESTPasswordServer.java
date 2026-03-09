package restPasswordServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RESTPasswordServer extends SpringBootServletInitializer
{

	public static void main(String[] args)
	{
		new SpringApplicationBuilder(RESTPasswordServer.class)
        //.profiles("password")
		.profiles("random")
        .run(args);
		
		//I prefer the builder method above to this one.
		//System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "password");
		//SpringApplication.run(RESTPasswordServer.class, args);
		
		//can also set the run configuration for the VM -Dsspring.profiles.active=password
	}
	
	@Autowired
	private ServletWebServerApplicationContext serverContext;
	
	@Bean
    public ApplicationListener<ServletWebServerInitializedEvent> serverPortListenerBean() {
        return event -> {
            int serverPort = event.getWebServer().getPort();
            System.out.println("Port is "+serverPort);
            // TODO do something with the `serverPort`
        };
    }

	@GetMapping("/")
	public String hello()
	{
		return """	
<html>
<body>
<h1>Password server</h1>
<p>This is a REST server for retrieving and storing passwords.  Here are the following links to services provide on the server.</p>
<ol>
<li><a href="/request">/request"</a> a JSON list of all the people that have requested passwords.
<li><a href="/auth">/auth"</a> a JSON list of all the people that have successfully submitted their password
<li><a href="/request/janedoe">/request/:username"</a> A request for a password for the username provided.
<li><a href="/auth/janedoe/4132">/auth/:username/:password"</a> Attempts to authenticate the password for the username provided.
</ol>
</html>""";
	}
	
	
	HashSet<String> requesters = new HashSet<>();
	HashSet<String> auths = new HashSet<>();
	
	
	private String getPassword(String user)
	{
		return "" + Math.abs(user.hashCode()) % 10000;
	}
	
	
	@GetMapping("/request")
	public String getRequesters()
	{
		return "Requesters: "+requesters.toString();
	}
	
	@GetMapping("/server")
	public String getServer()
	{
		InetAddress ipAddr;
		try
		{
			ipAddr = InetAddress.getLocalHost();

			return "ServerData IP:  "+ ipAddr.getHostAddress() +
			"\n try two "+this.serverContext.getWebServer().getPort();

		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
		return "ServerData IP:  Unknown   "+
		"\n try two "+this.serverContext.getWebServer().getPort();
	}
	
	@GetMapping("/request/{username}")
	public String requestPassword(@PathVariable String username)
	{
		
		requesters.add(username);
		
		return getPassword(username);
	}

	@GetMapping("/auth")
	public String getAuths()
	{
		return "Authorized: "+auths.toString();
	}

	@GetMapping("/auth/{username}/{password}")
	public String checkPassword(@PathVariable String username,@PathVariable String password)
	{
		String pw = getPassword(username);
		
		if(pw.equals(password))
		{
			auths.add(username);
			return "Authentication Successful";
		}
		else
		{
			return "Authentication Failed";
		}
	}
	
	

}
