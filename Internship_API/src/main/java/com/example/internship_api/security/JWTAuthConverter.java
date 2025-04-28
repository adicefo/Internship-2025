package com.example.internship_api.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JWTAuthConverter implements Converter<Jwt, AbstractAuthenticationToken>{
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;


    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        // Validate the audience claim
        validateAudience(jwt);

        Collection<GrantedAuthority> authorities= Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt)
                        .stream(),
                extractResourceRoles(jwt)
                        .stream()
        ).collect(Collectors.toSet());
        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPrincipleClaimName(jwt)
        );
    }

    /**
     * Validates the audience claim in the JWT token.
     * Accepts both "account" and the configured resourceId as valid audiences.
     */
    private void validateAudience(Jwt jwt) {
        // Get the audience claim
        List<String> audiences = jwt.getClaimAsStringList("aud");

        // If there's no audience claim, throw an exception
        if (audiences == null || audiences.isEmpty()) {
            throw new JwtException("Missing audience claim");
        }

        // Check if "account" or your resource ID is in the audience list
        boolean validAudience = audiences.contains("account") || audiences.contains(resourceId);

        // If neither "account" nor your resource ID is in the audience list, throw an exception
        if (!validAudience) {
            throw new JwtException("Invalid audience: " + audiences);
        }
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String,Object> resourceAccess;
        Map<String,Object> resource;
        Collection<String>resourceRoles;
        if(jwt.getClaim("resource_access")==null){
            return Set.of();
        }
        resourceAccess = jwt.getClaim("resource_access");
        if(resourceAccess.get(resourceId)==null){
            return Set.of();
        }
        resource = (Map<String, Object>) resourceAccess.get(resourceId);
        resourceRoles=(Collection<String>)resource.get("roles");
        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.toUpperCase())) // Add ROLE_ prefix and convert to uppercase
                .collect(Collectors.toSet());
    }
    private String getPrincipleClaimName(Jwt jwt) {
        String claimName= JwtClaimNames.SUB;
        if(principleAttribute!=null)
            claimName=principleAttribute;
        return jwt.getClaim(claimName);

    }
}