package org.example.app.repository.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.domain.entity.customer.Customer;
import org.example.app.repository.IRepository;
import org.example.app.utils.logging.LoggingErrorsMsg;
import org.example.app.utils.logging.LoggingSuccessMsg;
import org.example.app.utils.utils_obj.QueryResult;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@Repository
public class CustomerRepository implements IRepository<Customer> {

    private static final Logger CRUD_LOGGER =
            LogManager.getLogger(CustomerRepository.class);
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public QueryResult<Customer> create(Customer obj) {
        Transaction transaction = null;
        QueryResult<Customer> queryResult = new QueryResult<>();
        boolean isEmailExists = isEmailExists(obj.getEmail());
        if (isEmailExists) {
            queryResult.addError(LoggingErrorsMsg.DB_EMAIL_EXISTS.getMsg());
            return queryResult;
        }
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(obj);
            session.refresh(obj);
            transaction.commit();
            queryResult.setSuccess(true);
            CONSOLE_LOGGER.info(String.format("%s %s%n", LoggingSuccessMsg.DB_ENTITY_ADDED.getMsg(), "Entity Customer" ));
            queryResult.setMsg("Employee created successfully");
            queryResult.setEntity(Collections.singletonList(obj));
            return queryResult;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            queryResult.addError(LoggingErrorsMsg.DB_INSERT_ERROR.getMsg());
            return queryResult;
        }
    }

    @Override
    public QueryResult<Customer> readAll() {
        Transaction transaction = null;
        QueryResult<Customer> queryResult = new QueryResult<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
            cq.from(Customer.class);
            Query<Customer> query = session.createQuery(cq);
            List<Customer> result = query.getResultList();
            transaction.commit();
            queryResult.setSuccess(true);
            if (result.isEmpty()) {
                queryResult.setMsg("No Customers found");
                return queryResult;
            }
            queryResult.setMsg("Customers found");
            queryResult.setEntity(result);
            return queryResult;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            queryResult.addError(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
            CRUD_LOGGER.error(e.getMessage(), e);
            return queryResult;
        }
    }

    @Override
    public QueryResult<Customer> readById(Long id) {
        Transaction transaction = null;
        QueryResult<Customer> queryResult = new QueryResult<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
            Root<Customer> root = cq.from(Customer.class);

            cq.where(cb.equal(root.get("id"), id));
            List <Customer> result = session.createQuery(cq).getResultList();
            transaction.commit();
            if (result.isEmpty()) {
                queryResult.setMsg("No Customer found with id: " + id);
                return queryResult;
            }
            queryResult.setSuccess(true);
            queryResult.setMsg("Customer found");
            queryResult.setEntity(result);
            return queryResult;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            queryResult.addError(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
            return queryResult;

        }
    }

    @Override
    public QueryResult<Customer> update(Customer obj) {
        QueryResult<Customer> queryResult = new QueryResult<>();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(obj);
            session.refresh(obj);
            transaction.commit();
            queryResult.setSuccess(true);
            CONSOLE_LOGGER.info(String.format("%s %s%n",LoggingSuccessMsg.DB_ENTITY_UPDATED.getMsg(), "Entity Customer" ));
            queryResult.setMsg(String.format("Customer %s update successfully", obj.getCustomerId()));
            queryResult.setEntity(Collections.singletonList(obj));
            return queryResult;
        } catch (HeadlessException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            queryResult.setMsg("Update error");
            queryResult.addError(LoggingErrorsMsg.DB_UPDATE_ERROR.getMsg());
            return queryResult;
        }
    }

    @Override
    public QueryResult<Customer> delete(Long id) {
        QueryResult<Customer> queryResult = new QueryResult<>();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Customer customer = session.get(Customer.class, id);
            if (customer != null) {
                session.remove(customer);
                transaction.commit();
                queryResult.setSuccess(true);
                CONSOLE_LOGGER.info(String.format("%s %s%n",LoggingSuccessMsg.DB_ENTITY_DELETED.getMsg(), "Entity Customer" ));
                queryResult.setMsg(String.format("User %s deleted successfully", customer.getCustomerId()));
                queryResult.setEntity(Collections.singletonList(customer));
            } else {
                queryResult.setMsg("Customer not found");
            }
            return queryResult;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            queryResult.addError(LoggingErrorsMsg.DB_DELETE_ERROR.getMsg());
            return queryResult;
        }
    }


    public boolean isEmailExists(String email) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
            Root<Customer> root = cr.from(Customer.class);
            cr.select(root).where(cb.equal(root.get("email"), email)).distinct(true);
            long resultCount = session.createQuery(cr).stream().count();
            transaction.commit();

            return resultCount > 0;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            throw new HibernateException(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
        }
    }

    public boolean isCustomerEmail(String email, Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
            Root<Customer> root = cr.from(Customer.class);
            cr.select(root).where(cb.and(cb.equal(root.get("email"), email), cb.equal(root.get("id"), id))).distinct(true);
            long resultCount = session.createQuery(cr).stream().count();
            transaction.commit();

            return resultCount > 0;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            throw new HibernateException(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
        }
    }


}
