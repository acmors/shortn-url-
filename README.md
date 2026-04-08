
# shortn 🔗

Encurtador de URLs construído utilizando Spring Boot, com API REST, dashboard Thymeleaf, cache com Redis e rate limiting.

## Funcionalidades
- **Encurtamento de URLs** — gera códigos curtos únicos de 6 caracteres para qualquer URL válida
- **Redirecionamento** — resolve o código curto e redireciona para a URL original (HTTP 302)
- **Contagem de cliques** — cliques são incrementados no Redis e sincronizados no banco a cada  30 segundos via @Scheduled
- **Cache com Redis** — URLs resolvidas ficam em cache via @Cacheable para reduzir consultas ao banco
- **Rate limiting** — limita a criação de links globalmente via Resilience4j (3 requisições/min), retornando HTTP 429
- **Dashboard** — interface Thymeleaf para criar links e visualizar o resultado da URL encurtada
- **Tratamento global de exceções** — respostas de erro padronizadas via @RestControllerAdvice
 ---
## Tecnologias
| Categoria | Tecnologia|
| :--- | :--- | 
| Linguagem | Java 21 |
| Framework|  Spring Boot 3.5 | 
| Banco de Dados | PostgreSQL 15 | 
| Cache | Redis |
| Front-End | Thymeleaf |
| Rate Limit |  Resilience4j |
| Build |  Maven |
| Container|  Docker + Docker Compose |

---

## Estrutura do Projeto
 ```
 src/main/java/dev/shortn/
├── config/         # RedisConfig, UrlScheduler (sync de cliques)
├── domain/         # Entidade Url
├── exceptions/     # Exceções customizadas (InvalidUrl, RateLimitExceeded, UrlExpired, UrlNotFound)
├── repository/     # UrlRepository (Spring Data JPA)
├── service/        # UrlService, UrlClickService
└── web/
    ├── controller/ # UrlController (API REST), DashboardController (Thymeleaf)
    ├── dto/        # UrlRequestDto, UrlResponseDto, UrlRedirectDto
    ├── exception/  # ApiExceptionHandler, ErrorMessage
    └── mapper/     # UrlMapper
 ```

 ## Como Utilizar

 ### Pré-requisitos

- Docker e Docker Compose instalados.

### Rodando com Docker Compose
```
# Clone o repositório
git clone https://github.com/seu-usuario/shortn.git
cd shortn

# Sobe todos os serviços (app + postgres + redis)
docker compose up --build
```
A aplicação ficará disponível no http://localhost:8080.

### Rodando localmente (sem Docker)
Com Java 21, PostgreSQL e Redis rodando localmente:
```
./mvnw spring-boot:run
```

## Endpoints da API
| Categoria | Tecnologia| O que faz |
| :--- | :--- | :---|
| POST | /api | Cria a URL encurtada|
| GET| /api/{shortCode}| Redireciona a URL encurtada para a página original |
| GET| /api/information/{shortCode} | Retorna os dados da URL encurtada.  |

## Contagem de Clicks
A cada redirecionamento, o clique é incrementado diretamente no Redis (INCR click:<shortCode>), sem tocar no banco. A cada 30 segundos, o UrlScheduler sincroniza os contadores do Redis para o PostgreSQL e limpa as chaves — mantendo o banco atualizado sem impacto na performance dos redirecionamentos.

## Rate Limiting
- Criação de URLs: 3 requisições por minuto globalmente (Resilience4j)
- Ao ultrapassar o limite, a API retorna HTTP 429 Too Many Requests
- No dashboard, o usuário é redirecionado para uma página de erro amigável

##Imagens do Projeto funcionando

### Dashboard

<img width="1919" height="949" alt="image" src="https://github.com/user-attachments/assets/66c4f3c0-2383-4406-9163-07e9baa773f2" />

### Lista dados da URL

<img width="1918" height="953" alt="image" src="https://github.com/user-attachments/assets/d129a4b1-35eb-4094-8b74-c019fad78083" />
<img width="1919" height="950" alt="image" src="https://github.com/user-attachments/assets/100e7b0b-1749-43f9-85cb-066fce3c52ed" />

### Rate Limit Exception

<img width="1919" height="946" alt="image" src="https://github.com/user-attachments/assets/3350453a-3ff7-4f1e-83aa-6acd3e3f4453" />






## Contribuições

Contribuições são muito bem-vindas! Se você tiver sugestões, encontrou um bug ou deseja implementar um novo recurso, sinta-se à vontade para:

- 1 - Abrir uma **Issue** para discutir a mudança.
- 2 - Criar um **Pull Request** com sua implementação.

