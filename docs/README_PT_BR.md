# FridaGuard-Mobile

**FridaGuard-Mobile** é uma solução inovadora que protege aplicações contra uso não autorizado de scripts Frida, garantindo que apenas usuários autenticados possam realizar análises e pentests em apps sensíveis. A solução envolve dois componentes principais: um app Android de autenticação e o app alvo que contém o script Frida.
**Aviso de Isenção de Responsabilidade:** Não me responsabilizo por quaisquer danos a aplicações de terceiros. Este projeto é de natureza voluntária e deve ser utilizado com respeito pelas aplicações desenvolvidas por outros profissionais.

## Componentes Principais:

### App Android de Autenticação:

- **Tela de Login Segura:** Os usuários precisam realizar o login no app Android para obter um token de autenticação gerado pelo servidor.
- **Identificadores Únicos:** No primeiro acesso, o app Android gera identificadores customizados para o dispositivo e build, que são armazenados localmente e enviados ao servidor para validação.
- **Proteção de Acesso:** O token gerado durante o login expira rapidamente, forçando o usuário a realizar a autenticação e usar o script Frida em uma janela de tempo limitada.

### App Alvo com Script Frida:

- **Execução Condicionada:** O script Frida integrado ao app alvo só pode ser executado após receber um token válido do app Android de autenticação.
- **Conexão Segura:** O app alvo estabelece uma conexão TCP com o script Frida e, em seguida, recebe o token JWT via TCP, garantindo que a execução só ocorra se o token for válido.
- **Identificação de Frida:** O script Frida envia o token ao servidor em todas as requisições, juntamente com um cabeçalho especial que identifica o uso do script, permitindo que o servidor controle e monitore essas operações.
- **Encerramento Automático:** Se o token expirar ou for invalidado, o script Frida é automaticamente encerrado, impedindo o uso não autorizado.

## Como Funciona:

1. **Login no App Android:** O usuário realiza o login no app Android, que gera um token JWT baseado nos identificadores únicos do dispositivo.
2. **Conexão com o App Alvo:** Após o login, o usuário abre o app alvo que contém o script Frida. O app alvo se conecta ao script Frida e transmite o token JWT recebido.
3. **Validação e Execução:** O script Frida utiliza o token para autenticar-se com o servidor. Se o token for válido e o dispositivo estiver autorizado, a execução do script é permitida.
4. **Monitoramento e Controle:** Durante a execução, o servidor monitora as requisições do script Frida, garantindo que apenas operações autorizadas sejam realizadas.
5. **Encerramento em Caso de Falha:** Se o login não for realizado a tempo ou se o token for inválido, o script Frida é encerrado, impedindo o uso indevido.

## Configuração e Uso:

### Servidor Node.js

- **Dependências:** O servidor Node.js requer `Express.js` e `MongoDB`. Você pode encontrar o código do servidor [aqui](https://github.com/Vitor-VX/fridaguard-js) (substitua pelo link do seu repositório real).
- **Instruções de Configuração:**
    1. Clone o repositório.
    2. Instale as dependências usando `npm install`.
    3. Configure o MongoDB e a conexão com o banco de dados no código do servidor.
    4. Inicie o servidor com `npm start`.

### Script Frida
- **Script de configuração.js:** O script frida está na pasta /assets deste projeto, ele conterá a lógica a ser colocada no aplicativo de destino, sem ele o projeto não tem sentido.

### App Android

- **Arquivo de Configuração:** Você precisa configurar o app Android com os detalhes do servidor e o token público no arquivo `config.json`.
- **AndroidManifest:** Além do `config.json`, você também precisa adicionar o package do app no <queries></queries> para a função  ``openApp()`` funcionar.

  ```json
  {
    "splashTextSequence": true,
    "loginApp": [
      {
        "title": "Login FridaGuard",
        "titleInfo": "Bem-vindo ao FridaGuard!"
      }
    ],
    "timeExperienceForUsers": 2000,
    "configs": [
      {
        "public-token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoicHVibGljIiwicGVybWlzc2lvbnMiOlsicmVhZCJdLCJpYXQiOjE3MjQ2NDMxOTV9.BRV8NuejKLC0vlOEdpPxpzX-aT4Q8AjYIZYdlwjdK24"
      },
      {
        "package-app": "PACKAGE_APP"
      },
      {
        "url-server": "https://fridaguard-js.onrender.com"
      }
    ]
  }

## Casos de Uso:

- **Proteção de Aplicações Sensíveis:** Ideal para empresas que desejam proteger suas aplicações de análises não autorizadas, assegurando que apenas pentesters autorizados possam realizar testes de segurança.
- **Controle de Acesso a Ferramentas de Análise:** Facilita a gestão e controle de quem pode utilizar scripts Frida, evitando que sejam usados de forma indevida.
- **Segurança Avançada para Desenvolvedores:** Permite que desenvolvedores integrem uma camada extra de segurança em suas aplicações, protegendo contra uso não autorizado de ferramentas como o Frida.

---

## Diagrama de Funcionamento:

```text
+-------------------+    +------------------+    +--------------------+
|   App Android     |    |   Servidor Node  |    |  Script Frida       |
|                   |    |   (Express.js)   |    |  (App X)            |
+--------+----------+    +--------+---------+    +--------+-----------+
         |                        |                        |
  1. Token Público                |                        |
  GET /get-app-id                 |                        |
+---------------------->          |                        |
         |                        |                        |
  2. Gera customDeviceId e customBuildId                    |
         |                        |                        |
  3. Retorna IDs                  |                        |
<----------------------           |                        |
         |                        |                        |
  4. Armazena IDs em              |                        |
     SharedPreferences            |                        |
         |                        |                        |
  5. Login Request                |                        |
  POST /login-user                |                        |
  com customDeviceId,             |                        |
  customBuildId,                  |                        |
  deviceId, buildId               |                        |
+---------------------->          |                        |
         |                        |                        |
  6. Valida IDs e Credenciais     |                        |
         |                        |                        |
  7. Gera JWT com expiração de 10s|                        |
         |                        |                        |
  8. Retorna JWT                  |                        |
<----------------------           |                        |
         |                        |                        |
  9. Exibe Popup                  |                        |
         |                        |                        |
 10. Confirmação do usuário       |                        |
         |                        |                        |
 11. Estabelece conexão TCP com Script Frida e envia JWT   |
+---------------------->          |                        |
         |                        |                        |
                                  | 12. Recebe JWT         |
                                  |     e faz request      |
                                  |     com Bearer Token   |
                                  |     e 'X-Frida-Identifier'|
+----------------------------------------------->           |
                                  |                        |
                                  | 13. Valida JWT e header|
                                  |                        |
                                  | 14. JWT Válido ->      |
                                  |     Executa Script Frida|
                                  |<------------------------|
                                  |                        |
                                  | 15. Se JWT expirar ou   |
                                  |     header inválido,    |
                                  |     Script Frida encerra|
                                  |     App Android         |
+----------------------------------------------->