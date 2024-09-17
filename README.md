
# FastCard API

FastCard предоставляет функциональность для управления бизнес-картами, пользователями и заявками. Этот документ описывает доступные API-эндпоинты и их использование.

## Базовый URL

```
https://fastcard.kz/api/v1
```

## Контроллеры

### User Controller

#### Authenticate

- **URL:** `/users/authenticate`
- **Метод:** `POST`
- **Описание:** Аутентификация пользователя и создание JWT токена.
- **Тело запроса:**
  ```json
  {
    "username": "user",
    "password": "password"
  }
  ```
- **Ответ:**
    - 200 OK — успешная аутентификация.
    - 400 Bad Request — ошибка запроса.

#### Register

- **URL:** `/users/register`
- **Метод:** `POST`
- **Описание:** Регистрация нового пользователя.
- **Тело запроса:**
  ```json
  {
    "username": "user",
    "password": "password",
    "email": "user@example.com"
  }
  ```
- **Ответ:**
    - 200 OK — успешная регистрация.
    - 400 Bad Request — ошибка запроса.

#### Activate

- **URL:** `/users/activate/{link}`
- **Метод:** `GET`
- **Описание:** Активация пользователя по ссылке.
- **Параметры пути:** `link` — ссылка для активации.
- **Ответ:**
    - 200 OK — успешная активация.
    - 400 Bad Request — ошибка запроса.

#### Get User

- **URL:** `/users/{username}`
- **Метод:** `GET`
- **Описание:** Получение информации о пользователе по имени пользователя.
- **Параметры пути:** `username` — имя пользователя.
- **Ответ:**
    - 200 OK — информация о пользователе.
    - 400 Bad Request — ошибка запроса.

#### Get All Users

- **URL:** `/users/all`
- **Метод:** `GET`
- **Описание:** Получение всех пользователей.
- **Ответ:**
    - 200 OK — список пользователей.
    - 400 Bad Request — ошибка запроса.

#### Update Password Request

- **URL:** `/users/update-password-request`
- **Метод:** `GET`
- **Описание:** Запрос на обновление пароля для текущего пользователя.
- **Ответ:**
    - 200 OK — токен для сброса пароля выслан.
    - 400 Bad Request — ошибка запроса.

#### Update Password

- **URL:** `/users/update-password`
- **Метод:** `PUT`
- **Описание:** Обновление пароля пользователя.
- **Параметры запроса:**
    - `token` — токен для сброса пароля.
    - `newPassword` — новый пароль.
- **Ответ:**
    - 200 OK — пароль обновлен.
    - 400 Bad Request — ошибка запроса.

#### Delete User

- **URL:** `/users/`
- **Метод:** `DELETE`
- **Описание:** Удаление текущего пользователя.
- **Ответ:**
    - 200 OK — пользователь удален.
    - 400 Bad Request — ошибка запроса.

### Business Card Controller

#### Get Business Card

- **URL:** `/business-cards/{username}`
- **Метод:** `GET`
- **Описание:** Получение бизнес-карты пользователя по имени пользователя.
- **Параметры пути:** `username` — имя пользователя.
- **Ответ:**
    - 200 OK — информация о бизнес-карте.
    - 400 Bad Request — ошибка запроса.

#### Update Business Card

- **URL:** `/business-cards/`
- **Метод:** `PUT`
- **Описание:** Обновление бизнес-карты текущего пользователя.
- **Тело запроса:**
  ```json
  {
    "html": "<html>...</html>",
    "css": "body { ... }"
  }
  ```
- **Ответ:**
    - 200 OK — бизнес-карта обновлена.
    - 400 Bad Request — ошибка запроса.

### Application Controller

#### Create Application

- **URL:** `/applications/`
- **Метод:** `POST`
- **Описание:** Создание новой заявки.
- **Тело запроса:**
  ```json
  {
    "applicationData": "..."
  }
  ```
- **Ответ:**
    - 200 OK — заявка создана.
    - 400 Bad Request — ошибка запроса.

### Admin Controller

#### Activate Business Card

- **URL:** `/admin/activate-business-card`
- **Метод:** `POST`
- **Описание:** Активация бизнес-карты пользователя.
- **Параметры запроса:**
    - `username` — имя пользователя.
- **Ответ:**
    - 200 OK — бизнес-карта активирована.
    - 400 Bad Request — ошибка запроса.

#### Delete Business Card

- **URL:** `/admin/delete-business-card`
- **Метод:** `DELETE`
- **Описание:** Удаление бизнес-карты пользователя.
- **Параметры запроса:**
    - `username` — имя пользователя.
- **Ответ:**
    - 200 OK — бизнес-карта удалена.
    - 400 Bad Request — ошибка запроса.

## Общие ответы

Для всех эндпоинтов с ошибкой будет возвращаться объект ответа с кодом 400 и текстом "bad request":

```json
{
  "message": "bad request",
  "statusCode": 400
}
```

## Примечания

- Все запросы, требующие аутентификации, должны содержать JWT токен в заголовке `Authorization` в формате `Bearer <token>`.
- Обратите внимание, что некоторые эндпоинты могут требовать наличия активной сессии пользователя или администратора.

---

Эти инструкции помогут вам начать работу с API FastCard. Если у вас возникнут вопросы или проблемы, пожалуйста, свяжитесь с нашей службой поддержки.
```

Этот `README.md` файл содержит описание доступных эндпоинтов и их функциональности, а также формат запросов и ответов. Если нужно добавить или изменить что-то, дайте знать!