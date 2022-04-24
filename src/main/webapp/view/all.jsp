<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>

    <head>
        <title>Travel service</title>
        <link rel="stylesheet" type="text/css" href="/view/css/style.css"/>
        <style>
            .customButton{
            background: #008EB0; /* Синий цвет фона */
            color: #000000; /* Белый цвет текста */
            border: none; /* Убираем рамку */
            padding: 0.5rem 0.5rem; /* Поля вокруг текста */
            margin-bottom: 1rem; /* Отступ снизу */
            font-size: 0.9rem;
            font-weight: 400;
            }
           #pic {
            float: left; /* Обтекание картинки текстом */
           }
           #text_left {
            margin-left: 300px; /* Отступ от левого края */
           }
           #text_left {
            margin-bottom: 50px; /* Отступ от нижнего края */
           }
        </style>
    </head>

    <body>
        <h1><p align="center">Travel Service</p></h1>
        <a href="/travel/create">Добавить город</a>
        <p><br></p>
        <c:forEach items="${cityList}" var="city">
            <h2>${city.key.name}</h2>
            <div id="pic">
                <img src="${cityService.getImageReference(city.key.name)}" width="250px" height="187px" alt="${city.key.name}">
            </div>
            <div id="text_left">
                ${city.key.description}
                <br><br>
                Погода: ${city.value} &#176C
                <br><br><br><br>
                <a class="customButton" href="/travel/city?cityId=${city.key.id}" >Подробнее</a>
            </div>
            <div id="text_bottom">
                <hr>
            </div>

        </c:forEach>
    </body>
</html>