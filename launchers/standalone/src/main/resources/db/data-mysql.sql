-- sample user
insert into s2_user (id, `name`, password, display_name, email, is_admin) values (1, 'admin','admin','admin','admin@xx.com', 1);
insert into s2_user (id, `name`, password, display_name, email, is_admin) values (4, 'lucy','123456','lucy','lucy@xx.com', 1);


INSERT INTO s2_available_date_info (`item_id`, `type`, `date_format`, `start_date`, `end_date`, `unavailable_date`, `created_at`, `created_by`, `updated_at`, `updated_by`)
VALUES (1, 'dimension', 'yyyy-MM-dd', DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '[]', '2023-06-01', 'admin', '2023-06-01', 'admin');

INSERT INTO s2_available_date_info (`item_id`, `type`, `date_format`, `start_date`, `end_date`, `unavailable_date`, `created_at`, `created_by`, `updated_at`, `updated_by`)
VALUES (2, 'dimension', 'yyyy-MM-dd', DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '[]', '2023-06-01', 'admin', '2023-06-01', 'admin');

INSERT INTO s2_available_date_info (`item_id`, `type`, `date_format`, `start_date`, `end_date`, `unavailable_date`, `created_at`, `created_by`, `updated_at`, `updated_by`)
VALUES (3, 'dimension', 'yyyy-MM-dd', DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '[]', '2023-06-01', 'admin', '2023-06-01', 'admin');

insert into s2_view_info(`id`, `domain_id`, `type`, `config` ,`created_at`  ,`created_by`  ,`updated_at`  ,`updated_by` )
values (1, 1, 'modelEdgeRelation', '[{"source":"datasource-1","target":"datasource-3","type":"polyline","id":"edge-0.305251275235679741702883718912","style":{"active":{"stroke":"rgb(95, 149, 255)","lineWidth":1},"selected":{"stroke":"rgb(95, 149, 255)","lineWidth":2,"shadowColor":"rgb(95, 149, 255)","shadowBlur":10,"text-shape":{"fontWeight":500}},"highlight":{"stroke":"rgb(95, 149, 255)","lineWidth":2,"text-shape":{"fontWeight":500}},"inactive":{"stroke":"rgb(234, 234, 234)","lineWidth":1},"disable":{"stroke":"rgb(245, 245, 245)","lineWidth":1},"stroke":"#296df3","endArrow":true},"startPoint":{"x":-94,"y":-137.5,"anchorIndex":0,"id":"-94|||-137.5"},"endPoint":{"x":-234,"y":-45,"anchorIndex":1,"id":"-234|||-45"},"sourceAnchor":2,"targetAnchor":1,"label":"模型关系编辑"},{"source":"datasource-1","target":"datasource-2","type":"polyline","id":"edge-0.466237264629309141702883756359","style":{"active":{"stroke":"rgb(95, 149, 255)","lineWidth":1},"selected":{"stroke":"rgb(95, 149, 255)","lineWidth":2,"shadowColor":"rgb(95, 149, 255)","shadowBlur":10,"text-shape":{"fontWeight":500}},"highlight":{"stroke":"rgb(95, 149, 255)","lineWidth":2,"text-shape":{"fontWeight":500}},"inactive":{"stroke":"rgb(234, 234, 234)","lineWidth":1},"disable":{"stroke":"rgb(245, 245, 245)","lineWidth":1},"stroke":"#296df3","endArrow":true},"startPoint":{"x":-12,"y":-137.5,"anchorIndex":1,"id":"-12|||-137.5"},"endPoint":{"x":85,"y":31.5,"anchorIndex":0,"id":"85|||31.5"},"sourceAnchor":1,"targetAnchor":2,"label":"模型关系编辑"}]', '2023-06-01', 'admin', '2023-06-01', 'admin');

-- sample data
INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '周杰伦', '港台', '青花瓷', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), '周杰伦', '港台', '青花瓷', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), '周杰伦', '港台', '青花瓷', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), '周杰伦', '港台', '青花瓷', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), '周杰伦', '港台', '青花瓷', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), '周杰伦', '港台', '青花瓷', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), '周杰伦', '港台', '青花瓷', '国风', 1000000, 1000000, 1000000);


INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '陈奕迅', '港台', '爱情转移', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), '陈奕迅', '港台', '爱情转移', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), '陈奕迅', '港台', '爱情转移', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), '陈奕迅', '港台', '爱情转移', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), '陈奕迅', '港台', '爱情转移', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), '陈奕迅', '港台', '爱情转移', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), '陈奕迅', '港台', '爱情转移', '流行', 1000000, 1000000, 1000000);


INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '林俊杰', '港台', '美人鱼', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), '林俊杰', '港台', '美人鱼', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), '林俊杰', '港台', '美人鱼', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), '林俊杰', '港台', '美人鱼', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), '林俊杰', '港台', '美人鱼', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), '林俊杰', '港台', '美人鱼', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), '林俊杰', '港台', '美人鱼', '流行', 1000000, 1000000, 1000000);


INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '张碧晨', '内地', '光的方向', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), '张碧晨', '内地', '光的方向', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), '张碧晨', '内地', '光的方向', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), '张碧晨', '内地', '光的方向', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), '张碧晨', '内地', '光的方向', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), '张碧晨', '内地', '光的方向', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), '张碧晨', '内地', '光的方向', '流行', 1000000, 1000000, 1000000);


INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '程响', '内地', '人间烟火', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), '程响', '内地', '人间烟火', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), '程响', '内地', '人间烟火', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), '程响', '内地', '人间烟火', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), '程响', '内地', '人间烟火', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), '程响', '内地', '人间烟火', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), '程响', '内地', '人间烟火', '国风', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'Taylor Swift', '欧美', 'Love Story', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), 'Taylor Swift', '欧美', 'Love Story', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'Taylor Swift', '欧美', 'Love Story', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'Taylor Swift', '欧美', 'Love Story', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'Taylor Swift', '欧美', 'Love Story', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'Taylor Swift', '欧美', 'Love Story', '流行', 1000000, 1000000, 1000000);

INSERT INTO singer (imp_date, singer_name, act_area, song_name, genre, js_play_cnt, down_cnt, favor_cnt)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'Taylor Swift', '欧美', 'Love Story', '流行', 1000000, 1000000, 1000000);


---demo data for semantic and chat
insert into s2_user_department (user_name, department) values ('jack','HR');

insert into s2_user_department (user_name, department) values ('jack','HR');
insert into s2_user_department (user_name, department) values ('tom','sales');
insert into s2_user_department (user_name, department) values ('lucy','marketing');
insert into s2_user_department (user_name, department) values ('john','strategy');
insert into s2_user_department (user_name, department) values ('alice','sales');
insert into s2_user_department (user_name, department) values ('dean','marketing');


CREATE TABLE IF NOT EXISTS `s2_pv_uv_statis` (
    `imp_date` varchar(200) NOT NULL,
    `user_name` varchar(200) NOT NULL,
    `page` varchar(200) NOT NULL
    );
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'lucy', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'lucy', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'lucy', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'lucy', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'lucy', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'jack', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'lucy', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'jack', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'lucy', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'dean', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'tom', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'alice', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'john', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'dean', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'lucy', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'tom', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'jack', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'jack', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', 'p2');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'tom', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'dean', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'lucy', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'jack', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'lucy', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', 'p1');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'alice', 'p3');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'alice', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'tom', 'p4');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', 'p5');
INSERT INTO s2_pv_uv_statis (imp_date, user_name, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'tom', 'p4');






INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.7636857512911863', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'dean', '0.17663327393462436', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'alice', '0.38943688941552057', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'lucy', '0.2715819955225307', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', '0.9358210273119568', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'alice', '0.9364586435510802', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.9707723036513162', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'tom', '0.8497763866782723', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'alice', '0.15504417761372413', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.9507563118298399', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'alice', '0.9746364180572994', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'dean', '0.12869214941133378', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'lucy', '0.3024970533288409', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'tom', '0.6639702099980812', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'lucy', '0.4929901454858626', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', '0.06853040276026445', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', '0.8488086078299616', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'lucy', '0.8589111177125592', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', '0.5576357066482228', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'john', '0.8047888670006846', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'john', '0.766944548494366', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'lucy', '0.5280072184505449', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'tom', '0.9693343356046343', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'dean', '0.12805203958456424', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'dean', '0.16963603387027637', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'alice', '0.5901202956521101', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'jack', '0.12710364646712236', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', '0.6346530909156196', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'dean', '0.12461289103639872', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'john', '0.9863947334662437', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'alice', '0.48899961064192987', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'alice', '0.5382796792688207', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'dean', '0.3506568687014143', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'jack', '0.8633072449771709', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'tom', '0.13999135315363687', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'john', '0.07258740493845894', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'jack', '0.5244413940436958', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'john', '0.13258670732966138', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'john', '0.6015982054464575', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'lucy', '0.05513158944480323', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'alice', '0.6707121735296985', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'jack', '0.9330440339006469', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'dean', '0.5630674323371607', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'dean', '0.8720647566229917', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', '0.8331899070546519', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'alice', '0.6712876436249856', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'alice', '0.6694409980332703', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'john', '0.3703307480606334', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'dean', '0.775368688472696', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'lucy', '0.9151205443267096', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', '0.09543108823305857', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'dean', '0.7893992120771057', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'lucy', '0.5119923080070498', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'lucy', '0.49906724167974936', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'tom', '0.046258282700961884', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'dean', '0.44843595680103954', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'alice', '0.7743935471689718', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', '0.5855299615656824', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'lucy', '0.9412963512379853', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', '0.8383247587082538', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'lucy', '0.14517876867236124', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', '0.9327229861441061', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', '0.19042326582894153', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'jack', '0.6029067818254513', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', '0.21715964747214422', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'lucy', '0.34259842721045974', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'john', '0.7064419016593382', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'lucy', '0.5725636566517865', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'john', '0.22332539583809208', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'jack', '0.8049036189055911', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'alice', '0.6029674758974956', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'lucy', '0.11884976360561716', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'alice', '0.7124916829130662', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'jack', '0.5893693718556829', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'alice', '0.602073304496253', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'tom', '0.10491061160039927', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'dean', '0.9006548872378379', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'alice', '0.8545144244288455', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'jack', '0.16915384987875726', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'dean', '0.2271640700690446', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', '0.7807518577160636', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'john', '0.8919859648888653', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', '0.1564450687270359', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', '0.5840549187653847', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'tom', '0.2213255596777869', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'tom', '0.07868261880306426', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'jack', '0.07710010861455818', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'jack', '0.5131249730162654', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.5035035055368601', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', '0.8996978291173905', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'john', '0.057442290722216294', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', '0.6443079066865616', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'lucy', '0.7398098480748726', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'dean', '0.9835694815034591', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'john', '0.9879213445635557', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', '0.4020136688147111', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'lucy', '0.6698797170128024', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', '0.17325132416789113', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'lucy', '0.5784229486763606', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'tom', '0.9185978183932058', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', '0.5474783153973963', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'alice', '0.9730731954700215', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'tom', '0.5390873359288765', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'alice', '0.20522241320887713', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'alice', '0.4088233242325021', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', '0.7608047695853417', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'tom', '0.2749731221085713', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'john', '0.06154055374702494', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', '0.460668002022406', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'alice', '0.4474746325306228', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'alice', '0.5761666885467472', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'dean', '0.33233441360339655', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'alice', '0.7426534909874778', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'tom', '0.5841437875889118', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'alice', '0.2818296500094526', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'tom', '0.8670888843915217', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'alice', '0.5249294365740248', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'jack', '0.5483356748008438', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'dean', '0.7278566847412673', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.6779976902157362', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'lucy', '0.09995341651736978', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'jack', '0.4528538159233879', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'alice', '0.5870756885301056', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'tom', '0.9842091927290255', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'tom', '0.04580936015706816', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'alice', '0.8814678270145769', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'john', '0.06517379256096412', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'alice', '0.8769832364187129', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'dean', '0.584562279025023', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', '0.8102404090621375', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'john', '0.11481653429176686', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'jack', '0.43422888918962554', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'lucy', '0.0684414272594508', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'alice', '0.976546463969412', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.617906858141431', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'jack', '0.08663740247579998', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'lucy', '0.7124944606691416', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'alice', '0.1321700521239627', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'jack', '0.3078946609431664', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'alice', '0.6149442855237194', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'alice', '0.5963801306980994', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'lucy', '0.6999542038973406', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'john', '0.4599112653446624', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', '0.20300901401048832', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'john', '0.39989705958717037', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'jack', '0.2486378364940327', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'john', '0.16880398079144077', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', '0.73927288385526', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', '0.8645283506689198', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'alice', '0.3266940826759587', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'tom', '0.9195490073037541', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'lucy', '0.9452523036658287', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', '0.21269683438120535', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', '0.7377502855387184', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'tom', '0.38981597634408716', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', '0.7001799391999863', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'john', '0.6616720024008785', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'dean', '0.497721735058096', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'jack', '0.22255613760959603', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'jack', '0.05247640233319417', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'dean', '0.27237572107833363', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'alice', '0.9529452406380252', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'alice', '0.28243045060463157', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'lucy', '0.17880444250082506', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', '0.035050038002381156', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'lucy', '0.840803223728221', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.5318457377361356', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', '0.9280332892460665', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'lucy', '0.752354382202208', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'dean', '0.1866528331789219', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'alice', '0.7016165545791373', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', '0.4191547989960899', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'john', '0.7025516699007639', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'john', '0.6160127317884274', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'alice', '0.91223094958137', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'tom', '0.4383056089013998', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'jack', '0.595750781166582', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'lucy', '0.9472349338730268', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'jack', '0.0519104588842193', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'alice', '0.48043983034526205', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'lucy', '0.14754707786497478', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'alice', '0.36124288370035695', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'dean', '0.21777919493494613', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', '0.22637666702475057', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', '0.9378215576942598', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', '0.3309229261144562', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'alice', '0.7602880453727515', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'alice', '0.9470462487873785', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', '0.6770215935547629', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', '0.1586074803669385', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'lucy', '0.2754855564794071', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'tom', '0.8355347738454384', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'alice', '0.7251813505573811', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'jack', '0.006606625589642534', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'alice', '0.304832277753024', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.026368662837989554', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'tom', '0.6855977520602776', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'tom', '0.8193746826441749', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', '0.021179295102459972', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'jack', '0.1533849522536005', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'alice', '0.18893553542301778', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', '0.39870999343833624', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'john', '0.9985665103520182', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', '0.6961441157700171', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'tom', '0.9861933923851885', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'tom', '0.993076500099477', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'alice', '0.4320547269058953', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'lucy', '0.18441071030375877', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'jack', '0.1501504986117118', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'tom', '0.252021845734527', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'lucy', '0.24442701577183745', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'tom', '0.07563738855797564', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', '0.34247820646440985', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', '0.9456979276862031', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'alice', '0.19494357263973816', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'alice', '0.9371493867882469', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'john', '0.6136241316589367', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'alice', '0.8922330760877784', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'dean', '0.9001986074661864', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'tom', '0.4889702884422866', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'tom', '0.2689551234431401', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'dean', '0.5223573993758465', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'tom', '0.05042295556527243', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'tom', '0.2717147121880483', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'john', '0.7397093309370814', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'dean', '0.157064341631733', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'lucy', '0.7213399784998017', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'tom', '0.764081440588005', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', '0.7514070600074144', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'john', '0.611647412825278', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'tom', '0.6600796877195596', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'john', '0.8942204153751679', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.07398121085929721', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'dean', '0.1652506990439564', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.5849759516111703', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'tom', '0.1672502732600889', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'tom', '0.7836135556233219', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'dean', '0.26181269644936356', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'alice', '0.6577275876355586', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'tom', '0.3067293364197956', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'alice', '0.8608288543866495', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'john', '0.814283434116926', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'jack', '0.33993584425872936', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'john', '0.010812798859160089', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.5156558224263926', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'jack', '0.46320035330198406', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'lucy', '0.2651020283994786', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', '0.42467241545664147', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', '0.3695905136678498', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'tom', '0.15269122123348644', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', '0.6755688670583248', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'jack', '0.39064306179528907', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'john', '0.36479296691952023', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'lucy', '0.5069249157662691', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', '0.4785315495532231', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'dean', '0.7582526218052175', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.42064109605717914', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'dean', '0.5587757581237022', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'lucy', '0.3561686564964428', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', '0.7101688305173135', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'dean', '0.6518061375522985', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'tom', '0.7564485884156583', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'tom', '0.36531347293134464', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'jack', '0.5201689359070235', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'john', '0.7138792929290383', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'tom', '0.9751003716333827', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'tom', '0.5281906318027629', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', '0.6291356541485003', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'jack', '0.1938712974807698', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'john', '0.6267850210775459', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'tom', '0.4469970592043767', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'lucy', '0.7690659124175409', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'jack', '0.13335067838090386', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'jack', '0.2966621725922035', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'john', '0.5740481445089863', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'alice', '0.838028890036331', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'jack', '0.8094354537628714', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'alice', '0.5552924586108698', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'jack', '0.49150373927678315', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'dean', '0.7264346889377966', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'alice', '0.9292830287297702', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', '0.3905616258240767', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.15912349648571666', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'alice', '0.6030082006630102', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'lucy', '0.8712354035243679', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.7685306377211826', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', '0.2869913942171415', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', '0.7142615166855639', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'tom', '0.5625978475154423', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', '0.13611601734791123', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'alice', '0.6977333962685311', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'jack', '0.35140477709778295', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'john', '0.8805119222967716', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', '0.7014124236538637', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'alice', '0.12759538003439375', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', '0.7515403792213445', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'lucy', '0.03700239289885987', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'tom', '0.31674618364630946', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'dean', '0.4491378834800146', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'tom', '0.6742764131652571', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'lucy', '0.5286362221140248', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'alice', '0.007890326473113496', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'alice', '0.8046560540950831', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', '0.7198364371127147', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'tom', '0.7400546712169153', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'jack', '0.16859870460868698', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', '0.8462852684569557', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', '0.010211452005474353', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'alice', '0.8617802368201087', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'jack', '0.21667479046797633', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', '0.8667689615468714', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'jack', '0.16140709875863557', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', '0.16713368182304666', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'lucy', '0.8957484629768053', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'tom', '0.457835758220534', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'jack', '0.9435170960198477', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'jack', '0.9699253608913104', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'john', '0.2309897429566834', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'lucy', '0.7879705066452681', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', '0.20795869239817255', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'dean', '0.4110352469382019', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'jack', '0.4979592772533561', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'dean', '0.18810865430947044', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'tom', '0.5001240246982048', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'jack', '0.08341934160029707', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'lucy', '0.04812784841651041', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'alice', '0.4655982693269717', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'dean', '0.8539357978460663', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'john', '0.9649541785823592', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'john', '0.8243635648047365', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'john', '0.929949719929735', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'john', '0.055983276861168996', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'tom', '0.07845430274829746', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'alice', '0.28257674222099116', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', '0.1578419214960578', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'dean', '0.7853118484860825', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'lucy', '0.20790127125904156', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'tom', '0.8650538395535204', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', '0.902116091225815', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'lucy', '0.48542770770171373', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', '0.16725337150113984', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'lucy', '0.3157444453259486', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'tom', '0.565727220131555', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'jack', '0.2531688065358064', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'lucy', '0.9191434620980499', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'jack', '0.9224628853942058', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'jack', '0.3256288410730337', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'jack', '0.9709152566761661', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', '0.9794173893522709', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'alice', '0.16582064407977237', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'alice', '0.2652519246960059', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'alice', '0.04092489871261762', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'jack', '0.3020444893927522', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'john', '0.4655412764350543', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'dean', '0.9226436424888846', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'jack', '0.4707663393012884', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'lucy', '0.3277970119243966', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'tom', '0.4730675479071551', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'jack', '0.10261940477901954', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'alice', '0.4148892373198616', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', '0.2877219827348403', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'tom', '0.16212409974675845', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', '0.9567425121214822', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'lucy', '0.19795350030679149', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'john', '0.6954199597749198', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'alice', '0.32884293488801164', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'john', '0.4789917995407148', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'lucy', '0.0698927593996298', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', '0.3352267723792438', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'tom', '0.8085116661598726', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', '0.17515060210353794', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', '0.6006963088370202', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'alice', '0.8794167536704468', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.04091469320757368', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'tom', '0.6709116812690366', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'john', '0.4850646101328463', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'tom', '0.547488212623346', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'dean', '0.6301717145008927', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'lucy', '0.06123370093612068', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'alice', '0.2545600223228257', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'john', '0.28355287519210803', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', '0.3231348374147818', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'tom', '0.4585172495754063', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'john', '0.7893945285152268', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'john', '0.6810596014794181', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'john', '0.7136031244915907', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'jack', '0.259734039051829', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', '0.7759518703827996', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'john', '0.06288891046833589', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'dean', '0.8242980461154241', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'tom', '0.36590300307021595', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'lucy', '0.20254092528445444', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', '0.5427356081880325', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', '0.1467846603517391', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', '0.8975527268892767', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'dean', '0.3483541520806722', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'alice', '0.6922544855316723', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'tom', '0.3690185253006011', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'tom', '0.7564541265683148', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'tom', '0.3634152133342695', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'tom', '0.33740378933701987', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'lucy', '0.7942640738315301', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'jack', '0.7894896778233523', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'jack', '0.7153281477198108', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'tom', '0.5546359859065261', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'john', '0.7727157385809087', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'dean', '0.8707097754747494', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'john', '0.3873936520764878', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'alice', '0.7590305068820566', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'john', '0.512826935863365', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'john', '0.19120284727846926', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'dean', '0.5382693105670825', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'john', '0.826241649014955', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'lucy', '0.6133080470571559', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'jack', '0.6452862617544055', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'lucy', '0.3025772179023586', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'lucy', '4.709864550322962E-4', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'dean', '0.024816355013726588', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 6 DAY), 'alice', '0.8407500495605565', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'alice', '0.8420879584266481', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'lucy', '0.2719224735814776', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'tom', '0.8939712577294938', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'dean', '0.8086189323362379', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'tom', '0.6063415085381448', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'tom', '0.39783242658234674', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'tom', '0.6085577206028068', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'tom', '0.5154289424127074', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'john', '0.878436600887031', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'john', '0.5577906295015223', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'lucy', '0.1143260282925247', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'jack', '0.312756557275364', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', '0.05548807854726956', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'tom', '0.12140791431139175', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.23897628700410234', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'lucy', '0.22223137342481392', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'john', '0.12379891645900953', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'john', '0.33729146112854247', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.8816768640060831', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 21 DAY), 'jack', '0.6301700633426532', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), 'alice', '0.4566295223861714', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'john', '0.1777378523933678', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'tom', '0.8163769471165477', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'tom', '0.4380805149704541', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'alice', '0.2987018822475964', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'dean', '0.6726495645391617', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'alice', '0.8394327461109705', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'dean', '0.820512945501936', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'tom', '0.1580105370757261', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), 'jack', '0.9961450897279505', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY), 'john', '0.6574891890500061', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'john', '0.5201205570085158', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'alice', '0.2445069633928285', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), 'john', '0.3155229654901067', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'jack', '0.3665971881269575', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'john', '0.5544977915912215', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'tom', '0.15978771803015113', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'lucy', '0.038128748344929186', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'tom', '0.49026304025118594', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', '0.5166802080526571', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'alice', '0.22568230066042194', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 28 DAY), 'john', '0.9888634109849955', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'jack', '0.21022365182102054', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'john', '0.47052993358031114', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', '0.25686122383263454', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', '0.18929054223320718', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'jack', '0.7925339862375451', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 12 DAY), 'john', '0.12613308249498645', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.7381524971311578', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), 'alice', '0.08639585437319919', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 27 DAY), 'tom', '0.9519897106846164', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', '0.33446548574801926', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'jack', '0.40667134603483324', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'jack', '0.17100718420628735', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 26 DAY), 'lucy', '0.4445585525686886', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'tom', '0.47372916928883013', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'john', '0.19826861093848824', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 13 DAY), 'john', '0.13679268112019338', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 24 DAY), 'tom', '0.9805515708224516', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'dean', '0.4738376165601095', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'dean', '0.5739441073158964', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'alice', '0.8428505498030564', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'lucy', '0.32655416551155336', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 14 DAY), 'tom', '0.7055736367780644', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 'tom', '0.9621355090189875', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 9 DAY), 'jack', '0.9665339161730553', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'dean', '0.44309781869697995', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), 'tom', '0.8651220802537761', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'lucy', '0.6451892308277741', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), 'dean', '0.056797307451316725', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'lucy', '0.6847604118085596', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 23 DAY), 'jack', '0.13428051757364667', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 29 DAY), 'lucy', '0.9814797176951834', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 11 DAY), 'tom', '0.7386074051153445', 'p3');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), 'alice', '0.4825297824657663', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'alice', '0.06608870508231235', 'p5');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), 'lucy', '0.6278253028988848', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 'alice', '0.6705580511822682', 'p1');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 19 DAY), 'alice', '0.8131712486302015', 'p2');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), 'lucy', '0.8124302447925607', 'p4');
INSERT INTO s2_stay_time_statis (imp_date, user_name, stay_hours, page) VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 8 DAY), 'lucy', '0.039935860913407284', 'p2');



