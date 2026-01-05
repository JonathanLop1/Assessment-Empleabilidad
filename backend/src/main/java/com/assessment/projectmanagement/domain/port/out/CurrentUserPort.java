package com.assessment.projectmanagement.domain.port.out;

import java.util.UUID;

/**
 * Output port for getting current authenticated user
 */
public interface CurrentUserPort {
    UUID getCurrentUserId();
}
