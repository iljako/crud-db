package com.ilja.cruddb.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "writers")
public class Writer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    public Writer() {
    }

    public Writer(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getFirstName()               { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName()                { return lastName; }
    public void setLastName(String lastName)   { this.lastName = lastName; }

    public Set<Post> getPosts()                { return posts; }
    public void setPosts(Set<Post> posts)      { this.posts = posts; }

    public void addPost(Post post) {
        posts.add(post);
        post.setWriter(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setWriter(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Writer writer)) return false;
        return Objects.equals(id, writer.id) &&
                Objects.equals(firstName, writer.firstName) &&
                Objects.equals(lastName, writer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}