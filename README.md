# java-filmorate
Template repository for Filmorate project.

# ErDiagram-FilmRate

![Screenshot of a comment on a GitHub issue showing an image, added in the Markdown, of an Octocat smiling and raising a tentacle.](FilmRate_ErDiagrama.png)

# Пример запроса 
Получить информацию о фильме включая жанр,  рейтинг
>SELECT f.ID film_id,f.NAME,f.DESCRIPTION,f.RELEASEDATE,f.DURATION, <br> 
&nbsp;&nbsp;&nbsp;&nbsp;       m.id mpa_id,m.NAME mpa_name,g.ID genre_id,g.NAME genre_name <br> 
FROM FILMORATE.FILM f  <br> 
&nbsp;&nbsp;&nbsp;&nbsp;    LEFT JOIN FILMORATE.МРА m ON f.MPA_ID=m.id  <br> 
&nbsp;&nbsp;&nbsp;&nbsp;    LEFT JOIN FILMORATE.FILMGENRE fg ON fg.filmid=f.id <br> 
&nbsp;&nbsp;&nbsp;&nbsp;   LEFT JOIN FILMORATE.GENRE g ON fg.genreid=g.id

топ популярных фильмов
>SELECT f.ID film_id,f.NAME,f.DESCRIPTION,f.RELEASEDATE,f.DURATION,m.id mpa_id,  <br> 
&nbsp;&nbsp;&nbsp;&nbsp;  m.NAME mpa_name,g.ID genre_id,g.NAME genre_name,l.USERID like_user_id,  <br> 
&nbsp;&nbsp;&nbsp;&nbsp; (SELECT count(*) FROM FILMORATE.LIKES WHERE filmid=f.id) cnt <br> 
                                                FROM FILMORATE.FILM f  <br> 
&nbsp;&nbsp;&nbsp;&nbsp;                                                LEFT JOIN FILMORATE.МРА m ON f.MPA_ID=m.id <br> 
&nbsp;&nbsp;&nbsp;&nbsp;                                                LEFT JOIN FILMORATE.LIKES l ON f.id=l.filmid  <br> 
&nbsp;&nbsp;&nbsp;&nbsp;                                                LEFT JOIN FILMORATE.FILMGENRE fg ON fg.filmid=f.id  <br> 
&nbsp;&nbsp;&nbsp;&nbsp;                                                LEFT JOIN FILMORATE.GENRE g ON fg.genreid=g.id <br> 
                        ORDER BY cnt desc, f.NAME  <br> 
                        LIMIT ?  
