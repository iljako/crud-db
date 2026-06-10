package com.ilja.cruddb.repository.hibernate;

import com.ilja.cruddb.config.HibernateConfig;
import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.repository.PostRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class HibernatePostRepositoryImpl implements PostRepository {

    public static final String FIND_BY_ID = "SELECT p FROM Post p LEFT JOIN FETCH p.labels WHERE p.id = :id";
    public static final String FIND_ALL = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.labels ORDER BY p.created DESC";
    public static final String FIND_BY_WRITER_ID = "SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.labels " +
            "WHERE p.writer.id = :writerId ORDER BY p.created DESC";

    private Session getCurrentSession() {
        return HibernateConfig.getSessionFactory().openSession();
    }

    @Override
    public Post save(Post post) {
        Transaction tx = null;
        try (Session session = getCurrentSession()) {
            tx = session.beginTransaction();
            Post result = (Post) session.merge(post);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при сохранении Post", e);
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        try (Session session = getCurrentSession()) {
            Post post = session.createQuery(FIND_BY_ID, Post.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(post);
        }
    }

    @Override
    public List<Post> findAll() {
        try (Session session = getCurrentSession()) {
            return session.createQuery(FIND_ALL, Post.class).list();
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction tx = null;
        try (Session session = getCurrentSession()) {
            tx = session.beginTransaction();
            Post post = session.get(Post.class, id);
            if (post != null) {
                session.remove(post);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при удалении Post id=" + id, e);
        }
    }

    @Override
    public List<Post> findByWriterId(Long writerId) {
        try (Session session = getCurrentSession()) {
            return session.createQuery(FIND_BY_WRITER_ID, Post.class)
                    .setParameter("writerId", writerId)
                    .list();
        }
    }
}
