package service;

import corbaBanque.Compte;
import corbaBanque.IBanqueRemotePOA;

import java.util.ArrayList;
import java.util.List;

/**
 * Servant CORBA : implémentation concrète du service bancaire.
 * Hérite du squelette IBanqueRemotePOA généré par idlj.
 */
public class BanqueImpl extends IBanqueRemotePOA {


    /** La liste des comptes gérés par le serveur. */
    private final List<Compte> comptes = new ArrayList<>();

    /** Taux de conversion Euro -> Dinar Tunisien. */
    private static final double TAUX_EUR_DT = 3.35;

    @Override
    public void creerCompte(Compte cpte) {
        if (chercher(cpte.code) != null) {
            throw new org.omg.CORBA.BAD_PARAM("Le compte " + cpte.code + " existe déjà");
        }
        comptes.add(cpte);
        System.out.println("[Serveur] Compte créé : " + cpte.code + " (solde = " + cpte.solde + ")");
    }

    @Override
    public void verser(float mt, int code) {
        Compte c = trouver(code);
        if (mt <= 0) throw new org.omg.CORBA.BAD_PARAM("Montant invalide : " + mt);
        c.solde += mt;
        System.out.println("[Serveur] Versement de " + mt + " sur " + code + " -> solde = " + c.solde);
    }

    @Override
    public void retirer(float mt, int code) {
        Compte c = trouver(code);
        if (mt <= 0)      throw new org.omg.CORBA.BAD_PARAM("Montant invalide : " + mt);
        if (mt > c.solde) throw new org.omg.CORBA.BAD_PARAM("Solde insuffisant sur le compte " + code);
        c.solde -= mt;
        System.out.println("[Serveur] Retrait de " + mt + " sur " + code + " -> solde = " + c.solde);
    }

    @Override
    public Compte getCompte(int code) {
        return trouver(code);
    }

    @Override
    public Compte[] getComptes() {
        return comptes.toArray(new Compte[0]);
    }

    @Override
    public double conversion(float mt) {
        return mt * TAUX_EUR_DT;
    }

    /** Parcourt la liste et retourne le compte ayant ce code, ou null. */
    private Compte chercher(int code) {
        for (Compte c : comptes) {
            if (c.code == code) {
                return c;
            }
        }
        return null;
    }

    /** Retourne le compte ou lève une exception CORBA s'il n'existe pas. */
    private Compte trouver(int code) {
        Compte c = chercher(code);
        if (c == null) {
            throw new org.omg.CORBA.OBJECT_NOT_EXIST("Compte introuvable : " + code);
        }
        return c;
    }
}
