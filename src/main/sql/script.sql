-- \i D:/ANTONIO/S4/web-dynamique/biblio/src/main/sql/script.sql
\c postgres;
drop database if exists bibliotheque;
create database bibliotheque;
\c bibliotheque;

-- Table Profil
CREATE TABLE profil (
   id_profil SERIAL PRIMARY KEY,
   libelle VARCHAR(20) NOT NULL,
   quota_pret INT NOT NULL,
   quota_reservation INT NOT NULL,
   quota_prolongement INT NOT NULL,
   durree_de_pret int not null
);

-- Table Adherent
CREATE TABLE adherent (
   id_adherent SERIAL PRIMARY KEY,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   date_naissance DATE NOT NULL,
   id_profil INT NOT NULL REFERENCES profil(id_profil)
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


-- Table Bibliothecaire
CREATE TABLE bibliothecaire (
   id_bibliothecaire SERIAL PRIMARY KEY,
   nom VARCHAR(50) NOT NULL,
   prenom VARCHAR(50) NOT NULL,
   date_embauche DATE NOT NULL
);

-- Table Abonnement
CREATE TABLE abonnement (
   id_abonnement SERIAL PRIMARY KEY,
   date_debut DATE NOT NULL,
   date_expiration DATE NOT NULL,
   id_adherent INT NOT NULL REFERENCES adherent(id_adherent)
);

-- Table Exemplaire
CREATE TABLE exemplaire (
   id_exemplaire SERIAL PRIMARY KEY,
   ref VARCHAR(50) NOT NULL,
   titre VARCHAR(50) NOT NULL,
   restriction_age int
);

-- Table Utilisateur
CREATE TABLE utilisateur (
   id_utilisateur SERIAL PRIMARY KEY,
   nom_utilisateur VARCHAR(50) NOT NULL UNIQUE,
   mot_de_passe VARCHAR(100) NOT NULL,
   role VARCHAR(20) NOT NULL CHECK (role IN ('ADHERENT', 'BIBLIOTHECAIRE')),
   id_bibliothecaire INT REFERENCES bibliothecaire(id_bibliothecaire),
   id_adherent INT REFERENCES adherent(id_adherent)
);

-- Table Pret
CREATE TABLE pret (
   id_pret SERIAL PRIMARY KEY,
   date_de_pret DATE,
   date_retour_prevue DATE,
   date_retour_effective DATE,
   id_exemplaire INT NOT NULL REFERENCES exemplaire(id_exemplaire),
   id_adherent INT NOT NULL REFERENCES adherent(id_adherent),
   UNIQUE(id_exemplaire, date_de_pret) -- empêche deux prêts simultanés sur un même exemplaire
);

-- Verifier si un exemplaire est dispo
/*

select count(*)
from pret
where date_de_pret <= ? and ? <= date_retour_prevue
and id_exemplaire = ?;

*/
