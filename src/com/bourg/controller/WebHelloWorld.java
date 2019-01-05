package com.bourg.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bourg.utils.*;

@Controller
@RequestMapping("/user")
public class WebHelloWorld {

	@GetMapping(value = "/index")
    public ModelAndView springMvcTest(ModelMap modelMap){
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** TESTING </h3>**********</div><br><br>";
		return new ModelAndView("welcome", "message", message);
    }
	
	@GetMapping("/welcome")
	public ModelAndView helloWorld() {

		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is coming from CrunchifyHelloWorld.java **********</div><br><br>";
		return new ModelAndView("welcome", "message", message);
	}
	
	@GetMapping("/main-menu")
	public String showPage() {
		return "main-menu";
	}
	
	@PostMapping("/upload")
	public ModelAndView uploadImage(@RequestParam("file") MultipartFile file) {
		
		URL url = null;
		try {
			
			File conv = convert(file);
			
			url = S3ReceiptUtils.loadReceiptToS3File("ambrosebourg.com","tester.jpg", conv);
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Upload Page "+ url.toString() +"</h3></div><br><br>";
		
		return new ModelAndView("welcome", "message", message);
	}
	
	
	public File convert(MultipartFile file) throws IOException
	{    
	    File convFile = new File(System.getProperty("java.io.tmpdir")+"/" + file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}
	
}


