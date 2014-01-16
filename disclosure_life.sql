/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : disclosure_life

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2014-01-16 17:57:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `consume`
-- ----------------------------
DROP TABLE IF EXISTS `consume`;
CREATE TABLE `consume` (
  `CID` int(11) NOT NULL AUTO_INCREMENT,
  `UID` int(11) DEFAULT NULL,
  `CName` varchar(20) NOT NULL,
  `CMoney` decimal(10,0) NOT NULL,
  PRIMARY KEY (`CID`),
  KEY `FK_user_consume` (`UID`),
  CONSTRAINT `FK_user_consume` FOREIGN KEY (`UID`) REFERENCES `user` (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='消费单';

-- ----------------------------
-- Records of consume
-- ----------------------------
INSERT INTO `consume` VALUES ('1', '1', '买衣服', '200');
INSERT INTO `consume` VALUES ('2', '1', '买手机', '1500');
INSERT INTO `consume` VALUES ('3', '1', '车费', '80');
INSERT INTO `consume` VALUES ('4', '1', '月生活费', '1200');
INSERT INTO `consume` VALUES ('5', '1', '水电费', '120');
INSERT INTO `consume` VALUES ('7', '1', '买杂货', '50');
INSERT INTO `consume` VALUES ('8', '1', '买杂货', '50');
INSERT INTO `consume` VALUES ('9', '1', '买杂货', '50');
INSERT INTO `consume` VALUES ('10', '1', '买杂货', '50');
INSERT INTO `consume` VALUES ('11', '1', '买杂货', '50');
INSERT INTO `consume` VALUES ('12', '1', '买杂货', '50');
INSERT INTO `consume` VALUES ('13', '1', '买杂货', '50');
INSERT INTO `consume` VALUES ('14', '1', '买杂货', '50');
INSERT INTO `consume` VALUES ('16', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('17', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('18', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('19', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('20', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('21', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('22', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('23', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('24', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('25', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('26', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('27', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('28', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('29', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('30', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('31', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('32', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('33', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('34', '1', '买房子', '2000');
INSERT INTO `consume` VALUES ('35', '1', '买房子1', '2001');
INSERT INTO `consume` VALUES ('36', '1', '买房子2', '2020');
INSERT INTO `consume` VALUES ('37', '1', '买房子3', '2030');
INSERT INTO `consume` VALUES ('38', '1', '买房子4', '2040');
INSERT INTO `consume` VALUES ('39', '1', '买房子5', '2050');
INSERT INTO `consume` VALUES ('40', '1', '买房子1', '2001');
INSERT INTO `consume` VALUES ('41', '1', '买房子2', '2020');
INSERT INTO `consume` VALUES ('42', '1', '买房子3', '2030');
INSERT INTO `consume` VALUES ('43', '1', '买房子4', '2040');
INSERT INTO `consume` VALUES ('44', '1', '买房子5', '2050');
INSERT INTO `consume` VALUES ('45', '1', '买房子1', '2001');
INSERT INTO `consume` VALUES ('46', '1', '买房子2', '2020');
INSERT INTO `consume` VALUES ('47', '1', '买房子3', '2030');
INSERT INTO `consume` VALUES ('48', '1', '买房子4', '2040');
INSERT INTO `consume` VALUES ('49', '1', '买房子5', '2050');
INSERT INTO `consume` VALUES ('50', '1', '买房子1', '2001');
INSERT INTO `consume` VALUES ('51', '1', '买房子2', '2020');
INSERT INTO `consume` VALUES ('52', '1', '买房子3', '2030');
INSERT INTO `consume` VALUES ('53', '1', '买房子4', '2040');
INSERT INTO `consume` VALUES ('54', '1', '买房子5', '2050');
INSERT INTO `consume` VALUES ('55', '1', '买房子1', '2001');
INSERT INTO `consume` VALUES ('56', '1', '买房子2', '2020');
INSERT INTO `consume` VALUES ('57', '1', '买房子3', '2030');
INSERT INTO `consume` VALUES ('58', '1', '买房子4', '2040');
INSERT INTO `consume` VALUES ('59', '1', '买房子5', '2050');
INSERT INTO `consume` VALUES ('60', '1', '送礼物', '67');
INSERT INTO `consume` VALUES ('61', '1', '送礼物', '87');
INSERT INTO `consume` VALUES ('62', '1', '送礼物', '6');
INSERT INTO `consume` VALUES ('63', '1', '买食物', '8');

-- ----------------------------
-- Table structure for `consume_comment`
-- ----------------------------
DROP TABLE IF EXISTS `consume_comment`;
CREATE TABLE `consume_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `comment` text,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`,`cid`) USING BTREE,
  KEY `cid` (`cid`),
  CONSTRAINT `cid` FOREIGN KEY (`cid`) REFERENCES `consume` (`CID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consume_comment
-- ----------------------------
INSERT INTO `consume_comment` VALUES ('1', '62', 'hg', null);
INSERT INTO `consume_comment` VALUES ('2', '62', 'jjhhjjjjj', null);
INSERT INTO `consume_comment` VALUES ('3', '62', 'fffffff', null);
INSERT INTO `consume_comment` VALUES ('4', '62', 'kljg', null);
INSERT INTO `consume_comment` VALUES ('5', '62', '好的', null);
INSERT INTO `consume_comment` VALUES ('6', '61', '不错哦', null);
INSERT INTO `consume_comment` VALUES ('7', '62', '哈哈哈就呵呵\n\n\n\n\n和斤斤计较的人的的人的\n\n\n                  以后机会的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的', null);
INSERT INTO `consume_comment` VALUES ('8', '62', '回家', null);
INSERT INTO `consume_comment` VALUES ('9', '62', '个人', null);
INSERT INTO `consume_comment` VALUES ('10', '62', '韩国', null);
INSERT INTO `consume_comment` VALUES ('11', '62', '脚后跟不能。       \n\n\n\n\n回家解决方案设计师的人的的\n\n\n\n回家客户呵呵呵呵呵呵呵呵呵\n\n\n\n\n回家解决', null);
INSERT INTO `consume_comment` VALUES ('12', '62', '回家呵呵看\n\n不催', null);
INSERT INTO `consume_comment` VALUES ('13', '62', '哈哈哈哈', null);
INSERT INTO `consume_comment` VALUES ('14', '63', '规范化时间', '2014-01-16 17:21:38');
INSERT INTO `consume_comment` VALUES ('15', '63', '基本vjj\n', '2014-01-16 17:56:51');

-- ----------------------------
-- Table structure for `embarrass`
-- ----------------------------
DROP TABLE IF EXISTS `embarrass`;
CREATE TABLE `embarrass` (
  `EID` int(11) NOT NULL AUTO_INCREMENT,
  `UID` int(11) DEFAULT NULL,
  `ETitle` varchar(30) NOT NULL,
  `EDesc` text,
  `EPic` text,
  PRIMARY KEY (`EID`),
  KEY `FK_user_embarrass` (`UID`),
  CONSTRAINT `FK_user_embarrass` FOREIGN KEY (`UID`) REFERENCES `user` (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='糗事儿';

-- ----------------------------
-- Records of embarrass
-- ----------------------------
INSERT INTO `embarrass` VALUES ('1', '1', '滑稽', '有一天，小明啊。哈哈', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('2', '1', '搞笑', '1、“你为何要强迫受害人与你发生性关系？你不知道这是违法的吗？！” \r\n“法官大人，我有强迫症啊。”\r\n2、A：“昨晚体验了一把儿时的感觉。”\r\nB：“和儿子抢食吃了？”\r\nA：“艹，尿床了。”\r\n3、“中午小区门口拿快递，11点40到12点之间，过时不侯，圆通。”\r\n“为何你这么吊？为何你如此猖狂？你的自信从何而来？你的语气真是虎虎生威、刚劲有力、一身正气！看着我的眼睛，我只想认真的对你说三个字：我尽量！”\r\n4、A：你怎么只吃这么一点儿？怪不得你身材保持得这么好。 \r\nB：你也得注意点了，这么胖怎么还吃这么多！\r\nA：我和你一样，要保持身材啊。\r\n5、咖啡厅新招了一名服务员，在自我介绍的时候说从小到大的梦想就是当一名服务员，后来那新来的做了一上午就辞职了！\r\n问那新来的：你的梦想不是要做一名服务员么？\r\n那新来的回：是啊！\r\n她又问：那你什么辞职了！\r\n新来的：因为梦想实现了啊！', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('3', '1', '买房子1', '买房子1', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('4', '1', '买房子1', '买房子1', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('5', '1', '买房子1', '买房子1', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('6', '1', '买房子1', '买房子1', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('7', '1', '买房子1', '买房子1', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('8', '1', '买房子1', '买房子1', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('9', '1', '规划机会', '符合呵呵', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('10', '1', '发呵呵', '他银行股', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('11', '1', '规划规范\n', '符合呵呵呵', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('12', '1', '解决', '解决\n', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('13', '1', '广告', '广告\n', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('14', '1', '呵呵', '呵呵', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('15', '1', '解决', '解决', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('16', '1', '哈哈', '哈哈', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `embarrass` VALUES ('17', '1', '一样', '规划局', 'http://192.168.1.150:8080/DisclosureYourLife/cache_http%3A%2F%2Fyl.cms.palmtrends.com%2Fupload%2F1382335669%2F201310211409062_listthumb_iphone4.jpg');
INSERT INTO `embarrass` VALUES ('18', '1', '广告', '广告', 'http://192.168.1.150:8080/DisclosureYourLife/cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1382335669_2F201310211409062_listthumb_iphone4.jpg');
INSERT INTO `embarrass` VALUES ('19', '1', '一样', '一样', 'http://192.168.1.150:8080/DisclosureYourLife/1373877356.png');
INSERT INTO `embarrass` VALUES ('20', '1', '', '', null);
INSERT INTO `embarrass` VALUES ('21', '1', 'hh', '', null);
INSERT INTO `embarrass` VALUES ('22', '1', 'hj', 'hj', 'http://192.168.0.101:8080/DisclosureYourLife/cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1381731642_2F201310141430453_listthumb_iphone4.jpg');
INSERT INTO `embarrass` VALUES ('23', '1', 'gg', 'gg', 'http://192.168.1.150:8080/DisclosureYourLife/cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1381894316_2F201310161137212_listthumb_iphone4.jpg');
INSERT INTO `embarrass` VALUES ('24', '1', 'hb', 'hb', null);

-- ----------------------------
-- Table structure for `embarrass_comment`
-- ----------------------------
DROP TABLE IF EXISTS `embarrass_comment`;
CREATE TABLE `embarrass_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eid` int(11) DEFAULT NULL,
  `comment` text,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `eid` (`eid`),
  KEY `id` (`id`,`eid`) USING BTREE,
  CONSTRAINT `eid` FOREIGN KEY (`eid`) REFERENCES `embarrass` (`EID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of embarrass_comment
-- ----------------------------
INSERT INTO `embarrass_comment` VALUES ('1', '23', '个呵呵', null);
INSERT INTO `embarrass_comment` VALUES ('2', '23', '规划vh\nvhjj', null);
INSERT INTO `embarrass_comment` VALUES ('3', '23', '和此次规范化就\n\n\n\n\n符合基金会的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的', null);
INSERT INTO `embarrass_comment` VALUES ('4', '2', '呵呵不过如此', null);

-- ----------------------------
-- Table structure for `income`
-- ----------------------------
DROP TABLE IF EXISTS `income`;
CREATE TABLE `income` (
  `IID` int(11) NOT NULL AUTO_INCREMENT,
  `UID` int(11) DEFAULT NULL,
  `IName` varchar(20) NOT NULL,
  `IMoney` decimal(10,0) NOT NULL,
  PRIMARY KEY (`IID`),
  KEY `FK_user_income` (`UID`),
  CONSTRAINT `FK_user_income` FOREIGN KEY (`UID`) REFERENCES `user` (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='收入单';

-- ----------------------------
-- Records of income
-- ----------------------------
INSERT INTO `income` VALUES ('1', '1', '实习工资', '1500');
INSERT INTO `income` VALUES ('2', '1', '爸妈补给', '1100');
INSERT INTO `income` VALUES ('3', '1', '买房子1', '2001');
INSERT INTO `income` VALUES ('4', '1', '买房子2', '2020');
INSERT INTO `income` VALUES ('5', '1', '买房子3', '2030');
INSERT INTO `income` VALUES ('6', '1', '买房子4', '2040');
INSERT INTO `income` VALUES ('7', '1', '买房子5', '2050');
INSERT INTO `income` VALUES ('8', '1', '买房子1', '2001');
INSERT INTO `income` VALUES ('9', '1', '总共', '7');

-- ----------------------------
-- Table structure for `income_comment`
-- ----------------------------
DROP TABLE IF EXISTS `income_comment`;
CREATE TABLE `income_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `iid` int(11) DEFAULT NULL,
  `comment` text,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `iid` (`iid`),
  KEY `id` (`id`,`iid`) USING BTREE,
  CONSTRAINT `iid` FOREIGN KEY (`iid`) REFERENCES `income` (`IID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of income_comment
-- ----------------------------
INSERT INTO `income_comment` VALUES ('1', '8', '好机会', null);
INSERT INTO `income_comment` VALUES ('2', '8', '脚后跟', null);
INSERT INTO `income_comment` VALUES ('3', '8', '女方广告', null);
INSERT INTO `income_comment` VALUES ('4', '8', '家家户户\n\n\n\n\n\n\nvhjj\n\n\n公积金的人的的人的\n\n\n以后就的人的的人的', null);
INSERT INTO `income_comment` VALUES ('5', '8', '健康呵呵', null);
INSERT INTO `income_comment` VALUES ('6', '8', '个哈哈哈', null);
INSERT INTO `income_comment` VALUES ('7', '8', '回家呵呵呵呵呵呵呵', null);
INSERT INTO `income_comment` VALUES ('8', '8', '有呵呵vfg    ', null);
INSERT INTO `income_comment` VALUES ('9', '8', 'vhh和谐社会主义者的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人\n\n\n\n\n\n呵呵解决方案设计师的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的人的的', null);
INSERT INTO `income_comment` VALUES ('10', '8', '回家版本', null);
INSERT INTO `income_comment` VALUES ('11', '8', '个vhjjt      ', null);
INSERT INTO `income_comment` VALUES ('12', '9', '时间格式', '2014-01-16 17:29:03');
INSERT INTO `income_comment` VALUES ('13', '9', '和各个环节', '2014-01-16 17:34:32');

-- ----------------------------
-- Table structure for `picture`
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
  `PID` int(11) NOT NULL AUTO_INCREMENT,
  `EID` int(11) NOT NULL,
  `PPath` text,
  `PName` varchar(200) DEFAULT NULL,
  `PPic` text,
  PRIMARY KEY (`PID`),
  KEY `EID` (`EID`) USING BTREE,
  CONSTRAINT `picture_ibfk_1` FOREIGN KEY (`EID`) REFERENCES `embarrass` (`EID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of picture
-- ----------------------------
INSERT INTO `picture` VALUES ('4', '1', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('5', '2', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('6', '1', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('7', '2', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('8', '9', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('9', '10', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('10', '11', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('11', '12', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('12', '16', 'http://avatar.csdn.net/8/5/D/', '1_liuhe688.jpg', 'http://avatar.csdn.net/8/5/D/1_liuhe688.jpg');
INSERT INTO `picture` VALUES ('13', '17', 'http://192.168.1.150:8080/DisclosureYourLife/', 'cache_http%3A%2F%2Fyl.cms.palmtrends.com%2Fupload%2F1382335669%2F201310211409062_listthumb_iphone4.jpg', 'http://192.168.1.150:8080/DisclosureYourLife/cache_http%3A%2F%2Fyl.cms.palmtrends.com%2Fupload%2F1382335669%2F201310211409062_listthumb_iphone4.jpg');
INSERT INTO `picture` VALUES ('14', '17', 'http://192.168.1.150:8080/DisclosureYourLife/', 'cache_http%3A%2F%2Fyl.cms.palmtrends.com%2Fupload%2F1382335979%2F201310211414088_listthumb_iphone4.jpg', 'http://192.168.1.150:8080/DisclosureYourLife/cache_http%3A%2F%2Fyl.cms.palmtrends.com%2Fupload%2F1382335979%2F201310211414088_listthumb_iphone4.jpg');
INSERT INTO `picture` VALUES ('15', '18', 'http://192.168.1.150:8080/DisclosureYourLife/', 'cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1382335669_2F201310211409062_listthumb_iphone4.jpg', 'http://192.168.1.150:8080/DisclosureYourLife/cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1382335669_2F201310211409062_listthumb_iphone4.jpg');
INSERT INTO `picture` VALUES ('16', '18', 'http://192.168.1.150:8080/DisclosureYourLife/', 'cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1382335979_2F201310211414088_listthumb_iphone4.jpg', 'http://192.168.1.150:8080/DisclosureYourLife/cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1382335979_2F201310211414088_listthumb_iphone4.jpg');
INSERT INTO `picture` VALUES ('17', '19', 'http://192.168.1.150:8080/DisclosureYourLife/', '1373877356.png', 'http://192.168.1.150:8080/DisclosureYourLife/1373877356.png');
INSERT INTO `picture` VALUES ('18', '19', 'http://192.168.1.150:8080/DisclosureYourLife/', '201310161137212_cross_iphone4.jpg', 'http://192.168.1.150:8080/DisclosureYourLife/201310161137212_cross_iphone4.jpg');
INSERT INTO `picture` VALUES ('19', '19', 'http://192.168.1.150:8080/DisclosureYourLife/', '201310211426338_cross_iphone4.jpg', 'http://192.168.1.150:8080/DisclosureYourLife/201310211426338_cross_iphone4.jpg');
INSERT INTO `picture` VALUES ('20', '22', 'http://192.168.0.101:8080/DisclosureYourLife/', 'cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1381731642_2F201310141430453_listthumb_iphone4.jpg', 'http://192.168.0.101:8080/DisclosureYourLife/cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1381731642_2F201310141430453_listthumb_iphone4.jpg');
INSERT INTO `picture` VALUES ('21', '22', 'http://192.168.0.101:8080/DisclosureYourLife/', 'cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1381805498_2F201310151058258_listthumb_iphone4.jpg', 'http://192.168.0.101:8080/DisclosureYourLife/cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1381805498_2F201310151058258_listthumb_iphone4.jpg');
INSERT INTO `picture` VALUES ('22', '23', 'http://192.168.1.150:8080/DisclosureYourLife/', 'cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1381894316_2F201310161137212_listthumb_iphone4.jpg', 'http://192.168.1.150:8080/DisclosureYourLife/cache_http_3A_2F_2Fyl.cms.palmtrends.com_2Fupload_2F1381894316_2F201310161137212_listthumb_iphone4.jpg');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `UID` int(11) NOT NULL AUTO_INCREMENT,
  `UPhone` varchar(20) DEFAULT NULL,
  `UDevice` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '13637823045', null);