# api

## Формат ключа key
key - строка, состоящая из 8 символов английского алфавита (заглавные + прописные) + цифр
#
Получение прав администратора

**URL** : `/api/v1/users/auth`

**Метод** : `POST`

**Нужна авторизация** : Да

**Передаваемые данные**

```json
{
  "key": "string",
  "login": "string",
  "password": "string"
}
```

**Возвращаемые данные** : Нет

#
Получение статуса пользователя

**URL** : `/api/v1/users/whoami`

**Метод** : `POST`

**Нужна авторизация** : Нет

**Передаваемые данные**

```json
{
  "key": "string"
}
```

**Возвращаемые данные**

```json
{
    "role": "'admin' или 'user'"
}
```

#
Добавление нового пользователя

**URL** : `/api/v1/users/add`

**Метод** : `POST`

**Нужна авторизация** : Нет

**Передаваемые данные**

```json
{
  "key": "string"
}
```

**Возвращаемые данные** : Нет

## Примечания по системе пользователей
Все действия по заданию ражимов и координам могут быть изменены только администраторами



#
Информация о системе

**URL** : `/api/v1/system/info`

**Метод** : `GET`

**Нужна авторизация** : Нет

**Передаваемые данные** : Нет

**Возвращаемые данные**

```json
{
    "version": "string",
    "cores": "number",
    "revision": "number"
}
```

#
Перезагрузка системы

**URL** : `/api/v1/system/restart`

**Метод** : `POST`

**Нужна авторизация** : Да

**Передаваемые данные**

```json
{
  "key": "string"
}
```

**Возвращаемые данные** : Нет

#
Информация об углах и текущем состоянии

**URL** : `/api/v1/data/get/angles`

**Метод** : `GET`

**Нужна авторизация** : Нет

**Передаваемые данные** : Нет

**Возвращаемые данные**

```json
{
    "azimut": "float",
    "elevation": "float",
    "temp": "float",
    "setted_azimut": "float",
    "setted_elevation": "float",
    "is_ready": "bool",
    "delta_enabled": "bool",
    "dorotate_enabled": "bool"
}
```

#
Задание координат приемника и спутника

**URL** : `/api/v1/data/set/satgps` и `/api/v1/data/set/homegps`


**Метод** : `POST`

**Нужна авторизация** : Да

**Передаваемые данные**

```json
{
    "key": "string",
    "lat": "float",
    "lon": "float",
    "height": "float"
}
```

**Возвращаемые данные** : Нет


#
Получение углов для джойстика

**URL** : `/api/v1/data/get/joyangles`

**Метод** : `GET`

**Нужна авторизация** : Нет

**Передаваемые данные** : Нет

**Возвращаемые данные**

```json
{
  "azimut": "float",
  "elevation": "float",
  "deltajoyazimut": "float",
  "deltajoyelevation": "float"
}
```

#
Задание азимута и элевации для поворота

**URL** : `/api/v1/data/set/angles`

**Метод** : `POST`

**Нужна авторизация** : Да

**Передаваемые данные**

```json
{
    "key": "string",
    "azimut": "float",
    "elevation": "float"
}
```

**Возвращаемые данные** : Нет

#
Задание текщих углов поворота (затирает текщее значение)

**URL** : `/api/v1/data/set/currentangles`

**Метод** : `POST`

**Нужна авторизация** : Да

**Передаваемые данные**

```json
{
    "key": "string",
    "azimut": "float",
    "elevation": "float"
}
```

**Возвращаемые данные** : Нет

#
Задание статуса режим доворота

**URL** : `/api/v1/data/set/dorotate`

**Метод** : `POST`

**Нужна авторизация** : Да

**Передаваемые данные**

```json
{
    "key": "string",
    "value": "int(0/1)"
}
```

**Возвращаемые данные** : Нет