package com.sam.app;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.sam.scheduled.ScheduledTasks;
import com.sam.service.TwseStockService;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{
	
	@Autowired
	private TwseStockService twseStockService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("Spring Boot Startup");
		this.getStock();
	}
	
	private void getStock(){
		int retry = 10;
		int errorcount = 0;
		for(int i = 0 ; i < retry ; i++){
			try {
				ScheduledTasks.stocklist = twseStockService.getTwseStock();
				System.out.println("getStock Success");
				break;
			} catch (IOException e) {
				errorcount++;
				System.out.println("getStock Retry " + errorcount);
				e.printStackTrace();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		if(errorcount >= 10){
			System.exit(9);
		}
		
	}
}
