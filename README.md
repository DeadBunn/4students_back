# 4students_back
Учебный проект - backend приложение для площадки объявлений

## Запустить локально

Клонируйте проект

```bash
  git clone https://github.com/DeadBunn/4students_back.git
```

Перейдите в папку проекта

```bash
  cd 4students_back
```

Соберите docker-образ

```bash
  docker compose up
```

Дальнейшая работа состоит из следующих команд:


```bash
  docker compose start  - запуск контейнера
  docker compose stop - остановить контейнера
  docker compose logs --following - логи контейнера
```
Swagger - `http://localhost:8080/swagger-ui/index.html`  
Аккаунты:  
1. email: user@mail.ru password: String
2. email: admin@mail.ru password: String  
