#Deposit application security:

###To take token we should use this link (actual for default keycloak local instance):
```

POST
http://localhost:8080/auth/realms/DepositApplicationRealm/protocol/openid-connect/token

x-www-form-urlencoded

client_id: <deposit-app-user-client-id>
username: <depost-app-user-name>
password: <depost-app-user-password>
grant_type:password

```

