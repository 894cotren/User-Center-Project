CREATE TABLE user
(
    id           BIGINT AUTO_INCREMENT COMMENT '主键'
        PRIMARY KEY,
    username     VARCHAR(256)                       NULL COMMENT '昵称',
    user_account VARCHAR(256)                       NULL COMMENT '账号',
    avatar_url   VARCHAR(1024)                      NULL COMMENT '用户头像',
    gender       TINYINT                            NULL COMMENT '性别',
    user_password VARCHAR(512)                      NOT NULL COMMENT '用户密码',
    phone        VARCHAR(128)                       NULL COMMENT '电话',
    email        VARCHAR(512)                       NULL COMMENT '邮箱',
    user_status  INT      DEFAULT 0                 NOT NULL COMMENT '账号状态 ： 0-正常',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    update_time  DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete    TINYINT  DEFAULT 0                 NOT NULL COMMENT '是否删除',
    user_role    INT      DEFAULT 0                 NOT NULL COMMENT '用户角色: 0-普通用户  1-管理员',
    planet_code  VARCHAR(512)                       NULL COMMENT '星球编号'
)
    COMMENT '用户表';
