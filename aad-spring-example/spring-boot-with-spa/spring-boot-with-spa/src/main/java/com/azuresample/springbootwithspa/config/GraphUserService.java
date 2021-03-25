package com.azuresample.springbootwithspa.config;

import com.azuresample.springbootwithspa.constants.UserRoles;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.graph.auth.confidentialClient.ClientCredentialProvider;
import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IDirectoryObjectCollectionWithReferencesPage;
import com.microsoft.graph.requests.extensions.IUserRequestBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class GraphUserService {
    static Set<String> GSR_ROLES = EnumUtils.getEnumList(UserRoles.class)
                                    .stream()
                                    .map(UserRoles::getAccessRoles)
                                    .collect(Collectors.toSet());

    static String GROUP_PATTERN = "^(MIM_Q11-)[a-zA-Z0-9]{3}(_)";

    @Value("#{'${azure.activedirectory.user-group.allowed-groups}'.split(',')}")
    private List<String> allowedGroups;

    @Value("${azure.activedirectory.tenant-id}")
    private String tenant;
    @Value("${azure.activedirectory.client-id}")
    private String clientId;
    @Value("${azure.activedirectory.client-secret}")
    private String clientSecret;
    private String userPrincipal;
    private IGraphServiceClient graphClient;

    GraphUserService(OAuth2ClientProperties clientproperties) {
        clientproperties.getProvider().keySet().stream().findFirst()
                .ifPresent(provider -> {
                    OAuth2ClientProperties.Registration registration =
                            clientproperties.getRegistration().get(provider);
                    this.clientId = registration.getClientId();
                    this.clientSecret = registration.getClientSecret();
                });
    }
    public void initGraphClient(String userPrincipal){
        this.userPrincipal = userPrincipal;

        ClientCredentialProvider authProvider = new ClientCredentialProvider(
                clientId,
                Collections.singletonList("https://graph.microsoft.com/.default"),
                clientSecret,
                tenant,
                NationalCloud.Global);
        graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
    }

    public Set<String> getGsrRoles() throws OAuth2AuthorizationException {
        Set<String> roles = new HashSet<>();

        IUserRequestBuilder user = getUserBuilder();
        IDirectoryObjectCollectionWithReferencesPage members = user.memberOf().buildRequest().get();

        Optional<JsonArray> userGroups = members.getRawObject().entrySet()
                                            .stream()
                                            .filter((x -> x.getKey().equalsIgnoreCase("value")))
                                            .map(x -> x.getValue().getAsJsonArray())
                                            .findFirst();

        if(userGroups.isPresent()){
            JsonArray jsonElements = userGroups.get();

            roles = StreamSupport.stream(jsonElements.spliterator(),false)
                                .map(JsonElement::getAsJsonObject)
                                .map(group -> group.get("displayName").getAsString())
                                .filter(allowedGroups::contains)
                                .map(role -> role.replaceAll(GROUP_PATTERN," "))
                                .filter(GSR_ROLES::contains)
                                .collect(Collectors.toSet());
        }
        if(roles.isEmpty()){
            OAuth2Error error = new OAuth2Error(String.format("No GSR roles found for the user [%s]",userPrincipal));
            throw new OAuth2AuthorizationException(error);
        }
        return roles;
    }

    private IUserRequestBuilder getUserBuilder() {
        return graphClient.users(userPrincipal);
    }
}
