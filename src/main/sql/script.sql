-- \i D:/ANTONIO/S4/web-dynamique/biblio/src/main/sql/script.sql
\c postgres;
drop database if exists bibliotheque;
create database bibliotheque;
\c bibliotheque;

CREATE TABLE Profil(
   id_profil SERIAL,
   durree_penalite INT NOT NULL,
   quota_pret INT NOT NULL,
   quota_reservation INT NOT NULL,
   quota_prolongement INT NOT NULL,
   durree_de_pret INT,
   libelle VARCHAR(50),
   PRIMARY KEY(id_profil)
);

CREATE TABLE Adherent(
   id_adherent SERIAL,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   date_naissance DATE NOT NULL,
   id_profil INT NOT NULL,
   PRIMARY KEY(id_adherent),
   UNIQUE(id_profil),
   FOREIGN KEY(id_profil) REFERENCES Profil(id_profil)
);

CREATE TABLE Bibliothecaire(
   id_bibliothecaire SERIAL,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   date_embauche DATE NOT NULL,
   PRIMARY KEY(id_bibliothecaire)
);

CREATE TABLE Abonnement(
   id_abonnement SERIAL,
   date_debut DATE NOT NULL,
   date_expiration DATE NOT NULL,
   id_adherent INT NOT NULL,
   PRIMARY KEY(id_abonnement),
   UNIQUE(date_expiration),
   FOREIGN KEY(id_adherent) REFERENCES Adherent(id_adherent)
);

CREATE TABLE penalite(
   id_penalite SERIAL,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   resolu BOOLEAN,
   id_adherent INT NOT NULL,
   PRIMARY KEY(id_penalite),
   FOREIGN KEY(id_adherent) REFERENCES Adherent(id_adherent)
);

CREATE TABLE type_de_pret(
   id_type_de_pret SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id_type_de_pret)
);

CREATE TABLE livre(
   id_livre SERIAL,
   titre VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_livre)
);

CREATE TABLE restriction_age(
   id_restriction SERIAL,
   age INT,
   id_livre INT NOT NULL,
   PRIMARY KEY(id_restriction),
   UNIQUE(id_livre),
   FOREIGN KEY(id_livre) REFERENCES livre(id_livre)
);

CREATE TABLE statut_exemplaire(
   id_statut SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id_statut)
);

CREATE TABLE Utilisateur(
   id_utilisateur SERIAL,
   nom_utilisateur VARCHAR(50) NOT NULL,
   mot_de_passe VARCHAR(100) NOT NULL,
   role VARCHAR(20) NOT NULL CHECK(role IN('ADHERENT', 'BIBLIOTHECAIRE')),
   id_bibliothecaire INT,
   id_adherent INT,
   PRIMARY KEY(id_utilisateur),
   UNIQUE(nom_utilisateur),
   FOREIGN KEY(id_bibliothecaire) REFERENCES Bibliothecaire(id_bibliothecaire),
   FOREIGN KEY(id_adherent) REFERENCES Adherent(id_adherent)
);

CREATE TABLE exemplaire(
   id_exemplaire SERIAL,
   id_livre INT NOT NULL,
   PRIMARY KEY(id_exemplaire),
   FOREIGN KEY(id_livre) REFERENCES livre(id_livre)
);

CREATE TABLE reservation(
   id_reservation SERIAL,
   date_de_reservation DATE NOT NULL,
   date_de_pret DATE,
   date_acceptation date,
   etat VARCHAR(50),
   id_exemplaire INT NOT NULL,
   id_adherent INT NOT NULL,
   PRIMARY KEY(id_reservation),
   FOREIGN KEY(id_exemplaire) REFERENCES exemplaire(id_exemplaire),
   FOREIGN KEY(id_adherent) REFERENCES Adherent(id_adherent)
);

CREATE TABLE pret(
   id_pret SERIAL,
   date_de_pret DATE,
   date_retour_prevue DATE,
   date_retour_effective DATE,
   id_type_de_pret INT NOT NULL,
   id_exemplaire INT NOT NULL,
   id_adherent INT NOT NULL,
   PRIMARY KEY(id_pret),
   UNIQUE(id_exemplaire),
   FOREIGN KEY(id_type_de_pret) REFERENCES type_de_pret(id_type_de_pret),
   FOREIGN KEY(id_exemplaire) REFERENCES exemplaire(id_exemplaire),
   FOREIGN KEY(id_adherent) REFERENCES Adherent(id_adherent)
);

CREATE TABLE prolongement(
   id_prolongement SERIAL,
   accepted BOOLEAN,
   checked BOOLEAN,
   id_pret INT NOT NULL,
   date_retour_apres_prolongement date,
   PRIMARY KEY(id_prolongement),
   UNIQUE(id_pret),
   FOREIGN KEY(id_pret) REFERENCES pret(id_pret)
);

CREATE TABLE historique_statut_exemplaire(
   id_exemplaire INT,
   id_statut INT,
   date_changement DATE,
   PRIMARY KEY(id_exemplaire, id_statut),
   FOREIGN KEY(id_exemplaire) REFERENCES exemplaire(id_exemplaire),
   FOREIGN KEY(id_statut) REFERENCES statut_exemplaire(id_statut)
);
