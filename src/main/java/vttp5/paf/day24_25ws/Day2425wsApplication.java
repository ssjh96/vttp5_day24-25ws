package vttp5.paf.day24_25ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

import vttp5.paf.day24_25ws.service.OrderPollingService;

@SpringBootApplication
@EnableAsync
public class Day2425wsApplication implements CommandLineRunner {

	@Autowired
	@Qualifier("myredis")
	private RedisTemplate<String, String> template;

	@Autowired
	private OrderPollingService orderPollingService;

	public static void main(String[] args) {

		// Ensure application starts only if --name argument is provided

		String customerName = "";

		for (String arg : args)
		{
			if (arg.startsWith("--name"))
			{
				customerName = arg.split("=")[1];
				break;
			}
		}

		if (customerName.isBlank())
		{
			System.out.println("name argument cannot be blank/whitespaces");
			return; // Exit run method
		}

		System.out.println("Starting application for customer: " + customerName);

		// Set the customer name in system properties for later use in polling
		System.setProperty("customer.name", customerName); // This overrides the application.properties for the current runtime session		

		SpringApplication.run(Day2425wsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String customerName = System.getProperty("customer.name");

		// Register customer in Redis List
		template.opsForList().leftPush("registrations", customerName);
		System.out.println("Registered '" + customerName + "' in registraions list.");

		// Start polling for this customer
		orderPollingService.startPolling();
	}

}
