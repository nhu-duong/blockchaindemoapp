# USER MANAGEMENT - SHOPBACK
## ASSUMPTIONS
1. User can log in from many devices at the same time.
2. API signup also assign user role for user.
3. Default language is English

## API DESCRIPTIONS
General information:
+ There are 2 pre-built roles for user: admin & visitor
+ There are 2 supported devices: ios, android
+ There are 2 supported languages: en, vi

1. Signup
 When signing up, caller should also provide appropriate role for user.
```
curl -X POST \
  http://localhost:3000/api/auth/signup \
  -H 'Content-Type: application/json' \
  -H 'x-device: ios' \
  -H 'x-language: vi' \
  -d '{
	"email": "admin@test.com",
	"password": "123456",
	"name": "Test Admin",
	"role": "admin"
}'
```
2. Signin
```
curl -X POST \
  http://localhost:3000/api/auth/signin \
  -H 'Content-Type: application/json' \
  -H 'X-Device: email' \
  -H 'X-Language: en' \
  -d '{
	"email": "admin@test.com",
	"password": "123456"
}'
```
3. Search
+ For admin only.
+ Search by email: search for exact value
+ Search by name: search for users who have name contain the search term.
+ Search by latest access using 2 parameters: last_access_from and last_access_to
+ Pagination using 2 parameter: page (>= 1) and size (>= 1: items per page)
```
curl -X GET \
  'http://localhost:3000/api/user/search?name=admin&email=admin@test.com&last_access_to=1555263842&last_access_from=1555263840&page=1&size=10' \
  -H 'Content-Type: application/json' \
  -H 'uuid: r7QSBlHzLHvOyNrBsession15552670781555267150'
```

4. Signout
```
curl -X GET \
  http://localhost:3000/api/auth/signout \
  -H 'Content-Type: application/json' \
  -H 'uuid: r7QSBlHzLHvOyNrBsession15552670781555267198'
```

5. ACL
There are 2 roles: admin and visitor. For demo purpose, acl configuration is stored in /middlewares/acl.js
It can be extended to more roles easily.
In real world application, it can be managed in database by simple CRUD

6. Data formatter (converter)
Formatters are located in /formatters/*.js. To create new device, just simply create file and register it in /libs/format_manager.js
Formatters will override the original data from controller only if special format for that device is defined. Otherwise, it return the original data.
Formatter only supports output data conversion. Input conversion is not implemented in the demo but can be implemented easily using the same mechanism.

7. Multi-language
There are 2 pre-built languages: vi and en. To create new languages: copy file /languages/en.js and replace the content.

## TODOs
1. Write unit test
2. Mantain database files sessions during the application life time.
3. Moving configuration to environment setting.
4. Cleanup expired uuids.
5. More flexible router: support parameters in url, support more methods, support regular expression.
6. More flexible router: support router group.
