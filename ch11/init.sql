-- eBusiness 电子商务系统数据库初始化脚本
CREATE DATABASE IF NOT EXISTS shop CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE shop;

-- 管理员表
CREATE TABLE IF NOT EXISTS austertable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    aname VARCHAR(50) NOT NULL,
    apwd VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO austertable (aname, apwd) VALUES ('admin', 'admin');

-- 商品类型表
CREATE TABLE IF NOT EXISTS goodstype (
    id INT AUTO_INCREMENT PRIMARY KEY,
    typename VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO goodstype (typename) VALUES ('电子数码');
INSERT INTO goodstype (typename) VALUES ('服装鞋帽');
INSERT INTO goodstype (typename) VALUES ('图书音像');
INSERT INTO goodstype (typename) VALUES ('食品饮料');
INSERT INTO goodstype (typename) VALUES ('家居百货');

-- 商品表
CREATE TABLE IF NOT EXISTS goodstable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    gname VARCHAR(100) NOT NULL,
    goprice DOUBLE NOT NULL COMMENT '原价',
    grprice DOUBLE NOT NULL COMMENT '现价',
    gstore INT NOT NULL DEFAULT 0 COMMENT '库存',
    gpicture VARCHAR(200) DEFAULT '',
    isRecommend INT DEFAULT 0 COMMENT '是否推荐',
    isAdvertisement INT DEFAULT 0 COMMENT '是否广告',
    goodstype_id INT NOT NULL,
    FOREIGN KEY (goodstype_id) REFERENCES goodstype(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO goodstable (gname, goprice, grprice, gstore, gpicture, isRecommend, isAdvertisement, goodstype_id) VALUES
('智能手机', 3999, 2999, 100, 'phone.jpg', 1, 1, 1),
('笔记本电脑', 6999, 5499, 50, 'laptop.jpg', 1, 1, 1),
('无线耳机', 999, 699, 200, 'earphone.jpg', 1, 0, 1),
('平板电脑', 3299, 2599, 80, 'tablet.jpg', 0, 1, 1),
('智能手表', 1999, 1499, 150, 'watch.jpg', 1, 1, 1),
('男装外套', 599, 399, 300, 'jacket.jpg', 1, 0, 2),
('运动鞋', 799, 499, 250, 'shoes.jpg', 1, 0, 2),
('编程入门', 89, 59, 500, 'book1.jpg', 0, 0, 3),
('矿泉水', 3, 2, 1000, 'water.jpg', 0, 0, 4),
('台灯', 199, 129, 180, 'lamp.jpg', 0, 0, 5);

-- 前台用户表
CREATE TABLE IF NOT EXISTS busertable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bemail VARCHAR(100) NOT NULL UNIQUE,
    bpwd VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 购物车表
CREATE TABLE IF NOT EXISTS carttable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    busertable_id INT NOT NULL,
    goodstable_id INT NOT NULL,
    shoppingnum INT NOT NULL DEFAULT 1,
    FOREIGN KEY (busertable_id) REFERENCES busertable(id),
    FOREIGN KEY (goodstable_id) REFERENCES goodstable(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 收藏表
CREATE TABLE IF NOT EXISTS focustable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    goodstable_id INT NOT NULL,
    busertable_id INT NOT NULL,
    focustime DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (goodstable_id) REFERENCES goodstable(id),
    FOREIGN KEY (busertable_id) REFERENCES busertable(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单基础表
CREATE TABLE IF NOT EXISTS orderbasetable (
    id INT AUTO_INCREMENT PRIMARY KEY,
    busertable_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    status INT DEFAULT 0 COMMENT '0未支付 1已支付',
    orderdate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (busertable_id) REFERENCES busertable(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单详情表
CREATE TABLE IF NOT EXISTS orderdetail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderbasetable_id INT NOT NULL,
    goodstable_id INT NOT NULL,
    shoppingnum INT NOT NULL DEFAULT 1,
    FOREIGN KEY (orderbasetable_id) REFERENCES orderbasetable(id),
    FOREIGN KEY (goodstable_id) REFERENCES goodstable(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
