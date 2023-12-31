package pl.adrianpacholak.userservice.service.client;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.adrianpacholak.userservice.model.ERole;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final Keycloak keycloak;

    @Value("${keycloak.realm.name}")
    private String realmName;

    public void createNewUser(String username, String password, ERole role) {
        UserRepresentation userRepresentation = buildUserRepresentation(username, password);

        Response response = keycloak.realm(realmName)
                .users()
                .create(userRepresentation);

        if (response.getStatus() >= 400) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot create user.");
        }

        String userId = findUserIdByUsername(username);

        UserResource user = findUserById(userId);

        addRoleToUser(user, role);
    }

    private void addRoleToUser(UserResource user, ERole role) {
        RoleRepresentation roleRepresentation = findRole(role);

        user.roles()
                .realmLevel()
                .add(List.of(roleRepresentation));
    }

    private RoleRepresentation findRole(ERole role) {
        return keycloak.realm(realmName)
                .roles()
                .get(role.name())
                .toRepresentation();
    }

    private UserResource findUserById(String userId) {
        return keycloak.realm(realmName)
                .users()
                .get(userId);
    }

    private String findUserIdByUsername(String username) {
        return keycloak.realm(realmName)
                .users()
                .search(username)
                .get(0)
                .getId();
    }

    private UserRepresentation buildUserRepresentation(String username, String password) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(username);
        userRepresentation.setCredentials(buildCredentialsRepresentation(password));
        return userRepresentation;
    }

    private List<CredentialRepresentation> buildCredentialsRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(password);
        return List.of(credentialRepresentation);
    }
}
