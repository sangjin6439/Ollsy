INSERT INTO categories(category_id,name,parent_id,depth) value (1,'상의',NULL,0);
INSERT INTO categories(category_id,name,parent_id,depth) value (2,'하의',NULL,0);
INSERT INTO categories(category_id,name,parent_id,depth) value (3,'니트',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (4,'셔츠',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (5,'져지',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (6,'반바지',2,1);

INSERT INTO item_images(item_image_id,url) value (1, 'https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images57980b09-c8f0-4ecf-8291-37023925ecdd.png');
INSERT INTO item_images(item_image_id,url) value (2, 'https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images8dc89e2e-4517-4258-9ed5-c8cfbc261304.png');
INSERT INTO item_images(item_image_id,url) value (3, 'https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images4c2c523e-525c-425d-899b-b376a0d99e6a.png');
INSERT INTO item_images(item_image_id,url) value (4, 'https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images5b0480b8-e737-4d43-aea5-5efb8e5f42e9.png');

INSERT INTO items(item_id,category_id,name,price,stock,description) value (1, 3,'라운드 니트','50000','100','부드러운 니트');
INSERT INTO items(item_id,category_id,name,price,stock,description) value (2, 4,'옥스퍼드 셔츠','60000','140','클래식한 셔츠');
INSERT INTO items(item_id,category_id,name,price,stock,description) value (3, 5,'럭비 져지','70000','200','오버핏 럭비 져지');
INSERT INTO items(item_id,category_id,name,price,stock,description) value (4, 6,'린넨 반바지','45000','80','시원한 반바지');

UPDATE item_images SET create_at = NOW(), update_at = NOW();
UPDATE item_images SET item_item_id = 1 WHERE item_image_id = 1;
UPDATE item_images SET item_item_id = 2 WHERE item_image_id = 2;
UPDATE item_images SET item_item_id = 3 WHERE item_image_id = 3;
UPDATE item_images SET item_item_id = 4 WHERE item_image_id = 4;
UPDATE items SET create_at = NOW(), update_at = NOW();

