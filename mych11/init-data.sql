-- ============================================================
-- eBusiness 电商系统 - 一次性数据初始化脚本
-- 使用方法：mysql -u root -p ebusiness < init-data.sql
-- 注意：这会清空所有表数据并重置自增ID
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE orderdetail;
TRUNCATE TABLE focustable;
TRUNCATE TABLE cartitem;
TRUNCATE TABLE carttable;
TRUNCATE TABLE orderbasetable;
TRUNCATE TABLE goodstable;
TRUNCATE TABLE goodstype;
TRUNCATE TABLE busertable;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. 用户表 (busertable)
--    密码使用 BCrypt 哈希，与 Spring Security 兼容
--    admin123 -> $2a$10$... (需从已运行的应用获取)
--    以下为简化版，首次运行请使用 DataInitializer 生成带正确哈希的密码
-- ============================================================
INSERT INTO busertable (bemail, bpwd, role) VALUES
('admin@shop.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
('user1@shop.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER'),
('user2@shop.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER'),
('user3@shop.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER');

-- ============================================================
-- 2. 商品分类 (goodstype)
-- ============================================================
INSERT INTO goodstype (typename) VALUES
('电子数码'),
('服装鞋帽'),
('家居用品'),
('图书音像'),
('运动户外');

-- ============================================================
-- 3. 商品 (goodstable)
--    picture: 对应 static/images/ 下的文件名
--    is_recommend=1 推荐商品, is_advertisement=1 广告商品(轮播)
-- ============================================================
INSERT INTO goodstable (gname, description, goprice, grprice, gstore, gpicture, is_recommend, is_advertisement, goodstype_id) VALUES
('iPhone 15 Pro',   '苹果手机，A17芯片，4800万像素',             7999.0,  8999.0,  100, 'phone.jpg',      1, 1, 1),
('MacBook Air M3',  '轻薄笔记本，M3芯片，18小时续航',           10999.0, 12999.0,  100, 'laptop.jpg',     1, 1, 1),
('Sony降噪耳机',     '行业领先降噪技术，30小时续航',              2299.0,  2999.0,  100, 'headphones.jpg', 1, 1, 1),
('iPad Air',        'M2芯片，11英寸Liquid视网膜屏',               3999.0,  4799.0,  100, 'tablet.jpg',     0, 0, 1),
('机械键盘',         '红轴机械键盘，RGB背光',                      399.0,   599.0,  100, 'keyboard.jpg',   1, 0, 1),
('无线鼠标',         '人体工学设计，静音按键',                      129.0,   199.0,  100, 'mouse.jpg',      0, 0, 1),
('Nike Air Max',    '经典气垫跑步鞋，透气缓震',                    899.0,  1299.0,  100, 'shoes.jpg',      1, 0, 2),
('优衣库羽绒服',     '轻薄保暖，防水面料',                          599.0,   999.0,  100, 'jacket.jpg',     0, 0, 2),
('休闲T恤',          '纯棉圆领，四季百搭',                          89.0,   159.0,  100, 'tshirt.jpg',     1, 0, 2),
('运动长裤',         '速干面料，弹力修身',                          199.0,   299.0,  100, 'pants.jpg',      0, 0, 2),
('智能台灯',         '无频闪护眼，智能调光',                        299.0,   499.0,  100, 'lamp.jpg',       1, 0, 3),
('保温水杯',         '316不锈钢，12小时保温',                       129.0,   199.0,  100, 'water.jpg',      0, 0, 3),
('Java核心技术',     'Java开发者必读经典',                           89.0,   138.0,  100, 'book1.jpg',      1, 0, 4),
('Apple Watch SE',  '健康监测，运动追踪',                         2499.0,  2999.0,  100, 'watch.jpg',      1, 1, 1),
('Sony WF-1000XM5', '真无线降噪耳机，Hi-Res音质',                 1999.0,  2599.0,  100, 'earphone.jpg',   1, 1, 1),
('入耳式耳机',       '高音质动圈驱动，降噪通话',                    199.0,   299.0,  100, 'earphone.jpg',   0, 0, 1);

-- ============================================================
-- 4. 订单 (orderbasetable) + 订单详情 (orderdetail)
--    使用 busertable 的 id: 1=admin, 2=user1, 3=user2, 4=user3
-- ============================================================
INSERT INTO orderbasetable (busertable_id, amount, status, orderdate) VALUES
(2, 2999.0,  1, '2026-01-15 10:00:00'),
(3, 10999.0, 1, '2026-01-25 14:30:00'),
(4, 798.0,   1, '2026-02-10 09:15:00'),
(2, 2299.0,  1, '2026-02-20 16:45:00'),
(3, 3999.0,  1, '2026-03-08 11:00:00'),
(4, 599.0,   1, '2026-03-18 13:20:00'),
(2, 899.0,   1, '2026-03-28 08:30:00'),
(3, 199.0,   1, '2026-04-05 15:10:00'),
(4, 1299.0,  1, '2026-04-15 10:00:00'),
(2, 599.0,   1, '2026-04-25 17:00:00'),
(3, 89.0,    1, '2026-05-10 12:00:00'),
(2, 2599.0,  1, '2026-05-20 14:30:00'),
(3, 499.0,   1, '2026-06-01 09:00:00'),
(4, 299.0,   1, '2026-06-10 11:00:00'),
(2, 2299.0,  1, '2026-06-15 10:00:00'),
(3, 3999.0,  1, '2026-06-18 16:00:00'),
(2, 2999.0,  1, '2026-06-22 07:23:00'),
(4, 798.0,   1, '2026-06-25 00:33:00'),
(2, 2999.0,  1, '2026-06-25 14:24:00'),
(2, 1299.0,  0, '2026-06-26 14:30:00');

-- ============================================================
-- 5. 订单详情示例 (orderdetail) - 关联部分订单
-- ============================================================
INSERT INTO orderdetail (orderbasetable_id, goodstable_id, shoppingnum) VALUES
(1,  1,  1),
(2,  2,  1),
(3,  3,  1),
(4,  4,  1),
(5,  5,  2),
(6,  6,  1),
(7,  7,  1),
(8,  8,  2),
(9,  9,  1),
(10, 10, 1),
(11, 11, 1),
(12, 12, 1),
(13, 13, 2),
(14, 14, 1),
(15, 15, 1),
(16,  2,  1),
(17,  1,  1),
(18,  3,  2),
(19,  1,  1),
(20, 14,  1);