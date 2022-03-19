# Banking API

## Task

We would like you to create a banking application that supports the following operations via API

+ Creation of account
+ Deletion of account
+ Money deposit
+ Money withdrawal
+ Money transfer
+ International transfer
+ Getting the balance
+ List of accounts

## Tips

Enable annotations processing for development.

All accounts are in USD by default.

## Swagger

API can be tested via Swagger
http://localhost:8080/swagger-ui/index.html

## Methods

#### Creation of account

+ Returns 201 if succeed
+ 400 if accountId already exists

curl -X 'POST' \
'http://localhost:8080/accounts' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{
"accountId": "accountId2",
"owner": "ownerName"
}'

#### Deletion of account

+ 200 if deleted
+ 404 if account not found

curl -X 'DELETE' \
'http://localhost:8080/accounts/4' \
-H 'accept: application/json'

#### Money deposit

+ 404 if account not found

curl -X 'POST' \
'http://localhost:8080/accounts/1/deposit/10' \
-H 'accept: application/json' \
-d ''

#### Money withdrawal

+ 404 if account not found
+ 400 if there is not enough money
+ 200 if succeed

curl -X 'POST' \
'http://localhost:8080/accounts/1/withdrawal/1.3333333' \
-H 'accept: application/json' \
-d ''

#### Money transfer

+ 404 if account not found
+ 400 if there is not enough money
+ 200 if succeed

curl -X 'POST' \
'http://localhost:8080/transfer' \
-H 'Content-Type: application/json' \
-d '{
"fromAccountId": "1",
"toAccountId": "2",
"amount": 10 }'

#### International transfer

Available currencies are [UAE, EUR, USD, RUB, CNY]

+ 404 if account not found
+ 400 if there is not enough money
+ 200 if succeed

curl -X 'POST' \
'http://localhost:8080/internationalTransfer' \
-H 'Content-Type: application/json' \
-d '{
"fromAccountId": "1",
"toAccountId": "2",
"amount": 5,
"currency": "EUR"
}'

#### Getting the balance

+ 404 if account not found 200 if succeed

curl -X 'GET' \
'http://localhost:8080/accounts/2/balance' \
-H 'accept: application/json'

#### List of accounts

return 200 curl -X 'GET' \
'http://localhost:8080/accounts' \
-H 'accept: application/json'