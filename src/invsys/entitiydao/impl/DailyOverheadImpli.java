/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invsys.entitiydao.impl;

import invsys.controllers.database.HibernateUtil;
import invsys.controllers.formvalidation.AlertHandler;
import invsys.entities.OverheadCategory;
import invsys.entities.compositkeys.DailyOverheadId;
import invsys.entitiydao.DailyOverhead;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Kanishka
 */
public class DailyOverheadImpli implements DailyOverhead{
    
    private static Session session;
    private static DailyOverheadImpli daoHandler;
    
    
    public static DailyOverheadImpli getDao() {
        if (daoHandler == null) {
            daoHandler = new DailyOverheadImpli();
        }
        return daoHandler;
    }


    @Override
    public boolean saveDailyOverhead(invsys.entities.DailyOverhead overhead) {
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(overhead);
            session.getTransaction().commit();
            return true;

        } catch (Exception e) {
            AlertHandler.getAlert(Alert.AlertType.ERROR, "Cannot create entry", e.getLocalizedMessage());
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public invsys.entities.DailyOverhead getDailyOverheadById(DailyOverheadId id) {
          invsys.entities.DailyOverhead dailyOverhead = null;
        try {

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            dailyOverhead = session.get(invsys.entities.DailyOverhead.class, id);
            session.getTransaction().commit();

        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "Details Could not be loaded", e.getLocalizedMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        }

        return dailyOverhead;
    }

    @Override
    public boolean updateDailyOverhead(invsys.entities.DailyOverhead overhead) {
        try {

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            invsys.entities.DailyOverhead overHeadTobeUpdated = session.get(invsys.entities.DailyOverhead.class, overhead.getDailyOverheadId());
            overHeadTobeUpdated.setAmount(overhead.getAmount());
            overHeadTobeUpdated.setDescription(overhead.getDescription());
            overHeadTobeUpdated.setUser(overhead.getUser());
            overHeadTobeUpdated.getDailyOverheadId().setDateOfEntry(overhead.getDailyOverheadId().getDateOfEntry());
            session.update(overHeadTobeUpdated);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            AlertHandler.getAlert(AlertType.ERROR, "Details Could not be updated", e.getLocalizedMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }

    }

    @Override
    public OverheadCategory getOverheadCategoryByName(String overhead) {
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<OverheadCategory> criteria = builder.createQuery(OverheadCategory.class);
            Root<OverheadCategory> root = criteria.from(OverheadCategory.class);
            criteria.where(builder.equal(root.get("overheadCategory"), overhead));
            OverheadCategory overheadCat = session.createQuery(criteria).getSingleResult();
            session.getTransaction().commit();
            return overheadCat;

        } catch (Exception e) {
            AlertHandler.getAlert(Alert.AlertType.ERROR, "Cannot Create Category", e.getLocalizedMessage());
            session.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ObservableList<String> getOverheadCatNames() {
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<String> criteria = builder.createQuery(String.class);
            Root<OverheadCategory> root = criteria.from(OverheadCategory.class);
            criteria.select(root.get("overheadCategory"));
            Query<String> query = session.createQuery(criteria);
            ObservableList<String> catNames = FXCollections.observableArrayList(query.getResultList());
            session.getTransaction().commit();
            return catNames;
        } catch (Exception e) {
            AlertHandler.getAlert(Alert.AlertType.ERROR, "Cannot Load Overhead Categories", e.getLocalizedMessage());
            session.getTransaction().rollback();
            return null;

        }
    }

}
