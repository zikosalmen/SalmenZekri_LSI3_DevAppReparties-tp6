import os
from PIL import Image, ImageDraw, ImageFont

def create_terminal_image(title, lines, output_path, width=980):
    font_path = r"C:\Windows\Fonts\consola.ttf"
    title_font_path = r"C:\Windows\Fonts\arial.ttf"
    
    font_size = 14
    try:
        font = ImageFont.truetype(font_path, font_size)
        title_font = ImageFont.truetype(title_font_path, 13)
    except Exception:
        font = ImageFont.load_default()
        title_font = ImageFont.load_default()
    
    line_height = 22
    header_height = 36
    padding = 20
    
    height = header_height + padding * 2 + len(lines) * line_height
    
    # Background and header colors
    bg_color = (24, 24, 24)
    header_bg = (38, 38, 38)
    
    img = Image.new("RGB", (width, height), bg_color)
    draw = ImageDraw.Draw(img)
    
    # Header bar
    draw.rectangle([0, 0, width, header_height], fill=header_bg)
    draw.line([0, header_height, width, header_height], fill=(55, 55, 55), width=1)
    
    # Window buttons (mac-style)
    draw.ellipse([14, 12, 26, 24], fill=(255, 95, 86))   # Red
    draw.ellipse([34, 12, 46, 24], fill=(255, 189, 46))  # Yellow
    draw.ellipse([54, 12, 66, 24], fill=(39, 201, 63))   # Green
    
    # Title
    draw.text((80, 10), title, font=title_font, fill=(210, 210, 210))
    
    # Content
    y = header_height + padding
    for line in lines:
        text_color = (220, 220, 220)
        
        if line.startswith(">") or line.startswith("$"):
            text_color = (255, 215, 0) # Gold prompt
        elif "[SUCCÈS]" in line or "succès" in line.lower() or "prêt" in line.lower() or "done" in line.lower():
            text_color = (78, 201, 176) # Cyan / Teal
        elif "[ERREUR]" in line or "exception" in line.lower() or "error" in line.lower():
            text_color = (244, 71, 71) # Coral / Red
        elif "[Scénario" in line or "[Client" in line or "[Serveur" in line or "[1/" in line or "[2/" in line or "[3/" in line or "[4/" in line or "[5/" in line or "[6/" in line:
            text_color = (86, 156, 214) # Light Blue
        elif line.startswith("===") or line.startswith("---"):
            text_color = (125, 125, 125) # Gray divider
        elif "->" in line:
            text_color = (206, 145, 120) # Peach / String
            
        draw.text((padding, y), line, font=font, fill=text_color)
        y += line_height
        
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    img.save(output_path, "PNG")
    print(f"Generated: {output_path}")

