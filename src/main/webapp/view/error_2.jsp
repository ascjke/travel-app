<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>

    <body>
        <h1>Ошибка</h1>
        <p><big>Население может быть только целым числовым значением больше или равно 0. Вы ввели: "${population}"</big></p>
        <input type="button" onclick="history.back();" value="Назад"/>
        <hr>
        <p align="center"><a href ="/travel">Домой</a>
    </body>

</html>