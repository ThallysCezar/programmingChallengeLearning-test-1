package br.com.thallysprojects.pitang_desafio.configs;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

//    @Bean
//    public OpenAPI customOpenAPI() {
//        final String securitySchemeName = "bearerAuth";
//        return new OpenAPI().info(
//                new Info()
//                        .title("Game Quiz MJV School Java")
//                        .version("1.0")
//                        .description("O aplicativo de Game Quiz é uma plataforma divertida e interativa onde os usuários podem participar de quizzes" +
//                                     " de perguntas e respostas. Oferece funcionalidades de login com diferentes roles para os usuários, utiliza JWT com " +
//                                     "Token Bearer para autenticação segura e apresenta uma variedade de questões com alternativas. " +
//                                     "Os jogadores podem competir entre si, acompanhar suas pontuações e desfrutar de uma experiência de jogo dinâmica. "
//                                     + "O código-fonte do projeto esta disponivel no GitHub: "
//                                     + "https://github.com/ThallysCezar/game-quiz-springboot")
//        );
//    }

}
