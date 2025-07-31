package com.example.domibe.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import javax.management.relation.Role;
import java.util.Date;

@Entity
@Getter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String accountId;
  private String password;
  private int grade;
  private int classroom;
  private int number;
  private Role role;
  private Date createdAt;
}
