package org.example.moviesplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Integer id;

    private String name;        // Məsələn: ROLE_USER, ROLE_ADMIN

    private String description; // Rolun nə işə yaradığı barədə qısa məlumat
}
