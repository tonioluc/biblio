INSERT INTO profil (libelle, quota_pret, quota_reservation, quota_prolongement, durree_de_pret) VALUES
('ETUDIANT', 5, 3, 2,10),
('PROFESSEUR', 10, 5, 4,15);

-- Insertion de quelques exemplaires
INSERT INTO exemplaire (ref, titre,restriction_age) VALUES 
('REF-001', 'Le Petit Prince',5),
('REF-002', '1984',13),
('REF-003', 'L etranger',12),
('REF-004', 'Les Miserables',18),
('REF-005', 'Harry Potter a l ecole des sorciers',14);

insert into type_de_pret (id_type_de_pret,libelle) VALUES
(1,'Sur place'),
(2,'Emporte');

-- Insertion d'un bibliothécaire
INSERT INTO bibliothecaire (nom, prenom, date_embauche)
VALUES ('Dupont', 'Jean', '2024-09-01');

-- Création de l'utilisateur associé au bibliothécaire
INSERT INTO utilisateur (nom_utilisateur, mot_de_passe, role, id_bibliothecaire)
VALUES ('biblio', 'mdp123', 'BIBLIOTHECAIRE', 1);
