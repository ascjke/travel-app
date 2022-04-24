<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>

    <body>
        <h1>Ошибка</h1>
        <p><big>Города с названием "${cityName}" не существует или он уже содержится базе данных</big></p>
        <input type="button" onclick="history.back();" value="Назад"/>
        <hr>
        <p align="center"><a href ="/travel">Домой</a>
    </body>

</html>