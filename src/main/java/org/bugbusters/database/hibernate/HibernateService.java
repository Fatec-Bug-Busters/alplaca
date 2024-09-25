package org.bugbusters.database.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.lang.reflect.Field;
import java.util.List;


public class HibernateService {
    private static  Session session ;

    private HibernateService() {

    }

    public  static void insertValue(Object objectEntity) {
        Transaction transaction = null;

        try {

           int idObject = getIdObject(objectEntity);
            if (!(idObject == 0)) {
                throw new Exception("The entity already has an ID. Insert is not allowed.");
            }

            transaction = session.beginTransaction();

            // Save the object entity
            session.save(objectEntity);

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static <T> T findById(int id, Class<T> entityClass) {
        Transaction transaction = null;
        T entity = null;
        try {
            transaction = session.beginTransaction();
            entity = session.get(entityClass, id);
            transaction.rollback();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entity;
    }


    public static List findByCondition(String nameTable,String condition, Class entity) {
        Transaction transaction = null;;
        List results = null;
        try {
            transaction = session.beginTransaction();

            String sql = String.format("SELECT * FROM %s WHERE %s", nameTable, condition);
            NativeQuery query = session.createNativeQuery(sql, entity);
            results = query.getResultList();
            transaction.rollback();



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return results;
    }


    public static <T> void updateValue(T entity){
        Transaction transaction = null;
        int idObject = getIdObject(entity);

        try {
            if (findById(idObject, entity.getClass()) == null) {
                throw new RuntimeException(" ID Not Found");
            }

            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        }catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static <T> void deleteValue(T entity){
        Transaction transaction = null;
        int idObject = getIdObject(entity);

        try {
            if (findById(idObject, entity.getClass()) == null) {
                throw new RuntimeException("Invalid ID");
            }
           transaction = session.beginTransaction();
           session.delete(entity);
           transaction.commit();
        }catch (Exception e){

            if (transaction != null) {
                    transaction.rollback();
            }

            e.printStackTrace();
        }
    }


    public static void openSession() {
        session = HibernateFactory.getSessionFactory().openSession();
    }

    public static void closeSession() {
        session.close();
    }

    private static int getIdObject(Object objectEntity) {
        try {
            Field idField = objectEntity.getClass().getDeclaredField("id");
            idField.setAccessible(true);

            Object idValue = idField.get(objectEntity);
            return (int)idValue;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static <T> T findByConditionObject(String nameTable,String condition, Class entity) {
        Transaction transaction = null;;
        T results = null;
        try {
            transaction = session.beginTransaction();

            String sql = String.format("SELECT * FROM %s WHERE %s", nameTable, condition);
            NativeQuery query = session.createNativeQuery(sql, entity);
            results = (T) query.getSingleResult();
            transaction.rollback();



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return results;
    }

}

