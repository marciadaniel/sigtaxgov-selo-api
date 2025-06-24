package marcia.daniel.sigtaxgov_selo_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "API de Validação de Selo Fiscal",
				version = "1.0"

		)
)
public class SigtaxgovSeloApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SigtaxgovSeloApiApplication.class, args);
	}

}
