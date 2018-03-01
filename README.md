# IT-Berries-02-2018
Java course 02.2018. Project: IT-Berries

[![Build Status](https://travis-ci.org/ElenaOshkina/IT-Berries-02-2018.svg?branch=master)](https://travis-ci.org/ElenaOshkina/IT-Berries-02-2018)

MagicalDots - логическая настольная игра для двух человек, сочетающая позиционное стратегическое планирование с тактическим перебором вариантов. Ведётся на плоском игровом поле, расчерченном на клетки одинакового размера. Игроки поочерёдно ставят точки двух цветов в перекрестия линий. Цель — окружить точки соперника замыканием вокруг них непрерывной цепи своих точек.

Команда: Немшилов Иван, Пучнина Анастасия, Ошкина Елена

API:




# Sign Up 
POST
/api/signUp/
{ "login": "user4","email":"user@mail.ru", "password": "user4" }

# Update User
PUT
/api/users/{id}

# Get current auth user
GET
/api/currentUser/

# Sign In
POST
/api/login/
{"login" : "user4", "password" : "user4"}

# Sign Out
POST
/api/logOut/
