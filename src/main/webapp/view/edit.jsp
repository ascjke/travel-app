<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <head>
        <title>Edit city</title>
        <link rel="stylesheet" type="text/css" href="/view/css/style.css"/>
        <style>
           #descInput {
            width: 810px;
            height: 100px;
           }
           #climateInput {
            width: 810px;
            height: 75px;
          }
          </style>
    </head>
    <body>
        <h1>Редактировать город ${city.name}</h1> <br>
        <form method="POST" action="edit">
            <input hidden name="cityId" value="${city.id}">
            Название города <input name="name" value="${city.name}"><br>
            <p>Описание города</p>
            <textarea name="description" id="descInput">${city.description}</textarea><br>
            <p>Климат города</p>
            <textarea name="climate" id="climateInput">${city.climate}</textarea><br><br>
            Население города (кол. человек) <input name="population" value="${city.population}"><br><br>
            Географические координаты <br>
            Широта: град. <input name="latDeg" value="${latDeg}">
                    мин. <input name="latMin" value="${latMin}">
                    сек. <input name="latSec" value="${latSec}"><br>
            Долгота: град. <input name="lonDeg" value="${lonDeg}">
                     мин. <input name="lonMin" value="${lonMin}">
                     сек. <input name="lonSec" value="${lonSec}">
            <p>
            <input type="submit" value="Применить изменения">
        </form>
    </body>
</html>