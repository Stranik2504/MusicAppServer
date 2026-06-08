# MusicAppServer

Серверная часть приложения для прослушивания музыки, написанная на Kotlin с использованием Ktor.

## Стек технологий

*   **Язык:** [Kotlin](https://kotlinlang.org/) (JVM 21)
*   **Framework:** [Ktor](https://ktor.io/)
*   **ORM:** [Exposed](https://github.com/JetBrains/Exposed)
*   **База данных:** PostgreSQL
*   **Аутентификация:** JWT (JSON Web Tokens)
*   **Безопасность:** BCrypt для хеширования паролей
*   **Контейнеризация:** Docker & Docker Compose

## Структура проекта

*   `src/main/kotlin/controller` — Обработка входящих запросов
*   `src/main/kotlin/domain` — Бизнес-логика и модели данных
*   `src/main/kotlin/data` — Репозитории и доступ к данным
*   `src/main/kotlin/security` — Настройки безопасности и JWT
*   `src/main/kotlin/plugins` — Конфигурация плагинов Ktor (Serialization, Routing, HTTP и т.д.)
*   `src/main/kotlin/Routing` — Определение маршрутов API

## Запуск проекта

### С помощью Docker (рекомендуется)

Для запуска сервера вместе с базой данных PostgreSQL выполните команду:

```bash
docker-compose up -d
```

Сервер будет доступен по адресу: `http://localhost:9911` (внешний порт, проброшенный из 8080).

### Локальный запуск (через Gradle)

1.  Убедитесь, что у вас установлен JDK 21.
2.  Настройте базу данных PostgreSQL и укажите параметры подключения (по умолчанию сервер ожидает настройки из `application.yaml` или переменных окружения).
3.  Запустите проект:

```bash
./gradlew run
```

## Переменные окружения

Основные настройки (могут быть переопределены в `docker-compose.yml`):

*   `DB_JDBC_URL`: URL подключения к базе данных.
*   `DB_USERNAME` / `DB_PASSWORD`: учетные данные БД.
*   `MEDIA_ROOT`: Путь к хранилищу музыкальных файлов (в контейнере по умолчанию `/storage/music`).

## Хранилище

Проект использует локальные папки для хранения данных (монтируются через volumes в Docker):
*   `./storage/music` — музыкальная библиотека
*   `./storage/uploads` — загруженные файлы
*   `./storage/tmp` — временные файлы
