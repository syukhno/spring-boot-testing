package wf.garnier.springboottesting.todos.simple;

import java.io.IOException;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ContainersConfiguration.class)
@ExtendWith(OutputCaptureExtension.class)
class TodoApplicationWithTomcatTests {

	@LocalServerPort
	Long port;

	WebClient webClient = new WebClient();

	private String baseUrl;

	@BeforeEach
	void setUp() {
		baseUrl = "http://localhost:%s/".formatted(port);
	}

	@Test
	void logsIp(CapturedOutput output) throws IOException {
		webClient.getPage(baseUrl);
		assertThat(output.getOut()).contains("user with IP [127.0.0.1] requested [/]. We responded with [200]");
	}

	@Test
	void displaysPage() throws IOException {
		HtmlPage page = webClient.getPage(baseUrl);

		HtmlInput input = page.querySelector("form > input");
		HtmlButton button = (HtmlButton) page.getElementById("add-button");

		input.type("this is a todo");
		page = button.click();

		var addedToto = page.querySelector(".todo > [data-role=\"text\"]").getTextContent();
		assertThat(addedToto).isEqualTo("this is a todo");
	}

}
