/*
Navicat MySQL Data Transfer

Target Server Type    : MYSQL
Target Server Version : 50630
File Encoding         : 65001

Date: 2017-01-12 15:51:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `tel` varchar(255) NOT NULL DEFAULT '',
  `subject_id` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
