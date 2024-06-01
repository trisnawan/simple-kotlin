# LATIHAN CLIENT API ASYNC TASK & RECYCLER VIEW

## Base URL
```
https://crud.trisnawan.my.id
```

## List Artikel
```
GET /article
```
Contoh response
```json
{
    "status": true,
    "data": [
        {
            "id": "972c5fa7-58dc-4f6b-9bf5-65158ded079a",
            "slug": "jakarta-on-the-night-665981fb93467",
            "title": "Jakarta on the night",
            "content": "Lorem ipsum dolor sit amet",
            "image": "https://crud.trisnawan.my.id/contents/blog/image/img-1d8f8b26-4525-43b6-987f-b2db72da266c.jpg",
            "created_at": "2024-05-31 14:53:31"
        },
        {
            "id": "abbd0e8e-8693-43ab-a0ea-34130e2d594a",
            "slug": "testing-blog-6656a460532a6",
            "title": "Testing blog",
            "content": "Lorem ipsum dolor sit amet",
            "image": "https://crud.trisnawan.my.id/contents/blog/image/img-41c19712-58a4-42e2-b3d5-62933f1ceabb.jpg",
            "created_at": "2024-05-29 10:43:28"
        }
    ]
}
```

## Read Article
```
GET /article/read
```
Parameter
```
id = tipe data string (wajib)
```
Contoh response:
```json
{
    "status": true,
    "data": {
        "id": "0980cb7f-8a41-4f7e-b318-ef3c67428079",
        "slug": "lorem-ipsum-has-been-the-industry-s-standard-dummy-text-ever-since-the-1500s-6656a4a31cd8c",
        "title": "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        "content": "Lorem Ipsum comes from section...",
        "image": "https://crud.trisnawan.my.id/contents/blog/image/img-068252ff-3275-428e-b273-289054a01657.jpg",
        "created_at": "2024-05-29 10:44:35"
    }
}
```

## Insert Article
```
POST /article/create
```
Form Data Body
```
title = string (wajib)
content = string (wajib)
image = file multipart (wajib)
```
