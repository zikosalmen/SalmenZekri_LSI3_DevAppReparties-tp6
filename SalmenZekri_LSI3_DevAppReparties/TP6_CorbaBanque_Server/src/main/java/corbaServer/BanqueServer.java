package corbaServer;

import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import service.BanqueImpl;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * ============================================================================
 * Faculté des Sciences de Sfax (FSS) - LSI 3
 * Matière : Développement d'Applications Réparties
 * TP 6 : Service Banque en CORBA
 * ============================================================================
 * 
 * Classe BanqueServer : Serveur d'annuaire et d'hébergement du service CORBA.
 * 
 * Étapes d'exécution :
 * a. Initialisation de l'ORB.
 * b. Récupération et activation de l'adaptateur d'objets (RootPOA / POAManager).
 * c. Instanciation du servant métier (BanqueImpl).
 * d. Enregistrement de la référence du servant dans le RootPOA.
 * e. Configuration de l'annuaire JNDI et publication sous le nom "Banque".
 * f. Mise en attente des requêtes clientes (orb.run()).
 */
public class BanqueServer {

    public static final String SERVICE_NAME = "Banque";

    public static void main(String[] args) {
        try {
            System.out.println("==========================================================");
            System.out.println("         DÉMARRAGE DU SERVEUR CORBA BANQUE                ");
            System.out.println("==========================================================");

            // a. Initialiser l'ORB
            System.out.println("[1/5] Initialisation de l'ORB...");
            ORB orb = ORB.init(args, null);

            // b. Initialiser l'adaptateur d'objets (POA)
            System.out.println("[2/5] Activation du RootPOA et du POAManager...");
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            // c. Créer le servant métier (BanqueImpl)
            System.out.println("[3/5] Instanciation du servant BanqueImpl...");
            BanqueImpl banqueServant = new BanqueImpl();

            // d. Enregistrer sa référence pour la rendre disponible dans l'ORB
            System.out.println("[4/5] Enregistrement du servant dans le RootPOA...");
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(banqueServant);
            IBanqueRemote href = IBanqueRemoteHelper.narrow(ref);

            // e. Configurer l'annuaire JNDI et enregistrer la référence
            System.out.println("[5/5] Publication dans l'annuaire de noms via JNDI...");
            Context ctx = new InitialContext();
            ctx.rebind(SERVICE_NAME, href);

            System.out.println("----------------------------------------------------------");
            System.out.println(" Serveur CORBA BanqueServer prêt et en écoute !           ");
            System.out.println(" Service enregistré sous le nom : '" + SERVICE_NAME + "'       ");
            System.out.println(" Annuaire Naming Service        : iiop://localhost:900    ");
            System.out.println("==========================================================");

            // f. Mettre l'ORB en attente de requêtes des clients
            orb.run();

        } catch (Exception e) {
            System.err.println("[ERREUR CRITIQUE] Échec du démarrage du serveur CORBA :");
            e.printStackTrace();
        }
    }
}
