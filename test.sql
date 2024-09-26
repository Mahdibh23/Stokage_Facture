-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : dim. 15 sep. 2024 à 01:06
-- Version du serveur : 10.4.27-MariaDB
-- Version de PHP : 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `test`
--

-- --------------------------------------------------------

--
-- Structure de la table `entreprise`
--

CREATE TABLE `entreprise` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `total_amount` decimal(38,2) NOT NULL DEFAULT 0.00,
  `is_disabled` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `entreprise`
--

INSERT INTO `entreprise` (`id`, `name`, `description`, `adresse`, `code`, `total_amount`, `is_disabled`, `email`) VALUES
(1, 'DEV Ben Younesse', 'DEV Ben Younesse', 'CYBER PARK DJERBA', 'CPSD23', '131.93', b'0', 'mahy3241@gmail.com'),
(6, 'ghgf', 'dfgff', 'gfdg', 'DF45', '0.00', b'1', 'dsfg@td.gtr'),
(7, 'dfgfdf', 'dfgfd', 'dfgfd', 'fgds', '0.00', b'0', 'sdf@ds.sd');

-- --------------------------------------------------------

--
-- Structure de la table `facture`
--

CREATE TABLE `facture` (
  `id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `objet` varchar(255) DEFAULT NULL,
  `entreprise_id` bigint(20) DEFAULT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `facture`
--

INSERT INTO `facture` (`id`, `date`, `amount`, `objet`, `entreprise_id`, `invoice_number`, `file_path`) VALUES
(84, '2024-04-26', '131.93', '__', 1, '1212', 'C:/Users/MSI/Desktop/Info/Project_Stage_Cyber/SpringBoot/uploads/decompressed_18078907013870768243.jpg');

--
-- Déclencheurs `facture`
--
DELIMITER $$
CREATE TRIGGER `update_total_amount` AFTER INSERT ON `facture` FOR EACH ROW BEGIN
    UPDATE entreprise
    SET total_amount = total_amount + NEW.amount
    WHERE id = NEW.entreprise_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `recu`
--

CREATE TABLE `recu` (
  `id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `entreprise_id` bigint(11) NOT NULL,
  `file_data` longblob DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déclencheurs `recu`
--
DELIMITER $$
CREATE TRIGGER `update_montant_total` AFTER INSERT ON `recu` FOR EACH ROW BEGIN
    UPDATE entreprise
    SET total_amount = total_amount - NEW.amount
    WHERE id = NEW.entreprise_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `name`, `email`, `password`) VALUES
(1, 'mahdi', 'mahdi@gmail.com', 'aaaa'),
(2, 'mahdi2', 'mahdi2@gmail.com', NULL);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `entreprise`
--
ALTER TABLE `entreprise`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `facture`
--
ALTER TABLE `facture`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkdtg46u7xnusk50lsp6257mk9` (`entreprise_id`);

--
-- Index pour la table `recu`
--
ALTER TABLE `recu`
  ADD PRIMARY KEY (`id`),
  ADD KEY `recu_entreprise` (`entreprise_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `entreprise`
--
ALTER TABLE `entreprise`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `facture`
--
ALTER TABLE `facture`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=85;

--
-- AUTO_INCREMENT pour la table `recu`
--
ALTER TABLE `recu`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `facture`
--
ALTER TABLE `facture`
  ADD CONSTRAINT `FKkdtg46u7xnusk50lsp6257mk9` FOREIGN KEY (`entreprise_id`) REFERENCES `entreprise` (`id`);

--
-- Contraintes pour la table `recu`
--
ALTER TABLE `recu`
  ADD CONSTRAINT `recu_entreprise` FOREIGN KEY (`entreprise_id`) REFERENCES `entreprise` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
