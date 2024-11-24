package org.olzhas.projectnic.entity.projection;

import org.olzhas.projectnic.entity.Role;

import java.time.LocalDateTime;

/**
 * Projection for {@link org.olzhas.projectnic.entity.Users}
 */
public interface UsersAdminProjection {
    long getId();

    String getUsername();

    String getFirstName();

    String getLastName();

    String getEmail();

    boolean isIsActive();

    Role getRole();

    LocalDateTime getCreatedAt();
}