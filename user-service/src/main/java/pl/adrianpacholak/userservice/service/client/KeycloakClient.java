package pl.adrianpacholak.userservice.service.client;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
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
        Response response = keycloak.realm(realmName)
                .users()
                .create(buildUserRepresentation(username, password, role));

        if (response.getStatus() >= 400) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot create user.");
        }
    }

    private UserRepresentation buildUserRepresentation(String username, String password, ERole role) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(username);
        userRepresentation.setCredentials(buildCredentialsRepresentation(password));
        userRepresentation.setRealmRoles(List.of(role.name()));
        return userRepresentation;
    }

    private List<CredentialRepresentation> buildCredentialsRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setCredentialData(password);
        return List.of(credentialRepresentation);
    }
}
