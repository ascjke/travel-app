<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <head>
        <title>Create new city</title>
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
        <h1>Добавить новый город в Travel service</h1> <br>
        <form method="POST" action="create" enctype="multipart/form-data">
            Название города <input name="name"><br>
            <p>Описание города</p>
            <textarea name="description" id="descInput"></textarea><br>
            <p>Климат города</p>
            <textarea name="climate" id="climateInput"></textarea><br><br>
            Население города (кол. человек) <input name="population_str"><br><br>
            <br><br>
            <input type="submit" value="Добавить">
        </form>
    </body>
</html>