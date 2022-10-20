package com.pfi.crm.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.PfiApplication;

@RestController
@RequestMapping("/api/restart")
public class RestartController {
    
    @PostMapping("/restart")
    public void restart() {
        PfiApplication.restart();
    }
}
