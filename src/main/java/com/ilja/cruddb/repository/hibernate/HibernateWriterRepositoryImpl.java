package com.ilja.cruddb.repository.hibernate;

import com.ilja.cruddb.config.HibernateConfig;
import com.ilja.cruddb.model.Writer;
import com.ilja.cruddb.repository.WriterRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class HibernateWriterRepositoryImpl implements WriterRepository {

    public static final String FIND_BY_ID = "SELECT DISTINCT w FROM Writer w " +
            "LEFT JOIN FETCH w.posts p " +
            "LEFT JOIN FETCH p.labels " +
            "WHERE w.id = :id";
    public static final String FIND_ALL = "SELECT DISTINCT w FROM Writer w " +
            "LEFT JOIN FETCH w.posts p " +
            "LEFT JOIN FETCH p.labels " +
            "ORDER BY w.lastName, w.firstName";

    private Session getCurrentSession() {
        return HibernateConfig.getSessionFactory().openSession();
    }

    @Override
    public Writer save(Writer writer) {
        Transaction tx = null;
        try (Session session = getCurrentSession()) {
            tx = session.beginTransaction();
            Writer result = (Writer) session.merge(writer);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при сохранении Writer", e);
        }
    }

    @Override
    public Optional<Writer> findById(Long id) {
        try (Session session = getCurrentSession()) {
            Writer writer = session.createQuery(FIND_BY_ID, Writer.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(writer);
        }
    }

    @Override
    public List<Writer> findAll() {
        try (Session session = getCurrentSession()) {
            return session.createQuery(FIND_ALL, Writer.class).list();
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction tx = null;
        try (Session session = getCurrentSession()) {
            tx = session.beginTransaction();
            Writer writer = session.get(Writer.class, id);
            if (writer != null) {
                session.remove(writer);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при удалении Writer id=" + id, e);
        }
    }
}
