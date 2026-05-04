package com.chy.mapper;

import com.chy.pojo.RefreshToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RefreshTokenMapper {

    @Insert("insert into refresh_token(user_id, token_hash, expires_at) values(#{userId}, #{tokenHash}, #{expiresAt})")
    void insert(RefreshToken refreshToken);

    @Select("select id, user_id, token_hash, expires_at, created_at from refresh_token " +
            "where token_hash = #{hash} and expires_at > now()")
    RefreshToken findValidToken(String hash);

    @Insert("insert into refresh_token_blacklist(token_hash) values(#{hash})")
    void addToBlacklist(String hash);

    @Select("select count(1) from refresh_token_blacklist where token_hash = #{hash}")
    int countInBlacklist(String hash);

    @Delete("delete from refresh_token where expires_at < now()")
    void deleteExpired();

    @Delete("delete from refresh_token_blacklist where invalidated_at < date_sub(now(), interval 30 day)")
    void cleanOldBlacklist();
}
