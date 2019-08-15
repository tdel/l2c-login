SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_unique` (`login`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

INSERT INTO `account` (`id`, `login`, `password`) VALUES
(1, 'test', 'n4bQgYhMfWWaL+qgxVrQFaO/TxsrC4Is0V1sFbDwCgg=');

-- password : test

DROP TABLE IF EXISTS `gameserver`;
CREATE TABLE IF NOT EXISTS `gameserver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `status` int(1) NOT NULL,
  `host` varchar(255) NOT NULL,
  `port` int(4) NOT NULL,
  `playersMaximum` int(4) NOT NULL,
  `minAge` int(4) NOT NULL,
  `types` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

INSERT INTO `gameserver` (`id`, `name`, `status`, `host`, `port`, `playersMaximum`, `minAge`, `types`) VALUES
(1, 'localhost', 1, '127.0.0.1', 7777, 10, 18, 64),
(2, 'localhost', 1, '127.0.0.1', 7777, 2, 18, 8),
(3, 'localhost', 0, '127.0.0.1', 7777, 2, 18, 2);
COMMIT;
