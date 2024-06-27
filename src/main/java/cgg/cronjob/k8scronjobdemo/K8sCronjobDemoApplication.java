package cgg.cronjob.k8scronjobdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import cgg.cronjob.k8scronjobdemo.job.ReportGenerationScheduler;

@SpringBootApplication
@EnableScheduling
public class K8sCronjobDemoApplication implements CommandLineRunner {

	@Autowired
	private ReportGenerationScheduler scheduler;

	public static void main(String[] args) {
		SpringApplication.run(K8sCronjobDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		scheduler.generateReportAndSendEmail();
	}

}
