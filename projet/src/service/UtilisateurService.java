package service;

import entities.Utilisateur;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class UtilisateurService {

    public Utilisateur login(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Utilisateur u = null;
        try {
            u = (Utilisateur) session.createQuery(
                "from Utilisateur where username=:u and password=:p")
                .setParameter("u", username)
                .setParameter("p", password)
                .uniqueResult();
        } finally {
            session.close();
        }
        return u;
    }

    public List<Utilisateur> listerEmployes() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Utilisateur> liste = session.createQuery(
            "from Utilisateur where role='EMPLOYE'").list();
        session.close();
        return liste;
    }

    public void ajouter(Utilisateur u) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(u);
        tx.commit();
        session.close();
    }

    public void supprimer(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Utilisateur u = (Utilisateur) session.get(Utilisateur.class, id);
        if (u != null) session.delete(u);
        tx.commit();
        session.close();
    }
}