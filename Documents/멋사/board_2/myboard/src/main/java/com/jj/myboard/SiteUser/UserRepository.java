package com.jj.myboard.SiteUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    @Modifying
    @Query("DELETE FROM SiteUser")
    void deleteAllSiteUsers();

    Optional<SiteUser> findByUsername(String username);
}
