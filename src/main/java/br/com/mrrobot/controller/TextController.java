/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrrobot.controller;

import br.com.mrrobot.to.CategoryTO;
import br.com.mrrobot.service.TextClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alisson Diniz
 */
@RestController
public class TextController {
    
    @Autowired
    private TextClassificationService service;
    
    @RequestMapping("text/classify")
    public ResponseEntity<CategoryTO> classify(@RequestParam(value = "text") String text) {
        return ResponseEntity.ok(this.service.classify(text));
    }
    
    @RequestMapping("text/learn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void learn(@RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "text") String text) {
        this.service.learn(categoryName, text);
    }
    
    @RequestMapping("text/applyCategory")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void applyCategory(@RequestParam(value = "categoryId") long categoryId, @RequestParam(value = "text") String text) {
        this.service.applyCategory(categoryId, text);
    }
    
}
