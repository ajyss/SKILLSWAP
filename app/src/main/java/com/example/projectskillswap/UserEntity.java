package com.example.projectskillswap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users_table")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fullName;
    public String email;
    public String password;

    public UserEntity(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}
