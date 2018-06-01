package com.itberries2018.demo.auth.entities;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "users_")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "user_name")
    @JsonProperty("username")
    private String username;

    @Column(name = "email")
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    @Column(name = "password")
    private String password;

    @Column(name = "avatar")
    @JsonProperty("avatar")
    private String avatar;


    public User() {

    }

    @JsonManagedReference
    private Set<History> historySet = new HashSet<>();


    public void setHistorySet(Set<History> historySet) {
        this.historySet = historySet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @JsonCreator
    public User(@JsonProperty(value = "username") String username, @JsonProperty(value = "email") String email,
                @JsonProperty(value = "password") String password, @JsonProperty(value = "avatar") String avatar) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.historySet = new HashSet<>();
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = "noavatar.png";
        this.historySet = new HashSet<>();
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final User other = (User) obj;
        //noinspection RedundantIfStatement
        if (!Objects.equals(id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username
                + '\'' + ", email='" + email
                + '\'' + ", password='" + password
                + '\'' + ", avatar='" + avatar + '\'' + '}';
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(targetEntity = History.class, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<History> getHistorySet() {
        return historySet;
    }

}
