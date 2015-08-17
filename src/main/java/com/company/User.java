package com.company;

import javax.persistence.*;

/**
 * Created by lukoyanov on 8/17/15.
 */
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private long param;

    @Version
    private long version;

    public User() {
    }

    public User(String name, long param) {
        this.name = name;
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParam() {
        return param;
    }

    public void setParam(long param) {
        this.param = param;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
