package com.example.projectskillswap;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void registerUser(UserEntity user);

    @Query("SELECT * FROM users_table WHERE email = :email AND password = :password LIMIT 1")
    UserEntity login(String email, String password);

    @Query("SELECT * FROM users_table WHERE email = :email LIMIT 1")
    UserEntity getUserByEmail(String email);
}
