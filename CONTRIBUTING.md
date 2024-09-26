# Como Contribuir - Seu Passaporte de Entrada

Estamos felizes em receber você aqui e saber que está interessado em contribuir para o nosso projeto. Como um projeto de código aberto, cada contribuição é valorizada e ajuda a impulsionar o crescimento e a qualidade do nosso trabalho. Este guia foi criado para orientá-lo sobre como você pode participar e fazer parte da nossa comunidade de desenvolvimento. Estamos ansiosos para ver suas contribuições e trabalhar juntos para tornar nosso projeto ainda melhor!

## Código de Conduta

Para garantir um ambiente respeitável e inclusivo, leia e siga nosso [Código de Conduta](./CODE_OF_CONDUCT.md).

## Começando a Contribuir

Contribuir para o nosso projeto é fácil e estamos ansiosos para receber suas contribuições! Antes de entrarmos nos passos para instalação da aplicação, você precisará configurar algumas ferramentas e preparar seu ambiente de desenvolvimento.

Aqui está o que você precisa:

- Uma conta no [GitHub](https://github.com/)
- O *version control system* [Git](https://git-scm.com/) instalado.
- Um IDE para o desenvolvimento. Recomendamos o [IntelliJ IDEA](https://www.jetbrains.com/idea/).
- O compilador da linguagem [Java](https://www.oracle.com/java/technologies/javase-downloads.html) instalado.
- O build automation tool [Apache Maven](https://maven.apache.org/) instalado para gerenciar as dependências do projeto.
- O banco de dados [MySQL](https://dev.mysql.com/downloads/mysql/) configurado, pois o projeto utiliza o MySQL para armazenamento de dados.

### 1. Clonar o Repositório

O primeiro passo é clonar o repositório do projeto para o seu ambiente local.

1. Abra um terminal.
2. Execute o seguinte comando para clonar o repositório:
   ```bash
   git clone https://github.com/Fatec-Bug-Busters/alplaca.git
   ```
3. Navegue até o diretório do projeto:
   ```bash
   cd alpaca
   ```

### 2. Baixar as Dependências Maven

Certifique-se de que todas as dependências Maven estão baixadas para o projeto funcionar corretamente.

1. No seu IDE (IntelliJ IDEA):
  - Abra o projeto clonado.
  - O Maven deve reconhecer automaticamente o arquivo `pom.xml`. Caso não reconheça, clique com o botão direito no arquivo `pom.xml` e selecione "Add as Maven Project".
  - Isso fará com que o Maven baixe todas as dependências necessárias para o projeto.

2. Você também pode rodar o seguinte comando no terminal para garantir que todas as dependências estejam baixadas:
   ```bash
   mvn clean install
   ```

### 3. Definir a Pasta `resources`

Antes de executar o projeto, certifique-se de que a pasta `resources` está corretamente configurada como uma pasta de recursos na estrutura do seu projeto. Isso é essencial para o carregamento de arquivos de configuração, como o `hibernate.cfg.xml`.

Passos para definir a pasta `resources` no IntelliJ IDEA:

1. No menu principal, vá para **File** -> **Project Structure** -> **Modules**.
2. No painel esquerdo, selecione o módulo correspondente ao seu projeto.
3. Na aba **Sources**, localize a pasta `resources` dentro da estrutura do projeto.
4. Clique com o botão direito na pasta `resources` e selecione **Mark as Resources**.
5. Após isso, clique em **Apply** e, em seguida, **OK**.

### 4. Criar o Banco de Dados no MySQL

Você precisará configurar um banco de dados MySQL para este projeto. Siga os passos abaixo:

1. Crie um novo banco de dados na sua instância do MySQL:
   ```sql
   CREATE DATABASE nome_do_seu_banco_de_dados;
   ```
2. Anote o nome do banco de dados, usuário e senha para o próximo passo.

### 5. Atualizar o Arquivo `hibernate.cfg.xml`

O arquivo `hibernate.cfg.xml` localizado na pasta `resources` precisa ser atualizado com as configurações corretas de conexão com o banco de dados.

1. Abra o arquivo `hibernate.cfg.xml` na pasta `resources`.
2. Substitua os valores placeholders de conexão com o banco de dados (`url`, `username`, `password`) pelas suas credenciais do MySQL.

Exemplo:
```xml
<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/database_name</property>
<property name="hibernate.connection.username">your_username</property>
<property name="hibernate.connection.password">your_password</property>
```

Certifique-se de que essas alterações estão em vigor antes de executar a aplicação.

