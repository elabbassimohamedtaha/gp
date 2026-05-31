package service;

import entities.Medicament;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class MedicamentService {

    public void ajouter(Medicament m) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(m);
        tx.commit();
        session.close();
    }

    public List<Medicament> listerTous() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Medicament> liste = session.createQuery("from Medicament").list();
        session.close();
        return liste;
    }

    public void modifier(Medicament m) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(m);
        tx.commit();
        session.close();
    }

    public void supprimer(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Medicament m = (Medicament) session.get(Medicament.class, id);
        if (m != null) session.delete(m);
        tx.commit();
        session.close();
    }

    public List<Medicament> rechercherParNom(String nom) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Medicament> liste = session.createQuery(
            "from Medicament where lower(nom) like :nom")
            .setParameter("nom", "%" + nom.toLowerCase() + "%")
            .list();
        session.close();
        return liste;
    }

    public List<Medicament> stockFaible(int seuil) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Medicament> liste = session.createQuery(
            "from Medicament where quantite < :seuil")
            .setParameter("seuil", seuil)
            .list();
        session.close();
        return liste;
    }
}