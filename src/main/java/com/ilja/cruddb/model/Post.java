package com.ilja.cruddb.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "post_status")
    private PostStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Writer writer;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "post_labels",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labels = new HashSet<>();

    public Post() {
    }

    public Post(Long id, String content, PostStatus status) {
        this.id = id;
        this.content = content;
        this.status = status;
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getContent()                 { return content; }
    public void setContent(String content)     { this.content = content; }

    public LocalDateTime getCreated()          { return created; }
    public void setCreated(LocalDateTime c)    { this.created = c; }

    public LocalDateTime getUpdated()          { return updated; }
    public void setUpdated(LocalDateTime u)    { this.updated = u; }

    public PostStatus getStatus()              { return status; }
    public void setStatus(PostStatus status)   { this.status = status; }

    public Writer getWriter()                  { return writer; }
    public void setWriter(Writer writer)       { this.writer = writer; }

    public Set<Label> getLabels()              { return labels; }
    public void setLabels(Set<Label> labels)   { this.labels = labels; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(id, post.id) && Objects.equals(content, post.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }
}