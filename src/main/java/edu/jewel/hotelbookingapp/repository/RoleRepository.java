package edu.jewel.hotelbookingapp.repository;

import edu.jewel.hotelbookingapp.model.Role;
import edu.jewel.hotelbookingapp.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleType(RoleType roleType);
}
