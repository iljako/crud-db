package com.ilja.cruddb.repository.hibernate;

import com.ilja.cruddb.config.HibernateConfig;
import com.ilja.cruddb.model.Label;
import com.ilja.cruddb.repository.LabelRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class HibernateLabelRepositoryImpl implements LabelRepository {

    public static final String FIND_BY_NAME = "FROM Label WHERE name = :name";
    public static final String FIND_ALL = "FROM Label";

    private Session getCurrentSession() {
        return HibernateConfig.getSessionFactory().openSession();
    }

    @Override
    public Label save(Label label) {
        Transaction tx = null;
        try (Session session = getCurrentSession()) {
            tx = session.beginTransaction();
            Label result = (Label) session.merge(label);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при сохранении Label", e);
        }
    }

    @Override
    public Optional<Label> findById(Long id) {
        try (Session session = getCurrentSession()) {
            return Optional.ofNullable(session.get(Label.class, id));
        }
    }

    @Override
    public List<Label> findAll() {
        try (Session session = getCurrentSession()) {
            return session.createQuery(FIND_ALL, Label.class).list();
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction tx = null;
        try (Session session = getCurrentSession()) {
            tx = session.beginTransaction();
            Label label = session.get(Label.class, id);
            if (label != null) {
                session.remove(label);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка при удалении Label id=" + id, e);
        }
    }

    @Override
    public Optional<Label> findByName(String name) {
        try (Session session = getCurrentSession()) {
            return session.createQuery(FIND_BY_NAME, Label.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        }
    }
}
