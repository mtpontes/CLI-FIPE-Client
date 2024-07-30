# CLI FIPE Client


# 🔎 Sobre

Este projeto é a realização de um desafio dado pelo instrutor durante o curso [Java: trabalhando com lambdas, streams e Spring Framework](https://cursos.alura.com.br/course/java-trabalhando-lambdas-streams-spring-framework) da [Alura](https://www.alura.com.br), onde devemos usar o que foi aprendido para implementar um client CLI que consome e abstrai a [FIPE API HTTP REST](https://deividfortuna.github.io/fipe/). Esta é a minha versão do desafio, com as minhas ideias e implementações.

## 🖥️ Interface 

## Funções

A aplicação segue o seguinte fluxo ordenadamente em loop:

- Escolher o tipo de veículo
- Escolher a fabricante
- Escolher modelo de veículo
- Recuperar as 5 versões mais recentes, ou as 5 mais antigas, ou recuperar todas a partir de um ano (mínimo 1950)

<br>

![gif](/readme/video.gif)

## 🚀 Como Rodar
<details><summary>Clique para expandir</summary>

### Pré-requisitos

- Java 17

### Instalando

- Clone o projeto com o comando (ou baixe o zip pelo Github):

      git clone https://github.com/mtpontes/CLI-FIPE-Client.git

- Entre no diretório principal do projeto e execute: 
    * Para Linux: 
    
          ./mvnw clean install -DskipTests


    * Para Windows: 
          
          mvnw.cmd clean install -DskipTests


    * Caso já possua Maven instalado: 
    
          mvn clean install -DskipTests

### Deploy

O app empacotado pode ser encontrado no diretório `/target` após seguir o procedimento de instalação.

Para executar a aplicação use o comando: 
        
    java -jar nome_do_jar

</details>

## 🤝 Créditos

- [Alura](https://www.alura.com.br)

- [FIPE API HTTP REST](https://deividfortuna.github.io/fipe/)