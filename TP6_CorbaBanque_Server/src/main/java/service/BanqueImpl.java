package service;

import corbaBanque.Compte;
import corbaBanque.IBanqueRemotePOA;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * Servant CORBA : Implémentation concrète du service bancaire distant.
 * Hérite du squelette IBanqueRemotePOA généré par le compilateur idlj.
 * ============================================================================
 */
public class BanqueImpl extends IBanqueRemotePOA {

    /** Liste en mémoire des comptes bancaires gérés par le serveur */
    private final List<Compte> comptes = new ArrayList<Compte>();

    /** Taux de conversion indicatif : 1 Euro (€) = 3.35 Dinars Tunisiens (DT) */
    private static final double TAUX_EUR_DT = 3.35;

    public BanqueImpl() {
        super();
        System.out.println("[BanqueImpl] Servant CORBA Banque initialisé avec succès.");
    }

    @Override
    public void creerCompte(Compte cpte) {
        if (cpte == null) {
            System.err.println("[Serveur] Tentative de création avec un compte null !");
            throw new org.omg.CORBA.BAD_PARAM("Le compte fourni est null");
        }
        if (chercher(cpte.code) != null) {
            System.err.println("[Serveur] Le compte N° " + cpte.code + " existe déjà !");
            throw new org.omg.CORBA.BAD_PARAM("Le compte " + cpte.code + " existe déjà");
        }
        comptes.add(cpte);
        System.out.println("[Serveur] Compte créé : N° " + cpte.code + " | Solde initial = " + cpte.solde + " DT");
    }

    @Override
    public void verser(float mt, int code) {
        Compte c = trouver(code);
        if (mt <= 0) {
            System.err.println("[Serveur] Montant de versement invalide : " + mt);
            throw new org.omg.CORBA.BAD_PARAM("Montant invalide : " + mt);
        }
        c.solde += mt;
        System.out.println("[Serveur] Versement de " + mt + " DT sur compte N° " + code + " -> Nouveau solde = " + c.solde + " DT");
    }

    @Override
    public void retirer(float mt, int code) {
        Compte c = trouver(code);
        if (mt <= 0) {
            System.err.println("[Serveur] Montant de retrait invalide : " + mt);
            throw new org.omg.CORBA.BAD_PARAM("Montant invalide : " + mt);
        }
        if (mt > c.solde) {
            System.err.println("[Serveur] Solde insuffisant sur compte N° " + code + " (Solde: " + c.solde + ", Retrait: " + mt + ")");
            throw new org.omg.CORBA.BAD_PARAM("Solde insuffisant sur le compte " + code);
        }
        c.solde -= mt;
        System.out.println("[Serveur] Retrait de " + mt + " DT sur compte N° " + code + " -> Nouveau solde = " + c.solde + " DT");
    }

    @Override
    public Compte getCompte(int code) {
        Compte c = trouver(code);
        System.out.println("[Serveur] Consultation du compte N° " + code + " -> Solde = " + c.solde + " DT");
        return c;
    }

    @Override
    public Compte[] getComptes() {
        System.out.println("[Serveur] Consultation de tous les comptes (" + comptes.size() + " compte(s) au total).");
        return comptes.toArray(new Compte[0]);
    }

    @Override
    public double conversion(float mt) {
        double resultat = mt * TAUX_EUR_DT;
        System.out.println("[Serveur] Conversion devises : " + mt + " € -> " + String.format("%.2f", resultat) + " DT");
        return resultat;
    }

    /** Parcourt la liste et retourne le compte ayant ce code, ou null s'il n'existe pas. */
    private Compte chercher(int code) {
        for (Compte c : comptes) {
            if (c.code == code) {
                return c;
            }
        }
        return null;
    }

    /** Retourne le compte ou lève une exception CORBA si introuvable. */
    private Compte trouver(int code) {
        Compte c = chercher(code);
        if (c == null) {
            System.err.println("[Serveur] Compte introuvable : N° " + code);
            throw new org.omg.CORBA.OBJECT_NOT_EXIST("Compte introuvable : " + code);
        }
        return c;
    }
}
