/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrrobot.to;

/**
 *
 * @author Alisson Diniz
 */
public class CategoryTO {

    private final long id;
    private final String name;

    public CategoryTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

}