insert into genre(g_name,rating,most_popular_in) VALUES ('tagore',8,'孟加拉国');
insert into genre(g_name,rating,most_popular_in) VALUES ('nazrul',7,'孟加拉国');
insert into genre(g_name,rating,most_popular_in) VALUES ('民间',9,'锡尔赫特、吉大港、库斯蒂亚');
insert into genre(g_name,rating,most_popular_in) VALUES ('现代',8,'孟加拉国');
insert into genre(g_name,rating,most_popular_in) VALUES ('蓝调',7,'加拿大');
insert into genre(g_name,rating,most_popular_in) VALUES ('流行',9,'美国');

insert into artist(artist_name,country,gender,g_name) VALUES ('Shrikanta','印度','男性','tagore');
insert into artist(artist_name,country,gender,g_name) VALUES ('Prity','孟加拉国','女性','nazrul');
insert into artist(artist_name,country,gender,g_name) VALUES ('Farida','孟加拉国','女性','民间');
insert into artist(artist_name,country,gender,g_name) VALUES ('Topu','印度','女性','现代');
insert into artist(artist_name,country,gender,g_name) VALUES ('Enrique','美国','男性','蓝调');
insert into artist(artist_name,country,gender,g_name) VALUES ('Michel','英国','男性','流行');

insert into files(f_id,artist_name,file_size,duration,formats) VALUES (1,'Shrikanta','3.78 MB','3:45','mp4');
insert into files(f_id,artist_name,file_size,duration,formats) VALUES (2,'Prity','4.12 MB','2:56','mp3');
insert into files(f_id,artist_name,file_size,duration,formats) VALUES (3,'Farida','3.69 MB','4:12','mp4');
insert into files(f_id,artist_name,file_size,duration,formats) VALUES (4,'Enrique','4.58 MB','5:23','mp4');
insert into files(f_id,artist_name,file_size,duration,formats) VALUES (5,'Michel','5.10 MB','4:34','mp3');
insert into files(f_id,artist_name,file_size,duration,formats) VALUES (6,'Topu','4.10 MB','4:30','mp4');

