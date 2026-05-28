package com.ilja.cruddb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts = new ArrayList<>();

    public Writer() {
    }

    public Writer(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Writer)) return false;
        Writer writer = (Writer) o;
        return Objects.equals(id, writer.id) &&
                Objects.equals(firstName, writer.firstName) &&
                Objects.equals(lastName, writer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}