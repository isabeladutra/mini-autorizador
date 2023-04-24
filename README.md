# Mini-Autorizador
## Descrição do Projeto
<p>Este é um desafio proposto pela VR Beneficios, que consiste no desenvolvimento de uma API REST que realiza uma série de verificações e análises. Essas também são conhecidas como "Regras de Autorização". 
A fim de processar transações de Vale Refeição e Vale Alimentação, entre outros.</p>

## Funcionalidades
Esta é uma aplicação Spring Boot com interface totalmente REST que permite:
* a criação de cartões (todo cartão será criado com um saldo inicial de R$500,00)
* a obtenção de saldo do cartão
* a autorização de transações realizadas usando os cartões previamente criados como meio de pagamento

## Regras de autorização
Uma transação pode ser autorizada se:
   * o cartão existir
   * a senha do cartão for a correta
   * o cartão possuir saldo disponível
   
Ao final do processo, o autorizador toma uma decisão, aprovando ou não a transação:
* se aprovada, o valor da transação é debitado do saldo disponível do benefício, e é informado que ocorreu que a  transação foi realizada.
* Caso uma dessas regras não seja atendida, a transação não será autorizada e será informado o que impede a transação.

## Tecnologias Utilizadas
* java na versão 17
* Foi utilizado o docker e o docker compose para rodar um contêiner com a imagem do mongoDB, sendo utilizado como banco de dados.
* Springboot e SpringData
* Swagger

## Como Rodar Localmente
Primeiro é preciso clonar o repositório usando o comando: 
```git clone git@github.com:isabeladutra/mini-autorizador.git```

É preciso ter instalado na máquina o java 17, segue o link da [página de download do java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

É Preciso baixar o docker, segue o link da [página do docker](https://docs.docker.com/desktop/)

Com o docker instalado e configurado, você vai entrar na pasta onde clonou o projeto com o comando:
```cd mini-autorizador```

Vai entrar na pastar docker com o comando:
```cd docker```

E subir o docker compose com o comando:
```docker-compose up -d```

Em seguida, é só importar o projeto em uma IDE e mandar rodar, o projeto fica disponível em : ```http://localhost:8080/```
## Documentação
O projeto dispõe de um swagger que está disponível após subir a aplicação localmente e acessar o endereço no navegador: http://localhost:8080/swagger-ui/index.html#/

![swagger ](https://user-images.githubusercontent.com/39921576/233862747-a2c85bc5-2728-461a-a423-2879c7214f57.jpg)


