INSERT INTO profil (libelle, quota_pret, quota_reservation, quota_prolongement, durree_de_pret, durree_penalite) VALUES
('ETUDIANT', 5, 3, 2, 10, 7),
('PROFESSEUR', 10, 5, 4, 15, 5);

insert into type_de_pret (id_type_de_pret,libelle) VALUES
(1,'Sur place'),
(2,'Emporte');

-- Insertion d'un bibliothecaire
INSERT INTO bibliothecaire (nom, prenom, date_embauche)
VALUES ('Dupont', 'Jean', '2024-09-01');

-- Creation de l'utilisateur associe au bibliothecaire
INSERT INTO utilisateur (nom_utilisateur, mot_de_passe, role, id_bibliothecaire)
VALUES ('biblio', 'mdp123', 'BIBLIOTHECAIRE', 1);

INSERT INTO livre (id_livre, titre) VALUES
(1, 'Le Petit Prince'),
(2, '1984'),
(3, 'L etranger'),
(4, 'Les Miserables'),
(5, 'Harry Potter à l ecole des sorciers');

INSERT INTO exemplaire (id_exemplaire, id_livre) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2),
(5, 3),
(6, 3),
(7, 4),
(8, 4),
(9, 5),
(10, 5);

