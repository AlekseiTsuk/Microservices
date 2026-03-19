package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.exception.BackendResourcesException;
import com.itm.space.backendresources.mapper.UserMapper;
import com.itm.space.backendresources.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private Keycloak keycloakClient;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RealmResource realmResource;
    @Mock
    private UsersResource usersResource;
    @Mock
    private UserResource userResource;
    @Mock
    private RoleScopeResource roleScopeResource;
    @Mock
    private RoleMappingResource roleMappingResource;
    @Mock
    private MappingsRepresentation mappingsRepresentation;

    @InjectMocks
    private UserServiceImpl userService;

    private final String realm = "test-realm";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "realm", realm);
    }

    @Test
    void createUser_ShouldSuccess() {
        UserRequest request = new UserRequest("username", "mail@test.com", "pass", "First", "Last");

        RealmResource realmResource = mock(RealmResource.class);
        UsersResource usersResource = mock(UsersResource.class);

        when(keycloakClient.realm(realm)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        Response response = Response.created(URI.create("user-id")).build();
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);

        userService.createUser(request);

        verify(usersResource).create(any(UserRepresentation.class));
    }

    @Test
    void getUserById_Success() {

        UUID userId = UUID.randomUUID();
        String userIdStr = String.valueOf(userId);

        UserRepresentation mockUser = new UserRepresentation();
        List<RoleRepresentation> mockRoles = List.of(new RoleRepresentation());
        List<GroupRepresentation> mockGroups = List.of(new GroupRepresentation());
        UserResponse expectedResponse = new UserResponse(
                "First", "Last", "email@test.com", null, null);


        when(keycloakClient.realm(any())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(userIdStr)).thenReturn(userResource);

        when(userResource.toRepresentation()).thenReturn(mockUser);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.getAll()).thenReturn(mappingsRepresentation);
        when((mappingsRepresentation).getRealmMappings()).thenReturn(mockRoles);
        when(userResource.groups()).thenReturn(mockGroups);

        when(userMapper.userRepresentationToUserResponse(mockUser, mockRoles, mockGroups))
                .thenReturn(expectedResponse);

        UserResponse actualResponse = userService.getUserById(userId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(userResource).toRepresentation();
    }

    @Test
    void getUserById_ShouldThrowException_WhenKeycloakFails() {
        UUID userId = UUID.randomUUID();
        when(keycloakClient.realm(realm)).thenThrow(new RuntimeException("Keycloak down"));

        assertThrows(BackendResourcesException.class, () -> userService.getUserById(userId));
    }
}
