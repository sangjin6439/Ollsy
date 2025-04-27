INSERT INTO categories(category_id,name,parent_id,depth) value (1,'상의',NULL,0);
INSERT INTO categories(category_id,name,parent_id,depth) value (2,'하의',NULL,0);
INSERT INTO categories(category_id,name,parent_id,depth) value (3,'니트',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (4,'셔츠',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (5,'져지',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (6,'반바지',2,1);

INSERT INTO item_images(item_image_id,create_at,update_at,url,item_item_id) value (1,'2025-04-27 00:00:00', '2025-04-27 00:00:00','https://ollsy.s3.ap-northeast-2.amazonaws.com/item-imagesea90b7ef-ab50-4753-a0b6-b9fd9412cb57.png',NULL);
INSERT INTO item_images(item_image_id,create_at,update_at,url,item_item_id) value (2,'2025-04-27 00:00:00', '2025-04-27 00:00:00','https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images51ba5e39-dc23-4b86-b60d-414310386997.png',NULL);
INSERT INTO item_images(item_image_id,create_at,update_at,url,item_item_id) value (3,'2025-04-27 00:00:00', '2025-04-27 00:00:00','https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images58316170-7d63-4bfc-ac0f-7a3553e758a2.png',NULL);
INSERT INTO item_images(item_image_id,create_at,update_at,url,item_item_id) value (4,'2025-04-27 00:00:00', '2025-04-27 00:00:00','https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images5b0480b8-e737-4d43-aea5-5efb8e5f42e9.png',NULL);

INSERT INTO items(item_id,create_at,update_at,category_id,name,price,stock,description) value (1,'2025-04-27 00:00:00', '2025-04-27 00:00:00',3,'라운드 니트','50000','100','부드러운 니트');
INSERT INTO items(item_id,create_at,update_at,category_id,name,price,stock,description) value (2,'2025-04-27 00:00:00', '2025-04-27 00:00:00',4,'옥스퍼드 셔츠','60000','140','클래식한 셔츠');
INSERT INTO items(item_id,create_at,update_at,category_id,name,price,stock,description) value (3,'2025-04-27 00:00:00', '2025-04-27 00:00:00',5,'럭비 져지','70000','200','오버핏 럭비 져지');
INSERT INTO items(item_id,create_at,update_at,category_id,name,price,stock,description) value (4,'2025-04-27 00:00:00', '2025-04-27 00:00:00',6,'린넨 반바지','45000','80','시원한 반바지');