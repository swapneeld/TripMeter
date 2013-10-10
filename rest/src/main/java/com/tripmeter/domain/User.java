package com.tripmeter.domain;

import java.io.Serializable;

/**
 * Project: rest
 * User: Rajiv R. Nair
 * Created: 10/10/13 11:15 PM
 */
public class User implements Serializable {
    private Long id;
    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
