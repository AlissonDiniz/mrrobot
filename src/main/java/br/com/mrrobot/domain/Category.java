/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrrobot.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Id;

/**
 *
 * @author Alisson Diniz
 */
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "category_text", joinColumns = {
        @JoinColumn(name = "category_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "text_id", referencedColumnName = "id")})
    private List<Text> textList;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Text> getTextList() {
        if(this.textList == null){
            this.textList = new ArrayList<>();
        }
        return textList;
    }

    public void setTextList(List<Text> textList) {
        this.textList = textList;
    }

}
