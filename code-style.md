Fail fast: primeiro testa se deu erro, caso sim retorna esse erro, se não retorna o sucesso.
Responsabilidade única: funções ou classes que mudem apenas por um motivo.
Se tiver ficando com muitos ifs no código, podemos separar em funções privadas no service.
Uma função não terá mais de 3 argumentos recebidos, caso encontre alguma situação muito específica, perguntar antes.
Controllers apenas retornam o status HTTP e o que está sendo pedido, não existe lógica neles.
Services chamam repositories, validações serão realizadas no arquivo de validador separado.
Repository terá apenas a lógica de guardar os dados na memória.
Injeção de dependência via construtor, nunca @Autowired em campo.
Preferir final em campos e usar record para DTOs imutáveis quando possível.
Nunca retornar null: usar Optional para valores opcionais e coleção vazia para listas.
Nomes descritivos, sem abreviações (métodos como verbos, classes como substantivos).
Nada de números ou strings mágicas soltas no código, usar enum ou constantes nomeadas.
Exceções de negócio dedicadas, tratadas em um @ControllerAdvice único.
Logging via SLF4J, nunca System.out.println.
Testes seguem Given-When-Then, com um foco de asserção por teste.
Evitar comentário óbvio, comentar só decisão não trivial.
