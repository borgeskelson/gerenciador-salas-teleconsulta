# Gerenciador de Salas de Espera - Teleconsulta

Projeto Demonstrativo para Gerenciamento de Salas de Espera com Spring Boot, JSF e PrimeFaces.

Este é um sistema de gerenciamento de salas (CRUDs e agendamento) construído com uma stack moderna de Java, combinando o backend do Spring com a componentização de UI do PrimeFaces em uma arquitetura MVC (Model-View-Controller).



## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.6**
- **Jakarta Server Faces (JSF)** (Integrado via JoinFaces)
- **PrimeFaces** (Biblioteca de componentes UI para JSF)
- **Hibernate** (Implementação JPA)
- **SQLite** (Banco de dados embarcado)
- **Maven** (Gerenciador de Dependências)
- **Docker / Docker Compose** (Ambiente de desenvolvimento)



## 📂 Estrutura de Pastas

A arquitetura do projeto segue o padrão Maven, separando a lógica de backend (Spring) da camada de visualização (JSF/XHTML).

```
GERENCIADOR-SALAS-TELECONSULTA/
│
├── src/main/
│   ├── java/br/gov/ba/saude/teleconsulta/
│   │   ├── config/                         # Configurações do Spring
│   │   ├── controller/                     # Backing Beans do JSF
│   │   ├── converter/                      # Conversores JSF para Entidades
│   │   ├── exception/                      # Exceções customizadas
│   │   ├── model/                          # Entidades JPA
│   │   ├── repository/                     # Interfaces Spring Data JPA
│   │   ├── service/                        # Lógica de negócios
│   │   ├── util/                           # Classes utilitárias
│   │   └── TeleconsultaApplication.java    # Classe principal (Spring Boot)
│   │
│   ├── resources/
│   │   ├── static/                         # Recursos estáticos (CSS, JS, Imagens)
│   │   ├── templates/                      # Padrão Spring - pode conter templates de email, etc.
│   │   └── application.properties          # Configurações da aplicação (BD, JSF, Spring)
│   │
│   └── webapp/                             # Raiz das páginas web
│       ├── resources/                      # Recursos específicos do JSF
│       ├── templates/                      # Template mestre do Facelets (template.xhtml)
│       ├── WEB-INF/                        # Configurações web tradicionais
│       ├── disponibilidade.xhtml
│       ├── index.xhtml
│       ├── login.xhtml
│       ├── paciente.xhtml
│       ├── reserva.xhtml
│       ├── sala.xhtml
│       ├── unidadeSaude.xhtml
│       └── usuario.xhtml
│
├── pom.xml                      # Definição do projeto e dependências Maven
├── docker-compose.yml           # Ambiente de desenvolvimento em contêiner Docker
└── README.md                    # Este arquivo
```



## ▶️ Como Executar (Ambiente de Desenvolvimento)

Este projeto está configurado para ser executado facilmente com Docker Compose em um ambiente de desenvolvimento.

1.  Certifique-se de ter o Docker Engine [https://docs.docker.com/engine/install/] e o Docker Compose [https://docs.docker.com/compose/install/] instalados.

2.  Na raiz do projeto (onde está o `docker-compose.yml`), execute o comando:

    ```bash
    docker compose up -d
    ```

3.  A aplicação será compilada e iniciada. Na primeira execução, aguarde alguns minutos (para baixar todas as dependências) e acesse o seguinte endereço em seu navegador: **[http://localhost:8080](http://localhost:8080)**

4.  Graças à montagem de volumes no `docker-compose.yml` e ao `spring-boot-devtools`, qualquer alteração feita no código-fonte fará com que a aplicação seja recarregada automaticamente.

5.  O banco de dados SQLite (`teleconsulta.db`) será criado e persistido na pasta raiz do projeto em sua máquina local.

6.  Para teste da aplicação, foram criados os seguintes usuários:
```
Usuário 01 (Perfil Admin): usuario1@dominio.com / 123456
Usuário 02 (Perfil Restrito): usuario2@dominio.com / 123456
```