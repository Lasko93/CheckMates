package com.example.chessappgroupd.domain.pgn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface pgnRepository extends JpaRepository<pgnFile,Long> {

    @Query("SELECT f FROM pgnFile f WHERE f.id = :id")
    pgnFile findByFileId(@Param("id") Long id);

    @Query("SELECT f from pgnFile f WHERE f.username = :username")
    List<pgnFile> findByUsername(@Param("username") String username);
}
