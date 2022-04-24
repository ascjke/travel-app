<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <head>
        <title>Travel service</title>
        <link rel="stylesheet" type="text/css" href="/view/css/style.css"/>
    </head>
    <body>
         <h1>Информация о городе ${city.name}</h1> <br>
         <img src="${imgSrc}">
         <br>
         ${city.description}
         <br>
         <h3>Климат</h3>
         ${city.climate}
         <br><br>
         Население ${population} человек
         <br><br>
         Географические координаты: ${latitude}        ${longitude}
         <br><br>
         <form method="GET" action="edit">
             <input hidden name="cityId"  value="${city.id}">
             <input type="submit" value="Редактировать">
         </form>
         <form method="POST" action="delete">
              <input hidden name="cityId"  value="${city.id}">
              <input type="submit" value="Удалить">
          </form>
         <br><br>
         Города поблизости (${radius} км):
         <br>
         <ul>
             <c:forEach items="${distances}" var="distance">
                <li><a href="city?cityId=${distance.key.id}">${distance.key.name} ${distance.value} км</a></li>
                <br>
             </c:forEach>
         </ul>
        <hr>
        <a href="/travel">Домой</a>
    </body>
</html>