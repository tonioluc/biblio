INSERT INTO profil (libelle, quota_pret,durree_de_pret, quota_reservation, quota_prolongement,  durree_penalite) VALUES
('ETUDIANT', 2, 7, 1, 3, 10),
('ENSEIGNANT', 3, 9, 2, 5, 9),
('PROFESSIONNEL', 4, 12, 3, 7, 8);

insert into type_de_pret (id_type_de_pret,libelle) VALUES
(1,'Sur place'),
(2,'Emporte');

INSERT INTO adherent(id_adherent,nom,prenom,id_profil,date_naissance) values 
(1,'Amine Bensaid','ETU001',1,'2000-01-01'),
(2,'Sarah El Khattabi','ETU002',1,'2001-02-02'),
(3,'Youssef Moujahid','ETU003',1,'2002-03-03'),
(4,'Nadia Benali','ENS001',2,'1999-04-04'),
(5,'Karima Haddadi','ENS002',2,'2003-05-05'),
(6,'Salima Touhami','ENS003',2,'2004-06-06'),
(7,'Rachid El Mansouri','PROF001',3,'2005-07-07'),
(8,'Amina Zerouali','PROF002',3,'2006-08-08');

-- Creation de l'utilisateur associe au bibliothecaire
INSERT INTO utilisateur (nom_utilisateur, mot_de_passe, role, id_adherent)VALUES 
('ETU001', 'mdp123', 'ADHERENT', 1),
('ETU002', 'mdp123', 'ADHERENT', 2),
('ETU003', 'mdp123', 'ADHERENT', 3),
('ENS001', 'mdp123', 'ADHERENT', 4),
('ENS002', 'mdp123', 'ADHERENT', 5),
('ENS003', 'mdp123', 'ADHERENT', 6),
('PROF001', 'mdp123', 'ADHERENT', 7),
('PROF002', 'mdp123', 'ADHERENT', 8);

-- Insertion d'un bibliothecaire
INSERT INTO bibliothecaire (nom, prenom, date_embauche)
VALUES ('Dupont', 'Jean', '2024-09-01');

-- Creation de l'utilisateur associe au bibliothecaire
INSERT INTO utilisateur (nom_utilisateur, mot_de_passe, role, id_bibliothecaire)
VALUES ('biblio', 'mdp123', 'BIBLIOTHECAIRE', 1);

INSERT INTO livre (id_livre, titre) VALUES
(1, 'Les miserabes'),
(2, 'L etranger'),
(3, 'Harry Potter à l ecole des sorciers');

INSERT INTO exemplaire (id_exemplaire, id_livre) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 2),
(5, 2),
(6, 3);

insert into Abonnement(date_debut, date_expiration, id_adherent) VALUES
('2025-02-01', '2025-07-24', 1),
('2025-02-01', '2025-07-01', 2),
('2025-04-01', '2025-12-01', 3),
('2025-07-01', '2025-07-01', 4),
('2025-08-01', '2025-05-01', 5),
('2025-07-01', '2025-06-01', 6),
('2025-06-01', '2025-12-01', 7),
('2025-10-01', '2025-06-01', 8);

INSERT INTO statut_exemplaire (libelle) VALUES 
('Disponible'),
('Emprunte'),
('Reserve');

insert into jour_ferie(id_jour, date_jour) VALUES
(1, '2025-07-13'),
(2, '2025-07-20'),
(3, '2025-07-27'),
(4, '2025-08-03'),
(5, '2025-08-10'),
(6, '2025-08-17'),
(7, '2025-07-26'),
(8, '2025-07-19');