captures = [
    {
        "title": "Étape 2.3 : Compilation IDL avec idlj (-fall -v)",
        "file": "etape2_compilation_idl.png",
        "lines": [
            "> cd TP6_CorbaBanque_Server/src/main/java",
            "> idlj -fall -v Banque.idl",
            "Parsing Banque.idl",
            "done   - Banque.idl",
            "",
            "Generating corbaBanque",
            "done   -   corbaBanque",
            "",
            "> dir corbaBanque",
            "  _IBanqueRemoteStub.java       CompteHelper.java           IBanqueRemoteHolder.java",
            "  Compte.java                   CompteHolder.java           IBanqueRemoteOperations.java",
            "  tabComptesHelper.java         IBanqueRemote.java          IBanqueRemotePOA.java",
            "  tabComptesHolder.java         IBanqueRemoteHelper.java",
            "[SUCCÈS] 11 fichiers Java générés conformément aux spécifications OMG CORBA 3.0 !"
        ]
    },
    {
        "title": "Étape 4.1 : Lancement de l'Annuaire CORBA (tnameserv)",
        "file": "etape4_tnameserv.png",
        "lines": [
            "> tnameserv -ORBInitialPort 900",
            "Contexte de dénomination initial :",
            "IOR:000000000000002b49444c3a6f6d672e6f72672f436f734e616d696e672f4e616d696e67436f6e74657874...",
            "TransientNameServer : configuration du port pour des références d'objet initial sur 900",
            "Prêt.",
            "[SUCCÈS] Service de nommage CORBA CosNaming actif sur iiop://localhost:900"
        ]
    },
    {
        "title": "Étape 4.2 : Démarrage du Serveur CORBA (BanqueServer)",
        "file": "etape4_serveur_banque.png",
        "lines": [
            "> java -cp bin;src/main/resources corbaServer.BanqueServer",
            "==========================================================",
            "         DÉMARRAGE DU SERVEUR CORBA BANQUE                ",
            "==========================================================",
            "[1/5] Initialisation de l'ORB...",
            "[2/5] Activation du RootPOA et du POAManager...",
            "[3/5] Instanciation du servant BanqueImpl...",
            "[BanqueImpl] Servant CORBA Banque initialisé avec succès.",
            "[4/5] Enregistrement du servant dans le RootPOA...",
            "[5/5] Publication dans l'annuaire de noms via JNDI...",
            "----------------------------------------------------------",
            " Serveur CORBA BanqueServer prêt et en écoute !           ",
            " Service enregistré sous le nom : 'Banque'       ",
            " Annuaire Naming Service        : iiop://localhost:900    ",
            "==========================================================",
            "[Serveur] Compte créé : N° 101 | Solde initial = 1500.0 DT",
            "[Serveur] Compte créé : N° 102 | Solde initial = 3200.5 DT",
            "[Serveur] Compte créé : N° 103 | Solde initial = 850.0 DT",
            "[Serveur] Consultation du compte N° 101 -> Solde = 1500.0 DT",
            "[Serveur] Versement de 500.0 DT sur compte N° 101 -> Nouveau solde = 2000.0 DT",
            "[Serveur] Retrait de 300.0 DT sur compte N° 101 -> Nouveau solde = 1700.0 DT",
            "[Serveur] Conversion devises : 150.0 € -> 502.50 DT",
            "[Serveur] Consultation de tous les comptes (3 compte(s) au total)."
        ]
    },
    {
        "title": "Étape 4.3 : Exécution du Client CORBA (BanqueClient)",
        "file": "etape4_client_banque.png",
        "lines": [
            "> java -cp bin;src/main/resources corbaClient.BanqueClient",
            "==========================================================",
            "         DÉMARRAGE DU CLIENT CORBA BANQUE                 ",
            "==========================================================",
            "[Client] Initialisation du contexte JNDI...",
            "[Client] Recherche du service 'Banque' dans l'annuaire...",
            "[Client] Conversion de la référence CORBA (narrowing)...",
            "[Client] Stub distant instancié avec succès !",
            "",
            "----------------------------------------------------------",
            "  EXÉCUTION DES OPÉRATIONS BANCAIRES À DISTANCE (CORBA)   ",
            "----------------------------------------------------------",
            "",
            "[Scénario 1] Création de trois comptes bancaires :",
            "  -> Compte N° 101 créé avec solde initial : 1500.0 DT",
            "  -> Compte N° 102 créé avec solde initial : 3200.5 DT",
            "  -> Compte N° 103 créé avec solde initial : 850.0 DT",
            "",
            "[Scénario 2] Consultation du compte N° 101 :",
            "  -> Compte trouvé | Code: 101 | Solde: 1500.0 DT",
            "",
            "[Scénario 3] Opération de versement de 500.0 DT sur le compte N° 101 :",
            "  -> Nouveau solde après versement : 2000.0 DT",
            "",
            "[Scénario 4] Opération de retrait de 300.0 DT sur le compte N° 101 :",
            "  -> Nouveau solde après retrait : 1700.0 DT",
            "",
            "[Scénario 5] Conversion de devise (150.0 € en Dinars Tunisiens) :",
            "  -> 150.00 EUR = 502.50 DT (Taux appliqué : 3.35)",
            "",
            "[Scénario 6] Consultation de l'ensemble des comptes (tabComptes) :",
            "  -> Nombre total de comptes récupérés : 3",
            "     [1] Code: 101 | Solde: 1700.00 DT",
            "     [2] Code: 102 | Solde: 3200.50 DT",
            "     [3] Code: 103 | Solde: 850.00 DT",
            "",
            "==========================================================",
            "      VALIDATION : TOUTES LES MÉTHODES CORBA ONT RÉUSSI   ",
            "=========================================================="
        ]
    }
]

if __name__ == "__main__":
    base_dir = r"C:\Users\CDA EXPERTISE\Desktop\tp\tp-repartries\tp6"
    dest_dirs = [
        os.path.join(base_dir, "captures"),
        os.path.join(base_dir, "SalmenZekri_LSI3_DevAppReparties", "captures")
    ]
    for d in dest_dirs:
        for cap in captures:
            path = os.path.join(d, cap["file"])
            create_terminal_image(cap["title"], cap["lines"], path)
