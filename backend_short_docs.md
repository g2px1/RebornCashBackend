# Web3 service

## URLs

#### Main URL

```java
["/web3Service/"]
```

#### Secondary URLs

```java
["/deposit", "/withdraw"]
```

## Actions

- Deposit[^web3Deposit]

- Withdraw[^web3Withdraw]

[^web3Deposit]: Deposit should check transaction by it's hash and chain name and increase balance through balanceService
[^web3Withdraw]: Withdraw sould decrease users' balance through balanceService

## Usages

- no usages

# User service

## URLs

#### Main URL

```java
["/userService/"]
```

#### Secondary URLs

```java
["/getUser/{name}", "/saveUser", "/isExists/{name}", "/isExistsByEmail/{email}", "/isExistsByAll/{name}/{email}", "/changePasswordIfExists/{name}/{password}/{code}"]
```

## Actions

- Get user[^userServiceGetUser]

- Save user[^userServiceSaveUser]
- Is exist user by name[^userServiceExistByName]
- Is exist user by email[^userServiceExistByEmail]
- Is exist user by all[^userServiceExistByAll]
- Change password[^userServiceChangePassword]

[^userServiceGetUser]: Returns user's data by name
[^userServiceSaveUser]: Saving user or changes
[^userServiceExistByName]:  Returns boolean depends on existing by name
[^userServiceExistByEmail]:  Returns boolean depends on existing by email
[^userServiceExistByAll]:  Returns boolean depends on existing by both: email and name
[^userServiceChangePassword]:  Returns boolean depends on changing password

## Usages

- Web3Service



# 