INSERT INTO song (imp_date, song_name, artist_name, country, f_id, g_name, rating, languages, releasedate, resolution)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 0 DAY), 'Tumi 长袍 尼罗布', 'Shrikanta', '印度', 1, 'tagore', 8, '孟加拉语', '2011-08-28', 1080);

INSERT INTO song (imp_date, song_name, artist_name, country, f_id, g_name, rating, languages, releasedate, resolution)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 0 DAY), '舒克诺 帕塔尔 努普尔 帕埃', 'Prity', '孟加拉国', 2, 'nazrul', 5, '孟加拉语', '1997-09-21', 512);

INSERT INTO song (imp_date, song_name, artist_name, country, f_id, g_name, rating, languages, releasedate, resolution)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 0 DAY), '阿米·奥帕尔·霍伊', 'Farida', '孟加拉国', 3, '民间', 7, '孟加拉语', '2001-04-07', 320);

INSERT INTO song (imp_date, song_name, artist_name, country, f_id, g_name, rating, languages, releasedate, resolution)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 0 DAY), '我的爱', 'Enrique', '美国', 4, '蓝调', 6, '英文', '2007-01-24', 1080);

INSERT INTO song (imp_date, song_name, artist_name, country, f_id, g_name, rating, languages, releasedate, resolution)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 0 DAY), '打败它', 'Michel', '英国', 5, '流行', 8, '英文', '2002-03-17', 720);

INSERT INTO song (imp_date, song_name, artist_name, country, f_id, g_name, rating, languages, releasedate, resolution)
VALUES (DATE_SUB(CURRENT_DATE(), INTERVAL 0 DAY), '阿杰伊阿卡什', 'Topu', '印度', 6, '现代', 10, '孟加拉语', '2004-03-27', 320);
-- benchmark
