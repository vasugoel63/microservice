package com.example.billservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billservice.entities.LineItem;

public interface LineItemRepository extends JpaRepository<LineItem, UUID> {

}