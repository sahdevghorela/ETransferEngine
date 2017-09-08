# ETransferEngine. 
  
# Objective. 
Design and implement a RESTful API (including data model and the backing implementation) for money
transfers between internal users/accounts.  

# Build and Run. 
1. To compile project   
  cd_to_etransfer_parent_project> mvn clean install  
2. To run project and create rest endpoints  
  cd etransfer-main-app   
  mvn exec:java  
  
  
# RestEndpoints created with sample requests
1. Create Account with zero or more balance  
 Zero Balance account: http://localhost:7777/revolut/account/create  
 Account with balance: http://localhost:7777/revolut/account/create?balance=100  
2. Find account  
  Find account by account number: http://localhost:7777/revolut/account/find/{accountNumberToFind}  
  Example request: http://localhost:7777/revolut/account/find/11  
3. Delete Account  
  Delete account by account number: http://localhost:7777/revolut/account/delete/{accountNumberToDelete}  
  Example request: http://localhost:7777/revolut/account/delete/11  
4. Transfer money between accounts  
  Transfer money endpoint: http://localhost:7777/revolut/account/transfer?from={sourceAccountNumber}&to={targetAccountNuber}&amount={amountToTransfer}  
  Example request: http://localhost:7777/revolut/account/transfer?from=12&to=11&amount=50  
