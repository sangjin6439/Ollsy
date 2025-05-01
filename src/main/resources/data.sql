INSERT INTO categories(category_id,name,parent_id,depth) value (1,'상의',NULL,0);
INSERT INTO categories(category_id,name,parent_id,depth) value (2,'하의',NULL,0);
INSERT INTO categories(category_id,name,parent_id,depth) value (3,'니트',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (4,'셔츠',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (5,'져지',1,1);
INSERT INTO categories(category_id,name,parent_id,depth) value (6,'반바지',2,1);

INSERT INTO item_images(item_image_id,url) value (1, 'https://ollsy.s3.ap-northeast-2.amazonaws.com/item-imagesea90b7ef-ab50-4753-a0b6-b9fd9412cb57.png');
INSERT INTO item_images(item_image_id,url) value (2, 'https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images51ba5e39-dc23-4b86-b60d-414310386997.png');
INSERT INTO item_images(item_image_id,url) value (3, 'https://ollsy.s3.ap-northeast-2.amazonaws.com/item-images58316170-7d63-4bfc-ac0f-7a3553e758a2.png');
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