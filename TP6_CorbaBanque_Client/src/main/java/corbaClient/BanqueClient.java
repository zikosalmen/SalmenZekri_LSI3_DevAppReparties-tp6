package corbaClient;

import corbaBanque.Compte;
import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * ============================================================================
 * Faculté des Sciences de Sfax (FSS) - LSI 3
 * Matière : Développement d'Applications Réparties
 * TP 6 : Service Banque en CORBA
 * ============================================================================
 * 
 * Classe BanqueClient : Client CORBA invoquant les méthodes distantes du service.
 * 
 * Étapes du Client :
 * a. Récupérer les propriétés de l'annuaire JNDI via InitialContext.
 * b. Récupérer la référence distante de l'objet "Banque" (lookup).
 * c. Convertir la référence vers le type IBanqueRemote (narrow) pour créer le Stub.
 * d. Appeler à distance toutes les méthodes du service bancaire via le Stub.
 */
public class BanqueClient {

    public static void main(String[] args) {
        try {
            System.out.println("==========================================================");
            System.out.println("         DÉMARRAGE DU CLIENT CORBA BANQUE                 ");
            System.out.println("==========================================================");

            // a. Récupération des propriétés JNDI depuis jndi.properties
            System.out.println("[Client] Initialisation du contexte JNDI...");
            Context ctx = new InitialContext();

            // b. Recherche de l'objet distant dans l'annuaire CosNaming
            System.out.println("[Client] Recherche du service 'Banque' dans l'annuaire...");
            Object ref = ctx.lookup("Banque");

            // c. Transtypage (narrow) CORBA pour obtenir l'interface distante (Stub)
            System.out.println("[Client] Conversion de la référence CORBA (narrowing)...");
            IBanqueRemote stub = IBanqueRemoteHelper.narrow((org.omg.CORBA.Object) ref);
            System.out.println("[Client] Stub distant instancié avec succès !");

            System.out.println("\n----------------------------------------------------------");
            System.out.println("  EXÉCUTION DES OPÉRATIONS BANCAIRES À DISTANCE (CORBA)   ");
            System.out.println("----------------------------------------------------------");

            // 1. Création de comptes
            System.out.println("\n[Scénario 1] Création de trois comptes bancaires :");
            Compte cpte1 = new Compte(101, 1500.0f);
            Compte cpte2 = new Compte(102, 3200.5f);
            Compte cpte3 = new Compte(103, 850.0f);

            stub.creerCompte(cpte1);
            System.out.println("  -> Compte N° 101 créé avec solde initial : 1500.0 DT");
            stub.creerCompte(cpte2);
            System.out.println("  -> Compte N° 102 créé avec solde initial : 3200.5 DT");
            stub.creerCompte(cpte3);
            System.out.println("  -> Compte N° 103 créé avec solde initial : 850.0 DT");

            // 2. Consultation d'un compte
            System.out.println("\n[Scénario 2] Consultation du compte N° 101 :");
            Compte c = stub.getCompte(101);
            System.out.println("  -> Compte trouvé | Code: " + c.code + " | Solde: " + c.solde + " DT");

            // 3. Versement
            System.out.println("\n[Scénario 3] Opération de versement de 500.0 DT sur le compte N° 101 :");
            stub.verser(500.0f, 101);
            c = stub.getCompte(101);
            System.out.println("  -> Nouveau solde après versement : " + c.solde + " DT");

            // 4. Retrait
            System.out.println("\n[Scénario 4] Opération de retrait de 300.0 DT sur le compte N° 101 :");
            stub.retirer(300.0f, 101);
            c = stub.getCompte(101);
            System.out.println("  -> Nouveau solde après retrait : " + c.solde + " DT");

            // 5. Conversion Euro vers Dinar Tunisien
            System.out.println("\n[Scénario 5] Conversion de devise (150.0 € en Dinars Tunisiens) :");
            double montantDT = stub.conversion(150.0f);
            System.out.printf("  -> 150.00 EUR = %.2f DT (Taux appliqué : 3.35)%n", montantDT);

            // 6. Consultation de la liste globale de tous les comptes
            System.out.println("\n[Scénario 6] Consultation de l'ensemble des comptes (tabComptes) :");
            Compte[] tousLesComptes = stub.getComptes();
            System.out.println("  -> Nombre total de comptes récupérés : " + tousLesComptes.length);
            for (int i = 0; i < tousLesComptes.length; i++) {
                System.out.printf("     [%d] Code: %d | Solde: %.2f DT%n",
                        (i + 1), tousLesComptes[i].code, tousLesComptes[i].solde);
            }

            System.out.println("\n==========================================================");
            System.out.println("      VALIDATION : TOUTES LES MÉTHODES CORBA ONT RÉUSSI   ");
            System.out.println("==========================================================");

        } catch (Exception e) {
            System.err.println("[ERREUR CLIENT] Une exception est survenue :");
            e.printStackTrace();
        }
    }
}
