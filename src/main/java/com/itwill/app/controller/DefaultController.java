package com.itwill.app.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DefaultController {
    private int status = HttpStatus.OK.value();
    
    @Value(value = "${application.version}")
    private String applicationVersion;

    @Value(value = "${spring.profiles.active}")
    private String applicationProfile;
    
    @GetMapping(value = {"hello",""})
    public String welcome(){
        //return "Welcome  App ["+ applicationVersion +"]\n";
        //return "안녕  App ["+ applicationVersion +"]\n";
        //return "Bye  App ["+ applicationVersion +"]\n";
        //return "Hello  App ["+ applicationVersion +"]\n";
        return "Good Job  App ["+ applicationVersion +"]\n";
    }

    @GetMapping("hostname")
    public String hostname() {
        return "Hostname: " + getHostName() + "\n";
    }

    @GetMapping("favicon")
    public String favicon() {
        return "favicon";
    }
    @GetMapping("profile")
    public String getApplicationProfile() {

        return applicationProfile+"\n";
    }

    @GetMapping("version")
    public String getVersion() {

        return applicationVersion+"\n";
    }

    @GetMapping(value = { "ingress1", "ingress2" })
    public String ingress() {
        return "Hostname: " + getHostName() + "\n";
    }

    @GetMapping("health")
    public ResponseEntity<String> healthCheck() {
        if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return new ResponseEntity<>(getHostName()+" :Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(getHostName()+" is Running");
    }

    @PostMapping("status/{statusCode}")
    public String changeStatus(@PathVariable("statusCode") int statusCode) {
        status = statusCode;
        return "Status Code has Changed to " + status;
    }

    @GetMapping("sleep/{duration}")
    public String sleep(@PathVariable(name = "duration") int duration) throws InterruptedException {
        TimeUnit.SECONDS.sleep(duration);
        return "Slept for " + duration + " seconds.";
    }

    @PostMapping("termination")
    public void terminate() {
    	System.out.println(">>>> System.exit(1);");
        System.exit(1);
    }

    @GetMapping("**")
    public String undefinedPath() {
        return "No defined Path. [ deploy_app ]";
    }

    private String getHostName() {
        String hostname = "";
        InetAddress inetadd = null;
        try {
            inetadd = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        hostname = inetadd.getHostName();
        return hostname;
    }
}
