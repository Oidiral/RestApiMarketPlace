package org.olzhas.projectnic.repository;

import org.olzhas.projectnic.entity.User;
import org.olzhas.projectnic.entity.projection.UsersAdminProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u")
    Page<UsersAdminProjection> findAllUsers(Pageable pageable);

    @Query("SELECT u FROM User u " +
            "WHERE u.email =:email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.verificationTokens " +
            "WHERE u.email = :email")
    Optional<User> findByEmailWithVerificationToken(@Param("email") String email);

    @Query("SELECT u FROM User u " +
            "WHERE u.username =:username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u.id as id, u.username as username, u.firstName as firstName, " +
            "u.lastName as lastName, u.email as email, u.isActive as isActive, u.role as role, " +
            "u.createdAt as createdAt FROM User u " +
            "WHERE u.id IN :ids")
    Page<UsersAdminProjection> findAllByIdIn(Pageable pageable, @Param("ids") List<Long> ids);

    @Query("SELECT COUNT(u) > 0 FROM User u " +
            "WHERE u.id = :id " +
            "AND u.role = 'ROLE_ADMIN'")
    Boolean isAdmin(@Param("id") Long id);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
