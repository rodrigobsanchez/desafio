# desafio
Teste para vaga Desenvolvedor Java Back-end

Aplicação Movie Rental:

Sistema simula uma locadora de filmes. Possuindo cadastro de novos usuários que ao efetuar login tem acesso ao inventário de filmes, podendo alugar, devolver entre outros serviços. É uma aplicação de back-end contendo o mínimo possível de front-end. Programada em ambiente Linux e IntellyJ Community.

Tecnologias e Ferramentas utilizadas:
-Linux - Ubuntu 18.04
-JDK 1.8 - versão java 1.8.
-MySQL 
-Spring Boot (incluindo Spring Security) 2.2.5.RELEASE
-Maven
-IntellyJ Community 2020.
-Thymeleaf – spring-boot-starter-thymeleaf ; thymeleaf-extras-springsecurity5

Como executar a aplicação localmente:

1 - Clone git repository: https://github.com/rodrigobsanchez/desafio.git OU faça download do .Zip

2 – Banco de Dados → Dentro desse repositório tem um arquivo (scriptbancodedados.sql), nesse .sql é criado o banco de dados movierentaldb e acesso total a esse banco ao usuário criado por esse script.
    Segue:
    CREATE USER 'teste'@'localhost' IDENTIFIED BY 'teste';
    GRANT ALL ON movierentaldb.* TO teste@localhost;
	
	São criadas 3 tabelas: users, movies e uma tabela relacional user_movies. 
	São inseridos por esse script 10 filmes contendo a quantidade total e quantidade disponível de cada filme. Tambem é inserido um usuário, esse será o usuário necessário para acessar a primeira vez a aplicação. 
	
3 – Abrir com IntellyJ → Reaplicar as dependências Maven. Procurar a classe “MovieRentalApplication” e ir em Run MovieRentalApp...main().
	Abrir com Eclipse → Ir em File → Import → Existing Maven Project → Ir no diretório que foi clonado o repositório  →  Após finalizar downloads, botão direito no pom.xml - Run As Maven clean e depois Maven Install →
	Procurar a classe “MovieRentalApplication” e ir em Run as Java Application.

4 - Trocar no arquivo application.properties:
    spring.datasource.username=teste
    spring.datasource.password=teste
    *Esse seria o usuario criado no item 2. Claro que pode ser colocado como preferir.

5 – Aplicação vai executar em http://localhost:8080 ou outra porta que quiseres, não há uma definição para essa na aplicação. Nesse momento será necessário fazer o login devido ao Spring Security.
	 Apenas usuários que estão na tabela users terão acesso. O usuário inserido é 

		Username : teste   
		Password : teste

Ao efetuar o login será direcionado para tela inicial:


URLs:

Após o login: 

- http://localhost:8080/   →  GET   →  HomePage ou Movie Manager. Uma tela apenas demonstrativa com apenas uma interação (Create New User = http://localhost:8080/newUser ) e lista de todos os filmes na locadora. 

- http://localhost:8080/logout  →  GET → redireciona para tela de login fazendo o logoff. 




- http://localhost:8080/newUser  →   GET  →  Tela de criação de novo usuário que gera um form tipo POST (http://localhost:8080/saveUser ) possui 3 parâmetros: name , username, password todos Strings.
	
	Retorna uma String.

- http://localhost:8080/insertUser →  POST → post direto para criação de usuário.
	param: name=<text>
	param: username=<text> com regex para e-mail.
	param: password=<text>
	Exemplo: 
    http://localhost:8080/insertUser?name=teste&username=teste@teste.com&password=teste 
	
	Retorna uma String.

- http://localhost:8080/deleteUser →  DELETE  → É possível apenas deletar o usuário que está logado.
	Exemplo:
		 http://localhost:8080/deleteUser

- http://localhost:8080/allMovies  →  GET  → getter de todos os filmes. 
	
	Retorna uma lista de objetos em JSON.


- http://localhost:8080/allAvailableMovies  → GET  → getter de todos os filmes disponível para alugar

	Retorna uma lista de objetos em JSON.

- http://localhost:8080/myMovies →  GET   →  getter dos filmes pertencentes ao usuário logado.
	Retorna uma Lista de Strings com títulos em JSON.
	
	Exemplo:
		http://localhost:8080/myMovies

- http://localhost:8080/searchByTitle  →  GET  → pesquisa por título.
	param: title=<text>
	
	Retorna uma String “não econtrado..” ou o método toString() do objeto movie encontrado.
	
	Exemplo: 
        http://localhost:8080/searchByTitle?title=Seven
	
- http://localhost:8080/addMovie  →  POST  → Adiociona novo filme ou acumula com filme já existente.
	param: title=<text>
	param: director=<text>
	
	Retorna JSON do objecto Movie recem criado.

    Exemplo: 
    http://localhost:8080/addMovie?title=Seven&director=nao%20me%20lembro

- http://localhost:8080/rentMovie  →  PUT and POST →  Aluga um filme para o usuário logado. PUT request atualiza as quantidades, na tabela movies, do filme escolhido. Enqaunto o Post insere a relação usuário-filme na tabela relacional.
	Nesse método é feito verificação de disponibilidade junto a coluna “movie_amount_available”.
	
	param: moviename=<text> 

	Retorna uma String com o sucesso, falta de disponibilidade.

	Exemplo:
           http://localhost:8080/rentMovie?moviename=Avatar

- http://localhost:8080/giveMovieBack  →  PUT and DELETE  → Devolve o filme para a locadora.

Atualiza as quantidades referentes ao filme na tabela movies. Deleta o registro da tabela relacional. O método somente faz as alteraçoes se a quantidade disponível for menor que o total de filmes.
	param: moviename=<text> 
	Retorna uma String com o sucesso, falta de disponibilidade.
	


CAMADA DE TESTE: Foi utilizado Mockito + JUnit5 apenas na camada de Service. Classes: MovieServiceTest, UserMovieServiceTest, UserServiceTest.	

Notes:
 - Para um further developing seria necessário revisões de boas práticas, se o spring está sendo utilizado de maneira adequada.
 - A aplicação foi iniciada e utilizada com sucesso em outros sistemas operacionais.
 Agradeço a oportunidade.
 Att
 Rodrigo Sanches.

