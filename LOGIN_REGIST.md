# Login dan Registrasi

## Base URL
```
https://crud.trisnawan.my.id
```

## Login
```
POST /login
```
Parameter
```
email = String (wajib)
password = String (wajib)
```
Contoh Response:
```json
{
    "status": true,
    "message": "Success",
    "data": {
        "id": "8de8a43f-c2c1-4c96-a046-d2a7908c6dc5",
        "name": "Trisnawan",
        "email": "halo.trisnasejati@gmail.com",
        "phone": "087719734045",
        "image": "",
        "created_at": "2024-06-01 11:37:29"
    }
}
```

## Registration
```
POST /registration
```
Parameter
```
name = String (wajib)
email = String (wajib)
phone = String (wajib)
password = String (wajib)
```
Contoh Response:
```json
{
    "status": true,
    "message": "Success",
    "data": {
        "id": "8de8a43f-c2c1-4c96-a046-d2a7908c6dc5",
        "name": "Trisnawan",
        "email": "halo.trisnasejati@gmail.com",
        "phone": "087719734045",
        "image": "",
        "created_at": "2024-06-01 11:37:29"
    }
}
```
