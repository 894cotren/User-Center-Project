-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '主键'    primary key,
    username      varchar(256)                       null comment '昵称',
    user_account  varchar(256)                       null comment '账号',
    avatar_url    varchar(1024)                      null comment '用户头像',
    gender        tinyint                            null comment '性别',
    user_password varchar(512)                       not null comment '用户密码',
    phone         varchar(128)                       null comment '电话',
    email         varchar(512)                       null comment '邮箱',
    user_status   int      default 0                 not null comment '账号状态 ： 0-正常',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint  default 0                 not null comment '是否删除',
    user_role     int      default 0                 not null comment '用户角色: 0-普通用户  1-管理员',
    planet_code   varchar(512)                       null comment '星球编号',
    user_tags     varchar(1024)                      null comment '用户的标签'
)
    comment '用户表';



-- 标签表
create table tag
(
    id          bigint auto_increment comment '主键'
        primary key,
    tag_name    varchar(256)                       null comment '标签名称',
    user_id     bigint                             null comment '上传标签的用户id',
    parent_id   bigint                             null comment '父标签id',
    is_parent   tinyint                            null comment '是否为父标签 0-不是， 1-是',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    constraint unique_tag_name
        unique (tag_name)
)
    comment '标签表';
