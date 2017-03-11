/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrrobot.controller;

import br.com.mrrobot.controller.to.TextClassificationRequest;
import br.com.mrrobot.to.CategoryTO;
import br.com.mrrobot.service.TextClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alisson Diniz
 */
@RestController
public class TextClassificationController {
    
    @Autowired
    private TextClassificationService service;
    
    @RequestMapping("textClassification/classify")
    public ResponseEntity<CategoryTO> classify(@RequestBody TextClassificationRequest request) {
        return ResponseEntity.ok(this.service.classify(request.getText()));
    }
    
    @RequestMapping("textClassification/learn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void learn(@RequestBody TextClassificationRequest request) {
        this.service.learn(request.getCategoryName(), request.getText());
    }
    
    @RequestMapping("textClassification/applyCategory")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void applyCategory(@RequestBody TextClassificationRequest request) {
        this.service.applyCategory(request.getCategoryId(), request.getText());
    }
    
}
