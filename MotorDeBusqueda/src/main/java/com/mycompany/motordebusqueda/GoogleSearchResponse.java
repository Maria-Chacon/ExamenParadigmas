/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motordebusqueda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleSearchResponse {

    private List<GoogleSearchItem> items;

    public List<GoogleSearchItem> getItems() {
        return items;
    }

    public void setItems(List<GoogleSearchItem> items) {
        this.items = items;
    }
}
