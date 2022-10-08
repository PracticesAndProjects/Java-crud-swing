# Package configurations
Contêm classe com as variáveis de configuração do jdbc (senha, url, etc). *As configurações desse projeto são fictícias e não são usadas em nenhum sistema em produção*

# Package views
Contém classes de configuração dos componentes
e comportamento dos botões.

# Package services
Contém classe que executa regra de negócio e
comunica com a camada repository

# Package repository
Contém a classe que vai fazer a comunicação
com o banco de dados

# Package models
Contém as classes de representação das entidades

# Package enums
Contém um fake enum (classe com propriedades static)
utilizado no modal de confirmação

# Rodando o projeto
Para rodar o projeto, é necessário possuir uma IDE com o repositório Maven previamente indexado.
<br>
Ou, caso tenha um Maven instalado externamente, fazer a instalação das dependências
abrindo a pasta raiz do projeto em um terminal e executando o seguinte comando
> mvn install

Para instalar as dependência com o Maven bundled diretamente na IDE, é necessário utilizar as configurações da mesma para instalar as dependências necessárias configuradas no pom.xml

Com as dependências instaladas, basta rodar o método main da classe 
> StartApp.class 

IDE utilizada: IDEA Intellij Community Edition (Swing UI Designer -> GUI Form)
>     Para modificar os componentes visualmente
>     é necessário utilizar a IDE IDEA Intellij
>     Os .forms não são compatíveis com o netbeans