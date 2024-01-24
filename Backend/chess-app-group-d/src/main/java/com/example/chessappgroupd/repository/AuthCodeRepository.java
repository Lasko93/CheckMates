package com.example.chessappgroupd.repository;
import com.example.chessappgroupd.domain.appUser.AuthCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Repository
@Transactional
//@Transactional ist eine Annotation in der Java-Programmiersprache, die verwendet wird, um die Transaktionsverwaltung in einer Anwendung zu vereinfachen.
// Mit @Transactional können Sie angeben, dass eine Methode oder ein Klassenblock in einer Transaktion ausgeführt werden soll.
// Wenn die Transaktion erfolgreich ist, werden die Änderungen an der Datenbank gespeichert.
// Wenn die Transaktion fehlschlägt, werden die Änderungen rückgängig gemacht.
//AuthCode repo
public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    @NotNull Optional<AuthCode> findById(@NotNull Long id);
    @Query("SELECT CASE WHEN COUNT(ac) > 0 THEN true ELSE false END FROM AuthCode ac WHERE ac.user.userName = :userName")
    Boolean existsByUserName(@Param("userName") String userName);
    @Modifying
    @Query("DELETE FROM AuthCode ac WHERE ac.user.userName = :userName")
    void deleteByUserName(@Param("userName") String userName);
}